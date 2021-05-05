package com.github.terefang.jmelange.passwd.crypt;

import com.github.terefang.jmelange.passwd.HashUtil;

import java.nio.charset.StandardCharsets;

public class Mysql4Crypt
{
    public static final boolean checkPassword(String givenPasswaord, String encryptedPassword)
    {
        if(encryptedPassword.startsWith("*") && encryptedPassword.length()==41)
        {
            return crypt(givenPasswaord).equalsIgnoreCase(encryptedPassword);
        }
        return false;
    }

    public static final String crypt(String password)
    {
        return "*"+generate(password);
    }

    public static final String generate(String password)
    {
        byte[] buf = password.getBytes(StandardCharsets.UTF_8);
        return HashUtil.sha1Hex(HashUtil.sha1(buf)).toUpperCase();
    }
}