package com.github.terefang.jmelange.templating.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactory;
import com.github.terefang.jmelange.templating.impl.JxltTemplateScript;

import java.util.List;

public class JxltTemplateScriptFactory implements JMelangeTemplateScriptFactory {
    @Override
    public String getName() {
        return "jxlt";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("jxlt");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".jxlt", ".jxt", ".jt");

    @Override
    public List<String> getScriptNames() {
        return SCRIPTNAMES;
    }

    @Override
    public List<String> getScriptExts() {
        return SCRIPTEXTS;
    }

    @Override
    public AbstractTemplateScript createTemplateScript() {
        return new JxltTemplateScript();
    }
}
