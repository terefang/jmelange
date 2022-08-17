package com.github.terefang.jmelange.scripted.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.impl.LuajScript;

import java.util.List;

public class LuajScriptFactory implements JMelangeScriptFactory {
    @Override
    public String getName() {
        return "luaj";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("lua", "luaj", "luascript");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".lua", ".luaj");

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
        return LuajScript.create();
    }
}
