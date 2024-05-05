package com.github.terefang.jmelange.script;

import com.github.terefang.jmelange.commons.util.IOUtil;

import lombok.SneakyThrows;
import org.codehaus.plexus.util.StringUtils;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.cache.NonCacheableCacheEntryValidity;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.thymeleaf.templateresource.ITemplateResource;

import java.io.*;
import java.util.List;
import java.util.Map;

public class ThymeleafTemplateImpl extends AbstractTemplate implements ITemplateContext, ITemplateResolver
{
    TemplateEngine engine = null;
    private String script;
    private List<String> include;

    public synchronized TemplateEngine getEngine()
    {
        if(engine == null)
        {
            engine = new TemplateEngine();
            engine.setTemplateResolver(this);
        }
        return engine;
    }

    @SneakyThrows
    public static ITemplateContext create(String _name, String _ext, List<String> _path)
    {
        ThymeleafTemplateImpl _tl = new ThymeleafTemplateImpl();
        _tl.include = _path;

        return _tl;
    }

    public ThymeleafTemplateImpl()
    {
        this.setVp(BasicVariableProvider.create());
    }

    @Override
    @SneakyThrows
    public void compile(Reader _reader, String _name)
    {
        this.script = IOUtil.toString(_reader);
    }

    @Override
    @SneakyThrows
    public Object run(Map<String, Object> _top, boolean _useLocal, OutputStream _out)
    {
        TemplateSpec _ts = new TemplateSpec("main", TemplateMode.valueOf(this.getOutputType().toUpperCase()));
        final Context _tc = new Context();
        _tc.setVariables(this.provide(_top));

        OutputStreamWriter _writer = new OutputStreamWriter(_out);
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
        ITemplateResource templateResource = null;
        if(!"main".equalsIgnoreCase(_template))
        {
            if(ThymeleafTemplateImpl.this.include!=null && ThymeleafTemplateImpl.this.include.size()>0)
            {
                for(int _i = 0; _i<ThymeleafTemplateImpl.this.include.size(); _i++)
                {
                    File _file = new File(ThymeleafTemplateImpl.this.include.get(_i),_template);
                    if(_file.exists())
                    {
                        templateResource = new ITemplateResource(){
                            @Override
                            public String getDescription() {
                                return _file.getAbsolutePath();
                            }

                            @Override
                            public String getBaseName() {
                                return _file.getName();
                            }

                            @Override
                            public boolean exists() {
                                return true;
                            }

                            @Override
                            public Reader reader() throws IOException {
                                return new BufferedReader(new FileReader(_file), 8192);
                            }

                            @Override
                            public ITemplateResource relative(String s) {
                                return null;
                            }
                        };
                        break;
                    }
                }
            }

        }
        else
        {
            templateResource = new ITemplateResource(){
                @Override
                public String getDescription() {
                    return ThymeleafTemplateImpl.this.getFile()!=null ? ThymeleafTemplateImpl.this.getFile().getAbsolutePath() : "null";
                }

                @Override
                public String getBaseName() {
                    return ThymeleafTemplateImpl.this.getFile()!=null ? ThymeleafTemplateImpl.this.getFile().getName() : "null";
                }

                @Override
                public boolean exists() {
                    return true;
                }

                @Override
                public Reader reader() throws IOException {
                    return new StringReader(ThymeleafTemplateImpl.this.script);
                }

                @Override
                public ITemplateResource relative(String s) {
                    return null;
                }
            };
        }

        if(templateResource==null) return null;

        return new TemplateResolution(templateResource, TemplateMode.valueOf(this.getOutputType().toUpperCase()), NonCacheableCacheEntryValidity.INSTANCE);
    }

}
