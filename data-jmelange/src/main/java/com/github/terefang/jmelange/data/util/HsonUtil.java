package com.github.terefang.jmelange.data.util;

import lombok.SneakyThrows;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;
import org.hjson.Stringify;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class HsonUtil
{
    @SneakyThrows
    public static void writeAsJsonPerLine(Writer _out, List<Map<String, Object>> _res)
    {
        for(Map<String, Object> _row : _res)
        {
            JsonObject _obj = new JsonObject();
            _row.entrySet().forEach((_entry) -> {
                Object _v = _entry.getValue();
                if(_v==null) _v= "";
                _obj.set(_entry.getKey(), _v.toString());
            });
            _obj.writeTo(_out, Stringify.PLAIN);
            _out.write('\n');
        }
    }

    @SneakyThrows
    public static List<Map<String, Object>> readFileJsonPerLine(InputStream _in, Charset _cs)
    {
        List<Map<String, Object>> _res = new Vector<>();
        BufferedReader _inr = new BufferedReader(new InputStreamReader(_in,_cs), 65536);
        String _line = null;
        while((_line = _inr.readLine()) != null)
        {
            if(StringUtils.isNotBlank(_line) && !_line.startsWith("#"))
            {
                JsonValue _row = JsonValue.readJSON(_line);
                Map<String,Object> _map = new LinkedHashMap<>();
                _row.asObject()
                        .forEach(_m -> _map.put(_m.getName(), _m.getValue().asString()));
                _res.add(_map);
            }
        }

        IOUtil.close(_inr);

        return _res;
    }
    @SneakyThrows
    public static void writeAsHson(boolean _json, Writer _out, List<Map<String, Object>> _res)
    {
        JsonArray _arr = new JsonArray();
        for(Map<String, Object> _row : _res)
        {
            JsonObject _obj = new JsonObject();
            _row.entrySet().forEach((_entry) -> {
                Object _v = _entry.getValue();
                if(_v==null) _v= "";
                _obj.set(_entry.getKey(), _v.toString());
            });
            _arr.add(_obj);
        }
        _arr.writeTo(_out, _json ? Stringify.FORMATTED : Stringify.HJSON);
    }

    @SneakyThrows
    public static void writeAsHson(boolean _json, Writer _out, Map<String, Object> _res)
    {
        JsonObject _obj = new JsonObject();
        _res.entrySet().forEach((_entry) -> {
            Object _v = _entry.getValue();
            if(_v==null) _v= "";
            _obj.set(_entry.getKey(), toJsonObject(_v));
        });
        _obj.writeTo(_out, _json ? Stringify.FORMATTED : Stringify.HJSON);
    }

    public static String toHson(Map<String, Object> _res)
    {
        StringWriter _sw = new StringWriter();
        writeAsHson(false, _sw, _res);
        _sw.flush();
        return _sw.getBuffer().toString();
    }

    public static String toJson(Map<String, Object> _res)
    {
        StringWriter _sw = new StringWriter();
        writeAsHson(true, _sw, _res);
        _sw.flush();
        return _sw.getBuffer().toString();
    }

    private static JsonValue toJsonObject(Object _v) {
        if(_v instanceof Map)
        {
            return toJsonObject((Map)_v);
        }
        else
        if(_v instanceof List)
        {
            return toJsonObject((List)_v);
        }
        else
        if(_v instanceof String)
        {
            return JsonValue.valueOf((String)_v);
        }
        else
        if(_v instanceof Integer)
        {
            return JsonValue.valueOf((Integer)_v);
        }
        else
        if(_v instanceof Long)
        {
            return JsonValue.valueOf((Long)_v);
        }
        else
        if(_v instanceof Boolean)
        {
            return JsonValue.valueOf((Boolean)_v);
        }
        else
        if(_v instanceof Double)
        {
            return JsonValue.valueOf((Double)_v);
        }
        else
        if(_v instanceof Float)
        {
            return JsonValue.valueOf((Float)_v);
        }
        else
        if(_v.getClass().isArray())
        {
            return toJsonObject((List) Arrays.asList((Object[])_v));
        }
        return JsonValue.valueOf(_v.toString());
    }

    private static JsonValue toJsonObject(String _v) {
        return JsonValue.valueOf(_v);
    }

    private static JsonValue toJsonObject(int _v) {
        return JsonValue.valueOf(_v);
    }

    private static JsonValue toJsonObject(long _v) {
        return JsonValue.valueOf(_v);
    }

    private static JsonValue toJsonObject(boolean _v) {
        return JsonValue.valueOf(_v);
    }

    private static JsonValue toJsonObject(double _v) {
        return JsonValue.valueOf(_v);
    }

    private static JsonValue toJsonObject(float _v) {
        return JsonValue.valueOf(_v);
    }

    private static JsonValue toJsonObject(Map<String,Object> _v) {
        JsonObject _obj = new JsonObject();
        _v.entrySet().forEach((_entry) -> {
            Object _v1 = _entry.getValue();
            if(_v1==null) _v1= "";
            _obj.set(_entry.getKey(), toJsonObject(_v1));
        });
        return _obj;
    }

    private static JsonValue toJsonObject(List<Object> _v) {
        JsonArray _obj = new JsonArray();
        _v.forEach((_entry) -> {
            if(_entry==null) _entry= "";
            _obj.add(toJsonObject(_entry));
        });
        return _obj;
    }

    @SneakyThrows
    public static Map<String, Object> loadContextFromHjson(InputStream _source)
    {
        HashMap<String, Object> _obj = new HashMap<>();
        JsonValue _hson = JsonValue.readHjson(new InputStreamReader(_source));
        hjsonToMap(_hson)
                .entrySet()
                .forEach(_entry -> _obj.put(_entry.getKey(), _entry.getValue()));
        return _obj;
    }

    @SneakyThrows
    public static Map<String, Object> loadContextFromHjson(Reader _source)
    {
        HashMap<String, Object> _obj = new HashMap<>();
        JsonValue _hson = JsonValue.readHjson(_source);
        hjsonToMap(_hson)
                .entrySet()
                .forEach(_entry -> _obj.put(_entry.getKey(), _entry.getValue()));
        return _obj;
    }

    @SneakyThrows
    public static List loadListFromHjson(InputStream _source)
    {
        HashMap<String, Object> _obj = new HashMap<>();
        JsonValue _hson = JsonValue.readHjson(new InputStreamReader(_source));
        return hjsonToArray(_hson);
    }

    static Map<String, Object> hjsonToMap(JsonValue _v)
    {
        Map<String, Object> _ret = new HashMap<>();
        if(_v.isObject())
        {
            _v.asObject().forEach(m -> _ret.put(m.getName(), hjsonToValue(m.getValue())));
        }
        else
        {
            _ret.put("data", hjsonToValue(_v));
        }
        return _ret;
    }

    static Object hjsonToValue(JsonValue value)
    {
        if(value.isObject())
        {
            return hjsonToMap(value);
        }
        else
        if(value.isArray())
        {
            return hjsonToArray(value);
        }
        else
        if(value.isString())
        {
            return value.asString();
        }
        else
        if(value.isNumber())
        {
            return Double.valueOf(value.asDouble());
        }
        else
        if(value.isBoolean())
        {
            return Boolean.valueOf(value.asBoolean());
        }
        else
        if(value.isNull())
        {
            return null;
        }
        else
        {
            return value.toString();
        }
    }

    static List hjsonToArray(JsonValue value)
    {
        List _ret = new Vector();
        value.asArray().forEach(m -> _ret.add(hjsonToValue(m)));
        return _ret;
    }
}
