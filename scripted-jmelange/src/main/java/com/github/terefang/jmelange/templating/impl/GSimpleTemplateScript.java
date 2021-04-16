package com.github.terefang.jmelange.templating.impl;

import com.github.terefang.jmelange.templating.AbstractTemplateScript;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.IOUtil;

import java.io.OutputStreamWriter;
import java.io.Reader;

@Slf4j
public class GSimpleTemplateScript extends AbstractTemplateScript
{
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
    public boolean initTemplate(Reader _script)
    {
        this.script = getEngine().createTemplate(IOUtil.toString(_script));
        return (this.script != null);
    }

    @Override
    @SneakyThrows
    public boolean executeTemplate()
    {
        if(this.getOutputStream()==null) throw new IllegalArgumentException("missing outputstream");
        if(this.getErrorLogger()==null) this.setErrorLogger(this.log);

        OutputStreamWriter _writer = new OutputStreamWriter(this.getOutputStream());

        this.script.make(this.assembleContext()).writeTo(_writer);
        _writer.flush();
        return true;
    }
}
