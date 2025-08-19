package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.apache.lang3.tuple.ImmutablePair;
import com.github.terefang.jmelange.apache.lang3.tuple.ImmutableTriple;
import com.github.terefang.jmelange.apache.lang3.tuple.Pair;
import com.github.terefang.jmelange.apache.lang3.tuple.Triple;
import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.lang.Executable;
import com.github.terefang.jmelange.commons.lang.Executable2;
import com.github.terefang.jmelange.commons.lang.Executable3;
import com.github.terefang.jmelange.random.ArcRand;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.Callable;

public class ListMapUtil
{
    
    public static <T> T pickWeighted(T[] _a, int[] _weights, double _factor)
    {
        long _total = 0;
        for (int _w : _weights)
            _total += _w;
        
        long _z = (long) (_total*_factor);
        long _acc = 0;
        for (int _i=0; _i<_a.length; _i++)
        {
            if (_z <= (_acc += _weights[_i]))
            {
                return _a[_i];
            }
        }
        
        return _a[0];
    }
    
    public static <T> T pickWeighted(T[] _a, int[] _weights, ArcRand _rng)
    {
        return pickWeighted(_a,_weights, _rng.nextDouble());
    }
    
    public static <T> T pickWeighted(T[] _a, double[] _weights, double _factor)
    {
        double _total = 0;
        for (double _w : _weights)
            _total += _w;
        
        double _z = (_total*_factor);
        double _acc = 0;
        for (int _i=0; _i<_a.length; _i++)
        {
            if (_z <= (_acc += _weights[_i]))
            {
                return _a[_i];
            }
        }
        
        return _a[0];
    }
    
    public static <T> T pickWeighted(T[] _a, double[] _weights, ArcRand _rng)
    {
        return pickWeighted(_a,_weights, _rng.nextDouble());
    }
    
    public static <T> T pickWeighted(List<T> _a, int[] _weights, double _factor)
    {
        long _total = 0;
        for (int _w : _weights)
            _total += _w;
        
        long _z = (long) (_total*_factor);
        long _acc = 0;
        for (int _i=0; _i<_a.size(); _i++)
        {
            if (_z <= (_acc += _weights[_i]))
            {
                return _a.get(_i);
            }
        }
        
        return _a.get(0);
    }
    
    public static <T> T pickWeighted(List<T> _a, int[] _weights, ArcRand _rng)
    {
        return pickWeighted(_a,_weights, _rng.nextDouble());
    }
    
    public static <T> T pickWeighted(List<T> _a, double[] _weights, double _factor)
    {
        double _total = 0;
        for (double _w : _weights)
            _total += _w;
        
        double _z = (_total*_factor);
        double _acc = 0;
        for (int _i=0; _i<_a.size(); _i++)
        {
            if (_z <= (_acc += _weights[_i]))
            {
                return _a.get(_i);
            }
        }
        
        return _a.get(0);
    }
    
