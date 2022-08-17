package com.github.terefang.jmelange.scripted.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.impl.BeanScript;

import java.util.List;

public class BeanScriptFactory implements JMelangeScriptFactory {
    @Override
    public String getName() {
        return "bsh";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("bsh", "beanshell");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".bsh");

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
        return BeanScript.create();
    }
}
