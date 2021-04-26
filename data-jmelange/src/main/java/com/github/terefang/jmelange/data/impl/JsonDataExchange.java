package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.RowDataReader;
import com.github.terefang.jmelange.data.RowDataWriter;
import com.github.terefang.jmelange.data.util.HsonUtil;
import lombok.SneakyThrows;
import org.codehaus.plexus.util.IOUtil;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JsonDataExchange extends HsonDataExchange implements AbstractDataExchange, RowDataReader, RowDataWriter
{
    static String DATANAME = "json";
    static List<String> DATANAMES = Collections.unmodifiableList(CommonUtil.toList("json"));
    static List<String> DATAEXTS = Collections.unmodifiableList(CommonUtil.toList(".json"));

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
        try (Writer _writer = new OutputStreamWriter(_file))
        {
            HsonUtil.writeAsHson(true, _writer, _data);
        }
    }

    @Override
    @SneakyThrows
    public void writeObject(Map<String, Object> _data, OutputStream _file) {
        try (Writer _writer = new OutputStreamWriter(_file))
        {
            HsonUtil.writeAsHson(true, _writer, _data);
        }
    }
}
