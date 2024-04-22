package com.github.terefang.jmelange.data;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public interface RowDataWriter extends AbstractDataExchange
{
    void writeRows(List<Map<String, Object>> _data, File _file);
    void writeRows(List<Map<String, Object>> _data, OutputStream _file);
    void writeRows(List<Map<String, Object>> _data, Writer _file);

    public static RowDataWriter findByName(String _name) {
        return DataReadWriteFactory.findByName(_name, RowDataWriter.class);
    }

    public static RowDataWriter findByExtension(String _name) {
        return DataReadWriteFactory.findByExtension(_name, RowDataWriter.class);
    }

}
