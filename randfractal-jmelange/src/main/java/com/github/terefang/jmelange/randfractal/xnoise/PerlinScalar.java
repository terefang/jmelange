package com.github.terefang.jmelange.randfractal.xnoise;

/**
 * FastNoise function from Ken Perlin. Additional routines are provided to emulate
 * standard Renderman calls. This code was adapted mainly from the mrclasses
 * package by Gonzalo Garramuno (http://sourceforge.net/projects/mrclasses/).
 *
 * @link http://mrl.nyu.edu/~perlin/noise/
 */
public final class PerlinScalar {
	private static final double[] G1 = { -1, 1 };
	private static final double[][] G2 = { { 1, 0 }, { -1, 0 }, { 0, 1 },
			{ 0, -1 } };
	private static final double[][] G3 = { { 1, 1, 0 }, { -1, 1, 0 },
			{ 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 },
			{ 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 },
			{ 0, 1, -1 }, { 0, -1, -1 }, { 1, 1, 0 }, { -1, 1, 0 },
			{ 0, -1, 1 }, { 0, -1, -1 } };
	private static final double[][] G4 = { { -1, -1, -1, 0 }, { -1, -1, 1, 0 },
			{ -1, 1, -1, 0 }, { -1, 1, 1, 0 }, { 1, -1, -1, 0 },
			{ 1, -1, 1, 0 }, { 1, 1, -1, 0 }, { 1, 1, 1, 0 },
			{ -1, -1, 0, -1 }, { -1, 1, 0, -1 }, { 1, -1, 0, -1 },
			{ 1, 1, 0, -1 }, { -1, -1, 0, 1 }, { -1, 1, 0, 1 },
			{ 1, -1, 0, 1 }, { 1, 1, 0, 1 }, { -1, 0, -1, -1 },
			{ 1, 0, -1, -1 }, { -1, 0, -1, 1 }, { 1, 0, -1, 1 },
			{ -1, 0, 1, -1 }, { 1, 0, 1, -1 }, { -1, 0, 1, 1 }, { 1, 0, 1, 1 },
			{ 0, -1, -1, -1 }, { 0, -1, -1, 1 }, { 0, -1, 1, -1 },
			{ 0, -1, 1, 1 }, { 0, 1, -1, -1 }, { 0, 1, -1, 1 },
			{ 0, 1, 1, -1 }, { 0, 1, 1, 1 } };
	private static final int[] PERLIN = { 151, 160, 137, 91, 90, 15, 131, 13, 201,
			95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37,
			240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62,
			94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56,
			87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139,
			48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133,
			230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25,
			63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200,
			196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3,
			64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255,
			82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
			223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153,
			101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79,
			113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242,
			193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249,
			14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204,
			176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222,
			114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180,
			151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7,
			225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6,
			148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35,
			11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171,
			168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158,
			231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55,
			46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73,
			209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188,
			159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250,
			124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206,
			59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119,
			248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9,
			129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185,
			112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12,
			191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192,
			214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45,
			127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243,
			141, 128, 195, 78, 66, 215, 61, 156, 180 };
	
	public static final double snoise(int[] perlin, double x) {
		if(perlin == null) perlin = PERLIN;
		int xf = (int) Math.floor(x);
		int X = xf & 255;
		x -= xf;
		double u = fade(x);
		int A = perlin[X], B = perlin[X + 1];
		return lerp(u, grad(perlin[A], x), grad(perlin[B], x - 1));
	}
	
	public static final double snoise(int[] perlin, double x, double y) {
		if(perlin == null) perlin = PERLIN;
		int xf = (int) Math.floor(x);
		int yf = (int) Math.floor(y);
		int X = xf & 255;
		int Y = yf & 255;
		x -= xf;
		y -= yf;
		double u = fade(x);
		double v = fade(y);
		int A = perlin[X] + Y, B = perlin[X + 1] + Y;
		return lerp(v, lerp(u, grad(perlin[A], x, y), grad(perlin[B], x - 1, y)), lerp(u, grad(perlin[A + 1], x, y - 1), grad(perlin[B + 1], x - 1, y - 1)));
	}
	
