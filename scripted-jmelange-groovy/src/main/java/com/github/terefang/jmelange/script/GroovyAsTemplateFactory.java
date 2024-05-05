package com.github.terefang.jmelange.script;

import com.github.terefang.jmelange.script.IScriptContextFactory;
import com.github.terefang.jmelange.script.ITemplateContextFactory;
import com.github.terefang.jmelange.script.as.AbstractAsTemplateFactory;

import java.util.Arrays;
import java.util.List;

public class GroovyAsTemplateFactory
        extends AbstractAsTemplateFactory
        implements ITemplateContextFactory
{
    @Override
    public IScriptContextFactory getScriptFactory() {
        return IScriptContextFactory.findByName("groovy");
    }

    @Override
    public String getName() {
        return "groovy";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("groovy");
    }

    @Override
    public String getExt() {
        return ".groovy";
    }

    @Override
    public List<String> getExts() {
        return Arrays.asList(".gy", ".groovy");
    }
}
