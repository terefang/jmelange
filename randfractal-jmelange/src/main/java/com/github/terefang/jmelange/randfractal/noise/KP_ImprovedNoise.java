package com.github.terefang.jmelange.randfractal.noise;

import com.github.terefang.jmelange.randfractal.AbstractNoise;
import com.github.terefang.jmelange.randfractal.IRandom;
import com.github.terefang.jmelange.randfractal.noise.impl.KenPerlinImprovedNoiseRef;

public class KP_ImprovedNoise extends AbstractNoise
{
	int[] p = null;
	
	@Override
	public void setSeed(long s) 
	{
		p = KenPerlinImprovedNoiseRef.seeded_permutation((int)
			(s ^ (s >>> 8) ^ (s >>> 16) 
				^ (s >>> 24) ^ (s >>> 32)
				^ (s >>> 40) ^ (s >>> 48)
				^ (s >>> 56)) & 0xff);
	}

	@Override
	public void init() 
	{
	}

	@Override
	public double getNoise3d(double x, double y, double z) 
	{
		return KenPerlinImprovedNoiseRef.noise(x, y, z, p);
	}

	@Override
	public void setPrng(IRandom p) {

	}

}
