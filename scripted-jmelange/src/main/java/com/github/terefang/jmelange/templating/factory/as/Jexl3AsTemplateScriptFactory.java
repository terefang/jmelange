package com.github.terefang.jmelange.templating.factory.as;

import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.factory.Jexl3ScriptFactory;
import com.github.terefang.jmelange.templating.factory.as.AbstractScriptAsTemplateScriptFactory;

public class Jexl3AsTemplateScriptFactory extends AbstractScriptAsTemplateScriptFactory
{
    static Jexl3ScriptFactory factory;
    public synchronized JMelangeScriptFactory getScriptFactory()
    {
        if(factory==null)
        {
            factory = new Jexl3ScriptFactory();
        }
        return factory;
    }
}
