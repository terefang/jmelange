package com.github.terefang.jmelange.passwd.crypt;

import com.github.terefang.jmelange.passwd.HashUtil;

import java.util.Random;

public class Sha1Crypt
{
    public static Random RNG = new Random();

    public static final String SIMPLE_PREFIX = "{sha1}";

    public static final String simpleCrypt(char[] pass)
    {
        return simpleCrypt(new String(pass));
    }

    public static final boolean simpleCheckpw(char[] pass, char[] tocheck)
    {
        return simpleCheckpw(new String(pass), new String(tocheck));
    }

    public static final boolean simpleCheckpw(String pass, String tocheck)
    {
        return simpleCrypt(pass).equalsIgnoreCase(tocheck);
    }

    public static final String simpleCrypt(String pass)
    {
        return SIMPLE_PREFIX+sha1_hex(pass);
    }

    public static final String SHA1_PREFIX = "$4$";

    public static final String crypt(char[] pass)
    {
        return crypt(new String(pass), null, false);
    }

    public static final String crypt(String pass)
    {
        return crypt(pass, null, false);
    }

    public static final String crypt(char[] pass, char[] salt)
    {
        return crypt(new String(pass), new String(salt), false);
    }

    public static final boolean checkpw(char[] pass, char[] salt)
    {
        return checkpw(new String(pass), new String(salt));
    }

    public static final boolean checkpw(String pass, String salt)
    {
        String crypted = crypt(pass, salt, true);
        int of1 = 3;
        int of2 = salt.lastIndexOf('$');
        return crypted.substring(of1,of2).equals(salt.substring(of1, of2));
    }

    public static final boolean checkpw(String pass, String salt, long time)
    {
        if(!checkpw(pass, salt))
        {
            return false;
        }
        int of1 = salt.lastIndexOf('$');
        if((salt.substring(of1+1).length() > 0) && (time != -1L))
        {
            long t = Long.parseLong(salt.substring(of1+1));
            return (t >= time);
        }
        return true;
    }

    public static final String crypt(String pass, String salt, boolean for_check)
    {
        if(salt==null)
        {
            salt="";
        }

        if (salt.startsWith(SHA1_PREFIX))
        {
            salt = salt.substring(3);
        }

        if (salt.contains("$"))
        {
            salt = salt.substring(0, salt.indexOf('$'));
        }

        if (salt.length() < 8 && !for_check)
        {
            byte[] buf = HashUtil.randomSalt(6);
            salt = HashUtil.toBase64(buf);
        }

        return SHA1_PREFIX+salt+"$"+sha1_base64(pass, salt)+"$";
    }

    private static String sha1_base64(String pass, String salt)
    {
        String ret = HashUtil.toBase64Np(HashUtil.sha1((pass+salt).getBytes()));
        return ret;
    }

    private static String sha1_hex(String pass)
    {
        return HashUtil.sha1Hex((pass).getBytes()).toLowerCase();
    }

    public static void main(String[] args)
    {
        String plainpass = "s3cr3t";
        String crypted = crypt(plainpass);
        String crypted2 = "$4$/0KvJXIJ$TfM0qwGv55dKUyNOQ7Q+1lfy0l8$1528829197000";
        System.out.println(plainpass);
        System.out.println(crypted);
        System.out.println(checkpw(plainpass, crypted));
        System.out.println(checkpw("", crypted2));
        System.out.println(checkpw("", crypted2, System.currentTimeMillis()));
        System.out.println(checkpw("", crypted2, 1528829197000L));
    }

}