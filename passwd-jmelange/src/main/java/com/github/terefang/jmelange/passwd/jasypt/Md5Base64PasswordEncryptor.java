package com.github.terefang.jmelange.passwd.jasypt;

import org.jasypt.util.password.BasicPasswordEncryptor;

public class Md5Base64PasswordEncryptor extends TaggedPasswordEncryptor
{
    public static String _PREFIX = "$MD5$";

    public static Md5Base64PasswordEncryptor create() { return new Md5Base64PasswordEncryptor(); }

    public Md5Base64PasswordEncryptor()
    {
        this.passwordEncryptor = new BasicPasswordEncryptor();
        this.tagPrefix=_PREFIX;
    }
}
