package com.github.terefang.jmelange.passwd.util;

import com.github.terefang.jmelange.passwd.HashUtil;

public class KDF
{

    public static byte[] kdf_tacacs(int _len, int _session_id, byte[] _secret, byte _version, byte _seq)
    {
        byte[] _salt = new byte[_secret.length+6];
        _salt[0] = (byte) ((_session_id>>>24) & 0xff);
        _salt[1] = (byte) ((_session_id>>>16) & 0xff);
        _salt[2] = (byte) ((_session_id>>>8) & 0xff);
        _salt[3] = (byte) (_session_id & 0xff);
        for(int _i = 0; _i< _secret.length; _i++)
        {
            _salt[4+_i] = _secret[_i];
        }
        _salt[_secret.length+4] =  _version;
        _salt[_secret.length+5] =  _seq;

        return kdf("MD5", _len, _salt);
    }

    public static byte[] kdf_md5(int _len, byte[] _salt)
    {
        return kdf("MD5", _len, _salt);
    }

    public static byte[] kdf_sha1(int _len, byte[] _salt)
    {
        return kdf("SHA1", _len, _salt);
    }

    public static byte[] kdf(String _algo, int _len, byte[] _salt)
    {
        byte[] _md = null;
        byte[] _ret = new byte[_len];
        for(int _i = 0; _i<_len; _i++)
        {
            if(_i == 0)
            {
                _md = HashUtil.hash(_algo, _salt);
            }
            else
            if((_i%_md.length) == 0)
            {
                _md = HashUtil.hash(_algo, _salt, _md);
            }
            _ret[_i] = _md[_i%_md.length];
        }
        return _ret;
    }

    public static byte[] kdfi(String _algo, int _itr, int _len, byte[] _salt)
    {
        byte[] _md = null;
        byte[] _ret = new byte[_len];

        for(int _j = 0; _j<_itr; _j++)
        {
            for(int _i = 0; _i<_len; _i++)
            {
                if(_i == 0 && _j == 0)
                {
                    _md = HashUtil.hash(_algo, _salt);
                }
                else
                if((_i%_md.length) == 0)
                {
                    _md = HashUtil.hash(_algo, _salt, _ret);
                }
                _ret[_i] = _md[_i%_md.length];
            }
        }
        return _ret;
    }
}
