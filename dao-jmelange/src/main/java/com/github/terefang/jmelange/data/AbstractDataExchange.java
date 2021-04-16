package com.github.terefang.jmelange.data;

import java.util.List;

public interface AbstractDataExchange
{
    public String getName();

    List<String> getNames();

    default boolean matchesName(String _name)
    {
        for(String _test : getNames())
        {
            if(_test.equalsIgnoreCase(_name)) return true;
        }
        return false;
    }

    List<String> getExts();

    default boolean matchesExtension(String _file)
    {
        for(String _test : getExts())
        {
            if(_file.endsWith(_test)) return true;
        }
        return false;
    }
}
