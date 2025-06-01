package com.github.terefang.jmelange.commons.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArrayUtil
{
    public static <T> T[] asArray(List<T> param)
    {
        return param.toArray((T[]) Array.newInstance(param.get(0).getClass(),param.size()));
    }
    
    public static <T> T[] asArray(Collection<T> param)
    {
        return param.toArray((T[]) Array.newInstance(param.iterator().next().getClass(),param.size()));
    }
    
    public static <T> T[] asArray(Set<T> param)
    {
        return param.toArray((T[]) Array.newInstance(param.iterator().next().getClass(),param.size()));
    }
    
    public static <T> T[] asArray(Map<T,T> param)
    {
        T[] _ret =(T[]) Array.newInstance(param.keySet().iterator().next().getClass(),param.size()*2);
        int _i=0;
        for(Map.Entry<T, T> _entry : param.entrySet())
        {
            _ret[_i] = _entry.getKey();
            _ret[_i+1] = _entry.getValue();
            _i+=2;
        }
        return _ret;
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
    
    
    public static String[] toSArray(String... param)
    {
        return param;
    }
    
    public static char[] toCArray(char... param)
    {
        return param;
    }
    
    public static byte[] toBArray(byte... param)
    {
        return param;
    }
    
    public static <T> T[] toArray(T... param)
    {
        return param;
    }
}
