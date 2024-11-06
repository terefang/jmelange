package com.github.terefang.jmelange.commons.match.basic;

import com.github.terefang.jmelange.commons.util.StringUtil;

public class ContainsFilter extends AbstractFilter
{
    private IVariable key;
    private IVariable val;
    boolean insensitive;

    public static ContainsFilter from(IVariable k, IVariable v)
    {
        ContainsFilter _like = new ContainsFilter();
        _like.key = k;
        _like.val = v;
        return _like;
    }

    public static ContainsFilter from(IVariable k, IVariable v, boolean is)
    {
        ContainsFilter _like = new ContainsFilter();
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
            return StringUtil.containsIgnoreCase(this.key.asString(object), this.val.asString(object));
        }
        else
        {
            return StringUtil.contains(this.key.asString(object), this.val.asString(object));
        }
    }
}
