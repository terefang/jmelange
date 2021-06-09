package com.github.terefang.jmelange.data;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

public interface ObjectDataReader extends AbstractDataExchange
{
    Map<String, Object> readObject(File _file);
    Map<String, Object> readObject(Reader _file);
    Map<String, Object> readObject(InputStream _file);
}
