package com.github.terefang.jmelange.commons.match.basic;

import java.util.List;

public class FunctionCallFilter extends AbstractFilter
{
    String _func;
    List<IVariable> _args;

    public static FunctionCallFilter from(String _fn, List<IVariable> _args)
    {
        FunctionCallFilter _fcf = new FunctionCallFilter();
        _fcf._func = _fn.toLowerCase();
        _fcf._args = _args;
        return _fcf;
    }

    @Override
    public boolean doMatch(Object _context)
    {
        if("IPMATCH".equalsIgnoreCase(_func))
        {
            return ipMatch(this._func, this._args, _context);
        }
        return resolveParam(_context, this._func, this._args).asBoolean(_context);
    }

    public static boolean ipMatch(String _func, List<IVariable> _args, Object _context)
    {
        return false;
    }

    public interface FunctionCall
    {
        IVariable call(String _fn, List<IVariable> _args, Object _context);
    }
}
