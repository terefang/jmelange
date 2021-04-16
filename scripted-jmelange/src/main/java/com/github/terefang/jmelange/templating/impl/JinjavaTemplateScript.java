package com.github.terefang.jmelange.templating.impl;

import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import com.hubspot.jinjava.interpret.RenderResult;
import com.hubspot.jinjava.interpret.TemplateError;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.IOUtil;

import java.io.OutputStreamWriter;
import java.io.Reader;
import java.text.MessageFormat;

@Slf4j
public class JinjavaTemplateScript extends AbstractTemplateScript
{
    static Jinjava engine = null;
    private String script;

    public static synchronized Jinjava getEngine()
    {
        if(engine == null)
        {
            engine = new Jinjava();
        }

        return engine;
    }
    @Override
    @SneakyThrows
    public boolean initTemplate(Reader _script)
    {
        this.script = IOUtil.toString(_script);
        return (this.script != null);
    }

    @Override
    @SneakyThrows
    public boolean executeTemplate()
    {
        if(this.getOutputStream()==null) throw new IllegalArgumentException("missing outputstream");
        if(this.getErrorLogger()==null) this.setErrorLogger(this.log);

        JinjavaConfig _config = JinjavaConfig.newBuilder().build();
        OutputStreamWriter _writer = new OutputStreamWriter(this.getOutputStream());
        RenderResult _result = this.getEngine().renderForResult(this.script, this.assembleContext(), _config);
        for(TemplateError _error : _result.getErrors())
        {
            log.error(MessageFormat.format("L={0}:C={1} {2}: {3}", _error.getLineno(), _error.getStartPosition(), _error.getSeverity().toString(), _error.getMessage()));
        }
        _writer.write(_result.getOutput());
        _writer.flush();
        return true;
    }
}
