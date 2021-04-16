package com.github.terefang.jmelange.templating.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactory;
import com.github.terefang.jmelange.templating.impl.JinjavaTemplateScript;

import java.util.List;

public class JinjavaTemplateScriptFactory implements JMelangeTemplateScriptFactory {
    @Override
    public String getName() {
        return "jinjava";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("jinja", "jinjava");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".j2");

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
        return new JinjavaTemplateScript();
    }
}
