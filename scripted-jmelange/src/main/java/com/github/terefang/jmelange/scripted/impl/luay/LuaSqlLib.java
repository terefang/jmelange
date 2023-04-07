package com.github.terefang.jmelange.scripted.impl.luay;

import com.github.terefang.jmelange.dao.JDAO;
import com.github.terefang.jmelange.dao.utils.JdaoUtils;
import lombok.SneakyThrows;
import luay.vm.*;
import luay.vm.lib.ThreeArgFunction;
import luay.vm.lib.TwoArgFunction;
import luay.vm.lib.jse.CoerceJavaToLua;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LuaSqlLib extends TwoArgFunction
{
	public LuaSqlLib()
	{
	}

	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue _mysql = new _driver("mysql","","jdbc:mysql://");
		LuaValue _sqlite = new _driver("sqlite","","jdbc:sqlite:");

		env.set("luasql_mysql", _mysql);
		env.set("luasql_sqlite", _sqlite);

		return new LuaTable();
	}

	static final class _driver extends ThreeArgFunction
	{
		String driverName;
		String driverClass;
		String driverPrefix;

		public _driver(String _name, String _clazz, String _prefix)
		{
			super();
			this.driverName = _name;
			this.driverClass = _clazz;
			this.driverPrefix = _prefix;
		}

		@Override
		@SneakyThrows
		public LuaValue /*connect*/ call(LuaValue _lsuffix, LuaValue _luser, LuaValue _lpass)
		{
			String _suffix = _lsuffix.checkjstring();
			String _user = _luser.checkjstring();
			String _pass = _lpass.checkjstring();

			JDAO _dao = JDAO.createDaoFromDataSource(JdaoUtils.createDataSource(this.driverClass, this.driverPrefix + _suffix, _user, _pass), false);
			if("mysql".equalsIgnoreCase(this.driverName)
					|| "mariadb".equalsIgnoreCase(this.driverName))
			{
				_dao.setDbType(JDAO.DB_TYPE_MYSQL);
			}
			else
			if("sqlite".equalsIgnoreCase(this.driverName))
			{
				_dao.setDbType(JDAO.DB_TYPE_SQLITE);
			}
			else
			if("postgres".equalsIgnoreCase(this.driverName)
					|| "postgresql".equalsIgnoreCase(this.driverName))
			{
				_dao.setDbType(JDAO.DB_TYPE_POSTGRES);
			}
			else
			if("mssql".equalsIgnoreCase(this.driverName))
			{
				_dao.setDbType(JDAO.DB_TYPE_MSSQL);
			}
			return CoerceJavaToLua.coerce(new _conn(_dao));
		}
	}

	static final class _conn
	{
		JDAO _jdao;
		public _conn(JDAO _dao)
		{
			this._jdao = _dao;
		}

		public void setAutoCommit(boolean ac) {
			_jdao.setAutoCommit(ac);
		}

		public void rollbackTransaction() {
			_jdao.rollbackTransaction();
		}

		@SneakyThrows
		public void execute(String _query) {
			_jdao.execute(_query);
		}

		@SneakyThrows
		public Object query(String _query) {
			return javaListMapToLua(_jdao.queryForList(_query));
		}

		public void close() {
			_jdao.close();
		}
	}

	static LuaTable javaListMapToLua(List<Map<String, Object>> _res)
	{
		LuaTable _ret = new LuaTable(_res.size(), 0);
		int _i = 1;
		for(Map<String, Object> _row : _res)
		{
			LuaTable _lrow = new LuaTable();
			for(Map.Entry<String, Object> _col : _row.entrySet())
			{
				_lrow.set(_col.getKey(), javaToLua(_col.getValue()));
			}
			_ret.set(_i, _lrow);
			_i++;
		}
		return _ret;
	}

	static LuaTable javaListToLua(List<Object> _res)
	{
		LuaTable _ret = new LuaTable(_res.size(), 0);
		int _i = 1;
		for(Object _row : _res)
		{
			_ret.set(_i, javaToLua(_row));
			_i++;
		}
		return _ret;
	}

	static LuaTable javaMapToLua(Map<String, Object> _row)
	{
		LuaTable _lrow = new LuaTable();
		for(Map.Entry<String, Object> _col : _row.entrySet())
		{
			_lrow.set(_col.getKey(), javaToLua(_col.getValue()));
		}
		return _lrow;
	}

	static LuaValue javaToLua(Object _o)
	{
		if(_o == null)
		{
			return LuaNil.NIL;
		}
		else
		if(_o instanceof String)
		{
			return LuaString.valueOf((String)_o);
		}
		else
		if(_o instanceof Long)
		{
			return LuaNumber.valueOf((Long)_o);
		}
		else
		if(_o instanceof Integer)
		{
			return LuaInteger.valueOf((Integer)_o);
		}
		else
		if(_o instanceof Double)
		{
			return LuaDouble.valueOf((Double)_o);
		}
		else
		if(_o instanceof Float)
		{
			return LuaDouble.valueOf((Float)_o);
		}
		else
		if(_o instanceof Boolean)
		{
			return LuaBoolean.valueOf((Boolean) _o);
		}
		else
		{
			return LuaString.valueOf(Objects.toString(_o));
		}
	}
}
