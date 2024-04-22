package com.github.terefang.jmelange.script;

import java.util.List;

public interface IContextFactory
{
    String getName();
    List<String> getNames();
    String getExt();
    List<String> getExts();
}
