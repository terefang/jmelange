package com.github.terefang.jmelange.randfractal.lite;

public class FnPyramid  extends FastNoiseLite {
    // ----------------------------------------------------------------------------

    // 2d value
    public static final double singlePyramid (int interpolation, int seed, double x, double y)
    {
        final int STEPX = 0xD1B55;
        final int STEPY = 0xABC99;
        int xFloor = (x >= 0 ? (int) x : (int) x - 1) & -2;
        x -= xFloor;
        x *= 0.5;
        int yFloor = (y >= 0 ? (int) y : (int) y - 1) & -2;
        y -= yFloor;
        y *= 0.5;
        xFloor *= STEPX;
        yFloor *= STEPY;
        final int cap = hashPart1024(xFloor + STEPX, yFloor + STEPY, seed);
        if(x == 0.5 && y == 0.5) return cap * (0x1.0040100401004p-10);
        double xd = x - 0.5;
        double yd = y - 0.5;
        double xa = Math.abs(xd);
        double ya = Math.abs(yd);
        if(xa < ya){
            // flat base, cap points up or down
            if(yd >= 0){
                yFloor += STEPY << 1;
            }
//            x = (xd / ya + 1.0) * 0.5;
//            x = 1.0 - x;
            x += ya - 0.5;
            ya += ya;
            x /= ya;
//            x *= x * (3 - 2 * x);
            ya *= ya * (3 - 2 * ya);
//            ya = Math.sqrt(ya);

//            ya = (ya - 0.5);
//            ya = (ya * ya * ya + 0.125) * 4.0;

//            ya = 0.5 - ya;
//            ya *= ya * 4.0;
//            ya = 1.0 - ya;
//            if(x < 0 || x > 1)
//                System.out.println("x is out of bounds in a horizontal-base pyr: " + x);
//            if(ya < 0 || ya > 1)
//                System.out.println("ya is out of bounds in a horizontal-base pyr: " + ya);

            double cc = (1 - ya) * cap;
            return ((1 - x) * (ya * hashPart1024(xFloor, yFloor, seed) + cc)
                    + x * (ya * hashPart1024(xFloor + STEPX + STEPX, yFloor, seed) + cc))
                    * (0x1.0040100401004p-10);
//            return (ya * ((1 - x) * hashPart1024(xFloor, yFloor, seed) + x * hashPart1024(xFloor + STEPX + STEPX, yFloor, seed))
//                    + (1 - ya) * cap) * (0x1.0040100401004p-10);
        }
        else {
            // vertical base, cap points left or right
            if(xd >= 0){
                xFloor += STEPX << 1;
            }
//            y = (yd / xa + 1.0) * 0.5;
//            y = 1.0 - y;
            y += xa - 0.5;
            xa += xa;
            y /= xa;
//            y *= y * (3 - 2 * y);
            xa *= xa * (3 - 2 * xa);
//            xa = Math.sqrt(xa);

//            xa = (xa - 0.5);
//            xa = (xa * xa * xa + 0.125) * 4.0;

//            xa = 0.5 - xa;
//            xa *= xa * 4.0;
//            xa = 1.0 - xa;
//            if(y < 0 || y > 1)
//                System.out.println("y is out of bounds in a vertical-base pyr: " + y);
//            if(xa < 0 || xa > 1)
//                System.out.println("xa is out of bounds in a vertical-base pyr: " + xa);

            double cc = (1 - xa) * cap;
            return ((1 - y) * (xa * hashPart1024(xFloor, yFloor, seed) + cc)
                    + y * (xa * hashPart1024(xFloor, yFloor + STEPY + STEPY, seed) + cc)) * (0x1.0040100401004p-10);
//            return (xa * ((1 - y) * hashPart1024(xFloor, yFloor, seed) + y * hashPart1024(xFloor, yFloor + STEPY + STEPY, seed))
//                    + (1 - xa) * cap) * (0x1.0040100401004p-10);
        }
    }
}
