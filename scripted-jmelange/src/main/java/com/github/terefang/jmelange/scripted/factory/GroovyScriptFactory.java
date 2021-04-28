package com.github.terefang.jmelange.scripted.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.impl.Jsr223Script;
import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;

import java.util.List;

public class GroovyScriptFactory implements JMelangeScriptFactory {
    @Override
    public String getName() {
        return "groovy";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("groovyscript", "gscript", "groovy");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".groovy", ".gy");

    @Override
    public List<String> getScriptNames() {
        return SCRIPTNAMES;
    }

    @Override
    public List<String> getScriptExts() {
        return SCRIPTEXTS;
    }

    @Override
    public AbstractScript createScript() {
        return Jsr223Script.from(new GroovyScriptEngineFactory().getScriptEngine());
    }
}
