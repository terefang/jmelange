package com.github.terefang.jmelange.script;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlScript;
import org.apache.commons.jexl3.MapContext;
import com.github.terefang.jmelange.plexus.util.IOUtil;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

@Slf4j
public class Jexl3ScriptContext extends AbstractScript implements IScriptContext
{
    private JexlEngine engine;
    private JexlScript script;

    private List<String> includes;
    OutputStream outputStream;
    @Override
    public void setOutputStream(OutputStream _out) {
        this.outputStream = _out;
    }

    @Override
    @SneakyThrows
    public void compile(Reader _reader, String _name)
    {
        try
        {
            engine = Jexl3EngineFactory.newInstance(null);
            script = engine.createScript(IOUtil.toString(_reader));
        }
        finally
        {
            IOUtil.close(_reader);
        }
    }

    @Override
    public Object run(Map<String, Object> _top, boolean _useLocal)
    {
        JexlEngine l_engine = engine;
        JexlScript l_script = script;
        PrintStream _sout = null;
        try
        {
            Jexl3NsContext l_bind = new Jexl3NsContext();
            for(Map.Entry<String,Object> e : this.provide(_top).entrySet())
            {
                l_bind.set(e.getKey(), e.getValue());
            }

            if(this.outputStream!=null)
            {
                l_bind.set("_out", this.outputStream);
                l_bind.set("_sout", _sout = new PrintStream(this.outputStream) {
                    @Override
                    public void close() {
                        this.flush();
                    }
                });
            }

            Object ret = l_script.execute(l_bind);

            if(this.outputStream!=null)
            {
                IOUtil.close(_sout);
            }

            return ret;
        }
        catch(Exception xe)
        {
            log.warn("error in file ", xe);
        }
        return null;
    }


}
