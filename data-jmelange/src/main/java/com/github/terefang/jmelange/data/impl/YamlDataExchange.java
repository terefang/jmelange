package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.ObjectDataReader;
import com.github.terefang.jmelange.data.ObjectDataWriter;
import com.github.terefang.jmelange.data.RowDataReader;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class YamlDataExchange
        implements
        AbstractDataExchange,
        ObjectDataReader,
        ObjectDataWriter,
        RowDataReader
{
    static String DATANAME = "yaml";
    static List<String> DATANAMES = Collections.unmodifiableList(CommonUtil.toList("snakeyaml", "yaml"));
    static List<String> DATAEXTS = Collections.unmodifiableList(CommonUtil.toList(".yaml", ".yml"));

    static Yaml _y = new Yaml();

    @Override
    @SneakyThrows
    public Map<String, Object> readObject(File _file)
    {
        return readObject(new FileInputStream(_file));
    }

    @Override
    public Map<String, Object> readObject(Reader _file) {
        return _y.loadAs(_file, HashMap.class);
    }

    @Override
    @SneakyThrows
    public Map<String, Object> readObject(InputStream _file)
    {
        try (Reader _reader = new InputStreamReader(_file))
        {
            return _y.loadAs(_reader, HashMap.class);
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
        try (Writer _writer = new OutputStreamWriter(_file))
        {
            _y.dump(_data, _writer);
        }
    }

    @Override
    public void writeObject(Map<String, Object> _data, Writer _file) {
        _y.dump(_data, _file);
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

    @Override
    public List<Map<String, Object>> readRows(File _file) {
        return (List<Map<String, Object>>) this.readObject(_file).get("data");
    }

    @Override
    public List<Map<String, Object>> readRows(InputStream _file) {
        return (List<Map<String, Object>>) this.readObject(_file).get("data");
    }

    @Override
    public List<Map<String, Object>> readRows(Reader _file) {
        return (List<Map<String, Object>>) this.readObject(_file).get("data");
    }
}
