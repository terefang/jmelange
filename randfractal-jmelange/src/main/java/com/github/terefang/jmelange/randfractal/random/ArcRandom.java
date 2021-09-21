package com.github.terefang.jmelange.randfractal.random;

import com.github.terefang.jmelange.randfractal.AbstractRandom;
import lombok.SneakyThrows;

import java.security.MessageDigest;

public class ArcRandom extends AbstractRandom
{
	int[] _ctx;
	int _a, _b;

	public ArcRandom()
	{
	}

	@Override
	public void setSeed(long _seed)
	{
		_ctx = new int[256];
		for(int _i = 0 ; _i<256; _i++)
		{
			_ctx[_i] = _i;
		}

		int _j = 0;
		for(int _i = 0 ; _i<0x1000000; _i++)
		{
			_j = _j + (int) (((_seed >>> (_i % 48)) & 0xff) + _ctx[_i & 0xff]);
			int _t = _ctx[_j & 0xff];
			_ctx[_j & 0xff] = _ctx[_i & 0xff];
			_ctx[_i & 0xff] = _t;
		}
	}

	
	@Override
	protected int next(int bits) 
	{
		if(bits==8) return nextByte();

		long tmp=nextLong();
		tmp &= ((1L<<(bits+1))-1);
		return (int) (tmp);
	}

	@Override
	public byte nextByte()
	{
		this._a++;
		this._b+=this._ctx[this._a & 0xff];

		int _t = this._ctx[this._b & 0xff];
		this._ctx[this._b & 0xff] = this._ctx[this._a & 0xff];
		this._ctx[this._a & 0xff] = _t;

		int _ret = this._ctx[(this._ctx[this._b & 0xff] + this._ctx[this._a & 0xff]) & 0xff];
		return (byte) _ret;
	}

	@Override
	public short nextShort()
	{
		short b=(short) (nextByte() & 0xff);
		b<<=8;
		b|=(short) (nextByte() & 0xff);
		return b;
	}

	@Override
	public int nextInt()
	{
		int b=(int) (nextShort() & 0xffff);
		b<<=16;
		b|=(int) (nextShort() & 0xffff);
		return b;
	}
	
	@Override
	public long nextLong()
	{
		long b=(long) (nextInt() & 0xffffffff);
		b<<=32;
		b|=(long) (nextInt() & 0xffffffff);
		return b ;
	}
	
	@Override
	public void nextBytes(byte[] dest, int dest_len)
	{
		for(int i=0; i<dest_len ; ++i)
			dest[i]=(byte) (nextByte() & 0xff);
	}

	public synchronized double nextGaussian(double _bound, double _factor, double _base) {
		double _ret = -2.0*_bound;
		while(Math.abs(_ret)>=_bound)
		{
			_ret = super.nextGaussian();
		}
		return (_ret*_factor)+_base;
	}

	public static void main(String[] args)
	{
		long _count = 0x1ffffffffL;
		long[] _buf = new long[256];
		ArcRandom _rand = new ArcRandom();
		_rand.setSeed(0L);
		long _t0 = System.currentTimeMillis();
		for(long _i = 0; _i<_count; _i++)
		{
			_buf[_rand.next(8)&0xff]++;
			//_rand.next(8);
		}
		long _t1 = System.currentTimeMillis();

		for(int _i = 0; _i<0x100; _i++)
		{
			System.out.println(_buf[_i]);
		}
		System.out.println(_count*1000L/(_t1-_t0));
	}
}
