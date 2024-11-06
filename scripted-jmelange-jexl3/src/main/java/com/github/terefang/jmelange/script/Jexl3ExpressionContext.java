package com.github.terefang.jmelange.script;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.*;
import java.util.Map;

@Slf4j
public class Jexl3ExpressionContext extends AbstractExpression implements IExpressionContext
{
    private JexlEngine engine;
    private JexlInfo info;
    private JexlExpression expression;

    @Override
    public void compile(String _script, String _name)
    {
        this.engine = Jexl3EngineFactory.newInstance(null);
        this.info = this.engine.createInfo(_name, 1,0);
        this.expression = this.engine.createExpression(this.info, _script);
    }

    @Override
    public Object run(Map<String, Object> _top) {
        try
        {
            Jexl3NsContext l_bind = new Jexl3NsContext();
            for(Map.Entry<String,Object> e : this.provide(_top).entrySet())
            {
                l_bind.set(e.getKey(), e.getValue());
            }

            Object ret = this.expression.evaluate(l_bind);

            return ret;
        }
        catch(Exception xe)
        {
            log.warn("error in expression ", xe);
        }
        return null;
    }
}
