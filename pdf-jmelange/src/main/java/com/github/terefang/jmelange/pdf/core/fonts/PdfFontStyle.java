package com.github.terefang.jmelange.pdf.core.fonts;

import lombok.Builder;
import lombok.Data;

public enum PdfFontStyle
{
    BLACK(      PdfFontStyle.FONT_WEIGHT_BLACK,         PdfFontStyle.FONT_SLANT_UPRIGHT, "Black",        "Heavy"        ),
    EXTRABOLD(  PdfFontStyle.FONT_WEIGHT_EXTRA_BOLD,    PdfFontStyle.FONT_SLANT_UPRIGHT, "Extra Bold",   "Ultra Bold"   ),
    BOLD(       PdfFontStyle.FONT_WEIGHT_BOLD,          PdfFontStyle.FONT_SLANT_UPRIGHT, "Bold",         "Bold"         ),
    SEMIBOLD(   PdfFontStyle.FONT_WEIGHT_SEMI_BOLD,     PdfFontStyle.FONT_SLANT_UPRIGHT, "Semi Bold",    "Demi Bold"    ),
    MEDIUM(     PdfFontStyle.FONT_WEIGHT_MEDIUM,        PdfFontStyle.FONT_SLANT_UPRIGHT, "Medium",       "Medium"       ),
    NORMAL(     PdfFontStyle.FONT_WEIGHT_NORMAL,        PdfFontStyle.FONT_SLANT_UPRIGHT, "Regular",      "Normal"       ),
    LIGHT(      PdfFontStyle.FONT_WEIGHT_LIGHT,         PdfFontStyle.FONT_SLANT_UPRIGHT, "Light",        "Light"        ),
    EXTRALIGHT( PdfFontStyle.FONT_WEIGHT_EXTRA_LIGHT,   PdfFontStyle.FONT_SLANT_UPRIGHT, "Extra Light",  "Ultra Light"  ),
    THIN(       PdfFontStyle.FONT_WEIGHT_THIN,          PdfFontStyle.FONT_SLANT_UPRIGHT, "Thin",         "Thin"         ),
    
    BLACK_ITALIC(       PdfFontStyle.FONT_WEIGHT_BLACK,         PdfFontStyle.FONT_SLANT_ITALIC, "Black Italic",         "Heavy Italic"      ),
    EXTRABOLD_ITALIC(   PdfFontStyle.FONT_WEIGHT_EXTRA_BOLD,    PdfFontStyle.FONT_SLANT_ITALIC, "Extra Bold Italic",    "Ultra Bold Italic" ),
    BOLD_ITALIC(        PdfFontStyle.FONT_WEIGHT_BOLD,          PdfFontStyle.FONT_SLANT_ITALIC, "Bold Italic",          "Bold Italic"       ),
    SEMIBOLD_ITALIC(    PdfFontStyle.FONT_WEIGHT_SEMI_BOLD,     PdfFontStyle.FONT_SLANT_ITALIC, "Semi Bold Italic",     "Demi Bold Italic"  ),
    MEDIUM_ITALIC(      PdfFontStyle.FONT_WEIGHT_MEDIUM,        PdfFontStyle.FONT_SLANT_ITALIC, "Medium Italic",        "Medium Italic"     ),
    NORMAL_ITALIC(      PdfFontStyle.FONT_WEIGHT_NORMAL,        PdfFontStyle.FONT_SLANT_ITALIC, "Italic",               "Italic"     ),
    LIGHT_ITALIC(       PdfFontStyle.FONT_WEIGHT_LIGHT,         PdfFontStyle.FONT_SLANT_ITALIC, "Light Italic",         "Light Italic"      ),
    EXTRALIGHT_ITALIC(  PdfFontStyle.FONT_WEIGHT_EXTRA_LIGHT,   PdfFontStyle.FONT_SLANT_ITALIC, "Extra Light Italic",   "Ultra Light Italic"),
    THIN_ITALIC(        PdfFontStyle.FONT_WEIGHT_THIN,          PdfFontStyle.FONT_SLANT_ITALIC, "Thin Italic",          "Thin Italic"       )
    
    ;
    
    public static final int FONT_SLANT_ITALIC = 1;
    public static final int FONT_SLANT_UPRIGHT = 0;
    public static final int FONT_WEIGHT_MAX = 1000;
    public static final int FONT_WEIGHT_BLACK = 900;
    public static final int FONT_WEIGHT_EXTRA_BOLD = 800;
    public static final int FONT_WEIGHT_BOLD = 700;
    public static final int FONT_WEIGHT_SEMI_BOLD = 600;
    public static final int FONT_WEIGHT_MEDIUM = 500;
    public static final int FONT_WEIGHT_NORMAL = 400;
    public static final int FONT_WEIGHT_LIGHT = 300;
    public static final int FONT_WEIGHT_EXTRA_LIGHT = 200;
    public static final int FONT_WEIGHT_THIN = 100;
    public static final int FONT_WEIGHT_MIN = 1;
    public static final int FONT_WEIGHT_UNSPECIFIED = -1;

    int slant;
    int weight;
    
    String psName;
    String psAltName;
    
    PdfFontStyle(int w, int s, String n, String a)
    {
        this.setSlant(s);
        this.setWeight(w);
        this.setPsName(n);
        this.setPsAltName(a);
    }
    
    public String getPsName()
    {
        return psName;
    }
    
    public void setPsName(String _psName)
    {
        psName = _psName;
    }
    
    public String getPsAltName()
    {
        return psAltName;
    }
    
    public void setPsAltName(String _psAltName)
    {
        psAltName = _psAltName;
    }
    
    public int getSlant()
    {
        return slant;
    }
    
    public PdfFontStyle setSlant(int _slant)
    {
        slant = _slant;
        return this;
    }
    
    public int getWeight()
    {
        return weight;
    }
    
    public PdfFontStyle setWeight(int _weight)
    {
        weight = _weight;
        return this;
    }
    
    
}
