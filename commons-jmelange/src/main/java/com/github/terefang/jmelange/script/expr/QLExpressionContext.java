package com.github.terefang.jmelange.script.expr;

import com.github.terefang.jmelange.commons.match.QLFilter;
import com.github.terefang.jmelange.script.AbstractExpression;
import com.github.terefang.jmelange.script.IExpressionContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class QLExpressionContext extends AbstractExpression implements IExpressionContext
{
    private QLFilter expression;

    @Override
    public void compile(String _script, String _name)
    {
        this.expression = QLFilter.parse(_script);
    }

    @Override
    public Object run(Map<String, Object> _top) {
        try
        {
            Object ret = this.expression.match(this.provide(_top));

            return ret;
        }
        catch(Exception xe)
        {
            log.warn("error in expression ", xe);
        }
        return null;
    }
}
