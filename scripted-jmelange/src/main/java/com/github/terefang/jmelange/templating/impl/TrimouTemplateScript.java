package com.github.terefang.jmelange.templating.impl;

import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.codehaus.plexus.util.IOUtil;
import org.trimou.Mustache;
import org.trimou.engine.MustacheEngine;
import org.trimou.engine.MustacheEngineBuilder;

import java.io.OutputStreamWriter;
import java.io.Reader;

@Slf4j
public class TrimouTemplateScript extends AbstractTemplateScript
{
    static MustacheEngine engine = null;
    private Mustache script;

    public static synchronized MustacheEngine getEngine()
    {
        if(engine == null)
        {
            engine = MustacheEngineBuilder
                    .newBuilder()
                    .build();
        }

        return engine;
    }

    @Override
    @SneakyThrows
    public boolean initTemplate(Reader _script)
    {
        if(this.getScriptFile()!=null)
        {
            this.script = this.getEngine().compileMustache(this.getScriptFile().getName(), IOUtil.toString(_script));
        }
        else
        {
            this.script = this.getEngine().compileMustache(IOUtil.toString(_script));
        }
        return (this.script != null);
    }

    @Override
    @SneakyThrows
    public boolean executeTemplate()
    {
        if(this.getOutputStream()==null) throw new IllegalArgumentException("missing outputstream");
        if(this.getErrorLogger()==null) this.setErrorLogger(this.log);
        OutputStreamWriter _writer = new OutputStreamWriter(this.getOutputStream());
        this.script.render(_writer, this.assembleContext());
        _writer.flush();
        return true;
    }
}
