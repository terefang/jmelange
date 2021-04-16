package com.github.terefang.jmelange.data;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface RowDataWriter extends AbstractDataExchange
{
    void writeRows(List<Map<String, Object>> _data, File _file);
    void writeRows(List<Map<String, Object>> _data, OutputStream _file);
}
