package com.github.terefang.jmelange.commons.match.basic;

import com.github.terefang.jmelange.commons.match.Filter;
import com.github.terefang.jmelange.commons.match.MatchContext;
import com.github.terefang.jmelange.commons.util.StringUtil;

import java.util.Map;

public class LikeFilter extends AbstractFilter
{
    private IVariable key;
    private IVariable val;
    boolean insensitive;

    public static LikeFilter from(IVariable k, IVariable v)
    {
        LikeFilter _like = new LikeFilter();
        _like.key = k;
        _like.val = v;
        return _like;
    }

    public static LikeFilter from(IVariable k, IVariable v, boolean is)
    {
        LikeFilter _like = new LikeFilter();
        _like.key = k;
        _like.val = v;
        _like.insensitive = is;
        return _like;
    }

    @Override
    public boolean doMatch(Object object)
    {
        if("*".equalsIgnoreCase(this.val.asString(object)))
        {
            return this.key.asString(object)!=null;
        }
        else if(insensitive)
        {
            return StringUtil.wcmatch(this.val.asString(object).toLowerCase(), this.key.asString(object).toLowerCase());
        }
        else
        {
            return StringUtil.wcmatch(this.val.asString(object), this.key.asString(object));
        }
    }
}
