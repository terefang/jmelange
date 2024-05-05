package com.github.terefang.jmelange.script;

import java.util.Arrays;
import java.util.List;

public class BshScriptFactory extends AbstractScriptFactory implements IScriptContextFactory
{
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

    static List<String> SCRIPTNAMES = Arrays.asList("beanshell", "bsh");

    static List<String> SCRIPTEXTS = Arrays.asList(".bsh", ".beanshell");

    @Override
    public IScriptContext newInstance(String _name, String _ext, List<String> _path) {
        return BshScriptImpl.create(_name, _ext, _path);
    }
}
