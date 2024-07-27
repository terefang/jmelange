package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.data.*;
import com.github.terefang.jmelange.data.util.HsonUtil;
import lombok.SneakyThrows;
import org.codehaus.plexus.util.IOUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JsonPerLineDataExchange implements AbstractDataExchange, RowDataReader, RowDataWriter
{
    static String DATANAME = "jsonline";
    static List<String> DATANAMES = Collections.unmodifiableList(Arrays.asList("jsonline", "jsonperline"));
    static List<String> DATAEXTS = Collections.unmodifiableList(Arrays.asList(".jpl", ".jsonl"));

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
    public List<Map<String, Object>> readRows(File _file, Charset _cs) {
        return readRows(new FileReader(_file,_cs));
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> readRows(InputStream _file) {
        try {
            return HsonUtil.readFileJsonPerLine(_file, StandardCharsets.UTF_8);
        }
        finally {
            IOUtil.close(_file);
        }
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> readRows(InputStream _file, Charset _cs) {
        try {
            return HsonUtil.readFileJsonPerLine(_file, _cs);
        }
        finally {
            IOUtil.close(_file);
        }
    }

    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, File _file) {
        writeRows(_data, new FileOutputStream(_file));
    }

    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, File _file, Charset _cs) {
        writeRows(_data, new FileWriter(_file,_cs));
    }

    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, OutputStream _file) {
        try (Writer _writer = new OutputStreamWriter(_file))
        {
            HsonUtil.writeAsJsonPerLine(_writer, _data);
        }
    }

    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, OutputStream _file,Charset _cs) {
        try (Writer _writer = new OutputStreamWriter(_file, _cs))
        {
            HsonUtil.writeAsJsonPerLine(_writer, _data);
        }
    }

    @Override
    public List<Map<String, Object>> readRows(Reader _file) {
        return HsonUtil.readFileJsonPerLine(_file);
    }

    @Override
    public void writeRows(List<Map<String, Object>> _data, Writer _file) {
        HsonUtil.writeAsJsonPerLine(_file, _data);
    }
}
