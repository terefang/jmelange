package com.github.terefang.jmelange.ldata;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

public class LdataUtil
{
    public static void writeTo(Map _context, Writer _writer)
    {
        LdataWriter.writeTo(false, _context, _writer, true);
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

    public static Map<String,Object> loadFrom(File _in, boolean _bl)
    {
        return LdataParser.loadFrom(_in, _bl);
    }
}
