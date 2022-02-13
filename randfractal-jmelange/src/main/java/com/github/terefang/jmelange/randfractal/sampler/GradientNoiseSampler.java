package com.github.terefang.jmelange.randfractal.sampler;

import com.github.terefang.jmelange.randfractal.AbstractNoiseSampler;
import com.github.terefang.jmelange.random.IRandom;
import com.github.terefang.jmelange.random.BlockRandom;

public class GradientNoiseSampler extends AbstractNoiseSampler
{
    protected double gradTab[] = new double[TABSIZE*3];

    protected void initGradientTab()
    {
        double z, r, theta;
        for(int i = 0; i < TABSIZE; i++)
        {
            z = (1.0 - 2.0*((rnd.nextInt() & RANDMASK)/((double) RANDMASK)));
            /* r is radius of x,y circle */
            r = Math.sqrt(1 - z*z);
            /* theta is angle in (x,y) */
            theta = 2 * Math.PI * ((rnd.nextInt() & RANDMASK)/((double) RANDMASK));
            gradTab[i*3] = (r * Math.cos(theta));
            gradTab[i*3+1] = (r * Math.sin(theta));
            gradTab[i*3+2] = z;
        }
    }

    protected double glattice(int ix, int iy, int iz, double fx, double fy, double fz)
    {
        int i = INDEX(ix,iy,iz)*3;
        return gradTab[i]*fx + gradTab[i+1]*fy + gradTab[i+2]*fz;
    }

    public GradientNoiseSampler()
    {
        this(new BlockRandom(), 0L);
    }

    public GradientNoiseSampler(IRandom p)
    {
        this(p, 0L);
    }

    public GradientNoiseSampler(IRandom p, long seed)
    {
        setPrng(p);
        rnd.setSeed(seed);
        initGradientTab();
    }

    public void seed(long s)
    {
        rnd.setSeed(s);
        initGradientTab();
    }

    public double noise(double x, double y, double z)
    {
        int ix, iy, iz;
        double fx0, fx1, fy0, fy1, fz0, fz1;
        double wx, wy, wz;
        double vx0, vx1, vy0, vy1, vz0, vz1;
        ix = (int) Math.floor(x);
        fx0 = x - ix;
        fx1 = fx0 - 1;
        wx = SMOOTHSTEP(fx0);
        iy = (int) Math.floor(y);
        fy0 = y - iy;
        fy1 = fy0 - 1;
        wy = SMOOTHSTEP(fy0);
        iz = (int) Math.floor(z);
        fz0 = z - iz;
        fz1 = fz0 - 1;
        wz = SMOOTHSTEP(fz0) ;
        vx0 = glattice(ix,iy,iz,fx0,fy0,fz0);
        vx1 = glattice(ix+1,iy,iz,fx1,fy0,fz0);
        vy0 = LERP(wx, vx0, vx1);
        vx0 = glattice(ix,iy+1,iz,fx0,fy1,fz0);
        vx1 = glattice(ix+1,iy+1,iz,fx1,fy1,fz0);
        vy1 = LERP(wx, vx0, vx1);
        vz0 = LERP(wy, vy0, vy1);
        vx0 = glattice(ix,iy,iz+1,fx0,fy0,fz1);
        vx1 = glattice(ix+1,iy,iz+1,fx1,fy0,fz1);
        vy0 = LERP(wx, vx0, vx1);
        vx0 = glattice(ix,iy+1,iz+1,fx0,fy1,fz1);
        vx1 = glattice(ix+1,iy+1,iz+1,fx1,fy1,fz1);
        vy1 = LERP(wx, vx0, vx1);
        vz1 = LERP(wy, vy0, vy1);
        return LERP(wz, vz0, vz1);
    }
}