package com.github.terefang.jmelange.randfractal.random;

import com.github.terefang.jmelange.randfractal.AbstractRandom;
import lombok.SneakyThrows;

import java.security.MessageDigest;

public class BlockRandom extends AbstractRandom
{
	private static final int BLOCKBUFF = 0x1000;
	private static final int BLOCKMASK = 0xfff;
	private byte cbuf[];
	private MessageDigest ctx;
	private int offs = 0;
	
	public BlockRandom()
	{
	}

	@Override
	public void setSeed(long s)
	{
		if(cbuf==null) cbuf = new byte[BLOCKBUFF];
		
		offs=0;
		for(int i=0x100 ; i<BLOCKBUFF ; i++)
		{
			cbuf[i]=(byte) (s&0xff);
			s>>>=7;
		}
		md5ify();
	}

	
	@Override
	protected int next(int bits) 
	{
		if(bits == 8) return nextByte() & 0xff;

		long tmp=nextLong();
		tmp &= ((1L<<48)-1);
		return (int) (tmp >>> (48-bits));
	}

	@Override
	public byte nextByte()
	{
		byte b=(byte) (cbuf[offs] & 0xff);
		offs=(offs+1) & 0xff;
		if(offs==0) md5ify();
		return b;
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

	@SneakyThrows
	private void md5ify()
	{
		if(ctx==null) ctx = MessageDigest.getInstance("MD5");
		
		ctx.reset();
		ctx.update(cbuf, 0, BLOCKBUFF);
		ctx.digest(cbuf, 0, ctx.getDigestLength());
	}

	public static void main(String[] args)
	{
		long _count = 0x1ffffffffL;
		long[] _buf = new long[256];
		BlockRandom _rand = new BlockRandom();
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
