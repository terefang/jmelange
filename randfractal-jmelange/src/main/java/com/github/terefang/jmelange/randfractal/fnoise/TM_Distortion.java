package com.github.terefang.jmelange.randfractal.fnoise;

import com.github.terefang.jmelange.randfractal.AbstractFractalNoise;
import com.github.terefang.jmelange.randfractal.IFractalNoise;

public class TM_Distortion extends AbstractFractalNoise implements IFractalNoise{

	@Override
	public double distort(double x, double y) 
	{
		double[] t = new double[2];
		
		t[0] = x + 0.5;
		t[1] = y + 0.5;
		
		t = vec_noise(t[0], t[1]);
		
		t[0] *= getOffset();
		t[1] *= getOffset();

		t[0] += x;
		t[1] += y;
		
		return getNoise().getNoise2d(t[0], t[1]);
	}
	
	@Override
	public double distort(double x, double y, double z) 
	{
		double[] t = new double[3];
		
		t[0] = x + 0.5;
		t[1] = y + 0.5;
		t[2] = z + 0.5;
		
		t = vec_noise(t[0], t[1], t[2]);
		
		t[0] *= getOffset();
		t[1] *= getOffset();
		t[2] *= getOffset();

		t[0] += x;
		t[1] += y;
		t[2] += z;
		
		return getNoise().getNoise3d(t[0], t[1], t[2]);
	}
	
	double[] vec_noise(double x, double y)
	{
		double[] r = new double[2];
		r[0] = getNoise().getNoise2d(x, y);
		r[1] = getNoise().getNoise2d(x+.5, y+.5);
		return r;
	}

	double[] vec_noise(double x, double y, double z)
	{
		double[] r = new double[3];
		r[0] = getNoise().getNoise3d(x, y, z);
		r[1] = getNoise().getNoise3d(x+.33, y+.33, z+.33);
		r[2] = getNoise().getNoise3d(x+.77, y+.77, z+.77);
		return r;
	}
}
