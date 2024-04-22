package com.github.terefang.jmelange.script;

import java.io.File;
import java.util.*;

public interface IScriptContextFactory extends IContextFactory
{
    default IScriptContext newInstance()
    {
        return newInstance(null, null);
    }
    default IScriptContext newInstance(String _name, String _ext)
    {
        return newInstance(_name,_ext, Collections.emptyList());
    }
    IScriptContext newInstance(String _name, String _ext, List<String> _path);
    default IScriptContext newInstance(List<File> _path, String _name, String _ext)
    {
        List<String> _pth = new Vector<>();
        for(File _f : _path)
        {
            _pth.add(_f.getAbsolutePath());
        }
        return newInstance(_name,_ext,_pth);
    }

    public static IScriptContextFactory findByName(String _name)
    {
        ServiceLoader<IScriptContextFactory> _sl = ServiceLoader.load(IScriptContextFactory.class);
        Iterator<IScriptContextFactory>_it = _sl.iterator();
        while(_it.hasNext())
        {
            IScriptContextFactory _that = _it.next();
            if(_name.equalsIgnoreCase(_that.getName()))
            {
                return _that;
            }
            else
            {
                for(String _n : _that.getNames())
                {
                    if(_name.equalsIgnoreCase(_n))
                    {
                        return _that;
                    }
                }
            }
        }
        return null;
    }

    public static IScriptContextFactory findByExt(String _ext)
    {
        ServiceLoader<IScriptContextFactory> _sl = ServiceLoader.load(IScriptContextFactory.class);
        Iterator<IScriptContextFactory>_it = _sl.iterator();
        while(_it.hasNext())
        {
            IScriptContextFactory _that = _it.next();
            if(_ext.equalsIgnoreCase(_that.getExt()) || _ext.endsWith(_that.getExt()))
            {
                return _that;
            }
            else
            {
                for(String _n : _that.getExts())
                {
                    if(_ext.equalsIgnoreCase(_n) || _ext.endsWith(_n))
                    {
                        return _that;
                    }
                }
            }
        }
        return null;
    }
}
