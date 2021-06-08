package com.github.terefang.jmelange.randfractal.noise;

import com.github.terefang.jmelange.randfractal.AbstractNoise;
import com.github.terefang.jmelange.randfractal.IRandom;
import com.github.terefang.jmelange.randfractal.noise.impl.KenPerlinNoiseRef;

public class KP_Noise extends AbstractNoise
{
	KenPerlinNoiseRef noise = new KenPerlinNoiseRef();
	
	@Override
	public void setSeed(long s) 
	{
		noise.seed = (int) ((s ^ (s >>> 32)) & 0xffffffff);
	}

	@Override
	public void init() 
	{
		noise.init();
	}

	@Override
	public double getNoise1d(double x) 
	{
		return noise.noise(x);
	}

	@Override
	public double getNoise2d(double x, double y) 
	{
		return noise.noise(x, y);
	}

	@Override
	public double getNoise3d(double x, double y, double z) 
	{
		return noise.noise(x, y, z);
	}

	@Override
	public void setPrng(IRandom p) {
		noise.setPrng(p);
	}
}
