package com.github.terefang.jmelange.commons.match.basic;

import com.github.terefang.jmelange.commons.util.StringUtil;

public class EndsWithFilter extends AbstractFilter
{
    private IVariable key;
    private IVariable val;
    boolean insensitive;

    public static EndsWithFilter from(IVariable k, IVariable v)
    {
        EndsWithFilter _like = new EndsWithFilter();
        _like.key = k;
        _like.val = v;
        return _like;
    }

    public static EndsWithFilter from(IVariable k, IVariable v, boolean is)
    {
        EndsWithFilter _like = new EndsWithFilter();
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
            return StringUtil.endsWithIgnoreCase(this.key.asString(object), this.val.asString(object));
        }
        else
        {
            return StringUtil.endsWith(this.key.asString(object), this.val.asString(object));
        }
    }
}
