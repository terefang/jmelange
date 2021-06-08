package com.github.terefang.jmelange.data.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.data.AbstractDataExchange;
import com.github.terefang.jmelange.data.ObjectDataReader;
import com.github.terefang.jmelange.data.RowDataReader;
import lombok.SneakyThrows;
import org.ini4j.Ini;

import java.io.*;
import java.util.*;

public class IniDataExchange implements AbstractDataExchange, ObjectDataReader, RowDataReader
{
    static String DATANAME = "ini";
    static List<String> DATANAMES = Collections.unmodifiableList(CommonUtil.toList("ini"));
    static List<String> DATAEXTS = Collections.unmodifiableList(CommonUtil.toList(".ini"));

    @Override
    @SneakyThrows
    public Map<String, Object> readObject(File _file)
    {
        return loadContextFromIni(new FileInputStream(_file));
    }

    @Override
    public Map<String, Object> readObject(InputStream _file) {
        return loadContextFromIni(_file);
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> readRows(File _file) {
        return loadRowsFromIni(new FileInputStream(_file));
    }

    @Override
    public List<Map<String, Object>> readRows(InputStream _file) {
        return loadRowsFromIni(_file);
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

    @SneakyThrows
    public static Map<String, Object> loadContextFromIni(InputStream _source)
    {
        try
        {
            Ini _ini = new Ini();
            _ini.load(new InputStreamReader(_source));
            Map<String, Object> _ret = new HashMap<>();
            for(String _key : _ini.keySet())
            {
                Map<String, Object> _set = new HashMap<>();
                for(String _k : _ini.get(_key).keySet())
                {
                    _set.put(_k, _ini.get(_key, _k));
                }
                _ret.put(_key, _set);
            }
            return _ret;
        }
        finally
        {
            CommonUtil.close(_source);
        }
    }

    @SneakyThrows
    public static List<Map<String, Object>> loadRowsFromIni(InputStream _source)
    {
        List<Map<String, Object>> _ret = new Vector<>();
        try
        {
            Ini _ini = new Ini();
            _ini.load(new InputStreamReader(_source));
            for(String _key : _ini.keySet())
            {
                Map<String, Object> _set = new HashMap<>();
                for(String _k : _ini.get(_key).keySet())
                {
                    _set.put(_k, _ini.get(_key, _k));
                }
                _set.put("_section",_key);
                _ret.add(_set);
            }
            return _ret;
        }
        finally
        {
            CommonUtil.close(_source);
        }
    }
}
