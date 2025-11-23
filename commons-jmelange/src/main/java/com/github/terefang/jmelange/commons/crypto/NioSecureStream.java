package com.github.terefang.jmelange.commons.crypto;

/*
 * NioSecureStream.java
 *
 * Implements a streaming encrypt/decrypt layer for NIO channels that:
 *  - performs an X25519 key agreement
 *  - derives AES-256-CTR and HMAC-SHA256 keys with HKDF
 *  - frames data into chunks and authenticates each frame with HMAC
 *
 * Design notes:
 *  - Each frame layout: [uint32 BE len][16-byte IV][ciphertext len bytes][32-byte HMAC]
 *    where 'len' is the ciphertext length (not including IV or HMAC).
 *  - AES-CTR uses a 16-byte IV (counter). We use a per-frame random IV.
 *  - HKDF-SHA256 derives two keys: 32 bytes for AES-256, 32 bytes for HMAC-SHA256.
 *  - The code uses the platform JCA for X25519, AES/CTR/NoPadding and HmacSHA256.
 *    Java 11+ supports X25519/X448 in standard providers; if your JDK does not,
 *    add BouncyCastle as a provider and/or use its lightweight API.
 *
 * Usage (example):
 *  - Each side generates an X25519 KeyPair and sends the public key to the peer.
 *  - Call performKeyAgreement(localPrivate, remotePublic) to obtain a SecretKeyMaterial
 *    then create a StreamEncryptor on sender and StreamDecryptor on receiver.
 *  - Use writeEncrypted(frame) or readDecrypted() to stream data over NIO channels.
 *
 * NOTE: This is an example implementation, not a fully production-ready library.
 * This code demonstrates primitives wired together for streaming usage using NIO channels.
 */

import com.github.terefang.jmelange.apache.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.security.*;
import java.security.spec.NamedParameterSpec;
import java.security.spec.XECPrivateKeySpec;
import java.security.spec.XECPublicKeySpec;
import java.util.Arrays;

public final class NioSecureStream {
    
    private static final int IV_LEN = 16; // AES-CTR 128-bit IV
    private static final int HMAC_LEN = 32; // HMAC-SHA256 output
    private static final int LENGTH_FIELD = 4; // 4 bytes for ciphertext length
    private static final int MAX_FRAME_PLAINTEXT = 64 * 1024; // 64KB per frame (tunable)
    
    private NioSecureStream() { }
    
    // ------------------ Key agreement / derivation ------------------
    
