package com.github.terefang.jmelange.randfractal.lite;

import static com.github.terefang.jmelange.randfractal.lite.FnValue.singleValue;

public class FnFoam extends FastNoiseLite
{
    // ----------------------------------------------------------------------------

    public static final double singleFoam(NoiseType _type, double foamSharpness, boolean _normalizeNoise, int seed, double x, double y) {
        final double p0 = x;
        final double p1 = x * -0.5f + y * 0.8660254037844386f;
        final double p2 = x * -0.5f + y * -0.8660254037844387f;

        double xin = p2;
        double yin = p0;
        double a = singleNoiseByType(_type,seed, xin, yin);
        if(_normalizeNoise) a = (a * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p1;
        yin = p2;
        double b = singleNoiseByType(_type,seed, xin + a, yin);
        if(_normalizeNoise) b = (b * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        double c = singleNoiseByType(_type,seed, xin + b, yin);
        if(_normalizeNoise) c = (c * .5f) + .5f;
        final double result = (a + b + c) * F3f;
        final double sharp = foamSharpness * 2.2f;
        final double diff = 0.5f - result;
        final int sign = Float.floatToRawIntBits((float) diff) >> 31, one = sign | 1;
        return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;
    }

    public static final double singleFoam(NoiseType _type, double foamSharpness, boolean _normalizeNoise, int seed, double x, double y, double z){
        final double p0 = x;
        final double p1 = x * -0.3333333333333333f + y * 0.9428090415820634f;
        final double p2 = x * -0.3333333333333333f + y * -0.4714045207910317f + z * 0.816496580927726f;
        final double p3 = x * -0.3333333333333333f + y * -0.4714045207910317f + z * -0.816496580927726f;

        double xin = p3;
        double yin = p2;
        double zin = p0;
        double a = singleNoiseByType(_type,seed, xin, yin, zin);
        if(_normalizeNoise) a = (a * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p3;
        double b = singleNoiseByType(_type,seed, xin + a, yin, zin);
        if(_normalizeNoise) b = (b * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p1;
        yin = p2;
        zin = p3;
        double c = singleNoiseByType(_type,seed, xin + b, yin, zin);
        if(_normalizeNoise) c = (c * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p2;
        double d = singleNoiseByType(_type,seed, xin + c, yin, zin);
        if(_normalizeNoise) d = (d * .5f) + .5f;
        final double result = (a + b + c + d) * 0.25f;
        final double sharp = foamSharpness * 3.3f;
        final double diff = 0.5f - result;
        final int sign = Float.floatToRawIntBits((float) diff) >> 31, one = sign | 1;
        return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;

    }

    public static final double singleFoam(NoiseType _type, double foamSharpness, boolean _normalizeNoise, int seed, double x, double y, double z, double w)
    {
        final double p0 = x;
        final double p1 = x * -0.25f + y *  0.9682458365518543f;
        final double p2 = x * -0.25f + y * -0.3227486121839514f + z *  0.91287092917527690f;
        final double p3 = x * -0.25f + y * -0.3227486121839514f + z * -0.45643546458763834f + w *  0.7905694150420949f;
        final double p4 = x * -0.25f + y * -0.3227486121839514f + z * -0.45643546458763834f + w * -0.7905694150420947f;

        double xin = p1;
        double yin = p2;
        double zin = p3;
        double win = p4;
        double a = singleNoiseByType(_type,seed, xin, yin, zin, win);
        if(_normalizeNoise) a = (a * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p2;
        zin = p3;
        win = p4;
        double b = singleNoiseByType(_type,seed, xin + a, yin, zin, win);
        if(_normalizeNoise) b = (b * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p3;
        win = p4;
        double c = singleNoiseByType(_type,seed, xin + b, yin, zin, win);
        if(_normalizeNoise) c = (c * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p2;
        win = p4;
        double d = singleNoiseByType(_type,seed, xin + c, yin, zin, win);
        if(_normalizeNoise) d = (d * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p2;
        win = p3;
        double e = singleNoiseByType(_type,seed, xin + d, yin, zin, win);
        if(_normalizeNoise) e = (e * .5f) + .5f;
        final double result = (a + b + c + d + e) * 0.2f;
        final double sharp = foamSharpness * 4.4f;
        final double diff = 0.5f - result;
        final int sign = Float.floatToRawIntBits((float) diff) >> 31, one = sign | 1;
        return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;
    }

    public static final double singleFoam(NoiseType _type, double foamSharpness, boolean _normalizeNoise, int seed, double x, double y, double z, double w, double u) {
        final double p0 = x *  0.8157559148337911f + y *  0.5797766823136037f;
        final double p1 = x * -0.7314923478726791f + y *  0.6832997137249108f;
        final double p2 = x * -0.0208603044412437f + y * -0.3155296974329846f + z * 0.9486832980505138f;
        final double p3 = x * -0.0208603044412437f + y * -0.3155296974329846f + z * -0.316227766016838f + w *   0.8944271909999159f;
        final double p4 = x * -0.0208603044412437f + y * -0.3155296974329846f + z * -0.316227766016838f + w * -0.44721359549995804f + u *  0.7745966692414833f;
        final double p5 = x * -0.0208603044412437f + y * -0.3155296974329846f + z * -0.316227766016838f + w * -0.44721359549995804f + u * -0.7745966692414836f;

        double xin = p1;
        double yin = p2;
        double zin = p3;
        double win = p4;
        double uin = p5;
        double a = singleNoiseByType(_type,seed, xin, yin, zin, win, uin);
        if(_normalizeNoise) a = (a * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p2;
        zin = p3;
        win = p4;
        uin = p5;
        double b = singleNoiseByType(_type,seed, xin + a, yin, zin, win, uin);
        if(_normalizeNoise) b = (b * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p3;
        win = p4;
        uin = p5;
        double c = singleNoiseByType(_type,seed, xin + b, yin, zin, win, uin);
        if(_normalizeNoise) c = (c * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p2;
        win = p4;
        uin = p5;
        double d = singleNoiseByType(_type,seed, xin + c, yin, zin, win, uin);
        if(_normalizeNoise) d = (d * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p2;
        win = p3;
        uin = p5;
        double e = singleNoiseByType(_type,seed, xin + d, yin, zin, win, uin);
        if(_normalizeNoise) e = (e * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p2;
        win = p3;
        uin = p4;
        double f = singleNoiseByType(_type,seed, xin + e, yin, zin, win, uin);
        if(_normalizeNoise) f = (f * .5f) + .5f;
        final double result = (a + b + c + d + e + f) * 0.16666666666666666f;
        final double sharp = foamSharpness * 5.5f;
        final double diff = 0.5f - result;
        final int sign = Float.floatToRawIntBits((float) diff) >> 31, one = sign | 1;
        return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;
    }

    public static final double singleFoam(NoiseType _type, double foamSharpness, boolean _normalizeNoise, int seed, double x, double y, double z, double w, double u, double v) {
        final double p0 = x;
        final double p1 = x * -0.16666666666666666f + y *  0.98601329718326940f;
        final double p2 = x * -0.16666666666666666f + y * -0.19720265943665383f + z *  0.96609178307929590f;
        final double p3 = x * -0.16666666666666666f + y * -0.19720265943665383f + z * -0.24152294576982394f + w *  0.93541434669348530f;
        final double p4 = x * -0.16666666666666666f + y * -0.19720265943665383f + z * -0.24152294576982394f + w * -0.31180478223116176f + u *  0.8819171036881969f;
        final double p5 = x * -0.16666666666666666f + y * -0.19720265943665383f + z * -0.24152294576982394f + w * -0.31180478223116176f + u * -0.4409585518440984f + v *  0.7637626158259734f;
        final double p6 = x * -0.16666666666666666f + y * -0.19720265943665383f + z * -0.24152294576982394f + w * -0.31180478223116176f + u * -0.4409585518440984f + v * -0.7637626158259732f;
        double xin = p0;
        double yin = p5;
        double zin = p3;
        double win = p6;
        double uin = p1;
        double vin = p4;
        double a = singleNoiseByType(_type,seed, xin, yin, zin, win, uin, vin);
        if(_normalizeNoise) a = (a * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p2;
        yin = p6;
        zin = p0;
        win = p4;
        uin = p5;
        vin = p3;
        double b = singleNoiseByType(_type,seed, xin + a, yin, zin, win, uin, vin);
        if(_normalizeNoise) b = (b * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p1;
        yin = p2;
        zin = p3;
        win = p4;
        uin = p6;
        vin = p5;
        double c = singleNoiseByType(_type,seed, xin + b, yin, zin, win, uin, vin);
        if(_normalizeNoise) c = (c * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p6;
        yin = p0;
        zin = p2;
        win = p5;
        uin = p4;
        vin = p1;
        double d = singleNoiseByType(_type,seed, xin + c, yin, zin, win, uin, vin);
        if(_normalizeNoise) d = (d * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p2;
        yin = p1;
        zin = p5;
        win = p0;
        uin = p3;
        vin = p6;
        double e = singleNoiseByType(_type,seed, xin + d, yin, zin, win, uin, vin);
        if(_normalizeNoise) e = (e * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p4;
        zin = p6;
        win = p3;
        uin = p1;
        vin = p2;
        double f = singleNoiseByType(_type,seed, xin + e, yin, zin, win, uin, vin);
        if(_normalizeNoise) f = (f * .5f) + .5f;
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p5;
        yin = p1;
        zin = p2;
        win = p3;
        uin = p4;
        vin = p0;
        double g = singleNoiseByType(_type,seed, xin + f, yin, zin, win, uin, vin);
        if(_normalizeNoise) g = (g * .5f) + .5f;
        final double result = (a + b + c + d + e + f + g) * 0.14285714285714285f;
        final double sharp = foamSharpness * 6.6f;
        final double diff = 0.5f - result;
        final int sign = Float.floatToRawIntBits((float) diff) >> 31, one = sign | 1;
        return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;
    }

    public static final double singleFoam(NoiseType _type, double foamSharpness, boolean _normalizeNoise, int seed, double x, double y, double z, double w, double u, double v, double m) {
        final double p0 = x;
        final double p1 = x * -0.14285714285714285f + y * +0.9897433186107870f;
        final double p2 = x * -0.14285714285714285f + y * -0.1649572197684645f + z * +0.97590007294853320f;
        final double p3 = x * -0.14285714285714285f + y * -0.1649572197684645f + z * -0.19518001458970663f + w * +0.95618288746751490f;
        final double p4 = x * -0.14285714285714285f + y * -0.1649572197684645f + z * -0.19518001458970663f + w * -0.23904572186687872f + u * +0.92582009977255150f;
        final double p5 = x * -0.14285714285714285f + y * -0.1649572197684645f + z * -0.19518001458970663f + w * -0.23904572186687872f + u * -0.30860669992418377f + v * +0.8728715609439696f;
        final double p6 = x * -0.14285714285714285f + y * -0.1649572197684645f + z * -0.19518001458970663f + w * -0.23904572186687872f + u * -0.30860669992418377f + v * -0.4364357804719847f + m * +0.7559289460184545f;
        final double p7 = x * -0.14285714285714285f + y * -0.1649572197684645f + z * -0.19518001458970663f + w * -0.23904572186687872f + u * -0.30860669992418377f + v * -0.4364357804719847f + m * -0.7559289460184544f;
        double xin = p0;
        double yin = p6;
        double zin = p3;
        double win = p7;
        double uin = p1;
        double vin = p4;
        double min = p5;
        double a = singleNoiseByType(_type, BASE_HARSHNESS, min, foamSharpness, seed, xin, yin, zin, win, uin, vin);
        if(_normalizeNoise) a = (a * .5f) + .5f;
        seed += 0x9E377;
        xin = p2;
        yin = p3;
        zin = p0;
        win = p4;
        uin = p6;
        vin = p5;
        min = p7;
        double b = singleNoiseByType(_type, BASE_HARSHNESS, min, foamSharpness, seed, xin + a, yin, zin, win, uin, vin);
        if(_normalizeNoise) b = (b * .5f) + .5f;
        seed += 0x9E377;
        xin = p1;
        yin = p2;
        zin = p4;
        win = p3;
        uin = p5;
        vin = p7;
        min = p6;
        double c = singleNoiseByType(_type, BASE_HARSHNESS, min, foamSharpness, seed, xin + b, yin, zin, win, uin, vin);
        if(_normalizeNoise) c = (c * .5f) + .5f;
        seed += 0x9E377;
        xin = p7;
        yin = p0;
        zin = p2;
        win = p5;
        uin = p4;
        vin = p6;
        min = p1;
        double d = singleNoiseByType(_type, BASE_HARSHNESS, min, foamSharpness, seed, xin + c, yin, zin, win, uin, vin);
        if(_normalizeNoise) d = (d * .5f) + .5f;
        seed += 0x9E377;
        xin = p3;
        yin = p1;
        zin = p5;
        win = p6;
        uin = p7;
        vin = p0;
        min = p2;
        double e = singleNoiseByType(_type, BASE_HARSHNESS, min, foamSharpness, seed, xin + d, yin, zin, win, uin, vin);
        if(_normalizeNoise) e = (e * .5f) + .5f;
        seed += 0x9E377;
        xin = p4;
        yin = p7;
        zin = p6;
        win = p2;
        uin = p0;
        vin = p1;
        min = p3;
        double f = singleNoiseByType(_type, BASE_HARSHNESS, min, foamSharpness, seed, xin + e, yin, zin, win, uin, vin);
        if(_normalizeNoise) f = (f * .5f) + .5f;
        seed += 0x9E377;
        xin = p5;
        yin = p4;
        zin = p7;
        win = p1;
        uin = p2;
        vin = p3;
        min = p0;
        double g = singleNoiseByType(_type, BASE_HARSHNESS, min, foamSharpness, seed, xin + f, yin, zin, win, uin, vin);
        if(_normalizeNoise) g = (g * .5f) + .5f;
        seed += 0x9E377;
        xin = p6;
        yin = p5;
        zin = p1;
        win = p0;
        uin = p3;
        vin = p2;
        min = p4;
        double h = singleNoiseByType(_type, BASE_HARSHNESS, min, foamSharpness, seed, xin + g, yin, zin, win, uin, vin);
        if(_normalizeNoise) h = (h * .5f) + .5f;
        final double result = (a + b + c + d + e + f + g + h) * 0.125f;
        final double sharp = foamSharpness * 7.7f;
        final double diff = 0.5f - result;
        final int sign = Float.floatToRawIntBits((float) diff) >> 31, one = sign | 1;
        return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;
    }

}
