package com.github.terefang.jmelange.passwd.jasypt;

import com.github.terefang.jmelange.passwd.crypt.Sha1Crypt;

public class UnixSha1PasswordEncryptor extends TaggedPasswordEncryptor
{
    public static String _PREFIX = Sha1Crypt.SHA1_PREFIX;

    public UnixSha1PasswordEncryptor()
    {
        this.tagPrefix=_PREFIX;
    }

    @Override
    public String encryptPassword(String _plain)
    {
        return Sha1Crypt.crypt(_plain);
    }

    @Override
    public boolean checkPassword(String _plain, String _enc)
    {
        return Sha1Crypt.checkpw(_plain, _enc);
    }
}
