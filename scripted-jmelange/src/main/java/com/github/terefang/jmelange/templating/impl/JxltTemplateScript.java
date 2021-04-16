package com.github.terefang.jmelange.templating.impl;

import com.github.terefang.jmelange.templating.AbstractTemplateScript;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JxltEngine;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.logging.Log;
import org.codehaus.plexus.util.IOUtil;

import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class JxltTemplateScript extends AbstractTemplateScript
{
    static Log adaptLogger()
    {
        return new Log() {
            @Override
            public void debug(Object o) {
                log.debug(o.toString());
            }

            @Override
            public void debug(Object o, Throwable throwable) {
                log.debug(o.toString(), throwable);
            }

            @Override
            public void error(Object o) {
                log.error(o.toString());
            }

            @Override
            public void error(Object o, Throwable throwable) {
                log.error(o.toString(), throwable);
            }

            @Override
            public void fatal(Object o) {
                log.error(o.toString());
            }

            @Override
            public void fatal(Object o, Throwable throwable) {
                log.error(o.toString(), throwable);
            }

            @Override
            public void info(Object o) {
                log.info(o.toString());
            }

            @Override
            public void info(Object o, Throwable throwable) {
                log.info(o.toString(), throwable);
            }

            @Override
            public boolean isDebugEnabled() {
                return log.isDebugEnabled();
            }

            @Override
            public boolean isErrorEnabled() {
                return log.isErrorEnabled();
            }

            @Override
            public boolean isFatalEnabled() {
                return log.isErrorEnabled();
            }

            @Override
            public boolean isInfoEnabled() {
                return log.isInfoEnabled();
            }

            @Override
            public boolean isTraceEnabled() {
                return log.isTraceEnabled();
            }

            @Override
            public boolean isWarnEnabled() {
                return log.isWarnEnabled();
            }

            @Override
            public void trace(Object o) {
                log.trace(o.toString());
            }

            @Override
            public void trace(Object o, Throwable throwable) {
                log.trace(o.toString(), throwable);
            }

            @Override
            public void warn(Object o) {
                log.warn(o.toString());
            }

            @Override
            public void warn(Object o, Throwable throwable) {
                log.warn(o.toString(), throwable);
            }
        };
    }

    static JexlEngine engine = null;

    private JxltEngine.Template script;

    public static synchronized JexlEngine getEngine()
    {
        if(engine == null)
        {
            engine = new JexlBuilder().cache(512).charset(StandardCharsets.UTF_8).strict(true).silent(false).logger(adaptLogger()).create();
        }

        return engine;
    }
    @Override
    @SneakyThrows
    public boolean initTemplate(Reader _script)
    {
        JexlBuilder _builder = new JexlBuilder();
        _builder.charset(StandardCharsets.UTF_8);
        JexlEngine _jexl = _builder.create();

        JxltEngine _jxtl = _jexl.createJxltEngine();
        this.script = _jxtl.createTemplate(IOUtil.toString(_script));

        return (this.script != null);
    }

    @Override
    @SneakyThrows
    public boolean executeTemplate()
    {
        if(this.getOutputStream()==null) throw new IllegalArgumentException("missing outputstream");
        if(this.getErrorLogger()==null) this.setErrorLogger(this.log);

        OutputStreamWriter _writer = new OutputStreamWriter(this.getOutputStream());

        MapContext _jctx = new MapContext();
        for(Map.Entry<String, Object> _entry : this.assembleContext().entrySet())
        {
            _jctx.set(_entry.getKey(), _entry.getValue());
        }

        this.script.evaluate(_jctx, _writer);

        _writer.flush();
        return true;
    }
}
