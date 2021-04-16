package com.github.terefang.jmelange.templating;

import java.util.List;

public interface JMelangeTemplateScriptFactory {
    public String getName();

    List<String> getScriptNames();

    default boolean matchesName(String _name)
    {
        for(String _test : getScriptNames())
        {
            if(_test.equalsIgnoreCase(_name)) return true;
        }
        return false;
    }

    List<String> getScriptExts();

    default boolean matchesExtension(String _file)
    {
        for(String _test : getScriptExts())
        {
            if(_file.endsWith(_test)) return true;
        }
        return false;
    }

    public AbstractTemplateScript createTemplateScript();
}
