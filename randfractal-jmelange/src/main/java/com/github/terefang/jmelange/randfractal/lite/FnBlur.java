package com.github.terefang.jmelange.randfractal.lite;

public class FnBlur extends FastNoiseLite
{
    // ----------------------------------------------------------------------------
    // 19x19 sigma 1.8 gauss filter kernel
    static int kernelPF_size = 19;
    static double kernelPF[]= {
            6.822021344110449E-13,
            9.402987988084052E-12,
            9.518689974931354E-11,
            7.07696383009509E-10,
            3.864339401711907E-9,
            1.5497514221593594E-8,
            4.564648256577528E-8,
            9.874408370274873E-8,
            1.5688208199816736E-7,
            1.8306026960660868E-7,
            1.5688208199816736E-7,
            9.874408370274873E-8,
            4.564648256577528E-8,
            1.5497514221593594E-8,
            3.864339401711907E-9,
            7.07696383009509E-10,
            9.518689974931354E-11,
            9.402987988084052E-12,
            6.822021344110449E-13,
            9.402987988084052E-12,
            1.296040845436277E-10,
            1.3119883826491617E-9,
            9.754382539998703E-9,
            5.3263300044563384E-8,
            2.1360669033469094E-7,
            6.291585816201222E-7,
            1.361018070916605E-6,
            2.1623508021532578E-6,
            2.5231722819108467E-6,
            2.1623508021532578E-6,
            1.361018070916605E-6,
            6.291585816201222E-7,
            2.1360669033469094E-7,
            5.3263300044563384E-8,
            9.754382539998703E-9,
            1.3119883826491617E-9,
            1.296040845436277E-10,
            9.402987988084052E-12,
            9.518689974931354E-11,
            1.3119883826491617E-9,
            1.328132151287971E-8,
            9.874408370274873E-8,
            5.39186948668272E-7,
            2.1623508021532578E-6,
            6.3690025884311965E-6,
            1.3777651405863432E-5,
            2.1889581193578554E-5,
            2.55421943910653E-5,
            2.1889581193578554E-5,
            1.3777651405863432E-5,
            6.3690025884311965E-6,
            2.1623508021532578E-6,
            5.39186948668272E-7,
            9.874408370274873E-8,
            1.328132151287971E-8,
            1.3119883826491617E-9,
            9.518689974931354E-11,
            7.07696383009509E-10,
            9.754382539998703E-9,
            9.874408370274873E-8,
            7.341433649384898E-7,
            4.008751775122532E-6,
            1.6076664388815854E-5,
            4.7352315361478844E-5,
            1.0243420147073071E-4,
            1.6274484700191428E-4,
            1.8990132709740948E-4,
            1.6274484700191428E-4,
            1.0243420147073071E-4,
            4.7352315361478844E-5,
            1.6076664388815854E-5,
            4.008751775122532E-6,
            7.341433649384898E-7,
            9.874408370274873E-8,
            9.754382539998703E-9,
            7.07696383009509E-10,
            3.864339401711907E-9,
            5.3263300044563384E-8,
            5.39186948668272E-7,
            4.008751775122532E-6,
            2.1889581193578554E-5,
            8.778579223707142E-5,
            2.585648625693089E-4,
            5.593366453886795E-4,
            8.886597978933318E-4,
            0.0010369463492510736,
            8.886597978933318E-4,
            5.593366453886795E-4,
            2.585648625693089E-4,
            8.778579223707142E-5,
            2.1889581193578554E-5,
            4.008751775122532E-6,
            5.39186948668272E-7,
            5.3263300044563384E-8,
            3.864339401711907E-9,
            1.5497514221593594E-8,
            2.1360669033469094E-7,
            2.1623508021532578E-6,
            1.6076664388815854E-5,
            8.778579223707142E-5,
            3.520554025469868E-4,
            0.0010369463492510736,
            0.0022431589763387207,
            0.0035638737761774534,
            0.004158560914041074,
            0.0035638737761774534,
            0.0022431589763387207,
            0.0010369463492510736,
            3.520554025469868E-4,
            8.778579223707142E-5,
            1.6076664388815854E-5,
            2.1623508021532578E-6,
            2.1360669033469094E-7,
            1.5497514221593594E-8,
            4.564648256577528E-8,
            6.291585816201222E-7,
            6.3690025884311965E-6,
            4.7352315361478844E-5,
            2.585648625693089E-4,
            0.0010369463492510736,
            0.003054228747651787,
            0.006607015527886323,
            0.010497057777449161,
            0.012248653270922526,
            0.010497057777449161,
            0.006607015527886323,
            0.003054228747651787,
            0.0010369463492510736,
            2.585648625693089E-4,
            4.7352315361478844E-5,
            6.3690025884311965E-6,
            6.291585816201222E-7,
            4.564648256577528E-8,
            9.874408370274873E-8,
            1.361018070916605E-6,
            1.3777651405863432E-5,
            1.0243420147073071E-4,
            5.593366453886795E-4,
            0.0022431589763387207,
            0.006607015527886323,
            0.014292529405105257,
            0.02270760622826592,
            0.026496719480785674,
            0.02270760622826592,
            0.014292529405105257,
            0.006607015527886323,
            0.0022431589763387207,
            5.593366453886795E-4,
            1.0243420147073071E-4,
            1.3777651405863432E-5,
            1.361018070916605E-6,
            9.874408370274873E-8,
            1.5688208199816736E-7,
            2.1623508021532578E-6,
            2.1889581193578554E-5,
            1.6274484700191428E-4,
            8.886597978933318E-4,
            0.0035638737761774534,
            0.010497057777449161,
            0.02270760622826592,
            0.03607726568215401,
            0.04209731218713365,
            0.03607726568215401,
            0.02270760622826592,
            0.010497057777449161,
            0.0035638737761774534,
            8.886597978933318E-4,
            1.6274484700191428E-4,
            2.1889581193578554E-5,
            2.1623508021532578E-6,
            1.5688208199816736E-7,
            1.8306026960660868E-7,
            2.5231722819108467E-6,
            2.55421943910653E-5,
            1.8990132709740948E-4,
            0.0010369463492510736,
            0.004158560914041074,
            0.012248653270922526,
            0.026496719480785674,
            0.04209731218713365,
            0.049121896016017075,
            0.04209731218713365,
            0.026496719480785674,
            0.012248653270922526,
            0.004158560914041074,
            0.0010369463492510736,
            1.8990132709740948E-4,
            2.55421943910653E-5,
            2.5231722819108467E-6,
            1.8306026960660868E-7,
            1.5688208199816736E-7,
            2.1623508021532578E-6,
            2.1889581193578554E-5,
            1.6274484700191428E-4,
            8.886597978933318E-4,
            0.0035638737761774534,
            0.010497057777449161,
            0.02270760622826592,
            0.03607726568215401,
            0.04209731218713365,
            0.03607726568215401,
            0.02270760622826592,
            0.010497057777449161,
            0.0035638737761774534,
            8.886597978933318E-4,
            1.6274484700191428E-4,
            2.1889581193578554E-5,
            2.1623508021532578E-6,
            1.5688208199816736E-7,
            9.874408370274873E-8,
            1.361018070916605E-6,
            1.3777651405863432E-5,
            1.0243420147073071E-4,
            5.593366453886795E-4,
            0.0022431589763387207,
            0.006607015527886323,
            0.014292529405105257,
            0.02270760622826592,
            0.026496719480785674,
            0.02270760622826592,
            0.014292529405105257,
            0.006607015527886323,
            0.0022431589763387207,
            5.593366453886795E-4,
            1.0243420147073071E-4,
            1.3777651405863432E-5,
            1.361018070916605E-6,
            9.874408370274873E-8,
            4.564648256577528E-8,
            6.291585816201222E-7,
            6.3690025884311965E-6,
            4.7352315361478844E-5,
            2.585648625693089E-4,
            0.0010369463492510736,
            0.003054228747651787,
            0.006607015527886323,
            0.010497057777449161,
            0.012248653270922526,
            0.010497057777449161,
            0.006607015527886323,
            0.003054228747651787,
            0.0010369463492510736,
            2.585648625693089E-4,
            4.7352315361478844E-5,
            6.3690025884311965E-6,
            6.291585816201222E-7,
            4.564648256577528E-8,
            1.5497514221593594E-8,
            2.1360669033469094E-7,
            2.1623508021532578E-6,
            1.6076664388815854E-5,
            8.778579223707142E-5,
            3.520554025469868E-4,
            0.0010369463492510736,
            0.0022431589763387207,
            0.0035638737761774534,
            0.004158560914041074,
            0.0035638737761774534,
            0.0022431589763387207,
            0.0010369463492510736,
            3.520554025469868E-4,
            8.778579223707142E-5,
            1.6076664388815854E-5,
            2.1623508021532578E-6,
            2.1360669033469094E-7,
            1.5497514221593594E-8,
            3.864339401711907E-9,
            5.3263300044563384E-8,
            5.39186948668272E-7,
            4.008751775122532E-6,
            2.1889581193578554E-5,
            8.778579223707142E-5,
            2.585648625693089E-4,
            5.593366453886795E-4,
            8.886597978933318E-4,
            0.0010369463492510736,
            8.886597978933318E-4,
            5.593366453886795E-4,
            2.585648625693089E-4,
            8.778579223707142E-5,
            2.1889581193578554E-5,
            4.008751775122532E-6,
            5.39186948668272E-7,
            5.3263300044563384E-8,
            3.864339401711907E-9,
            7.07696383009509E-10,
            9.754382539998703E-9,
            9.874408370274873E-8,
            7.341433649384898E-7,
            4.008751775122532E-6,
            1.6076664388815854E-5,
            4.7352315361478844E-5,
            1.0243420147073071E-4,
            1.6274484700191428E-4,
            1.8990132709740948E-4,
            1.6274484700191428E-4,
            1.0243420147073071E-4,
            4.7352315361478844E-5,
            1.6076664388815854E-5,
            4.008751775122532E-6,
            7.341433649384898E-7,
            9.874408370274873E-8,
            9.754382539998703E-9,
            7.07696383009509E-10,
            9.518689974931354E-11,
            1.3119883826491617E-9,
            1.328132151287971E-8,
            9.874408370274873E-8,
            5.39186948668272E-7,
            2.1623508021532578E-6,
            6.3690025884311965E-6,
            1.3777651405863432E-5,
            2.1889581193578554E-5,
            2.55421943910653E-5,
            2.1889581193578554E-5,
            1.3777651405863432E-5,
            6.3690025884311965E-6,
            2.1623508021532578E-6,
            5.39186948668272E-7,
            9.874408370274873E-8,
            1.328132151287971E-8,
            1.3119883826491617E-9,
            9.518689974931354E-11,
            9.402987988084052E-12,
            1.296040845436277E-10,
            1.3119883826491617E-9,
            9.754382539998703E-9,
            5.3263300044563384E-8,
            2.1360669033469094E-7,
            6.291585816201222E-7,
            1.361018070916605E-6,
            2.1623508021532578E-6,
            2.5231722819108467E-6,
            2.1623508021532578E-6,
            1.361018070916605E-6,
            6.291585816201222E-7,
            2.1360669033469094E-7,
            5.3263300044563384E-8,
            9.754382539998703E-9,
            1.3119883826491617E-9,
            1.296040845436277E-10,
            9.402987988084052E-12,
            6.822021344110449E-13,
            9.402987988084052E-12,
            9.518689974931354E-11,
            7.07696383009509E-10,
            3.864339401711907E-9,
            1.5497514221593594E-8,
            4.564648256577528E-8,
            9.874408370274873E-8,
            1.5688208199816736E-7,
            1.8306026960660868E-7,
            1.5688208199816736E-7,
            9.874408370274873E-8,
            4.564648256577528E-8,
            1.5497514221593594E-8,
            3.864339401711907E-9,
            7.07696383009509E-10,
            9.518689974931354E-11,
            9.402987988084052E-12,
            6.822021344110449E-13,
    };


