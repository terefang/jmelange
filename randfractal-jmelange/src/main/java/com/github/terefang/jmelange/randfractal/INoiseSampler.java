package com.github.terefang.jmelange.randfractal;

import com.github.terefang.jmelange.randfractal.sampler.GradientNoiseSampler;
import com.github.terefang.jmelange.randfractal.sampler.ImpulseNoiseSampler;
import com.github.terefang.jmelange.randfractal.sampler.PluginNoiseSampler;
import com.github.terefang.jmelange.randfractal.sampler.ValueNoiseSampler;

public interface INoiseSampler
{
    public double noise(double x, double y, double z);
    public double snoise(double x, double y, double z);

    // fractal sum
    public double fSum(double x, double y, double z);
    public double fSum(double x, double y, double z, double fMin, double fMax);
    public double fSum(double x, double y, double z, double fMin, double fMax, double gap);
    public double fSum(double x, double y, double z, double fMin, double fMax, double gap, double offset);

    // fractal sum of inversion
    public double fInvSum(double x, double y, double z);
    public double fInvSum(double x, double y, double z, double fMin, double fMax);
    public double fInvSum(double x, double y, double z, double fMin, double fMax, double gap);
    public double fInvSum(double x, double y, double z, double fMin, double fMax, double gap, double offset);

    // fractal brownian motion
    public double fBm(double x, double y, double z);
    public double fBm(double x, double y, double z, double H, double lacunarity);
    public double fBm(double x, double y, double z, double H, double lacunarity, double octaves);
    public double fBm(double x, double y, double z, double H, double lacunarity, double octaves, double offset);

    // multi-fractal
    public double mF(double x, double y, double z);
    public double mF(double x, double y, double z, double H, double lacunarity);
    public double mF(double x, double y, double z, double H, double lacunarity, double octaves);
    public double mF(double x, double y, double z, double H, double lacunarity, double octaves, double offset);

    // ridged-multi-fractal
    public double RmF(double x, double y, double z);
    public double RmF(double x, double y, double z, double gain);
    public double RmF(double x, double y, double z, double gain, double H, double lacunarity);
    public double RmF(double x, double y, double z, double gain, double H, double lacunarity, double octaves);
    public double RmF(double x, double y, double z, double gain, double H, double lacunarity, double octaves, double offset);

    public static INoiseSampler valueNoiseSampler(long seed)
    {
        ValueNoiseSampler _vn = new ValueNoiseSampler();
        _vn.seed(seed);
        return _vn;
    }

    public static INoiseSampler gradientNoiseSampler(long seed)
    {
        GradientNoiseSampler _vn = new GradientNoiseSampler();
        _vn.seed(seed);
        return _vn;
    }

    public static INoiseSampler impulseNoiseSampler(long seed)
    {
        ImpulseNoiseSampler _vn = new ImpulseNoiseSampler();
        _vn.seed(seed);
        return _vn;
    }

    public static INoiseSampler pluginNoiseSampler(INoise plugin)
    {
        PluginNoiseSampler _vn = new PluginNoiseSampler();
        _vn.setNoisePlugin(plugin);
        return _vn;
    }
}