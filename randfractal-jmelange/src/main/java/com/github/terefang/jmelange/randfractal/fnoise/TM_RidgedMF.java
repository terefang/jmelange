package com.github.terefang.jmelange.randfractal.fnoise;

public class TM_RidgedMF extends TM_MultiFractal {

	@Override
	public double distort(double x, double y, double z) 
	{
		return 1.0 - Math.abs(super.distort(x, y, z));
	}
}
