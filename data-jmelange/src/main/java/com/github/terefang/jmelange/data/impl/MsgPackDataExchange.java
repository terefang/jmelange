package com.github.terefang.jmelange.data.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.terefang.jmelange.data.*;
import lombok.SneakyThrows;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.*;
import java.util.*;

public class MsgPackDataExchange implements AbstractDataExchange, ObjectDataReader, ObjectDataWriter, RowDataReader, RowDataWriter
{
    static String DATANAME = "msgpack";
    static List<String> DATANAMES = Collections.unmodifiableList(Arrays.asList("msgpack"));
    static List<String> DATAEXTS = Collections.unmodifiableList(Arrays.asList(".msgp"));

    static ObjectMapper _om = new ObjectMapper(new MessagePackFactory());

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

    @Override
    @SneakyThrows
    public List<Map<String, Object>> readRows(File _file)
    {
        return (List)_om.readValue(_file, new TypeReference<ArrayList<LinkedHashMap<String, Object>>>() {});
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> readRows(InputStream _file)
    {
        return (List)_om.readValue(_file, new TypeReference<ArrayList<LinkedHashMap<String, Object>>>() {});
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> readRows(Reader _file)
    {
        return (List)_om.readValue(_file, new TypeReference<ArrayList<LinkedHashMap<String, Object>>>() {});
    }

    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, File _file)
    {
        _om.writeValue(_file, _data);
    }

    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, OutputStream _file)
    {
        _om.writeValue(_file, _data);
    }

    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, Writer _file)
    {
        _om.writeValue(_file, _data);
    }
}