    /**
     * Generate an X25519 keypair.
     */
    public static KeyPair generateX25519KeyPair() throws GeneralSecurityException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("X25519");
        kpg.initialize(new NamedParameterSpec("X25519"));
        return kpg.generateKeyPair();
    }
    
    /**
     * The function expects a raw 32-byte X25519 public key (Montgomery u-coordinate).
     *
     * XECPublicKeySpec and NamedParameterSpec are the correct JCA classes for X25519/X448 key material.
     *
     * The returned key is a standard JCA PublicKey object usable in KeyAgreement.getInstance("X25519").
     */
    public static PublicKey x25519PublicKeyFromBytes(byte[] raw)
            throws GeneralSecurityException {
        
        if (raw == null || raw.length != 32) {
            throw new InvalidKeyException("X25519 public key must be 32 bytes");
        }
        
        KeyFactory kf = KeyFactory.getInstance("X25519");
        
        // Java expects the public key in X.509 SubjectPublicKeyInfo format.
        // Construct a NamedParameterSpec-wrapped key.
        XECPublicKeySpec spec = new XECPublicKeySpec(
                new NamedParameterSpec("X25519"),
                new BigInteger(raw)
        );
        
        return kf.generatePublic(spec);
    }
    
    /**
     * Input must be exactly 32 bytes (RFC 7748 X25519 private scalar).
     *
     * If you are loading a private key encoded in PKCS#8 or PEM, you must
     * instead use PKCS8EncodedKeySpec, but this function is specifically
     * for raw key bytes.
     */
    public static PrivateKey x25519PrivateKeyFromBytes(byte[] raw)
            throws GeneralSecurityException {
        
        if (raw == null || raw.length != 32) {
            throw new InvalidKeyException("X25519 private key must be 32 bytes");
        }
        
        KeyFactory kf = KeyFactory.getInstance("X25519");
        
        XECPrivateKeySpec spec = new XECPrivateKeySpec(
                new NamedParameterSpec("X25519"),
                raw
        );
        
        return kf.generatePrivate(spec);
    }
    
    /**
     * Perform X25519 key agreement to derive raw shared secret bytes.
     */
    public static byte[] performKeyAgreement(PrivateKey localPriv, PublicKey remotePub) throws GeneralSecurityException {
        KeyAgreement ka = KeyAgreement.getInstance("X25519");
        ka.init(localPriv);
        ka.doPhase(remotePub, true);
        return ka.generateSecret();
    }
    
    /**
     * HKDF-SHA256 to derive AES and HMAC keys from the shared secret.
     */
    public static SecretKeys deriveKeysHKDF(byte[] sharedSecret, byte[] salt, byte[] info) throws GeneralSecurityException {
        // HKDF-Extract
        Mac mac = Mac.getInstance("HmacSHA256");
        byte[] prk;
        if (salt == null) {
            salt = new byte[32]; // zeros
        }
        SecretKeySpec saltKey = new SecretKeySpec(salt, "HmacSHA256");
        mac.init(saltKey);
        prk = mac.doFinal(sharedSecret);
        
        // HKDF-Expand to 64 bytes (32 for AES key, 32 for HMAC key)
        int length = 64;
        int hashLen = 32;
        int n = (length + hashLen - 1) / hashLen;
        byte[] okm = new byte[length];
        byte[] t = new byte[0];
        SecretKeySpec prkKey = new SecretKeySpec(prk, "HmacSHA256");
        mac.init(prkKey);
        int pos = 0;
        for (int i = 1; i <= n; i++) {
            mac.reset();
            mac.update(t);
            if (info != null) mac.update(info);
            mac.update((byte) i);
            t = mac.doFinal();
            System.arraycopy(t, 0, okm, pos, Math.min(t.length, length - pos));
            pos += t.length;
        }
        
        byte[] aesKey = Arrays.copyOfRange(okm, 0, 32);
        byte[] hmacKey = Arrays.copyOfRange(okm, 32, 64);
        return new SecretKeys(aesKey, hmacKey);
    }
    
    public static byte[] computeHmac(SecretKey key, byte[] data) throws GeneralSecurityException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        return mac.doFinal(data);
    }
    
    public static byte[] computeHmac(byte[] key, byte[] data) throws GeneralSecurityException
    {
        MessageDigest md = MessageDigest.getInstance("SHA256");
        byte[] pKey = md.digest(key);
        return computeHmac(new SecretKeySpec(pKey, "HmacSHA256"), data);
    }
    
    
    public static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a == null || b == null) return false;
        if (a.length != b.length) return false;
        
        int diff = 0;
        for (int i = 0; i < a.length; i++) {
            diff |= (a[i] ^ b[i]);
        }
        return diff == 0;
    }
    
    public static final class SecretKeys {
        private final SecretKey aesKey; // AES-256 key
        private final SecretKey hmacKey; // HMAC-SHA256 key
        
        SecretKeys(byte[] aesKeyBytes, byte[] hmacKeyBytes) {
            this.aesKey = new SecretKeySpec(aesKeyBytes, "AES");
            this.hmacKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA256");
        }
        
        public SecretKey getAesKey()
        {
            return aesKey;
        }
        
        public SecretKey getHmacKey()
        {
            return hmacKey;
        }
    }
    
    // ------------------ Stream encryptor / decryptor ------------------
    
    /**
     * StreamEncryptor: takes plaintext ByteBuffers and writes framed, encrypted,
     * authenticated frames to a WritableByteChannel.
     */
    public static final class StreamEncryptor {
        private final WritableByteChannel out;
        private final SecretKeys aesHmacKeys;
        private final SecureRandom rng = new SecureRandom();
        
        public StreamEncryptor(WritableByteChannel out, SecretKeys aesHmacKeys) {
            this.out = out;
            this.aesHmacKeys = aesHmacKeys;
        }
        
        /**
         * Encrypts a plaintext ByteBuffer (may be partial) and writes a frame.
         * This method will block until the full frame is written or throw IOException.
         */
        public void writeEncrypted(ByteBuffer plaintext) throws GeneralSecurityException, IOException {
            while (plaintext.hasRemaining()) {
                int toProcess = Math.min(plaintext.remaining(), MAX_FRAME_PLAINTEXT);
                byte[] chunk = new byte[toProcess];
                plaintext.get(chunk);
                
                // generate IV
                byte[] iv = new byte[IV_LEN];
                rng.nextBytes(iv);
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                
                // encrypt
                Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
                cipher.init(Cipher.ENCRYPT_MODE, aesHmacKeys.getAesKey(), ivSpec);
                byte[] ciphertext = cipher.update(chunk);
                if (ciphertext == null) ciphertext = new byte[0];
                byte[] finalPart = cipher.doFinal();
                if (finalPart != null && finalPart.length > 0) {
                    byte[] tmp = new byte[ciphertext.length + finalPart.length];
                    System.arraycopy(ciphertext, 0, tmp, 0, ciphertext.length);
                    System.arraycopy(finalPart, 0, tmp, ciphertext.length, finalPart.length);
                    ciphertext = tmp;
                }
                
                // compute HMAC over (len || iv || ciphertext)
                ByteBuffer macInput = ByteBuffer.allocate(LENGTH_FIELD + IV_LEN + ciphertext.length);
                macInput.putInt(ciphertext.length);
                macInput.put(iv);
                macInput.put(ciphertext);
                byte[] macBytes = computeHmac(aesHmacKeys.getHmacKey(), macInput.array());
                
                // write frame: length(4) | iv(16) | ciphertext | hmac(32)
                ByteBuffer outBuf = ByteBuffer.allocate(LENGTH_FIELD + IV_LEN + ciphertext.length + HMAC_LEN);
                outBuf.putInt(ciphertext.length);
                outBuf.put(iv);
                outBuf.put(ciphertext);
                outBuf.put(macBytes);
                outBuf.flip();
                while (outBuf.hasRemaining()) {
                    out.write(outBuf);
                }
            }
        }
        
        public WritableByteChannel getOut()
        {
            return out;
        }
    }
    
    /**
     * StreamDecryptor: reads framed encrypted data from a ReadableByteChannel
     * and produces plaintext into a provided ByteBuffer.
     */
    public static final class StreamDecryptor {
        private final ReadableByteChannel in;
        private final SecretKeys aesHmacKeys;
        
        public StreamDecryptor(ReadableByteChannel in, SecretKeys aesHmacKeys) {
            this.in = in;
            this.aesHmacKeys = aesHmacKeys;
        }
        
        /**
         * Attempts to read and decrypt a single frame. Writes plaintext into dst.
         * Returns number of plaintext bytes written, or -1 if stream ended cleanly.
         * Throws IOException on I/O or authentication failure.
         */
        public int readDecrypted(ByteBuffer dst) throws GeneralSecurityException, IOException {
            // read length field
            ByteBuffer lenBuf = ByteBuffer.allocate(LENGTH_FIELD);
            int r = readFully(in, lenBuf);
            if (r == -1) return -1;
            lenBuf.flip();
            int cipherLen = lenBuf.getInt();
            if (cipherLen < 0 || cipherLen > (10 * 1024 * 1024)) {
                throw new IOException("invalid cipher length: " + cipherLen);
            }
            
            // read IV
            ByteBuffer ivBuf = ByteBuffer.allocate(IV_LEN);
            readFullyOrThrow(ivBuf);
            ivBuf.flip();
            byte[] iv = new byte[IV_LEN];
            ivBuf.get(iv);
            
            // read ciphertext
            ByteBuffer ctBuf = ByteBuffer.allocate(cipherLen);
            readFullyOrThrow(ctBuf);
            ctBuf.flip();
            byte[] ciphertext = new byte[cipherLen];
            ctBuf.get(ciphertext);
            
            // read hmac
            ByteBuffer hmacBuf = ByteBuffer.allocate(HMAC_LEN);
            readFullyOrThrow(hmacBuf);
            hmacBuf.flip();
            byte[] receivedHmac = new byte[HMAC_LEN];
            hmacBuf.get(receivedHmac);
            
            // verify HMAC over (len || iv || ciphertext)
            ByteBuffer macInput = ByteBuffer.allocate(LENGTH_FIELD + IV_LEN + ciphertext.length);
            macInput.putInt(ciphertext.length);
            macInput.put(iv);
            macInput.put(ciphertext);
            byte[] computed = computeHmac(aesHmacKeys.getHmacKey(), macInput.array());
            if (!constantTimeEquals(computed, receivedHmac)) {
                throw new SecurityException("HMAC verification failed");
            }
            
            // decrypt
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, aesHmacKeys.getAesKey(), new IvParameterSpec(iv));
            byte[] plain = cipher.doFinal(ciphertext);
            
            int toWrite = Math.min(dst.remaining(), plain.length);
            dst.put(plain, 0, toWrite);
            // if plaintext doesn't fit, remaining data is dropped in this simplified API.
            return toWrite;
        }
        
        private static int readFully(ReadableByteChannel ch, ByteBuffer buf) throws IOException {
            while (buf.hasRemaining()) {
                int r = ch.read(buf);
                if (r == -1) {
                    if (buf.position() == 0) return -1; // clean EOF before we started
                    throw new IOException("unexpected EOF");
                }
            }
            return buf.position();
        }
        
        private void readFullyOrThrow(ByteBuffer buf) throws IOException {
            int got = readFully(in, buf);
            if (got == -1) throw new IOException("stream closed unexpectedly");
        }
        
        public ReadableByteChannel getIn()
        {
            return in;
        }
    }
    
    // ------------------ Utility: small example ------------------
    
    /**
     * how to derive keys given local and remote X25519 keys.
     */
    public static SecretKeys deriveKeys(PrivateKey localPriv, PublicKey remotePub) throws GeneralSecurityException {
        byte[] shared = performKeyAgreement(localPriv, remotePub);
        // optional: include local/remote pubkey info in info for HKDF
        byte[] info = null;
        return deriveKeysHKDF(shared, null, info);
    }
    
    public static class EncryptedChannelPair
    {
        private final ReadableByteChannel ich;
        private final WritableByteChannel och;
        
        private KeyPair localKey;
        
        private PublicKey remotePub;
        
        private StreamEncryptor ose;
        private StreamDecryptor isd;
        private SecretKeys sessionKeys;
        
        public EncryptedChannelPair(ReadableByteChannel ich, WritableByteChannel och)
                throws GeneralSecurityException
        {
            this(ich, och, null);
        }
        
        public EncryptedChannelPair(ReadableByteChannel ich, WritableByteChannel och, KeyPair localKey)
        {
            this(ich, och, localKey, null);
        }

        public EncryptedChannelPair(ReadableByteChannel ich, WritableByteChannel och, KeyPair localKey, PublicKey remotePub)
        {
            this.ich = ich;
            this.och = och;
            this.localKey = localKey;
            this.remotePub = remotePub;
        }
        
        public boolean initialHandshake()
                throws IOException, GeneralSecurityException
        {
            // we have no local key
            if(this.localKey==null)
            {
                this.localKey = generateX25519KeyPair();
            }
            
            // we have not exchanged keys
            if(this.remotePub==null)
            {
                // send local public
                byte[] _iv = new byte[IV_LEN];
                byte[] _buf = localKey.getPublic()
                        .getEncoded();
                byte[] _mac = computeHmac(_iv,_buf);
                ByteBuffer outBuf = ByteBuffer.allocate(LENGTH_FIELD + IV_LEN + _buf.length + HMAC_LEN);
                outBuf.putInt(_buf.length);
                outBuf.put(_iv);
                outBuf.put(_buf);
                outBuf.put(_mac);
                
                och.write(outBuf);

                // and read remote public
                ByteBuffer inBuf = ByteBuffer.allocate(LENGTH_FIELD + IV_LEN + _buf.length + HMAC_LEN);
                ich.read(inBuf);
                if(inBuf.getInt()!=_buf.length)
                {
                    throw new IOException("Key Exchange failed - keylen invalid.");
                }
                inBuf.get(_iv);
                inBuf.get(_buf);
                inBuf.get(_mac);
                
                // we check mac here
                byte[] _cmac = computeHmac(_iv, _buf);
                if(!constantTimeEquals(_mac, _cmac))
                {
                    throw new IOException("Key Exchange failed - keymac invalid.");
                }
                
                this.remotePub = x25519PublicKeyFromBytes(_buf);
            }
            
            this.sessionKeys = deriveKeys(this.localKey.getPrivate(),this.remotePub);
            
            this.isd = new StreamDecryptor(this.ich,this.sessionKeys);
            this.ose = new StreamEncryptor(this.och,this.sessionKeys);
            
            return true;
        }
        
        public int read(ByteBuffer _buf)
                throws GeneralSecurityException, IOException
        {
            return this.isd.readDecrypted(_buf);
        }

        public void write(ByteBuffer _buf)
                throws GeneralSecurityException, IOException
        {
            this.ose.writeEncrypted(_buf);
        }
    }
    
    public static EncryptedChannelPair create(ReadableByteChannel ich, WritableByteChannel och)
            throws GeneralSecurityException
    {
        return new EncryptedChannelPair(ich, och);
    }
    
    public static EncryptedChannelPair create(ReadableByteChannel ich, WritableByteChannel och, KeyPair localKey)
            throws GeneralSecurityException
    {
        return new EncryptedChannelPair(ich, och, localKey);
    }
    
    public static EncryptedChannelPair create(ReadableByteChannel ich, WritableByteChannel och, KeyPair localKey, PublicKey remotePub)
    {
        return new EncryptedChannelPair(ich, och, localKey, remotePub);
    }
    
    public static EncryptedChannelPair create(URI _uri)
            throws IOException, GeneralSecurityException
    {
        return create(_uri, null, null);
    }

    public static EncryptedChannelPair create(URI _uri, KeyPair localKey)
            throws IOException, GeneralSecurityException
    {
        return create(_uri, localKey, null);
    }

    public static EncryptedChannelPair create(URI _uri, KeyPair localKey, PublicKey remotePub)
            throws IOException, GeneralSecurityException
    {
        String host = _uri.getHost();
        
        int port = _uri.getPort();
        if (host == null || port < 0)
            throw new IOException("tcp://host:port required");
        
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(host, port));
        sc.configureBlocking(true);
        
        return new EncryptedChannelPair(sc, sc, localKey, remotePub);
    }
}
