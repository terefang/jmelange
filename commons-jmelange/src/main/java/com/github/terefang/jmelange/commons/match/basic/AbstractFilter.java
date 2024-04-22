package com.github.terefang.jmelange.commons.match.basic;

import com.github.terefang.jmelange.commons.match.Filter;
import com.github.terefang.jmelange.commons.match.MatchContext;

import java.util.List;
import java.util.Map;

public abstract class AbstractFilter implements Filter
{
    public boolean invert;

    public boolean isInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    @Override
    public boolean match(Object object) {
        return isInvert() ? !doMatch(object) : doMatch(object);
    }

    public abstract boolean doMatch(Object object);

    public static Object resolveParam(Object object, String _key)
    {
        if(object instanceof MatchContext)
        {
            return ((MatchContext) object).get(_key);
        }
        else
        if(object instanceof Map)
        {
            return ((Map)object).get(_key);
        }
        else
        if(object != null)
        {
            return object;
        }
        return null;
    }

    public static IVariable resolveParam(Object _context, String _fn, List<IVariable> _args)
    {
        _fn = _fn.toLowerCase();
        // look for __functions
        Object _obj = resolveParam(_context, "__functions");
        if(_obj!=null)
        {
            _obj = resolveParam(_obj, _fn);
            if(_obj!=null && _obj instanceof FunctionCallFilter.FunctionCall)
            {
                return ((FunctionCallFilter.FunctionCall)_obj).call(_fn, _args, _context);
            }
        }

        _obj = resolveParam(_context, "_fn_"+_fn);
        if(_obj!=null && _obj instanceof FunctionCallFilter.FunctionCall)
        {
            return ((FunctionCallFilter.FunctionCall)_obj).call(_fn, _args, _context);
        }

        return IVariable.make(false);
    }
}
