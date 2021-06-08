package com.github.terefang.jmelange.randfractal.fnoise;

import com.github.terefang.jmelange.randfractal.AbstractFractalNoise;
import com.github.terefang.jmelange.randfractal.IFractalNoise;

public class TM_MultiFractal extends AbstractFractalNoise implements IFractalNoise
{

	@Override
	public double distort(double x, double y) 
	{
		double value = 1.0;
		double scale = 0.0;
		
		for(int i = 0; i<getOctaves(); i++)
		{
			value += (getNoise().getNoise2d(x, y) + getOffset()) * Math.pow(getLacunarity(), -(getH()*i));
			scale += (1.0 + getOffset()) * Math.pow(getLacunarity(), -(getH()*i));
			x*=getLacunarity();
			y*=getLacunarity();
		}
		
		return value/scale;
	}

	@Override
	public double distort(double x, double y, double z) 
	{
		double value = 1.0;
		double scale = 0.0;
		
		for(int i = 0; i<getOctaves(); i++)
		{
			value += (getNoise().getNoise2d(x, y) + getOffset()) * Math.pow(getLacunarity(), -(getH()*i));
			scale += (1.0 + getOffset()) * Math.pow(getLacunarity(), -(getH()*i));
			x*=getLacunarity();
			y*=getLacunarity();
		}
		
		return value/scale;
	}

}
