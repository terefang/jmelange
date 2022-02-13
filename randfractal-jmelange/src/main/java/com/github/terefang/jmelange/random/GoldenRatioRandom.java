package com.github.terefang.jmelange.random;

public class GoldenRatioRandom extends AbstractRandom
{
    long stateA;
    long stateB;
    long stateC;
    long stateD;

    @Override
    protected int next(int bits)
    {
        long tmp=nextLong();
        tmp &= ((1L<<53)-1);
        return (int) (tmp >>> (53-bits));
    }

    @Override
    public long nextLong() {
        final long fa = this.stateA;
        final long fb = this.stateB;
        final long fc = this.stateC;
        final long fd = this.stateD;

        this.stateA = 0xD1342543DE82EF95L * fd;
        this.stateB = fa + 0xC6BC279692B5C323L;
        this.stateC = Long.rotateLeft(fb, 41);
        this.stateD = fb ^ fc;

        return fd;
    }


    public void setSeed(long _seed)
    {
        this.stateA = _seed ^= 0x9E3779B97F4A7C15L;
        this.stateB = 0x3243F6A8885A308DL;
        this.stateC = _seed += 0x9E3779B97F4A7C15L;
        this.stateD = _seed += 0x9E3779B97F4A7C15L;
        for(int _i = 0; _i<(_seed & 0xffff); _i++)
        {
            nextLong();
        }
    }

    public static void main(String[] args)
    {
        long _count = 0x10000L;
        long[] _buf = new long[256];
        GoldenRatioRandom _rand = new GoldenRatioRandom();
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
