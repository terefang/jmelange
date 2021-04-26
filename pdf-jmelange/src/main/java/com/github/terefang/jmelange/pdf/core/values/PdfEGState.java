package com.github.terefang.jmelange.pdf.core.values;

import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.PdfResRef;

public class PdfEGState extends PdfDictObject implements PdfResRef
{
    public static PdfEGState create(PdfDocument doc)
    {
        return new PdfEGState(doc);
    }

    public PdfEGState(PdfDocument doc) {
        super(doc);
        this.setType("ExtGState");
    }

    public void fillAlpha(float _a)
    {
        this.set("ca", PdfFloat.of(_a));
    }

    public void strokeAlpha(float _a)
    {
        this.set("CA", PdfFloat.of(_a));
    }

    PdfResource _res;
    @Override
    public PdfResource getResource()
    {
        if(_res==null)
        {
            _res = new PdfEGStateResource(this);
        }
        return _res;
    }

    public static class PdfEGStateResource extends PdfResource<PdfEGState>
    {
        public PdfEGStateResource(PdfEGState _xo)
        {
            super("EGS", "PdfEGState");
            set(_xo);
            _xo.setName(this.getResName());
        }
    }
}
