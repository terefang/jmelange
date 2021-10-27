package com.github.terefang.jmelange.passwd.jasypt;

import com.github.terefang.jmelange.passwd.crypt.Sha2Crypt;

import java.nio.charset.StandardCharsets;

public class UnixSha256PasswordEncryptor extends TaggedPasswordEncryptor
{
    public static String _PREFIX = Sha2Crypt.SHA256_PREFIX;

    public UnixSha256PasswordEncryptor()
    {
        this.tagPrefix=_PREFIX;
    }

    @Override
    public String encryptPassword(String _plain)
    {
        return Sha2Crypt.sha256Crypt(_plain.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean checkPassword(String _plain, String _enc)
    {
        return Sha2Crypt.sha256Crypt(_plain.getBytes(StandardCharsets.UTF_8), _enc).equalsIgnoreCase(_enc);
    }
}
