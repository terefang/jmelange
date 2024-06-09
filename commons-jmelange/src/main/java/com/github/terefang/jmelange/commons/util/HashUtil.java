package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.commons.base.B32;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
public class HashUtil
{
    @SneakyThrows
    public static MessageDigest getMd5Digest()
    {
        return MessageDigest.getInstance("MD5");
    }

    @SneakyThrows
    public static MessageDigest getDigest(String _name)
    {
        return MessageDigest.getInstance(_name);
    }

    public static String hashMacHex(String _name, String _key, String... _buffer)
    {
        try {
            final SecretKeySpec _keySpec = new SecretKeySpec(_key.getBytes(StandardCharsets.UTF_8), _name);
            final Mac _mac = Mac.getInstance(_name);
            _mac.init(_keySpec);
            for(String _b : _buffer)
            {
                _mac.update(_b.getBytes(StandardCharsets.UTF_8));
            }
            return toHex(_mac.doFinal());
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e)
        {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    public static String hashHex(String _name, String... _buffer)
    {
        try {
            MessageDigest _md = MessageDigest.getInstance(_name);
            for(String _b : _buffer)
            {
                _md.update(_b.getBytes(StandardCharsets.UTF_8));
            }
            return toHex(_md.digest());
        }
        catch (NoSuchAlgorithmException e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    public static String hashMacHex(String _name, byte[] _key, byte[]... _buffer)
    {
        return toHex(hashMac(_name, _key, _buffer));
    }

    public static byte[] hashMac(String _name, byte[] _key, byte[]... _buffer)
    {
        try {
            final SecretKeySpec _keySpec = new SecretKeySpec(_key, _name);
            final Mac _mac = Mac.getInstance(_name);
            _mac.init(_keySpec);
            for(byte[] _b : _buffer)
            {
                _mac.update(_b);
            }
            return _mac.doFinal();
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    public static String hashHex(String _name, byte[]... _buffer)
    {
        return toHex(hash(_name, _buffer));
    }

    public static byte[] hash(String _name, String _buffer)
    {
        return hash(_name, _buffer.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] hash(String _name, byte[]... _buffer)
    {
        try {
            MessageDigest _md = MessageDigest.getInstance(_name);
            for(byte[] _b : _buffer)
            {
                _md.update(_b);
            }
            return _md.digest();
        }
        catch (NoSuchAlgorithmException e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    public static byte[] sha512HMac(byte[] _key, byte[] _buffer)
    {
        return hashMac("HMacSHA512", _key, _buffer);
    }
    public static byte[] sha512HMac(byte[] _key, byte[]... _buffer)
    {
        return hashMac("HMacSHA512", _key, _buffer);
    }

    public static byte[] sha256HMac(byte[] _key, byte[] _buffer)
    {
        return hashMac("HMacSHA256", _key, _buffer);
    }
    public static byte[] sha256HMac(byte[] _key, byte[]... _buffer)
    {
        return hashMac("HMacSHA256", _key, _buffer);
    }

    public static byte[] sha1HMac(byte[] _key, byte[] _buffer)
    {
        return hashMac("HMacSHA1", _key, _buffer);
    }
    public static byte[] sha1HMac(byte[] _key, byte[]... _buffer)
    {
        return hashMac("HMacSHA1", _key, _buffer);
    }

    public static byte[] md5HMac(byte[] _key, byte[]... _buffer)
    {
        return hashMac("HMacMD5", _key, _buffer);
    }
    public static byte[] md5HMac(byte[] _key, byte[] _buffer)
    {
        return hashMac("HMacMD5", _key, _buffer);
    }

    public static byte[] sha512(byte[] ..._name)
    {
        return hash("SHA-512", _name);
    }

    public static byte[] sha256(byte[] ..._name)
    {
        return hash("SHA-256", _name);
    }

    public static byte[] sha1(byte[] ..._name)
    {
        return hash("SHA-1", _name);
    }

    public static byte[] md5(byte[] ... _name)
    {
        return hash("MD5", _name);
    }


    public static String sha512HMacHex(byte[] _key, byte[]... _buffer)
    {
        return hashMacHex("HMacSHA512", _key, _buffer);
    }
    public static String sha512HMacHex(byte[] _key, byte[] _buffer)
    {
        return hashMacHex("HMacSHA512", _key, _buffer);
    }

    public static String sha256HMacHex(byte[] _key, byte[]... _buffer)
    {
        return hashMacHex("HMacSHA256", _key, _buffer);
    }
    public static String sha256HMacHex(byte[] _key, byte[] _buffer)
    {
        return hashMacHex("HMacSHA256", _key, _buffer);
    }

    public static String sha1HMacHex(byte[] _key, byte[] _buffer)
    {
        return hashMacHex("HMacSHA1", _key, _buffer);
    }

    public static String sha1HMacHex(byte[] _key, byte[]... _buffer)
    {
        return hashMacHex("HMacSHA1", _key, _buffer);
    }

    public static String md5HMacHex(byte[] _key, byte[]... _buffer)
    {
        return hashMacHex("HMacMD5", _key, _buffer);
    }
    public static String md5HMacHex(byte[] _key, byte[] _buffer)
    {
        return hashMacHex("HMacMD5", _key, _buffer);
    }

    static char[] HEXDIGITS = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    public static byte[] fromHex(String _hex)
    {
        return fromHex(_hex.toCharArray());
    }

    public static byte[] fromHex(char[] _hex)
    {
        byte[] _ret = new byte[_hex.length/2];
        for(int _i=0; _i<_ret.length; _i++)
        {
            _ret[_i] = (byte) ((toDigit(_hex[_i*2])<<4) | toDigit(_hex[(_i*2)+1]));
        }
        return _ret;
    }


    public static String toBase64(Long _n)
    {
        return toBase64(BigInteger.valueOf(_n).toByteArray());
    }

    public static String toBase64(String binaryData) {
        return toBase64(binaryData.getBytes());
    }

    public static String toBase64Np(Long _n)
    {
        return toBase64Np(BigInteger.valueOf(_n).toByteArray());
    }

    public static String toBase64Np(byte[] binaryData) {
        return new String(Base64.encodeBase64(binaryData, false));
    }

    public static String toBase64Np(String binaryData) {
        return toBase64Np(binaryData.getBytes());
    }

    public static byte[] fromBase64(String _b64) {
        return Base64.decodeBase64(_b64.getBytes(StandardCharsets.UTF_8));
    }

    public static String toBase32(Long _n)
    {
        return toBase32(BigInteger.valueOf(_n).toByteArray());
    }

    public static String toBase32(byte[] binaryData) {
        return B32.encode(binaryData);
    }

    public static String toBase32(String binaryData) {
        return toBase32(binaryData.getBytes());
    }

    @SneakyThrows
    public static byte[] fromBase32(String _b32) {
        return B32.decode(_b32);
    }

    @SneakyThrows
    public static byte[] randomSalt(int _l)
    {
        return SecureRandom.getInstanceStrong().generateSeed(_l);
    }

    public static String sha512HMacHex(String _key, String _buffer)
    {
        return hashMacHex("HMacSHA512", _key, _buffer);
    }

    public static String sha256HMacHex(String _key, String _buffer)
    {
        return hashMacHex("HMacSHA256", _key, _buffer);
    }

    public static String sha1HMacHex(String _key, String _buffer)
    {
        return hashMacHex("HMacSHA1", _key, _buffer);
    }

    public static String md5HMacHex(String _key, String _buffer)
    {
        return hashMacHex("HMacMD5", _key, _buffer);
    }

    public static String sha384HMacHex(String _key, String _buffer)
    {
        return hashMacHex("HMacSHA384", _key, _buffer);
    }

    public static String hashMacHex(String _name, String _key, String _buffer, String _buffer2)
    {
        return hashMacHex(_name, _key, StringUtil.toArray(_buffer, _buffer2));
    }

    public static String hashMacHex(String _name, String _key, String _buffer, String _buffer2, String _buffer3)
    {
        return hashMacHex(_name, _key, StringUtil.toArray(_buffer, _buffer2, _buffer3));
    }

    public static String hashMacHex(String _name, String _key, String _buffer)
    {
        try {
            final SecretKeySpec _keySpec = new SecretKeySpec(_key.getBytes(StandardCharsets.UTF_8), _name);
            final Mac _mac = Mac.getInstance(_name);
            _mac.init(_keySpec);
            return toHex(_mac.doFinal(_buffer.getBytes(StandardCharsets.UTF_8)));
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return null;
        }
    }

    public static String hashHex(String _name, String _buffer, String _buffer2)
    {
        return hashHex(_name, StringUtil.toArray(_buffer, _buffer2));
    }

    public static String hashHex(String _name, String _buffer, String _buffer2, String _buffer3)
    {
        return hashHex(_name, StringUtil.toArray(_buffer, _buffer2, _buffer3));
    }

    public static String hashHex(String _name, String _buffer)
    {
        try {
            MessageDigest _md = MessageDigest.getInstance(_name);
            return toHex(_md.digest(_buffer.getBytes(StandardCharsets.UTF_8)));
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String sha512Hex(String _name)
    {
        return hashHex("SHA-512", _name);
    }

    public static String sha256Hex(String _name)
    {
        return hashHex("SHA-256", _name);
    }

    public static String sha1Hex(String _name)
    {
        return hashHex("SHA-1", _name);
    }

    public static String sha1Hex(byte[] _name)
    {
        return hashHex("SHA-1", _name);
    }

    public static String md5Hex(String _name)
    {
        return hashHex("MD5", _name);
    }

    @SneakyThrows
    static int toDigit(final char ch)
    {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new IllegalArgumentException("Illegal hexadecimal character " + ch);
        }
        return digit;
    }

    public static String toHex(Long _n)
    {
        return toHex(BigInteger.valueOf(_n).toByteArray());
    }

    public static String toHex(byte[] _buf)
    {
        char[] _out = new char[_buf.length*2];
        for(int _i=0; _i<_buf.length; _i++)
        {
            _out[_i*2] = HEXDIGITS[(_buf[_i]>>>4) & 0xf];
            _out[_i*2+1] = HEXDIGITS[_buf[_i] & 0xf];
        }
        return new String(_out);
    }

    public static String toHex(String _buf)
    {
        return toHex(_buf.getBytes());
    }

    public static String toBase64(byte[] binaryData) {
        return new String(Base64.encodeBase64(binaryData));
    }

    static char[] B26DIGITS = {'Q','W','E','R','T','Z','U','I','O','P',
            'A','S','D','F','G','H','J','K','L',
            'Y','X','C','V','B','N','M'};
    static BigInteger B26L = BigInteger.valueOf(B26DIGITS.length);

    public static String toBase26(Long _n)
    {
        return toBase26(BigInteger.valueOf(_n).toByteArray());
    }

    public static String toBase26(byte[] binaryData)
    {
        StringBuffer _sb = new StringBuffer();
        BigInteger _bi = new BigInteger(1,binaryData);
        while(_bi.compareTo(BigInteger.ZERO) > 0)
        {
            _sb.append((char)B26DIGITS[(_bi.intValue() & 0x7fffffff)% B26DIGITS.length]);
            _bi = _bi.divide(B26L);
        }
        return _sb.toString();
    }

    public static String toBase26(String binaryData) {
        return toBase26(binaryData.getBytes());
    }


    static char[] B62DIGITS = {'Q','W','E','R','T','Z','U','I','O','P',
            'A','S','D','F','G','H','J','K','L',
            'Y','X','C','V','B','N','M',
            '0','1','2','3','4','5','6','7','8','9',
            'q','w','e','r','t','z','u','i','o','p',
            'a','s','d','f','g','h','j','k','l',
            'y','x','c','v','b','n','m'};
    static BigInteger B62L = BigInteger.valueOf(B62DIGITS.length);

    public static String toBase62(Long _n)
    {
        return toBase62(BigInteger.valueOf(_n).toByteArray());
    }

    public static String toBase62(byte[] binaryData)
    {
        StringBuffer _sb = new StringBuffer();
        BigInteger _bi = new BigInteger(1,binaryData);
        while(_bi.compareTo(BigInteger.ZERO) > 0)
        {
            _sb.append((char)B62DIGITS[(_bi.intValue() & 0x7fffffff)% B62DIGITS.length]);
            _bi = _bi.divide(B62L);
        }
        return _sb.toString();
    }

    public static String toBase62(String binaryData) {
        return toBase62(binaryData.getBytes());
    }


    public static String toBase36(Long _n)
    {
        return Long.toString(_n,36);
    }

    public static String toBase36(byte[] binaryData)
    {
        return new BigInteger(1,binaryData).toString(36);
    }

    public static String toBase36(String binaryData) {
        return toBase36(binaryData.getBytes());
    }

    public static String base64(String binaryData) { return toBase64(binaryData); }

    public static byte[] md5(byte[] data) {
        return hash("MD5", data);
    }

    public static byte[] sha1(byte[] data) { return hash("SHA-1", data); }

    public static byte[] sha256(byte[] data) {
        return hash("SHA-256", data);
    }

    public static byte[] sha512(byte[] data) {
        return hash("SHA-512", data);
    }

    public static String base64(byte[] binaryData) { return toBase64(binaryData); }

    public static byte[] md5(String data) {
        return hash("MD5", data);
    }

    public static byte[] sha1(String data) { return hash("SHA-1", data); }

    public static byte[] sha256(String data) {
        return hash("SHA-256", data);
    }

    public static byte[] sha512(String data) {
        return hash("SHA-512", data);
    }

    public static byte[] gpsk256(byte[] _key, byte[] _secret, int _len)
    {
        _len=(_len/32)+(_len%32!=0 ? 1 : 0);
        byte[] _ret = new byte[_len*32];
        for(int _i = 1; _i<=_len; _i++)
        {
            byte[] _tmp = sha256HMac(_key,new byte[]{(byte)0, (byte)_i}, _secret);
            System.arraycopy(_tmp,0,_ret,(_i-1)*32, _tmp.length);
        }
        return _ret;
    }

    public static void main(String[] args) {
        System.err.println(toHex(gpsk256("12345".getBytes(),"secret".getBytes(), 45)));
    }
}
