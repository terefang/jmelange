package com.github.terefang.jmelange.passwd.crypt;

import com.github.terefang.jmelange.commons.util.HashUtil;

import java.nio.charset.StandardCharsets;

public class B64Sha256Crypt {
    public static String _PREFIX = "$SHA256$";

    public static String crypt(String _plain)
    {
        return _PREFIX+ HashUtil.base64(HashUtil.sha256(_plain.getBytes(StandardCharsets.UTF_8)));
    }

    public static String crypt(char[] _plain)
    {
        return crypt(new String(_plain));
    }

    public static String cryptUCS2(String _plain)
    {
        return _PREFIX+ HashUtil.base64(HashUtil.sha256(_plain.getBytes(StandardCharsets.UTF_16LE)));
    }

    public static String cryptUCS2(char[] _plain)
    {
        return cryptUCS2(new String(_plain));
    }

    public static boolean checkPw(String _plain, String _encr)
    {
        if(checkPwUCS2(_plain, _encr))
        {
            return true;
        }
        return _encr.equals(crypt(_plain));
    }

    public static boolean checkPw(char[] _plain, char[] _encr)
    {
        if(checkPwUCS2(_plain, _encr))
        {
            return true;
        }
        return new String(_encr).equals(crypt(_plain));
    }

    public static boolean checkPwUCS2(String _plain, String _encr)
    {
        return _encr.equals(cryptUCS2(_plain));
    }

    public static boolean checkPwUCS2(char[] _plain, char[] _encr)
    {
        return new String(_encr).equals(cryptUCS2(_plain));
    }

}
