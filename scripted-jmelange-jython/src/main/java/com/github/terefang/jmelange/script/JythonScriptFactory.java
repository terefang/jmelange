package com.github.terefang.jmelange.script;

import java.util.Arrays;
import java.util.List;

public class JythonScriptFactory extends AbstractScriptFactory implements IScriptContextFactory
{
    @Override
    public IScriptContext newInstance(String _name, String _ext, List<String> _path) {
        return JythonScriptContext.create(_path);
    }

    @Override
    public String getName() {
        return "jython";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("python", "jython");
    }

    @Override
    public String getExt() {
        return ".jy";
    }

    @Override
    public List<String> getExts() {
        return Arrays.asList(".py",".jy");
    }

}
