package com.github.terefang.jmelange.gfx.impl;

import com.github.terefang.jmelange.commons.gfx.AbstractGfxInterface;
import com.github.terefang.jmelange.commons.gfx.GfxInterface;
import com.github.terefang.jmelange.commons.gfx.GfxUtil;
import com.github.terefang.jmelange.gfx.G2dProxy;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.g2d.PdfGraphics2D;
import com.github.terefang.jmelange.commons.loader.FileResourceLoader;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import lombok.SneakyThrows;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

public class PdfImage extends AbstractGfxInterface
        implements GfxInterface
{
    PdfFontRegistry freg;
    PdfDocument document;
    PdfPage page;
    PdfGraphics2D g2d;
    public static final PdfImage create(int _width, int _height) {
        return new PdfImage(_width, _height);
    }

    public void registerFont(Font _font, File _file)
    {
        String _fname = _font.getFontName();
        if(this.freg.lookupFont(_fname)==null)
        {
            if(_file.getName().endsWith(".ttf"))
            {
                this.freg.registerFont( this.document.registerTtfFont(PDF.ENCODING_PDFDOC,_font, FileResourceLoader.of(_file, null)), _fname);
            }
            else
            {
                this.freg.registerFont( this.document.registerAwtFont(_font, PDF.ENCODING_PDFDOC, null), _fname);
            }
        }
    }
    
    @Override
    public GfxInterface getSub()
    {
        return G2dProxy.create(this, UUID.randomUUID().toString());
    }
    @Override
    public GfxInterface getSub(String _id)
    {
        return G2dProxy.create(this, _id);
    }
    
    @Override public Font getPSFont(String _name, int _size)
    {
        Font _font = super.getPSFont(_name, _size);
        registerFont(_font, new File(_name));
        return _font;
    }
    
    @Override public  Font getTTFont(String _name, int _size)
    {
        Font _font = super.getTTFont(_name, _size);
        registerFont(_font, new File(_name));
        return _font;
    }
    
    
    @Override public Font getPSFont(String _name, float _size)
    {
        Font _font = super.getPSFont(_name, _size);
        registerFont(_font, new File(_name));
        return _font;
    }
    
    @Override
    public Font getTTFont(String _name, float _size)
    {
        Font _font = super.getTTFont(_name, _size);
        registerFont(_font, new File(_name));
        return _font;
    }
    
    @SneakyThrows
    public PdfImage(int width, int height)
    {
        super();
        this.document = new PdfDocument();
        this.freg = PdfFontRegistry.of(this.document);
        this.newPage(width, height);
    }

    public void newPage(int width, int height)
    {
        if(this.g2d!=null) this.g2d.dispose();

        this.page = this.document.newPage();
        this.page.setMediabox(0,0,width, height);
        this.g2d = PdfGraphics2D.from(this.page);
        this.g2d.setPdfFontRegistry(this.freg);
    }

    @Override
    public Graphics2D getG2d() {
        return (Graphics2D) this.g2d.create();
    }

    @Override
    public void gSet(int _x, int _y, long _color) {
        Graphics2D _g = this.getG2d();
        _g.setColor(GfxUtil.createColor(_color));
        _g.fillRect(_x, _y, 1, 1);
        _g.dispose();
    }

    public String beginGroup()
    {
        return beginGroup(UUID.randomUUID().toString());
    }

    public String beginGroup(String _id)
    {
        this.g2d.startLayer(_id);
        return _id;
    }

    public void endGroup()
    {
        this.g2d.endLayer();
    }

    @Override
    public int gGet(int _x, int _y) {
        return 0;
    }

    @SneakyThrows
    public void save(String _path) {
        this.save(new File(_path));
    }

    @SneakyThrows
    public void save(File _out)
    {
        this.save(new FileOutputStream(_out));
    }

    @SneakyThrows
    public void save(OutputStream _out)
    {
        this.g2d.dispose();
        this.document.streamBegin(_out);
        this.document.streamEnd(true);
    }
}
