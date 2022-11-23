package com.github.terefang.jmelange.data;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.zip.ByFileArchiver;
import com.github.terefang.jmelange.data.util.CsvUtil;
import lombok.SneakyThrows;
import net.jpountz.lz4.LZ4FrameInputStream;
import net.jpountz.lz4.LZ4FrameOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorInputStream;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;

public class DataUtil {
    public static final String[] EXTENSION_LIST = {
            ".props",
            ".properties",
            ".yaml",
            ".yml",
            ".json",
            ".hson",
            ".hcl",
            ".hjson",
            ".ini",
            ".pdx",
            ".pdata",
            ".sqlite.csv",
            ".list",
            ".scsv",
            ".csv",
            ".tsv",
            ".txt" };
    public static final String EXTENSIONS = CommonUtil.join(EXTENSION_LIST," ");

    public static Map<String, Object> loadContextFrom(String _file)
    {
        return loadContextFrom(new File(_file));
    }

    public static List<Map<String, Object>> loadRowsFrom(String _file)
    {
        return loadRowsFrom(new File(_file));
    }

    @SneakyThrows
    public static Map<String, Object> loadContextFrom(File _file)
    {
        Map<String, Object> _ret = new HashMap<>();
        InputStream _fh = getStreamBySuffix(_file);
        String _fn = unCompressedFilename(_file);

        if(_fn.endsWith(".props")
                || _fn.endsWith(".properties"))
        {
            _ret.putAll(loadContextFromProperties(_fh));
        }
        else
        if(_fn.endsWith(".yaml")
                || _fn.endsWith(".yml"))
        {
            _ret.putAll(loadContextFromYaml(_fh));
        }
        else
        if(_fn.endsWith(".json")
                || _fn.endsWith(".hson")
                || _fn.endsWith(".hjson"))
        {
            _ret.putAll(loadContextFromHjson(_fh));
        }
        else
        if(_fn.endsWith(".hcl"))
        {
            _ret.putAll(loadContextFromHcl(_fh));
        }
        else
        if(_fn.endsWith(".ini"))
        {
            _ret.putAll(loadContextFromIni(_fh));
        }
        else
        if(_fn.endsWith(".pdx")
                || _fn.endsWith(".pdata") )
        {
            _ret.putAll(loadContextFromPdx(_fh));
        }
        else
        if(_fn.endsWith(".sqlite.csv"))
        {
            _ret.putAll(loadContextFromSqliteCsv(_fh));
        }
        else
        if(_fn.endsWith(".list"))
        {
            _ret.putAll(loadContextFromSqliteList(_fh));
        }
        else
        if(_fn.endsWith(".scsv"))
        {
            _ret.putAll(loadContextFromScsv(_fh));
        }
        else
        if(_fn.endsWith(".csv"))
        {
            _ret.putAll(loadContextFromCsv(_fh));
        }
        else
        if(_fn.endsWith(".tsv"))
        {
            _ret.putAll(loadContextFromTsv(_fh));
        }
        else
        if(_fn.endsWith(".txt"))
        {
            _ret.putAll(loadContextFromTxt(_fh));
        }
        else
        {
            throw new IllegalArgumentException(MessageFormat.format("Context file '{0}' is unknown format", _file.getName()));
        }
        IOUtil.close(_fh);
        return _ret;
    }

    public static String unCompressedFilename(File _res)
    {
        String _fn = _res.getName();
        String _ext = FileUtils.getExtension(_fn);
        if("gz".equalsIgnoreCase(_ext))
        {
            return _fn.substring(0, _fn.length()-3);
        }
        else
        if("zst".equalsIgnoreCase(_ext))
        {
            return _fn.substring(0, _fn.length()-4);
        }
        else
        if("zstd".equalsIgnoreCase(_ext))
        {
            return _fn.substring(0, _fn.length()-5);
        }
        else
        if("lz4".equalsIgnoreCase(_ext))
        {
            return _fn.substring(0, _fn.length()-4);
        }
        else
        if("xz".equalsIgnoreCase(_ext))
        {
            return _fn.substring(0, _fn.length()-3);
        }
        else
        if("bz2".equalsIgnoreCase(_ext))
        {
            return _fn.substring(0, _fn.length()-4);
        }
        return _fn;
    }

