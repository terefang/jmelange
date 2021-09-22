package com.github.terefang.jmelange.randfractal.lite;

import static com.github.terefang.jmelange.randfractal.lite.FnCellular.singleCellular;
import static com.github.terefang.jmelange.randfractal.lite.FnCubic.singleCubic;
import static com.github.terefang.jmelange.randfractal.lite.FnFoam.singleFoam;
import static com.github.terefang.jmelange.randfractal.lite.FnHoney.singleHoney;
import static com.github.terefang.jmelange.randfractal.lite.FnPerlin.singlePerlin;
import static com.github.terefang.jmelange.randfractal.lite.FnValue.singleValue;
import static com.github.terefang.jmelange.randfractal.lite.FnWhite.singleWhiteNoise;

public class FastNoiseLite extends FastNoiseLiteBase
{

    public static final float singleNoiseByType(NoiseType type, int seed, float x, float y)
    {
        return singleNoiseByType(type, BASE_MUTATION, BASE_SHARPNESS, false, seed, x, y);
    }

    public static final float singleNoiseByType(NoiseType type, int seed, float x, float y, float z)
    {
        return singleNoiseByType(type, BASE_MUTATION, BASE_SHARPNESS, false, seed, x, y, z);
    }

    public static final float singleNoiseByType(NoiseType type, int seed, float x, float y, float z, float w)
    {
        return singleNoiseByType(type, BASE_MUTATION, BASE_SHARPNESS, false, seed, x, y, z, w);
    }

    public static final float singleNoiseByType(NoiseType type, int seed, float x, float y, float z, float w, float u)
    {
        return singleNoiseByType(type, BASE_MUTATION, BASE_SHARPNESS, false, seed, x, y, z, w, u);
    }

    public static final float singleNoiseByType(NoiseType type, int seed, float x, float y, float z, float w, float u, float v)
    {
        return singleNoiseByType(type, BASE_MUTATION, BASE_SHARPNESS, false, seed, x, y, z, w, u, v);
    }

    public static final float singleNoiseByType(NoiseType type, float mutation, float foamSharpness, int seed, float x, float y)
    {
        return singleNoiseByType(type, mutation, foamSharpness, false, seed, x, y);
    }

    public static final float singleNoiseByType(NoiseType type, float mutation, float foamSharpness, int seed, float x, float y, float z)
    {
        return singleNoiseByType(type, mutation, foamSharpness, false, seed, x, y, z);
    }

    public static final float singleNoiseByType(NoiseType type, float mutation, float foamSharpness, int seed, float x, float y, float z, float w)
    {
        return singleNoiseByType(type, mutation, foamSharpness, false, seed, x, y, z, w);
    }

    public static final float singleNoiseByType(NoiseType type, float mutation, float foamSharpness, int seed, float x, float y, float z, float w, float u)
    {
        return singleNoiseByType(type, mutation, foamSharpness, false, seed, x, y, z, w, u);
    }

    public static final float singleNoiseByType(NoiseType type, float mutation, float foamSharpness, int seed, float x, float y, float z, float w, float u, float v)
    {
        return singleNoiseByType(type, mutation, foamSharpness, false, seed, x, y, z, w, u, v);
    }

