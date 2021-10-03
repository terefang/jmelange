package com.github.terefang.jmelange.scripted.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import lombok.SneakyThrows;
import org.codehaus.plexus.util.IOUtil;

import javax.script.*;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Map;


public class Jsr223Script extends AbstractScript
{
    String engineName;
    String script;

    ScriptEngineManager scriptEngineManager;
    ScriptEngine scriptEngine;
    CompiledScript compiledScript;

    public static Jsr223Script from(String _engine)
    {
        Jsr223Script _ret = new Jsr223Script();
        _ret.engineName = _engine;
        return _ret;
    }

    public static Jsr223Script from(ScriptEngineManager _engine)
    {
        Jsr223Script _ret = new Jsr223Script();
        _ret.setScriptEngineManager(_engine);
        return _ret;
    }

    public static Jsr223Script from(ScriptEngine _engine)
    {
        Jsr223Script _ret = new Jsr223Script();
        _ret.setScriptEngine(_engine);
        return _ret;
    }

    public ScriptEngineManager getScriptEngineManager() {
        return scriptEngineManager;
    }

    public void setScriptEngineManager(ScriptEngineManager scriptEngineManager) {
        this.scriptEngineManager = scriptEngineManager;
    }

    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    public void setScriptEngine(ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    @Override
    @SneakyThrows
    public boolean init(Reader _script)
    {
        if(this.scriptEngine == null)
        {
            if(this.scriptEngineManager == null)
            {
                this.scriptEngineManager = new ScriptEngineManager();
            }

            this.scriptEngine = this.scriptEngineManager.getEngineByName(this.engineName);
        }

        synchronized (this.scriptEngine)
        {
            try
            {
                this.scriptEngine.put(ScriptEngine.FILENAME, (this.getScriptFile()==null) ? "null": this.getScriptFile().getAbsolutePath());

                this.script = IOUtil.toString(_script);

                if(this.scriptEngine instanceof Compilable)
                {
                    return (this.compiledScript = ((Compilable)this.scriptEngine).compile(_script)) != null;
                }
                else
                {
                    return CommonUtil.isNotBlank(this.script);
                }
            }
            finally
            {
                this.scriptEngine.put(ScriptEngine.FILENAME, null);
            }
        }
    }

    @Override
    @SneakyThrows
    public Object executeObject(boolean _scopeOrBinding)
    {
        Bindings scriptEngineBindings = this.scriptEngine.createBindings();
        for(Map.Entry<String, Object> _entry : this.assembleContext().entrySet())
        {
            scriptEngineBindings.put(_entry.getKey(), _entry.getValue());
        }

        SimpleScriptContext _context = new SimpleScriptContext();

        if(this.getInputStream()!=null)
        {
            _context.setReader(new InputStreamReader(this.getInputStream()));
        }

        if(this.getOutputStream()!=null)
        {
            _context.setWriter(new OutputStreamWriter(this.getOutputStream()));
        }

        _context.setBindings(scriptEngineBindings, ScriptContext.GLOBAL_SCOPE);

        Object _res = null;

        if((this.compiledScript != null) && _scopeOrBinding)
        {
            _res = this.compiledScript.eval(_context);
        }
        else
        if((this.compiledScript != null) && !_scopeOrBinding)
        {
            _res = this.compiledScript.eval(scriptEngineBindings);
        }
        else
        {
            synchronized (this.scriptEngine)
            {
                try
                {
                    this.scriptEngine.put(ScriptEngine.FILENAME, (this.getScriptFile()==null) ? "null": this.getScriptFile().getAbsolutePath());

                    _res = this.scriptEngine.eval(this.script, _context);
                }
                finally
                {
                    this.scriptEngine.put(ScriptEngine.FILENAME, null);
                }
            }
        }

        if(_res == null)
        {
            _res = scriptEngineBindings;
        }

        if((_res instanceof Bindings) && ((Bindings)_res).containsKey("_result"))
        {
            _res = ((Bindings)_res).get("_result");
        }

        return _res;
    }
}
