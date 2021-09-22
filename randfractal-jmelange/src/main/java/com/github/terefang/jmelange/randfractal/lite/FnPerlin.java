package com.github.terefang.jmelange.randfractal.lite;

public class FnPerlin extends FastNoiseLite
{
    // ----------------------------------------------------------------------------

    // 2d perlin
    public static final float singlePerlin(int interpolation, int seed, float x, float y) {
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

        float xf0 = lerp(gradCoord2D(seed, x0, y0, xd0, yd0), gradCoord2D(seed, x1, y0, xd1, yd0), xs);
        float xf1 = lerp(gradCoord2D(seed, x0, y1, xd0, yd1), gradCoord2D(seed, x1, y1, xd1, yd1), xs);

        return lerp(xf0, xf1, ys);
    }

    // 3d perlin
    public static final float singlePerlin(int interpolation, int seed, float x, float y, float z) {
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

        final float xf00 = lerp(gradCoord3D(seed, x0, y0, z0, xd0, yd0, zd0), gradCoord3D(seed, x1, y0, z0, xd1, yd0, zd0), xs);
        final float xf10 = lerp(gradCoord3D(seed, x0, y1, z0, xd0, yd1, zd0), gradCoord3D(seed, x1, y1, z0, xd1, yd1, zd0), xs);
        final float xf01 = lerp(gradCoord3D(seed, x0, y0, z1, xd0, yd0, zd1), gradCoord3D(seed, x1, y0, z1, xd1, yd0, zd1), xs);
        final float xf11 = lerp(gradCoord3D(seed, x0, y1, z1, xd0, yd1, zd1), gradCoord3D(seed, x1, y1, z1, xd1, yd1, zd1), xs);

        final float yf0 = lerp(xf00, xf10, ys);
        final float yf1 = lerp(xf01, xf11, ys);

        return lerp(yf0, yf1, zs);
    }

    // 4d perlin
    public static final float singlePerlin(int interpolation, int seed, float x, float y, float z, float w) {
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int z0 = fastFloor(z);
        int w0 = fastFloor(w);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;
        int w1 = w0 + 1;

        float xs, ys, zs, ws;
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

        final float xd0 = x - x0;
        final float yd0 = y - y0;
        final float zd0 = z - z0;
        final float wd0 = w - w0;
        final float xd1 = xd0 - 1;
        final float yd1 = yd0 - 1;
        final float zd1 = zd0 - 1;
        final float wd1 = wd0 - 1;

        final float xf000 = lerp(gradCoord4D(seed, x0, y0, z0, w0, xd0, yd0, zd0, wd0), gradCoord4D(seed, x1, y0, z0, w0, xd1, yd0, zd0, wd0), xs);
        final float xf100 = lerp(gradCoord4D(seed, x0, y1, z0, w0, xd0, yd1, zd0, wd0), gradCoord4D(seed, x1, y1, z0, w0, xd1, yd1, zd0, wd0), xs);
        final float xf010 = lerp(gradCoord4D(seed, x0, y0, z1, w0, xd0, yd0, zd1, wd0), gradCoord4D(seed, x1, y0, z1, w0, xd1, yd0, zd1, wd0), xs);
        final float xf110 = lerp(gradCoord4D(seed, x0, y1, z1, w0, xd0, yd1, zd1, wd0), gradCoord4D(seed, x1, y1, z1, w0, xd1, yd1, zd1, wd0), xs);
        final float xf001 = lerp(gradCoord4D(seed, x0, y0, z0, w1, xd0, yd0, zd0, wd1), gradCoord4D(seed, x1, y0, z0, w1, xd1, yd0, zd0, wd1), xs);
        final float xf101 = lerp(gradCoord4D(seed, x0, y1, z0, w1, xd0, yd1, zd0, wd1), gradCoord4D(seed, x1, y1, z0, w1, xd1, yd1, zd0, wd1), xs);
        final float xf011 = lerp(gradCoord4D(seed, x0, y0, z1, w1, xd0, yd0, zd1, wd1), gradCoord4D(seed, x1, y0, z1, w1, xd1, yd0, zd1, wd1), xs);
        final float xf111 = lerp(gradCoord4D(seed, x0, y1, z1, w1, xd0, yd1, zd1, wd1), gradCoord4D(seed, x1, y1, z1, w1, xd1, yd1, zd1, wd1), xs);

        final float yf00 = lerp(xf000, xf100, ys);
        final float yf10 = lerp(xf010, xf110, ys);
        final float yf01 = lerp(xf001, xf101, ys);
        final float yf11 = lerp(xf011, xf111, ys);

        final float zf0 = lerp(yf00, yf10, zs);
        final float zf1 = lerp(yf01, yf11, zs);
        return lerp(zf0, zf1, ws) * 0.55f;
    }

