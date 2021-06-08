package com.github.terefang.jmelange.gfx;

import java.awt.*;

public interface StackedGfxInterface
{
    public Graphics2D getCurrentGraphics2D();

    public void beginDraw();
    public void beginDraw(String _id);
    public void endDraw();
    public void beginText();
    public void endText();

    public void draw3DRect(int x, int y, int width, int height, boolean raised);

    public void fill3DRect(int x, int y, int width, int height, boolean raised);

    public void drawString(String str, int x, int y);

    public void drawString(String str, float x, float y);

    public void setComposite(Composite comp);

    public void setPaint(Paint paint);

    public void setStroke(Stroke s);

    public void translate(int x, int y);

    public void translate(double tx, double ty);

    public void rotate(double theta);

    public void rotate(double theta, double x, double y);

    public void scale(double sx, double sy);

    public void shear(double shx, double shy);

    public void setBackground(int _color);

    public void clip(Shape s);

    default void clip(int[] _points)
    {
        Polygon _p = new Polygon();
        for(int _i = 0; _i<_points.length; _i+=2)
        {
            _p.addPoint(_points[_i], _points[_i+1]);
        }
        clip(_p);
    }

    public void setColor(long _color);

    public void setPaintMode();

    public void setFont(Font font);

    public void clipRect(int x, int y, int width, int height);

    public void setClip(int x, int y, int width, int height);

    public void drawLine(int x1, int y1, int x2, int y2);

    public void fillRect(int x, int y, int width, int height);

    public void drawRect(int x, int y, int width, int height);

    public void clearRect(int x, int y, int width, int height);

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);

    public void drawOval(int x, int y, int width, int height);

    public void fillOval(int x, int y, int width, int height);

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle);

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle);

    public void drawPolyline(int[] points);

    public void drawPolygon(int[] points);

    public void fillPolygon(int[] points);

    public void dispose();
}
