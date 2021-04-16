package com.github.terefang.jmelange.scripted.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.scripted.util.SimpleVariableProvider;
import org.python.jsr223.PyScriptEngineFactory;

import javax.script.ScriptContext;

public class JythonScript extends Jsr223Script
{
    public static JythonScript create()
    {
        JythonScript _scp = new JythonScript();
        _scp.engineName = "jython";
        _scp.setScriptEngine(new PyScriptEngineFactory().getScriptEngine());
        return _scp;
    }
}
