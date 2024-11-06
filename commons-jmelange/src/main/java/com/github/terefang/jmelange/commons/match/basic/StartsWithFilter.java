package com.github.terefang.jmelange.commons.match.basic;

import com.github.terefang.jmelange.commons.util.StringUtil;

public class StartsWithFilter extends AbstractFilter
{
    private IVariable key;
    private IVariable val;
    boolean insensitive;

    public static StartsWithFilter from(IVariable k, IVariable v)
    {
        StartsWithFilter _like = new StartsWithFilter();
        _like.key = k;
        _like.val = v;
        return _like;
    }

    public static StartsWithFilter from(IVariable k, IVariable v, boolean is)
    {
        StartsWithFilter _like = new StartsWithFilter();
        _like.key = k;
        _like.val = v;
        _like.insensitive = is;
        return _like;
    }

    @Override
    public boolean doMatch(Object object)
    {
        if(object == null) return false;

        if(insensitive)
        {
            return StringUtil.startsWithIgnoreCase(this.key.asString(object), this.val.asString(object));
        }
        else
        {
            return StringUtil.startsWith(this.key.asString(object), this.val.asString(object));
        }
    }
}
