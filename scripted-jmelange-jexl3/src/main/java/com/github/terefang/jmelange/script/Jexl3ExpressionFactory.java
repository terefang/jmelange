package com.github.terefang.jmelange.script;

import com.github.terefang.jmelange.script.expr.AbstractExpressionFactory;

import java.util.Arrays;
import java.util.List;

public class Jexl3ExpressionFactory extends AbstractExpressionFactory implements IExpressionContextFactory
{
    @Override
    public IExpressionContext newInstance() {
        return new Jexl3ExpressionContext();
    }

    @Override
    public String getName() {
        return "jexl3";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("jexl3", "jexl");
    }

}
