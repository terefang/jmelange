package com.github.terefang.jmelange.randfractal.sampler;

import com.github.terefang.jmelange.randfractal.AbstractNoiseSampler;
import com.github.terefang.jmelange.randfractal.IRandom;
import com.github.terefang.jmelange.randfractal.random.BlockRandom;

public class ImpulseNoiseSampler extends AbstractNoiseSampler
{
    protected double impulseTab[] = new double[TABSIZE*4];

    protected double catromTab[] = new double[401];

    protected double catrom2(double d)
    {
        if (d >= 4)
            return 0;
        d = d*100.0 + 0.5;
        int i = (int) Math.floor(d);
        if (i >= 401)
            return 0;
        return catromTab[i];
    }

    protected void initTabs()
    {
        /* catrom2 table */
        for(int i=0; i<401; i++)
        {
            double x0 = i/100.0;
            double x = Math.sqrt(x0);
            if(x < 1)
                catromTab[i] = (0.5 * (2+x0*(-5+x*3)));
            else
                catromTab[i] = (0.5 * (4+x*(-8+x*(5-x))));
        }
        /* impulse table */
        for(int i=0; i<TABSIZE; i++)
        {
            impulseTab[i*4] = ((rnd.nextInt() & RANDMASK)/((double) RANDMASK));
            impulseTab[i*4+1] = ((rnd.nextInt() & RANDMASK)/((double) RANDMASK));
            impulseTab[i*4+2] = ((rnd.nextInt() & RANDMASK)/((double) RANDMASK));
            impulseTab[i*4+3] = (1.0 - 2.0*((rnd.nextInt() & RANDMASK)/((double) RANDMASK)));
        }
    }

    public void seed(long s)
    {
        rnd.setSeed(s);
        initTabs();
    }

    public ImpulseNoiseSampler()
    {
        this(new BlockRandom(), 0L);
    }

    public ImpulseNoiseSampler(IRandom p)
    {
        this(p, 0L);
    }

    public ImpulseNoiseSampler(IRandom p, long seed)
    {
        setPrng(p);
        rnd.setSeed(seed);
        initTabs();
    }

    public double noise(double x, double y, double z)
    {
        int i, j, k, h, n;
        int ix, iy, iz;
        double sum = 0;
        double fx, fy, fz, dx, dy, dz, distsq;
        ix = (int) Math.floor(x); fx = x - ix;
        iy = (int) Math.floor(y); fy = y - iy;
        iz = (int) Math.floor(z); fz = z - iz;
        /* Perform the sparse convolution. */
        for (i = -2; i <= 2; i++)
        {
            for (j = -2; j <= 2; j++)
            {
                for (k = -2; k <= 2; k++)
                {
                    /* Compute voxel hash code. */
                    h = INDEX(ix+i,iy+j,iz+k);
                    for (n = 3; n > 0; n--, h = (((h)+1) & TABMASK))
                    {
                        /* Convolve filter and impulse. */
                        dx = fx - (i + impulseTab[h*4]);
                        dy = fy - (j + impulseTab[h*4+1]);
                        dz = fz - (k + impulseTab[h*4+2]);
                        distsq = dx*dx + dy*dy + dz*dz;
                        sum += catrom2(distsq) * impulseTab[h*4+3];
                    }
                }
            }
        }
        return sum / 3.0;
    }
}
