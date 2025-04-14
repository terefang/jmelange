package com.github.terefang.jmelange.data.impl;


import com.github.terefang.jmelange.data.*;
import com.github.terefang.jmelange.data.util.PlistUtil;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PlistDataExchange implements AbstractDataExchange, ObjectDataReader, ObjectDataWriter, RowDataReader, RowDataWriter
{
    static String DATANAME = "plist";
    static List<String> DATANAMES = Collections.unmodifiableList(Arrays.asList("plist"));
    static List<String> DATAEXTS = Collections.unmodifiableList(Arrays.asList(".plist"));

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
    public List<Map<String, Object>> readRows(File _file)
    {
        return PlistUtil.loadRows(_file);
    }
    
    @SneakyThrows
    @Override
    public List<Map<String, Object>> readRows(File _file, Charset _cs)
    {
        return PlistUtil.loadRows(new FileReader(_file, _cs));
    }
    
    @Override
    @SneakyThrows
    public List<Map<String, Object>> readRows(InputStream _file)
    {
        return PlistUtil.loadRows(_file);
    }
    
    @Override
    public List<Map<String, Object>> readRows(InputStream _file, Charset _cs)
    {
        return readRows(new InputStreamReader(_file, _cs));
    }
    
    @Override
    public List<Map<String, Object>> readRows(Reader _file)
    {
        return PlistUtil.loadRows(_file);
    }
    
    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, File _file, Charset _cs)
    {
        PlistUtil.writeRows(_data, new FileWriter(_file, _cs));
    }
    
    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, File _file)
    {
        PlistUtil.writeRows(_data, _file);
    }
    
    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, OutputStream _file, Charset _cs)
    {
        PlistUtil.writeRows(_data, new OutputStreamWriter(_file, _cs));
    }
    
    @Override
    @SneakyThrows
    public void writeRows(List<Map<String, Object>> _data, OutputStream _file)
    {
        PlistUtil.writeRows(_data, _file);
    }
    
    @Override
    public void writeRows(List<Map<String, Object>> _data, Writer _file)
    {
        PlistUtil.writeRows(_data, _file);
    }
    
    @Override
    public Map<String, Object> readObject(Reader _file)
    {
        return PlistUtil.loadContext(_file);
    }
    
    @Override
    public Map<String, Object> readObject(File _file)
    {
        return PlistUtil.loadContext(_file);
    }
    
    @Override
    public Map<String, Object> readObject(InputStream _file)
    {
        return PlistUtil.loadContext(_file);
    }
    
    @Override
    public Map<String, Object> readObject(File _file, Charset _cs)
    {
        return PlistUtil.loadContext(_file, _cs);
    }
    
    @Override
    public Map<String, Object> readObject(InputStream _file, Charset _cs)
    {
        return PlistUtil.loadContext(_file, _cs);
    }
    
    @Override
    @SneakyThrows
    public void writeObject(Map<String, Object> _data, File _file)
    {
        PlistUtil.writeContext(_data,_file);
    }
    
    @Override
    public void writeObject(Map<String, Object> _data, OutputStream _file)
    {
        PlistUtil.writeContext(_data,_file);
    }
    
    @Override
    public void writeObject(Map<String, Object> _data, File _file, Charset _cs)
    {
        PlistUtil.writeContext(_data,_file, _cs);
    }
    
    @Override
    public void writeObject(Map<String, Object> _data, OutputStream _file, Charset _cs)
    {
        PlistUtil.writeContext(_data,_file, _cs);
    }
    
    @Override
    public void writeObject(Map<String, Object> _data, Writer _file)
    {
        PlistUtil.writeContext(_data,_file);
    }
}
