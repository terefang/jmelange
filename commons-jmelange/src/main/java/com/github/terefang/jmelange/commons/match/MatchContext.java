package com.github.terefang.jmelange.commons.match;

import com.github.terefang.jmelange.commons.match.basic.FunctionCallFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MatchContext
{
    public static final MatchContext create()
    {
        return new MatchContext();
    }

    Map<String,Object> _map = new HashMap<>();

    public int size()
    {
        return _map.size();
    }

    public boolean isEmpty() {
        return _map.isEmpty();
    }

    public boolean hasKey(String key)
    {
        return _map.containsKey(key);
    }

    public Object get(String key)
    {
        return _map.get(key);
    }

    public String getString(String key)
    {
        return _map.containsKey(key) ? _map.get(key).toString() : null;
    }

    public Object set(String key, String value)
    {
        return _map.put(key, value);
    }

    public Object set(String key, Object value)
    {
        return _map.put(key, value);
    }

    public Object register(String key, FunctionCallFilter.FunctionCall value)
    {
        return _map.put("_fn_"+key.toLowerCase(), value);
    }

    public Object remove(String key)
    {
        return _map.remove(key);
    }

    public void setAll(Map<? extends String, ? extends String> _m)
    {
        _map.putAll(_m);
    }

    public void clear()
    {
        _map.clear();
    }

    public Set<String> keys()
    {
        return _map.keySet();
    }

    public Set<Map.Entry<String, Object>> entries()
    {
        return _map.entrySet();
    }
}
