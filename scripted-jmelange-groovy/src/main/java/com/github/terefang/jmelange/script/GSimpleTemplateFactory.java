package com.github.terefang.jmelange.script;

import java.util.Arrays;
import java.util.List;

public class GSimpleTemplateFactory
        extends AbstractTemplateFactory
        implements ITemplateContextFactory
{
    @Override
    public String getName() {
        return "gsimple";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("gsimple", "gsimpletemplate", "groovysimpletemplate");
    }

    @Override
    public String getExt() {
        return ".gsp";
    }

    @Override
    public List<String> getExts() {
        return Arrays.asList(".gst", ".gsp");
    }

    @Override
    public ITemplateContext newInstance(String _name, String _ext, List<String> _path) {
        return GSimpleTemplateImpl.create();
    }
}
