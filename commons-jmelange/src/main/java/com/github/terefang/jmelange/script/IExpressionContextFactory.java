package com.github.terefang.jmelange.script;

import java.util.Iterator;
import java.util.ServiceLoader;

public interface IExpressionContextFactory extends IContextFactory
{
    public IExpressionContext newInstance();

    public static IExpressionContextFactory findByName(String _name)
    {
        ServiceLoader<IExpressionContextFactory> _sl = ServiceLoader.load(IExpressionContextFactory.class);
        Iterator<IExpressionContextFactory>_it = _sl.iterator();
        while(_it.hasNext())
        {
            IExpressionContextFactory _that = _it.next();
            if(_name.equalsIgnoreCase(_that.getName()))
            {
                return _that;
            }
            else
            {
                for(String _n : _that.getNames())
                {
                    if(_name.equalsIgnoreCase(_n))
                    {
                        return _that;
                    }
                }
            }
        }
        return null;
    }

}