    // 2d Blur
    public static final double singleBlur(int interpolation, int seed, double x, double y)
    {
        final int _rsize = (kernelPF_size >> 1);

        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int x1 = x0 + 1;
        int y1 = y0 + 1;

        double xs = x - x0;
        double ys = y - y0;

        double _v = 0.;
        for(int _x=-_rsize; _x<=_rsize; _x++)
        {
            for(int _y=-_rsize; _y<=_rsize; _y++)
            {

                double v00 = valCoord2D(seed, x0+_x, y0+_y);
                double v01 = valCoord2D(seed, x0+_x, y1+_y);
                double v10 = valCoord2D(seed, x1+_x, y0+_y);
                double v11 = valCoord2D(seed, x1+_x, y1+_y);

                double xf0 = lerp(v00, v10, xs);
                double xf1 = lerp(v01, v11, xs);

                _v += kernelPF[(_rsize+_x)*kernelPF_size+(_rsize+_y)] * lerp(xf0, xf1, ys);
            }
        }

        return _v;
    }

    // 3d Blur
    public static final double singleBlur(int interpolation, int seed, double x, double y, double z) {
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int z0 = fastFloor(z);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;

        double xs, ys, zs;
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

        final double xf00 = lerp(valCoord3D(seed, x0, y0, z0), valCoord3D(seed, x1, y0, z0), xs);
        final double xf10 = lerp(valCoord3D(seed, x0, y1, z0), valCoord3D(seed, x1, y1, z0), xs);
        final double xf01 = lerp(valCoord3D(seed, x0, y0, z1), valCoord3D(seed, x1, y0, z1), xs);
        final double xf11 = lerp(valCoord3D(seed, x0, y1, z1), valCoord3D(seed, x1, y1, z1), xs);

        final double yf0 = lerp(xf00, xf10, ys);
        final double yf1 = lerp(xf01, xf11, ys);

        return lerp(yf0, yf1, zs);
    }

