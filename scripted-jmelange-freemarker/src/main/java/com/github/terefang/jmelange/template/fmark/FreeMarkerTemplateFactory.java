package com.github.terefang.jmelange.template.fmark;

import com.github.terefang.jmelange.script.AbstractTemplateFactory;
import com.github.terefang.jmelange.script.ITemplateContext;
import com.github.terefang.jmelange.script.ITemplateContextFactory;

import java.util.Arrays;
import java.util.List;

public class FreeMarkerTemplateFactory extends AbstractTemplateFactory implements ITemplateContextFactory {
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

    static List<String> SCRIPTNAMES = Arrays.asList("fm", "freemarker");

    static List<String> SCRIPTEXTS = Arrays.asList(".ftl",".fm",".fhtml",".fxml",".fjs",".ftxt");

    @Override
    public ITemplateContext newInstance(String _name, String _ext, List<String> _path) {
        return FreeMarkerTemplateImpl.create(_name, _ext, _path);
    }
}
