package com.github.terefang.jmelange.passwd.otp;

/**
 Copyright (c) 2011 IETF Trust and the persons identified as
 authors of the code. All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, is permitted pursuant to, and subject to the license
 terms contained in, the Simplified BSD License set forth in Section
 4.c of the IETF Trust's Legal Provisions Relating to IETF Documents
 (http://trustee.ietf.org/license-info).
 */

import com.github.terefang.jmelange.passwd.HashUtil;

import java.lang.reflect.UndeclaredThrowableException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.util.TimeZone;


/**
 * This is an example implementation of the OATH
 * HOTP algorithm.
 * Visit www.openauthentication.org for more information.
 *
 * @author Johan Rydell, PortWise, Inc.
 */

public class HOTP {

    private HOTP() {}

    public static byte[] hmac_sha(String crypto, byte[] keyBytes,
                                   byte[] text)
    {
        try
        {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey =
                    new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        }
        catch (GeneralSecurityException gse)
        {
            throw new UndeclaredThrowableException(gse);
        }
    }

    private static byte[] hexStr2Bytes(String hex)
    {
        // Adding one byte to get the right conversion
        // Values starting with "0" can be converted
        byte[] bArray = new BigInteger("10" + hex,16).toByteArray();

        // Copy all the REAL bytes, not the "first"
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length; i++)
            ret[i] = bArray[i+1];
        return ret;
    }

    private static final int[] DIGITS_POWER
            // 0 1  2   3    4     5      6       7        8
            = {1,10,100,1000,10000,100000,1000000,10000000,100000000 };

    public static String generateOTP(String key,
                                      String counter,
                                      String returnDigits){
        return generateOTP(key, counter, returnDigits, "HmacSHA1");
    }

    public static String generateOTP(String key,
                                      String counter,
                                      String returnDigits,
                                      String crypto)
    {
        int codeDigits = Integer.decode(returnDigits).intValue();
        byte[] k = hexStr2Bytes(key);
        return generateOTP(k, Long.parseLong(counter, 16), codeDigits, crypto);
    }

    public static String generateOTP(byte[] key,
                                      long counter,
                                      int codeDigits,
                                      String crypto)
    {
        String result = null;
        String time = Long.toHexString(counter);

        // Using the counter
        // First 8 bytes are for the movingFactor
        // Compliant with base RFC 4226 (HOTP)
        while (time.length() < 16 )
            time = "0" + time;

        // Get the HEX in a Byte[]
        byte[] msg = hexStr2Bytes(time);
        byte[] k = key;
        byte[] hash = hmac_sha(crypto, k, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary =
                ((hash[offset] & 0x7f) << 24) |
                        ((hash[offset + 1] & 0xff) << 16) |
                        ((hash[offset + 2] & 0xff) << 8) |
                        (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];

        result = Integer.toString(otp);
        while (result.length() < codeDigits)
        {
            result = "0" + result;
        }
        return result;
    }

    public static void main(String[] args)
    {
        // Seed for HMAC-SHA1 - 20 bytes
        String seed = "3132333435363738393031323334353637383930";
        // Seed for HMAC-SHA256 - 32 bytes
        String seed32 = "3132333435363738393031323334353637383930" +
                "313233343536373839303132";
        // Seed for HMAC-SHA512 - 64 bytes
        String seed64 = "3132333435363738393031323334353637383930" +
                "3132333435363738393031323334353637383930" +
                "3132333435363738393031323334353637383930" +
                "31323334";
        long T0 = 0;
        long X = 30;
        long testTime[] = {59L, 1111111109L, 1111111111L,
                1234567890L, 2000000000L, 20000000000L};

        String steps = "0";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        try
        {
            System.out.println(
                    "+---------------+-----------------------+" +
                            "------------------+--------+--------+");
            System.out.println(
                    "|  Time(sec)    |   Time (UTC format)   " +
                            "| Value of T(Hex)  |  HOTP  | Mode   |");
            System.out.println(
                    "+---------------+-----------------------+" +
                            "------------------+--------+--------+");

            for (int i=0; i<testTime.length; i++)
            {
                long T = (testTime[i] - T0)/X;
                steps = Long.toHexString(T).toUpperCase();
                while (steps.length() < 16) steps = "0" + steps;
                String fmtTime = String.format("%1$-11s", testTime[i]);
                String utcTime = df.format(new Date(testTime[i]*1000));
                System.out.print("|  " + fmtTime + "  |  " + utcTime +
                        "  | " + steps + " |");
                System.out.println(generateOTP(seed, steps, "8",
                        "HmacSHA1") + "| SHA1   |");
                System.out.print("|  " + fmtTime + "  |  " + utcTime +
                        "  | " + steps + " |");
                System.out.println(generateOTP(seed32, steps, "8",
                        "HmacSHA256") + "| SHA256 |");
                System.out.print("|  " + fmtTime + "  |  " + utcTime +
                        "  | " + steps + " |");
                System.out.println(generateOTP(seed64, steps, "8",
                        "HmacSHA512") + "| SHA512 |");

                System.out.println(
                        "+---------------+-----------------------+" +
                                "------------------+--------+--------+");
            }
        }
        catch (final Exception e)
        {
            System.out.println("Error : " + e);
        }
    }

    public static final String PREFIX_TOTP = "{TOTP}";
    public static final String PREFIX_GAUTH = "{gauth}";


    public static boolean checkOtp(String _given, long _tick, byte[] _key, String _algo, int _digits)
    {
        return generateOTP(_key, _tick, _digits, _algo).equalsIgnoreCase(_given);
    }

    public static boolean checkTotp(String _given, long _tick, String _encrypted, int _window)
    {
        String algorithm = "HmacSHA1";
        long period = 30000L;
        int digits = 6;
        byte[] secret = null;

        if(_encrypted.startsWith(PREFIX_TOTP))
        { // b64 encoded secret with rfc4226 params
            // = $a=HmacSHA1$p=30$c=b64$n=6
            secret = HashUtil.fromBase64(_encrypted.substring(6));
        }
        else if(_encrypted.startsWith(PREFIX_GAUTH))
        { // {gauth} SECRET $a=algo$p=period$c=codec$n=digits
            String codec = "b32";

            int off = 0;
            while((off = _encrypted.lastIndexOf('$')) > 0)
            {
                if(_encrypted.substring(off).startsWith("$a="))
                {
                    algorithm = _encrypted.substring(off+3);
                }
                else
                if(_encrypted.substring(off).startsWith("$p="))
                {
                    period = Long.parseLong(_encrypted.substring(off+3))*1000L;
                }
                else
                if(_encrypted.substring(off).startsWith("$c="))
                {
                    codec = _encrypted.substring(off+3);
                }
                else
                if(_encrypted.substring(off).startsWith("$n="))
                {
                    digits = Integer.parseInt(_encrypted.substring(off+3));
                }

                _encrypted=_encrypted.substring(0, off);
            }

            if(codec.equalsIgnoreCase("a32"))
            {
                secret = new BigInteger(_encrypted.substring(7), 32).toByteArray();
            }
            else
            if(codec.equalsIgnoreCase("a36"))
            {
                secret = new BigInteger(_encrypted.substring(7), 36).toByteArray();
            }
            else
            if(codec.equalsIgnoreCase("b32"))
            {
                secret = HashUtil.fromBase32(_encrypted.substring(7).toUpperCase());
            }
            else
            if(codec.equalsIgnoreCase("b64"))
            {
                secret = HashUtil.fromBase64(_encrypted.substring(7));
            }
            else
            if(codec.equalsIgnoreCase("b16") || codec.equalsIgnoreCase("hex"))
            {
                secret = HashUtil.fromHex(_encrypted.substring(7).toUpperCase());
            }
            else
            if(codec.equalsIgnoreCase("utf8"))
            {
                secret = _encrypted.substring(7).getBytes(StandardCharsets.UTF_8);
                secret = HashUtil.sha1(secret);
            }
            else
            if(codec.equalsIgnoreCase("ascii"))
            {
                secret = _encrypted.substring(7).getBytes(StandardCharsets.US_ASCII);
                secret = HashUtil.sha1(secret);
            }
            else
            {
                return false;
            }
        }

        if(_tick==-1L) _tick = System.currentTimeMillis();

        _tick = _tick / period;

        for (int i = -_window; i <= _window; ++i)
        {
            if (checkOtp(_given, _tick+i, secret,algorithm, digits))
            {
                return true;
            }
        }

        return false;
    }
}
