package com.github.terefang.jmelange.randfractal.lite;

public class FnCubic extends FastNoiseLite
{
    // ----------------------------------------------------------------------------

    private final static double CUBIC_2D_BOUNDING = 1 / 2.25f;

    public static final double singleCubic(int seed, double x, double y) {
        int x1 = fastFloor(x);
        int y1 = fastFloor(y);

        int x0 = x1 - 1;
        int y0 = y1 - 1;
        int x2 = x1 + 1;
        int y2 = y1 + 1;
        int x3 = x1 + 2;
        int y3 = y1 + 2;

        double xs = x - (double) x1;
        double ys = y - (double) y1;

        return cubicLerp(
                cubicLerp(valCoord2D(seed, x0, y0), valCoord2D(seed, x1, y0), valCoord2D(seed, x2, y0), valCoord2D(seed, x3, y0),
                        xs),
                cubicLerp(valCoord2D(seed, x0, y1), valCoord2D(seed, x1, y1), valCoord2D(seed, x2, y1), valCoord2D(seed, x3, y1),
                        xs),
                cubicLerp(valCoord2D(seed, x0, y2), valCoord2D(seed, x1, y2), valCoord2D(seed, x2, y2), valCoord2D(seed, x3, y2),
                        xs),
                cubicLerp(valCoord2D(seed, x0, y3), valCoord2D(seed, x1, y3), valCoord2D(seed, x2, y3), valCoord2D(seed, x3, y3),
                        xs),
                ys) * CUBIC_2D_BOUNDING;
    }

    private final static double CUBIC_3D_BOUNDING = 1 / (double) (1.5 * 1.5 * 1.5);

    public static final double singleCubic(int seed, double x, double y, double z) {
        int x1 = fastFloor(x);
        int y1 = fastFloor(y);
        int z1 = fastFloor(z);

        int x0 = x1 - 1;
        int y0 = y1 - 1;
        int z0 = z1 - 1;
        int x2 = x1 + 1;
        int y2 = y1 + 1;
        int z2 = z1 + 1;
        int x3 = x1 + 2;
        int y3 = y1 + 2;
        int z3 = z1 + 2;

        double xs = x - (double) x1;
        double ys = y - (double) y1;
        double zs = z - (double) z1;

        return cubicLerp(
                cubicLerp(
                        cubicLerp(valCoord3D(seed, x0, y0, z0), valCoord3D(seed, x1, y0, z0), valCoord3D(seed, x2, y0, z0), valCoord3D(seed, x3, y0, z0), xs),
                        cubicLerp(valCoord3D(seed, x0, y1, z0), valCoord3D(seed, x1, y1, z0), valCoord3D(seed, x2, y1, z0), valCoord3D(seed, x3, y1, z0), xs),
                        cubicLerp(valCoord3D(seed, x0, y2, z0), valCoord3D(seed, x1, y2, z0), valCoord3D(seed, x2, y2, z0), valCoord3D(seed, x3, y2, z0), xs),
                        cubicLerp(valCoord3D(seed, x0, y3, z0), valCoord3D(seed, x1, y3, z0), valCoord3D(seed, x2, y3, z0), valCoord3D(seed, x3, y3, z0), xs),
                        ys),
                cubicLerp(
                        cubicLerp(valCoord3D(seed, x0, y0, z1), valCoord3D(seed, x1, y0, z1), valCoord3D(seed, x2, y0, z1), valCoord3D(seed, x3, y0, z1), xs),
                        cubicLerp(valCoord3D(seed, x0, y1, z1), valCoord3D(seed, x1, y1, z1), valCoord3D(seed, x2, y1, z1), valCoord3D(seed, x3, y1, z1), xs),
                        cubicLerp(valCoord3D(seed, x0, y2, z1), valCoord3D(seed, x1, y2, z1), valCoord3D(seed, x2, y2, z1), valCoord3D(seed, x3, y2, z1), xs),
                        cubicLerp(valCoord3D(seed, x0, y3, z1), valCoord3D(seed, x1, y3, z1), valCoord3D(seed, x2, y3, z1), valCoord3D(seed, x3, y3, z1), xs),
                        ys),
                cubicLerp(
                        cubicLerp(valCoord3D(seed, x0, y0, z2), valCoord3D(seed, x1, y0, z2), valCoord3D(seed, x2, y0, z2), valCoord3D(seed, x3, y0, z2), xs),
                        cubicLerp(valCoord3D(seed, x0, y1, z2), valCoord3D(seed, x1, y1, z2), valCoord3D(seed, x2, y1, z2), valCoord3D(seed, x3, y1, z2), xs),
                        cubicLerp(valCoord3D(seed, x0, y2, z2), valCoord3D(seed, x1, y2, z2), valCoord3D(seed, x2, y2, z2), valCoord3D(seed, x3, y2, z2), xs),
                        cubicLerp(valCoord3D(seed, x0, y3, z2), valCoord3D(seed, x1, y3, z2), valCoord3D(seed, x2, y3, z2), valCoord3D(seed, x3, y3, z2), xs),
                        ys),
                cubicLerp(
                        cubicLerp(valCoord3D(seed, x0, y0, z3), valCoord3D(seed, x1, y0, z3), valCoord3D(seed, x2, y0, z3), valCoord3D(seed, x3, y0, z3), xs),
                        cubicLerp(valCoord3D(seed, x0, y1, z3), valCoord3D(seed, x1, y1, z3), valCoord3D(seed, x2, y1, z3), valCoord3D(seed, x3, y1, z3), xs),
                        cubicLerp(valCoord3D(seed, x0, y2, z3), valCoord3D(seed, x1, y2, z3), valCoord3D(seed, x2, y2, z3), valCoord3D(seed, x3, y2, z3), xs),
                        cubicLerp(valCoord3D(seed, x0, y3, z3), valCoord3D(seed, x1, y3, z3), valCoord3D(seed, x2, y3, z3), valCoord3D(seed, x3, y3, z3), xs),
                        ys),
                zs) * CUBIC_3D_BOUNDING;
    }

