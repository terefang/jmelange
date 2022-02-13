package com.github.terefang.jmelange.pdf.ml.script;


import com.github.terefang.jmelange.data.util.HsonUtil;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import com.github.terefang.jmelange.pdf.ext.text.Cell;
import com.github.terefang.jmelange.pdf.ml.PmlParser;
import com.github.terefang.jmelange.pdf.ml.PmlParserContext;
import com.github.terefang.jmelange.pdf.ml.PmlUtil;
import com.hubspot.jinjava.Jinjava;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.codehaus.plexus.util.IOUtil;
import org.slf4j.Marker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Data
public class AbstractPmlScriptContext {

    public static StreamTokenizer initializeStreamTokenizer(Reader _reader) {
        StreamTokenizer st = new StreamTokenizer(_reader);
        st.commentChar('#');
        st.eolIsSignificant(true);
        st.lowerCaseMode(true);
        st.wordChars('a', 'z');
        st.wordChars('A', 'Z');
        st.wordChars('-', '-');
        st.wordChars('_', '_');
        st.parseNumbers();
        st.whitespaceChars(0, 9);
        st.whitespaceChars(11, 12);
        st.whitespaceChars(14, 32);
        st.quoteChar('"');
        st.quoteChar('~');
        st.quoteChar('|');
        st.quoteChar('\'');
        st.slashSlashComments(true);
        st.slashStarComments(true);
        return st;
    }

    Marker marker;

    public static void extractBindings(AbstractPmlScriptContext _psc, Properties _p)
    {
        if(_p==null || _p.size()==0) return;

        for(String _key : _p.stringPropertyNames())
        {
            String _val = _p.getProperty(_key);
            if(_key.startsWith("int:"))
            {
                _psc.getBinding().put(_key.substring(4), CommonUtil.createInteger(_val));
            }
            else
            if(_key.startsWith("i:"))
            {
                _psc.getBinding().put(_key.substring(2),CommonUtil.createInteger(_val));
            }
            else
            if(_key.startsWith("float:"))
            {
                _psc.getBinding().put(_key.substring(6),CommonUtil.toFloat(_val));
            }
            else
            if(_key.startsWith("f:"))
            {
                _psc.getBinding().put(_key.substring(2),CommonUtil.toFloat(_val));
            }
            else
            if(_key.startsWith("long:"))
            {
                _psc.getBinding().put(_key.substring(5),CommonUtil.toLong(_val));
            }
            else
            if(_key.startsWith("l:"))
            {
                _psc.getBinding().put(_key.substring(2),CommonUtil.toLong(_val));
            }
            else
            if(_key.startsWith("double:"))
            {
                _psc.getBinding().put(_key.substring(7),CommonUtil.toDouble(_val));
            }
            else
            if(_key.startsWith("d:"))
            {
                _psc.getBinding().put(_key.substring(2),CommonUtil.toDouble(_val));
            }
            else
            if(_key.startsWith("bool:"))
            {
                _psc.getBinding().put(_key.substring(5), CommonUtil.checkBoolean(_val));
            }
            else
            if(_key.startsWith("b:"))
            {
                _psc.getBinding().put(_key.substring(2),CommonUtil.checkBoolean(_val));
            }
            else
            if(_key.startsWith("string:"))
            {
                _psc.getBinding().put(_key.substring(7),_val);
            }
            else
            if(_key.startsWith("s:"))
            {
                _psc.getBinding().put(_key.substring(2),_val);
            }
            else
            if(_key.startsWith("_"))
            {
                _psc.getBinding().put(_key,_val);
            }
            else
            if("json".equalsIgnoreCase(_key))
            {
                _psc.loadContextFromHjson(new InputStreamReader(PmlUtil.sourceToLoader(_val, _psc.getParserContext().getBasedir(), _psc.getParserContext().getFile().getParentFile(), Collections.emptyMap(), Collections.emptyList()).getInputStream(), StandardCharsets.UTF_8));
            }
        }
    }

    @SneakyThrows
    public void loadContextFromHjson(Reader _source)
    {
        Map<String, Object> _map = HsonUtil.loadContextFromHjson(_source);
        this.getBinding().putAll(_map);
        IOUtil.close(_source);
    }



    static Jinjava jinjava = new Jinjava();

    @SneakyThrows
    public static Reader applyTemplating(Map<String, Object> _context, Reader _source)
    {
        String renderedTemplate = jinjava.render(IOUtil.toString(_source), _context);
        return new StringReader(renderedTemplate);
    }

    public PmlParser parser;
    public PmlParserContext parserContext;
    public PdfPage page;
    public PdfContent content;
    public Map<String, Object> binding;

    public PdfPage getPage()
    {
        if(page==null)
        {
            this.page = this.getParser().getPage();
        }
        return page;
    }

    public PdfFontRegistry getRegistry()
    {
        return this.getParser()._reg;
    }

    public PdfDocument getDocument()
    {
        return this.getParser()._pdf;
    }

    public void setTitle(String title) {
        getDocument().setTitle(title);
    }

    public void setCreator(String creator) {
        getDocument().setCreator(creator);
    }

    public void setAuthor(String author) {
        getDocument().setAuthor(author);
    }

    public void setSubject(String subject) {
        getDocument().setSubject(subject);
    }

    public void setKeywords(String subject) {
        getDocument().setKeywords(subject);
    }

    public void setProducer(String producer) {
        getDocument().setProducer(producer);
    }

    public void setJpegCompression(float _f)
    {
        getDocument().setJpegCompression(_f/100f);
    }

    public void newPage()
    {
        this.page = this.getParser().newPage();
        this.content = null;
    }

    public void newPage(int w, int h)
    {
        this.newPage(0,0, w, h);
    }

    public void newPage(int x0, int y0, int x1, int y1)
    {
        this.newPage();
        this.page.setMediabox(x0, y0, x1, y1);
    }

    public void pageMediabox(List<String> args)
    {
        if(args.size()==2)
        {
            this.pageMediabox((int)CommonUtil.toDouble(args.get(0)),
                    (int)CommonUtil.toDouble(args.get(1)));
        }
        else
        if(args.size()==4)
        {
            this.pageMediabox((int)CommonUtil.toDouble(args.get(0)),
                    (int)CommonUtil.toDouble(args.get(1)),
                    (int)CommonUtil.toDouble(args.get(2)),
                    (int)CommonUtil.toDouble(args.get(3)));
        }
    }

    public void pageMediabox(int w, int h)
    {
        this.pageMediabox(0,0, w, h);
    }

    public void pageMediabox(int x0, int y0, int x1, int y1)
    {
        this.page.setMediabox(x0, y0, x1, y1);
    }

