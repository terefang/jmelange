package com.github.terefang.jmelange.commons.geom;

import com.github.terefang.jmelange.commons.util.ListMapUtil;

import java.util.*;

public class Voronoi
{
    public List<Triangle> triangles = new Vector<>();
    
    private boolean _regionsDirty;
    private Map<Point, Region> _regions = new HashMap<>();
    
    public List<Point> points;
    public List<Point> frame;
    
    public static Voronoi from(double minx,double miny,double maxx,double maxy)
    {
        return new Voronoi(minx, miny, maxx, maxy);
    }
    
    public Voronoi(double minx,double miny,double maxx,double maxy)
    {
        this(new Point( minx, miny ),
                new Point( minx, maxy ),
                new Point( maxx, miny ),
                new Point( maxx, maxy ));
    }
    
    public Voronoi(Point c1,Point c2,Point c3,Point c4)
    {
        frame = ListMapUtil.asList(c1, c2, c3, c4);
        points = ListMapUtil.asList(c1, c2, c3, c4);
        triangles.add( new Triangle( c1, c2, c3 ) );
        triangles.add( new Triangle( c2, c3, c4 ) );
        
        // Maybe we shouldn't do it beause these temporary
        // regions will be discarded anyway
        for (Point p : points)
        {
            _regions.put(p, buildRegion( p ));
        }
        _regionsDirty = false;
    }
    
    public Voronoi(Point c1,Point c2,Point c3)
    {
        frame = ListMapUtil.asList(c1, c2, c3);
        points = ListMapUtil.asList(c1, c2, c3);
        triangles.add( new Triangle( c1, c2, c3 ) );
        
        // Maybe we shouldn't do it beause these temporary
        // regions will be discarded anyway
        for (Point p : points)
        {
            _regions.put(p, buildRegion( p ));
        }
        _regionsDirty = false;
    }
    
    private Region buildRegion(Point p )
    {
        var r = new Region( p );
        for (Triangle tr : triangles)
            if (tr.p1 == p || tr.p2 == p || tr.p3 == p)
                r.vertices.add( tr );
        
        return r.sortVertices();
    }
    
    public Map<Point, Region> getRegions()
    {
        if (_regionsDirty)
        {
            _regions = new HashMap<>();
            _regionsDirty = false;
            for (Point p : points)
            {
                _regions.put(p,buildRegion( p ));
            }
        }
        return _regions;
    }
    
    public void add(double x, double y)
    {
        add(Point.from(x,y));
    }
    public void add(Point p)
    {
        List<Triangle> toSplit = new Vector<>();
        for (Triangle tr : triangles)
        {
            if (Point.distance( p, tr.c ) < tr.r)
                toSplit.add( tr );
        }
        
        if (toSplit.size() > 0) {
            
            points.add( p );
            
            List<Point> a = new Vector<>();
            List<Point> b = new Vector<>();
            for (Triangle  t1 : toSplit)
            {
                boolean e1 = true;
                boolean e2 = true;
                boolean e3 = true;
                for (Triangle t2 : toSplit) if (!t2.equals(t1))
                {
                    // If triangles have a common edge, it goes in opposite directions
                    if (e1 && t2.hasEdge( t1.p2, t1.p1 )) e1 = false;
                    if (e2 && t2.hasEdge( t1.p3, t1.p2 )) e2 = false;
                    if (e3 && t2.hasEdge( t1.p1, t1.p3 )) e3 = false;
                    if (!(e1 || e2 || e3)) break;
                }
                if (e1) { a.add( t1.p1 ); b.add( t1.p2 ); }
                if (e2) { a.add( t1.p2 ); b.add( t1.p3 ); }
                if (e3) { a.add( t1.p3 ); b.add( t1.p1 ); }
            }
            
            int index = 0;
            do
            {
                triangles.add( new Triangle( p, a.get(index), b.get(index) ) );
                index = a.indexOf( b.get(index) );
            }
            while (index != 0);
            
            for (Triangle tr : toSplit)
                triangles.remove( tr );
            
            _regionsDirty = true;
        }
    }
    
    public static Voronoi from(Point... vertices )
    {
        return from(Arrays.asList());
    }
    public static Voronoi from(List<Point> vertices )
    {
        double minx = Double.POSITIVE_INFINITY;
        double miny = Double.POSITIVE_INFINITY;
        double maxx = Double.NEGATIVE_INFINITY;
        double maxy = Double.NEGATIVE_INFINITY;
        for (Point v : vertices) {
            if (v.x < minx) minx = v.x;
            if (v.y < miny) miny = v.y;
            if (v.x > maxx) maxx = v.x;
            if (v.y > maxy) maxy = v.y;
        }
        double dx = (maxx - minx) * 0.5;
        double dy = (maxy - miny) * 0.5;
        
        Voronoi voronoi = new Voronoi( minx - dx/2., miny - dy/2., maxx + dx/2., maxy + dy/2. );
        for (Point v : vertices)
            voronoi.add( v );
    
        return voronoi;
    }
    
    public boolean isReal(Triangle tr )
    {
        return !(frame.contains( tr.p1 ) || frame.contains( tr.p2 ) || frame.contains( tr.p3 ));
    }
    
    public List<Triangle> triangulation()
    {
        return triangulation(false);
    }
    
    public List<Triangle> triangulation(boolean _x)
    {
        List<Triangle> _ret = new ArrayList<>();
        for(Triangle _t : this.triangles)
        {
            if(_x || this.isReal(_t))
            {
                _ret.add(_t);
            }
        }
        return _ret;
    }
    
    
    public List<Region> partioning()
    {
        // Iterating over points, not regions, to use points ordering
        List<Region> result = new Vector<>();
        for (Point p : points)
        {
            Region r = this.getRegions().get(p);
            var isReal = true;
            for (Triangle v : r.vertices)
            {
                if (!this.isReal( v )) {
                    isReal = false;
                    break;
                }
            }
            
            if (isReal) result.add( r );
        }
        return result;
    }
    
    
    public static void main(String[] args)
    {
        Voronoi _v = Voronoi.from(0,0,10,10);
        
        _v.add(3,4);
        _v.add(7,8);
        
        for(Triangle _t : _v.triangulation(true))
        {
            System.err.println(_t.asString());
        }

        for(Map.Entry<Point, Region> _e : _v.getRegions().entrySet())
        {
            System.err.println(_e.getKey().asString()+" = "+_e.getValue().asString());
        }
    }
}

// https://github.com/watabou/TownGeneratorOS/blob/master/Source/com/watabou/geom/Voronoi.hx