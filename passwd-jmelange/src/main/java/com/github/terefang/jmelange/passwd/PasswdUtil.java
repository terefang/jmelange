package com.github.terefang.jmelange.passwd;

import com.github.terefang.jmelange.passwd.crypt.*;
import com.github.terefang.jmelange.passwd.otp.HOTP;

import java.nio.charset.StandardCharsets;

public class PasswdUtil
{
    public static final boolean matchPassword(String _identity, String _givenPassword, long _tick, String _encPassword)
    {
        try
        {
            if (_encPassword.startsWith("{plain}"))
            {
                return _encPassword.substring(7).equalsIgnoreCase(_givenPassword);
            }
            else
            if(_encPassword.startsWith(PCrypt.PREFIX_PBKDF2))
            {
                return PCrypt.crypt_pbkdf2_sha1(_givenPassword, _encPassword).equalsIgnoreCase(_encPassword);
            }
            else
            if(_encPassword.startsWith(Md5Crypt.APR1_PREFIX))
            {
                return Md5Crypt.apr1Crypt(_givenPassword, _encPassword).equalsIgnoreCase(_encPassword);
            }
            else
            if(_encPassword.startsWith(BCrypt.PREFIX_BCRYPT))
            {
                return BCrypt.checkPassword(_givenPassword, _encPassword);
            }
            else
            if(_encPassword.startsWith(PCrypt.PREFIX_ATLASSIAN))
            {
                return PCrypt.atlassian_pbkdf2_sha1(_givenPassword, _encPassword).equalsIgnoreCase(_encPassword);
            }
            else
            if(_encPassword.startsWith(PCrypt.PREFIX_RADIATOR))
            {
                return PCrypt.radiator_pbkdf2(_givenPassword, _encPassword).equalsIgnoreCase(_encPassword);
            }
            else
            if (_encPassword.startsWith("{CRYPT}"))
            {
                return matchPassword(_identity, _givenPassword, _tick, _encPassword.substring(7));
            }
            else
            if(_encPassword.startsWith(SCrypt.PREFIX_SCRYPT))
            {
                return SCrypt.check(_givenPassword, _encPassword);
            }
            else
            if(_encPassword.startsWith(Md5Crypt.MD5_PREFIX))
            {
                return Md5Crypt.md5Crypt(_givenPassword.getBytes(StandardCharsets.UTF_8), _encPassword).equalsIgnoreCase(_encPassword);
            }
            else
            if(_encPassword.startsWith(HOTP.PREFIX_TOTP)
                    || _encPassword.startsWith(HOTP.PREFIX_TOTP))
            {
                return HOTP.checkTotp(_givenPassword, _tick, _encPassword, 2);
            }
            else
            if(_encPassword.startsWith(BsdNtCrypt.BSDNT_PREFIX)
                    || _encPassword.startsWith(BsdNtCrypt.NT_PREFIX)
                    || _encPassword.startsWith(BsdNtCrypt.NT2_PREFIX)
                    || _encPassword.startsWith(BsdNtCrypt.MSNT_PREFIX))
            {
                return BsdNtCrypt.checkPassword(_givenPassword, _encPassword);
            }
            else
            if(_encPassword.startsWith(Rfs2307Crypt.PREFIX_MD5)
                    || _encPassword.startsWith(Rfs2307Crypt.PREFIX_SHA)
                    || _encPassword.startsWith(Rfs2307Crypt.PREFIX_SMD5)
                    || _encPassword.startsWith(Rfs2307Crypt.PREFIX_SSHA)
                    || _encPassword.startsWith(Rfs2307Crypt.PREFIX_HSHA)
                    || _encPassword.startsWith(Rfs2307Crypt.PREFIX_SSHA256)
                    || _encPassword.startsWith(Rfs2307Crypt.PREFIX_HSHA256))
            {
                return Rfs2307Crypt.checkPw(_givenPassword, _encPassword);
            }
            else
            if(_encPassword.startsWith(Sha1Crypt.SHA1_PREFIX))
            {
                return Sha1Crypt.checkpw(_givenPassword, _encPassword);
            }
            else
            if(_encPassword.startsWith(Sha1Crypt.SIMPLE_PREFIX))
            {
                return Sha1Crypt.simpleCheckpw(_givenPassword, _encPassword);
            }
            else
            if(_encPassword.startsWith(Sha2Crypt.SHA256_PREFIX))
            {
                return Sha2Crypt.sha256Crypt(_givenPassword.getBytes(StandardCharsets.UTF_8), _encPassword).equalsIgnoreCase(_encPassword);
            }
            else
            if(_encPassword.startsWith(Sha2Crypt.SHA512_PREFIX))
            {
                return Sha2Crypt.sha512Crypt(_givenPassword.getBytes(StandardCharsets.UTF_8), _encPassword).equalsIgnoreCase(_encPassword);
            }
            else
            if(_encPassword.startsWith(PCrypt.PREFIX_PBKDF2_256))
            {
                return PCrypt.crypt_pbkdf2_sha256(_givenPassword, _encPassword).equalsIgnoreCase(_encPassword);
            }
            else
            if(_encPassword.startsWith(PCrypt.PREFIX_PBKDF2_512))
            {
                return PCrypt.crypt_pbkdf2_sha512(_givenPassword, _encPassword).equalsIgnoreCase(_encPassword);
            }
            else
            if (_encPassword.startsWith("*") && _encPassword.trim().length() == 41)
            {
                return Mysql4Crypt.checkPassword(_givenPassword, _encPassword.trim());
            }
            else
            {
                return DesCrypt.crypt(_givenPassword, _encPassword).equals(_encPassword);
            }
        }
        catch(Exception _xe)
        {
        }
        return false;
    }

    public static final boolean matchPassword(String _givenPassword, String _encPassword)
    {
        return matchPassword("-nil-", _givenPassword, -1,_encPassword);
    }

    public static String hashPassword(String _givenPassword)
    {
        return PCrypt.crypt_pbkdf2_sha1(_givenPassword);
    }


}


