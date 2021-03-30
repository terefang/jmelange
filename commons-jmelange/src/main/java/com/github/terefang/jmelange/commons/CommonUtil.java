package com.github.terefang.jmelange.commons;

import java.util.*;

public class CommonUtil
{
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

    public static Map<String, Object> toMap(String... _args)
    {
        Map<String, Object> _ret = new HashMap();
        for(int _i = 0; (_i+1)<_args.length; _i+=2)
        {
            _ret.put(_args[_i], _args[_i+1]);
        }
        return _ret;
    }

    public static List<String> toList(String... _args)
    {
        return Arrays.asList(_args);
    }
}
