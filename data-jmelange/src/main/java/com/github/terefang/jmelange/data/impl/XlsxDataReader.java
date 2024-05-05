package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.ObjectDataReader;
import com.github.terefang.jmelange.data.RowDataReader;
import com.github.terefang.jmelange.data.util.XlsxUtil;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class XlsxDataReader implements AbstractDataExchange, RowDataReader, ObjectDataReader
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
    public List<Map<String, Object>> readRows(File _file)
    {
        return XlsxUtil.fromXlsx(_file);
    }

    @Override
    public List<Map<String, Object>> readRows(InputStream _file)
    {
        try
        {
            return XlsxUtil.fromXlsx(_file);
        }
        finally
        {
            try { _file.close(); } catch (Exception _xe) {}
        }
    }

    @Override
    public List<Map<String, Object>> readRows(Reader _file)
    {
        throw new IllegalArgumentException("reader not supported on binary data");
    }

    @Override
    public Map<String, Object> readObject(File _file)
    {
        return (Map)XlsxUtil.fromXlsxSheets(_file);
    }

    @Override
    public Map<String, Object> readObject(Reader _file)
    {
        throw new IllegalArgumentException("reader not supported on binary data");
    }

    @Override
    public Map<String, Object> readObject(InputStream _file)
    {
        return (Map)XlsxUtil.fromXlsxSheets(_file);
    }
}
