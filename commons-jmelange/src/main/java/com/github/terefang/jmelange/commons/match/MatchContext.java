package com.github.terefang.jmelange.commons.match;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MatchContext
{
    public static final MatchContext create()
    {
        return new MatchContext();
    }

    Map<String,String> _map = new HashMap<>();

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

    public String get(String key)
    {
        return _map.get(key);
    }

    public String set(String key, String value)
    {
        return _map.put(key, value);
    }

    public String remove(String key)
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

    public Set<Map.Entry<String, String>> entries()
    {
        return _map.entrySet();
    }
}
