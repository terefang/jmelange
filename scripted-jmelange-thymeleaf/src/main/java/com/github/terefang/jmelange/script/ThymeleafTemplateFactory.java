package com.github.terefang.jmelange.script;

import java.util.Arrays;
import java.util.List;

public class ThymeleafTemplateFactory extends AbstractTemplateFactory implements ITemplateContextFactory {
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

    static List<String> SCRIPTNAMES = Arrays.asList("thymeleaf");

    static List<String> SCRIPTEXTS = Arrays.asList(".tl",".tlf",".thtml");

    @Override
    public ITemplateContext newInstance(String _name, String _ext, List<String> _path) {
        return ThymeleafTemplateImpl.create(_name, _ext, _path);
    }
}
