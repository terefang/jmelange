package com.github.terefang.jmelange.template.jinjava;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.IOUtil;
import com.github.terefang.jmelange.script.AbstractTemplate;
import com.github.terefang.jmelange.script.BasicVariableProvider;
import com.github.terefang.jmelange.script.ITemplateContext;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import com.hubspot.jinjava.interpret.RenderResult;
import com.hubspot.jinjava.loader.CascadingResourceLocator;
import com.hubspot.jinjava.loader.FileLocator;
import com.hubspot.jinjava.loader.ResourceLocator;
import lombok.SneakyThrows;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public class JinjavaTemplateImpl extends AbstractTemplate implements ITemplateContext
{
    static Jinjava engine = null;
    private String script;
    private ResourceLocator locator;

    public static synchronized Jinjava getEngine()
    {
        if(engine == null)
        {
            engine = new Jinjava();
        }

        return engine;
    }

    @SneakyThrows
    public static ITemplateContext create(String _name, String _ext, List<String> _path) {
        JinjavaTemplateImpl _bt = new JinjavaTemplateImpl();
        if(_path!=null && _path.size()>0)
        {
            ResourceLocator[] _rl = new ResourceLocator[_path.size()];
            for(int _i=0; _i<_path.size(); _i++)
            {
                _rl[_i] = new FileLocator(new File(_path.get(_i)));
            }
            _bt.locator = new CascadingResourceLocator(_rl);
        }
        return _bt;
    }

    public JinjavaTemplateImpl()
    {
        this.setVp(BasicVariableProvider.create());
    }

    @Override
    @SneakyThrows
    public void compile(Reader _reader, String _name)
    {
        this.script = CommonUtil.toString(_reader);
    }

    @Override
    @SneakyThrows
    public Object run(Map<String, Object> _top, boolean _useLocal, OutputStream _out)
    {
        JinjavaConfig _config = JinjavaConfig.newBuilder().build();
        OutputStreamWriter _writer = new OutputStreamWriter(_out);
        Jinjava _engine = getEngine();
        synchronized (_engine)
        {
            ResourceLocator _trl = _engine.getResourceLocator();
            if(this.locator!=null)
            {
                _engine.setResourceLocator(this.locator);
            }
            RenderResult _result = this.getEngine().renderForResult(this.script, this.provide(_top), _config);
            _writer.write(_result.getOutput());
            _engine.setResourceLocator(_trl);
        }
        IOUtil.flush(_writer);
        return true;
    }

}
