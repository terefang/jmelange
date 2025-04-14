package com.github.terefang.jmelange.fonts.sfnt;

import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.Font;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.FontFactory;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.Tag;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SfntUtil
{
    public static Font[] loadFonts(File _file) throws IOException
    {
        try (BufferedInputStream _bin = new BufferedInputStream(new FileInputStream(_file), 8192))
        {
            return FontFactory.getInstance().loadFonts(_bin);
        }
    }

    public static Font[] loadFonts(String _file) throws IOException
    {
        try (BufferedInputStream _bin = new BufferedInputStream(new FileInputStream(_file), 8192))
        {
            return FontFactory.getInstance().loadFonts(_bin);
        }
    }

    public static Font loadFont(File _file) throws IOException
    {
        try (BufferedInputStream _bin = new BufferedInputStream(new FileInputStream(_file), 8192))
        {
            return FontFactory.getInstance().loadFonts(_bin)[0];
        }
    }

    public static Font loadFont(String _file) throws IOException
    {
        try (BufferedInputStream _bin = new BufferedInputStream(new FileInputStream(_file), 8192))
        {
            return FontFactory.getInstance().loadFonts(_bin)[0];
        }
    }

    public static int[] findGlyphCharacters(Font _font)
    {
        CMap _cmap = findCMap(_font, false);
        int  _ng   = getNumGlyphs(_font);
        int[] _chr = new int[_ng];

        for(int _c=0; _c<0x20000; _c++)
        {
            int _g = _cmap.glyphId(_c);
            if(_g>=0 && _g<_chr.length)
            {
                _chr[_g] = _c;
            }
        }

        return _chr;
    }

    public static CMap findCMap(Font _font, boolean _prefer30)
    {
        CMap _cmap = null;
        CMapTable _cmt = (CMapTable)_font.getTable(Tag.cmap);
        for(int[] _pfEn : platformEncoding)
        {
            try
            {
                _cmap = _cmt.cmap(_pfEn[0], _pfEn[1]);
                if(_cmap !=null) break;
            }
            catch(Exception _Xe) { /* IGNORE */}
        }

        int _nc = _cmt.numCMaps();

        if(_cmap == null && _nc>0)
        {
            try
            {
                _cmap =_cmt.iterator().next();
            }
            catch(Exception _Xe) {}
        }

        if(_prefer30 && _cmt.cmap(3,0)!=null)
        {
            _cmap = _cmt.cmap(3,0);
        }

        return _cmap;
    }

    public static String findName(Font _font, int _nid)
    {
        try
        {
            NameTable _nt = (NameTable)_font.getTable(Tag.name);
            for(int[] _pfEn : platformEncoding)
            {
                try
                {
                    for(NameTable.NameEntry _nameEntry : _nt.names())
                    {
                        if(_nameEntry.platformId()==_pfEn[0]
                                && _nameEntry.encodingId()==_pfEn[1]
                                && _nameEntry.languageId()==_pfEn[2])
                        {
                            if(_nameEntry.nameId()==_nid)
                            {
                                return _nameEntry.name();
                            }
                        }
                    }
                } catch (Exception _xe) {}
            }
        } catch (Exception _xe) {}
        return null;
    }

    public static Map<String, Integer> getGlyphNames(Font _font, CMap _cmap, String _cs)
    {
        Map<String, Integer> _ret  = new HashMap<>();
        PostScriptTable      _post = _font.getTable(Tag.post);
        int                  _ng   = _post.numberOfGlyphs();
        if(_ng>0)
        {
            String [] _names = new String[_ng];
            for(int _g=0; _g<_ng; _g++)
            {
                _names[_g] = _post.glyphName(_g);
            }

            if(_cs!=null)
            {
                String[] _cn = AFM.getGlyphNamesBase(_cs);
                for(int _c=0; _c<_cn.length; _c++)
                {
                    _ret.put(_cn[_c], _c);
                }
            }
            int _umax = _cmap.maxCodePoint();
            for(int _c=0; _c<_umax; _c++)
            {
                int _g = _cmap.glyphId(_c);
                _ret.put(_names[_g], _c);
            }
        }
        return _ret;
    }

    public static boolean isCFF(Font _font)
    {
        return _font.hasTable(Tag.CFF);
    }

    public static int getUnitsPerEm(Font _font)
    {
        FontHeaderTable _head = (FontHeaderTable)_font.getTable(Tag.head);
        return _head.unitsPerEm();
    }

    public static int getNumGlyphs(Font _font)
    {
        MaximumProfileTable _maxp = (MaximumProfileTable)_font.getTable(Tag.maxp);
        return _maxp.numGlyphs();
    }

    public static int[] getFontBBox(Font _font)
    {
        FontHeaderTable _head = (FontHeaderTable)_font.getTable(Tag.head);
        return new int[] {
                _head.xMin(),
                _head.yMin(),
                _head.xMax(),
                _head.yMax()
        };
    }

    public static String getFontStretch(Font _font)
    {
        OS2Table _os2 = (OS2Table)_font.getTable(Tag.OS_2);
        if(_os2!=null)
        {
            return _os2.usWidthClass() < WIDTH_CLASS.length ? WIDTH_CLASS[_os2.usWidthClass()] : "Normal";
        }
        return "Normal";
    }

    public static int getCapHeight(Font _font, int _em, boolean _approx)
    {
        OS2Table _os2 = (OS2Table)_font.getTable(Tag.OS_2);
        if(_os2!=null && _os2.tableVersion()>=2)
        {
            return _os2.sCapHeight()*1000/_em;
        }
        else
        if(_os2!=null && _approx)
        {
            return (getAscender(_font, _em)+getDescender(_font, _em));
        }
        return Integer.MIN_VALUE;
    }

    public static int getXHeight(Font _font, int _em, boolean _approx)
    {
        OS2Table _os2 = (OS2Table)_font.getTable(Tag.OS_2);
        if(_os2!=null && _os2.tableVersion()>=2)
        {
            return _os2.sxHeight()*1000/_em;
        }
        else
        if(_os2!=null && _approx)
        {
            return getAscender(_font, _em) >> 1;
        }
        return Integer.MIN_VALUE;
    }

    public static int getAscender(Font _font, int _em)
    {
        OS2Table              _os2  = (OS2Table)_font.getTable(Tag.OS_2);
        HorizontalHeaderTable _hhea = (HorizontalHeaderTable)_font.getTable(Tag.hhea);
        if(_hhea!=null)
        {
            return _hhea.ascender()*1000/_em;
        }
        else
        if(_os2!=null)
        {
            return _os2.sTypoAscender()*1000/_em;
        }
        return 800;
    }

    public static int getDescender(Font _font, int _em)
    {
        OS2Table _os2 = (OS2Table)_font.getTable(Tag.OS_2);
        HorizontalHeaderTable _hhea = (HorizontalHeaderTable)_font.getTable(Tag.hhea);
        if(_hhea!=null)
        {
            return _hhea.descender()*1000/_em;
        }
        else
        if(_os2!=null)
        {
            return _os2.sTypoDescender()*1000/_em;
        }
        return -200;
    }

    public static int[][] platformEncoding = {
            {Font.PlatformId.Windows.value(), Font.WindowsEncodingId.UnicodeUCS4.value(), NameTable.WindowsLanguageId.English_UnitedStates.value()},
            {Font.PlatformId.Unicode.value(), Font.UnicodeEncodingId.Unicode2_0.value(),  NameTable.UnicodeLanguageId.All.value()},
            {Font.PlatformId.Windows.value(), Font.WindowsEncodingId.UnicodeUCS2.value(), NameTable.WindowsLanguageId.English_UnitedStates.value()},
            {Font.PlatformId.ISO.value(),     0, 0},
            {Font.PlatformId.Unicode.value(), Font.UnicodeEncodingId.Unicode2_0_BMP.value(), NameTable.UnicodeLanguageId.All.value()},
            {Font.PlatformId.Windows.value(), Font.WindowsEncodingId.Symbol.value(), 0},
            {Font.PlatformId.Macintosh.value(), Font.MacintoshEncodingId.Roman.value(), NameTable.MacintoshLanguageId.English.value()},
    };

    public static final String[] WIDTH_CLASS = { "Normal", "UltraCondensed", "ExtraCondensed", "Condensed", "SemiCondensed", "Normal", "SemiExpanded", "Expanded", "ExtraExpanded", "UltraExpanded" };


}
