package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.apache.csv.CSVFormat;
import com.github.terefang.jmelange.commons.util.IOUtil;
import com.github.terefang.jmelange.commons.util.ListMapUtil;
import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.RowDataReader;
import com.github.terefang.jmelange.data.RowDataWriter;
import com.github.terefang.jmelange.data.util.CsvUtil;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TxtDataReader
        implements RowDataReader
{
    static String DATANAME = "txt";
    static List<String> DATANAMES = Collections.unmodifiableList(Collections.singletonList("txt"));
    static List<String> DATAEXTS = Collections.unmodifiableList(Collections.singletonList(".txt"));

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
        return readRows(new FileReader(_file, _cs));
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> readRows(InputStream _file)
    {
        return readRows(_file, StandardCharsets.UTF_8);
    }
    @Override
    @SneakyThrows
    public List<Map<String, Object>> readRows(InputStream _file, Charset _cs) {
        List<Map<String, Object>> _ret = new LinkedList<>();
        for(String _line : IOUtil.toString(_file, _cs).split("\n"))
        {
            _ret.add(ListMapUtil.toMap(_ROW_DATA_KEY, _line));
        }
        return _ret;
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> readRows(Reader _file) {
        List<Map<String, Object>> _ret = new LinkedList<>();
        for(String _line : IOUtil.toString(_file).split("\n"))
        {
            _ret.add(ListMapUtil.toMap(_ROW_DATA_KEY, _line));
        }
        return _ret;
    }
}
