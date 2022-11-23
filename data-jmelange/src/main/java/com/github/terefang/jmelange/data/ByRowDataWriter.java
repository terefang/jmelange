package com.github.terefang.jmelange.data;

import com.github.terefang.jmelange.commons.util.ListMapUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface ByRowDataWriter extends AbstractDataExchange
{
    public void open(File _out);

    public void open(OutputStream _out);

    public void close();

    public void newSheet(String _name, List<String> _cols, List<String> _colNames, boolean _headerUppercase);

    default void newSheet(String _name, List<String> _cols, List<String> _colNames)
    {
        this.newSheet(_name, _cols, _colNames, true);
    }

    default void newSheet(String _name, List<String> _cols)
    {
        this.newSheet(_name, _cols, _cols, true);
    }

    public void write(List _cols);

    public void write(Map _cols);

    default void write(String _c1, String _c2, String _c3, String _c4, String _c5, String _c6, String _c7, String _c8)
    {
        this.write(ListMapUtil.toList(_c1,_c2,_c3,_c4,_c5,_c6,_c7,_c8));
    }

    default void write(Object _c1, Object _c2, Object _c3, Object _c4, Object _c5, Object _c6, Object _c7, Object _c8)
    {
        this.write(ListMapUtil.toList(_c1,_c2,_c3,_c4,_c5,_c6,_c7,_c8));
    }

    default void write(String _c1, String _c2, String _c3, String _c4, String _c5, String _c6, String _c7)
    {
        this.write(ListMapUtil.toList(_c1,_c2,_c3,_c4,_c5,_c6,_c7));
    }

    default void write(Object _c1, Object _c2, Object _c3, Object _c4, Object _c5, Object _c6, Object _c7)
    {
        this.write(ListMapUtil.toList(_c1,_c2,_c3,_c4,_c5,_c6,_c7));
    }

    default void write(String _c1, String _c2, String _c3, String _c4, String _c5, String _c6)
    {
        this.write(ListMapUtil.toList(_c1,_c2,_c3,_c4,_c5,_c6));
    }

    default void write(Object _c1, Object _c2, Object _c3, Object _c4, Object _c5, Object _c6)
    {
        this.write(ListMapUtil.toList(_c1,_c2,_c3,_c4,_c5,_c6));
    }

    default void write(String _c1, String _c2, String _c3, String _c4, String _c5)
    {
        this.write(ListMapUtil.toList(_c1,_c2,_c3,_c4,_c5));
    }

    default void write(Object _c1, Object _c2, Object _c3, Object _c4, Object _c5)
    {
        this.write(ListMapUtil.toList(_c1,_c2,_c3,_c4,_c5));
    }

    default void write(String _c1, String _c2, String _c3, String _c4)
    {
        this.write(ListMapUtil.toList(_c1,_c2,_c3,_c4));
    }

    default void write(Object _c1, Object _c2, Object _c3, Object _c4)
    {
        this.write(ListMapUtil.toList(_c1,_c2,_c3,_c4));
    }

    default void write(String _c1, String _c2, String _c3)
    {
        this.write(ListMapUtil.toList(_c1,_c2,_c3));
    }

    default void write(Object _c1, Object _c2, Object _c3)
    {
        this.write(ListMapUtil.toList(_c1,_c2,_c3));
    }

    default void write(String _c1, String _c2)
    {
        this.write(ListMapUtil.toList(_c1,_c2));
    }

    default void write(Object _c1, Object _c2)
    {
        this.write(ListMapUtil.toList(_c1,_c2));
    }


    default void write(String... _cols)
    {
        this.write(Arrays.asList(_cols));
    }

    default void write(Object... _cols)
    {
        this.write(Arrays.asList(_cols));
    }


    public static ByRowDataWriter findByName(String _name) {
        return DataReadWriteFactory.findByName(_name, ByRowDataWriter.class);
    }

    public static ByRowDataWriter findByExtension(String _name) {
        return DataReadWriteFactory.findByExtension(_name, ByRowDataWriter.class);
    }
}
