package com.github.terefang.jmelange.commons.geom;

import com.github.terefang.jmelange.commons.util.MathUtil;

public class Point
{
    double x;
    double y;
    
    public static Point from(double _x, double _y)
    {
        return new Point(_x,_y);
    }
    
    public Point clone()
    {
        return new Point(this.x,this.y);
    }
    
    public Point()
    {
    }
    
    public static Point polar(double _l, double _d)
    {
        return new Point(Math.cos(_d*Math.PI/180.) * _l,Math.sin(_d*Math.PI/180.) * _l);
    }

    public Point(double _x, double _y)
    {
        x = _x;
        y = _y;
    }
    
    public double getX()
    {
        return x;
    }
    
    public void setX(double _x)
    {
        x = _x;
    }
    
    public double getY()
    {
        return y;
    }
    
    public void setY(double _y)
    {
        y = _y;
    }
    
    public double length() {
        return Math.sqrt(x * x + y * y);
    }
    
    public Point offset(double _dx, double _dy)
    {
        return new Point(this.x+_dx,this.y+_dy);
    }
    
    public Point add(Point _xy)
    {
        return new Point(this.x+_xy.x,this.y+_xy.y);
    }
    
    
    public Point sub(Point _xy)
    {
        return new Point(this.x-_xy.x,this.y-_xy.y);
    }
    
    public void addEq(Point _xy)
    {
        this.x+=_xy.x;
        this.y+=_xy.y;
    }
    public void subEq(Point _xy)
    {
        this.x-=_xy.x;
        this.y-=_xy.y;
    }
    public void scaleEq(Point _xy)
    {
        this.x*=_xy.x;
        this.y*=_xy.y;
    }

    public void scaleEq(double _f)
    {
        this.x*=_f;
        this.y*=_f;
    }
    
    public void normalize()
    {
        normalize(1.);
    }
    
    public void normalize(double _l)
    {
        if (y == 0) x = x < 0 ? -_l : _l;
        else if (x == 0) y = y < 0 ? -_l : _l;
        else {
            double m = _l / length();
            x *= m;
            y *= m;
        }
    }
    
    public static Point lerp(Point _1, Point _2, double _f)
    {
        return new Point(MathUtil.lerp(_1.x,_2.x, _f), MathUtil.lerp(_1.y,_2.y, _f));
    }

    public static double distance(Point _1, Point _2)
    {
        return new Point(_2.x-_1.x, _2.y-_1.y).length();
    }
    
    public static Point norm(Point _xy)
    {
        return norm(_xy,1./ _xy.length());
    }
    
    public static Point norm(Point _xy, double _f)
    {
        _xy = _xy.clone();
        _xy.normalize(_f);
        return _xy;
    }

    public static Point scale(Point _xy, double _f)
    {
        _xy = _xy.clone();
        _xy.x *= _f;
        _xy.y *= _f;
        return _xy;
    }
    
    public boolean equals(Point _o)
    {
        if (this == _o)
        {
            return true;
        }
        return Double.compare(_o.x, x) == 0 && Double.compare(_o.y, y) == 0;
    }

    public String asString()
    {
        return String.format("P(%f,%f)", x, y);
    }
    
}
