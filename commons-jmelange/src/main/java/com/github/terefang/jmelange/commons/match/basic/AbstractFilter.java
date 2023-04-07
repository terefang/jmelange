package com.github.terefang.jmelange.commons.match.basic;

import com.github.terefang.jmelange.commons.match.Filter;

public abstract class AbstractFilter implements Filter
{
    public boolean invert;

    public boolean isInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    @Override
    public boolean match(Object object) {
        return isInvert() ? !doMatch(object) : doMatch(object);
    }

    public abstract boolean doMatch(Object object);
}
