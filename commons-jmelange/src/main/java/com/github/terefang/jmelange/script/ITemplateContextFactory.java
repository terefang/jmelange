package com.github.terefang.jmelange.script;

import java.io.File;
import java.util.*;

public interface ITemplateContextFactory extends IContextFactory
{
    default ITemplateContext newInstance()
    {
        return newInstance(null, null);
    }
    default ITemplateContext newInstance(String _name, String _ext)
    {
        return newInstance(_name,_ext, Collections.emptyList());
    }
    ITemplateContext newInstance(String _name, String _ext, List<String> _path);
    default ITemplateContext newInstance(List<File> _path, String _name, String _ext)
    {
        List<String> _pth = new Vector<>();
        for(File _f : _path)
        {
            _pth.add(_f.getAbsolutePath());
        }
        return newInstance(_name,_ext,_pth);
    }

    public static ITemplateContextFactory findByName(String _name)
    {
        ServiceLoader<ITemplateContextFactory> _sl = ServiceLoader.load(ITemplateContextFactory.class);
        Iterator<ITemplateContextFactory>_it = _sl.iterator();
        while(_it.hasNext())
        {
            ITemplateContextFactory _that = _it.next();
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

    public static ITemplateContextFactory findByExt(String _ext)
    {
        ServiceLoader<ITemplateContextFactory> _sl = ServiceLoader.load(ITemplateContextFactory.class);
        Iterator<ITemplateContextFactory>_it = _sl.iterator();
        while(_it.hasNext())
        {
            ITemplateContextFactory _that = _it.next();
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
