package com.github.terefang.jmelange.script;

import lombok.SneakyThrows;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface IScriptContext extends IContext
{
    @SneakyThrows
    default public Object execute(File _file)
    {
        this.setFile(_file);
        return execute(_file, false);
    }
    @SneakyThrows
    default public Object execute(InputStream _stream, String _name)
    {
        return execute(_stream, _name, false);
    }
    @SneakyThrows
    default public Object execute(String _script, String _name)
    {
        return execute(_script, _name, false);
    }
    default public Object execute(Reader _reader, String _name)
    {
        return execute(_reader, _name, false);
    }

    @SneakyThrows
    default public Object execute(File _file, boolean _useLocal)
    {
        this.setFile(_file);
        return execute(new FileReader(_file), _file.getName(), _useLocal);
    }
    @SneakyThrows
    default public Object execute(InputStream _stream, String _name, boolean _useLocal)
    {
        return execute(new InputStreamReader(_stream), _name, _useLocal);
    }
    @SneakyThrows
    default public Object execute(String _script, String _name, boolean _useLocal)
    {
        return execute(new StringReader(_script), _name, _useLocal);
    }
    @SneakyThrows
    default public Object execute(Reader _reader, String _name, boolean _useLocal)
    {
        compile(_reader,_name);
        return run(Collections.emptyMap(), _useLocal);
    }

    @SneakyThrows
    default public void compile(File _file)
    {
        this.setFile(_file);
        this.compile(new FileReader(_file), _file.getName());
    }
    @SneakyThrows
    default public void compile(InputStream _stream, String _name)
    {
        compile(new InputStreamReader(_stream), _name);
    }
    @SneakyThrows
    default public void compile(String _script, String _name)
    {
        compile(new StringReader(_script), _name);
    }
    public void compile(Reader _reader, String _name);

    @SneakyThrows
    default public Object run()
    {
        return run(Collections.emptyMap(), false);
    }
    @SneakyThrows
    default public Object run(Map<String,Object> _top)
    {
        return run(_top, false);
    }
    @SneakyThrows
    default public Object run(boolean _useLocal)
    {
        return run(Collections.emptyMap(), _useLocal);
    }
    public Object run(Map<String,Object> _top, boolean _useLocal);

    void setOutputStream(OutputStream out);
    void setIncludes(List<String> _incl);
}
