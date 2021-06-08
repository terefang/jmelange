package com.github.terefang.jmelange.randfractal.xnoise;

/**
 * Vector versions of the standard noise functions. These are provided to
 * emulate standard renderman calls.This code was adapted mainly from the
 * mrclasses package by Gonzalo Garramuno
 * (http://sourceforge.net/projects/mrclasses/).
 */
public class PerlinVector {
	private static final double P1x = 0.34f;
	private static final double P1y = 0.66f;
	private static final double P1z = 0.237f;
	private static final double P2x = 0.011f;
	private static final double P2y = 0.845f;
	private static final double P2z = 0.037f;
	private static final double P3x = 0.34f;
	private static final double P3y = 0.12f;
	private static final double P3z = 0.9f;
	
	public static final Vector3 snoise(int[] perlin, double x) {
		return new Vector3(PerlinScalar.snoise(perlin, x + P1x), PerlinScalar.snoise(perlin, x + P2x), PerlinScalar.snoise(perlin, x + P3x));
	}
	
	public static final Vector3 snoise(int[] perlin, double x, double y) {
		return new Vector3(PerlinScalar.snoise(perlin, x + P1x, y + P1y), PerlinScalar.snoise(perlin, x + P2x, y + P2y), PerlinScalar.snoise(perlin, x + P3x, y + P3y));
	}
	
	public static final Vector3 snoise(int[] perlin, double x, double y, double z) {
		return new Vector3(PerlinScalar.snoise(perlin, x + P1x, y + P1y, z + P1z), PerlinScalar.snoise(perlin, x + P2x, y + P2y, z + P2z), PerlinScalar.snoise(perlin, x + P3x, y + P3y, z + P3z));
	}
	
	public static final Vector3 snoise(int[] perlin, double x, double y, double z, double t) {
		return new Vector3(PerlinScalar.snoise(perlin, x + P1x, y + P1y, z + P1z, t), PerlinScalar.snoise(perlin, x + P2x, y + P2y, z + P2z, t), PerlinScalar.snoise(perlin, x + P3x, y + P3y, z + P3z, t));
	}
	
	public static final Vector3 snoise(int[] perlin, Point2 p) {
		return snoise(perlin, p.x, p.y);
	}
	
	public static final Vector3 snoise(int[] perlin, Point3 p) {
		return snoise(perlin, p.x, p.y, p.z);
	}
	
	public static final Vector3 snoise(int[] perlin, Point3 p, double t) {
		return snoise(perlin, p.x, p.y, p.z, t);
	}
	
	public static final Vector3 noise(int[] perlin, double x) {
		return new Vector3(PerlinScalar.noise(perlin, x + P1x), PerlinScalar.noise(perlin, x + P2x), PerlinScalar.noise(perlin, x + P3x));
	}
	
	public static final Vector3 noise(int[] perlin, double x, double y) {
		return new Vector3(PerlinScalar.noise(perlin, x + P1x, y + P1y), PerlinScalar.noise(perlin, x + P2x, y + P2y), PerlinScalar.noise(perlin, x + P3x, y + P3y));
	}
	
	public static final Vector3 noise(int[] perlin, double x, double y, double z) {
		return new Vector3(PerlinScalar.noise(perlin, x + P1x, y + P1y, z + P1z), PerlinScalar.noise(perlin, x + P2x, y + P2y, z + P2z), PerlinScalar.noise(perlin, x + P3x, y + P3y, z + P3z));
	}
	
	public static final Vector3 noise(int[] perlin, double x, double y, double z, double t) {
		return new Vector3(PerlinScalar.noise(perlin, x + P1x, y + P1y, z + P1z, t), PerlinScalar.noise(perlin, x + P2x, y + P2y, z + P2z, t), PerlinScalar.noise(perlin, x + P3x, y + P3y, z + P3z, t));
	}
	
	public static final Vector3 noise(int[] perlin, Point2 p) {
		return noise(perlin, p.x, p.y);
	}
	
	public static final Vector3 noise(int[] perlin, Point3 p) {
		return noise(perlin, p.x, p.y, p.z);
	}
	
	public static final Vector3 noise(int[] perlin, Point3 p, double t) {
		return noise(perlin, p.x, p.y, p.z, t);
	}
	
