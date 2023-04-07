package com.github.terefang.jmelange.commons.match.basic;

import com.github.terefang.jmelange.commons.match.MatchContext;

import java.util.Map;

public class KeyValueFilter extends AbstractFilter {
    private final String key;
    private final String val;

    public KeyValueFilter(String k, String v)
    {
        this.key = k.trim();
        this.val = v.trim().toLowerCase();
    }

    public boolean doMatch(Object object)
    {
        if("*".equalsIgnoreCase(this.val))
        {
            if(object instanceof MatchContext)
            {
                return ((MatchContext) object).hasKey(this.key);
            }
            else
            if(object instanceof Map)
            {
                return ((Map)object).containsKey(this.key);
            }
            else
            if(object != null)
            {
                return true;
            }
        }
        else
        {
            Object _test = null;
            if(object instanceof MatchContext)
            {
                _test = ((MatchContext) object).get(this.key);
            }
            else
            if(object instanceof Map)
            {
                _test = ((Map) object).get(this.key);
            }
            else
            if(object instanceof String)
            {
                _test = object.toString();
            }

            if(_test == null) return false;
            return _test.toString().equalsIgnoreCase(this.val);
        }
        return false;
    }
}