    // 5d perlin
    public static final float singlePerlin(int interpolation, int seed, float x, float y, float z, float w, float u) {
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

        float xs, ys, zs, ws, us;
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

        final float xd0 = x - x0;
        final float yd0 = y - y0;
        final float zd0 = z - z0;
        final float wd0 = w - w0;
        final float ud0 = u - u0;
        final float xd1 = xd0 - 1;
        final float yd1 = yd0 - 1;
        final float zd1 = zd0 - 1;
        final float wd1 = wd0 - 1;
        final float ud1 = ud0 - 1;

        final float xf0000 = lerp(gradCoord5D(seed, x0, y0, z0, w0, u0, xd0, yd0, zd0, wd0, ud0), gradCoord5D(seed, x1, y0, z0, w0, u0, xd1, yd0, zd0, wd0, ud0), xs);
        final float xf1000 = lerp(gradCoord5D(seed, x0, y1, z0, w0, u0, xd0, yd1, zd0, wd0, ud0), gradCoord5D(seed, x1, y1, z0, w0, u0, xd1, yd1, zd0, wd0, ud0), xs);
        final float xf0100 = lerp(gradCoord5D(seed, x0, y0, z1, w0, u0, xd0, yd0, zd1, wd0, ud0), gradCoord5D(seed, x1, y0, z1, w0, u0, xd1, yd0, zd1, wd0, ud0), xs);
        final float xf1100 = lerp(gradCoord5D(seed, x0, y1, z1, w0, u0, xd0, yd1, zd1, wd0, ud0), gradCoord5D(seed, x1, y1, z1, w0, u0, xd1, yd1, zd1, wd0, ud0), xs);
        final float xf0010 = lerp(gradCoord5D(seed, x0, y0, z0, w1, u0, xd0, yd0, zd0, wd1, ud0), gradCoord5D(seed, x1, y0, z0, w1, u0, xd1, yd0, zd0, wd1, ud0), xs);
        final float xf1010 = lerp(gradCoord5D(seed, x0, y1, z0, w1, u0, xd0, yd1, zd0, wd1, ud0), gradCoord5D(seed, x1, y1, z0, w1, u0, xd1, yd1, zd0, wd1, ud0), xs);
        final float xf0110 = lerp(gradCoord5D(seed, x0, y0, z1, w1, u0, xd0, yd0, zd1, wd1, ud0), gradCoord5D(seed, x1, y0, z1, w1, u0, xd1, yd0, zd1, wd1, ud0), xs);
        final float xf1110 = lerp(gradCoord5D(seed, x0, y1, z1, w1, u0, xd0, yd1, zd1, wd1, ud0), gradCoord5D(seed, x1, y1, z1, w1, u0, xd1, yd1, zd1, wd1, ud0), xs);
        final float xf0001 = lerp(gradCoord5D(seed, x0, y0, z0, w0, u1, xd0, yd0, zd0, wd0, ud1), gradCoord5D(seed, x1, y0, z0, w0, u1, xd1, yd0, zd0, wd0, ud1), xs);
        final float xf1001 = lerp(gradCoord5D(seed, x0, y1, z0, w0, u1, xd0, yd1, zd0, wd0, ud1), gradCoord5D(seed, x1, y1, z0, w0, u1, xd1, yd1, zd0, wd0, ud1), xs);
        final float xf0101 = lerp(gradCoord5D(seed, x0, y0, z1, w0, u1, xd0, yd0, zd1, wd0, ud1), gradCoord5D(seed, x1, y0, z1, w0, u1, xd1, yd0, zd1, wd0, ud1), xs);
        final float xf1101 = lerp(gradCoord5D(seed, x0, y1, z1, w0, u1, xd0, yd1, zd1, wd0, ud1), gradCoord5D(seed, x1, y1, z1, w0, u1, xd1, yd1, zd1, wd0, ud1), xs);
        final float xf0011 = lerp(gradCoord5D(seed, x0, y0, z0, w1, u1, xd0, yd0, zd0, wd1, ud1), gradCoord5D(seed, x1, y0, z0, w1, u1, xd1, yd0, zd0, wd1, ud1), xs);
        final float xf1011 = lerp(gradCoord5D(seed, x0, y1, z0, w1, u1, xd0, yd1, zd0, wd1, ud1), gradCoord5D(seed, x1, y1, z0, w1, u1, xd1, yd1, zd0, wd1, ud1), xs);
        final float xf0111 = lerp(gradCoord5D(seed, x0, y0, z1, w1, u1, xd0, yd0, zd1, wd1, ud1), gradCoord5D(seed, x1, y0, z1, w1, u1, xd1, yd0, zd1, wd1, ud1), xs);
        final float xf1111 = lerp(gradCoord5D(seed, x0, y1, z1, w1, u1, xd0, yd1, zd1, wd1, ud1), gradCoord5D(seed, x1, y1, z1, w1, u1, xd1, yd1, zd1, wd1, ud1), xs);

        final float yf000 = lerp(xf0000, xf1000, ys);
        final float yf100 = lerp(xf0100, xf1100, ys);
        final float yf010 = lerp(xf0010, xf1010, ys);
        final float yf110 = lerp(xf0110, xf1110, ys);
        final float yf001 = lerp(xf0001, xf1001, ys);
        final float yf101 = lerp(xf0101, xf1101, ys);
        final float yf011 = lerp(xf0011, xf1011, ys);
        final float yf111 = lerp(xf0111, xf1111, ys);

        final float zf00 = lerp(yf000, yf100, zs);
        final float zf10 = lerp(yf010, yf110, zs);
        final float zf01 = lerp(yf001, yf101, zs);
        final float zf11 = lerp(yf011, yf111, zs);

        final float wf0 = lerp(zf00, zf10, ws);
        final float wf1 = lerp(zf01, zf11, ws);

        return lerp(wf0, wf1, us) * 0.7777777f;
    }

