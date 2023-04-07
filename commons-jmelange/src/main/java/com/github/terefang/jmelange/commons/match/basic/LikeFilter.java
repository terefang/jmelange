package com.github.terefang.jmelange.commons.match.basic;

import com.github.terefang.jmelange.commons.match.Filter;
import com.github.terefang.jmelange.commons.match.MatchContext;
import com.github.terefang.jmelange.commons.util.StringUtil;

import java.util.Map;

public class LikeFilter extends AbstractFilter
{
    private String key;
    private String val;

    public static LikeFilter from(String k, String v)
    {
        LikeFilter _like = new LikeFilter();
        _like.key = k.trim();
        _like.val = v.trim().toLowerCase();
        return _like;
    }

    @Override
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
                _test = object.toString().toLowerCase();
            }

            if(_test == null) return false;
            return StringUtil.wcmatch(this.val, _test.toString().toLowerCase());
        }
        return false;
    }
}