    @SneakyThrows
    public void endPage()
    {
        if(this.page!=null) this.getPage().getDoc().streamOut(this.page);
        this.page = null;
        this.content = null;
    }

    public void placeImage(String _ref, int _w, int _h)
    {
        this.getContent(false).image(this.getParser().IMG_REGISTRY.get(_ref), _w, _h);
    }

    public void placeSVG(String _ref, int _w, int _h)
    {
        if(this.getParser().SVG_REGISTRY.containsKey(_ref))
        {
            this.getContent(false).form(this.getParser().SVG_REGISTRY.get(_ref), _w, _h);
        }
        else
        {
            this.getContent(false).image(this.getParser().IMG_REGISTRY.get(_ref), _w, _h);
        }
    }

    public void placeImage(String _ref, int _x, int _y, int _w, int _h, boolean _bg, String _layer)
    {
        this.getParser().placeImage(_ref, _x, _y, _w, _h, _bg, _layer);
    }

    public void placeSvg(String _ref, int _x, int _y, int _w, int _h, boolean _bg, String _layer)
    {
        this.getParser().placeSvg(_ref, _x, _y, _w, _h, _bg, _layer);
    }

    public void placeHeading(int _x, int _y, int _level, String _fn, String _text)
    {
        this.placeHeading("", _x, _y, _level, _fn, -1, _text, "");
    }

    public void placeHeading(int _x, int _y, int _level, String _fn, String _text, String _outline)
    {
        this.placeHeading("", _x, _y, _level, _fn, -1, _text, _outline);
    }

    public void placeHeading(String _id, int _x, int _y, int _level, String _fn, String _text, String _outline)
    {
        this.placeHeading(_id, _x, _y, _level, _fn, -1, _text, _outline);
    }

    public void placeHeading(String _id, int _x, int _y, int _level, String _fn, int _fs, String _text, String _outline)
    {
        this.placeHeading(_id, _x, _y, _level, _fn, _fs, "#000", _text, _outline);
    }

    public void placeHeading(String _id, int _x, int _y, int _level, String _fn, int _fs, String _color, String _text, String _outline)
    {
        this.placeHeading(_id, _x, _y, _level, _fn, _fs, _color, _text, _outline, "false");
    }

    public void placeHeading(String _id, int _x, int _y, int _level, String _fn, int _fs, String _color, String _text, String _outline, String _layer)
    {
        this.getParser().placeHeading(_id, _x, _y, _level, _fn, _fs, _color, _text, _outline, _layer);
    }

    public PdfContent getContent(boolean _bg)
    {
        if(this.content==null)
        {
            this.content = _bg ? this.getParser().getBackground() : this.getPage().newContent();
        }
        return this.content;
    }

    public PdfContent getContent()
    {
        return getContent(false);
    }

    public PdfContent newContent()
    {
        this.content = this.getPage().newContent();
        return this.content;
    }

    public PdfContent newContent(boolean _bg)
    {
        this.content = _bg ? this.getParser().getBackground() : this.getPage().newContent();
        return this.content;
    }

    @SneakyThrows
    public void endContent()
    {
        if(this.content!=null) this.getPage().getDoc().streamOut(this.content);
        this.content = null;
    }

    public void addContent(List<String> _args)
    {
        for(String _chunk : _args)
        {
            this.getContent(false).addContent(_chunk);
        }
    }

    public void addContent(String... _args)
    {
        addContent(Arrays.asList(_args));
    }

    public void addContent(String _arg)
    {
        addContent(Collections.singletonList(_arg));
    }

    public void loadFont(List<String> _args)
    {
        if(_args.size()==2)
        {
            this.loadFont(_args.get(0), _args.get(1));
        }
        else
        if(_args.size()==3)
        {
            this.loadFont(_args.get(0), _args.get(1), _args.get(2));
        }
        if(_args.size()==4)
        {
            this.loadFont(_args.get(0), _args.get(1), _args.get(2), _args.get(3));
        }
    }

    public void loadFont(String _id, String _src)
    {
        loadFont(_id, _src, "pdfdoc");
    }

    public void loadFont(String _id, String _src, String _cs)
    {
        loadFont(_id, _src, _cs, null);
    }

    public void loadFont(String _id, String _src, String _cs, String _im)
    {
        this.getParser().registerFont(_id, _src, null, _cs,
                this.getParserContext().getBasedir(),
                this.getParserContext().getFile().getParentFile());
        if("icons".equalsIgnoreCase(_cs) && CommonUtil.isNotBlank(_im))
        {
            this.getParser().registerIconMap(_id, _im,
                    this.getParserContext().getBasedir(),
                    this.getParserContext().getFile().getParentFile());
        }
    }

    public void mountZip(String _src)
    {
        this.getParser().mountZip(_src,
                this.getParserContext().getBasedir(),
                this.getParserContext().getFile().getParentFile());
    }

    public void mountDir(String _src)
    {
        this.getParser().mountDir(_src,
                this.getParserContext().getBasedir(),
                this.getParserContext().getFile().getParentFile());
    }

    public void defineEntity(String _name, int _code)
    {
        this.getParser().defineEntityOrIcon(null, null, _name, _code, false);
    }

    public void defineIcon(String _font, String _name, int _code)
    {
        this.getParser().defineEntityOrIcon(null, _font, _name, _code, true);
    }

    public void loadImage(String _id, String _src)
    {
        this.getParser().loadImage(_id, _src, "off", "off", 100, 0, this.getParserContext());
    }

    public void loadImage(String _id, String _src, String _compress)
    {
        this.getParser().loadImage(_id, _src, _compress, "off", 100, 0, this.getParserContext());
    }

    public void loadImage(String _id, String _src, String _compress, String _transp, float _alphabias)
    {
        this.getParser().loadImage(_id, _src, _compress, _transp, _alphabias, 0, this.getParserContext());
    }

    public void loadImage(String _id, String _src, String _compress, String _transp, float _alphabias, int _rot)
    {
        this.getParser().loadImage(_id, _src, _compress, _transp, _alphabias, _rot, this.getParserContext());
    }

    public void loadSvg(String _id, String _src, float _s, String _compress, String _transp, float _alphabias, int _rot)
    {
        this.getParser().loadSvg(_id, _src, _s, _compress, _transp, _alphabias, _rot, this.getParserContext());
    }

    public void gSave()
    {
        this.getContent(false).save();
    }

    public void gRestore()
    {
        this.getContent(false).restore();
    }

    public void startText()
    {
        this.getContent(false).startText();
    }

    public void endText()
    {
        this.getContent(false).endText();
    }

    public void moveText(String... _args)
    {
        moveText(Arrays.asList(_args));
    }

