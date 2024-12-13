package com.github.terefang.jmelange.fonts;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.gfx.GfxFont;
import com.github.terefang.jmelange.commons.gfx.GfxInterface;
import com.github.terefang.jmelange.commons.io.CustomStreamTokenizer;
import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.commons.loader.ResourceLoader;
import com.github.terefang.jmelange.fonts.AFM;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class BDF
{
    static private int _NOT_SET = -2345;
    
    public static class BDChar {
        String name;
        int unicode = _NOT_SET;
        int encode;
        int dwX;
        int dwY;
        int bbW;
        int bbH;
        int bbX;
        int bbY;
        List<Long> bitmap = new Vector<>();
        
        public String getName()
        {
            return name;
        }
        
        public char getUnicode()
        {
            return (char)unicode;
        }
        
        public int getEncode()
        {
            return encode;
        }
        
        public int getDwX()
        {
            return dwX;
        }
        
        public int getDwY()
        {
            return dwY;
        }
        
        public int getBbW()
        {
            return bbW;
        }
        
        public int getBbH()
        {
            return bbH;
        }
        
        public int getBbX()
        {
            return bbX;
        }
        
        public int getBbY()
        {
            return bbY;
        }
        
        BufferedImage charImage;
        
        public void init()
        {
            if(charImage==null && bbW>0 && bbH>0)
            {
                IndexColorModel _icm = new IndexColorModel(8,2,new byte[]{0, (byte) 255},new byte[]{0, (byte) 255},new byte[]{0, (byte) 255});
                charImage = new BufferedImage(bbW,bbH,BufferedImage.TYPE_BYTE_INDEXED,_icm);
                int _h = 0;
                for(Long _i : this.bitmap)
                {
                    for(int _b = 0; _b<bbW; _b++)
                    {
                        charImage.setRGB(_b,_h,0);
                        if(((_i>>>_b)&1)==1)
                        {
                            charImage.setRGB(_b,_h,0xffffffff);
                        }
                    }
                    _h++;
                }
            }
        }
        
        public BufferedImage getCharImage()
        {
            return charImage;
        }
        
        public void setCharImage(BufferedImage _charImage)
        {
            charImage = _charImage;
        }
    }
    
    public static class BDFont implements GfxFont
    {
        public int ascent = _NOT_SET;
        public int descent = _NOT_SET;
        public Charset cset;
        String fontName;
        int size = _NOT_SET;
        int dpiX = _NOT_SET;
        int dpiY = _NOT_SET;
        int bpp = 1;
        int bbW = _NOT_SET;
        int bbH = _NOT_SET;
        int bbX = _NOT_SET;
        int bbY = _NOT_SET;
        int capHeight = _NOT_SET;
        int xHeight = _NOT_SET;
        
        String charsetRegistry;
        String charsetEncoding;
        
        public String getCharsetRegistry()
        {
            return charsetRegistry;
        }
        
        public void setCharsetRegistry(String _charsetRegistry)
        {
            charsetRegistry = _charsetRegistry;
        }
        
        public String getCharsetEncoding()
        {
            return charsetEncoding;
        }
        
        public void setCharsetEncoding(String _charsetEncoding)
        {
            charsetEncoding = _charsetEncoding;
        }
        
        public boolean isCharset()
        {
            return (getCharsetRegistry()!=null);
        }
        
        public boolean isUnicode()
        {
            return isCharset()
                    && "ISO10646".equalsIgnoreCase(this.charsetRegistry)
                    && "1".equalsIgnoreCase(this.charsetEncoding);
        }
        
        
        public String getFontName()
        {
            return fontName;
        }
        
        public int getSize()
        {
            return size;
        }
        
        public int getDpiX()
        {
            return dpiX;
        }
        
        public int getDpiY()
        {
            return dpiY;
        }
        
        public int getBpp()
        {
            return bpp;
        }
        
        public int getBbW()
        {
            return bbW;
        }
        
        public int getBbH()
        {
            return bbH;
        }
        
        public int getBbX()
        {
            return bbX;
        }
        
        public int getBbY()
        {
            return bbY;
        }
        
        public int getCapHeight()
        {
            return capHeight;
        }
        
        public int getxHeight()
        {
            return xHeight;
        }
       
        List<BDChar> charList = new Vector<>();
        Map<String, Integer> chars= new LinkedHashMap<>();;
        Map<Character, Integer> unicode = new LinkedHashMap<>();;
        
        public BDChar getChar(char _c)
        {
            Integer _entry = this.unicode.get(_c);
            if(_entry==null) return null;
            return this.charList.get(_entry);
        }
        
        public Set<Character> getChars()
        {
            return this.unicode.keySet();
        }

        public Collection<BDChar> getBDChars()
        {
            return Collections.unmodifiableList(this.charList);
        }
        
        @Override
        public void drawString(GfxInterface _surface, int _x, int _y, String _s, long _color)
        {
            BDF.drawString(_surface.getG2d(), this,_x,_y,_s,new Color((int)_color));
        }
        
        @Override
        public void drawString(GfxInterface _surface, int _x, int _y, String _s, long _color, long _mcolor)
        {
            BDF.drawString(_surface.getG2d(), this,_x,_y,_s,new Color((int)_color),new Color((int)_mcolor));
        }
    }
    
    @SneakyThrows
    public static BDFont load(File _file)
    {
        return load(new FileReader(_file));
    }

    @SneakyThrows
    public static BDFont load(URL _file)
    {
        return load(new InputStreamReader(_file.openStream(), StandardCharsets.UTF_8));
    }

    @SneakyThrows
    public static BDFont load(ResourceLoader _rl)
    {
        return load(new InputStreamReader(_rl.getInputStream(), StandardCharsets.UTF_8));
    }
    
    @SneakyThrows
    public static BDFont load(Reader _file)
    {
        //AFM.parseAglFN();
        CustomStreamTokenizer _st = new CustomStreamTokenizer(_file);
        _st.resetSyntax();
        _st.quoteChar('"');
        _st.whitespaceChars(0, 9);
        _st.whitespaceChars(11, 12);
        _st.whitespaceChars(14, 32);
        _st.wordChars('a','z');
        _st.wordChars('A','Z');
        _st.wordChar('_');
        _st.wordChar('-');
        _st.wordChar('+');
        _st.wordChar('.');
        _st.wordChars('0','9');
        _st.autoUnicodeMode(true);
        _st.byteLiterals(false);
        _st.eolIsSignificant(true);
        //_st.commentChar('#');
        _st.hexLiterals(false);

        BDFont _bdf = new BDFont();
        int _lineNo = 0;
        try
        {
            int _token = 0;
            while(_token != CustomStreamTokenizer.TOKEN_TYPE_EOF)
            {
                List<String> _line = new Vector<>();
                while ((_token = _st.nextToken()) != CustomStreamTokenizer.TOKEN_TYPE_EOL)
                {
                    if (_token == CustomStreamTokenizer.TOKEN_TYPE_EOF) break;
                    _line.add(_st.tokenAsString());
                }
                
                //System.err.printf("line = %d\n", (_lineNo++));
                
                if (_token == CustomStreamTokenizer.TOKEN_TYPE_EOF) break;
                
                if ("FONT".equalsIgnoreCase(_line.get(0)))
                {
                    _bdf.fontName = _line.get(1);
                }
                else if ("SIZE".equalsIgnoreCase(_line.get(0)))
                {
                    _bdf.size = CommonUtil.checkInt(_line.get(1));
                    _bdf.dpiX = CommonUtil.checkInt(_line.get(2));
                    _bdf.dpiY = CommonUtil.checkInt(_line.get(3));
                    if (_line.size() == 5)
                        _bdf.bpp = CommonUtil.checkInt(_line.get(4));
                }
                else if ("FONTBOUNDINGBOX".equalsIgnoreCase(_line.get(0)))
                {
                    _bdf.bbW = CommonUtil.checkInt(_line.get(1));
                    _bdf.bbH = CommonUtil.checkInt(_line.get(2));
                    _bdf.bbX = CommonUtil.checkInt(_line.get(3));
                    _bdf.bbY = CommonUtil.checkInt(_line.get(4));
                }
                else if ("FONT_ASCENT".equalsIgnoreCase(_line.get(0)))
                {
                    _bdf.ascent = CommonUtil.checkInt(_line.get(1));
                }
                else if ("FONT_DESCENT".equalsIgnoreCase(_line.get(0)))
                {
                    _bdf.descent = CommonUtil.checkInt(_line.get(1));
                }
                else if ("PIXEL_SIZE".equalsIgnoreCase(_line.get(0)))
                {
                    _bdf.size = CommonUtil.checkInt(_line.get(1));
                }
                else if ("CAP_HEIGHT".equalsIgnoreCase(_line.get(0)))
                {
                    _bdf.capHeight = CommonUtil.checkInt(_line.get(1));
                }
                else if ("X_HEIGHT".equalsIgnoreCase(_line.get(0)))
                {
                    _bdf.xHeight = CommonUtil.checkInt(_line.get(1));
                }
                else if ("RESOLUTION_X".equalsIgnoreCase(_line.get(0)))
                {
                    _bdf.dpiX = CommonUtil.checkInt(_line.get(1));
                }
                else if ("RESOLUTION_Y".equalsIgnoreCase(_line.get(0)))
                {
                    _bdf.dpiY = CommonUtil.checkInt(_line.get(1));
                }
                else if ("CHARSET_REGISTRY".equalsIgnoreCase(_line.get(0)))
                {
                    _bdf.charsetRegistry = _line.get(1);
                }
                else if ("CHARSET_ENCODING".equalsIgnoreCase(_line.get(0)))
                {
                    _bdf.charsetEncoding = _line.get(1);
                }
                else if ("ENDPROPERTIES".equalsIgnoreCase(_line.get(0)))
                {
                    //-- must be set
                    // int size = _NOT_SET;
                    // int bpp = 1;
                    // int bbW = _NOT_SET;
                    // int bbH = _NOT_SET;
                    // int bbX = _NOT_SET;
                    // int bbY = _NOT_SET;
                    //String fontName;
                    //-- not needed
                    //int dpiX = _NOT_SET;
                    //int dpiY = _NOT_SET;
                    
                    //public Charset cset;
                    if(_bdf.charsetRegistry!=null && _bdf.charsetEncoding!=null)
                    {
                        String _cs = String.format("%s-%s",_bdf.charsetRegistry, _bdf.charsetEncoding);
                        if(Charset.isSupported(_cs))
                        {
                            _bdf.cset = Charset.forName(_cs);
                        }
                    }
                    
                    _bdf.capHeight = _bdf.capHeight<0 ? Math.max(_bdf.size * 800/1000,_bdf.capHeight) : _bdf.capHeight;
                    _bdf.xHeight = _bdf.xHeight<0 ? Math.max(_bdf.size * 500/1000,_bdf.xHeight): _bdf.xHeight;
                    
                    _bdf.ascent = _bdf.ascent < 0 ? Math.max(_bdf.capHeight,_bdf.ascent) : _bdf.ascent;
                    _bdf.descent = _bdf.descent < 0 ? Math.max(_bdf.capHeight/5,_bdf.descent) : _bdf.descent;
                }
                else if ("STARTCHAR".equalsIgnoreCase(_line.get(0)))
                {
                    BDChar _char = readChar(_bdf, _line.get(1), _st);
                    _bdf.charList.add(_char);
                    int _index = _bdf.charList.size()-1;
                    _bdf.chars.put(_char.name, _index);
                    
                    if(_char.unicode!=_NOT_SET && _char.unicode!=-1)
                    {
                        _bdf.unicode.put((char) _char.unicode, _index);
                    }
                    else
                    if (_char.name.startsWith("char") &&_char.name.length()==8)
                    {
                        _bdf.unicode.put((char) CommonUtil.checkInt("0x" + _char.name.substring(4)), _index);
                    }
                    else if (_char.name.startsWith("char"))
                    {
                        _bdf.unicode.put((char) CommonUtil.checkInt(_char.name.substring(4)), _index);
                    }
                    else if (_char.name.startsWith("uni") &&_char.name.length()==7)
                    {
                        _bdf.unicode.put((char) CommonUtil.checkInt("0x" + _char.name.substring(3)), _index);
                    }
                    else
                    {
                        int _code = AFM.getUnicode(_char.name);
                        //_code = _code > 0 ? _code : _char.unicode;
                        
                        if (_code > 0)
                            _bdf.unicode.put((char) _code, _index);
                    }
                }
            }
        }
        catch(Exception _xe) { _xe.printStackTrace();}
        return _bdf;
    }

    
    @SneakyThrows
    private static BDChar readChar(BDFont _bdf, String _name, CustomStreamTokenizer _st) {
        BDChar _char = new BDChar();
        _char.name = _name;
        boolean _inbitmap = false;
        while(!"ENDCHAR".equalsIgnoreCase(_st.tokenAsString())) {
            List<String> _line = new Vector<>();
            while (_st.nextToken() != CustomStreamTokenizer.TOKEN_TYPE_EOL) {
                _line.add(_st.tokenAsString());
            }

            if("ENDCHAR".equalsIgnoreCase(_line.get(0)))
            {
                _char.init();
                break;
            }
            else
            if("BITMAP".equalsIgnoreCase(_line.get(0)))
            {
                _inbitmap = true;
            }
            else
            if("BBX".equalsIgnoreCase(_line.get(0)))
            {
                _char.bbW = CommonUtil.checkInt(_line.get(1));
                _char.bbH = CommonUtil.checkInt(_line.get(2));
                _char.bbX = CommonUtil.checkInt(_line.get(3));
                _char.bbY = CommonUtil.checkInt(_line.get(4));
            }
            else
            if("DWIDTH".equalsIgnoreCase(_line.get(0)))
            {
                _char.dwX = CommonUtil.checkInt(_line.get(1));
                _char.dwY = CommonUtil.checkInt(_line.get(2));
            }
            else
            if("ENCODING".equalsIgnoreCase(_line.get(0)))
            {
                _char.encode = CommonUtil.checkInt(_line.get(1));
                if(_char.encode < 0 && _line.size()==3)
                {
                    _char.unicode = (char) CommonUtil.checkInt(_line.get(2));
                }
                else
                if(_bdf.isUnicode() && _line.size()==2)
                {
                    _char.unicode = (char) _char.encode;
                }
                else
                if(_bdf.cset!=null && _char.encode > 0 && _char.encode < 256)
                {
                    _char.unicode = new String(new byte[]{(byte) _char.encode}, _bdf.cset).charAt(0);
                }
                else
                if(_char.encode > 0 && _char.encode < 256)
                {
                    _char.unicode = adobeStdEncoding[_char.encode];
                }
                else // ?
                if(_char.encode >= 256)
                {
                    _char.unicode = (char) _char.encode;
                }
            }
            else
            if(_inbitmap)
            {
                String _b = _line.get(0);
                long _l = CommonUtil.checkLong("0x"+_b);
                long _c =0;
                for(int _i = 0; _i<_b.length()*4; _i++)
                {
                    _c = _c<<1;
                    if((_l&1)==1) _c|=1;
                    _l=_l>>>1;
                }
                _char.bitmap.add(_c);
            }
        }
        return _char;
    }

    // #  Name:             Adobe Standard Encoding to Unicode
    // #  Unicode version:  2.0
    // #  Table version:    1.0
    // #  Date:             2011 July 12
    public static char[] adobeStdEncoding = {
            (char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,
            (char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,
            (char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,
            (char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,
            0x20,   0x21,   0x22,   0x23,   0x24,   0x25,   0x26,    0x2019,
            0x28,   0x29,   0x2A,   0x2B,   0x2C,   0x2D,   0x2E,    0x2F,
            0x30,   0x31,   0x32,   0x33,   0x34,   0x35,   0x36,    0x37,
            0x38,   0x39,   0x3A,   0x3B,   0x3C,   0x3D,   0x3E,    0x3F,
            0x40,   0x41,   0x42,   0x43,   0x44,   0x45,   0x46,    0x47,
            0x48,   0x49,   0x4A,   0x4B,   0x4C,   0x4D,   0x4E,    0x4F,
            0x50,   0x51,   0x52,   0x53,   0x54,   0x55,   0x56,    0x57,
            0x58,   0x59,   0x5A,   0x5B,   0x5C,   0x5D,   0x5E,    0x5F,
            0x2018, 0x61,   0x62,   0x63,   0x64,   0x65,   0x66,    0x67,
            0x68,   0x69,   0x6A,   0x6B,   0x6C,   0x6D,   0x6E,    0x6F,
            0x70,   0x71,   0x72,   0x73,   0x74,   0x75,   0x76,    0x77,
            0x78,   0x79,   0x7A,   0x7B,   0x7C,   0x7D,   0x7E,    0x7F,
            (char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,
            (char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,
            (char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,
            (char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,(char)-1,
            0xA0,   0xA1,   0xA2,   0xA3,   0x2044, 0x2215, 0x0192,  0xA7,
            0x00A4, 0x0027, 0x201C, 0xAB,   0x2039, 0x203A, 0xFB01,  0xFB02,
            0xB0,   0x2013, 0x2020, 0x2021, 0x00B7, 0xB5,   0xB6,    0x2022,
            0x201A, 0x201E, 0x201D, 0xBB,   0x2026, 0x2030, 0xBE,    0xBF,
            0xC0,   0x0060, 0x00B4, 0x02C6, 0x02DC, 0x00AF, 0x02D8,  0x02D9,
            0x00A8, 0xC9,   0x02DA, 0x00B8, 0xCC,   0x02DD, 0x02DB,  0x02C7,
            0x2014, 0xD1,   0xD2,   0xD3,   0xD4,   0xD5,   0xD6,    0xD7,
            0xD8,   0xD9,   0xDA,   0xDB,   0xDC,   0xDD,   0xDE,    0xDF,
            0xE0,   0x00C6, 0xE2,   0x00AA, 0xE4,   0xE5,   0xE6,    0xE7,
            0x0141, 0x00D8, 0x0152, 0x00BA, 0xEC,   0xED,   0xEE,    0xEF,
            0xF0,   0x00E6, 0xF2,   0xF3,   0xF4,   0x0131, 0xF6,    0xF7,
            0x0142, 0x00F8, 0x0153, 0x00DF, 0xFC,   0xFD,   0xFE,    0xFF,
    };
    
    public static int drawChar(Graphics2D _surface, BDFont _font, char _c, int _x, int _y)
    {
        return drawChar(_surface, _font, _c, _x,_y, Color.BLACK, Color.WHITE, false);
    }
    
    public static int drawChar(Graphics2D _surface, BDFont _font, char _c, int _x, int _y, Color _color, Color _mcolor, boolean _useMColor)
    {
        Composite _composite = new Composite(){
            
            @Override
            public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints)
            {
                return new CompositeContext()
                {
                    @Override
                    public void dispose()
                    {
                    
                    }
                    
                    @Override
                    public void compose(Raster src, Raster dstIn, WritableRaster dstOut)
                    {
                        final int w = Math.min(src.getWidth(), dstIn.getWidth());
                        final int h = Math.min(src.getHeight(), dstIn.getHeight());
                        for (int y = 0; y < h; ++y) {
                            for (int x = 0; x < w; ++x)
                            {
                                Object _obj = src.getDataElements(x, y, null);
                                
                                boolean _b = false;
                                if(_obj instanceof byte[])
                                {
                                    _b = ((byte[])_obj)[0]!=0;
                                }
                                else
                                if(_obj instanceof int[])
                                {
                                    _b = ((int[])_obj)[0]!=0;
                                }
                                
                                if(_b)
                                {
                                    dstOut.setDataElements(x, y, dstColorModel.getDataElements(_color.getRGB(), null));
                                }
                                else
                                if(!_b && _useMColor)
                                {
                                    dstOut.setDataElements(x, y, dstColorModel.getDataElements(_mcolor.getRGB(), null));
                                }
                            }
                        }
                    }
                };
            }
        };
        
        Composite _comp = _surface.getComposite();
        Color _ocolor = _surface.getColor();
        BDChar _ch = _font.getChar(_c);
        if(_ch==null)
        {
            _ch = _font.getChar('?');
        }
        
        if(_useMColor)
        {
            _surface.setColor(_mcolor);
            _surface.fillRect(_x,_y,_font.bbW,_font.size+ _font.descent);
        }
        
        if(_ch==null) return _font.getBbW();

        BufferedImage _ci = _ch.getCharImage();
        if(_ci==null) return _ch.getDwX();
        
        //setComposite(_composite);
        if(_surface.getDeviceConfiguration().getImageCapabilities()
                .isTrueVolatile())
        {
            _surface.setColor(Color.BLACK);
            _surface.fillRect(_x,_y,_font.bbW,_font.size+ _font.descent);
        }
        else
        {
            _surface.setComposite(_composite);
        }
        _surface.drawImage(_ci,_x+_ch.bbX,_y+_font.size-_ch.bbH-_ch.bbY,_ci.getWidth(), _ci.getHeight(), null);
        _surface.setComposite(_comp);
        _surface.setColor(_ocolor);
        return _ch.getDwX();
    }
    
    public static void drawString(Graphics2D _surface, BDFont _font, int _x, int _y, String _s)
    {
        for(char _c : _s.toCharArray())
        {
            _x += drawChar(_surface,_font, _c, _x, _y, Color.BLACK, Color.WHITE, false);
        }
    }
    
    public static void drawString(Graphics2D _surface, BDFont _font, int _x, int _y, String _s, Color _color)
    {
        for(char _c : _s.toCharArray())
        {
            _x += drawChar(_surface,_font, _c, _x, _y, _color, Color.WHITE, false);
        }
    }
    
    public static void drawString(Graphics2D _surface, BDFont _font, int _x, int _y, String _s, Color _color, Color _mcolor)
    {
        for(char _c : _s.toCharArray())
        {
            _x += drawChar(_surface,_font, _c, _x, _y, _color, _mcolor, true);
        }
    }
    
    @SneakyThrows
    public static BDFont load(String _fontName)
    {
        return load(ClasspathResourceLoader.of("fonts/bdf/"+_fontName+".bdf", null,null));
    }
    
    public static final String FONT_FIXED                  = "Fixed-Medium";
    public static final String FONT_4_X_6                  = "4x6";
    public static final String FONT_5_X_7                  = "5x7";
    public static final String FONT_5_X_8                  = "5x8";
    public static final String FONT_6_X_9                  = "6x9";
    public static final String FONT_6_X_10                 = "6x10";
    public static final String FONT_6_X_12                 = "6x12";
    public static final String FONT_6_X_13                 = "6x13";
    public static final String FONT_6_X_13_B                = "6x13B";
    public static final String FONT_6_X_13_O                = "6x13O";
    public static final String FONT_7_X_13                 = "7x13";
    public static final String FONT_7_X_13_B                = "7x13B";
    public static final String FONT_7_X_13_O                = "7x13O";
    public static final String FONT_7_X_14                 = "7x14";
    public static final String FONT_7_X_14_B                = "7x14B";
    public static final String FONT_8_X_13                 = "8x13";
    public static final String FONT_8_X_13_B                = "8x13B";
    public static final String FONT_8_X_13_O                = "8x13O";
    public static final String FONT_9_X_15                 = "9x15";
    public static final String FONT_9_X_15_B                = "9x15B";
    public static final String FONT_9_X_18                 = "9x18";
    public static final String FONT_9_X_18_B                = "9x18B";
    public static final String FONT_10_X_20                = "10x20";
    public static final String FONT_CL_R_6_X_12              = "clR6x12";
    public static final String FONT_CREEP                = "creep";
    public static final String FONT_HAXOR_MEDIUM_10       = "HaxorMedium-10";
    public static final String FONT_HAXOR_MEDIUM_11       = "HaxorMedium-11";
    public static final String FONT_HAXOR_MEDIUM_12       = "HaxorMedium-12";
    public static final String FONT_HAXOR_MEDIUM_13       = "HaxorMedium-13";
    public static final String FONT_HAXOR_NARROW_15       = "HaxorNarrow-15";
    public static final String FONT_HAXOR_NARROW_16       = "HaxorNarrow-16";
    public static final String FONT_HAXOR_NARROW_17       = "HaxorNarrow-17";
    public static final String FONT_HELV_R_12              = "helvR12";
    public static final String FONT_KNXT                 = "knxt";
    public static final String FONT_PEEP_10_X_20           = "peep-10x20";
    public static final String FONT_SCIENTIFICA_11       = "scientifica-11";
    public static final String FONT_SCIENTIFICA_BOLD_11   = "scientificaBold-11";
    public static final String FONT_SCIENTIFICA_ITALIC_11 = "scientificaItalic-11";
    public static final String FONT_SPLEEN_5_X_8           = "spleen-5x8";
    public static final String FONT_SPLEEN_8_X_16          = "spleen-8x16";
    public static final String FONT_SPLEEN_12_X_24         = "spleen-12x24";
    public static final String FONT_SPLEEN_16_X_32         = "spleen-16x32";
    public static final String FONT_SPLEEN_32_X_64         = "spleen-32x64";
    public static final String FONT_TOM_THUM             = "tom-thumb";
    public static String[] _FONTLIST = {
            FONT_FIXED,
        FONT_4_X_6              ,
        FONT_5_X_7              ,
        FONT_5_X_8              ,
        FONT_6_X_9              ,
        FONT_6_X_10              ,
        FONT_6_X_12              ,
        FONT_6_X_13              ,
        FONT_6_X_13_B              ,
        FONT_6_X_13_O              ,
        FONT_7_X_13              ,
        FONT_7_X_13_B              ,
        FONT_7_X_13_O              ,
        FONT_7_X_14              ,
        FONT_7_X_14_B              ,
        FONT_8_X_13              ,
        FONT_8_X_13_B              ,
        FONT_8_X_13_O              ,
        FONT_9_X_15              ,
        FONT_9_X_15_B              ,
        FONT_9_X_18              ,
        FONT_9_X_18_B              ,
        FONT_10_X_20              ,
        FONT_CL_R_6_X_12              ,
        FONT_CREEP              ,
        FONT_HAXOR_MEDIUM_10      ,
        FONT_HAXOR_MEDIUM_11      ,
        FONT_HAXOR_MEDIUM_12      ,
        FONT_HAXOR_MEDIUM_13      ,
        FONT_HAXOR_NARROW_15      ,
        FONT_HAXOR_NARROW_16      ,
        FONT_HAXOR_NARROW_17      ,
        FONT_HELV_R_12              ,
        FONT_KNXT              ,
        FONT_PEEP_10_X_20          ,
        FONT_SCIENTIFICA_11     ,
        FONT_SCIENTIFICA_BOLD_11   ,
        FONT_SCIENTIFICA_ITALIC_11 ,
        FONT_SPLEEN_5_X_8           ,
        FONT_SPLEEN_8_X_16          ,
        FONT_SPLEEN_12_X_24         ,
        FONT_SPLEEN_16_X_32         ,
        FONT_SPLEEN_32_X_64         ,
        FONT_TOM_THUM
        
    };
 
    
}

