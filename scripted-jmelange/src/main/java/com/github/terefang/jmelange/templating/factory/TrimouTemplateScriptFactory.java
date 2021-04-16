package com.github.terefang.jmelange.templating.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactory;
import com.github.terefang.jmelange.templating.impl.TrimouTemplateScript;

import java.util.List;

public class TrimouTemplateScriptFactory implements JMelangeTemplateScriptFactory {
    @Override
    public String getName() {
        return "trimou";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("trimou", "handlebars", "mustache");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".tri", ".trimou", ".hbs", ".handlebars", ".mustache");

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
        return new TrimouTemplateScript();
    }
}