    public static final float singleNoiseByType(NoiseType type, float mutation, float foamSharpness, boolean _distort, int seed, float x, float y) {
        switch (type)
        {
            case BLUE:
            case WHITE:
                return singleWhiteNoise(seed, x, y);
            case MUTANT_QUINTIC:
                return singleFoam(QUINTIC, foamSharpness, seed, x, y, mutation);
            case MUTANT_HERMITE:
                return singleFoam(HERMITE, foamSharpness, seed, x, y, mutation);
            case MUTANT_LINEAR:
                return singleFoam(LINEAR, foamSharpness, seed, x, y, mutation);
            case HONEY_QUINTIC:
                return singleHoney(QUINTIC, seed, x, y);
            case HONEY_HERMITE:
                return singleHoney(HERMITE, seed, x, y);
            case HONEY_LINEAR:
                return singleHoney(LINEAR, seed, x, y);
            case FOAM_QUINTIC:
                return singleFoam(QUINTIC,foamSharpness,seed, x, y);
            case FOAM_HERMITE:
                return singleFoam(HERMITE,foamSharpness,seed, x, y);
            case FOAM_LINEAR:
                return singleFoam(LINEAR,foamSharpness,seed, x, y);
            case CUBIC:
                return singleCubic(seed, x, y);
            case CELLULAR_EUCLIDEAN_CELL_VALUE:
                return singleCellular(EUCLIDEAN,CELL_VALUE,seed, x, y);
            case CELLULAR_EUCLIDEAN_NOISE_LOOKUP:
                return singleCellular(EUCLIDEAN,NOISE_LOOKUP,seed, x, y);
            case CELLULAR_EUCLIDEAN_DISTANCE:
                return singleCellular(EUCLIDEAN,DISTANCE,seed, x, y);
            case CELLULAR_MANHATTAN_CELL_VALUE:
                return singleCellular(MANHATTAN,CELL_VALUE,seed, x, y);
            case CELLULAR_MANHATTAN_NOISE_LOOKUP:
                return singleCellular(MANHATTAN,NOISE_LOOKUP,seed, x, y);
            case CELLULAR_MANHATTAN_DISTANCE:
                return singleCellular(MANHATTAN,DISTANCE,seed, x, y);
            case CELLULAR_NATURAL_CELL_VALUE:
                return singleCellular(NATURAL,CELL_VALUE,seed, x, y);
            case CELLULAR_NATURAL_NOISE_LOOKUP:
                return singleCellular(NATURAL,NOISE_LOOKUP,seed, x, y);
            case CELLULAR_NATURAL_DISTANCE:
                return singleCellular(NATURAL,DISTANCE,seed, x, y);
            case SIMPLEX:
                return FnSimplex.singleSimplex(seed, x, y);
            case PERLIN_QUINTIC:
                return singlePerlin(QUINTIC, seed, x, y);
            case PERLIN_HERMITE:
                return singlePerlin(HERMITE, seed, x, y);
            case PERLIN_LINEAR:
                return singlePerlin(LINEAR, seed, x, y);
            case VALUE_QUINTIC:
                return singleValue(QUINTIC, seed, x, y);
            case VALUE_HERMITE:
                return singleValue(HERMITE, seed, x, y);
            case VALUE_LINEAR:
            default:
                return singleValue(LINEAR, seed, x, y);
        }
    }

    public static final float singleNoiseByType(NoiseType type, float mutation, float foamSharpness, boolean _distort, int seed, float x, float y, float z) {
        switch (type)
        {
            case BLUE:
            case WHITE:
                return singleWhiteNoise(seed, x, y, z);
            case MUTANT_QUINTIC:
                return singleFoam(QUINTIC, foamSharpness, seed, x, y, z, mutation);
            case MUTANT_HERMITE:
                return singleFoam(HERMITE, foamSharpness, seed, x, y, z, mutation);
            case MUTANT_LINEAR:
                return singleFoam(LINEAR, foamSharpness, seed, x, y, z, mutation);
            case HONEY_QUINTIC:
                return singleHoney(QUINTIC, seed, x, y,z);
            case HONEY_HERMITE:
                return singleHoney(HERMITE, seed, x, y,z);
            case HONEY_LINEAR:
                return singleHoney(LINEAR, seed, x, y,z);
            case FOAM_QUINTIC:
                return singleFoam(QUINTIC,foamSharpness,seed, x, y, z);
            case FOAM_HERMITE:
                return singleFoam(HERMITE,foamSharpness,seed, x, y, z);
            case FOAM_LINEAR:
                return singleFoam(LINEAR,foamSharpness,seed, x, y, z);
            case CUBIC:
                return singleCubic(seed, x, y, z);
            case CELLULAR_EUCLIDEAN_CELL_VALUE:
                return singleCellular(EUCLIDEAN,CELL_VALUE,seed, x, y, z);
            case CELLULAR_EUCLIDEAN_NOISE_LOOKUP:
                return singleCellular(EUCLIDEAN,NOISE_LOOKUP,seed, x, y, z);
            case CELLULAR_EUCLIDEAN_DISTANCE:
                return singleCellular(EUCLIDEAN,DISTANCE,seed, x, y, z);
            case CELLULAR_MANHATTAN_CELL_VALUE:
                return singleCellular(MANHATTAN,CELL_VALUE,seed, x, y, z);
            case CELLULAR_MANHATTAN_NOISE_LOOKUP:
                return singleCellular(MANHATTAN,NOISE_LOOKUP,seed, x, y, z);
            case CELLULAR_MANHATTAN_DISTANCE:
                return singleCellular(MANHATTAN,DISTANCE,seed, x, y, z);
            case CELLULAR_NATURAL_CELL_VALUE:
                return singleCellular(NATURAL,CELL_VALUE,seed, x, y, z);
            case CELLULAR_NATURAL_NOISE_LOOKUP:
                return singleCellular(NATURAL,NOISE_LOOKUP,seed, x, y, z);
            case CELLULAR_NATURAL_DISTANCE:
                return singleCellular(NATURAL,DISTANCE,seed, x, y, z);
            case SIMPLEX:
                return FnSimplex.singleSimplex(seed, x, y, z);
            case PERLIN_QUINTIC:
                return singlePerlin(QUINTIC, seed, x, y, z);
            case PERLIN_HERMITE:
                return singlePerlin(HERMITE, seed, x, y, z);
            case PERLIN_LINEAR:
                return singlePerlin(LINEAR, seed, x, y, z);
            case VALUE_QUINTIC:
                return singleValue(QUINTIC, seed, x, y, z);
            case VALUE_HERMITE:
                return singleValue(HERMITE, seed, x, y, z);
            case VALUE_LINEAR:
            default:
                return singleValue(LINEAR, seed, x, y, z);
        }
    }

