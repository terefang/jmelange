package com.github.terefang.jmelange.randfractal.noise.impl;

//JAVA REFERENCE IMPLEMENTATION OF IMPROVED NOISE - COPYRIGHT 2002 KEN PERLIN.

import com.github.terefang.jmelange.randfractal.xnoise.PerlinScalar;

/**
Improved version of {@link Noise}.
<p>
Algorithm:<br>
Find unit cube that contains point (x, y, z).<br>
Find the relative x,y,z of the point in the cube.<br>
Compute the fade curves for each of x,y,z.<br>
Hash coordinates of the eight cube corners and add blended results from the
eight corners of the cube.<br>
Return the sum.<br>
@author Ken Perlin 2001
 */

public class KenPerlinImprovedNoiseRef 
{
	/**
    Computes the Perlin noise function value at the point (x, y, z).
    @param x x coordinate
    @param y y coordinate
    @param z z coordinate
    @return the noise function value at (x, y, z)
	 */
	static public double noise(double x, double y, double z, int kernal[]) {
		return PerlinScalar.snoise(kernal, x, y, z);
	}
	static public int[] seeded_permutation(int s)
	{
		return PerlinScalar.permutation(s);
	}
}
