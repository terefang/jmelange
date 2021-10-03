package com.github.terefang.jmelange.templating.factory.esp;

import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactory;
import com.github.terefang.jmelange.templating.impl.EspTemplateScript;

import java.util.List;

public abstract class AbstractEspTemplateScriptFactory implements JMelangeTemplateScriptFactory
{
    @Override
    public String getName() {
        return getScriptFactory().getName();
    }

    @Override
    public List<String> getScriptNames() {
        return getScriptFactory().getScriptNames();
    }

    @Override
    public List<String> getScriptExts() {
        return getScriptFactory().getScriptExts();
    }

    @Override
    public boolean matchesName(String _name)
    {
        for(String _n : this.getScriptNames())
        {
            if(_name.equalsIgnoreCase(_n)) return true;
        }
        return false;
    }

    @Override
    public boolean matchesExtension(String _file)
    {
        for(String _e : this.getScriptExts())
        {
            if(_file.endsWith("."+_e)) return true;
        }
        return false;
    }

    @Override
    public AbstractTemplateScript createTemplateScript() {
        return EspTemplateScript.from(getScriptFactory().createScript());
    }

    public abstract JMelangeScriptFactory getScriptFactory();
}
