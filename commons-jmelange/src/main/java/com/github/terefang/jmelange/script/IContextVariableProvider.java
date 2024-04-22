package com.github.terefang.jmelange.script;

import java.util.Map;

public interface IContextVariableProvider
{
    Map<String, Object> provide(Map<String, Object> _map);
}
