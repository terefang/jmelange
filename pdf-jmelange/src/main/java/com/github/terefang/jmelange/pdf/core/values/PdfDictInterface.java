package com.github.terefang.jmelange.pdf.core.values;

import com.github.terefang.jmelange.pdf.core.PdfValue;

public interface PdfDictInterface<T>
{
    T set(String key, PdfValue value);
    default T setAsName(String key, String value)
    {
        return set(key, PdfName.of(value));
    }

    default T setAsString(String key, String value)
    {
        return set(key, PdfString.of(value));
    }

    default T setAsNum(String key, int value)
    {
        return set(key, PdfNum.of(value));
    }

    default T setAsDocString(String key, String value)
    {
        return set(key, PdfDocString.of(value));
    }
}
