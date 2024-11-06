package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.apache.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class NumberUtil
{
    
    public static int gate(int _v, int _min, int _max)
    {
        if(_v<_min) return  _min;
        if(_v>_max) return  _max;
        return _v;
    }
    
    public static long gate(long _v, long _min, long _max)
    {
        if(_v<_min) return  _min;
        if(_v>_max) return  _max;
        return _v;
    }
    
    public static float gate(float _v, float _min, float _max)
    {
        if(_v<_min) return  _min;
        if(_v>_max) return  _max;
        return _v;
    }
    
    public static double gate(double _v, double _min, double _max)
    {
        if(_v<_min) return  _min;
        if(_v>_max) return  _max;
        return _v;
    }
    
    public static int sign(int _v)
    {
        if(_v<0) return -1;
        return 1;
    }
    
    public static long sign(long _v)
    {
        if(_v<0L) return -1L;
        return 1L;
    }
    
    public static float sign(float _v)
    {
        if(_v<0) return -1f;
        return 1f;
    }
    
    public static double sign(double _v)
    {
        if(_v<0.) return -1.;
        return 1.;
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
        String[] _parts = StringUtil.split(_text);
        StringBuilder _sb = new StringBuilder();
        for(String _part : _parts)
        {
            _sb.append((char)toInt(_part));
        }
        return _sb.toString();
    }

    public static String decimalToHex(String _text)
    {
        String[] _parts = StringUtil.split(_text);
        for(int _i=0; _i<_parts.length; _i++)
        {
            _parts[_i] = toBase(16,toInt(_parts[_i]));
        }
        return StringUtil.join(_parts, "");
    }

    public static String decimalToHex(String _text, String _sep)
    {
        String[] _parts = StringUtil.split(_text, _sep);
        for(int _i=0; _i<_parts.length; _i++)
        {
            _parts[_i] = toBase(16,toInt(_parts[_i]));
        }
        return StringUtil.join(_parts, "");
    }

    public static String decimalToHex(String _text, String _sep, String _delim)
    {
        String[] _parts = StringUtil.split(_text, _sep);
        for(int _i=0; _i<_parts.length; _i++)
        {
            _parts[_i] = toBase(16,toInt(_parts[_i]));
        }
        return StringUtil.join(_parts, _delim);
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

    public static Integer checkIntegerDefaultIfNull(final Object _str, Integer _default)
    {
        if(_str==null)
        {
            return _default;
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

    public static BigInteger checkBigInt(String _str)
    {
        return checkBigInteger(_str);
    }

    public static BigInteger checkBigInteger(String _str)
    {
        if(_str==null)
        {
            return BigInteger.ZERO;
        }
        _str = _str.trim();

        if(_str.length()==0) return BigInteger.ZERO;

        if(_str.startsWith("0x"))
        {
            return new BigInteger(_str.substring(2),16);
        }
        return new BigInteger(_str);
    }

    public static BigDecimal checkBigNum(String _str)
    {
        return checkBigDecimal(_str);
    }

    public static BigDecimal checkBigDecimal(String _str)
    {
        if(_str==null)
        {
            return BigDecimal.ZERO;
        }
        _str = _str.trim();

        if(_str.length()==0) return BigDecimal.ZERO;

        return new BigDecimal(_str);
    }


    public static int toInt(String str) {
        return NumberUtils.toInt(str);
    }

    public static int toInt(String str, int defaultValue) {
        return NumberUtils.toInt(str, defaultValue);
    }

    public static long toLong(String str) {
        return NumberUtils.toLong(str);
    }

    public static long toLong(String str, long defaultValue) {
        return NumberUtils.toLong(str, defaultValue);
    }

    public static float toFloat(String str) {
        return NumberUtils.toFloat(str);
    }

    public static float toFloat(String str, float defaultValue) {
        return NumberUtils.toFloat(str, defaultValue);
    }

    public static double toDouble(String str) {
        return NumberUtils.toDouble(str);
    }

    public static double toDouble(String str, double defaultValue) {
        return NumberUtils.toDouble(str, defaultValue);
    }

    public static double toDouble(BigDecimal value) {
        return NumberUtils.toDouble(value);
    }

    public static double toDouble(BigDecimal value, double defaultValue) {
        return NumberUtils.toDouble(value, defaultValue);
    }

    public static byte toByte(String str) {
        return NumberUtils.toByte(str);
    }

    public static byte toByte(String str, byte defaultValue) {
        return NumberUtils.toByte(str, defaultValue);
    }

    public static short toShort(String str) {
        return NumberUtils.toShort(str);
    }

    public static short toShort(String str, short defaultValue) {
        return NumberUtils.toShort(str, defaultValue);
    }

    public static BigDecimal toScaledBigDecimal(BigDecimal value) {
        return NumberUtils.toScaledBigDecimal(value);
    }

    public static BigDecimal toScaledBigDecimal(BigDecimal value, int scale, RoundingMode roundingMode) {
        return NumberUtils.toScaledBigDecimal(value, scale, roundingMode);
    }

    public static BigDecimal toScaledBigDecimal(Float value) {
        return NumberUtils.toScaledBigDecimal(value);
    }

    public static BigDecimal toScaledBigDecimal(Float value, int scale, RoundingMode roundingMode) {
        return NumberUtils.toScaledBigDecimal(value, scale, roundingMode);
    }

    public static BigDecimal toScaledBigDecimal(Double value) {
        return NumberUtils.toScaledBigDecimal(value);
    }

    public static BigDecimal toScaledBigDecimal(Double value, int scale, RoundingMode roundingMode) {
        return NumberUtils.toScaledBigDecimal(value, scale, roundingMode);
    }

    public static BigDecimal toScaledBigDecimal(String value) {
        return NumberUtils.toScaledBigDecimal(value);
    }

    public static BigDecimal toScaledBigDecimal(String value, int scale, RoundingMode roundingMode) {
        return NumberUtils.toScaledBigDecimal(value, scale, roundingMode);
    }

    public static Number createNumber(String str) {
        return NumberUtils.createNumber(str);
    }

    public static Float createFloat(String str) {
        return NumberUtils.createFloat(str);
    }

    public static Double createDouble(String str) {
        return NumberUtils.createDouble(str);
    }

    public static Long createLong(String str) {
        return NumberUtils.createLong(str);
    }

    public static BigInteger createBigInteger(String str) {
        return NumberUtils.createBigInteger(str);
    }

    public static BigDecimal createBigDecimal(String str) {
        return NumberUtils.createBigDecimal(str);
    }

    public static boolean isDigits(String str) {
        return NumberUtils.isDigits(str);
    }

    @Deprecated
    public static boolean isNumber(String str) {
        return NumberUtils.isNumber(str);
    }

    public static boolean isCreatable(String str) {
        return NumberUtils.isCreatable(str);
    }

    public static boolean isParsable(String str) {
        return NumberUtils.isParsable(str);
    }

    public static int compare(int x, int y) {
        return NumberUtils.compare(x, y);
    }

    public static int compare(long x, long y) {
        return NumberUtils.compare(x, y);
    }

    public static int compare(short x, short y) {
        return NumberUtils.compare(x, y);
    }

    public static int compare(byte x, byte y) {
        return NumberUtils.compare(x, y);
    }


}
