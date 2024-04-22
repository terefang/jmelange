package com.github.terefang.jmelange.passwd.crypt;

import com.github.terefang.jmelange.commons.util.HashUtil;
import com.github.terefang.jmelange.passwd.util.PBKDF;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * Created by fredo on 26.08.15.
 */
public class PCrypt
{
    public static final int LOAD_FACTOR = 1000;
    public static final int SPACE_FACTOR = 30;

    public static final String pcrypt0(String passcode, String salt)
    {
        return pcrypt(passcode, salt, 0);
    }

    public static final String pcrypt0(String passcode)
    {
        return pcrypt(passcode, 0);
    }

    public static final String pcrypt5(String passcode, String salt)
    {
        return pcrypt(passcode, salt, 5);
    }

    public static final String pcrypt5(String passcode)
    {
        return pcrypt(passcode, 5);
    }

    public static final String pcrypt9(String passcode, String salt)
    {
        return pcrypt(passcode, salt, 9);
    }

    public static final String pcrypt9(String passcode)
    {
        return pcrypt(passcode, 9);
    }

    public static final String pcrypt(String passcode, String salt, int c)
    {
        try
        {
            salt = HashUtil.sha256Hex(salt).substring(0, 16).toUpperCase();
            byte[] saltBytes = HashUtil.fromHex(salt.toCharArray());
            return pcrypt_(passcode.getBytes(), saltBytes, c);
        }
        catch(Exception xe)
        {
            return null;
        }
    }

    public static final String pcrypt(String passcode, int c)
    {
        try
        {
            byte[] saltBytes = new byte[8];
            SecureRandom.getInstance("SHA1PRNG").nextBytes(saltBytes);
            return pcrypt_(passcode.getBytes(), saltBytes, c);
        }
        catch(Exception xe)
        {
            return null;
        }
    }

    public static final String pcrypt_(byte[] credential, byte[] saltBytes, int c)
    {
        try
        {
            byte[] DK = PBKDF.pbkdf2("HmacSHA256", credential, saltBytes, LOAD_FACTOR<<c, SPACE_FACTOR);
            return "$p"+c+"$"+(HashUtil.toHex(saltBytes).toUpperCase())+"$"+(HashUtil.toBase64(DK));
        }
        catch(Exception xe)
        {
            return null;
        }
    }

    public static final boolean verify(String plain, String encrypted)
    {
        try
        {
            String[] parts = encrypted.split("\\$");
            int c = Integer.parseInt(parts[1].substring(1));
            byte[] saltBytes = HashUtil.fromHex(parts[2].toCharArray());
            return encrypted.equalsIgnoreCase(pcrypt_(plain.getBytes(), saltBytes, c));
        }
        catch(Exception xe)
        {
            return false;
        }
    }

    public static final String PREFIX_PBKDF2 = "$pbkdf2$";

    @SneakyThrows
    public static final String crypt_pbkdf2_sha1(String _pw, byte[] _salt, int _rounds)
    {
        byte[] _buf = PBKDF.pbkdf2("HMACSHA1", _pw.getBytes(StandardCharsets.UTF_8), _salt,_rounds, -1);
        return PREFIX_PBKDF2+_rounds+"$"+HashUtil.toBase64Np(_salt).replace('+', '.')+"$"+HashUtil.toBase64Np(_buf).replace('+', '.');
    }

    @SneakyThrows
    public static final String crypt_pbkdf2_sha1(String _pw, byte[] _salt)
    {
        return crypt_pbkdf2_sha1(_pw, _salt, 99999);
    }

    @SneakyThrows
    public static final String crypt_pbkdf2_sha1(String _pw)
    {
        return crypt_pbkdf2_sha1(_pw, HashUtil.randomSalt(16), 99999);
    }

    @SneakyThrows
    public static final String crypt_pbkdf2_sha1(String _pw, String _encoded)
    {
        if(_encoded==null)
        {
            return crypt_pbkdf2_sha1(_pw);
        }

        if(!_encoded.startsWith(PREFIX_PBKDF2))
        {
            throw new IllegalArgumentException(_encoded);
        }

        String[] _parts = _encoded.substring(PREFIX_PBKDF2.length()).split("\\$");
        int _rounds = Integer.parseInt(_parts[0]);
        byte[] _salt = HashUtil.fromBase64(_parts[1].replace('.', '+'));
        return crypt_pbkdf2_sha1(_pw, _salt, _rounds);
    }