    @SneakyThrows
    public static InputStream getStreamBySuffix(File _res)
    {
        InputStream _stream = new FileInputStream(_res);
        String _ext = FileUtils.getExtension(_res.getName());
        if("gz".equalsIgnoreCase(_ext))
        {
            _stream = new GzipCompressorInputStream(_stream);
        }
        else
        if("zstd".equalsIgnoreCase(_ext) || "zst".equalsIgnoreCase(_ext))
        {
            _stream = new ZstdCompressorInputStream(_stream);
        }
        else
        if("lz4".equalsIgnoreCase(_ext))
        {
            _stream = new LZ4FrameInputStream(_stream);
        }
        else
        if("xz".equalsIgnoreCase(_ext))
        {
            _stream = new XZCompressorInputStream(_stream);
        }
        else
        if("bz2".equalsIgnoreCase(_ext))
        {
            _stream = new BZip2CompressorInputStream(_stream);
        }
        return _stream;
    }

    @SneakyThrows
    public static OutputStream getOutputStreamBySuffix(File _res)
    {
        OutputStream _stream = new FileOutputStream(_res);
        String _ext = FileUtils.getExtension(_res.getName());
        if("gz".equalsIgnoreCase(_ext))
        {
            _stream = new GzipCompressorOutputStream(_stream);
        }
        else
        if("zstd".equalsIgnoreCase(_ext) || "zst".equalsIgnoreCase(_ext))
        {
            _stream = new ZstdCompressorOutputStream(_stream);
        }
        else
        if("lz4".equalsIgnoreCase(_ext))
        {
            _stream = new LZ4FrameOutputStream(_stream);
        }
        else
        if("xz".equalsIgnoreCase(_ext))
        {
            _stream = new XZCompressorOutputStream(_stream);
        }
        else
        if("bz2".equalsIgnoreCase(_ext))
        {
            _stream = new BZip2CompressorOutputStream(_stream);
        }
        return _stream;
    }

    @SneakyThrows
    public static List<Map<String, Object>> loadRowsFrom(File _file)
    {
        List<Map<String, Object>> _ret = new Vector<>();
        InputStream _fh = getStreamBySuffix(_file);
        String _fn = unCompressedFilename(_file);

        if(_fn.endsWith(".json")
                || _fn.endsWith(".hson")
                || _fn.endsWith(".hjson"))
        {
            _ret.addAll(loadRowsFromType("hson", _fh));
        }
        else
        if(_fn.endsWith(".jsonl")
                || _fn.endsWith(".jpl"))
        {
            _ret.addAll(loadRowsFromJsonPerLine(_fh));
        }
        else
        if(_fn.endsWith(".scsv"))
        {
            _ret.addAll(loadRowsFromType("scsv", _fh));
        }
        else
        if(_fn.endsWith(".csv"))
        {
            _ret.addAll(loadRowsFromType("csv", _fh));
        }
        else
        if(_fn.endsWith(".tsv"))
        {
            _ret.addAll(loadRowsFromType("tsv", _fh));
        }
        else
        {
            throw new IllegalArgumentException(MessageFormat.format("Rows file '{0}' is unknown format", _file.getName()));
        }
        IOUtil.close(_fh);
        return _ret;
    }

    public static Map<String,Object> loadContextFromIni(InputStream _fh)
    {
        return loadContextFromType("ini", _fh);
    }

    public static Map<String,Object> loadContextFromType(String _type, InputStream _fh)
    {
        try
        {
            ObjectDataReader _rd = DataReadWriteFactory.findByName(_type, ObjectDataReader.class);
            return _rd.readObject(_fh);
        }
        finally {
            CommonUtil.close(_fh);
        }
    }

    public static Map<String,Object> loadContextFromType(String _type, File _fh)
    {
        ObjectDataReader _rd = DataReadWriteFactory.findByName(_type, ObjectDataReader.class);
        return _rd.readObject(_fh);
    }

    public static List<Map<String,Object>> loadRowsFromType(String _type, InputStream _fh)
    {
        try
        {
            RowDataReader _rd = DataReadWriteFactory.findByName(_type, RowDataReader.class);
            return _rd.readRows(_fh);
        }
        finally {
            CommonUtil.close(_fh);
        }
    }

    public static List<Map<String,Object>> loadRowsFromType(String _type, File _fh)
    {
        RowDataReader _rd = DataReadWriteFactory.findByName(_type, RowDataReader.class);
        return _rd.readRows(_fh);
    }


