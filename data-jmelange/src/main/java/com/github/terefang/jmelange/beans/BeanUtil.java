package com.github.terefang.jmelange.beans;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
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
    static ObjectMapper _om = new ObjectMapper()
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    static TypeReference<Map<String, Object>> _type = new TypeReference<Map<String, Object>>() {};

    @SneakyThrows
    public static <T> T mapToBean(Map<String, ? extends Object> _res, Class<T> _clazz)
    {
        return _om.convertValue(_res, _clazz);
    }

    @SneakyThrows
    public static <T> T mapToBean(Map<String, ? extends Object> _res, String _key, Class<T> _clazz)
    {
        if(!_res.containsKey(_key)) return null;
        _res = (Map<String, ? extends Object>) _res.get(_key);
        return _om.convertValue(_res, _clazz);
    }

    @SneakyThrows
    public static <T> T mapToBean(Map<String, ? extends Object> _res, String _key1, String _key2, Class<T> _clazz)
    {
        if(!_res.containsKey(_key1)) return null;
        _res = (Map<String, ? extends Object>) _res.get(_key1);
        if(!_res.containsKey(_key2)) return null;
        _res = (Map<String, ? extends Object>) _res.get(_key2);
        return _om.convertValue(_res, _clazz);
    }

    @SneakyThrows
    public static <T> T mapToBean(Map<String, ? extends Object> _res, String _key1, String _key2, String _key3, Class<T> _clazz)
    {
        if(!_res.containsKey(_key1)) return null;
        _res = (Map<String, ? extends Object>) _res.get(_key1);
        if(!_res.containsKey(_key2)) return null;
        _res = (Map<String, ? extends Object>) _res.get(_key2);
        if(!_res.containsKey(_key3)) return null;
        _res = (Map<String, ? extends Object>) _res.get(_key3);
        return _om.convertValue(_res, _clazz);
    }

    @SneakyThrows
    public static <T> T mapToBean(Map<String, ? extends Object> _res, String _key1, String _key2, String _key3, String _key4, Class<T> _clazz)
    {
        if(!_res.containsKey(_key1)) return null;
        _res = (Map<String, ? extends Object>) _res.get(_key1);
        if(!_res.containsKey(_key2)) return null;
        _res = (Map<String, ? extends Object>) _res.get(_key2);
        if(!_res.containsKey(_key3)) return null;
        _res = (Map<String, ? extends Object>) _res.get(_key3);
        if(!_res.containsKey(_key4)) return null;
        _res = (Map<String, ? extends Object>) _res.get(_key4);
        return _om.convertValue(_res, _clazz);
    }

    @SneakyThrows
    public static <T> Map<String, ? extends Object> mapFromBean(T _res)
    {
        return _om.convertValue(_res, _type);
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