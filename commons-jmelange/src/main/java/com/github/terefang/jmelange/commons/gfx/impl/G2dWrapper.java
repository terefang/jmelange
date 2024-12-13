package com.github.terefang.jmelange.commons.gfx.impl;

import com.github.terefang.jmelange.commons.gfx.AbstractGfxInterface;
import com.github.terefang.jmelange.commons.gfx.GfxInterface;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.OutputStream;

public class G2dWrapper extends AbstractGfxInterface implements GfxInterface
{
    
    public static G2dWrapper from(Graphics2D _context)
    {
        G2dWrapper _w = new G2dWrapper();
        _w.context = _context;
        return _w;
    }
    
    public GfxInterface wrap(Graphics2D _context)
    {
        this.context = _context;
        return this;
    }
    
    Graphics2D context;
    @Override
    public Graphics2D getG2d()
    {
        return null;
    }
    
    @Override
    public GfxInterface getSub()
    {
        return this;
    }
    
    @Override
    public String beginGroup()
    {
        return "null";
    }
    
    @Override
    public String beginGroup(String _id)
    {
        return "null";
    }
    
    @Override
    public void endGroup()
    {
    
    }
    
    @Override
    public GfxInterface getSub(String _id)
    {
        return this;
    }
    
    @Override
    public void gSet(int _x, int _y, long _color)
    {
        context.setColor(new Color((int)_color));
        context.fillRect(_x,_y,1,1);
    }
    
    @Override
    public int gGet(int _x, int _y)
    {
        return 0;
    }
    
    @Override
    public void save(File _out)
    {
    
    }
    
    @Override
    public void save(OutputStream _out)
    {
    
    }
}
