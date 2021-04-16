package com.github.terefang.jmelange.commons;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.Base64;
import org.codehaus.plexus.util.StringUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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

    public static Integer checkInteger(String _str)
    {
        if(_str==null)
        {
            return -1;
        }
        _str = _str.trim();

        if(_str.length()==0) return 0;

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

        return Long.parseLong(_str);
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

    public static long toLong(String str) {
        return Long.parseLong(str);
    }

    public static String sha512Hex(String _name)
    {
        try {
            MessageDigest _md = MessageDigest.getInstance("SHA-512");
            return toHex(_md.digest(_name.getBytes(StandardCharsets.UTF_8)));
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String sha256Hex(String _name)
    {
        try {
            MessageDigest _md = MessageDigest.getInstance("SHA-256");
            return toHex(_md.digest(_name.getBytes(StandardCharsets.UTF_8)));
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String sha1Hex(String _name)
    {
        try {
            MessageDigest _md = MessageDigest.getInstance("SHA-1");
            return toHex(_md.digest(_name.getBytes(StandardCharsets.UTF_8)));
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String sha384Hex(String _name)
    {
        try {
            MessageDigest _md = MessageDigest.getInstance("SHA-384");
            return toHex(_md.digest(_name.getBytes(StandardCharsets.UTF_8)));
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
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
        String[] _parts = StringUtils.split(_text);
        StringBuilder _sb = new StringBuilder();
        for(String _part : _parts)
        {
            _sb.append((char)toInt(_part));
        }
        return _sb.toString();
    }

    public static String decimalToHex(String _text)
    {
        String[] _parts = StringUtils.split(_text);
        for(int _i=0; _i<_parts.length; _i++)
        {
            _parts[_i] = toBase(16,toInt(_parts[_i]));
        }
        return StringUtils.join(_parts, "");
    }

    public static String decimalToHex(String _text, String _sep)
    {
        String[] _parts = StringUtils.split(_text, _sep);
        for(int _i=0; _i<_parts.length; _i++)
        {
            _parts[_i] = toBase(16,toInt(_parts[_i]));
        }
        return StringUtils.join(_parts, "");
    }

    public static String decimalToHex(String _text, String _sep, String _delim)
    {
        String[] _parts = StringUtils.split(_text, _sep);
        for(int _i=0; _i<_parts.length; _i++)
        {
            _parts[_i] = toBase(16,toInt(_parts[_i]));
        }
        return StringUtils.join(_parts, _delim);
    }

    public static long ipToLong(String _addr)
    {
        String[] _parts = StringUtils.split(_addr, ".", 4);
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
}
