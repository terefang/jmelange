package com.github.terefang.jmelange.randfractal.noise;


import com.github.terefang.jmelange.randfractal.AbstractNoise;
import com.github.terefang.jmelange.randfractal.IRandom;
import com.github.terefang.jmelange.randfractal.noise.impl.JavaGameProgFastNoiseRef;

public class JGP_FastNoise extends AbstractNoise
{
	int seed = 0;
	@Override
	public void setSeed(long s) 
	{
		seed = (int) (s ^ (s>>>32));
	}

	@Override
	public void init() 
	{
	}

	@Override
	public double getNoise2d(double x, double y) 
	{
		int oct = 1;
		//int zz = JavaGameProgFastNoiseRef.noise(x*z, y*z, oct, true, false, seed);
		//return (zz / ((double)(1<<(13-oct))));
		
		return JavaGameProgFastNoiseRef.noise(x, y,oct,true,true, seed);
	}

	@Override
	public double getNoise3d(double x, double y, double z) 
	{
		if(z==0.0)
			z=1.0;
		
		int oct = 1;
		//int zz = JavaGameProgFastNoiseRef.noise(x*z, y*z, oct, true, false, seed);
		//return (zz / ((double)(1<<(13-oct))));
		
		return JavaGameProgFastNoiseRef.noise(x+z, y+z,oct,true,true, seed);
	}

	@Override
	public void setPrng(IRandom p) {

	}


}
