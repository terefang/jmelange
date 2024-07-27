package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.RowDataWriter;
import com.github.terefang.jmelange.data.util.XlsxUtil;
import lombok.SneakyThrows;
import org.codehaus.plexus.util.IOUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class XlsxDataWriter implements AbstractDataExchange, RowDataWriter
{
    static String DATANAME = "xlsx";
    static List<String> DATANAMES = Collections.unmodifiableList(Collections.singletonList("xlsx"));
    static List<String> DATAEXTS = Collections.unmodifiableList(Collections.singletonList(".xlsx"));

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
        writeRows(_data, new FileOutputStream(_file));
    }

    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, OutputStream _file)
    {
        XlsxUtil.toXlsx(_file, _data);
        IOUtil.close(_file);
    }

    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, OutputStream _file, Charset _cs)
    {
        XlsxUtil.toXlsx(_file, _data);
        IOUtil.close(_file);
    }

    @Override
    public void writeRows(List<Map<String, Object>> _data, Writer _file) {
        throw new IllegalArgumentException("writer not supported on binary data");
    }

}
