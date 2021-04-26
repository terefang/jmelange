package com.github.terefang.jmelange.pdf.core.fonts;

import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.values.*;

import java.io.IOException;

public class PdfFontDescriptor extends PdfDictObject {
    public PdfFontDescriptor(PdfDocument doc) {
        super(doc);
        this.setType("FontDescriptor");
    }

    public static final PdfFontDescriptor create(PdfDocument doc)
    {
        return new PdfFontDescriptor(doc);
    }

    public void setFontBBox(float lowerLeftX, float lowerLeftY, float upperRightX, float upperRightY)
    {
        this.set("FontBBox", PdfArray.fromFloat(lowerLeftX,lowerLeftY,upperRightX,upperRightY));
    }

    PdfFontFileStream pdfFontFileStream;

    public void setFontBBox(int[] fontBBox) {
        this.setFontBBox(fontBBox[0], fontBBox[1], fontBBox[2], fontBBox[3]);
    }

    public void setFontBBox(float[] fontBBox) {
        this.setFontBBox(fontBBox[0], fontBBox[1], fontBBox[2], fontBBox[3]);
    }

    public void setFontFile3(PdfFontFileStream fs)
    {
        pdfFontFileStream = fs;
        this.set("FontFile3", fs);
    }

    public void setFontFile2(PdfFontFileStream fs)
    {
        pdfFontFileStream = fs;
        this.set("FontFile2", fs);
    }

    public void setFontFile(PdfFontFileStream fs)
    {
        pdfFontFileStream = fs;
        this.set("FontFile", fs);
    }

    public void setFontName(String fontName) {
        this.set("FontName", PdfName.of(fontName));
    }

    public void setFontFamily(String s) {
        this.set("FontFamily", PdfString.of(s));
    }

    public void setFontStretch(String s) {
        this.set("FontStretch", PdfName.of(s));
    }

    public void setCapHeight(int i) {
        this.set("CapHeight", PdfNum.of(i));
    }

    public void setFlags(int i) {
        this.set("Flags", PdfNum.of(i));
    }

    public void setStemV(int i) {
        this.set("StemV", PdfNum.of(i));
    }

    public void setItalicAngle(float i) {
        this.set("ItalicAngle", PdfFloat.of(i));
    }

    public void setAscent(int i) {
        this.set("Ascent", PdfNum.of(i));
    }

    public void setDescent(int i) {
        this.set("Descent", PdfNum.of(i));
    }

    public void setMaxWidth(int i) {
        this.set("MaxWidth", PdfNum.of(i));
    }

    public void setMissingWidth(int i) {
        this.set("MissingWidth", PdfNum.of(i));
    }

    public void setCharSet(String s) {
        this.set("CharSet", PdfString.of(s));
    }

    public void setAverageWidth(int avgWidth) {
        this.set("AverageWidth", PdfNum.of(avgWidth));
    }

    public void setXHeight(int i) {
        this.set("XHeight", PdfNum.of(i));
    }

    @Override
    public void streamOut(boolean _res) throws IOException
    {
        if(_res && this.pdfFontFileStream!=null)
        {
            this.pdfFontFileStream.streamOut();
        }
        super.streamOut(_res);
    }
}
