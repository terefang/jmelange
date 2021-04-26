package com.github.terefang.jmelange.pdf.core.fonts;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.loader.PdfResourceLoader;
import com.github.terefang.jmelange.pdf.core.util.AFM;
import com.github.terefang.jmelange.pdf.core.values.PdfResource;
import com.github.terefang.jmelange.pdf.core.values.PdfString;
import lombok.SneakyThrows;

import java.awt.*;
import java.io.OutputStream;

public class PdfType1Font extends PdfBaseFont
{
    public static final PdfFontResource createResource(PdfType1Font _f)
    {
        return new PdfFontResource(_f, "FT");
    }

    PdfResource _res;
    @Override
    public PdfResource getResource()
    {
        if(_res==null)
        {
            _res = createResource(this);
        }
        return _res;
    }

    public PdfType1Font(PdfDocument doc, String _cs, String _name, int _first, String[] _glyphs, int[] _widths) {
        super(doc, _cs, _name, _first, _glyphs, _widths);
        this.setSubtype("Type1");
    }

    @Override
    public char glyphToChar(String _name)
    {
        for(int _i = this.firstChar; _i<this.glyphs.length; _i++)
        {
            if(_name.equals(this.glyphs[_i])) return (char) _i;
        }
        return 0;
    }

    @SneakyThrows
    public static PdfFont of(PdfDocument doc, Font _afm, String _cs, PdfResourceLoader _rl)
    {
        PdfFont _pdfFont = null;

        Font _font = _afm.deriveFont(1000f);
        Character[] _charset = AFM.loadCharset(_cs == null ? PDF.ENCODING_PDFDOC : _cs);
        String[] _glyphs = AFM.getGlyphNamesBase(_charset);
        int[] _widths = new int[_charset.length];
        FontMetrics _metrics = PdfJavaFont.getFontMetrics(_font);
        int _mc = _font.getMissingGlyphCode();
        for(int _i = 0; _i<_widths.length; _i++)
        {
            if(_font.canDisplay(_charset[_i]!=null ? _charset[_i] : _mc))
            {
                _widths[_i] = _metrics.charWidth(_charset[_i]!=null ? _charset[_i] : _mc);
            }
            else
            {
                _widths[_i] = _metrics.charWidth(_mc);
            }
        }
        String _name = _font.getFontName();

        _pdfFont = new PdfType1Font(doc, _cs == null ? PDF.ENCODING_PDFDOC : _cs, _name, 0, _glyphs, _widths);

        _pdfFont.setFontName("T1-"+_afm.getPSName());
        _pdfFont.setBaseFont(_afm.getPSName());
        PdfFontDescriptor _desc = PdfFontDescriptor.create(doc);
        _desc.set("X_PsName", PdfString.of(_afm.getPSName()));
        //_desc.setFontName(_afm.getFontName().replaceAll("\\s+", "-"));
        _desc.setFontName(_afm.getPSName());
        _desc.setFontFamily(_afm.getFontName());
        _desc.setStemV(0);
        _desc.setItalicAngle(_afm.getItalicAngle());

        includePfb(doc, _desc, _rl);

        _pdfFont.setFontDescriptor(_desc);
        return _pdfFont;
    }

    @SneakyThrows
    public static PdfFont of(PdfDocument doc, String _cs, PdfResourceLoader _pfbl, PdfResourceLoader _afml)
    {
        PdfFont _pdfFont = null;
        AFM _afm = new AFM(_afml.getInputStream());
        Character[] _charset = AFM.loadCharset(_cs == null ? PDF.ENCODING_PDFDOC : _cs);
        String[] _glyphs = _afm.getGlyphNames(_cs);
        int[] _widths = new int[_charset.length];
        for(int _i = 0; _i<_widths.length; _i++)
        {
            _widths[_i] = _afm.getWidth(_glyphs[_i]);
        }
        String _name = _afm.getFontName();

        _pdfFont = new PdfType1Font(doc, _cs == null ? PDF.ENCODING_PDFDOC : _cs, _name, 0, _glyphs, _widths);
        _pdfFont.setBaseFont(_afm.getFullName());
        PdfFontDescriptor _desc = PdfFontDescriptor.create(doc);
        _desc.set("X_PsName", PdfString.of(_afm.getFullName()));
        _desc.setFontName(_afm.getFontName());
        _desc.setFontFamily(_afm.getFamilyName());
        _desc.setStemV(0);
        _desc.setItalicAngle(_afm.getItalicAngle());

        includePfb(doc, _desc, _pfbl);

        _pdfFont.setFontDescriptor(_desc);
        return _pdfFont;
    }

    @SneakyThrows
    private static void includePfb(PdfDocument doc, PdfFontDescriptor _desc, PdfResourceLoader _pfbl)
    {
        if(_pfbl!=null)
        {
            PdfFontFileStream _fs = PdfFontFileStream.create(doc);
            _fs.setFlateFilter();
            OutputStream _out = _fs.getOutputStream();
            byte[] _buf = CommonUtil.toByteArray(_pfbl.getInputStream());
            int _offs = 0;
            for(int _i = 0; _i<3; _i++)
            {
                int _code = _buf[_offs] & 0xff;
                if(_code!= 0x80) throw new IllegalArgumentException(_pfbl.getName());

                int _type = _buf[_offs+1] & 0xff;
                int _size = _buf[_offs+2] & 0xff;
                _size |= (_buf[_offs+3] & 0xff) << 8;
                _size |= (_buf[_offs+4] & 0xff) << 16;
                _size |= (_buf[_offs+5] & 0xff) << 24;

                _out.write(_buf, _offs+6, _size);
                if(_i==0) _fs.setLength1(_size);
                if(_i==1) _fs.setLength2(_size);
                if(_i==2) _fs.setLength3(_size);
                _out.flush();
                _offs += _size+6;
            }

            _desc.setFontFile(_fs);
        }
    }
}