    @SneakyThrows
    public static Map<String,?> loadContextFromTxt(InputStream _source)
    {
        Map<String, Object> _ret = new HashMap<>();
        _ret.put("data", loadFileLines(_source));
        return _ret;
    }

    @SneakyThrows
    public static List<String> loadFileLines(InputStream _source)
    {
        final List<String> _lines = new ArrayList<String>();
        BufferedReader _reader = null;
        try
        {
            _reader = new BufferedReader(new InputStreamReader(_source));

            for (String _line = _reader.readLine(); _line != null; _line = _reader.readLine())
            {
                _line = _line.trim();

                if (!_line.startsWith("#") && (_line.length() != 0))
                {
                    _lines.add(_line);
                }
            }

            _reader.close();
            _reader = null;
        }
        finally
        {
            IOUtil.close(_reader);
        }

        return _lines;
    }

    @SneakyThrows
    public static Map<String, Object> loadContextFromTsv(InputStream _source)
    {
        Map<String, Object> _ret = new HashMap<>();
        _ret.put("data", readFileCsv("TDF", _source, StandardCharsets.UTF_8));
        return _ret;
    }

    @SneakyThrows
    public static Map<String, Object> loadContextFromCsv(InputStream _source)
    {
        Map<String, Object> _ret = new HashMap<>();
        _ret.put("data", readFileCsv("Default", _source, StandardCharsets.UTF_8));
        return _ret;
    }

    @SneakyThrows
    public static Map<String, Object> loadContextFromScsv(InputStream _source)
    {
        Map<String, Object> _ret = new HashMap<>();
        _ret.put("data", readFileCsv("scsv", _source, StandardCharsets.UTF_8));
        return _ret;
    }

    @SneakyThrows
    public static Map<String, Object> loadContextFromSqliteCsv(InputStream _source)
    {
        Map<String, Object> _ret = new HashMap<>();
        _ret.put("data", readFileCsv("sqlite-csv", _source, StandardCharsets.UTF_8));
        return _ret;
    }

    @SneakyThrows
    public static Map<String, Object> loadContextFromSqliteList(InputStream _source)
    {
        Map<String, Object> _ret = new HashMap<>();
        _ret.put("data", readFileCsv("sqlite-list", _source, StandardCharsets.UTF_8));
        return _ret;
    }

    @SneakyThrows
    public static List<Map<String, Object>> readFileCsv(String _infmt, InputStream _in, Charset _cs)
    {
        return CsvUtil.readFileCsv(_infmt, _in, _cs);
    }

    @SneakyThrows
    public static Map<String, Object> loadContextFromPdx(InputStream _source)
    {
        return loadContextFromType("pdata", _source);
    }

    @SneakyThrows
    public static Map<String, Object> loadContextFromPdata(InputStream _source)
    {
        return loadContextFromType("pdata", _source);
    }

    @SneakyThrows
    public static Map<String, Object> fromPdx(String _source)
    {
        return loadContextFromType("pdata", new StringBufferInputStream(_source));
    }

    @SneakyThrows
    public static Map<String, Object> fromPdata(String _source)
    {
        return loadContextFromType("pdata", new StringBufferInputStream(_source));
    }

    @SneakyThrows
    public static Map<String,?> loadContextFromProperties(InputStream _source)
    {
        return loadContextFromType("properties", _source);
    }

    @SneakyThrows
    public static Map<String, Object> loadContextFromYaml(InputStream _source)
    {
        return loadContextFromType("yaml", _source);
    }

    @SneakyThrows
    public static Map<String, Object> fromYaml(String _source)
    {
        return loadContextFromType("yaml", new StringBufferInputStream(_source));
    }

    public static void writeContextAsType(String _type, Map<String,Object> _data, OutputStream _fh)
    {
        try
        {
            ObjectDataWriter _rd = DataReadWriteFactory.findByName(_type, ObjectDataWriter.class);
            _rd.writeObject(_data, _fh);
        }
        finally {
            CommonUtil.close(_fh);
        }
    }

    public static void writeContextAsType(String _type, Map<String,Object> _data, Writer _fh)
    {
        try
        {
            ObjectDataWriter _rd = DataReadWriteFactory.findByName(_type, ObjectDataWriter.class);
            _rd.writeObject(_data, _fh);
        }
        finally {
            CommonUtil.close(_fh);
        }
    }