    public static final String PREFIX_PBKDF2_256 = "$pbkdf2-sha256$";

    @SneakyThrows
    public static final String crypt_pbkdf2_sha256(String _pw, byte[] _salt, int _rounds)
    {
        byte[] _buf = PBKDF.pbkdf2("HMACSHA256", _pw.getBytes(StandardCharsets.UTF_8), _salt,_rounds, -1);
        return PREFIX_PBKDF2_256+_rounds+"$"+HashUtil.toBase64Np(_salt).replace('+', '.')+"$"+HashUtil.toBase64Np(_buf).replace('+', '.');
    }

    @SneakyThrows
    public static final String crypt_pbkdf2_sha256(String _pw, byte[] _salt)
    {
        return crypt_pbkdf2_sha256(_pw, _salt, 99999);
    }

    @SneakyThrows
    public static final String crypt_pbkdf2_sha256(String _pw)
    {
        return crypt_pbkdf2_sha256(_pw, HashUtil.randomSalt(16), 99999);
    }

    @SneakyThrows
    public static final String crypt_pbkdf2_sha256(String _pw, String _encoded)
    {
        if(_encoded==null)
        {
            return crypt_pbkdf2_sha256(_pw);
        }

        if(!_encoded.startsWith(PREFIX_PBKDF2_256))
        {
            throw new IllegalArgumentException(_encoded);
        }

        String[] _parts = _encoded.substring(PREFIX_PBKDF2_256.length()).split("\\$");
        int _rounds = Integer.parseInt(_parts[0]);
        byte[] _salt = HashUtil.fromBase64(_parts[1].replace('.', '+'));
        return crypt_pbkdf2_sha256(_pw, _salt, _rounds);
    }

    public static final String PREFIX_PBKDF2_512 = "$pbkdf2-sha512$";

    @SneakyThrows
    public static final String crypt_pbkdf2_sha512(String _pw, byte[] _salt, int _rounds)
    {
        byte[] _buf = PBKDF.pbkdf2("HMACSHA512", _pw.getBytes(StandardCharsets.UTF_8), _salt,_rounds, -1);
        return PREFIX_PBKDF2_512+_rounds+"$"+HashUtil.toBase64Np(_salt).replace('+', '.')+"$"+HashUtil.toBase64Np(_buf).replace('+', '.');
    }

    @SneakyThrows
    public static final String crypt_pbkdf2_sha512(String _pw, byte[] _salt)
    {
        return crypt_pbkdf2_sha512(_pw, _salt, 99999);
    }

    @SneakyThrows
    public static final String crypt_pbkdf2_sha512(String _pw)
    {
        return crypt_pbkdf2_sha512(_pw, HashUtil.randomSalt(16), 99999);
    }

    @SneakyThrows
    public static final String crypt_pbkdf2_sha512(String _pw, String _encoded)
    {
        if(_encoded==null)
        {
            return crypt_pbkdf2_sha512(_pw);
        }

        if(!_encoded.startsWith(PREFIX_PBKDF2_512))
        {
            throw new IllegalArgumentException(_encoded);
        }

        String[] _parts = _encoded.substring(PREFIX_PBKDF2_512.length()).split("\\$");
        int _rounds = Integer.parseInt(_parts[0]);
        byte[] _salt = HashUtil.fromBase64(_parts[1].replace('.', '+'));
        return crypt_pbkdf2_sha512(_pw, _salt, _rounds);
    }

/*
    A PBKDF2 (Password-Based Key Derivation Function 2) derived password. Radiator currently
    supports password derivation with Pseudo Random Function (PRF) HMAC-SHA1 and the following
    password format (PRF:rounds:salt:hash). See goodies/pbkdf2.pl for the format details. Requires
    Digest::HMAC_SHA1 and MIME::Base64.
        User-Password = {PBKDF2}HMACSHA1:
        9000:h9Pwh4tcu0w=:iN9vitCZ1mqBKEu21dlc0RW2tlc=
*/

    public static final String PREFIX_RADIATOR = "{PBKDF2}";

