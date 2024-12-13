package com.github.terefang.jmelange.pdf.core.fonts;

import com.github.terefang.jmelange.commons.util.GuidUtil;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.*;
import com.github.terefang.jmelange.fonts.sfnt.subsetter.GlyphCoverage;
import com.github.terefang.jmelange.fonts.sfnt.subsetter.HintStripper;
import com.github.terefang.jmelange.fonts.sfnt.subsetter.RenumberingSubsetter;
import com.github.terefang.jmelange.fonts.sfnt.subsetter.Subsetter;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.commons.loader.*;
import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.fonts.sfnt.SfntUtil;
import com.github.terefang.jmelange.pdf.core.values.PdfDict;
import com.github.terefang.jmelange.pdf.core.values.PdfHex;
import com.github.terefang.jmelange.pdf.core.values.PdfResource;
import com.github.terefang.jmelange.pdf.core.values.PdfString;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.FontFactory;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.Tag;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.truetype.Glyph;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.truetype.GlyphTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.truetype.LocaTable;
import lombok.SneakyThrows;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class PdfTtfFont extends PdfBaseFont
{
    public static final PdfFontResource createResource(PdfTtfFont _f)
    {
        return PdfFont.createResource(_f, "FT");
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

    public PdfTtfFont(PdfDocument doc, String _cs, String _name, int _first, String[] _glyphs, int[] _widths, boolean _otf, boolean _cff) {
        super(doc, _cs, _name, _first, _glyphs, _widths, _otf, _cff);
        this.setSubtype("TrueType");
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
    public static PdfFont of(PdfDocument doc, Font _afm, String _cs, ResourceLoader _rl)
    {
        PdfFont _pdfFont = null;
        _cs = _cs == null ? PDF.ENCODING_PDFDOC : _cs;

        Character[] _charset = AFM.loadCharset(_cs);
        String[] _glyphs = AFM.getGlyphNamesBase(_charset);
        int[] _widths = new int[_charset.length];
        String _name = null;
        CMap   _map  = null;

        if(_rl!=null)
        {
            com.github.terefang.jmelange.fonts.sfnt.sfntly.Font[] _sfonts = FontFactory.getInstance().loadFonts(_rl.getInputStream());
            com.github.terefang.jmelange.fonts.sfnt.sfntly.Font   _sfont  = _sfonts[0];
            _name = _rl.getName();
            if(_name.lastIndexOf('/')>0) _name = _name.substring(_name.lastIndexOf('/')+1);
            _name = (_name.replaceAll("[^a-zA-z0-9]+", "_"));
            FontHeaderTable _head  = (FontHeaderTable)_sfont.getTable(Tag.head);
            int             emUnit = _head.unitsPerEm();
            MaximumProfileTable _maxp = (MaximumProfileTable)_sfont.getTable(Tag.maxp);
            int numGlyphs = _maxp.numGlyphs();
            _map = SfntUtil.findCMap(_sfont, false);

            if(_map!=null)
            {
                HorizontalMetricsTable _hmtx = (HorizontalMetricsTable)_sfont.getTable(Tag.hmtx);
                for(int _i = 0; _i<_widths.length; _i++)
                {
                    int _g = _map.glyphId(_charset[_i].charValue());
                    _widths[_i] = _hmtx.advanceWidth(_g)*1000/emUnit;
                }
            }
            else
            {
                awtToMetrics(_afm, _widths, _charset);
            }
        }
        else
        {
            _name = awtToMetrics(_afm, _widths, _charset);
        }

        if(doc.isObfuscate())
        {
            _name = GuidUtil.toHashGUID(_name).substring(0,8);
        }

        _pdfFont = new PdfTtfFont(doc, _cs, "TT-"+_name, 0, _glyphs, _widths, false, false);
        _pdfFont.setFontName(makeFontSubsetTag(_pdfFont.getRef().getValue(), "TT", _name));
        _pdfFont.setFontDescriptor(PdfFontDescriptor.create(doc));
        if(_afm!=null)
        {
            _pdfFont.getFontDescriptor().set("X_PsName", PdfString.of(_afm.getPSName()));
            _pdfFont.getFontDescriptor().setFontName(_afm.getPSName());
            _pdfFont.getFontDescriptor().setItalicAngle(_afm.getItalicAngle());
        }

        includeTTf(doc, _pdfFont, _pdfFont.getFontDescriptor(), _rl,_charset, _map);

        return _pdfFont;
    }

    private static String awtToMetrics(Font _afm, int[] _widths, Character[] _charset)
    {
        if(_afm!=null)
        {
            Font _font = _afm.deriveFont(1000f);
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
            return _font.getFontName();
        }
        return null;
    }

    @SneakyThrows
    private static void includeTTf(PdfDocument doc, PdfFont _font, PdfFontDescriptor _desc, ResourceLoader _rl, Character[] _charset, CMap _map)
    {
        if(_rl!=null)
        {
            _desc.setFontName(_rl.getName().replaceAll("[^a-zA-Z0-9]+", "_"));
            FontFactory                                           _sffactory = FontFactory.getInstance();
            com.github.terefang.jmelange.fonts.sfnt.sfntly.Font[] _sfonts    = _sffactory.loadFonts(_rl.getInputStream());
            com.github.terefang.jmelange.fonts.sfnt.sfntly.Font   _sfont     = _sfonts[0];

            FontHeaderTable _head = (FontHeaderTable)_sfont.getTable(Tag.head);
            int emUnit = _head.unitsPerEm();
            emUnit = emUnit==0 ? 1000 : emUnit;
            _desc.setFlags(_head.flagsAsInt());
            _desc.setFontBBox(_head.xMin(), _head.yMin(), _head.xMax(), _head.yMax());

            PostScriptTable _pst = (PostScriptTable)_sfont.getTable(Tag.post);
            _desc.setItalicAngle(_pst.italicAngle());

            OS2Table _os2 = (OS2Table)_sfont.getTable(Tag.OS_2);
            HorizontalHeaderTable _hhea = (HorizontalHeaderTable)_sfont.getTable(Tag.hhea);
            if(_os2!=null)
            {
                if(_os2.dataLength()>2)
                {
                    _desc.setAverageWidth(_os2.xAvgCharWidth()*1000/emUnit);
                }

                if(_os2.dataLength()>70)
                {
                    _desc.setAscent(_os2.sTypoAscender()*1000/emUnit);
                    _font.setFontAscent(_os2.sTypoAscender()*1000/emUnit);

                    _desc.setDescent(_os2.sTypoDescender()*1000/emUnit);
                    _font.setFontDescent(_os2.sTypoDescender()*1000/emUnit);
                }
                else
                {
                    _desc.setAscent(_hhea.ascender()*1000/emUnit);
                    _font.setFontAscent(_hhea.ascender()*1000/emUnit);

                    _desc.setDescent(_hhea.descender()*1000/emUnit);
                    _font.setFontDescent(_hhea.descender()*1000/emUnit);
                }

                if(_os2.dataLength()>42)
                {
                    PdfDict _style = PdfDict.create();
                    byte[] _u8 = _os2.panose();
                    _style.set("Panose", PdfHex.of(_u8));
                    _desc.set("Style", _style);

                }
            }

            if((_os2!=null) && (_os2.dataLength()>88))
            {
                _desc.setCapHeight(_os2.sCapHeight()*1000/emUnit);
                _font.setFontCapHeight(_os2.sCapHeight()*1000/emUnit);

                _desc.setXHeight(_os2.sxHeight()*1000/emUnit);
                _font.setFontXHeight(_os2.sxHeight()*1000/emUnit);
            }
            else if(_sfont.hasTable(Tag.loca) && (_map!=null))
            {
                /* this is our last resort, measure the canonical glyphs */
                LocaTable _loca = (LocaTable)_sfont.getTable(Tag.loca);
                GlyphTable _glyf = (GlyphTable)_sfont.getTable(Tag.glyf);
                int _iH = _map.glyphId('H');
                Glyph _g = _glyf.glyph(_loca.glyphOffset(_iH),_loca.glyphLength(_iH));
                if(_g.numberOfContours()>0)
                {
                    _desc.setCapHeight(_g.yMax()*1000/emUnit);
                    _font.setFontCapHeight(_g.yMax()*1000/emUnit);
                }

                int _ix = _map.glyphId('x');
                _g = _glyf.glyph(_loca.glyphOffset(_ix),_loca.glyphLength(_ix));
                if(_g.numberOfContours()>0)
                {
                    _desc.setXHeight(_g.yMax()*1000/emUnit);
                    _font.setFontXHeight(_g.yMax()*1000/emUnit);
                }
            }

            try
            {
                NameTable _nt  = (NameTable)_sfont.getTable(Tag.name);
                boolean   _ffs = false;
                boolean _fns = false;
                for(int[] _pfEn : SfntUtil.platformEncoding)
                {
                    try
                    {
                        for(NameTable.NameEntry _nameEntry : _nt.names())
                        {
                            if(_nameEntry.platformId()==_pfEn[0]
                                    && _nameEntry.encodingId()==_pfEn[1]
                                    && _nameEntry.languageId()==_pfEn[2])
                            {
                                if(!_ffs && _nameEntry.nameId()==NameTable.NameId.FontFamilyName.value())
                                {
                                    _desc.setFontFamily(_nameEntry.name());
                                    _ffs=true;
                                    break;
                                }
                                else
                                if(!_fns && _nameEntry.nameId()==NameTable.NameId.FullFontName.value())
                                {
                                    _desc.setFontName(_nameEntry.name().replaceAll("\\s+", ""));
                                    _fns=true;
                                    break;
                                }
                            }
                        }
                    } catch (Exception _xe) {}
                }
            } catch (Exception _xe) {}

            _desc.setStemV(0);
            PdfFontFileStream _fs = PdfFontFileStream.create(doc);
            _fs.setFlateFilter();

            ResourceLoader _xrl = _rl;
            if(!_sfont.hasTable(Tag.CFF))
            {
                try {
                    _xrl = subsetFontFile(_rl.getName(), _sffactory, _sfont, _charset, true);
                }
                catch (Exception _xe) { /* IGNORE */ }
            }
            _fs.putStream(_xrl);
            _fs.setLength1(_fs.getBuf().toByteArray().length);
            _desc.setFontFile2(_fs);
        }
    }

    public static ResourceLoader subsetFontFile(String _name, FontFactory _sffactory, com.github.terefang.jmelange.fonts.sfnt.sfntly.Font _sfont, Character[] _charset, boolean _strip) throws IOException
    {
        List<CMapTable.CMapId> cmapIds = new ArrayList<>();
        cmapIds.add(CMapTable.CMapId.WINDOWS_BMP);

        com.github.terefang.jmelange.fonts.sfnt.sfntly.Font newFont   = _sfont;
        Subsetter                                           subsetter = new RenumberingSubsetter(newFont, _sffactory);
        subsetter.setCMaps(cmapIds, 1);
        List<Integer> glyphs = GlyphCoverage.getGlyphCoverage(_sfont, _charset);
        subsetter.setGlyphs(glyphs);
        Set<Integer> removeTables = new HashSet<>();
        // Most of the following are valid tables, but we don't renumber them yet, so strip
        removeTables.add(Tag.GDEF);
        removeTables.add(Tag.GPOS);
        removeTables.add(Tag.GSUB);
        removeTables.add(Tag.kern);
        removeTables.add(Tag.hdmx);
        removeTables.add(Tag.vmtx);
        removeTables.add(Tag.VDMX);
        removeTables.add(Tag.LTSH);
        removeTables.add(Tag.DSIG);
        removeTables.add(Tag.vhea);
        // AAT tables, not yet defined in sfntly Tag class
        removeTables.add(Tag.intValue(new byte[] {'m', 'o', 'r', 't'}));
        removeTables.add(Tag.intValue(new byte[] {'m', 'o', 'r', 'x'}));
        subsetter.setRemoveTables(removeTables);
        newFont = subsetter.subset().build();
        if (_strip) {
            Subsetter hintStripper = new HintStripper(newFont, _sffactory);
            removeTables = new HashSet<>();
            removeTables.add(Tag.fpgm);
            removeTables.add(Tag.prep);
            removeTables.add(Tag.cvt);
            removeTables.add(Tag.hdmx);
            removeTables.add(Tag.VDMX);
            removeTables.add(Tag.LTSH);
            removeTables.add(Tag.DSIG);
            removeTables.add(Tag.vhea);
            hintStripper.setRemoveTables(removeTables);
            newFont = hintStripper.subset().build();
        }

        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        _sffactory.serializeFont(newFont, _baos);
        _baos.flush();
        _baos.close();

        return ByteArrayResourceLoader.of(_name+"-"+UUID.randomUUID().toString(), _baos.toByteArray(), null);
    }
}