    public void moveText(List<String> _args)
    {
        moveText(CommonUtil.toDouble(_args.get(0)),
                CommonUtil.toDouble(_args.get(1)));
    }

    public void moveText(int x, int y)
    {
        moveText((double)x, (double)y);
    }

    public void moveText(float x, float y)
    {
        moveText((double)x, (double)y);
    }

    public void moveText(double x, double y)
    {
        this.getContent(false).moveText(x,y);
    }

    public void font(String... _args)
    {
        font(Arrays.asList(_args));
    }

    public void font(List<String> _args)
    {
        String _fontName = _args.get(0);
        double _fontSize = CommonUtil.toDouble(_args.get(1));
        this.font(_fontName, _fontSize);
    }

    public void font(String _name, int _size)
    {
        this.font(_name, (double)_size);
    }

    public void font(String _name, float _size)
    {
        this.font(_name, (double)_size);
    }

    public void font(String _name, double _size)
    {
        PdfFont _font = this.getRegistry().lookupFont(_name);
        this.getContent(false).setFont(_font, _size);
        this.getContent(false).font(_font, _size);
    }

    public void setFont(String _name, double _size)
    {
        PdfFont _font = this.getRegistry().lookupFont(_name);
        this.getContent(false).setFont(_font, _size);
    }

    public void text(String _arg)
    {
        text(Collections.singletonList(_arg));
    }

    public void text(String... _args)
    {
        text(Arrays.asList(_args));
    }

    public void text(List<String> _args)
    {
        for (String _text : _args) {
            this.getContent(false).text(this.getParser().mapEntitiesToString(_text));
        }
    }

    public void textCenter(String _arg)
    {
        this.textCenter(_arg, 0);
    }

    public void textCenter(String _arg, float _w)
    {
        if(_w!=0f) this.getContent(false).moveText(_w/2, 0);
        this.getContent(false).text_center(_arg);
    }

    public void drawString(String _text, float _x, float _y)
    {
        this.getContent(false).drawString(_text, _x, _y);
    }

    public void drawString(String _text, float _x, float _y, String _align)
    {
        this.getContent(false).drawString(_text, _x, _y, _align);
    }

    public void drawString(String _text, float _x, float _y, String _align, float _w)
    {
        this.getContent(false).drawString(_text, _x, _y, _align, _w);
    }

    public void drawString(String _text, float _x, float _y, String _align, float _w, float _hs)
    {
        this.getContent(false).drawString(_text, _x, _y, _align, _w, _hs);
    }

    public double textWidth(String _text)
    {
        return this.getContent(false).advancewidth(this.getParser().mapEntitiesToString(_text));
    }

    public double textWidth(String _text, String _fn, float _size)
    {
        return this.textWidth(_text, _fn, _size, 0f, 0f, 100f);
    }

    public double textWidth(String _text, String _fn, float _size, float _wsp, float _csp, float _hsp)
    {
        return PDF.advancewidth(this.getParser().mapEntitiesToString(_text), this.getRegistry().lookupFont(_fn), _size, _wsp, _csp, _hsp);
    }

    public void renderText(String _arg)
    {
        this.renderText(_arg, "left", 0);
    }

    public void renderText(String _arg, String _align, float _w)
    {
        String _fn = this.getContent(false).getFont().getFontName();
        float _fs = (float) this.getContent(false).getFontSize();
        this.renderText(_arg, _align, _w, _fn, _fs);
    }

    public void renderText(String _arg, String _align, float _w, String _fn, float _fs)
    {
        boolean _sbf = false;
        List<Cell> _ccq = new Vector<>();
        Cell _cell = this.getParser().mapTextToCell(_arg, _fn, (int) _fs, _sbf);
        _ccq.add(_cell);
        List<Cell[]> _cells = this.getParser().layoutCells(_ccq, _w, (float) this.getContent(false).gethScale(), false, _align, PmlParser.SOURCE_TYPE_TEXT, false);
        this.getParser().renderCells(_cells, this.getContent(false), _w, 0, _fs, (float) this.getContent(false).gethScale(), _align, false);
    }

    public void renderText(List<String> _args)
    {
        if(_args.size()==1)
        {
            this.renderText(_args.get(0));
        }
        else
        if(_args.size()==2)
        {
            this.renderText(_args.get(0), _args.get(1), 0);
        }
        else
        if(_args.size()==3)
        {
            this.renderText(_args.get(0), _args.get(1), CommonUtil.toFloat(_args.get(2)));
        }
        else
        if(_args.size()==5)
        {
            this.renderText(_args.get(0), _args.get(1), CommonUtil.toFloat(_args.get(2)), _args.get(3), CommonUtil.toFloat(_args.get(4)));
        }
        else
        {
            throw new IllegalArgumentException(_args.toString());
        }
    }

    public void startLayer(List<String> _args)
    {
        startLayer(_args.get(0));
    }

    public void startLayer(String... _args)
    {
        startLayer(_args[0]);
    }

    public void startLayer(String _arg)
    {
        this.getContent(false).startLayer(_arg);
    }

    public void endLayer()
    {
        this.getContent(false).endLayer();
    }

    public void fillAlpha(List<String> args)
    {
        this.fillAlpha(CommonUtil.checkDouble(args.get(0)).floatValue()/100f);
    }

    public void strokeAlpha(List<String> args)
    {
        this.strokeAlpha(CommonUtil.checkDouble(args.get(0)).floatValue()/100f);
    }

    public void fillAlpha(int _a)
    {
        this.fillAlpha((float) _a/255f);
    }

    public void strokeAlpha(int _a)
    {
        this.strokeAlpha((float) _a/255f);
    }

    public void fillAlpha(float _a)
    {
        this.getContent(false).fillAlpha(_a);
    }

    public void strokeAlpha(float _a)
    {
        this.getContent(false).strokeAlpha(_a);
    }

    public void fillColor(String... _args)
    {
        fillColor(Arrays.asList(_args));
    }

    public void fillColor(List<String> _args)
    {
        color(true, _args);
    }

    public void fillColor(String _arg)
    {
        color(true, _arg);
    }

    public void strokeColor(String... _args)
    {
        strokeColor(Arrays.asList(_args));
    }

    public void strokeColor(List<String> _args)
    {
        color(false, _args);
    }

    public void strokeColor(String _arg)
    {
        color(false, _arg);
    }

    public void color(boolean _fillOrStroke, String _arg)
    {
        if(_fillOrStroke)
        {
            this.getContent(false).fillColor(_arg);
        }
        else
        {
            this.getContent(false).strokeColor(_arg);
        }
    }

    public void fillColor(int _arg)
    {
        this.getContent(false).fillColor(_arg);
    }

    public void strokeColor(int _arg)
    {
        this.getContent(false).strokeColor(_arg);
    }

