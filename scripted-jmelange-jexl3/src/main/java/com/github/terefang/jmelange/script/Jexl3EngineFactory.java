package com.github.terefang.jmelange.script;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.annotations.NoJexl;

import java.util.Collections;
import java.util.Map;

public class Jexl3EngineFactory
{
    public static JexlEngine newInstance(Map<String, Object> _ns)
    {
        if(_ns==null || _ns.size()==0)
        {
            _ns = Collections.singletonMap("_", Jexl3EngineHelper.class);
        }

        JexlEngine _engine = new JexlBuilder()
                .cache(512)
                .strict(true)
                .silent(false)
                .namespaces(_ns)
                .create();

        return _engine;
    }

    public static class Jexl3EngineHelper implements Jexl3NsContext.Jexl3NsContextFactory
    {
        @Override
        @NoJexl
        public String getName()
        {
            return "_";
        }

        @Override
        @NoJexl
        public Object newInstance()
        {
            return this;
        }
    }
}
