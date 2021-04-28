package com.github.terefang.jmelange.commons;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.Base64;
import org.codehaus.plexus.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.nio.channels.Channel;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CommonUtil
{
    public static void trace(String msg) {
        log.trace(msg);
    }

    public static void debug(String msg) {
        log.debug(msg);
    }

    public static void info(String msg) {
        log.info(msg);
    }

    public static void warn(String msg) {
        log.warn(msg);
    }

    public static void error(String msg) {
        log.error(msg);
    }

    public static boolean isNumber(final String str) {
        return isNumberCreatable(str);
    }

    public static boolean isNumberCreatable(final String str) {
        if (isEmpty(str)) {
            return false;
        }
        final char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        final int start = chars[0] == '-' || chars[0] == '+' ? 1 : 0;
        if (sz > start + 1 && chars[start] == '0' && !contains(str, '.')) { // leading 0, skip if is a decimal number
            if (chars[start + 1] == 'x' || chars[start + 1] == 'X') { // leading 0x/0X
                int i = start + 2;
                if (i == sz) {
                    return false; // str == "0x"
                }
                // checking hex (it can't be anything else)
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9')
                            && (chars[i] < 'a' || chars[i] > 'f')
                            && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
            }
            if (Character.isDigit(chars[start + 1])) {
                // leading 0, but not hex, must be octal
                int i = start + 1;
                for (; i < chars.length; i++) {
                    if (chars[i] < '0' || chars[i] > '7') {
                        return false;
                    }
                }
                return true;
            }
        }
        sz--; // don't want to loop to the last char, check it afterwords
        // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || i < sz + 1 && allowSigns && !foundDigit) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                // single trailing decimal point after non-exponent is ok
                return foundDigit;
            }
            if (!allowSigns
                    && (chars[i] == 'd'
                    || chars[i] == 'D'
                    || chars[i] == 'f'
                    || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l'
                    || chars[i] == 'L') {
                // not allowing L with an exponent or decimal point
                return foundDigit && !hasExp && !hasDecPoint;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return !allowSigns && foundDigit;
    }
    public static int checkInt(final Object _str)
    {
        if(_str==null)
        {
            return -1;
        }

        return checkInt(_str.toString());
    }

    public static int checkInt(String _str)
    {
        if(_str==null)
        {
            return -1;
        }
        _str = _str.trim();

        if(_str.length()==0) return 0;

        if(_str.startsWith("0x"))
        {
            return Integer.parseInt(_str.substring(2),16);
        }
        return Integer.parseInt(_str);
    }

    public static Integer checkInteger(final Object _str)
    {
        if(_str==null)
        {
            return -1;
        }

        return checkInteger(_str.toString());
    }

    public static Float checkFloat(Object _str)
    {
        if(_str==null)
        {
            return -1f;
        }
        return checkFloat(_str.toString());
    }

    public static Float checkFloat(String _str)
    {
        if(_str==null)
        {
            return -1f;
        }
        _str = _str.trim();

        if(_str.length()==0) return 0f;

        return Float.parseFloat(_str);
    }

    public static Double checkDouble(Object _str)
    {
        if(_str==null)
        {
            return -1d;
        }
        return checkDouble(_str.toString());
    }

    public static Double checkDouble(String _str)
    {
        if(_str==null)
        {
            return -1d;
        }
        _str = _str.trim();

        if(_str.length()==0) return 0d;

        return Double.parseDouble(_str);
    }

    public static Integer checkInteger(String _str)
    {
        if(_str==null)
        {
            return -1;
        }
        _str = _str.trim();

        if(_str.length()==0) return 0;

        if(_str.startsWith("0x"))
        {
            return Integer.parseInt(_str.substring(2),16);
        }
        return Integer.parseInt(_str);
    }

    public static Integer createInteger(String _str)
    {
        if(_str==null)
        {
            return -1;
        }
        _str = _str.trim();

        if(_str.length()==0) return 0;

        if(_str.startsWith("0x"))
        {
            return Integer.parseInt(_str.substring(2),16);
        }
        return Integer.parseInt(_str);
    }

    public static Long checkLong(final Object _str)
    {
        if(_str==null)
        {
            return -1L;
        }

        return checkLong(_str.toString());
    }

    public static Long checkLong(String _str)
    {
        if(_str==null)
        {
            return -1L;
        }
        _str = _str.trim();

        if(_str.length()==0) return 0L;

        if(_str.startsWith("0x"))
        {
            return Long.parseLong(_str.substring(2),16);
        }
        return Long.parseLong(_str);
    }

    public static String checkStringDefaultIfNullOrBlank(Object _str, String _def, String _blank)
    {
        if(_str==null)
        {
            return _def;
        }

        return checkStringDefaultIfNullOrBlank(_str.toString(), _def, _blank);
    }

    public static String checkStringDefaultIfNullOrBlank(String _str, String _def, String _blank)
    {
        if(_str==null)
        {
            return _def;
        }

        if(_str.trim().length()==0) return _blank;

        return _str;
    }

    public static String checkStringDefaultIfNull(Object _str, String _def)
    {
        return checkStringDefaultIfNullOrBlank(_str, _def, "(blank)");
    }

    public static String checkStringDefaultIfNull(String _str, String _def)
    {
        return checkStringDefaultIfNullOrBlank(_str, _def, "(blank)");
    }

    public static String checkStringDefaultIfBlank(Object _str, String _blank)
    {
        return checkStringDefaultIfNullOrBlank(_str,"(nil)", _blank);
    }

    public static String checkStringDefaultIfBlank(String _str, String _blank)
    {
        return checkStringDefaultIfNullOrBlank(_str,"(nil)", _blank);
    }

    public static String checkString(Object _str)
    {
        return checkStringDefaultIfNullOrBlank(_str, "(nil)", "(blank)");
    }

    public static String checkString(String _str)
    {
        return checkStringDefaultIfNullOrBlank(_str, "(nil)", "(blank)");
    }

    public static boolean checkBooleanDefaultIfNull(Object _bool, boolean _b)
    {
        if(_bool==null)
        {
            return _b;
        }
        return checkBoolean(_bool);
    }

    public static boolean checkBooleanDefaultIfNull(String _bool, boolean _b)
    {
        if(_bool==null)
        {
            return _b;
        }
        return checkBoolean(_bool);
    }

    public static Boolean checkBoolean(final Object _str)
    {
        if(_str==null)
        {
            return false;
        }

        return checkBoolean(_str.toString());
    }

    public static Boolean checkBoolean(String _str)
    {
        if (_str == null) {
            return false;
        }

        _str = _str.trim();
        // any defined string is "true" unless it is a false indicator:
        // false, f, off, none, no, n, null, nul, nil, 0, <blank>
        for(String _test : Arrays.asList("false", "f", "off", "none", "n", "no", "null", "nul", "nil", "0", ""))
        {
            if(_str.equalsIgnoreCase(_test))
            {
                return false;
            }
        }
        return true;
    }

    public static float toFloat(String str) {
        return Float.parseFloat(str);
    }

    public static double toDouble(String str) {
        return Double.parseDouble(str);
    }

    public static int toInt(String str) {
        return Integer.parseInt(str);
    }

    public static int toInteger(String str) {
        return checkInteger(str);
    }

    public static long toLong(String str) {
        return Long.parseLong(str);
    }

    public static String hashMacHex(String _name, String _key, String _buffer)
    {
        try {
            final SecretKeySpec _keySpec = new SecretKeySpec(_key.getBytes(StandardCharsets.UTF_8), _name);
            final Mac _mac = Mac.getInstance(_name);
            _mac.init(_keySpec);
            return toHex(_mac.doFinal(_buffer.getBytes(StandardCharsets.UTF_8)));
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return null;
        }
    }

    public static String sha512HMacHex(String _key, String _buffer)
    {
        return hashMacHex("HMacSHA512", _key, _buffer);
    }

    public static String sha256HMacHex(String _key, String _buffer)
    {
        return hashMacHex("HMacSHA256", _key, _buffer);
    }

    public static String sha1HMacHex(String _key, String _buffer)
    {
        return hashMacHex("HMacSHA1", _key, _buffer);
    }

    public static String md5HMacHex(String _key, String _buffer)
    {
        return hashMacHex("HMacMD5", _key, _buffer);
    }

    public static String sha384HMacHex(String _key, String _buffer)
    {
        return hashMacHex("HMacSHA384", _key, _buffer);
    }

    public static String hashHex(String _name, String _buffer)
    {
        try {
            MessageDigest _md = MessageDigest.getInstance(_name);
            return toHex(_md.digest(_buffer.getBytes(StandardCharsets.UTF_8)));
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String sha512Hex(String _name)
    {
        return hashHex("SHA-512", _name);
    }

    public static String sha256Hex(String _name)
    {
        return hashHex("SHA-256", _name);
    }

    public static String sha1Hex(String _name)
    {
        return hashHex("SHA-1", _name);
    }

    public static String md5Hex(String _name)
    {
        return hashHex("MD5", _name);
    }

    public static String sha384Hex(String _name)
    {
        return hashHex("SHA-384", _name);
    }

    static char[] HEXDIGITS = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    public static String toHex(Long _n)
    {
        return toHex(BigInteger.valueOf(_n).toByteArray());
    }

    public static String toHex(byte[] _buf)
    {
        char[] _out = new char[_buf.length*2];
        for(int _i=0; _i<_buf.length; _i++)
        {
            _out[_i*2] = HEXDIGITS[(_buf[_i]>>>4) & 0xf];
            _out[_i*2+1] = HEXDIGITS[_buf[_i] & 0xf];
        }
        return new String(_out);
    }

    public static String toHex(String _buf)
    {
        return toHex(_buf.getBytes());
    }

    public static String toBase64(Long _n)
    {
        return toBase64(BigInteger.valueOf(_n).toByteArray());
    }

    public static String toBase64(byte[] binaryData) {
        return new String(Base64.encodeBase64(binaryData));
    }

    public static String toBase64(String binaryData) {
        return new String(Base64.encodeBase64(binaryData.getBytes()));
    }


    static char[] B26DIGITS = {'Q','W','E','R','T','Z','U','I','O','P',
            'A','S','D','F','G','H','J','K','L',
            'Y','X','C','V','B','N','M'};
    static BigInteger B26L = BigInteger.valueOf(B26DIGITS.length);

    public static String toBase26(Long _n)
    {
        return toBase26(BigInteger.valueOf(_n).toByteArray());
    }

    public static String toBase26(byte[] binaryData)
    {
        StringBuffer _sb = new StringBuffer();
        BigInteger _bi = new BigInteger(1,binaryData);
        while(_bi.compareTo(BigInteger.ZERO) > 0)
        {
            _sb.append((char)B26DIGITS[(_bi.intValue() & 0x7fffffff)% B26DIGITS.length]);
            _bi = _bi.divide(B26L);
        }
        return _sb.toString();
    }

    public static String toBase26(String binaryData) {
        return toBase26(binaryData.getBytes());
    }


    static char[] B62DIGITS = {'Q','W','E','R','T','Z','U','I','O','P',
            'A','S','D','F','G','H','J','K','L',
            'Y','X','C','V','B','N','M',
            '0','1','2','3','4','5','6','7','8','9',
            'q','w','e','r','t','z','u','i','o','p',
            'a','s','d','f','g','h','j','k','l',
            'y','x','c','v','b','n','m'};
    static BigInteger B62L = BigInteger.valueOf(B62DIGITS.length);

    public static String toBase62(Long _n)
    {
        return toBase62(BigInteger.valueOf(_n).toByteArray());
    }

    public static String toBase62(byte[] binaryData)
    {
        StringBuffer _sb = new StringBuffer();
        BigInteger _bi = new BigInteger(1,binaryData);
        while(_bi.compareTo(BigInteger.ZERO) > 0)
        {
            _sb.append((char)B62DIGITS[(_bi.intValue() & 0x7fffffff)% B62DIGITS.length]);
            _bi = _bi.divide(B62L);
        }
        return _sb.toString();
    }

    public static String toBase62(String binaryData) {
        return toBase62(binaryData.getBytes());
    }


    public static String toBase36(Long _n)
    {
        return Long.toString(_n,36);
    }

    public static String toBase36(byte[] binaryData)
    {
        return new BigInteger(1,binaryData).toString(36);
    }

    public static String toBase36(String binaryData) {
        return toBase36(binaryData.getBytes());
    }


    public static String toGuid(String data)
    {
        return GuidUtil.toGUID(data);
    }

    // netcool like inspired by probe functions

    @SneakyThrows
    public static Date dateToTime(String _format, String _text)
    {
        SimpleDateFormat _sdf = new SimpleDateFormat(_format);
        return _sdf.parse(_text);
    }

    @SneakyThrows
    public static Long dateToLong(String _format, String _text)
    {
        Date _d = dateToTime(_format, _text);
        return (_d==null) ? null : _d.getTime();
    }

    public static Date getDate()
    {
        return new Date();
    }

    public static Long getDateLong()
    {
        return new Date().getTime();
    }

    public static String timeToDate(String _format, Date _time)
    {
        SimpleDateFormat _sdf = new SimpleDateFormat(_format);
        return _sdf.format(_time);
    }

    public static String timeToDate(String _format, long _time)
    {
        SimpleDateFormat _sdf = new SimpleDateFormat(_format);
        return _sdf.format(new Date(_time));
    }

    public static String toBase(int _base, long _n)
    {
        return Long.toString(_n, _base);
    }

    public static String toBase(int _base, long _n, int _min)
    {
        String _ret = Long.toString(_n, _base);
        while(_ret.length()<_min)
        {
            _ret="0"+_ret;
        }
        return _ret;
    }

    public static String decimalToAscii(String _text)
    {
        String[] _parts = split(_text);
        StringBuilder _sb = new StringBuilder();
        for(String _part : _parts)
        {
            _sb.append((char)toInt(_part));
        }
        return _sb.toString();
    }

    public static String decimalToHex(String _text)
    {
        String[] _parts = split(_text);
        for(int _i=0; _i<_parts.length; _i++)
        {
            _parts[_i] = toBase(16,toInt(_parts[_i]));
        }
        return join(_parts, "");
    }

    public static String decimalToHex(String _text, String _sep)
    {
        String[] _parts = split(_text, _sep);
        for(int _i=0; _i<_parts.length; _i++)
        {
            _parts[_i] = toBase(16,toInt(_parts[_i]));
        }
        return join(_parts, "");
    }

    public static String decimalToHex(String _text, String _sep, String _delim)
    {
        String[] _parts = split(_text, _sep);
        for(int _i=0; _i<_parts.length; _i++)
        {
            _parts[_i] = toBase(16,toInt(_parts[_i]));
        }
        return join(_parts, _delim);
    }

    public static long ipToLong(String _addr)
    {
        String[] _parts = split(_addr, ".", 4);
        long _ret = 0L;
        try
        {
            for(String _p : _parts)
            {
                _ret = (_ret<<8) | (Integer.parseInt(_p) & 0xff);
            }
        }
        catch(Exception _xe)
        {
            return -1L;
        }
        return _ret;
    }

    public static String longToIp(long _addr)
    {
        return Long.toString((_addr >>> 24) & 0xff)
                + "." + Long.toString((_addr >>> 16) & 0xff)
                + "." + Long.toString((_addr >>> 8) & 0xff)
                + "." + Long.toString((_addr) & 0xff);
    }

    public static String ipToHex(String _addr)
    {
        return ipToHex(ipToLong(_addr));
    }

    public static String ipToHex(long _addr)
    {
        return toHex(new byte[]{(byte) ((_addr >>> 24) & 0xff),
                (byte) ((_addr >>> 16) & 0xff),
                (byte) ((_addr >>> 8) & 0xff),
                (byte) ((_addr) & 0xff)}).toUpperCase();
    }

    public static String hexToIp(String _hex)
    {
        return Long.toString((Integer.parseInt(_hex.substring(0,2),16)) & 0xff)
                + "." + Long.toString((Integer.parseInt(_hex.substring(2,4),16)) & 0xff)
                + "." + Long.toString((Integer.parseInt(_hex.substring(4,6),16)) & 0xff)
                + "." + Long.toString((Integer.parseInt(_hex.substring(6,8),16)) & 0xff);
    }

    public static String extract(String s, String rx)
    {
        Pattern p = Pattern.compile(rx);
        Matcher m = p.matcher(s);
        if(m.find())
        {
            return m.group(1);
        }
        return "";
    }

    public static String[] extractN(String s, String rx)
    {
        Pattern p = Pattern.compile(rx);
        Matcher m = p.matcher(s);
        if(m.find())
        {
            String[] sa = new String[m.groupCount()];
            for(int i=0; i< sa.length; i++)
            {
                sa[i]=m.group(i+1);
            }
            return sa;
        }
        return new String[]{};
    }

    public static String formatMsg(String _fmt, Object... _params)
    {
        return MessageFormat.format(_fmt, _params);
    }

    public static String format(String _fmt, Object... _params)
    {
        return String.format(_fmt, _params);
    }

    public static boolean fnmatch(String a, String fx)
    {
        return wcmatch(fx, a);
    }

    public static boolean wcmatch(String expr, String value)
    {
        if(value==null)
            return false;

        ArrayList<String> p = new ArrayList();

        switch(wildcard_substring(expr.toLowerCase(),p))
        {
            case -1: // presense
                return (value.length() > 0);

            case 1: // wcmatch
                return wildcard_check((String[])p.toArray(new String[] {}), value.toLowerCase());

            case 0: // simple
            default:
                return expr.equalsIgnoreCase(value);
        }
    }

    private static boolean wildcard_check(String[] pieces, String s)
    {
        // Walk the pieces to match the string
        // There are implicit stars between each piece,
        // and the first and last pieces might be "" to anchor the match.
        // assert (pieces.length > 1)
        // minimal case is <string>*<string>

        boolean result = false;
        int len = pieces.length;

        int index = 0;
        for (int i = 0; i < len; i++)
        {
            String piece = (String) pieces[i];

            if (i == len - 1)
            {
                // this is the last piece
                if (s.endsWith(piece))
                {
                    result = true;
                }
                else
                {
                    result = false;
                }
                break;
            }
            // initial non-star; assert index == 0
            else if (i == 0)
            {
                if (!s.startsWith(piece))
                {
                    result = false;
                    break;
                }
            }
            // assert i > 0 && i < len-1
            else
            {
                // Sure wish stringbuffer supported e.g. indexOf
                index = s.indexOf(piece, index);
                if (index < 0)
                {
                    result = false;
                    break;
                }
            }
            // start beyond the matching piece
            index += piece.length();
        }

        return result;
    }

    private static int wildcard_substring(String wcstring, ArrayList pieces)
    {
        pieces.clear();
        StringBuffer ss = new StringBuffer();
        boolean wasStar = false; // indicates last piece was a star
        boolean leftstar = false; // track if the initial piece is a star
        boolean rightstar = false; // track if the final piece is a star

        char[] wcs = wcstring.toCharArray();
        int wco=0;
        // We assume (sub)strings can contain leading and trailing blanks
        for (;wco<wcs.length;)
        {
            int c = wcs[wco];
            switch (c)
            {
                case '\\' :
                    wasStar = false;
                    wco++;
                    if(wco<wcs.length)
                    {
                        c = wcs[wco]; wco++;
                        switch(c)
                        {
                            case 'n':
                                ss.append((char) 10);
                                break;
                            case 'r':
                                ss.append((char) 13);
                                break;
                            case 't':
                                ss.append((char) 9);
                                break;
                            default:
                                ss.append((char) c);
                                break;
                        }
                    }
                    break;
                case '*' :
                    if (wasStar)
                    {
                        // encountered two successive stars;
                        // I assume this is illegal but permissive
                        wco++;
                        break;
                    }
                    wco++;
                    if (ss.length() > 0)
                    {
                        pieces.add(ss.toString()); // accumulate the pieces
                        // between '*' occurrences
                    }
                    ss.setLength(0);
                    // if this is a leading star, then track it
                    if (pieces.size() == 0)
                    {
                        leftstar = true;
                    }
                    ss.setLength(0);
                    wasStar = true;
                    break;
                default :
                    wasStar = false;
                    c = wcs[wco]; wco++;
                    ss.append((char) c);
            }
        }

        if (ss.length() > 0)
        {
            pieces.add(ss.toString()); // accumulate the pieces
            ss.setLength(0);
        }

        if (pieces.size() == 0)
        {
            return -1; // presense
        }
        if (leftstar || rightstar || pieces.size() > 1)
        {
            // insert leading and/or trailing "" to anchor ends
            if (rightstar)
            {
                pieces.add("");
            }
            if (leftstar)
            {
                pieces.add(0, "");
            }
            return 1; // wcmatch
        }
        // assert !leftstar && !rightstar && pieces.size == 1
        return 0; // simple
    }

    public static <T> T[] toArray(T... _args)
    {
        return _args;
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3, Object _a4, Object _a5, Object _a6)
    {
        return new Object[]{_a1, _a2, _a3, _a4, _a5, _a6};
    }

    public static String[] toArray(String _a1, String _a2, String _a3, String _a4, String _a5, String _a6)
    {
        return new String[]{_a1, _a2, _a3, _a4, _a5, _a6};
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3, Object _a4, Object _a5)
    {
        return new Object[]{_a1, _a2, _a3, _a4, _a5};
    }

    public static String[] toArray(String _a1, String _a2, String _a3, String _a4, String _a5)
    {
        return new String[]{_a1, _a2, _a3, _a4, _a5};
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3, Object _a4)
    {
        return new Object[]{_a1, _a2, _a3, _a4};
    }

    public static String[] toArray(String _a1, String _a2, String _a3, String _a4)
    {
        return new String[]{_a1, _a2, _a3, _a4};
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3)
    {
        return new Object[]{_a1, _a2, _a3};
    }

    public static String[] toArray(String _a1, String _a2, String _a3)
    {
        return new String[]{_a1, _a2, _a3};
    }

    public static Object[] toArray(Object _a1, Object _a2)
    {
        return new Object[]{_a1, _a2};
    }

    public static String[] toArray(String _a1, String _a2)
    {
        return new String[]{_a1, _a2};
    }

    public static Object[] toArray(Object _a1)
    {
        return new Object[]{_a1};
    }

    public static String[] toArray(String _a1)
    {
        return new String[]{_a1};
    }

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3, String _k4, Object _v4, String _k5, Object _v5, String _k6, Object _v6)
    {
        Map<String, Object> _ret = new HashMap();
        _ret.put(_k1, _v1);
        _ret.put(_k2, _v2);
        _ret.put(_k3, _v3);
        _ret.put(_k4, _v4);
        _ret.put(_k5, _v5);
        _ret.put(_k6, _v6);
        return _ret;
    }

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3, String _k4, Object _v4, String _k5, Object _v5)
    {
        Map<String, Object> _ret = new HashMap();
        _ret.put(_k1, _v1);
        _ret.put(_k2, _v2);
        _ret.put(_k3, _v3);
        _ret.put(_k4, _v4);
        _ret.put(_k5, _v5);
        return _ret;
    }

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3, String _k4, Object _v4)
    {
        Map<String, Object> _ret = new HashMap();
        _ret.put(_k1, _v1);
        _ret.put(_k2, _v2);
        _ret.put(_k3, _v3);
        _ret.put(_k4, _v4);
        return _ret;
    }

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3)
    {
        Map<String, Object> _ret = new HashMap();
        _ret.put(_k1, _v1);
        _ret.put(_k2, _v2);
        _ret.put(_k3, _v3);
        return _ret;
    }

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2)
    {
        Map<String, Object> _ret = new HashMap();
        _ret.put(_k1, _v1);
        _ret.put(_k2, _v2);
        return _ret;
    }

    public static Map<String, Object> toMap(String _k, Object _v)
    {
        return toMap((Object)_k, _v);
    }

    public static Map<String, Object> toMap(String... _args)
    {
        return toMap((Object[])_args);
    }

    public static Map<String, Object> toMap(Object... _args)
    {
        Map<String, Object> _ret = new HashMap();
        for(int _i = 0; (_i+1)<_args.length; _i+=2)
        {
            _ret.put(_args[_i].toString(), _args[_i+1]);
        }
        return _ret;
    }

    public static List<String> toList(String... _args)
    {
        return Arrays.asList(_args);
    }

    public static List<String> toList(String _a1, String _a2, String _a3, String _a4, String _a5, String _a6)
    {
        return Arrays.asList(_a1, _a2, _a3, _a4, _a5, _a6);
    }

    public static List<String> toList(String _a1, String _a2, String _a3, String _a4, String _a5)
    {
        return Arrays.asList(_a1, _a2, _a3, _a4, _a5);
    }

    public static List<String> toList(String _a1, String _a2, String _a3, String _a4)
    {
        return Arrays.asList(_a1, _a2, _a3, _a4);
    }

    public static List<String> toList(String _a1, String _a2, String _a3)
    {
        return Arrays.asList(_a1, _a2, _a3);
    }

    public static List<String> toList(String _a1, String _a2)
    {
        return Arrays.asList(_a1, _a2);
    }

    public static <T> List<T> toList(T... _args)
    {
        return Arrays.asList(_args);
    }

    public static List<Object> toList(Object _a1, Object _a2, Object _a3, Object _a4, Object _a5, Object _a6)
    {
        return Arrays.asList(_a1, _a2, _a3, _a4, _a5, _a6);
    }

    public static List<Object> toList(Object _a1, Object _a2, Object _a3, Object _a4, Object _a5)
    {
        return Arrays.asList(_a1, _a2, _a3, _a4, _a5);
    }

    public static List<Object> toList(Object _a1, Object _a2, Object _a3, Object _a4)
    {
        return Arrays.asList(_a1, _a2, _a3, _a4);
    }

    public static List<Object> toList(Object _a1, Object _a2, Object _a3)
    {
        return Arrays.asList(_a1, _a2, _a3);
    }

    public static List<Object> toList(Object _a1, Object _a2)
    {
        return Arrays.asList(_a1, _a2);
    }

    public static List newList()
    {
        return new Vector();
    }

    public static Map newMap()
    {
        return new HashMap();
    }

    public static Map newLMap()
    {
        return new LinkedHashMap();
    }

    public static String toString(Object _o)
    {
        return Objects.toString(_o);
    }

    // StringUtils

    public static String stringClean(String str) {
        return StringUtils.clean(str);
    }

    public static String trim(String str) {
        return StringUtils.trim(str);
    }

    public static String deleteWhitespace(String str) {
        return StringUtils.deleteWhitespace(str);
    }

    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    public static boolean stringEquals(String str1, String str2) {
        return StringUtils.equals(str1, str2);
    }

    public static boolean stringEqualsIgnoreCase(String str1, String str2) {
        return StringUtils.equalsIgnoreCase(str1, str2);
    }

    public static int indexOfAny(String str, String[] searchStrs) {
        return StringUtils.indexOfAny(str, searchStrs);
    }

    public static int lastIndexOfAny(String str, String[] searchStrs) {
        return StringUtils.lastIndexOfAny(str, searchStrs);
    }

    public static String substring(String str, int start) {
        return StringUtils.substring(str, start);
    }

    public static String substring(String str, int start, int end) {
        return StringUtils.substring(str, start, end);
    }

    public static String left(String str, int len) {
        return StringUtils.left(str, len);
    }

    public static String right(String str, int len) {
        return StringUtils.right(str, len);
    }

    public static String mid(String str, int pos, int len) {
        return StringUtils.mid(str, pos, len);
    }

    public static String[] split(String str) {
        return StringUtils.split(str);
    }

    public static String[] split(String text, String separator) {
        return StringUtils.split(text, separator);
    }

    public static String[] split(String str, String separator, int max) {
        return StringUtils.split(str, separator, max);
    }

    public static String concat(Object[] array) {
        return StringUtils.concatenate(array);
    }

    public static String concatenate(Object[] array) {
        return StringUtils.concatenate(array);
    }

    public static String join(Object[] array, String separator) {
        return StringUtils.join(array, separator);
    }

    public static String join(Iterator<?> iterator, String separator) {
        return StringUtils.join(iterator, separator);
    }

    public static String join(List<?> list, String separator) {
        return StringUtils.join(list.listIterator(), separator);
    }

    public static String join(Collection<?> col, String separator) {
        return StringUtils.join(col.toArray(), separator);
    }

    public static String join(Set<?> set, String separator) {
        return StringUtils.join(set.toArray(), separator);
    }

    public static String replaceOnce(String text, char repl, char with) {
        return StringUtils.replaceOnce(text, repl, with);
    }

    public static String replace(String text, char repl, char with) {
        return StringUtils.replace(text, repl, with);
    }

    public static String replace(String text, char repl, char with, int max) {
        return StringUtils.replace(text, repl, with, max);
    }

    public static String replaceOnce(String text, String repl, String with) {
        return StringUtils.replaceOnce(text, repl, with);
    }

    public static String replace(String text, String repl, String with) {
        return StringUtils.replace(text, repl, with);
    }

    public static String replace(String text, String repl, String with, int max) {
        return StringUtils.replace(text, repl, with, max);
    }

    public static String overlayString(String text, String overlay, int start, int end) {
        return StringUtils.overlayString(text, overlay, start, end);
    }

    public static String center(String str, int size) {
        return StringUtils.center(str, size);
    }

    public static String center(String str, int size, String delim) {
        return StringUtils.center(str, size, delim);
    }

    public static String chomp(String str) {
        return StringUtils.chomp(str);
    }

    public static String chomp(String str, String sep) {
        return StringUtils.chomp(str, sep);
    }

    public static String chompLast(String str) {
        return StringUtils.chompLast(str);
    }

    public static String chompLast(String str, String sep) {
        return StringUtils.chompLast(str, sep);
    }

    public static String getChomp(String str, String sep) {
        return StringUtils.getChomp(str, sep);
    }

    public static String prechomp(String str, String sep) {
        return StringUtils.prechomp(str, sep);
    }

    public static String getPrechomp(String str, String sep) {
        return StringUtils.getPrechomp(str, sep);
    }

    public static String chop(String str) {
        return StringUtils.chop(str);
    }

    public static String chopNewline(String str) {
        return StringUtils.chopNewline(str);
    }

    public static String escape(String str) {
        return StringUtils.escape(str);
    }

    public static String repeat(String str, int repeat) {
        return StringUtils.repeat(str, repeat);
    }

    public static String rightPad(String str, int size) {
        return StringUtils.rightPad(str, size);
    }

    public static String rightPad(String str, int size, String delim) {
        return StringUtils.rightPad(str, size, delim);
    }

    public static String leftPad(String str, int size) {
        return StringUtils.leftPad(str, size);
    }

    public static String leftPad(String str, int size, String delim) {
        return StringUtils.leftPad(str, size, delim);
    }

    public static String strip(String str) {
        return StringUtils.strip(str);
    }

    public static String strip(String str, String delim) {
        return StringUtils.strip(str, delim);
    }

    public static String[] stripAll(String[] strs) {
        return StringUtils.stripAll(strs);
    }

    public static String[] stripAll(String[] strs, String delimiter) {
        return StringUtils.stripAll(strs, delimiter);
    }

    public static String stripEnd(String str, String strip) {
        return StringUtils.stripEnd(str, strip);
    }

    public static String stripStart(String str, String strip) {
        return StringUtils.stripStart(str, strip);
    }

    public static String upperCase(String str) {
        return StringUtils.upperCase(str);
    }

    public static String lowerCase(String str) {
        return StringUtils.lowerCase(str);
    }

    public static String uncapitalise(String str) {
        return StringUtils.uncapitalise(str);
    }

    public static String capitalise(String str) {
        return StringUtils.capitalise(str);
    }

    public static String swapCase(String str) {
        return StringUtils.swapCase(str);
    }

    public static String capitaliseAllWords(String str) {
        return StringUtils.capitaliseAllWords(str);
    }

    public static String uncapitaliseAllWords(String str) {
        return StringUtils.uncapitaliseAllWords(str);
    }

    public static String getNestedString(String str, String tag) {
        return StringUtils.getNestedString(str, tag);
    }

    public static String getNestedString(String str, String open, String close) {
        return StringUtils.getNestedString(str, open, close);
    }

    public static int countMatches(String str, String sub) {
        return StringUtils.countMatches(str, sub);
    }

    public static boolean isAlpha(String str) {
        return StringUtils.isAlpha(str);
    }

    public static boolean isWhitespace(String str) {
        return StringUtils.isWhitespace(str);
    }

    public static boolean isAlphaSpace(String str) {
        return StringUtils.isAlphaSpace(str);
    }

    public static boolean isAlphanumeric(String str) {
        return StringUtils.isAlphanumeric(str);
    }

    public static boolean isAlphanumericSpace(String str) {
        return StringUtils.isAlphanumericSpace(str);
    }

    public static boolean isNumeric(String str) {
        return StringUtils.isNumeric(str);
    }

    public static boolean isNumericSpace(String str) {
        return StringUtils.isNumericSpace(str);
    }

    public static String defaultString(Object obj) {
        return StringUtils.defaultString(obj);
    }

    public static String defaultString(Object obj, String defaultString) {
        return StringUtils.defaultString(obj, defaultString);
    }

    public static String reverse(String str) {
        return StringUtils.reverse(str);
    }

    public static String reverseDelimitedString(String str, String delimiter) {
        return StringUtils.reverseDelimitedString(str, delimiter);
    }

    public static String abbreviate(String s, int maxWidth) {
        return StringUtils.abbreviate(s, maxWidth);
    }

    public static String abbreviate(String s, int offset, int maxWidth) {
        return StringUtils.abbreviate(s, offset, maxWidth);
    }

    public static String difference(String s1, String s2) {
        return StringUtils.difference(s1, s2);
    }

    public static int differenceAt(String s1, String s2) {
        return StringUtils.differenceAt(s1, s2);
    }

    public static String interpolate(String text, Map<?, ?> namespace) {
        return StringUtils.interpolate(text, namespace);
    }

    public static String removeAndHump(String data, String replaceThis) {
        return StringUtils.removeAndHump(data, replaceThis);
    }

    public static String capitalizeFirstLetter(String data) {
        return StringUtils.capitalizeFirstLetter(data);
    }

    public static String lowercaseFirstLetter(String data) {
        return StringUtils.lowercaseFirstLetter(data);
    }

    public static String addAndDeHump(String view) {
        return StringUtils.addAndDeHump(view);
    }

    public static String quoteAndEscape(String source, char quoteChar) {
        return StringUtils.quoteAndEscape(source, quoteChar);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] quotingTriggers) {
        return StringUtils.quoteAndEscape(source, quoteChar, quotingTriggers);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] escapedChars, char escapeChar, boolean force) {
        return StringUtils.quoteAndEscape(source, quoteChar, escapedChars, escapeChar, force);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] escapedChars, char[] quotingTriggers, char escapeChar, boolean force) {
        return StringUtils.quoteAndEscape(source, quoteChar, escapedChars, quotingTriggers, escapeChar, force);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] escapedChars, char[] quotingTriggers, String escapePattern, boolean force) {
        return StringUtils.quoteAndEscape(source, quoteChar, escapedChars, quotingTriggers, escapePattern, force);
    }

    public static String escape(String source, char[] escapedChars, char escapeChar) {
        return StringUtils.escape(source, escapedChars, escapeChar);
    }

    public static String escape(String source, char[] escapedChars, String escapePattern) {
        return StringUtils.escape(source, escapedChars, escapePattern);
    }

    public static String removeDuplicateWhitespace(String s) {
        return StringUtils.removeDuplicateWhitespace(s);
    }

    public static String unifyLineSeparators(String s) {
        return StringUtils.unifyLineSeparators(s);
    }

    public static String unifyLineSeparators(String s, String ls) {
        return StringUtils.unifyLineSeparators(s, ls);
    }

    public static boolean contains(String str, char searchChar) {
        return StringUtils.contains(str, searchChar);
    }

    public static boolean contains(String str, String searchStr) {
        return StringUtils.contains(str, searchStr);
    }

    /*
     * Behold, intrepid explorers; a map of this class:
     *
     *       Method      Input               Output          Dependency
     *       ------      -----               ------          -------
     * 1     copy        InputStream         OutputStream    (primitive)
     * 2     copy        Reader              Writer          (primitive)
     *
     * 3     copy        InputStream         Writer          2
     * 4     toString    InputStream         String          3
     * 5     toByteArray InputStream         byte[]          1
     *
     * 6     copy        Reader              OutputStream    2
     * 7     toString    Reader              String          2
     * 8     toByteArray Reader              byte[]          6
     *
     * 9     copy        String              OutputStream    2
     * 10    copy        String              Writer          (trivial)
     * 11    toByteArray String              byte[]          9
     *
     * 12    copy        byte[]              Writer          3
     * 13    toString    byte[]              String          12
     * 14    copy        byte[]              OutputStream    (trivial)
     *
     *
     * Note that only the first two methods shuffle bytes; the rest use these two, or (if possible) copy
     * using native Java copy methods. As there are method variants to specify buffer size and encoding,
     * each row may correspond to up to 4 methods.
     *
     */
    ///////////////////////////////////////////////////////////////
    // Core copy methods
    ///////////////////////////////////////////////////////////////
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 16;

    /**
     * Copy bytes from an <code>InputStream</code> to an <code>OutputStream</code>.
     */
    public static void copy( final InputStream input, final OutputStream output )
            throws IOException
    {
        copy( input, output, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Copy bytes from an <code>InputStream</code> to an <code>OutputStream</code>.
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final InputStream input,
                             final OutputStream output,
                             final int bufferSize )
            throws IOException
    {
        final byte[] buffer = new byte[bufferSize];
        int n = 0;
        while ( -1 != ( n = input.read( buffer ) ) )
        {
            output.write( buffer, 0, n );
        }
    }

    /**
     * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
     */
    public static void copy( final Reader input, final Writer output )
            throws IOException
    {
        copy( input, output, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final Reader input, final Writer output, final int bufferSize )
            throws IOException
    {
        final char[] buffer = new char[bufferSize];
        int n = 0;
        while ( -1 != ( n = input.read( buffer ) ) )
        {
            output.write( buffer, 0, n );
        }
        output.flush();
    }

    ///////////////////////////////////////////////////////////////
    // Derived copy methods
    // InputStream -> *
    ///////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    // InputStream -> Writer

    /**
     * Copy and convert bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code>.
     * The platform's default encoding is used for the byte-to-char conversion.
     */
    public static void copy( final InputStream input, final Writer output )
            throws IOException
    {
        copy( input, output, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Copy and convert bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code>.
     * The platform's default encoding is used for the byte-to-char conversion.
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final InputStream input, final Writer output, final int bufferSize )
            throws IOException
    {
        final InputStreamReader in = new InputStreamReader( input );
        copy( in, output, bufferSize );
    }

    /**
     * Copy and convert bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code>, using the specified encoding.
     * @param encoding The name of a supported character encoding. See the
     * <a href="http://www.iana.org/assignments/character-sets">IANA
     * Charset Registry</a> for a list of valid encoding types.
     */
    public static void copy( final InputStream input, final Writer output, final String encoding )
            throws IOException
    {
        final InputStreamReader in = new InputStreamReader( input, encoding );
        copy( in, output );
    }

    /**
     * Copy and convert bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code>, using the specified encoding.
     * @param encoding The name of a supported character encoding. See the
     *        <a href="http://www.iana.org/assignments/character-sets">IANA
     *        Charset Registry</a> for a list of valid encoding types.
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final InputStream input,
                             final Writer output,
                             final String encoding,
                             final int bufferSize )
            throws IOException
    {
        final InputStreamReader in = new InputStreamReader( input, encoding );
        copy( in, output, bufferSize );
    }


    ///////////////////////////////////////////////////////////////
    // InputStream -> String

    /**
     * Get the contents of an <code>InputStream</code> as a String.
     * The platform's default encoding is used for the byte-to-char conversion.
     */
    public static String toString( final InputStream input )
            throws IOException
    {
        return toString( input, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of an <code>InputStream</code> as a String.
     * The platform's default encoding is used for the byte-to-char conversion.
     * @param bufferSize Size of internal buffer to use.
     */
    public static String toString( final InputStream input, final int bufferSize )
            throws IOException
    {
        final StringWriter sw = new StringWriter();
        copy( input, sw, bufferSize );
        return sw.toString();
    }

    /**
     * Get the contents of an <code>InputStream</code> as a String.
     * @param encoding The name of a supported character encoding. See the
     *    <a href="http://www.iana.org/assignments/character-sets">IANA
     *    Charset Registry</a> for a list of valid encoding types.
     */
    public static String toString( final InputStream input, final String encoding )
            throws IOException
    {
        return toString( input, encoding, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of an <code>InputStream</code> as a String.
     * @param encoding The name of a supported character encoding. See the
     *   <a href="http://www.iana.org/assignments/character-sets">IANA
     *   Charset Registry</a> for a list of valid encoding types.
     * @param bufferSize Size of internal buffer to use.
     */
    public static String toString( final InputStream input,
                                   final String encoding,
                                   final int bufferSize )
            throws IOException
    {
        final StringWriter sw = new StringWriter();
        copy( input, sw, encoding, bufferSize );
        return sw.toString();
    }

    ///////////////////////////////////////////////////////////////
    // InputStream -> byte[]

    /**
     * Get the contents of an <code>InputStream</code> as a <code>byte[]</code>.
     */
    public static byte[] toByteArray( final InputStream input )
            throws IOException
    {
        return toByteArray( input, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of an <code>InputStream</code> as a <code>byte[]</code>.
     * @param bufferSize Size of internal buffer to use.
     */
    public static byte[] toByteArray( final InputStream input, final int bufferSize )
            throws IOException
    {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy( input, output, bufferSize );
        return output.toByteArray();
    }


    ///////////////////////////////////////////////////////////////
    // Derived copy methods
    // Reader -> *
    ///////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    // Reader -> OutputStream
    /**
     * Serialize chars from a <code>Reader</code> to bytes on an <code>OutputStream</code>, and
     * flush the <code>OutputStream</code>.
     */
    public static void copy( final Reader input, final OutputStream output )
            throws IOException
    {
        copy( input, output, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Serialize chars from a <code>Reader</code> to bytes on an <code>OutputStream</code>, and
     * flush the <code>OutputStream</code>.
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final Reader input, final OutputStream output, final int bufferSize )
            throws IOException
    {
        final OutputStreamWriter out = new OutputStreamWriter( output );
        copy( input, out, bufferSize );
        // NOTE: Unless anyone is planning on rewriting OutputStreamWriter, we have to flush
        // here.
        out.flush();
    }

    ///////////////////////////////////////////////////////////////
    // Reader -> String
    /**
     * Get the contents of a <code>Reader</code> as a String.
     */
    public static String toString( final Reader input )
            throws IOException
    {
        return toString( input, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of a <code>Reader</code> as a String.
     * @param bufferSize Size of internal buffer to use.
     */
    public static String toString( final Reader input, final int bufferSize )
            throws IOException
    {
        final StringWriter sw = new StringWriter();
        copy( input, sw, bufferSize );
        return sw.toString();
    }


    ///////////////////////////////////////////////////////////////
    // Reader -> byte[]
    /**
     * Get the contents of a <code>Reader</code> as a <code>byte[]</code>.
     */
    public static byte[] toByteArray( final Reader input )
            throws IOException
    {
        return toByteArray( input, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of a <code>Reader</code> as a <code>byte[]</code>.
     * @param bufferSize Size of internal buffer to use.
     */
    public static byte[] toByteArray( final Reader input, final int bufferSize )
            throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy( input, output, bufferSize );
        return output.toByteArray();
    }


    ///////////////////////////////////////////////////////////////
    // Derived copy methods
    // String -> *
    ///////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    // String -> OutputStream

    /**
     * Serialize chars from a <code>String</code> to bytes on an <code>OutputStream</code>, and
     * flush the <code>OutputStream</code>.
     */
    public static void copy( final String input, final OutputStream output )
            throws IOException
    {
        copy( input, output, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Serialize chars from a <code>String</code> to bytes on an <code>OutputStream</code>, and
     * flush the <code>OutputStream</code>.
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final String input, final OutputStream output, final int bufferSize )
            throws IOException
    {
        final StringReader in = new StringReader( input );
        final OutputStreamWriter out = new OutputStreamWriter( output );
        copy( in, out, bufferSize );
        // NOTE: Unless anyone is planning on rewriting OutputStreamWriter, we have to flush
        // here.
        out.flush();
    }



    ///////////////////////////////////////////////////////////////
    // String -> Writer

    /**
     * Copy chars from a <code>String</code> to a <code>Writer</code>.
     */
    public static void copy( final String input, final Writer output )
            throws IOException
    {
        output.write( input );
    }

    /**
     * Copy bytes from an <code>InputStream</code> to an
     * <code>OutputStream</code>, with buffering.
     * This is equivalent to passing a
     * {@link BufferedInputStream} and
     * {@link BufferedOutputStream} to {@link #copy(InputStream, OutputStream)},
     * and flushing the output stream afterwards. The streams are not closed
     * after the copy.
     * @deprecated Buffering streams is actively harmful! See the class description as to why. Use
     * {@link #copy(InputStream, OutputStream)} instead.
     */
    public static void bufferedCopy( final InputStream input, final OutputStream output )
            throws IOException
    {
        final BufferedInputStream in = new BufferedInputStream( input );
        final BufferedOutputStream out = new BufferedOutputStream( output );
        copy( in, out );
        out.flush();
    }


    ///////////////////////////////////////////////////////////////
    // String -> byte[]
    /**
     * Get the contents of a <code>String</code> as a <code>byte[]</code>.
     */
    public static byte[] toByteArray( final String input )
            throws IOException
    {
        return toByteArray( input, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of a <code>String</code> as a <code>byte[]</code>.
     * @param bufferSize Size of internal buffer to use.
     */
    public static byte[] toByteArray( final String input, final int bufferSize )
            throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy( input, output, bufferSize );
        return output.toByteArray();
    }



    ///////////////////////////////////////////////////////////////
    // Derived copy methods
    // byte[] -> *
    ///////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    // byte[] -> Writer

    /**
     * Copy and convert bytes from a <code>byte[]</code> to chars on a
     * <code>Writer</code>.
     * The platform's default encoding is used for the byte-to-char conversion.
     */
    public static void copy( final byte[] input, final Writer output )
            throws IOException
    {
        copy( input, output, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Copy and convert bytes from a <code>byte[]</code> to chars on a
     * <code>Writer</code>.
     * The platform's default encoding is used for the byte-to-char conversion.
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final byte[] input, final Writer output, final int bufferSize )
            throws IOException
    {
        final ByteArrayInputStream in = new ByteArrayInputStream( input );
        copy( in, output, bufferSize );
    }

    /**
     * Copy and convert bytes from a <code>byte[]</code> to chars on a
     * <code>Writer</code>, using the specified encoding.
     * @param encoding The name of a supported character encoding. See the
     * <a href="http://www.iana.org/assignments/character-sets">IANA
     * Charset Registry</a> for a list of valid encoding types.
     */
    public static void copy( final byte[] input, final Writer output, final String encoding )
            throws IOException
    {
        final ByteArrayInputStream in = new ByteArrayInputStream( input );
        copy( in, output, encoding );
    }

    /**
     * Copy and convert bytes from a <code>byte[]</code> to chars on a
     * <code>Writer</code>, using the specified encoding.
     * @param encoding The name of a supported character encoding. See the
     *        <a href="http://www.iana.org/assignments/character-sets">IANA
     *        Charset Registry</a> for a list of valid encoding types.
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final byte[] input,
                             final Writer output,
                             final String encoding,
                             final int bufferSize )
            throws IOException
    {
        final ByteArrayInputStream in = new ByteArrayInputStream( input );
        copy( in, output, encoding, bufferSize );
    }


    ///////////////////////////////////////////////////////////////
    // byte[] -> String

    /**
     * Get the contents of a <code>byte[]</code> as a String.
     * The platform's default encoding is used for the byte-to-char conversion.
     */
    public static String toString( final byte[] input )
            throws IOException
    {
        return toString( input, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of a <code>byte[]</code> as a String.
     * The platform's default encoding is used for the byte-to-char conversion.
     * @param bufferSize Size of internal buffer to use.
     */
    public static String toString( final byte[] input, final int bufferSize )
            throws IOException
    {
        final StringWriter sw = new StringWriter();
        copy( input, sw, bufferSize );
        return sw.toString();
    }

    /**
     * Get the contents of a <code>byte[]</code> as a String.
     * @param encoding The name of a supported character encoding. See the
     *    <a href="http://www.iana.org/assignments/character-sets">IANA
     *    Charset Registry</a> for a list of valid encoding types.
     */
    public static String toString( final byte[] input, final String encoding )
            throws IOException
    {
        return toString( input, encoding, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Get the contents of a <code>byte[]</code> as a String.
     * @param encoding The name of a supported character encoding. See the
     *   <a href="http://www.iana.org/assignments/character-sets">IANA
     *   Charset Registry</a> for a list of valid encoding types.
     * @param bufferSize Size of internal buffer to use.
     */
    public static String toString( final byte[] input,
                                   final String encoding,
                                   final int bufferSize )
            throws IOException
    {
        final StringWriter sw = new StringWriter();
        copy( input, sw, encoding, bufferSize );
        return sw.toString();
    }


    ///////////////////////////////////////////////////////////////
    // byte[] -> OutputStream

    /**
     * Copy bytes from a <code>byte[]</code> to an <code>OutputStream</code>.
     */
    public static void copy( final byte[] input, final OutputStream output )
            throws IOException
    {
        copy( input, output, DEFAULT_BUFFER_SIZE );
    }

    /**
     * Copy bytes from a <code>byte[]</code> to an <code>OutputStream</code>.
     * @param bufferSize Size of internal buffer to use.
     */
    public static void copy( final byte[] input,
                             final OutputStream output,
                             final int bufferSize )
            throws IOException
    {
        output.write( input );
    }

    /**
     * Compare the contents of two Streams to determine if they are equal or not.
     *
     * @param input1 the first stream
     * @param input2 the second stream
     * @return true if the content of the streams are equal or they both don't exist, false otherwise
     */
    public static boolean contentEquals( final InputStream input1,
                                         final InputStream input2 )
            throws IOException
    {
        final InputStream bufferedInput1 = new BufferedInputStream( input1 );
        final InputStream bufferedInput2 = new BufferedInputStream( input2 );

        int ch = bufferedInput1.read();
        while ( -1 != ch )
        {
            final int ch2 = bufferedInput2.read();
            if ( ch != ch2 )
            {
                return false;
            }
            ch = bufferedInput1.read();
        }

        final int ch2 = bufferedInput2.read();
        if ( -1 != ch2 )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    // ----------------------------------------------------------------------
    // closeXXX()
    // ----------------------------------------------------------------------

    /**
     * Closes the input stream. The input stream can be null and any IOException's will be swallowed.
     *
     * @param inputStream The stream to close.
     */
    public static void close( InputStream inputStream )
    {
        if ( inputStream == null )
        {
            return;
        }

        try
        {
            inputStream.close();
        }
        catch( IOException ex )
        {
            // ignore
        }
    }

    /**
     * Closes a channel. Channel can be null and any IOException's will be swallowed.
     *
     * @param channel The stream to close.
     */
    public static void close( Channel channel )
    {
        if ( channel == null )
        {
            return;
        }

        try
        {
            channel.close();
        }
        catch( IOException ex )
        {
            // ignore
        }
    }

    /**
     * Closes the output stream. The output stream can be null and any IOException's will be swallowed.
     *
     * @param outputStream The stream to close.
     */
    public static void close( OutputStream outputStream )
    {
        if ( outputStream == null )
        {
            return;
        }

        try
        {
            outputStream.close();
        }
        catch( IOException ex )
        {
            // ignore
        }
    }

    /**
     * Closes the reader. The reader can be null and any IOException's will be swallowed.
     *
     * @param reader The reader to close.
     */
    public static void close( Reader reader )
    {
        if ( reader == null )
        {
            return;
        }

        try
        {
            reader.close();
        }
        catch( IOException ex )
        {
            // ignore
        }
    }

    /**
     * Closes the writer. The writer can be null and any IOException's will be swallowed.
     *
     * @param writer The writer to close.
     */
    public static void close( Writer writer )
    {
        if ( writer == null )
        {
            return;
        }

        try
        {
            writer.close();
        }
        catch( IOException ex )
        {
            // ignore
        }
    }

    public static int countPrefix(String _text, char _c)
    {
        int _l = _text.length();
        int _i = 0;
        while(_text.charAt(_i)==_c && _i<_l) _i++;
        return _i;
    }

    public static int countSuffix(String _text, char _c)
    {
        int _l = _text.length()-1;
        int _i = _l;
        while(_text.charAt(_i)==_c && _i>0) _i--;
        return _l-_i;
    }

    // Boolean utilities
    //--------------------------------------------------------------------------
    /**
     * <p>Negates the specified boolean.</p>
     *
     * <p>If {@code null} is passed in, {@code null} will be returned.</p>
     *
     * <p>NOTE: This returns {@code null} and will throw a {@code NullPointerException}
     * if unboxed to a boolean. </p>
     *
     * <pre>
     *   BooleanUtils.negate(Boolean.TRUE)  = Boolean.FALSE;
     *   BooleanUtils.negate(Boolean.FALSE) = Boolean.TRUE;
     *   BooleanUtils.negate(null)          = null;
     * </pre>
     *
     * @param bool  the Boolean to negate, may be null
     * @return the negated Boolean, or {@code null} if {@code null} input
     */
    public static Boolean negate(final Boolean bool) {
        if (bool == null) {
            return null;
        }
        return bool.booleanValue() ? Boolean.FALSE : Boolean.TRUE;
    }

    // boolean Boolean methods
    //-----------------------------------------------------------------------
    /**
     * <p>Checks if a {@code Boolean} value is {@code true},
     * handling {@code null} by returning {@code false}.</p>
     *
     * <pre>
     *   BooleanUtils.isTrue(Boolean.TRUE)  = true
     *   BooleanUtils.isTrue(Boolean.FALSE) = false
     *   BooleanUtils.isTrue(null)          = false
     * </pre>
     *
     * @param bool the boolean to check, {@code null} returns {@code false}
     * @return {@code true} only if the input is non-null and true
     * @since 2.1
     */
    public static boolean isTrue(final Boolean bool) {
        return Boolean.TRUE.equals(bool);
    }

    /**
     * <p>Checks if a {@code Boolean} value is <i>not</i> {@code true},
     * handling {@code null} by returning {@code true}.</p>
     *
     * <pre>
     *   BooleanUtils.isNotTrue(Boolean.TRUE)  = false
     *   BooleanUtils.isNotTrue(Boolean.FALSE) = true
     *   BooleanUtils.isNotTrue(null)          = true
     * </pre>
     *
     * @param bool  the boolean to check, null returns {@code true}
     * @return {@code true} if the input is null or false
     * @since 2.3
     */
    public static boolean isNotTrue(final Boolean bool) {
        return !isTrue(bool);
    }

    /**
     * <p>Checks if a {@code Boolean} value is {@code false},
     * handling {@code null} by returning {@code false}.</p>
     *
     * <pre>
     *   BooleanUtils.isFalse(Boolean.TRUE)  = false
     *   BooleanUtils.isFalse(Boolean.FALSE) = true
     *   BooleanUtils.isFalse(null)          = false
     * </pre>
     *
     * @param bool  the boolean to check, null returns {@code false}
     * @return {@code true} only if the input is non-{@code null} and {@code false}
     * @since 2.1
     */
    public static boolean isFalse(final Boolean bool) {
        return Boolean.FALSE.equals(bool);
    }

    /**
     * <p>Checks if a {@code Boolean} value is <i>not</i> {@code false},
     * handling {@code null} by returning {@code true}.</p>
     *
     * <pre>
     *   BooleanUtils.isNotFalse(Boolean.TRUE)  = true
     *   BooleanUtils.isNotFalse(Boolean.FALSE) = false
     *   BooleanUtils.isNotFalse(null)          = true
     * </pre>
     *
     * @param bool  the boolean to check, null returns {@code true}
     * @return {@code true} if the input is {@code null} or {@code true}
     * @since 2.3
     */
    public static boolean isNotFalse(final Boolean bool) {
        return !isFalse(bool);
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Converts a Boolean to a boolean handling {@code null}
     * by returning {@code false}.</p>
     *
     * <pre>
     *   BooleanUtils.toBoolean(Boolean.TRUE)  = true
     *   BooleanUtils.toBoolean(Boolean.FALSE) = false
     *   BooleanUtils.toBoolean(null)          = false
     * </pre>
     *
     * @param bool  the boolean to convert
     * @return {@code true} or {@code false}, {@code null} returns {@code false}
     */
    public static boolean toBoolean(final Boolean bool) {
        return bool != null && bool.booleanValue();
    }

    /**
     * <p>Converts a Boolean to a boolean handling {@code null}.</p>
     *
     * <pre>
     *   BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false)  = true
     *   BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true)   = true
     *   BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true)  = false
     *   BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false) = false
     *   BooleanUtils.toBooleanDefaultIfNull(null, true)           = true
     *   BooleanUtils.toBooleanDefaultIfNull(null, false)          = false
     * </pre>
     *
     * @param bool  the boolean object to convert to primitive
     * @param valueIfNull  the boolean value to return if the parameter {@code bool} is {@code null}
     * @return {@code true} or {@code false}
     */
    public static boolean toBooleanDefaultIfNull(final Boolean bool, final boolean valueIfNull) {
        if (bool == null) {
            return valueIfNull;
        }
        return bool.booleanValue();
    }

    public static boolean toBooleanDefaultIfNull(final String bool, final boolean valueIfNull) {
        Boolean _val = toBooleanObject(bool);
        if (_val == null) {
            return valueIfNull;
        }
        return _val.booleanValue();
    }

    // Integer to Boolean methods
    //-----------------------------------------------------------------------
    /**
     * <p>Converts an int to a boolean using the convention that {@code zero}
     * is {@code false}, everything else is {@code true}.</p>
     *
     * <pre>
     *   BooleanUtils.toBoolean(0) = false
     *   BooleanUtils.toBoolean(1) = true
     *   BooleanUtils.toBoolean(2) = true
     * </pre>
     *
     * @param value  the int to convert
     * @return {@code true} if non-zero, {@code false}
     *  if zero
     */
    public static boolean toBoolean(final int value) {
        return value != 0;
    }

    /**
     * <p>Converts an int to a Boolean using the convention that {@code zero}
     * is {@code false}, everything else is {@code true}.</p>
     *
     * <pre>
     *   BooleanUtils.toBoolean(0) = Boolean.FALSE
     *   BooleanUtils.toBoolean(1) = Boolean.TRUE
     *   BooleanUtils.toBoolean(2) = Boolean.TRUE
     * </pre>
     *
     * @param value  the int to convert
     * @return Boolean.TRUE if non-zero, Boolean.FALSE if zero,
     *  {@code null} if {@code null}
     */
    public static Boolean toBooleanObject(final int value) {
        return value == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * <p>Converts an Integer to a Boolean using the convention that {@code zero}
     * is {@code false}, every other numeric value is {@code true}.</p>
     *
     * <p>{@code null} will be converted to {@code null}.</p>
     *
     * <p>NOTE: This method may return {@code null} and may throw a {@code NullPointerException}
     * if unboxed to a {@code boolean}.</p>
     *
     * <pre>
     *   BooleanUtils.toBoolean(Integer.valueOf(0))    = Boolean.FALSE
     *   BooleanUtils.toBoolean(Integer.valueOf(1))    = Boolean.TRUE
     *   BooleanUtils.toBoolean(Integer.valueOf(null)) = null
     * </pre>
     *
     * @param value  the Integer to convert
     * @return Boolean.TRUE if non-zero, Boolean.FALSE if zero,
     *  {@code null} if {@code null} input
     */
    public static Boolean toBooleanObject(final Integer value) {
        if (value == null) {
            return null;
        }
        return value.intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * <p>Converts an int to a boolean specifying the conversion values.</p>
     *
     * <p>If the {@code trueValue} and {@code falseValue} are the same number then
     * the return value will be {@code true} in case {@code value} matches it.</p>
     *
     * <pre>
     *   BooleanUtils.toBoolean(0, 1, 0) = false
     *   BooleanUtils.toBoolean(1, 1, 0) = true
     *   BooleanUtils.toBoolean(1, 1, 1) = true
     *   BooleanUtils.toBoolean(2, 1, 2) = false
     *   BooleanUtils.toBoolean(2, 2, 0) = true
     * </pre>
     *
     * @param value  the {@code Integer} to convert
     * @param trueValue  the value to match for {@code true}
     * @param falseValue  the value to match for {@code false}
     * @return {@code true} or {@code false}
     * @throws IllegalArgumentException if {@code value} does not match neither
     * {@code trueValue} no {@code falseValue}
     */
    public static boolean toBoolean(final int value, final int trueValue, final int falseValue) {
        if (value == trueValue) {
            return true;
        }
        if (value == falseValue) {
            return false;
        }
        throw new IllegalArgumentException("The Integer did not match either specified value");
    }

    /**
     * <p>Converts an Integer to a boolean specifying the conversion values.</p>
     *
     * <pre>
     *   BooleanUtils.toBoolean(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0)) = false
     *   BooleanUtils.toBoolean(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)) = true
     *   BooleanUtils.toBoolean(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(2)) = false
     *   BooleanUtils.toBoolean(Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(0)) = true
     *   BooleanUtils.toBoolean(null, null, Integer.valueOf(0))                     = true
     * </pre>
     *
     * @param value  the Integer to convert
     * @param trueValue  the value to match for {@code true}, may be {@code null}
     * @param falseValue  the value to match for {@code false}, may be {@code null}
     * @return {@code true} or {@code false}
     * @throws IllegalArgumentException if no match
     */
    public static boolean toBoolean(final Integer value, final Integer trueValue, final Integer falseValue) {
        if (value == null) {
            if (trueValue == null) {
                return true;
            }
            if (falseValue == null) {
                return false;
            }
        } else if (value.equals(trueValue)) {
            return true;
        } else if (value.equals(falseValue)) {
            return false;
        }
        throw new IllegalArgumentException("The Integer did not match either specified value");
    }

    /**
     * <p>Converts an int to a Boolean specifying the conversion values.</p>
     *
     * <p>NOTE: This method may return {@code null} and may throw a {@code NullPointerException}
     * if unboxed to a {@code boolean}.</p>
     *
     * <p>The checks are done first for the {@code trueValue}, then for the {@code falseValue} and
     * finally for the {@code nullValue}.</p>
     *
     * <pre>
     *   BooleanUtils.toBooleanObject(0, 0, 2, 3) = Boolean.TRUE
     *   BooleanUtils.toBooleanObject(0, 0, 0, 3) = Boolean.TRUE
     *   BooleanUtils.toBooleanObject(0, 0, 0, 0) = Boolean.TRUE
     *   BooleanUtils.toBooleanObject(2, 1, 2, 3) = Boolean.FALSE
     *   BooleanUtils.toBooleanObject(2, 1, 2, 2) = Boolean.FALSE
     *   BooleanUtils.toBooleanObject(3, 1, 2, 3) = null
     * </pre>
     *
     * @param value  the Integer to convert
     * @param trueValue  the value to match for {@code true}
     * @param falseValue  the value to match for {@code false}
     * @param nullValue  the value to to match for {@code null}
     * @return Boolean.TRUE, Boolean.FALSE, or {@code null}
     * @throws IllegalArgumentException if no match
     */
    public static Boolean toBooleanObject(final int value, final int trueValue, final int falseValue, final int nullValue) {
        if (value == trueValue) {
            return Boolean.TRUE;
        }
        if (value == falseValue) {
            return Boolean.FALSE;
        }
        if (value == nullValue) {
            return null;
        }
        throw new IllegalArgumentException("The Integer did not match any specified value");
    }

    /**
     * <p>Converts an Integer to a Boolean specifying the conversion values.</p>
     *
     * <p>NOTE: This method may return {@code null} and may throw a {@code NullPointerException}
     * if unboxed to a {@code boolean}.</p>
     *
     * <p>The checks are done first for the {@code trueValue}, then for the {@code falseValue} and
     * finally for the {@code nullValue}.</p>
     **
     * <pre>
     *   BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(2), Integer.valueOf(3)) = Boolean.TRUE
     *   BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(3)) = Boolean.TRUE
     *   BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0)) = Boolean.TRUE
     *   BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)) = Boolean.FALSE
     *   BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(2)) = Boolean.FALSE
     *   BooleanUtils.toBooleanObject(Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)) = null
     * </pre>
     *
     * @param value  the Integer to convert
     * @param trueValue  the value to match for {@code true}, may be {@code null}
     * @param falseValue  the value to match for {@code false}, may be {@code null}
     * @param nullValue  the value to to match for {@code null}, may be {@code null}
     * @return Boolean.TRUE, Boolean.FALSE, or {@code null}
     * @throws IllegalArgumentException if no match
     */
    public static Boolean toBooleanObject(final Integer value, final Integer trueValue, final Integer falseValue, final Integer nullValue) {
        if (value == null) {
            if (trueValue == null) {
                return Boolean.TRUE;
            }
            if (falseValue == null) {
                return Boolean.FALSE;
            }
            if (nullValue == null) {
                return null;
            }
        } else if (value.equals(trueValue)) {
            return Boolean.TRUE;
        } else if (value.equals(falseValue)) {
            return Boolean.FALSE;
        } else if (value.equals(nullValue)) {
            return null;
        }
        throw new IllegalArgumentException("The Integer did not match any specified value");
    }

    // Boolean to Integer methods
    //-----------------------------------------------------------------------
    /**
     * <p>Converts a boolean to an int using the convention that
     * {@code true} is {@code 1} and {@code false} is {@code 0}.</p>
     *
     * <pre>
     *   BooleanUtils.toInteger(true)  = 1
     *   BooleanUtils.toInteger(false) = 0
     * </pre>
     *
     * @param bool  the boolean to convert
     * @return one if {@code true}, zero if {@code false}
     */
    public static int toInteger(final boolean bool) {
        return bool ? 1 : 0;
    }

    /**
     * <p>Converts a boolean to an int specifying the conversion values.</p>
     *
     * <pre>
     *   BooleanUtils.toInteger(true, 1, 0)  = 1
     *   BooleanUtils.toInteger(false, 1, 0) = 0
     * </pre>
     *
     * @param bool  the to convert
     * @param trueValue  the value to return if {@code true}
     * @param falseValue  the value to return if {@code false}
     * @return the appropriate value
     */
    public static int toInteger(final boolean bool, final int trueValue, final int falseValue) {
        return bool ? trueValue : falseValue;
    }

    /**
     * <p>Converts a Boolean to an int specifying the conversion values.</p>
     *
     * <pre>
     *   BooleanUtils.toInteger(Boolean.TRUE, 1, 0, 2)  = 1
     *   BooleanUtils.toInteger(Boolean.FALSE, 1, 0, 2) = 0
     *   BooleanUtils.toInteger(null, 1, 0, 2)          = 2
     * </pre>
     *
     * @param bool  the Boolean to convert
     * @param trueValue  the value to return if {@code true}
     * @param falseValue  the value to return if {@code false}
     * @param nullValue  the value to return if {@code null}
     * @return the appropriate value
     */
    public static int toInteger(final Boolean bool, final int trueValue, final int falseValue, final int nullValue) {
        if (bool == null) {
            return nullValue;
        }
        return bool.booleanValue() ? trueValue : falseValue;
    }

    /**
     * <p>Converts a boolean to an Integer specifying the conversion values.</p>
     *
     * <pre>
     *   BooleanUtils.toIntegerObject(true, Integer.valueOf(1), Integer.valueOf(0))  = Integer.valueOf(1)
     *   BooleanUtils.toIntegerObject(false, Integer.valueOf(1), Integer.valueOf(0)) = Integer.valueOf(0)
     * </pre>
     *
     * @param bool  the to convert
     * @param trueValue  the value to return if {@code true}, may be {@code null}
     * @param falseValue  the value to return if {@code false}, may be {@code null}
     * @return the appropriate value
     */
    public static Integer toIntegerObject(final boolean bool, final Integer trueValue, final Integer falseValue) {
        return bool ? trueValue : falseValue;
    }

    /**
     * <p>Converts a Boolean to an Integer specifying the conversion values.</p>
     *
     * <pre>
     *   BooleanUtils.toIntegerObject(Boolean.TRUE, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(2))  = Integer.valueOf(1)
     *   BooleanUtils.toIntegerObject(Boolean.FALSE, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(2)) = Integer.valueOf(0)
     *   BooleanUtils.toIntegerObject(null, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(2))          = Integer.valueOf(2)
     * </pre>
     *
     * @param bool  the Boolean to convert
     * @param trueValue  the value to return if {@code true}, may be {@code null}
     * @param falseValue  the value to return if {@code false}, may be {@code null}
     * @param nullValue  the value to return if {@code null}, may be {@code null}
     * @return the appropriate value
     */
    public static Integer toIntegerObject(final Boolean bool, final Integer trueValue, final Integer falseValue, final Integer nullValue) {
        if (bool == null) {
            return nullValue;
        }
        return bool.booleanValue() ? trueValue : falseValue;
    }

    // String to Boolean methods
    //-----------------------------------------------------------------------
    /**
     * <p>Converts a String to a Boolean.</p>
     *
     * <p>{@code 'true'}, {@code 'on'}, {@code 'y'}, {@code 't'} or {@code 'yes'}
     * (case insensitive) will return {@code true}.
     * {@code 'false'}, {@code 'off'}, {@code 'n'}, {@code 'f'} or {@code 'no'}
     * (case insensitive) will return {@code false}.
     * Otherwise, {@code null} is returned.</p>
     *
     * <p>NOTE: This method may return {@code null} and may throw a {@code NullPointerException}
     * if unboxed to a {@code boolean}.</p>
     *
     * <pre>
     *   // N.B. case is not significant
     *   BooleanUtils.toBooleanObject(null)    = null
     *   BooleanUtils.toBooleanObject("true")  = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("T")     = Boolean.TRUE // i.e. T[RUE]
     *   BooleanUtils.toBooleanObject("false") = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("f")     = Boolean.FALSE // i.e. f[alse]
     *   BooleanUtils.toBooleanObject("No")    = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("n")     = Boolean.FALSE // i.e. n[o]
     *   BooleanUtils.toBooleanObject("on")    = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("ON")    = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("off")   = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("oFf")   = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("yes")   = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("Y")     = Boolean.TRUE // i.e. Y[ES]
     *   BooleanUtils.toBooleanObject("blue")  = null
     *   BooleanUtils.toBooleanObject("true ") = null // trailing space (too long)
     *   BooleanUtils.toBooleanObject("ono")   = null // does not match on or no
     * </pre>
     *
     * @param str  the String to check; upper and lower case are treated as the same
     * @return the Boolean value of the string, {@code null} if no match or {@code null} input
     */
    public static Boolean toBooleanObject(final String str) {
        // Previously used equalsIgnoreCase, which was fast for interned 'true'.
        // Non interned 'true' matched 15 times slower.
        //
        // Optimisation provides same performance as before for interned 'true'.
        // Similar performance for null, 'false', and other strings not length 2/3/4.
        // 'true'/'TRUE' match 4 times slower, 'tRUE'/'True' 7 times slower.
        if (str == "true") {
            return Boolean.TRUE;
        }
        if (str == null) {
            return null;
        }
        switch (str.length()) {
            case 1: {
                final char ch0 = str.charAt(0);
                if (ch0 == 'y' || ch0 == 'Y' ||
                        ch0 == 't' || ch0 == 'T') {
                    return Boolean.TRUE;
                }
                if (ch0 == 'n' || ch0 == 'N' ||
                        ch0 == 'f' || ch0 == 'F') {
                    return Boolean.FALSE;
                }
                break;
            }
            case 2: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                if ((ch0 == 'o' || ch0 == 'O') &&
                        (ch1 == 'n' || ch1 == 'N') ) {
                    return Boolean.TRUE;
                }
                if ((ch0 == 'n' || ch0 == 'N') &&
                        (ch1 == 'o' || ch1 == 'O') ) {
                    return Boolean.FALSE;
                }
                break;
            }
            case 3: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                if ((ch0 == 'y' || ch0 == 'Y') &&
                        (ch1 == 'e' || ch1 == 'E') &&
                        (ch2 == 's' || ch2 == 'S') ) {
                    return Boolean.TRUE;
                }
                if ((ch0 == 'o' || ch0 == 'O') &&
                        (ch1 == 'f' || ch1 == 'F') &&
                        (ch2 == 'f' || ch2 == 'F') ) {
                    return Boolean.FALSE;
                }
                break;
            }
            case 4: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                final char ch3 = str.charAt(3);
                if ((ch0 == 't' || ch0 == 'T') &&
                        (ch1 == 'r' || ch1 == 'R') &&
                        (ch2 == 'u' || ch2 == 'U') &&
                        (ch3 == 'e' || ch3 == 'E') ) {
                    return Boolean.TRUE;
                }
                break;
            }
            case 5: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                final char ch3 = str.charAt(3);
                final char ch4 = str.charAt(4);
                if ((ch0 == 'f' || ch0 == 'F') &&
                        (ch1 == 'a' || ch1 == 'A') &&
                        (ch2 == 'l' || ch2 == 'L') &&
                        (ch3 == 's' || ch3 == 'S') &&
                        (ch4 == 'e' || ch4 == 'E') ) {
                    return Boolean.FALSE;
                }
                break;
            }
            default:
                break;
        }

        return null;
    }

    /**
     * <p>Converts a String to a Boolean throwing an exception if no match.</p>
     *
     * <p>NOTE: This method may return {@code null} and may throw a {@code NullPointerException}
     * if unboxed to a {@code boolean}.</p>
     *
     * <pre>
     *   BooleanUtils.toBooleanObject("true", "true", "false", "null")   = Boolean.TRUE
     *   BooleanUtils.toBooleanObject(null, null, "false", "null")       = Boolean.TRUE
     *   BooleanUtils.toBooleanObject(null, null, null, "null")          = Boolean.TRUE
     *   BooleanUtils.toBooleanObject(null, null, null, null)            = Boolean.TRUE
     *   BooleanUtils.toBooleanObject("false", "true", "false", "null")  = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("false", "true", "false", "false") = Boolean.FALSE
     *   BooleanUtils.toBooleanObject(null, "true", null, "false")       = Boolean.FALSE
     *   BooleanUtils.toBooleanObject(null, "true", null, null)          = Boolean.FALSE
     *   BooleanUtils.toBooleanObject("null", "true", "false", "null")   = null
     * </pre>
     *
     * @param str  the String to check
     * @param trueString  the String to match for {@code true} (case sensitive), may be {@code null}
     * @param falseString  the String to match for {@code false} (case sensitive), may be {@code null}
     * @param nullString  the String to match for {@code null} (case sensitive), may be {@code null}
     * @return the Boolean value of the string, {@code null} if either the String matches {@code nullString}
     *  or if {@code null} input and {@code nullString} is {@code null}
     * @throws IllegalArgumentException if the String doesn't match
     */
    public static Boolean toBooleanObject(final String str, final String trueString, final String falseString, final String nullString) {
        if (str == null) {
            if (trueString == null) {
                return Boolean.TRUE;
            }
            if (falseString == null) {
                return Boolean.FALSE;
            }
            if (nullString == null) {
                return null;
            }
        } else if (str.equals(trueString)) {
            return Boolean.TRUE;
        } else if (str.equals(falseString)) {
            return Boolean.FALSE;
        } else if (str.equals(nullString)) {
            return null;
        }
        // no match
        throw new IllegalArgumentException("The String did not match any specified value");
    }

    // String to boolean methods
    //-----------------------------------------------------------------------
    /**
     * <p>Converts a String to a boolean (optimised for performance).</p>
     *
     * <p>{@code 'true'}, {@code 'on'}, {@code 'y'}, {@code 't'} or {@code 'yes'}
     * (case insensitive) will return {@code true}. Otherwise,
     * {@code false} is returned.</p>
     *
     * <p>This method performs 4 times faster (JDK1.4) than
     * {@code Boolean.valueOf(String)}. However, this method accepts
     * 'on' and 'yes', 't', 'y' as true values.
     *
     * <pre>
     *   BooleanUtils.toBoolean(null)    = false
     *   BooleanUtils.toBoolean("true")  = true
     *   BooleanUtils.toBoolean("TRUE")  = true
     *   BooleanUtils.toBoolean("tRUe")  = true
     *   BooleanUtils.toBoolean("on")    = true
     *   BooleanUtils.toBoolean("yes")   = true
     *   BooleanUtils.toBoolean("false") = false
     *   BooleanUtils.toBoolean("x gti") = false
     *   BooleanUtils.toBooleanObject("y") = true
     *   BooleanUtils.toBooleanObject("n") = false
     *   BooleanUtils.toBooleanObject("t") = true
     *   BooleanUtils.toBooleanObject("f") = false
     * </pre>
     *
     * @param str  the String to check
     * @return the boolean value of the string, {@code false} if no match or the String is null
     */
    public static boolean toBoolean(final String str) {
        return toBooleanObject(str) == Boolean.TRUE;
    }

    /**
     * <p>Converts a String to a Boolean throwing an exception if no match found.</p>
     *
     * <pre>
     *   BooleanUtils.toBoolean("true", "true", "false")  = true
     *   BooleanUtils.toBoolean("false", "true", "false") = false
     * </pre>
     *
     * @param str  the String to check
     * @param trueString  the String to match for {@code true} (case sensitive), may be {@code null}
     * @param falseString  the String to match for {@code false} (case sensitive), may be {@code null}
     * @return the boolean value of the string
     * @throws IllegalArgumentException if the String doesn't match
     */
    public static boolean toBoolean(final String str, final String trueString, final String falseString) {
        if (str == trueString) {
            return true;
        } else if (str == falseString) {
            return false;
        } else if (str != null) {
            if (str.equalsIgnoreCase(trueString)) {
                return true;
            } else if (str.equalsIgnoreCase(falseString)) {
                return false;
            }
        }
        throw new IllegalArgumentException("The String did not match either specified value");
    }

    public static boolean toBoolean(final String str, final String trueString)
    {
        if (str == trueString)
        {
            return true;
        }
        else
        if ((str != null) && str.equalsIgnoreCase(trueString))
        {
            return true;
        }
        return false;
    }

    // Boolean to String methods
    //-----------------------------------------------------------------------
    /**
     * <p>Converts a Boolean to a String returning {@code 'true'},
     * {@code 'false'}, or {@code null}.</p>
     *
     * <pre>
     *   BooleanUtils.toStringTrueFalse(Boolean.TRUE)  = "true"
     *   BooleanUtils.toStringTrueFalse(Boolean.FALSE) = "false"
     *   BooleanUtils.toStringTrueFalse(null)          = null;
     * </pre>
     *
     * @param bool  the Boolean to check
     * @return {@code 'true'}, {@code 'false'}, or {@code null}
     */
    public static String toStringTrueFalse(final Boolean bool) {
        return toString(bool, "true", "false", null);
    }

    /**
     * <p>Converts a Boolean to a String returning {@code 'on'},
     * {@code 'off'}, or {@code null}.</p>
     *
     * <pre>
     *   BooleanUtils.toStringOnOff(Boolean.TRUE)  = "on"
     *   BooleanUtils.toStringOnOff(Boolean.FALSE) = "off"
     *   BooleanUtils.toStringOnOff(null)          = null;
     * </pre>
     *
     * @param bool  the Boolean to check
     * @return {@code 'on'}, {@code 'off'}, or {@code null}
     */
    public static String toStringOnOff(final Boolean bool) {
        return toString(bool, "on", "off", null);
    }

    /**
     * <p>Converts a Boolean to a String returning {@code 'yes'},
     * {@code 'no'}, or {@code null}.</p>
     *
     * <pre>
     *   BooleanUtils.toStringYesNo(Boolean.TRUE)  = "yes"
     *   BooleanUtils.toStringYesNo(Boolean.FALSE) = "no"
     *   BooleanUtils.toStringYesNo(null)          = null;
     * </pre>
     *
     * @param bool  the Boolean to check
     * @return {@code 'yes'}, {@code 'no'}, or {@code null}
     */
    public static String toStringYesNo(final Boolean bool) {
        return toString(bool, "yes", "no", null);
    }

    /**
     * <p>Converts a Boolean to a String returning one of the input Strings.</p>
     *
     * <pre>
     *   BooleanUtils.toString(Boolean.TRUE, "true", "false", null)   = "true"
     *   BooleanUtils.toString(Boolean.FALSE, "true", "false", null)  = "false"
     *   BooleanUtils.toString(null, "true", "false", null)           = null;
     * </pre>
     *
     * @param bool  the Boolean to check
     * @param trueString  the String to return if {@code true}, may be {@code null}
     * @param falseString  the String to return if {@code false}, may be {@code null}
     * @param nullString  the String to return if {@code null}, may be {@code null}
     * @return one of the three input Strings
     */
    public static String toString(final Boolean bool, final String trueString, final String falseString, final String nullString) {
        if (bool == null) {
            return nullString;
        }
        return bool.booleanValue() ? trueString : falseString;
    }

    // boolean to String methods
    //-----------------------------------------------------------------------
    /**
     * <p>Converts a boolean to a String returning {@code 'true'}
     * or {@code 'false'}.</p>
     *
     * <pre>
     *   BooleanUtils.toStringTrueFalse(true)   = "true"
     *   BooleanUtils.toStringTrueFalse(false)  = "false"
     * </pre>
     *
     * @param bool  the Boolean to check
     * @return {@code 'true'}, {@code 'false'}, or {@code null}
     */
    public static String toStringTrueFalse(final boolean bool) {
        return toString(bool, "true", "false");
    }

    /**
     * <p>Converts a boolean to a String returning {@code 'on'}
     * or {@code 'off'}.</p>
     *
     * <pre>
     *   BooleanUtils.toStringOnOff(true)   = "on"
     *   BooleanUtils.toStringOnOff(false)  = "off"
     * </pre>
     *
     * @param bool  the Boolean to check
     * @return {@code 'on'}, {@code 'off'}, or {@code null}
     */
    public static String toStringOnOff(final boolean bool) {
        return toString(bool, "on", "off");
    }

    /**
     * <p>Converts a boolean to a String returning {@code 'yes'}
     * or {@code 'no'}.</p>
     *
     * <pre>
     *   BooleanUtils.toStringYesNo(true)   = "yes"
     *   BooleanUtils.toStringYesNo(false)  = "no"
     * </pre>
     *
     * @param bool  the Boolean to check
     * @return {@code 'yes'}, {@code 'no'}, or {@code null}
     */
    public static String toStringYesNo(final boolean bool) {
        return toString(bool, "yes", "no");
    }

    /**
     * <p>Converts a boolean to a String returning one of the input Strings.</p>
     *
     * <pre>
     *   BooleanUtils.toString(true, "true", "false")   = "true"
     *   BooleanUtils.toString(false, "true", "false")  = "false"
     * </pre>
     *
     * @param bool  the Boolean to check
     * @param trueString  the String to return if {@code true}, may be {@code null}
     * @param falseString  the String to return if {@code false}, may be {@code null}
     * @return one of the two input Strings
     */
    public static String toString(final boolean bool, final String trueString, final String falseString) {
        return bool ? trueString : falseString;
    }

}
