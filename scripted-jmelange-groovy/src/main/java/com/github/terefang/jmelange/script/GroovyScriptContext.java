package com.github.terefang.jmelange.script;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.CfgDataUtil;
import groovy.lang.Binding;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceConnector;
import groovy.util.ResourceException;
import lombok.SneakyThrows;
import com.github.terefang.jmelange.plexus.util.IOUtil;
import com.github.terefang.jmelange.plexus.util.StringInputStream;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GroovyScriptContext extends AbstractScript implements IScriptContext, ResourceConnector
{
    public static final String INTERNAL_SCRIPT_NAME = "C2C095C3-899B-4CA2-A6E2-3E3849951841";

    File file;

    @Override
    public File getFile()
    {
        return file;
    }

    @Override
    public File getScriptFile()
    {
        return file;
    }

    @Override
    public void setFile(File file)
    {
        this.file = file;
    }

    private GroovyScriptEngine scriptEngine;
    private Script script;
    private StringWriter _sw;

    OutputStream outputStream;
    @Override
    public void setOutputStream(OutputStream _out) {
        this.outputStream = _out;
    }

    public static IScriptContext create(List<String> _path)
    {
        GroovyScriptContext _p = new GroovyScriptContext();
        _p.setIncludes(_path);;
        _p.scriptEngine = new GroovyScriptEngine(_p);
        return _p;
    }

    @Override
    public Object execute(File _file) {
        this.setFile(_file);
        return super.execute(_file);
    }

    @Override
    public Object execute(File _file, boolean _useLocal) {
        this.setFile(_file);
        return super.execute(_file, _useLocal);
    }

    @Override
    public void compile(File _file) {
        this.setFile(_file);
        super.compile(_file);
    }

    @Override
    @SneakyThrows
    public void compile(Reader _reader, String _name)
    {
        this._sw = new StringWriter();
        IOUtil.copy(_reader, _sw);
        this.script = this.scriptEngine.createScript(this.getFile()==null ? INTERNAL_SCRIPT_NAME : this.getFile().getAbsolutePath(), new Binding(this.provide(Collections.emptyMap())));
    }

    @Override
    public Object run(Map<String, Object> _top, boolean _useLocal)
    {
        Binding _bind = new Binding(this.provide(_top));
        if(this.outputStream!=null)
        {
            _bind.setVariable("out", this.outputStream);
        }
        this.script.setBinding(_bind);
        return this.script.run();
    }



    @Override
    @SneakyThrows
    public URLConnection getResourceConnection(String _rs) throws ResourceException
    {
        File _file = Paths.get(_rs).toFile();
        if(_file.exists())
        {
            return _file.toURL().openConnection();
        }
        else
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
        if(_rs.endsWith(".groovy") && this.getIncludes()!=null && this.getIncludes().size()>0)
        {
            for(String _dir : this.getIncludes())
            {
                File _res = new File(_dir, _rs);
                if(_res.exists())
                {
                    return _res.toURL().openConnection();
                }
            }
        }
        
        if(_rs.endsWith(".groovy"))
        {
            final URL _res = GroovyScriptContext.class.getResource("/groovylib/"+_rs);
            if(_res!=null)
            {
                return _res.openConnection();
                /*
                
                CfgDataUtil.setCacheDataAsString(_rs, CommonUtil.toString(_res));
                return CfgDataUtil.getCacheFile(_rs).toURL().openConnection();
                
                
                return new URLConnection(new URL("file://"+_rs)){
                    @Override
                    public void connect() throws IOException {
                    
                    }
                    
                    @Override
                    public InputStream getInputStream() throws IOException {
                        return _res.openStream();
                    }
                }
                */
            }
        }
        
        throw new ResourceException(_rs);
    }

}
