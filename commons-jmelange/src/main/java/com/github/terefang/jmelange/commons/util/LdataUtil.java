package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.commons.base.Base64;
import com.github.terefang.jmelange.data.ldata.LdataParser;
import com.github.terefang.jmelange.data.ldata.LdataWriter;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

public class LdataUtil
{
    public static void writeTo(Map _context, Writer _writer)
    {
        LdataWriter.writeTo(false, _context, _writer, true);
    }

    public static void writeTo(Map _context, Writer _writer, boolean _autoclose)
    {
        LdataWriter.writeTo(false, _context, _writer, _autoclose);
    }

    public static void writeTo(Map _context, OutputStream _writer, boolean _autoclose)
    {
        LdataWriter.writeTo(false, _context, new OutputStreamWriter(_writer), _autoclose);
    }

    public static void writeTo(Map _context, File _out)
    {
        LdataWriter.writeTo(false, _context, _out);
    }

    public static void writeToAlt(Map _context, Writer _writer)
    {
        LdataWriter.writeTo(true, _context, _writer, true);
    }

    public static void writeToAlt(Map _context, File _out)
    {
        LdataWriter.writeTo(true, _context, _out);
    }

    public static Map<String,Object> loadFrom(Reader _reader)
    {
        return LdataParser.loadFrom(_reader);
    }

    public static Map<String,Object> loadFrom(File _in)
    {
        return LdataParser.loadFrom(_in);
    }
    
    public static Map<String,Object> loadFrom(Reader _reader, boolean _bl)
    {
        return LdataParser.loadFrom(_reader, _bl);
    }

    public static Map<String,Object> loadFrom(Reader _reader, boolean _bl, boolean _dk)
    {
        return LdataParser.loadFrom(_reader, _bl, _dk);
    }
    
    public static Map<String,Object> loadFrom(File _in, boolean _bl)
    {
        return LdataParser.loadFrom(_in, _bl);
    }
    public static Map<String,Object> loadFrom(File _in, boolean _bl, boolean _dk)
    {
        return LdataParser.loadFrom(_in, _bl, _dk);
    }

    @SneakyThrows
    public static void writeTo(Map<String, Object> _data, File _file, Charset _cs)
    {
        writeTo(_data, new FileWriter(_file, _cs));
    }
}
