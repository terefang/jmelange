package com.github.terefang.jmelange.randfractal;

import com.github.terefang.jmelange.randfractal.random.BlockRandom;
import com.github.terefang.jmelange.randfractal.random.JavaRandom;
import com.github.terefang.jmelange.randfractal.random.MTFRandom;
import com.github.terefang.jmelange.randfractal.random.MTRandom;

public interface IRandom
{
	public void setSeed(long s);
	public byte nextByte();
	public short nextShort();
	public int nextInt();
	public long nextLong();
	public void nextBytes(byte[] dest, int dest_len);

	public double rand();
	public double rand(final double n);
	public double randExc();
	public double randExc(final double n);
	public double randDblExc();
	public double randDblExc(final double n);
	public double rand53();
	public double randNorm(final double mean, final double variance);
	public int randInt();
	public int randInt(final int n);

	public static IRandom blockRandom(long seed)
	{
		BlockRandom _rng = new BlockRandom();
		_rng.setSeed(seed);
		return _rng;
	}

	public static IRandom mtRandom(long seed)
	{
		MTRandom _rng = new MTRandom();
		_rng.setSeed(seed);
		return _rng;
	}

	public static IRandom mtfRandom(long seed)
	{
		MTFRandom _rng = new MTFRandom();
		_rng.setSeed(seed);
		return _rng;
	}

	public static IRandom javaRandom(long seed)
	{
		JavaRandom _rng = new JavaRandom((int) seed);
		_rng.setSeed(seed);
		return _rng;
	}

}
