package com.github.terefang.jmelange.data;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

public interface ObjectDataWriter extends AbstractDataExchange
{
    void writeObject(Map<String, Object>_data, File _file);
    void writeObject(Map<String, Object>_data, OutputStream _file);
    void writeObject(Map<String, Object>_data, Writer _file);
}
