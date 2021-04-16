package com.github.terefang.jmelange.templating.factory.as;

import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.factory.JythonScriptFactory;

public class JythonAsTemplateScriptFactory extends AbstractScriptAsTemplateScriptFactory
{
    static JythonScriptFactory factory;
    public synchronized JMelangeScriptFactory getScriptFactory()
    {
        if(factory==null)
        {
            factory = new JythonScriptFactory();
        }
        return factory;
    }
}
