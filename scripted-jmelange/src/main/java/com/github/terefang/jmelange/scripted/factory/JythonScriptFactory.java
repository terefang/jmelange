package com.github.terefang.jmelange.scripted.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.impl.JythonScript;

import java.util.List;

public class JythonScriptFactory implements JMelangeScriptFactory {
    @Override
    public String getName() {
        return "jython";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("jython", "python");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".jy", ".py");

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
        return JythonScript.create();
    }
}
