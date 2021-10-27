package com.github.terefang.jmelange.passwd.jasypt;

import org.jasypt.util.password.PasswordEncryptor;

public class TaggedPasswordEncryptor implements PasswordEncryptor
{
    public String encryptPassword(String _plain)
    {
        return this.tagPrefix+this.passwordEncryptor.encryptPassword(_plain);
    }

    public boolean checkPassword(String _plain, String _enc)
    {
        return this.passwordEncryptor.checkPassword(_plain, _enc.substring(this.tagPrefix.length()));
    }

    String tagPrefix;
    PasswordEncryptor passwordEncryptor;

    public String getTagPrefix() {
        return tagPrefix;
    }

    public void setTagPrefix(String tagPrefix) {
        this.tagPrefix = tagPrefix;
    }

    public PasswordEncryptor getPasswordEncryptor() {
        return passwordEncryptor;
    }

    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }
}
