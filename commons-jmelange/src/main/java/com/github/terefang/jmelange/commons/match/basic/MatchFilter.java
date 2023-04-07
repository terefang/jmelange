package com.github.terefang.jmelange.commons.match.basic;

import com.github.terefang.jmelange.commons.match.MatchContext;
import com.github.terefang.jmelange.commons.util.StringUtil;

import java.util.Map;

public class MatchFilter extends AbstractFilter
{
    private String key;
    private String val;

    public static MatchFilter from(String k, String v)
    {
        MatchFilter _like = new MatchFilter();
        _like.key = k.trim();
        _like.val = v.trim();
        return _like;
    }

    @Override
    public boolean doMatch(Object object)
    {
        if(object == null) return false;

        if("*".equalsIgnoreCase(this.val))
        {
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
            return StringUtil.irmatch(_test.toString(), this.val);
        }
        return false;
    }
}
