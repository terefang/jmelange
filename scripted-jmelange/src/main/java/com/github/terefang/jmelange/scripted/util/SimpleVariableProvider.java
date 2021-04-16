package com.github.terefang.jmelange.scripted.util;

import com.github.terefang.jmelange.scripted.VariableProvider;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
public class SimpleVariableProvider implements VariableProvider
{
    public static SimpleVariableProvider create(Map<String, Object> _m)
    {
        SimpleVariableProvider _ret = new SimpleVariableProvider();
        _ret.variables = _m;
        return _ret;
    }

    public static SimpleVariableProvider create()
    {
        SimpleVariableProvider _ret = new SimpleVariableProvider();
        _ret.variables = new HashMap<>();
        return _ret;
    }

    Map<String, Object> variables;

    public SimpleVariableProvider add(String _k, Object _v)
    {
        this.variables.put(_k, _v);
        return this;
    }

    @Override
    public Map<String, Object> provide(Map<String, Object> _map) {
        return Collections.unmodifiableMap(this.variables);
    }
}
