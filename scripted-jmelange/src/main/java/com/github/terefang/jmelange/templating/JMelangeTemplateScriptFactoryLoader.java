package com.github.terefang.jmelange.templating;

import java.util.Iterator;
import java.util.ServiceLoader;

public class JMelangeTemplateScriptFactoryLoader {
    public static JMelangeTemplateScriptFactory loadFactoryByName(String _name)
    {
        ServiceLoader<JMelangeTemplateScriptFactory> _loader = ServiceLoader.load(JMelangeTemplateScriptFactory.class);
        Iterator<JMelangeTemplateScriptFactory> _it = _loader.iterator();
        while (_it.hasNext())
        {
            JMelangeTemplateScriptFactory _impl = _it.next();
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

    public static JMelangeTemplateScriptFactory loadFactoryByExtension(String _name)
    {
        ServiceLoader<JMelangeTemplateScriptFactory> _loader = ServiceLoader.load(JMelangeTemplateScriptFactory.class);
        Iterator<JMelangeTemplateScriptFactory> _it = _loader.iterator();
        while (_it.hasNext())
        {
            JMelangeTemplateScriptFactory _impl = _it.next();
            if(_impl.matchesExtension(_name))
            {
                return _impl;
            }
        }
        return null;
    }
}
