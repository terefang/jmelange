package com.github.terefang.jmelange.commons.util;

import org.apache.commons.lang3.BooleanUtils;

import java.util.Arrays;

public class BooleanUtil extends BooleanUtils
{
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
            if(toBoolean(_str, _test))
            {
                return false;
            }
        }
        return true;
    }


    public static boolean toBoolean(String _str, String _check)
    {
        if (_str == null) {
            return false;
        }

        if(_check.trim().equalsIgnoreCase(_str.trim()))
        {
            return true;
        }
        return false;
    }

    public static boolean toBooleanDefaultIfNull(String bool, boolean valueIfNull) {
        return bool == null ? valueIfNull : toBoolean(bool);
    }

    //*************************************************************************

}
