package com.github.terefang.jmelange.gfx;

import com.github.terefang.jmelange.gfx.impl.G2dProxy;
import com.github.terefang.jmelange.gfx.impl.PdfImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;

public interface GfxInterface
{
    public Graphics2D getG2d();
    default GfxInterface getSub()
    {
        return G2dProxy.create(this);
    }

    public String beginGroup();

    public String beginGroup(String _id);

    public void endGroup();

    default GfxInterface getSub(String _id)
    {
        return G2dProxy.create(this, _id);
    }

    default Font getFont(String _name, int _size)
    {
        return ImageUtil.getFont(_name, _size);
    }

    default Font getPSFont(String _name, int _size)
    {
        Font _font = ImageUtil.getPSFont(new File(_name), _size);
        if(this instanceof PdfImage)
        {
            ((PdfImage)this).registerFont(_font, new File(_name));
        }
        return _font;
    }

    default Font getTTFont(String _name, int _size)
    {
        Font _font = ImageUtil.getTTFont(new File(_name), _size);
        if(this instanceof PdfImage)
        {
            ((PdfImage)this).registerFont(_font, new File(_name));
        }
        return _font;
    }

    default Font getFont(String _name, float _size)
    {
        return ImageUtil.getFont(_name, _size);
    }

    default Font getPSFont(String _name, float _size)
    {
        Font _font = ImageUtil.getPSFont(new File(_name), _size);
        if(this instanceof PdfImage)
        {
            ((PdfImage)this).registerFont(_font, new File(_name));
        }
        return _font;
    }

    default Font getTTFont(String _name, float _size)
    {
        Font _font = ImageUtil.getTTFont(new File(_name), _size);
        if(this instanceof PdfImage)
        {
            ((PdfImage)this).registerFont(_font, new File(_name));
        }
        return _font;
    }

    public void gSet(int _x, int _y, long _color);
    public int gGet(int _x, int _y);

    default void gImage(int _x1, int _y1, BufferedImage _img)
    {
        Graphics2D _g = (Graphics2D) this.getG2d();
        _g.drawImage(_img, _x1, _y1, null);
        _g.dispose();
    }

    default void gLine(int _x1, int _y1, int _x2, int _y2, long _color)
    {
        gLine(_x1, _y1, _x2, _y2, 1, _color);
    }

    default void gLine(int _x1, int _y1, int _x2, int _y2, float _lineWidth, long _color)
    {
        Graphics2D _g = (Graphics2D) this.getG2d();
        _g.setPaint(ImageUtil.createColor(_color));
        Stroke _s = new BasicStroke(_lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);
        _g.setStroke(_s);
        _g.drawLine(_x1, _y1, _x2, _y2);
        _g.dispose();
    }

    default void gDashedLine(int _x1, int _y1, int _x2, int _y2, long _color, float _shape)
    {
        gDashedLine(_x1, _y1, _x2, _y2, 1, _color, new float[] {_shape});
    }

    default void gDashedLine(int _x1, int _y1, int _x2, int _y2, long _color, float... _shape)
    {
        gDashedLine(_x1, _y1, _x2, _y2, 1, _color, _shape);
    }

    default void gDashedLine(int _x1, int _y1, int _x2, int _y2, float _lineWidth, long _color, float _shape)
    {
        gDashedLine(_x1, _y1, _x2, _y2, _lineWidth, _color, new float[] {_shape});
    }

    default void gDashedLine(int _x1, int _y1, int _x2, int _y2, float _lineWidth, long _color, float... _shape)
    {
        Graphics2D _g = (Graphics2D) this.getG2d();
        _g.setPaint(ImageUtil.createColor(_color));
        Stroke _dashed = new BasicStroke(_lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, _shape, 0);
        _g.setStroke(_dashed);
        _g.drawLine(_x1, _y1, _x2, _y2);
        _g.dispose();
    }

    default void gRectangle(int _x1, int _y1, int _x2, int _y2, long _color)
    {
        gRectangle(false, _x1, _y1, _x2, _y2, 1, _color, null);
    }

    default void gRectangle(int _x1, int _y1, int _x2, int _y2, float _lineWidth, long _color, float _dash)
    {
        gRectangle(false, _x1, _y1, _x2, _y2, _lineWidth, _color, new float[] {_dash});
    }

    default void gRectangle(int _x1, int _y1, int _x2, int _y2, float _lineWidth, long _color, float[] _dash)
    {
        gRectangle(false, _x1, _y1, _x2, _y2, _lineWidth, _color, null);
    }

