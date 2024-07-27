package com.github.terefang.jmelange.data;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;

public interface ObjectDataReader extends AbstractDataExchange
{
    Map<String, Object> readObject(Reader _file);
    Map<String, Object> readObject(File _file);
    Map<String, Object> readObject(InputStream _file);
    Map<String, Object> readObject(File _file, Charset _cs);
    Map<String, Object> readObject(InputStream _file, Charset _cs);

    public static ObjectDataReader findByName(String _name) {
        return DataReadWriteFactory.findByName(_name, ObjectDataReader.class);
    }

    public static ObjectDataReader findByExtension(String _name) {
        return DataReadWriteFactory.findByExtension(_name, ObjectDataReader.class);
    }

}
