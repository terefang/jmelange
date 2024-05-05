package com.github.terefang.jmelange.template.fmark;

import com.github.terefang.jmelange.commons.util.IOUtil;
import com.github.terefang.jmelange.script.AbstractTemplate;
import com.github.terefang.jmelange.script.BasicVariableProvider;
import com.github.terefang.jmelange.script.ITemplateContext;
import freemarker.cache.*;
import freemarker.core.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;
import org.codehaus.plexus.util.StringUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

public class FreeMarkerTemplateImpl extends AbstractTemplate implements ITemplateContext
{
    private Template script;
    private TemplateLoader loader;

    @SneakyThrows
    public static ITemplateContext create(String _name, String _ext, List<String> _path)
    {
        FreeMarkerTemplateImpl _ftl = new FreeMarkerTemplateImpl();

        if(".fhtml".equalsIgnoreCase(_ext) || StringUtils.contains(_name,"html"))
        {
            _ftl.setOutputType("html");
        }
        else
        if(".fxml".equalsIgnoreCase(_ext) || StringUtils.contains(_name,"xml"))
        {
            _ftl.setOutputType("xml");
        }
        else
        if(".fjs".equalsIgnoreCase(_ext) || StringUtils.contains(_name,"ecma"))
        {
            _ftl.setOutputType("ecma");
        }
        else
        if(".ftxt".equalsIgnoreCase(_ext) || StringUtils.contains(_name,"txt"))
        {
            _ftl.setOutputType("text");
        }

        if(_path!=null && _path.size()>0)
        {
            TemplateLoader[] _tl = new TemplateLoader[_path.size()];
            for(int _i = 0; _i<_tl.length; _i++)
            {
                _tl[_i] = new FileTemplateLoader(new File(_path.get(_i)));
            }
            _ftl.loader = new MultiTemplateLoader(_tl);
        }

        return _ftl;
    }

    public FreeMarkerTemplateImpl()
    {
        this.setVp(BasicVariableProvider.create());
    }

    @Override
    @SneakyThrows
    public void compile(Reader _reader, String _name)
    {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(true);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);

        if(this.loader!=null)
        {
            cfg.setTemplateLoader(this.loader);
        }

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

        if(this.getFile()!=null)
        {
            IOUtil.closeQuietly(_reader);
            cfg.setDirectoryForTemplateLoading(this.getFile().getParentFile());
            this.script = cfg.getTemplate(this.getFile().getName());
        }
        else
        {
            this.script =  new Template("main", _reader, cfg);
        }
    }

    @Override
    @SneakyThrows
    public Object run(Map<String, Object> _top, boolean _useLocal, OutputStream _out)
    {
        OutputStreamWriter _writer = new OutputStreamWriter(_out);
        this.script.process(this.provide(_top), _writer);
        IOUtil.flush(_writer);
        return true;
    }

}
