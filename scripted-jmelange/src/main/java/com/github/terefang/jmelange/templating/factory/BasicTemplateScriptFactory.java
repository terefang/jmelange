package com.github.terefang.jmelange.templating.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactory;


import java.util.List;

public class BasicTemplateScriptFactory implements JMelangeTemplateScriptFactory {
    @Override
    public String getName() {
        return "basicTemplate";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("basicTemplate", "basic");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".bt", ".basictemplate");

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
        return null;
    }
}
