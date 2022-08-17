package com.github.terefang.jmelange.randfractal.lite;

public class FnCellular2Edge extends FastNoiseLite
{
    public static final double singleCellular2Edge(int cellularDistanceFunction, int cellularReturnType, int seed, double x, double y)
    {
        int xr = fastRound(x);
        int yr = fastRound(y);

        double distance = 999999;
        double distance2 = 999999;

        switch (cellularDistanceFunction) {
            default:
            case EUCLIDEAN:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        double2 vec = CELL_2D[hash256(xi, yi, seed)];

                        double vecX = xi - x + vec.x;
                        double vecY = yi - y + vec.y;

                        double newDistance = vecX * vecX + vecY * vecY;

                        distance2 = Math.max(Math.min(distance2, newDistance), distance);
                        distance = Math.min(distance, newDistance);
                    }
                }
                break;
            case MANHATTAN:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        double2 vec = CELL_2D[hash256(xi, yi, seed)];

                        double vecX = xi - x + vec.x;
                        double vecY = yi - y + vec.y;

                        double newDistance = Math.abs(vecX) + Math.abs(vecY);

                        distance2 = Math.max(Math.min(distance2, newDistance), distance);
                        distance = Math.min(distance, newDistance);
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

                        distance2 = Math.max(Math.min(distance2, newDistance), distance);
                        distance = Math.min(distance, newDistance);
                    }
                }
                break;
        }

        switch (cellularReturnType) {
            case DISTANCE_2:
                return distance2 - 1;
            case DISTANCE_2_ADD:
                return distance2 + distance - 1;
            case DISTANCE_2_SUB:
                return distance2 - distance - 1;
            case DISTANCE_2_MUL:
                return distance2 * distance - 1;
            case DISTANCE_2_DIV:
                return distance / distance2 - 1;
            default:
                return 0;
        }
    }

    public static final double singleCellular2Edge(int cellularDistanceFunction, int cellularReturnType, int seed, double x, double y, double z)
    {
        int xr = fastRound(x);
        int yr = fastRound(y);
        int zr = fastRound(z);

        double distance = 999999;
        double distance2 = 999999;

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

                            distance2 = Math.max(Math.min(distance2, newDistance), distance);
                            distance = Math.min(distance, newDistance);
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

                            distance2 = Math.max(Math.min(distance2, newDistance), distance);
                            distance = Math.min(distance, newDistance);
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

                            distance2 = Math.max(Math.min(distance2, newDistance), distance);
                            distance = Math.min(distance, newDistance);
                        }
                    }
                }
                break;
            default:
                break;
        }

        switch (cellularReturnType) {
            case DISTANCE_2:
                return distance2 - 1;
            case DISTANCE_2_ADD:
                return distance2 + distance - 1;
            case DISTANCE_2_SUB:
                return distance2 - distance - 1;
            case DISTANCE_2_MUL:
                return distance2 * distance - 1;
            case DISTANCE_2_DIV:
                return distance / distance2 - 1;
            default:
                return 0;
        }
    }


}
