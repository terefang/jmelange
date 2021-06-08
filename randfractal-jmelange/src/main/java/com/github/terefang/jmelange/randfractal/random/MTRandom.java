package com.github.terefang.jmelange.randfractal.random;

import com.github.terefang.jmelange.randfractal.AbstractRandom;

public class MTRandom extends AbstractRandom
{
	private MersenneTwister MT;

	@Override
	protected int next(int bits) 
	{
		if(MT==null)
		{
			MT = new MersenneTwister();
		}
		long tmp = MT.nextInt();
		tmp ^= (tmp << 32);
		return (int) (tmp >>> (48-bits));
	}

	public MTRandom()
	{ 
		MT = new MersenneTwister();
	}

	@Override
	public void setSeed(long s)
	{
		if(MT==null)
		{
			MT = new MersenneTwister();
		}
		MT.setSeed(s);
	}
	
	public static void main(String[] args)
	{
		MTRandom rand = new MTRandom();
		rand.setSeed(0x123456789abcdefL);
		for(int i = 0; i<32; i++)
		System.out.println(String.format("%08x", rand.next(32)));
	}
}
