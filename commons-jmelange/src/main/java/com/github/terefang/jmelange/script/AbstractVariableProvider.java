package com.github.terefang.jmelange.script;

import lombok.Data;

import java.util.Map;

@Data
public abstract class AbstractVariableProvider implements IContextVariableProvider
{
    @Override
    public abstract Map<String, Object> provide(Map<String, Object> _map);
    public abstract void setAll(Map<String,Object> _vars);
    public abstract void set(String _key, Object _val);
    public abstract void unset(String _key);
    public abstract void unsetAll();
}
