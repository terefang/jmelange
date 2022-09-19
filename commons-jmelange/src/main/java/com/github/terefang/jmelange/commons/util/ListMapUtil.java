package com.github.terefang.jmelange.commons.util;

import java.util.*;

public class ListMapUtil
{
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

    public static Map<String, Object> toMap(List<Object> _args)
    {
        Map<String, Object> _ret = new HashMap<>();
        for(int _i = 0; (_i+1)<_args.size(); _i+=2)
        {
            _ret.put(_args.get(_i).toString(), _args.get(_i+1));
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

    public static <T> List<T> toList(T _a1, T _a2, T _a3, T _a4, T _a5, T _a6)
    {
        return Arrays.asList(_a1, _a2, _a3, _a4, _a5, _a6);
    }

    public static <T> List<T> toList(T _a1, T _a2, T _a3, T _a4, T _a5)
    {
        return Arrays.asList(_a1, _a2, _a3, _a4, _a5);
    }

    public static <T> List<T> toList(T _a1, T _a2, T _a3, T _a4)
    {
        return Arrays.asList(_a1, _a2, _a3, _a4);
    }

    public static <T> List<T> toList(T _a1, T _a2, T _a3)
    {
        return Arrays.asList(_a1, _a2, _a3);
    }

    public static <T> List<T> toList(T _a1, T _a2)
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

    public static Object getAsObject(Map _map, String _key)
    {
        return getAsObject(_map, toList(_key));
    }

    public static Object getAsObject(Map _map, List<String> _keys)
    {
        Object _ret = _map;
        for(String _key : _keys)
        {
            if(_ret == null) return null;
            Object _tmp = _ret;
            _ret = null;
            for(Object _k : ((Map)_tmp).keySet())
            {
                if(_key.equalsIgnoreCase(_k.toString()))
                {
                    _ret = ((Map)_tmp).get(_k);
                    break;
                }
            }
        }
        return _ret;
    }

    public static String getAsString(Map _map, String _key)
    {
        return getAsString(_map, toList(_key));
    }

    public static String getAsString(Map _map, List<String> _keys)
    {
        Object _ret = getAsObject(_map, _keys);
        return StringUtil.checkStringDefaultIfNullOrBlank(_ret, null, null);
    }

    public static String getAsStringDefault(Map _map, String _key, String _def)
    {
        return getAsStringDefault(_map, toList(_key), _def);
    }

    public static String getAsStringDefault(Map _map, List<String> _keys, String _def)
    {
        Object _ret = getAsObject(_map, _keys);
        return StringUtil.checkStringDefaultIfNullOrBlank(_ret, _def, _def);
    }

    public static Boolean getAsBoolean(Map _map, String _key)
    {
        return getAsBoolean(_map, toList(_key));
    }

    public static Boolean getAsBoolean(Map _map, List<String> _keys)
    {
        Object _ret = getAsObject(_map, _keys);
        return BooleanUtil.checkBoolean(_ret);
    }

    public static Boolean getAsBooleanDefault(Map _map, String _key, Boolean _def)
    {
        return getAsBooleanDefault(_map, toList(_key), _def);
    }

    public static Boolean getAsBooleanDefault(Map _map, List<String> _keys, Boolean _def)
    {
        Object _ret = getAsObject(_map, _keys);
        return BooleanUtil.checkBooleanDefaultIfNull(_ret, _def);
    }

    public static Integer getAsInteger(Map _map, String _key)
    {
        return getAsInteger(_map, toList(_key));
    }

    public static Integer getAsInteger(Map _map, List<String> _keys)
    {
        Object _ret = getAsObject(_map, _keys);
        return NumberUtil.checkInteger(_ret);
    }

    public static Integer getAsIntegerDefault(Map _map, String _key, Integer _def)
    {
        return getAsIntegerDefault(_map, toList(_key), _def);
    }

    public static Integer getAsIntegerDefault(Map _map, List<String> _keys, Integer _def)
    {
        Object _ret = getAsObject(_map, _keys);
        return NumberUtil.checkIntegerDefaultIfNull(_map.get(_ret), _def);
    }

}