	public static final Vector3 pnoise(int[] perlin, double x, double period) {
		return new Vector3(PerlinScalar.pnoise(perlin, x + P1x, period), PerlinScalar.pnoise(perlin, x + P2x, period), PerlinScalar.pnoise(perlin, x + P3x, period));
	}
	
	public static final Vector3 pnoise(int[] perlin, double x, double y, double w, double h) {
		return new Vector3(PerlinScalar.pnoise(perlin, x + P1x, y + P1y, w, h), PerlinScalar.pnoise(perlin, x + P2x, y + P2y, w, h), PerlinScalar.pnoise(perlin, x + P3x, y + P3y, w, h));
	}
	
	public static final Vector3 pnoise(int[] perlin, double x, double y, double z, double w, double h, double d) {
		return new Vector3(PerlinScalar.pnoise(perlin, x + P1x, y + P1y, z + P1z, w, h, d), PerlinScalar.pnoise(perlin, x + P2x, y + P2y, z + P2z, w, h, d), PerlinScalar.pnoise(perlin, x + P3x, y + P3y, z + P3z, w, h, d));
	}
	
	public static final Vector3 pnoise(int[] perlin, double x, double y, double z, double t, double w, double h, double d, double p) {
		return new Vector3(PerlinScalar.pnoise(perlin, x + P1x, y + P1y, z + P1z, t, w, h, d, p), PerlinScalar.pnoise(perlin, x + P2x, y + P2y, z + P2z, t, w, h, d, p), PerlinScalar.pnoise(perlin, x + P3x, y + P3y, z + P3z, t, w, h, d, p));
	}
	
	public static final Vector3 pnoise(int[] perlin, Point2 p, double periodx, double periody) {
		return pnoise(perlin, p.x, p.y, periodx, periody);
	}
	
	public static final Vector3 pnoise(int[] perlin, Point3 p, Vector3 period) {
		return pnoise(perlin, p.x, p.y, p.z, period.x, period.y, period.z);
	}
	
	public static final Vector3 pnoise(int[] perlin, Point3 p, double t, Vector3 pperiod, double tperiod) {
		return pnoise(perlin, p.x, p.y, p.z, t, pperiod.x, pperiod.y, pperiod.z, tperiod);
	}
	
	public static final Vector3 spnoise(int[] perlin, double x, double period) {
		return new Vector3(PerlinScalar.spnoise(perlin, x + P1x, period), PerlinScalar.spnoise(perlin, x + P2x, period), PerlinScalar.spnoise(perlin, x + P3x, period));
	}
	
	public static final Vector3 spnoise(int[] perlin, double x, double y, double w, double h) {
		return new Vector3(PerlinScalar.spnoise(perlin, x + P1x, y + P1y, w, h), PerlinScalar.spnoise(perlin, x + P2x, y + P2y, w, h), PerlinScalar.spnoise(perlin, x + P3x, y + P3y, w, h));
	}
	
	public static final Vector3 spnoise(int[] perlin, double x, double y, double z, double w, double h, double d) {
		return new Vector3(PerlinScalar.spnoise(perlin, x + P1x, y + P1y, z + P1z, w, h, d), PerlinScalar.spnoise(perlin, x + P2x, y + P2y, z + P2z, w, h, d), PerlinScalar.spnoise(perlin, x + P3x, y + P3y, z + P3z, w, h, d));
	}
	
	public static final Vector3 spnoise(int[] perlin, double x, double y, double z, double t, double w, double h, double d, double p) {
		return new Vector3(PerlinScalar.spnoise(perlin, x + P1x, y + P1y, z + P1z, t, w, h, d, p), PerlinScalar.spnoise(perlin, x + P2x, y + P2y, z + P2z, t, w, h, d, p), PerlinScalar.spnoise(perlin, x + P3x, y + P3y, z + P3z, t, w, h, d, p));
	}
	
	public static final Vector3 spnoise(int[] perlin, Point2 p, double periodx, double periody) {
		return spnoise(perlin, p.x, p.y, periodx, periody);
	}
	
	public static final Vector3 spnoise(int[] perlin, Point3 p, Vector3 period) {
		return spnoise(perlin, p.x, p.y, p.z, period.x, period.y, period.z);
	}
	
	public static final Vector3 spnoise(int[] perlin, Point3 p, double t, Vector3 pperiod, double tperiod) {
		return spnoise(perlin, p.x, p.y, p.z, t, pperiod.x, pperiod.y, pperiod.z, tperiod);
	}
}