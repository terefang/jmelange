package com.github.terefang.jmelange.randfractal;

import com.github.terefang.jmelange.randfractal.utils.WgUtils;

public abstract class AbstractNoise implements INoise
{
    public void seed(long s)
    {
        this.setSeed(s);
        this.init();
    }

    public double noise(double x, double y, double z)
    {
        return this.getNoise3d(x,y,z);
    }

    public abstract void setSeed(long s);

    public abstract void init();

    @Override
    public double getNoise1d(double x)
    {
        return getNoise3d(x, x+.33, x+.66);
    }

    @Override
    public double getNoise2d(double x, double y)
    {
        return getNoise3d(x, y, WgUtils.lerp(1.5, x, y));
    }

    public abstract double getNoise3d(double x, double y, double z);

}
