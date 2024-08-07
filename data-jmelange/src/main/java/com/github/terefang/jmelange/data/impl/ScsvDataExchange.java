package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.RowDataReader;
import com.github.terefang.jmelange.data.RowDataWriter;
import com.github.terefang.jmelange.data.util.CsvUtil;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ScsvDataExchange extends CsvDataExchange implements AbstractDataExchange, RowDataReader, RowDataWriter
{
    static String DATANAME = "scsv";
    static List<String> DATANAMES = Collections.unmodifiableList(Collections.singletonList("scsv"));
    static List<String> DATAEXTS = Collections.unmodifiableList(Collections.singletonList(".scsv"));

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
    public void writeRows(List<Map<String, Object>> _data, OutputStream _file) {
        try (OutputStreamWriter _writer = new OutputStreamWriter(_file))
        {
            CsvUtil.writeAsCsv(_writer, "scsv", true, _data);
        }
    }

    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, OutputStream _file, Charset _cs) {
        try (OutputStreamWriter _writer = new OutputStreamWriter(_file, _cs))
        {
            CsvUtil.writeAsCsv(_writer, "scsv", true, _data);
        }
    }

    @Override
    public List<Map<String, Object>> readRows(InputStream _file) {
        return CsvUtil.readFileCsv("scsv", _file, StandardCharsets.UTF_8);
    }

    @Override
    public List<Map<String, Object>> readRows(InputStream _file, Charset _cs) {
        return CsvUtil.readFileCsv("scsv", _file, _cs);
    }

    @Override
    public List<Map<String, Object>> readRows(Reader _file) {
        return CsvUtil.readFileCsv("scsv", _file);
    }

    @Override
    public void writeRows(List<Map<String, Object>> _data, Writer _file) {
        CsvUtil.writeAsCsv(_file, "scsv", true, _data);
    }
}
