package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.data.*;
import com.github.terefang.jmelange.pdata.PdataUtil;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PdataDataExchange
        implements
        AbstractDataExchange,
        ObjectDataReader,
        ObjectDataWriter,
        RowDataReader,
        RowDataWriter
{
    static String DATANAME = "pdata";
    static List<String> DATANAMES = Collections.unmodifiableList(CommonUtil.toList("pdata", "pdx"));
    static List<String> DATAEXTS = Collections.unmodifiableList(CommonUtil.toList(".pdata", ".pdx"));

    @Override
    public Map<String, Object> readObject(File _file) {
        return PdataUtil.loadFrom(_file);
    }

    @Override
    public Map<String, Object> readObject(Reader _file) {
        return PdataUtil.loadFrom(_file);
    }

    @Override
    public Map<String, Object> readObject(InputStream _file) {
        return PdataUtil.loadFrom(new InputStreamReader(_file));
    }

    @Override
    public void writeObject(Map<String, Object> _data, File _file) {
        PdataUtil.writeTo(_data, _file);
    }

    @Override
    public void writeObject(Map<String, Object> _data, OutputStream _file) {
        PdataUtil.writeTo(_data, new OutputStreamWriter(_file));
    }

    @Override
    public void writeObject(Map<String, Object> _data, Writer _file) {
        PdataUtil.writeTo(_data, _file);
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
        return (List<Map<String, Object>>) PdataUtil.loadFrom(_file).get(_ROW_DATA_KEY);
    }

    @Override
    public List<Map<String, Object>> readRows(InputStream _file) {
        return (List<Map<String, Object>>) PdataUtil.loadFrom(new InputStreamReader(_file)).get(_ROW_DATA_KEY);
    }

    @Override
    public List<Map<String, Object>> readRows(Reader _file) {
        return (List<Map<String, Object>>) PdataUtil.loadFrom(_file).get(_ROW_DATA_KEY);
    }

    @Override
    public void writeRows(List<Map<String, Object>> _data, File _file) {
        PdataUtil.writeTo(CommonUtil.toMap(_ROW_DATA_KEY, _data), _file);
    }

    @Override
    public void writeRows(List<Map<String, Object>> _data, OutputStream _file) {
        PdataUtil.writeTo(CommonUtil.toMap(_ROW_DATA_KEY, _data), new OutputStreamWriter(_file));
    }

    @Override
    public void writeRows(List<Map<String, Object>> _data, Writer _file) {
        PdataUtil.writeTo(CommonUtil.toMap(_ROW_DATA_KEY, _data), _file);
    }
}
