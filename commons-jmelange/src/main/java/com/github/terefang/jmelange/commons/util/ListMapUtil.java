package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.apache.lang3.tuple.ImmutablePair;
import com.github.terefang.jmelange.apache.lang3.tuple.ImmutableTriple;
import com.github.terefang.jmelange.apache.lang3.tuple.Pair;
import com.github.terefang.jmelange.apache.lang3.tuple.Triple;
import com.github.terefang.jmelange.random.ArcRand;

import java.lang.reflect.Array;
import java.util.*;

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
    
}
