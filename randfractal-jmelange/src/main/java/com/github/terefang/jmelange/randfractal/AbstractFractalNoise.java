package com.github.terefang.jmelange.randfractal;

import lombok.Data;

@Data
public abstract class AbstractFractalNoise implements IFractalNoise
{
    double H , lacunarity, octaves, offset;
    INoise noise;

    @Override
    public double distort(double x)
    {
        return distort(x, x+.333, x+.777);
    }
}