	public static final double snoise(int[] perlin, double x, double y, double z) {
		if(perlin == null) perlin = PERLIN;
		int xf = (int) Math.floor(x);
		int yf = (int) Math.floor(y);
		int zf = (int) Math.floor(z);
		int X = xf & 255;
		int Y = yf & 255;
		int Z = zf & 255;
		x -= xf;
		y -= yf;
		z -= zf;
		double u = fade(x);
		double v = fade(y);
		double w = fade(z);
		int A = perlin[X] + Y, AA = perlin[A] + Z, AB = perlin[A + 1] + Z, B = perlin[X + 1] + Y, BA = perlin[B] + Z, BB = perlin[B + 1] + Z;
		return lerp(w, lerp(v, lerp(u, grad(perlin[AA], x, y, z), grad(perlin[BA], x - 1, y, z)), lerp(u, grad(perlin[AB], x, y - 1, z), grad(perlin[BB], x - 1, y - 1, z))), lerp(v, lerp(u, grad(perlin[AA + 1], x, y, z - 1), grad(perlin[BA + 1], x - 1, y, z - 1)), lerp(u, grad(perlin[AB + 1], x, y - 1, z - 1), grad(perlin[BB + 1], x - 1, y - 1, z - 1))));
	}
	
	public static final double snoise(int[] perlin, double x, double y, double z, double w) {
		if(perlin == null) perlin = PERLIN;
		int xf = (int) Math.floor(x);
		int yf = (int) Math.floor(y);
		int zf = (int) Math.floor(z);
		int wf = (int) Math.floor(w);
		int X = xf & 255;
		int Y = yf & 255;
		int Z = zf & 255;
		int W = wf & 255;
		x -= xf;
		y -= yf;
		z -= zf;
		w -= wf;
		double u = fade(x);
		double v = fade(y);
		double t = fade(z);
		double s = fade(w);
		int A = perlin[X] + Y, AA = perlin[A] + Z, AB = perlin[A + 1] + Z, B = perlin[X + 1] + Y, BA = perlin[B] + Z, BB = perlin[B + 1] + Z, AAA = perlin[AA] + W, AAB = perlin[AA + 1] + W, ABA = perlin[AB] + W, ABB = perlin[AB + 1] + W, BAA = perlin[BA] + W, BAB = perlin[BA + 1] + W, BBA = perlin[BB] + W, BBB = perlin[BB + 1] + W;
		return lerp(s, lerp(t, lerp(v, lerp(u, grad(perlin[AAA], x, y, z, w), grad(perlin[BAA], x - 1, y, z, w)), lerp(u, grad(perlin[ABA], x, y - 1, z, w), grad(perlin[BBA], x - 1, y - 1, z, w))), lerp(v, lerp(u, grad(perlin[AAB], x, y, z - 1, w), grad(perlin[BAB], x - 1, y, z - 1, w)), lerp(u, grad(perlin[ABB], x, y - 1, z - 1, w), grad(perlin[BBB], x - 1, y - 1, z - 1, w)))), lerp(t, lerp(v, lerp(u, grad(perlin[AAA + 1], x, y, z, w - 1), grad(perlin[BAA + 1], x - 1, y, z, w - 1)), lerp(u, grad(perlin[ABA + 1], x, y - 1, z, w - 1), grad(perlin[BBA + 1], x - 1, y - 1, z, w - 1))), lerp(v, lerp(u, grad(perlin[AAB + 1], x, y, z - 1, w - 1), grad(perlin[BAB + 1], x - 1, y, z - 1, w - 1)), lerp(u, grad(perlin[ABB + 1], x, y - 1, z - 1, w - 1), grad(perlin[BBB + 1], x - 1, y - 1, z - 1, w - 1)))));
	}
	
	public static final double snoise(int[] perlin, Point2 p) {
		if(perlin == null) perlin = PERLIN;
		return snoise(perlin, p.x, p.y);
	}
	
	public static final double snoise(int[] perlin, Point3 p) {
		if(perlin == null) perlin = PERLIN;
		return snoise(perlin, p.x, p.y, p.z);
	}
	
	public static final double snoise(int[] perlin, Point3 p, double t) {
		if(perlin == null) perlin = PERLIN;
		return snoise(perlin, p.x, p.y, p.z, t);
	}
	
	public static final double noise(int[] perlin, double x) {
		if(perlin == null) perlin = PERLIN;
		return 0.5f + 0.5f * snoise(perlin, x);
	}
	
	public static final double noise(int[] perlin, double x, double y) {
		if(perlin == null) perlin = PERLIN;
		return 0.5f + 0.5f * snoise(perlin, x, y);
	}
	
