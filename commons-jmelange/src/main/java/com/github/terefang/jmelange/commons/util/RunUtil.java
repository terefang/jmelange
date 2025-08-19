package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.commons.lang.*;
import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.Callable;

public class RunUtil
{
    // -------------------------------------------------------------------------------
    public static <T> void forEach(T[] _param, Executable<T> _func)
    {
        for(T _p : _param) _func.execute(_p);
    }
    
    public static <T,S> void forEach(T[] _param, S _ctx, Executable2<T,S> _func)
    {
        for(T _p : _param) _func.execute(_p,_ctx);
    }
    
    // -------------------------------------------------------------------------------
    public static <T> void forEach(List<T> _param, Executable<T> _func)
    {
        for(T _p : _param) _func.execute(_p);
    }
    
    public static <T> void forEach(Collection<T> _param, Executable<T> _func)
    {
        for(T _p : _param) _func.execute(_p);
    }
    
    public static <T> void forEach(Set<T> _param, Executable<T> _func)
    {
        for(T _p : _param) _func.execute(_p);
    }
    
    public static void repeat(int _n, Executable<Integer> _func)
    {
        for(int _i=0; _i<_n; _i++) _func.execute(_i);
    }
    
    public static void repeat(int _n, Runnable _func)
    {
        for(int _i=0; _i<_n; _i++) _func.run();
    }
    
    public static <T,S> void forEach(List<T> _param, S _ctx, Executable2<T,S> _func)
    {
        for(T _p : _param) _func.execute(_p,_ctx);
    }
    
    public static <T,S> void forEach(Collection<T> _param, S _ctx, Executable2<T,S> _func)
    {
        for(T _p : _param) _func.execute(_p,_ctx);
    }
    
    public static <T,S> void forEach(Set<T> _param, S _ctx, Executable2<T,S> _func)
    {
        for(T _p : _param) _func.execute(_p,_ctx);
    }
    
    public static <S> void repeat(int _n, S _ctx, Executable2<Integer,S> _func)
    {
        for(int _i=0; _i<_n; _i++) _func.execute(_i,_ctx);
    }
    
    // -------------------------------------------------------------------------------
    public static <K,V> void forEach(Map<K,V> _param, Executable2<K,V> _func)
    {
        for(Map.Entry<K, V> _p : _param.entrySet()) _func.execute(_p.getKey(), _p.getValue());
    }
    
    public static <K,V,S> void forEach(Map<K,V> _param, S _ctx, Executable3<K,V,S> _func)
    {
        for(Map.Entry<K, V> _p : _param.entrySet()) _func.execute(_p.getKey(), _p.getValue(), _ctx);
    }
    
    // -------------------------------------------------------------------------------
    public static <T,R> List<R> forEach(T[] _param, CallableWithParam<R,T> _func)
    {
        List<R> _ret = new ArrayList<>(_param.length);
        for(T _p : _param) _ret.add(_func.call(_p));
        return _ret;
    }
    
    public static <T,S,R> List<R> forEach(T[] _param, S _ctx, CallableWithParam2<R, T, S> _func)
    {
        List<R> _ret = new ArrayList<>(_param.length);
        for(T _p : _param) _ret.add(_func.call(_p,_ctx));
        return _ret;
    }
    
    // -------------------------------------------------------------------------------
    public static <T,R> List<R> forEach(List<T> _param, CallableWithParam<R, T> _func)
    {
        List<R> _ret = new ArrayList<>(_param.size());
        for(T _p : _param) _ret.add(_func.call(_p));
        return _ret;
    }
    
    public static <T,R> List<R> forEach(Collection<T> _param, CallableWithParam<R, T> _func)
    {
        List<R> _ret = new ArrayList<>(_param.size());
        for(T _p : _param) _ret.add(_func.call(_p));
        return _ret;
    }
    
    public static <T,R> List<R> forEach(Set<T> _param, CallableWithParam<R, T> _func)
    {
        List<R> _ret = new ArrayList<>(_param.size());
        for(T _p : _param) _ret.add(_func.call(_p));
        return _ret;
    }
    
    public static <R> List<R> repeat(int _n, CallableWithParam<R, Integer> _func)
    {
        List<R> _ret = new ArrayList<>(_n);
        for(int _i=0; _i<_n; _i++) _ret.add(_func.call(_i));
        return _ret;
    }
    
    @SneakyThrows
    public static <R> List<R> repeat(int _n, Callable<R> _func)
    {
        List<R> _ret = new ArrayList<>(_n);
        for(int _i=0; _i<_n; _i++) _ret.add(_func.call());
        return _ret;
    }
    
    public static <T,S,R> List<R> forEach(List<T> _param, S _ctx, CallableWithParam2<R,T,S> _func)
    {
        List<R> _ret = new ArrayList<>(_param.size());
        for(T _p : _param) _ret.add(_func.call(_p,_ctx));
        return _ret;
    }
    
