package com.github.terefang.jmelange.script;

import java.util.Map;

public interface IExpressionContext extends IContext
{
    public void compile(String _script, String _name);

    public Object run(Map<String,Object> _top);

    default Object eval(String _script, String _name, Map<String,Object> _top)
    {
        compile(_script, _name);
        return run(_top);
    }
}
