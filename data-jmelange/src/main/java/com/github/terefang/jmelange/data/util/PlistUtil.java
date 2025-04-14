package com.github.terefang.jmelange.data.util;

import com.dd.plist.*;
import com.github.terefang.jmelange.commons.util.ListMapUtil;
import com.github.terefang.jmelange.plexus.util.IOUtil;
import lombok.SneakyThrows;
import org.codehaus.plexus.util.Base64;
import org.hjson.JsonValue;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class PlistUtil
{
    public static Object plistToJava(NSObject _nso)
    {
        if(_nso instanceof NSNull)
        {
            return null;
        }
        else
        if(_nso instanceof NSData) // byte[]
        {
            return ((NSData)_nso).toJavaObject();
        }
        else
        if(_nso instanceof NSDate) // date
        {
            return ((NSDate)_nso).toJavaObject();
        }
        else
        if(_nso instanceof NSString)
        {
            return ((NSString)_nso).toString();
        }
        else
        if(_nso instanceof NSNumber)
        {
            if(((NSNumber)_nso).isReal())
            {
                return ((NSNumber)_nso).doubleValue();
            }
            else
            if(((NSNumber)_nso).isBoolean())
            {
                return ((NSNumber)_nso).boolValue();
            }
            else
            if(((NSNumber)_nso).isInteger())
            {
                return ((NSNumber)_nso).longValue();
            }
            else
            {
                return ((NSNumber)_nso).stringValue();
            }
        }
        else
        if(_nso instanceof NSDictionary)
        {
            Map<String,Object> _ret = new LinkedHashMap<>();
            for(String _k : ((NSDictionary)_nso).keySet())
            {
                _ret.put(_k, plistToJava(((NSDictionary)_nso).get(_k)));
            }
            return _ret;
        }
        else
        if(_nso instanceof NSArray)
        {
            List<Object> _ret = new Vector<>();
            for(NSObject _v : ((NSArray)_nso).getArray())
            {
                _ret.add(plistToJava(_v));
            }
            return _ret;
        }
        else
        if(_nso instanceof NSSet)
        {
            List<Object> _ret = new Vector<>();
            for(NSObject _v : ((NSSet)_nso).allObjects())
            {
                _ret.add(plistToJava(_v));
            }
            return _ret;
        }
        else
        {
            return _nso.toString();
        }
    }
    
    public static NSDictionary toPlist(Map _v)
    {
        NSDictionary _ret = new NSDictionary();
        for(Object _k : _v.keySet())
        {
            _ret.put(_k.toString(),toPlist(_v.get(_k)));
        }
        return _ret;
    }
    
    public static NSArray toPlist(List _v)
    {
        NSArray _ret = new NSArray(_v.size());
        int _i=0;
        for(Object _k : _v)
        {
            _ret.setValue(_i++,toPlist(_k));
        }
        return _ret;
    }

    public static NSArray toPlistFromArray(Object[] _v)
    {
        NSArray _ret = new NSArray(_v.length);
        int _i=0;
        for(Object _k : _v)
        {
            _ret.setValue(_i++,toPlist(_k));
        }
        return _ret;
    }
    
    public static NSObject toPlist(Object _v)
    {
        if(_v == null)
        {
            return NSNull.fromJavaObject(null);
        }
        else
        if(_v instanceof Map)
        {
            return toPlist((Map)_v);
        }
        else
        if(_v instanceof List)
        {
            return toPlist((List)_v);
        }
        else
        if(_v instanceof String)
        {
            return new NSString((String)_v);
        }
        else
        if(_v instanceof Integer)
        {
            return new NSNumber(((Integer)_v).intValue());
        }
        else
        if(_v instanceof Long)
        {
            return new NSNumber(((Long)_v).longValue());
        }
        else
        if(_v instanceof Boolean)
        {
            return new NSNumber(((Boolean)_v).booleanValue());
        }
        else
        if(_v instanceof Double)
        {
            return new NSNumber(((Double)_v).doubleValue());
        }
        else
        if(_v instanceof Float)
        {
            return new NSNumber(((Float)_v).doubleValue());
        }
        else
        if(_v instanceof byte[])
        {
            return new NSData((byte[])_v);
        }
        else
        if(_v.getClass().isArray())
        {
            return toPlistFromArray((Object[])_v);
        }
        return new NSString(_v.toString());
    }
    
    @SneakyThrows
    public static Map<String, Object> loadContext(File _file)
    {
        return loadContext(new FileInputStream(_file));
    }
    
    @SneakyThrows
    public static Map<String, Object> loadContext(InputStream _file)
    {
        NSObject _nso = PropertyListParser.parse(_file);
        if(_nso instanceof NSArray)
        {
            return ListMapUtil.toLMap("data",PlistUtil.plistToJava(_nso));
        }
        else
        if(_nso instanceof NSDictionary)
        {
            return (Map)PlistUtil.plistToJava(_nso);
        }
        throw new IllegalArgumentException(_nso.getClass().getName());
    }
    
    @SneakyThrows
    public static Map<String, Object> loadContext(File _file, Charset _cs)
    {
        return loadContext(new FileReader(_file, _cs));
    }
    
    @SneakyThrows
    public static Map<String, Object> loadContext(InputStream _file, Charset _cs)
    {
        return loadContext(new InputStreamReader(_file, _cs));
    }
    
    @SneakyThrows
    public static Map<String, Object> loadContext(Reader _file)
    {
        NSObject _nso = PropertyListParser.parse(IOUtil.toString(_file).getBytes(StandardCharsets.UTF_8));
        if(_nso instanceof NSArray)
        {
            return ListMapUtil.toLMap("data",PlistUtil.plistToJava(_nso));
        }
        else
        if(_nso instanceof NSDictionary)
        {
            return (Map)PlistUtil.plistToJava(_nso);
        }
        throw new IllegalArgumentException(_nso.getClass().getName());
    }
    
    @SneakyThrows
    public static void writeContext(Map<String, Object> _data, File _file)
    {
        Files.writeString(_file.toPath(), toPlist(_data).toASCIIPropertyList() );
    }
    
    @SneakyThrows
    public static void writeContext(Map<String, Object> _data, OutputStream _file)
    {
        //String _o = toPlist(_data).toASCIIPropertyList();
        String _o = toPlist(_data).toGnuStepASCIIPropertyList();
        IOUtil.copy(_o, _file);
        IOUtil.close(_file);
    }
    
    @SneakyThrows
    public static void writeContext(Map<String, Object> _data, Writer _file)
    {
        //String _o = toPlist(_data).toASCIIPropertyList();
        String _o = toPlist(_data).toGnuStepASCIIPropertyList();
        IOUtil.copy(_o, _file);
        IOUtil.close(_file);
    }
    
    @SneakyThrows
    public static List<Map<String, Object>> loadRows(File _file)
    {
        return loadRows(new FileInputStream(_file));
    }
    
    @SneakyThrows
    public static List<Map<String, Object>> loadRows(InputStream _file)
    {
        NSObject _nso = PropertyListParser.parse(_file);
        if(_nso instanceof NSArray)
        {
            return (List)PlistUtil.plistToJava(_nso);
        }
        else
        if(_nso instanceof NSDictionary)
        {
            return (List)((Map)PlistUtil.plistToJava(_nso)).get("data");
        }
        throw new IllegalArgumentException(_nso.getClass().getName());
    }
    
    @SneakyThrows
    public static List<Map<String, Object>> loadRows(Reader _file)
    {
        NSObject _nso = PropertyListParser.parse(IOUtil.toString(_file).getBytes(StandardCharsets.UTF_8));
        if(_nso instanceof NSArray)
        {
            return (List)PlistUtil.plistToJava(_nso);
        }
        else
        if(_nso instanceof NSDictionary)
        {
            return (List)((Map)PlistUtil.plistToJava(_nso)).get("data");
        }
        throw new IllegalArgumentException(_nso.getClass().getName());
    }
    
    @SneakyThrows
    public static void writeRows(List<Map<String, Object>> _data, File _file)
    {
        Files.writeString(_file.toPath(), toPlist(_data).toASCIIPropertyList() );
    }
    
    @SneakyThrows
    public static void writeRows(List<Map<String, Object>> _data, OutputStream _file)
    {
        //String _o = toPlist(_data).toASCIIPropertyList();
        String _o = toPlist(_data).toGnuStepASCIIPropertyList();
        IOUtil.copy(_o, _file);
        IOUtil.close(_file);
    }
    
    @SneakyThrows
    public static void writeRows(List<Map<String, Object>> _data, Writer _file)
    {
        //String _o = toPlist(_data).toASCIIPropertyList();
        String _o = toPlist(_data).toGnuStepASCIIPropertyList();
        IOUtil.copy(_o, _file);
        IOUtil.close(_file);
    }
    
    @SneakyThrows
    public static void writeContext(Map<String, Object> _data, File _file, Charset _cs)
    {
        writeContext(_data,new FileWriter(_file,_cs));
    }
    
    @SneakyThrows
    public static void writeContext(Map<String, Object> _data, OutputStream _file, Charset _cs)
    {
        writeContext(_data,new OutputStreamWriter(_file,_cs));
    }
}
