package com.github.terefang.jmelange.randfractal.noise;

import com.github.terefang.jmelange.randfractal.AbstractNoise;
import com.github.terefang.jmelange.random.IRandom;
import com.github.terefang.jmelange.randfractal.noise.impl.TM3LatticeNoiseRef;

public class TM_SparseConvolutionNoise extends AbstractNoise
{
	double[] itab = null;
	
	@Override
	public void setSeed(long s) 
	{
		itab = TM3LatticeNoiseRef.itable(s);
	}

	@Override
	public void init() 
	{
	}

	@Override
	public double getNoise2d(double x, double y) 
	{
		return TM3LatticeNoiseRef.scnoise(x, y, itab);
	}

	@Override
	public double getNoise3d(double x, double y, double z) 
	{
		return TM3LatticeNoiseRef.scnoise(x, y, z, itab);
	}

	@Override
	public void setPrng(IRandom p) {

	}

}