    public void color(boolean _fillOrStroke, int _arg)
    {
        if(_fillOrStroke)
        {
            this.getContent(false).fillColor(_arg);
        }
        else
        {
            this.getContent(false).strokeColor(_arg);
        }
    }

    public void fillColor(float _arg)
    {
        this.getContent(false).fillColorGray(_arg);
    }

    public void strokeColor(float _arg)
    {
        this.getContent(false).strokeColorGray(_arg);
    }

    public void color(boolean _fillOrStroke, float _arg)
    {
        if(_fillOrStroke)
        {
            this.getContent(false).fillColorGray(_arg/100.0);
        }
        else
        {
            this.getContent(false).strokeColorGray(_arg/100.0);
        }
    }

    public void fillColor(double _arg)
    {
        this.getContent(false).fillColorGray(_arg);
    }

    public void strokeColor(double _arg)
    {
        this.getContent(false).strokeColorGray(_arg);
    }

    public void color(boolean _fillOrStroke, double _arg)
    {
        if(_fillOrStroke)
        {
            this.getContent(false).fillColorGray(_arg/100.0);
        }
        else
        {
            this.getContent(false).strokeColorGray(_arg/100.0);
        }
    }

    public void color(boolean _fillOrStroke, int _r, int _g, int _b)
    {
        if(_fillOrStroke)
        {
            this.getContent(false).fillColor(_r, _g, _b);
        }
        else
        {
            this.getContent(false).strokeColor(_r, _g, _b);
        }
    }

    public void color(boolean _fillOrStroke, double _r, double _g, double _b)
    {
        if(_fillOrStroke)
        {
            this.getContent(false).fillColorRGB(_r/100.0, _g/100.0, _b/100.0);
        }
        else
        {
            this.getContent(false).strokeColorRGB(_r/100.0, _g/100.0, _b/100.0);
        }
    }

    public void color(boolean _fillOrStroke, float _r, float _g, float _b)
    {
        if(_fillOrStroke)
        {
            this.getContent(false).fillColorRGB(_r/100.0, _g/100.0, _b/100.0);
        }
        else
        {
            this.getContent(false).strokeColorRGB(_r/100.0, _g/100.0, _b/100.0);
        }
    }

    public void color(boolean _fillOrStroke, double _c, double _m, double _y, double _k)
    {
        if(_fillOrStroke)
        {
            this.getContent(false).fillColorCMYK(_c/100.0, _m/100.0, _y/100.0, _k/100.0);
        }
        else
        {
            this.getContent(false).strokeColorCMYK(_c/100.0, _m/100.0, _y/100.0, _k/100.0);
        }
    }

    public void color(boolean _fillOrStroke, float _c, float _m, float _y, float _k)
    {
        if(_fillOrStroke)
        {
            this.getContent(false).fillColorCMYK(_c/100.0, _m/100.0, _y/100.0, _k/100.0);
        }
        else
        {
            this.getContent(false).strokeColorCMYK(_c/100.0, _m/100.0, _y/100.0, _k/100.0);
        }
    }

    public void color(boolean _fillOrStroke, int _c, int _m, int _y, int _k)
    {
        if(_fillOrStroke)
        {
            this.getContent(false).fillColor(_c, _m, _y, _k);
        }
        else
        {
            this.getContent(false).strokeColor(_c, _m, _y, _k);
        }
    }

    public void colorHSL(boolean _fillOrStroke, float _h, float _s, float _l)
    {
        if(_fillOrStroke)
        {
            this.getContent(false).fillColorHSL(_h, _s, _l);
        }
        else
        {
            this.getContent(false).strokeColorHSL(_h, _s, _l);
        }
    }

    public void colorHSV(boolean _fillOrStroke, float _h, float _s, float _v)
    {
        if(_fillOrStroke)
        {
            this.getContent(false).fillColorHSV(_h, _s, _v);
        }
        else
        {
            this.getContent(false).strokeColorHSV(_h, _s, _v);
        }
    }

    public void colorHWB(boolean _fillOrStroke, float _h, float _w, float _b)
    {
        if(_fillOrStroke)
        {
            this.getContent(false).fillColorHWB(_h, _w, _b);
        }
        else
        {
            this.getContent(false).strokeColorHWB(_h, _w, _b);
        }
    }

    public void color(boolean _fillOrStroke, List<String> _args)
    {
        if(_args.size()==1)
        {
            this.color(_fillOrStroke, _args.get(0));
        }
        else if(_args.size()==2 && "gray".equalsIgnoreCase(_args.get(0)))
        {
            this.color(_fillOrStroke, (int)CommonUtil.toDouble(_args.get(1)));
        }
        else if(_args.size()==2 && "gray%".equalsIgnoreCase(_args.get(0)))
        {
            this.color(_fillOrStroke, CommonUtil.toDouble(_args.get(1)));
        }
        else if(_args.size()==4 && "rgb".equalsIgnoreCase(_args.get(0)))
        {
            this.color(_fillOrStroke, (int)CommonUtil.toDouble(_args.get(1)),
                    (int)CommonUtil.toDouble(_args.get(2)),
                    (int)CommonUtil.toDouble(_args.get(3)));
        }
        else if(_args.size()==4 && "rgb%".equalsIgnoreCase(_args.get(0)))
        {
            this.color(_fillOrStroke, CommonUtil.toDouble(_args.get(1)),
                    CommonUtil.toDouble(_args.get(2)),
                    CommonUtil.toDouble(_args.get(3)));
        }
        else if(_args.size()==4 && "hsl%".equalsIgnoreCase(_args.get(0)))
        {
            this.colorHSL(_fillOrStroke, CommonUtil.toFloat(_args.get(1)),
                    CommonUtil.toFloat(_args.get(2)),
                    CommonUtil.toFloat(_args.get(3)));
        }
        else if(_args.size()==4 && "hsv%".equalsIgnoreCase(_args.get(0)))
        {
            this.colorHSV(_fillOrStroke, CommonUtil.toFloat(_args.get(1)),
                    CommonUtil.toFloat(_args.get(2)),
                    CommonUtil.toFloat(_args.get(3)));
        }
        else if(_args.size()==4 && "hwb%".equalsIgnoreCase(_args.get(0)))
        {
            this.colorHWB(_fillOrStroke, CommonUtil.toFloat(_args.get(1)),
                    CommonUtil.toFloat(_args.get(2)),
                    CommonUtil.toFloat(_args.get(3)));
        }
        else if(_args.size()==5 && "cmyk".equalsIgnoreCase(_args.get(0)))
        {
            this.color(_fillOrStroke, CommonUtil.checkDouble(_args.get(1)).intValue(),
                    CommonUtil.checkDouble(_args.get(2)).intValue(),
                    CommonUtil.checkDouble(_args.get(3)).intValue(),
                    CommonUtil.checkDouble(_args.get(4)).intValue());
        }
        else if(_args.size()==5 && "cmyk%".equalsIgnoreCase(_args.get(0)))
        {
            this.color(_fillOrStroke, CommonUtil.checkDouble(_args.get(1)),
                    CommonUtil.checkDouble(_args.get(2)),
                    CommonUtil.checkDouble(_args.get(3)),
                    CommonUtil.checkDouble(_args.get(4)));
        }
        else if(_args.size()==3 && CommonUtil.isNumberCreatable(_args.get(0)))
        {
            this.color(_fillOrStroke, CommonUtil.checkFloat(_args.get(0)),
                    CommonUtil.checkFloat(_args.get(1)),
                    CommonUtil.checkFloat(_args.get(2)));
        }
        else if(_args.size()==4 && CommonUtil.isNumberCreatable(_args.get(0)))
        {
            this.color(_fillOrStroke, CommonUtil.checkFloat(_args.get(0)),
                    CommonUtil.checkFloat(_args.get(1)),
                    CommonUtil.checkFloat(_args.get(2)),
                    CommonUtil.checkFloat(_args.get(3)));
        }
        /*
        else if(_args.size()==2 && !NumberUtils.isCreatable(_args.get(0)))
        {
            String _space = _args.get(0);
            this.color(_fillOrStroke, CommonUtil.checkFloat(_args.get(0)),
                    CommonUtil.checkFloat(_args.get(1)),
                    CommonUtil.checkFloat(_args.get(2)),
                    CommonUtil.checkFloat(_args.get(3)));
        }
        */
    }

