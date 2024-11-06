package com.github.terefang.jmelange.commons.geom;

public class Matrix
{
    double a;
    double b;
    double c;
    double d;
    double tx;
    double ty;
    
    public Matrix()
    {
    }

    public Matrix(double _a, double _b, double _c, double _d, double _tx, double _ty)
    {
        this.a = _a;
        this.b = _b;
        this.c = _c;
        this.d = _d;
        this.tx = _tx;
        this.ty = _ty;
    }
    
    public void setTo(double _a, double _b, double _c, double _d, double _tx, double _ty)
    {
        this.a = _a;
        this.b = _b;
        this.c = _c;
        this.d = _d;
        this.tx = _tx;
        this.ty = _ty;
    }
    
    public void setTo(Matrix _matrix)
    {
        this.a = _matrix.a;
        this.b = _matrix.b;
        this.c = _matrix.c;
        this.d = _matrix.d;
        this.tx = _matrix.tx;
        this.ty = _matrix.ty;
    }
    
    public Matrix clone()
    {
        return new Matrix( this.a, this.b, this.c, this.d, this.tx, this.ty );
    }
    
    public void identity()
    {
        a = d = 1;
        b = c = tx = ty = 0;
    }
    
    public boolean isIdentity()
    {
        return a == 1 && d == 1 && tx == 0 && ty == 0 && b == 0 && c == 0;
    }
    
    public void invert()
    {
        double t, n = a * d - b * c;
        if (n == 0) {
            a = b = c = d = 0;
            tx = -tx;
            ty = -ty;
        } else {
            n = 1 / n;
            //
            t = d * n;
            d = a * n;
            a = t;
            //
            b *= -n;
            c *= -n;
            //
            t = -a * tx - c * ty;
            ty = -b * tx - d * ty;
            tx = t;
        }
    }
    
    public void translate(double _x, double _y)
    {
        tx += _x;
        ty += _y;
    }
    
    public void rotate(double o)
    {
        double ox = Math.cos(o), oy = Math.sin(o), t;
        //
        t = a * ox - b * oy;
        b = a * oy + b * ox;
        a = t;
        //
        t = c * ox - d * oy;
        d = c * oy + d * ox;
        c = t;
        //
        t = tx * ox - ty * oy;
        ty = tx * oy + ty * ox;
        tx = t;
    }
    
    public void scale(double x, double y) {
        a *= x; b *= y;
        c *= x; d *= y;
        tx *= x; ty *= y;
    }
    
    public void concat(Matrix o) {
        double t;
        t = a * o.a + b * o.c;
        b = a * o.b + b * o.d;
        a = t;
        //
        t = c * o.a + d * o.c;
        d = c * o.b + d * o.d;
        c = t;
        //
        t = tx * o.a + ty * o.c + o.tx;
        ty = tx * o.b + ty * o.d + o.ty;
        tx = t;
        //
    }
    
    public Point transformPoint(Point o)
    {
        return new Point(o.x * a + o.y * c + tx, o.x * b + o.y * d + ty);
    }
    
    public static Matrix createBox(double sx, double sy, double r, double x, double y)
    {
        return new Matrix(sx, r, 0, sy, x, y);
    }
}


// https://github.com/YAL-Haxe/openfl-bitfive/blob/master/openfl/geom/Matrix.hx
