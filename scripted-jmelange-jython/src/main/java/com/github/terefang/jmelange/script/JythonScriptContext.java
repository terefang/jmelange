package com.github.terefang.jmelange.script;

import com.github.terefang.jmelange.commons.CommonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

@Slf4j
public class JythonScriptContext extends AbstractScript implements IScriptContext
{
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

    PythonInterpreter engine;
    private PyCode script;

    OutputStream outputStream;
    @Override
    public void setOutputStream(OutputStream _out) {
        this.outputStream = _out;
    }

    public static IScriptContext create(List<String> _path)
    {
        JythonScriptContext _p = new JythonScriptContext();
        _p.setIncludes(_path);
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
        try
        {
            this.engine = new PythonInterpreter();
            this.engine.exec("import sys\n");
            this.engine.exec("import os \n");
            if(this.getIncludes()!=null)
            {
                for(String _d : this.getIncludes())
                {
                    this.engine.exec("sys.path.append('" + _d + "')\n");
                }
            }
            this.script = this.engine.compile(_reader);
        }
        catch (Exception e)
        {
            log.warn("error", e);
        }
        finally
        {
            CommonUtil.close(_reader);
        }
    }

    @Override
    public Object run(Map<String, Object> _top, boolean _useLocal)
    {
        PrintStream _sout = null;
        try
        {
            this.engine.set("_result", new PyBoolean(false));
            for(Map.Entry<String,Object> e : this.provide(_top).entrySet())
            {
                this.engine.set(e.getKey(), toPyObject(e.getValue()));
            }

            if(this.outputStream!=null)
            {
                this.engine.set("_out", toPyObject(this.outputStream));
                this.engine.set("_sout", toPyObject(_sout = new PrintStream(this.outputStream) {
                    @Override
                    public void close() {
                        this.flush();
                    }
                }));
            }

            this.engine.exec(this.script);

            PyObject _res = this.engine.get("_result");

            if(this.outputStream!=null)
            {
                CommonUtil.close(_sout);
            }

            if(_res!=null)
            {
                return toJavaObject(_res);
            }
        }
        catch(Exception xe)
        {
            log.warn("error in file ", xe);
        }

        return null;
    }

    PyObject toPyObject(Object _o)
    {
        if(_o instanceof Boolean)
        {
            return new PyBoolean((Boolean) _o);
        }
        else
        if(_o instanceof Float)
        {
            return new PyFloat((Float)_o);
        }
        else
        if(_o instanceof Double)
        {
            return new PyFloat((Double)_o);
        }
        else
        if(_o instanceof Long)
        {
            return new PyLong((Long)_o);
        }
        else
        if(_o instanceof Integer)
        {
            return new PyInteger((Integer)_o);
        }
        else
        if(_o instanceof String)
        {
            return new PyString(_o.toString());
        }
        else
        if(_o instanceof Map)
        {
            PyDictionary _list = new PyDictionary();
            for(Object _s : ((Map)_o).keySet())
            {
                _list.put(toPyObject(_s), toPyObject(((Map)_o).get(_s)));
            }
            return _list;
        }
        else
        if(_o instanceof List)
        {
            PyList _list = new PyList();
            for(Object _s : (List)_o)
            {
                _list.append(toPyObject(_s));
            }
            return _list;
        }
        return null;
    }

    Object toJavaObject(PyObject _o)
    {
        if(_o instanceof PyBoolean)
        {
            return ((PyBoolean) _o).getBooleanValue();
        }
        else
        if(_o instanceof PyFloat)
        {
            return ((PyFloat)_o).getValue();
        }
        else
        if(_o instanceof PyLong)
        {
            return ((PyLong)_o).getValue().longValue();
        }
        else
        if(_o instanceof PyInteger)
        {
            return ((PyInteger)_o).getValue();
        }
        else
        if(_o instanceof PyString)
        {
            return ((PyString)_o).getString();
        }
        else
        if(_o instanceof Map)
        {
            PyDictionary _list = new PyDictionary();
            for(Object _s : ((Map)_o).keySet())
            {
                _list.put(toPyObject(_s), toPyObject(((Map)_o).get(_s)));
            }
            return _list;
        }
        else
        if(_o instanceof List)
        {
            PyList _list = new PyList();
            for(Object _s : (List)_o)
            {
                _list.append(toPyObject(_s));
            }
            return _list;
        }
        return null;
    }
}
