package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.RowDataReader;
import com.github.terefang.jmelange.data.RowDataWriter;
import com.github.terefang.jmelange.data.util.XlsxUtil;
import lombok.SneakyThrows;
import org.codehaus.plexus.util.IOUtil;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class XlsxDataReader implements AbstractDataExchange, RowDataReader
{
    static String DATANAME = "xlsx";
    static List<String> DATANAMES = Collections.unmodifiableList(CommonUtil.toList("xlsx"));
    static List<String> DATAEXTS = Collections.unmodifiableList(CommonUtil.toList(".xlsx"));

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
    public List<Map<String, Object>> readRows(File _file)
    {
        return XlsxUtil.fromXlsx(_file);
    }

    @Override
    public List<Map<String, Object>> readRows(InputStream _file) {
        try
        {
            return XlsxUtil.fromXlsx(_file);
        }
        finally
        {
            IOUtil.close(_file);
        }
    }

    @Override
    public List<Map<String, Object>> readRows(Reader _file) {
        throw new IllegalArgumentException("reader not supported on binary data");
    }
}
