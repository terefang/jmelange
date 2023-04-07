package com.github.terefang.jmelange.scripted.impl.luay.useful;

import com.github.terefang.jmelange.scripted.impl.luay.AbstractLibrary;
import com.google.common.collect.Lists;
import luay.vm.*;
import luay.vm.lib.OneArgFunction;
import luay.vm.lib.TwoArgFunction;
import luay.vm.lib.VarArgFunction;

import java.util.List;

public class BitopLib extends AbstractLibrary
{
	public BitopLib()
	{
		super();
	}

	@Override
	public List<Class> getLibraryFunctions() {
		return Lists.newArrayList(_tobit.class,
				_tohex.class,
				_bnot.class,
				_bor.class,
				_band.class,
				_bxor.class,
				_rol.class,
				_ror.class,
				_lshift.class,
				_rshift.class,
				_arshift.class,
				_bswap.class
		);
	}

	@Override
	public String getLibraryName() {
		return "bit";
	}

	// y = bit.tobit(x)
	static class _tobit extends OneArgFunction
	{
		@Override
		public LuaValue call(LuaValue arg) {
			return LuaInteger.valueOf(arg.checkint());
		}
	}

	// y = bit.tohex(x [,n])
	static class _tohex extends VarArgFunction
	{
		@Override
		public Varargs invoke(Varargs _args)
		{
			int _i = _args.checkint(1);
			int _n = 8;
			if(_args.narg()==2)
			{
				_n = _args.checkint(2);
				if(_n<0) _n = -_n;
			}

			return LuaString.valueOf(String.format("%0"+_n+"x", _i));
		}
	}

	// y = bit.bnot(x)
	static class _bnot extends OneArgFunction
	{
		@Override
		public LuaValue call(LuaValue arg) {
			return LuaInteger.valueOf(~arg.checkint());
		}
	}

	// y = bit.bor(x1 [,x2...])
	static class _bor extends VarArgFunction
	{
		@Override
		public Varargs invoke(Varargs _args)
		{
			int _n = _args.checkint(1);
			for(int _i=2; _i<=_args.narg(); _i++)
			{
				_n |= _args.checkint(_i);
			}
			return LuaInteger.valueOf(_n);
		}
	}

	// y = bit.band(x1 [,x2...])
	static class _band extends VarArgFunction
	{
		@Override
		public Varargs invoke(Varargs _args)
		{
			int _n = _args.checkint(1);
			for(int _i=2; _i<=_args.narg(); _i++)
			{
				_n &= _args.checkint(_i);
			}
			return LuaInteger.valueOf(_n);
		}
	}

	// y = bit.bxor(x1 [,x2...])
	static class _bxor extends VarArgFunction
	{
		@Override
		public Varargs invoke(Varargs _args)
		{
			int _n = _args.checkint(1);
			for(int _i=2; _i<=_args.narg(); _i++)
			{
				_n ^= _args.checkint(_i);
			}
			return LuaInteger.valueOf(_n);
		}
	}

	// y = bit.lshift(x, n)
	static class _lshift extends TwoArgFunction
	{
		@Override
		public LuaValue call(LuaValue _arg1, LuaValue _arg2) {
			return LuaInteger.valueOf(_arg1.checkint() << _arg2.checkint());
		}
	}

	// y = bit.rshift(x, n)
	static class _rshift extends TwoArgFunction
	{
		@Override
		public LuaValue call(LuaValue _arg1, LuaValue _arg2) {
			return LuaInteger.valueOf(_arg1.checkint() >>> _arg2.checkint());
		}
	}

	// y = bit.arshift(x, n)
	static class _arshift extends TwoArgFunction
	{
		@Override
		public LuaValue call(LuaValue _arg1, LuaValue _arg2) {
			return LuaInteger.valueOf(_arg1.checkint() >> _arg2.checkint());
		}
	}

	// y = bit.rol(x, n)
	static class _rol extends TwoArgFunction
	{
		@Override
		public LuaValue call(LuaValue _arg1, LuaValue _arg2) {
			return LuaInteger.valueOf(Integer.rotateLeft(_arg1.checkint(), _arg2.checkint()));
		}
	}

	// y = bit.ror(x, n)
	static class _ror extends TwoArgFunction
	{
		@Override
		public LuaValue call(LuaValue _arg1, LuaValue _arg2) {
			return LuaInteger.valueOf(Integer.rotateRight(_arg1.checkint(), _arg2.checkint()));
		}
	}

	// y = bit.bswap(x)
	static class _bswap extends OneArgFunction
	{
		@Override
		public LuaValue call(LuaValue _arg1) {
			return LuaInteger.valueOf(Integer.reverseBytes(_arg1.checkint()));
		}
	}
}





