package com.github.terefang.jmelange.pdf.ext.text;

import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import jhyphenator.Hyphenator;

import java.util.List;

public class BreakCell implements Cell {
    @Override
    public List<Cell> breakup(PdfFontRegistry _r, double _limit, double _hspace) {
        return null;
    }

    @Override
    public double width(PdfFontRegistry _r, boolean _firstInLine) {
        return 0;
    }
    @Override

    public double width(PdfFontRegistry _r, double _hspace, boolean _firstInLine) {
        return 0;
    }

    @Override
    public String render(PdfFontRegistry _r, PdfContent _cnt, boolean _firstInLine, double _ident, double _wordspace, double _charspace, double _hspace) {
        return "";
    }

    @Override
    public int numberOfSpaces(boolean _firstInLine) {
        return 0;
    }

    @Override
    public int numberOfChars(boolean _firstInLine) {
        return 0;
    }

    @Override
    public boolean canHypenate(PdfFontRegistry reg, boolean _firstInLine, double _limit, double _hspace, Hyphenator _hy) {
        return false;
    }

    @Override
    public Cell[] hypenate(PdfFontRegistry reg, boolean _firstInLine, double _limit, double _hspace, Hyphenator _hy) {
        return new Cell[0];
    }

    @Override
    public String getLink() {
        return "";
    }
}
