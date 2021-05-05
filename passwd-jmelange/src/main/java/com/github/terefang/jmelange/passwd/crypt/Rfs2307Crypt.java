package com.github.terefang.jmelange.passwd.crypt;

import com.github.terefang.jmelange.passwd.HashUtil;

import java.nio.charset.StandardCharsets;

public class Rfs2307Crypt
{
    public static final String PREFIX_MD5 = "{MD5}";
    public static final String PREFIX_SHA = "{SHA}";

    public static final String PREFIX_SMD5 = "{SMD5}";
    public static final String PREFIX_SSHA = "{SSHA}";
    public static final String PREFIX_SSHA256 = "{SSHA-256}";

    public static final String PREFIX_HMD5 = "{HMD5}";
    public static final String PREFIX_HSHA = "{HSHA}";
    public static final String PREFIX_HSHA256 = "{HSHA-256}";

    public static final String PREFIX_CRYPT = "{CRYPT}";

    public static boolean checkPw(String _given, String _encoded)
    {
        String _crypted = crypt(_given, _encoded);
        return _encoded.equals(_crypted);
    }

    public static String crypt(String _given, String _encoded)
    {
        if(_encoded == null)
        {
            return cryptStrong(_given);
        }
        else
        if(_encoded.startsWith(PREFIX_MD5))
        {
            return crypt_md5(_given);
        }
        else
        if(_encoded.startsWith(PREFIX_SHA))
        {
            return crypt_sha(_given);
        }
        else
        if(_encoded.startsWith(PREFIX_SMD5))
        {
            return crypt_smd5(_given, _encoded);
        }
        else
        if(_encoded.startsWith(PREFIX_SSHA))
        {
            return crypt_ssha(_given, _encoded);
        }
        else
        if(_encoded.startsWith(PREFIX_SSHA256))
        {
            return crypt_ssha256(_given, _encoded);
        }
        else
        if(_encoded.startsWith(PREFIX_HSHA256))
        {
            return crypt_hsha256(_given, _encoded);
        }
        throw new IllegalArgumentException("Unknown Algorithm "+_encoded.substring(0,_encoded.indexOf('}')+1));
    }

    public static String crypt_md5(String _given)
    {
        return crypt_generic(PREFIX_MD5, "MD5", _given);
    }

    public static String crypt_sha(String _given)
    {
        return crypt_generic(PREFIX_SHA, "SHA1", _given);
    }

    public static String crypt_generic(String _prefix, String _algo, String _given)
    {
        byte[] _crypted = HashUtil.hash(_algo, _given.getBytes(StandardCharsets.UTF_8));
        return _prefix + HashUtil.toBase64(_crypted);
    }

    public static String crypt_smd5(String _given, String _encoded)
    {
        return crypt_smd5(_given, extractSalt(_encoded, 16));
    }

    public static String crypt_smd5(String _given, byte[] _salt)
    {
        return crypt_salted_generic(PREFIX_SMD5,"MD5", _given, _salt);
    }

    public static String crypt_ssha(String _given, String _encoded)
    {
        return crypt_ssha(_given, extractSalt(_encoded, 20));
    }

    public static String crypt_ssha(String _given, byte[] _salt)
    {
        return crypt_salted_generic(PREFIX_SSHA,"SHA1", _given, _salt);
    }

    public static String crypt_ssha256(String _given, String _encoded)
    {
        return crypt_ssha256(_given, extractSalt(_encoded, 32));
    }

    public static String crypt_ssha256(String _given, byte[] _salt)
    {
        return crypt_salted_generic(PREFIX_SSHA256,"SHA-256", _given, _salt);
    }

    public static String crypt_salted_generic(String _prefix, String _algo, String _given, byte[] _salt)
    {
        byte[] _crypted = HashUtil.hash(_algo, _given.getBytes(StandardCharsets.UTF_8), _salt);
        byte[] _ret = new byte[_crypted.length+_salt.length];
        System.arraycopy(_crypted, 0, _ret, 0, _crypted.length);
        System.arraycopy(_salt, 0, _ret, _crypted.length, _salt.length);
        return _prefix + HashUtil.toBase64(_ret);
    }

    public static String crypt_hmd5(String _given, String _encoded)
    {
        return crypt_hmd5(_given, extractSalt(_encoded, 16));
    }

    public static String crypt_hmd5(String _given, byte[] _salt)
    {
        return crypt_hmac_generic(PREFIX_HMD5,"MD5", _given, _salt);
    }

    public static String crypt_hsha(String _given, String _encoded)
    {
        return crypt_hsha(_given, extractSalt(_encoded, 20));
    }

    public static String crypt_hsha(String _given, byte[] _salt)
    {
        return crypt_hmac_generic(PREFIX_HSHA,"SHA1", _given, _salt);
    }

    public static String crypt_hsha256(String _given, String _encoded)
    {
        return crypt_hsha256(_given, extractSalt(_encoded, 32));
    }

    public static String crypt_hsha256(String _given, byte[] _salt)
    {
        return crypt_hmac_generic(PREFIX_HSHA256,"SHA256", _given, _salt);
    }

    public static String crypt_hmac_generic(String _prefix, String _algo, String _given, byte[] _salt)
    {
        byte[] _crypted = HashUtil.hashMac("HMac"+_algo, _salt, _given.getBytes(StandardCharsets.UTF_8));
        byte[] _ret = new byte[_crypted.length+_salt.length];
        System.arraycopy(_crypted, 0, _ret, 0, _crypted.length);
        System.arraycopy(_salt, 0, _ret, _crypted.length, _salt.length);
        return _prefix + HashUtil.toBase64(_ret);
    }

    public static byte[] extractSalt(String _encoded, int _l)
    {
        byte[] _salt = null;
        if(_encoded == null)
        {
            _salt = HashUtil.randomSalt(_l>>2);
        }
        else
        {
            byte[] _buf = HashUtil.fromBase64(_encoded.substring(_encoded.lastIndexOf('}')+1));
            _salt = new byte[_buf.length-_l];
            System.arraycopy(_buf,_l,_salt,0,_salt.length);
        }
        return _salt;
    }

    public static String cryptStrong(String _given)
    {
        return crypt_hsha256(_given, (String)null);
    }

}

// An example ldap_md5 hash (of password) is {MD5}X03MO1qnZdYdgyfeuILPmQ==.
// An example ldap_sha1 hash (of password) is {SHA}W6ph5Mm5Pz8GgiULbPgzG37mj9g=.
// An example hash (of password) is {SMD5}jNoSMNY0cybfuBWiaGlFw3Mfi/U=.
//      After decoding, this results in a raw salt string s\x1f\x8b\xf5, and a raw MD5 checksum of \x8c\xda\x120\xd64s&\xdf\xb8\x15\xa2hiE\xc3.
// An example hash (of password) is {SSHA}pKqkNr1tq3wtQqk+UcPyA3HnA2NsU5NJ.
//      After decoding, this results in a raw salt string lS\x93I, and a raw SHA1 checksum of \xa4\xaa\xa46\xbdm\xab|-B\xa9>Q\xc3\xf2\x03q\xe7\x03c.