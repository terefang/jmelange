package com.github.terefang.jmelange.script.esp;

import com.github.terefang.jmelange.commons.util.IOUtil;
import com.github.terefang.jmelange.script.AbstractTemplate;
import com.github.terefang.jmelange.script.IScriptContext;
import com.github.terefang.jmelange.script.ITemplateContext;
import lombok.SneakyThrows;

import java.io.OutputStream;
import java.io.Reader;
import java.util.Map;

public class EspTemplateImpl extends AbstractTemplate implements ITemplateContext
{
    EspDeTagifier detag = new EspDeTagifier();
    IScriptContext script = null;

    public void setScript(IScriptContext script) {
        this.script = script;
    }

    public void setDeTagifier(EspDeTagifier dt)
    {
        this.detag = dt;
    }

    public EspDeTagifier getDeTagifier()
    {
        return this.detag;
    }

    @Override
    @SneakyThrows
    public void compile(Reader _reader, String _name) {
        String _esp = this.detag.parse(_reader);
        this.script.compile(_esp, _name);
    }

    @Override
    public Object run(Map<String, Object> _top, boolean _useLocal, OutputStream _out)
    {
        try {
            this.script.setOutputStream(_out);
            return this.script.run(this.provide(_top), _useLocal);
        }
        finally
        {
            this.script.setOutputStream(null);
            IOUtil.flush(_out);;
            IOUtil.close(_out);
        }
    }

    @Override
    public void setAll(Map<String, Object> _vars) {
        this.script.setAll(_vars);
    }

    @Override
    public void set(String _key, Object _val) {
        this.script.set(_key, _val);
    }

    @Override
    public void unset(String _key) {
        this.script.unset(_key);
    }

    @Override
    public void unsetAll() {
        this.script.unsetAll();
    }

}
