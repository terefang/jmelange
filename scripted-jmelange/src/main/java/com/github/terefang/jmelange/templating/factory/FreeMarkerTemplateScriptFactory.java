package com.github.terefang.jmelange.templating.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactory;
import com.github.terefang.jmelange.templating.impl.FreeMarkerTemplateScript;

import java.util.List;

public class FreeMarkerTemplateScriptFactory implements JMelangeTemplateScriptFactory {
    @Override
    public String getName() {
        return "freemarker";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("freemarker");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".fm", ".ftl");

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
        return new FreeMarkerTemplateScript();
    }
}
