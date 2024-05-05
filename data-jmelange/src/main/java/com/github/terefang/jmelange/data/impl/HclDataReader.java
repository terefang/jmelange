package com.github.terefang.jmelange.data.impl;

import com.bertramlabs.plugins.hcl4j.HCLParser;
import com.github.terefang.jmelange.data.*;
import lombok.SneakyThrows;
import org.codehaus.plexus.util.IOUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HclDataReader implements AbstractDataExchange, ObjectDataReader
{
    static String DATANAME = "hcl";
    static List<String> DATANAMES = Collections.unmodifiableList(Arrays.asList("hcl"));
    static List<String> DATAEXTS = Collections.unmodifiableList(Arrays.asList(".hcl"));

    static HCLParser _p = new HCLParser();

    @Override
    @SneakyThrows
    public Map<String, Object> readObject(File _file)
    {
        return _p.parse(_file, StandardCharsets.UTF_8);
    }

    @Override
    @SneakyThrows
    public Map<String, Object> readObject(Reader _file)
    {
        try {
            return _p.parse(_file);
        }
        finally {
            IOUtil.close(_file);
        }
    }


    @Override
    @SneakyThrows
    public Map<String, Object> readObject(InputStream _file)
    {
        try {
            return _p.parse(_file, StandardCharsets.UTF_8);
        }
        finally {
            IOUtil.close(_file);
        }
    }

    @Override
    public String getName() {
        return DATANAME;
    }

    @Override
    public List<String> getNames() {
        return DATANAMES;
    }

    @Override
    public List<String> getExts() {
        return DATAEXTS;
    }

}
