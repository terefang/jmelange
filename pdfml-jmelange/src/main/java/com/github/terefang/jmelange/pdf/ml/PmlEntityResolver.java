package com.github.terefang.jmelange.pdf.ml;

import com.github.terefang.jmelange.pdf.ml.kxml2.EntityResolver;

public class PmlEntityResolver implements EntityResolver
{
    @Override
    public String resolve(String _code)
    {
        return "&{"+_code+"}";
    }
}
