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

public class ScsvDataExchange extends CsvDataExchange implements AbstractDataExchange, RowDataReader, RowDataWriter
{
    static String DATANAME = "scsv";
    static List<String> DATANAMES = Collections.unmodifiableList(CommonUtil.toList("scsv"));
    static List<String> DATAEXTS = Collections.unmodifiableList(CommonUtil.toList(".scsv"));

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
            CsvUtil.writeAsCsv(_writer, "scsv", true, _data);
        }
    }
    @Override
    public List<Map<String, Object>> readRows(InputStream _file) {
        return CsvUtil.readFileCsv("scsv", _file, StandardCharsets.UTF_8);
    }
}