    // 4d Blur
    public static final double singleBlur(int interpolation, int seed, double x, double y, double z, double w) {
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int z0 = fastFloor(z);
        int w0 = fastFloor(w);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;
        int w1 = w0 + 1;

        double xs, ys, zs, ws;
        switch (interpolation) {
            default:
            case LINEAR:
                xs = x - x0;
                ys = y - y0;
                zs = z - z0;
                ws = w - w0;
                break;
            case HERMITE:
                xs = hermiteInterpolator(x - x0);
                ys = hermiteInterpolator(y - y0);
                zs = hermiteInterpolator(z - z0);
                ws = hermiteInterpolator(w - w0);
                break;
            case QUINTIC:
                xs = quinticInterpolator(x - x0);
                ys = quinticInterpolator(y - y0);
                zs = quinticInterpolator(z - z0);
                ws = quinticInterpolator(w - w0);
                break;
        }

        final double xf000 = lerp(valCoord4D(seed, x0, y0, z0, w0), valCoord4D(seed, x1, y0, z0, w0), xs);
        final double xf100 = lerp(valCoord4D(seed, x0, y1, z0, w0), valCoord4D(seed, x1, y1, z0, w0), xs);
        final double xf010 = lerp(valCoord4D(seed, x0, y0, z1, w0), valCoord4D(seed, x1, y0, z1, w0), xs);
        final double xf110 = lerp(valCoord4D(seed, x0, y1, z1, w0), valCoord4D(seed, x1, y1, z1, w0), xs);
        final double xf001 = lerp(valCoord4D(seed, x0, y0, z0, w1), valCoord4D(seed, x1, y0, z0, w1), xs);
        final double xf101 = lerp(valCoord4D(seed, x0, y1, z0, w1), valCoord4D(seed, x1, y1, z0, w1), xs);
        final double xf011 = lerp(valCoord4D(seed, x0, y0, z1, w1), valCoord4D(seed, x1, y0, z1, w1), xs);
        final double xf111 = lerp(valCoord4D(seed, x0, y1, z1, w1), valCoord4D(seed, x1, y1, z1, w1), xs);

        final double yf00 = lerp(xf000, xf100, ys);
        final double yf10 = lerp(xf010, xf110, ys);
        final double yf01 = lerp(xf001, xf101, ys);
        final double yf11 = lerp(xf011, xf111, ys);

        final double zf0 = lerp(yf00, yf10, zs);
        final double zf1 = lerp(yf01, yf11, zs);
        return lerp(zf0, zf1, ws);
    }

