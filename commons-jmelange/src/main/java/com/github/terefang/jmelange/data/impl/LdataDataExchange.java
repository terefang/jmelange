package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.commons.util.LdataUtil;
import com.github.terefang.jmelange.data.*;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LdataDataExchange
        implements
        AbstractDataExchange,
        ObjectDataReader,
        ObjectDataWriter,
        RowDataReader,
        RowDataWriter
{
    static String DATANAME = "ldata";
    static List<String> DATANAMES = Collections.unmodifiableList(Arrays.asList("ldata", "ldx", "pdata", "pdx"));
    static List<String> DATAEXTS = Collections.unmodifiableList(Arrays.asList(".ldata", ".ldx", ".pdata", ".pdx"));

    @Override
    public Map<String, Object> readObject(File _file) {
        return LdataUtil.loadFrom(_file);
    }

    @Override
    public Map<String, Object> readObject(Reader _file) {
        return LdataUtil.loadFrom(_file);
    }

    @Override
    public Map<String, Object> readObject(InputStream _file) {
        return LdataUtil.loadFrom(new InputStreamReader(_file));
    }

    @Override
    public void writeObject(Map<String, Object> _data, File _file) {
        LdataUtil.writeTo(_data, _file);
    }

    @Override
    public void writeObject(Map<String, Object> _data, OutputStream _file) {
        LdataUtil.writeTo(_data, new OutputStreamWriter(_file));
    }

    @Override
    public void writeObject(Map<String, Object> _data, Writer _file) {
        LdataUtil.writeTo(_data, _file);
    }

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
    public List<Map<String, Object>> readRows(File _file) {
        return (List<Map<String, Object>>) LdataUtil.loadFrom(_file).get(_ROW_DATA_KEY);
    }

    @Override
    public List<Map<String, Object>> readRows(InputStream _file) {
        return (List<Map<String, Object>>) LdataUtil.loadFrom(new InputStreamReader(_file)).get(_ROW_DATA_KEY);
    }

    @Override
    public List<Map<String, Object>> readRows(Reader _file) {
        return (List<Map<String, Object>>) LdataUtil.loadFrom(_file).get(_ROW_DATA_KEY);
    }

    @Override
    public void writeRows(List<Map<String, Object>> _data, File _file) {
        LdataUtil.writeTo(Collections.singletonMap(_ROW_DATA_KEY, _data), _file);
    }

    @Override
    public void writeRows(List<Map<String, Object>> _data, OutputStream _file) {
        LdataUtil.writeTo(Collections.singletonMap(_ROW_DATA_KEY, _data), new OutputStreamWriter(_file));
    }

    @Override
    public void writeRows(List<Map<String, Object>> _data, Writer _file) {
        LdataUtil.writeTo(Collections.singletonMap(_ROW_DATA_KEY, _data), _file);
    }
}
