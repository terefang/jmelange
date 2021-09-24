package com.github.terefang.jmelange.randfractal.lite;

public class FnCellular2Edge extends FastNoiseLite
{
    public static final float singleCellular2Edge(int cellularDistanceFunction, int cellularReturnType, int seed, float x, float y)
    {
        int xr = fastRound(x);
        int yr = fastRound(y);

        float distance = 999999;
        float distance2 = 999999;

        switch (cellularDistanceFunction) {
            default:
            case EUCLIDEAN:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        Float2 vec = CELL_2D[hash256(xi, yi, seed)];

                        float vecX = xi - x + vec.x;
                        float vecY = yi - y + vec.y;

                        float newDistance = vecX * vecX + vecY * vecY;

                        distance2 = Math.max(Math.min(distance2, newDistance), distance);
                        distance = Math.min(distance, newDistance);
                    }
                }
                break;
            case MANHATTAN:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        Float2 vec = CELL_2D[hash256(xi, yi, seed)];

                        float vecX = xi - x + vec.x;
                        float vecY = yi - y + vec.y;

                        float newDistance = Math.abs(vecX) + Math.abs(vecY);

                        distance2 = Math.max(Math.min(distance2, newDistance), distance);
                        distance = Math.min(distance, newDistance);
                    }
                }
                break;
            case NATURAL:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        Float2 vec = CELL_2D[hash256(xi, yi, seed)];

                        float vecX = xi - x + vec.x;
                        float vecY = yi - y + vec.y;

                        float newDistance = (Math.abs(vecX) + Math.abs(vecY)) + (vecX * vecX + vecY * vecY);

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

    public static final float singleCellular2Edge(int cellularDistanceFunction, int cellularReturnType, int seed, float x, float y, float z)
    {
        int xr = fastRound(x);
        int yr = fastRound(y);
        int zr = fastRound(z);

        float distance = 999999;
        float distance2 = 999999;

        switch (cellularDistanceFunction) {
            case EUCLIDEAN:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        for (int zi = zr - 1; zi <= zr + 1; zi++) {
                            Float3 vec = CELL_3D[hash256(xi, yi, zi, seed)];

                            float vecX = xi - x + vec.x;
                            float vecY = yi - y + vec.y;
                            float vecZ = zi - z + vec.z;

                            float newDistance = vecX * vecX + vecY * vecY + vecZ * vecZ;

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
                            Float3 vec = CELL_3D[hash256(xi, yi, zi, seed)];

                            float vecX = xi - x + vec.x;
                            float vecY = yi - y + vec.y;
                            float vecZ = zi - z + vec.z;

                            float newDistance = Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ);

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
                            Float3 vec = CELL_3D[hash256(xi, yi, zi, seed)];

                            float vecX = xi - x + vec.x;
                            float vecY = yi - y + vec.y;
                            float vecZ = zi - z + vec.z;

                            float newDistance = (Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ)) + (vecX * vecX + vecY * vecY + vecZ * vecZ);

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
