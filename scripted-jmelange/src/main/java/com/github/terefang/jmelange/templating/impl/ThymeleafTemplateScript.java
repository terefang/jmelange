package com.github.terefang.jmelange.templating.impl;

import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.IOUtil;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.cache.NonCacheableCacheEntryValidity;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.thymeleaf.templateresource.ITemplateResource;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

@Slf4j
public class ThymeleafTemplateScript extends AbstractTemplateScript implements ITemplateResolver
{
    TemplateEngine engine = null;
    private String script;

    public synchronized TemplateEngine getEngine()
    {
        if(engine == null)
        {
            engine = new TemplateEngine();
            engine.setTemplateResolver(this);
        }
        return engine;
    }

    @Override
    @SneakyThrows
    public boolean initTemplate(Reader _script)
    {
        return ((this.script = IOUtil.toString(_script)) != null);
    }

    @Override
    @SneakyThrows
    public boolean executeTemplate()
    {
        if(this.getOutputStream()==null) throw new IllegalArgumentException("missing outputstream");
        if(this.getErrorLogger()==null) this.setErrorLogger(this.log);


        TemplateSpec _ts = new TemplateSpec("main", TemplateMode.valueOf(this.getOutputType().toUpperCase()));
        final Context _tc = new Context();
        _tc.setVariables(this.assembleContext());

        OutputStreamWriter _writer = new OutputStreamWriter(this.getOutputStream());
        this.getEngine().process(_ts, _tc, _writer);
        _writer.flush();

        return true;
    }

    @Override
    public String getName() {
        return this.getClass().toString();
    }

    @Override
    public Integer getOrder() {
        return null;
    }

    @Override
    public TemplateResolution resolveTemplate(IEngineConfiguration _iEngineConfiguration, String _ownerTemplate, String _template, Map<String, Object> _map)
    {
        if(!"main".equalsIgnoreCase(_template)) return null;

        ITemplateResource templateResource = new ITemplateResource(){
            @Override
            public String getDescription() {
                return ThymeleafTemplateScript.this.getScriptFile()!=null ? ThymeleafTemplateScript.this.getScriptFile().getAbsolutePath() : "null";
            }

            @Override
            public String getBaseName() {
                return ThymeleafTemplateScript.this.getScriptFile()!=null ? ThymeleafTemplateScript.this.getScriptFile().getName() : "null";
            }

            @Override
            public boolean exists() {
                return true;
            }

            @Override
            public Reader reader() throws IOException {
                return new StringReader(ThymeleafTemplateScript.this.script);
            }

            @Override
            public ITemplateResource relative(String s) {
                return null;
            }
        };

        return new TemplateResolution(templateResource, TemplateMode.valueOf(this.getOutputType().toUpperCase()), NonCacheableCacheEntryValidity.INSTANCE);
    }
}
