package com.github.terefang.jmelange.commons;

import com.github.terefang.jmelange.commons.util.*;
import com.github.terefang.jmelange.commons.zip.ByFileArchiver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.github.terefang.jmelange.apache.codec.digest.DigestUtils;
import com.github.terefang.jmelange.apache.io.LineIterator;
import com.github.terefang.jmelange.apache.io.function.IOConsumer;
import com.github.terefang.jmelange.apache.lang3.BooleanUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
public class CommonUtil
{
    public static String toKebabCase(String _name)
    {
        return _name.trim().toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }

    public static String toSnakeCase(String _name)
    {
        return _name.trim().toLowerCase().replaceAll("[^a-z0-9]+", "_");
    }
    
    static char[] _PUNCTUATION = ",.;:!?#=/&%$§*+_|()[]{}<>".toCharArray();
    
    public static String removePunctuation(String _text)
    {
        for(char _p : _PUNCTUATION) {
            _text = _text.replace(_p, ' ');
        }
        return _text.replaceAll(" +", " ");
    }
    
    public static String safeNameTrim(String _name)
    {
        return safeNameTrim('_', _name);
    }
    public static String safeNameTrim(String _c, String _name)
    {
        return safeNameTrim(_c.charAt(0), _name);
    }
    
    public static String safeNameTrim(char _c, String _name)
    {
        return safeName(_c,safeName(' ',_name).trim());
    }
    
    public static String safeName(String _name)
    {
        return safeName('_', _name);
    }
    public static String safeName(String _c, String _name)
    {
        return safeName(_c.charAt(0), _name);
    }
    
    public static String safeName(char _c, String _name)
    {
        char[] _n = StringUtil.asciifyAccents(_name.trim()).toLowerCase().toCharArray();
        for(int _i=0; _i< _n.length; _i++)
        {
            if(_n[_i]<0x30)
            {
                _n[_i]=_c;
            }
            else if((_n[_i]>0x39) && (_n[_i]<0x41))
            {
                _n[_i]=_c;
            }
            else if((_n[_i]>0x5a) && (_n[_i]<0x61))
            {
                _n[_i]=_c;
            }
            else if(_n[_i]>0x7a)
            {
                _n[_i]=_c;
            }
        }
        return new String(_n);
    }
    
    public static List<String> normalizeVariants(String _input, String _subSpace, String _subNWS)
    {
        return StringUtil.normalizeVariants(_input, _subSpace, _subNWS);
    }

    public static String toHexDumo(byte[] _input)
    {
        return IOUtil.toHexDumo(_input);
    }

    //*************************************************************************

    public static String obfEncode(String _plain)
    {
        String _pwd = CommonUtil.trim(_plain);
        byte[] _buf = _pwd.getBytes(StandardCharsets.US_ASCII);
        for(int _i=0; _i<_buf.length; _i++)
        {
            _buf[_i] = (byte) (_buf[_i] ^ 0x31);
        }
        return ("OBF:"+Base64.getEncoder().encodeToString(_buf));
    }

    public static String obfDecode(String _obf)
    {
        String _pwd = CommonUtil.trim(_obf);
        if(!_pwd.startsWith("OBF:")) return _pwd;
        byte[] _buf = Base64.getDecoder().decode(_pwd.substring(4));
        for(int _i=0; _i<_buf.length; _i++)
        {
            _buf[_i] = (byte) (_buf[_i] ^ 0x31);
        }
        return (new String(_buf, StandardCharsets.US_ASCII));
    }


    //*************************************************************************

