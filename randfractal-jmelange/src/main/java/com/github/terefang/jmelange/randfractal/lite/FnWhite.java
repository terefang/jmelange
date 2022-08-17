package com.github.terefang.jmelange.randfractal.lite;

public class FnWhite extends FastNoiseLite
{
    // ----------------------------------------------------------------------------
    // White Noise

    public static final double singleWhiteNoise(int seed, double x, double y, boolean _normalized) {
        int xi = floatToIntMixed((float)x);
        int yi = floatToIntMixed((float)y);

        return valCoord2D(seed, xi, yi);
    }

    public static final double singleWhiteNoise(int seed, double x, double y, double z, boolean _normalized) {
        int xi = floatToIntMixed((float)x);
        int yi = floatToIntMixed((float)y);
        int zi = floatToIntMixed((float)z);

        return valCoord3D(seed, xi, yi, zi);
    }

    public static final double singleWhiteNoise(int seed, double x, double y, double z, double w, boolean _normalized) {
        int xi = floatToIntMixed((float)x);
        int yi = floatToIntMixed((float)y);
        int zi = floatToIntMixed((float)z);
        int wi = floatToIntMixed((float)w);

        return valCoord4D(seed, xi, yi, zi, wi);
    }

    public static final double singleWhiteNoise(int seed, double x, double y, double z, double w, double u, boolean _normalized) {
        int xi = floatToIntMixed((float)x);
        int yi = floatToIntMixed((float)y);
        int zi = floatToIntMixed((float)z);
        int wi = floatToIntMixed((float)w);
        int ui = floatToIntMixed((float)u);

        return valCoord5D(seed, xi, yi, zi, wi, ui);
    }

    public static final double singleWhiteNoise(int seed, double x, double y, double z, double w, double u, double v, boolean _normalized) {
        int xi = floatToIntMixed((float)x);
        int yi = floatToIntMixed((float)y);
        int zi = floatToIntMixed((float)z);
        int wi = floatToIntMixed((float)w);
        int ui = floatToIntMixed((float)u);
        int vi = floatToIntMixed((float)v);

        return valCoord6D(seed, xi, yi, zi, wi, ui, vi);
    }

}