    public static void writeContextAsType(String _type, Map<String,Object> _data, File _fh)
    {
        ObjectDataWriter _rd = DataReadWriteFactory.findByName(_type, ObjectDataWriter.class);
        _rd.writeObject(_data, _fh);
    }

    public static void writeContextAsType(String _type, Map<String,Object> _data, String _fh)
    {
        writeContextAsType(_type, _data, new File(_fh));
    }

    public static void writeRowsAsType(String _type, List<Map<String,Object>> _data, Writer _fh)
    {
        try
        {
            RowDataWriter _rd = DataReadWriteFactory.findByName(_type, RowDataWriter.class);
            _rd.writeRows(_data, _fh);
        }
        finally {
            CommonUtil.close(_fh);
        }
    }

    public static void writeRowsAsType(String _type, List<Map<String,Object>> _data, OutputStream _fh)
    {
        try
        {
            RowDataWriter _rd = DataReadWriteFactory.findByName(_type, RowDataWriter.class);
            _rd.writeRows(_data, _fh);
        }
        finally {
            CommonUtil.close(_fh);
        }
    }

    public static void writeRowsAsType(String _type, List<Map<String,Object>> _data, File _fh)
    {
        RowDataWriter _rd = DataReadWriteFactory.findByName(_type, RowDataWriter.class);
        _rd.writeRows(_data, _fh);
    }

    @SneakyThrows
    public static void writeAsHson(boolean _json, Writer _out, List<Map<String, Object>> _res)
    {
        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        if(_json)
        {
            writeRowsAsType("json", _res, _baos);
        }
        else
        {
            writeRowsAsType("hson", _res, _baos);
        }
        _out.write(_baos.toString("UTF-8"));
    }

    @SneakyThrows
    public static void writeAsHson(boolean _json, Writer _out, Map<String, Object> _res)
    {
        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        if(_json)
        {
            writeContextAsType("json", _res, _baos);
        }
        else
        {
            writeContextAsType("hson", _res, _baos);
        }
        _out.write(_baos.toString("UTF-8"));
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

    @SneakyThrows
    public static void writeAsPdata(Writer _out, Map<String, Object> _res)
    {
        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        writeContextAsType("pdata", _res, _baos);
        _out.write(_baos.toString("UTF-8"));
    }

    public static String toPdata(Map<String, Object> _res)
    {
        StringWriter _sw = new StringWriter();
        writeAsPdata(_sw, _res);
        _sw.flush();
        return _sw.getBuffer().toString();
    }

    @SneakyThrows
    public static Map<String, Object> loadContextFromHjson(InputStream _source)
    {
        return loadContextFromType("hson", _source);
    }

    @SneakyThrows
    public static Map<String, Object> loadContextFromHcl(InputStream _source)
    {
        return loadContextFromType("hcl", _source);
    }

    @SneakyThrows
    public static Map<String, Object> fromJson(String _source)
    {
        return loadContextFromType("json", new StringBufferInputStream(_source));
    }

    @SneakyThrows
    public static Map<String, Object> fromHjson(String _source)
    {
        return loadContextFromType("hson", new StringBufferInputStream(_source));
    }

    @SneakyThrows
    public static void writeAsJsonPerLine(Writer _out, List<Map<String, Object>> _res)
    {
        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        writeRowsAsType("jsonline", _res, _baos);
        _out.write(_baos.toString("UTF-8"));
    }

    public static String toJsonPerLine(List<Map<String, Object>> _res)
    {
        StringWriter _sw = new StringWriter();
        writeAsJsonPerLine(_sw, _res);
        _sw.flush();
        return _sw.getBuffer().toString();
    }

    @SneakyThrows
    public static List<Map<String, Object>> loadRowsFromJsonPerLine(InputStream _source)
    {
        return loadRowsFromType("jsonline", _source);
    }

    @SneakyThrows
    public static List<Map<String, Object>> fromJsonPerLine(String _source)
    {
        return loadRowsFromType("jsonline", new StringBufferInputStream(_source));
    }

    public static ByRowDataWriter newByRowDataWriter(File _file)
    {
        ByRowDataWriter _wr = ByRowDataWriter.findByExtension(_file.getName());
        _wr.open(_file);
        return _wr;
    }
}
