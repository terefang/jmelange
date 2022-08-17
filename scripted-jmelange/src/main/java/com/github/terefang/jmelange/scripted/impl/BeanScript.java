package com.github.terefang.jmelange.scripted.impl;


import bsh.engine.BshScriptEngine;
import com.github.terefang.jmelange.commons.util.IOUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import lombok.extern.slf4j.Slf4j;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.SimpleBindings;
import java.io.Reader;
import java.util.Map;

@Slf4j
public class BeanScript extends AbstractScript
{
    private BshScriptEngine engine;
    private CompiledScript script;

    public static AbstractScript create() {
        return new BeanScript();
    }

    @Override
    public boolean init(Reader _script) {
        try
        {
            this.engine = new BshScriptEngine();
            this.script = engine.compile(_script);
            return true;
        }
        catch (Exception e)
        {
            log.warn("error", e);
        }
        finally
        {
            IOUtil.close(_script);
        }
        return false;
    }

    @Override
    public Object executeObject(boolean _scopeOrBinding)
    {
        try
        {
            if(_scopeOrBinding)
            {
                ScriptContext l_bind = this.script.getEngine().getContext();
                for(Map.Entry<String,Object> e : this.assembleContext().entrySet())
                {
                    l_bind.setAttribute(e.getKey(), e.getValue(), ScriptContext.GLOBAL_SCOPE);
                }

                Object ret = this.script.eval(l_bind);
                return ret;
            }
            else
            {
                Bindings l_bind = new SimpleBindings();
                for(Map.Entry<String,Object> e : this.assembleContext().entrySet())
                {
                    l_bind.put(e.getKey(), e.getValue());
                }

                Object ret = this.script.eval(l_bind);
                return ret;
            }
        }
        catch(Exception xe)
        {
            log.warn("error in file ", xe);
        }

        return null;
    }
}
