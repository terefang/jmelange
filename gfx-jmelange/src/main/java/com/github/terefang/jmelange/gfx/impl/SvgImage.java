package com.github.terefang.jmelange.gfx.impl;

import com.github.terefang.jmelange.commons.gfx.AbstractGfxInterface;
import com.github.terefang.jmelange.commons.gfx.GfxInterface;
import com.github.terefang.jmelange.commons.gfx.GfxUtil;
import com.github.terefang.jmelange.gfx.G2dProxy;
import lombok.SneakyThrows;
import org.codehaus.plexus.util.IOUtil;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGHints;
import org.jfree.graphics2d.svg.SVGUnits;

import java.awt.*;
import java.io.*;
import java.util.UUID;

public class SvgImage extends AbstractGfxInterface
        implements GfxInterface
{
    SVGGraphics2D g2d;
    public static final SvgImage create(int _width, int _height) {
        return new SvgImage(_width, _height);
    }

    public SvgImage(int width, int height)
    {
        super();
        this.g2d = new SVGGraphics2D(width, height);
        this.g2d.setFontSizeUnits(SVGUnits.PT);
    }

    @Override
    public Graphics2D getG2d() {
        return (Graphics2D) this.g2d.create();
    }
    
    @Override
    public GfxInterface getSub()
    {
        return G2dProxy.create(this, UUID.randomUUID().toString());
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
        this.g2d.setRenderingHint(SVGHints.KEY_BEGIN_GROUP, _id);
        return _id;
    }

    public void endGroup()
    {
        this.g2d.setRenderingHint(SVGHints.KEY_END_GROUP, "true");
    }
    
    @Override
    public GfxInterface getSub(String _id)
    {
        return G2dProxy.create(this, _id);
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
        _out.getParentFile().mkdirs();
        this.save(new FileOutputStream(_out));
    }

    @SneakyThrows
    public void save(OutputStream _out)
    {
        BufferedWriter _bw = new BufferedWriter(new OutputStreamWriter(_out), 8192);
        IOUtil.copy(this.g2d.getSVGDocument(), _bw);
        IOUtil.close(_bw);
    }
    
    @Override
    public void beginDraw(String _id) {
        super.beginDraw(_id);
        getCurrentGraphics2D().setRenderingHint(SVGHints.KEY_BEGIN_GROUP, _id);
    }
    
    @Override
    public void endDraw()
    {
        getCurrentGraphics2D().setRenderingHint(SVGHints.KEY_END_GROUP, "true");
        super.endDraw();
    }
}
