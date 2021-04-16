package com.github.terefang.jmelange.templating.factory.as;

import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactory;
import com.github.terefang.jmelange.templating.impl.ScriptAsTemplateScript;

import java.util.List;

public abstract class AbstractScriptAsTemplateScriptFactory implements JMelangeTemplateScriptFactory
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
    public boolean matchesName(String _name) {
        return getScriptFactory().matchesExtension(_name);
    }

    @Override
    public boolean matchesExtension(String _file) {
        return getScriptFactory().matchesExtension(_file);
    }

    @Override
    public AbstractTemplateScript createTemplateScript() {
        return ScriptAsTemplateScript.from(getScriptFactory().createScript());
    }

    public abstract JMelangeScriptFactory getScriptFactory();
}
