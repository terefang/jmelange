package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.RowDataReader;
import com.github.terefang.jmelange.data.RowDataWriter;
import com.github.terefang.jmelange.data.util.CsvUtil;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TsvDataExchange implements AbstractDataExchange, RowDataReader, RowDataWriter
{
    static String DATANAME = "tsv";
    static List<String> DATANAMES = Collections.unmodifiableList(Collections.singletonList("tsv"));
    static List<String> DATAEXTS = Collections.unmodifiableList(Collections.singletonList(".tsv"));

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
        try (OutputStreamWriter _writer = new OutputStreamWriter(_file))
        {
            CsvUtil.writeAsCsv(_writer, CSVFormat.Predefined.TDF.name(), true, _data);
        }
    }

    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, OutputStream _file, Charset _cs) {
        try (OutputStreamWriter _writer = new OutputStreamWriter(_file, _cs))
        {
            CsvUtil.writeAsCsv(_writer, CSVFormat.Predefined.TDF.name(), true, _data);
        }
    }

    @Override
    public void writeRows(List<Map<String, Object>> _data, Writer _file)
    {
        CsvUtil.writeAsCsv(_file, CSVFormat.Predefined.TDF.name(), true, _data);
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> readRows(File _file) {
        return readRows(new FileInputStream(_file));
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> readRows(File _file, Charset _cs) {
        return readRows(new FileReader(_file, _cs));
    }

    @Override
    public List<Map<String, Object>> readRows(InputStream _file) {
        return CsvUtil.readFileCsv(CSVFormat.Predefined.TDF.name(), _file, StandardCharsets.UTF_8);
    }
    @Override
    public List<Map<String, Object>> readRows(InputStream _file, Charset _cs) {
        return CsvUtil.readFileCsv(CSVFormat.Predefined.TDF.name(), _file, _cs);
    }

    @Override
    public List<Map<String, Object>> readRows(Reader _file) {
        return CsvUtil.readFileCsv(CSVFormat.Predefined.TDF.name(), _file);
    }
}