    public static <T,S,R> List<R> forEach(Collection<T> _param, S _ctx, CallableWithParam2<R,T,S> _func)
    {
        List<R> _ret = new ArrayList<>(_param.size());
        for(T _p : _param) _ret.add(_func.call(_p,_ctx));
        return _ret;
    }
    
    public static <T,S,R> List<R> forEach(Set<T> _param, S _ctx, CallableWithParam2<R,T,S> _func)
    {
        List<R> _ret = new ArrayList<>(_param.size());
        for(T _p : _param) _ret.add(_func.call(_p,_ctx));
        return _ret;
    }
    
    public static <S,R> List<R> repeat(int _n, S _ctx, CallableWithParam2<R,Integer,S> _func)
    {
        List<R> _ret = new ArrayList<>(_n);
        for(int _i=0; _i<_n; _i++) _ret.add(_func.call(_i,_ctx));
        return _ret;
    }
    
    // -------------------------------------------------------------------------------
    @SneakyThrows
    public static <K,V,R> List<R> forEach(Map<K,V> _param, CallableWithParam2<R,K,V> _func)
    {
        List<R> _ret = new ArrayList<>(_param.size());
        for(Map.Entry<K, V> _p : _param.entrySet()) _ret.add(_func.call(_p.getKey(), _p.getValue()));
        return _ret;
    }
    
    @SneakyThrows
    public static <K,V,S,R> List<R> forEach(Map<K,V> _param, S _ctx, CallableWithParam3<R,K,V,S> _func)
    {
        List<R> _ret = new ArrayList<>(_param.size());
        for(Map.Entry<K, V> _p : _param.entrySet()) _ret.add(_func.call(_p.getKey(), _p.getValue(), _ctx));
        return _ret;
    }
    
    // -------------------------------------------------------------------------------
    public static <T,R> R forFirst(T[] _param, CallableWithParam<R,T> _func)
    {
        for(T _p : _param)
        {
            R _ret = _func.call(_p);
            if(_ret!=null) return _ret;
        }
        return null;
    }
    
    public static <T,S,R> R forFirst(T[] _param, S _ctx, CallableWithParam2<R, T, S> _func)
    {
        for(T _p : _param)
        {
            R _ret = _func.call(_p,_ctx);
            if(_ret!=null) return _ret;
        }
        return null;
    }
    
    // -------------------------------------------------------------------------------
    public static <T,R> R forFirst(List<T> _param, CallableWithParam<R, T> _func)
    {
        for(T _p : _param)
        {
            R _ret = _func.call(_p);
            if(_ret!=null) return _ret;
        }
        return null;
    }
    
    public static <T,R> R forFirst(Collection<T> _param, CallableWithParam<R, T> _func)
    {
        for(T _p : _param)
        {
            R _ret = _func.call(_p);
            if(_ret!=null) return _ret;
        }
        return null;
    }
    
    public static <T,R> R forFirst(Set<T> _param, CallableWithParam<R, T> _func)
    {
        for(T _p : _param)
        {
            R _ret = _func.call(_p);
            if(_ret!=null) return _ret;
        }
        return null;
    }
    
    public static <R> R forFirst(int _n, CallableWithParam<R, Integer> _func)
    {
        for(int _i=0; _i<_n; _i++)
        {
            R _ret = _func.call(_i);
            if(_ret!=null) return _ret;
        }
        return null;
    }
    
    @SneakyThrows
    public static <R> R forFirst(int _n, Callable<R> _func)
    {
        for(int _i=0; _i<_n; _i++)
        {
            R _ret = _func.call();
            if(_ret!=null) return _ret;
        }
        return null;
    }
    
    public static <T,S,R> R forFirst(List<T> _param, S _ctx, CallableWithParam2<R,T,S> _func)
    {
        for(T _p : _param)
        {
            R _ret = _func.call(_p,_ctx);
            if(_ret!=null) return _ret;
        }
        return null;
    }
    
    public static <T,S,R> R forFirst(Collection<T> _param, S _ctx, CallableWithParam2<R,T,S> _func)
    {
        for(T _p : _param)
        {
            R _ret = _func.call(_p,_ctx);
            if(_ret!=null) return _ret;
        }
        return null;
    }
    
    public static <T,S,R> R forFirst(Set<T> _param, S _ctx, CallableWithParam2<R,T,S> _func)
    {
        for(T _p : _param)
        {
            R _ret = _func.call(_p,_ctx);
            if(_ret!=null) return _ret;
        }
        return null;
    }
    
    public static <S,R> R forFirst(int _n, S _ctx, CallableWithParam2<R,Integer,S> _func)
    {
        for(int _i=0; _i<_n; _i++)
        {
            R _ret = _func.call(_i,_ctx);
            if(_ret!=null) return _ret;
        }
        return null;
    }
    
