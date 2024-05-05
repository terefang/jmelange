package com.github.terefang.jmelange.fonts;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.io.CustomStreamTokenizer;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class BDF
{
    public static class BDChar {
        String name;
        char unicode;
        int encode;
        int dwX;
        int dwY;
        int bbW;
        int bbH;
        int bbX;
        int bbY;
        List<Integer> bitmap = new Vector<>();
    }
    public static class BDFont {
        String fontName;
        int size;
        int dpiX;
        int dpiY;
        int bpp = 1;
        int bbW;
        int bbH;
        int bbX;
        int bbY;
        int capHeight;
        int xHeight;

        Map<String, BDChar> chars= new LinkedHashMap<>();;
        Map<Character, String> encode= new LinkedHashMap<>();;
    }

    @SneakyThrows
    public static BDFont load(File _file)
    {
        return load(new FileReader(_file));
    }
    @SneakyThrows
    public static BDFont load(Reader _file)
    {
        AFM.parseAglFN();
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
        _st.wordChar('.');
        _st.wordChars('0','9');
        _st.autoUnicodeMode(true);
        _st.byteLiterals(false);
        _st.eolIsSignificant(true);
        //_st.commentChar('#');
        _st.hexLiterals(false);

        BDFont _bdf = new BDFont();
        int _token = 0;
        while(_token != CustomStreamTokenizer.TOKEN_TYPE_EOF)
        {
            List<String> _line = new Vector<>();
            while((_token = _st.nextToken()) != CustomStreamTokenizer.TOKEN_TYPE_EOL)
            {
                if(_token == CustomStreamTokenizer.TOKEN_TYPE_EOF) break;
                _line.add(_st.tokenAsString());
            }
            if(_token == CustomStreamTokenizer.TOKEN_TYPE_EOF) break;

            if("FONT".equalsIgnoreCase(_line.get(0)))
            {
                _bdf.fontName = _line.get(1);
            }
            else
            if("SIZE".equalsIgnoreCase(_line.get(0)))
            {
                _bdf.size = CommonUtil.checkInt(_line.get(1));
                _bdf.dpiX = CommonUtil.checkInt(_line.get(2));
                _bdf.dpiY = CommonUtil.checkInt(_line.get(3));
                if(_line.size()==5)
                    _bdf.bpp = CommonUtil.checkInt(_line.get(4));
            }
            else
            if("FONTBOUNDINGBOX".equalsIgnoreCase(_line.get(0)))
            {
                _bdf.bbW = CommonUtil.checkInt(_line.get(1));
                _bdf.bbH = CommonUtil.checkInt(_line.get(2));
                _bdf.bbX = CommonUtil.checkInt(_line.get(3));
                _bdf.bbY = CommonUtil.checkInt(_line.get(4));
            }
            else
            if("PIXEL_SIZE".equalsIgnoreCase(_line.get(0)))
            {
                _bdf.size = CommonUtil.checkInt(_line.get(1));
            }
            else
            if("RESOLUTION_X".equalsIgnoreCase(_line.get(0)))
            {
                _bdf.dpiX = CommonUtil.checkInt(_line.get(1));
            }
            else
            if("RESOLUTION_Y".equalsIgnoreCase(_line.get(0)))
            {
                _bdf.dpiY = CommonUtil.checkInt(_line.get(1));
            }
            else
            if("STARTCHAR".equalsIgnoreCase(_line.get(0)))
            {
                BDChar _char = readChar(_line.get(1), _st);
                _bdf.chars.put(_char.name, _char);

                if(_char.name.startsWith("char"))
                {
                    _bdf.encode.put((char)CommonUtil.checkInt(_char.name.substring(4)), _char.name);
                }
                else
                if(_char.name.startsWith("uni"))
                {
                    _bdf.encode.put((char)CommonUtil.checkInt("0x"+_char.name.substring(3)), _char.name);
                }
                else
                {
                    int _code = AFM.getUnicode(_char.name);
                    _code = _code > 0 ? _code : _char.unicode;

                    if(_code>0)
                        _bdf.encode.put((char)_code, _char.name);
                }
            }
            System.err.printf("%s\n", _line.toString());
        }
        return _bdf;
    }

    @SneakyThrows
    private static BDChar readChar(String _name, CustomStreamTokenizer _st) {
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
                    _char.unicode = (char) CommonUtil.checkInt(_line.get(2));
                if(_char.encode > 0)
                    _char.unicode = adobeStdEncoding[_char.encode];
            }
            else
            if(_inbitmap)
            {
                int _l = CommonUtil.checkInt("0x"+_line.get(0));
                System.err.println(Integer.toBinaryString(0x8000 | _l));
                _char.bitmap.add(_l);
            }
        }
        return _char;
    }

    public static void main(String[] args) {
        BDF.load(new File("/u/fredo/IdeaProjects/jmelange/fonts-jmelange/src/test/Fixed-Medium.bdf"));
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

}

