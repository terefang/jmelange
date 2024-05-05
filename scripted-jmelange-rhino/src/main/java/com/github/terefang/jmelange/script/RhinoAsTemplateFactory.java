package com.github.terefang.jmelange.script;


import com.github.terefang.jmelange.script.as.AbstractAsTemplateFactory;

import java.util.Arrays;
import java.util.List;

public class RhinoAsTemplateFactory extends AbstractAsTemplateFactory implements ITemplateContextFactory
{
    @Override
    public IScriptContextFactory getScriptFactory() {
        return IScriptContextFactory.findByName("rhino");
    }

    @Override
    public String getName() {
        return "rhino";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("ecmascript", "javascript", "rhino");
    }

    @Override
    public String getExt() {
        return ".rhino";
    }

    @Override
    public List<String> getExts() {
        return Arrays.asList(".js", ".ecma", ".rhino");
    }

}
