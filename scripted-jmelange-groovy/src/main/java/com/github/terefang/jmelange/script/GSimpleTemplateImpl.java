package com.github.terefang.jmelange.script;

import com.github.terefang.jmelange.commons.util.IOUtil;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Map;

public class GSimpleTemplateImpl extends AbstractTemplate implements ITemplateContext
{
    public static ITemplateContext create() {
        return new GSimpleTemplateImpl();
    }

    public GSimpleTemplateImpl()
    {
        this.setVp(BasicVariableProvider.create());
    }

    static SimpleTemplateEngine engine = null;
    private Template script;

    public static synchronized SimpleTemplateEngine getEngine()
    {
        if(engine == null)
        {
            engine = new SimpleTemplateEngine();
        }

        return engine;
    }

    @Override
    @SneakyThrows
    public void compile(Reader _reader, String _name)
    {
        this.script = getEngine().createTemplate(_reader);
    }

    @Override
    @SneakyThrows
    public Object run(Map<String, Object> _top, boolean _useLocal, OutputStream _out)
    {
        OutputStreamWriter _writer = new OutputStreamWriter(_out) {
            @Override
            public void close() throws IOException {
                this.flush();
                super.close();
            }
        };

        this.script.make(this.provide(_top)).writeTo(_writer);
        IOUtil.flush(_writer);
        return true;
    }

}
