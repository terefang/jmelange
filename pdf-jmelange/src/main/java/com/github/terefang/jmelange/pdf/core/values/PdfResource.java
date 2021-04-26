package com.github.terefang.jmelange.pdf.core.values;

import com.github.terefang.jmelange.pdf.core.PdfResNamed;

public class PdfResource<T extends AbstractPdfObject> implements PdfResNamed
{
    T _res;
    String _prefix = "R";
    String _type = "Resource";

    public PdfResource(String prefix, String type)
    {
        _prefix = prefix;
        _type = type;
    }

    public T get() { return _res; }

    public void set(T res) { _res = (T)res; }

    public String getResPrefix() { return _prefix; }

    @Override
    public String getResName() {
        return this.getResPrefix()+this._res.getRef().getValue();
    }

    public String getResType()
    {
        return _type;
    }

}
