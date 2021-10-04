package com.github.terefang.jmelange.scripted.impl;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;

import java.util.Map;

@Slf4j
public class GroovyScript extends Jsr223Script
{

    public static GroovyScript create() {
        GroovyScript _g = new GroovyScript();
        _g.setScriptEngine(new GroovyScriptEngineFactory().getScriptEngine());
        return _g;
    }

    @Override
    public Map<String, Object> assembleContext() {
        Map<String, Object> _context = super.assembleContext();

        if(this.getArgs()!=null)
        {
            String[] _args = this.getArgs().toArray(new String[this.getArgs().size()]);
            _context.put("args", _args);
        }

        return _context;
    }
}
