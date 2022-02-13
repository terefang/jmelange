package com.github.terefang.jmelange.random;

import java.util.Random;

public class AbstractRandom extends Random implements IRandom
{

	public byte nextByte() 
	{
		return (byte)next(8);
	}

	public void nextBytes(byte[] dest, int destLen) 
	{
		for(int i=0; i<destLen; i++)
			dest[i]=nextByte();
	}

	@Override
	public int nextInt()
	{
		int tmp = (int) (next(8)<<24);
		tmp |= (int)(next(8)<<16);
		tmp |= (int)(next(8)<<8);
		tmp |= (int)(next(8));
		return tmp;
	}

	@Override
	public short nextShort()
	{
		short tmp = (short) (next(8)<<8);
		tmp |= (short)next(8);
		return tmp;
	}

	@Override
	public long nextLong() {
		long tmp = ((long)next(8))<<56;
		tmp |= (long)next(8)<<48;
		tmp |= (long)next(8)<<40;
		tmp |= (long)next(8)<<32;
		tmp |= (long)next(8)<<24;
		tmp |= (long)next(8)<<16;
		tmp |= (long)next(8)<<8;
		tmp |= (long)next(8);
		return tmp;
	}
	
	public double rand()
	{ 
		return (double)(randInt() & 0x7fffffff) * (1.0/4294967295.0);
	}

	public double rand(final double n)
	{ 
		return rand() * n; 
	}


	public double randExc(final double n)
	{ 
		return randExc() * n; 
	}


	public double randDblExc(final double n)
	{ 
		return randDblExc() * n; 
	}

	public double rand53()
	{
		int a = randInt() >>> 5, b = randInt() >>> 6;
		return ( a * 67108864.0 + b ) * (1.0/9007199254740992.0);
	}

	public double randExc()
	{
		return (double)(randInt() & 0x7fffffff) * (1.0/4294967296.0);
	}

	public double randDblExc()
	{
		return ( (double)(randInt() & 0x7fffffff) + 0.5 ) * (1.0/4294967296.0);
	}

	public double randNorm(final double mean, final double variance)
	{
		// Return a real number from a normal (Gaussian) distribution with given
		// mean and variance by Box-Muller method
		double r = Math.sqrt( -2.0 * Math.log( 1.0-randDblExc()) ) * variance;
		double phi = 2.0 * 3.14159265358979323846264338328 * randExc();
		return mean + r * Math.cos(phi);
	}

	public int randInt()
	{
		return next(32);
	}

	public int randInt(final int n)
	{
		// Find which bits are used in n
		int used = n;
		used |= used >>> 1;
		used |= used >>> 2;
		used |= used >>> 4;
		used |= used >>> 8;
		used |= used >>> 16;
		
		// Draw numbers until one is found in [0,n]
		int i;
		do
		{
			i = randInt() & used;  // toss unused bits to shorten search
		}
		while( i > n );
		return i;
	}

	public synchronized double nextGaussian(double _bound, double _factor, double _base) {
		double _ret = -2.0*_bound;
		while(Math.abs(_ret)>=_bound)
		{
			_ret = super.nextGaussian();
		}
		return (_ret*_factor)+_base;
	}
}
