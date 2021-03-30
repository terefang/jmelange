package com.github.terefang.jmelange.utils;

import lombok.SneakyThrows;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImageUtil
{
    public static final int ALPHA_OPAQUE = 0xff000000;
    public static final int ALPHA_MASK = 0xff000000;
    public static final int COLOR_MASK = 0x00ffffff;
    public static final int ALPHA_TRANSPARENT = 0x00000000;

    public static final int WHITE = 0xffffffff;
    public static final int BLACK = 0xffffffff;

    public static final int RED = 0xffff0000;
    public static final int GREEN = 0xff00ff00;
    public static final int BLUE = 0xff0000ff;
    public static final int YELLOW = 0xffffff00;
    public static final int MAGENTA = 0xffff00ff;
    public static final int CYAN = 0xff00ffff;

    public static final int HALF_WHITE = 0x88ffffff;
    public static final int HALF_RED = 0x88ff0000;
    public static final int HALF_GREEN = 0x8800ff00;
    public static final int HALF_BLUE = 0x880000ff;
    public static final int HALF_YELLOW = 0x88ffff00;
    public static final int HALF_MAGENTA = 0x88ff00ff;
    public static final int HALF_CYAN = 0x8800ffff;

    public static final Map<String, Font> FONT_MAP = new HashMap<>();
    public static Color createColor(long _color)
    {
        return new Color((int)_color, true);
    }

    public static Font getFont(String _name, int _size)
    {
        String _l = String.format("%s/%d", _name, _size);
        if(!FONT_MAP.containsKey(_l))
        {
            Font _awt = Font.getFont(_name);
            if(_awt==null)
            {
                _awt = new Font(_name, Font.PLAIN,_size);
            }
            else
            {
                _awt = _awt.deriveFont((float) _size);
            }
            FONT_MAP.put(_l, _awt);
        }
        return FONT_MAP.get(_l);
    }

    @SneakyThrows
    public static Font getTTFont(File _name, int _size)
    {
        String _l = String.format("%s/%d", _name.getName(), _size);
        if(!FONT_MAP.containsKey(_l))
        {
            FONT_MAP.put(_l,Font.createFont(Font.TRUETYPE_FONT, _name).deriveFont((float)_size));
        }
        return FONT_MAP.get(_l);
    }

    @SneakyThrows
    public static Font getPSFont(File _name, int _size)
    {
        String _l = String.format("%s/%d", _name.getName(), _size);
        if(!FONT_MAP.containsKey(_l))
        {
            FONT_MAP.put(_l,Font.createFont(Font.TYPE1_FONT, _name).deriveFont((float)_size));
        }
        return FONT_MAP.get(_l);
    }

    public static Font getFont(String _name, float _size)
    {
        String _l = String.format("%s/%f", _name, _size);
        if(!FONT_MAP.containsKey(_l))
        {
            Font _awt = Font.getFont(_name);
            if(_awt==null)
            {
                _awt = new Font(_name, Font.PLAIN, (int)_size);
            }
            else
            {
                _awt = _awt.deriveFont(_size);
            }
            FONT_MAP.put(_l, _awt);
        }
        return FONT_MAP.get(_l);
    }

    @SneakyThrows
    public static Font getTTFont(File _name, float _size)
    {
        String _l = String.format("%s/%f", _name.getName(), _size);
        if(!FONT_MAP.containsKey(_l))
        {
            FONT_MAP.put(_l,Font.createFont(Font.TRUETYPE_FONT, _name).deriveFont(_size));
        }
        return FONT_MAP.get(_l);
    }

    @SneakyThrows
    public static Font getPSFont(File _name, float _size)
    {
        String _l = String.format("%s/%f", _name.getName(), _size);
        if(!FONT_MAP.containsKey(_l))
        {
            FONT_MAP.put(_l,Font.createFont(Font.TYPE1_FONT, _name).deriveFont(_size));
        }
        return FONT_MAP.get(_l);
    }

    public static int toColor(float _r, float _g, float _b) {
        return (((0xff) << 24) | (((int) (_r * 255)) << 16) | (((int) (_g * 255)) << 8) | ((int) (_b * 255)));
    }

    public static int toColor(float _r, float _g, float _b, float _a) {
        return((((int) (_a * 255)) << 24) | (((int) (_r * 255)) << 16) | (((int) (_g * 255)) << 8) | ((int) (_b * 255)));
    }

    public static int toColor(int _rgb, float _a) {
        return((((_rgb)) << 8) | ((int) (_a * 255)));
    }
}
