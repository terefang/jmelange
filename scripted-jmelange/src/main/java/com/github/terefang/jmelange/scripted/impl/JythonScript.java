package com.github.terefang.jmelange.scripted.impl;


import com.github.terefang.jmelange.commons.util.IOUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import lombok.extern.slf4j.Slf4j;
import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.Reader;
import java.util.List;
import java.util.Map;

@Slf4j
public class JythonScript extends AbstractScript
{
    PythonInterpreter engine;
    private PyCode script;

    public static AbstractScript create() {
        return new JythonScript();
    }

    @Override
    public boolean init(Reader _script) {
        try
        {
            this.engine = new PythonInterpreter();
            this.engine.exec("import sys\n");
            this.engine.exec("import os \n");
            if(this.getIncludeDirectories()!=null)
            {
                for(File _d : this.getIncludeDirectories())
                {
                    this.engine.exec("sys.path.append('" + _d.getAbsolutePath() + "')\n");
                }
            }
            this.script = this.engine.compile(_script);
            return (this.script != null);
        }
        catch (Exception e)
        {
            log.warn("error", e);
        }
        finally
        {
            IOUtil.close(_script);
        }
        return false;
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

    @Override
    public Object executeObject(boolean _scopeOrBinding)
    {
        try
        {
            this.engine.set("_result", new PyBoolean(false));
            for(Map.Entry<String,Object> e : this.assembleContext().entrySet())
            {
                this.engine.set(e.getKey(), toPyObject(e.getValue()));
            }

            this.engine.exec(this.script);

            PyObject _res = this.engine.get("_result");
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
}
