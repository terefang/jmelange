package com.github.terefang.jmelange.script;

import com.github.terefang.jmelange.script.IScriptContextFactory;
import com.github.terefang.jmelange.script.ITemplateContextFactory;
import com.github.terefang.jmelange.script.as.AbstractAsTemplateFactory;

import java.util.Arrays;
import java.util.List;

public class Jexl3AsTemplateFactory
        extends AbstractAsTemplateFactory
        implements ITemplateContextFactory
{
    @Override
    public IScriptContextFactory getScriptFactory() {
        return IScriptContextFactory.findByName("jexl3");
    }

    @Override
    public String getName() {
        return "jexl3";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("jexl", "jexl3");
    }

    @Override
    public String getExt() {
        return ".jexl";
    }

    @Override
    public List<String> getExts() {
        return Arrays.asList(".jx", ".jexl", ".jexl3");
    }
}
