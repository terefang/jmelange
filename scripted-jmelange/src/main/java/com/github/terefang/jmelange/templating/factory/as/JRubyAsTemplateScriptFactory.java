package com.github.terefang.jmelange.templating.factory.as;

import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.factory.JRubyScriptFactory;

public class JRubyAsTemplateScriptFactory extends AbstractScriptAsTemplateScriptFactory
{
    static JRubyScriptFactory factory;
    public synchronized JMelangeScriptFactory getScriptFactory()
    {
        if(factory==null)
        {
            factory = new JRubyScriptFactory();
        }
        return factory;
    }
}
