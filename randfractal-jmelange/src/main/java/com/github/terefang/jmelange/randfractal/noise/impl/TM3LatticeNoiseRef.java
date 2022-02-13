package com.github.terefang.jmelange.randfractal.noise.impl;

import java.util.Random;

public class TM3LatticeNoiseRef 
{
	public static int TABSIZE = 256;
	public static int TABMASK = 0xff;
	public static int PERM(int x, int[] p) { return p[x % p.length]; }
	public static int INDEX(int ix, int iy, int iz, int[] p) { return PERM(ix+PERM(iy+PERM(iz, p), p), p); }
	
	public static final int perm[] = new int[] {
		225, 155, 210, 108, 175, 199, 221, 144, 203, 116, 70, 213, 69, 158, 33, 252,
		5, 82, 173, 133, 222, 139, 174, 27, 9, 71, 90, 246, 75, 130, 91, 191,
		169, 138, 2, 151, 194, 235, 81, 7, 25, 113, 228, 159, 205, 253, 134, 142,
		248, 65, 224, 217, 22, 121, 229, 63, 89, 103, 96, 104, 156, 17, 201, 129,
		36, 8, 165, 110, 237, 117, 231, 56, 132, 211, 152, 20, 181, 111, 239, 218,
		170, 163, 51, 172, 157, 47, 80, 212, 176, 250, 87, 49, 99, 242, 136, 189,
		162, 115, 44, 43, 124, 94, 150, 16, 141, 247, 32, 10, 198, 223, 255, 72,
		53, 131, 84, 57, 220, 197, 58, 50, 208, 11, 241, 28, 3, 192, 62, 202,
		18, 215, 153, 24, 76, 41, 15, 179, 39, 46, 55, 6, 128, 167, 23, 188,
		106, 34, 187, 140, 164, 73, 112, 182, 244, 195, 227, 13, 35, 77, 196, 185,
		26, 200, 226, 119, 31, 123, 168, 125, 249, 68, 183, 230, 177, 135, 160, 180,
		12, 1, 243, 148, 102, 166, 38, 238, 251, 37, 240, 126, 64, 74, 161, 40,
		184, 149, 171, 178, 101, 66, 29, 59, 146, 61, 254, 107, 42, 86, 154, 4,
		236, 232, 120, 21, 233, 209, 45, 98, 193, 114, 78, 19, 206, 14, 118, 127,
		48, 79, 147, 85, 30, 207, 219, 54, 88, 234, 190, 122, 95, 67, 143, 109,
		137, 214, 145, 93, 92, 100, 245, 0, 216, 186, 60, 83, 105, 97, 204, 52
	};
	
	public static double[] vtable(long s) 
	{
		Random rnd = new Random(s);
		double[] vtab = new double[TABSIZE];
		for(int i=0; i<TM3LatticeNoiseRef.TABSIZE; i++)
		{
			vtab[i]=1.f - 2.f*rnd.nextDouble();
		}
		return vtab;
	}

	public static double[] gtable(long s) 
	{
		
		Random rnd = new Random(s);
		double[] gtab = new double[TABSIZE*3];
		for(int i=0; i<TM3LatticeNoiseRef.TABSIZE; i++)
		{
			double z = 1.f - 2.f*rnd.nextDouble();
			double r = Math.sqrt(1.0 - z*z);
			double T = 2 * Math.PI * rnd.nextDouble();
			gtab[3*i] = r * Math.cos(T); 
			gtab[3*i+1] = r * Math.sin(T);
			gtab[3*i+2] = z;
		}
		return gtab;
	}

	public static double[] itable(long s) 
	{
		
		Random rnd = new Random(s);
		double[] itab = new double[TABSIZE*4];
		for(int i=0; i<TM3LatticeNoiseRef.TABSIZE; i++)
		{
			itab[4*i] = rnd.nextDouble();
			itab[4*i+1] = rnd.nextDouble();
			itab[4*i+2] = rnd.nextDouble();
			itab[4*i+3] = 1.f - 2.f*rnd.nextDouble();
		}
		return itab;
	}

	public static final double vlattice(int ix, int iy, int iz, double[] p)
	{
		return p[INDEX(ix, iy, iz, perm)];
	}

	public static final double glattice(int ix, int iy, int iz, double fx, double fy, double fz, double[] p)
	{
		int i = INDEX(ix, iy, iz, perm)*3;
		return p[(i+0)%p.length]*fx + p[(i+1)%p.length]*fy + p[(i+2)%p.length]*fz;
	}

	public static final double vnoise(double x, double y, double z, double[] p)
	{
		int ix, iy,iz;
		double fx,fy,fz;
		double xknots[] = new double[4], yknots[] = new double[4], zknots[] = new double[4];
		
		ix = (int)Math.floor(x);
		fx = x - ix;

		iy = (int)Math.floor(y);
		fy = y - iy;
		
		iz = (int)Math.floor(z);
		fz = z - iz;
		
		for(int k=-1; k<=2; k++)
		{
			for(int j=-1; j<=2; j++)
			{
				for(int l=-1; l<=2; l++)
				{
					xknots[l+1]=vlattice(ix+l, iy+j, iz+k, p);
				}
				yknots[j+1]=spline(fx,xknots);
			}
			zknots[k+1]=spline(fy,yknots);
		}
		return spline(fz,zknots);
	}
	
