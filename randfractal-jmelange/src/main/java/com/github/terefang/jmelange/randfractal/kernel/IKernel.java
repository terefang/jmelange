package com.github.terefang.jmelange.randfractal.kernel;

import com.github.terefang.jmelange.randfractal.Noisefield;

/**
 * Created by fredo on 08.12.15.
 */
public interface IKernel
{
	public int getWidth();
	public int getHeight();
	public String getName();
	public double calculateSample(Noisefield nf, int x, int y);
}
