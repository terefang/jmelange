package com.github.terefang.jmelange.script;

import java.util.Arrays;
import java.util.List;

public class Jexl3ScriptFactory extends AbstractScriptFactory implements IScriptContextFactory
{
    @Override
    public IScriptContext newInstance(String _name, String _ext, List<String> _path) {
        return new Jexl3ScriptContext();
    }

    @Override
    public String getName() {
        return "jexl3";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("jexl3", "jexl");
    }

    @Override
    public String getExt() {
        return ".jexl";
    }

    @Override
    public List<String> getExts() {
        return Arrays.asList(".jx",".jexl");
    }

}
