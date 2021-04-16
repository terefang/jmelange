package com.github.terefang.jmelange.templating.factory.as;

import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.factory.GroovyScriptFactory;
import com.github.terefang.jmelange.templating.factory.as.AbstractScriptAsTemplateScriptFactory;

public class GroovyAsTemplateScriptFactory extends AbstractScriptAsTemplateScriptFactory
{
    static GroovyScriptFactory factory;
    public synchronized JMelangeScriptFactory getScriptFactory()
    {
        if(factory==null)
        {
            factory = new GroovyScriptFactory();
        }
        return factory;
    }
}
