package com.github.terefang.jmelange.preproc;

import lombok.SneakyThrows;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;

import java.io.*;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Properties;

public class PreProcUtil {
    @SneakyThrows
    public static Reader resolveIncludes(boolean _prefixAsComment, Properties _props, Reader _reader, File _basedir)
    {
        return resolveIncludes(_prefixAsComment, _props, _reader, _basedir, false);
    }

    @SneakyThrows
    public static Reader resolveIncludes(boolean _prefixAsComment, Properties _props, Reader _reader, File _basedir, boolean _breakCircuits)
    {
        HashSet<String> _inc = new HashSet<>();

        ArrayDeque<BufferedReader> _queue = new ArrayDeque<>();
        _queue.add(new BufferedReader(_reader));
        ArrayDeque<File> _dirs = new ArrayDeque<>();
        _dirs.add(_basedir);

        StringBuilder _sb = new StringBuilder();

        while(_queue.size()>0)
        {
            BufferedReader _lr = _queue.poll();
            File _bd = _dirs.poll();

            String _line = "";
            while((_line = _lr.readLine()) != null)
            {
                if(_line.startsWith("%!include "))
                {
                    File _next = new File(_bd, _line.substring(10).trim());
                    if(_breakCircuits && _inc.contains(_next.getName()))
                    {
                        continue;
                    }
                    _queue.push(_lr);
                    _dirs.push(_bd);
                    _bd = _next.getParentFile();
                    _lr = new BufferedReader(new FileReader(_next));
                    _inc.add(_next.getName());
                }
                else
                if(_line.startsWith("%!require "))
                {
                    File _next = new File(_bd, _line.substring(10).trim());
                    if(_inc.contains(_next.getName()))
                    {
                        continue;
                    }
                    _queue.push(_lr);
                    _dirs.push(_bd);
                    _bd = _next.getParentFile();
                    _lr = new BufferedReader(new FileReader(_next));
                    _inc.add(_next.getName());
                }
                else
                if(_line.startsWith("%!class "))
                {
                    _props.setProperty("class", _line.substring(8).trim());
                }
                else
                if(_line.startsWith("%!set "))
                {
                    String[] _parts = StringUtils.split(_line, " ", 3);
                    if(_parts.length==3)
                    {
                        _props.setProperty(_parts[1], _parts[2].trim());
                    }
                }
                else
                if(_prefixAsComment && _line.startsWith("%!"))
                {
                    // IGNORE
                }
                else
                {
                    _sb.append(_line);
                    _sb.append('\n');
                }
            }

            IOUtil.close(_lr);
        }
        return new StringReader(_sb.toString());
    }
}