    public static final float singleNoiseByType(NoiseType type, float mutation, float foamSharpness, boolean _distort, int seed, float x, float y, float z, float w) {
        switch (type)
        {
            case BLUE:
            case WHITE:
                return singleWhiteNoise(seed, x, y, z, w);
            case CELLULAR_EUCLIDEAN_CELL_VALUE:
            case CELLULAR_EUCLIDEAN_NOISE_LOOKUP:
            case CELLULAR_EUCLIDEAN_DISTANCE:
            case CELLULAR_MANHATTAN_CELL_VALUE:
            case CELLULAR_MANHATTAN_NOISE_LOOKUP:
            case CELLULAR_MANHATTAN_DISTANCE:
            case CELLULAR_NATURAL_CELL_VALUE:
            case CELLULAR_NATURAL_NOISE_LOOKUP:
            case CELLULAR_NATURAL_DISTANCE:
                return -1;
            case MUTANT_QUINTIC:
                return singleFoam(QUINTIC, foamSharpness, seed, x, y, z, w, mutation);
            case MUTANT_HERMITE:
                return singleFoam(HERMITE, foamSharpness, seed, x, y, z, w, mutation);
            case MUTANT_LINEAR:
                return singleFoam(LINEAR, foamSharpness, seed, x, y, z, w, mutation);
            case HONEY_QUINTIC:
                return singleHoney(QUINTIC, seed, x, y,z,w);
            case HONEY_HERMITE:
                return singleHoney(HERMITE, seed, x, y,z,w);
            case HONEY_LINEAR:
                return singleHoney(LINEAR, seed, x, y,z,w);
            case FOAM_QUINTIC:
                return singleFoam(QUINTIC,foamSharpness,seed, x, y, z, w);
            case FOAM_HERMITE:
                return singleFoam(HERMITE,foamSharpness,seed, x, y, z, w);
            case FOAM_LINEAR:
                return singleFoam(LINEAR,foamSharpness,seed, x, y, z, w);
            case CUBIC:
                return singleCubic(seed, x, y, z, w);
            case SIMPLEX:
                return FnSimplex.singleSimplex(seed, x, y, z, w);
            case PERLIN_QUINTIC:
                return singlePerlin(QUINTIC, seed, x, y, z, w);
            case PERLIN_HERMITE:
                return singlePerlin(HERMITE, seed, x, y, z, w);
            case PERLIN_LINEAR:
                return singlePerlin(LINEAR, seed, x, y, z, w);
            case VALUE_QUINTIC:
                return singleValue(QUINTIC, seed, x, y, z, w);
            case VALUE_HERMITE:
                return singleValue(HERMITE, seed, x, y, z, w);
            case VALUE_LINEAR:
            default:
                return singleValue(LINEAR, seed, x, y, z, w);
        }
    }

