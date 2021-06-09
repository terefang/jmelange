package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.data.*;
import com.github.terefang.jmelange.data.util.HsonUtil;
import lombok.SneakyThrows;
import org.codehaus.plexus.util.IOUtil;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HsonDataExchange implements AbstractDataExchange, ObjectDataReader, ObjectDataWriter, RowDataReader, RowDataWriter
{
    static String DATANAME = "hson";
    static List<String> DATANAMES = Collections.unmodifiableList(CommonUtil.toList("hjson", "hson"));
    static List<String> DATAEXTS = Collections.unmodifiableList(CommonUtil.toList(".hjson", ".hson"));

    @Override
    @SneakyThrows
    public Map<String, Object> readObject(File _file)
    {
        return readObject(new FileInputStream(_file));
    }

    @Override
    public Map<String, Object> readObject(Reader _file) {
        return null;
    }

    @Override
    @SneakyThrows
    public Map<String, Object> readObject(InputStream _file)
    {
        try {
            return HsonUtil.loadContextFromHjson(_file);
        }
        finally {
            IOUtil.close(_file);
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
            HsonUtil.writeAsHson(false, _writer, _data);
        }
    }

    @Override
    public void writeObject(Map<String, Object> _data, Writer _file) {
        HsonUtil.writeAsHson(false, _file, _data);
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
    public List<Map<String, Object>> readRows(File _file) {
        return readRows(new FileInputStream(_file));
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> readRows(InputStream _file) {
        try {
            return HsonUtil.loadListFromHjson(_file);
        }
        finally {
            IOUtil.close(_file);
        }
    }

    @Override
    public List<Map<String, Object>> readRows(Reader _file) {
        return HsonUtil.loadListFromHjson(_file);
    }

    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, File _file) {
        writeRows(_data, new FileOutputStream(_file));
    }

    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, OutputStream _file) {
        try (Writer _writer = new OutputStreamWriter(_file))
        {
            HsonUtil.writeAsHson(false, _writer, _data);
        }
    }

    @Override
    public void writeRows(List<Map<String, Object>> _data, Writer _file) {
        HsonUtil.writeAsHson(false, _file, _data);
    }
}
