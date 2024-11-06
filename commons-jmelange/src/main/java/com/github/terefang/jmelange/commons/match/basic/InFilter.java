package com.github.terefang.jmelange.commons.match.basic;

import java.util.List;

public class InFilter extends AbstractFilter
{
    public static InFilter from(IVariable _v, List<IVariable> _l)
    {
        InFilter _inf = new InFilter();
        _inf._val = _v;
        _inf._list = _l;
        return  _inf;
    }

    IVariable _val;
    List<IVariable> _list;
    @Override
    public boolean doMatch(Object object)
    {
        boolean _ret = doMatch_(_val, _list, object);
        if(this.isInvert())
        {
            return !_ret;
        }
        return _ret;
    }

    static double EPSILON = 0.000001d;

    static boolean doMatch_(IVariable _val, List<IVariable> _list, Object object)
    {
        if(_val.type() == IVariable.TYPE_STRING)
        {
            for(IVariable _v : _list)
            {
                if(_val.asString(object).equalsIgnoreCase(_v.asString(object)))
                {
                    return true;
                }
            }
        }
        else
        if(_val.type() == IVariable.TYPE_LONG)
        {
            for(IVariable _v : _list)
            {
                if(_val.asLong(object)==_v.asLong(object))
                {
                    return true;
                }
            }
        }
        else
        if(_val.type() == IVariable.TYPE_CHAR)
        {
            for(IVariable _v : _list)
            {
                if(_val.asChar(object)==_v.asChar(object))
                {
                    return true;
                }
            }
        }
        else
        if(_val.type() == IVariable.TYPE_BOOLEAN)
        {
            for(IVariable _v : _list)
            {
                if(_val.asBoolean(object)==_v.asBoolean(object))
                {
                    return true;
                }
            }
        }
        else
        if(_val.type() == IVariable.TYPE_DOUBLE)
        {
            for(IVariable _v : _list)
            {
                if(Math.abs(_val.asDouble(object)-_v.asDouble(object)) < EPSILON)
                {
                    return true;
                }
            }
        }
        else
        if(_val.type() == IVariable.TYPE_RESOLVED)
        {
            return doMatch_(_val.resolve(object), _list, object);
        }
        return false;
    }
}
