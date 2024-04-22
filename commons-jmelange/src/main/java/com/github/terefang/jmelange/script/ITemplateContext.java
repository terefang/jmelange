package com.github.terefang.jmelange.script;

import lombok.SneakyThrows;

import java.io.*;
import java.util.Collections;
import java.util.Map;

public interface ITemplateContext extends IContext
{
    @SneakyThrows
    default public Object execute(File _file, File _out)
    {
        this.setFile(_file);
        return execute(new FileReader(_file), _file.getName(), false, new FileOutputStream(_out));
    }
    @SneakyThrows
    default public Object execute(InputStream _stream, String _name, OutputStream _out)
    {
        return execute(new InputStreamReader(_stream),_name, false, _out);
    }
    @SneakyThrows
    default public Object execute(String _script, String _name, OutputStream _out)
    {
        return execute(new StringReader(_script),_name, false, _out);
    }
    @SneakyThrows
    default public Object execute(Reader _reader, String _name, OutputStream _out)
    {
        return execute(_reader, _name, false, _out);
    }

    @SneakyThrows
    default public Object execute(File _file, boolean _useLocal, File _out)
    {
        this.setFile(_file);
        return execute(new FileReader(_file), _file.getName(), _useLocal, new FileOutputStream(_out));
    }
    @SneakyThrows
    default public Object execute(InputStream _stream, String _name, boolean _useLocal, OutputStream _out)
    {
        return execute(new InputStreamReader(_stream),_name, _useLocal, _out);
    }
    @SneakyThrows
    default public Object execute(String _script, String _name, boolean _useLocal, OutputStream _out)
    {
        return execute(new StringReader(_script),_name, _useLocal, _out);
    }
    default public Object execute(Reader _reader, String _name, boolean _useLocal, OutputStream _out)
    {
        compile(_reader, _name);
        return run(Collections.emptyMap(), _useLocal,_out);
    }

    @SneakyThrows
    default public void compile(File _file)
    {
        this.setFile(_file);
        compile(new FileReader(_file), _file.getName());
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
    default public Object run(File _out)
    {
        return run(Collections.emptyMap(), false, new FileOutputStream(_out));
    }
    @SneakyThrows
    default public Object run(OutputStream _out)
    {
        return run(Collections.emptyMap(), false, _out);
    }
    @SneakyThrows
    default public Object run(Map<String,Object> _top, OutputStream _out)
    {
        return run(_top, false, _out);
    }
    @SneakyThrows
    default public Object run(boolean _useLocal, OutputStream _out)
    {
        return run(Collections.emptyMap(), _useLocal, _out);
    }
    public Object run(Map<String,Object> _top, boolean _useLocal, OutputStream _out);

}
