package com.github.terefang.jmelange.randfractal.xnoise;

public final class Vector3 {
	private static final double[] COS_THETA = new double[256];
	private static final double[] SIN_THETA = new double[256];
	private static final double[] COS_PHI = new double[256];
	private static final double[] SIN_PHI = new double[256];
	
	public double x, y, z;
	
	static {
		// precompute tables to compress unit vectors
		for (int i = 0; i < 256; i++) {
			double angle = (i * Math.PI) / 256.0;
			COS_THETA[i] = (double) Math.cos(angle);
			SIN_THETA[i] = (double) Math.sin(angle);
			COS_PHI[i] = (double) Math.cos(2 * angle);
			SIN_PHI[i] = (double) Math.sin(2 * angle);
		}
	}
	
	public Vector3() {
	}
	
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(Vector3 v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	public static final Vector3 decode(short n, Vector3 dest) {
		int t = (n & 0xFF00) >>> 8;
		int p = n & 0xFF;
		dest.x = SIN_THETA[t] * COS_PHI[p];
		dest.y = SIN_THETA[t] * SIN_PHI[p];
		dest.z = COS_THETA[t];
		return dest;
	}
	
	public static final Vector3 decode(short n) {
		return decode(n, new Vector3());
	}
	
	public final short encode() {
		int theta = (int) (Math.acos(z) * (256.0 / Math.PI));
		if (theta > 255)
			theta = 255;
		int phi = (int) (Math.atan2(y, x) * (128.0 / Math.PI));
		if (phi < 0)
			phi += 256;
		else if (phi > 255)
			phi = 255;
		return (short) (((theta & 0xFF) << 8) | (phi & 0xFF));
	}
	
	public double get(int i) {
		switch (i) {
			case 0:
				return x;
			case 1:
				return y;
			default:
				return z;
		}
	}
	
	public final double length() {
		return (double) Math.sqrt((x * x) + (y * y) + (z * z));
	}
	
	public final double lengthSquared() {
		return (x * x) + (y * y) + (z * z);
	}
	
	public final Vector3 negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
	
	public final Vector3 negate(Vector3 dest) {
		dest.x = -x;
		dest.y = -y;
		dest.z = -z;
		return dest;
	}
	
	public final Vector3 mul(double s) {
		x *= s;
		y *= s;
		z *= s;
		return this;
	}
	
	public final Vector3 mul(double s, Vector3 dest) {
		dest.x = x * s;
		dest.y = y * s;
		dest.z = z * s;
		return dest;
	}
	
	public final Vector3 div(double d) {
		x /= d;
		y /= d;
		z /= d;
		return this;
	}
	
	public final Vector3 div(double d, Vector3 dest) {
		dest.x = x / d;
		dest.y = y / d;
		dest.z = z / d;
		return dest;
	}
	
	public final double normalizeLength() {
		double n = (double) Math.sqrt(x * x + y * y + z * z);
		double in = 1.0f / n;
		x *= in;
		y *= in;
		z *= in;
		return n;
	}
	
	public final Vector3 normalize() {
		double in = 1.0f / (double) Math.sqrt((x * x) + (y * y) + (z * z));
		x *= in;
		y *= in;
		z *= in;
		return this;
	}
	
	public final Vector3 normalize(Vector3 dest) {
		double in = 1.0f / (double) Math.sqrt((x * x) + (y * y) + (z * z));
		dest.x = x * in;
		dest.y = y * in;
		dest.z = z * in;
		return dest;
	}
	
	public final Vector3 set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public final Vector3 set(Vector3 v) {
		x = v.x;
		y = v.y;
		z = v.z;
		return this;
	}
	
	public final double dot(double vx, double vy, double vz) {
		return vx * x + vy * y + vz * z;
	}
	
	public static final double dot(Vector3 v1, Vector3 v2) {
		return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
	}
	
	public static final Vector3 cross(Vector3 v1, Vector3 v2, Vector3 dest) {
		dest.x = (v1.y * v2.z) - (v1.z * v2.y);
		dest.y = (v1.z * v2.x) - (v1.x * v2.z);
		dest.z = (v1.x * v2.y) - (v1.y * v2.x);
		return dest;
	}
	
	public static final Vector3 add(Vector3 v1, Vector3 v2, Vector3 dest) {
		dest.x = v1.x + v2.x;
		dest.y = v1.y + v2.y;
		dest.z = v1.z + v2.z;
		return dest;
	}
	
	public static final Vector3 sub(Vector3 v1, Vector3 v2, Vector3 dest) {
		dest.x = v1.x - v2.x;
		dest.y = v1.y - v2.y;
		dest.z = v1.z - v2.z;
		return dest;
	}
	
	@Override
	public final String toString() {
		return String.format("(%.2f, %.2f, %.2f)", x, y, z);
	}
}