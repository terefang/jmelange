package com.github.terefang.jmelange.randfractal;

public interface IFractalNoise
{
    double getOffset();

    void setOffset(double offset);

    INoise getNoise();

    void setNoise(INoise noise);

    double getH();

    void setH(double h);

    double getLacunarity();

    void setLacunarity(double lacunarity);

    double getOctaves();

    void setOctaves(double octaves);

    double distort(double x);

    double distort(double x, double y);

    double distort(double x, double y, double z);
}