    public void hScale(String... _args)
    {
        this.hScale(Arrays.asList(_args));
    }

    public void hScale(List<String> _args)
    {
        this.hScale(CommonUtil.checkDouble(_args.get(0)));
    }

    public void hScale(int _scale)
    {
        this.hScale((double)_scale);
    }

    public void hScale(float _scale)
    {
        this.hScale((double)_scale);
    }

    public void hScale(double _scale)
    {
        this.getContent(false).hscale(_scale);
    }

    public void hLine(String... _args)
    {
        this.hLine(Arrays.asList(_args));
    }

    public void hLine(List<String> _args)
    {
        this.hLine(CommonUtil.checkDouble(_args.get(0)));
    }

    public void hLine(int _scale)
    {
        this.hLine((double)_scale);
    }

    public void hLine(float _scale)
    {
        this.hLine((double)_scale);
    }

    public void hLine(double _scale)
    {
        this.getContent(false).hline(_scale);
    }

    public void vLine(String... _args)
    {
        this.vLine(Arrays.asList(_args));
    }

    public void vLine(List<String> _args)
    {
        this.vLine(CommonUtil.checkDouble(_args.get(0)));
    }

    public void vLine(int _scale)
    {
        this.vLine((double)_scale);
    }

    public void vLine(float _scale)
    {
        this.vLine((double)_scale);
    }

    public void vLine(double _scale)
    {
        this.getContent(false).vline(_scale);
    }

    public void moveTo(String... _args)
    {
        moveTo(Arrays.asList(_args));
    }

