package com.github.terefang.jmelange.data;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public interface RowDataReader extends AbstractDataExchange
{
    List<Map<String, Object>> readRows(File _file);
    List<Map<String, Object>> readRows(InputStream _file);
    List<Map<String, Object>> readRows(Reader _file);


    public static RowDataReader findByName(String _name) {
        return DataReadWriteFactory.findByName(_name, RowDataReader.class);
    }

    public static RowDataReader findByExtension(String _name) {
        return DataReadWriteFactory.findByExtension(_name, RowDataReader.class);
    }
}
