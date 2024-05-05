package com.github.terefang.jmelange.random;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.HashUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class ArcRand {
    public int[] _ctx;
    int _a, _b;

    public static ArcRand from(byte[] _bytes)
    {
        return new ArcRand().init().seed( _bytes);
    }

    public static ArcRand from(String _text, int _limit)
    {
        return new ArcRand().init(_limit).seed(_text);
    }

    public static ArcRand from(long _seed, int _limit)
    {
        return new ArcRand().init(_limit).seed(_seed);
    }

    public static ArcRand from(String _text)
    {
        return from(_text,256);
    }

    public static ArcRand from(long _seed)
    {
        return from(_seed, 256);
    }

    public static ArcRand from(int _seed)
    {
        return from(_seed, 256);
    }

    public static ArcRand from(int _seed, int _limit)
    {
        return new ArcRand().init(_limit).seed(_seed);
    }

    public ArcRand init()
    {
        return init(256);
    }

    public ArcRand init(int _limit)
    {
        this._ctx = new int[_limit];
        for(int _i = 0 ; _i<_limit; _i++)
        {
            this._ctx[_i] = _i;
        }
        return this;
    }

    public ArcRand seed(long _long)
    {
        return seed((int)(_long >>> 32)).seed((int)(_long & 0xffffffffL));
    }

    public ArcRand seed(String _text)
    {
        return seed(_text.getBytes(StandardCharsets.UTF_8));
    }

    public ArcRand seed(int _seed)
    {
        while((_seed & 0x7fffffff)>0)
        {
            long _j = _seed & 0xffffffffL;
            for(int _i = 0 ; _i<_ctx.length; _i++)
            {
                _j = _j + _ctx[_i % _ctx.length];
                int _t = _ctx[(int)(_j % _ctx.length)];
                _ctx[(int)(_j % _ctx.length)] = _ctx[_i % _ctx.length];
                _ctx[_i % _ctx.length] = _t;
            }
            _seed>>>=1;
        }
        return this;
    }

    public ArcRand seed(byte[] _seed)
    {
        _seed = HashUtil.sha512(_seed);
        for(byte _s : _seed)
        {
            int _j = _s & 0xff;
            for(int _i = 0 ; _i<_ctx.length; _i++)
            {
                _j = _j + _ctx[_i % _ctx.length];
                int _t = _ctx[_j % _ctx.length];
                _ctx[_j % _ctx.length] = _ctx[_i % _ctx.length];
                _ctx[_i % _ctx.length] = _t;
            }
        }
        return this;
    }

    public int next()
    {
        this._a++;
        if(_ctx.length!=256)
        {
            this._b+=this._ctx[this._a % _ctx.length];

            int _t = this._ctx[this._b % _ctx.length];
            this._ctx[this._b % _ctx.length] = this._ctx[this._a % _ctx.length];
            this._ctx[this._a % _ctx.length] = _t;

            return this._ctx[(this._ctx[this._b % _ctx.length] + this._ctx[this._a % _ctx.length]) % _ctx.length];
        }
        else
        {
            this._b+=this._ctx[this._a & 0xff];

            int _t = this._ctx[this._b & 0xff];
            this._ctx[this._b & 0xff] = this._ctx[this._a & 0xff];
            this._ctx[this._a & 0xff] = _t;

            return this._ctx[(this._ctx[this._b & 0xff] + this._ctx[this._a & 0xff]) & 0xff];
        }
    }

    public int next16()
    {
        return (next()<<8) | (~next() & 0xff) &0xffff;
    }

    public int next32()
    {
        return (next16()<<16) | (~next16() & 0xffff);
    }

    public long next64() { return ((long) next32() <<32L)|(~next32() & 0xffffffffffffL); }

    public int nextInt(int _x) { return ((next32() & 0x7fffffff) % _x); }
    public long nextLong(long _x) { return ((next64() & 0x7fffffffffffffffL) % _x); }

    public float nextFloat()
    {
        return next()/256f;
    }

    public float nextFloat16()
    {
        return next16()/65536f;
    }

    public float nextFloat32()
    {
        return (next32() & 0x7fffffff)/((float)0x80000000L);
    }

    public double nextDouble()
    {
        return (next64() & 0x7ffffffffffffffL)/((double)0x800000000000000L);
    }

    public float nextGauss()
    {
        return (nextFloat32() - nextFloat32());
    }

    public float nextGauss(int _d, int _t)
    {
        float _r = 1f;
        for(int _i = 0; _i<(_d*2); _i++) _r*=nextFloat32()*(_t+1);
        for(int _i = 0; _i<_d; _i++) _r/=_t;
        return _r;
    }

    public float nextITF(float _d)
    {
        float _r = 0f;
        for(int _i = 0; _i<10; _i++) _r+=nextFloat32()*_d;
        return _r/10f;
    }

    public int nextRange(int _s, int _e)
    {
        return (int)((nextFloat32()*(_e-_s))+_s);
    }

    public int nextDice(int _d, int _t, int _b)
    {
        int _r = 0;
        for(int _i = 0; _i<_d; _i++) _r+=(nextFloat32()*(_t))+1;
        return _r+_b;
    }

    public float next(float _v)
    {
        return nextFloat32()*_v;
    }

    public double next(double _v)
    {
        return nextFloat32()*_v;
    }

    public float nextBounds(float _x, float _y, float _g)
    {
        return _x+(((int)(next(_y-_x)/_g))*_g);
    }

    public float nextBounds(float _x, float _y)
    {
        return nextBounds(_x, _y, 1f);
    }

    public float nextVariant(float _x, float _y, float _g)
    {
        return _x+nextBounds(-_y,_y,_g);
    }

    public float nextVariant(float _x, float _y)
    {
        return nextVariant(_x,_y,1f);
    }

    public double nextVariant(double _x, double _y, double _g)
    {
        return _x+nextBounds((float)-_y,(float)_y,(float)_g);
    }

    public double nextVariant(double _x, double _y)
    {
        return nextVariant((double)_x,(double)_y,(double)1);
    }

    public float nextAbout(float _x, float _y, float _g)
    {
        return ((int)((_x+((next(_y*2f)-_y)*_x))/_g))*_g;
    }

    public float nextAbout(float _x, float _y)
    {
        return nextAbout(_x,_y, 0.00001f);
    }

    public boolean nextBoolean()
    {
        return ((next() & 1) == 0);
    }

    public int peakedPowerLaw(int _max, int _mode, int _pct) {
        if (nextBoolean()) {
            // going upward from mode
            return _mode + powerLaw(_max - _mode, _pct);
        } else {
            // going downward from mode
            return _mode - powerLaw(_mode + 1, _pct);
        }
    }

    public int powerLaw(int _max, int _pct) {
        for (int r = 0; true; r = (r + 1) % _max) {
            int randomPercent = nextInt(101);
            if (randomPercent <= _pct) {
                return r;
            }
        }
    }

    public int powerLaw(int _max, double _alpha)
    {
        if(_alpha<1.0) _alpha=1.0;
        return (int)(_max * Math.pow(nextDouble(), _alpha));
    }

    public <T> T powerLaw(int _pct, T... _list)
    {
        return _list[powerLaw(_list.length, _pct)];
    }

    public <T> T powerLaw(double _alpha, T... _list)
    {
        return _list[powerLaw(_list.length, _alpha)];
    }

    public <T> T powerLaw(int _pct, List<T> _list)
    {
        return _list.get(powerLaw(_list.size(), _pct));
    }

    public <T> T powerLaw(double _alpha, List<T> _list)
    {
        return _list.get(powerLaw(_list.size(), _alpha));
    }


    public static void main(String[] args)
    {
        ArcRand _rand = from(0x1234567);
        int[] _histo = new int[256];
        for(int _i = 0; _i<8192; _i++)
        {
            _histo[_rand.powerLaw(256, 1.7)]++;
        }
        for(int _i = 0; _i<256; _i++)
        {
            System.out.println(_i+" = "+_histo[_i]);
        }
        System.out.println();
    }

}