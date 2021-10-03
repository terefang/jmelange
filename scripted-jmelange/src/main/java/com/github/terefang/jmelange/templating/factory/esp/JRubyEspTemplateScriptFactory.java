package com.github.terefang.jmelange.templating.factory.esp;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.factory.JRubyScriptFactory;
import com.github.terefang.jmelange.scripted.util.DeTagifier;
import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.impl.EspTemplateScript;

import java.util.List;

public class JRubyEspTemplateScriptFactory extends AbstractEspTemplateScriptFactory
{
    static JRubyScriptFactory factory;
    public synchronized JMelangeScriptFactory getScriptFactory()
    {
        if(factory==null)
        {
            factory = new JRubyScriptFactory();
        }
        return factory;
    }

    @Override
    public AbstractTemplateScript createTemplateScript() {
        EspTemplateScript _ets = (EspTemplateScript) super.createTemplateScript();
        DeTagifier _dt = new DeTagifier();
        _ets.setDeTagifier(_dt);

        _dt.setParseStart("### START\n");
        _dt.setParseEnd("\n### END\n");
        _dt.setOutputStart("\nputs \"");
        _dt.setOutputEnd("\"\n");
        _dt.setExprStart("\nputs (");
        _dt.setExprEnd(")\n");
        _dt.setCommentStart("\n=begin\n");
        _dt.setCommentEnd("\n=end\n");
        _dt.setEscapeStyle(DeTagifier.ESCAPE_JAVA_UNICODE);
        return _ets;
    }

    @Override
    public String getName() {
        return "jruby-esp";
    }

    @Override
    public List<String> getScriptNames() {
        return CommonUtil.toList("jruby-esp", "ruby-esp");
    }

    @Override
    public List<String> getScriptExts() {
        return CommonUtil.toList("ejrb", "erb");
    }

}
