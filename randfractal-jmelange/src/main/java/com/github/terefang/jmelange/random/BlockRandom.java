package com.github.terefang.jmelange.random;

import lombok.SneakyThrows;

import java.security.MessageDigest;

public class BlockRandom extends AbstractRandom
{

	public BlockRandom()
	{
	}

	private static final int BLOCKBUFF = 0x1000;
	private static final int BLOCKMASK = 0xfff;
	private byte cbuf[];
	private MessageDigest ctx;
	private int offs = 0;

	@Override
	public void setSeed(long s)
	{
		if(cbuf==null) cbuf = new byte[BLOCKBUFF];
		
		offs=0;
		for(int _i=0 ; _i<BLOCKBUFF ; _i++)
		{
			cbuf[_i]=(byte) (((s >>> (_i % 56))&0xff) ^ _i);
		}
		md5ify();
	}

	@Override
	public byte nextByte()
	{
		byte b=(byte) (cbuf[offs & BLOCKMASK] & 0xff);
		offs=(offs+1) & BLOCKMASK;
		if(offs==0) md5ify();
		return b;
	}


	@SneakyThrows
	private void md5ify()
	{
		if(ctx==null) ctx = MessageDigest.getInstance("MD5");

		for(int _i=0; _i<BLOCKBUFF; _i+=ctx.getDigestLength())
		{
			ctx.reset();
			ctx.update(cbuf, 0, BLOCKBUFF);
			ctx.digest(cbuf, _i, ctx.getDigestLength());
		}
	}

	@Override
	protected int next(int bits) 
	{
		if(bits == 8) return nextByte() & 0xff;
		if(bits == 16) return nextShort() & 0xffff;
		if(bits == 24) return nextInt() & 0xffffff;
		if(bits == 32) return nextInt();

		int _tmp = nextInt();

		return _tmp & ((1<<(bits-1))-1);
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

	public static void main(String[] args)
	{
		long _count = 0x1ffffffL;
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
