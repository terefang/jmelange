package com.github.terefang.jmelange.randfractal;

import com.github.terefang.jmelange.randfractal.noise.*;

public interface INoise
{
    public void setSeed(long s);
    public void init();

    public double getNoise1d(double x);
    public double getNoise2d(double x, double y);
    public double getNoise3d(double x, double y, double z);

    public static INoise jgpFastNoise(long seed)
    {
        JGP_FastNoise _n =  new JGP_FastNoise();
        _n.setSeed(seed);
        _n.init();
        return _n;
    }

    public static INoise jgpFastValueNoise(long seed)
    {
        JGP_FastValueNoise _n =  new JGP_FastValueNoise();
        _n.setSeed(seed);
        _n.init();
        return _n;
    }

    public static INoise jlGradientCoherentNoise(long seed)
    {
        JL_GradientCoherentNoise _n =  new JL_GradientCoherentNoise();
        _n.setSeed(seed);
        _n.init();
        return _n;
    }

    public static INoise jlGradientNoise(long seed)
    {
        JL_GradientNoise _n =  new JL_GradientNoise();
        _n.setSeed(seed);
        _n.init();
        return _n;
    }

    public static INoise jlValueNoise(long seed)
    {
        JL_ValueNoise _n =  new JL_ValueNoise();
        _n.setSeed(seed);
        _n.init();
        return _n;
    }

    public static INoise kpImprovedNoise(long seed)
    {
        KP_ImprovedNoise _n =  new KP_ImprovedNoise();
        _n.setSeed(seed);
        _n.init();
        return _n;
    }

    public static INoise kpNoise(long seed)
    {
        KP_Noise _n =  new KP_Noise();
        _n.setSeed(seed);
        _n.init();
        return _n;
    }

    public static INoise kpNoise(IRandom _r, long _seed)
    {
        KP_Noise _n =  new KP_Noise();
        _n.setSeed(_seed);
        _n.setPrng(_r);
        _n.init();
        return _n;
    }

    public static INoise tmGradientNoise(long seed)
    {
        TM_GradientNoise _n =  new TM_GradientNoise();
        _n.setSeed(seed);
        _n.init();
        return _n;
    }

    public static INoise tmSparseConvolutionNoise(long seed)
    {
        TM_SparseConvolutionNoise _n =  new TM_SparseConvolutionNoise();
        _n.setSeed(seed);
        _n.init();
        return _n;
    }

    public static INoise tmValueNoise(long seed)
    {
        TM_ValueNoise _n =  new TM_ValueNoise();
        _n.setSeed(seed);
        _n.init();
        return _n;
    }


    public void setPrng(IRandom p);
}
