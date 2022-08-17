package com.github.terefang.jmelange.scripted.impl;

import com.github.terefang.jmelange.scripted.AbstractScript;
import groovy.lang.Binding;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceConnector;
import groovy.util.ResourceException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringInputStream;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class GroovyScript extends AbstractScript implements ResourceConnector
{
    public static final String INTERNAL_SCRIPT_NAME = "SCRIPT";

    private GroovyScriptEngine scriptEngine;
    private Script script;
    private StringWriter _sw;

    File _parentDir;

    public static GroovyScript create() {
        GroovyScript _g = new GroovyScript();
        _g.scriptEngine = new GroovyScriptEngine(_g);
        return _g;
    }


    @Override
    public boolean init(File _script, List<File> _inc)
    {
        this._parentDir = _script.getParentFile();
        return super.init(_script, _inc);
    }

    @Override
    public boolean init(File _script)
    {
        this._parentDir = _script.getParentFile();
        return super.init(_script);
    }

    @Override
    @SneakyThrows
    public boolean init(Reader _script)
    {
        this._sw = new StringWriter();
        IOUtil.copy(_script, _sw);
        this.script = this.scriptEngine.createScript(INTERNAL_SCRIPT_NAME, new Binding(assembleContext()));
        return this.script != null;
    }

    @Override
    public Map<String, Object> assembleContext() {
        Map<String, Object> _context = super.assembleContext();

        if(this.getArgs()!=null)
        {
            String[] _args = this.getArgs().toArray(new String[this.getArgs().size()]);
            _context.put("args", _args);
        }

        return _context;
    }

    @Override
    public Object executeObject(boolean _scopeOrBinding)
    {
        Binding _bind = new Binding(assembleContext());
        this.script.setBinding(_bind);
        return this.script.run();
    }

    @Override
    @SneakyThrows
    public URLConnection getResourceConnection(String _rs) throws ResourceException
    {
        if(INTERNAL_SCRIPT_NAME.equalsIgnoreCase(_rs))
        {
            return new URLConnection(new URL("file://"+INTERNAL_SCRIPT_NAME)){
                @Override
                public void connect() throws IOException {

                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return new StringInputStream(_sw.getBuffer().toString());
                }
            };
        }
        else
        if(_rs.endsWith(".groovy") && new File(this._parentDir,_rs).exists())
        {
            return new File(this._parentDir,_rs).toURL().openConnection();
        }
        else
        if(_rs.endsWith(".groovy"))
        {
            for(File _dir : this.getIncludeDirectories())
            {
                File _res = new File(_dir, _rs);
                if(_res.exists())
                {
                    return _res.toURL().openConnection();
                }
            }
        }
        throw new ResourceException(_rs);
    }

    /*
    public static void main(String[] args) {
        GroovyScript _s = GroovyScript.create();
        HashMap<String, Object> _v = new HashMap<>();
        _v.put("_log", log);
        _s.setVars(_v);
        _s.init(new File(".../debug.gy"));
        System.err.println(_s.execute());
    }
     */
}
