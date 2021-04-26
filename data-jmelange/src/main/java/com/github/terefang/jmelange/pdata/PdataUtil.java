package com.github.terefang.jmelange.pdata;

import com.github.terefang.jmelange.pdata.PdataParser;
import com.github.terefang.jmelange.pdata.PdataWriter;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

public class PdataUtil
{
    public static void writeTo(Map _context, Writer _writer)
    {
        PdataWriter.writeTo(_context, _writer, true);
    }

    public static void writeTo(Map _context, File _out)
    {
        PdataWriter.writeTo(_context, _out);
    }

    public static Map<String,Object> loadFrom(Reader _reader)
    {
        return PdataParser.loadFrom(_reader);
    }

    public static Map<String,Object> loadFrom(File _in)
    {
        return PdataParser.loadFrom(_in);
    }
}
