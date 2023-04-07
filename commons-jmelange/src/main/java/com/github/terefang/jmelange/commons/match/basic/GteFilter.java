package com.github.terefang.jmelange.commons.match.basic;

import com.github.terefang.jmelange.commons.match.MatchContext;
import com.github.terefang.jmelange.commons.util.NumberUtil;

import java.util.Map;

public class GteFilter extends AbstractFilter {
    private final String key;
    private final String val;

    public GteFilter(String k, String v)
    {
        this.key = k;
        this.val = v;
    }

    public boolean doMatch(Object object)
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


        try {
            return NumberUtil.checkLong(_test) >= NumberUtil.checkLong(this.val);
        }
        catch(Exception _xe)
        {
            return false;
        }
    }
}
