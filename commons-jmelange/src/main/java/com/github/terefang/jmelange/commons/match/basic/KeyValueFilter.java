package com.github.terefang.jmelange.commons.match.basic;

public class KeyValueFilter extends AbstractFilter {
    private final IVariable key;
    private final IVariable val;

    public KeyValueFilter(IVariable k, IVariable v)
    {
        this.key = k;
        this.val = v;
    }

    public boolean doMatch(Object object)
    {
        if("*".equalsIgnoreCase(this.val.asString(object)))
        {
            return this.key.asString(object)!=null;
        }
        else
        {
            return this.key.asString(object).equalsIgnoreCase(this.val.asString(object));
        }
    }
}
