package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.commons.match.Filter;
import com.github.terefang.jmelange.commons.match.Matcher;

import java.util.Map;

public class MatchUtil
{
    public static boolean match(Map<String,String> _map, Filter _filter)
    {
        return _filter.match(_map);
    }

    public static Filter qlFilter(String _ql)
    {
        return Matcher.parseQL(_ql);
    }

    public static Filter simpleFilter(String _ql)
    {
        return Matcher.parse(_ql);
    }

    public static Filter kvFilter(String _key, String _val)
    {
        return Matcher.keyValueFilter(_key, _val);
    }

    public static Filter likeFilter(String _key, String _val)
    {
        return Matcher.likeFilter(_key, _val);
    }

    public static Filter rxFilter(String _key, String _val)
    {
        return Matcher.matchFilter(_key, _val);
    }

    public static boolean qlMatch(Map<String,String> _map, String _filter)
    {
        return qlFilter(_filter).match(_map);
    }

    public static boolean simpleMatch(Map<String,String> _map, String _filter)
    {
        return simpleFilter(_filter).match(_map);
    }

    public static boolean kvMatch(Map<String,String> _map, String _key, String _val)
    {
        return kvFilter(_key, _val).match(_map);
    }

    public static boolean likeMatch(Map<String,String> _map, String _key, String _val)
    {
        return likeFilter(_key, _val).match(_map);
    }

    public static boolean rxMatch(Map<String,String> _map, String _key, String _val)
    {
        return rxFilter(_key, _val).match(_map);
    }

}
