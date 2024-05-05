/*
 * Copyright (c) 2017. terefang@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.terefang.jmelange.dao;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.dao.rsh.BasicXRowProcessor;
import com.github.terefang.jmelange.dao.rsh.KvListMapHandler;
import com.github.terefang.jmelange.dao.rsh.KvMapHandler;
import com.github.terefang.jmelange.dao.utils.JdaoUtils;
import com.github.terefang.jmelange.dao.utils.JndiUtils;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.*;

import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.Connection;
import java.util.*;

public class JDAO implements Closeable
{
	public void checkReadOnly() throws Exception
	{
		if(this.isReadOnly())
		{
			throw new IllegalAccessException("JDAO is read-only.");
		}
	}
	
	public boolean isReadOnly()
	{
		return readOnly;
	}
	
	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}
	
	private boolean readOnly = false;
	
	public static Map<String,Object> toMap(Object... values)
	{
		Map<String,Object> ret = new HashMap();
		for(int i=0; i<values.length; i+=2)
		{
			ret.put(String.valueOf(values[i]), values[i+1]);
		}
		return ret;
	}
	
	public static Map<String, String> toMapString(String... values)
	{
		Map<String, String> ret = new HashMap();
		for(int i=0; i<values.length; i+=2)
		{
			ret.put(values[i], values[i+1]);
		}
		return ret;
	}
	
	public static Collection toCollection(Object... values)
	{
		ArrayList ret = new ArrayList(values.length);
		for(int i=0; i<values.length; i++)
		{
			ret.add(values[i]);
		}
		return ret;
	}
	
	static GenerousBeanProcessor generousBeanProcessor = new GenerousBeanProcessor();
	static BasicRowProcessor generousRowProcessor = new BasicRowProcessor(generousBeanProcessor);
	static BasicXRowProcessor basicxRowProcessor  = new BasicXRowProcessor();
	static MapListHandler mapListHandler = new MapListHandler(basicxRowProcessor);
	static MapHandler mapHandler = new MapHandler(basicxRowProcessor);
	static ScalarHandler<Object> scalarHandler = new ScalarHandler<Object>();
	static ArrayListHandler arrayListHandler = new ArrayListHandler();
	static ColumnListHandler<Object> columnListHandler = new ColumnListHandler<Object>();
	static KvMapHandler kvMapHandler = new KvMapHandler();
	static KvListMapHandler kvListMapHandler = new KvListMapHandler();
	
	public static JDAO createDaoFromDataSource(DataSource ds, boolean pmt)
			throws Exception
	{
		return new JDAO(new QueryRunner(ds, pmt));
	}
	
	public static JDAO createDaoFromConnection(Connection conn, boolean pmt)
			throws Exception
	{
		return new JDAO(conn, new QueryRunner(pmt));
	}
	
	public static JDAO createDaoFromJndi(String jndiUri, boolean pmt)
			throws Exception
	{
		return new JDAO(JndiUtils.lookupDataSourceFromJndi(jndiUri).getConnection(), new QueryRunner(pmt));
	}
	
	/**
	 * queries connection according to give dbType and returns data as given by resultsethandler
	 *
	 * @param  dbType, type of database
	 * @param  rsHandler, resultsethandler
	 * @param  conn, database connection
	 * @param  ds, query runner
	 * @param  sql, sql query
	 * @param  args, sql parameters
	 * @return object of type T or null
	 */

	public static <T> T
	queryForT(int dbType, ResultSetHandler<T> rsHandler, Connection conn, QueryRunner ds, String sql, List args) throws Exception
	{
		return queryForT(dbType, rsHandler, conn, ds, sql, args.toArray());
	}

	public static <T> T
	queryForT(int dbType, ResultSetHandler<T> rsHandler, Connection conn, QueryRunner ds, String sql, Collection args) throws Exception
	{
		return queryForT(dbType, rsHandler, conn, ds, sql, args.toArray());
	}

	public static <T> T
	queryForT(int dbType, ResultSetHandler<T> rsHandler, Connection conn, QueryRunner ds, String sql, Object... args) throws Exception
	{
		if(args == null)
		{
			if(conn==null)
			{
				return ds.query(sql, rsHandler);
			}
			else
			{
				return ds.query(conn, sql, rsHandler);
			}
		}
		else
		if(args.length == 1 && args[0] instanceof Map)
		{
			List nArgs = new Vector();
			sql = JdaoUtils.preparseParameters(dbType, sql, nArgs, (Map) args[0]);
			if(conn==null)
			{
				return ds.query(sql, rsHandler, nArgs.toArray());
			}
			else
			{
				return ds.query(conn, sql, rsHandler, nArgs.toArray());
			}
		}
		else
		if(args.length > 0 && args[0] instanceof Collection)
		{
			if(conn==null)
			{
				return ds.query(sql, rsHandler, ((Collection)args[0]).toArray());
			}
			else
			{
				return ds.query(conn, sql, rsHandler, ((Collection)args[0]).toArray());
			}
		}
		else
		{
			if(conn==null)
			{
				return ds.query(sql, rsHandler, args);
			}
			else
			{
				return ds.query(conn, sql, rsHandler, args);
			}
		}
	}
	
	public static String join(Collection c, final char separator)
	{
		if (c == null)
		{
			return null;
		}
		
		final StringBuilder buf = new StringBuilder();
		Object[] array = c.toArray();
		int end = array.length;
		for (int i = 0; i < end; i++)
		{
			if (i > 0)
			{
				buf.append(separator);
			}
			if (array[i] != null)
			{
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}
	
	public static <T> T
	queryTemplateForT(int dbType, ResultSetHandler<T> rsHandler, Connection conn, QueryRunner ds, String table, Collection cols, Map<String,Object> vm, String suffixQuery, int templateType, int constraintType)
			throws Exception
	{
		List param = new Vector();
		String colString = ((cols == null) ? "*" : JDAO.join(cols, ','));
		return JDAO.queryForT(dbType, rsHandler, conn, ds,  "SELECT "+colString+" FROM "+table+" WHERE "+JdaoUtils.buildWhere(dbType, templateType, constraintType, param, vm)+(suffixQuery==null?"":" "+suffixQuery), param);
	}
	
	public static <T> T
	queryTemplateForT(int dbType, ResultSetHandler<T> rsHandler, Connection conn, QueryRunner ds, String table, Collection cols, Map<String,Object> vm, String suffixQuery)
			throws Exception
	{
		return JDAO.queryTemplateForT(dbType, rsHandler, conn, ds, table, cols, vm, suffixQuery, JdaoUtils.TEMPLATE_TYPE_AUTO, JdaoUtils.CONSTRAINT_ALL_OF);
	}
	
	public static <T> T
	queryTemplateForT(int dbType, ResultSetHandler<T> rsHandler, Connection conn, QueryRunner ds, String table, Collection cols, Map<String,Object> vm)
			throws Exception
	{
		return JDAO.queryTemplateForT(dbType, rsHandler, conn, ds, table, cols, vm, null, JdaoUtils.TEMPLATE_TYPE_AUTO, JdaoUtils.CONSTRAINT_ALL_OF);
	}
	
	public static <T> T
	queryTemplateForT(int dbType, ResultSetHandler<T> rsHandler, Connection conn, QueryRunner ds, String table, Map<String,Object> vm)
			throws Exception
	{
		return JDAO.queryTemplateForT(dbType, rsHandler, conn, ds, table, null, vm, null, JdaoUtils.TEMPLATE_TYPE_AUTO, JdaoUtils.CONSTRAINT_ALL_OF);
	}
	
	/**
	 * executes a query and returns a list of rows (Map)
	 * <p>
	 * if the only (first) argument is a map, the sql string is expected to have "?{field-name}" named parameters
	 * <p>
	 * if the only (first) argument is a list (collection), it is take as the list of arguments.
	 *
	 */
	public static List<Map<String,Object>>
	queryForList(int dbType, Connection conn, QueryRunner ds, String sql, Object... args)
			throws Exception
	{
		return queryForMapList(dbType, conn, ds, sql, args);
	}

	public static List<Map<String,Object>>
	queryForList(int dbType, Connection conn, QueryRunner ds, String sql, List args)
			throws Exception
	{
		return queryForMapList(dbType, conn, ds, sql, args);
	}

	public static List<Map<String,Object>>
	queryForList(int dbType, Connection conn, QueryRunner ds, String sql, Collection args)
			throws Exception
	{
		return queryForMapList(dbType, conn, ds, sql, args);
	}

	public static List<Map<String,Object>>
	queryForList(int dbType, Connection conn, QueryRunner ds, String sql)
			throws Exception
	{
		return queryForMapList(dbType, conn, ds, sql);
	}

	public static List<Map<String,Object>>
	queryForMapList(int dbType, Connection conn, QueryRunner ds, String sql, Object... args)
			throws Exception
	{
		return queryForT(dbType, mapListHandler, conn, ds, sql, args);
	}

	public static List<Map<String,Object>>
	queryForMapList(int dbType, Connection conn, QueryRunner ds, String sql, List args)
			throws Exception
	{
		return queryForT(dbType, mapListHandler, conn, ds, sql, args);
	}

	public static List<Map<String,Object>>
	queryForMapList(int dbType, Connection conn, QueryRunner ds, String sql, Collection args)
			throws Exception
	{
		return queryForT(dbType, mapListHandler, conn, ds, sql, args);
	}

	public static List<Map<String,Object>>
	queryForMapList(int dbType, Connection conn, QueryRunner ds, String sql)
			throws Exception
	{
		return queryForT(dbType, mapListHandler, conn, ds, sql);
	}
	
	public static List<Map<String,Object>>
	queryTemplateForMapList(int dbType, Connection conn, QueryRunner ds, String table, Collection cols, Map vm, String suffixQuery, int templateType, int constraintType)
			throws Exception
	{
		return queryTemplateForT(dbType, mapListHandler, conn, ds, table, cols, vm, suffixQuery, templateType, constraintType);
	}
	
	public static List<Map<String,Object>>
	queryTemplateForMapList(int dbType, Connection conn, QueryRunner ds, String table, Collection cols, Map vm, String suffixQuery)
			throws Exception
	{
		return queryTemplateForT(dbType, mapListHandler, conn, ds, table, cols, vm, suffixQuery, JdaoUtils.TEMPLATE_TYPE_AUTO, JdaoUtils.CONSTRAINT_ALL_OF);
	}
	
	public static List<Map<String,Object>>
	queryTemplateForMapList(int dbType, Connection conn, QueryRunner ds, String table, Collection cols, Map vm)
			throws Exception
	{
		return queryTemplateForT(dbType, mapListHandler, conn, ds, table, cols, vm);
	}
	
	public static List<Map<String,Object>>
	queryTemplateForMapList(int dbType, Connection conn, QueryRunner ds, String table, Map vm)
			throws Exception
	{
		return queryTemplateForT(dbType, mapListHandler, conn, ds, table, vm);
	}
	
	/**
	 * executes a query and returns a list of arrays (Object[])
	 * <p>
	 * if the only (first) argument is a map, the sql string is expected to have "?{field-name}" named parameters
	 * <p>
	 * if the only (first) argument is a list (collection), it is take as the list of arguments.
	 *
	 */
	public static List<Object[]> queryForArrayList(int dbType, Connection conn, QueryRunner ds, String sql, Object... args)
			throws Exception
	{
		return queryForT(dbType, arrayListHandler, conn, ds, sql, args);
	}

	public static List<Object[]> queryForArrayList(int dbType, Connection conn, QueryRunner ds, String sql, List args)
			throws Exception
	{
		return queryForT(dbType, arrayListHandler, conn, ds, sql, args);
	}

	public static List<Object[]> queryForArrayList(int dbType, Connection conn, QueryRunner ds, String sql, Collection args)
			throws Exception
	{
		return queryForT(dbType, arrayListHandler, conn, ds, sql, args);
	}

	public static List<Object[]> queryForArrayList(int dbType, Connection conn, QueryRunner ds, String sql)
			throws Exception
	{
		return queryForT(dbType, arrayListHandler, conn, ds, sql);
	}
	
	public static List<Object[]>
	queryTemplateForArrayList(int dbType, Connection conn, QueryRunner ds, String table, Collection cols, Map vm, String suffixQuery, int templateType, int constraintType)
			throws Exception
	{
		return queryTemplateForT(dbType, arrayListHandler, conn, ds, table, cols, vm, suffixQuery, templateType, constraintType);
	}
	
	public static List<Object[]>
	queryTemplateForArrayList(int dbType, Connection conn, QueryRunner ds, String table, Collection cols, Map vm, String suffixQuery)
			throws Exception
	{
		return queryTemplateForT(dbType, arrayListHandler, conn, ds, table, cols, vm, suffixQuery, JdaoUtils.TEMPLATE_TYPE_AUTO, JdaoUtils.CONSTRAINT_ALL_OF);
	}
	
	public static List<Object[]>
	queryTemplateForArrayList(int dbType, Connection conn, QueryRunner ds, String table, Collection cols, Map vm)
			throws Exception
	{
		return queryTemplateForT(dbType, arrayListHandler, conn, ds, table, cols, vm);
	}
	
	public static List<Object[]>
	queryTemplateForArrayList(int dbType, Connection conn, QueryRunner ds, String table, Map vm)
			throws Exception
	{
		return queryTemplateForT(dbType, arrayListHandler, conn, ds, table, vm);
	}
	
	/**
	 * executes a query and returns a list of scalars (Object)
	 * <p>
	 * if the only (first) argument is a map, the sql string is expected to have "?{field-name}" named parameters
	 * <p>
	 * if the only (first) argument is a list (collection), it is take as the list of arguments.
	 *
	 */
	public static List<Object> queryForColumnList(int dbType, Connection conn, QueryRunner ds, String sql, Object... args)
			throws Exception
	{
		return queryForT(dbType, columnListHandler, conn, ds, sql, args);
	}

	public static List<Object> queryForColumnList(int dbType, Connection conn, QueryRunner ds, String sql, List args)
			throws Exception
	{
		return queryForT(dbType, columnListHandler, conn, ds, sql, args);
	}

	public static List<Object> queryForColumnList(int dbType, Connection conn, QueryRunner ds, String sql, Collection args)
			throws Exception
	{
		return queryForT(dbType, columnListHandler, conn, ds, sql, args);
	}

	public static List<Object> queryForColumnList(int dbType, Connection conn, QueryRunner ds, String sql)
			throws Exception
	{
		return queryForT(dbType, columnListHandler, conn, ds, sql);
	}
	
	public static List<Object>
	queryTemplateForColumnList(int dbType, Connection conn, QueryRunner ds, String table, String col, Map vm, String suffixQuery, int templateType, int constraintType)
			throws Exception
	{
		return queryTemplateForT(dbType, columnListHandler, conn, ds, table, Collections.singletonList(col), vm, suffixQuery, templateType, constraintType);
	}
	
	public static List<Object>
	queryTemplateForColumnList(int dbType, Connection conn, QueryRunner ds, String table, String col, Map vm, String suffixQuery)
			throws Exception
	{
		return queryTemplateForT(dbType, columnListHandler, conn, ds, table, Collections.singletonList(col), vm, suffixQuery);
	}
	
	public static List<Object>
	queryTemplateForColumnList(int dbType, Connection conn, QueryRunner ds, String table, String col, Map vm)
			throws Exception
	{
		return queryTemplateForT(dbType, columnListHandler, conn, ds, table, Collections.singletonList(col), vm);
	}
	
	/**
	 * executes a query and returns exactly one row (Map)
	 * <p>
	 * if the only (first) argument is a map, the sql string is expected to have "?{field-name}" named parameters
	 * <p>
	 * if the only (first) argument is a list (collection), it is take as the list of arguments.
	 *
	 */
	public static Map<String,Object> queryForMap(int dbType, Connection conn, QueryRunner ds, String sql, Object... args)
			throws Exception
	{
		return queryForT(dbType, mapHandler, conn, ds, sql, args);
	}

	public static Map<String,Object> queryForMap(int dbType, Connection conn, QueryRunner ds, String sql, List args)
			throws Exception
	{
		return queryForT(dbType, mapHandler, conn, ds, sql, args);
	}

	public static Map<String,Object> queryForMap(int dbType, Connection conn, QueryRunner ds, String sql, Collection args)
			throws Exception
	{
		return queryForT(dbType, mapHandler, conn, ds, sql, args);
	}

	public static Map<String,Object> queryForMap(int dbType, Connection conn, QueryRunner ds, String sql)
			throws Exception
	{
		return queryForT(dbType, mapHandler, conn, ds, sql);
	}
	
	public static Map<String,Object>
	queryTemplateForMap(int dbType, Connection conn, QueryRunner ds, String table, Collection cols, Map vm, String suffixQuery, int templateType, int constraintType)
			throws Exception
	{
		return queryTemplateForT(dbType, mapHandler, conn, ds, table, cols, vm, suffixQuery, templateType, constraintType);
	}
	
	public static Map<String,Object>
	queryTemplateForMap(int dbType, Connection conn, QueryRunner ds, String table, Collection cols, Map vm, String suffixQuery)
			throws Exception
	{
		return queryTemplateForT(dbType, mapHandler, conn, ds, table, cols, vm, suffixQuery);
	}
	
	public static Map<String,Object>
	queryTemplateForMap(int dbType, Connection conn, QueryRunner ds, String table, Collection cols, Map vm)
			throws Exception
	{
		return queryTemplateForT(dbType, mapHandler, conn, ds, table, cols, vm);
	}
	
	public static Map<String,Object>
	queryTemplateForMap(int dbType, Connection conn, QueryRunner ds, String table, Map vm)
			throws Exception
	{
		return queryTemplateForT(dbType, mapHandler, conn, ds, table, vm);
	}

	public static Map<String,String> queryForKvMap(int dbType, Connection conn, QueryRunner ds, String sql, Object... args)
			throws Exception
	{
		return queryForT(dbType, kvMapHandler, conn, ds, sql, args);
	}

	public static Map<String,String> queryForKvMap(int dbType, Connection conn, QueryRunner ds, String sql, List args)
			throws Exception
	{
		return queryForT(dbType, kvMapHandler, conn, ds, sql, args);
	}

	public static Map<String,String> queryForKvMap(int dbType, Connection conn, QueryRunner ds, String sql, Collection args)
			throws Exception
	{
		return queryForT(dbType, kvMapHandler, conn, ds, sql, args);
	}

	public static Map<String,String> queryForKvMap(int dbType, Connection conn, QueryRunner ds, String sql)
			throws Exception
	{
		return queryForT(dbType, kvMapHandler, conn, ds, sql);
	}

	public static Map<String, List<String>> queryForKvListMap(int dbType, Connection conn, QueryRunner ds, String sql, Object... args)
			throws Exception
	{
		return queryForT(dbType, kvListMapHandler, conn, ds, sql, args);
	}

	public static Map<String, List<String>> queryForKvListMap(int dbType, Connection conn, QueryRunner ds, String sql, List args)
			throws Exception
	{
		return queryForT(dbType, kvListMapHandler, conn, ds, sql, args);
	}

	public static Map<String, List<String>> queryForKvListMap(int dbType, Connection conn, QueryRunner ds, String sql, Collection args)
			throws Exception
	{
		return queryForT(dbType, kvListMapHandler, conn, ds, sql, args);
	}

	public static Map<String, List<String>> queryForKvListMap(int dbType, Connection conn, QueryRunner ds, String sql)
			throws Exception
	{
		return queryForT(dbType, kvListMapHandler, conn, ds, sql);
	}
	
	
	/**
	 * executes a query and returns exactly one Object
	 * <p>
	 * if the only (first) argument is a map, the sql string is expected to have "?{field-name}" named parameters
	 * <p>
	 * if the only (first) argument is a list (collection), it is take as the list of arguments.
	 *
	 */
	public static Object queryForScalar(int dbType, Connection conn, QueryRunner ds, String sql, Object... args)
			throws Exception
	{
		return queryForT(dbType, scalarHandler, conn, ds, sql, args);
	}

	public static Object queryForScalar(int dbType, Connection conn, QueryRunner ds, String sql, List args)
			throws Exception
	{
		return queryForT(dbType, scalarHandler, conn, ds, sql, args);
	}

	public static Object queryForScalar(int dbType, Connection conn, QueryRunner ds, String sql, Collection args)
			throws Exception
	{
		return queryForT(dbType, scalarHandler, conn, ds, sql, args);
	}

	public static Object queryForScalar(int dbType, Connection conn, QueryRunner ds, String sql)
			throws Exception
	{
		return queryForT(dbType, scalarHandler, conn, ds, sql);
	}
	
	public static Object
	queryTemplateForScalar(int dbType, Connection conn, QueryRunner ds, String table, String col, Map vm, String suffixQuery, int templateType, int constraintType)
			throws Exception
	{
		return queryTemplateForT(dbType, scalarHandler, conn, ds, table, Collections.singletonList(col), vm, suffixQuery, templateType, constraintType);
	}
	
	public static Object
	queryTemplateForScalar(int dbType, Connection conn, QueryRunner ds, String table, String col, Map vm, String suffixQuery)
			throws Exception
	{
		return queryTemplateForT(dbType, scalarHandler, conn, ds, table, Collections.singletonList(col), vm, suffixQuery);
	}
	
	public static Object
	queryTemplateForScalar(int dbType, Connection conn, QueryRunner ds, String table, String col, Map vm)
			throws Exception
	{
		return queryTemplateForT(dbType, scalarHandler, conn, ds, table, Collections.singletonList(col), vm);
	}
	
	/**
	 * executes a statment and returns the number of rows effected.
	 * <p>
	 * if the only (first) argument is a map, the sql string is expected to have "?{field-name}" named parameters
	 * <p>
	 * if the only (first) argument is a list (collection), it is take as the list of arguments.
	 *
	 */
	public static int update(int dbType, Connection conn, QueryRunner ds, String sql, List args)
			throws Exception
	{
		return update(dbType, conn, ds, sql, args.toArray());
	}

	public static int update(int dbType, Connection conn, QueryRunner ds, String sql, Collection args)
			throws Exception
	{
		return update(dbType, conn, ds, sql, args.toArray());
	}

	public static int update(int dbType, Connection conn, QueryRunner ds, String sql, Map args)
			throws Exception
	{
		return update(dbType, conn, ds, sql, (Object)args);
	}

	public static int update(int dbType, Connection conn, QueryRunner ds, String sql, Object... args)
			throws Exception
	{
		if(args == null)
		{
			if(conn==null)
			{
				return ds.update(sql);
			}
			else
			{
				return ds.update(conn, sql);
			}
		}
		else
		if(args.length >0 && args[0] instanceof Map)
		{
			List nArgs = new Vector();
			sql = JdaoUtils.preparseParameters(dbType, sql, nArgs, (Map) args[0]);
			if(conn==null)
			{
				return ds.update(sql, nArgs.toArray());
			}
			else
			{
				return ds.update(conn, sql, nArgs.toArray());
			}
		}
		else
		if(args.length >0 && args[0] instanceof Collection)
		{
			if(conn==null)
			{
				return ds.update(sql, ((Collection)args[0]).toArray());
			}
			else
			{
				return ds.update(conn, sql, ((Collection)args[0]).toArray());
			}
		}
		else
		{
			if(conn==null)
			{
				return ds.update(sql, args);
			}
			else
			{
				return ds.update(conn, sql, args);
			}
		}
	}
	
	/**
	 * executes a statment and returns the number of rows effected.
	 * <p>
	 * if the only (first) argument is a map, the sql string is expected to have "?{field-name}" named parameters
	 * <p>
	 * if the only (first) argument is a list (collection), it is take as the list of arguments.
	 *
	 */
	public static int execute(int dbType, Connection conn, QueryRunner ds, String sql, Object... args)
			throws Exception
	{
		return update(dbType, conn, ds, sql, args);
	}

	public static int execute(int dbType, Connection conn, QueryRunner ds, String sql, List args)
			throws Exception
	{
		return update(dbType, conn, ds, sql, args);
	}

	public static int execute(int dbType, Connection conn, QueryRunner ds, String sql, Collection args)
			throws Exception
	{
		return update(dbType, conn, ds, sql, args);
	}

	public static int execute(int dbType, Connection conn, QueryRunner ds, String sql, Map args)
			throws Exception
	{
		return update(dbType, conn, ds, sql, args);
	}

	public static int execute(int dbType, Connection conn, QueryRunner ds, String sql)
			throws Exception
	{
		return update(dbType, conn, ds, sql);
	}
	
	/**
	 * executes an insert and returns the number of rows effected.
	 *
	 */
	public static int insert(int dbType, Connection conn, QueryRunner ds, String table, Map cols)
			throws Exception
	{
		return insert(dbType, conn, ds, table, cols, false);
	}
	
	public static int insert(int dbType, Connection conn, QueryRunner ds, String table, Map cols, boolean onDuplicateKeyUpdate)
			throws Exception
	{
		return insert(dbType, conn, ds, table, cols, onDuplicateKeyUpdate, cols.keySet());
	}
	
	public static int insert(int dbType, Connection conn, QueryRunner ds, String table, Map cols, boolean onDuplicateKeyUpdate, Collection updateFields)
			throws Exception
	{
		Vector parm = new Vector();

		StringBuilder qq=new StringBuilder();
		qq.append("INSERT INTO "+table+" ( ");

		boolean op = true;
		for(Object kv : cols.entrySet())
		{
			Object _val = ((Map.Entry) kv).getValue();
			parm.add(_val);
			if(!op)
			{
				qq.append(",");
			}
			qq.append(((Map.Entry)kv).getKey());
			op=false;
		}
		qq.append(" ) VALUES (");

		op = true;
		for(Object v : parm)
		{
			if(!op)
			{
				qq.append(",");
			}
			qq.append("?");
			op=false;
		}
		qq.append(" ) ");

		if(onDuplicateKeyUpdate)
		{
			switch(dbType)
			{
				case JDAO.DB_TYPE_POSTGRES:
				case JDAO.DB_TYPE_SQLITE:
				{
					qq.append(" ON CONFLICT ( ");
					boolean ff = true;
					for(Object k : cols.keySet())
					{
						if(!updateFields.contains(k))
						{
							if(!ff) qq.append(",");
							qq.append(k.toString());
							ff=false;
						}
					}
					qq.append(" ) ");
					Map um =new HashMap();
					for(Object o : updateFields)
					{
						um.put(o, cols.get(o));
					}
					String setuqq = JdaoUtils.buildSet(dbType, parm, um);
					qq.append(" DO UPDATE SET ");
					qq.append(setuqq);
					break;
				}
				case JDAO.DB_TYPE_MYSQL:
				case JDAO.DB_TYPE_CRATE:
				{
					Map um =new HashMap();
					for(Object o : updateFields)
					{
						um.put(o, cols.get(o));
					}
					String setuqq = JdaoUtils.buildSet(dbType, parm, um);
					qq.append(" ON DUPLICATE KEY UPDATE ");
					qq.append(setuqq);
					break;
				}
				default:
					throw new IllegalArgumentException("DB TYPE NOT UPSERT-ABLE");
			}
		}

		if(conn==null)
		{
			return ds.update(qq.toString(), parm.toArray());
		}
		return ds.update(conn, qq.toString(), parm.toArray());
	}
	
	public void insertList(String table, List<Map> colList) throws Exception
	{
		this.checkReadOnly();
		this.insertList(table, colList, null);
	}
	
	public void insertList(String table, List<Map> colList, String pkField) throws Exception
	{
		this.checkReadOnly();
		if(pkField==null)
		{
			JDAO.insertList(this.dbType, this.conn, this.queryRunner, table, colList, false, null);
		}
		else
		{
			for(Map row : colList)
			{
				Collection updateFields = row.keySet();
				updateFields.remove(pkField);
				JDAO.insert(this.dbType, this.conn, this.queryRunner, table, row, true, updateFields);
			}
		}
	}
	
	public static void insertList(int dbType, Connection conn, QueryRunner ds, String table, List<Map> colList, boolean onDuplicateKeyUpdate, Collection updateFields) throws Exception
	{
		for(Map row : colList)
		{
			JDAO.insert(dbType, conn, ds, table, row, onDuplicateKeyUpdate, updateFields);
		}
	}
	
	public <T> List<T> insertListWithPK(String table, List<Map> colList, Class<T> clazz) throws Exception
	{
		this.checkReadOnly();
		return this.insertListWithPK(table, colList, null, clazz);
	}
	
	public <T> List<T> insertListWithPK(String table, List<Map> colList, String pkField, Class<T> clazz) throws Exception
	{
		this.checkReadOnly();
		if(pkField==null)
		{
			return this.insertListWithPK(table, colList, false, null, clazz);
		}
		else
		{
			ArrayList<T> res = new ArrayList<T>();
			for(Map row : colList)
			{
				Collection updateFields = row.keySet();
				updateFields.remove(pkField);
				res.add(JDAO.insertWithPK(this.dbType, this.conn, this.queryRunner, table, row, true, updateFields, clazz));
			}
			return res;
		}
	}
	
	public <T> List<T> insertListWithPK(String table, List<Map> colList, boolean onDuplicateKeyUpdate, Collection updateFields, Class<T> clazz) throws Exception
	{
		this.checkReadOnly();
		return JDAO.insertListWithPK(this.dbType, this.conn, this.queryRunner, table, colList, onDuplicateKeyUpdate, updateFields, clazz);
	}
	
	public static <T> List<T> insertListWithPK(int dbType, Connection conn, QueryRunner ds, String table, List<Map> colList, boolean onDuplicateKeyUpdate, Collection updateFields, Class<T> clazz) throws Exception
	{
		ArrayList<T> res = new ArrayList<T>();
		for(Map row : colList)
		{
			res.add(JDAO.insertWithPK(dbType, conn, ds, table, row, onDuplicateKeyUpdate, updateFields, clazz));
		}
		return res;
	}
	
	public static <T> T insertWithPK(int dbType, Connection conn, QueryRunner ds, String table, Map cols, Class<T> clazz)
			throws Exception
	{
		return insertWithPK(dbType, conn, ds, table, cols, false, clazz);
	}
	
	public static <T> T insertWithPK(int dbType, Connection conn, QueryRunner ds, String table, Map cols, boolean onDuplicateKeyUpdate, Class<T> clazz)
			throws Exception
	{
		return insertWithPK(dbType, conn, ds, table, cols, onDuplicateKeyUpdate, cols.keySet(), clazz);
	}

	public static <T> T insertWithPK(int dbType, Connection conn, QueryRunner ds, String table, Map cols, boolean onDuplicateKeyUpdate, Collection updateFields, Class<T> clazz)
			throws Exception
	{
		Vector parm = new Vector();
		StringBuilder qq=new StringBuilder();
		qq.append("INSERT INTO "+table+" ( ");
		boolean op = true;
		for(Object kv : cols.entrySet())
		{
			parm.add(((Map.Entry)kv).getValue());
			if(!op)
			{
				qq.append(",");
			}
			qq.append(((Map.Entry)kv).getKey());
			op=false;
		}
		qq.append(" ) VALUES (");
		op = true;
		for(Object v : parm)
		{
			if(!op)
			{
				qq.append(",");
			}
			qq.append("?");
			op=false;
		}
		qq.append(" ) ");

		if(onDuplicateKeyUpdate && ((dbType == JDAO.DB_TYPE_MYSQL) || (dbType == JDAO.DB_TYPE_CRATE)))
		{
			Map um =new HashMap();
			for(Object o : updateFields)
			{
				um.put(o, cols.get(o));
			}
			String setuqq = JdaoUtils.buildSet(dbType, parm, um);
			qq.append(" ON DUPLICATE KEY UPDATE ");
			qq.append(setuqq);
		}
		else
		if(onDuplicateKeyUpdate && (dbType == JDAO.DB_TYPE_SQLITE))
		{
			Map um =new HashMap();
			for(Object o : updateFields)
			{
				um.put(o, cols.get(o));
			}
			String setuqq = JdaoUtils.buildSet(dbType, parm, um);
			qq.append(" ON CONFLICT("+ CommonUtil.join(cols.keySet(), ",") +") DO UPDATE SET ");
			qq.append(setuqq);
		}
		else
		{
			throw new IllegalArgumentException("DB TYPE NO UPSERT SUPPORT");
		}

		if(conn==null)
		{
			return ds.insert(qq.toString(), new ScalarHandler<T>(), parm.toArray());
		}
		return ds.insert(conn, qq.toString(), new ScalarHandler<T>(), parm.toArray());
	}

	public void insertKvMap(String table, String kvSF, String kvKF, String kvVF, String scopeId, Map<String,String> kvMap, boolean onDuplicateKeyUpdate) throws Exception
	{
		JDAO.insertKvMap(this.dbType, this.conn, this.queryRunner, table,kvSF, kvKF, kvVF, scopeId, kvMap, onDuplicateKeyUpdate);
	}
	
	public static void insertKvMap(int dbType, Connection conn, QueryRunner ds, String table, String kvSF, String kvKF, String kvVF, String scopeId, Map<String,String> kvMap, boolean onDuplicateKeyUpdate)
			throws Exception
	{
		for(Map.Entry<String, String> entry : kvMap.entrySet())
		{
			JDAO.insert(dbType, conn, ds, table, JDAO.toMap(kvSF, scopeId, kvKF, entry.getKey(), kvVF, entry.getValue()), onDuplicateKeyUpdate, JDAO.toCollection(kvKF, kvVF));
		}
	}

	public List<String> queryFieldList(String schemaName, String tableName) throws Exception
	{
		return queryFieldList(this.dbType, this.conn, this.queryRunner, schemaName, tableName);
	}

	public static List<String> queryFieldList(int dbType, Connection conn, QueryRunner ds, String schemaName, String tableName) throws Exception
	{
		switch(dbType)
		{
			case DB_TYPE_POSTGRES:
			case DB_TYPE_ORACLE:
			case DB_TYPE_MSSQL:
			case DB_TYPE_SYBASE:
			case DB_TYPE_DB2:
			case DB_TYPE_H2:
			case DB_TYPE_SQLITE:
			case DB_TYPE_CRATE:
			{
				return (List)queryForColumnList(dbType, conn, ds, "SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_NAME=? AND TABLE_SCHEMA=? ", tableName, schemaName);
			}
			case DB_TYPE_ANSI:
			case DB_TYPE_MYSQL:
			default:
			{
				return (List)queryForColumnList(dbType, conn, ds, "SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_NAME=? AND TABLE_SCHEMA=? ORDER BY ORDINAL_POSITION", tableName, schemaName);
			}
		}
	}

	QueryRunner queryRunner = null;
	Connection conn = null;
	
	public int getDbType()
	{
		return dbType;
	}
	
	public void setDbType(int dbType)
	{
		this.dbType = dbType;
	}
	
	int dbType = 0;
	
	public static final int DB_TYPE_ANSI = 0;
	public static final int DB_TYPE_MYSQL = 1;
	public static final int DB_TYPE_ORACLE = 2;
	public static final int DB_TYPE_POSTGRES = 3;
	public static final int DB_TYPE_MSSQL = 4;
	public static final int DB_TYPE_SYBASE = 5;
	public static final int DB_TYPE_DB2 = 6;
	public static final int DB_TYPE_H2 = 7;
	public static final int DB_TYPE_SQLITE = 8;
	public static final int DB_TYPE_CRATE = 9;
	
	public JDAO() { }
	
	public JDAO(QueryRunner queryRunner)
	{
		this();
		this.queryRunner = queryRunner;
	}
	
	public JDAO(Connection conn, QueryRunner queryRunner)
	{
		this(queryRunner);
		this.conn = conn;
	}

	public  List<Map<String,Object>>
	queryForList(String sql, Object... args)
			throws Exception
	{
		return JDAO.queryForList(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  List<Map<String,Object>>
	queryForList(String sql, List args)
			throws Exception
	{
		return JDAO.queryForList(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  List<Map<String,Object>>
	queryForList(String sql, Collection args)
			throws Exception
	{
		return JDAO.queryForList(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  List<Map<String,Object>>
	queryForList(String sql)
			throws Exception
	{
		return JDAO.queryForList(this.dbType, this.conn, this.queryRunner, sql);
	}

	public  List<Map<String,Object>>
	queryForMapList(String sql, Object... args)
			throws Exception
	{
		return JDAO.queryForMapList(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  List<Map<String,Object>>
	queryForMapList(String sql, List args)
			throws Exception
	{
		return JDAO.queryForMapList(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  List<Map<String,Object>>
	queryForMapList(String sql, Collection args)
			throws Exception
	{
		return JDAO.queryForMapList(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  List<Map<String,Object>>
	queryForMapList(String sql)
			throws Exception
	{
		return JDAO.queryForMapList(this.dbType, this.conn, this.queryRunner, sql);
	}
	
	public List<Map<String,Object>>
	queryTemplateForMapList(String table, Map<String,Object> vm, Collection fieldList, String suffixQuery)
			throws Exception
	{
		return JDAO.queryTemplateForMapList(this.dbType, this.conn, this.queryRunner,  table, fieldList, vm, suffixQuery);
	}
	
	public List<Map<String,Object>>
	queryTemplateForMapList(String table, Map<String,Object> vm, Collection fieldList)
			throws Exception
	{
		return JDAO.queryTemplateForMapList(this.dbType, this.conn, this.queryRunner,  table, fieldList, vm);
	}
	
	public List<Map<String,Object>>
	queryTemplateForMapList(String table, Map<String,Object> vm)
			throws Exception
	{
		return JDAO.queryTemplateForMapList(this.dbType, this.conn, this.queryRunner,  table, vm);
	}

	public  List<Object[]> queryForArrayList(String sql, Object... args)
			throws Exception
	{
		return JDAO.queryForArrayList(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  List<Object[]> queryForArrayList(String sql, List args)
			throws Exception
	{
		return JDAO.queryForArrayList(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  List<Object[]> queryForArrayList(String sql, Collection args)
			throws Exception
	{
		return JDAO.queryForArrayList(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  List<Object[]> queryForArrayList(String sql)
			throws Exception
	{
		return JDAO.queryForArrayList(this.dbType, this.conn, this.queryRunner, sql);
	}
	
	public List<Object[]>
	queryTemplateForArrayList(String table, List<String> cols, Map<String,Object> vm, String suffixQuery)
			throws Exception
	{
		return JDAO.queryTemplateForArrayList(this.dbType, this.conn, this.queryRunner, table, cols, vm, suffixQuery);
	}
	
	public List<Object[]>
	queryTemplateForArrayList(String table, List<String> cols, Map<String,Object> vm)
			throws Exception
	{
		return JDAO.queryTemplateForArrayList(this.dbType, this.conn, this.queryRunner, table, cols, vm);
	}

	public  List<Object> queryForColumnList(String sql, Object... args)
			throws Exception
	{
		return JDAO.queryForColumnList(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  List<Object> queryForColumnList(String sql, List args)
			throws Exception
	{
		return JDAO.queryForColumnList(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  List<Object> queryForColumnList(String sql, Collection args)
			throws Exception
	{
		return JDAO.queryForColumnList(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  List<Object> queryForColumnList(String sql)
			throws Exception
	{
		return JDAO.queryForColumnList(this.dbType, this.conn, this.queryRunner, sql);
	}
	
	public List<Object>
	queryTemplateForColumnList(String table, String col, Map<String,Object> vm, String suffixQuery, int templateType, int constraintType)
			throws Exception
	{
		List param = new Vector();
		return JDAO.queryTemplateForColumnList(this.dbType, this.conn, this.queryRunner,  table, col, vm, suffixQuery, templateType, constraintType);
	}
	
	public List<Object>
	queryTemplateForColumnList(String table, String col, Map<String,Object> vm, String suffixQuery)
			throws Exception
	{
		List param = new Vector();
		return JDAO.queryTemplateForColumnList(this.dbType, this.conn, this.queryRunner,  table, col, vm, suffixQuery);
	}
	
	public List<Object>
	queryTemplateForColumnList(String table, String col, Map<String,Object> vm)
			throws Exception
	{
		List param = new Vector();
		return JDAO.queryTemplateForColumnList(this.dbType, this.conn, this.queryRunner,  table, col, vm);
	}

	public  Map<String,Object> queryForMap(String sql, Object... args)
			throws Exception
	{
		return JDAO.queryForMap(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  Map<String,Object> queryForMap(String sql, List args)
			throws Exception
	{
		return JDAO.queryForMap(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  Map<String,Object> queryForMap(String sql, Collection args)
			throws Exception
	{
		return JDAO.queryForMap(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  Map<String,Object> queryForMap(String sql)
			throws Exception
	{
		return JDAO.queryForMap(this.dbType, this.conn, this.queryRunner, sql);
	}
	
	public  Map<String,Object> queryTemplateForMap(String table, Map<String,Object> vm, List<String> fieldList, String suffixQuery, int templateType, int constraintType)
			throws Exception
	{
		return JDAO.queryTemplateForMap(this.dbType, this.conn, this.queryRunner,  table, fieldList, vm, suffixQuery, templateType, constraintType);
	}
	
	public  Map<String,Object> queryTemplateForMap(String table, Map<String,Object> vm, List<String> fieldList, String suffixQuery)
			throws Exception
	{
		return JDAO.queryTemplateForMap(this.dbType, this.conn, this.queryRunner,  table, fieldList, vm, suffixQuery);
	}
	
	public  Map<String,Object> queryTemplateForMap(String table, Map<String,Object> vm, List<String> fieldList)
			throws Exception
	{
		return JDAO.queryTemplateForMap(this.dbType, this.conn, this.queryRunner,  table, fieldList, vm);
	}
	
	public  Map<String,Object> queryTemplateForMap(String table, Map<String,Object> vm)
			throws Exception
	{
		return JDAO.queryTemplateForMap(this.dbType, this.conn, this.queryRunner,  table, vm);
	}

	public  Map<String,String> queryForKvMap(String sql, Object... args)
			throws Exception
	{
		return JDAO.queryForKvMap(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  Map<String,String> queryForKvMap(String sql, List args)
			throws Exception
	{
		return JDAO.queryForKvMap(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  Map<String,String> queryForKvMap(String sql, Collection args)
			throws Exception
	{
		return JDAO.queryForKvMap(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  Map<String,String> queryForKvMap(String sql)
			throws Exception
	{
		return JDAO.queryForKvMap(this.dbType, this.conn, this.queryRunner, sql);
	}
	
	public  Map<String,String>
	queryTemplateForKvMap(String table, String c1, String c2, Map<String,Object> vm, String suffixQuery, int templateType, int constraintType)
			throws Exception
	{
		List param = new Vector();
		return JDAO.queryForKvMap(this.dbType, this.conn, this.queryRunner,  "SELECT "+c1+","+c2+" FROM "+table+" WHERE "+JdaoUtils.buildWhere(dbType, templateType, constraintType, param, vm)+(suffixQuery==null?"":" "+suffixQuery), param);
	}
	
	public  Map<String,String>
	queryTemplateForKvMap(String table, String c1, String c2, Map<String,Object> vm, String suffixQuery)
			throws Exception
	{
		return queryTemplateForKvMap(table, c1, c2, vm, suffixQuery, JdaoUtils.TEMPLATE_TYPE_AUTO, JdaoUtils.CONSTRAINT_ALL_OF);
	}
	
	public  Map<String,String>
	queryTemplateForKvMap(String table, String c1, String c2, Map<String,Object> vm) throws Exception
	{
		return queryTemplateForKvMap(table, c1, c2, vm, null, JdaoUtils.TEMPLATE_TYPE_AUTO, JdaoUtils.CONSTRAINT_ALL_OF);
	}

	public  Map<String, List<String>> queryForKvListMap(String sql, Object... args)
			throws Exception
	{
		return JDAO.queryForKvListMap(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  Map<String, List<String>> queryForKvListMap(String sql, List args)
			throws Exception
	{
		return JDAO.queryForKvListMap(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  Map<String, List<String>> queryForKvListMap(String sql, Collection args)
			throws Exception
	{
		return JDAO.queryForKvListMap(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  Map<String, List<String>> queryForKvListMap(String sql)
			throws Exception
	{
		return JDAO.queryForKvListMap(this.dbType, this.conn, this.queryRunner, sql);
	}
	
	public Map<String,String>
	queryScopeForKvMap(String table, String kvSF, String kvKF, String kvVF, String scopeId) throws Exception
	{
		return this.queryTemplateForKvMap(table, kvKF, kvVF, JDAO.toMap(kvSF, scopeId), null, JdaoUtils.TEMPLATE_TYPE_EQUAL, JdaoUtils.CONSTRAINT_ALL_OF);
	}
	
	public Map<String, List<String>>
	queryScopeForKvListMap(String table, String kvSF, String kvKF, String kvVF, String scopeId) throws Exception
	{
		return this.queryTemplateForKvListMap(table, kvKF, kvVF, JDAO.toMap(kvSF, scopeId), null, JdaoUtils.TEMPLATE_TYPE_EQUAL, JdaoUtils.CONSTRAINT_ALL_OF);
	}
	
	public  Map<String, List<String>>
	queryTemplateForKvListMap(String table, String c1, String c2, Map<String,Object> vm, String suffixQuery, int templateType, int constraintType)
			throws Exception
	{
		List param = new Vector();
		return JDAO.queryForKvListMap(this.dbType, this.conn, this.queryRunner,  "SELECT "+c1+","+c2+" FROM "+table+" WHERE "+JdaoUtils.buildWhere(dbType, templateType, constraintType, param, vm)+(suffixQuery==null?"":" "+suffixQuery), param);
	}
	
	public  Map<String, List<String>>
	queryTemplateForKvListMap(String table, String c1, String c2, Map<String,Object> vm, String suffixQuery)
			throws Exception
	{
		return queryTemplateForKvListMap(table, c1, c2, vm, suffixQuery, JdaoUtils.TEMPLATE_TYPE_AUTO, JdaoUtils.CONSTRAINT_ALL_OF);
	}
	
	public  Map<String, List<String>>
	queryTemplateForKvListMap(String table, String c1, String c2, Map<String,Object> vm)
			throws Exception
	{
		return queryTemplateForKvListMap(table, c1, c2, vm, null, JdaoUtils.TEMPLATE_TYPE_AUTO, JdaoUtils.CONSTRAINT_ALL_OF);
	}

	public  Object queryForScalar(String sql, Object... args)
			throws Exception
	{
		return JDAO.queryForScalar(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  Object queryForScalar(String sql, Collection args)
			throws Exception
	{
		return JDAO.queryForScalar(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  Object queryForScalar(String sql, List<Object> args)
			throws Exception
	{
		return JDAO.queryForScalar(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  Object queryForScalar(String sql)
			throws Exception
	{
		return JDAO.queryForScalar(this.dbType, this.conn, this.queryRunner, sql);
	}
	
	public  Object
	queryTemplateForScalar(String table, String col, Map<String,Object> vm, String suffixQuery, int templateType, int constraintType)
			throws Exception
	{
		return JDAO.queryTemplateForScalar(this.dbType, this.conn, this.queryRunner,  table, col, vm, suffixQuery, templateType, constraintType);
	}
	
	public  Object
	queryTemplateForScalar(String table, String col, Map<String,Object> vm, String suffixQuery)
			throws Exception
	{
		return JDAO.queryTemplateForScalar(this.dbType, this.conn, this.queryRunner,  table, col, vm, suffixQuery);
	}
	
	public  Object
	queryTemplateForScalar(String table, String col, Map<String,Object> vm)
			throws Exception
	{
		return JDAO.queryTemplateForScalar(this.dbType, this.conn, this.queryRunner,  table, col, vm);
	}

	public  int update(String sql, Object... args)
			throws Exception
	{
		this.checkReadOnly();
		return JDAO.update(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  int update(String sql, List args)
			throws Exception
	{
		this.checkReadOnly();
		return JDAO.update(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  int update(String sql, Collection args)
			throws Exception
	{
		this.checkReadOnly();
		return JDAO.update(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  int update(String sql)
			throws Exception
	{
		this.checkReadOnly();
		return JDAO.update(this.dbType, this.conn, this.queryRunner, sql);
	}

	public  int execute(String sql, Object... args)
			throws Exception
	{
		return JDAO.execute(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  int execute(String sql, List args)
			throws Exception
	{
		return JDAO.execute(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  int execute(String sql, Collection args)
			throws Exception
	{
		return JDAO.execute(this.dbType, this.conn, this.queryRunner, sql, args);
	}

	public  int execute(String sql)
			throws Exception
	{
		return JDAO.execute(this.dbType, this.conn, this.queryRunner, sql);
	}
	
	public  int insert(String table, Map cols)
			throws Exception
	{
		this.checkReadOnly();
		return JDAO.insert(this.dbType, this.conn, this.queryRunner, table, cols);
	}
	
	public  int insert(String table, Map cols, boolean onDuplicateKeyUpdate)
			throws Exception
	{
		this.checkReadOnly();
		return JDAO.insert(this.dbType, this.conn, this.queryRunner, table, cols, onDuplicateKeyUpdate);
	}
	
	public  int insert(String table, Map cols, boolean onDuplicateKeyUpdate, Collection updateCols)
			throws Exception
	{
		this.checkReadOnly();
		return JDAO.insert(this.dbType, this.conn, this.queryRunner, table, cols, onDuplicateKeyUpdate, updateCols);
	}
	
	public void setAutoCommit(boolean ac)
	{
		try
		{
			if(conn!=null)
			{
				conn.setAutoCommit(ac);
			}
		}
		catch(Exception xe) {}
	}
	
	public void beginTransaction()
	{
		try
		{
			if(conn!=null)
			{
				conn.setAutoCommit(false);
			}
		}
		catch(Exception xe) {}
	}
	
	public void rollbackTransaction()
	{
		try
		{
			if(conn!=null)
			{
				conn.rollback();
			}
		}
		catch(Exception xe) {}
	}
	
	public void commitTransaction()
	{
		try
		{
			if(conn!=null)
			{
				conn.commit();
			}
		}
		catch(Exception xe) {}
	}
	
	public void close()
	{
		try
		{
			if(conn!=null)
			{
				conn.close();
			}
		}
		catch(Exception xe) {}
		finally
		{
			conn=null;
			queryRunner=null;
		}
	}
	
	public Connection getConnection()
	{
		return this.conn;
	}
	
	public QueryRunner getQueryRunner()
	{
		return this.queryRunner;
	}
}
