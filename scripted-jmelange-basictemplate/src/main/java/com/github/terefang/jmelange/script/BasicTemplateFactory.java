package com.github.terefang.jmelange.script;

import java.util.Arrays;
import java.util.List;

public class BasicTemplateFactory extends AbstractTemplateFactory implements ITemplateContextFactory {
    @Override
    public String getName() {
        return SCRIPTNAMES.get(0);
    }

    @Override
    public List<String> getNames() {
        return SCRIPTNAMES;
    }

    @Override
    public String getExt() {
        return SCRIPTEXTS.get(0);
    }

    @Override
    public List<String> getExts() {
        return SCRIPTEXTS;
    }

    static List<String> SCRIPTNAMES = Arrays.asList("basicTemplate", "basic");

    static List<String> SCRIPTEXTS = Arrays.asList(".bt", ".basictemplate");

    @Override
    public ITemplateContext newInstance(String _name, String _ext, List<String> _path) {
        return BasicTemplateImpl.create(_name, _ext, _path);
    }
}