    // -------------------------------------------------------------------------------
    @SneakyThrows
    public static <K,V,R> R forFirst(Map<K,V> _param, CallableWithParam2<R,K,V> _func)
    {
        for(Map.Entry<K, V> _p : _param.entrySet())
        {
            R _ret = _func.call(_p.getKey(), _p.getValue());
            if(_ret!=null) return _ret;
        }
        return null;
    }
    
    @SneakyThrows
    public static <K,V,S,R> R forFirst(Map<K,V> _param, S _ctx, CallableWithParam3<R,K,V,S> _func)
    {
        for(Map.Entry<K, V> _p : _param.entrySet())
        {
            R _ret = _func.call(_p.getKey(), _p.getValue(),_ctx);
            if(_ret!=null) return _ret;
        }
        return null;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // -------------------------------------------------------------------------------
    public static <T,R> R forLast(T[] _param, CallableWithParam<R,T> _func)
    {
        R _ret = null;
        for(T _p : _param)
        {
            R _tmp = _func.call(_p);
            if(_ret!=null) _ret=_tmp;
        }
        return _ret;
    }
    
    public static <T,S,R> R forLast(T[] _param, S _ctx, CallableWithParam2<R, T, S> _func)
    {
        R _ret = null;
        for(T _p : _param)
        {
            R _tmp = _func.call(_p,_ctx);
            if(_ret!=null) _ret=_tmp;
        }
        return _ret;
    }
    
    // -------------------------------------------------------------------------------
    public static <T,R> R forLast(List<T> _param, CallableWithParam<R, T> _func)
    {
        R _ret = null;
        for(T _p : _param)
        {
            R _tmp = _func.call(_p);
            if(_ret!=null) _ret=_tmp;
        }
        return _ret;
    }
    
    public static <T,R> R forLast(Collection<T> _param, CallableWithParam<R, T> _func)
    {
        R _ret = null;
        for(T _p : _param)
        {
            R _tmp = _func.call(_p);
            if(_ret!=null) _ret=_tmp;
        }
        return _ret;
    }
    
    public static <T,R> R forLast(Set<T> _param, CallableWithParam<R, T> _func)
    {
        R _ret = null;
        for(T _p : _param)
        {
            R _tmp = _func.call(_p);
            if(_ret!=null) _ret=_tmp;
        }
        return _ret;
    }
    
    public static <R> R forLast(int _n, CallableWithParam<R, Integer> _func)
    {
        R _ret = null;
        for(int _i=0; _i<_n; _i++)
        {
            R _tmp = _func.call(_i);
            if(_ret!=null) _ret=_tmp;
        }
        return _ret;
    }
    
    @SneakyThrows
    public static <R> R forLast(int _n, Callable<R> _func)
    {
        R _ret = null;
        for(int _i=0; _i<_n; _i++)
        {
            R _tmp = _func.call();
            if(_ret!=null) _ret=_tmp;
        }
        return _ret;
    }
    
    public static <T,S,R> R forLast(List<T> _param, S _ctx, CallableWithParam2<R,T,S> _func)
    {
        R _ret = null;
        for(T _p : _param)
        {
            R _tmp = _func.call(_p,_ctx);
            if(_ret!=null) _ret=_tmp;
        }
        return _ret;
    }
    
    public static <T,S,R> R forLast(Collection<T> _param, S _ctx, CallableWithParam2<R,T,S> _func)
    {
        R _ret = null;
        for(T _p : _param)
        {
            R _tmp = _func.call(_p,_ctx);
            if(_ret!=null) _ret=_tmp;
        }
        return _ret;
    }
    
    public static <T,S,R> R forLast(Set<T> _param, S _ctx, CallableWithParam2<R,T,S> _func)
    {
        R _ret = null;
        for(T _p : _param)
        {
            R _tmp = _func.call(_p,_ctx);
            if(_ret!=null) _ret=_tmp;
        }
        return _ret;
    }
    
    public static <S,R> R forLast(int _n, S _ctx, CallableWithParam2<R,Integer,S> _func)
    {
        R _ret = null;
        for(int _i=0; _i<_n; _i++)
        {
            R _tmp = _func.call(_i,_ctx);
            if(_ret!=null) _ret=_tmp;
        }
        return _ret;
    }
    
    // -------------------------------------------------------------------------------
    @SneakyThrows
    public static <K,V,R> R forLast(Map<K,V> _param, CallableWithParam2<R,K,V> _func)
    {
        R _ret = null;
        for(Map.Entry<K, V> _p : _param.entrySet())
        {
            R _tmp = _func.call(_p.getKey(), _p.getValue());
            if(_ret!=null) _ret=_tmp;
        }
        return _ret;
    }
    
    @SneakyThrows
    public static <K,V,S,R> R forLast(Map<K,V> _param, S _ctx, CallableWithParam3<R,K,V,S> _func)
    {
        R _ret = null;
        for(Map.Entry<K, V> _p : _param.entrySet())
        {
            R _tmp = _func.call(_p.getKey(), _p.getValue(),_ctx);
            if(_ret!=null) _ret=_tmp;
        }
        return _ret;
    }
    
}
