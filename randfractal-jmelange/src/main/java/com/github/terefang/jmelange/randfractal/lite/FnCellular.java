package com.github.terefang.jmelange.randfractal.lite;

public class FnCellular extends FastNoiseLite
{
    public static final double singleCellular(int cellularDistanceFunction, int cellularReturnType, int seed, double x, double y)
    {
        int xr = fastRound(x);
        int yr = fastRound(y);

        double distance = 999999;
        int xc = 0, yc = 0;

        switch (cellularDistanceFunction) {
            default:
            case EUCLIDEAN:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        double2 vec = CELL_2D[hash256(xi, yi, seed)];

                        double vecX = xi - x + vec.x;
                        double vecY = yi - y + vec.y;

                        double newDistance = vecX * vecX + vecY * vecY;

                        if (newDistance < distance) {
                            distance = newDistance;
                            xc = xi;
                            yc = yi;
                        }
                    }
                }
                break;
            case MANHATTAN:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        double2 vec = CELL_2D[hash256(xi, yi, seed)];

                        double vecX = xi - x + vec.x;
                        double vecY = yi - y + vec.y;

                        double newDistance = (Math.abs(vecX) + Math.abs(vecY));

                        if (newDistance < distance) {
                            distance = newDistance;
                            xc = xi;
                            yc = yi;
                        }
                    }
                }
                break;
            case NATURAL:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        double2 vec = CELL_2D[hash256(xi, yi, seed)];

                        double vecX = xi - x + vec.x;
                        double vecY = yi - y + vec.y;

                        double newDistance = (Math.abs(vecX) + Math.abs(vecY)) + (vecX * vecX + vecY * vecY);

                        if (newDistance < distance) {
                            distance = newDistance;
                            xc = xi;
                            yc = yi;
                        }
                    }
                }
                break;
        }

        switch (cellularReturnType) {
            case CELL_VALUE:
                return valCoord2D(0, xc, yc);

            case NOISE_LOOKUP:
                double2 vec = CELL_2D[hash256(xc, yc, seed)];
                return f_brownian_motion(BASE_NOISETYPE, xc + vec.x, yc + vec.y, seed+123, BASE_OFFSET, BASE_H, 3, BASE_FREQUENCY, BASE_LACUNARITY, BASE_GAIN, BASE_HARSHNESS, BASE_MUTATION, BASE_SHARPNESS, BASE_SEED_VARIATION);

            case DISTANCE:
                return distance - 1;
            default:
                return 0;
        }
    }

    public static final double singleCellular(int cellularDistanceFunction, int cellularReturnType, int seed, double x, double y, double z)
    {
        int xr = fastRound(x);
        int yr = fastRound(y);
        int zr = fastRound(z);

        double distance = 999999;
        int xc = 0, yc = 0, zc = 0;

        switch (cellularDistanceFunction) {
            case EUCLIDEAN:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        for (int zi = zr - 1; zi <= zr + 1; zi++) {
                            double3 vec = CELL_3D[hash256(xi, yi, zi, seed)];

                            double vecX = xi - x + vec.x;
                            double vecY = yi - y + vec.y;
                            double vecZ = zi - z + vec.z;

                            double newDistance = vecX * vecX + vecY * vecY + vecZ * vecZ;

                            if (newDistance < distance) {
                                distance = newDistance;
                                xc = xi;
                                yc = yi;
                                zc = zi;
                            }
                        }
                    }
                }
                break;
            case MANHATTAN:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        for (int zi = zr - 1; zi <= zr + 1; zi++) {
                            double3 vec = CELL_3D[hash256(xi, yi, zi, seed)];

                            double vecX = xi - x + vec.x;
                            double vecY = yi - y + vec.y;
                            double vecZ = zi - z + vec.z;

                            double newDistance = Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ);

                            if (newDistance < distance) {
                                distance = newDistance;
                                xc = xi;
                                yc = yi;
                                zc = zi;
                            }
                        }
                    }
                }
                break;
            case NATURAL:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        for (int zi = zr - 1; zi <= zr + 1; zi++) {
                            double3 vec = CELL_3D[hash256(xi, yi, zi, seed)];

                            double vecX = xi - x + vec.x;
                            double vecY = yi - y + vec.y;
                            double vecZ = zi - z + vec.z;

                            double newDistance = (Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ)) + (vecX * vecX + vecY * vecY + vecZ * vecZ);

                            if (newDistance < distance) {
                                distance = newDistance;
                                xc = xi;
                                yc = yi;
                                zc = zi;
                            }
                        }
                    }
                }
                break;
        }

        switch (cellularReturnType) {
            case CELL_VALUE:
                return valCoord3D(0, xc, yc, zc);

            case NOISE_LOOKUP:
                double3 vec = CELL_3D[hash256(xc, yc, zc, seed)];
                return fBM3D(BASE_NOISETYPE, xc + vec.x, yc + vec.y, zc + vec.z, seed+123, BASE_OFFSET, BASE_H, 3, BASE_SEED_VARIATION);

            case DISTANCE:
                return distance - 1;
            default:
                return 0;
        }
    }


}
