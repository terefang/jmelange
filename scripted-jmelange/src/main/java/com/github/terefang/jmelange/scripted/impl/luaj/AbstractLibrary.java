package com.github.terefang.jmelange.scripted.impl.luaj;

import lombok.SneakyThrows;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.List;

public abstract class AbstractLibrary extends TwoArgFunction
{
	public abstract List<Class> getLibraryFunctions();
	public abstract String getLibraryName();

	@Override
	@SneakyThrows
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaTable string = new LuaTable();
		for(Class _fn : this.getLibraryFunctions())
		{
			String _n = _fn.getSimpleName();
			if(_n.startsWith("_"))
			{
				_n.substring(1);
			}
			System.err.println(_n);
			string.set(_n, (LuaValue) _fn.newInstance());
		}

		env.set(this.getLibraryName(), string);

		if (!env.get("package").isnil()) env.get("package").get("loaded").set(this.getLibraryName(), string);

		return string;
	}
}
