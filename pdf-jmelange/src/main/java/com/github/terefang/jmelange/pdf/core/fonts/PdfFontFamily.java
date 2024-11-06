package com.github.terefang.jmelange.pdf.core.fonts;

public class PdfFontFamily
{
    String name;
    PdfFont[] upright = new PdfFont[10];
    PdfFont[] slanted = new PdfFont[10];
    
    public static PdfFontFamily from(PdfFont _r,PdfFont _i,PdfFont _b,PdfFont _bi)
    {
        return new PdfFontFamily()
                .defaultUpright(_r)
                .defaultSlanted(_i)
                .setFont(PdfFontStyle.BOLD, _b)
                .setFont(PdfFontStyle.BOLD_ITALIC, _bi);
    }
    
    public static PdfFontFamily fromWithBold(PdfFont _r,PdfFont _b)
    {
        return new PdfFontFamily()
                .defaultFont(_r)
                .setFont(PdfFontStyle.BOLD, _b);
    }
    
    public static PdfFontFamily fromWithItalic(PdfFont _r,PdfFont _i)
    {
        return new PdfFontFamily()
                .defaultUpright(_r)
                .defaultSlanted(_i);
    }
    
    public static PdfFontFamily from(PdfFont _r)
    {
        return new PdfFontFamily()
                .defaultFont(_r);
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String _name)
    {
        name = _name;
    }
    
    public PdfFontFamily defaultFont(PdfFont f)
    {
        this.upright[0] = this.slanted[0] = this.upright[4] = this.slanted[4] = f;
        return this;
    }
    
    public PdfFontFamily defaultUpright(PdfFont f)
    {
        this.upright[0] = this.upright[4] = f;
        return this;
    }
    
    public PdfFontFamily defaultSlanted(PdfFont f)
    {
        this.slanted[0] = this.slanted[4] = f;
        return this;
    }
    
    public PdfFontFamily setFont(PdfFontStyle s, PdfFont f)
    {
        if(s.getWeight()>=PdfFontStyle.FONT_WEIGHT_MAX) throw new IllegalArgumentException("FONT_WEIGHT too large");
        if(s.getWeight()<=PdfFontStyle.FONT_WEIGHT_MIN) throw new IllegalArgumentException("FONT_WEIGHT too small");
        
        int _index = s.getWeight()/100;
        if(_index>=this.upright.length) _index = this.upright.length-1;
        
        if(s.getSlant()==PdfFontStyle.FONT_SLANT_ITALIC)
        {
            this.slanted[_index] = f;
        }
        else
        {
            this.upright[_index] = f;
        }
        return this;
    }

    public PdfFont getFont(PdfFontStyle s)
    {
        int _index = s.getWeight()/100;
        if(_index<0) _index = 0;
        
        PdfFont[] _set = s.getSlant()==PdfFontStyle.FONT_SLANT_ITALIC ? this.slanted : this.upright;

        if(_index>=_set.length) _index = _set.length-1;

        for(int _x = _index; _x<_set.length; _x++)
        {
            if(_set[_x]!=null) return _set[_x];
        }
        return _set[0];
    }
}
