package com.github.terefang.jmelange.script;

import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
public class BasicVariableProvider extends AbstractVariableProvider implements IContextVariableProvider
{
    public static IContextVariableProvider create(Map<String, Object> _m)
    {
        BasicVariableProvider _ret = new BasicVariableProvider();
        _ret.variables = _m;
        return _ret;
    }

    public static BasicVariableProvider create()
    {
        BasicVariableProvider _ret = new BasicVariableProvider();
        _ret.variables = new HashMap<>();
        return _ret;
    }

    Map<String, Object> variables;

    @Override
    public Map<String, Object> provide(Map<String, Object> _map)
    {
        Map<String, Object> _tmp = new HashMap<>(_map);
        _tmp.putAll(this.variables);
        return Collections.unmodifiableMap(_tmp);
    }

    @Override
    public void setAll(Map<String, Object> _vars) {
        this.variables.putAll(_vars);
    }

    @Override
    public void set(String _key, Object _val) {
        this.variables.put(_key, _val);
    }

    @Override
    public void unset(String _key) {
        this.variables.remove(_key);
    }

    @Override
    public void unsetAll() {
        this.variables.clear();
    }
}