    public static final float singleNoiseByType(NoiseType type, float mutation, float foamSharpness, boolean _distort, int seed, float x, float y, float z, float w, float u) {
        switch (type)
        {
            case BLUE:
            case WHITE:
                return singleWhiteNoise(seed, x, y, z, w, u);
            case CELLULAR_EUCLIDEAN_CELL_VALUE:
            case CELLULAR_EUCLIDEAN_NOISE_LOOKUP:
            case CELLULAR_EUCLIDEAN_DISTANCE:
            case CELLULAR_MANHATTAN_CELL_VALUE:
            case CELLULAR_MANHATTAN_NOISE_LOOKUP:
            case CELLULAR_MANHATTAN_DISTANCE:
            case CELLULAR_NATURAL_CELL_VALUE:
            case CELLULAR_NATURAL_NOISE_LOOKUP:
            case CELLULAR_NATURAL_DISTANCE:
            case CUBIC:
                return -1;
            case MUTANT_QUINTIC:
                return singleFoam(QUINTIC, foamSharpness, seed, x, y, z, w, u, mutation);
            case MUTANT_HERMITE:
                return singleFoam(HERMITE, foamSharpness, seed, x, y, z, w, u, mutation);
            case MUTANT_LINEAR:
                return singleFoam(LINEAR, foamSharpness, seed, x, y, z, w, u, mutation);
            case HONEY_QUINTIC:
                return singleHoney(QUINTIC, seed, x, y,z,w,u);
            case HONEY_HERMITE:
                return singleHoney(HERMITE, seed, x, y,z,w,u);
            case HONEY_LINEAR:
                return singleHoney(LINEAR, seed, x, y,z,w,u);
            case FOAM_QUINTIC:
                return singleFoam(QUINTIC,foamSharpness,seed, x, y, z, w, u);
            case FOAM_HERMITE:
                return singleFoam(HERMITE,foamSharpness,seed, x, y, z, w, u);
            case FOAM_LINEAR:
                return singleFoam(LINEAR,foamSharpness,seed, x, y, z, w, u);
            case SIMPLEX:
                return FnSimplex.singleSimplex(seed, x, y, z, w, u);
            case PERLIN_QUINTIC:
                return singlePerlin(QUINTIC, seed, x, y, z, w, u);
            case PERLIN_HERMITE:
                return singlePerlin(HERMITE, seed, x, y, z, w, u);
            case PERLIN_LINEAR:
                return singlePerlin(LINEAR, seed, x, y, z, w, u);
            case VALUE_QUINTIC:
                return singleValue(QUINTIC, seed, x, y, z, w, u);
            case VALUE_HERMITE:
                return singleValue(HERMITE, seed, x, y, z, w, u);
            case VALUE_LINEAR:
            default:
                return singleValue(LINEAR, seed, x, y, z, w, u);
        }
    }

    public static final float singleNoiseByType(NoiseType type, float mutation, float foamSharpness, boolean _distort, int seed, float x, float y, float z, float w, float u, float v) {
        switch (type)
        {
            case BLUE:
            case WHITE:
                return singleWhiteNoise(seed, x, y, z, w, u, v);
            case CELLULAR_EUCLIDEAN_CELL_VALUE:
            case CELLULAR_EUCLIDEAN_NOISE_LOOKUP:
            case CELLULAR_EUCLIDEAN_DISTANCE:
            case CELLULAR_MANHATTAN_CELL_VALUE:
            case CELLULAR_MANHATTAN_NOISE_LOOKUP:
            case CELLULAR_MANHATTAN_DISTANCE:
            case CELLULAR_NATURAL_CELL_VALUE:
            case CELLULAR_NATURAL_NOISE_LOOKUP:
            case CELLULAR_NATURAL_DISTANCE:
            case CUBIC:
                return -1;
            case MUTANT_QUINTIC:
                return singleFoam(QUINTIC, foamSharpness, seed, x, y, z, w, u, v+mutation);
            case MUTANT_HERMITE:
                return singleFoam(HERMITE, foamSharpness, seed, x, y, z, w, u, v+mutation);
            case MUTANT_LINEAR:
                return singleFoam(LINEAR, foamSharpness, seed, x, y, z, w, u, v+mutation);
            case HONEY_QUINTIC:
                return singleHoney(QUINTIC, seed, x, y,z,w,u,v);
            case HONEY_HERMITE:
                return singleHoney(HERMITE, seed, x, y,z,w,u,v);
            case HONEY_LINEAR:
                return singleHoney(LINEAR, seed, x, y,z,w,u,v);
            case FOAM_QUINTIC:
                return singleFoam(QUINTIC,foamSharpness,seed, x, y, z, w, u, v);
            case FOAM_HERMITE:
                return singleFoam(HERMITE,foamSharpness,seed, x, y, z, w, u, v);
            case FOAM_LINEAR:
                return singleFoam(LINEAR,foamSharpness,seed, x, y, z, w, u, v);
            case SIMPLEX:
                return FnSimplex.singleSimplex(seed, x, y, z, w, u, v);
            case PERLIN_QUINTIC:
                return singlePerlin(QUINTIC, seed, x, y, z, w, u, v);
            case PERLIN_HERMITE:
                return singlePerlin(HERMITE, seed, x, y, z, w, u, v);
            case PERLIN_LINEAR:
                return singlePerlin(LINEAR, seed, x, y, z, w, u, v);
            case VALUE_QUINTIC:
                return singleValue(QUINTIC, seed, x, y, z, w, u, v);
            case VALUE_HERMITE:
                return singleValue(HERMITE, seed, x, y, z, w, u, v);
            case VALUE_LINEAR:
            default:
                return singleValue(LINEAR, seed, x, y, z, w, u, v);
        }
    }