    @SneakyThrows
    public static final String radiator_pbkdf2(String _pw, byte[] _salt, int _rounds, String _algo, int _dkLen, String _sep)
    {
        byte[] _buf = PBKDF.pbkdf2(_algo, _pw.getBytes(StandardCharsets.UTF_8), _salt,_rounds, _dkLen);
        return PREFIX_RADIATOR+_algo+_sep+_rounds+_sep+HashUtil.toBase64(_salt)+_sep+HashUtil.toBase64(_buf);
    }

    @SneakyThrows
    public static final String radiator_pbkdf2(String _pw, byte[] _salt, int _rounds, String _algo, int _dkLen)
    {
        return radiator_pbkdf2(_pw, _salt, _rounds, _algo, _dkLen, ":");
    }

    @SneakyThrows
    public static final String radiator_pbkdf2(String _pw, byte[] _salt, int _rounds, String _algo)
    {
        return radiator_pbkdf2(_pw, _salt, _rounds, _algo, 20, ":");
    }

    @SneakyThrows
    public static final String radiator_pbkdf2(String _pw, byte[] _salt, int _rounds)
    {
        return radiator_pbkdf2(_pw, _salt, _rounds, "HMACSHA1", 20, ":");
    }

    @SneakyThrows
    public static final String radiator_pbkdf2(String _pw, byte[] _salt)
    {
        return radiator_pbkdf2(_pw, _salt, 9000, "HMACSHA1", 20, ":");
    }

    @SneakyThrows
    public static final String radiator_pbkdf2(String _pw)
    {
        byte[] _salt = HashUtil.randomSalt(8);
        return radiator_pbkdf2(_pw, _salt);
    }

    @SneakyThrows
    public static final String radiator_pbkdf2(String _pw, String _encoded)
    {
        if(_encoded==null)
        {
            return radiator_pbkdf2(_pw);
        }

        if(!_encoded.startsWith(PREFIX_RADIATOR))
        {
            throw new IllegalArgumentException(_encoded);
        }

        String[] _parts = _encoded.substring(PREFIX_RADIATOR.length()).split(":");
        int _rounds = Integer.parseInt(_parts[1]);
        byte[] _salt = HashUtil.fromBase64(_parts[2]);
        byte[] _chk = HashUtil.fromBase64(_parts[3]);
        return radiator_pbkdf2(_pw, _salt, _rounds, _parts[0], _chk.length);
    }

    public static final String PREFIX_ATLASSIAN = "{PKCS5S2}";

    @SneakyThrows
    public static final String atlassian_pbkdf2_sha1(String _pw, byte[] _salt)
    {
        byte[] _buf = PBKDF.pbkdf2("HMacSHA1", _pw.getBytes(StandardCharsets.UTF_8), _salt,10000, 32);
        byte[] _ret = new byte[_buf.length+_salt.length];
        System.arraycopy(_salt,0, _ret,0, _salt.length);
        System.arraycopy(_buf,0, _ret,_salt.length, _buf.length);
        return PREFIX_ATLASSIAN+HashUtil.toBase64(_ret);
    }

    @SneakyThrows
    public static final String atlassian_pbkdf2_sha1(String _pw, String _encoded)
    {
        if(_encoded==null)
        {
            return atlassian_pbkdf2_sha1(_pw);
        }

        if(!_encoded.startsWith(PREFIX_ATLASSIAN))
        {
            throw new IllegalArgumentException(_encoded);
        }

        byte[] _buf = HashUtil.fromBase64(_encoded.substring(PREFIX_ATLASSIAN.length()));
        byte[] _salt = new byte[_buf.length-32];
        System.arraycopy(_buf,0, _salt,0, _salt.length);
        return atlassian_pbkdf2_sha1(_pw, _salt);
    }

    @SneakyThrows
    public static final String atlassian_pbkdf2_sha1(String _pw)
    {
        byte[] _salt = HashUtil.randomSalt(16);
        return atlassian_pbkdf2_sha1(_pw, _salt);
    }

    public static void main(String[] args)
    {
        String epwd = pcrypt0("s3cr3t", "el-torro");
        System.out.println(epwd+" "+epwd.length());
        System.out.println(verify("s3cr3t", epwd));

        for(int i = 0; i<10; i++)
        {
            long stamp = System.nanoTime();
            epwd = pcrypt("s3cr3t", i);
            System.out.println(epwd+" "+epwd.length());
            System.out.println(verify("s3cr3t", epwd));
            stamp = System.nanoTime()-stamp;
            System.out.println("ms="+(stamp/1000000));
        }
    }
}
