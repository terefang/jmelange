package com.github.terefang.jmelange.passwd.jasypt;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class Sha256Base64PasswordEncryptor extends TaggedPasswordEncryptor
{
    public static String _PREFIX = "$SHA256$";

    public static Sha256Base64PasswordEncryptor create() { return new Sha256Base64PasswordEncryptor(); }

    public Sha256Base64PasswordEncryptor()
    {
        this.passwordEncryptor = new StrongPasswordEncryptor();
        this.tagPrefix=_PREFIX;
    }
}
