package com.github.terefang.jmelange.script.expr;

import com.github.terefang.jmelange.script.IExpressionContext;
import com.github.terefang.jmelange.script.IExpressionContextFactory;

import java.util.Arrays;
import java.util.List;

public class QLExpressionFactory extends AbstractExpressionFactory implements IExpressionContextFactory
{
    @Override
    public IExpressionContext newInstance() {
        return new QLExpressionContext();
    }

    @Override
    public String getName() {
        return "ql";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("qlang", "ql");
    }

}
