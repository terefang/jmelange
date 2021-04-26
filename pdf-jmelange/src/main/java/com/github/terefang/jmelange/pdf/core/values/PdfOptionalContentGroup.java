package com.github.terefang.jmelange.pdf.core.values;

import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.PdfResRef;

public class PdfOptionalContentGroup extends PdfDictObject implements PdfResRef
{
    public PdfOptionalContentGroup(PdfDocument doc, String _name)
    {
        super(doc);
        this.setType("OCG");
        this.set("Name", PdfString.of(_name));
    }

    PdfResource _res ;
    @Override
    public PdfResource getResource()
    {
        if(_res==null)
        {
            _res = new PdfResource("XC", "Properties");
            _res.set(this);
        }
        return _res;
    }

    public static final PdfOptionalContentGroup of(PdfDocument _doc, String _cg)
    {
        return new PdfOptionalContentGroup(_doc, _cg);
    }
}