    // ----------------------------------------------------------------------------


    // ----------------------------------------------------------------------------

    public static final void singleGradientPerturb2(int interpolation, int seed, float perturbAmp, float frequency, float[] v2) {
        float xf = v2[0] * frequency;
        float yf = v2[1] * frequency;

        int x0 = fastFloor(xf);
        int y0 = fastFloor(yf);
        int x1 = x0 + 1;
        int y1 = y0 + 1;

        float xs, ys;
        switch (interpolation) {
            default:
            case LINEAR:
                xs = xf - x0;
                ys = yf - y0;
                break;
            case HERMITE:
                xs = hermiteInterpolator(xf - x0);
                ys = hermiteInterpolator(yf - y0);
                break;
            case QUINTIC:
                xs = quinticInterpolator(xf - x0);
                ys = quinticInterpolator(yf - y0);
                break;
        }

        Float2 vec0 = CELL_2D[hash256(x0, y0, seed)];
        Float2 vec1 = CELL_2D[hash256(x1, y0, seed)];

        float lx0x = lerp(vec0.x, vec1.x, xs);
        float ly0x = lerp(vec0.y, vec1.y, xs);

        vec0 = CELL_2D[hash256(x0, y1, seed)];
        vec1 = CELL_2D[hash256(x1, y1, seed)];

        float lx1x = lerp(vec0.x, vec1.x, xs);
        float ly1x = lerp(vec0.y, vec1.y, xs);

        v2[0] += lerp(lx0x, lx1x, ys) * perturbAmp;
        v2[1] += lerp(ly0x, ly1x, ys) * perturbAmp;
    }

    public static final void singleGradientPerturb3(int interpolation, int seed, float perturbAmp, float frequency, float[] v3) {
        float xf = v3[0] * frequency;
        float yf = v3[1] * frequency;
        float zf = v3[2] * frequency;

        int x0 = fastFloor(xf);
        int y0 = fastFloor(yf);
        int z0 = fastFloor(zf);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;

        float xs, ys, zs;
        switch (interpolation) {
            default:
            case LINEAR:
                xs = xf - x0;
                ys = yf - y0;
                zs = zf - z0;
                break;
            case HERMITE:
                xs = hermiteInterpolator(xf - x0);
                ys = hermiteInterpolator(yf - y0);
                zs = hermiteInterpolator(zf - z0);
                break;
            case QUINTIC:
                xs = quinticInterpolator(xf - x0);
                ys = quinticInterpolator(yf - y0);
                zs = quinticInterpolator(zf - z0);
                break;
        }

        Float3 vec0 = CELL_3D[hash256(x0, y0, z0, seed)];
        Float3 vec1 = CELL_3D[hash256(x1, y0, z0, seed)];

        float lx0x = lerp(vec0.x, vec1.x, xs);
        float ly0x = lerp(vec0.y, vec1.y, xs);
        float lz0x = lerp(vec0.z, vec1.z, xs);

        vec0 = CELL_3D[hash256(x0, y1, z0, seed)];
        vec1 = CELL_3D[hash256(x1, y1, z0, seed)];

        float lx1x = lerp(vec0.x, vec1.x, xs);
        float ly1x = lerp(vec0.y, vec1.y, xs);
        float lz1x = lerp(vec0.z, vec1.z, xs);

        float lx0y = lerp(lx0x, lx1x, ys);
        float ly0y = lerp(ly0x, ly1x, ys);
        float lz0y = lerp(lz0x, lz1x, ys);

        vec0 = CELL_3D[hash256(x0, y0, z1, seed)];
        vec1 = CELL_3D[hash256(x1, y0, z1, seed)];

        lx0x = lerp(vec0.x, vec1.x, xs);
        ly0x = lerp(vec0.y, vec1.y, xs);
        lz0x = lerp(vec0.z, vec1.z, xs);

        vec0 = CELL_3D[hash256(x0, y1, z1, seed)];
        vec1 = CELL_3D[hash256(x1, y1, z1, seed)];

        lx1x = lerp(vec0.x, vec1.x, xs);
        ly1x = lerp(vec0.y, vec1.y, xs);
        lz1x = lerp(vec0.z, vec1.z, xs);

        v3[0] += lerp(lx0y, lerp(lx0x, lx1x, ys), zs) * perturbAmp;
        v3[1] += lerp(ly0y, lerp(ly0x, ly1x, ys), zs) * perturbAmp;
        v3[2] += lerp(lz0y, lerp(lz0x, lz1x, ys), zs) * perturbAmp;
    }

