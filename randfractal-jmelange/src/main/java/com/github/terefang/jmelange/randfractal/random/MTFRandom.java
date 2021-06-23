package com.github.terefang.jmelange.randfractal.random;

import com.github.terefang.jmelange.randfractal.AbstractRandom;

public class MTFRandom extends AbstractRandom
{
	private MersenneTwisterFast MT;

	@Override
	protected int next(int bits)
	{
		if(MT==null)
		{
			MT = new MersenneTwisterFast();
		}
		long tmp = MT.nextInt();
		tmp ^= (tmp << 32);
		return (int) (tmp >>> (48-bits)) & ((1<<bits)-1);
	}

	public MTFRandom()
	{ 
		MT = new MersenneTwisterFast();
	}

	@Override
	public void setSeed(long s)
	{
		if(MT==null)
		{
			MT = new MersenneTwisterFast();
		}
		MT.setSeed(s);
	}

	public static void main(String[] args)
	{
		long _count = 0x1ffffffffL;
		long[] _buf = new long[256];
		MTFRandom _rand = new MTFRandom();
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
