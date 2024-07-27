package com.github.terefang.jmelange.data;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Vector;

public class DataReadWriteFactory
{
    public static List<String> listByName(Class<? extends AbstractDataExchange> _clazz)
    {
        List<String> _ret = new Vector<>();
        ServiceLoader<? extends AbstractDataExchange> _loader = ServiceLoader.load(_clazz);
        Iterator<AbstractDataExchange> _it = (Iterator<AbstractDataExchange>) _loader.iterator();
        while (_it.hasNext())
        {
            AbstractDataExchange _impl = _it.next();
            _ret.add(_impl.getName());
            // _ret.addAll(_impl.getNames()); // NOT ENABLE
        }
        return _ret;
    }

    public static <T> T findByName(String _name, Class<? extends AbstractDataExchange> _clazz)
    {
        ServiceLoader<? extends AbstractDataExchange> _loader = ServiceLoader.load(_clazz);
        Iterator<AbstractDataExchange> _it = (Iterator<AbstractDataExchange>) _loader.iterator();
        while (_it.hasNext())
        {
            AbstractDataExchange _impl = _it.next();
            if(_impl.getName().equalsIgnoreCase(_name))
            {
                return (T)_impl;
            }
            else
            if(_impl.matchesName(_name))
            {
                return (T)_impl;
            }
        }
        return null;
    }


    public static <T> T findByExtension(String _name, Class<? extends AbstractDataExchange> _clazz)
    {
        ServiceLoader<? extends AbstractDataExchange> _loader = ServiceLoader.load(_clazz);
        Iterator<AbstractDataExchange> _it = (Iterator<AbstractDataExchange>) _loader.iterator();
        while (_it.hasNext())
        {
            AbstractDataExchange _impl = _it.next();
            if(_impl.matchesExtension(_name))
            {
                return (T)_impl;
            }
        }
        return null;
    }

}
