package com.github.terefang.jmelange.commons.gfx;

import lombok.Data;

import java.awt.Font;

public interface GfxFont
{
    public void drawString(GfxInterface _surface, int _x, int _y, String _s, long _color);

    public void drawString(GfxInterface _surface, int _x, int _y, String _s, long _color, long _mcolor);

    default GfxFont getFont(String _name, int _size)
    {
        return AwtFont.from(GfxUtil.getFont(_name, _size));
    }
    
    @Data
    public static class AwtFont implements GfxFont
    {
        Font fontObject;
        
        public static GfxFont from(Font _font)
        {
            AwtFont _af = new AwtFont();
            _af.setFontObject(_font);
            return _af;
        }
        
        public void drawString(GfxInterface _surface, int _x, int _y, String _s, long _color)
        {
            _surface.gString(this.getFontObject(), _x, _y, _s, _color);
        }
        
        public void drawString(GfxInterface _surface, int _x, int _y, String _s, long _color, long _mcolor)
        {
            _surface.gString(this.getFontObject(), _x, _y, _s, _color);
        }
        
    }
    
}
