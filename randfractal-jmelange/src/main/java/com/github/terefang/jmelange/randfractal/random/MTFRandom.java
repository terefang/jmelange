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
		return (int) (tmp >>> (48-bits));
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
		MTFRandom rand = new MTFRandom();
		rand.setSeed(0x123456789abcdefL);
		for(int i = 0; i<32; i++)
		System.out.println(String.format("%08x", rand.next(32)));
	}
}
