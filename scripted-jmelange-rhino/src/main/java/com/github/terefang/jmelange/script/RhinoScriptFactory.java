package com.github.terefang.jmelange.script;

import java.util.Arrays;
import java.util.List;

public class RhinoScriptFactory extends AbstractScriptFactory implements IScriptContextFactory
{
    @Override
    public IScriptContext newInstance(String _name, String _ext, List<String> _path) {
        return RhinoScriptContext.create(_path);
    }

    @Override
    public String getName() {
        return "rhino";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("ecmascript", "javascript", "rhino");
    }

    @Override
    public String getExt() {
        return ".js";
    }

    @Override
    public List<String> getExts() {
        return Arrays.asList(".ecma",".rhino",".js");
    }

}
