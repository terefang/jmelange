package com.github.terefang.jmelange.pdf.ext;

import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;

public class PDFX extends PDF
{
    public static final String UNICODE_REGULAR = "cp:fonts/NotoSansMerged-Regular.ttf";

    public static final String TEX_GYRE_ADVENTOR_BOLD = "cp:fonts/TeXGyreAdventor-Bold.otf";
    public static final String TEX_GYRE_ADVENTOR_BOLDITALIC = "cp:fonts/TeXGyreAdventor-BoldItalic.otf";
    public static final String TEX_GYRE_ADVENTOR_ITALIC = "cp:fonts/TeXGyreAdventor-Italic.otf";
    public static final String TEX_GYRE_ADVENTOR_REGULAR = "cp:fonts/TeXGyreAdventor-Regular.otf";
    public static final String TEX_GYRE_BONUM_BOLD = "cp:fonts/TeXGyreBonum-Bold.otf";
    public static final String TEX_GYRE_BONUM_BOLDITALIC = "cp:fonts/TeXGyreBonum-BoldItalic.otf";
    public static final String TEX_GYRE_BONUM_ITALIC = "cp:fonts/TeXGyreBonum-Italic.otf";
    public static final String TEX_GYRE_BONUM_REGULAR = "cp:fonts/TeXGyreBonum-Regular.otf";
    public static final String TEX_GYRE_CHORUS_MEDIUM = "cp:fonts/TeXGyreChorus-Medium.otf";
    public static final String TEX_GYRE_CURSOR_BOLD = "cp:fonts/TeXGyreCursor-Bold.otf";
    public static final String TEX_GYRE_CURSOR_BOLDITALIC = "cp:fonts/TeXGyreCursor-BoldItalic.otf";
    public static final String TEX_GYRE_CURSOR_ITALIC = "cp:fonts/TeXGyreCursor-Italic.otf";
    public static final String TEX_GYRE_CURSOR_REGULAR = "cp:fonts/TeXGyreCursor-Regular.otf";
    public static final String TEX_GYRE_HEROS_BOLD = "cp:fonts/TeXGyreHeros-Bold.otf";
    public static final String TEX_GYRE_HEROS_BOLDITALIC = "cp:fonts/TeXGyreHeros-BoldItalic.otf";
    public static final String TEX_GYRE_HEROS_CONDENSED_BOLD = "cp:fonts/TeXGyreHerosCondensed-Bold.otf";
    public static final String TEX_GYRE_HEROS_CONDENSED_BOLDITALIC = "cp:fonts/TeXGyreHerosCondensed-BoldItalic.otf";
    public static final String TEX_GYRE_HEROS_CONDENSED_ITALIC = "cp:fonts/TeXGyreHerosCondensed-Italic.otf";
    public static final String TEX_GYRE_HEROS_CONDENSED_REGULAR = "cp:fonts/TeXGyreHerosCondensed-Regular.otf";
    public static final String TEX_GYRE_HEROS_ITALIC = "cp:fonts/TeXGyreHeros-Italic.otf";
    public static final String TEX_GYRE_HEROS_REGULAR = "cp:fonts/TeXGyreHeros-Regular.otf";
    public static final String TEX_GYRE_PAGELLA_BOLD = "cp:fonts/TeXGyrePagella-Bold.otf";
    public static final String TEX_GYRE_PAGELLA_BOLDITALIC = "cp:fonts/TeXGyrePagella-BoldItalic.otf";
    public static final String TEX_GYRE_PAGELLA_ITALIC = "cp:fonts/TeXGyrePagella-Italic.otf";
    public static final String TEX_GYRE_PAGELLA_REGULAR = "cp:fonts/TeXGyrePagella-Regular.otf";
    public static final String TEX_GYRE_SCHOLA_BOLD = "cp:fonts/TeXGyreSchola-Bold.otf";
    public static final String TEX_GYRE_SCHOLA_BOLDITALIC = "cp:fonts/TeXGyreSchola-BoldItalic.otf";
    public static final String TEX_GYRE_SCHOLA_ITALIC = "cp:fonts/TeXGyreSchola-Italic.otf";
    public static final String TEX_GYRE_SCHOLA_REGULAR = "cp:fonts/TeXGyreSchola-Regular.otf";
    public static final String TEX_GYRE_TERMES_BOLD = "cp:fonts/TeXGyreTermes-Bold.otf";
    public static final String TEX_GYRE_TERMES_BOLDITALIC = "cp:fonts/TeXGyreTermes-BoldItalic.otf";
    public static final String TEX_GYRE_TERMES_ITALIC = "cp:fonts/TeXGyreTermes-Italic.otf";
    public static final String TEX_GYRE_TERMES_REGULAR = "cp:fonts/TeXGyreTermes-Regular.otf";

    public static final String ICC_CGATS21 = "cp:icc/CGATS21_CRPC1.icc";
    public static final String ICC_PSO_COATED_V3 = "cp:icc/PSOcoated_v3.icc";
    public static final String ICC_PSO_UNCOATED_V3 = "cp:icc/PSOuncoated_v3.icc";
    
    
    public static PdfExtDocument create()
    {
        return PdfExtDocument.create();
    }
}
