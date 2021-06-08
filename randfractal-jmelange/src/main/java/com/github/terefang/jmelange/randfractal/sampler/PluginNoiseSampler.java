package com.github.terefang.jmelange.randfractal.sampler;

import com.github.terefang.jmelange.randfractal.AbstractNoiseSampler;
import com.github.terefang.jmelange.randfractal.INoise;
import com.github.terefang.jmelange.randfractal.INoiseSampler;
import com.github.terefang.jmelange.randfractal.IRandom;
import lombok.Data;

@Data
public class PluginNoiseSampler extends AbstractNoiseSampler implements INoiseSampler
{
    public static INoiseSampler getInstance(INoise ni, long s)
    {
        PluginNoiseSampler pn = new PluginNoiseSampler();
        pn.setNoisePlugin(ni);
        pn.seed(s);
        return pn;
    }

    INoise noisePlugin = null;

    @Override
    protected void seed(long s)
    {

        noisePlugin.setSeed(s);
        noisePlugin.init();
    }

    @Override
    protected void setPrng(IRandom p) {
        noisePlugin.setPrng(p);
    }

    @Override
    public double noise(double x, double y, double z)
    {
        return noisePlugin.getNoise3d(x,y,z);
    }
}