    // 6d perlin
    public static final float singlePerlin(int interpolation, int seed, float x, float y, float z, float w, float u, float v) {
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

        float xs, ys, zs, ws, us, vs;
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

        final float xd0 = x - x0;
        final float yd0 = y - y0;
        final float zd0 = z - z0;
        final float wd0 = w - w0;
        final float ud0 = u - u0;
        final float vd0 = v - v0;
        final float xd1 = xd0 - 1;
        final float yd1 = yd0 - 1;
        final float zd1 = zd0 - 1;
        final float wd1 = wd0 - 1;
        final float ud1 = ud0 - 1;
        final float vd1 = vd0 - 1;

        final float xf00000 = lerp(gradCoord6D(seed, x0, y0, z0, w0, u0, v0, xd0, yd0, zd0, wd0, ud0, vd0), gradCoord6D(seed, x1, y0, z0, w0, u0, v0, xd1, yd0, zd0, wd0, ud0, vd0), xs);
        final float xf10000 = lerp(gradCoord6D(seed, x0, y1, z0, w0, u0, v0, xd0, yd1, zd0, wd0, ud0, vd0), gradCoord6D(seed, x1, y1, z0, w0, u0, v0, xd1, yd1, zd0, wd0, ud0, vd0), xs);
        final float xf01000 = lerp(gradCoord6D(seed, x0, y0, z1, w0, u0, v0, xd0, yd0, zd1, wd0, ud0, vd0), gradCoord6D(seed, x1, y0, z1, w0, u0, v0, xd1, yd0, zd1, wd0, ud0, vd0), xs);
        final float xf11000 = lerp(gradCoord6D(seed, x0, y1, z1, w0, u0, v0, xd0, yd1, zd1, wd0, ud0, vd0), gradCoord6D(seed, x1, y1, z1, w0, u0, v0, xd1, yd1, zd1, wd0, ud0, vd0), xs);
        final float xf00100 = lerp(gradCoord6D(seed, x0, y0, z0, w1, u0, v0, xd0, yd0, zd0, wd1, ud0, vd0), gradCoord6D(seed, x1, y0, z0, w1, u0, v0, xd1, yd0, zd0, wd1, ud0, vd0), xs);
        final float xf10100 = lerp(gradCoord6D(seed, x0, y1, z0, w1, u0, v0, xd0, yd1, zd0, wd1, ud0, vd0), gradCoord6D(seed, x1, y1, z0, w1, u0, v0, xd1, yd1, zd0, wd1, ud0, vd0), xs);
        final float xf01100 = lerp(gradCoord6D(seed, x0, y0, z1, w1, u0, v0, xd0, yd0, zd1, wd1, ud0, vd0), gradCoord6D(seed, x1, y0, z1, w1, u0, v0, xd1, yd0, zd1, wd1, ud0, vd0), xs);
        final float xf11100 = lerp(gradCoord6D(seed, x0, y1, z1, w1, u0, v0, xd0, yd1, zd1, wd1, ud0, vd0), gradCoord6D(seed, x1, y1, z1, w1, u0, v0, xd1, yd1, zd1, wd1, ud0, vd0), xs);

        final float xf00010 = lerp(gradCoord6D(seed, x0, y0, z0, w0, u1, v0, xd0, yd0, zd0, wd0, ud1, vd0), gradCoord6D(seed, x1, y0, z0, w0, u1, v0, xd1, yd0, zd0, wd0, ud1, vd0), xs);
        final float xf10010 = lerp(gradCoord6D(seed, x0, y1, z0, w0, u1, v0, xd0, yd1, zd0, wd0, ud1, vd0), gradCoord6D(seed, x1, y1, z0, w0, u1, v0, xd1, yd1, zd0, wd0, ud1, vd0), xs);
        final float xf01010 = lerp(gradCoord6D(seed, x0, y0, z1, w0, u1, v0, xd0, yd0, zd1, wd0, ud1, vd0), gradCoord6D(seed, x1, y0, z1, w0, u1, v0, xd1, yd0, zd1, wd0, ud1, vd0), xs);
        final float xf11010 = lerp(gradCoord6D(seed, x0, y1, z1, w0, u1, v0, xd0, yd1, zd1, wd0, ud1, vd0), gradCoord6D(seed, x1, y1, z1, w0, u1, v0, xd1, yd1, zd1, wd0, ud1, vd0), xs);
        final float xf00110 = lerp(gradCoord6D(seed, x0, y0, z0, w1, u1, v0, xd0, yd0, zd0, wd1, ud1, vd0), gradCoord6D(seed, x1, y0, z0, w1, u1, v0, xd1, yd0, zd0, wd1, ud1, vd0), xs);
        final float xf10110 = lerp(gradCoord6D(seed, x0, y1, z0, w1, u1, v0, xd0, yd1, zd0, wd1, ud1, vd0), gradCoord6D(seed, x1, y1, z0, w1, u1, v0, xd1, yd1, zd0, wd1, ud1, vd0), xs);
        final float xf01110 = lerp(gradCoord6D(seed, x0, y0, z1, w1, u1, v0, xd0, yd0, zd1, wd1, ud1, vd0), gradCoord6D(seed, x1, y0, z1, w1, u1, v0, xd1, yd0, zd1, wd1, ud1, vd0), xs);
        final float xf11110 = lerp(gradCoord6D(seed, x0, y1, z1, w1, u1, v0, xd0, yd1, zd1, wd1, ud1, vd0), gradCoord6D(seed, x1, y1, z1, w1, u1, v0, xd1, yd1, zd1, wd1, ud1, vd0), xs);

        final float xf00001 = lerp(gradCoord6D(seed, x0, y0, z0, w0, u0, v1, xd0, yd0, zd0, wd0, ud0, vd1), gradCoord6D(seed, x1, y0, z0, w0, u0, v1, xd1, yd0, zd0, wd0, ud0, vd1), xs);
        final float xf10001 = lerp(gradCoord6D(seed, x0, y1, z0, w0, u0, v1, xd0, yd1, zd0, wd0, ud0, vd1), gradCoord6D(seed, x1, y1, z0, w0, u0, v1, xd1, yd1, zd0, wd0, ud0, vd1), xs);
        final float xf01001 = lerp(gradCoord6D(seed, x0, y0, z1, w0, u0, v1, xd0, yd0, zd1, wd0, ud0, vd1), gradCoord6D(seed, x1, y0, z1, w0, u0, v1, xd1, yd0, zd1, wd0, ud0, vd1), xs);
        final float xf11001 = lerp(gradCoord6D(seed, x0, y1, z1, w0, u0, v1, xd0, yd1, zd1, wd0, ud0, vd1), gradCoord6D(seed, x1, y1, z1, w0, u0, v1, xd1, yd1, zd1, wd0, ud0, vd1), xs);
        final float xf00101 = lerp(gradCoord6D(seed, x0, y0, z0, w1, u0, v1, xd0, yd0, zd0, wd1, ud0, vd1), gradCoord6D(seed, x1, y0, z0, w1, u0, v1, xd1, yd0, zd0, wd1, ud0, vd1), xs);
        final float xf10101 = lerp(gradCoord6D(seed, x0, y1, z0, w1, u0, v1, xd0, yd1, zd0, wd1, ud0, vd1), gradCoord6D(seed, x1, y1, z0, w1, u0, v1, xd1, yd1, zd0, wd1, ud0, vd1), xs);
        final float xf01101 = lerp(gradCoord6D(seed, x0, y0, z1, w1, u0, v1, xd0, yd0, zd1, wd1, ud0, vd1), gradCoord6D(seed, x1, y0, z1, w1, u0, v1, xd1, yd0, zd1, wd1, ud0, vd1), xs);
        final float xf11101 = lerp(gradCoord6D(seed, x0, y1, z1, w1, u0, v1, xd0, yd1, zd1, wd1, ud0, vd1), gradCoord6D(seed, x1, y1, z1, w1, u0, v1, xd1, yd1, zd1, wd1, ud0, vd1), xs);

        final float xf00011 = lerp(gradCoord6D(seed, x0, y0, z0, w0, u1, v1, xd0, yd0, zd0, wd0, ud1, vd1), gradCoord6D(seed, x1, y0, z0, w0, u1, v1, xd1, yd0, zd0, wd0, ud1, vd1), xs);
        final float xf10011 = lerp(gradCoord6D(seed, x0, y1, z0, w0, u1, v1, xd0, yd1, zd0, wd0, ud1, vd1), gradCoord6D(seed, x1, y1, z0, w0, u1, v1, xd1, yd1, zd0, wd0, ud1, vd1), xs);
        final float xf01011 = lerp(gradCoord6D(seed, x0, y0, z1, w0, u1, v1, xd0, yd0, zd1, wd0, ud1, vd1), gradCoord6D(seed, x1, y0, z1, w0, u1, v1, xd1, yd0, zd1, wd0, ud1, vd1), xs);
        final float xf11011 = lerp(gradCoord6D(seed, x0, y1, z1, w0, u1, v1, xd0, yd1, zd1, wd0, ud1, vd1), gradCoord6D(seed, x1, y1, z1, w0, u1, v1, xd1, yd1, zd1, wd0, ud1, vd1), xs);
        final float xf00111 = lerp(gradCoord6D(seed, x0, y0, z0, w1, u1, v1, xd0, yd0, zd0, wd1, ud1, vd1), gradCoord6D(seed, x1, y0, z0, w1, u1, v1, xd1, yd0, zd0, wd1, ud1, vd1), xs);
        final float xf10111 = lerp(gradCoord6D(seed, x0, y1, z0, w1, u1, v1, xd0, yd1, zd0, wd1, ud1, vd1), gradCoord6D(seed, x1, y1, z0, w1, u1, v1, xd1, yd1, zd0, wd1, ud1, vd1), xs);
        final float xf01111 = lerp(gradCoord6D(seed, x0, y0, z1, w1, u1, v1, xd0, yd0, zd1, wd1, ud1, vd1), gradCoord6D(seed, x1, y0, z1, w1, u1, v1, xd1, yd0, zd1, wd1, ud1, vd1), xs);
        final float xf11111 = lerp(gradCoord6D(seed, x0, y1, z1, w1, u1, v1, xd0, yd1, zd1, wd1, ud1, vd1), gradCoord6D(seed, x1, y1, z1, w1, u1, v1, xd1, yd1, zd1, wd1, ud1, vd1), xs);

        final float yf0000 = lerp(xf00000, xf10000, ys);
        final float yf1000 = lerp(xf01000, xf11000, ys);
        final float yf0100 = lerp(xf00100, xf10100, ys);
        final float yf1100 = lerp(xf01100, xf11100, ys);

        final float yf0010 = lerp(xf00010, xf10010, ys);
        final float yf1010 = lerp(xf01010, xf11010, ys);
        final float yf0110 = lerp(xf00110, xf10110, ys);
        final float yf1110 = lerp(xf01110, xf11110, ys);

        final float yf0001 = lerp(xf00001, xf10001, ys);
        final float yf1001 = lerp(xf01001, xf11001, ys);
        final float yf0101 = lerp(xf00101, xf10101, ys);
        final float yf1101 = lerp(xf01101, xf11101, ys);

        final float yf0011 = lerp(xf00011, xf10011, ys);
        final float yf1011 = lerp(xf01011, xf11011, ys);
        final float yf0111 = lerp(xf00111, xf10111, ys);
        final float yf1111 = lerp(xf01111, xf11111, ys);

        final float zf000 = lerp(yf0000, yf1000, zs);
        final float zf100 = lerp(yf0100, yf1100, zs);

        final float zf010 = lerp(yf0010, yf1010, zs);
        final float zf110 = lerp(yf0110, yf1110, zs);

        final float zf001 = lerp(yf0001, yf1001, zs);
        final float zf101 = lerp(yf0101, yf1101, zs);

        final float zf011 = lerp(yf0011, yf1011, zs);
        final float zf111 = lerp(yf0111, yf1111, zs);

        final float wf00 = lerp(zf000, zf100, ws);
        final float wf10 = lerp(zf010, zf110, ws);
        final float wf01 = lerp(zf001, zf101, ws);
        final float wf11 = lerp(zf011, zf111, ws);

        final float uf0 = lerp(wf00, wf10, us);
        final float uf1 = lerp(wf01, wf11, us);

        return lerp(uf0, uf1, vs) * 1.61f;
    }
}
