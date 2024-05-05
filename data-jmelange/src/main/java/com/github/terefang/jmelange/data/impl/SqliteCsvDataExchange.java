package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.RowDataReader;
import com.github.terefang.jmelange.data.RowDataWriter;
import com.github.terefang.jmelange.data.util.CsvUtil;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SqliteCsvDataExchange implements AbstractDataExchange, RowDataReader, RowDataWriter
{
    static String DATANAME = "sqlite-csv";
    static List<String> DATANAMES = Collections.unmodifiableList(CommonUtil.toList("sqlite-csv"));
    static List<String> DATAEXTS = Collections.unmodifiableList(CommonUtil.toList(".sqlite.csv"));

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
    public void writeRows(List<Map<String, Object>> _data, OutputStream _file) {
        try (Writer _writer = new OutputStreamWriter(_file))
        {
            CsvUtil.writeAsCsv(_writer, DATANAME, true, _data);
        }
    }

    @Override
    public void writeRows(List<Map<String, Object>> _data, Writer _file) {
        CsvUtil.writeAsCsv(_file, DATANAME, true, _data);
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> readRows(File _file) {
        return readRows(new FileInputStream(_file));
    }

    @Override
    public List<Map<String, Object>> readRows(InputStream _file) {
        return CsvUtil.readFileCsv(DATANAME, _file, StandardCharsets.UTF_8);
    }

    @Override
    public List<Map<String, Object>> readRows(Reader _file) {
        return CsvUtil.readFileCsv(DATANAME, _file);
    }
}