	public static final double noise(int[] perlin, double x, double y, double z) {
		if(perlin == null) perlin = PERLIN;
		return 0.5f + 0.5f * snoise(perlin, x, y, z);
	}
	
	public static final double noise(int[] perlin, double x, double y, double z, double t) {
		if(perlin == null) perlin = PERLIN;
		return 0.5f + 0.5f * snoise(perlin, x, y, z, t);
	}
	
	public static final double noise(int[] perlin, Point2 p) {
		if(perlin == null) perlin = PERLIN;
		return 0.5f + 0.5f * snoise(perlin, p.x, p.y);
	}
	
	public static final double noise(int[] perlin, Point3 p) {
		return 0.5f + 0.5f * snoise(perlin, p.x, p.y, p.z);
	}
	
	public static final double noise(int[] perlin, Point3 p, double t) {
		if(perlin == null) perlin = PERLIN;
		return 0.5f + 0.5f * snoise(perlin, p.x, p.y, p.z, t);
	}
	
	public static final double pnoise(int[] perlin, double xi, double period) {
		if(perlin == null) perlin = PERLIN;
		double x = (xi % period) + ((xi < 0) ? period : 0);
		return ((period - x) * noise(perlin, x) + x * noise(perlin, x - period)) / period;
	}
	
	public static final double pnoise(int[] perlin, double xi, double yi, double w, double h) {
		if(perlin == null) perlin = PERLIN;
		double x = (xi % w) + ((xi < 0) ? w : 0);
		double y = (yi % h) + ((yi < 0) ? h : 0);
		double w_x = w - x;
		double h_y = h - y;
		double x_w = x - w;
		double y_h = y - h;
		return (noise(perlin, x, y) * (w_x) * (h_y) + noise(perlin, x_w, y) * (x) * (h_y) + noise(perlin, x_w, y_h) * (x) * (y) + noise(perlin, x, y_h) * (w_x) * (y)) / (w * h);
	}
	
	public static final double pnoise(int[] perlin, double xi, double yi, double zi, double w, double h, double d) {
		if(perlin == null) perlin = PERLIN;
		double x = (xi % w) + ((xi < 0) ? w : 0);
		double y = (yi % h) + ((yi < 0) ? h : 0);
		double z = (zi % d) + ((zi < 0) ? d : 0);
		double w_x = w - x;
		double h_y = h - y;
		double d_z = d - z;
		double x_w = x - w;
		double y_h = y - h;
		double z_d = z - d;
		double xy = x * y;
		double h_yXd_z = h_y * d_z;
		double h_yXz = h_y * z;
		double w_xXy = w_x * y;
		return (noise(perlin, x, y, z) * (w_x) * h_yXd_z + noise(perlin, x, y_h, z) * w_xXy * (d_z) + noise(perlin, x_w, y, z) * (x) * h_yXd_z + noise(perlin, x_w, y_h, z) * (xy) * (d_z) + noise(perlin, x_w, y_h, z_d) * (xy) * (z) + noise(perlin, x, y, z_d) * (w_x) * h_yXz + noise(perlin, x, y_h, z_d) * w_xXy * (z) + noise(perlin, x_w, y, z_d) * (x) * h_yXz) / (w * h * d);
	}
	
