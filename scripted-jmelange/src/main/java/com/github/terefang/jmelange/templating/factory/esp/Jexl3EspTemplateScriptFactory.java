package com.github.terefang.jmelange.templating.factory.esp;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.factory.Jexl3ScriptFactory;
import com.github.terefang.jmelange.scripted.util.DeTagifier;
import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.impl.EspTemplateScript;

import java.util.List;

public class Jexl3EspTemplateScriptFactory extends AbstractEspTemplateScriptFactory
{
    static Jexl3ScriptFactory factory;
    public synchronized JMelangeScriptFactory getScriptFactory()
    {
        if(factory==null)
        {
            factory = new Jexl3ScriptFactory();
        }
        return factory;
    }

    @Override
    public AbstractTemplateScript createTemplateScript() {
        EspTemplateScript _ets = (EspTemplateScript) super.createTemplateScript();
        DeTagifier _dt = new DeTagifier();
        _ets.setDeTagifier(_dt);

        _dt.setParseStart("/* START */ ");
        _dt.setParseEnd(" /* END */");
        _dt.setOutputStart(" _sout.print(`");
        _dt.setOutputEnd("`);\n");
        _dt.setExprStart(" _sout.print(");
        _dt.setExprEnd(");\n");
        _dt.setCommentStart(" /* ");
        _dt.setCommentEnd(" */\n");
        _dt.setEscapeStyle(DeTagifier.ESCAPE_JAVA_UNICODE);
        return _ets;
    }

    @Override
    public String getName() {
        return "jexl-esp";
    }

    @Override
    public List<String> getScriptNames() {
        return CommonUtil.toList("jexl-esp", "jx-esp");
    }

    @Override
    public List<String> getScriptExts() {
        return CommonUtil.toList("jxp", "ejx");
    }

}
