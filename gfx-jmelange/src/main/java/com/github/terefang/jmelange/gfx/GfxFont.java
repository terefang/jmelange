package com.github.terefang.jmelange.gfx;

import com.github.terefang.jmelange.gfx.impl.AwtFont;
import com.github.terefang.jmelange.gfx.impl.PdfImage;

import java.awt.*;
import java.io.File;

public interface GfxFont
{
    public void drawString(GfxInterface _surface, int _x, int _y, String _s, long _color);

    public void drawString(GfxInterface _surface, int _x, int _y, String _s, long _color, long _mcolor);

    default GfxFont getFont(String _name, int _size)
    {
        return AwtFont.from(ImageUtil.getFont(_name, _size));
    }

}
