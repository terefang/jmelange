package com.github.terefang.jmelange.data;

import com.github.terefang.jmelange.commons.util.ListMapUtil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface ByRowDataReader extends AbstractDataExchange
{
    public void open(File _out);

    public void open(InputStream _in);

    public void close();

    public void useSheet(String _name);

    public List<String> getHeader();

    public List readRowAsList();

    public List readRowAsList(List<String> _cols);

    public Map<String,?> readRowAsMap();

    public Map<String,?> readRowAsMap(List<String> _cols);

    public static ByRowDataReader findByName(String _name) {
        return DataReadWriteFactory.findByName(_name, ByRowDataReader.class);
    }

    public static ByRowDataReader findByExtension(String _name) {
        return DataReadWriteFactory.findByExtension(_name, ByRowDataReader.class);
    }
}