    // 5d Blur
    public static final double singleBlur(int interpolation, int seed, double x, double y, double z, double w, double u) {
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int z0 = fastFloor(z);
        int w0 = fastFloor(w);
        int u0 = fastFloor(u);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;
        int w1 = w0 + 1;
        int u1 = u0 + 1;

        double xs, ys, zs, ws, us;
        switch (interpolation) {
            default:
            case LINEAR:
                xs = x - x0;
                ys = y - y0;
                zs = z - z0;
                ws = w - w0;
                us = u - u0;
                break;
            case HERMITE:
                xs = hermiteInterpolator(x - x0);
                ys = hermiteInterpolator(y - y0);
                zs = hermiteInterpolator(z - z0);
                ws = hermiteInterpolator(w - w0);
                us = hermiteInterpolator(u - u0);
                break;
            case QUINTIC:
                xs = quinticInterpolator(x - x0);
                ys = quinticInterpolator(y - y0);
                zs = quinticInterpolator(z - z0);
                ws = quinticInterpolator(w - w0);
                us = quinticInterpolator(u - u0);
                break;
        }

        final double xf0000 = lerp(valCoord5D(seed, x0, y0, z0, w0, u0), valCoord5D(seed, x1, y0, z0, w0, u0), xs);
        final double xf1000 = lerp(valCoord5D(seed, x0, y1, z0, w0, u0), valCoord5D(seed, x1, y1, z0, w0, u0), xs);
        final double xf0100 = lerp(valCoord5D(seed, x0, y0, z1, w0, u0), valCoord5D(seed, x1, y0, z1, w0, u0), xs);
        final double xf1100 = lerp(valCoord5D(seed, x0, y1, z1, w0, u0), valCoord5D(seed, x1, y1, z1, w0, u0), xs);
        final double xf0010 = lerp(valCoord5D(seed, x0, y0, z0, w1, u0), valCoord5D(seed, x1, y0, z0, w1, u0), xs);
        final double xf1010 = lerp(valCoord5D(seed, x0, y1, z0, w1, u0), valCoord5D(seed, x1, y1, z0, w1, u0), xs);
        final double xf0110 = lerp(valCoord5D(seed, x0, y0, z1, w1, u0), valCoord5D(seed, x1, y0, z1, w1, u0), xs);
        final double xf1110 = lerp(valCoord5D(seed, x0, y1, z1, w1, u0), valCoord5D(seed, x1, y1, z1, w1, u0), xs);
        final double xf0001 = lerp(valCoord5D(seed, x0, y0, z0, w0, u1), valCoord5D(seed, x1, y0, z0, w0, u1), xs);
        final double xf1001 = lerp(valCoord5D(seed, x0, y1, z0, w0, u1), valCoord5D(seed, x1, y1, z0, w0, u1), xs);
        final double xf0101 = lerp(valCoord5D(seed, x0, y0, z1, w0, u1), valCoord5D(seed, x1, y0, z1, w0, u1), xs);
        final double xf1101 = lerp(valCoord5D(seed, x0, y1, z1, w0, u1), valCoord5D(seed, x1, y1, z1, w0, u1), xs);
        final double xf0011 = lerp(valCoord5D(seed, x0, y0, z0, w1, u1), valCoord5D(seed, x1, y0, z0, w1, u1), xs);
        final double xf1011 = lerp(valCoord5D(seed, x0, y1, z0, w1, u1), valCoord5D(seed, x1, y1, z0, w1, u1), xs);
        final double xf0111 = lerp(valCoord5D(seed, x0, y0, z1, w1, u1), valCoord5D(seed, x1, y0, z1, w1, u1), xs);
        final double xf1111 = lerp(valCoord5D(seed, x0, y1, z1, w1, u1), valCoord5D(seed, x1, y1, z1, w1, u1), xs);

        final double yf000 = lerp(xf0000, xf1000, ys);
        final double yf100 = lerp(xf0100, xf1100, ys);
        final double yf010 = lerp(xf0010, xf1010, ys);
        final double yf110 = lerp(xf0110, xf1110, ys);
        final double yf001 = lerp(xf0001, xf1001, ys);
        final double yf101 = lerp(xf0101, xf1101, ys);
        final double yf011 = lerp(xf0011, xf1011, ys);
        final double yf111 = lerp(xf0111, xf1111, ys);

        final double zf00 = lerp(yf000, yf100, zs);
        final double zf10 = lerp(yf010, yf110, zs);
        final double zf01 = lerp(yf001, yf101, zs);
        final double zf11 = lerp(yf011, yf111, zs);

        final double wf0 = lerp(zf00, zf10, ws);
        final double wf1 = lerp(zf01, zf11, ws);

        return lerp(wf0, wf1, us);
    }

