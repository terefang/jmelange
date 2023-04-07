package com.github.terefang.jmelange.commons.match.basic;

import com.github.terefang.jmelange.commons.match.Filter;

import java.util.List;
import java.util.Vector;

public class AndFilter extends AbstractFilter
{
    List<Filter> _filter = new Vector<>();

    public static AndFilter from(Filter...args)
    {
        AndFilter _and = new AndFilter();
        if(args==null || args.length==0) return _and;
        for(Filter _f : args)
            _and._filter.add(_f);
        return _and;
    }

    public void add(Filter arg)
    {
        this._filter.add(arg);
    }

    @Override
    public boolean doMatch(Object object)
    {
        for(Filter _f : this._filter)
        {
            try
            {
                if(_f.match(object)==false)
                {
                    return false;
                }
            }
            catch (Exception _xe)
            {
                return false;
            }
        }
        return true;
    }
}
