package com.github.terefang.jmelange.script;

import com.github.terefang.jmelange.script.AbstractTemplate;
import com.github.terefang.jmelange.script.IScriptContextFactory;
import com.github.terefang.jmelange.script.ITemplateContextFactory;
import com.github.terefang.jmelange.script.esp.AbstractEspTemplateFactory;
import com.github.terefang.jmelange.script.esp.EspDeTagifier;
import com.github.terefang.jmelange.script.esp.EspTemplateImpl;

import java.util.Arrays;
import java.util.List;

public class Jexl3EspTemplateFactory
        extends AbstractEspTemplateFactory
        implements ITemplateContextFactory
{
    @Override
    public IScriptContextFactory getScriptFactory() {
        return IScriptContextFactory.findByName("jexl3");
    }

    @Override
    public AbstractTemplate createTemplateScript()
    {
        EspTemplateImpl _ets = (EspTemplateImpl)super.createTemplateScript();
        EspDeTagifier _dt = _ets.getDeTagifier();
        _dt.setParseStart("/* START */ ");
        _dt.setParseEnd(" /* END */");
        _dt.setOutputStart(" _sout.print(`");
        _dt.setOutputEnd("`);\n");
        _dt.setExprStart(" _sout.print(");
        _dt.setExprEnd(");\n");
        _dt.setCommentStart(" /* ");
        _dt.setCommentEnd(" */\n");
        _dt.setEscapeStyle(EspDeTagifier.ESCAPE_JAVA_UNICODE);
        return _ets;
    }

    @Override
    public String getName() {
        return "jexl3-esp";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("ejxl", "jexl3-esp");
    }

    @Override
    public String getExt() {
        return ".jxp";
    }

    @Override
    public List<String> getExts() {
        return Arrays.asList(".jexl.esp", ".jexl3.esp", ".jx.esp", ".ejx", ".jxp");
    }
}