    public void moveTo(List<String> _args)
    {
        moveTo(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)));
    }

    public void moveTo(int x, int y)
    {
        moveTo((double)x, (double)y);
    }

    public void moveTo(float x, float y)
    {
        moveTo((double)x, (double)y);
    }

    public void moveTo(double x, double y)
    {
        this.getContent(false).moveTo(x,y);
    }

    public void movePolar(List<String> _args)
    {
        movePolar(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)));
    }

    public void movePolar(int _dL, float _alpha)
    {
        this.movePolar((double) _dL, (double)_alpha);
    }

    public void movePolar(float _dL, float _alpha)
    {
        this.movePolar((double) _dL, (double)_alpha);
    }

    public void movePolar(double _dL, double _alpha)
    {
        double dx = _dL * Math.cos(Math.toRadians(_alpha));
        double dy = _dL * Math.sin(Math.toRadians(_alpha));
        this.moveRel(dx, dy);
    }

    public void moveRel(List<String> _args)
    {
        moveRel(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)));
    }

    public void moveRel(int x, int y)
    {
        moveRel((double)x, (double)y);
    }

    public void moveRel(float x, float y)
    {
        moveRel((double)x, (double)y);
    }

    public void moveRel(double x, double y)
    {
        this.getContent(false).moveTo(x+this.getContent(false).getPx(),y+this.getContent(false).getPy());
    }

    public void moveToX(List<String> _args)
    {
        moveToX(CommonUtil.checkDouble(_args.get(0)));
    }

    public void moveToX(int x)
    {
        moveToX((double)x);
    }

    public void moveToX(float x)
    {
        moveToX((double)x);
    }

    public void moveToX(double x)
    {
        this.getContent(false).moveTo(x, this.getContent(false).getPy());
    }

    public void moveToY(List<String> _args)
    {
        moveToY(CommonUtil.checkDouble(_args.get(0)));
    }

    public void moveToY(int y)
    {
        moveToY((double)y);
    }

    public void moveToY(float y)
    {
        moveToY((double)y);
    }

    public void moveToY(double y)
    {
        this.getContent(false).moveTo(this.getContent(false).getPx(),y);
    }

    public void line(String... _args)
    {
        line(Arrays.asList(_args));
    }

    public void line(List<String> _args)
    {
        line(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)),
                CommonUtil.checkDouble(_args.get(2)),
                CommonUtil.checkDouble(_args.get(3)));
    }

    public void line(int x, int y, int x1, int y1)
    {
        line((double)x, (double)y, (double)x1, (double)y1);
    }

    public void line(float x, float y, float x1, float y1)
    {
        line((double)x, (double)y, (double)x1, (double)y1);
    }

    public void line(double x, double y, double x1, double y1)
    {
        this.getContent(false).moveTo(x,y);
        this.getContent(false).lineTo(x1,y1);
    }

    public void lineTo(String... _args)
    {
        lineTo(Arrays.asList(_args));
    }

    public void lineTo(List<String> _args)
    {
        lineTo(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)));
    }

    public void lineTo(int x, int y)
    {
        lineTo((double)x, (double)y);
    }

    public void lineTo(float x, float y)
    {
        lineTo((double)x, (double)y);
    }

    public void lineTo(double x, double y)
    {
        this.getContent(false).lineTo(x,y);
    }

    public void curveTo(String... _args)
    {
        curveTo(Arrays.asList(_args));
    }

    public void curveTo(List<String> _args)
    {
        if(_args.size()==5)
        {
            this.curveTo(CommonUtil.checkDouble(_args.get(0)),
                    CommonUtil.checkDouble(_args.get(1)),
                    CommonUtil.checkDouble(_args.get(2)),
                    CommonUtil.checkDouble(_args.get(3)),
                    CommonUtil.checkBoolean(_args.get(4)));
        }
        else
        if(_args.size()==6)
        {
            this.curveTo(CommonUtil.checkDouble(_args.get(0)),
                    CommonUtil.checkDouble(_args.get(1)),
                    CommonUtil.checkDouble(_args.get(2)),
                    CommonUtil.checkDouble(_args.get(3)),
                    CommonUtil.checkDouble(_args.get(4)),
                    CommonUtil.checkDouble(_args.get(5)));
        }
        else
        {
            throw new IllegalArgumentException("curveTo - "+ CommonUtil.join(_args.iterator(), ","));
        }
    }

    public void curveTo(int _x1, int _y1, int _x2, int _y2, int _x3, int _y3)
    {
        this.getContent(false).curveTo(_x1, _y1, _x2, _y2, _x3, _y3);
    }

    public void curveTo(int _x2, int _y2, int _x3, int _y3, boolean _se)
    {
        this.getContent(false).curveTo(_x2, _y2, _x3, _y3, _se);
    }

    public void curveTo(float _x1, float _y1, float _x2, float _y2, float _x3, float _y3)
    {
        this.getContent(false).curveTo(_x1, _y1, _x2, _y2, _x3, _y3);
    }

    public void curveTo(float _x2, float _y2, float _x3, float _y3, boolean _se)
    {
        this.getContent(false).curveTo(_x2, _y2, _x3, _y3, _se);
    }

    public void curveTo(double _x1, double _y1, double _x2, double _y2, double _x3, double _y3)
    {
        this.getContent(false).curveTo(_x1, _y1, _x2, _y2, _x3, _y3);
    }

    public void curveTo(double _x2, double _y2, double _x3, double _y3, boolean _se)
    {
        this.getContent(false).curveTo(_x2, _y2, _x3, _y3, _se);
    }

    public void lineWidth(List<String> args)
    {
        this.lineWidth(CommonUtil.checkDouble(args.get(0)).doubleValue());
    }

    public void lineWidth(double _w)
    {
        this.getContent(false).linewidth(_w);
    }

    public void lineWidth(int _w)
    {
        this.getContent(false).linewidth(_w);
    }

    public void lineWidth(float _w)
    {
        this.getContent(false).linewidth(_w);
    }

    public void arc(List<String> _args)
    {
        if(_args.size()==7)
        {
            this.arc(CommonUtil.checkDouble(_args.get(0)),
                    CommonUtil.checkDouble(_args.get(1)),
                    CommonUtil.checkDouble(_args.get(2)),
                    CommonUtil.checkDouble(_args.get(3)),
                    CommonUtil.checkDouble(_args.get(4)),
                    CommonUtil.checkDouble(_args.get(5)));
        }
        else
        if(_args.size()==7)
        {
            this.arc(CommonUtil.checkDouble(_args.get(0)),
                    CommonUtil.checkDouble(_args.get(1)),
                    CommonUtil.checkDouble(_args.get(2)),
                    CommonUtil.checkDouble(_args.get(3)),
                    CommonUtil.checkDouble(_args.get(4)),
                    CommonUtil.checkDouble(_args.get(5)),
                    CommonUtil.checkBoolean(_args.get(6)));
        }
        else
        {
            throw new IllegalArgumentException("arc - "+CommonUtil.join(_args.iterator(), ","));
        }
    }

    public void arc(double _x, double _y, double _a, double _b, double _alpha, double _beta)
    {
        this.arc(_x, _y, _a, _b, _alpha, _beta, false);
    }

    public void arc(double _x, double _y, double _a, double _b, double _alpha, double _beta, boolean _move)
    {
        this.getContent(false).arc(_x, _y, _a, _b, _alpha, _beta, _move);
    }

    public void spline(List<String> _args)
    {
        if(_args.size()==4)
        {
            this.spline(CommonUtil.checkDouble(_args.get(0)),
                    CommonUtil.checkDouble(_args.get(1)),
                    CommonUtil.checkDouble(_args.get(2)),
                    CommonUtil.checkDouble(_args.get(3)));
        }
        else
        {
            throw new IllegalArgumentException("spline - "+CommonUtil.join(_args.iterator(), ","));
        }
    }

    public void spline(double _x1, double _y1, double _x2, double _y2)
    {
        this.getContent(false).spline(_x1, _y1, _x2, _y2);
    }

    public void pen(List<String> _args)
    {
        if(_args.size()==2)
        {
            this.pen(_args.get(0), CommonUtil.checkDouble(_args.get(1)).doubleValue());
        }
        else
        if(_args.size()==3)
        {
            this.pen(_args.get(0), CommonUtil.checkDouble(_args.get(1)).doubleValue(),
                    CommonUtil.checkDouble(_args.get(2)).doubleValue());
        }

        throw new IllegalArgumentException("pen - "+CommonUtil.join(_args.iterator(), ","));
    }

    public void pen(String _color, int _w)
    {
        this.pen(_color, (double)_w);
    }

    public void pen(String _color, int _w, float _a)
    {
        this.pen(_color, (double)_w, (double) _a);
    }

    public void pen(String _color, float _w)
    {
        this.pen(_color, (double)_w);
    }

    public void pen(String _color, float _w, float _a)
    {
        this.pen(_color, (double)_w, (double) _a);
    }

    public void pen(String _color, double _w)
    {
        this.strokeColor(_color);
        this.lineWidth(_w);
    }

    public void pen(String _color, double _w, double _a)
    {
        this.strokeAlpha((float)_a);
        this.strokeColor(_color);
        this.lineWidth(_w);
    }

    public void ellipse(List<String> _args)
    {
        if(_args.size()!=4)
            throw new IllegalArgumentException("ellipse - "+CommonUtil.join(_args.iterator(), ","));

        this.ellipse(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)),
                CommonUtil.checkDouble(_args.get(2)),
                CommonUtil.checkDouble(_args.get(3)));
    }

    public void ellipse(int _x,int _y,int _a,int _b)
    {
        this.getContent(false).ellipse(_x, _y, _a, _b);
    }

    public void ellipse(float _x,float _y,float _a,float _b)
    {
        this.getContent(false).ellipse(_x, _y, _a, _b);
    }

    public void ellipse(double _x,double _y,double _a,double _b)
    {
        this.getContent(false).ellipse(_x, _y, _a, _b);
    }

    public void clip(List<String> _args)
    {
        if(_args.size()>1)
            throw new IllegalArgumentException("clip - "+CommonUtil.join(_args.iterator(), ","));

        if(_args.size()==0)
        {
            this.clip();
        }
        else
        if(_args.size()==1)
        {
            this.clip(CommonUtil.checkBoolean(_args.get(0)));
        }
    }

    public void clip() { this.getContent(false).clip(); }

    public void clip(boolean _evenOdd) { this.getContent(false).clip(_evenOdd); }

    public void circle(List<String> _args)
    {
        if(_args.size()!=3)
            throw new IllegalArgumentException("circle - "+CommonUtil.join(_args.iterator(), ","));

        this.circle(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)),
                CommonUtil.checkDouble(_args.get(2)));
    }

    public void circle(int _x,int _y,int _r)
    {
        this.getContent(false).circle(_x, _y, _r);
    }

    public void circle(float _x,float _y,float _r)
    {
        this.getContent(false).circle(_x, _y, _r);
    }

    public void circle(double _x,double _y,double _r)
    {
        this.getContent(false).circle(_x, _y, _r);
    }

    public void pie(List<String> _args)
    {
        if(_args.size()!=6)
            throw new IllegalArgumentException("pie - "+CommonUtil.join(_args.iterator(), ","));

        this.pie(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)),
                CommonUtil.checkDouble(_args.get(2)),
                CommonUtil.checkDouble(_args.get(3)),
                CommonUtil.checkDouble(_args.get(4)),
                CommonUtil.checkDouble(_args.get(5)));
    }

    public void pie(int _x, int _y, int _a, int _b, float _alpha, float _beta)
    {
        this.getContent(false).pie(_x, _y, _a, _b, _alpha, _beta);
    }

    public void pie(float _x, float _y, float _a, float _b, float _alpha, float _beta)
    {
        this.getContent(false).pie(_x, _y, _a, _b, _alpha, _beta);
    }

    public void pie(double _x, double _y, double _a, double _b, double _alpha, double _beta)
    {
        this.getContent(false).pie(_x, _y, _a, _b, _alpha, _beta);
    }

    public void rect(List<String> _args)
    {
        if(_args.size()!=4)
            throw new IllegalArgumentException("rect - "+CommonUtil.join(_args.iterator(), ","));

        this.rect(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)),
                CommonUtil.checkDouble(_args.get(2)),
                CommonUtil.checkDouble(_args.get(3)));
    }

    public void rect(int _a, int _b, int _c, int _d)
    {
        this.getContent(false).rectangle(_a, _b, _c, _d);
    }

    public void rect(float _a, float _b, float _c, float _d)
    {
        this.getContent(false).rectangle(_a, _b, _c, _d);
    }

    public void rect(double _a, double _b, double _c, double _d)
    {
        this.getContent(false).rectangle(_a, _b, _c, _d);
    }

    public void rectXY(List<String> _args)
    {
        if(_args.size()!=4)
            throw new IllegalArgumentException("rectxy - "+CommonUtil.join(_args.iterator(), ","));

        this.rectXY(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)),
                CommonUtil.checkDouble(_args.get(2)),
                CommonUtil.checkDouble(_args.get(3)));
    }

    public void rectXY(int _a, int _b, int _c, int _d)
    {
        this.getContent(false).rectanglexy(_a, _b, _c, _d);
    }

    public void rectXY(float _a, float _b, float _c, float _d)
    {
        this.getContent(false).rectanglexy(_a, _b, _c, _d);
    }

    public void rectXY(double _a, double _b, double _c, double _d)
    {
        this.getContent(false).rectanglexy(_a, _b, _c, _d);
    }


    public void arrowTo(List<String> _args)
    {
        this.arrowTo(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)),
                CommonUtil.checkDouble(_args.get(2)),
                CommonUtil.checkBoolean(_args.get(3)),
                CommonUtil.checkBoolean(_args.get(4)));
    }

    public void arrowTo(double _x1, double _y1, double _scale, boolean _from, boolean _to)
    {
        this.arrow(this.getContent(false).getPx(), this.getContent(false).getPy(),
                _x1, _y1, _scale, _from, _to);
    }

    public void arrowRel(List<String> _args)
    {
        this.arrowRel(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)),
                CommonUtil.checkDouble(_args.get(2)),
                CommonUtil.checkBoolean(_args.get(3)),
                CommonUtil.checkBoolean(_args.get(4)));
    }

    public void arrowRel(double _x1, double _y1, double _scale, boolean _from, boolean _to)
    {
        this.arrow(this.getContent(false).getPx(), this.getContent(false).getPy(),
                this.getContent(false).getPx()+_x1, this.getContent(false).getPy()+_y1,
                _scale, _from, _to);
    }

    public void arrowPolar(List<String> _args)
    {
        this.arrowPolar(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)),
                CommonUtil.checkDouble(_args.get(2)),
                CommonUtil.checkBoolean(_args.get(3)),
                CommonUtil.checkBoolean(_args.get(4)));
    }

    public void arrowPolar(double _dL, double _alpha, double _scale, boolean _from, boolean _to)
    {
        double _dx = _dL * Math.cos(Math.toRadians(_alpha));
        double _dy = _dL * Math.sin(Math.toRadians(_alpha));
        this.arrowTo(_dx, _dy, _scale, _from, _to);
    }

    public void arrow(List<String> _args)
    {
        this.arrow(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)),
                CommonUtil.checkDouble(_args.get(2)),
                CommonUtil.checkDouble(_args.get(3)),
                CommonUtil.checkDouble(_args.get(4)),
                CommonUtil.checkBoolean(_args.get(5)),
                CommonUtil.checkBoolean(_args.get(6)));
    }

    public void arrow(double _x0, double _y0, double _x1, double _y1, double _scale, boolean _from, boolean _to)
    {
        double _sMax = 15. * _scale;
        double _sMin = 7. * _scale;
        double _h = Math.hypot(_y1-_y0, _x1-_x0);
        double _len = _h / 4d;
        if (_len > _sMax) _len = _sMax;
        if (_len < _sMin) _len = _sMin;
        double _dx = _len * (_x1-_x0) / _h;
        double _dy = _len * (_y1-_y0) / _h;
        if (_h >= _len)
        {
            if(_from)
            {
                this.moveTo(_x0 + _dx/2d, _y0 + _dy/2d);
            }
            else
            {
                this.moveTo(_x0,_y0);
            }

            if(_to)
            {
                this.lineTo(_x1 - _dx/2d,_y1 - _dy/2d);
            }
            else
            {
                this.lineTo(_x1, _y1);
            }
            this.stroke();
        }

        if(_from)
        {
            this.moveTo(_x0,_y0);
            this.lineTo((_x0 + _dx + _dy/3d), (_y0 + _dy - _dx/3d));
            this.lineTo((_x0 + _dx - _dy/3d), (_y0 + _dy + _dx/3d));
            this.closePath();
            this.fill();
        }

        if(_to)
        {
            this.moveTo(_x1,_y1);
            this.lineTo((_x1 - _dx + _dy/3d), (_y1 - _dy - _dx/3d));
            this.lineTo((_x1 - _dx - _dy/3d), (_y1 - _dy + _dx/3d));
            this.closePath();
            this.fill();
        }
    }

    public void matrix(List<String> _args)
    {
        if(_args.size()!=6)
            throw new IllegalArgumentException("matrix - "+CommonUtil.join(_args.iterator(), ","));

        this.matrix(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)),
                CommonUtil.checkDouble(_args.get(2)),
                CommonUtil.checkDouble(_args.get(3)),
                CommonUtil.checkDouble(_args.get(4)),
                CommonUtil.checkDouble(_args.get(5)));
    }

    public void matrix(float _a, float _b, float _c, float _d, float _e, float _f)
    {
        this.getContent(false).matrix(_a, _b, _c, _d, _e, _f);
    }

    public void matrix(double _a, double _b, double _c, double _d, double _e, double _f)
    {
        this.getContent(false).matrix(_a, _b, _c, _d, _e, _f);
    }

    public void rotate(List<String> _args)
    {
        this.rotate(CommonUtil.checkDouble(_args.get(0)));
    }

    public void rotate(double _s)
    {
        this.getContent(false).rotate(_s);
    }

    public void scale(List<String> _args)
    {
        this.scale(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)));
    }

    public void scale(double _sx, double _sy)
    {
        this.getContent(false).scale(_sx, _sy);
    }

    public void skew(List<String> _args)
    {
        this.skew(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)));
    }

    public void skew(double _sx, double _sy)
    {
        this.getContent(false).skew(_sx, _sy);
    }

    public void translate(List<String> _args)
    {
        this.translate(CommonUtil.checkDouble(_args.get(0)),
                CommonUtil.checkDouble(_args.get(1)));
    }

    public void translate(double _tx, double _ty)
    {
        this.getContent(false).translate(_tx, _ty);
    }

    public void stroke()
    {
        this.getContent(false).stroke();
    }

    public void fill(List<String> _args)
    {
        if(_args.size()==0)
        {
            this.fill();
        }
        else
        {
            boolean _b = CommonUtil.checkBoolean(_args.get(0));
            this.fill(_b);
        }
    }

    public void fill()
    {
        this.getContent(false).fill();
    }

    public void fill(boolean _b)
    {
        this.getContent(false).fill(_b);
    }

    public void fillStroke(List<String> _args)
    {
        if(_args.size()==0)
        {
            this.fillStroke();
        }
        else
        {
            boolean _b = CommonUtil.checkBoolean(_args.get(0));
            this.fillStroke(_b);
        }
    }

    public void fillStroke()
    {
        this.getContent(false).fillStroke();
    }

    public void fillStroke(boolean _b)
    {
        this.getContent(false).fillStroke(_b);
    }

    public void lineDash(List<String> _args)
    {
        float[] _fargs = new float[_args.size()];
        for (int _i = 0; _i < _fargs.length; _i++)
        {
            _fargs[_i] = CommonUtil.checkDouble(_args.get(_i)).floatValue();
        }
        this.lineDash(_fargs);
    }

    public void lineDash(float... fargs)
    {
        if(fargs.length==0)
        {
            this.getContent(false).linedash();
        }
        else
        {
            this.lineDashX(0f, fargs);
        }
    }

    public void lineDashX(List<String> _args)
    {
        double _off = CommonUtil.checkDouble(_args.get(0));
        float[] _fargs = new float[_args.size() - 1];
        for (int _i = 0; _i < _fargs.length; _i++)
        {
            _fargs[_i] = CommonUtil.checkDouble(_args.get(_i + 1)).floatValue();
        }
        this.lineDashX(_off, _fargs);
    }

    public void lineDashX(double _off, float... _fargs)
    {
        this.getContent(false).linedashWithOffset(_off, _fargs);
    }

    public void lineCap(List<String> args)
    {
        int style = CommonUtil.checkDouble(args.get(0)).intValue();
        this.lineCap(style);
    }

    public void lineCap(int style)
    {
        this.getContent(false).linecap(style);
    }

    public void lineJoin(List<String> args)
    {
        int style = CommonUtil.checkDouble(args.get(0)).intValue();
        this.lineJoin(style);
    }

    public void lineJoin(int style)
    {
        this.getContent(false).linejoin(style);
    }

    public void meterlimit(List<String> args)
    {
        double _off = CommonUtil.checkDouble(args.get(0));
        this.meterlimit(_off);
    }

    public void meterlimit(double off)
    {
        this.getContent(false).meterlimit(off);
    }

    public void flatness(List<String> _args)
    {
        int _off = CommonUtil.checkDouble(_args.get(0)).intValue();
        this.flatness(_off);
    }

    public void flatness(int off)
    {
        this.getContent(false).flatness(off);
    }

    public void endPath()
    {
        this.getContent(false).endpath();
    }

    public void closePath()
    {
        this.getContent(false).closepath();
    }

    public float paragraph(String _text, float _lead, float _w, float _h)
    {
        return this.getContent(false).paragraph(_text, _lead, _w, _h, false, true);
    }

    public void drawText(float _x, float _y, String _text)
    {
        this.getContent(false).drawString(this.getParser().mapEntitiesToString(_text), _x, _y);
    }

    public void part(String _text)
    {
        this.getParser().addPart(_text);
    }

    public void outline(String _text)
    {
        this.getParser().addOutline(_text);
    }

    public void chapter(String _text)
    {
        this.getParser().addChapter(_text);
    }

    public void section(String _text)
    {
        this.getParser().addSection(_text);
    }

    public void subsection(String _text)
    {
        this.getParser().addSubSection(_text);
    }

    public void subsubsection(String _text)
    {
        this.getParser().addSubSubSection(_text);
    }

    public void reference(String _text)
    {
        this.getParser().addReference(_text);
    }

    public void logError(String _msg)
    {
        if(log.isErrorEnabled()) log.error(getMarker(), _msg);
    }

    public void logWarn(String _msg)
    {
        if(log.isWarnEnabled()) log.warn(getMarker(), _msg);
    }

    public void logInfo(String _msg)
    {
        if(log.isInfoEnabled()) log.info(getMarker(), _msg);
    }

    public void logDebug(String _msg)
    {
        if(log.isDebugEnabled()) log.debug(getMarker(), _msg);
    }

    public void logTrace(String _msg)
    {
        if(log.isTraceEnabled()) log.trace(getMarker(), _msg);
    }

}
