package com.github.terefang.jmelange.commons.geom;

import java.util.Objects;

public class Triangle
{
    public Point p1;
    public Point p2;
    public Point p3;
    
    public Point c;
    public double r;
    
    public Triangle(Point p1, Point p2, Point p3)
    {
        double s = (p2.x - p1.x) * (p2.y + p1.y) + (p3.x - p2.x) * (p3.y + p2.y) + (p1.x - p3.x) * (p1.y + p3.y);
        this.p1 = p1;
        // CCW
        this.p2 = s > 0 ? p2 : p3;
        this.p3 = s > 0 ? p3 : p2;
        
        double x1 = (p1.x + p2.x) / 2;
        double y1 = (p1.y + p2.y) / 2;
        double x2 = (p2.x + p3.x) / 2;
        double y2 = (p2.y + p3.y) / 2;
        
        double dx1 = p1.y - p2.y;
        double dy1 = p2.x - p1.x;
        double dx2 = p2.y - p3.y;
        double dy2 = p3.x - p2.x;
        
        double tg1 = dy1 / dx1;
        double t2 = ((y1 - y2) - (x1 - x2) * tg1) /
                (dy2 - dx2 * tg1);
        
        c = new Point(x2 + dx2 * t2, y2 + dy2 * t2);
        r = Point.distance(c, p1);
    }
    
    public boolean hasEdge(Point _a, Point _b)
    {
        return (p1.equals(_a) && p2.equals(_b)) ||
                (p2.equals(_a) && p3.equals(_b)) ||
                (p3.equals(_a) && p1.equals(_b));
    }
    
    public boolean equals(Triangle _o)
    {
        if (_o == null)
        {
            return false;
        }
        if (this == _o)
        {
            return true;
        }
        Triangle triangle = (Triangle) _o;
        return Objects.equals(p1, triangle.p1) && Objects.equals(p2, triangle.p2) &&
                Objects.equals(p3, triangle.p3);
    }
    
    public String asString()
    {
        StringBuilder _sb = new StringBuilder();
        _sb.append("T< ");
        _sb.append(this.p1.asString());
        _sb.append(" ");
        _sb.append(this.p2.asString());
        _sb.append(" ");
        _sb.append(this.p3.asString());
        _sb.append(" >");
        return _sb.toString();
    }
}