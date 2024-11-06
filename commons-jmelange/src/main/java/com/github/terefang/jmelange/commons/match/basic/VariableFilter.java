package com.github.terefang.jmelange.commons.match.basic;

public class VariableFilter extends AbstractFilter
{
    public static VariableFilter from(IVariable _v)
    {
        VariableFilter _vf = new VariableFilter();
        _vf._val = _v;
        return _vf;
    }

    IVariable _val;

    @Override
    public boolean doMatch(Object object) {
        return _val.asBoolean(object);
    }
}
