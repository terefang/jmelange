package com.github.terefang.jmelange.script;

import bsh.engine.BshScriptEngine;
import com.github.terefang.jmelange.commons.util.IOUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.SimpleBindings;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

@Slf4j
public class BshScriptImpl extends AbstractScript implements IScriptContext
{
    private BshScriptEngine engine;
    private CompiledScript script;
    OutputStream outputStream;
    @Override
    public void setOutputStream(OutputStream _out) {
        this.outputStream = _out;
    }

    public static IScriptContext create(String _name, String _ext, List<String> _path)
    {
        BshScriptImpl _bt = new BshScriptImpl();
        return _bt;
    }

    public BshScriptImpl()
    {
        this.setVp(BasicVariableProvider.create());
    }

    @Override
    @SneakyThrows
    public void compile(Reader _reader, String _name)
    {
        try
        {
            this.engine = new BshScriptEngine();
            this.script = engine.compile(_reader);
        }
        catch (Exception e)
        {
            log.warn("error", e);
        }
        finally
        {
            IOUtil.close(_reader);
        }
    }

    @Override
    public Object run(Map<String, Object> _top, boolean _useLocal)
    {
        try
        {
            if(_useLocal)
            {
                ScriptContext l_bind = this.script.getEngine().getContext();
                PrintStream _sout = null;

                for(Map.Entry<String,Object> e : this.provide(_top).entrySet())
                {
                    l_bind.setAttribute(e.getKey(), e.getValue(), ScriptContext.GLOBAL_SCOPE);
                }

                if(this.outputStream!=null)
                {
                    l_bind.setAttribute("_out", this.outputStream, ScriptContext.GLOBAL_SCOPE);
                    l_bind.setAttribute("_sout", _sout = new PrintStream(this.outputStream){
                        @Override
                        public void close() {
                            this.flush();
                        }
                    }, ScriptContext.GLOBAL_SCOPE);
                }

                Object ret = this.script.eval(l_bind);

                if(this.outputStream!=null)
                {
                    IOUtil.close(_sout);
                }

                return ret;
            }
            else
            {
                Bindings l_bind = new SimpleBindings();
                PrintStream _sout = null;

                for(Map.Entry<String,Object> e : this.provide(_top).entrySet())
                {
                    l_bind.put(e.getKey(), e.getValue());
                }

                if(this.outputStream!=null)
                {
                    l_bind.put("_out", this.outputStream);
                    l_bind.put("_sout", _sout = new PrintStream(this.outputStream)
                    {
                        @Override
                        public void close() {
                            this.flush();
                        }
                    });
                }

                Object ret = this.script.eval(l_bind);

                if(this.outputStream!=null)
                {
                    IOUtil.close(_sout);
                }
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
