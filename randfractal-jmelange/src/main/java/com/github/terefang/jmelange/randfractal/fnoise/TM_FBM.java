package com.github.terefang.jmelange.randfractal.fnoise;

import com.github.terefang.jmelange.randfractal.AbstractFractalNoise;
import com.github.terefang.jmelange.randfractal.IFractalNoise;

public class TM_FBM extends AbstractFractalNoise implements IFractalNoise {

	@Override
	public double distort(double x, double y) 
	{
		double value = 0.0;
		double scale = 0.0;
		
		for(int i = 0; i<getOctaves(); i++)
		{
			value += getNoise().getNoise2d(x, y) * Math.pow(getLacunarity(), -(getH()*i));
			scale += Math.pow(getLacunarity(), -(getH()*i));
			x*=getLacunarity();
			y*=getLacunarity();
		}
		
		double reminder = getOctaves() - (int)getOctaves();
		if(reminder > 0.0)
		{
			value += reminder * getNoise().getNoise2d(x, y) * Math.pow(getLacunarity(), -(getH()*(int)getOctaves()));
			scale += reminder * Math.pow(getLacunarity(), -(getH()*(int)getOctaves()));
		}
		return value/scale;
	}

	@Override
	public double distort(double x, double y, double z) 
	{
		double value = 0.0;
		double scale = 0.0;
		
		for(int i = 0; i<getOctaves(); i++)
		{
			value += getNoise().getNoise3d(x, y, z) * Math.pow(getLacunarity(), -(getH()*i));
			scale += Math.pow(getLacunarity(), -(getH()*i));
			x*=getLacunarity();
			y*=getLacunarity();
			z*=getLacunarity();
		}
		
		double reminder = getOctaves() - (int)getOctaves();
		if(reminder > 0.0)
		{
			value += reminder * getNoise().getNoise3d(x, y, z) * Math.pow(getLacunarity(), -(getH()*(int)getOctaves()));
			scale += reminder * Math.pow(getLacunarity(), -(getH()*(int)getOctaves()));
		}
		return value/scale;
	}

}
