package com.github.terefang.jmelange.scripted.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.impl.Jexl3Script;

import java.util.List;

public class Jexl3ScriptFactory implements JMelangeScriptFactory {
    @Override
    public String getName() {
        return "jexl";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("jexl3", "jexl");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".jexl", ".jx");

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
        return Jexl3Script.create();
    }
}
