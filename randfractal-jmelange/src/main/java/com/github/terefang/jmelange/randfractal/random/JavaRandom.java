package com.github.terefang.jmelange.randfractal.random;

import com.github.terefang.jmelange.randfractal.AbstractRandom;

public class JavaRandom extends AbstractRandom {
    public JavaRandom(int seed) {
        super();
        this.setSeed(seed);
    }

    public static void main(String[] args)
    {
        long _count = 0x1ffffffffL;
        long[] _buf = new long[256];
        JavaRandom _rand = new JavaRandom(0);
        long _t0 = System.currentTimeMillis();
        for(long _i = 0; _i<_count; _i++)
        {
            _buf[_rand.next(8)]++;
            //_rand.next(8);
        }
        long _t1 = System.currentTimeMillis();

        for(int _i = 0; _i<0x100; _i++)
        {
            System.out.println(_buf[_i]);
        }
        // System.out.println(_count*1000L/(_t1-_t0));
    }
}
