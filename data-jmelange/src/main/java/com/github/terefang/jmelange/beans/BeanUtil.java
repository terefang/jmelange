package com.github.terefang.jmelange.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.terefang.jmelange.data.DataReadWriteFactory;
import com.github.terefang.jmelange.data.ObjectDataReader;
import com.github.terefang.jmelange.data.ObjectDataWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class BeanUtil
{
    static ObjectMapper _om = new ObjectMapper();

    @SneakyThrows
    public static <T> T mapToBean(Map<String, ? extends Object> _res, Class<T> _clazz)
    {
        StringWriter _sw = new StringWriter();
        _om.writeValue(_sw, _res);
        return _om.readValue(_sw.getBuffer().toString(), _clazz);
    }

    @SneakyThrows
    public static <T> T toBean(File _file, ObjectDataReader _rd, Class<T> _clazz)
    {
        return mapToBean( _rd.readObject(_file), _clazz);
    }

    @SneakyThrows
    public static <T> T toBean(Reader _file, ObjectDataReader _rd, Class<T> _clazz)
    {
        return mapToBean( _rd.readObject(_file), _clazz);
    }

    @SneakyThrows
    public static <T> T toBean(InputStream _file, ObjectDataReader _rd, Class<T> _clazz)
    {
        return mapToBean( _rd.readObject(_file), _clazz);
    }

    @SneakyThrows
    public static <T> T toBean(String _data, ObjectDataReader _rd, Class<T> _clazz)
    {
        Reader _file = new StringReader(_data);
        return mapToBean( _rd.readObject(_file), _clazz);
    }

    @SneakyThrows
    public static <T> T toBean(File _file, String _type, Class<T> _clazz)
    {
        ObjectDataReader _rd = DataReadWriteFactory.findByName(_type, ObjectDataReader.class);
        return mapToBean(_rd.readObject(_file), _clazz);
    }

    @SneakyThrows
    public static <T> T toBean(Reader _file, String _type, Class<T> _clazz)
    {
        ObjectDataReader _rd = DataReadWriteFactory.findByName(_type, ObjectDataReader.class);
        return mapToBean( _rd.readObject(_file), _clazz);
    }

    @SneakyThrows
    public static <T> T toBean(InputStream _file, String _type, Class<T> _clazz)
    {
        ObjectDataReader _rd = DataReadWriteFactory.findByName(_type, ObjectDataReader.class);
        return mapToBean( _rd.readObject(_file), _clazz);
    }

    @SneakyThrows
    public static <T> T toBean(String _data, String _type, Class<T> _clazz)
    {
        ObjectDataReader _rd = DataReadWriteFactory.findByName(_type, ObjectDataReader.class);
        Reader _file = new StringReader(_data);
        return mapToBean( _rd.readObject(_file), _clazz);
    }

    @SneakyThrows
    public static void writeBean(Object _bean, ObjectDataWriter _wr, File _file)
    {
        StringWriter _sw = new StringWriter();
        _om.writeValue(_sw, _bean);
        _bean = _om.readValue(_sw.getBuffer().toString(), LinkedHashMap.class);
        _wr.writeObject((Map)_bean, _file);
    }

    @SneakyThrows
    public static void writeBean(Object _bean, ObjectDataWriter _wr, Writer _file)
    {
        StringWriter _sw = new StringWriter();
        _om.writeValue(_sw, _bean);
        _bean = _om.readValue(_sw.getBuffer().toString(), LinkedHashMap.class);
        _wr.writeObject((Map)_bean, _file);
    }

    @SneakyThrows
    public static void writeBean(Object _bean, ObjectDataWriter _wr, OutputStream _file)
    {
        StringWriter _sw = new StringWriter();
        _om.writeValue(_sw, _bean);
        _bean = _om.readValue(_sw.getBuffer().toString(), LinkedHashMap.class);
        _wr.writeObject((Map)_bean, _file);
    }

    @SneakyThrows
    public static void writeBean(Object _bean, String _type, File _file)
    {
        StringWriter _sw = new StringWriter();
        _om.writeValue(_sw, _bean);
        _bean = _om.readValue(_sw.getBuffer().toString(), LinkedHashMap.class);
        ObjectDataWriter _wr = DataReadWriteFactory.findByName(_type, ObjectDataWriter.class);
        _wr.writeObject((Map)_bean, _file);
    }

    @SneakyThrows
    public static void writeBean(Object _bean, String _type, Writer _file)
    {
        StringWriter _sw = new StringWriter();
        _om.writeValue(_sw, _bean);
        _bean = _om.readValue(_sw.getBuffer().toString(), LinkedHashMap.class);
        ObjectDataWriter _wr = DataReadWriteFactory.findByName(_type, ObjectDataWriter.class);
        _wr.writeObject((Map)_bean, _file);
    }

    @SneakyThrows
    public static void writeBean(Object _bean, String _type, OutputStream _file)
    {
        StringWriter _sw = new StringWriter();
        _om.writeValue(_sw, _bean);
        _bean = _om.readValue(_sw.getBuffer().toString(), LinkedHashMap.class);
        ObjectDataWriter _wr = DataReadWriteFactory.findByName(_type, ObjectDataWriter.class);
        _wr.writeObject((Map)_bean, _file);
    }
}