package com.github.terefang.jmelange.scripted.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.impl.RhinoScript;

import java.util.List;

public class RhinoScriptFactory implements JMelangeScriptFactory {
    @Override
    public String getName() {
        return "rhino";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("rhinoscript", "ecmascript", "ecma");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".js", ".ecma");

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
        RhinoScript _scp = new RhinoScript();
        return _scp;
    }
}
