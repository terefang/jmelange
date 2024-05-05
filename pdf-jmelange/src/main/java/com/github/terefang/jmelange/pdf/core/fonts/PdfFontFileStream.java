package com.github.terefang.jmelange.pdf.core.fonts;

import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.values.PdfDictObjectWithStream;
import com.github.terefang.jmelange.pdf.core.values.PdfNum;
import com.github.terefang.jmelange.pdf.core.values.PdfString;

public class PdfFontFileStream extends PdfDictObjectWithStream
{
    public PdfFontFileStream(PdfDocument doc)
    {
        super(doc);
        this.set("X_Class", PdfString.of("PdfFontFileStream"));
    }

    public void setLength1(int _l)
    {
        this.set("Length1", PdfNum.of(_l));
    }

    public void setLength2(int _l)
    {
        this.set("Length2", PdfNum.of(_l));
    }

    public void setLength3(int _l)
    {
        this.set("Length3", PdfNum.of(_l));
    }

    public static final PdfFontFileStream create(PdfDocument doc) { return new PdfFontFileStream(doc); }

}
