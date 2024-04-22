package com.github.terefang.jmelange.commons.match.basic;

import com.github.terefang.jmelange.commons.match.MatchContext;
import com.github.terefang.jmelange.commons.util.NumberUtil;

import java.util.Map;

public class EqFilter extends AbstractFilter {
    private final IVariable key;
    private final IVariable val;

    public EqFilter(IVariable k, IVariable v)
    {
        this.key = k;
        this.val = v;
    }

    public boolean doMatch(Object object)
    {
        int _r = this.key.compareTo(this.val, object);
        if(_r==-2) return false;
        return _r == 0;
    }
}
