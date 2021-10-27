package com.github.terefang.jmelange.passwd.jasypt;

import com.github.terefang.jmelange.passwd.crypt.Sha2Crypt;

import java.nio.charset.StandardCharsets;

public class UnixSha512PasswordEncryptor extends TaggedPasswordEncryptor
{
    public static String _PREFIX = Sha2Crypt.SHA512_PREFIX;

    public UnixSha512PasswordEncryptor()
    {
        this.tagPrefix=_PREFIX;
    }

    @Override
    public String encryptPassword(String _plain)
    {
        return Sha2Crypt.sha512Crypt(_plain.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean checkPassword(String _plain, String _enc)
    {
        return Sha2Crypt.sha512Crypt(_plain.getBytes(StandardCharsets.UTF_8), _enc).equalsIgnoreCase(_enc);
    }
}
