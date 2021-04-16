package com.github.terefang.jmelange.templating.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactory;
import com.github.terefang.jmelange.templating.impl.ThymeleafTemplateScript;

import java.util.List;

public class ThymeleafTemplateScriptFactory implements JMelangeTemplateScriptFactory {
    @Override
    public String getName() {
        return "thymeleaf";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("thymeleaf");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".tl", ".tlf");

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
        return new ThymeleafTemplateScript();
    }
}