	public static final double pnoise(int[] perlin, double xi, double yi, double zi, double ti, double w, double h, double d, double p) {
		if(perlin == null) perlin = PERLIN;
		double x = (xi % w) + ((xi < 0) ? w : 0);
		double y = (yi % h) + ((yi < 0) ? h : 0);
		double z = (zi % d) + ((zi < 0) ? d : 0);
		double t = (ti % p) + ((ti < 0) ? p : 0);
		double w_x = w - x;
		double h_y = h - y;
		double d_z = d - z;
		double p_t = p - t;
		double x_w = x - w;
		double y_h = y - h;
		double z_d = z - d;
		double t_p = t - p;
		double xy = x * y;
		double d_zXp_t = (d_z) * (p_t);
		double zXp_t = z * (p_t);
		double zXt = z * t;
		double d_zXt = d_z * t;
		double w_xXy = w_x * y;
		double w_xXh_y = w_x * h_y;
		double xXh_y = x * h_y;
		return (noise(perlin, x, y, z, t) * (w_xXh_y) * d_zXp_t + noise(perlin, x_w, y, z, t) * (xXh_y) * d_zXp_t + noise(perlin, x_w, y_h, z, t) * (xy) * d_zXp_t + noise(perlin, x, y_h, z, t) * (w_xXy) * d_zXp_t + noise(perlin, x_w, y_h, z_d, t) * (xy) * (zXp_t) + noise(perlin, x, y, z_d, t) * (w_xXh_y) * (zXp_t) + noise(perlin, x, y_h, z_d, t) * (w_xXy) * (zXp_t) + noise(perlin, x_w, y, z_d, t) * (xXh_y) * (zXp_t) + noise(perlin, x, y, z, t_p) * (w_xXh_y) * (d_zXt) + noise(perlin, x_w, y, z, t_p) * (xXh_y) * (d_zXt) + noise(perlin, x_w, y_h, z, t_p) * (xy) * (d_zXt) + noise(perlin, x, y_h, z, t_p) * (w_xXy) * (d_zXt) + noise(perlin, x_w, y_h, z_d, t_p) * (xy) * (zXt) + noise(perlin, x, y, z_d, t_p) * (w_xXh_y) * (zXt) + noise(perlin, x, y_h, z_d, t_p) * (w_xXy) * (zXt) + noise(perlin, x_w, y, z_d, t_p) * (xXh_y) * (zXt)) / (w * h * d * t);
	}
	
	public static final double pnoise(int[] perlin, Point2 p, double periodx, double periody) {
		if(perlin == null) perlin = PERLIN;
		return pnoise(perlin, p.x, p.y, periodx, periody);
	}
	
	public static final double pnoise(int[] perlin, Point3 p, Vector3 period) {
		if(perlin == null) perlin = PERLIN;
		return pnoise(perlin, p.x, p.y, p.z, period.x, period.y, period.z);
	}
	
	public static final double pnoise(int[] perlin, Point3 p, double t, Vector3 pperiod, double tperiod) {
		if(perlin == null) perlin = PERLIN;
		return pnoise(perlin, p.x, p.y, p.z, t, pperiod.x, pperiod.y, pperiod.z, tperiod);
	}
	
	public static final double spnoise(int[] perlin, double xi, double period) {
		if(perlin == null) perlin = PERLIN;
		double x = (xi % period) + ((xi < 0) ? period : 0);
		return (((period - x) * snoise(perlin, x) + x * snoise(perlin, x - period)) / period);
	}
	
	public static final double spnoise(int[] perlin, double xi, double yi, double w, double h) {
		if(perlin == null) perlin = PERLIN;
		double x = (xi % w) + ((xi < 0) ? w : 0);
		double y = (yi % h) + ((yi < 0) ? h : 0);
		double w_x = w - x;
		double h_y = h - y;
		double x_w = x - w;
		double y_h = y - h;
		return ((snoise(perlin, x, y) * (w_x) * (h_y) + snoise(perlin, x_w, y) * (x) * (h_y) + snoise(perlin, x_w, y_h) * (x) * (y) + snoise(perlin, x, y_h) * (w_x) * (y)) / (w * h));
	}
	
	public static final double spnoise(int[] perlin, double xi, double yi, double zi, double w, double h, double d) {
		if(perlin == null) perlin = PERLIN;
		double x = (xi % w) + ((xi < 0) ? w : 0);
		double y = (yi % h) + ((yi < 0) ? h : 0);
		double z = (zi % d) + ((zi < 0) ? d : 0);
		double w_x = w - x;
		double h_y = h - y;
		double d_z = d - z;
		double x_w = x - w;
		double y_h = y - h;
		double z_d = z - d;
		double xy = x * y;
		double h_yXd_z = h_y * d_z;
		double h_yXz = h_y * z;
		double w_xXy = w_x * y;
		return ((snoise(perlin, x, y, z) * (w_x) * h_yXd_z + snoise(perlin, x, y_h, z) * w_xXy * (d_z) + snoise(perlin, x_w, y, z) * (x) * h_yXd_z + snoise(perlin, x_w, y_h, z) * (xy) * (d_z) + snoise(perlin, x_w, y_h, z_d) * (xy) * (z) + snoise(perlin, x, y, z_d) * (w_x) * h_yXz + snoise(perlin, x, y_h, z_d) * w_xXy * (z) + snoise(perlin, x_w, y, z_d) * (x) * h_yXz) / (w * h * d));
	}
	
