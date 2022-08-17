package com.github.terefang.jmelange.scripted.impl;

import com.github.terefang.jmelange.scripted.AbstractScript;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlScript;
import org.apache.commons.jexl3.MapContext;
import org.codehaus.plexus.util.IOUtil;

import java.io.Reader;
import java.util.Map;

@Slf4j
public class Jexl3Script extends AbstractScript
{
    public static AbstractScript create() {
        return new Jexl3Script();
    }

    private JexlEngine engine;
    private JexlScript script;

    @Override
    public boolean init(Reader _script) {
        try
        {
            engine = new JexlBuilder().cache(512).strict(true).silent(false).create();
            script = engine.createScript(IOUtil.toString(_script));
            return false;
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
        JexlEngine l_engine = engine;
        JexlScript l_script = script;

        try
        {
            MapContext l_bind = new MapContext();
            for(Map.Entry<String,Object> e : this.assembleContext().entrySet())
            {
                l_bind.set(e.getKey(), e.getValue());
            }

            Object ret = l_script.execute(l_bind);

            return ret;
        }
        catch(Exception xe)
        {
            log.warn("error in file ", xe);
        }

        return null;
    }
}
