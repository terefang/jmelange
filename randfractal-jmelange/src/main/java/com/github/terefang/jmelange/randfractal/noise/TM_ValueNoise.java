package com.github.terefang.jmelange.randfractal.noise;

import com.github.terefang.jmelange.randfractal.AbstractNoise;
import com.github.terefang.jmelange.randfractal.IRandom;
import com.github.terefang.jmelange.randfractal.noise.impl.TM3LatticeNoiseRef;

public class TM_ValueNoise extends AbstractNoise
{
	double[] vtab = new double[TM3LatticeNoiseRef.TABSIZE];
	
	@Override
	public void setSeed(long s) 
	{
		vtab = TM3LatticeNoiseRef.vtable(s);
	}

	@Override
	public void init() 
	{
	}

	@Override
	public double getNoise3d(double x, double y, double z) 
	{
		return TM3LatticeNoiseRef.vnoise(x, y, z, vtab);
	}

	@Override
	public void setPrng(IRandom p) {

	}

}
