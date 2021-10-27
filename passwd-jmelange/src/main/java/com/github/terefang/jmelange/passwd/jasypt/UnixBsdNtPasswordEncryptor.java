package com.github.terefang.jmelange.passwd.jasypt;

import com.github.terefang.jmelange.passwd.crypt.BsdNtCrypt;

public class UnixBsdNtPasswordEncryptor extends TaggedPasswordEncryptor
{
    public static String _PREFIX = BsdNtCrypt.BSDNT_PREFIX;

    public UnixBsdNtPasswordEncryptor()
    {
        this.tagPrefix=_PREFIX;
    }

    @Override
    public String encryptPassword(String _plain)
    {
        return BsdNtCrypt.crypt(_PREFIX, _plain);
    }

    @Override
    public boolean checkPassword(String _plain, String _enc)
    {
        return BsdNtCrypt.checkPassword(_plain, _enc);
    }
}
