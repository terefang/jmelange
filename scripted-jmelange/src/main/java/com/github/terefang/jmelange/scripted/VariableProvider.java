package com.github.terefang.jmelange.scripted;

import java.util.Map;

public interface VariableProvider
{
    Map<String, Object> provide(Map<String, Object> _map);
}
