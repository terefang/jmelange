package com.github.terefang.jmelange.script;

import com.github.terefang.jmelange.commons.CommonUtil;
import lombok.SneakyThrows;
import org.trimou.Mustache;
import org.trimou.engine.MustacheEngine;
import org.trimou.engine.MustacheEngineBuilder;

import java.io.*;
import java.util.List;
import java.util.Map;

public class TrimouTemplateImpl extends AbstractTemplate implements ITemplateContext
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

    private List<String> include;

    @SneakyThrows
    public static ITemplateContext create(String _name, String _ext, List<String> _path)
    {
        TrimouTemplateImpl _tl = new TrimouTemplateImpl();
        _tl.include = _path;
        return _tl;
    }

    public TrimouTemplateImpl()
    {
        this.setVp(BasicVariableProvider.create());
    }

    @Override
    @SneakyThrows
    public void compile(Reader _reader, String _name)
    {
        this.script = this.getEngine().compileMustache(_name, CommonUtil.toString(_reader));
    }

    @Override
    @SneakyThrows
    public Object run(Map<String, Object> _top, boolean _useLocal, OutputStream _out)
    {
        OutputStreamWriter _writer = new OutputStreamWriter(_out);
        this.script.render(_writer, this.provide(_top));
        _writer.flush();
        return true;
    }

}
