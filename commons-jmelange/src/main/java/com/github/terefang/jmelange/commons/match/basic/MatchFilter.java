package com.github.terefang.jmelange.commons.match.basic;

import com.github.terefang.jmelange.commons.util.StringUtil;

public class MatchFilter extends AbstractFilter
{
    private IVariable key;
    private IVariable val;
    boolean insensitive;

    public static MatchFilter from(IVariable k, IVariable v)
    {
        MatchFilter _like = new MatchFilter();
        _like.key = k;
        _like.val = v;
        return _like;
    }

    public static MatchFilter from(IVariable k, IVariable v, boolean is)
    {
        MatchFilter _like = new MatchFilter();
        _like.key = k;
        _like.val = v;
        _like.insensitive = is;
        return _like;
    }

    @Override
    public boolean doMatch(Object object)
    {
        if(object == null) return false;

        if("*".equalsIgnoreCase(this.val.asString(object)))
        {
            return this.key.asString(object)!=null;
        }
        else if(insensitive)
        {
            return StringUtil.irmatch(this.key.asString(object), this.val.asString(object));
        }
        else
        {
            return StringUtil.rmatch(this.key.asString(object), this.val.asString(object));
        }
    }
}
