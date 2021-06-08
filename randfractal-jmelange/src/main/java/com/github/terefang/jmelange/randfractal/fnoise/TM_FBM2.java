package com.github.terefang.jmelange.randfractal.fnoise;

import com.github.terefang.jmelange.randfractal.AbstractFractalNoise;
import com.github.terefang.jmelange.randfractal.IFractalNoise;

public class TM_FBM2 extends AbstractFractalNoise implements IFractalNoise {

	@Override
	public double distort(double x, double y) 
	{
		double  freq = 1.0f;
		double  amp = 1.0f;
		double  max = 1.0f;
		double  total = getNoise().getNoise2d(x, y);
		int i;
		
		for (i = 1; i < getOctaves(); ++i) {
			freq *= getLacunarity();
			amp *= getH();
			max += amp;
			total += getNoise().getNoise2d(x * freq, y * freq) * amp;
		}
		return total / max;
	}

	@Override
	public double distort(double x, double y, double z) 
	{
		double  freq = 1.0f;
		double  amp = 1.0f;
		double  max = 1.0f;
		double  total = getNoise().getNoise3d(x, y, z);
		int i;
		
		for (i = 1; i < getOctaves(); ++i) {
			freq *= getLacunarity();
			amp *= getH();
			max += amp;
			total += getNoise().getNoise3d(x * freq, y * freq, z * freq) * amp;
		}
		return total / max;
	}

}
