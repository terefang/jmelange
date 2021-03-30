package com.github.terefang.jmelange.image;

import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGHints;

import java.awt.*;
import java.io.File;
import java.io.OutputStream;
import java.util.UUID;

public class G2dProxy implements GfxSubInterface
{
    Graphics2D _g2d;
    GfxInterface _g;
    public static final  GfxSubInterface create(GfxInterface _g, String _id)
    {
        G2dProxy _p = new G2dProxy();
        _p._g2d = _g.getG2d();
        _p._g=_g;

        if(_p._g2d instanceof SVGGraphics2D)
        {
            ((SVGGraphics2D)_p._g2d).setRenderingHint(SVGHints.KEY_BEGIN_GROUP, _id);
        }
        return _p;
    }

    public static final  GfxSubInterface create(GfxInterface _g)
    {
        return create(_g,UUID.randomUUID().toString());
    }

    @Override
    public Graphics2D getG2d() {
        return (Graphics2D) _g2d.create();
    }

    @Override
    public String beginGroup() {
        return this._g.beginGroup();
    }

    @Override
    public String beginGroup(String _id) {
        return this._g.beginGroup(_id);
    }

    @Override
    public void endGroup() {
        this._g.endGroup();
    }

    @Override
    public void gSet(int _x, int _y, long _color) {
        _g.gSet(_x, _y, _color);
    }

    @Override
    public int gGet(int _x, int _y) {
        return _g.gGet(_x, _y);
    }

    @Override
    public void save(File _out) {

    }

    @Override
    public void save(OutputStream _out) {

    }

    @Override
    public StackedGfxInterface getStackedGfxInterface() {
        return _g.getStackedGfxInterface();
    }

    @Override
    public void dispose()
    {
        if(this._g2d instanceof SVGGraphics2D)
        {
            ((SVGGraphics2D)this._g2d).setRenderingHint(SVGHints.KEY_END_GROUP, "true");
        }
        this._g2d.dispose();
        this._g2d=null;
    }
}
