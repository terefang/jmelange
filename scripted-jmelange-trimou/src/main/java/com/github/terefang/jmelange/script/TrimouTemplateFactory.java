package com.github.terefang.jmelange.script;

import java.util.Arrays;
import java.util.List;

public class TrimouTemplateFactory extends AbstractTemplateFactory implements ITemplateContextFactory {
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

    static List<String> SCRIPTNAMES = Arrays.asList("trimou", "handlebars", "mustache");

    static List<String> SCRIPTEXTS = Arrays.asList(".tri", ".trimou", ".hbs", ".handlebars", ".mustache");

    @Override
    public ITemplateContext newInstance(String _name, String _ext, List<String> _path) {
        return TrimouTemplateImpl.create(_name, _ext, _path);
    }
}
