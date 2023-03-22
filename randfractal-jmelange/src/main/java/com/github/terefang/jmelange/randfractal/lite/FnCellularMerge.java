package com.github.terefang.jmelange.randfractal.lite;

public class FnCellularMerge extends FastNoiseLite
{
    public static final double singleCellularMerge(int cellularDistanceFunction, double _sharpness, int seed, double x, double y)
    {
        int xr = fastRound(x);
        int yr = fastRound(y);

        double sum = 0f;
        int hash;

        switch (cellularDistanceFunction)
        {
            default:
            case EUCLIDEAN:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        hash = hashAll(xi, yi, seed);
                        double2 vec = CELL_2D[hash & 255];

                        double vecX = xi - x + vec.x;
                        double vecY = yi - y + vec.y;

                        double distance = (_sharpness*1.) - (vecX * vecX + vecY * vecY);

                        if (distance > 0f) {
                            distance *= 3f;
                            sum += ((hash >>> 28) - (hash >>> 24 & 15)) * distance * distance * distance;
                        }
                    }
                }
                break;
            case MANHATTAN:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        hash = hashAll(xi, yi, seed);
                        double2 vec = CELL_2D[hash & 255];

                        double vecX = xi - x + vec.x;
                        double vecY = yi - y + vec.y;

                        double distance = (_sharpness*1.) -  (Math.abs(vecX) + Math.abs(vecY));

                        if (distance > 0f) {
                            distance *= 3f;
                            sum += ((hash >>> 28) - (hash >>> 24 & 15)) * distance * distance * distance;
                        }
                    }
                }
                break;
            case NATURAL:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        hash = hashAll(xi, yi, seed);
                        double2 vec = CELL_2D[hash & 255];

                        double vecX = xi - x + vec.x;
                        double vecY = yi - y + vec.y;

                        double distance = (_sharpness*2.) - ((Math.abs(vecX) + Math.abs(vecY)) + (vecX * vecX + vecY * vecY));

                        if (distance > 0f) {
                            distance *= 3f;
                            sum += ((hash >>> 28) - (hash >>> 24 & 15)) * distance * distance * distance * 0.125f;
                        }
                    }
                }
                break;
        }

        return sum / (64f + Math.abs(sum));
    }

    public static final double singleCellularMerge(int cellularDistanceFunction, double _sharpness, int seed, double x, double y, double z)
    {
        int xr = fastRound(x);
        int yr = fastRound(y);
        int zr = fastRound(z);

        double sum = 0f;
        int hash;

        switch (cellularDistanceFunction) {
            case EUCLIDEAN:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        for (int zi = zr - 1; zi <= zr + 1; zi++) {
                            hash = hashAll(xi, yi, zi, seed);
                            double3 vec = CELL_3D[hash & 255];

                            double vecX = xi - x + vec.x;
                            double vecY = yi - y + vec.y;
                            double vecZ = zi - z + vec.z;

                            double distance = (_sharpness*1.) - (vecX * vecX + vecY * vecY + vecZ * vecZ);

                            if (distance > 0.) {
                                distance *= 3.;
                                sum += ((hash >>> 28) - (hash >>> 24 & 15)) * distance * distance * distance;
                            }
                        }
                    }
                }
                break;
            case MANHATTAN:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        for (int zi = zr - 1; zi <= zr + 1; zi++) {
                            hash = hashAll(xi, yi, zi, seed);
                            double3 vec = CELL_3D[hash & 255];

                            double vecX = xi - x + vec.x;
                            double vecY = yi - y + vec.y;
                            double vecZ = zi - z + vec.z;

                            double distance = (_sharpness*1.) - (Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ));

                            if (distance > 0.) {
                                distance *= 3.;
                                sum += ((hash >>> 28) - (hash >>> 24 & 15)) * distance * distance * distance;
                            }
                        }
                    }
                }
                break;
            case NATURAL:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        for (int zi = zr - 1; zi <= zr + 1; zi++) {
                            hash = hashAll(xi, yi, zi, seed);
                            double3 vec = CELL_3D[hash & 255];

                            double vecX = xi - x + vec.x;
                            double vecY = yi - y + vec.y;
                            double vecZ = zi - z + vec.z;

                            double distance = (_sharpness*2.) - ((Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ)) + (vecX * vecX + vecY * vecY + vecZ * vecZ));

                            if (distance > 0.) {
                                distance *= 3.;
                                sum += ((hash >>> 28) - (hash >>> 24 & 15)) * distance * distance * distance * 0.125f;
                            }
                        }
                    }
                }
                break;
        }

        return sum / (64f + Math.abs(sum));
    }


}
