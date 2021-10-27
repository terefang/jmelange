package com.github.terefang.jmelange.data.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlFactory;
import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.data.*;
import lombok.SneakyThrows;

import java.io.*;
import java.util.*;

public class TomlDataExchange implements AbstractDataExchange, ObjectDataReader, ObjectDataWriter
{
    static String DATANAME = "toml";
    static List<String> DATANAMES = Collections.unmodifiableList(CommonUtil.toList("toml"));
    static List<String> DATAEXTS = Collections.unmodifiableList(CommonUtil.toList(".tml", ".toml"));

    static ObjectMapper _om = new ObjectMapper(new TomlFactory());

    @Override
    @SneakyThrows
    public Map<String, Object> readObject(File _file)
    {
        return _om.readValue(_file, new TypeReference<LinkedHashMap<String, Object>>() {});
    }

    @Override
    @SneakyThrows
    public Map<String, Object> readObject(Reader _file)
    {
        return _om.readValue(_file, new TypeReference<LinkedHashMap<String, Object>>() {});
    }

    @Override
    @SneakyThrows
    public Map<String, Object> readObject(InputStream _file)
    {
        return _om.readValue(_file, new TypeReference<LinkedHashMap<String, Object>>() {});
    }

    @Override
    @SneakyThrows
    public void writeObject(Map<String, Object> _data, File _file)
    {
        _om.writeValue(_file, _data);
    }

    @Override
    @SneakyThrows
    public void writeObject(Map<String, Object> _data, OutputStream _file)
    {
        _om.writeValue(_file, _data);
    }

    @Override
    @SneakyThrows
    public void writeObject(Map<String, Object> _data, Writer _file) {
        _om.writeValue(_file, _data);
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
