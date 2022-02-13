package com.github.terefang.jmelange.randfractal.sampler;

import com.github.terefang.jmelange.randfractal.AbstractNoiseSampler;
import com.github.terefang.jmelange.random.IRandom;
import com.github.terefang.jmelange.random.BlockRandom;

public class ValueNoiseSampler extends AbstractNoiseSampler
{
    protected double valueTab[] = new double[TABSIZE];

    protected void initValueTab()
    {
        for(int i=0 ; i<TABSIZE ; i++)
        {
            valueTab[i]=(1.0 - 2.0*Math.abs(((rnd.nextInt() & RANDMASK)/(double)RANDMASK)));
        }
    }
    public void seed(long s)
    {
        rnd.setSeed(s);
        initValueTab();
    }
    public ValueNoiseSampler()
    {
        this(new BlockRandom(), 0L);
    }

    public ValueNoiseSampler(IRandom p)
    {
        this(p, 0L);
    }

    public ValueNoiseSampler(IRandom p, long seed)
    {
        setPrng(p);
        rnd.setSeed(seed);
        initValueTab();
    }

    public double noise(double x, double y, double z)
    {
        int ix, iy, iz;
        int i, j, k;
        double fx, fy, fz;
        double xknots[] = new double[4], yknots[] = new double[4], zknots[] = new double[4];
        ix = (int) Math.floor(x);
        fx = x - ix;
        iy = (int) Math.floor(y);
        fy = y - iy;
        iz = (int) Math.floor(z);
        fz = z - iz;
        for (k = -1; k <= 2; k++)
        {
            for (j = -1; j <= 2; j++)
            {
                for (i = -1; i <= 2; i++)
                    xknots[i+1] = valueTab[INDEX(ix+i,iy+j,iz+k)];
                yknots[j+1] = spline(fx, 4, xknots);
            }
            zknots[k+1] = spline(fy, 4, yknots);
        }
        return spline(fz, 4, zknots);
    }
}