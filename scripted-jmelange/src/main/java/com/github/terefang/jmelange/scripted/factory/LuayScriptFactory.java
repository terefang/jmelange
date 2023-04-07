package com.github.terefang.jmelange.scripted.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.impl.LuayScript;

import java.util.List;

public class LuayScriptFactory implements JMelangeScriptFactory {
    @Override
    public String getName() {
        return "luay";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("lua", "luay", "luayscript");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".lua", ".luay");

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
        return LuayScript.create();
    }
}
