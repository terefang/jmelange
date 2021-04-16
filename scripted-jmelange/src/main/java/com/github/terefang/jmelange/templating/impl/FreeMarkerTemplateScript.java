package com.github.terefang.jmelange.templating.impl;

import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import freemarker.cache.ConditionalTemplateConfigurationFactory;
import freemarker.cache.FileNameGlobMatcher;
import freemarker.core.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


import java.io.OutputStreamWriter;
import java.io.Reader;

@Slf4j
public class FreeMarkerTemplateScript extends AbstractTemplateScript
{
    private Template script;

    @Override
    @SneakyThrows
    public boolean initTemplate(Reader _script)
    {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(true);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);

        TemplateConfiguration _tcfg = new TemplateConfiguration();
        _tcfg.setEncoding("UTF-8");
        _tcfg.setWhitespaceStripping(true);
        cfg.setTemplateConfigurations(new ConditionalTemplateConfigurationFactory(new FileNameGlobMatcher("*"), _tcfg));

        if("text".equalsIgnoreCase(this.getOutputType()))
        {
            _tcfg.setOutputFormat(PlainTextOutputFormat.INSTANCE);
        }
        else
        if("xml".equalsIgnoreCase(this.getOutputType()))
        {
            _tcfg.setOutputFormat(XMLOutputFormat.INSTANCE);
        }
        else
        if("html".equalsIgnoreCase(this.getOutputType()))
        {
            _tcfg.setOutputFormat(HTMLOutputFormat.INSTANCE);
        }
        else
        if("javascript".equalsIgnoreCase(this.getOutputType())
                || "js".equalsIgnoreCase(this.getOutputType())
                || "ecmascript".equalsIgnoreCase(this.getOutputType())
                || "ecma".equalsIgnoreCase(this.getOutputType()))
        {
            _tcfg.setOutputFormat(JavaScriptOutputFormat.INSTANCE);
        }

        if(this.getScriptFile()!=null)
        {
            cfg.setDirectoryForTemplateLoading(this.getScriptFile().getParentFile());
            this.script = cfg.getTemplate(this.getScriptFile().getName());
        }
        else
        {
            this.script =  new Template("main", _script, cfg);
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
        this.script.process(this.assembleContext(), _writer);
        _writer.flush();
        return true;
    }
}