    public static long ipToLong(String _addr)
    {
        String[] _parts = StringUtil.split(_addr, ".", 4);
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
        return HashUtil.toHex(new byte[]{(byte) ((_addr >>> 24) & 0xff),
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

    //*************************************************************************
    // static LogUtil _s0;

    public static void trace(String msg) {
        LogUtil.trace(msg);
    }

    public static void debug(String msg) {
        LogUtil.debug(msg);
    }

    public static void info(String msg) {
        LogUtil.info(msg);
    }

    public static void warn(String msg) {
        LogUtil.warn(msg);
    }

    public static void error(String msg) {
        LogUtil.error(msg);
    }

    //*************************************************************************
    // static OsUtil _s0;

    public static String getUserConfigDirectory() {
        return OsUtil.getUserConfigDirectory();
    }

    public static String getUserConfigDirectory(String applicationName) {
        return OsUtil.getUserConfigDirectory(applicationName);
    }

    public static String getUserDataDirectory() {
        return OsUtil.getUserDataDirectory();
    }

    public static String getUserDataDirectory(String applicationName) {
        return OsUtil.getUserDataDirectory(applicationName);
    }

    //*************************************************************************
    // static DateUtil _s0;

    @SneakyThrows
    public static Date dateToTime(String _format, String _text) {
        return DateUtil.dateToTime(_format, _text);
    }

    @SneakyThrows
    public static Long dateToLong(String _format, String _text) {
        return DateUtil.dateToLong(_format, _text);
    }

    public static Date getDate() {
        return DateUtil.getDate();
    }

    public static Long getDateLong() {
        return DateUtil.getDateLong();
    }

    public static String timeToDate(String _format, Date _time) {
        return DateUtil.timeToDate(_format, _time);
    }

    public static String timeToDate(String _format, long _time) {
        return DateUtil.timeToDate(_format, _time);
    }

    //*************************************************************************
    // static ListMapUtil _s0;

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3, String _k4, Object _v4, String _k5, Object _v5, String _k6, Object _v6) {
        return ListMapUtil.toMap(_k1, _v1, _k2, _v2, _k3, _v3, _k4, _v4, _k5, _v5, _k6, _v6);
    }

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3, String _k4, Object _v4, String _k5, Object _v5) {
        return ListMapUtil.toMap(_k1, _v1, _k2, _v2, _k3, _v3, _k4, _v4, _k5, _v5);
    }

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3, String _k4, Object _v4) {
        return ListMapUtil.toMap(_k1, _v1, _k2, _v2, _k3, _v3, _k4, _v4);
    }

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3) {
        return ListMapUtil.toMap(_k1, _v1, _k2, _v2, _k3, _v3);
    }

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2) {
        return ListMapUtil.toMap(_k1, _v1, _k2, _v2);
    }

    public static Map<String, Object> toMap(String _k, Object _v) {
        return ListMapUtil.toMap(_k, _v);
    }

    public static Map<String, Object> toMap(String... _args) {
        return ListMapUtil.toMap(_args);
    }

    public static Map<String, Object> toMap(Object... _args) {
        return ListMapUtil.toMap(_args);
    }

    public static Map<String, Object> toMap(List<Object> _args) {
        return ListMapUtil.toMap(_args);
    }

    public static List<String> toList(String... _args) {
        return ListMapUtil.toList(_args);
    }

    public static List<String> toList(String _a1, String _a2, String _a3, String _a4, String _a5, String _a6) {
        return ListMapUtil.toList(_a1, _a2, _a3, _a4, _a5, _a6);
    }

    public static List<String> toList(String _a1, String _a2, String _a3, String _a4, String _a5) {
        return ListMapUtil.toList(_a1, _a2, _a3, _a4, _a5);
    }

    public static List<String> toList(String _a1, String _a2, String _a3, String _a4) {
        return ListMapUtil.toList(_a1, _a2, _a3, _a4);
    }

    public static List<String> toList(String _a1, String _a2, String _a3) {
        return ListMapUtil.toList(_a1, _a2, _a3);
    }

    public static List<String> toList(String _a1, String _a2) {
        return ListMapUtil.toList(_a1, _a2);
    }

    public static <T> List<T> toList(T... _args) {
        return ListMapUtil.toList(_args);
    }

    public static <T> List<T> toList(T _a1, T _a2, T _a3, T _a4, T _a5, T _a6) {
        return ListMapUtil.toList(_a1, _a2, _a3, _a4, _a5, _a6);
    }

    public static <T> List<T> toList(T _a1, T _a2, T _a3, T _a4, T _a5) {
        return ListMapUtil.toList(_a1, _a2, _a3, _a4, _a5);
    }

    public static <T> List<T> toList(T _a1, T _a2, T _a3, T _a4) {
        return ListMapUtil.toList(_a1, _a2, _a3, _a4);
    }

    public static <T> List<T> toList(T _a1, T _a2, T _a3) {
        return ListMapUtil.toList(_a1, _a2, _a3);
    }

    public static <T> List<T> toList(T _a1, T _a2) {
        return ListMapUtil.toList(_a1, _a2);
    }

    public static List newList() {
        return ListMapUtil.newList();
    }

    public static Map newMap() {
        return ListMapUtil.newMap();
    }

    public static Map newLMap() {
        return ListMapUtil.newLMap();
    }

    //*************************************************************************
    // static HashUtil _s0;

    public static String hashMacHex(String _name, String _key, String... _buffer) {
        return HashUtil.hashMacHex(_name, _key, _buffer);
    }

    public static String hashHex(String _name, String... _buffer) {
        return HashUtil.hashHex(_name, _buffer);
    }

    public static String hashMacHex(String _name, byte[] _key, byte[]... _buffer) {
        return HashUtil.hashMacHex(_name, _key, _buffer);
    }

    public static byte[] hashMac(String _name, byte[] _key, byte[]... _buffer) {
        return HashUtil.hashMac(_name, _key, _buffer);
    }

    public static String hashHex(String _name, byte[]... _buffer) {
        return HashUtil.hashHex(_name, _buffer);
    }

    public static byte[] hash(String _name, byte[]... _buffer) {
        return HashUtil.hash(_name, _buffer);
    }

    public static byte[] sha512HMac(byte[] _key, byte[] _buffer) {
        return HashUtil.sha512HMac(_key, _buffer);
    }

    public static byte[] sha256HMac(byte[] _key, byte[] _buffer) {
        return HashUtil.sha256HMac(_key, _buffer);
    }

    public static byte[] sha1HMac(byte[] _key, byte[] _buffer) {
        return HashUtil.sha1HMac(_key, _buffer);
    }

    public static byte[] md5HMac(byte[] _key, byte[] _buffer) {
        return HashUtil.md5HMac(_key, _buffer);
    }

    public static byte[] sha512(byte[]... _name) {
        return HashUtil.sha512(_name);
    }

    public static byte[] sha256(byte[]... _name) {
        return HashUtil.sha256(_name);
    }

    public static byte[] sha1(byte[]... _name) {
        return HashUtil.sha1(_name);
    }

    public static byte[] md5(byte[]... _name) {
        return HashUtil.md5(_name);
    }

    public static String sha512HMacHex(byte[] _key, byte[] _buffer) {
        return HashUtil.sha512HMacHex(_key, _buffer);
    }

    public static String sha256HMacHex(byte[] _key, byte[] _buffer) {
        return HashUtil.sha256HMacHex(_key, _buffer);
    }

    public static String sha1HMacHex(byte[] _key, byte[] _buffer) {
        return HashUtil.sha1HMacHex(_key, _buffer);
    }

    public static String md5HMacHex(byte[] _key, byte[] _buffer) {
        return HashUtil.md5HMacHex(_key, _buffer);
    }

    public static byte[] fromHex(String _hex) {
        return HashUtil.fromHex(_hex);
    }

    public static byte[] fromHex(char[] _hex) {
        return HashUtil.fromHex(_hex);
    }

    public static String toBase64(Long _n) {
        return HashUtil.toBase64(_n);
    }

    public static String toBase64(String binaryData) {
        return HashUtil.toBase64(binaryData);
    }

    public static String toBase64Np(Long _n) {
        return HashUtil.toBase64Np(_n);
    }

    public static String toBase64Np(byte[] binaryData) {
        return HashUtil.toBase64Np(binaryData);
    }

    public static String toBase64Np(String binaryData) {
        return HashUtil.toBase64Np(binaryData);
    }

    public static byte[] fromBase64(String _b64) {
        return HashUtil.fromBase64(_b64);
    }

    public static String toBase32(Long _n) {
        return HashUtil.toBase32(_n);
    }

    public static String toBase32(byte[] binaryData) {
        return HashUtil.toBase32(binaryData);
    }

    public static String toBase32(String binaryData) {
        return HashUtil.toBase32(binaryData);
    }

    @SneakyThrows
    public static byte[] fromBase32(String _b32) {
        return HashUtil.fromBase32(_b32);
    }

    @SneakyThrows
    public static byte[] randomSalt(int _l) {
        return HashUtil.randomSalt(_l);
    }

    public static String sha512HMacHex(String _key, String _buffer) {
        return HashUtil.sha512HMacHex(_key, _buffer);
    }

    public static String sha256HMacHex(String _key, String _buffer) {
        return HashUtil.sha256HMacHex(_key, _buffer);
    }

    public static String sha1HMacHex(String _key, String _buffer) {
        return HashUtil.sha1HMacHex(_key, _buffer);
    }

    public static String md5HMacHex(String _key, String _buffer) {
        return HashUtil.md5HMacHex(_key, _buffer);
    }

    public static String sha384HMacHex(String _key, String _buffer) {
        return HashUtil.sha384HMacHex(_key, _buffer);
    }

    public static String hashMacHex(String _name, String _key, String _buffer, String _buffer2) {
        return HashUtil.hashMacHex(_name, _key, _buffer, _buffer2);
    }

    public static String hashMacHex(String _name, String _key, String _buffer, String _buffer2, String _buffer3) {
        return HashUtil.hashMacHex(_name, _key, _buffer, _buffer2, _buffer3);
    }

    public static String hashMacHex(String _name, String _key, String _buffer) {
        return HashUtil.hashMacHex(_name, _key, _buffer);
    }

    public static String hashHex(String _name, String _buffer, String _buffer2) {
        return HashUtil.hashHex(_name, _buffer, _buffer2);
    }

    public static String hashHex(String _name, String _buffer, String _buffer2, String _buffer3) {
        return HashUtil.hashHex(_name, _buffer, _buffer2, _buffer3);
    }

    public static String hashHex(String _name, String _buffer) {
        return HashUtil.hashHex(_name, _buffer);
    }

    public static String sha512Hex(String _name) {
        return HashUtil.sha512Hex(_name);
    }

    public static String sha256Hex(String _name) {
        return HashUtil.sha256Hex(_name);
    }

    public static String sha1Hex(String _name) {
        return HashUtil.sha1Hex(_name);
    }

    public static String md5Hex(String _name) {
        return HashUtil.md5Hex(_name);
    }

    public static String toHex(Long _n) {
        return HashUtil.toHex(_n);
    }

    public static String toHex(byte[] _buf) {
        return HashUtil.toHex(_buf);
    }

    public static String toHex(String _buf) {
        return HashUtil.toHex(_buf);
    }

    public static String toBase64(byte[] binaryData) {
        return HashUtil.toBase64(binaryData);
    }

    public static String toBase26(Long _n) {
        return HashUtil.toBase26(_n);
    }

    public static String toBase26(byte[] binaryData) {
        return HashUtil.toBase26(binaryData);
    }

    public static String toBase26(String binaryData) {
        return HashUtil.toBase26(binaryData);
    }

    public static String toBase62(Long _n) {
        return HashUtil.toBase62(_n);
    }

    public static String toBase62(byte[] binaryData) {
        return HashUtil.toBase62(binaryData);
    }

    public static String toBase62(String binaryData) {
        return HashUtil.toBase62(binaryData);
    }

    public static String toBase36(Long _n) {
        return HashUtil.toBase36(_n);
    }

    public static String toBase36(byte[] binaryData) {
        return HashUtil.toBase36(binaryData);
    }

    public static String toBase36(String binaryData) {
        return HashUtil.toBase36(binaryData);
    }

    public static String base64(String binaryData) {
        return HashUtil.base64(binaryData);
    }

    public static byte[] md5(byte[] data) {
        return HashUtil.md5(data);
    }

    public static byte[] sha1(byte[] data) {
        return HashUtil.sha1(data);
    }

    public static byte[] sha256(byte[] data) {
        return HashUtil.sha256(data);
    }

    public static byte[] sha512(byte[] data) {
        return HashUtil.sha512(data);
    }

    public static String base64(byte[] binaryData) {
        return HashUtil.base64(binaryData);
    }

    public static byte[] md2(byte[] data) {
        return DigestUtils.md2(data);
    }

    public static byte[] md2(String data) {
        return DigestUtils.md2(data);
    }

    public static String md2Hex(byte[] data) {
        return DigestUtils.md2Hex(data);
    }

    public static String md2Hex(String data) {
        return DigestUtils.md2Hex(data);
    }

    public static byte[] md5(String data) {
        return DigestUtils.md5(data);
    }

    public static String md5Hex(byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    public static byte[] sha(byte[] data) {
        return DigestUtils.sha(data);
    }

    public static byte[] sha(String data) {
        return DigestUtils.sha(data);
    }

    public static byte[] sha1(String data) {
        return DigestUtils.sha1(data);
    }

    public static String sha1Hex(byte[] data) {
        return DigestUtils.sha1Hex(data);
    }

    public static byte[] sha256(String data) {
        return DigestUtils.sha256(data);
    }

    public static String sha256Hex(byte[] data) {
        return DigestUtils.sha256Hex(data);
    }

    public static byte[] sha384(byte[] data) {
        return DigestUtils.sha384(data);
    }

    public static byte[] sha384(String data) {
        return DigestUtils.sha384(data);
    }

    public static String sha384Hex(byte[] data) {
        return DigestUtils.sha384Hex(data);
    }

    public static String sha384Hex(String data) {
        return DigestUtils.sha384Hex(data);
    }

    public static byte[] sha512(String data) {
        return DigestUtils.sha512(data);
    }

    public static byte[] sha512_224(byte[] data) {
        return DigestUtils.sha512_224(data);
    }

    public static byte[] sha512_224(String data) {
        return DigestUtils.sha512_224(data);
    }

    public static String sha512_224Hex(byte[] data) {
        return DigestUtils.sha512_224Hex(data);
    }

    public static String sha512Hex(byte[] data) {
        return DigestUtils.sha512Hex(data);
    }

    public static String shaHex(byte[] data) {
        return DigestUtils.shaHex(data);
    }

    public static String shaHex(String data) {
        return DigestUtils.shaHex(data);
    }

    //*************************************************************************
    //static BooleanUtil _s0;

    public static boolean toBooleanDefaultIfNull(String bool, boolean valueIfNull) {
        return BooleanUtil.toBooleanDefaultIfNull(bool, valueIfNull);
    }

    public static boolean checkBooleanDefaultIfNull(Object _bool, boolean _b) {
        return BooleanUtil.checkBooleanDefaultIfNull(_bool, _b);
    }

    public static boolean checkBooleanDefaultIfNull(String _bool, boolean _b) {
        return BooleanUtil.checkBooleanDefaultIfNull(_bool, _b);
    }

    public static Boolean checkBoolean(Object _str) {
        return BooleanUtil.checkBoolean(_str);
    }

    public static Boolean checkBoolean(String _str) {
        return BooleanUtil.checkBoolean(_str);
    }

    public static Boolean negate(Boolean bool) {
        return BooleanUtils.negate(bool);
    }

    public static boolean isTrue(Boolean bool) {
        return BooleanUtils.isTrue(bool);
    }

    public static boolean isNotTrue(Boolean bool) {
        return BooleanUtils.isNotTrue(bool);
    }

    public static boolean isFalse(Boolean bool) {
        return BooleanUtils.isFalse(bool);
    }

    public static boolean isNotFalse(Boolean bool) {
        return BooleanUtils.isNotFalse(bool);
    }

    public static boolean toBoolean(Boolean bool) {
        return BooleanUtils.toBoolean(bool);
    }

    public static boolean toBooleanDefaultIfNull(Boolean bool, boolean valueIfNull) {
        return BooleanUtils.toBooleanDefaultIfNull(bool, valueIfNull);
    }

    public static boolean toBoolean(int value) {
        return BooleanUtils.toBoolean(value);
    }

    public static boolean toBoolean(String _value, String _check) {
        return BooleanUtil.toBoolean(_value, _check);
    }

    public static Boolean toBooleanObject(int value) {
        return BooleanUtils.toBooleanObject(value);
    }

    public static Boolean toBooleanObject(Integer value) {
        return BooleanUtils.toBooleanObject(value);
    }

    public static boolean toBoolean(int value, int trueValue, int falseValue) {
        return BooleanUtils.toBoolean(value, trueValue, falseValue);
    }

    public static boolean toBoolean(Integer value, Integer trueValue, Integer falseValue) {
        return BooleanUtils.toBoolean(value, trueValue, falseValue);
    }

    public static Boolean toBooleanObject(int value, int trueValue, int falseValue, int nullValue) {
        return BooleanUtils.toBooleanObject(value, trueValue, falseValue, nullValue);
    }

    public static Boolean toBooleanObject(Integer value, Integer trueValue, Integer falseValue, Integer nullValue) {
        return BooleanUtils.toBooleanObject(value, trueValue, falseValue, nullValue);
    }

    public static int toInteger(boolean bool) {
        return BooleanUtils.toInteger(bool);
    }

    public static Integer toIntegerObject(boolean bool) {
        return BooleanUtils.toIntegerObject(bool);
    }

    public static Integer toIntegerObject(Boolean bool) {
        return BooleanUtils.toIntegerObject(bool);
    }

    public static int toInteger(boolean bool, int trueValue, int falseValue) {
        return BooleanUtils.toInteger(bool, trueValue, falseValue);
    }

    public static int toInteger(Boolean bool, int trueValue, int falseValue, int nullValue) {
        return BooleanUtils.toInteger(bool, trueValue, falseValue, nullValue);
    }

    public static Integer toIntegerObject(boolean bool, Integer trueValue, Integer falseValue) {
        return BooleanUtils.toIntegerObject(bool, trueValue, falseValue);
    }

    public static Integer toIntegerObject(Boolean bool, Integer trueValue, Integer falseValue, Integer nullValue) {
        return BooleanUtils.toIntegerObject(bool, trueValue, falseValue, nullValue);
    }

    public static Boolean toBooleanObject(String str) {
        return BooleanUtils.toBooleanObject(str);
    }

    public static Boolean toBooleanObject(String str, String trueString, String falseString, String nullString) {
        return BooleanUtils.toBooleanObject(str, trueString, falseString, nullString);
    }

    public static boolean toBoolean(String str) {
        return BooleanUtils.toBoolean(str);
    }

    public static boolean toBoolean(String str, String trueString, String falseString) {
        return BooleanUtils.toBoolean(str, trueString, falseString);
    }

    public static String toStringTrueFalse(Boolean bool) {
        return BooleanUtils.toStringTrueFalse(bool);
    }

    public static String toStringOnOff(Boolean bool) {
        return BooleanUtils.toStringOnOff(bool);
    }

    public static String toStringYesNo(Boolean bool) {
        return BooleanUtils.toStringYesNo(bool);
    }

    public static String toString(Boolean bool, String trueString, String falseString, String nullString) {
        return BooleanUtils.toString(bool, trueString, falseString, nullString);
    }

    public static String toStringTrueFalse(boolean bool) {
        return BooleanUtils.toStringTrueFalse(bool);
    }

    public static String toStringOnOff(boolean bool) {
        return BooleanUtils.toStringOnOff(bool);
    }

    public static String toStringYesNo(boolean bool) {
        return BooleanUtils.toStringYesNo(bool);
    }

    public static String toString(boolean bool, String trueString, String falseString) {
        return BooleanUtils.toString(bool, trueString, falseString);
    }

    public static boolean and(boolean... array) {
        return BooleanUtils.and(array);
    }

    public static Boolean and(Boolean... array) {
        return BooleanUtils.and(array);
    }

    public static boolean or(boolean... array) {
        return BooleanUtils.or(array);
    }

    public static Boolean or(Boolean... array) {
        return BooleanUtils.or(array);
    }

    public static boolean xor(boolean... array) {
        return BooleanUtils.xor(array);
    }

    public static Boolean xor(Boolean... array) {
        return BooleanUtils.xor(array);
    }

    public static int compare(boolean x, boolean y) {
        return BooleanUtils.compare(x, y);
    }

    //*************************************************************************
    // static GuidUtil _s0;

    public static String toGuid(String data) {
        return GuidUtil.toGuid(data);
    }

    public static String toGuid(String data, String data2) {
        return GuidUtil.toGuid(data, data2);
    }

    public static String randomUUID() {
        return GuidUtil.randomUUID();
    }

    public static String toUUID(String _name) {
        return GuidUtil.toUUID(_name);
    }

    public static String toTUUID() {
        return GuidUtil.toTUUID();
    }

    public static String toTUUID(long _serverid) {
        return GuidUtil.toTUUID(_serverid);
    }

    public static String toTUID(String _name) {
        return GuidUtil.toTUID(_name);
    }

    public static String toTUID(String _name, String _key) {
        return GuidUtil.toTUID(_name, _key);
    }

    public static String randomGUID() {
        return GuidUtil.randomGUID();
    }

    public static String toGUID(long _n1, long _n2) {
        return GuidUtil.toGUID(_n1, _n2);
    }

    public static String toGUID(long _n1, long _n2, long _n3) {
        return GuidUtil.toGUID(_n1, _n2, _n3);
    }

    public static String toGUID(long _n1, long _n2, long _n3, long _n4) {
        return GuidUtil.toGUID(_n1, _n2, _n3, _n4);
    }

    public static String toGUID(long _n1, long _n2, long _n3, long _n4, long _n5) {
        return GuidUtil.toGUID(_n1, _n2, _n3, _n4, _n5);
    }

    public static String toXUID(long _n1, long _n2) {
        return GuidUtil.toXUID(_n1, _n2);
    }

    public static String toXUID(long _n1, long _n2, long _n3) {
        return GuidUtil.toXUID(_n1, _n2, _n3);
    }

    public static String toXUID(long _n1, long _n2, long _n3, long _n4) {
        return GuidUtil.toXUID(_n1, _n2, _n3, _n4);
    }

    public static String toXUID(long _n1, long _n2, long _n3, long _n4, long _n5) {
        return GuidUtil.toXUID(_n1, _n2, _n3, _n4, _n5);
    }

    public static String dateID() {
        return GuidUtil.dateID();
    }

    public static String dateIDX() {
        return GuidUtil.dateIDX();
    }

    public static String toHashGUID(String _name) {
        return GuidUtil.toHashGUID(_name);
    }

    public static String toHashGUID(String _name, String _key) {
        return GuidUtil.toHashGUID(_name, _key);
    }

    public static String toGUID(String _name) {
        return GuidUtil.toGUID(_name);
    }

    public static String toGUID(String _name1, String _name2) {
        return GuidUtil.toGUID(_name1, _name2);
    }

    public static String toGUID(String _name1, String _name2, String _name3) {
        return GuidUtil.toGUID(_name1, _name2, _name3);
    }

    public static String toGUID(String _name1, long _num) {
        return GuidUtil.toGUID(_name1, _num);
    }

    public static String toGUID(String _name1, String _name2, long _num) {
        return GuidUtil.toGUID(_name1, _name2, _num);
    }

    public static String toGUID(String _name1, String _name2, String _name3, long _num) {
        return GuidUtil.toGUID(_name1, _name2, _name3, _num);
    }

    public static String toXUID(String _name) {
        return GuidUtil.toXUID(_name);
    }

    public static String toXUID(String _name1, String _name2) {
        return GuidUtil.toXUID(_name1, _name2);
    }

    public static String toXUID(String _name1, String _name2, String _name3) {
        return GuidUtil.toXUID(_name1, _name2, _name3);
    }

    public static String toXUID(String _name1, long _num) {
        return GuidUtil.toXUID(_name1, _num);
    }

    public static String toXUID(String _name1, String _name2, long _num) {
        return GuidUtil.toXUID(_name1, _name2, _num);
    }

    public static String toXUID(String _name1, String _name2, String _name3, long _num) {
        return GuidUtil.toXUID(_name1, _name2, _name3, _num);
    }

    public static BigInteger sha512BigInt(String _name) {
        return GuidUtil.sha512BigInt(_name);
    }

    public static BigInteger sha512HMacBigInt(String _key, String _buffer) {
        return GuidUtil.sha512HMacBigInt(_key, _buffer);
    }

    //*************************************************************************
    // static FileUtil

    @SneakyThrows
    public static void filesToZip(String zipFile, Map<String, byte[]> files) throws IOException
    {
        ZipUtil.filesToZip(zipFile, files);
    }

    @SneakyThrows
    public static void filesToZip(File zipFile, Map<String, byte[]> files) throws IOException
    {
        ZipUtil.filesToZip(zipFile, files);
    }

    @SneakyThrows
    public static void filesToZip(String zipFile, String[] files) throws IOException
    {
        ZipUtil.filesToZip(zipFile, files);
    }

    @SneakyThrows
    public static void filesToZip(String zipFile, List<String> files) throws IOException
    {
        ZipUtil.filesToZip(zipFile, files);
    }

    @SneakyThrows
    public static void filesToZip(File zipFile, File[] files) throws IOException
    {
        ZipUtil.filesToZip(zipFile, files);
    }

    @SneakyThrows
    public static void filesToZip(File zipFile, List<File> files) throws IOException
    {
        ZipUtil.filesToZip(zipFile, files);
    }


    @SneakyThrows
    public static void filesToArchive(String zipFile, Map<String, byte[]> files) throws IOException
    {
        ZipUtil.filesToArchive(zipFile, files);
    }

    @SneakyThrows
    public static void filesToArchive(File zipFile, Map<String, byte[]> files) throws IOException
    {
        ZipUtil.filesToArchive(zipFile, files);
    }

    @SneakyThrows
    public static void filesToArchive(String zipFile, String[] files) throws IOException
    {
        ZipUtil.filesToArchive(zipFile, files);
    }

    @SneakyThrows
    public static void filesToArchive(String zipFile, List<String> files) throws IOException
    {
        ZipUtil.filesToArchive(zipFile, files);
    }

    @SneakyThrows
    public static void filesToArchive(File zipFile, File[] files) throws IOException
    {
        ZipUtil.filesToArchive(zipFile, files);
    }

    @SneakyThrows
    public static void filesToArchive(File zipFile, List<File> files) throws IOException
    {
        ZipUtil.filesToArchive(zipFile, files);
    }


    //*************************************************************************
    // static IOUtil _s0;

    @SneakyThrows
    public static String read4CC(InputStream _in) {
        return IOUtil.read4CC(_in);
    }

    @SneakyThrows
    public static String read2CC(InputStream _in) {
        return IOUtil.read2CC(_in);
    }

    public static byte[] from4CC(String _s) {
        return IOUtil.from4CC(_s);
    }

    public static byte[] from2CC(String _s) {
        return IOUtil.from2CC(_s);
    }

    public static byte[] from4CC(int _s) {
        return IOUtil.from4CC(_s);
    }

    public static byte[] from2CC(int _s) {
        return IOUtil.from2CC(_s);
    }

    public static int to4CCInt(byte[] _cc) {
        return IOUtil.to4CCInt(_cc);
    }

    public static int to2CCInt(byte[] _cc) {
        return IOUtil.to2CCInt(_cc);
    }

    public static String to4CC(byte[] _cc) {
        return IOUtil.to4CC(_cc);
    }

    public static String to2CC(byte[] _cc) {
        return IOUtil.to2CC(_cc);
    }

    public static String to4CC(int _cc) {
        return IOUtil.to4CC(_cc);
    }

    public static String to2CC(int _cc) {
        return IOUtil.to2CC(_cc);
    }

    public static long bytesToLong(byte[] _buf) {
        return IOUtil.bytesToLong(_buf);
    }

    @SneakyThrows
    public static byte[] readBytes(int _i, InputStream _in) {
        return IOUtil.readBytes(_i, _in);
    }

    @SneakyThrows
    public static long readLong(InputStream _in) {
        return IOUtil.readLong(_in);
    }

    @SneakyThrows
    public static int readInt(InputStream _in) {
        return IOUtil.readInt(_in);
    }

    @SneakyThrows
    public static int readShort(InputStream _in) {
        return IOUtil.readShort(_in);
    }

    @SneakyThrows
    public static int readByte(InputStream _in) {
        return IOUtil.readByte(_in);
    }

    public static byte[] toByteArray(Long _i) {
        return IOUtil.toByteArray(_i);
    }

    public static byte[] toByteArray(long _i) {
        return IOUtil.toByteArray(_i);
    }

    public static byte[] toByteArray(Integer _i) {
        return IOUtil.toByteArray(_i);
    }

    public static byte[] toByteArray(int _i) {
        return IOUtil.toByteArray(_i);
    }

    public static byte[] toByteArray(Short _i) {
        return IOUtil.toByteArray(_i);
    }

    public static byte[] toByteArray(short _i) {
        return IOUtil.toByteArray(_i);
    }

    public static BufferedInputStream buffer(InputStream inputStream) {
        return IOUtil.buffer(inputStream);
    }

    public static BufferedInputStream buffer(InputStream inputStream, int size) {
        return IOUtil.buffer(inputStream, size);
    }

    public static BufferedOutputStream buffer(OutputStream outputStream) {
        return IOUtil.buffer(outputStream);
    }

    public static BufferedOutputStream buffer(OutputStream outputStream, int size) {
        return IOUtil.buffer(outputStream, size);
    }

    public static BufferedReader buffer(Reader reader) {
        return IOUtil.buffer(reader);
    }

    public static BufferedReader buffer(Reader reader, int size) {
        return IOUtil.buffer(reader, size);
    }

    public static BufferedWriter buffer(Writer writer) {
        return IOUtil.buffer(writer);
    }

    public static BufferedWriter buffer(Writer writer, int size) {
        return IOUtil.buffer(writer, size);
    }

    public static byte[] byteArray() {
        return IOUtil.byteArray();
    }

    public static byte[] byteArray(int size) {
        return IOUtil.byteArray(size);
    }

    public static void close(Closeable closeable) {
        IOUtil.closeQuietly(closeable);
    }

    public static void close(Closeable... closeables)
    {
        IOUtil.closeQuietly(closeables);
    }

    public static void close(Closeable closeable, IOConsumer<IOException> consumer) throws IOException {
        IOUtil.close(closeable, consumer);
    }

    public static void close(URLConnection conn) {
        IOUtil.close(conn);
    }

    public static void closeQuietly(Closeable closeable) {
        IOUtil.closeQuietly(closeable);
    }

    public static void closeQuietly(Closeable... closeables) {
        IOUtil.closeQuietly(closeables);
    }

    public static void closeQuietly(Closeable closeable, Consumer<IOException> consumer) {
        IOUtil.closeQuietly(closeable, consumer);
    }

    public static void closeQuietly(InputStream input) {
        IOUtil.closeQuietly(input);
    }

    public static void closeQuietly(OutputStream output) {
        IOUtil.closeQuietly(output);
    }

    public static void closeQuietly(Reader reader) {
        IOUtil.closeQuietly(reader);
    }

    public static void closeQuietly(Selector selector) {
        IOUtil.closeQuietly(selector);
    }

    public static void closeQuietly(ServerSocket serverSocket) {
        IOUtil.closeQuietly(serverSocket);
    }

    public static void closeQuietly(Socket socket) {
        IOUtil.closeQuietly(socket);
    }

    public static void closeQuietly(Writer writer) {
        IOUtil.closeQuietly(writer);
    }

    public static long consume(InputStream input) throws IOException {
        return IOUtil.consume(input);
    }

    public static void copyToFile(InputStream input, File _file) throws IOException {
        IOUtil.copyToFile(input, _file);
    }

    public static void copyToFile(InputStream input, String _file) throws IOException {
        IOUtil.copyToFile(input, _file);
    }

    public static void copyToFile(Reader input, File _file) throws IOException {
        IOUtil.copyToFile(input, _file);
    }

    public static void copyToFile(Reader input, String _file) throws IOException {
        IOUtil.copyToFile(input, _file);
    }

    public static void copyToFile(Reader input, File _file, Charset _cs) throws IOException {
        IOUtil.copyToFile(input, _file, _cs);
    }

    public static void copyToFile(Reader input, String _file, Charset _cs) throws IOException {
        IOUtil.copyToFile(input, _file, _cs);
    }

    public static void copyToFile(Reader input, File _file, String _cs) throws IOException {
        IOUtil.copyToFile(input, _file, _cs);
    }

    public static void copyToFile(Reader input, String _file, String _cs) throws IOException {
        IOUtil.copyToFile(input, _file, _cs);
    }

    public static void copy(InputStream input, OutputStream output) throws IOException {
        IOUtil.copy(input, output);
    }

    public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        IOUtil.copy(input, output, bufferSize);
    }

    public static void copy(Reader input, Writer output) throws IOException {
        IOUtil.copy(input, output);
    }

    public static long copy(URL url, File file) throws IOException {
        return IOUtil.copy(url, file);
    }

    public static long copy(URL url, OutputStream outputStream) throws IOException {
        return IOUtil.copy(url, outputStream);
    }

    public static long copyLarge(InputStream inputStream, OutputStream outputStream) throws IOException {
        return IOUtil.copyLarge(inputStream, outputStream);
    }

    public static long copyLarge(InputStream inputStream, OutputStream outputStream, byte[] buffer) throws IOException {
        return IOUtil.copyLarge(inputStream, outputStream, buffer);
    }

    public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length) throws IOException {
        return IOUtil.copyLarge(input, output, inputOffset, length);
    }

    public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length, byte[] buffer) throws IOException {
        return IOUtil.copyLarge(input, output, inputOffset, length, buffer);
    }

    public static long copyLarge(Reader reader, Writer writer) throws IOException {
        return IOUtil.copyLarge(reader, writer);
    }

    public static long copyLarge(Reader reader, Writer writer, char[] buffer) throws IOException {
        return IOUtil.copyLarge(reader, writer, buffer);
    }

    public static long copyLarge(Reader reader, Writer writer, long inputOffset, long length) throws IOException {
        return IOUtil.copyLarge(reader, writer, inputOffset, length);
    }

    public static long copyLarge(Reader reader, Writer writer, long inputOffset, long length, char[] buffer) throws IOException {
        return IOUtil.copyLarge(reader, writer, inputOffset, length, buffer);
    }

    public static int length(byte[] array) {
        return IOUtil.length(array);
    }

    public static int length(char[] array) {
        return IOUtil.length(array);
    }

    public static int length(CharSequence csq) {
        return IOUtil.length(csq);
    }

    public static String strip(String str) {
        return StringUtil.strip(str);
    }

    public static String strip(String str, String delim) {
        return StringUtil.strip(str, delim);
    }

    public static String stripAccents(String input) {
        return StringUtil.stripAccents(input);
    }

    public static String[] stripAll(String[] strs) {
        return StringUtil.stripAll(strs);
    }

    public static String[] stripAll(String[] strs, String delimiter) {
        return StringUtil.stripAll(strs, delimiter);
    }

    public static String stripEnd(String str, String strip) {
        return StringUtil.stripEnd(str, strip);
    }

    public static String stripStart(String str, String strip) {
        return StringUtil.stripStart(str, strip);
    }

    public static String stripToEmpty(String str) {
        return StringUtil.stripToEmpty(str);
    }

    public static String stripToNull(String str) {
        return StringUtil.stripToNull(str);
    }

    public static String upperCase(String str) {
        return StringUtil.upperCase(str);
    }

    public static String valueOf(char[] value) {
        return StringUtil.valueOf(value);
    }

    public static String wrap(String str, char wrapWith) {
        return StringUtil.wrap(str, wrapWith);
    }

    public static String wrap(String str, String wrapWith) {
        return StringUtil.wrap(str, wrapWith);
    }

    public static String wrapIfMissing(String str, char wrapWith) {
        return StringUtil.wrapIfMissing(str, wrapWith);
    }

    public static String wrapIfMissing(String str, String wrapWith) {
        return StringUtil.wrapIfMissing(str, wrapWith);
    }

    public static String lowerCase(String str) {
        return StringUtil.lowerCase(str);
    }

    public static String uncapitalise(String str) {
        return StringUtil.uncapitalise(str);
    }

    public static String capitalise(String str) {
        return StringUtil.capitalise(str);
    }

    public static String swapCase(String str) {
        return StringUtil.swapCase(str);
    }

    public static String initials(String str) {
        return StringUtil.initials(str);
    }

    public static String initials(String str, char... delimiters) {
        return StringUtil.initials(str, delimiters);
    }

    public static boolean containsAllWords(CharSequence word, CharSequence... words) {
        return StringUtil.containsAllWords(word, words);
    }

    @Deprecated
    public static boolean isDelimiter(char ch, char[] delimiters) {
        return StringUtil.isDelimiter(ch, delimiters);
    }

    @Deprecated
    public static boolean isDelimiter(int codePoint, char[] delimiters) {
        return StringUtil.isDelimiter(codePoint, delimiters);
    }

    public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
        return StringUtil.abbreviate(str, lower, upper, appendToEnd);
    }

    public static int[] toCodePoints(CharSequence str) {
        return StringUtil.toCodePoints(str);
    }

    public static String toEncodedString(byte[] bytes, Charset charset) {
        return StringUtil.toEncodedString(bytes, charset);
    }

    public static String toRootLowerCase(String source) {
        return StringUtil.toRootLowerCase(source);
    }

    public static String toRootUpperCase(String source) {
        return StringUtil.toRootUpperCase(source);
    }

    public static int length(Object[] array) {
        return IOUtil.length(array);
    }

    public static LineIterator lineIterator(InputStream input, Charset charset) {
        return IOUtil.lineIterator(input, charset);
    }

    public static LineIterator lineIterator(InputStream input, String charsetName) {
        return IOUtil.lineIterator(input, charsetName);
    }

    public static LineIterator lineIterator(Reader reader) {
        return IOUtil.lineIterator(reader);
    }

    public static int read(InputStream input, byte[] buffer) throws IOException {
        return IOUtil.read(input, buffer);
    }

    public static int read(InputStream input, byte[] buffer, int offset, int length) throws IOException {
        return IOUtil.read(input, buffer, offset, length);
    }

    public static int read(ReadableByteChannel input, ByteBuffer buffer) throws IOException {
        return IOUtil.read(input, buffer);
    }

    public static int read(Reader reader, char[] buffer) throws IOException {
        return IOUtil.read(reader, buffer);
    }

    public static int read(Reader reader, char[] buffer, int offset, int length) throws IOException {
        return IOUtil.read(reader, buffer, offset, length);
    }

    public static void readFully(InputStream input, byte[] buffer) throws IOException {
        IOUtil.readFully(input, buffer);
    }

    public static void readFully(InputStream input, byte[] buffer, int offset, int length) throws IOException {
        IOUtil.readFully(input, buffer, offset, length);
    }

    public static byte[] readFully(InputStream input, int length) throws IOException {
        return IOUtil.readFully(input, length);
    }

    public static void readFully(ReadableByteChannel input, ByteBuffer buffer) throws IOException {
        IOUtil.readFully(input, buffer);
    }

    public static void readFully(Reader reader, char[] buffer) throws IOException {
        IOUtil.readFully(reader, buffer);
    }

    public static void readFully(Reader reader, char[] buffer, int offset, int length) throws IOException {
        IOUtil.readFully(reader, buffer, offset, length);
    }

    @Deprecated
    public static List<String> readLines(InputStream input) throws IOException {
        return IOUtil.readLines(input);
    }

    public static List<String> readLines(InputStream input, Charset charset) throws IOException {
        return IOUtil.readLines(input, charset);
    }

    public static List<String> readLines(InputStream input, String charsetName) throws IOException {
        return IOUtil.readLines(input, charsetName);
    }

    public static List<String> readLines(Reader reader) throws IOException {
        return IOUtil.readLines(reader);
    }

    public static byte[] resourceToByteArray(String name) throws IOException {
        return IOUtil.resourceToByteArray(name);
    }

    public static byte[] resourceToByteArray(String name, ClassLoader classLoader) throws IOException {
        return IOUtil.resourceToByteArray(name, classLoader);
    }

    public static String resourceToString(String name, Charset charset) throws IOException {
        return IOUtil.resourceToString(name, charset);
    }

    public static String resourceToString(String name, Charset charset, ClassLoader classLoader) throws IOException {
        return IOUtil.resourceToString(name, charset, classLoader);
    }

    public static URL resourceToURL(String name) throws IOException {
        return IOUtil.resourceToURL(name);
    }

    public static URL resourceToURL(String name, ClassLoader classLoader) throws IOException {
        return IOUtil.resourceToURL(name, classLoader);
    }

    public static long skip(InputStream input, long toSkip) throws IOException {
        return IOUtil.skip(input, toSkip);
    }

    public static long skip(ReadableByteChannel input, long toSkip) throws IOException {
        return IOUtil.skip(input, toSkip);
    }

    public static long skip(Reader reader, long toSkip) throws IOException {
        return IOUtil.skip(reader, toSkip);
    }

    public static void skipFully(InputStream input, long toSkip) throws IOException {
        IOUtil.skipFully(input, toSkip);
    }

    public static void skipFully(ReadableByteChannel input, long toSkip) throws IOException {
        IOUtil.skipFully(input, toSkip);
    }

    public static void skipFully(Reader reader, long toSkip) throws IOException {
        IOUtil.skipFully(reader, toSkip);
    }

    public static InputStream toBufferedInputStream(InputStream input) throws IOException {
        return IOUtil.toBufferedInputStream(input);
    }

    public static InputStream toBufferedInputStream(InputStream input, int size) throws IOException {
        return IOUtil.toBufferedInputStream(input, size);
    }

    public static BufferedReader toBufferedReader(Reader reader) {
        return IOUtil.toBufferedReader(reader);
    }

    public static BufferedReader toBufferedReader(Reader reader, int size) {
        return IOUtil.toBufferedReader(reader, size);
    }

    public static void copy(Reader input, Writer output, int bufferSize) throws IOException {
        IOUtil.copy(input, output, bufferSize);
    }

    @Deprecated
    public static void copy(InputStream input, Writer output) throws IOException {
        IOUtil.copy(input, output);
    }

    public static void copy(InputStream input, Writer writer, Charset inputCharset) throws IOException {
        IOUtil.copy(input, writer, inputCharset);
    }

    public static void copy(InputStream input, Writer output, int bufferSize) throws IOException {
        IOUtil.copy(input, output, bufferSize);
    }

    public static void copy(InputStream input, Writer output, String encoding) throws IOException {
        IOUtil.copy(input, output, encoding);
    }

    public static long copy(Reader reader, Appendable output) throws IOException {
        return IOUtil.copy(reader, output);
    }

    public static long copy(Reader reader, Appendable output, CharBuffer buffer) throws IOException {
        return IOUtil.copy(reader, output, buffer);
    }

    public static void copy(InputStream input, Writer output, String encoding, int bufferSize) throws IOException {
        IOUtil.copy(input, output, encoding, bufferSize);
    }

    @Deprecated
    public static String toString(InputStream input) throws IOException {
        return IOUtil.toString(input);
    }

    public static String toString(InputStream input, Charset charset) throws IOException {
        return IOUtil.toString(input, charset);
    }

    public static String toString(InputStream input, int bufferSize) throws IOException {
        return IOUtil.toString(input, bufferSize);
    }

    public static String toString(InputStream input, String encoding) throws IOException {
        return IOUtil.toString(input, encoding);
    }

    public static String toString(InputStream input, String encoding, int bufferSize) throws IOException {
        return IOUtil.toString(input, encoding, bufferSize);
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        return IOUtil.toByteArray(input);
    }

    public static byte[] toByteArray(InputStream input, int bufferSize) throws IOException {
        return IOUtil.toByteArray(input, bufferSize);
    }

    public static byte[] toByteArray(InputStream input, long size) throws IOException {
        return IOUtil.toByteArray(input, size);
    }

    @Deprecated
    public static void copy(Reader input, OutputStream output) throws IOException {
        IOUtil.copy(input, output);
    }

    public static void copy(Reader reader, OutputStream output, Charset outputCharset) throws IOException {
        IOUtil.copy(reader, output, outputCharset);
    }

    public static void copy(Reader reader, OutputStream output, String outputCharsetName) throws IOException {
        IOUtil.copy(reader, output, outputCharsetName);
    }

    public static void copy(Reader input, OutputStream output, int bufferSize) throws IOException {
        IOUtil.copy(input, output, bufferSize);
    }

    public static String toString(Reader input) throws IOException {
        return IOUtil.toString(input);
    }

    @Deprecated
    public static String toString(URI uri) throws IOException {
        return IOUtil.toString(uri);
    }

    public static String toString(URI uri, Charset encoding) throws IOException {
        return IOUtil.toString(uri, encoding);
    }

    public static String toString(URI uri, String charsetName) throws IOException {
        return IOUtil.toString(uri, charsetName);
    }

    @Deprecated
    public static String toString(URL url) throws IOException {
        return IOUtil.toString(url);
    }

    public static String toString(URL url, Charset encoding) throws IOException {
        return IOUtil.toString(url, encoding);
    }

    public static String toString(URL url, String charsetName) throws IOException {
        return IOUtil.toString(url, charsetName);
    }

    public static void write(byte[] data, OutputStream output) throws IOException {
        IOUtil.write(data, output);
    }

    @Deprecated
    public static void write(byte[] data, Writer writer) throws IOException {
        IOUtil.write(data, writer);
    }

    public static void write(byte[] data, Writer writer, Charset charset) throws IOException {
        IOUtil.write(data, writer, charset);
    }

    public static void write(byte[] data, Writer writer, String charsetName) throws IOException {
        IOUtil.write(data, writer, charsetName);
    }

    @Deprecated
    public static void write(char[] data, OutputStream output) throws IOException {
        IOUtil.write(data, output);
    }

    public static void write(char[] data, OutputStream output, Charset charset) throws IOException {
        IOUtil.write(data, output, charset);
    }

    public static void write(char[] data, OutputStream output, String charsetName) throws IOException {
        IOUtil.write(data, output, charsetName);
    }

    public static void write(char[] data, Writer writer) throws IOException {
        IOUtil.write(data, writer);
    }

    @Deprecated
    public static void write(CharSequence data, OutputStream output) throws IOException {
        IOUtil.write(data, output);
    }

    public static void write(CharSequence data, OutputStream output, Charset charset) throws IOException {
        IOUtil.write(data, output, charset);
    }

    public static void write(CharSequence data, OutputStream output, String charsetName) throws IOException {
        IOUtil.write(data, output, charsetName);
    }

    public static void write(CharSequence data, Writer writer) throws IOException {
        IOUtil.write(data, writer);
    }

    @Deprecated
    public static void write(String data, OutputStream output) throws IOException {
        IOUtil.write(data, output);
    }

    public static void write(String data, OutputStream output, Charset charset) throws IOException {
        IOUtil.write(data, output, charset);
    }

    public static void write(String data, OutputStream output, String charsetName) throws IOException {
        IOUtil.write(data, output, charsetName);
    }

    public static void write(String data, Writer writer) throws IOException {
        IOUtil.write(data, writer);
    }

    @Deprecated
    public static void write(StringBuffer data, OutputStream output) throws IOException {
        IOUtil.write(data, output);
    }

    @Deprecated
    public static void write(StringBuffer data, OutputStream output, String charsetName) throws IOException {
        IOUtil.write(data, output, charsetName);
    }

    @Deprecated
    public static void write(StringBuffer data, Writer writer) throws IOException {
        IOUtil.write(data, writer);
    }

    public static void writeChunked(byte[] data, OutputStream output) throws IOException {
        IOUtil.writeChunked(data, output);
    }

    public static void writeChunked(char[] data, Writer writer) throws IOException {
        IOUtil.writeChunked(data, writer);
    }

    @Deprecated
    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output) throws IOException {
        IOUtil.writeLines(lines, lineEnding, output);
    }

    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, Charset charset) throws IOException {
        IOUtil.writeLines(lines, lineEnding, output, charset);
    }

    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, String charsetName) throws IOException {
        IOUtil.writeLines(lines, lineEnding, output, charsetName);
    }

    public static void writeLines(Collection<?> lines, String lineEnding, Writer writer) throws IOException {
        IOUtil.writeLines(lines, lineEnding, writer);
    }

    public static Writer writer(Appendable appendable) {
        return IOUtil.writer(appendable);
    }

    public static String toString(Reader input, int bufferSize) throws IOException {
        return IOUtil.toString(input, bufferSize);
    }

    @Deprecated
    public static byte[] toByteArray(Reader input) throws IOException {
        return IOUtil.toByteArray(input);
    }

    public static byte[] toByteArray(Reader reader, Charset charset) throws IOException {
        return IOUtil.toByteArray(reader, charset);
    }

    public static byte[] toByteArray(Reader reader, String charsetName) throws IOException {
        return IOUtil.toByteArray(reader, charsetName);
    }

    public static byte[] toByteArray(Reader input, int bufferSize) throws IOException {
        return IOUtil.toByteArray(input, bufferSize);
    }

    public static void copy(String input, OutputStream output) throws IOException {
        IOUtil.copy(input, output);
    }

    public static void copy(String input, OutputStream output, int bufferSize) throws IOException {
        IOUtil.copy(input, output, bufferSize);
    }

    public static void copy(String input, Writer output) throws IOException {
        IOUtil.copy(input, output);
    }

    public static void bufferedCopy(InputStream input, OutputStream output) throws IOException {
        IOUtil.bufferedCopy(input, output);
    }

    @Deprecated
    public static byte[] toByteArray(String input) throws IOException {
        return IOUtil.toByteArray(input);
    }

    public static byte[] toByteArray(URI uri) throws IOException {
        return IOUtil.toByteArray(uri);
    }

    public static byte[] toByteArray(URL url) throws IOException {
        return IOUtil.toByteArray(url);
    }

    public static byte[] toByteArray(URLConnection urlConn) throws IOException {
        return IOUtil.toByteArray(urlConn);
    }

    @Deprecated
    public static char[] toCharArray(InputStream inputStream) throws IOException {
        return IOUtil.toCharArray(inputStream);
    }

    public static char[] toCharArray(InputStream inputStream, Charset charset) throws IOException {
        return IOUtil.toCharArray(inputStream, charset);
    }

    public static char[] toCharArray(InputStream inputStream, String charsetName) throws IOException {
        return IOUtil.toCharArray(inputStream, charsetName);
    }

    public static char[] toCharArray(Reader reader) throws IOException {
        return IOUtil.toCharArray(reader);
    }

    @Deprecated
    public static InputStream toInputStream(CharSequence input) {
        return IOUtil.toInputStream(input);
    }

    public static InputStream toInputStream(CharSequence input, Charset charset) {
        return IOUtil.toInputStream(input, charset);
    }

    public static InputStream toInputStream(CharSequence input, String charsetName) {
        return IOUtil.toInputStream(input, charsetName);
    }

    @Deprecated
    public static InputStream toInputStream(String input) {
        return IOUtil.toInputStream(input);
    }

    public static InputStream toInputStream(String input, Charset charset) {
        return IOUtil.toInputStream(input, charset);
    }

    public static InputStream toInputStream(String input, String charsetName) {
        return IOUtil.toInputStream(input, charsetName);
    }

    public static byte[] toByteArray(String input, int bufferSize) throws IOException {
        return IOUtil.toByteArray(input, bufferSize);
    }

    public static void copy(byte[] input, Writer output) throws IOException {
        IOUtil.copy(input, output);
    }

    public static void copy(byte[] input, Writer output, int bufferSize) throws IOException {
        IOUtil.copy(input, output, bufferSize);
    }

    public static void copy(byte[] input, Writer output, String encoding) throws IOException {
        IOUtil.copy(input, output, encoding);
    }

    public static void copy(byte[] input, Writer output, String encoding, int bufferSize) throws IOException {
        IOUtil.copy(input, output, encoding, bufferSize);
    }

    @Deprecated
    public static String toString(byte[] input) throws IOException {
        return IOUtil.toString(input);
    }

    public static String toString(byte[] input, int bufferSize) throws IOException {
        return IOUtil.toString(input, bufferSize);
    }

    @Deprecated
    public static String toString(byte[] input, String encoding) throws IOException {
        return IOUtil.toString(input, encoding);
    }

    public static String capitaliseAllWords(String str) {
        return StringUtil.capitaliseAllWords(str);
    }

    public static String uncapitaliseAllWords(String str) {
        return StringUtil.uncapitaliseAllWords(str);
    }

    public static String getNestedString(String str, String tag) {
        return StringUtil.getNestedString(str, tag);
    }

    public static String getNestedString(String str, String open, String close) {
        return StringUtil.getNestedString(str, open, close);
    }

    public static int countMatches(String str, String sub) {
        return StringUtil.countMatches(str, sub);
    }

    public static boolean isAlpha(String str) {
        return StringUtil.isAlpha(str);
    }

    public static boolean isWhitespace(String str) {
        return StringUtil.isWhitespace(str);
    }

    public static boolean isAlphaSpace(String str) {
        return StringUtil.isAlphaSpace(str);
    }

    public static boolean isAlphanumeric(String str) {
        return StringUtil.isAlphanumeric(str);
    }

    public static boolean isAlphanumericSpace(String str) {
        return StringUtil.isAlphanumericSpace(str);
    }

    public static boolean isNumeric(String str) {
        return StringUtil.isNumeric(str);
    }

    public static boolean isNumericSpace(String str) {
        return StringUtil.isNumericSpace(str);
    }

    @Deprecated
    public static String defaultString(Object obj) {
        return StringUtil.defaultString(obj);
    }

    @Deprecated
    public static String defaultString(Object obj, String defaultString) {
        return StringUtil.defaultString(obj, defaultString);
    }

    public static String reverse(String str) {
        return StringUtil.reverse(str);
    }

    public static String reverseDelimited(String str, char separatorChar) {
        return StringUtil.reverseDelimited(str, separatorChar);
    }

    public static String reverseDelimitedString(String str, String delimiter) {
        return StringUtil.reverseDelimitedString(str, delimiter);
    }

    public static String abbreviate(String s, int maxWidth) {
        return StringUtil.abbreviate(s, maxWidth);
    }

    public static String abbreviate(String s, int offset, int maxWidth) {
        return StringUtil.abbreviate(s, offset, maxWidth);
    }

    public static String abbreviate(String str, String abbrevMarker, int maxWidth) {
        return StringUtil.abbreviate(str, abbrevMarker, maxWidth);
    }

    public static String abbreviate(String str, String abbrevMarker, int offset, int maxWidth) {
        return StringUtil.abbreviate(str, abbrevMarker, offset, maxWidth);
    }

    public static String abbreviateMiddle(String str, String middle, int length) {
        return StringUtil.abbreviateMiddle(str, middle, length);
    }

    public static String appendIfMissing(String str, CharSequence suffix, CharSequence... suffixes) {
        return StringUtil.appendIfMissing(str, suffix, suffixes);
    }

    public static String appendIfMissingIgnoreCase(String str, CharSequence suffix, CharSequence... suffixes) {
        return StringUtil.appendIfMissingIgnoreCase(str, suffix, suffixes);
    }

    public static String capitalize(String str) {
        return StringUtil.capitalize(str);
    }

    public static String capitalize(String str, char... delimiters) {
        return StringUtil.capitalize(str, delimiters);
    }

    public static String capitalizeFully(String str) {
        return StringUtil.capitalizeFully(str);
    }

    public static String capitalizeFully(String str, char... delimiters) {
        return StringUtil.capitalizeFully(str, delimiters);
    }

    public static String difference(String s1, String s2) {
        return StringUtil.difference(s1, s2);
    }

    public static boolean endsWith(CharSequence str, CharSequence suffix) {
        return StringUtil.endsWith(str, suffix);
    }

    public static boolean endsWithAny(CharSequence sequence, CharSequence... searchStrings) {
        return StringUtil.endsWithAny(sequence, searchStrings);
    }

    public static boolean endsWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return StringUtil.endsWithIgnoreCase(str, suffix);
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        return StringUtil.equals(cs1, cs2);
    }

    public static boolean equalsAny(CharSequence string, CharSequence... searchStrings) {
        return StringUtil.equalsAny(string, searchStrings);
    }

    public static boolean equalsAnyIgnoreCase(CharSequence string, CharSequence... searchStrings) {
        return StringUtil.equalsAnyIgnoreCase(string, searchStrings);
    }

    public static boolean equalsIgnoreCase(CharSequence cs1, CharSequence cs2) {
        return StringUtil.equalsIgnoreCase(cs1, cs2);
    }

    @SafeVarargs
    public static <T extends CharSequence> T firstNonBlank(T... values) {
        return StringUtil.firstNonBlank(values);
    }

    @SafeVarargs
    public static <T extends CharSequence> T firstNonEmpty(T... values) {
        return StringUtil.firstNonEmpty(values);
    }

    public static byte[] getBytes(String string, Charset charset) {
        return StringUtil.getBytes(string, charset);
    }

    public static byte[] getBytes(String string, String charset) throws UnsupportedEncodingException {
        return StringUtil.getBytes(string, charset);
    }

    public static String getCommonPrefix(String... strs) {
        return StringUtil.getCommonPrefix(strs);
    }

    public static String getDigits(String str) {
        return StringUtil.getDigits(str);
    }

    @Deprecated
    public static int getFuzzyDistance(CharSequence term, CharSequence query, Locale locale) {
        return StringUtil.getFuzzyDistance(term, query, locale);
    }

    public static <T extends CharSequence> T getIfBlank(T str, Supplier<T> defaultSupplier) {
        return StringUtil.getIfBlank(str, defaultSupplier);
    }

    public static <T extends CharSequence> T getIfEmpty(T str, Supplier<T> defaultSupplier) {
        return StringUtil.getIfEmpty(str, defaultSupplier);
    }

    @Deprecated
    public static double getJaroWinklerDistance(CharSequence first, CharSequence second) {
        return StringUtil.getJaroWinklerDistance(first, second);
    }

    @Deprecated
    public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
        return StringUtil.getLevenshteinDistance(s, t);
    }

    @Deprecated
    public static int getLevenshteinDistance(CharSequence s, CharSequence t, int threshold) {
        return StringUtil.getLevenshteinDistance(s, t, threshold);
    }

    public static int indexOf(CharSequence seq, CharSequence searchSeq) {
        return StringUtil.indexOf(seq, searchSeq);
    }

    public static int indexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
        return StringUtil.indexOf(seq, searchSeq, startPos);
    }

    public static int indexOf(CharSequence seq, int searchChar) {
        return StringUtil.indexOf(seq, searchChar);
    }

    public static int indexOf(CharSequence seq, int searchChar, int startPos) {
        return StringUtil.indexOf(seq, searchChar, startPos);
    }

    public static int indexOfAny(CharSequence cs, char... searchChars) {
        return StringUtil.indexOfAny(cs, searchChars);
    }

    public static int indexOfAny(CharSequence str, CharSequence... searchStrs) {
        return StringUtil.indexOfAny(str, searchStrs);
    }

    public static int indexOfAny(CharSequence cs, String searchChars) {
        return StringUtil.indexOfAny(cs, searchChars);
    }

    public static int indexOfAnyBut(CharSequence cs, char... searchChars) {
        return StringUtil.indexOfAnyBut(cs, searchChars);
    }

    public static int indexOfAnyBut(CharSequence seq, CharSequence searchChars) {
        return StringUtil.indexOfAnyBut(seq, searchChars);
    }

    public static int indexOfDifference(CharSequence... css) {
        return StringUtil.indexOfDifference(css);
    }

    public static int indexOfDifference(CharSequence cs1, CharSequence cs2) {
        return StringUtil.indexOfDifference(cs1, cs2);
    }

    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return StringUtil.indexOfIgnoreCase(str, searchStr);
    }

    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        return StringUtil.indexOfIgnoreCase(str, searchStr, startPos);
    }

    public static boolean isAllBlank(CharSequence... css) {
        return StringUtil.isAllBlank(css);
    }

    public static boolean isAllEmpty(CharSequence... css) {
        return StringUtil.isAllEmpty(css);
    }

    public static int differenceAt(String s1, String s2) {
        return StringUtil.differenceAt(s1, s2);
    }

    public static String interpolate(String text, Map<?, ?> namespace) {
        return StringUtil.interpolate(text, namespace);
    }

    public static String removeAndHump(String data, String replaceThis) {
        return StringUtil.removeAndHump(data, replaceThis);
    }

    public static String capitalizeFirstLetter(String data) {
        return StringUtil.capitalizeFirstLetter(data);
    }

    public static String lowercaseFirstLetter(String data) {
        return StringUtil.lowercaseFirstLetter(data);
    }

    public static String addAndDeHump(String view) {
        return StringUtil.addAndDeHump(view);
    }

    public static String quoteAndEscape(String source, char quoteChar) {
        return StringUtil.quoteAndEscape(source, quoteChar);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] quotingTriggers) {
        return StringUtil.quoteAndEscape(source, quoteChar, quotingTriggers);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] escapedChars, char escapeChar, boolean force) {
        return StringUtil.quoteAndEscape(source, quoteChar, escapedChars, escapeChar, force);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] escapedChars, char[] quotingTriggers, char escapeChar, boolean force) {
        return StringUtil.quoteAndEscape(source, quoteChar, escapedChars, quotingTriggers, escapeChar, force);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] escapedChars, char[] quotingTriggers, String escapePattern, boolean force) {
        return StringUtil.quoteAndEscape(source, quoteChar, escapedChars, quotingTriggers, escapePattern, force);
    }

    public static String escape(String source, char[] escapedChars, char escapeChar) {
        return StringUtil.escape(source, escapedChars, escapeChar);
    }

    public static String escape(String source, char[] escapedChars, String escapePattern) {
        return StringUtil.escape(source, escapedChars, escapePattern);
    }

    public static String removeDuplicateWhitespace(String s) {
        return StringUtil.removeDuplicateWhitespace(s);
    }

    public static String unifyLineSeparators(String s) {
        return StringUtil.unifyLineSeparators(s);
    }

    public static String unifyLineSeparators(String s, String ls) {
        return StringUtil.unifyLineSeparators(s, ls);
    }

    public static boolean contains(String str, char searchChar) {
        return StringUtil.contains(str, searchChar);
    }

    public static boolean contains(String str, String searchStr) {
        return StringUtil.contains(str, searchStr);
    }

    public static <T> T[] toArray(T... _args) {
        return StringUtil.toArray(_args);
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3, Object _a4, Object _a5, Object _a6) {
        return StringUtil.toArray(_a1, _a2, _a3, _a4, _a5, _a6);
    }

    public static String[] toArray(String _a1, String _a2, String _a3, String _a4, String _a5, String _a6) {
        return StringUtil.toArray(_a1, _a2, _a3, _a4, _a5, _a6);
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3, Object _a4, Object _a5) {
        return StringUtil.toArray(_a1, _a2, _a3, _a4, _a5);
    }

    public static String[] toArray(String _a1, String _a2, String _a3, String _a4, String _a5) {
        return StringUtil.toArray(_a1, _a2, _a3, _a4, _a5);
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3, Object _a4) {
        return StringUtil.toArray(_a1, _a2, _a3, _a4);
    }

    public static String[] toArray(String _a1, String _a2, String _a3, String _a4) {
        return StringUtil.toArray(_a1, _a2, _a3, _a4);
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3) {
        return StringUtil.toArray(_a1, _a2, _a3);
    }

    public static String[] toArray(String _a1, String _a2, String _a3) {
        return StringUtil.toArray(_a1, _a2, _a3);
    }

    public static Object[] toArray(Object _a1, Object _a2) {
        return StringUtil.toArray(_a1, _a2);
    }

    public static String[] toArray(String _a1, String _a2) {
        return StringUtil.toArray(_a1, _a2);
    }

    public static Object[] toArray(Object _a1) {
        return StringUtil.toArray(_a1);
    }

    public static String[] toArray(String _a1) {
        return StringUtil.toArray(_a1);
    }

    public static boolean isEmpty(CharSequence cs) {
        return StringUtil.isEmpty(cs);
    }

    public static boolean isMixedCase(CharSequence cs) {
        return StringUtil.isMixedCase(cs);
    }

    public static boolean isNoneBlank(CharSequence... css) {
        return StringUtil.isNoneBlank(css);
    }

    public static boolean isNoneEmpty(CharSequence... css) {
        return StringUtil.isNoneEmpty(css);
    }

    public static boolean isNotBlank(CharSequence cs) {
        return StringUtil.isNotBlank(cs);
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return StringUtil.isNotEmpty(cs);
    }

    public static boolean isNumeric(CharSequence cs) {
        return StringUtil.isNumeric(cs);
    }

    public static boolean isNumericSpace(CharSequence cs) {
        return StringUtil.isNumericSpace(cs);
    }

    public static boolean isWhitespace(CharSequence cs) {
        return StringUtil.isWhitespace(cs);
    }

    public static String join(byte[] array, char separator) {
        return StringUtil.join(array, separator);
    }

    public static String join(byte[] array, char separator, int startIndex, int endIndex) {
        return StringUtil.join(array, separator, startIndex, endIndex);
    }

    public static String join(char[] array, char separator) {
        return StringUtil.join(array, separator);
    }

    public static String join(char[] array, char separator, int startIndex, int endIndex) {
        return StringUtil.join(array, separator, startIndex, endIndex);
    }

    public static String join(double[] array, char separator) {
        return StringUtil.join(array, separator);
    }

    public static String join(double[] array, char separator, int startIndex, int endIndex) {
        return StringUtil.join(array, separator, startIndex, endIndex);
    }

    public static String join(float[] array, char separator) {
        return StringUtil.join(array, separator);
    }

    public static String join(float[] array, char separator, int startIndex, int endIndex) {
        return StringUtil.join(array, separator, startIndex, endIndex);
    }

    public static String join(int[] array, char separator) {
        return StringUtil.join(array, separator);
    }

    public static String join(int[] array, char separator, int startIndex, int endIndex) {
        return StringUtil.join(array, separator, startIndex, endIndex);
    }

    public static String join(Iterable<?> iterable, char separator) {
        return StringUtil.join(iterable, separator);
    }

    public static String join(Iterable<?> iterable, String separator) {
        return StringUtil.join(iterable, separator);
    }

    public static String join(Iterator<?> iterator, char separator) {
        return StringUtil.join(iterator, separator);
    }

    public static String join(Iterator<?> iterator, String separator) {
        return StringUtil.join(iterator, separator);
    }

    public static String join(List<?> list, char separator, int startIndex, int endIndex) {
        return StringUtil.join(list, separator, startIndex, endIndex);
    }

    public static String join(List<?> list, String separator, int startIndex, int endIndex) {
        return StringUtil.join(list, separator, startIndex, endIndex);
    }

    public static String join(long[] array, char separator) {
        return StringUtil.join(array, separator);
    }

    public static String join(long[] array, char separator, int startIndex, int endIndex) {
        return StringUtil.join(array, separator, startIndex, endIndex);
    }

    public static String join(Object[] array, char separator) {
        return StringUtil.join(array, separator);
    }

    public static String join(Object[] array, char separator, int startIndex, int endIndex) {
        return StringUtil.join(array, separator, startIndex, endIndex);
    }

    public static boolean isAsciiPrintable(CharSequence cs) {
        return StringUtil.isAsciiPrintable(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        return StringUtil.isBlank(cs);
    }

    public static boolean isAllLowerCase(CharSequence cs) {
        return StringUtil.isAllLowerCase(cs);
    }

    public static boolean isAllUpperCase(CharSequence cs) {
        return StringUtil.isAllUpperCase(cs);
    }

    public static boolean isAlpha(CharSequence cs) {
        return StringUtil.isAlpha(cs);
    }

    public static boolean isAlphanumeric(CharSequence cs) {
        return StringUtil.isAlphanumeric(cs);
    }

    public static boolean isAlphanumericSpace(CharSequence cs) {
        return StringUtil.isAlphanumericSpace(cs);
    }

    public static boolean isAlphaSpace(CharSequence cs) {
        return StringUtil.isAlphaSpace(cs);
    }

    public static boolean isAnyBlank(CharSequence... css) {
        return StringUtil.isAnyBlank(css);
    }

    public static boolean isAnyEmpty(CharSequence... css) {
        return StringUtil.isAnyEmpty(css);
    }

    public static String clean(String str) {
        return StringUtil.clean(str);
    }

    public static CharSequence subSequence(CharSequence cs, int start) {
        return StringUtil.subSequence(cs, start);
    }

    public static char[] toCharArray(CharSequence source) {
        return StringUtil.toCharArray(source);
    }

    public static String wrap(String str, int wrapLength) {
        return StringUtil.wrap(str, wrapLength);
    }

    public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords) {
        return StringUtil.wrap(str, wrapLength, newLineStr, wrapLongWords);
    }

    public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords, String wrapOn) {
        return StringUtil.wrap(str, wrapLength, newLineStr, wrapLongWords, wrapOn);
    }

    public static String toCamelCase(String str, boolean capitalizeFirstLetter, char... delimiters) {
        return StringUtil.toCamelCase(str, capitalizeFirstLetter, delimiters);
    }

    public static String escapeJava(String input) {
        return StringUtil.escapeJava(input);
    }

    public static String escapeEcmaScript(String input) {
        return StringUtil.escapeEcmaScript(input);
    }

    public static String escapeJson(String input) {
        return StringUtil.escapeJson(input);
    }

    public static String unescapeJava(String input) {
        return StringUtil.unescapeJava(input);
    }

    public static String unescapeEcmaScript(String input) {
        return StringUtil.unescapeEcmaScript(input);
    }

    public static String unescapeJson(String input) {
        return StringUtil.unescapeJson(input);
    }

    public static String escapeHtml4(String input) {
        return StringUtil.escapeHtml4(input);
    }

    public static String escapeHtml3(String input) {
        return StringUtil.escapeHtml3(input);
    }

    public static String unescapeHtml4(String input) {
        return StringUtil.unescapeHtml4(input);
    }

    public static String unescapeHtml3(String input) {
        return StringUtil.unescapeHtml3(input);
    }

    @Deprecated
    public static String escapeXml(String input) {
        return StringUtil.escapeXml(input);
    }

    public static String escapeXml10(String input) {
        return StringUtil.escapeXml10(input);
    }

    public static String escapeXml11(String input) {
        return StringUtil.escapeXml11(input);
    }

    public static String unescapeXml(String input) {
        return StringUtil.unescapeXml(input);
    }

    public static String escapeCsv(String input) {
        return StringUtil.escapeCsv(input);
    }

    public static String unescapeCsv(String input) {
        return StringUtil.unescapeCsv(input);
    }

    public static String toString(byte[] input, String encoding, int bufferSize) throws IOException {
        return IOUtil.toString(input, encoding, bufferSize);
    }

    public static void copy(byte[] input, OutputStream output) throws IOException {
        IOUtil.copy(input, output);
    }

    public static void copy(byte[] input, OutputStream output, int bufferSize) throws IOException {
        IOUtil.copy(input, output, bufferSize);
    }

    public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
        return IOUtil.contentEquals(input1, input2);
    }

    public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
        return IOUtil.contentEquals(input1, input2);
    }

    public static boolean contentEqualsIgnoreEOL(Reader reader1, Reader reader2) throws IOException {
        return IOUtil.contentEqualsIgnoreEOL(reader1, reader2);
    }

    public static void close(InputStream inputStream) {
        IOUtil.closeQuietly(inputStream);
    }

    public static void close(Channel channel) {
        IOUtil.closeQuietly(channel);
    }

    public static void close(OutputStream outputStream) {
        IOUtil.closeQuietly(outputStream);
    }

    public static void close(Reader reader) {
        IOUtil.closeQuietly(reader);
    }

    public static void close(OutputStreamWriter writer) {
        IOUtil.closeQuietly(writer);
    }

    //*************************************************************************
    // static NumberUtil _s0;

    public static String toBase(int _base, long _n) {
        return NumberUtil.toBase(_base, _n);
    }

    public static String toBase(int _base, long _n, int _min) {
        return NumberUtil.toBase(_base, _n, _min);
    }

    public static String decimalToAscii(String _text) {
        return NumberUtil.decimalToAscii(_text);
    }

    public static String decimalToHex(String _text) {
        return NumberUtil.decimalToHex(_text);
    }

    public static String decimalToHex(String _text, String _sep) {
        return NumberUtil.decimalToHex(_text, _sep);
    }

    public static String decimalToHex(String _text, String _sep, String _delim) {
        return NumberUtil.decimalToHex(_text, _sep, _delim);
    }

    public static int checkInt(Object _str) {
        return NumberUtil.checkInt(_str);
    }

    public static int checkInt(String _str) {
        return NumberUtil.checkInt(_str);
    }

    public static Integer checkInteger(Object _str) {
        return NumberUtil.checkInteger(_str);
    }

    public static Float checkFloat(Object _str) {
        return NumberUtil.checkFloat(_str);
    }

    public static Float checkFloat(String _str) {
        return NumberUtil.checkFloat(_str);
    }

    public static Double checkDouble(Object _str) {
        return NumberUtil.checkDouble(_str);
    }

    public static Double checkDouble(String _str) {
        return NumberUtil.checkDouble(_str);
    }

    public static Integer checkInteger(String _str) {
        return NumberUtil.checkInteger(_str);
    }

    public static Integer createInteger(String _str) {
        return NumberUtil.createInteger(_str);
    }

    public static Long checkLong(Object _str) {
        return NumberUtil.checkLong(_str);
    }

    public static Long checkLong(String _str) {
        return NumberUtil.checkLong(_str);
    }

    public static BigInteger checkBigInt(String _str) {
        return NumberUtil.checkBigInt(_str);
    }

    public static BigInteger checkBigInteger(String _str) {
        return NumberUtil.checkBigInteger(_str);
    }

    public static BigDecimal checkBigNum(String _str) {
        return NumberUtil.checkBigNum(_str);
    }

    public static BigDecimal checkBigDecimal(String _str) {
        return NumberUtil.checkBigDecimal(_str);
    }

    public static int toInt(String str) {
        return NumberUtil.toInt(str);
    }

    public static int toInt(String str, int defaultValue) {
        return NumberUtil.toInt(str, defaultValue);
    }

    public static long toLong(String str) {
        return NumberUtil.toLong(str);
    }

    public static long toLong(String str, long defaultValue) {
        return NumberUtil.toLong(str, defaultValue);
    }

    public static float toFloat(String str) {
        return NumberUtil.toFloat(str);
    }

    public static float toFloat(String str, float defaultValue) {
        return NumberUtil.toFloat(str, defaultValue);
    }

    public static double toDouble(String str) {
        return NumberUtil.toDouble(str);
    }

    public static double toDouble(String str, double defaultValue) {
        return NumberUtil.toDouble(str, defaultValue);
    }

    public static double toDouble(BigDecimal value) {
        return NumberUtil.toDouble(value);
    }

    public static double toDouble(BigDecimal value, double defaultValue) {
        return NumberUtil.toDouble(value, defaultValue);
    }

    public static byte toByte(String str) {
        return NumberUtil.toByte(str);
    }

    public static byte toByte(String str, byte defaultValue) {
        return NumberUtil.toByte(str, defaultValue);
    }

    public static short toShort(String str) {
        return NumberUtil.toShort(str);
    }

    public static short toShort(String str, short defaultValue) {
        return NumberUtil.toShort(str, defaultValue);
    }

    public static BigDecimal toScaledBigDecimal(BigDecimal value) {
        return NumberUtil.toScaledBigDecimal(value);
    }

    public static BigDecimal toScaledBigDecimal(BigDecimal value, int scale, RoundingMode roundingMode) {
        return NumberUtil.toScaledBigDecimal(value, scale, roundingMode);
    }

    public static BigDecimal toScaledBigDecimal(Float value) {
        return NumberUtil.toScaledBigDecimal(value);
    }

    public static BigDecimal toScaledBigDecimal(Float value, int scale, RoundingMode roundingMode) {
        return NumberUtil.toScaledBigDecimal(value, scale, roundingMode);
    }

    public static BigDecimal toScaledBigDecimal(Double value) {
        return NumberUtil.toScaledBigDecimal(value);
    }

    public static BigDecimal toScaledBigDecimal(Double value, int scale, RoundingMode roundingMode) {
        return NumberUtil.toScaledBigDecimal(value, scale, roundingMode);
    }

    public static BigDecimal toScaledBigDecimal(String value) {
        return NumberUtil.toScaledBigDecimal(value);
    }

    public static BigDecimal toScaledBigDecimal(String value, int scale, RoundingMode roundingMode) {
        return NumberUtil.toScaledBigDecimal(value, scale, roundingMode);
    }

    public static Number createNumber(String str) {
        return NumberUtil.createNumber(str);
    }

    public static Float createFloat(String str) {
        return NumberUtil.createFloat(str);
    }

    public static Double createDouble(String str) {
        return NumberUtil.createDouble(str);
    }

    public static Long createLong(String str) {
        return NumberUtil.createLong(str);
    }

    public static BigInteger createBigInteger(String str) {
        return NumberUtil.createBigInteger(str);
    }

    public static BigDecimal createBigDecimal(String str) {
        return NumberUtil.createBigDecimal(str);
    }

    public static long min(long... array) {
        return MathUtil.min(array);
    }

    public static int min(int... array) {
        return MathUtil.min(array);
    }

    public static short min(short... array) {
        return MathUtil.min(array);
    }

    public static byte min(byte... array) {
        return MathUtil.min(array);
    }

    public static double min(double... array) {
        return MathUtil.min(array);
    }

    public static float min(float... array) {
        return MathUtil.min(array);
    }

    public static long max(long... array) {
        return MathUtil.max(array);
    }

    public static int max(int... array) {
        return MathUtil.max(array);
    }

    public static short max(short... array) {
        return MathUtil.max(array);
    }

    public static byte max(byte... array) {
        return MathUtil.max(array);
    }

    public static double max(double... array) {
        return MathUtil.max(array);
    }

    public static float max(float... array) {
        return MathUtil.max(array);
    }

    public static long min(long a, long b, long c) {
        return MathUtil.min(a, b, c);
    }

    public static int min(int a, int b, int c) {
        return MathUtil.min(a, b, c);
    }

    public static short min(short a, short b, short c) {
        return MathUtil.min(a, b, c);
    }

    public static byte min(byte a, byte b, byte c) {
        return MathUtil.min(a, b, c);
    }

    public static double min(double a, double b, double c) {
        return MathUtil.min(a, b, c);
    }

    public static float min(float a, float b, float c) {
        return MathUtil.min(a, b, c);
    }

    public static long max(long a, long b, long c) {
        return MathUtil.max(a, b, c);
    }

    public static int max(int a, int b, int c) {
        return MathUtil.max(a, b, c);
    }

    public static short max(short a, short b, short c) {
        return MathUtil.max(a, b, c);
    }

    public static byte max(byte a, byte b, byte c) {
        return MathUtil.max(a, b, c);
    }

    public static double max(double a, double b, double c) {
        return MathUtil.max(a, b, c);
    }

    public static float max(float a, float b, float c) {
        return MathUtil.max(a, b, c);
    }

    public static boolean isDigits(String str) {
        return NumberUtil.isDigits(str);
    }

    @Deprecated
    public static boolean isNumber(String str) {
        return NumberUtil.isNumber(str);
    }

    public static boolean isNumberCreatable(String str) {
        return NumberUtil.isCreatable(str);
    }

    public static boolean isParsable(String str) {
        return NumberUtil.isParsable(str);
    }

    public static int compare(int x, int y) {
        return NumberUtil.compare(x, y);
    }

    public static int compare(long x, long y) {
        return NumberUtil.compare(x, y);
    }

    public static int compare(short x, short y) {
        return NumberUtil.compare(x, y);
    }

    public static int compare(byte x, byte y) {
        return NumberUtil.compare(x, y);
    }

    //*************************************************************************
    // static StringUtil _s0;

    public static String regReplace(String _text, String _expr, String _repl) {
        return StringUtil.regReplace(_text, _expr, _repl);
    }

    public static String regReplace(String _text, String _expr, String _repl, int _count) {
        return StringUtil.regReplace(_text, _expr, _repl, _count);
    }

    public static String extract(String s, String rx) {
        return StringUtil.extract(s, rx);
    }

    public static String[] extractN(String s, String rx) {
        return StringUtil.extractN(s, rx);
    }

    public static String formatMsg(String _fmt, Object... _params) {
        return StringUtil.formatMsg(_fmt, _params);
    }

    public static String format(String _fmt, Object... _params) {
        return StringUtil.format(_fmt, _params);
    }

    public static String mformat(String _pattern, Object... _objs) {
        return StringUtil.mformat(_pattern, _objs);
    }

    public static String sformat(String _pattern, Object... _objs) {
        return StringUtil.sformat(_pattern, _objs);
    }

    public static boolean fnmatch(String a, String fx) {
        return StringUtil.fnmatch(a, fx);
    }

    public static boolean wcmatch(String expr, String value) {
        return StringUtil.wcmatch(expr, value);
    }

    public static int countPrefix(String _text, char _c) {
        return StringUtil.countPrefix(_text, _c);
    }

    public static int countSuffix(String _text, char _c) {
        return StringUtil.countSuffix(_text, _c);
    }

    public static String checkStringDefaultIfNullOrBlank(Object _str, String _def, String _blank) {
        return StringUtil.checkStringDefaultIfNullOrBlank(_str, _def, _blank);
    }

    public static String checkStringDefaultIfNullOrBlank(String _str, String _def, String _blank) {
        return StringUtil.checkStringDefaultIfNullOrBlank(_str, _def, _blank);
    }

    public static String checkStringDefaultIfNull(Object _str, String _def) {
        return StringUtil.checkStringDefaultIfNull(_str, _def);
    }

    public static String checkStringDefaultIfNull(String _str, String _def) {
        return StringUtil.checkStringDefaultIfNull(_str, _def);
    }

    public static String checkStringDefaultIfBlank(Object _str, String _blank) {
        return StringUtil.checkStringDefaultIfBlank(_str, _blank);
    }

    public static String checkStringDefaultIfBlank(String _str, String _blank) {
        return StringUtil.checkStringDefaultIfBlank(_str, _blank);
    }

    public static String checkString(Object _str) {
        return StringUtil.checkString(_str);
    }

    public static String checkString(String _str) {
        return StringUtil.checkString(_str);
    }

    public static String toString(Object _o) {
        return StringUtil.toString(_o);
    }

    public static String stringClean(String str) {
        return StringUtil.cleanString(str);
    }

    public static String cleanString(String str) {
        return StringUtil.cleanString(str);
    }

    public static String trim(String str) {
        return StringUtil.trim(str);
    }

    public static String trimToEmpty(String str) {
        return StringUtil.trimToEmpty(str);
    }

    public static String trimToNull(String str) {
        return StringUtil.trimToNull(str);
    }

    public static String truncate(String str, int maxWidth) {
        return StringUtil.truncate(str, maxWidth);
    }

    public static String truncate(String str, int offset, int maxWidth) {
        return StringUtil.truncate(str, offset, maxWidth);
    }

    public static String uncapitalize(String str) {
        return StringUtil.uncapitalize(str);
    }

    public static String uncapitalize(String str, char... delimiters) {
        return StringUtil.uncapitalize(str, delimiters);
    }

    public static String unwrap(String str, char wrapChar) {
        return StringUtil.unwrap(str, wrapChar);
    }

    public static String unwrap(String str, String wrapToken) {
        return StringUtil.unwrap(str, wrapToken);
    }

    public static boolean isNotEmpty(String str) {
        return StringUtil.isNotEmpty(str);
    }

    public static boolean isEmpty(String str) {
        return StringUtil.isEmpty(str);
    }

    public static boolean isBlank(String str) {
        return StringUtil.isBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return StringUtil.isNotBlank(str);
    }

    @Deprecated
    public static boolean equals(String str1, String str2) {
        return StringUtil.equals(str1, str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return StringUtil.equalsIgnoreCase(str1, str2);
    }

    public static boolean stringEquals(String str1, String str2) {
        return StringUtil.stringEquals(str1, str2);
    }

    public static boolean stringEqualsIgnoreCase(String str1, String str2) {
        return StringUtil.stringEqualsIgnoreCase(str1, str2);
    }

    public static int indexOfAny(String str, String[] searchStrs) {
        return StringUtil.indexOfAny(str, searchStrs);
    }

    public static int lastIndexOfAny(String str, String[] searchStrs) {
        return StringUtil.lastIndexOfAny(str, searchStrs);
    }

    public static String substring(String str, int start) {
        return StringUtil.substring(str, start);
    }

    public static String substring(String str, int start, int end) {
        return StringUtil.substring(str, start, end);
    }

    public static String substringAfter(String str, int separator) {
        return StringUtil.substringAfter(str, separator);
    }

    public static String substringAfter(String str, String separator) {
        return StringUtil.substringAfter(str, separator);
    }

    public static String substringAfterLast(String str, int separator) {
        return StringUtil.substringAfterLast(str, separator);
    }

    public static String substringAfterLast(String str, String separator) {
        return StringUtil.substringAfterLast(str, separator);
    }

    public static String substringBefore(String str, String separator) {
        return StringUtil.substringBefore(str, separator);
    }

    public static String substringBeforeLast(String str, String separator) {
        return StringUtil.substringBeforeLast(str, separator);
    }

    public static String substringBetween(String str, String tag) {
        return StringUtil.substringBetween(str, tag);
    }

    public static String substringBetween(String str, String open, String close) {
        return StringUtil.substringBetween(str, open, close);
    }

    public static String[] substringsBetween(String str, String open, String close) {
        return StringUtil.substringsBetween(str, open, close);
    }

    public static String left(String str, int len) {
        return StringUtil.left(str, len);
    }

    public static String right(String str, int len) {
        return StringUtil.right(str, len);
    }

    public static String mid(String str, int pos, int len) {
        return StringUtil.mid(str, pos, len);
    }

    public static String normalize(String input) {
        return StringUtil.normalize(input);
    }

    public static String asciifyAccents(String input) {
        return StringUtil.asciifyAccents(input);
    }

    public static String normalizeSpace(String str) {
        return StringUtil.normalizeSpace(str);
    }

    public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
        return StringUtil.ordinalIndexOf(str, searchStr, ordinal);
    }

    public static String overlay(String str, String overlay, int start, int end) {
        return StringUtil.overlay(str, overlay, start, end);
    }

    public static String prependIfMissing(String str, CharSequence prefix, CharSequence... prefixes) {
        return StringUtil.prependIfMissing(str, prefix, prefixes);
    }

    public static String prependIfMissingIgnoreCase(String str, CharSequence prefix, CharSequence... prefixes) {
        return StringUtil.prependIfMissingIgnoreCase(str, prefix, prefixes);
    }

    public static String remove(String str, char remove) {
        return StringUtil.remove(str, remove);
    }

    public static String remove(String str, String remove) {
        return StringUtil.remove(str, remove);
    }

    @Deprecated
    public static String removeAll(String text, String regex) {
        return StringUtil.removeAll(text, regex);
    }

    public static String removeEnd(String str, String remove) {
        return StringUtil.removeEnd(str, remove);
    }

    public static String removeEndIgnoreCase(String str, String remove) {
        return StringUtil.removeEndIgnoreCase(str, remove);
    }

    @Deprecated
    public static String removeFirst(String text, String regex) {
        return StringUtil.removeFirst(text, regex);
    }

    public static String removeIgnoreCase(String str, String remove) {
        return StringUtil.removeIgnoreCase(str, remove);
    }

    @Deprecated
    public static String removePattern(String source, String regex) {
        return StringUtil.removePattern(source, regex);
    }

    public static String removeStart(String str, String remove) {
        return StringUtil.removeStart(str, remove);
    }

    public static String removeStartIgnoreCase(String str, String remove) {
        return StringUtil.removeStartIgnoreCase(str, remove);
    }

    public static String repeat(char ch, int repeat) {
        return StringUtil.repeat(ch, repeat);
    }

    public static String repeat(String str, int repeat) {
        return StringUtil.repeat(str, repeat);
    }

    public static String repeat(String str, String separator, int repeat) {
        return StringUtil.repeat(str, separator, repeat);
    }

    public static String[] split(String text, String separator) {
        return StringUtil.split(text, separator);
    }

    public static String[] split(String str, String separator, int max) {
        return StringUtil.split(str, separator, max);
    }

    public static String[] splitByCharacterType(String str) {
        return StringUtil.splitByCharacterType(str);
    }

    public static String[] splitByCharacterTypeCamelCase(String str) {
        return StringUtil.splitByCharacterTypeCamelCase(str);
    }

    public static String[] splitByWholeSeparator(String str, String separator) {
        return StringUtil.splitByWholeSeparator(str, separator);
    }

    public static String[] splitByWholeSeparator(String str, String separator, int max) {
        return StringUtil.splitByWholeSeparator(str, separator, max);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator) {
        return StringUtil.splitByWholeSeparatorPreserveAllTokens(str, separator);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator, int max) {
        return StringUtil.splitByWholeSeparatorPreserveAllTokens(str, separator, max);
    }

    public static String[] splitPreserveAllTokens(String str) {
        return StringUtil.splitPreserveAllTokens(str);
    }

    public static String[] splitPreserveAllTokens(String str, char separatorChar) {
        return StringUtil.splitPreserveAllTokens(str, separatorChar);
    }

    public static String[] splitPreserveAllTokens(String str, String separatorChars) {
        return StringUtil.splitPreserveAllTokens(str, separatorChars);
    }

    public static String[] splitPreserveAllTokens(String str, String separatorChars, int max) {
        return StringUtil.splitPreserveAllTokens(str, separatorChars, max);
    }

    public static boolean startsWith(CharSequence str, CharSequence prefix) {
        return StringUtil.startsWith(str, prefix);
    }

    public static boolean startsWithAny(CharSequence sequence, CharSequence... searchStrings) {
        return StringUtil.startsWithAny(sequence, searchStrings);
    }

    public static boolean startsWithIgnoreCase(CharSequence str, CharSequence prefix) {
        return StringUtil.startsWithIgnoreCase(str, prefix);
    }

    public static String concat(Object... array) {
        return StringUtil.concat(array);
    }

    public static String concatenate(Object... array) {
        return StringUtil.concatenate(array);
    }

    public static String concat(String... array) {
        return StringUtil.concat(array);
    }

    public static String concatenate(String... array) {
        return StringUtil.concatenate(array);
    }

    public static String concat(String a1, String a2, String a3, String a4, String a5, String a6) {
        return StringUtil.concat(a1, a2, a3, a4, a5, a6);
    }

    public static String concatenate(String a1, String a2, String a3, String a4, String a5, String a6) {
        return StringUtil.concatenate(a1, a2, a3, a4, a5, a6);
    }

    public static String concat(String a1, String a2, String a3, String a4, String a5) {
        return StringUtil.concat(a1, a2, a3, a4, a5);
    }

    public static String concatenate(String a1, String a2, String a3, String a4, String a5) {
        return StringUtil.concatenate(a1, a2, a3, a4, a5);
    }

    public static String concat(String a1, String a2, String a3, String a4) {
        return StringUtil.concat(a1, a2, a3, a4);
    }

    public static String concatenate(String a1, String a2, String a3, String a4) {
        return StringUtil.concatenate(a1, a2, a3, a4);
    }

    public static String concat(String a1, String a2, String a3) {
        return StringUtil.concat(a1, a2, a3);
    }

    public static String concatenate(String a1, String a2, String a3) {
        return StringUtil.concatenate(a1, a2, a3);
    }

    public static String join(Object[] array, String separator) {
        return StringUtil.join(array, separator);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        return StringUtil.join(array, separator, startIndex, endIndex);
    }

    public static String join(short[] array, char separator) {
        return StringUtil.join(array, separator);
    }

    public static String join(short[] array, char separator, int startIndex, int endIndex) {
        return StringUtil.join(array, separator, startIndex, endIndex);
    }

    @SafeVarargs
    public static <T> String join(T... elements) {
        return StringUtil.join(elements);
    }

    public static String joinWith(String separator, Object... objects) {
        return StringUtil.joinWith(separator, objects);
    }

    public static int lastIndexOf(CharSequence seq, CharSequence searchSeq) {
        return StringUtil.lastIndexOf(seq, searchSeq);
    }

    public static int lastIndexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
        return StringUtil.lastIndexOf(seq, searchSeq, startPos);
    }

    public static int lastIndexOf(CharSequence seq, int searchChar) {
        return StringUtil.lastIndexOf(seq, searchChar);
    }

    public static int lastIndexOf(CharSequence seq, int searchChar, int startPos) {
        return StringUtil.lastIndexOf(seq, searchChar, startPos);
    }

    public static int lastIndexOfAny(CharSequence str, CharSequence... searchStrs) {
        return StringUtil.lastIndexOfAny(str, searchStrs);
    }

    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return StringUtil.lastIndexOfIgnoreCase(str, searchStr);
    }

    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        return StringUtil.lastIndexOfIgnoreCase(str, searchStr, startPos);
    }

    public static int lastOrdinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
        return StringUtil.lastOrdinalIndexOf(str, searchStr, ordinal);
    }

    public static String join(List<?> list, String separator) {
        return StringUtil.join(list, separator);
    }

    public static String join(Collection<?> col, String separator) {
        return StringUtil.join(col, separator);
    }

    public static String join(Set<?> set, String separator) {
        return StringUtil.join(set, separator);
    }

    public static String replaceOnce(String text, char repl, char with) {
        return StringUtil.replaceOnce(text, repl, with);
    }

    public static String replace(String text, char repl, char with) {
        return StringUtil.replace(text, repl, with);
    }

    public static String replace(String text, char repl, char with, int max) {
        return StringUtil.replace(text, repl, with, max);
    }

    public static String replaceOnce(String text, String repl, String with) {
        return StringUtil.replaceOnce(text, repl, with);
    }

    public static String replaceOnceIgnoreCase(String text, String searchString, String replacement) {
        return StringUtil.replaceOnceIgnoreCase(text, searchString, replacement);
    }

    @Deprecated
    public static String replacePattern(String source, String regex, String replacement) {
        return StringUtil.replacePattern(source, regex, replacement);
    }

    public static String replace(String text, String repl, String with) {
        return StringUtil.replace(text, repl, with);
    }

    public static String replace(String text, String repl, String with, int max) {
        return StringUtil.replace(text, repl, with, max);
    }

    @Deprecated
    public static String replaceAll(String text, String regex, String replacement) {
        return StringUtil.replaceAll(text, regex, replacement);
    }

    public static String replaceChars(String str, char searchChar, char replaceChar) {
        return StringUtil.replaceChars(str, searchChar, replaceChar);
    }

    public static String replaceChars(String str, String searchChars, String replaceChars) {
        return StringUtil.replaceChars(str, searchChars, replaceChars);
    }

    public static String replaceEach(String text, String[] searchList, String[] replacementList) {
        return StringUtil.replaceEach(text, searchList, replacementList);
    }

    public static String replaceEachRepeatedly(String text, String[] searchList, String[] replacementList) {
        return StringUtil.replaceEachRepeatedly(text, searchList, replacementList);
    }

    @Deprecated
    public static String replaceFirst(String text, String regex, String replacement) {
        return StringUtil.replaceFirst(text, regex, replacement);
    }

    public static String replaceIgnoreCase(String text, String searchString, String replacement) {
        return StringUtil.replaceIgnoreCase(text, searchString, replacement);
    }

    public static String replaceIgnoreCase(String text, String searchString, String replacement, int max) {
        return StringUtil.replaceIgnoreCase(text, searchString, replacement, max);
    }

    public static String overlayString(String text, String overlay, int start, int end) {
        return StringUtil.overlayString(text, overlay, start, end);
    }

    public static String center(String str, int size) {
        return StringUtil.center(str, size);
    }

    public static String center(String str, int size, char padChar) {
        return StringUtil.center(str, size, padChar);
    }

    public static String center(String str, int size, String delim) {
        return StringUtil.center(str, size, delim);
    }

    public static String chomp(String str) {
        return StringUtil.chomp(str);
    }

    @Deprecated
    public static String chomp(String str, String sep) {
        return StringUtil.chomp(str, sep);
    }

    public static String chop(String str) {
        return StringUtil.chop(str);
    }

    public static int compare(String str1, String str2) {
        return StringUtil.compare(str1, str2);
    }

    public static int compare(String str1, String str2, boolean nullIsLess) {
        return StringUtil.compare(str1, str2, nullIsLess);
    }

    public static int compareIgnoreCase(String str1, String str2) {
        return StringUtil.compareIgnoreCase(str1, str2);
    }

    public static int compareIgnoreCase(String str1, String str2, boolean nullIsLess) {
        return StringUtil.compareIgnoreCase(str1, str2, nullIsLess);
    }

    public static boolean contains(CharSequence seq, CharSequence searchSeq) {
        return StringUtil.contains(seq, searchSeq);
    }

    public static boolean contains(CharSequence seq, int searchChar) {
        return StringUtil.contains(seq, searchChar);
    }

    public static boolean containsAny(CharSequence cs, char... searchChars) {
        return StringUtil.containsAny(cs, searchChars);
    }

    public static boolean containsAny(CharSequence cs, CharSequence searchChars) {
        return StringUtil.containsAny(cs, searchChars);
    }

    public static boolean containsAny(CharSequence cs, CharSequence... searchCharSequences) {
        return StringUtil.containsAny(cs, searchCharSequences);
    }

    public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
        return StringUtil.containsIgnoreCase(str, searchStr);
    }

    public static boolean containsNone(CharSequence cs, char... searchChars) {
        return StringUtil.containsNone(cs, searchChars);
    }

    public static boolean containsNone(CharSequence cs, String invalidChars) {
        return StringUtil.containsNone(cs, invalidChars);
    }

    public static boolean containsOnly(CharSequence cs, char... valid) {
        return StringUtil.containsOnly(cs, valid);
    }

    public static boolean containsOnly(CharSequence cs, String validChars) {
        return StringUtil.containsOnly(cs, validChars);
    }

    public static boolean containsWhitespace(CharSequence seq) {
        return StringUtil.containsWhitespace(seq);
    }

    public static int countMatches(CharSequence str, char ch) {
        return StringUtil.countMatches(str, ch);
    }

    public static int countMatches(CharSequence str, CharSequence sub) {
        return StringUtil.countMatches(str, sub);
    }

    public static <T extends CharSequence> T defaultIfBlank(T str, T defaultStr) {
        return StringUtil.defaultIfBlank(str, defaultStr);
    }

    public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr) {
        return StringUtil.defaultIfEmpty(str, defaultStr);
    }

    public static String defaultString(String str) {
        return StringUtil.defaultString(str);
    }

    public static String defaultString(String str, String defaultStr) {
        return StringUtil.defaultString(str, defaultStr);
    }

    public static String deleteWhitespace(String str) {
        return StringUtil.deleteWhitespace(str);
    }

    public static String chompLast(String str) {
        return StringUtil.chompLast(str);
    }

    public static String chompLast(String str, String sep) {
        return StringUtil.chompLast(str, sep);
    }

    public static String getChomp(String str, String sep) {
        return StringUtil.getChomp(str, sep);
    }

    public static String prechomp(String str, String sep) {
        return StringUtil.prechomp(str, sep);
    }

    public static String getPrechomp(String str, String sep) {
        return StringUtil.getPrechomp(str, sep);
    }

    public static String chopNewline(String str) {
        return StringUtil.chopNewline(str);
    }

    public static String escape(String str) {
        return StringUtil.escape(str);
    }

    public static String rightPad(String str, int size) {
        return StringUtil.rightPad(str, size);
    }

    public static String rightPad(String str, int size, char padChar) {
        return StringUtil.rightPad(str, size, padChar);
    }

    public static String rightPad(String str, int size, String delim) {
        return StringUtil.rightPad(str, size, delim);
    }

    public static String rotate(String str, int shift) {
        return StringUtil.rotate(str, shift);
    }

    public static String[] split(String str) {
        return StringUtil.split(str);
    }

    public static String[] split(String str, char separatorChar) {
        return StringUtil.split(str, separatorChar);
    }

    public static String leftPad(String str, int size) {
        return StringUtil.leftPad(str, size);
    }

    public static String leftPad(String str, int size, char padChar) {
        return StringUtil.leftPad(str, size, padChar);
    }

    public static String leftPad(String str, int size, String delim) {
        return StringUtil.leftPad(str, size, delim);
    }
    
}
