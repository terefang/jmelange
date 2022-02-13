package com.github.terefang.jmelange.randfractal.noise;

import com.github.terefang.jmelange.randfractal.AbstractNoise;
import com.github.terefang.jmelange.random.IRandom;
import com.github.terefang.jmelange.randfractal.noise.impl.TM3LatticeNoiseRef;

public class TM_GradientNoise extends AbstractNoise
{
	double[] gtab = null;
	
	@Override
	public void setSeed(long s) 
	{
		gtab = TM3LatticeNoiseRef.gtable(s);
	}

	@Override
	public void init() 
	{
	}

	@Override
	public double getNoise3d(double x, double y, double z) 
	{
		return TM3LatticeNoiseRef.gnoise(x, y, z, gtab);
	}

	@Override
	public void setPrng(IRandom p) {

	}

}
