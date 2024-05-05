package com.github.terefang.jmelange.template.jinjava;

import com.github.terefang.jmelange.script.AbstractTemplateFactory;
import com.github.terefang.jmelange.script.ITemplateContext;
import com.github.terefang.jmelange.script.ITemplateContextFactory;

import java.util.Arrays;
import java.util.List;

public class JinjavaTemplateFactory extends AbstractTemplateFactory implements ITemplateContextFactory {
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

    static List<String> SCRIPTNAMES = Arrays.asList("jinjava","jinja2");

    static List<String> SCRIPTEXTS = Arrays.asList(".j2",".jj2");

    @Override
    public ITemplateContext newInstance(String _name, String _ext, List<String> _path) {
        return JinjavaTemplateImpl.create(_name, _ext, _path);
    }
}