    // ----------------------------------------------------------------------------
    public static final float fRM(NoiseType type, float x, float y, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;

        float sum = 0f, exp = 2f, correction = 0f, spike;
        for (int i = 0; i < octaves; i++) {
            spike = 1f - Math.abs(singleNoiseByType(type, mutation, foamSharpness, seed, x, y));
            correction += (exp *= 0.5);
            sum += spike * exp;
            x *= lacunarity;
            y *= lacunarity;
        }
        return sum * 2f / correction - 1f;
    }

    public static final float fRM(NoiseType type, float x, float y, float z, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;

        float sum = 0f, exp = 2f, correction = 0f, spike;
        for (int i = 0; i < octaves; i++) {
            spike = 1f - Math.abs(singleNoiseByType(type, mutation, foamSharpness, seed, x, y, z));
            correction += (exp *= 0.5);
            sum += spike * exp;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
        }
        return sum * 2f / correction - 1f;
    }

    public static final float fRM(NoiseType type, float x, float y, float z, float w, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;

        float sum = 0f, exp = 2f, correction = 0f, spike;
        for (int i = 0; i < octaves; i++) {
            spike = 1f - Math.abs(singleNoiseByType(type, mutation, foamSharpness, seed, x, y, z, w));
            correction += (exp *= 0.5);
            sum += spike * exp;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
        }
        return sum * 2f / correction - 1f;
    }

    public static final float fRM(NoiseType type, float x, float y, float z, float w, float u, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;

        float sum = 0f, exp = 2f, correction = 0f, spike;
        for (int i = 0; i < octaves; i++) {
            spike = 1f - Math.abs(singleNoiseByType(type, mutation, foamSharpness, seed, x, y, z, w, u));
            correction += (exp *= 0.5);
            sum += spike * exp;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
        }
        return sum * 2f / correction - 1f;
    }

    public static final float fRM(NoiseType type, float x, float y, float z, float w, float u, float v, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        float sum = 0f, exp = 2f, correction = 0f, spike;
        for (int i = 0; i < octaves; i++) {
            spike = 1f - Math.abs(singleNoiseByType(type, mutation, foamSharpness, seed, x, y, z, w, u, v));
            correction += (exp *= 0.5);
            sum += spike * exp;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            v *= lacunarity;
        }
        return sum * 2f / correction - 1f;
    }

    // ----------------------------------------------------------------------------

    public static final float fMF(NoiseType type, float x, float y, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;

        float sum = 1f, correction = 0f;
        for (int i = 0; i < octaves; i++) {
            correction += gain;
            sum += singleNoiseByType(type, mutation, foamSharpness, seed, x, y) * gain;
            x *= lacunarity;
            y *= lacunarity;
        }
        return sum/correction;
    }

    public static final float fMF(NoiseType type, float x, float y, float z, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;

        float sum = 1f, correction = 0f;
        for (int i = 0; i < octaves; i++) {
            correction += gain;
            sum += singleNoiseByType(type, mutation, foamSharpness, seed, x, y, z) * gain;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
        }
        return sum/correction;
    }

