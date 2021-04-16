package com.github.terefang.jmelange.data;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface RowDataReader extends AbstractDataExchange
{
    List<Map<String, Object>> readRows(File _file);
    List<Map<String, Object>> readRows(InputStream _file);
}
