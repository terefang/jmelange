package com.github.terefang.jmelange.commons.loader;

import lombok.SneakyThrows;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class LoaderUtil
{
    public static String unCompressedFilename(File _res)
    {
        return unCompressedFilename(_res.getName());
    }

    public static String unCompressedFilename(String _fn)
    {
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
        return getStreamBySuffix(new FileInputStream(_res), _res.getName());
    }

    @SneakyThrows
    public static InputStream getStreamBySuffix(InputStream _stream, String _fn)
    {
        String _ext = FileUtils.getExtension(_fn);
        if("gz".equalsIgnoreCase(_ext))
        {
            //    _stream = new GzipCompressorInputStream(_stream);
        }
        else
        if("zstd".equalsIgnoreCase(_ext) || "zst".equalsIgnoreCase(_ext))
        {
            //    _stream = new ZstdCompressorInputStream(_stream);
        }
        else
        if("xz".equalsIgnoreCase(_ext))
        {
            //    _stream = new XZCompressorInputStream(_stream);
        }
        else
        if("bz2".equalsIgnoreCase(_ext))
        {
            //    _stream = new BZip2CompressorInputStream(_stream);
        }
        return _stream;
    }

}
