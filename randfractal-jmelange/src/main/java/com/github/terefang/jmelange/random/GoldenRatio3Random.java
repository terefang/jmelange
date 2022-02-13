package com.github.terefang.jmelange.random;

public class GoldenRatio3Random extends GoldenRatioRandom
{
    @Override
    public long nextLong() {
        final long fa = this.stateA;
        final long fb = this.stateB;
        final long fc = this.stateC;
        final long fd = this.stateD;

        this.stateA = fb ^ fc ^ fd;
        this.stateB = Long.rotateLeft(fa, 41);
        this.stateC = fa + fb;
        this.stateD = fc + 0xC6BC279692B5C323L;

        return fc;
    }

    public static void main(String[] args)
    {
        long _count = 0x10000L;
        long[] _buf = new long[256];
        GoldenRatio3Random _rand = new GoldenRatio3Random();
        _rand.setSeed(0L);
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