    // 6d Blur
    public static final double singleBlur(int interpolation, int seed, double x, double y, double z, double w, double u, double v) {
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int z0 = fastFloor(z);
        int w0 = fastFloor(w);
        int u0 = fastFloor(u);
        int v0 = fastFloor(v);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;
        int w1 = w0 + 1;
        int u1 = u0 + 1;
        int v1 = v0 + 1;

        double xs, ys, zs, ws, us, vs;
        switch (interpolation) {
            default:
            case LINEAR:
                xs = x - x0;
                ys = y - y0;
                zs = z - z0;
                ws = w - w0;
                us = u - u0;
                vs = v - v0;
                break;
            case HERMITE:
                xs = hermiteInterpolator(x - x0);
                ys = hermiteInterpolator(y - y0);
                zs = hermiteInterpolator(z - z0);
                ws = hermiteInterpolator(w - w0);
                us = hermiteInterpolator(u - u0);
                vs = hermiteInterpolator(v - v0);
                break;
            case QUINTIC:
                xs = quinticInterpolator(x - x0);
                ys = quinticInterpolator(y - y0);
                zs = quinticInterpolator(z - z0);
                ws = quinticInterpolator(w - w0);
                us = quinticInterpolator(u - u0);
                vs = quinticInterpolator(v - v0);
                break;
        }

        final double xf00000 = lerp(valCoord6D(seed, x0, y0, z0, w0, u0, v0), valCoord6D(seed, x1, y0, z0, w0, u0, v0), xs);
        final double xf10000 = lerp(valCoord6D(seed, x0, y1, z0, w0, u0, v0), valCoord6D(seed, x1, y1, z0, w0, u0, v0), xs);
        final double xf01000 = lerp(valCoord6D(seed, x0, y0, z1, w0, u0, v0), valCoord6D(seed, x1, y0, z1, w0, u0, v0), xs);
        final double xf11000 = lerp(valCoord6D(seed, x0, y1, z1, w0, u0, v0), valCoord6D(seed, x1, y1, z1, w0, u0, v0), xs);
        final double xf00100 = lerp(valCoord6D(seed, x0, y0, z0, w1, u0, v0), valCoord6D(seed, x1, y0, z0, w1, u0, v0), xs);
        final double xf10100 = lerp(valCoord6D(seed, x0, y1, z0, w1, u0, v0), valCoord6D(seed, x1, y1, z0, w1, u0, v0), xs);
        final double xf01100 = lerp(valCoord6D(seed, x0, y0, z1, w1, u0, v0), valCoord6D(seed, x1, y0, z1, w1, u0, v0), xs);
        final double xf11100 = lerp(valCoord6D(seed, x0, y1, z1, w1, u0, v0), valCoord6D(seed, x1, y1, z1, w1, u0, v0), xs);

        final double xf00010 = lerp(valCoord6D(seed, x0, y0, z0, w0, u1, v0), valCoord6D(seed, x1, y0, z0, w0, u1, v0), xs);
        final double xf10010 = lerp(valCoord6D(seed, x0, y1, z0, w0, u1, v0), valCoord6D(seed, x1, y1, z0, w0, u1, v0), xs);
        final double xf01010 = lerp(valCoord6D(seed, x0, y0, z1, w0, u1, v0), valCoord6D(seed, x1, y0, z1, w0, u1, v0), xs);
        final double xf11010 = lerp(valCoord6D(seed, x0, y1, z1, w0, u1, v0), valCoord6D(seed, x1, y1, z1, w0, u1, v0), xs);
        final double xf00110 = lerp(valCoord6D(seed, x0, y0, z0, w1, u1, v0), valCoord6D(seed, x1, y0, z0, w1, u1, v0), xs);
        final double xf10110 = lerp(valCoord6D(seed, x0, y1, z0, w1, u1, v0), valCoord6D(seed, x1, y1, z0, w1, u1, v0), xs);
        final double xf01110 = lerp(valCoord6D(seed, x0, y0, z1, w1, u1, v0), valCoord6D(seed, x1, y0, z1, w1, u1, v0), xs);
        final double xf11110 = lerp(valCoord6D(seed, x0, y1, z1, w1, u1, v0), valCoord6D(seed, x1, y1, z1, w1, u1, v0), xs);

        final double xf00001 = lerp(valCoord6D(seed, x0, y0, z0, w0, u0, v1), valCoord6D(seed, x1, y0, z0, w0, u0, v1), xs);
        final double xf10001 = lerp(valCoord6D(seed, x0, y1, z0, w0, u0, v1), valCoord6D(seed, x1, y1, z0, w0, u0, v1), xs);
        final double xf01001 = lerp(valCoord6D(seed, x0, y0, z1, w0, u0, v1), valCoord6D(seed, x1, y0, z1, w0, u0, v1), xs);
        final double xf11001 = lerp(valCoord6D(seed, x0, y1, z1, w0, u0, v1), valCoord6D(seed, x1, y1, z1, w0, u0, v1), xs);
        final double xf00101 = lerp(valCoord6D(seed, x0, y0, z0, w1, u0, v1), valCoord6D(seed, x1, y0, z0, w1, u0, v1), xs);
        final double xf10101 = lerp(valCoord6D(seed, x0, y1, z0, w1, u0, v1), valCoord6D(seed, x1, y1, z0, w1, u0, v1), xs);
        final double xf01101 = lerp(valCoord6D(seed, x0, y0, z1, w1, u0, v1), valCoord6D(seed, x1, y0, z1, w1, u0, v1), xs);
        final double xf11101 = lerp(valCoord6D(seed, x0, y1, z1, w1, u0, v1), valCoord6D(seed, x1, y1, z1, w1, u0, v1), xs);

        final double xf00011 = lerp(valCoord6D(seed, x0, y0, z0, w0, u1, v1), valCoord6D(seed, x1, y0, z0, w0, u1, v1), xs);
        final double xf10011 = lerp(valCoord6D(seed, x0, y1, z0, w0, u1, v1), valCoord6D(seed, x1, y1, z0, w0, u1, v1), xs);
        final double xf01011 = lerp(valCoord6D(seed, x0, y0, z1, w0, u1, v1), valCoord6D(seed, x1, y0, z1, w0, u1, v1), xs);
        final double xf11011 = lerp(valCoord6D(seed, x0, y1, z1, w0, u1, v1), valCoord6D(seed, x1, y1, z1, w0, u1, v1), xs);
        final double xf00111 = lerp(valCoord6D(seed, x0, y0, z0, w1, u1, v1), valCoord6D(seed, x1, y0, z0, w1, u1, v1), xs);
        final double xf10111 = lerp(valCoord6D(seed, x0, y1, z0, w1, u1, v1), valCoord6D(seed, x1, y1, z0, w1, u1, v1), xs);
        final double xf01111 = lerp(valCoord6D(seed, x0, y0, z1, w1, u1, v1), valCoord6D(seed, x1, y0, z1, w1, u1, v1), xs);
        final double xf11111 = lerp(valCoord6D(seed, x0, y1, z1, w1, u1, v1), valCoord6D(seed, x1, y1, z1, w1, u1, v1), xs);

        final double yf0000 = lerp(xf00000, xf10000, ys);
        final double yf1000 = lerp(xf01000, xf11000, ys);
        final double yf0100 = lerp(xf00100, xf10100, ys);
        final double yf1100 = lerp(xf01100, xf11100, ys);

        final double yf0010 = lerp(xf00010, xf10010, ys);
        final double yf1010 = lerp(xf01010, xf11010, ys);
        final double yf0110 = lerp(xf00110, xf10110, ys);
        final double yf1110 = lerp(xf01110, xf11110, ys);

        final double yf0001 = lerp(xf00001, xf10001, ys);
        final double yf1001 = lerp(xf01001, xf11001, ys);
        final double yf0101 = lerp(xf00101, xf10101, ys);
        final double yf1101 = lerp(xf01101, xf11101, ys);

        final double yf0011 = lerp(xf00011, xf10011, ys);
        final double yf1011 = lerp(xf01011, xf11011, ys);
        final double yf0111 = lerp(xf00111, xf10111, ys);
        final double yf1111 = lerp(xf01111, xf11111, ys);

        final double zf000 = lerp(yf0000, yf1000, zs);
        final double zf100 = lerp(yf0100, yf1100, zs);

        final double zf010 = lerp(yf0010, yf1010, zs);
        final double zf110 = lerp(yf0110, yf1110, zs);

        final double zf001 = lerp(yf0001, yf1001, zs);
        final double zf101 = lerp(yf0101, yf1101, zs);

        final double zf011 = lerp(yf0011, yf1011, zs);
        final double zf111 = lerp(yf0111, yf1111, zs);

        final double wf00 = lerp(zf000, zf100, ws);
        final double wf10 = lerp(zf010, zf110, ws);
        final double wf01 = lerp(zf001, zf101, ws);
        final double wf11 = lerp(zf011, zf111, ws);

        final double uf0 = lerp(wf00, wf10, us);
        final double uf1 = lerp(wf01, wf11, us);

        return lerp(uf0, uf1, vs);
    }
}