	public static final double spnoise(int[] perlin, double xi, double yi, double zi, double ti, double w, double h, double d, double p) {
		if(perlin == null) perlin = PERLIN;
		double x = (xi % w) + ((xi < 0) ? w : 0);
		double y = (yi % h) + ((yi < 0) ? h : 0);
		double z = (zi % d) + ((zi < 0) ? d : 0);
		double t = (ti % p) + ((ti < 0) ? p : 0);
		double w_x = w - x;
		double h_y = h - y;
		double d_z = d - z;
		double p_t = p - t;
		double x_w = x - w;
		double y_h = y - h;
		double z_d = z - d;
		double t_p = t - p;
		double xy = x * y;
		double d_zXp_t = (d_z) * (p_t);
		double zXp_t = z * (p_t);
		double zXt = z * t;
		double d_zXt = d_z * t;
		double w_xXy = w_x * y;
		double w_xXh_y = w_x * h_y;
		double xXh_y = x * h_y;
		return ((snoise(perlin, x, y, z, t) * (w_xXh_y) * d_zXp_t + snoise(perlin, x_w, y, z, t) * (xXh_y) * d_zXp_t + snoise(perlin, x_w, y_h, z, t) * (xy) * d_zXp_t + snoise(perlin, x, y_h, z, t) * (w_xXy) * d_zXp_t + snoise(perlin, x_w, y_h, z_d, t) * (xy) * (zXp_t) + snoise(perlin, x, y, z_d, t) * (w_xXh_y) * (zXp_t) + snoise(perlin, x, y_h, z_d, t) * (w_xXy) * (zXp_t) + snoise(perlin, x_w, y, z_d, t) * (xXh_y) * (zXp_t) + snoise(perlin, x, y, z, t_p) * (w_xXh_y) * (d_zXt) + snoise(perlin, x_w, y, z, t_p) * (xXh_y) * (d_zXt) + snoise(perlin, x_w, y_h, z, t_p) * (xy) * (d_zXt) + snoise(perlin, x, y_h, z, t_p) * (w_xXy) * (d_zXt) + snoise(perlin, x_w, y_h, z_d, t_p) * (xy) * (zXt) + snoise(perlin, x, y, z_d, t_p) * (w_xXh_y) * (zXt) + snoise(perlin, x, y_h, z_d, t_p) * (w_xXy) * (zXt) + snoise(perlin, x_w, y, z_d, t_p) * (xXh_y) * (zXt)) / (w * h * d * t));
	}
	
	public static final double spnoise(int[] perlin, Point2 p, double periodx, double periody) {
		if(perlin == null) perlin = PERLIN;
		return spnoise(perlin, p.x, p.y, periodx, periody);
	}
	
	public static final double spnoise(int[] perlin, Point3 p, Vector3 period) {
		if(perlin == null) perlin = PERLIN;
		return spnoise(perlin, p.x, p.y, p.z, period.x, period.y, period.z);
	}
	
	public static final double spnoise(int[] perlin, Point3 p, double t, Vector3 pperiod, double tperiod) {
		if(perlin == null) perlin = PERLIN;
		return spnoise(perlin, p.x, p.y, p.z, t, pperiod.x, pperiod.y, pperiod.z, tperiod);
	}
	
	private static final double fade(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}
	
	private static final double lerp(double t, double a, double b) {
		return a + t * (b - a);
	}
	
	private static final double grad(int hash, double x) {
		int h = hash & 0x1;
		return x * G1[h];
	}
	
	private static final double grad(int hash, double x, double y) {
		int h = hash & 0x3;
		return x * G2[h][0] + y * G2[h][1];
	}
	
	private static final double grad(int hash, double x, double y, double z) {
		int h = hash & 15;
		return x * G3[h][0] + y * G3[h][1] + z * G3[h][2];
	}
	
	private static final double grad(int hash, double x, double y, double z, double w) {
		int h = hash & 31;
		return x * G4[h][0] + y * G4[h][1] + z * G4[h][2] + w * G4[h][3];
	}
	
	static public int[] permutation(int s)
	{
		int[] p = new int[512];
		for (int i=0; i < 256 ; i++)
		{
			p[256+i] = p[i] = ((PERLIN[i] ^ s) & 0xff);
		}
		return p;
	}
}