package com.github.terefang.jmelange.randfractal.lite;

import static com.github.terefang.jmelange.randfractal.lite.FnCellular.singleCellular;
import static com.github.terefang.jmelange.randfractal.lite.FnCellular2Edge.singleCellular2Edge;
import static com.github.terefang.jmelange.randfractal.lite.FnCellularMerge.singleCellularMerge;
import static com.github.terefang.jmelange.randfractal.lite.FnCubic.singleCubic;
import static com.github.terefang.jmelange.randfractal.lite.FnFoam.singleFoam;
import static com.github.terefang.jmelange.randfractal.lite.FnHoney.singleHoney;
import static com.github.terefang.jmelange.randfractal.lite.FnPerlin.singlePerlin;
import static com.github.terefang.jmelange.randfractal.lite.FnPyramid.singlePyramid;
import static com.github.terefang.jmelange.randfractal.lite.FnSolid.singleSolid;
import static com.github.terefang.jmelange.randfractal.lite.FnValue.singleValue;

public class FastNoiseLite extends FastNoiseLiteBase
{

    public static double singleNoiseByType(NoiseType type, int seed, double x, double y)
    {
        return singleNoiseByType(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y);
    }

    public static double singleNoiseByType(NoiseType type, int seed, double x, double y, double z)
    {
        return singleNoiseByType(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z);
    }

    public static double singleNoiseByType(NoiseType type, int seed, double x, double y, double z, double w)
    {
        return singleNoiseByType(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w);
    }

    public static double singleNoiseByType(NoiseType type, int seed, double x, double y, double z, double w, double u)
    {
        return singleNoiseByType(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, u);
    }

    public static double singleNoiseByType(NoiseType type, int seed, double x, double y, double z, double w, double u, double v)
    {
        return singleNoiseByType(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, u, v);
    }

