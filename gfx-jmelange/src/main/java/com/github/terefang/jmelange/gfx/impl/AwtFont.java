package com.github.terefang.jmelange.gfx.impl;

import com.github.terefang.jmelange.gfx.GfxFont;
import com.github.terefang.jmelange.gfx.GfxInterface;
import lombok.Data;

import java.awt.*;

@Data
public class AwtFont implements GfxFont
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
