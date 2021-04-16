package com.github.terefang.jmelange.templating.factory;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactory;
import com.github.terefang.jmelange.templating.impl.GSimpleTemplateScript;

import java.util.List;

public class GSimpleTemplateScriptFactory implements JMelangeTemplateScriptFactory {
    @Override
    public String getName() {
        return "gsimple";
    }

    static List<String> SCRIPTNAMES = CommonUtil.toList("gsimple", "gsimpletemplate", "groovysimpletemplate");

    static List<String> SCRIPTEXTS = CommonUtil.toList(".gsp", ".gst");

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
        return new GSimpleTemplateScript();
    }
}