    public static final float fMF(NoiseType type, float x, float y, float z, float w, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;

        float sum = 1f, correction = 0f;
        for (int i = 0; i < octaves; i++) {
            correction += gain;
            sum += singleNoiseByType(type, mutation, foamSharpness, seed, x, y, z, w) * gain;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
        }
        return sum/correction;
    }

    public static final float fMF(NoiseType type, float x, float y, float z, float w, float u, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;

        float sum = 1f, correction = 0f;
        for (int i = 0; i < octaves; i++) {
            correction += gain;
            sum += singleNoiseByType(type, mutation, foamSharpness, seed, x, y, z, w, u) * gain;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
        }
        return sum/correction;
    }

    public static final float fMF(NoiseType type, float x, float y, float z, float w, float u, float v, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        float sum = 1f, correction = 0f;
        for (int i = 0; i < octaves; i++) {
            correction += gain;
            sum += singleNoiseByType(type, mutation, foamSharpness, seed, x, y, z, w, u, v) * gain;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            v *= lacunarity;
        }
        return sum/correction;
    }

    // ----------------------------------------------------------------------------
    /**
     * Generates layered noise with the given amount of octaves and specified lacunarity (the amount of
     * frequency change between octaves) and gain (loosely, how much to emphasize lower-frequency octaves) in 2D.
     * @param x
     * @param y
     * @param seed
     * @param octaves
     * @return noise as a float between -1f and 1f
     */
    public static final float fBM(NoiseType type, float x, float y, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;

        float sum = singleNoiseByType(type, mutation, foamSharpness, seed, x, y);
        float amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;

            amp *= gain;
            sum += singleNoiseByType(type, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y) * amp;
        }
        amp = gain;
        float ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }
        return sum / ampFractal;
    }

    /**
     * Generates layered simplex noise with the given amount of octaves and specified lacunarity (the amount of
     * frequency change between octaves) and gain (loosely, how much to emphasize lower-frequency octaves) in 3D.
     * @param x
     * @param y
     * @param z
     * @param seed
     * @param octaves
     * @param frequency
     * @param lacunarity
     * @param gain
     * @return noise as a float between -1f and 1f
     */
    public static final float fBM(NoiseType type, float x, float y, float z, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;

        float sum = singleNoiseByType(type, mutation, foamSharpness, seed, x, y, z);
        float amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;

            amp *= gain;
            sum += singleNoiseByType(type, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z) * amp;
        }
        amp = gain;
        float ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }
        return sum / ampFractal;
    }

    public static final float fBM3D(NoiseType type, float x, float y, float z, int seed, boolean _vseed)
    {
        return fBM(type, x, y, z, seed, BASE_OCTAVES, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, BASE_MUTATION, BASE_SHARPNESS, _vseed);
    }

    public static final float fBM3D(NoiseType type, float x, float y, float z, int seed, int octaves, boolean _vseed)
    {
        return fBM(type, x, y, z, seed, octaves, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, BASE_MUTATION, BASE_SHARPNESS, _vseed);
    }
    // ----------------------------------------------------------------------------

    public static final float fBM(NoiseType type, float x, float y, float z, float w, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;

        float sum = singleNoiseByType(type, mutation, foamSharpness, seed, x, y, z, w);
        float amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;

            amp *= gain;
            sum += singleNoiseByType(type, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w) * amp;
        }
        amp = gain;
        float ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }
        return sum / ampFractal;
    }

    // ----------------------------------------------------------------------------

    public static final float fBM(NoiseType type, float x, float y, float z, float w, float u, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;

        float sum = singleNoiseByType(type, mutation, foamSharpness, seed, x, y, z, w, u);
        float amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;

            amp *= gain;
            sum += singleNoiseByType(type, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u) * amp;
        }
        amp = gain;
        float ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }
        return sum / ampFractal;
    }

    // ----------------------------------------------------------------------------

    public static final float fBM(NoiseType type, float x, float y, float z, float w, float u, float v, int seed, int octaves, float frequency, float lacunarity, float gain, float mutation, float foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        float sum = singleNoiseByType(type, mutation, foamSharpness, seed, x, y, z, w, u, v);
        float amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            v *= lacunarity;

            amp *= gain;
            sum += singleNoiseByType(type, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u, v) * amp;
        }
        amp = gain;
        float ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }
        return sum / ampFractal;
    }


}
