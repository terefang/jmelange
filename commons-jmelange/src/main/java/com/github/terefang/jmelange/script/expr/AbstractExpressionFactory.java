package com.github.terefang.jmelange.script.expr;

import com.github.terefang.jmelange.script.IContextFactory;

import java.util.Collections;
import java.util.List;

public abstract class AbstractExpressionFactory implements IContextFactory
{
    @Override
    public abstract String getName();

    @Override
    public abstract List<String> getNames();

    @Override
    public String getExt() {
        return "";
    }

    @Override
    public List<String> getExts() {
        return Collections.emptyList();
    }
}
