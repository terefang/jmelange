package com.github.terefang.jmelange.randfractal.lite;

public class FnValue extends FastNoiseLite{
    // ----------------------------------------------------------------------------

    // 2d value
    public static final float singleValue (int interpolation, int seed, float x, float y) {
        int xFloor = x >= 0 ? (int) x : (int) x - 1;
        x -= xFloor;
        int yFloor = y >= 0 ? (int) y : (int) y - 1;
        y -= yFloor;
        switch (interpolation) {
            case HERMITE:
                x = hermiteInterpolator(x);
                y = hermiteInterpolator(y);
                break;
            case QUINTIC:
                x = quinticInterpolator(x);
                y = quinticInterpolator(y);
                break;
        }
        xFloor *= 0xD1B55;
        yFloor *= 0xABC99;
        return ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, seed) + x * hashPart1024(xFloor + 0xD1B55, yFloor, seed))
                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xABC99, seed) + x * hashPart1024(xFloor + 0xD1B55, yFloor + 0xABC99, seed)))
                * 0x1p-9f;
    }

    // 3d value
    public static final float singleValue(int interpolation, int seed, float x, float y, float z) {
        int xFloor = x >= 0 ? (int) x : (int) x - 1;
        x -= xFloor;
        int yFloor = y >= 0 ? (int) y : (int) y - 1;
        y -= yFloor;
        int zFloor = z >= 0 ? (int) z : (int) z - 1;
        z -= zFloor;
        switch (interpolation) {
            case HERMITE:
                x = hermiteInterpolator(x);
                y = hermiteInterpolator(y);
                z = hermiteInterpolator(z);
                break;
            case QUINTIC:
                x = quinticInterpolator(x);
                y = quinticInterpolator(y);
                z = quinticInterpolator(z);
                break;
        }
        //0xDB4F1, 0xBBE05, 0xA0F2F
        xFloor *= 0xDB4F1;
        yFloor *= 0xBBE05;
        zFloor *= 0xA0F2F;
        return ((1 - z) *
                ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, seed) + x * hashPart1024(xFloor + 0xDB4F1, yFloor, zFloor, seed))
                        + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xBBE05, zFloor, seed) + x * hashPart1024(xFloor + 0xDB4F1, yFloor + 0xBBE05, zFloor, seed)))
                + z *
                ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xA0F2F, seed) + x * hashPart1024(xFloor + 0xDB4F1, yFloor, zFloor + 0xA0F2F, seed))
                        + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xBBE05, zFloor + 0xA0F2F, seed) + x * hashPart1024(xFloor + 0xDB4F1, yFloor + 0xBBE05, zFloor + 0xA0F2F, seed)))
        ) * 0x1p-9f;
    }

    // 4d value
    public static final float singleValue(int interpolation, int seed, float x, float y, float z, float w) {
        int xFloor = x >= 0 ? (int) x : (int) x - 1;
        x -= xFloor;
        int yFloor = y >= 0 ? (int) y : (int) y - 1;
        y -= yFloor;
        int zFloor = z >= 0 ? (int) z : (int) z - 1;
        z -= zFloor;
        int wFloor = w >= 0 ? (int) w : (int) w - 1;
        w -= wFloor;
        switch (interpolation) {
            case HERMITE:
                x = hermiteInterpolator(x);
                y = hermiteInterpolator(y);
                z = hermiteInterpolator(z);
                w = hermiteInterpolator(w);
                break;
            case QUINTIC:
                x = quinticInterpolator(x);
                y = quinticInterpolator(y);
                z = quinticInterpolator(z);
                w = quinticInterpolator(w);
                break;
        }
        //0xE19B1, 0xC6D1D, 0xAF36D, 0x9A695
        xFloor *= 0xE19B1;
        yFloor *= 0xC6D1D;
        zFloor *= 0xAF36D;
        wFloor *= 0x9A695;
        return ((1 - w) *
                ((1 - z) *
                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, seed) + x * hashPart1024(xFloor + 0xE19B1, yFloor, zFloor, wFloor, seed))
                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xC6D1D, zFloor, wFloor, seed) + x * hashPart1024(xFloor + 0xE19B1, yFloor + 0xC6D1D, zFloor, wFloor, seed)))
                        + z *
                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xAF36D, wFloor, seed) + x * hashPart1024(xFloor + 0xE19B1, yFloor, zFloor + 0xAF36D, wFloor, seed))
                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xC6D1D, zFloor + 0xAF36D, wFloor, seed) + x * hashPart1024(xFloor + 0xE19B1, yFloor + 0xC6D1D, zFloor + 0xAF36D, wFloor, seed))))
                + (w *
                ((1 - z) *
                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0x9A695, seed) + x * hashPart1024(xFloor + 0xE19B1, yFloor, zFloor, wFloor + 0x9A695, seed))
                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xC6D1D, zFloor, wFloor + 0x9A695, seed) + x * hashPart1024(xFloor + 0xE19B1, yFloor + 0xC6D1D, zFloor, wFloor + 0x9A695, seed)))
                        + z *
                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xAF36D, wFloor + 0x9A695, seed) + x * hashPart1024(xFloor + 0xE19B1, yFloor, zFloor + 0xAF36D, wFloor + 0x9A695, seed))
                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xC6D1D, zFloor + 0xAF36D, wFloor + 0x9A695, seed) + x * hashPart1024(xFloor + 0xE19B1, yFloor + 0xC6D1D, zFloor + 0xAF36D, wFloor + 0x9A695, seed)))
                ))) * 0x1p-9f;
    }

    // 5d value
    public static final float singleValue(int interpolation, int seed, float x, float y, float z, float w, float u) {
        int xFloor = x >= 0 ? (int) x : (int) x - 1;
        x -= xFloor;
        int yFloor = y >= 0 ? (int) y : (int) y - 1;
        y -= yFloor;
        int zFloor = z >= 0 ? (int) z : (int) z - 1;
        z -= zFloor;
        int wFloor = w >= 0 ? (int) w : (int) w - 1;
        w -= wFloor;
        int uFloor = u >= 0 ? (int) u : (int) u - 1;
        u -= uFloor;
        switch (interpolation) {
            case HERMITE:
                x = hermiteInterpolator(x);
                y = hermiteInterpolator(y);
                z = hermiteInterpolator(z);
                w = hermiteInterpolator(w);
                u = hermiteInterpolator(u);
                break;
            case QUINTIC:
                x = quinticInterpolator(x);
                y = quinticInterpolator(y);
                z = quinticInterpolator(z);
                w = quinticInterpolator(w);
                u = quinticInterpolator(u);
                break;
        }
        //0xE60E3, 0xCEBD7, 0xB9C9B, 0xA6F57, 0x9609D, 0x86D51
        xFloor *= 0xE60E3;
        yFloor *= 0xCEBD7;
        zFloor *= 0xB9C9B;
        wFloor *= 0xA6F57;
        uFloor *= 0x9609D;
        return ((1 - u) *
                ((1 - w) *
                        ((1 - z) *
                                ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, uFloor, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor, zFloor, wFloor, uFloor, seed))
                                        + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xCEBD7, zFloor, wFloor, uFloor, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor + 0xCEBD7, zFloor, wFloor, uFloor, seed)))
                                + z *
                                ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xB9C9B, wFloor, uFloor, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor, zFloor + 0xB9C9B, wFloor, uFloor, seed))
                                        + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xCEBD7, zFloor + 0xB9C9B, wFloor, uFloor, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor + 0xCEBD7, zFloor + 0xB9C9B, wFloor, uFloor, seed))))
                        + (w *
                        ((1 - z) *
                                ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0xA6F57, uFloor, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor, zFloor, wFloor + 0xA6F57, uFloor, seed))
                                        + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xCEBD7, zFloor, wFloor + 0xA6F57, uFloor, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor + 0xCEBD7, zFloor, wFloor + 0xA6F57, uFloor, seed)))
                                + z *
                                ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xB9C9B, wFloor + 0xA6F57, uFloor, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor, zFloor + 0xB9C9B, wFloor + 0xA6F57, uFloor, seed))
                                        + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xCEBD7, zFloor + 0xB9C9B, wFloor + 0xA6F57, uFloor, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor + 0xCEBD7, zFloor + 0xB9C9B, wFloor + 0xA6F57, uFloor, seed)))
                        )))
                + (u *
                ((1 - w) *
                        ((1 - z) *
                                ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, uFloor + 0x9609D, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor, zFloor, wFloor, uFloor + 0x9609D, seed))
                                        + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xCEBD7, zFloor, wFloor, uFloor + 0x9609D, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor + 0xCEBD7, zFloor, wFloor, uFloor + 0x9609D, seed)))
                                + z *
                                ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xB9C9B, wFloor, uFloor + 0x9609D, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor, zFloor + 0xB9C9B, wFloor, uFloor + 0x9609D, seed))
                                        + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xCEBD7, zFloor + 0xB9C9B, wFloor, uFloor + 0x9609D, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor + 0xCEBD7, zFloor + 0xB9C9B, wFloor, uFloor + 0x9609D, seed))))
                        + (w *
                        ((1 - z) *
                                ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0xA6F57, uFloor + 0x9609D, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor, zFloor, wFloor + 0xA6F57, uFloor + 0x9609D, seed))
                                        + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xCEBD7, zFloor, wFloor + 0xA6F57, uFloor + 0x9609D, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor + 0xCEBD7, zFloor, wFloor + 0xA6F57, uFloor + 0x9609D, seed)))
                                + z *
                                ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xB9C9B, wFloor + 0xA6F57, uFloor + 0x9609D, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor, zFloor + 0xB9C9B, wFloor + 0xA6F57, uFloor + 0x9609D, seed))
                                        + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xCEBD7, zFloor + 0xB9C9B, wFloor + 0xA6F57, uFloor + 0x9609D, seed) + x * hashPart1024(xFloor + 0xE60E3, yFloor + 0xCEBD7, zFloor + 0xB9C9B, wFloor + 0xA6F57, uFloor + 0x9609D, seed)))
                        ))))
        ) * 0x1p-9f;
    }

    // 6d value
    public static final float singleValue(int interpolation, int seed, float x, float y, float z, float w, float u, float v) {
        int xFloor = x >= 0 ? (int) x : (int) x - 1;
        x -= xFloor;
        int yFloor = y >= 0 ? (int) y : (int) y - 1;
        y -= yFloor;
        int zFloor = z >= 0 ? (int) z : (int) z - 1;
        z -= zFloor;
        int wFloor = w >= 0 ? (int) w : (int) w - 1;
        w -= wFloor;
        int uFloor = u >= 0 ? (int) u : (int) u - 1;
        u -= uFloor;
        int vFloor = v >= 0 ? (int) v : (int) v - 1;
        v -= vFloor;
        switch (interpolation) {
            case HERMITE:
                x = hermiteInterpolator(x);
                y = hermiteInterpolator(y);
                z = hermiteInterpolator(z);
                w = hermiteInterpolator(w);
                u = hermiteInterpolator(u);
                v = hermiteInterpolator(v);
                break;
            case QUINTIC:
                x = quinticInterpolator(x);
                y = quinticInterpolator(y);
                z = quinticInterpolator(z);
                w = quinticInterpolator(w);
                u = quinticInterpolator(u);
                v = quinticInterpolator(v);
                break;
        }
        //0xE95E1, 0xD4BC7, 0xC1EDB, 0xB0C8B, 0xA1279, 0x92E85
        xFloor *= 0xE95E1;
        yFloor *= 0xD4BC7;
        zFloor *= 0xC1EDB;
        wFloor *= 0xB0C8B;
        uFloor *= 0xA127B;
        vFloor *= 0x92E85;
        return ((1 - v) *
                ((1 - u) *
                        ((1 - w) *
                                ((1 - z) *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, uFloor, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor, wFloor, uFloor, vFloor, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor, wFloor, uFloor, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor, wFloor, uFloor, vFloor, seed)))
                                        + z *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC1EDB, wFloor, uFloor, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor + 0xC1EDB, wFloor, uFloor, vFloor, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor, uFloor, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor, uFloor, vFloor, seed))))
                                + (w *
                                ((1 - z) *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0xB0C8B, uFloor, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor, wFloor + 0xB0C8B, uFloor, vFloor, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor, wFloor + 0xB0C8B, uFloor, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor, wFloor + 0xB0C8B, uFloor, vFloor, seed)))
                                        + z *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor, vFloor, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor, vFloor, seed)))
                                )))
                        + (u *
                        ((1 - w) *
                                ((1 - z) *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, uFloor + 0xA127B, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor, wFloor, uFloor + 0xA127B, vFloor, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor, wFloor, uFloor + 0xA127B, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor, wFloor, uFloor + 0xA127B, vFloor, seed)))
                                        + z *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC1EDB, wFloor, uFloor + 0xA127B, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor + 0xC1EDB, wFloor, uFloor + 0xA127B, vFloor, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor, uFloor + 0xA127B, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor, uFloor + 0xA127B, vFloor, seed))))
                                + (w *
                                ((1 - z) *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor, seed)))
                                        + z *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor, seed)))
                                )))))
                + (v *
                ((1 - u) *
                        ((1 - w) *
                                ((1 - z) *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, uFloor, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor, wFloor, uFloor, vFloor + 0x92E85, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor, wFloor, uFloor, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor, wFloor, uFloor, vFloor + 0x92E85, seed)))
                                        + z *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC1EDB, wFloor, uFloor, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor + 0xC1EDB, wFloor, uFloor, vFloor + 0x92E85, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor, uFloor, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor, uFloor, vFloor + 0x92E85, seed))))
                                + (w *
                                ((1 - z) *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0xB0C8B, uFloor, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor, wFloor + 0xB0C8B, uFloor, vFloor + 0x92E85, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor, wFloor + 0xB0C8B, uFloor, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor, wFloor + 0xB0C8B, uFloor, vFloor + 0x92E85, seed)))
                                        + z *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor, vFloor + 0x92E85, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor, vFloor + 0x92E85, seed)))
                                )))
                        + (u *
                        ((1 - w) *
                                ((1 - z) *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, uFloor + 0xA127B, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor, wFloor, uFloor + 0xA127B, vFloor + 0x92E85, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor, wFloor, uFloor + 0xA127B, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor, wFloor, uFloor + 0xA127B, vFloor + 0x92E85, seed)))
                                        + z *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC1EDB, wFloor, uFloor + 0xA127B, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor + 0xC1EDB, wFloor, uFloor + 0xA127B, vFloor + 0x92E85, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor, uFloor + 0xA127B, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor, uFloor + 0xA127B, vFloor + 0x92E85, seed))))
                                + (w *
                                ((1 - z) *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor + 0x92E85, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor + 0x92E85, seed)))
                                        + z *
                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor + 0x92E85, seed))
                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor + 0x92E85, seed) + x * hashPart1024(xFloor + 0xE95E1, yFloor + 0xD4BC7, zFloor + 0xC1EDB, wFloor + 0xB0C8B, uFloor + 0xA127B, vFloor + 0x92E85, seed)))
                                ))))))
        ) * 0x1p-9f;
    }

    public static final float singleValue(int interpolation, int seed, float x, float y, float z, float w, float u, float v, float m) {
        int xFloor = x >= 0 ? (int) x : (int) x - 1;
        x -= xFloor;
        int yFloor = y >= 0 ? (int) y : (int) y - 1;
        y -= yFloor;
        int zFloor = z >= 0 ? (int) z : (int) z - 1;
        z -= zFloor;
        int wFloor = w >= 0 ? (int) w : (int) w - 1;
        w -= wFloor;
        int uFloor = u >= 0 ? (int) u : (int) u - 1;
        u -= uFloor;
        int vFloor = v >= 0 ? (int) v : (int) v - 1;
        v -= vFloor;
        int mFloor = m >= 0 ? (int) m : (int) m - 1;
        m -= mFloor;
        switch (interpolation) {
            case HERMITE:
                x = hermiteInterpolator(x);
                y = hermiteInterpolator(y);
                z = hermiteInterpolator(z);
                w = hermiteInterpolator(w);
                u = hermiteInterpolator(u);
                v = hermiteInterpolator(v);
                break;
            case QUINTIC:
                x = quinticInterpolator(x);
                y = quinticInterpolator(y);
                z = quinticInterpolator(z);
                w = quinticInterpolator(w);
                u = quinticInterpolator(u);
                v = quinticInterpolator(v);
                break;
        }
        xFloor *= 0xEBEDF;
        yFloor *= 0xD96EB;
        zFloor *= 0xC862B;
        wFloor *= 0xB8ACD;
        uFloor *= 0xAA323;
        vFloor *= 0x9CDA5;
        mFloor *= 0x908E3;
        return
                ((1 - m) *
                        ((1 - v) *
                                ((1 - u) *
                                        ((1 - w) *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, uFloor, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor, uFloor, vFloor, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor, uFloor, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor, uFloor, vFloor, mFloor, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor, uFloor, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor, uFloor, vFloor, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor, vFloor, mFloor, seed))))
                                                + (w *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0xB8ACD, uFloor, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor + 0xB8ACD, uFloor, vFloor, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor, vFloor, mFloor, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor, mFloor, seed)))
                                                )))
                                        + (u *
                                        ((1 - w) *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, uFloor + 0xAA323, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor, uFloor + 0xAA323, vFloor, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor, uFloor + 0xAA323, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor, uFloor + 0xAA323, vFloor, mFloor, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor, mFloor, seed))))
                                                + (w *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor, seed)))
                                                )))))
                                + (v *
                                ((1 - u) *
                                        ((1 - w) *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, uFloor, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor, uFloor, vFloor + 0x9CDA5, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor, uFloor, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor, uFloor, vFloor + 0x9CDA5, mFloor, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor, uFloor, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor, uFloor, vFloor + 0x9CDA5, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor, vFloor + 0x9CDA5, mFloor, seed))))
                                                + (w *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor, seed)))
                                                )))
                                        + (u *
                                        ((1 - w) *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed))))
                                                + (w *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor, seed)))
                                                )))))))
                        + (m *
                        ((1 - v) *
                                ((1 - u) *
                                        ((1 - w) *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, uFloor, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor, uFloor, vFloor, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor, uFloor, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor, uFloor, vFloor, mFloor + 0x908E3, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor, uFloor, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor, uFloor, vFloor, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor, vFloor, mFloor + 0x908E3, seed))))
                                                + (w *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0xB8ACD, uFloor, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor + 0xB8ACD, uFloor, vFloor, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor, vFloor, mFloor + 0x908E3, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor, mFloor + 0x908E3, seed)))
                                                )))
                                        + (u *
                                        ((1 - w) *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed))))
                                                + (w *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor, mFloor + 0x908E3, seed)))
                                                )))))
                                + (v *
                                ((1 - u) *
                                        ((1 - w) *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed))))
                                                + (w *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor, vFloor + 0x9CDA5, mFloor + 0x908E3, seed)))
                                                )))
                                        + (u *
                                        ((1 - w) *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed))))
                                                + (w *
                                                ((1 - z) *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed)))
                                                        + z *
                                                        ((1 - y) * ((1 - x) * hashPart1024(xFloor, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed))
                                                                + y * ((1 - x) * hashPart1024(xFloor, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed) + x * hashPart1024(xFloor + 0xEBEDF, yFloor + 0xD96EB, zFloor + 0xC862B, wFloor + 0xB8ACD, uFloor + 0xAA323, vFloor + 0x9CDA5, mFloor + 0x908E3, seed)))
                                                ))))))))
                ) * 0x1p-10f + 0.5f;
    }

}