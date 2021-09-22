package com.github.terefang.jmelange.randfractal.lite;

import static com.github.terefang.jmelange.randfractal.lite.FnSimplex.singleSimplex;
import static com.github.terefang.jmelange.randfractal.lite.FnValue.singleValue;

public class FnHoney extends FastNoiseLite
{
    // ----------------------------------------------------------------------------

    public static final float singleHoney(int interpolation, int seed, float x, float y){
        final float result = (singleSimplex(seed, x, y) + singleValue(interpolation, seed ^ 0x9E3779B9, x, y)) * 0.5f + 1f;
        return (result <= 1f) ? result * result - 1f : (result - 2f) * -(result - 2f) + 1f;
    }

    public static final float singleHoney(int interpolation, int seed, float x, float y, float z){
        final float result = (singleSimplex(seed, x, y, z) + singleValue(interpolation, seed ^ 0x9E3779B9, x, y, z)) * 0.5f + 1f;
        return (result <= 1f) ? result * result - 1f : (result - 2f) * -(result - 2f) + 1f;
    }

    public static final float singleHoney(int interpolation, int seed, float x, float y, float z, float w) {
        final float result = (singleSimplex(seed, x, y, z, w) + singleValue(interpolation, seed ^ 0x9E3779B9, x, y, z, w)) * 0.5f + 1f;
        return (result <= 1f) ? result * result - 1f : (result - 2f) * -(result - 2f) + 1f;
    }

    public static final float singleHoney(int interpolation, int seed, float x, float y, float z, float w, float u) {
        final float result = (singleSimplex(seed, x, y, z, w, u) + singleValue(interpolation, seed ^ 0x9E3779B9, x, y, z, w, u)) * 0.5f + 1f;
        return (result <= 1f) ? result * result - 1f : (result - 2f) * -(result - 2f) + 1f;
    }

    public static final float singleHoney(int interpolation, int seed, float x, float y, float z, float w, float u, float v)
    {
        final float result = (singleSimplex(seed, x, y, z, w, u, v)
                + singleValue(interpolation, seed ^ 0x9E3779B9, x, y, z, w, u, v)) * 0.5f + 1f;
        return (result <= 1f) ? result * result - 1f : (result - 2f) * -(result - 2f) + 1f;
    }


}
