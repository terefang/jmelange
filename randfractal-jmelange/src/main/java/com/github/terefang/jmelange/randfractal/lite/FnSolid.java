package com.github.terefang.jmelange.randfractal.lite;

import com.github.terefang.jmelange.random.ArcRandom;

public class FnSolid extends FastNoiseLite
{
    // ----------------------------------------------------------------------------

    public static synchronized void mkTable()
    {
        if(TABLE1==null)
        {
            ArcRandom _arc = new ArcRandom();
            _arc.setSeed(0xd1ceBeafCaf34ff3L);
            TABLE1 = _arc.getContext();
            _arc = new ArcRandom();
            _arc.setSeed(0xcaf34ff3D1ceBeafL);
            TABLE2 = _arc.getContext();
            _arc = new ArcRandom();
            _arc.setSeed(0x123456789abcdefL);
            TABLE3 = _arc.getContext();
        }
    }

    static int[] TABLE1 = null;
    static int[] TABLE2 = null;
    static int[] TABLE3 = null;

    // 2d
    public static final float singleSolid(int interpolation, int seed, float x, float y)
    {
        mkTable();

        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int x1 = x0 + 1;
        int y1 = y0 + 1;

        float xs, ys;
        switch (interpolation) {
            default:
            case LINEAR:
                xs = x - x0;
                ys = y - y0;
                break;
            case HERMITE:
                xs = hermiteInterpolator(x - x0);
                ys = hermiteInterpolator(y - y0);
                break;
            case QUINTIC:
                xs = quinticInterpolator(x - x0);
                ys = quinticInterpolator(y - y0);
                break;
        }

        float xd0 = x - x0;
        float yd0 = y - y0;
        float xd1 = xd0 - 1;
        float yd1 = yd0 - 1;

        float xf0 = lerp(table2D(seed, x0, y0, xd0, yd0), table2D(seed, x1, y0, xd1, yd0), xs);
        float xf1 = lerp(table2D(seed, x0, y1, xd0, yd1), table2D(seed, x1, y1, xd1, yd1), xs);

        return lerp(xf0, xf1, ys);
    }

    private static float table2D(int _seed, int _x0, int _y0, float _xd, float _yd)
    {
        int _hash = hash256(_x0, _y0, _seed) & -2 ;
        float _n0 = 128f-((float)TABLE1[_hash]);
        float _n1 = 128f-((float)TABLE2[_hash]);
        float _n2 = 128f-((float)TABLE1[_hash+1]);
        float _n3 = 128f-((float)TABLE2[_hash+1]);

        return _xd*(_n0/128f)+_yd*(_n1/128f)-(_n2/255f)+(_n3/511f);
    }

    public static final float singleSolid(int interpolation, int seed, float x, float y, float z)
    {
        mkTable();

        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int z0 = fastFloor(z);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;

        float xs, ys, zs;
        switch (interpolation) {
            default:
            case LINEAR:
                xs = x - x0;
                ys = y - y0;
                zs = z - z0;
                break;
            case HERMITE:
                xs = hermiteInterpolator(x - x0);
                ys = hermiteInterpolator(y - y0);
                zs = hermiteInterpolator(z - z0);
                break;
            case QUINTIC:
                xs = quinticInterpolator(x - x0);
                ys = quinticInterpolator(y - y0);
                zs = quinticInterpolator(z - z0);
                break;
        }

        final float xd0 = x - x0;
        final float yd0 = y - y0;
        final float zd0 = z - z0;
        final float xd1 = xd0 - 1;
        final float yd1 = yd0 - 1;
        final float zd1 = zd0 - 1;

        final float xf00 = lerp(table3D(seed, x0, y0, z0, xd0, yd0, zd0), table3D(seed, x1, y0, z0, xd1, yd0, zd0), xs);
        final float xf10 = lerp(table3D(seed, x0, y1, z0, xd0, yd1, zd0), table3D(seed, x1, y1, z0, xd1, yd1, zd0), xs);
        final float xf01 = lerp(table3D(seed, x0, y0, z1, xd0, yd0, zd1), table3D(seed, x1, y0, z1, xd1, yd0, zd1), xs);
        final float xf11 = lerp(table3D(seed, x0, y1, z1, xd0, yd1, zd1), table3D(seed, x1, y1, z1, xd1, yd1, zd1), xs);

        final float yf0 = lerp(xf00, xf10, ys);
        final float yf1 = lerp(xf01, xf11, ys);

        return lerp(yf0, yf1, zs);
    }

    private static float table3D(int _seed, int _x0, int _y0, int _z0, float _xd, float _yd, float _zd)
    {
        int _hash = hash256(_x0, _y0, _z0, _seed) & -2 ;
        float _n0 = 128f-((float)TABLE1[_hash]);
        float _n1 = 128f-((float)TABLE2[_hash]);
        float _n2 = 128f-((float)TABLE3[_hash]);
        float _n3 = 128f-((float)TABLE1[_hash+1]);
        float _n4 = 128f-((float)TABLE2[_hash+1]);
        float _n5 = 128f-((float)TABLE3[_hash+1]);

        return _xd*(_n0/128f)+_yd*(_n1/128f)+_zd*(_n2/128f)-(_n3/255f)+(_n4/511f)-(_n5/1023f);
    }

}
