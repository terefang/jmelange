package com.github.terefang.jmelange.templating.impl;

import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import io.marioslab.basis.template.Template;
import io.marioslab.basis.template.TemplateContext;
import io.marioslab.basis.template.TemplateLoader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.IOUtil;


import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class BasicTemplateScript extends AbstractTemplateScript
{
    private Template script;
    static TemplateLoader.MapTemplateLoader loader = new TemplateLoader.MapTemplateLoader();

    @Override
    @SneakyThrows
    public boolean initTemplate(Reader _script)
    {
        String _id = UUID.randomUUID().toString();
        if(this.getScriptFile()!=null)
        {
            _id = this.getScriptFile().getAbsolutePath();
        }
        loader.set(_id, IOUtil.toString(_script));
        this.script = loader.load(_id);
        return (this.script != null);
    }

    @Override
    @SneakyThrows
    public boolean executeTemplate()
    {
        if(this.getOutputStream()==null) throw new IllegalArgumentException("missing outputstream");
        if(this.getErrorLogger()==null) this.setErrorLogger(this.log);

        TemplateContext _context = new TemplateContext();

        for(Map.Entry<String, Object> _entry : this.assembleContext().entrySet())
        {
            _context.set(_entry.getKey(), _entry.getValue());
        }

        OutputStream _out = this.getOutputStream();
        this.script.render(_context, _out);
        _out.flush();
        return true;
    }
}
