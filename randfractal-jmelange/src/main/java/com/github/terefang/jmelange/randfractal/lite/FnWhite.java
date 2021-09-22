package com.github.terefang.jmelange.randfractal.lite;

public class FnWhite extends FastNoiseLite
{
    // ----------------------------------------------------------------------------
    // White Noise

    public static final float singleWhiteNoise(int seed, float x, float y) {
        int xi = floatToIntMixed(x);
        int yi = floatToIntMixed(y);

        return valCoord2D(seed, xi, yi);
    }

    public static final float singleWhiteNoise(int seed, float x, float y, float z) {
        int xi = floatToIntMixed(x);
        int yi = floatToIntMixed(y);
        int zi = floatToIntMixed(z);

        return valCoord3D(seed, xi, yi, zi);
    }

    public static final float singleWhiteNoise(int seed, float x, float y, float z, float w) {
        int xi = floatToIntMixed(x);
        int yi = floatToIntMixed(y);
        int zi = floatToIntMixed(z);
        int wi = floatToIntMixed(w);

        return valCoord4D(seed, xi, yi, zi, wi);
    }

    public static final float singleWhiteNoise(int seed, float x, float y, float z, float w, float u) {
        int xi = floatToIntMixed(x);
        int yi = floatToIntMixed(y);
        int zi = floatToIntMixed(z);
        int wi = floatToIntMixed(w);
        int ui = floatToIntMixed(u);

        return valCoord5D(seed, xi, yi, zi, wi, ui);
    }

    public static final float singleWhiteNoise(int seed, float x, float y, float z, float w, float u, float v) {
        int xi = floatToIntMixed(x);
        int yi = floatToIntMixed(y);
        int zi = floatToIntMixed(z);
        int wi = floatToIntMixed(w);
        int ui = floatToIntMixed(u);
        int vi = floatToIntMixed(v);

        return valCoord6D(seed, xi, yi, zi, wi, ui, vi);
    }

}