    public static <T> T pickWeighted(List<T> _a, double[] _weights, ArcRand _rng)
    {
        return pickWeighted(_a,_weights, _rng.nextDouble());
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
        return toMap( new Object[]{_k, _v} );
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
    
    public static <K,V> Map<K, V> toMap(K _k, V _v, Object... _args)
    {
        Map<K, V> _ret = new HashMap();
        _ret.put(_k,_v);
        for(int _i = 0; (_i+1)<_args.length; _i+=2)
        {
            _ret.put((K)_args[_i], (V)_args[_i+1]);
        }
        return _ret;
    }
    
    public static <K,V> Map<K, V> toMap(K _k, V _v)
    {
        Map<K, V> _ret = new HashMap();
        _ret.put(_k,_v);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toMap(K _k1, V _v1,K _k2, V _v2)
    {
        Map<K, V> _ret = new HashMap();
        _ret.put(_k1,_v1);
        _ret.put(_k2,_v2);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toMap(K _k1, V _v1,K _k2, V _v2,K _k3, V _v3)
    {
        Map<K, V> _ret = new HashMap();
        _ret.put(_k1,_v1);
        _ret.put(_k2,_v2);
        _ret.put(_k3,_v3);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toMap(K _k1, V _v1,K _k2, V _v2,K _k3, V _v3,K _k4, V _v4)
    {
        Map<K, V> _ret = new HashMap();
        _ret.put(_k1,_v1);
        _ret.put(_k2,_v2);
        _ret.put(_k3,_v3);
        _ret.put(_k4,_v4);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toMap(K _k1, V _v1,K _k2, V _v2,K _k3, V _v3,K _k4, V _v4,K _k5, V _v5)
    {
        Map<K, V> _ret = new HashMap();
        _ret.put(_k1,_v1);
        _ret.put(_k2,_v2);
        _ret.put(_k3,_v3);
        _ret.put(_k4,_v4);
        _ret.put(_k5,_v5);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toMap(K _k1, V _v1,K _k2, V _v2,K _k3, V _v3,K _k4, V _v4,K _k5, V _v5,K _k6, V _v6)
    {
        Map<K, V> _ret = new HashMap();
        _ret.put(_k1,_v1);
        _ret.put(_k2,_v2);
        _ret.put(_k3,_v3);
        _ret.put(_k4,_v4);
        _ret.put(_k5,_v5);
        _ret.put(_k6,_v6);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toMap(K _k1, V _v1,K _k2, V _v2,K _k3, V _v3,K _k4, V _v4,K _k5, V _v5,K _k6, V _v6,K _k7, V _v7)
    {
        Map<K, V> _ret = toMap(_k1,_v1,_k2,_v2,_k3,_v3,_k4,_v4,_k5,_v5,_k6,_v6);
        _ret.put(_k7,_v7);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toMap(K _k1, V _v1,K _k2, V _v2,K _k3, V _v3,K _k4, V _v4,K _k5, V _v5,K _k6, V _v6,K _k7, V _v7,K _k8, V _v8)
    {
        Map<K, V> _ret = toMap(_k1,_v1,_k2,_v2,_k3,_v3,_k4,_v4,_k5,_v5,_k6,_v6);
        _ret.put(_k7,_v7);
        _ret.put(_k8,_v8);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toMap(K _k1, V _v1,K _k2, V _v2,K _k3, V _v3,K _k4, V _v4,K _k5, V _v5,K _k6, V _v6,K _k7, V _v7,K _k8, V _v8,K _k9, V _v9)
    {
        Map<K, V> _ret = toMap(_k1,_v1,_k2,_v2,_k3,_v3,_k4,_v4,_k5,_v5,_k6,_v6);
        _ret.put(_k7,_v7);
        _ret.put(_k8,_v8);
        _ret.put(_k9,_v9);
        return _ret;
    }
    
    
    public static Map<String, Object> toLMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3, String _k4, Object _v4, String _k5, Object _v5, String _k6, Object _v6)
    {
        Map<String, Object> _ret = new LinkedHashMap();
        _ret.put(_k1, _v1);
        _ret.put(_k2, _v2);
        _ret.put(_k3, _v3);
        _ret.put(_k4, _v4);
        _ret.put(_k5, _v5);
        _ret.put(_k6, _v6);
        return _ret;
    }
    
    public static Map<String, Object> toLMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3, String _k4, Object _v4, String _k5, Object _v5)
    {
        Map<String, Object> _ret = new LinkedHashMap();
        _ret.put(_k1, _v1);
        _ret.put(_k2, _v2);
        _ret.put(_k3, _v3);
        _ret.put(_k4, _v4);
        _ret.put(_k5, _v5);
        return _ret;
    }
    
    public static Map<String, Object> toLMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3, String _k4, Object _v4)
    {
        Map<String, Object> _ret = new LinkedHashMap();
        _ret.put(_k1, _v1);
        _ret.put(_k2, _v2);
        _ret.put(_k3, _v3);
        _ret.put(_k4, _v4);
        return _ret;
    }
    
    public static Map<String, Object> toLMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3)
    {
        Map<String, Object> _ret = new LinkedHashMap();
        _ret.put(_k1, _v1);
        _ret.put(_k2, _v2);
        _ret.put(_k3, _v3);
        return _ret;
    }
    
    public static Map<String, Object> toLMap(String _k1, Object _v1, String _k2, Object _v2)
    {
        Map<String, Object> _ret = new LinkedHashMap();
        _ret.put(_k1, _v1);
        _ret.put(_k2, _v2);
        return _ret;
    }
    
    public static Map<String, Object> toLMap(String _k, Object _v)
    {
        return toLMap( new Object[]{_k, _v} );
    }
    
    public static Map<String, Object> toLMap(String... _args)
    {
        return toLMap((Object[])_args);
    }
    
    public static Map<String, Object> toLMap(Object... _args)
    {
        Map<String, Object> _ret = new LinkedHashMap();
        for(int _i = 0; (_i+1)<_args.length; _i+=2)
        {
            _ret.put(_args[_i].toString(), _args[_i+1]);
        }
        return _ret;
    }
    
    public static Map<String, Object> toLMap(List<Object> _args)
    {
        Map<String, Object> _ret = new LinkedHashMap<>();
        for(int _i = 0; (_i+1)<_args.size(); _i+=2)
        {
            _ret.put(_args.get(_i).toString(), _args.get(_i+1));
        }
        return _ret;
    }
    
    public static <K,V> Map<K, V> toLMap(K _k, V _v, Object... _args)
    {
        Map<K, V> _ret = new LinkedHashMap();
        _ret.put(_k,_v);
        for(int _i = 0; (_i+1)<_args.length; _i+=2)
        {
            _ret.put((K)_args[_i], (V)_args[_i+1]);
        }
        return _ret;
    }
    
    public static <K,V> Map<K, V> toLMap(K _k, V _v)
    {
        Map<K, V> _ret = new LinkedHashMap();
        _ret.put(_k,_v);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toLMap(K _k1, V _v1,K _k2, V _v2)
    {
        Map<K, V> _ret = new LinkedHashMap();
        _ret.put(_k1,_v1);
        _ret.put(_k2,_v2);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toLMap(K _k1, V _v1,K _k2, V _v2,K _k3, V _v3)
    {
        Map<K, V> _ret = new LinkedHashMap();
        _ret.put(_k1,_v1);
        _ret.put(_k2,_v2);
        _ret.put(_k3,_v3);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toLMap(K _k1, V _v1,K _k2, V _v2,K _k3, V _v3,K _k4, V _v4)
    {
        Map<K, V> _ret = new LinkedHashMap();
        _ret.put(_k1,_v1);
        _ret.put(_k2,_v2);
        _ret.put(_k3,_v3);
        _ret.put(_k4,_v4);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toLMap(K _k1, V _v1,K _k2, V _v2,K _k3, V _v3,K _k4, V _v4,K _k5, V _v5)
    {
        Map<K, V> _ret = new LinkedHashMap();
        _ret.put(_k1,_v1);
        _ret.put(_k2,_v2);
        _ret.put(_k3,_v3);
        _ret.put(_k4,_v4);
        _ret.put(_k5,_v5);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toLMap(K _k1, V _v1,K _k2, V _v2,K _k3, V _v3,K _k4, V _v4,K _k5, V _v5,K _k6, V _v6)
    {
        Map<K, V> _ret = new LinkedHashMap();
        _ret.put(_k1,_v1);
        _ret.put(_k2,_v2);
        _ret.put(_k3,_v3);
        _ret.put(_k4,_v4);
        _ret.put(_k5,_v5);
        _ret.put(_k6,_v6);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toLMap(K _k1, V _v1,K _k2, V _v2,K _k3, V _v3,K _k4, V _v4,K _k5, V _v5,K _k6, V _v6,K _k7, V _v7)
    {
        Map<K, V> _ret = toLMap(_k1,_v1,_k2,_v2,_k3,_v3,_k4,_v4,_k5,_v5,_k6,_v6);
        _ret.put(_k7,_v7);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toLMap(K _k1, V _v1,K _k2, V _v2,K _k3, V _v3,K _k4, V _v4,K _k5, V _v5,K _k6, V _v6,K _k7, V _v7,K _k8, V _v8)
    {
        Map<K, V> _ret = toLMap(_k1,_v1,_k2,_v2,_k3,_v3,_k4,_v4,_k5,_v5,_k6,_v6);
        _ret.put(_k7,_v7);
        _ret.put(_k8,_v8);
        return _ret;
    }
    
    public static <K,V> Map<K, V> toLMap(K _k1, V _v1,K _k2, V _v2,K _k3, V _v3,K _k4, V _v4,K _k5, V _v5,K _k6, V _v6,K _k7, V _v7,K _k8, V _v8,K _k9, V _v9)
    {
        Map<K, V> _ret = toLMap(_k1,_v1,_k2,_v2,_k3,_v3,_k4,_v4,_k5,_v5,_k6,_v6);
        _ret.put(_k7,_v7);
        _ret.put(_k8,_v8);
        _ret.put(_k9,_v9);
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
    
    
    private static List<String> _asList(String... _args)
    {
        List<String> _ret = new ArrayList<>(_args.length);
        for(String _a : _args) _ret.add(_a);
        return _ret;
    }

    private static <T> List<T> _asList(T... _args)
    {
        List<T> _ret = new ArrayList<>(_args.length);
        for(T _a : _args) _ret.add(_a);
        return _ret;
    }

    public static List<String> asList(String... _args)
    {
        return _asList(_args);
    }
    
    public static List<String> asList(String _a1, String _a2, String _a3, String _a4, String _a5, String _a6)
    {
        return _asList(_a1, _a2, _a3, _a4, _a5, _a6);
    }
    
    public static List<String> asList(String _a1, String _a2, String _a3, String _a4, String _a5)
    {
        return _asList(_a1, _a2, _a3, _a4, _a5);
    }
    
    public static List<String> asList(String _a1, String _a2, String _a3, String _a4)
    {
        return _asList(_a1, _a2, _a3, _a4);
    }
    
    public static List<String> asList(String _a1, String _a2, String _a3)
    {
        return _asList(_a1, _a2, _a3);
    }
    
    public static List<String> asList(String _a1, String _a2)
    {
        return _asList(_a1, _a2);
    }
    
    public static <T> List<T> asList(T... _args)
    {
        return _asList(_args);
    }
    
    public static <T> List<T> asList(T _a1, T _a2, T _a3, T _a4, T _a5, T _a6)
    {
        return _asList(_a1, _a2, _a3, _a4, _a5, _a6);
    }
    
    public static <T> List<T> asList(T _a1, T _a2, T _a3, T _a4, T _a5)
    {
        return _asList(_a1, _a2, _a3, _a4, _a5);
    }
    
    public static <T> List<T> asList(T _a1, T _a2, T _a3, T _a4)
    {
        return _asList(_a1, _a2, _a3, _a4);
    }
    
    public static <T> List<T> asList(T _a1, T _a2, T _a3)
    {
        return _asList(_a1, _a2, _a3);
    }
    
    public static <T> List<T> asList(T _a1, T _a2)
    {
        return _asList(_a1, _a2);
    }
    
    public static void uniq(String[] _arr)
    {
        boolean changed = true;
        int _breaker  = 3000;
        while(changed)
        {
            Arrays.sort(_arr);
            changed = false;
            for(int _i = _arr.length-1; _i>0; _i--)
            {
                if(_arr[_i].equals(_arr[_i-1]))
                {
                    _arr[_i-1]="";
                    changed = true;
                }
            }
            if((--_breaker)<0) break;
        }
    }
    
    public static void uniq(Integer[] _arr)
    {
        boolean changed = true;
        int _breaker  = 3000;
        while(changed)
        {
            Arrays.sort(_arr);
            changed = false;
            for(int _i = _arr.length-1; _i>0; _i--)
            {
                if(Objects.equals(_arr[_i], _arr[_i - 1]))
                {
                    _arr[_i-1]=null;
                    changed = true;
                }
            }
            if((--_breaker)<0) break;
        }
    }
    
    public static <T> void shuffle(T[] _arr)
    {
        for(int _i = _arr.length-1; _i>=0; _i--)
        {
            T _1 = _arr[_i];
            int _off = (int) (Math.random()*_arr.length);
            T _2 = _arr[_off];;
            _arr[_i] = _2;
            _arr[_off] = _1;
        }
    }
    
    public static <T> void shuffle(ArcRand _rng, T[] _arr)
    {
        for(int _i = _arr.length-1; _i>=0; _i--)
        {
            T _1 = _arr[_i];
            int _off = _rng.nextInt(_arr.length);
            T _2 = _arr[_off];;
            _arr[_i] = _2;
            _arr[_off] = _1;
        }
    }
    
    
    public static <R,S> Pair<R,S> asPair(R _v1, S _v2)
    {
        return new ImmutablePair<>(_v1,_v2);
    }
    
    public static <L, M, R> Triple<L, M, R> asTriple(L _v1, M _v2, R _v3)
    {
        return new ImmutableTriple<L, M, R>(_v1,_v2,_v3);
    }
    
    
    public static <T> T[] asArray(List<T> param)
    {
        return param.toArray((T[])Array.newInstance(param.get(0).getClass(),param.size()));
    }
    
    public static <T> T[] asArray(List<T> param, Class<T> _clazz)
    {
        return param.toArray((T[])Array.newInstance(_clazz,param.size()));
    }
    public static int[] toIntArray(int _n, int _t)
    {
        int[] _ret = new int[_n];
        int _x = _t/_n;
        for(int _i = 0; _i<_n; _i++)
        {
            _ret[_i] = _x;
            _t-=_x;
        }
        
        if(_t>0)
        {
            _ret[_ret.length-1]+=_t;
        }
        return _ret;
    }
    
    public static int[] toIArray(int... param)
    {
        return param;
    }
    public static float[] toFArray(float... param)
    {
        return param;
    }
    public static long[] toLArray(long... param)
    {
        return param;
    }
    public static double[] toDArray(double... param)
    {
        return param;
    }

    
    public static String[] toArray(String... param)
    {
        return param;
    }
    
    public static <T> T[] toArray(T... param)
    {
        return param;
    }
    
    public static void sort(List<String> _words, boolean _nocase)
    {
        String[] _w = _words.toArray(new String[_words.size()]);
        Arrays.sort(_w, (a,b)->{ return _nocase ? a.compareToIgnoreCase(b) : a.compareTo(b); });
        _words.clear();
        for(String _i : _w) _words.add(_i);
    }
    
    public static void sort(String[] _words, boolean _nocase)
    {
        Arrays.sort(_words, (a,b)->{ return _nocase ? a.compareToIgnoreCase(b) : a.compareTo(b); });
    }
    
    public interface ListFilterFunction<T>{
        public boolean isMatch(T _item);
    }

    public static List<String> filterStringList(List<String> _list, ListFilterFunction<String> _func)
    {
        List<String> _ret = new Vector<>();
        for(String _i : _list)
        {
            if(_func.isMatch(_i))
            {
                _ret.add(_i);
            }
        }
        return _ret;
    }
    
    public static <T> List<T> filter(List<T> _list, ListFilterFunction<T> _func)
    {
        List<T> _ret = new Vector<>();
        for(T _i : _list)
        {
            if(_func.isMatch(_i))
            {
                _ret.add(_i);
            }
        }
        return _ret;
    }
    
    //******************************************************************************
    
    /**
     * Returns the value at a given path.
     *
     * @param <T> The type of value to expect.
     * @param root The root object.
     * @param path The path to the value as a JavaScript path.
     * @return The value at the given path.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getByPath(Object root, String path) {
        return (T) getByPath(root, parsePath(path));
    }
    
    /**
     * Returns the value at a given path.
     *
     * @param <T> The type of value to expect.
     * @param root The root object.
     * @param keys The path to the value as a sequence of keys.
     * @return The value at the given path.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getByPath(Object root, List<String> keys) {
        if(keys==null) return (T)root;
        
        Object value = root;
        
        for (int i = 0, n = keys.size(); i < n; i++) {
            if (value == null)
            {
                break;
            }
            
            String key = keys.get(i);
            
            Map<String, T> adapter = (Map<String, T>) (value instanceof Map
                    ? ((Map<String, T>) value)
                    : new BeanAdapter(value));
            if (adapter.containsKey(key)) {
                value = adapter.get(key);
            } else if (value instanceof List<?>) {
                List<Object> sequence = (List<Object>) value;
                value = sequence.get(Integer.parseInt(key));
            } else if (value instanceof Collection<?>) {
                Collection<Object> sequence = (Collection<Object>) value;
                int _i = Integer.parseInt(key);
                
                if(_i>=sequence.size()) throw new IllegalArgumentException("Property \"" + key + "\" index "+_i+" out of bounds.");
                
                Iterator<Object> _it = sequence.iterator();
                while(_it.hasNext() && _i-->0)
                {
                    _it.next();
                }
                value = _it.next();
            } else if (value instanceof Dictionary<?, ?>) {
                Dictionary<String, Object> dictionary = (Dictionary<String, Object>) value;
                value = dictionary.get(key);
            } else {
                throw new IllegalArgumentException("Property \"" + key + "\" not found.");
            }
        }
        
        return (T) value;
    }
    
    public static byte getByPathAsByte(Object root, String path) {
        Number value = getByPath(root, path);
        return value.byteValue();
    }
    
    public static short getByPathAsShort(Object root, String path) {
        Number value = getByPath(root, path);
        return value.shortValue();
    }
    
    public static int getByPathAsInt(Object root, String path) {
        Number value = getByPath(root, path);
        return value.intValue();
    }
    
    public static long getByPathAsLong(Object root, String path) {
        Number value = getByPath(root, path);
        return value.longValue();
    }
    
    public static float getByPathAsFloat(Object root, String path) {
        Number value = getByPath(root, path);
        return value.floatValue();
    }
    
    public static double getByPathAsDouble(Object root, String path) {
        Number value = getByPath(root, path);
        return value.doubleValue();
    }
    
    /**
     * Sets the value at the given path.
     *
     * @param <T> The type of value we're dealing with.
     * @param root The root object.
     * @param path The path to the desired location from the root.
     * @param value The new value to set at the given path.
     * @return The value previously associated with the path.
     */
    @SuppressWarnings("unchecked")
    public static <T> T putByPath(Object root, String path, T value) {
        if(root==null) return null;
        
        List<String> keys = parsePath(path);
        if (keys.size() == 0) {
            throw new IllegalArgumentException("Path is empty.");
        }
        
        String key = keys.remove(keys.size() - 1);
        Object parent = getByPath(root, keys);
        if (parent == null) {
            throw new IllegalArgumentException("Invalid path.");
        }
        
        Map<String, T> adapter = (Map<String, T>) (parent instanceof Map
                ? ((Map<String, T>) parent)
                : new BeanAdapter(parent));
        
        Object previousValue;
        if (adapter.containsKey(key)) {
            previousValue = adapter.put(key, value);
        } else if (parent instanceof List<?>) {
            List<Object> sequence = (List<Object>) parent;
            previousValue = sequence.set(Integer.parseInt(key), value);
        } else if (parent instanceof Collection<?>) {
            throw new IllegalArgumentException("Property \"" + key + "\" is Collection but not List.");
        } else if (parent instanceof Dictionary<?, ?>) {
            Dictionary<String, Object> dictionary = (Dictionary<String, Object>) parent;
            previousValue = dictionary.put(key, value);
        } else {
            throw new IllegalArgumentException("Property \"" + key + "\" not found.");
        }
        
        return (T) previousValue;
    }
    
    /**
     * Removes the value at the given path.
     *
     * @param <T> The type of value we're dealing with.
     * @param root The root object.
     * @param path The path to the object (from the root) to remove.
     * @return The value that was removed.
     */
    @SuppressWarnings("unchecked")
    public static <T> T removeByPath(Object root, String path) {
        if(root==null) return null;
        
        List<String> keys = parsePath(path);
        if (keys.size() == 0) {
            throw new IllegalArgumentException("Path is empty.");
        }
        
        String key = keys.remove(keys.size() - 1);
        Object parent = getByPath(root, keys);
        if (parent == null) {
            throw new IllegalArgumentException("Invalid path.");
        }
        
        Object previousValue;
        if (parent instanceof List<?>) {
            List<Object> sequence = (List<Object>) parent;
            previousValue = sequence.remove(Integer.parseInt(key));
        } else if (parent instanceof Collection<?>) {
            Collection<Object> sequence = (Collection<Object>) parent;
            previousValue = sequence.remove(Integer.parseInt(key));
        } else if (parent instanceof Dictionary<?, ?>) {
            Dictionary<String, Object> dictionary = (Dictionary<String, Object>) parent;
            previousValue = dictionary.remove(key);
        } else {
            throw new IllegalArgumentException("Property \"" + key + "\" not found.");
        }
        
        return (T) previousValue;
    }
    
    /**
     * Tests the existence of a path in a given object.
     *
     * @param <T> The type of value we're dealing with.
     * @param root The root object.
     * @param path The path to test (from the root).
     * @return {@code true} if the path exists; {@code false}, otherwise.
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean containsKey(Object root, String path) {
        if(root==null) return false;
        
        List<String> keys = parsePath(path);
        if (keys.size() == 0) {
            throw new IllegalArgumentException("Path is empty.");
        }
        
        String key = keys.remove(keys.size() - 1);
        Object parent = getByPath(root, keys);
        
        boolean containsKey;
        if (parent == null) {
            containsKey = false;
        } else {
            Map<String, T> adapter = (Map<String, T>) (parent instanceof Map
                    ? ((Map<String, T>) parent)
                    : new BeanAdapter(parent));
            containsKey = adapter.containsKey(key);
            
            if (!containsKey) {
                if (parent instanceof List<?>) {
                    List<Object> sequence = (List<Object>) parent;
                    containsKey = (sequence.size() > Integer.parseInt(key));
                } else if (parent instanceof Collection<?>) {
                    Collection<Object> sequence = (Collection<Object>) parent;
                    containsKey = (sequence.size() > Integer.parseInt(key));
                } else if (parent instanceof Dictionary<?, ?>) {
                    Dictionary<String, Object> dictionary = (Dictionary<String, Object>) parent;
                    containsKey = dictionary.get(key)==null;
                } else {
                    throw new IllegalArgumentException("Property \"" + key + "\" not found.");
                }
            }
        }
        
        return containsKey;
    }
    
    /**
     * Parses a JSON path into a sequence of string keys.
     *
     * @param path The path to parse.
     * @return The sequence of keys corresponding to the given path.
     */
    public static List<String> parsePath(String path) {
        if(path==null) return Collections.emptyList();
        
        ArrayList<String> keys = new ArrayList<>();
        
        int i = 0;
        int n = path.length();
        
        while (i < n) {
            char c = path.charAt(i++);
            
            StringBuilder identifierBuilder = new StringBuilder();
            
            boolean bracketed = (c == '[');
            if (bracketed && i < n) {
                c = path.charAt(i++);
                
                char quote = Character.UNASSIGNED;
                
                boolean quoted = (c == '"' || c == '\'');
                if (quoted && i < n) {
                    quote = c;
                    c = path.charAt(i++);
                }
                
                while (i <= n && bracketed) {
                    bracketed = quoted || (c != ']');
                    
                    if (bracketed) {
                        if (c == quote) {
                            if (i < n) {
                                c = path.charAt(i++);
                                quoted = (c == quote);
                            }
                        }
                        
                        if (quoted || c != ']') {
                            if (Character.isISOControl(c)) {
                                throw new IllegalArgumentException("Illegal identifier character.");
                            }
                            
                            identifierBuilder.append(c);
                            
                            if (i < n) {
                                c = path.charAt(i++);
                            }
                        }
                    }
                }
                
                if (quoted) {
                    throw new IllegalArgumentException("Unterminated quoted identifier.");
                }
                
                if (bracketed) {
                    throw new IllegalArgumentException("Unterminated bracketed identifier.");
                }
                
                if (i < n) {
                    c = path.charAt(i);
                    
                    if (c == '.') {
                        i++;
                    }
                }
            } else {
                while (i <= n && c != '.' && c != '[') {
                    if (!Character.isJavaIdentifierPart(c)) {
                        throw new IllegalArgumentException("Illegal identifier character.");
                    }
                    
                    identifierBuilder.append(c);
                    
                    if (i < n) {
                        c = path.charAt(i);
                    }
                    
                    i++;
                }
                
                if (c == '[') {
                    i--;
                }
            }
            
            if (c == '.' && i == n) {
                throw new IllegalArgumentException("Path cannot end with a '.' character.");
            }
            
            if (identifierBuilder.length() == 0) {
                throw new IllegalArgumentException("Missing identifier.");
            }
            
            keys.add(identifierBuilder.toString());
        }
        
        return keys;
    }
    
    /**
     * Exposes Java bean properties of an object via the {@link Map} interface. A
     * call to {@link Map#get(Object)} invokes the getter for the corresponding
     * property, and a call to {@link Map#put(Object, Object)} invokes the
     * property's setter. <p> Properties may provide multiple setters; the
     * appropriate setter to invoke is determined by the type of the value being
     * set. If the value is {@code null}, the return type of the getter method is
     * used. <p> Getter methods must be named "getProperty" where "property" is the
     * property name. If there is no "get" method, then an "isProperty" method can
     * also be used. Setter methods (if present) must be named "setProperty". <p>
     * Getter and setter methods are checked before straight fields named "property"
     * in order to support proper data encapsulation. And only <code>public</code>
     * and non-<code>static</code> methods and fields can be accessed.
     */
    public static class BeanAdapter implements Map<String, Object> {
        /**
         * Property iterator. Returns a property name for each getter method and public,
         * non-final field defined by the bean.
         */
        private class PropertyIterator implements Iterator<String> {
            /**
             * The list of methods in the bean object.
             */
            private Method[] methods = null;
            /**
             * The list of fields in the bean object.
             */
            private Field[] fields = null;
            
            /**
             * Current index into the {@link #methods} array.
             */
            private int methodIndex = 0;
            /**
             * Current index into the {@link #fields} array.
             */
            private int fieldIndex = 0;
            /**
             * The next property name to return (if any) during the iteration.
             */
            private String nextPropertyName = null;
            
            /**
             * Construct the property iterator over our bean object.
             */
            PropertyIterator() {
                methods = beanClass.getMethods();
                fields = beanClass.getFields();
                nextProperty();
            }
            
            @Override
            public boolean hasNext() {
                return (nextPropertyName != null);
            }
            
            @Override
            public String next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                
                String nameToReturn = nextPropertyName;
                nextProperty();
                
                return nameToReturn;
            }
            
            /**
             * Iterate to the next acceptable property method/field in the bean object.
             */
            private void nextProperty() {
                nextPropertyName = null;
                
                while (methodIndex < methods.length && nextPropertyName == null) {
                    Method method = methods[methodIndex++];
                    
                    if (method.getParameterTypes().length == 0
                            && (method.getModifiers() & Modifier.STATIC) == 0) {
                        String methodName = method.getName();
                        
                        String prefix = null;
                        if (methodName.startsWith(GET_PREFIX)) {
                            prefix = GET_PREFIX;
                        } else {
                            if (methodName.startsWith(IS_PREFIX)) {
                                prefix = IS_PREFIX;
                            }
                        }
                        
                        if (prefix != null) {
                            int propertyOffset = prefix.length();
                            String propertyName = Character.toLowerCase(methodName.charAt(propertyOffset))
                                    + methodName.substring(propertyOffset + 1);
                            
                            if (!propertyName.equals("class")) {
                                if (!ignoreReadOnlyProperties || !isReadOnly(propertyName)) {
                                    nextPropertyName = propertyName;
                                }
                            }
                        }
                    }
                }
                
                if (nextPropertyName == null) {
                    while (fieldIndex < fields.length && nextPropertyName == null) {
                        Field field = fields[fieldIndex++];
                        
                        int modifiers = field.getModifiers();
                        if ((modifiers & Modifier.PUBLIC) != 0 && (modifiers & Modifier.STATIC) == 0) {
                            if (!ignoreReadOnlyProperties || (modifiers & Modifier.FINAL) == 0) {
                                nextPropertyName = field.getName();
                            }
                        }
                    }
                }
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
        
        /**
         * The POJO object we are wrapping in order to get/set its properties.
         */
        private final Object bean;
        /**
         * For convenience, the class of the bean object.
         */
        private final Class<?> beanClass;
        /**
         * For convenience, name of the bean object's class.
         */
        private final String beanClassName;
        /**
         * Flag to say whether or not we ignore properties that have no "setting" methods
         * and are thus "readonly".
         */
        private final boolean ignoreReadOnlyProperties;
        
        /**
         * List of listeners for changes to properties (that is, values) in this map (bean).
         */
        //private MapListener.Listeners<String, Object> mapListeners = new MapListener.Listeners<>();
        
        /** Prefix for "getProperty" method names. */
        public static final String GET_PREFIX = "get";
        /** Prefix for "isProperty" method names. */
        public static final String IS_PREFIX = "is";
        /** Prefix for "setProperty" method names. */
        public static final String SET_PREFIX = "set";
        
        /** Method name of an enum class to return an enum value from a String. */
        private static final String ENUM_VALUE_OF_METHOD_NAME = "valueOf";
        
        /** Error message format for illegal access exceptions. */
        private static final String ILLEGAL_ACCESS_EXCEPTION_MESSAGE_FORMAT =
                "Unable to access property \"%s\" for type %s.";
        /** Error message for failed attempt to coerce to an enum value. */
        private static final String ENUM_COERCION_EXCEPTION_MESSAGE =
                "Unable to coerce %s (\"%s\") to %s.\nValid enum constants - %s";
        
        /**
         * Creates a new bean dictionary.
         *
         * @param beanObject The bean object to wrap.
         */
        public BeanAdapter(final Object beanObject) {
            this(beanObject, false);
        }
        
        /**
         * Creates a new bean dictionary which can ignore readonly fields (that is,
         * straight fields marked as <code>final</code> or bean properties where
         * there is a "get" method but no corresponding "set" method).
         *
         * @param beanObject The bean object to wrap.
         * @param ignoreReadOnlyValue {@code true} if {@code final} or non-settable
         * fields should be excluded from the dictionary, {@code false} to include all fields.
         */
        public BeanAdapter(final Object beanObject, final boolean ignoreReadOnlyValue) {
            CommonUtil.checkNull(beanObject, "bean object");
            
            bean = beanObject;
            beanClass = bean.getClass();
            beanClassName = beanClass.getName();
            ignoreReadOnlyProperties = ignoreReadOnlyValue;
        }
        
        /**
         * Returns the bean object this dictionary wraps.
         *
         * @return The bean object, or {@code null} if no bean has been set.
         */
        public Object getBean() {
            return bean;
        }
        
        /**
         * Invokes the getter method for the given property.
         *
         * @param key The property name.
         * @return The value returned by the method, or {@code null} if no such
         * method exists.
         */
        public Object get(final String key) {
            CommonUtil.checkNullOrEmpty(key, "key");
            
            Object value = null;
            
            Method getterMethod = getGetterMethod(beanClass, key);
            
            if (getterMethod == null) {
                Field field = getField(beanClass, key);
                
                if (field != null) {
                    try {
                        value = field.get(bean);
                    } catch (IllegalAccessException exception) {
                        throw new RuntimeException(String.format(
                                ILLEGAL_ACCESS_EXCEPTION_MESSAGE_FORMAT, key, beanClassName),
                                exception);
                    }
                }
            } else {
                try {
                    value = getterMethod.invoke(bean, new Object[] {});
                } catch (IllegalAccessException exception) {
                    throw new RuntimeException(String.format(ILLEGAL_ACCESS_EXCEPTION_MESSAGE_FORMAT,
                            key, beanClassName), exception);
                } catch (InvocationTargetException exception) {
                    throw new RuntimeException(String.format(
                            "Error getting property \"%s\" for type %s.", key, beanClassName),
                            exception.getCause());
                }
            }
            
            return value;
        }
        
        public Object get(final Object key) {
            return get(key.toString());
        }
        
        /**
         * Invokes the setter method for the given property. The method signature is
         * determined by the type of the value. If the value is {@code null}, the
         * return type of the getter method is used.
         *
         * @param key The property name.
         * @param value The new property value.
         * @return Returns {@code null}, since returning the previous value would
         * require a call to the getter method, which may not be an efficient
         * operation.
         * @throws IllegalArgumentException If the given property does not exist or
         * is read-only.
         */
        @Override
        public Object put(final String key, final Object value) {
            CommonUtil.checkNullOrEmpty(key, "key");
            
            Method setterMethod = null;
            Object valueUpdated = value;
            
            if (valueUpdated != null) {
                // Get the setter method for the value type
                setterMethod = getSetterMethod(beanClass, key, valueUpdated.getClass());
            }
            
            if (setterMethod == null) {
                // Get the property type and attempt to coerce the value to it
                Class<?> propertyType = getType(key);
                
                if (propertyType != null) {
                    setterMethod = getSetterMethod(beanClass, key, propertyType);
                    valueUpdated = coerce(valueUpdated, propertyType, key);
                }
            }
            
            if (setterMethod == null) {
                Field field = getField(beanClass, key);
                
                if (field == null) {
                    throw new IllegalArgumentException("Property \"" + key + "\""
                            + " does not exist or is read-only for type "
                            + beanClassName + ".");
                }
                
                Class<?> fieldType = field.getType();
                if (valueUpdated != null) {
                    Class<?> valueType = valueUpdated.getClass();
                    if (!fieldType.isAssignableFrom(valueType)) {
                        valueUpdated = coerce(valueUpdated, fieldType, key);
                    }
                }
                
                try {
                    field.set(bean, valueUpdated);
                } catch (IllegalAccessException exception) {
                    throw new RuntimeException(String.format(ILLEGAL_ACCESS_EXCEPTION_MESSAGE_FORMAT,
                            key, beanClassName), exception);
                }
            } else {
                try {
                    setterMethod.invoke(bean, new Object[] {valueUpdated});
                } catch (IllegalAccessException exception) {
                    throw new RuntimeException(String.format(ILLEGAL_ACCESS_EXCEPTION_MESSAGE_FORMAT,
                            key, beanClassName), exception);
                } catch (InvocationTargetException exception) {
                    throw new RuntimeException(String.format(
                            "Error setting property \"%s\" for type %s to value \"%s\"", key,
                            beanClassName, "" + valueUpdated), exception.getCause());
                }
                
            }
            
            Object previousValue = null;
            //mapListeners.valueUpdated(this, key, previousValue);
            
            return previousValue;
        }
        
        /**
         * Invokes the setter methods for all the given properties that are present
         * in the map. The method signatures are determined by the type of the
         * values. If any value is {@code null}, the return type of the getter
         * method is used. There is an option to ignore (that is, not throw)
         * exceptions during the process, but to return status if any exceptions
         * were caught and ignored.
         *
         * @param valueMap The map of keys and values to be set.
         * @param ignoreErrors If <code>true</code> then any
         * {@link IllegalArgumentException} thrown by the {@link #put put()} method
         * will be caught and ignored.
         * @return <code>true</code> if any exceptions were caught,
         * <code>false</code> if not.
         */
        public boolean putAll(final Map<String, ?> valueMap, final boolean ignoreErrors) {
            boolean anyErrors = false;
            for (String key : valueMap.keySet()) {
                try {
                    put(key, valueMap.get(key));
                } catch (IllegalArgumentException ex) {
                    if (!ignoreErrors) {
                        throw ex;
                    }
                    anyErrors = true;
                }
            }
            return anyErrors;
        }
        
        @Override
        public void putAll(@NotNull Map m)
        {
            putAll(m, true);
        }
        
        /**
         * @throws UnsupportedOperationException This operation is not supported.
         */
        public Object remove(final String key) {
            throw new UnsupportedOperationException();
        }
        
        public Object remove(final Object key) {
            throw new UnsupportedOperationException();
        }
        
        /**
         * @throws UnsupportedOperationException This operation is not supported.
         */
        public synchronized void clear() {
            throw new UnsupportedOperationException();
        }
        
        @NotNull
        @Override
        public Set<String> keySet()
        {
            throw new UnsupportedOperationException();
        }
        
        @NotNull
        @Override
        public Collection<Object> values()
        {
            throw new UnsupportedOperationException();
        }
        
        @NotNull
        @Override
        public Set<Entry<String, Object>> entrySet()
        {
            throw new UnsupportedOperationException();
        }
        
        /**
         * Verifies the existence of a property. The property must have a getter
         * method; write-only properties are not supported.
         *
         * @param key The property name.
         * @return {@code true} if the property exists; {@code false}, otherwise.
         */
        public boolean containsKey(final String key) {
            CommonUtil.checkNullOrEmpty(key, "key");
            
            boolean containsKey = (getGetterMethod(beanClass, key) != null);
            
            if (!containsKey) {
                containsKey = (getField(beanClass, key) != null);
            }
            
            return containsKey;
        }
        
        public boolean containsKey(final Object key)
        {
            return containsKey(key.toString());
        }

        public boolean containsValue(final Object key)
        {
            throw new UnsupportedOperationException();
        }
            
            /**
             * @throws UnsupportedOperationException This operation is not supported.
             */
        public boolean isEmpty() {
            throw new UnsupportedOperationException();
        }
        
        /**
         * @throws UnsupportedOperationException This operation is not supported.
         */
        public int getCount() {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            throw new UnsupportedOperationException();
        }
        
        public final Comparator<String> getComparator() {
            return null;
        }
        
        /**
         * @throws UnsupportedOperationException This operation is not supported.
         */
        public void setComparator(final Comparator<String> comparator) {
            throw new UnsupportedOperationException();
        }
        
        /**
         * Tests the read-only state of a property.
         *
         * @param key The property name.
         * @return {@code true} if the property is read-only; {@code false},
         * otherwise.
         */
        public boolean isReadOnly(final String key) {
            return isReadOnly(beanClass, key);
        }
        
        /**
         * Returns the type of a property.
         *
         * @param key The property name.
         * @return The real class type of this property.
         * @see #getType(Class, String)
         */
        public Class<?> getType(final String key) {
            return getType(beanClass, key);
        }
        
        /**
         * Returns the generic type of a property.
         *
         * @param key The property name.
         * @return The generic type of this property.
         * @see #getGenericType(Class, String)
         */
        public Type getGenericType(final String key) {
            return getGenericType(beanClass, key);
        }
        
        /**
         * Returns an iterator over the bean's properties.
         *
         * @return A property iterator for this bean.
         */
        public Iterator<String> iterator() {
            return new PropertyIterator();
        }
        
        //public final ListenerList<MapListener<String, Object>> getMapListeners() {
        //    return mapListeners;
        //}
        
        /**
         * Tests the read-only state of a property. Note that if no such property
         * exists, this method will return {@code true} (it will <u>not</u> throw
         * an exception).
         *
         * @param beanClass The bean class.
         * @param key The property name.
         * @return {@code true} if the property is read-only; {@code false},
         * otherwise.
         */
        public static boolean isReadOnly(final Class<?> beanClass, final String key) {
            CommonUtil.checkNull(beanClass, "beanClass");
            CommonUtil.checkNullOrEmpty(key, "key");
            
            boolean isReadOnly = true;
            
            Method getterMethod = getGetterMethod(beanClass, key);
            if (getterMethod == null) {
                Field field = getField(beanClass, key);
                if (field != null) {
                    isReadOnly = ((field.getModifiers() & Modifier.FINAL) != 0);
                }
            } else {
                Method setterMethod = getSetterMethod(beanClass, key, getType(beanClass, key));
                isReadOnly = (setterMethod == null);
            }
            
            return isReadOnly;
        }
        
        /**
         * Returns the type of a property.
         *
         * @param beanClass The bean class.
         * @param key The property name.
         * @return The type of the property, or {@code null} if no such bean
         * property exists.
         */
        public static Class<?> getType(final Class<?> beanClass, final String key) {
            CommonUtil.checkNull(beanClass, "beanClass");
            CommonUtil.checkNullOrEmpty(key, "key");
            
            Class<?> type = null;
            
            Method getterMethod = getGetterMethod(beanClass, key);
            
            if (getterMethod == null) {
                Field field = getField(beanClass, key);
                
                if (field != null) {
                    type = field.getType();
                }
            } else {
                type = getterMethod.getReturnType();
            }
            
            return type;
        }
        
        /**
         * Returns the generic type of a property.
         *
         * @param beanClass The bean class.
         * @param key The property name.
         * @return The generic type of the property, or {@code null} if no such bean
         * property exists. If the type is a generic, an instance of
         * {@link java.lang.reflect.ParameterizedType} will be returned. Otherwise,
         * an instance of {@link java.lang.Class} will be returned.
         */
        public static Type getGenericType(final Class<?> beanClass, final String key) {
            CommonUtil.checkNull(beanClass, "beanClass");
            CommonUtil.checkNullOrEmpty(key, "key");
            
            Type genericType = null;
            
            Method getterMethod = getGetterMethod(beanClass, key);
            
            if (getterMethod == null) {
                Field field = getField(beanClass, key);
                
                if (field != null) {
                    genericType = field.getGenericType();
                }
            } else {
                genericType = getterMethod.getGenericReturnType();
            }
            
            return genericType;
        }
        
        /**
         * Returns the public, non-static fields for a property. Note that fields
         * will only be consulted for bean properties after bean methods.
         *
         * @param beanClass The bean class.
         * @param key The property name.
         * @return The field, or {@code null} if the field does not exist, or is
         * non-public or static.
         */
        public static Field getField(final Class<?> beanClass, final String key) {
            CommonUtil.checkNull(beanClass, "beanClass");
            CommonUtil.checkNullOrEmpty(key, "key");
            
            Field field = null;
            
            try {
                field = beanClass.getField(key);
                
                int modifiers = field.getModifiers();
                
                // Exclude non-public and static fields
                if ((modifiers & Modifier.PUBLIC) == 0 || (modifiers & Modifier.STATIC) > 0) {
                    field = null;
                }
            } catch (NoSuchFieldException exception) {
                // No-op
            }
            
            return field;
        }
        
        /**
         * Returns the getter method for a property.
         *
         * @param beanClass The bean class.
         * @param key The property name.
         * @return The getter method, or {@code null} if the method does not exist.
         */
        public static Method getGetterMethod(final Class<?> beanClass, final String key) {
            CommonUtil.checkNull(beanClass, "beanClass");
            CommonUtil.checkNullOrEmpty(key, "key");
            
            // Upper-case the first letter
            String keyUpdated = Character.toUpperCase(key.charAt(0)) + key.substring(1);
            Method getterMethod = null;
            
            try {
                getterMethod = beanClass.getMethod(GET_PREFIX + keyUpdated);
            } catch (NoSuchMethodException exception) {
                // No-op
            }
            
            if (getterMethod == null) {
                try {
                    getterMethod = beanClass.getMethod(IS_PREFIX + keyUpdated);
                } catch (NoSuchMethodException exception) {
                    // No-op
                }
            }
            
            return getterMethod;
        }
        
        /**
         * Simplified version of {@link #getSetterMethod(Class, String, Class)} that
         * doesn't do the null checks, or have to redo the method name calculation.
         *
         * @param beanClass The bean class.
         * @param methodName The setter method name we are looking for.
         * @param valueType The type of the property value.
         * @return The setter method, or {@code null} if the method cannot be found.
         */
        private static Method internalGetSetterMethod(final Class<?> beanClass, final String methodName,
                                                      final Class<?> valueType) {
            Method setterMethod = null;
            
            try {
                setterMethod = beanClass.getMethod(methodName, valueType);
            } catch (NoSuchMethodException exception) {
                // No-op
            }
            
            if (setterMethod == null) {
                // Look for a match on the value's super type
                Class<?> superType = valueType.getSuperclass();
                if (superType != null) {
                    setterMethod = internalGetSetterMethod(beanClass, methodName, superType);
                }
            }
            
            if (setterMethod == null) {
                // If value type is a primitive wrapper, look for a method
                // signature with the corresponding primitive type
                try {
                    Field primitiveTypeField = valueType.getField("TYPE");
                    Class<?> primitiveValueType = (Class<?>) primitiveTypeField.get(null);
                    
                    try {
                        setterMethod = beanClass.getMethod(methodName, primitiveValueType);
                    } catch (NoSuchMethodException exception) {
                        // No-op
                    }
                } catch (NoSuchFieldException exception) {
                    // No-op
                } catch (IllegalAccessException exception) {
                    throw new RuntimeException(String.format(
                            ILLEGAL_ACCESS_EXCEPTION_MESSAGE_FORMAT, methodName, beanClass.getName()),
                            exception);
                }
            }
            
            if (setterMethod == null) {
                // Walk the interface graph to find a matching method
                Class<?>[] interfaces = valueType.getInterfaces();
                
                int i = 0, n = interfaces.length;
                while (setterMethod == null && i < n) {
                    Class<?> interfaceType = interfaces[i++];
                    setterMethod = internalGetSetterMethod(beanClass, methodName, interfaceType);
                }
            }
            
            return setterMethod;
        }
        
        /**
         * Returns the setter method for a property.
         *
         * @param beanClass The bean class.
         * @param key The property name.
         * @param valueType The type of the property.
         * @return The setter method, or {@code null} if the method does not exist.
         */
        public static Method getSetterMethod(final Class<?> beanClass, final String key,
                                             final Class<?> valueType) {
            CommonUtil.checkNull(beanClass, "beanClass");
            CommonUtil.checkNullOrEmpty(key, "key");
            
            Method setterMethod = null;
            
            if (valueType != null) {
                // Upper-case the first letter and prepend the "set" prefix to
                // determine the method name
                String keyUpdated = Character.toUpperCase(key.charAt(0)) + key.substring(1);
                final String methodName = SET_PREFIX + keyUpdated;
                
                setterMethod = internalGetSetterMethod(beanClass, methodName, valueType);
            }
            
            return setterMethod;
        }
        
        /**
         * Coerces a value to a given type.
         *
         * @param <T> The parametric type to coerce to.
         * @param value The object to be coerced.
         * @param type The type to coerce it to.
         * @param key The property name in question.
         * @return The coerced value.
         * @throws IllegalArgumentException for all the possible other exceptions.
         */
        @SuppressWarnings("unchecked")
        public static <T> T coerce(final Object value, final Class<? extends T> type, final String key) {
            CommonUtil.checkNull(type, "type");
            
            Object coercedValue;
            
            if (value == null) {
                // Null values can only be coerced to null
                coercedValue = null;
            } else if (type == Object.class || type.isAssignableFrom(value.getClass())) {
                // Value doesn't need coercion
                coercedValue = value;
            } else if (type.isEnum()) {
                // Find and invoke the valueOf(String) method using an upper
                // case conversion of the supplied Object's toString() value
                try {
                    String valueString   = value.toString().toUpperCase(Locale.ENGLISH);
                    Method valueOfMethod = type.getMethod(ENUM_VALUE_OF_METHOD_NAME, String.class);
                    coercedValue = valueOfMethod.invoke(null, valueString);
                } catch (IllegalAccessException | InvocationTargetException
                         | SecurityException | NoSuchMethodException e) {
                    // Nothing to be gained by handling the getMethod() & invoke() exceptions separately
                    throw new IllegalArgumentException(String.format(
                            ENUM_COERCION_EXCEPTION_MESSAGE, value.getClass().getName(), value, type,
                            Arrays.toString(type.getEnumConstants())), e);
                }
            } else if (type == String.class) {
                coercedValue = value.toString();
            } else if (type == Boolean.class || type == Boolean.TYPE) {
                coercedValue = Boolean.parseBoolean(value.toString());
            } else if (type == Character.class || type == Character.TYPE) {
                coercedValue = value.toString().charAt(0);
            } else if (type == Byte.class || type == Byte.TYPE) {
                if (value instanceof Number) {
                    coercedValue = ((Number) value).byteValue();
                } else {
                    coercedValue = Byte.parseByte(value.toString());
                }
            } else if (type == Short.class || type == Short.TYPE) {
                if (value instanceof Number) {
                    coercedValue = ((Number) value).shortValue();
                } else {
                    coercedValue = Short.parseShort(value.toString());
                }
            } else if (type == Integer.class || type == Integer.TYPE) {
                if (value instanceof Number) {
                    coercedValue = ((Number) value).intValue();
                } else {
                    coercedValue = Integer.parseInt(value.toString());
                }
            } else if (type == Long.class || type == Long.TYPE) {
                if (value instanceof Number) {
                    coercedValue = ((Number) value).longValue();
                } else {
                    coercedValue = Long.parseLong(value.toString());
                }
            } else if (type == Float.class || type == Float.TYPE) {
                if (value instanceof Number) {
                    coercedValue = ((Number) value).floatValue();
                } else {
                    coercedValue = Float.parseFloat(value.toString());
                }
            } else if (type == Double.class || type == Double.TYPE) {
                if (value instanceof Number) {
                    coercedValue = ((Number) value).doubleValue();
                } else {
                    coercedValue = Double.parseDouble(value.toString());
                }
            } else if (type == BigInteger.class) {
                coercedValue = new BigInteger(value.toString());
            } else if (type == BigDecimal.class) {
                coercedValue = new BigDecimal(value.toString());
            } else {
                throw new IllegalArgumentException("Unable to coerce "
                        + value.getClass().getName() + " to " + type + " for \"" + key + "\" property.");
            }
            
            return (T) coercedValue;
        }
    }
    
}
