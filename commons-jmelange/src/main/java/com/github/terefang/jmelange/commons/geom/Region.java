package com.github.terefang.jmelange.commons.geom;

import com.github.terefang.jmelange.commons.util.MathUtil;

import java.util.List;
import java.util.Vector;

public class Region
{
    public Point seed;
    public List<Triangle> vertices = new Vector<>();
    
    public Region(Point seed )
    {
        this.seed = seed;
    }
    
    public Region sortVertices()
    {
        vertices.sort( (a,b)->{ return compareAngles(a,b,this.seed);} );
        return this;
    }
    
   public String asString()
   {
       StringBuilder _sb = new StringBuilder();
       _sb.append("R{ "+seed.asString()+"\n");
       for(Triangle _t: vertices)
       {
           _sb.append(_t.asString());
           _sb.append("\n");
       }
       _sb.append("}");
       return _sb.toString();
   }
   
    public Point center()
    {
        Point c = new Point();
        for(Triangle _v : this.vertices)
        {
            c.addEq( _v.c );
        }
        c.scaleEq( 1 / vertices.size() );
        return c;
    }
    
    public boolean borders(Region r )
    {
        int len1 = vertices.size();
        int len2 = r.vertices.size();
        for (int _i=0; _i<len1; _i++)
        {
            var j = r.vertices.indexOf( vertices.get(_i) );
            if (j != -1)
                return vertices.get((_i + 1) % len1).equals(r.vertices.get((j + len2 - 1) % len2));
        }
        return false;
    }
    
    private static int compareAngles(Triangle v1, Triangle v2, Point seed)
    {
        double x1 = v1.c.x - seed.x;
        double y1 = v1.c.y - seed.y;
        double x2 = v2.c.x - seed.x;
        double y2 = v2.c.y - seed.y;

        if (x1 >= 0 && x2 < 0) return 1;
        if (x2 >= 0 && x1 < 0) return -1;
        if (x1 == 0 && x2 == 0)
            return y2 > y1 ? 1 : -1;
        
        return MathUtil.sign( x2 * y1 - x1 * y2 );
    }


}
