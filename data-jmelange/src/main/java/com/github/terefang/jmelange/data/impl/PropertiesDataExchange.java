package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.ObjectDataReader;
import com.github.terefang.jmelange.data.ObjectDataWriter;
import lombok.SneakyThrows;

import java.io.*;
import java.util.*;

public class PropertiesDataExchange implements AbstractDataExchange, ObjectDataReader, ObjectDataWriter
{
    static String DATANAME = "properties";
    static List<String> DATANAMES = Collections.unmodifiableList(CommonUtil.toList("properties", "props"));
    static List<String> DATAEXTS = Collections.unmodifiableList(CommonUtil.toList(".props", ".properties"));

    @Override
    @SneakyThrows
    public Map<String, Object> readObject(File _file)
    {
        return readObject(new FileInputStream(_file));
    }

    @Override
    @SneakyThrows
    public Map<String, Object> readObject(Reader _file) {
        Properties _p = new Properties();
        _p.load(_file);
        HashMap<String, Object> _ret = new HashMap<>();
        for (final String name : _p.stringPropertyNames())
            _ret.put(name, _p.getProperty(name));
        return _ret;
    }

    @Override
    @SneakyThrows
    public Map<String, Object> readObject(InputStream _file)
    {
        try (Reader _reader = new InputStreamReader(_file))
        {
            return readObject(_reader);
        }
    }

    @Override
    @SneakyThrows
    public void writeObject(Map<String, Object> _data, File _file)
    {
        writeObject(_data, new FileOutputStream(_file));
    }

    @Override
    @SneakyThrows
    public void writeObject(Map<String, Object> _data, OutputStream _file) {
        try (OutputStreamWriter _writer = new OutputStreamWriter(_file))
        {
            writeObject(_data, _writer);
        }
    }

    @Override
    @SneakyThrows
    public void writeObject(Map<String, Object> _data, Writer _file) {
        Properties _p = new Properties();
        for(Map.Entry<String, Object> _entry : _data.entrySet())
        {
            _p.setProperty(_entry.getKey(), _entry.getValue().toString());
        }
        _p.store(_file, "###");
    }

    @Override
    public String getName() {
        return DATANAME;
    }

    @Override
    public List<String> getNames() {
        return DATANAMES;
    }

    @Override
    public List<String> getExts() {
        return DATAEXTS;
    }
}
