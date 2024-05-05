package com.github.terefang.jmelange.script;

import com.github.terefang.jmelange.commons.util.IOUtil;
import io.marioslab.basis.template.Template;
import io.marioslab.basis.template.TemplateContext;
import io.marioslab.basis.template.TemplateLoader;
import lombok.SneakyThrows;

import java.io.OutputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public class BasicTemplateImpl extends AbstractTemplate implements ITemplateContext
{
    private Template script;
    static TemplateLoader.MapTemplateLoader loader = new TemplateLoader.MapTemplateLoader();

    public static ITemplateContext create(String _name, String _ext, List<String> _path) {
        BasicTemplateImpl _bt = new BasicTemplateImpl();
        return _bt;
    }

    public BasicTemplateImpl()
    {
        this.setVp(BasicVariableProvider.create());
    }

    @Override
    @SneakyThrows
    public void compile(Reader _reader, String _name)
    {
        loader.set(_name, IOUtil.toString(_reader));
        this.script = loader.load(_name);
    }

    @Override
    public Object run(Map<String, Object> _top, boolean _useLocal, OutputStream _out)
    {
        TemplateContext _context = new TemplateContext();

        for(Map.Entry<String, Object> _entry : this.provide(_top).entrySet())
        {
            _context.set(_entry.getKey(), _entry.getValue());
        }

        this.script.render(_context, _out);
        IOUtil.flush(_out);
        return true;
    }

}
