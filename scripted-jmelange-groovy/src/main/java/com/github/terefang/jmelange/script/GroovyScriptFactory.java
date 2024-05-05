package com.github.terefang.jmelange.script;

import java.util.Arrays;
import java.util.List;

public class GroovyScriptFactory extends AbstractScriptFactory implements IScriptContextFactory
{
    @Override
    public IScriptContext newInstance(String _name, String _ext, List<String> _path) {
        return GroovyScriptContext.create(_path);
    }

    @Override
    public String getName() {
        return "groovy";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("groovyscript", "gscript", "groovy");
    }

    @Override
    public String getExt() {
        return ".groovy";
    }

    @Override
    public List<String> getExts() {
        return Arrays.asList(".gy",".groovy");
    }

}
