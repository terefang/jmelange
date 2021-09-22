package com.github.terefang.jmelange.randfractal.lite;

import static com.github.terefang.jmelange.randfractal.lite.FnValue.singleValue;

public class FnFoam extends FastNoiseLite
{
    // ----------------------------------------------------------------------------

    public static final float singleFoam(int interpolation, float foamSharpness, int seed, float x, float y) {
        final float p0 = x;
        final float p1 = x * -0.5f + y * 0.8660254037844386f;
        final float p2 = x * -0.5f + y * -0.8660254037844387f;

        float xin = p2;
        float yin = p0;
        final float a = singleValue(interpolation, seed, xin, yin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p1;
        yin = p2;
        final float b = singleValue(interpolation, seed, xin + a, yin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        final float c = singleValue(interpolation, seed, xin + b, yin);
        final float result = (a + b + c) * F3f;
        final float sharp = foamSharpness * 2.2f;
        final float diff = 0.5f - result;
        final int sign = Float.floatToRawIntBits(diff) >> 31, one = sign | 1;
        return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;
    }

    public static final float singleFoam(int interpolation, float foamSharpness, int seed, float x, float y, float z){
        final float p0 = x;
        final float p1 = x * -0.3333333333333333f + y * 0.9428090415820634f;
        final float p2 = x * -0.3333333333333333f + y * -0.4714045207910317f + z * 0.816496580927726f;
        final float p3 = x * -0.3333333333333333f + y * -0.4714045207910317f + z * -0.816496580927726f;

        float xin = p3;
        float yin = p2;
        float zin = p0;
        final float a = singleValue(interpolation, seed, xin, yin, zin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p3;
        final float b = singleValue(interpolation, seed, xin + a, yin, zin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p1;
        yin = p2;
        zin = p3;
        final float c = singleValue(interpolation, seed, xin + b, yin, zin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p2;
        final float d = singleValue(interpolation, seed, xin + c, yin, zin);

        final float result = (a + b + c + d) * 0.25f;
        final float sharp = foamSharpness * 3.3f;
        final float diff = 0.5f - result;
        final int sign = Float.floatToRawIntBits(diff) >> 31, one = sign | 1;
        return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;

    }

    public static final float singleFoam(int interpolation, float foamSharpness, int seed, float x, float y, float z, float w)
    {
        final float p0 = x;
        final float p1 = x * -0.25f + y *  0.9682458365518543f;
        final float p2 = x * -0.25f + y * -0.3227486121839514f + z *  0.91287092917527690f;
        final float p3 = x * -0.25f + y * -0.3227486121839514f + z * -0.45643546458763834f + w *  0.7905694150420949f;
        final float p4 = x * -0.25f + y * -0.3227486121839514f + z * -0.45643546458763834f + w * -0.7905694150420947f;

        float xin = p1;
        float yin = p2;
        float zin = p3;
        float win = p4;
        final float a = singleValue(interpolation, seed, xin, yin, zin, win);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p2;
        zin = p3;
        win = p4;
        final float b = singleValue(interpolation, seed, xin + a, yin, zin, win);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p3;
        win = p4;
        final float c = singleValue(interpolation, seed, xin + b, yin, zin, win);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p2;
        win = p4;
        final float d = singleValue(interpolation, seed, xin + c, yin, zin, win);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p2;
        win = p3;
        final float e = singleValue(interpolation, seed, xin + d, yin, zin, win);

        final float result = (a + b + c + d + e) * 0.2f;
        final float sharp = foamSharpness * 4.4f;
        final float diff = 0.5f - result;
        final int sign = Float.floatToRawIntBits(diff) >> 31, one = sign | 1;
        return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;
    }

    public static final float singleFoam(int interpolation, float foamSharpness, int seed, float x, float y, float z, float w, float u) {
        final float p0 = x *  0.8157559148337911f + y *  0.5797766823136037f;
        final float p1 = x * -0.7314923478726791f + y *  0.6832997137249108f;
        final float p2 = x * -0.0208603044412437f + y * -0.3155296974329846f + z * 0.9486832980505138f;
        final float p3 = x * -0.0208603044412437f + y * -0.3155296974329846f + z * -0.316227766016838f + w *   0.8944271909999159f;
        final float p4 = x * -0.0208603044412437f + y * -0.3155296974329846f + z * -0.316227766016838f + w * -0.44721359549995804f + u *  0.7745966692414833f;
        final float p5 = x * -0.0208603044412437f + y * -0.3155296974329846f + z * -0.316227766016838f + w * -0.44721359549995804f + u * -0.7745966692414836f;

        float xin = p1;
        float yin = p2;
        float zin = p3;
        float win = p4;
        float uin = p5;
        final float a = singleValue(interpolation, seed, xin, yin, zin, win, uin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p2;
        zin = p3;
        win = p4;
        uin = p5;
        final float b = singleValue(interpolation, seed, xin + a, yin, zin, win, uin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p3;
        win = p4;
        uin = p5;
        final float c = singleValue(interpolation, seed, xin + b, yin, zin, win, uin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p2;
        win = p4;
        uin = p5;
        final float d = singleValue(interpolation, seed, xin + c, yin, zin, win, uin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p2;
        win = p3;
        uin = p5;
        final float e = singleValue(interpolation, seed, xin + d, yin, zin, win, uin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p1;
        zin = p2;
        win = p3;
        uin = p4;
        final float f = singleValue(interpolation, seed, xin + e, yin, zin, win, uin);

        final float result = (a + b + c + d + e + f) * 0.16666666666666666f;
        final float sharp = foamSharpness * 5.5f;
        final float diff = 0.5f - result;
        final int sign = Float.floatToRawIntBits(diff) >> 31, one = sign | 1;
        return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;
    }

    public static final float singleFoam(int interpolation, float foamSharpness, int seed, float x, float y, float z, float w, float u, float v) {
        final float p0 = x;
        final float p1 = x * -0.16666666666666666f + y *  0.98601329718326940f;
        final float p2 = x * -0.16666666666666666f + y * -0.19720265943665383f + z *  0.96609178307929590f;
        final float p3 = x * -0.16666666666666666f + y * -0.19720265943665383f + z * -0.24152294576982394f + w *  0.93541434669348530f;
        final float p4 = x * -0.16666666666666666f + y * -0.19720265943665383f + z * -0.24152294576982394f + w * -0.31180478223116176f + u *  0.8819171036881969f;
        final float p5 = x * -0.16666666666666666f + y * -0.19720265943665383f + z * -0.24152294576982394f + w * -0.31180478223116176f + u * -0.4409585518440984f + v *  0.7637626158259734f;
        final float p6 = x * -0.16666666666666666f + y * -0.19720265943665383f + z * -0.24152294576982394f + w * -0.31180478223116176f + u * -0.4409585518440984f + v * -0.7637626158259732f;
        float xin = p0;
        float yin = p5;
        float zin = p3;
        float win = p6;
        float uin = p1;
        float vin = p4;
        final float a = singleValue(interpolation, seed, xin, yin, zin, win, uin, vin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p2;
        yin = p6;
        zin = p0;
        win = p4;
        uin = p5;
        vin = p3;
        final float b = singleValue(interpolation, seed, xin + a, yin, zin, win, uin, vin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p1;
        yin = p2;
        zin = p3;
        win = p4;
        uin = p6;
        vin = p5;
        final float c = singleValue(interpolation, seed, xin + b, yin, zin, win, uin, vin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p6;
        yin = p0;
        zin = p2;
        win = p5;
        uin = p4;
        vin = p1;
        final float d = singleValue(interpolation, seed, xin + c, yin, zin, win, uin, vin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p2;
        yin = p1;
        zin = p5;
        win = p0;
        uin = p3;
        vin = p6;
        final float e = singleValue(interpolation, seed, xin + d, yin, zin, win, uin, vin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p0;
        yin = p4;
        zin = p6;
        win = p3;
        uin = p1;
        vin = p2;
        final float f = singleValue(interpolation, seed, xin + e, yin, zin, win, uin, vin);
        seed += 0x9E3779BD;
        seed ^= seed >>> 14;
        xin = p5;
        yin = p1;
        zin = p2;
        win = p3;
        uin = p4;
        vin = p0;
        final float g = singleValue(interpolation, seed, xin + f, yin, zin, win, uin, vin);
        final float result = (a + b + c + d + e + f + g) * 0.14285714285714285f;
        final float sharp = foamSharpness * 6.6f;
        final float diff = 0.5f - result;
        final int sign = Float.floatToRawIntBits(diff) >> 31, one = sign | 1;
        return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;
    }

    public static final float singleFoam(int interpolation, float foamSharpness, int seed, float x, float y, float z, float w, float u, float v, float m) {
        final float p0 = x;
        final float p1 = x * -0.14285714285714285f + y * +0.9897433186107870f;
        final float p2 = x * -0.14285714285714285f + y * -0.1649572197684645f + z * +0.97590007294853320f;
        final float p3 = x * -0.14285714285714285f + y * -0.1649572197684645f + z * -0.19518001458970663f + w * +0.95618288746751490f;
        final float p4 = x * -0.14285714285714285f + y * -0.1649572197684645f + z * -0.19518001458970663f + w * -0.23904572186687872f + u * +0.92582009977255150f;
        final float p5 = x * -0.14285714285714285f + y * -0.1649572197684645f + z * -0.19518001458970663f + w * -0.23904572186687872f + u * -0.30860669992418377f + v * +0.8728715609439696f;
        final float p6 = x * -0.14285714285714285f + y * -0.1649572197684645f + z * -0.19518001458970663f + w * -0.23904572186687872f + u * -0.30860669992418377f + v * -0.4364357804719847f + m * +0.7559289460184545f;
        final float p7 = x * -0.14285714285714285f + y * -0.1649572197684645f + z * -0.19518001458970663f + w * -0.23904572186687872f + u * -0.30860669992418377f + v * -0.4364357804719847f + m * -0.7559289460184544f;
        float xin = p0;
        float yin = p6;
        float zin = p3;
        float win = p7;
        float uin = p1;
        float vin = p4;
        float min = p5;
        final float a = singleValue(interpolation, seed, xin, yin, zin, win, uin, vin, min);
        seed += 0x9E377;
        xin = p2;
        yin = p3;
        zin = p0;
        win = p4;
        uin = p6;
        vin = p5;
        min = p7;
        final float b = singleValue(interpolation, seed, xin + a, yin, zin, win, uin, vin, min);
        seed += 0x9E377;
        xin = p1;
        yin = p2;
        zin = p4;
        win = p3;
        uin = p5;
        vin = p7;
        min = p6;
        final float c = singleValue(interpolation, seed, xin + b, yin, zin, win, uin, vin, min);
        seed += 0x9E377;
        xin = p7;
        yin = p0;
        zin = p2;
        win = p5;
        uin = p4;
        vin = p6;
        min = p1;
        final float d = singleValue(interpolation, seed, xin + c, yin, zin, win, uin, vin, min);
        seed += 0x9E377;
        xin = p3;
        yin = p1;
        zin = p5;
        win = p6;
        uin = p7;
        vin = p0;
        min = p2;
        final float e = singleValue(interpolation, seed, xin + d, yin, zin, win, uin, vin, min);
        seed += 0x9E377;
        xin = p4;
        yin = p7;
        zin = p6;
        win = p2;
        uin = p0;
        vin = p1;
        min = p3;
        final float f = singleValue(interpolation, seed, xin + e, yin, zin, win, uin, vin, min);
        seed += 0x9E377;
        xin = p5;
        yin = p4;
        zin = p7;
        win = p1;
        uin = p2;
        vin = p3;
        min = p0;
        final float g = singleValue(interpolation, seed, xin + f, yin, zin, win, uin, vin, min);
        seed += 0x9E377;
        xin = p6;
        yin = p5;
        zin = p1;
        win = p0;
        uin = p3;
        vin = p2;
        min = p4;
        final float h = singleValue(interpolation, seed, xin + g, yin, zin, win, uin, vin, min);
        final float result = (a + b + c + d + e + f + g + h) * 0.125f;
        final float sharp = foamSharpness * 7.7f;
        final float diff = 0.5f - result;
        final int sign = Float.floatToRawIntBits(diff) >> 31, one = sign | 1;
        return (((result + sign)) / (Float.MIN_VALUE - sign + (result + sharp * diff) * one) - sign - sign) - 1f;
    }

}
