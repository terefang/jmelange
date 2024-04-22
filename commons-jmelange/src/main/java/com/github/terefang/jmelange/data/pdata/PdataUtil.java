package com.github.terefang.jmelange.data.pdata;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

public class PdataUtil
{
	public static void writeTo(Map _context, Writer _writer)
	{
		PdataWriter.writeTo(false, _context, _writer, true);
	}

	public static void writeTo(Map _context, File _out)
	{
		PdataWriter.writeTo(false, _context, _out);
	}

	public static void writeToAlt(Map _context, Writer _writer)
	{
		PdataWriter.writeTo(true, _context, _writer, true);
	}

	public static void writeToAlt(Map _context, File _out)
	{
		PdataWriter.writeTo(true, _context, _out);
	}

	public static Map<String,Object> loadFrom(Reader _reader)
	{
		return PdataParser.loadFrom(_reader);
	}

	public static Map<String,Object> loadFrom(File _in)
	{
		return PdataParser.loadFrom(_in);
	}

	public static Map<String,Object> loadFrom(Reader _reader, boolean _bl)
	{
		return PdataParser.loadFrom(_reader, _bl);
	}

	public static Map<String,Object> loadFrom(File _in, boolean _bl)
	{
		return PdataParser.loadFrom(_in, _bl);
	}
}