package com.github.terefang.jmelange.image;

import com.github.terefang.jmelange.utils.ImageUtil;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGHints;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public abstract class AbstractGfxInterface implements GfxInterface, StackedGfxInterface
{
    Deque<Graphics2D> _queue = new ArrayDeque<>();
    Graphics2D _current;

    @Override
    public StackedGfxInterface getStackedGfxInterface()
    {
        this.beginDraw();
        return this;
    }

    @Override
    public Graphics2D getCurrentGraphics2D()
    {
        if(_current!=null) return _current;
        if(!_queue.isEmpty())
        {
            return (_current = _queue.poll());
        }
        return (_current = getG2d());
    }

    public void dispose()
    {
        this.endDraw();
    }

    public void beginDraw()
    {
        beginDraw(UUID.randomUUID().toString());
    }

    @Override
    public void beginDraw(String _id) {
        if(_current!=null)
        {
            _queue.push(_current);
            _current = (Graphics2D) _current.create();
        }
        else
        {
            getCurrentGraphics2D();
        }

        if(_current instanceof SVGGraphics2D)
        {
            ((SVGGraphics2D)_current).setRenderingHint(SVGHints.KEY_BEGIN_GROUP, _id);
        }
    }

    @Override
    public void endDraw() {
        if(_current!=null)
        {
            _current.dispose();
        }
        _current = _queue.poll();

        if(_current instanceof SVGGraphics2D)
        {
            ((SVGGraphics2D)_current).setRenderingHint(SVGHints.KEY_END_GROUP, "true");
        }
    }

    @Override
    public void beginText() {
        if(_current!=null)
        {
            _queue.push(_current);
            _current = (Graphics2D) _current.create();
        }
        else
        {
            getCurrentGraphics2D();
        }
    }

    @Override
    public void endText() {
        if(_current!=null)
        {
            _current.dispose();
        }
        _current = _queue.poll();
    }


    public void draw3DRect(int x, int y, int width, int height, boolean raised) {
        _current.draw3DRect(x, y, width, height, raised);
    }

    public void fill3DRect(int x, int y, int width, int height, boolean raised) {
        _current.fill3DRect(x, y, width, height, raised);
    }

    public void drawString(String str, int x, int y) {
        _current.drawString(str, x, y);
    }

    public void drawString(String str, float x, float y) {
        _current.drawString(str, x, y);
    }

    public void setComposite(Composite comp) {
        _current.setComposite(comp);
    }

    public void setPaint(Paint paint) {
        _current.setPaint(paint);
    }

    public void setStroke(Stroke s) {
        _current.setStroke(s);
    }

    public void translate(int x, int y) {
        _current.translate(x, y);
    }

    public void translate(double tx, double ty) {
        _current.translate(tx, ty);
    }

    public void rotate(double theta) {
        _current.rotate(theta);
    }

    public void rotate(double theta, double x, double y) {
        _current.rotate(theta, x, y);
    }

    public void scale(double sx, double sy) {
        _current.scale(sx, sy);
    }

    public void shear(double shx, double shy) {
        _current.shear(shx, shy);
    }

    public void setBackground(int _color) {
        _current.setBackground(ImageUtil.createColor(_color));
    }

    public void clip(Shape s) {
        _current.clip(s);
    }

    public void setColor(long _color) {
        _current.setColor(ImageUtil.createColor(_color));
    }

    public void setPaintMode() {
        _current.setPaintMode();
    }

    public void setFont(Font font) {
        _current.setFont(font);
    }

    public void clipRect(int x, int y, int width, int height) {
        _current.clipRect(x, y, width, height);
    }

    public void setClip(int x, int y, int width, int height) {
        _current.setClip(x, y, width, height);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        _current.drawLine(x1, y1, x2, y2);
    }

    public void fillRect(int x, int y, int width, int height) {
        _current.fillRect(x, y, width, height);
    }

    public void drawRect(int x, int y, int width, int height) {
        _current.drawRect(x, y, width, height);
    }

    public void clearRect(int x, int y, int width, int height) {
        _current.clearRect(x, y, width, height);
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        _current.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        _current.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void drawOval(int x, int y, int width, int height) {
        _current.drawOval(x, y, width, height);
    }

    public void fillOval(int x, int y, int width, int height) {
        _current.fillOval(x, y, width, height);
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        _current.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        _current.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    public void drawPolyline(int[] points) {
        int _np = points.length/2;
        int[] _x = new int[_np];
        int[] _y = new int[_np];
        for(int _i = 0; _i<_np; _i++)
        {
            _x[_i] = points[_i*2];
            _y[_i] = points[(_i*2)+1];
        }
        _current.drawPolyline(_x, _y, _np);
    }

    public void drawPolygon(int[] points) {
        int _np = points.length/2;
        int[] _x = new int[_np];
        int[] _y = new int[_np];
        for(int _i = 0; _i<_np; _i++)
        {
            _x[_i] = points[_i*2];
            _y[_i] = points[(_i*2)+1];
        }
       _current.drawPolygon(_x, _y, _np);
    }

    public void fillPolygon(int[] points) {
        int _np = points.length/2;
        int[] _x = new int[_np];
        int[] _y = new int[_np];
        for(int _i = 0; _i<_np; _i++)
        {
            _x[_i] = points[_i*2];
            _y[_i] = points[(_i*2)+1];
        }
        _current.fillPolygon(_x, _y, _np);
    }

}
