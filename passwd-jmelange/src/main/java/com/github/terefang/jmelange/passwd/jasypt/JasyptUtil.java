package com.github.terefang.jmelange.passwd.jasypt;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;

public class JasyptUtil
{
    public static PasswordEncryptor findPasswordEncryptor(String _hashedPassword)
    {
        if(_hashedPassword.startsWith(Md5Base64PasswordEncryptor._PREFIX))
        {
            return new Md5Base64PasswordEncryptor();
        }
        else
        if(_hashedPassword.startsWith(Sha256Base64PasswordEncryptor._PREFIX))
        {
            return new Sha256Base64PasswordEncryptor();
        }
        else
        if(_hashedPassword.startsWith(UnixBCryptPasswordEncryptor._PREFIX))
        {
            return new UnixBCryptPasswordEncryptor();
        }
        else
        if(_hashedPassword.startsWith(UnixBsdNtPasswordEncryptor._PREFIX))
        {
            return new UnixBsdNtPasswordEncryptor();
        }
        else
        if(_hashedPassword.startsWith(UnixSha1PasswordEncryptor._PREFIX))
        {
            return new UnixSha1PasswordEncryptor();
        }
        else
        if(_hashedPassword.startsWith(UnixSha256PasswordEncryptor._PREFIX))
        {
            return new UnixSha256PasswordEncryptor();
        }
        else
        if(_hashedPassword.startsWith(UnixSha512PasswordEncryptor._PREFIX))
        {
            return new UnixSha512PasswordEncryptor();
        }
        return new BasicPasswordEncryptor();
    }

    public static boolean checkPassword(String _plain, String _enc)
    {
        return findPasswordEncryptor(_enc).checkPassword(_plain, _enc);
    }

    public static String encryptPassword(String _plain, String _enc)
    {
        return findPasswordEncryptor(_enc).encryptPassword(_plain);
    }

    public static String encryptPassword(String _plain)
    {
        return new UnixBCryptPasswordEncryptor().encryptPassword(_plain);
    }

    public static void main(String[] args)
    {
        System.out.println(encryptPassword("s3cr3t"));
    }
}