    private final static double CUBIC_4D_BOUNDING = 1f / (1.5f * 1.5f);

    public static final double singleCubic(int seed, double x, double y, double z, double w)
    {
        int x1 = fastFloor(x);
        int y1 = fastFloor(y);
        int z1 = fastFloor(z);
        int w1 = fastFloor(w);

        int x0 = x1 - 1;
        int y0 = y1 - 1;
        int z0 = z1 - 1;
        int w0 = w1 - 1;
        int x2 = x1 + 1;
        int y2 = y1 + 1;
        int z2 = z1 + 1;
        int w2 = w1 + 1;
        int x3 = x1 + 2;
        int y3 = y1 + 2;
        int z3 = z1 + 2;
        int w3 = w1 + 2;

        double xs = x - (double) x1;
        double ys = y - (double) y1;
        double zs = z - (double) z1;
        double ws = w - (double) w1;

        return cubicLerp(
                cubicLerp(
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z0, w0), valCoord4D(seed, x1, y0, z0, w0), valCoord4D(seed, x2, y0, z0, w0), valCoord4D(seed, x3, y0, z0, w0), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z0, w0), valCoord4D(seed, x1, y1, z0, w0), valCoord4D(seed, x2, y1, z0, w0), valCoord4D(seed, x3, y1, z0, w0), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z0, w0), valCoord4D(seed, x1, y2, z0, w0), valCoord4D(seed, x2, y2, z0, w0), valCoord4D(seed, x3, y2, z0, w0), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z0, w0), valCoord4D(seed, x1, y3, z0, w0), valCoord4D(seed, x2, y3, z0, w0), valCoord4D(seed, x3, y3, z0, w0), xs),
                                ys),
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z1, w0), valCoord4D(seed, x1, y0, z1, w0), valCoord4D(seed, x2, y0, z1, w0), valCoord4D(seed, x3, y0, z1, w0), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z1, w0), valCoord4D(seed, x1, y1, z1, w0), valCoord4D(seed, x2, y1, z1, w0), valCoord4D(seed, x3, y1, z1, w0), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z1, w0), valCoord4D(seed, x1, y2, z1, w0), valCoord4D(seed, x2, y2, z1, w0), valCoord4D(seed, x3, y2, z1, w0), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z1, w0), valCoord4D(seed, x1, y3, z1, w0), valCoord4D(seed, x2, y3, z1, w0), valCoord4D(seed, x3, y3, z1, w0), xs),
                                ys),
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z2, w0), valCoord4D(seed, x1, y0, z2, w0), valCoord4D(seed, x2, y0, z2, w0), valCoord4D(seed, x3, y0, z2, w0), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z2, w0), valCoord4D(seed, x1, y1, z2, w0), valCoord4D(seed, x2, y1, z2, w0), valCoord4D(seed, x3, y1, z2, w0), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z2, w0), valCoord4D(seed, x1, y2, z2, w0), valCoord4D(seed, x2, y2, z2, w0), valCoord4D(seed, x3, y2, z2, w0), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z2, w0), valCoord4D(seed, x1, y3, z2, w0), valCoord4D(seed, x2, y3, z2, w0), valCoord4D(seed, x3, y3, z2, w0), xs),
                                ys),
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z3, w0), valCoord4D(seed, x1, y0, z3, w0), valCoord4D(seed, x2, y0, z3, w0), valCoord4D(seed, x3, y0, z3, w0), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z3, w0), valCoord4D(seed, x1, y1, z3, w0), valCoord4D(seed, x2, y1, z3, w0), valCoord4D(seed, x3, y1, z3, w0), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z3, w0), valCoord4D(seed, x1, y2, z3, w0), valCoord4D(seed, x2, y2, z3, w0), valCoord4D(seed, x3, y2, z3, w0), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z3, w0), valCoord4D(seed, x1, y3, z3, w0), valCoord4D(seed, x2, y3, z3, w0), valCoord4D(seed, x3, y3, z3, w0), xs),
                                ys),
                        zs),
                cubicLerp(
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z0, w1), valCoord4D(seed, x1, y0, z0, w1), valCoord4D(seed, x2, y0, z0, w1), valCoord4D(seed, x3, y0, z0, w1), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z0, w1), valCoord4D(seed, x1, y1, z0, w1), valCoord4D(seed, x2, y1, z0, w1), valCoord4D(seed, x3, y1, z0, w1), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z0, w1), valCoord4D(seed, x1, y2, z0, w1), valCoord4D(seed, x2, y2, z0, w1), valCoord4D(seed, x3, y2, z0, w1), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z0, w1), valCoord4D(seed, x1, y3, z0, w1), valCoord4D(seed, x2, y3, z0, w1), valCoord4D(seed, x3, y3, z0, w1), xs),
                                ys),
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z1, w1), valCoord4D(seed, x1, y0, z1, w1), valCoord4D(seed, x2, y0, z1, w1), valCoord4D(seed, x3, y0, z1, w1), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z1, w1), valCoord4D(seed, x1, y1, z1, w1), valCoord4D(seed, x2, y1, z1, w1), valCoord4D(seed, x3, y1, z1, w1), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z1, w1), valCoord4D(seed, x1, y2, z1, w1), valCoord4D(seed, x2, y2, z1, w1), valCoord4D(seed, x3, y2, z1, w1), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z1, w1), valCoord4D(seed, x1, y3, z1, w1), valCoord4D(seed, x2, y3, z1, w1), valCoord4D(seed, x3, y3, z1, w1), xs),
                                ys),
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z2, w1), valCoord4D(seed, x1, y0, z2, w1), valCoord4D(seed, x2, y0, z2, w1), valCoord4D(seed, x3, y0, z2, w1), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z2, w1), valCoord4D(seed, x1, y1, z2, w1), valCoord4D(seed, x2, y1, z2, w1), valCoord4D(seed, x3, y1, z2, w1), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z2, w1), valCoord4D(seed, x1, y2, z2, w1), valCoord4D(seed, x2, y2, z2, w1), valCoord4D(seed, x3, y2, z2, w1), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z2, w1), valCoord4D(seed, x1, y3, z2, w1), valCoord4D(seed, x2, y3, z2, w1), valCoord4D(seed, x3, y3, z2, w1), xs),
                                ys),
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z3, w1), valCoord4D(seed, x1, y0, z3, w1), valCoord4D(seed, x2, y0, z3, w1), valCoord4D(seed, x3, y0, z3, w1), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z3, w1), valCoord4D(seed, x1, y1, z3, w1), valCoord4D(seed, x2, y1, z3, w1), valCoord4D(seed, x3, y1, z3, w1), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z3, w1), valCoord4D(seed, x1, y2, z3, w1), valCoord4D(seed, x2, y2, z3, w1), valCoord4D(seed, x3, y2, z3, w1), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z3, w1), valCoord4D(seed, x1, y3, z3, w1), valCoord4D(seed, x2, y3, z3, w1), valCoord4D(seed, x3, y3, z3, w1), xs),
                                ys),
                        zs),
                cubicLerp(
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z0, w2), valCoord4D(seed, x1, y0, z0, w2), valCoord4D(seed, x2, y0, z0, w2), valCoord4D(seed, x3, y0, z0, w2), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z0, w2), valCoord4D(seed, x1, y1, z0, w2), valCoord4D(seed, x2, y1, z0, w2), valCoord4D(seed, x3, y1, z0, w2), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z0, w2), valCoord4D(seed, x1, y2, z0, w2), valCoord4D(seed, x2, y2, z0, w2), valCoord4D(seed, x3, y2, z0, w2), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z0, w2), valCoord4D(seed, x1, y3, z0, w2), valCoord4D(seed, x2, y3, z0, w2), valCoord4D(seed, x3, y3, z0, w2), xs),
                                ys),
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z1, w2), valCoord4D(seed, x1, y0, z1, w2), valCoord4D(seed, x2, y0, z1, w2), valCoord4D(seed, x3, y0, z1, w2), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z1, w2), valCoord4D(seed, x1, y1, z1, w2), valCoord4D(seed, x2, y1, z1, w2), valCoord4D(seed, x3, y1, z1, w2), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z1, w2), valCoord4D(seed, x1, y2, z1, w2), valCoord4D(seed, x2, y2, z1, w2), valCoord4D(seed, x3, y2, z1, w2), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z1, w2), valCoord4D(seed, x1, y3, z1, w2), valCoord4D(seed, x2, y3, z1, w2), valCoord4D(seed, x3, y3, z1, w2), xs),
                                ys),
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z2, w2), valCoord4D(seed, x1, y0, z2, w2), valCoord4D(seed, x2, y0, z2, w2), valCoord4D(seed, x3, y0, z2, w2), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z2, w2), valCoord4D(seed, x1, y1, z2, w2), valCoord4D(seed, x2, y1, z2, w2), valCoord4D(seed, x3, y1, z2, w2), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z2, w2), valCoord4D(seed, x1, y2, z2, w2), valCoord4D(seed, x2, y2, z2, w2), valCoord4D(seed, x3, y2, z2, w2), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z2, w2), valCoord4D(seed, x1, y3, z2, w2), valCoord4D(seed, x2, y3, z2, w2), valCoord4D(seed, x3, y3, z2, w2), xs),
                                ys),
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z3, w2), valCoord4D(seed, x1, y0, z3, w2), valCoord4D(seed, x2, y0, z3, w2), valCoord4D(seed, x3, y0, z3, w2), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z3, w2), valCoord4D(seed, x1, y1, z3, w2), valCoord4D(seed, x2, y1, z3, w2), valCoord4D(seed, x3, y1, z3, w2), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z3, w2), valCoord4D(seed, x1, y2, z3, w2), valCoord4D(seed, x2, y2, z3, w2), valCoord4D(seed, x3, y2, z3, w2), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z3, w2), valCoord4D(seed, x1, y3, z3, w2), valCoord4D(seed, x2, y3, z3, w2), valCoord4D(seed, x3, y3, z3, w2), xs),
                                ys),
                        zs),
                cubicLerp(
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z0, w3), valCoord4D(seed, x1, y0, z0, w3), valCoord4D(seed, x2, y0, z0, w3), valCoord4D(seed, x3, y0, z0, w3), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z0, w3), valCoord4D(seed, x1, y1, z0, w3), valCoord4D(seed, x2, y1, z0, w3), valCoord4D(seed, x3, y1, z0, w3), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z0, w3), valCoord4D(seed, x1, y2, z0, w3), valCoord4D(seed, x2, y2, z0, w3), valCoord4D(seed, x3, y2, z0, w3), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z0, w3), valCoord4D(seed, x1, y3, z0, w3), valCoord4D(seed, x2, y3, z0, w3), valCoord4D(seed, x3, y3, z0, w3), xs),
                                ys),
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z1, w3), valCoord4D(seed, x1, y0, z1, w3), valCoord4D(seed, x2, y0, z1, w3), valCoord4D(seed, x3, y0, z1, w3), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z1, w3), valCoord4D(seed, x1, y1, z1, w3), valCoord4D(seed, x2, y1, z1, w3), valCoord4D(seed, x3, y1, z1, w3), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z1, w3), valCoord4D(seed, x1, y2, z1, w3), valCoord4D(seed, x2, y2, z1, w3), valCoord4D(seed, x3, y2, z1, w3), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z1, w3), valCoord4D(seed, x1, y3, z1, w3), valCoord4D(seed, x2, y3, z1, w3), valCoord4D(seed, x3, y3, z1, w3), xs),
                                ys),
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z2, w3), valCoord4D(seed, x1, y0, z2, w3), valCoord4D(seed, x2, y0, z2, w3), valCoord4D(seed, x3, y0, z2, w3), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z2, w3), valCoord4D(seed, x1, y1, z2, w3), valCoord4D(seed, x2, y1, z2, w3), valCoord4D(seed, x3, y1, z2, w3), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z2, w3), valCoord4D(seed, x1, y2, z2, w3), valCoord4D(seed, x2, y2, z2, w3), valCoord4D(seed, x3, y2, z2, w3), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z2, w3), valCoord4D(seed, x1, y3, z2, w3), valCoord4D(seed, x2, y3, z2, w3), valCoord4D(seed, x3, y3, z2, w3), xs),
                                ys),
                        cubicLerp(
                                cubicLerp(valCoord4D(seed, x0, y0, z3, w3), valCoord4D(seed, x1, y0, z3, w3), valCoord4D(seed, x2, y0, z3, w3), valCoord4D(seed, x3, y0, z3, w3), xs),
                                cubicLerp(valCoord4D(seed, x0, y1, z3, w3), valCoord4D(seed, x1, y1, z3, w3), valCoord4D(seed, x2, y1, z3, w3), valCoord4D(seed, x3, y1, z3, w3), xs),
                                cubicLerp(valCoord4D(seed, x0, y2, z3, w3), valCoord4D(seed, x1, y2, z3, w3), valCoord4D(seed, x2, y2, z3, w3), valCoord4D(seed, x3, y2, z3, w3), xs),
                                cubicLerp(valCoord4D(seed, x0, y3, z3, w3), valCoord4D(seed, x1, y3, z3, w3), valCoord4D(seed, x2, y3, z3, w3), valCoord4D(seed, x3, y3, z3, w3), xs),
                                ys),
                        zs),
                ws) * CUBIC_4D_BOUNDING;
    }


}
