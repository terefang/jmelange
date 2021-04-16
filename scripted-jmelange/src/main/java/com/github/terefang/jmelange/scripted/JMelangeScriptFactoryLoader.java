package com.github.terefang.jmelange.scripted;

import java.util.Iterator;
import java.util.ServiceLoader;

public class JMelangeScriptFactoryLoader
{
    public static JMelangeScriptFactory loadFactoryByName(String _name)
    {
        ServiceLoader<JMelangeScriptFactory> _loader = ServiceLoader.load(JMelangeScriptFactory.class);
        Iterator<JMelangeScriptFactory> _it = _loader.iterator();
        while (_it.hasNext())
        {
            JMelangeScriptFactory _impl = _it.next();
            if(_impl.getName().equalsIgnoreCase(_name))
            {
                return _impl;
            }
            else
            if(_impl.matchesName(_name))
            {
                return _impl;
            }
        }
        return null;
    }

    public static JMelangeScriptFactory loadFactoryByExtension(String _name)
    {
        ServiceLoader<JMelangeScriptFactory> _loader = ServiceLoader.load(JMelangeScriptFactory.class);
        Iterator<JMelangeScriptFactory> _it = _loader.iterator();
        while (_it.hasNext())
        {
            JMelangeScriptFactory _impl = _it.next();
            if(_impl.matchesExtension(_name))
            {
                return _impl;
            }
        }
        return null;
    }
}