    public static double singleNoiseByType(NoiseType type, double _harshness, double mutation, double foamSharpness, int seed, double x, double y) {
        switch (type)
        {
            case CELLULAR_MERGE_EUCLIDEAN:
                return singleCellularMerge(EUCLIDEAN,foamSharpness, seed, x, y);
            case CELLULAR_MERGE_MANHATTAN:
                return singleCellularMerge(MANHATTAN,foamSharpness, seed, x, y);
            case CELLULAR_MERGE_NATURAL:
                return singleCellularMerge(NATURAL,foamSharpness, seed, x, y);
            case CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2:
                return singleCellular2Edge(EUCLIDEAN,DISTANCE_2,seed, x, y);
            case CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2_ADD:
                return singleCellular2Edge(EUCLIDEAN,DISTANCE_2_ADD,seed, x, y);
            case CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2_SUB:
                return singleCellular2Edge(EUCLIDEAN,DISTANCE_2_SUB,seed, x, y);
            case CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2_MUL:
                return singleCellular2Edge(EUCLIDEAN,DISTANCE_2_MUL,seed, x, y);
            case CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2_DIV:
                return singleCellular2Edge(EUCLIDEAN,DISTANCE_2_DIV,seed, x, y);
            case CELLULAR2EDGE_MANHATTAN_DISTANCE_2:
                return singleCellular2Edge(MANHATTAN,DISTANCE_2,seed, x, y);
            case CELLULAR2EDGE_MANHATTAN_DISTANCE_2_ADD:
                return singleCellular2Edge(MANHATTAN,DISTANCE_2_ADD,seed, x, y);
            case CELLULAR2EDGE_MANHATTAN_DISTANCE_2_SUB:
                return singleCellular2Edge(MANHATTAN,DISTANCE_2_SUB,seed, x, y);
            case CELLULAR2EDGE_MANHATTAN_DISTANCE_2_MUL:
                return singleCellular2Edge(MANHATTAN,DISTANCE_2_MUL,seed, x, y);
            case CELLULAR2EDGE_MANHATTAN_DISTANCE_2_DIV:
                return singleCellular2Edge(MANHATTAN,DISTANCE_2_DIV,seed, x, y);
            case CELLULAR2EDGE_NATURAL_DISTANCE_2:
                return singleCellular2Edge(NATURAL,DISTANCE_2,seed, x, y);
            case CELLULAR2EDGE_NATURAL_DISTANCE_2_ADD:
                return singleCellular2Edge(NATURAL,DISTANCE_2_ADD,seed, x, y);
            case CELLULAR2EDGE_NATURAL_DISTANCE_2_SUB:
                return singleCellular2Edge(NATURAL,DISTANCE_2_SUB,seed, x, y);
            case CELLULAR2EDGE_NATURAL_DISTANCE_2_MUL:
                return singleCellular2Edge(NATURAL,DISTANCE_2_MUL,seed, x, y);
            case CELLULAR2EDGE_NATURAL_DISTANCE_2_DIV:
                return singleCellular2Edge(NATURAL,DISTANCE_2_DIV,seed, x, y);
            case MUTANT_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC, foamSharpness,false, seed, x, y, mutation);
            case MUTANT_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE, foamSharpness,false, seed, x, y, mutation);
            case MUTANT_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR, foamSharpness,false, seed, x, y, mutation);
            case MUTANT_NORMAL_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC, foamSharpness,true, seed, x, y, mutation);
            case MUTANT_NORMAL_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE, foamSharpness,true, seed, x, y, mutation);
            case MUTANT_NORMAL_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR, foamSharpness,true, seed, x, y, mutation);
            case HONEY_QUINTIC:
                return singleHoney(QUINTIC, seed, x, y);
            case HONEY_HERMITE:
                return singleHoney(HERMITE, seed, x, y);
            case HONEY_LINEAR:
                return singleHoney(LINEAR, seed, x, y);
            case FOAM_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC,foamSharpness,false,seed, x, y);
            case FOAM_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE,foamSharpness,false,seed, x, y);
            case FOAM_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR,foamSharpness,false,seed, x, y);
            case RIPPLE_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC,foamSharpness,true,seed, x, y);
            case RIPPLE_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE,foamSharpness,true,seed, x, y);
            case RIPPLE_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR,foamSharpness,true,seed, x, y);
            case SUPERFOAM_CELLULAR2EDGE_MANHATTAN_DISTANCE_2:
                return singleFoam(NoiseType.CELLULAR2EDGE_MANHATTAN_DISTANCE_2,foamSharpness,false,seed, x, y);
            case SUPERFOAM_CELLULAR_EUCLIDEAN_CELL_VALUE:
                return singleFoam(NoiseType.CELLULAR_EUCLIDEAN_CELL_VALUE,foamSharpness,false,seed, x, y);
            case SUPERFOAM_CELLULAR_NATURAL_DISTANCE:
                return singleFoam(NoiseType.CELLULAR_NATURAL_DISTANCE,foamSharpness,false,seed, x, y);
            case SUPERFOAM_HONEY_LINEAR:
                return singleFoam(NoiseType.HONEY_LINEAR,foamSharpness,false,seed, x, y);
            case SUPERFOAM_HONEY_HERMITE:
                return singleFoam(NoiseType.HONEY_HERMITE,foamSharpness,false,seed, x, y);
            case SUPERFOAM_HONEY_QUINTIC:
                return singleFoam(NoiseType.HONEY_QUINTIC,foamSharpness,false,seed, x, y);
            case SUPERFOAM_MUTANT_NORMAL_LINEAR:
                return singleFoam(NoiseType.MUTANT_NORMAL_LINEAR,foamSharpness,false,seed, x, y, mutation);
            case SUPERFOAM_MUTANT_NORMAL_HERMITE:
                return singleFoam(NoiseType.MUTANT_NORMAL_HERMITE,foamSharpness,false,seed, x, y, mutation);
            case SUPERFOAM_MUTANT_NORMAL_QUINTIC:
                return singleFoam(NoiseType.MUTANT_NORMAL_QUINTIC,foamSharpness,false,seed, x, y, mutation);
            case SUPERFOAM_PYRAMID_LINEAR:
                return singleFoam(NoiseType.PYRAMID_LINEAR,foamSharpness,false,seed, x, y);
            case SUPERFOAM_PYRAMID_HERMITE:
                return singleFoam(NoiseType.PYRAMID_HERMITE,foamSharpness,false,seed, x, y);
            case SUPERFOAM_PYRAMID_QUINTIC:
                return singleFoam(NoiseType.PYRAMID_QUINTIC,foamSharpness,false,seed, x, y);
            case SUPERFOAM_RIPPLE_LINEAR:
                return singleFoam(NoiseType.RIPPLE_LINEAR,foamSharpness,false,seed, x, y);
            case SUPERFOAM_RIPPLE_HERMITE:
                return singleFoam(NoiseType.RIPPLE_HERMITE,foamSharpness,false,seed, x, y);
            case SUPERFOAM_RIPPLE_QUINTIC:
                return singleFoam(NoiseType.RIPPLE_QUINTIC,foamSharpness,false,seed, x, y);
            case PYRAMID_LINEAR:
                return (double) singlePyramid(LINEAR, seed, x, y);
            case PYRAMID_HERMITE:
                return (double) singlePyramid(HERMITE, seed, x, y);
            case PYRAMID_QUINTIC:
                return (double) singlePyramid(QUINTIC, seed, x, y);
            case CUBIC:
                return singleCubic(seed, x, y);
            case WHITE:
                return FnWhite.singleWhiteNoise(seed, x, y, false);
            case WHITE_NORMAL:
                return FnWhite.singleWhiteNoise(seed, x, y, true);
            case BLUR:
                return FnBlur.singleBlur(LINEAR, seed, x, y);
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
            case SIMPLEX_LUMP:
                return FnLump.singleLump(seed, _harshness, x, y);
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
                return singleValue(LINEAR, seed, x, y);
            case SOLID:
                return singleSolid(QUINTIC, seed, x, y);
            case SUPERFOAM_NORMAL_CELLULAR2EDGE_MANHATTAN_DISTANCE_2:
                return singleFoam(NoiseType.CELLULAR2EDGE_MANHATTAN_DISTANCE_2,foamSharpness,true,seed, x, y);
            case SUPERFOAM_NORMAL_CELLULAR_EUCLIDEAN_CELL_VALUE:
                return singleFoam(NoiseType.CELLULAR_EUCLIDEAN_CELL_VALUE,foamSharpness,true,seed, x, y);
            case SUPERFOAM_NORMAL_CELLULAR_NATURAL_DISTANCE:
                return singleFoam(NoiseType.CELLULAR_NATURAL_DISTANCE,foamSharpness,true,seed, x, y);
            case SUPERFOAM_NORMAL_HONEY_LINEAR:
                return singleFoam(NoiseType.HONEY_LINEAR,foamSharpness,true,seed, x, y);
            case SUPERFOAM_NORMAL_HONEY_HERMITE:
                return singleFoam(NoiseType.HONEY_HERMITE,foamSharpness,true,seed, x, y);
            case SUPERFOAM_NORMAL_HONEY_QUINTIC:
                return singleFoam(NoiseType.HONEY_QUINTIC,foamSharpness,true,seed, x, y);
            case SUPERFOAM_NORMAL_MUTANT_NORMAL_LINEAR:
                return singleFoam(NoiseType.MUTANT_NORMAL_LINEAR,foamSharpness,true,seed, x, y, mutation);
            case SUPERFOAM_NORMAL_MUTANT_NORMAL_HERMITE:
                return singleFoam(NoiseType.MUTANT_NORMAL_HERMITE,foamSharpness,true,seed, x, y, mutation);
            case SUPERFOAM_NORMAL_MUTANT_NORMAL_QUINTIC:
                return singleFoam(NoiseType.MUTANT_NORMAL_QUINTIC,foamSharpness,true,seed, x, y, mutation);
            case SUPERFOAM_NORMAL_PYRAMID_LINEAR:
                return singleFoam(NoiseType.PYRAMID_LINEAR,foamSharpness,true,seed, x, y);
            case SUPERFOAM_NORMAL_PYRAMID_HERMITE:
                return singleFoam(NoiseType.PYRAMID_HERMITE,foamSharpness,true,seed, x, y);
            case SUPERFOAM_NORMAL_PYRAMID_QUINTIC:
                return singleFoam(NoiseType.PYRAMID_QUINTIC,foamSharpness,true,seed, x, y);
            case SUPERFOAM_NORMAL_RIPPLE_LINEAR:
                return singleFoam(NoiseType.RIPPLE_LINEAR,foamSharpness,true,seed, x, y);
            case SUPERFOAM_NORMAL_RIPPLE_HERMITE:
                return singleFoam(NoiseType.RIPPLE_HERMITE,foamSharpness,true,seed, x, y);
            case SUPERFOAM_NORMAL_RIPPLE_QUINTIC:
                return singleFoam(NoiseType.RIPPLE_QUINTIC,foamSharpness,true,seed, x, y);
            default:
                return -1;
        }
    }

    public static double singleNoiseByType(NoiseType type, double _harshness, double mutation, double foamSharpness, int seed, double x, double y, double z) {
        switch (type)
        {
            case CELLULAR_MERGE_EUCLIDEAN:
                return singleCellularMerge(EUCLIDEAN,foamSharpness, seed, x, y, z);
            case CELLULAR_MERGE_MANHATTAN:
                return singleCellularMerge(MANHATTAN,foamSharpness, seed, x, y, z);
            case CELLULAR_MERGE_NATURAL:
                return singleCellularMerge(NATURAL,foamSharpness, seed, x, y, z);
            case CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2:
                return singleCellular2Edge(EUCLIDEAN,DISTANCE_2,seed, x, y, z);
            case CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2_ADD:
                return singleCellular2Edge(EUCLIDEAN,DISTANCE_2_ADD,seed, x, y, z);
            case CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2_SUB:
                return singleCellular2Edge(EUCLIDEAN,DISTANCE_2_SUB,seed, x, y, z);
            case CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2_MUL:
                return singleCellular2Edge(EUCLIDEAN,DISTANCE_2_MUL,seed, x, y, z);
            case CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2_DIV:
                return singleCellular2Edge(EUCLIDEAN,DISTANCE_2_DIV,seed, x, y, z);
            case CELLULAR2EDGE_MANHATTAN_DISTANCE_2:
                return singleCellular2Edge(MANHATTAN,DISTANCE_2,seed, x, y, z);
            case CELLULAR2EDGE_MANHATTAN_DISTANCE_2_ADD:
                return singleCellular2Edge(MANHATTAN,DISTANCE_2_ADD,seed, x, y, z);
            case CELLULAR2EDGE_MANHATTAN_DISTANCE_2_SUB:
                return singleCellular2Edge(MANHATTAN,DISTANCE_2_SUB,seed, x, y, z);
            case CELLULAR2EDGE_MANHATTAN_DISTANCE_2_MUL:
                return singleCellular2Edge(MANHATTAN,DISTANCE_2_MUL,seed, x, y, z);
            case CELLULAR2EDGE_MANHATTAN_DISTANCE_2_DIV:
                return singleCellular2Edge(MANHATTAN,DISTANCE_2_DIV,seed, x, y, z);
            case CELLULAR2EDGE_NATURAL_DISTANCE_2:
                return singleCellular2Edge(NATURAL,DISTANCE_2,seed, x, y, z);
            case CELLULAR2EDGE_NATURAL_DISTANCE_2_ADD:
                return singleCellular2Edge(NATURAL,DISTANCE_2_ADD,seed, x, y, z);
            case CELLULAR2EDGE_NATURAL_DISTANCE_2_SUB:
                return singleCellular2Edge(NATURAL,DISTANCE_2_SUB,seed, x, y, z);
            case CELLULAR2EDGE_NATURAL_DISTANCE_2_MUL:
                return singleCellular2Edge(NATURAL,DISTANCE_2_MUL,seed, x, y, z);
            case CELLULAR2EDGE_NATURAL_DISTANCE_2_DIV:
                return singleCellular2Edge(NATURAL,DISTANCE_2_DIV,seed, x, y, z);
            case MUTANT_NORMAL_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC, foamSharpness,true, seed, x, y, z, mutation);
            case MUTANT_NORMAL_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE, foamSharpness,true, seed, x, y, z, mutation);
            case MUTANT_NORMAL_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR, foamSharpness,true, seed, x, y, z, mutation);
            case MUTANT_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC, foamSharpness,false, seed, x, y, z, mutation);
            case MUTANT_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE, foamSharpness,false, seed, x, y, z, mutation);
            case MUTANT_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR, foamSharpness,false, seed, x, y, z, mutation);
            case HONEY_QUINTIC:
                return singleHoney(QUINTIC, seed, x, y,z);
            case HONEY_HERMITE:
                return singleHoney(HERMITE, seed, x, y,z);
            case HONEY_LINEAR:
                return singleHoney(LINEAR, seed, x, y,z);
            case FOAM_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC,foamSharpness,false,seed, x, y, z);
            case FOAM_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE,foamSharpness,false,seed, x, y, z);
            case FOAM_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR,foamSharpness,false,seed, x, y, z);
            case RIPPLE_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC,foamSharpness,true,seed, x, y, z);
            case RIPPLE_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE,foamSharpness,true,seed, x, y, z);
            case RIPPLE_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR,foamSharpness,true,seed, x, y, z);
            case SUPERFOAM_CELLULAR2EDGE_MANHATTAN_DISTANCE_2:
                return singleFoam(NoiseType.CELLULAR2EDGE_MANHATTAN_DISTANCE_2,foamSharpness,false,seed, x, y, z);
            case SUPERFOAM_CELLULAR_EUCLIDEAN_CELL_VALUE:
                return singleFoam(NoiseType.CELLULAR_EUCLIDEAN_CELL_VALUE,foamSharpness,false,seed, x, y, z);
            case SUPERFOAM_CELLULAR_NATURAL_DISTANCE:
                return singleFoam(NoiseType.CELLULAR_NATURAL_DISTANCE,foamSharpness,false,seed, x, y, z);
            case SUPERFOAM_HONEY_LINEAR:
                return singleFoam(NoiseType.HONEY_LINEAR,foamSharpness,false,seed, x, y, z);
            case SUPERFOAM_HONEY_HERMITE:
                return singleFoam(NoiseType.HONEY_HERMITE,foamSharpness,false,seed, x, y, z);
            case SUPERFOAM_HONEY_QUINTIC:
                return singleFoam(NoiseType.HONEY_QUINTIC,foamSharpness,false,seed, x, y, z);
            case SUPERFOAM_MUTANT_NORMAL_LINEAR:
                return singleFoam(NoiseType.MUTANT_NORMAL_LINEAR,foamSharpness,false,seed, x, y, z, mutation);
            case SUPERFOAM_MUTANT_NORMAL_HERMITE:
                return singleFoam(NoiseType.MUTANT_NORMAL_HERMITE,foamSharpness,false,seed, x, y, z, mutation);
            case SUPERFOAM_MUTANT_NORMAL_QUINTIC:
                return singleFoam(NoiseType.MUTANT_NORMAL_QUINTIC,foamSharpness,false,seed, x, y, z, mutation);
            case SUPERFOAM_PYRAMID_LINEAR:
                return singleFoam(NoiseType.PYRAMID_LINEAR,foamSharpness,false,seed, x, y, z);
            case SUPERFOAM_PYRAMID_HERMITE:
                return singleFoam(NoiseType.PYRAMID_HERMITE,foamSharpness,false,seed, x, y, z);
            case SUPERFOAM_PYRAMID_QUINTIC:
                return singleFoam(NoiseType.PYRAMID_QUINTIC,foamSharpness,false,seed, x, y, z);
            case SUPERFOAM_RIPPLE_LINEAR:
                return singleFoam(NoiseType.RIPPLE_LINEAR,foamSharpness,false,seed, x, y, z);
            case SUPERFOAM_RIPPLE_HERMITE:
                return singleFoam(NoiseType.RIPPLE_HERMITE,foamSharpness,false,seed, x, y, z);
            case SUPERFOAM_RIPPLE_QUINTIC:
                return singleFoam(NoiseType.RIPPLE_QUINTIC,foamSharpness,false,seed, x, y, z);
            case PYRAMID_LINEAR:
                return (double) singlePyramid(LINEAR, seed, x, y, z);
            case PYRAMID_HERMITE:
                return (double) singlePyramid(HERMITE, seed, x, y, z);
            case PYRAMID_QUINTIC:
                return (double) singlePyramid(QUINTIC, seed, x, y, z);
            case CUBIC:
                return singleCubic(seed, x, y, z);
            case WHITE:
                return FnWhite.singleWhiteNoise(seed, x, y, z, false);
            case WHITE_NORMAL:
                return FnWhite.singleWhiteNoise(seed, x, y, z, true);
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
            case SIMPLEX_LUMP:
                return FnLump.singleLump(seed, _harshness, x, y, z);
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
                return singleValue(LINEAR, seed, x, y);
            case SUPERFOAM_NORMAL_CELLULAR2EDGE_MANHATTAN_DISTANCE_2:
                return singleFoam(NoiseType.CELLULAR2EDGE_MANHATTAN_DISTANCE_2,foamSharpness,true,seed, x, y, z);
            case SUPERFOAM_NORMAL_CELLULAR_EUCLIDEAN_CELL_VALUE:
                return singleFoam(NoiseType.CELLULAR_EUCLIDEAN_CELL_VALUE,foamSharpness,true,seed, x, y, z);
            case SUPERFOAM_NORMAL_CELLULAR_NATURAL_DISTANCE:
                return singleFoam(NoiseType.CELLULAR_NATURAL_DISTANCE,foamSharpness,true,seed, x, y, z);
            case SUPERFOAM_NORMAL_HONEY_LINEAR:
                return singleFoam(NoiseType.HONEY_LINEAR,foamSharpness,true,seed, x, y, z);
            case SUPERFOAM_NORMAL_HONEY_HERMITE:
                return singleFoam(NoiseType.HONEY_HERMITE,foamSharpness,true,seed, x, y, z);
            case SUPERFOAM_NORMAL_HONEY_QUINTIC:
                return singleFoam(NoiseType.HONEY_QUINTIC,foamSharpness,true,seed, x, y, z);
            case SUPERFOAM_NORMAL_MUTANT_NORMAL_LINEAR:
                return singleFoam(NoiseType.MUTANT_NORMAL_LINEAR,foamSharpness,true,seed, x, y, z, mutation);
            case SUPERFOAM_NORMAL_MUTANT_NORMAL_HERMITE:
                return singleFoam(NoiseType.MUTANT_NORMAL_HERMITE,foamSharpness,true,seed, x, y, z, mutation);
            case SUPERFOAM_NORMAL_MUTANT_NORMAL_QUINTIC:
                return singleFoam(NoiseType.MUTANT_NORMAL_QUINTIC,foamSharpness,true,seed, x, y, z, mutation);
            case SUPERFOAM_NORMAL_PYRAMID_LINEAR:
                return singleFoam(NoiseType.PYRAMID_LINEAR,foamSharpness,true,seed, x, y, z);
            case SUPERFOAM_NORMAL_PYRAMID_HERMITE:
                return singleFoam(NoiseType.PYRAMID_HERMITE,foamSharpness,true,seed, x, y, z);
            case SUPERFOAM_NORMAL_PYRAMID_QUINTIC:
                return singleFoam(NoiseType.PYRAMID_QUINTIC,foamSharpness,true,seed, x, y, z);
            case SUPERFOAM_NORMAL_RIPPLE_LINEAR:
                return singleFoam(NoiseType.RIPPLE_LINEAR,foamSharpness,true,seed, x, y, z);
            case SUPERFOAM_NORMAL_RIPPLE_HERMITE:
                return singleFoam(NoiseType.RIPPLE_HERMITE,foamSharpness,true,seed, x, y, z);
            case SUPERFOAM_NORMAL_RIPPLE_QUINTIC:
                return singleFoam(NoiseType.RIPPLE_QUINTIC,foamSharpness,true,seed, x, y, z);
            default:
                return -1;
        }
    }

    public static double singleNoiseByType(NoiseType type, double _harshness, double mutation, double foamSharpness, int seed, double x, double y, double z, double w) {
        switch (type)
        {
            case MUTANT_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC, foamSharpness,false, seed, x, y, z, w, mutation);
            case MUTANT_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE, foamSharpness,false, seed, x, y, z, w, mutation);
            case MUTANT_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR, foamSharpness,false, seed, x, y, z, w, mutation);
            case MUTANT_NORMAL_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC, foamSharpness,true, seed, x, y, z, w, mutation);
            case MUTANT_NORMAL_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE, foamSharpness,true, seed, x, y, z, w, mutation);
            case MUTANT_NORMAL_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR, foamSharpness,true, seed, x, y, z, w, mutation);
            case HONEY_QUINTIC:
                return singleHoney(QUINTIC, seed, x, y,z,w);
            case HONEY_HERMITE:
                return singleHoney(HERMITE, seed, x, y,z,w);
            case HONEY_LINEAR:
                return singleHoney(LINEAR, seed, x, y,z,w);
            case FOAM_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC,foamSharpness,false,seed, x, y, z, w);
            case FOAM_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE,foamSharpness,false,seed, x, y, z, w);
            case FOAM_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR,foamSharpness,false,seed, x, y, z, w);
            case RIPPLE_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC,foamSharpness,true,seed, x, y, z, w);
            case RIPPLE_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE,foamSharpness,true,seed, x, y, z, w);
            case RIPPLE_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR,foamSharpness,true,seed, x, y, z, w);
            case PYRAMID_LINEAR:
                return (double) singlePyramid(LINEAR, seed, x, y, z, w);
            case PYRAMID_HERMITE:
                return (double) singlePyramid(HERMITE, seed, x, y, z, w);
            case PYRAMID_QUINTIC:
                return (double) singlePyramid(QUINTIC, seed, x, y, z, w);
            case SUPERFOAM_CELLULAR2EDGE_MANHATTAN_DISTANCE_2:
                return singleFoam(NoiseType.CELLULAR2EDGE_MANHATTAN_DISTANCE_2,foamSharpness,false,seed, x, y, z, w);
            case SUPERFOAM_CELLULAR_EUCLIDEAN_CELL_VALUE:
                return singleFoam(NoiseType.CELLULAR_EUCLIDEAN_CELL_VALUE,foamSharpness,false,seed, x, y, z, w);
            case SUPERFOAM_CELLULAR_NATURAL_DISTANCE:
                return singleFoam(NoiseType.CELLULAR_NATURAL_DISTANCE,foamSharpness,false,seed, x, y, z, w);
            case SUPERFOAM_HONEY_LINEAR:
                return singleFoam(NoiseType.HONEY_LINEAR,foamSharpness,false,seed, x, y, z, w);
            case SUPERFOAM_HONEY_HERMITE:
                return singleFoam(NoiseType.HONEY_HERMITE,foamSharpness,false,seed, x, y, z, w);
            case SUPERFOAM_HONEY_QUINTIC:
                return singleFoam(NoiseType.HONEY_QUINTIC,foamSharpness,false,seed, x, y, z, w);
            case SUPERFOAM_MUTANT_NORMAL_LINEAR:
                return singleFoam(NoiseType.MUTANT_NORMAL_LINEAR,foamSharpness,false,seed, x, y, z, w, mutation);
            case SUPERFOAM_MUTANT_NORMAL_HERMITE:
                return singleFoam(NoiseType.MUTANT_NORMAL_HERMITE,foamSharpness,false,seed, x, y, z, w, mutation);
            case SUPERFOAM_MUTANT_NORMAL_QUINTIC:
                return singleFoam(NoiseType.MUTANT_NORMAL_QUINTIC,foamSharpness,false,seed, x, y, z, w, mutation);
            case SUPERFOAM_PYRAMID_LINEAR:
                return singleFoam(NoiseType.PYRAMID_LINEAR,foamSharpness,false,seed, x, y, z, w);
            case SUPERFOAM_PYRAMID_HERMITE:
                return singleFoam(NoiseType.PYRAMID_HERMITE,foamSharpness,false,seed, x, y, z, w);
            case SUPERFOAM_PYRAMID_QUINTIC:
                return singleFoam(NoiseType.PYRAMID_QUINTIC,foamSharpness,false,seed, x, y, z, w);
            case SUPERFOAM_RIPPLE_LINEAR:
                return singleFoam(NoiseType.RIPPLE_LINEAR,foamSharpness,false,seed, x, y, z, w);
            case SUPERFOAM_RIPPLE_HERMITE:
                return singleFoam(NoiseType.RIPPLE_HERMITE,foamSharpness,false,seed, x, y, z, w);
            case SUPERFOAM_RIPPLE_QUINTIC:
                return singleFoam(NoiseType.RIPPLE_QUINTIC,foamSharpness,false,seed, x, y, z, w);
            case CUBIC:
                return singleCubic(seed, x, y, z, w);
            case WHITE:
                return FnWhite.singleWhiteNoise(seed, x, y, z, w, false);
            case WHITE_NORMAL:
                return FnWhite.singleWhiteNoise(seed, x, y, z, w, true);
            case SIMPLEX:
                return FnSimplex.singleSimplex(seed, x, y, z, w);
            case SIMPLEX_LUMP:
                return FnLump.singleLump(seed, _harshness, x, y, z, w);
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
                return singleValue(LINEAR, seed, x, y);
            default:
                return -1;
        }
    }

    public static double singleNoiseByType(NoiseType type, double _harshness, double mutation, double foamSharpness, int seed, double x, double y, double z, double w, double u) {
        switch (type)
        {
            case MUTANT_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC, foamSharpness,false, seed, x, y, z, w, u, mutation);
            case MUTANT_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE, foamSharpness,false, seed, x, y, z, w, u, mutation);
            case MUTANT_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR, foamSharpness,false, seed, x, y, z, w, u, mutation);
            case MUTANT_NORMAL_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC, foamSharpness,true, seed, x, y, z, w, u, mutation);
            case MUTANT_NORMAL_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE, foamSharpness,true, seed, x, y, z, w, u, mutation);
            case MUTANT_NORMAL_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR, foamSharpness,true, seed, x, y, z, w, u, mutation);
            case HONEY_QUINTIC:
                return singleHoney(QUINTIC, seed, x, y,z,w,u);
            case HONEY_HERMITE:
                return singleHoney(HERMITE, seed, x, y,z,w,u);
            case HONEY_LINEAR:
                return singleHoney(LINEAR, seed, x, y,z,w,u);
            case FOAM_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC,foamSharpness,false,seed, x, y, z, w, u);
            case FOAM_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE,foamSharpness,false,seed, x, y, z, w, u);
            case FOAM_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR,foamSharpness,false,seed, x, y, z, w, u);
            case RIPPLE_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC,foamSharpness,true,seed, x, y, z, w, u);
            case RIPPLE_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE,foamSharpness,true,seed, x, y, z, w, u);
            case RIPPLE_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR,foamSharpness,true,seed, x, y, z, w, u);
            case PYRAMID_LINEAR:
                return (double) singlePyramid(LINEAR, seed, x, y, z, w, u);
            case PYRAMID_HERMITE:
                return (double) singlePyramid(HERMITE, seed, x, y, z, w, u);
            case PYRAMID_QUINTIC:
                return (double) singlePyramid(QUINTIC, seed, x, y, z, w, u);
            case SUPERFOAM_CELLULAR2EDGE_MANHATTAN_DISTANCE_2:
                return singleFoam(NoiseType.CELLULAR2EDGE_MANHATTAN_DISTANCE_2,foamSharpness,false,seed, x, y, z, w, u);
            case SUPERFOAM_CELLULAR_EUCLIDEAN_CELL_VALUE:
                return singleFoam(NoiseType.CELLULAR_EUCLIDEAN_CELL_VALUE,foamSharpness,false,seed, x, y, z, w, u);
            case SUPERFOAM_CELLULAR_NATURAL_DISTANCE:
                return singleFoam(NoiseType.CELLULAR_NATURAL_DISTANCE,foamSharpness,false,seed, x, y, z, w, u);
            case SUPERFOAM_HONEY_LINEAR:
                return singleFoam(NoiseType.HONEY_LINEAR,foamSharpness,false,seed, x, y, z, w, u);
            case SUPERFOAM_HONEY_HERMITE:
                return singleFoam(NoiseType.HONEY_HERMITE,foamSharpness,false,seed, x, y, z, w, u);
            case SUPERFOAM_HONEY_QUINTIC:
                return singleFoam(NoiseType.HONEY_QUINTIC,foamSharpness,false,seed, x, y, z, w, u);
            case SUPERFOAM_MUTANT_NORMAL_LINEAR:
                return singleFoam(NoiseType.MUTANT_NORMAL_LINEAR,foamSharpness,false,seed, x, y, z, w, u, mutation);
            case SUPERFOAM_MUTANT_NORMAL_HERMITE:
                return singleFoam(NoiseType.MUTANT_NORMAL_HERMITE,foamSharpness,false,seed, x, y, z, w, u, mutation);
            case SUPERFOAM_MUTANT_NORMAL_QUINTIC:
                return singleFoam(NoiseType.MUTANT_NORMAL_QUINTIC,foamSharpness,false,seed, x, y, z, w, u, mutation);
            case SUPERFOAM_PYRAMID_LINEAR:
                return singleFoam(NoiseType.PYRAMID_LINEAR,foamSharpness,false,seed, x, y, z, w, u);
            case SUPERFOAM_PYRAMID_HERMITE:
                return singleFoam(NoiseType.PYRAMID_HERMITE,foamSharpness,false,seed, x, y, z, w, u);
            case SUPERFOAM_PYRAMID_QUINTIC:
                return singleFoam(NoiseType.PYRAMID_QUINTIC,foamSharpness,false,seed, x, y, z, w, u);
            case SUPERFOAM_RIPPLE_LINEAR:
                return singleFoam(NoiseType.RIPPLE_LINEAR,foamSharpness,false,seed, x, y, z, w, u);
            case SUPERFOAM_RIPPLE_HERMITE:
                return singleFoam(NoiseType.RIPPLE_HERMITE,foamSharpness,false,seed, x, y, z, w, u);
            case SUPERFOAM_RIPPLE_QUINTIC:
                return singleFoam(NoiseType.RIPPLE_QUINTIC,foamSharpness,false,seed, x, y, z, w, u);
            case SIMPLEX:
                return FnSimplex.singleSimplex(seed, x, y, z, w, u);
            case SIMPLEX_LUMP:
                return FnLump.singleLump(seed, _harshness, x, y, z, w, u);
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
                return singleValue(LINEAR, seed, x, y);
            case WHITE:
                return FnWhite.singleWhiteNoise(seed, x, y, z, w, u, false);
            case WHITE_NORMAL:
                return FnWhite.singleWhiteNoise(seed, x, y, z, w, u, true);
            default:
                return -1;
        }
    }

    public static double singleNoiseByType(NoiseType type, double _harshness, double mutation, double foamSharpness, int seed, double x, double y, double z, double w, double u, double v) {
        switch (type)
        {
            case MUTANT_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC, foamSharpness,false, seed, x, y, z, w, u, v+mutation);
            case MUTANT_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE, foamSharpness,false, seed, x, y, z, w, u, v+mutation);
            case MUTANT_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR, foamSharpness,false, seed, x, y, z, w, u, v+mutation);
            case MUTANT_NORMAL_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC, foamSharpness,true, seed, x, y, z, w, u, v+mutation);
            case MUTANT_NORMAL_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE, foamSharpness,true, seed, x, y, z, w, u, v+mutation);
            case MUTANT_NORMAL_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR, foamSharpness,true, seed, x, y, z, w, u, v+mutation);
            case HONEY_QUINTIC:
                return singleHoney(QUINTIC, seed, x, y,z,w,u,v);
            case HONEY_HERMITE:
                return singleHoney(HERMITE, seed, x, y,z,w,u,v);
            case HONEY_LINEAR:
                return singleHoney(LINEAR, seed, x, y,z,w,u,v);
            case FOAM_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC,foamSharpness,false,seed, x, y, z, w, u, v);
            case FOAM_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE,foamSharpness,false,seed, x, y, z, w, u, v);
            case FOAM_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR,foamSharpness,false,seed, x, y, z, w, u, v);
            case RIPPLE_QUINTIC:
                return singleFoam(NoiseType.VALUE_QUINTIC,foamSharpness,true,seed, x, y, z, w, u, v);
            case RIPPLE_HERMITE:
                return singleFoam(NoiseType.VALUE_HERMITE,foamSharpness,true,seed, x, y, z, w, u, v);
            case RIPPLE_LINEAR:
                return singleFoam(NoiseType.VALUE_LINEAR,foamSharpness,true,seed, x, y, z, w, u, v);
            case PYRAMID_LINEAR:
                return (double) singlePyramid(LINEAR, seed, x, y, z, w, u, v);
            case PYRAMID_HERMITE:
                return (double) singlePyramid(HERMITE, seed, x, y, z, w, u, v);
            case PYRAMID_QUINTIC:
                return (double) singlePyramid(QUINTIC, seed, x, y, z, w, u, v);
            case SUPERFOAM_CELLULAR2EDGE_MANHATTAN_DISTANCE_2:
                return singleFoam(NoiseType.CELLULAR2EDGE_MANHATTAN_DISTANCE_2,foamSharpness,false,seed, x, y, z, w, u, v);
            case SUPERFOAM_CELLULAR_EUCLIDEAN_CELL_VALUE:
                return singleFoam(NoiseType.CELLULAR_EUCLIDEAN_CELL_VALUE,foamSharpness,false,seed, x, y, z, w, u, v);
            case SUPERFOAM_CELLULAR_NATURAL_DISTANCE:
                return singleFoam(NoiseType.CELLULAR_NATURAL_DISTANCE,foamSharpness,false,seed, x, y, z, w, u, v);
            case SUPERFOAM_HONEY_LINEAR:
                return singleFoam(NoiseType.HONEY_LINEAR,foamSharpness,false,seed, x, y, z, w, u, v);
            case SUPERFOAM_HONEY_HERMITE:
                return singleFoam(NoiseType.HONEY_HERMITE,foamSharpness,false,seed, x, y, z, w, u, v);
            case SUPERFOAM_HONEY_QUINTIC:
                return singleFoam(NoiseType.HONEY_QUINTIC,foamSharpness,false,seed, x, y, z, w, u, v);
            case SUPERFOAM_MUTANT_NORMAL_LINEAR:
                return singleFoam(NoiseType.MUTANT_NORMAL_LINEAR,foamSharpness,false,seed, x, y, z, w, u, v, mutation);
            case SUPERFOAM_MUTANT_NORMAL_HERMITE:
                return singleFoam(NoiseType.MUTANT_NORMAL_HERMITE,foamSharpness,false,seed, x, y, z, w, u, v, mutation);
            case SUPERFOAM_MUTANT_NORMAL_QUINTIC:
                return singleFoam(NoiseType.MUTANT_NORMAL_QUINTIC,foamSharpness,false,seed, x, y, z, w, u, v, mutation);
            case SUPERFOAM_PYRAMID_LINEAR:
                return singleFoam(NoiseType.PYRAMID_LINEAR,foamSharpness,false,seed, x, y, z, w, u, v);
            case SUPERFOAM_PYRAMID_HERMITE:
                return singleFoam(NoiseType.PYRAMID_HERMITE,foamSharpness,false,seed, x, y, z, w, u, v);
            case SUPERFOAM_PYRAMID_QUINTIC:
                return singleFoam(NoiseType.PYRAMID_QUINTIC,foamSharpness,false,seed, x, y, z, w, u, v);
            case SUPERFOAM_RIPPLE_LINEAR:
                return singleFoam(NoiseType.RIPPLE_LINEAR,foamSharpness,false,seed, x, y, z, w, u, v);
            case SUPERFOAM_RIPPLE_HERMITE:
                return singleFoam(NoiseType.RIPPLE_HERMITE,foamSharpness,false,seed, x, y, z, w, u, v);
            case SUPERFOAM_RIPPLE_QUINTIC:
                return singleFoam(NoiseType.RIPPLE_QUINTIC,foamSharpness,false,seed, x, y, z, w, u, v);
            case SIMPLEX:
                return FnSimplex.singleSimplex(seed, x, y, z, w, u, v);
            case SIMPLEX_LUMP:
                return FnLump.singleLump(seed, _harshness, x, y, z, w, u, v);
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
                return singleValue(LINEAR, seed, x, y);
            case WHITE:
                return FnWhite.singleWhiteNoise(seed, x, y, z, w, u, v, false);
            case WHITE_NORMAL:
                return FnWhite.singleWhiteNoise(seed, x, y, z, w, u, v, true);
            default:
                return -1;
        }
    }


    // ----------------------------------------------------------------------------

    public static double singleByNoiseAndTransform(NoiseType type, TransformType _distort, int seed, double x, double y)
    {
        return singleByNoiseAndTransform(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, _distort, seed, x, y);
    }

    public static double singleByNoiseAndTransform(NoiseType type, TransformType _distort, int seed, double x, double y, double z)
    {
        return singleByNoiseAndTransform(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, _distort, seed, x, y, z);
    }

    public static double singleByNoiseAndTransform(NoiseType type, TransformType _distort, int seed, double x, double y, double z, double w)
    {
        return singleByNoiseAndTransform(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, _distort, seed, x, y, z, w);
    }

    public static double singleByNoiseAndTransform(NoiseType type, TransformType _distort, int seed, double x, double y, double z, double w, double u)
    {
        return singleByNoiseAndTransform(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, _distort, seed, x, y, z, w, u);
    }

    public static double singleByNoiseAndTransform(NoiseType type, TransformType _distort, int seed, double x, double y, double z, double w, double u, double v)
    {
        return singleByNoiseAndTransform(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, _distort, seed, x, y, z, w, u, v);
    }

    public static double singleByNoiseAndTransform(NoiseType type, TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y)
    {
        return singleByNoiseAndTransform(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, _distort, _arg1, _arg2, _arg3, seed, x, y);
    }

    public static double singleByNoiseAndTransform(NoiseType type, TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y, double z)
    {
        return singleByNoiseAndTransform(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, _distort, _arg1, _arg2, _arg3, seed, x, y, z);
    }

    public static double singleByNoiseAndTransform(NoiseType type, TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y, double z, double w)
    {
        return singleByNoiseAndTransform(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, _distort, _arg1, _arg2, _arg3, seed, x, y, z, w);
    }

    public static double singleByNoiseAndTransform(NoiseType type, TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y, double z, double w, double u)
    {
        return singleByNoiseAndTransform(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, _distort, _arg1, _arg2, _arg3, seed, x, y, z, w, u);
    }

    public static double singleByNoiseAndTransform(NoiseType type, TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y, double z, double w, double u, double v)
    {
        return singleByNoiseAndTransform(type, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, _distort, _arg1, _arg2, _arg3, seed, x, y, z, w, u, v);
    }

    public static double singleTransform(TransformType _distort, double _result, double _arg1, double _arg2, double _arg3)
    {
        return singleTransformUnit(_distort, _result, _arg1,_arg2,_arg3);
    }

    public static double singleTransformUnit(TransformType _distort, double _result, double _arg1, double _arg2, double _arg3)
    {
        switch (_distort)
        {
            case T_EX: try { _result = ((_result-1.) / (_result+1.)); break; } catch (Exception _xe) { _result = Double.MAX_VALUE; break; }
            case T_EXP: _result = -Math.log(0.5 - _result * 0.5) / 2.0; break;
            case T_SINE: _result = Math.sin(_result*Math.PI); break;
            case T_COSINE: _result = Math.cos(_result*Math.PI); break;
            case T_SINE_2: _result = Math.sin(_result*Math.PI/2.); break;
            case T_COSINE_2: _result = Math.cos(_result*Math.PI/2.); break;
            case T_SQ_SINE: _result = Math.sin(_result*_result); break;
            case T_SQ_COSINE: _result = Math.cos(_result*_result); break;
            case T_INVERT: _result = -_result; break;
            case T_SQUARE_ROOT: _result = Math.pow(_result, .5); break;
            case T_CUBE_ROOT: _result = Math.pow(_result, 1./3.); break;
            case T_SQUARE: _result = (_result*_result); break;
            case T_CUBE: _result = (_result*_result*_result); break;
            case T_QUART: _result = (_result*_result*_result*_result); break;
            case T_ABS: _result = Math.abs(_result); break;
            case T_ABS1M: _result = 1.-Math.abs(_result); break;
            case T_HERMITESPLINE: _result = (hermiteInterpolator(0.5+_result*0.5) * 2.) - 1.; break;
            case T_QUINTICSPLINE: _result = (quinticInterpolator(0.5+_result*0.5) * 2.) - 1.; break;
            case T_BARRONSPLINE: _result = (barronSpline(0.5+(_result*0.5), _arg1, _arg2) * 2.) - 1.; break;
            case T_BINARY: _result = (_result<0.) ? -.987f : .987f;
            case T_0NONE:
            default: break;
        }
        return _result;
    }

    public static double singleTransformAbs(TransformType _distort, double _result, double _arg1, double _arg2, double _arg3)
    {
        // result comes in 0..1 output must be 0..1
        _result = (_result * 2f) - 1f;
        _result = singleTransformUnit(_distort, _result, _arg1,_arg2,_arg3);
        _result = (_result*.5f) + .5f;
        return _result;
    }

    public static double singleByNoiseAndTransform(NoiseType type, double _harshness, double mutation, double foamSharpness, TransformType _distort, int seed, double x, double y)
    {
        double _result = singleNoiseByType(type,_harshness,mutation, foamSharpness, seed, x, y);
        return singleTransformUnit(_distort, _result, 0f, 0f, 0f);
    }

    public static double singleByNoiseAndTransform(NoiseType type, double _harshness, double mutation, double foamSharpness, TransformType _distort, int seed, double x, double y, double z)
    {
        double _result = singleNoiseByType(type,_harshness,mutation, foamSharpness, seed, x, y, z);
        return singleTransformUnit(_distort, _result, 0f, 0f, 0f);
    }

    public static double singleByNoiseAndTransform(NoiseType type, double _harshness, double mutation, double foamSharpness, TransformType _distort, int seed, double x, double y, double z, double w)
    {
        double _result = singleNoiseByType(type,_harshness,mutation, foamSharpness, seed, x, y, z, w);
        return singleTransformUnit(_distort, _result, 0f, 0f, 0f);
    }

    public static double singleByNoiseAndTransform(NoiseType type, double _harshness, double mutation, double foamSharpness, TransformType _distort, int seed, double x, double y, double z, double w, double u)
    {
        double _result = singleNoiseByType(type,_harshness,mutation, foamSharpness, seed, x, y, z, w, u);
        return singleTransformUnit(_distort, _result, 0f, 0f, 0f);
    }

    public static double singleByNoiseAndTransform(NoiseType type, double _harshness, double mutation, double foamSharpness, TransformType _distort, int seed, double x, double y, double z, double w, double u, double v)
    {
        double _result = singleNoiseByType(type,_harshness,mutation, foamSharpness, seed, x, y, z, w, u, v);
        return singleTransformUnit(_distort, _result, 0f, 0f, 0f);
    }


    public static double singleByNoiseAndTransform(NoiseType type, double _harshness, double mutation, double foamSharpness, TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y)
    {
        double _result = singleNoiseByType(type,_harshness,mutation, foamSharpness, seed, x, y);
        return singleTransformUnit(_distort, _result, _arg1, _arg2, _arg3);
    }

    public static double singleByNoiseAndTransform(NoiseType type, double _harshness, double mutation, double foamSharpness, TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y, double z)
    {
        double _result = singleNoiseByType(type,_harshness,mutation, foamSharpness, seed, x, y, z);
        return singleTransformUnit(_distort, _result, _arg1, _arg2, _arg3);
    }

    public static double singleByNoiseAndTransform(NoiseType type, double _harshness, double mutation, double foamSharpness, TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y, double z, double w)
    {
        double _result = singleNoiseByType(type,_harshness,mutation, foamSharpness, seed, x, y, z, w);
        return singleTransformUnit(_distort, _result, _arg1, _arg2, _arg3);
    }

    public static double singleByNoiseAndTransform(NoiseType type, double _harshness, double mutation, double foamSharpness, TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y, double z, double w, double u)
    {
        double _result = singleNoiseByType(type,_harshness,mutation, foamSharpness, seed, x, y, z, w, u);
        return singleTransformUnit(_distort, _result, _arg1, _arg2, _arg3);
    }

    public static double singleByNoiseAndTransform(NoiseType type, double _harshness, double mutation, double foamSharpness, TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y, double z, double w, double u, double v)
    {
        double _result = singleNoiseByType(type,_harshness,mutation, foamSharpness, seed, x, y, z, w, u, v);
        return singleTransformUnit(_distort, _result, _arg1, _arg2, _arg3);
    }


    // ----------------------------------------------------------------------------

    public static void singleGradientPerturb2(int interpolation, int seed, double perturbAmp, double frequency, double[] v2) {
        double xf = v2[0] * frequency;
        double yf = v2[1] * frequency;

        int x0 = fastFloor(xf);
        int y0 = fastFloor(yf);
        int x1 = x0 + 1;
        int y1 = y0 + 1;

        double xs, ys;
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

        double2 vec0 = CELL_2D[hash256(x0, y0, seed)];
        double2 vec1 = CELL_2D[hash256(x1, y0, seed)];

        double lx0x = lerp(vec0.x, vec1.x, xs);
        double ly0x = lerp(vec0.y, vec1.y, xs);

        vec0 = CELL_2D[hash256(x0, y1, seed)];
        vec1 = CELL_2D[hash256(x1, y1, seed)];

        double lx1x = lerp(vec0.x, vec1.x, xs);
        double ly1x = lerp(vec0.y, vec1.y, xs);

        v2[0] += lerp(lx0x, lx1x, ys) * perturbAmp;
        v2[1] += lerp(ly0x, ly1x, ys) * perturbAmp;
    }

    public static void singleGradientPerturb3(int interpolation, int seed, double perturbAmp, double frequency, double[] v3) {
        double xf = v3[0] * frequency;
        double yf = v3[1] * frequency;
        double zf = v3[2] * frequency;

        int x0 = fastFloor(xf);
        int y0 = fastFloor(yf);
        int z0 = fastFloor(zf);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;

        double xs, ys, zs;
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

        double3 vec0 = CELL_3D[hash256(x0, y0, z0, seed)];
        double3 vec1 = CELL_3D[hash256(x1, y0, z0, seed)];

        double lx0x = lerp(vec0.x, vec1.x, xs);
        double ly0x = lerp(vec0.y, vec1.y, xs);
        double lz0x = lerp(vec0.z, vec1.z, xs);

        vec0 = CELL_3D[hash256(x0, y1, z0, seed)];
        vec1 = CELL_3D[hash256(x1, y1, z0, seed)];

        double lx1x = lerp(vec0.x, vec1.x, xs);
        double ly1x = lerp(vec0.y, vec1.y, xs);
        double lz1x = lerp(vec0.z, vec1.z, xs);

        double lx0y = lerp(lx0x, lx1x, ys);
        double ly0y = lerp(ly0x, ly1x, ys);
        double lz0y = lerp(lz0x, lz1x, ys);

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

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, BASE_OFFSET, BASE_H, BASE_OCTAVES, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, BASE_SEED_VARIATION);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double offset, double H, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, offset, H, BASE_OCTAVES, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double offset, double H, int octaves, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, offset, H, octaves, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double offset, double H, int octaves, double frequency, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, offset, H, octaves, frequency, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double offset, double H, int octaves, double frequency, double lacunarity, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, offset, H, octaves, frequency, lacunarity, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, offset, H, octaves, frequency, lacunarity, gain, _vseed);
    }

    // ----------------------------------------------------------------------------

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, BASE_OFFSET, BASE_H, BASE_OCTAVES, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, BASE_SEED_VARIATION);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double offset, double H, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, offset, H, BASE_OCTAVES, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double offset, double H, int octaves, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, offset, H, octaves, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double offset, double H, int octaves, double frequency, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, offset, H, octaves, frequency, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double offset, double H, int octaves, double frequency, double lacunarity, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, offset, H, octaves, frequency, lacunarity, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, offset, H, octaves, frequency, lacunarity, gain, _vseed);
    }

    // ----------------------------------------------------------------------------

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, BASE_OFFSET, BASE_H, BASE_OCTAVES, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, BASE_SEED_VARIATION);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double offset, double H, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, offset, H, BASE_OCTAVES, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double offset, double H, int octaves, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, offset, H, octaves, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double offset, double H, int octaves, double frequency, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, offset, H, octaves, frequency, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double offset, double H, int octaves, double frequency, double lacunarity, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, offset, H, octaves, frequency, lacunarity, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, offset, H, octaves, frequency, lacunarity, gain, _vseed);
    }

    // ----------------------------------------------------------------------------

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double u)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, u, BASE_OFFSET, BASE_H, BASE_OCTAVES, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, BASE_SEED_VARIATION);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double u, double offset, double H, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, u, offset, H, BASE_OCTAVES, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double u, double offset, double H, int octaves, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, u, offset, H, octaves, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double u, double offset, double H, int octaves, double frequency, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, u, offset, H, octaves, frequency, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double u, double offset, double H, int octaves, double frequency, double lacunarity, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, u, offset, H, octaves, frequency, lacunarity, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double u, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, u, offset, H, octaves, frequency, lacunarity, gain, _vseed);
    }

    // ----------------------------------------------------------------------------

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double u, double v)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, u, v, BASE_OFFSET, BASE_H, BASE_OCTAVES, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, BASE_SEED_VARIATION);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double u, double v, double offset, double H, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, u, v, offset, H, BASE_OCTAVES, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double u, double v, double offset, double H, int octaves, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, u, v, offset, H, octaves, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double u, double v, double offset, double H, int octaves, double frequency, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, u, v, offset, H, octaves, frequency, BASE_LACUNARITY, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double u, double v, double offset, double H, int octaves, double frequency, double lacunarity, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, u, v, offset, H, octaves, frequency, lacunarity, BASE_GAIN, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, int seed, double x, double y, double z, double w, double u, double v, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        return fractalByType(_ftype, _ntype, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, seed, x, y, z, w, u, v, offset, H, octaves, frequency, lacunarity, gain, _vseed);
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, double _harshness, double mutation, double foamSharpness,  int seed, double x, double y, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        switch(_ftype)
        {
            case F_0NONE:
                return singleNoiseByType(_ntype, _harshness, mutation, foamSharpness, seed, x*frequency, y*frequency) + offset;
            case F_0VERSAMPLE:
                return f_sample(_ntype, x, y, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_BILLOW:
                return f_billow(_ntype, x, y, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
           case F_MULTI:
                return f_multi(_ntype, x, y, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_RIDGED:
                return f_ridged_multi(_ntype, x, y, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_DISTORT:
                return f_distort(_ntype, x, y, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_HETERO:
                return f_musgrave_hetero_terrain(_ntype, x, y, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_HYBRID:
                return f_musgrave_hybrid_terrain(_ntype, x, y, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_FBM2:
                return f_musgrave(_ntype, x, y, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_FBM:
            default:
                return f_brownian_motion(_ntype, x, y, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
        }
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, double _harshness, double mutation, double foamSharpness,  int seed, double x, double y, double z, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        switch(_ftype)
        {
            case F_0NONE:
                return singleNoiseByType(_ntype, _harshness, mutation, foamSharpness, seed, x*frequency, y*frequency, z*frequency) + offset;
            case F_0VERSAMPLE:
                return f_sample(_ntype, x, y, z, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_BILLOW:
                return f_billow(_ntype, x, y, z, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_MULTI:
                return f_multi(_ntype, x, y, z, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_RIDGED:
                return f_ridged_multi(_ntype, x, y, z, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_DISTORT:
                return f_distort(_ntype, x, y, z, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_HETERO:
                return f_musgrave_hetero_terrain(_ntype, x, y, z, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_FBM2:
                return f_musgrave(_ntype, x, y, z, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_FBM:
            default:
                return f_brownian_motion(_ntype, x, y, z, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
        }
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, double _harshness, double mutation, double foamSharpness,  int seed, double x, double y, double z, double w, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        switch(_ftype)
        {
            case F_0NONE:
                return singleNoiseByType(_ntype, _harshness, mutation, foamSharpness, seed, x*frequency, y*frequency, z*frequency, w*frequency) + offset;
            case F_0VERSAMPLE:
                return f_sample(_ntype, x, y, z, w, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_BILLOW:
                return f_billow(_ntype, x, y, z, w, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_MULTI:
                return f_multi(_ntype, x, y, z, w, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_RIDGED:
                return f_ridged_multi(_ntype, x, y, z, w, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_DISTORT:
                return f_distort(_ntype, x, y, z, w, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_HETERO:
                return f_musgrave_hetero_terrain(_ntype, x, y, z, w, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_FBM2:
                return f_musgrave(_ntype, x, y, z, w, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_FBM:
            default:
                return f_brownian_motion(_ntype, x, y, z, w, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
        }
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, double _harshness, double mutation, double foamSharpness,  int seed, double x, double y, double z, double w, double u, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        switch(_ftype)
        {
            case F_0NONE:
                return singleNoiseByType(_ntype, _harshness, mutation, foamSharpness, seed, x*frequency, y*frequency, z*frequency, w*frequency, u*frequency) + offset;
            case F_0VERSAMPLE:
                return f_sample(_ntype, x, y, z, w, u, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_BILLOW:
                return f_billow(_ntype, x, y, z, w, u, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_MULTI:
                return f_multi(_ntype, x, y, z, w, u, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_RIDGED:
                return f_ridged_multi(_ntype, x, y, z, w, u, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_DISTORT:
                return f_distort(_ntype, x, y, z, w, u, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_HETERO:
                return f_musgrave_hetero_terrain(_ntype, x, y, z, w, u, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_FBM2:
                return f_musgrave(_ntype, x, y, z, w, u, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_FBM:
            default:
                return f_brownian_motion(_ntype, x, y, z, w, u, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
        }
    }

    public static double fractalByType(FractalType _ftype, NoiseType _ntype, double _harshness, double mutation, double foamSharpness,  int seed, double x, double y, double z, double w, double u, double v, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        switch(_ftype)
        {
            case F_0NONE:
                return singleNoiseByType(_ntype, _harshness, mutation, foamSharpness, seed, x*frequency, y*frequency, z*frequency, w*frequency, u*frequency, v*frequency) + offset;
            case F_0VERSAMPLE:
                return f_sample(_ntype, x, y, z, w, u, v, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_BILLOW:
                return f_billow(_ntype, x, y, z, w, u, v, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_MULTI:
                return f_multi(_ntype, x, y, z, w, u, v, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_RIDGED:
                return f_ridged_multi(_ntype, x, y, z, w, u, v, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_DISTORT:
                return f_distort(_ntype, x, y, z, w, u, v, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_HETERO:
                return f_musgrave_hetero_terrain(_ntype, x, y, z, w, u, v, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_FBM2:
                return f_musgrave(_ntype, x, y, z, w, u, v, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
            case F_FBM:
            default:
                return f_brownian_motion(_ntype, x, y, z, w, u, v, seed, offset, H, octaves, frequency, lacunarity, gain, _harshness, mutation, foamSharpness, _vseed);
        }
    }

    // ----------------------------------------------------------------------------

    public static double fractalByTypeAndTransform(FractalType _ftype, NoiseType _ntype, double _harshness, double mutation, double foamSharpness,  TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        double _result = fractalByType(_ftype, _ntype, _harshness, mutation, foamSharpness, seed, x, y, offset, H, octaves, frequency, lacunarity, gain, _vseed);
        return singleTransformUnit(_distort, _result, _arg1, _arg2, _arg3);
    }

    public static double fractalByTypeAndTransform(FractalType _ftype, NoiseType _ntype, double _harshness, double mutation, double foamSharpness,  TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y, double z, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        double _result = fractalByType(_ftype, _ntype, _harshness, mutation, foamSharpness, seed, x, y, z, offset, H, octaves, frequency, lacunarity, gain, _vseed);
        return singleTransformUnit(_distort, _result, _arg1, _arg2, _arg3);
    }

    public static double fractalByTypeAndTransform(FractalType _ftype, NoiseType _ntype, double _harshness, double mutation, double foamSharpness,  TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y, double z, double w, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        double _result = fractalByType(_ftype, _ntype, _harshness, mutation, foamSharpness, seed, x, y, z, w, offset, H, octaves, frequency, lacunarity, gain, _vseed);
        return singleTransformUnit(_distort, _result, _arg1, _arg2, _arg3);
    }

    public static double fractalByTypeAndTransform(FractalType _ftype, NoiseType _ntype, double _harshness, double mutation, double foamSharpness,  TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y, double z, double w, double u, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        double _result = fractalByType(_ftype, _ntype, _harshness, mutation, foamSharpness, seed, x, y, z, w, u, offset, H, octaves, frequency, lacunarity, gain, _vseed);
        return singleTransformUnit(_distort, _result, _arg1, _arg2, _arg3);
    }

    public static double fractalByTypeAndTransform(FractalType _ftype, NoiseType _ntype, double _harshness, double mutation, double foamSharpness,  TransformType _distort, double _arg1, double _arg2, double _arg3, int seed, double x, double y, double z, double w, double u, double v, double offset, double H, int octaves, double frequency, double lacunarity, double gain, boolean _vseed)
    {
        double _result = fractalByType(_ftype, _ntype, _harshness, mutation, foamSharpness, seed, x, y, z, w, u, v, offset, H, octaves, frequency, lacunarity, gain, _vseed);
        return singleTransformUnit(_distort, _result, _arg1, _arg2, _arg3);
    }

    // ----------------------------------------------------------------------------

    static double F_SAMPLE_DECAY = 0.36f;

    public static double f_sample(NoiseType type, double x, double y,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;

        double amp = 2f * (double) Math.pow(lacunarity, -H);
        double sum = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y) * .5f;
        for (int i = 0; i < octaves; i++) {
            lacunarity *= F_SAMPLE_DECAY;
            x *= lacunarity;
            y *= lacunarity;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y) * amp;
            amp *= gain;
        }
        return sum;
    }

    public static double f_sample(NoiseType type, double x, double y, double z,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;

        double amp = 2f * (double) Math.pow(lacunarity, -H);
        double sum = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z) * .5f;
        for (int i = 0; i < octaves; i++) {
            lacunarity *= F_SAMPLE_DECAY;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z) * amp;
            amp *= gain;
        }
        return sum;
    }

    public static double f_sample(NoiseType type, double x, double y, double z, double w,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;

        double amp = 2f * (double) Math.pow(lacunarity, -H);
        double sum = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w) * .5f;
        for (int i = 0; i < octaves; i++) {
            lacunarity *= F_SAMPLE_DECAY;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w) * amp;
            amp *= gain;
        }
        return sum;
    }

    public static double f_sample(NoiseType type, double x, double y, double z, double w, double u,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;

        double amp = 2f * (double) Math.pow(lacunarity, -H);
        double sum = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w, u) * .5f;
        for (int i = 0; i < octaves; i++) {
            lacunarity *= F_SAMPLE_DECAY;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u) * amp;
            amp *= gain;
        }
        return sum;
    }

    public static double f_sample(NoiseType type, double x, double y, double z, double w, double u, double v,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        double amp = 2f * (double) Math.pow(lacunarity, -H);
        double sum = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w, u, v) * .5f;
        for (int i = 0; i < octaves; i++) {
            lacunarity *= F_SAMPLE_DECAY;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            v *= lacunarity;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u, v) * amp;
            amp *= gain;
        }
        return sum;
    }

    // ----------------------------------------------------------------------------
    public static double f_ridged_multi(NoiseType type, double x, double y,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;

        double sum = 0f, exp = 2f, correction = 0f, spike;
        for (int i = 0; i < octaves; i++) {
            spike = 1f - Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y));
            correction += (exp *= 0.5);
            sum += spike * exp;
            x *= lacunarity;
            y *= lacunarity;
        }
        return sum * 2f / correction - 1f;
    }

    public static double f_ridged_multi(NoiseType type, double x, double y, double z,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;

        double sum = 0f, exp = 2f, correction = 0f, spike;
        for (int i = 0; i < octaves; i++) {
            spike = 1f - Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z));
            correction += (exp *= 0.5);
            sum += spike * exp;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
        }
        return sum * 2f / correction - 1f;
    }

    public static double f_ridged_multi(NoiseType type, double x, double y, double z, double w,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;

        double sum = 0f, exp = 2f, correction = 0f, spike;
        for (int i = 0; i < octaves; i++) {
            spike = 1f - Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w));
            correction += (exp *= 0.5);
            sum += spike * exp;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
        }
        return sum * 2f / correction - 1f;
    }

    public static double f_ridged_multi(NoiseType type, double x, double y, double z, double w, double u,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;

        double sum = 0f, exp = 2f, correction = 0f, spike;
        for (int i = 0; i < octaves; i++) {
            spike = 1f - Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w, u));
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

    public static double f_ridged_multi(NoiseType type, double x, double y, double z, double w, double u, double v,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        double sum = 0f, exp = 2f, correction = 0f, spike;
        for (int i = 0; i < octaves; i++) {
            spike = 1f - Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w, u, v));
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

    public static double f_multi(NoiseType type, double x, double y,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;

        double sum = 0f, correction = 0f;
        for (int i = 0; i < octaves; i++) {
            correction += gain;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y) * gain;
            x *= lacunarity;
            y *= lacunarity;
        }
        return sum/correction;
    }

    public static double f_multi(NoiseType type, double x, double y, double z,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;

        double sum = 0f, correction = 0f;
        for (int i = 0; i < octaves; i++) {
            correction += gain;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z) * gain;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
        }
        return sum/correction;
    }

    public static double f_multi(NoiseType type, double x, double y, double z, double w,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;

        double sum = 0f, correction = 0f;
        for (int i = 0; i < octaves; i++) {
            correction += gain;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w) * gain;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
        }
        return sum/correction;
    }

    public static double f_multi(NoiseType type, double x, double y, double z, double w, double u,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;

        double sum = 0f, correction = 0f;
        for (int i = 0; i < octaves; i++) {
            correction += gain;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w, u) * gain;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
        }
        return sum/correction;
    }

    public static double f_multi(NoiseType type, double x, double y, double z, double w, double u, double v,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        double sum = 0f, correction = 0f;
        for (int i = 0; i < octaves; i++) {
            correction += gain;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w, u, v) * gain;
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
     * @return noise as a double between -1f and 1f
     */
    public static double f_brownian_motion(NoiseType type, double x, double y,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;

        double sum = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y);
        double amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;

            amp *= gain;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y) * amp;
        }
        amp = gain;
        double ampFractal = 1;
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
     * @return noise as a double between -1f and 1f
     */
    public static double f_brownian_motion(NoiseType type, double x, double y, double z,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;

        double sum = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z);
        double amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;

            amp *= gain;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z) * amp;
        }
        amp = gain;
        double ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }
        return sum / ampFractal;
    }

    public static double fBM3D(NoiseType type, double x, double y, double z,  int seed, boolean _vseed)
    {
        return f_brownian_motion(type, x, y, z, seed, BASE_OFFSET, BASE_H, BASE_OCTAVES, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, _vseed);
    }

    public static double fBM3D(NoiseType type, double x, double y, double z,  int seed, double offset, double H, int octaves, boolean _vseed)
    {
        return f_brownian_motion(type, x, y, z, seed, offset, H, octaves, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, _vseed);
    }
    // ----------------------------------------------------------------------------

    public static double f_brownian_motion(NoiseType type, double x, double y, double z, double w,
        int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness,
        double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;

        double sum = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w);
        double amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;

            amp *= gain;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w) * amp;
        }
        amp = gain;
        double ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }
        return sum / ampFractal;
    }

    // ----------------------------------------------------------------------------

    public static double f_brownian_motion(NoiseType type, double x, double y, double z, double w, double u,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;

        double sum = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w, u);
        double amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;

            amp *= gain;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u) * amp;
        }
        amp = gain;
        double ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }
        return sum / ampFractal;
    }

    // ----------------------------------------------------------------------------

    public static double f_brownian_motion(NoiseType type, double x, double y, double z, double w, double u, double v,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        double sum = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w, u, v);
        double amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            v *= lacunarity;

            amp *= gain;
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u, v) * amp;
        }
        amp = gain;
        double ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }
        return sum / ampFractal;
    }

    // ----------------------------------------------------------------------------
    // https://github.com/blender/blender/blob/594f47ecd2d5367ca936cf6fc6ec8168c2b360d0/intern/cycles/kernel/svm/svm_fractal_noise.h
    // https://github.com/blender/blender/blob/594f47ecd2d5367ca936cf6fc6ec8168c2b360d0/intern/cycles/kernel/svm/svm_musgrave.h

    public static double f_musgrave(NoiseType type, double x, double y,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;

        double value = 0.0f;
        double pwr = 1.0f;
        double pwHL = (double) Math.pow(lacunarity, -H);

        for (int i = 0; i < octaves; i++) {
            value += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y) * pwr;
            pwr *= pwHL;
            x *= lacunarity;
            y *= lacunarity;
        }

        return value;
    }

    // ----------------------------------------------------------------------------

    public static double f_musgrave(NoiseType type, double x, double y, double z,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;

        double value = 0.0f;
        double pwr = 1.0f;
        double pwHL = (double) Math.pow(lacunarity, -H);

        for (int i = 0; i < octaves; i++) {
            value += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z) * pwr;
            pwr *= pwHL;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
        }

        return value;
    }

    // ----------------------------------------------------------------------------

    public static double f_musgrave(NoiseType type, double x, double y, double z, double w,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;

        double value = 0.0f;
        double pwr = 1.0f;
        double pwHL = (double) Math.pow(lacunarity, -H);

        for (int i = 0; i < octaves; i++) {
            value += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w) * pwr;
            pwr *= pwHL;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
        }

        return value;
    }

    // ----------------------------------------------------------------------------

    public static double f_musgrave(NoiseType type, double x, double y, double z, double w, double u,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;

        double value = 0.0f;
        double pwr = 1.0f;
        double pwHL = (double) Math.pow(lacunarity, -H);

        for (int i = 0; i < octaves; i++) {
            value += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u) * pwr;
            pwr *= pwHL;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
        }

        return value;
    }

    // ----------------------------------------------------------------------------

    public static double f_musgrave(NoiseType type, double x, double y, double z, double w, double u, double v,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        double value = 0.0f;
        double pwr = 1.0f;
        double pwHL = (double) Math.pow(lacunarity, -H);

        for (int i = 0; i < octaves; i++) {
            value += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u, v) * pwr;
            pwr *= pwHL;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            v *= lacunarity;
        }

        return value;
    }

    // ----------------------------------------------------------------------------
    public static double f_oneil(NoiseType type, double x, double y, double z, double w, double u, double v,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        double value = 0.0f;
        double pwr = 1.0f;
        double pwHL = (double) Math.pow(lacunarity, -H);

        for (int i = 0; i < octaves; i++) {
            value += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u, v) * pwr;
            pwr *= pwHL;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            v *= lacunarity;
        }

        return value;
    }

    // ----------------------------------------------------------------------------

    public static double f_musgrave_hetero_terrain(NoiseType type, double x, double y,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;

        double pwr = 1.0f;
        double pwHL = (double) Math.pow(lacunarity, -H);

        double value = 1f;

        for (int i = 0; i < octaves; i++)
        {
            value += (singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y) + offset) * pwr * value;
            pwr *= pwHL;
            x *= lacunarity;
            y *= lacunarity;
        }

        return value;
    }

    // ----------------------------------------------------------------------------

    public static double f_musgrave_hetero_terrain(NoiseType type, double x, double y, double z,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;

        double pwr = 1.0f;
        double pwHL = (double) Math.pow(lacunarity, -H);

        double value = 1f;

        for (int i = 0; i < octaves; i++)
        {
            value += (singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z) + offset) * pwr * value;
            pwr *= pwHL;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
        }

        return value;
    }

    // ----------------------------------------------------------------------------

    public static double f_musgrave_hetero_terrain(NoiseType type, double x, double y, double z, double w,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;

        double pwr = 1.0f;
        double pwHL = (double) Math.pow(lacunarity, -H);

        double value = 1f;

        for (int i = 0; i < octaves; i++)
        {
            value += (singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w) + offset) * pwr * value;
            pwr *= pwHL;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
        }

        return value;
    }

    // ----------------------------------------------------------------------------

    public static double f_musgrave_hetero_terrain(NoiseType type, double x, double y, double z, double w, double u,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;

        double pwr = 1.0f;
        double pwHL = (double) Math.pow(lacunarity, -H);

        double value = 1f;

        for (int i = 0; i < octaves; i++)
        {
            value += (singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u) + offset) * pwr * value;
            pwr *= pwHL;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
        }

        return value;
    }

    // ----------------------------------------------------------------------------

    public static double f_musgrave_hetero_terrain(NoiseType type, double x, double y, double z, double w, double u, double v,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        double pwr = 1.0f;
        double pwHL = (double) Math.pow(lacunarity, -H);

        double value = 1f;

        for (int i = 0; i < octaves; i++)
        {
            value += (singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u, v) + offset) * pwr * value;
            pwr *= pwHL;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            v *= lacunarity;
        }

        return value;
    }

    // ----------------------------------------------------------------------------

    public static double f_musgrave_hybrid_terrain(NoiseType type, double x, double y,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;

        double pwHL = (double) Math.pow(lacunarity, -H);
        double pwr = pwHL;

        double value = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y) + offset;
        double weight = gain * value;
        x *= lacunarity;
        y *= lacunarity;

        for (int i = 1; (weight > 0.001f) && (i < octaves); i++)
        {
            if (weight > 1.0f) {
                weight = 1.0f;
            }
            double signal = (singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y) + offset) * pwr;
            pwr *= pwHL;
            value += weight * signal;
            weight *= gain * signal;

            x *= lacunarity;
            y *= lacunarity;
        }

        return value;
    }

    // ----------------------------------------------------------------------------

    public static double f_musgrave_hybrid_terrain(NoiseType type, double x, double y, double z, double w, double u, double v,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        double pwHL = (double) Math.pow(lacunarity, -H);
        double pwr = pwHL;

        double value = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w, u, v) + offset;
        double weight = gain * value;
        x *= lacunarity;
        y *= lacunarity;
        z *= lacunarity;
        w *= lacunarity;
        u *= lacunarity;
        v *= lacunarity;

        for (int i = 1; (weight > 0.001f) && (i < octaves); i++)
        {
            if (weight > 1.0f) {
                weight = 1.0f;
            }
            double signal = (singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u, v) + offset) * pwr;
            pwr *= pwHL;
            value += weight * signal;
            weight *= gain * signal;

            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            v *= lacunarity;
        }

        return value;
    }

    // ----------------------------------------------------------------------------

    public static double f_musgrave_multi_fractal(NoiseType type, double x, double y, double z, double w, double u, double v,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        double value = 1.0f;
        double pwr = 1.0f;
        double pwHL = (double) Math.pow(lacunarity, -H);

        for (int i = 0; i < octaves; i++) {
            value *= ((pwr * singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u, v)) + 1.0f);
            pwr *= pwHL;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            v *= lacunarity;
        }

        return value;
    }

    // ----------------------------------------------------------------------------

    public static double f_billow(NoiseType type, double x, double y,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;

        double sum = ((Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y)) * 2) - 1);
        double amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;

            amp *= gain;
            sum += ((Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y)) * 2) - 1) * amp;
        }

        amp = gain;
        double ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }

        return sum / ampFractal;
    }

    // ----------------------------------------------------------------------------

    public static double f_billow(NoiseType type, double x, double y, double z,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;

        double sum = ((Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z)) * 2) - 1);
        double amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;

            amp *= gain;
            sum += ((Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z)) * 2) - 1) * amp;
        }

        amp = gain;
        double ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }

        return sum / ampFractal;
    }

    // ----------------------------------------------------------------------------

    public static double f_billow(NoiseType type, double x, double y, double z, double w,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;

        double sum = ((Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w)) * 2) - 1);
        double amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;

            amp *= gain;
            sum += ((Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w)) * 2) - 1) * amp;
        }

        amp = gain;
        double ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }

        return sum / ampFractal;
    }

    // ----------------------------------------------------------------------------

    public static double f_billow(NoiseType type, double x, double y, double z, double w, double u,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;

        double sum = ((Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w, u)) * 2) - 1);
        double amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;

            amp *= gain;
            sum += ((Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u)) * 2) - 1) * amp;
        }

        amp = gain;
        double ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }

        return sum / ampFractal;
    }

    // ----------------------------------------------------------------------------

    public static double f_billow(NoiseType type, double x, double y, double z, double w, double u, double v,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        double sum = ((Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed, x, y, z, w, u, v)) * 2) - 1);
        double amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            v *= lacunarity;

            amp *= gain;
            sum += ((Math.abs(singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x, y, z, w, u, v)) * 2) - 1) * amp;
        }

        amp = gain;
        double ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }

        return sum / ampFractal;
    }

    // ----------------------------------------------------------------------------

    public static double f_distort(NoiseType type, double x, double y,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;

        double sum = 0;
        double amp = 1f;
        double max = 1f;

        double _dx = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED1, x, y);
        double _dy = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED2, y, x);
        for (int i = 0; i < octaves; i++) {
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x+_dx, y-_dy) * amp;
            sum -= singleNoiseByType(type, _harshness, mutation, foamSharpness, 0x123-(seed+(_vseed ? i : 0)), x-_dx, y+_dy) * amp;
            x *= lacunarity;
            y *= lacunarity;
            amp *= gain;
            max += amp;
        }
        return sum / max;
    }

    // ----------------------------------------------------------------------------

    public static double f_distort(NoiseType type, double x, double y, double z,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;

        double sum = 0;
        double amp = 1f;
        double max = 1f;

        double _dx = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED1, x, y, z);
        double _dy = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED2, y, z, x);
        double _dz = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED3, z, x, y);
        for (int i = 0; i < octaves; i++) {
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x+_dx, y-_dy, z+_dz) * amp;
            sum -= singleNoiseByType(type, _harshness, mutation, foamSharpness, 0x123-(seed+(_vseed ? i : 0)), x-_dx, y+_dy, z-_dz) * amp;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            amp *= gain;
            max += amp;
        }
        return sum / max;
    }

    // ----------------------------------------------------------------------------

    public static double f_distort(NoiseType type, double x, double y, double z, double w,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;

        double sum = 0;
        double amp = 1f;
        double max = 1f;

        double _dx = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED1, x, y, z, w);
        double _dy = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED2, y, z, w, x);
        double _dz = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED3, z, w, x, y);
        double _dw = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED1, w, x, y, z);
        for (int i = 0; i < octaves; i++) {
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x+_dx, y-_dy, z+_dz, w-_dw) * amp;
            sum -= singleNoiseByType(type, _harshness, mutation, foamSharpness, 0x123-(seed+(_vseed ? i : 0)), x-_dx, y+_dy, z-_dz, w+_dw) * amp;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            amp *= gain;
            max += amp;
        }
        return sum / max;
    }

    // ----------------------------------------------------------------------------

    public static double f_distort(NoiseType type, double x, double y, double z, double w, double u,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;

        double sum = 0;
        double amp = 1f;
        double max = 1f;

        double _dx = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED1, x, y, z, w, u);
        double _dy = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED2, y, z, w, u, x);
        double _dz = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED3, z, w, u, x, y);
        double _dw = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED1, w, u, x, y, z);
        double _du = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED2, u, x, y, z, w);
        for (int i = 0; i < octaves; i++) {
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x+_dx, y-_dy, z+_dz, w-_dw, u+_du) * amp;
            sum -= singleNoiseByType(type, _harshness, mutation, foamSharpness, 0x123-(seed+(_vseed ? i : 0)), x-_dx, y+_dy, z-_dz, w+_dw, u-_du) * amp;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            amp *= gain;
            max += amp;
        }
        return sum / max;
    }

    // ----------------------------------------------------------------------------

    public static double f_distort(NoiseType type, double x, double y, double z, double w, double u, double v,  int seed, double offset, double H, int octaves, double frequency, double lacunarity, double gain, double _harshness, double mutation, double foamSharpness, boolean _vseed)
    {
        x *= frequency;
        y *= frequency;
        z *= frequency;
        w *= frequency;
        u *= frequency;
        v *= frequency;

        double sum = 0;
        double amp = 1f;
        double max = 1f;

        double _dx = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED1, x, y, z, w, u, v);
        double _dy = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED2, y, z, w, u, v, x);
        double _dz = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED3, z, w, u, v, x, y);
        double _dw = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED1, w, u, v, x, y, z);
        double _du = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED2, u, v, x, y, z, w);
        double _dv = singleNoiseByType(type, _harshness, mutation, foamSharpness, seed^BASE_SEED3, v, x, y, z, w, u);
        for (int i = 0; i < octaves; i++) {
            sum += singleNoiseByType(type, _harshness, mutation, foamSharpness, seed+(_vseed ? i : 0), x+_dx, y-_dy, z+_dz, w-_dw, u+_du, v-_dv) * amp;
            sum -= singleNoiseByType(type, _harshness, mutation, foamSharpness, 0x123-(seed+(_vseed ? i : 0)), x+_dx, y-_dy, z+_dz, w-_dw, u+_du, v-_dv) * amp;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            w *= lacunarity;
            u *= lacunarity;
            v *= lacunarity;
            amp *= gain;
            max += amp;
        }
        return sum / max;
    }


    // ----------------------------------------------------------------------------

    public static enum FractalType
    {
        F_0NONE,
        F_0VERSAMPLE,
        F_DISTORT,
        F_BILLOW,
        F_MULTI,
        F_RIDGED,
        F_HETERO,
        F_HYBRID,
        F_FBM,
        F_FBM2;
    }

    public static enum TransformType
    {
        T_0NONE,
        T_SINE,
        T_COSINE,
        T_SINE_2,
        T_COSINE_2,
        T_SQ_SINE,
        T_SQ_COSINE,
        T_SQUARE_ROOT,
        T_CUBE_ROOT,
        T_SQUARE,
        T_CUBE,
        T_QUART,
        T_ABS,
        T_ABS1M,
        T_INVERT,
        T_EXP,
        T_EX,
        T_HERMITESPLINE,
        T_QUINTICSPLINE,
        T_BARRONSPLINE, T_BINARY;
    }

    public static enum NoiseType
    {
        //BLUE,
        //GREY,
        SOLID,
        SIMPLEX,
        SIMPLEX_LUMP,
        PERLIN_LINEAR,
        PERLIN_HERMITE,
        PERLIN_QUINTIC,

        MUTANT_LINEAR,
        MUTANT_HERMITE,
        MUTANT_QUINTIC,

        MUTANT_NORMAL_LINEAR,
        MUTANT_NORMAL_HERMITE,
        MUTANT_NORMAL_QUINTIC,

        PYRAMID_LINEAR,
        PYRAMID_HERMITE,
        PYRAMID_QUINTIC,

        HONEY_LINEAR,
        HONEY_HERMITE,
        HONEY_QUINTIC,

        FOAM_LINEAR,
        FOAM_HERMITE,
        FOAM_QUINTIC,

        RIPPLE_LINEAR,
        RIPPLE_HERMITE,
        RIPPLE_QUINTIC,

        VALUE_LINEAR,
        VALUE_HERMITE,
        VALUE_QUINTIC,

        CUBIC,
        WHITE,
        WHITE_NORMAL,
        BLUR,

        CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2,
        CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2_ADD,
        CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2_SUB,
        CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2_MUL,
        CELLULAR2EDGE_EUCLIDEAN_DISTANCE_2_DIV,
        CELLULAR2EDGE_MANHATTAN_DISTANCE_2,
        CELLULAR2EDGE_MANHATTAN_DISTANCE_2_ADD,
        CELLULAR2EDGE_MANHATTAN_DISTANCE_2_SUB,
        CELLULAR2EDGE_MANHATTAN_DISTANCE_2_MUL,
        CELLULAR2EDGE_MANHATTAN_DISTANCE_2_DIV,
        CELLULAR2EDGE_NATURAL_DISTANCE_2,
        CELLULAR2EDGE_NATURAL_DISTANCE_2_ADD,
        CELLULAR2EDGE_NATURAL_DISTANCE_2_SUB,
        CELLULAR2EDGE_NATURAL_DISTANCE_2_MUL,
        CELLULAR2EDGE_NATURAL_DISTANCE_2_DIV,

        CELLULAR_EUCLIDEAN_CELL_VALUE,
        CELLULAR_EUCLIDEAN_NOISE_LOOKUP,
        CELLULAR_EUCLIDEAN_DISTANCE,
        CELLULAR_MANHATTAN_CELL_VALUE,
        CELLULAR_MANHATTAN_NOISE_LOOKUP,
        CELLULAR_MANHATTAN_DISTANCE,
        CELLULAR_NATURAL_CELL_VALUE,
        CELLULAR_NATURAL_NOISE_LOOKUP,
        CELLULAR_NATURAL_DISTANCE,

        CELLULAR_MERGE_EUCLIDEAN,
        CELLULAR_MERGE_MANHATTAN,
        CELLULAR_MERGE_NATURAL,

        SUPERFOAM_CELLULAR2EDGE_MANHATTAN_DISTANCE_2,
        SUPERFOAM_CELLULAR_EUCLIDEAN_CELL_VALUE,
        SUPERFOAM_CELLULAR_NATURAL_DISTANCE,
        SUPERFOAM_HONEY_LINEAR,
        SUPERFOAM_HONEY_HERMITE,
        SUPERFOAM_HONEY_QUINTIC,
        SUPERFOAM_MUTANT_NORMAL_LINEAR,
        SUPERFOAM_MUTANT_NORMAL_HERMITE,
        SUPERFOAM_MUTANT_NORMAL_QUINTIC,
        SUPERFOAM_PYRAMID_LINEAR,
        SUPERFOAM_PYRAMID_HERMITE,
        SUPERFOAM_PYRAMID_QUINTIC,
        SUPERFOAM_RIPPLE_LINEAR,
        SUPERFOAM_RIPPLE_HERMITE,
        SUPERFOAM_RIPPLE_QUINTIC,

        SUPERFOAM_NORMAL_CELLULAR2EDGE_MANHATTAN_DISTANCE_2,
        SUPERFOAM_NORMAL_CELLULAR_EUCLIDEAN_CELL_VALUE,
        SUPERFOAM_NORMAL_CELLULAR_NATURAL_DISTANCE,
        SUPERFOAM_NORMAL_HONEY_LINEAR,
        SUPERFOAM_NORMAL_HONEY_HERMITE,
        SUPERFOAM_NORMAL_HONEY_QUINTIC,
        SUPERFOAM_NORMAL_MUTANT_NORMAL_LINEAR,
        SUPERFOAM_NORMAL_MUTANT_NORMAL_HERMITE,
        SUPERFOAM_NORMAL_MUTANT_NORMAL_QUINTIC,
        SUPERFOAM_NORMAL_PYRAMID_LINEAR,
        SUPERFOAM_NORMAL_PYRAMID_HERMITE,
        SUPERFOAM_NORMAL_PYRAMID_QUINTIC,
        SUPERFOAM_NORMAL_RIPPLE_LINEAR,
        SUPERFOAM_NORMAL_RIPPLE_HERMITE,
        SUPERFOAM_NORMAL_RIPPLE_QUINTIC,
        ;
    }

    // ----------------------------------------------------------------------------

    public static NoiseType BASE_NOISETYPE = NoiseType.SIMPLEX;
    public static TransformType BASE_TRANSFORM = TransformType.T_0NONE;
    public static int BASE_SEED0 = 0x12345678;
    public static int BASE_SEED1 = 0xdeadbeaf;
    public static int BASE_SEED2 = 0xcafeaffe;
    public static int BASE_SEED3 = 0xd1ceB33f;
    public static int BASE_OCTAVES = 8;
    public static double BASE_MUTATION = 0f;
    public static double BASE_SHARPNESS = 1f;
    public static double BASE_FREQUENCY = 0.03125f;
    public static double BASE_LACUNARITY = 2f;
    public static double BASE_HARSHNESS = (double) HARSH;
    public static double BASE_H = 1.9f;
    public static double BASE_GAIN = 0.5f;
    public static double BASE_OFFSET = 0f;
    public static boolean BASE_SEED_VARIATION = false;

}
