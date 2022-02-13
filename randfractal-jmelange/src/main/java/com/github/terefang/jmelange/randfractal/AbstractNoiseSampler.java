package com.github.terefang.jmelange.randfractal;

/*
LUMPY: 	  .03 * noise(8*x,8*y,8*z);
CRINKLY: 	-.10 * turbulence(x,y,z);
MARBLED: 	 .01 * stripes(x + 2*turbulence(x,y,z), 1.6);

double stripes(double x, double f) {
   double t = .5 + .5 * Math.sin(f * 2*Math.PI * x);
   return t * t - .5;
}
double turbulence(double x, double y, double z) {
   double t = -.5;
   for (double f = 1 ; f <= W/12 ; f *= 2)
      t += Math.abs(noise(f*x,f*y,f*z) / f);
   return t;
}
*/

import com.github.terefang.jmelange.random.IRandom;

public abstract class AbstractNoiseSampler implements INoiseSampler
{
    public static final int TABSIZE = 0x100;
    public static final int TABMASK = 0xFF;
    public static final int perm[] = {
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
    protected int PERM(int x)
    {
        return perm[(x)&TABMASK];
    }
    protected int INDEX(int ix, int iy, int iz)
    {
        return PERM((ix)+PERM((iy)+PERM(iz)));
    }

    public static final int RANDMASK = 0x3fffffff;

    protected double clamp(double x, double a, double b)
    {
        return (x < a ? a: (x > b ? b : x));
    }
    protected double spline(double x, int nknots, double[] kknot)
    {
        double knot[]=new double[kknot.length+1];
        System.arraycopy(kknot, 0, knot, 0, kknot.length);
        knot[kknot.length]=kknot[0];
        /* Coefficients of basis matrix. */
        double CR00 = -0.5;
        double CR01 = 1.5;
        double CR02 = -1.5;
        double CR03 = 0.5;
        double CR10 = 1.0;
        double CR11 = -2.5;
        double CR12 = 2.0;
        double CR13 = -0.5;
        double CR20 = -0.5;
        double CR21 = 0.0;
        double CR22 = 0.5;
        double CR23 = 0.0;
        double CR30 = 0.0;
        double CR31 = 1.0;
        double CR32 = 0.0;
        double CR33 = 0.0;
        int span;
        int nspans = nknots - 3;
        double c0, c1, c2, c3; /* coefficients of the cubic.*/
        if(nspans < 1)
        {/* illegal */
            return 0;
        }
        /* Find the appropriate 4-point span of the spline. */
        x = clamp(x, 0, 1) * nspans;
        span = (int) x;
        if (span >= nknots - 3)
            span = nknots - 3;
        x -= span;

        /* Evaluate the span cubic at x using Horner�s rule. */
        c3 = CR00*knot[span] + CR01*knot[span+1] + CR02*knot[span+2] + CR03*knot[span+3];
        c2 = CR10*knot[span] + CR11*knot[span+1] + CR12*knot[span+2] + CR13*knot[span+3];
        c1 = CR20*knot[span] + CR21*knot[span+1] + CR22*knot[span+2] + CR23*knot[span+3];
        c0 = CR30*knot[span] + CR31*knot[span+1] + CR32*knot[span+2] + CR33*knot[span+3];
        return ((c3*x + c2)*x + c1)*x + c0;
    }

    protected double LERP(double t, double x0, double xl)
    {
        return ((x0) + (t)*((xl)-(x0)));
    }
    protected double SMOOTHSTEP(double x)
    {
        return (x*x*(3.0 - 2.0*x));
    }
    protected IRandom rnd = null;

    public abstract double noise(double x, double y, double z);
    public double snoise(double x, double y, double z)
    {
        double n = noise(x,y,z);
        return ((2.0*Math.abs(n))-1.0);
    }
    protected void seed(long s)
    {
        rnd.setSeed(s);
    }
    protected void setPrng(IRandom p)
    {
        if(p!=null)
            rnd=p;
    }
    protected double[] vecNoise(double x, double y, double z)
    {
        double result[]=new double[3];
        result[0] = noise(x, y, z);
        x+=3.33; y+=3.33; z+=3.33;
        result[1] = noise(x, y, z);
        x+=4.44; y+=4.44; z+=4.44;
        result[2] = noise(x, y, z);
        return result;
    }
    protected double distNoise(double x, double y, double z, double distortion)
    {
        double offset[] = vecNoise(x+0.5, y+0.5, z+0.5);

        offset[0] *= distortion;
        offset[1] *= distortion;
        offset[2] *= distortion;

        return noise(x+offset[0], y+offset[1], z+offset[2]); /* distorted domain noise */
    }

    // fractal sum
    public double fSum(double x, double y, double z)
    {
        return fSum(x, y, z, 1.0, 16.0, 2.0, 0.0);
    }
    public double fSum(double x, double y, double z, double fMin, double fMax)
    {
        return fSum(x, y, z, fMin, fMax, 2.0, 0.0);
    }
    public double fSum(double x, double y, double z, double fMin, double fMax, double gap)
    {
        return fSum(x, y, z, fMin, fMax, gap, 0.0);
    }
    public double fSum(double x, double y, double z, double fMin, double fMax, double gap, double offset)
    {
        double value = offset;
        for(double f = fMin; f < fMax; f *= gap)
            value += noise(x*f,y*f,z*f)/f;
        return value;
    }

    // fractal sum of inversion
    public double fInvSum(double x, double y, double z)
    {
        return fInvSum(x, y, z, 1.0, 16.0, 2.0, 0.0);
    }
    public double fInvSum(double x, double y, double z, double fMin, double fMax)
    {
        return fInvSum(x, y, z, fMin, fMax, 2.0, 0.0);
    }
    public double fInvSum(double x, double y, double z, double fMin, double fMax, double gap)
    {
        return fInvSum(x, y, z, fMin, fMax, gap, 0.0);
    }
    public double fInvSum(double x, double y, double z, double fMin, double fMax, double gap, double offset)
    {
        double value = offset;
        for(double f = fMin; f < fMax; f *= gap)
            value += 1.0/(noise(x*f,y*f,z*f)/f);
        return value;
    }

    // fractal brownian motion
    public double fBm(double x, double y, double z)
    {
        return fBm(x, y, z, 1.0, 2.0, 6.0, 0.0);
    }
    public double fBm(double x, double y, double z, double H, double lacunarity)
    {
        return fBm(x, y, z, H, lacunarity, 6.0, 0.0);
    }
    public double fBm(double x, double y, double z, double H, double lacunarity, double octaves)
    {
        return fBm(x, y, z, H, lacunarity, octaves, 0.0);
    }
    public double fBm(double x, double y, double z, double H, double lacunarity, double octaves, double offset)
    {
        double value = 0.0, remainder;
        int i;
        /* inner loop of fractal construction */
        for(i=0; i<octaves; i++)
        {
            value += (noise(x, y, z)+offset) * Math.pow( lacunarity, -H*i );
            x*=lacunarity;
            y*=lacunarity;
            z*=lacunarity;
        }
        remainder = octaves - (int)octaves;
        if( remainder > 0.0) /* add in octaves remainder */
            /* �i� and spatial freq. are preset in loop above */
            value += remainder * (noise(x, y, z)+offset) * Math.pow( lacunarity, -H*i );

        return value;
    }

    // multi-fractal
    public double mF(double x, double y, double z)
    {
        return mF(x, y, z, 1.0, 2.0, 6.0, 0.0);
    }
    public double mF(double x, double y, double z, double H, double lacunarity)
    {
        return mF(x, y, z, H, lacunarity, 6.0, 0.0);
    }
    public double mF(double x, double y, double z, double H, double lacunarity, double octaves)
    {
        return mF(x, y, z, H, lacunarity, octaves, 0.0);
    }
    public double mF(double x, double y, double z, double H, double lacunarity, double octaves, double offset)
    {
        double value = 1.0;
        /* inner loop of fractal construction */
        for(int i=0; i<octaves; i++)
        {
            value += distNoise(x, y, z, offset) * Math.pow( lacunarity, -H*i );
            x*=lacunarity;
            y*=lacunarity;
            z*=lacunarity;
        }
        return value;
    }

    // ridged-multi-fractal
    public double RmF(double x, double y, double z)
    {
        return RmF(x, y, z, 2.0, 1.0, 2.0, 6.0, 1.0);
    }
    public double RmF(double x, double y, double z, double gain)
    {
        return RmF(x, y, z, gain, 1.0, 2.0, 6.0, 1.0);
    }
    public double RmF(double x, double y, double z, double gain, double H, double lacunarity)
    {
        return RmF(x, y, z, gain, H, lacunarity, 6.0, 1.0);
    }
    public double RmF(double x, double y, double z, double gain, double H, double lacunarity, double octaves)
    {
        return RmF(x, y, z, gain, H, lacunarity, octaves, 1.0);
    }
    public double RmF(double x, double y, double z, double gain, double H, double lacunarity, double octaves, double offset)
    {
        double result, signal, weight, freq;
        /* assign initial values */
        result = 0.0;
        weight = 1.0;
        freq=1.0f;
        /* inner loop of fractal construction */
        for(int i=1; i<octaves; i++)
        {
            signal=noise(x, y, z);
            /* get absolute value of signal (this creates the ridges) */
            if(signal < 0.0)
                signal=-signal;
            /* invert and translate (note that �offset� should be = 1.0) */
            signal=offset - signal;
            /* square the signal, to increase �sharpness� of ridges */
            signal*=signal;
            signal*=weight;
            result+=signal * Math.pow( freq, -H );
            // for next loop
            weight=signal * gain;
            if(weight > 1.0) weight=1.0;
            if(weight < 0.0) weight=0.0;
            x*=lacunarity;
            y*=lacunarity;
            z*=lacunarity;
            freq*=lacunarity;
        }
        return result;
    }
}
