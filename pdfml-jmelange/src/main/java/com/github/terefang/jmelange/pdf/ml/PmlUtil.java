package com.github.terefang.jmelange.pdf.ml;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.commons.loader.FileResourceLoader;
import com.github.terefang.jmelange.commons.loader.ResourceLoader;
import com.github.terefang.jmelange.commons.loader.ZipResourceLoader;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PmlUtil
{

    public static String formatSimple(final String template, final Map<String, Object> _scontext)
    {
        Properties table = new Properties();
        for(Map.Entry<String, Object> _entry : _scontext.entrySet())
        {
            table.setProperty(_entry.getKey(), _entry.getValue().toString());
        }
        return formatSimple(template, table);
    }

    public static String formatSimple(final String template, final Map<String, Object> _scontext, String startPrefix, String endSuffix)
    {
        Properties table = new Properties();
        for(Map.Entry<String, Object> _entry : _scontext.entrySet())
        {
            table.setProperty(_entry.getKey(), _entry.getValue().toString());
        }
        return formatSimple(template, table, startPrefix, endSuffix);
    }

    public static String formatSimple(final String template, final Properties table)
    {
        return formatSimple(template, table, "{{", "}}");
    }

    public static String formatSimple(final String template, final Properties table, String startPrefix, String endSuffix)
    {
        if(template==null)
            return "";

        StringBuilder sb = new StringBuilder();
        int offset = 0;
        int found = 0;

        while((found = template.indexOf(startPrefix, offset))>=0)
        {
            if(found>offset)
            {
                sb.append(template.substring(offset,found));
            }
            offset = template.indexOf(endSuffix, found+startPrefix.length());
            String tag = template.substring(found+startPrefix.length(), offset);
            String _repl = table.getProperty(tag, null);
            if(_repl==null)
            {
                _repl = startPrefix+tag+endSuffix;
            }
            sb.append(_repl);
            offset+=endSuffix.length();
        }

        if(offset<template.length())
        {
            sb.append(template.substring(offset));
        }

        return sb.toString();
    }

    public static ResourceLoader sourceToLoader(String _src, File basedir, File parentDir, Map<String, ZipFile> ZIP_MOUNTS, List<File> DIR_MOUNTS)
    {
        return sourceToLoader(_src, null, basedir, parentDir, ZIP_MOUNTS, DIR_MOUNTS);
    }

    public static ResourceLoader sourceToLoader(String _src, String[] _options, File basedir, File parentDir, Map<String, ZipFile> ZIP_MOUNTS, List<File> DIR_MOUNTS)
    {
        ResourceLoader _rl = PDF.loadFrom(_src);
        if(_rl==null)
        {
            int _ofs = _src.indexOf(';');
            if(_ofs>0)
            {
                _options = CommonUtil.split(_src.substring(_ofs+1), ";");
                _src = _src.substring(0, _ofs);
            }

            if(_src.startsWith("./") || _src.startsWith("../"))
            {
                for(File _d : Arrays.asList(parentDir, basedir))
                {
                    File _test = new File(_d, _src);
                    if(_test.exists())
                    {
                        return FileResourceLoader.of(_test, _options);
                    }
                }

                if(_src.startsWith("./")) _src=_src.substring(2);
                if(_src.startsWith("../")) _src=_src.substring(3);
                for(ZipFile _zip : ZIP_MOUNTS.values())
                {
                    ZipEntry _zentry = _zip.getEntry(_src);
                    if(_zentry!=null)
                    {
                        return ZipResourceLoader.of(_zip, _zentry, _options);
                    }
                }

                for(File _dir : DIR_MOUNTS)
                {
                    File _entry = new File(_dir, _src);
                    if(_entry.exists())
                    {
                        return FileResourceLoader.of(_entry, _options);
                    }
                }
            }
            else
            if(_src.startsWith("mnt:") || _src.startsWith("zip:"))
            {
                if(_src.startsWith("mnt:/")) _src=_src.substring(5);
                if(_src.startsWith("mnt:")) _src=_src.substring(4);
                if(_src.startsWith("zip:/")) _src=_src.substring(5);
                if(_src.startsWith("zip:")) _src=_src.substring(4);
                for(ZipFile _zip : ZIP_MOUNTS.values())
                {
                    ZipEntry _zentry = _zip.getEntry(_src);
                    if(_zentry!=null)
                    {
                        return ZipResourceLoader.of(_zip, _zentry, _options);
                    }
                }
                for(File _dir : DIR_MOUNTS)
                {
                    File _entry = new File(_dir, _src);
                    if(_entry.exists())
                    {
                        return FileResourceLoader.of(_entry, _options);
                    }
                }
            }
            throw new IllegalArgumentException(_src);
        }
        return _rl;
    }

    @SneakyThrows
    static File sourceToFile(String _src, File basedir, File parentDir)
    {
        if(_src.startsWith("file:"))
        {
            return new File(new URL(_src).toURI());
        }
        else
        if(_src.startsWith("./") || _src.startsWith("../"))
        {
            for(File _d : Arrays.asList(parentDir, basedir))
            {
                File _test = new File(_d, _src);
                if(_test.exists())
                {
                    return _test;
                }
            }
        }
        else
        if(_src.startsWith("/"))
        {
            return new File(_src);
        }
        return new File(_src);
    }

}
