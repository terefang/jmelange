package com.github.terefang.jmelange.script;


import com.github.terefang.jmelange.script.esp.AbstractEspTemplateFactory;
import com.github.terefang.jmelange.script.esp.EspDeTagifier;
import com.github.terefang.jmelange.script.esp.EspTemplateImpl;

import java.util.Arrays;
import java.util.List;

public class RhinoEspTemplateFactory extends AbstractEspTemplateFactory implements ITemplateContextFactory
{
    @Override
    public IScriptContextFactory getScriptFactory() {
        return IScriptContextFactory.findByName("rhino");
    }

    @Override
    public AbstractTemplate createTemplateScript()
    {
        EspTemplateImpl _ets = (EspTemplateImpl)super.createTemplateScript();
        _ets.setVp(BasicVariableProvider.create());
        EspDeTagifier _dt = _ets.getDeTagifier();
        _dt.setParseStart("/* START */");
        _dt.setParseEnd("\n/* END */\n");
        _dt.setOutputStart(" print(`");
        _dt.setOutputEnd("`);\n");
        _dt.setExprStart(" print(");
        _dt.setExprEnd(");\n");
        _dt.setCommentStart(" /* ");
        _dt.setCommentEnd(" */ ");
        _dt.setEscapeStyle(EspDeTagifier.ESCAPE_JAVA_UNICODE);
        return _ets;
    }

    @Override
    public String getName() {
        return "rhino-esp";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("ejs", "rhino-esp");
    }

    @Override
    public String getExt() {
        return ".ejs";
    }

    @Override
    public List<String> getExts() {
        return Arrays.asList(".rhino.esp", ".ejs");
    }

}
