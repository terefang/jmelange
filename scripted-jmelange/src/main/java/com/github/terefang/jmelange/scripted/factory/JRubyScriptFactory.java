package com.github.terefang.jmelange.scripted.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.impl.JRubyScript;

import java.util.List;

public class JRubyScriptFactory implements JMelangeScriptFactory {
    @Override
    public String getName() {
        return "jruby";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("jruby", "ruby");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".jrb", ".rb");

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
        JRubyScript _scp = JRubyScript.create();
        return _scp;
    }
}
