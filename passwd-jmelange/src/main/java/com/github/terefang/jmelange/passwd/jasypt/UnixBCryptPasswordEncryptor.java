package com.github.terefang.jmelange.passwd.jasypt;

import com.github.terefang.jmelange.passwd.crypt.BCrypt;

public class UnixBCryptPasswordEncryptor extends TaggedPasswordEncryptor
{
    public static String _PREFIX = BCrypt.PREFIX_BCRYPT;

    public UnixBCryptPasswordEncryptor()
    {
        this.tagPrefix="$2y$";
    }

    @Override
    public String encryptPassword(String _plain)
    {
        return BCrypt.generate(_plain);
    }

    @Override
    public boolean checkPassword(String _plain, String _enc)
    {
        return BCrypt.checkPassword(_enc, _plain);
    }
}
