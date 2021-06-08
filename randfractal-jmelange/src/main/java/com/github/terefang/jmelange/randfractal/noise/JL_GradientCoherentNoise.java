package com.github.terefang.jmelange.randfractal.noise;

import com.github.terefang.jmelange.randfractal.INoise;
import com.github.terefang.jmelange.randfractal.IRandom;
import com.github.terefang.jmelange.randfractal.noise.impl.JlibNoiseRef;

public class JL_GradientCoherentNoise implements INoise
{
	long seedValue = 0;
    /* ------------------------------------------------------- */
    // MAIN INTERFACE
    /* ------------------------------------------------------- */
	@Override
	public void setSeed(long s) 
	{
		seedValue = s;
	}

	@Override
	public void init() 
	{
	}

	@Override
	public double getNoise1d(double x) 
	{
		return getNoise2d(x, 0);
	}

	@Override
	public double getNoise2d(double x, double y) 
	{
		return getNoise3d(x, y, 0);
	}

	@Override
	public double getNoise3d(double x, double y, double z) 
	{
		return JlibNoiseRef.ValueCoherentNoise3D(x, y, z, (int) seedValue, JlibNoiseRef.NoiseQuality.FAST);
	}

	@Override
	public void setPrng(IRandom p) {

	}

	/* ------------------------------------------------------- */
}
