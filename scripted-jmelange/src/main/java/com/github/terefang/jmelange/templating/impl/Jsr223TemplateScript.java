package com.github.terefang.jmelange.templating.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import lombok.Data;
import lombok.SneakyThrows;
import org.codehaus.plexus.util.IOUtil;

import javax.script.*;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Map;

public class Jsr223TemplateScript extends AbstractTemplateScript
{
    String engineName;
    String script;

    ScriptEngineManager scriptEngineManager;
    ScriptEngine scriptEngine;
    private CompiledScript compiledScript;

    public static Jsr223TemplateScript from(String _engine)
    {
        Jsr223TemplateScript _ret = new Jsr223TemplateScript();
        _ret.engineName = _engine;
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
    public boolean initTemplate(Reader _script)
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

                if(this.scriptEngine instanceof Compilable)
                {
                    return (this.compiledScript = ((Compilable)this.scriptEngine).compile(_script)) != null;
                }
                else
                {
                    return CommonUtil.isNotBlank(this.script = IOUtil.toString(_script));
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
    public boolean executeTemplate()
    {
        Bindings _bind = this.scriptEngine.createBindings();
        for(Map.Entry<String, Object> _entry : this.assembleContext().entrySet())
        {
            _bind.put(_entry.getKey(), _entry.getValue());
        }

        Object _res = null;
        if(this.compiledScript != null)
        {
            SimpleScriptContext _context = new SimpleScriptContext();

            if(this.getInputStream()!=null)
            {
                _context.setReader(new InputStreamReader(this.getInputStream()));
            }

            if(this.getOutputStream()!=null)
            {
                _context.setWriter(new OutputStreamWriter(this.getOutputStream()));
            }

            _context.setBindings(_bind, ScriptContext.GLOBAL_SCOPE);
            _res = this.compiledScript.eval((_context));
        }
        else
        {
            synchronized (this.scriptEngine)
            {
                try
                {
                    this.scriptEngine.put(ScriptEngine.FILENAME, (this.getScriptFile()==null) ? "null": this.getScriptFile().getAbsolutePath());

                    _res = this.scriptEngine.eval(this.script, _bind);
                }
                finally
                {
                    this.scriptEngine.put(ScriptEngine.FILENAME, null);
                }
            }
        }
        return CommonUtil.checkBoolean(_res);
    }
}
