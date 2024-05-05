package com.github.terefang.jmelange.script;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.MapContext;

import java.util.Iterator;
import java.util.ServiceLoader;

public class Jexl3NsContext extends MapContext implements JexlContext.NamespaceResolver {

    @Override
    public Object resolveNamespace(String _name)
    {
        Jexl3NsContextFactory _ns = Jexl3NsContextFactory.findByName(_name);
        if(_ns!=null)
        {
            return _ns.newInstance();
        }
        return null;
    }

    public interface Jexl3NsContextFactory
    {
        public static Jexl3NsContextFactory findByName(String _name)
        {
            ServiceLoader<Jexl3NsContextFactory> _sl = ServiceLoader.load(Jexl3NsContextFactory.class);
            Iterator<Jexl3NsContextFactory> _it = _sl.iterator();
            while(_it.hasNext())
            {
                Jexl3NsContextFactory _that =  _it.next();
                if(_name.equalsIgnoreCase(_that.getName()))
                {
                    return _that;
                }
            }
            return null;
        }

        public String getName();

        public Object newInstance();
    }
}