    default void gRectangle(boolean _fill, int _x1, int _y1, int _x2, int _y2, float _lineWidth, long _color, float[] _dash)
    {
        Graphics2D _g = (Graphics2D) this.getG2d();
        _g.setPaint(ImageUtil.createColor(_color));
        if(!_fill)
        {
            Stroke _s = null;
            if(_dash == null || _dash.length==0)
            {
                _s = new BasicStroke(_lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);
            }
            else
            {
                _s = new BasicStroke(_lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, _dash, 0f);
            }
            _g.setStroke(_s);
            _g.drawRect(_x1,_y1, _x2-_x1, _y2-_y1);
        }
        else
        {
            _g.fillRect(_x1,_y1, _x2-_x1, _y2-_y1);
        }
        _g.dispose();
    }

    default void gFilledRectangle(int _x1, int _y1, int _x2, int _y2, long _color)
    {
        gRectangle(true, _x1, _y1, _x2, _y2, 1, _color, null);
    }

    default void gFilledRectangle(int _x1, int _y1, int _x2, int _y2, float _lineWidth, long _color)
    {
        gRectangle(true, _x1, _y1, _x2, _y2, _lineWidth, _color, null);
    }

    default void gPolygon(long _color, int... _points)
    {
        gPolygon(1, _color, null, _points);
    }

    default void gPolygon(float _lineWidth, long _color, float[] _dash, int... _points)
    {
        this.gPolygon(false,_lineWidth, _color, _dash, _points);
    }

    default void gPolygon(boolean _fill, float _lineWidth, long _color, float[] _dash, int... _points)
    {
        Graphics2D _g = (Graphics2D) this.getG2d();
        _g.setPaint(ImageUtil.createColor(_color));
        Polygon _p = new Polygon();
        for(int _i = 0; _i<_points.length; _i+=2)
        {
            _p.addPoint(_points[_i], _points[_i+1]);
        }
        if(!_fill)
        {
            Stroke _s = null;
            if(_dash == null || _dash.length==0)
            {
                _s = new BasicStroke(_lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);
            }
            else
            {
                _s = new BasicStroke(_lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, _dash, 0f);
            }
            _g.setStroke(_s);
            _g.drawPolygon(_p);
        }
        else
        {
            _g.fillPolygon(_p);
        }
        _g.dispose();
    }

    default void gFilledPolygon(long _color, int... _points)
    {
        gFilledPolygon(1, _color, _points);
    }

    default void gFilledPolygon(float _lineWidth, long _color, int... _points)
    {
        this.gPolygon(true,_lineWidth, _color, null, _points);
    }

    default void gPolyline(long _color, int... _points)
    {
        gPolyline(1, _color, null, _points);
    }

    default void gPolyline(float _lineWidth, long _color, int... _points)
    {
        gPolyline(_lineWidth, _color, null, _points);
    }

    default void gPolyline(float _lineWidth, long _color, float[] _dash, int... _points)
    {
        Graphics2D _g = (Graphics2D) this.getG2d();
        _g.setPaint(ImageUtil.createColor(_color));
        Stroke _s = null;
        if(_dash == null || _dash.length==0)
        {
            _s = new BasicStroke(_lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);
        }
        else
        {
            _s = new BasicStroke(_lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, _dash, 0f);
        }
        _g.setStroke(_s);
        int _lx = _points[0];
        int _ly = _points[1];
        for(int _i = 2; _i<_points.length; _i+=2)
        {
            _g.drawLine(_lx,_ly,_points[_i], _points[_i+1]);
            _lx = _points[_i];
            _ly = _points[_i+1];
        }
        _g.dispose();
    }

    default void gCircle(int _cx, int _cy, int _r, long _color)
    {
        this.gOval(_cx, _cy, _r, _r, 1, _color, null);
    }

    default void gCircle(int _cx, int _cy, int _r, float _lineWidth, long _color)
    {
        this.gOval(_cx, _cy, _r, _r, _lineWidth, _color, null);
    }

    default void gCircle(int _cx, int _cy, int _r, float _lineWidth, long _color, float _dash)
    {
        this.gOval(_cx, _cy, _r, _r, _lineWidth, _color, new float[]{ _dash });
    }

    default void gCircle(int _cx, int _cy, int _r, float _lineWidth, long _color, float[] _dash)
    {
        this.gOval(false, _cx, _cy, _r, _r, _lineWidth, _color, _dash);
    }

    default void gCircle(boolean _fill, int _cx, int _cy, int _r, float _lineWidth, long _color, float _dash)
    {
        this.gOval(_fill, _cx, _cy, _r, _r, _lineWidth, _color, new float[]{ _dash });
    }

    default void gCircle(boolean _fill, int _cx, int _cy, int _r, float _lineWidth, long _color, float[] _dash)
    {
        this.gOval(_fill, _cx, _cy, _r, _r, _lineWidth, _color, _dash);
    }