	public static double spline(double f, double[] k) 
	{
		return 0.5 * ((2 * k[1]) +
                (k[2] - k[0]) * f +
                (2*k[0] - 5*k[1] + 4*k[2] - k[3]) * f * f +
                (3*k[1] -k[0] - 3 * k[2] + k[3]) * f * f * f);
	}

	static double lerp(double t, double a, double b) { return a + t * (b - a); }
	static double step(double t) { return (t * t) * (3.0 - (2.0*t)); }
	
	public static final double gnoise(double x, double y, double z, double[] p)
	{
		int ix = (int)Math.floor(x);
		double fx0 = x - ix;
		double fx1 = fx0 - 1;
		double wx = step(fx0);

		int iy = (int)Math.floor(y);
		double fy0 = y - iy;
		double fy1 = fy0 - 1;
		double wy = step(fy0);
		
		int iz = (int)Math.floor(z);
		double fz0 = z - iz;
		double fz1 = fz0 - 1;
		double wz = step(fz0);

		double vx0 = glattice(ix, iy, iz, fx0, fy0, fz0, p);
		double vx1 = glattice(ix+1, iy, iz, fx1, fy0, fz0, p);
		
		double vy0 = lerp(wx, vx0, vx1);
				
		vx0 = glattice(ix, iy+1, iz, fx0, fy1, fz0, p);
		vx1 = glattice(ix+1, iy+1, iz, fx1, fy1, fz0, p);
		
		double vy1 = lerp(wx, vx0, vx1);
		double vz0 = lerp(wy, vy0, vy1);
		
		vx0 = glattice(ix, iy, iz+1, fx0, fy0, fz1, p);
		vx1 = glattice(ix+1, iy, iz+1, fx1, fy0, fz1, p);
		
		vy0 = lerp(wx, vx0, vx1);
		
		vx0 = glattice(ix, iy+1, iz+1, fx0, fy1, fz1, p);
		vx1 = glattice(ix+1, iy+1, iz+1, fx1, fy1, fz1, p);
		
		vy0 = lerp(wx, vx0, vx1);
		double vz1 = lerp(wy, vy0, vy1);
		
		return lerp(wz, vz0, vz1);
	}
	
	private final static double NIMPULSES = 3.0;

    public static int floor(double x) 
    {
        int ix = (int)x;
        if (x < 0 && x != ix)
                return ix-1;
        return ix;
    }

	private final static int SAMPRATE = 1000;  /* table entries per unit distance */
	private final static int NENTRIES = (4*SAMPRATE+1);
	private static double[] table;

	public static double catrom2(double d)
	{
		double x;
		int i;

		if (d < 0.) return 0;
		if (d >= 4.) return 0;

		if (table == null) {
			table = new double[NENTRIES];
			for (i = 0; i < NENTRIES; i++) {
				x = i/(double)SAMPRATE;
				x = Math.sqrt(x);
				if (x < 1)
					table[i] = 0.5 * (2+x*x*(-5+x*3));
				else
					table[i] = 0.5 * (4+x*(-8+x*(5-x)));
			}
		}

		d = d*((double)SAMPRATE) + 0.5;
		i = floor(d);
		if (i >= NENTRIES)
			return 0;
		return table[i];
	}


	public static final double scnoise(double x, double y, double z, double[] p)
	{
	    double sum = 0;
	    double dx, dy, dz, distsq;
	
	    int ix = (int)Math.floor(x);
	    double fx = x - ix;

		int iy = (int)Math.floor(y);
		double fy = y - iy;
		
		int iz = (int)Math.floor(z);
		double fz = z - iz;
	    
	    /* Perform the sparse convolution. */
	    int m = 2;
	    for (int i = -m; i <= m; i++) 
	    {
	      for (int j = -m; j <= m; j++) 
	      {
	            for (int k = -m; k <= m; k++) 
	            {
	                    /* Compute voxel hash code. */
	                    int h = perm[(ix+i + perm[(iy+j + perm[(iz+k)&TABMASK])&TABMASK])&TABMASK];
	                    
	                    for (int n = (int) NIMPULSES; n > 0; n--, h = (h+1) & TABMASK) {
	                            /* Convolve filter and impulse. */
	                            int h4 = h*4;
	                            dx = fx - (i + p[h4++]);
	                            dy = fy - (j + p[h4++]);
	                            dz = fz - (k + p[h4++]);
	                            distsq = dx*dx + dy*dy + dz*dz;
	                            sum += catrom2(distsq) * p[h4];
	                    }
	            }
	      }
	    }
	
	    return sum / NIMPULSES;
	}
	
	public static final double scnoise(double x, double y, double[] p)
	{
	    double sum = 0;
	    double dx, dy, dz, distsq;
	
	    int ix = (int)Math.floor(x);
	    double fx = x - ix;

		int iy = (int)Math.floor(y);
		double fy = y - iy;
		
	    /* Perform the sparse convolution. */
	    int m = 2;
	    for (int i = -m; i <= m; i++) 
	    {
	      for (int j = -m; j <= m; j++) 
	      {
            /* Compute voxel hash code. */
            int h = perm[(ix+i + perm[(iy+j)&TABMASK])&TABMASK];
            
            for (int n = (int) NIMPULSES; n > 0; n--, h = (h+1) & TABMASK) {
                    /* Convolve filter and impulse. */
                    int h4 = h*4;
                    dx = fx - (i + p[h4++]);
                    dy = fy - (j + p[h4++]);
                    distsq = dx*dx + dy*dy;
                    sum += catrom2(distsq) * p[h4];
            }
	      }
	    }
	
	    return sum / NIMPULSES;
	}
}