    default void gFilledCircle(int _cx, int _cy, int _r, long _color)
    {
        this.gCircle(true, _cx, _cy, _r, 1, _color, null);
    }

    default void gOval(int _cx, int _cy, int _ra, int _rb, long _color)
    {
        this.gOval(_cx, _cy, _ra, _rb, 1, _color, null);
    }

    default void gOval(int _cx, int _cy, int _ra, int _rb, float _lineWidth, long _color, float _dash)
    {
        this.gOval(false, _cx, _cy, _ra, _rb, _lineWidth, _color, new float[]{ _dash });
    }

    default void gOval(int _cx, int _cy, int _ra, int _rb, float _lineWidth, long _color, float[] _dash)
    {
        this.gOval(false, _cx, _cy, _ra, _rb, 1, _color, null);
    }

    default void gOval(boolean _fill, int _cx, int _cy, int _ra, int _rb, float _lineWidth, long _color, float _dash)
    {
        this.gOval(_fill, _cx, _cy, _ra, _rb, _lineWidth, _color, new float[]{ _dash });
    }

    default void gOval(boolean _fill, int _cx, int _cy, int _ra, int _rb, float _lineWidth, long _color, float[] _dash)
    {
        Graphics2D _g = (Graphics2D) this.getG2d();
        _g.setPaint(ImageUtil.createColor(_color));
        if(!_fill)
        {
            Stroke _s = null;
            if(_dash == null || _dash.length==0)
            {
                _s = new BasicStroke(_lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);
            }
            else
            {
                _s = new BasicStroke(_lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, _dash, 0f);
            }
            _g.setStroke(_s);
            _g.drawOval(_cx-_ra, _cy-_rb, _ra*2,_rb*2);
        }
        else
        {
            _g.fillOval(_cx-_ra, _cy-_rb,_ra*2,_rb*2);
        }
        _g.dispose();
    }

    default void gFilledOval(int _cx, int _cy, int _ra, int _rb, long _color)
    {
        this.gOval(true, _cx, _cy, _ra, _rb, 1, _color, null);
    }

    default void gArc(int _cx, int _cy, int _ra, int _rb, int _as, int _ae, long _color)
    {
        gArc(false, _cx, _cy, _ra, _rb, _as, _ae,1, _color, null);
    }

    default void gArc(boolean _fill, int _cx, int _cy, int _ra, int _rb, int _as, int _ae, float _lineWidth, long _color, float _dash)
    {
        gArc(_fill, _cx, _cy, _ra, _rb, _as, _ae,1, _color, new float[] { _dash });
    }

    default void gArc(boolean _fill, int _cx, int _cy, int _ra, int _rb, int _as, int _ae, float _lineWidth, long _color, float[] _dash)
    {
        Graphics2D _g = (Graphics2D) this.getG2d();
        _g.setPaint(ImageUtil.createColor(_color));
        if(!_fill)
        {
            Stroke _s = null;
            if(_dash == null || _dash.length==0)
            {
                _s = new BasicStroke(_lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);
            }
            else
            {
                _s = new BasicStroke(_lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, _dash, 0f);
            }
            _g.setStroke(_s);
            _g.drawArc(_cx-_ra, _cy-_rb, _ra*2,_rb*2, _as, _ae-_as);
        }
        else
        {
            _g.fillArc(_cx-_ra, _cy-_rb, _ra*2,_rb*2, _as, _ae-_as);
        }
        _g.dispose();
    }

    default void gFilledArc(int _cx, int _cy, int _ra, int _rb, int _as, int _ae, long _color)
    {
        gArc(true, _cx, _cy, _ra, _rb, _as, _ae,1, _color, null);
    }

    default void gString(String _font, int _size, int _x, int _y, String _s, long _color)
    {
        Graphics2D _g = (Graphics2D) this.getG2d();
        _g.setPaint(ImageUtil.createColor(_color));
        _g.setFont(ImageUtil.getFont(_font, _size));
        _g.drawString(_s, _x, _y);
        _g.dispose();
    }

    default void gString(Font _font, int _x, int _y, String _s, long _color)
    {
        Graphics2D _g = (Graphics2D) this.getG2d();
        _g.setPaint(ImageUtil.createColor(_color));
        _g.setFont(_font);
        _g.drawString(_s, _x, _y);
        _g.dispose();
    }

    default void gString(GfxFont _font, int _x, int _y, String _s, long _color)
    {
        _font.drawString(this, _x, _y, _s , _color);
    }

    public void save(File _out);
    public void save(OutputStream _out);

    public StackedGfxInterface getStackedGfxInterface();
}
