package com.github.terefang.jmelange.dao2.util;

import com.github.terefang.jmelange.commons.util.StringUtil;
import com.github.terefang.jmelange.dao2.DAO;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class DaoUtil
{
    
    public static String join(final char separator, List c)
    {
        return join(Character.toString(separator), c);
    }

    public static String join(final char separator, String... c)
    {
        return join(Character.toString(separator), Arrays.asList(c));
    }
    public static String join(final String separator, List c)
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

    public static Map<String,Object> toMap(Object... values)
    {
        Map<String,Object> ret = new HashMap();
        for(int i=0; i<values.length; i+=2)
        {
            ret.put(String.valueOf(values[i]), values[i+1]);
        }
        return ret;
    }

    public static Map<String, String> toMap(String... values)
    {
        Map<String, String> ret = new HashMap();
        for(int i=0; i<values.length; i+=2)
        {
            ret.put(values[i], values[i+1]);
        }
        return ret;
    }

    public static List<Object> toList(Object... values)
    {
        ArrayList ret = new ArrayList(values.length);
        for(int i=0; i<values.length; i++)
        {
            ret.add(values[i]);
        }
        return ret;
    }

    public static List<String> toList(String... values)
    {
        ArrayList<String> ret = new ArrayList<String>(values.length);
        for(int i=0; i<values.length; i++)
        {
            ret.add(values[i]);
        }
        return ret;
    }

    @SneakyThrows
    public static DataSource createDataSource(final String _jdbcDriver, final String _url, final String _user, final String _pass)
    {
        return createDataSource(_jdbcDriver, _url, _user, _pass, null);
    }

    @SneakyThrows
    public static DataSource createDataSource(final String _jdbcDriver, final String _url, final String _user, final String _pass, final Properties _infos) {
        return new DataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                return createConnection(_jdbcDriver, _url, _user, _pass, _infos);
            }

            @Override
            public Connection getConnection(String username, String password) throws SQLException {
                return createConnection(_jdbcDriver, _url, username, password, _infos);
            }

            @Override
            public PrintWriter getLogWriter() throws SQLException {
                return null;
            }

            @Override
            public void setLogWriter(PrintWriter out) throws SQLException {

            }

            @Override
            public void setLoginTimeout(int seconds) throws SQLException {

            }

            @Override
            public int getLoginTimeout() throws SQLException {
                return 0;
            }

            @Override
            public <T> T unwrap(Class<T> iface) throws SQLException {
                return (T) this;
            }

            @Override
            public boolean isWrapperFor(Class<?> iface) throws SQLException {
                return false;
            }

            @Override
            public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                return null;
            }
        };
    }

    @SneakyThrows
    public static Connection createConnection(String _jdbcDriver, String _url, String _user, String _pass)
    {
        return createConnection(_jdbcDriver, _url, _user, _pass, null);
    }

    @SneakyThrows
    public static Connection createConnection(String _jdbcDriver, String _url, String _user, String _pass, Properties _infos) {
        Properties _info = new Properties();
        if(_infos!=null)
        {
            _info.putAll(_infos);
        }
        if (_user != null) _info.put("user", _user);
        if (_pass != null) _info.put("password", _pass);

        if (_jdbcDriver != null && !_jdbcDriver.trim().equalsIgnoreCase("")) {
            Class<?> _clazz = Class.forName(_jdbcDriver, true, Thread.currentThread().getContextClassLoader());
            Driver _driver = (Driver) _clazz.newInstance();
            return _driver.connect(_url, _info);
        }
        return DriverManager.getConnection(_url, _info);
    }

    @SneakyThrows
    public static DAO daoFromJdbc(boolean _connectionOrDataSource, String jdbcDriver, String _url, String _user, String _pass)
    {
        if(_connectionOrDataSource)
        {
            return daoFromConnection(createConnection(jdbcDriver, _url, _user, _pass));
        }
        return daoFromDataSource(createDataSource(jdbcDriver, _url, _user, _pass));
    }

    @SneakyThrows
    public static DAO daoFromJdbc(boolean _connectionOrDataSource, String jdbcDriver, String _url, String _user, String _pass, Properties _infos)
    {
        if(_connectionOrDataSource)
        {
            return daoFromConnection(createConnection(jdbcDriver, _url, _user, _pass, _infos));
        }
        return daoFromDataSource(createDataSource(jdbcDriver, _url, _user, _pass, _infos));
    }

    @SneakyThrows
    public static DAO daoFromDataSource(DataSource _ds) {
        DAO _dao = new DAO();
        _dao.setDataSource(_ds);
        return _dao;
    }

    @SneakyThrows
    public static DAO daoFromConnection(Connection _conn) {
        DAO _dao = new DAO();
        _dao.setConnection(_conn);
        return _dao;
    }

    public static DAO oracleDao(boolean _cds, String _hostPortDb, String _user, String _pass, Properties _infos) {
        DAO _dao = daoFromJdbc(_cds, "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@" + _hostPortDb, _user, _pass, _infos);
        _dao.setDbType(DAO.DbType.DB_TYPE_ORACLE);
        return _dao;
    }

    public static DAO oracleDao(boolean _cds, String _hostPortDb, String _user, String _pass) {
        return oracleDao(_cds, _hostPortDb, _user, _pass, null);
    }

    public static DAO oracleDao(String _hostPortDb, String _user, String _pass, Properties _infos)
    {
        return oracleDao(true, _hostPortDb, _user, _pass, _infos);
    }

    public static DAO oracleDao(String _hostPortDb, String _user, String _pass)
    {
        return oracleDao(true, _hostPortDb, _user, _pass, null);
    }

    public static DAO mariadbDao(boolean _cds, String _hostPortDb, String _user, String _pass, Properties _infos) {
        DAO _dao = daoFromJdbc(_cds, "org.mariadb.jdbc.Driver", "jdbc:mariadb://" + _hostPortDb, _user, _pass, _infos);
        _dao.setDbType(DAO.DbType.DB_TYPE_MYSQL);
        return _dao;
    }

    public static DAO mariadbDao(String _hostPortDb, String _user, String _pass, Properties _infos)
    {
        return mariadbDao(true, _hostPortDb, _user, _pass, _infos);
    }

    public static DAO mariadbDao(boolean _cds, String _hostPortDb, String _user, String _pass) {
        return mariadbDao(_cds, _hostPortDb, _user, _pass, null);
    }

    public static DAO mariadbDao(String _hostPortDb, String _user, String _pass)
    {
        return mariadbDao(true, _hostPortDb, _user, _pass, null);
    }

    public static DAO mysqlDao(boolean _cds, String _hostPortDb, String _user, String _pass, Properties _infos) {
        DAO _dao = daoFromJdbc(_cds, "com.mysql.cj.jdbc.Driver", "jdbc:mysql://" + _hostPortDb, _user, _pass, _infos);
        _dao.setDbType(DAO.DbType.DB_TYPE_MYSQL);
        return _dao;
    }

    public static DAO mysqlDao(boolean _cds, String _hostPortDb, String _user, String _pass) {
        return mysqlDao(_cds, _hostPortDb, _user, _pass, null);
    }

    public static DAO mysqlDao(String _hostPortDb, String _user, String _pass, Properties _infos)
    {
        return mysqlDao(true, _hostPortDb, _user, _pass, _infos);
    }

    public static DAO mysqlDao(String _hostPortDb, String _user, String _pass)
    {
        return mysqlDao(true, _hostPortDb, _user, _pass, null);
    }

    public static DAO mysql5Dao(boolean _cds, String _hostPortDb, String _user, String _pass, Properties _infos) {
        DAO _dao = daoFromJdbc(_cds, "com.mysql.jdbc.Driver", "jdbc:mysql://" + _hostPortDb, _user, _pass, _infos);
        _dao.setDbType(DAO.DbType.DB_TYPE_MYSQL);
        return _dao;
    }

    public static DAO mysql5Dao(boolean _cds, String _hostPortDb, String _user, String _pass) {
        return mysql5Dao(_cds, _hostPortDb, _user, _pass, null);
    }

    public static DAO mysql5Dao(String _hostPortDb, String _user, String _pass, Properties _infos)
    {
        return mysql5Dao(true, _hostPortDb, _user, _pass, _infos);
    }

    public static DAO mysql5Dao(String _hostPortDb, String _user, String _pass)
    {
        return mysql5Dao(true, _hostPortDb, _user, _pass, null);
    }

    public static DAO sqliteDao(boolean _cds, String _hostPortDb, String _user, String _pass, Properties _infos) {
        DAO _dao = daoFromJdbc(_cds, "", "jdbc:sqlite:" + _hostPortDb, _user, _pass, _infos);
        _dao.setDbType(DAO.DbType.DB_TYPE_SQLITE);
        return _dao;
    }

    public static DAO sqliteDao(boolean _cds, String _hostPortDb, String _user, String _pass) {
        return sqliteDao(_cds, _hostPortDb, _user, _pass, null);
    }

    public static DAO sqliteDao(String _hostPortDb, String _user, String _pass, Properties _infos)
    {
        return sqliteDao(true, _hostPortDb, _user, _pass, _infos);
    }

    public static DAO sqliteDao(String _hostPortDb, String _user, String _pass)
    {
        return sqliteDao(true, _hostPortDb, _user, _pass, null);
    }

    public static DAO pgsqlDao(boolean _cds, String _hostPortDb, String _user, String _pass, Properties _infos) {
        DAO _dao = daoFromJdbc(_cds, "org.postgresql.Driver", "jdbc:postgresql://" + _hostPortDb, _user, _pass, _infos);
        _dao.setDbType(DAO.DbType.DB_TYPE_POSTGRES);
        return _dao;
    }

    public static DAO pgsqlDao(boolean _cds, String _hostPortDb, String _user, String _pass) {
        return pgsqlDao(_cds, _hostPortDb, _user, _pass, null);
    }

    public static DAO pgsqlDao(String _hostPortDb, String _user, String _pass, Properties _infos)
    {
        return pgsqlDao(true, _hostPortDb, _user, _pass, _infos);
    }

    public static DAO pgsqlDao(String _hostPortDb, String _user, String _pass)
    {
        return pgsqlDao(true, _hostPortDb, _user, _pass, null);
    }

    public static DAO mssqlDao(boolean _cds, String _hostPortDb, String _user, String _pass, Properties _infos) {
        DAO _dao = daoFromJdbc(_cds, "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://" + _hostPortDb, _user, _pass, _infos);
        _dao.setDbType(DAO.DbType.DB_TYPE_MSSQL);
        return _dao;
    }

    public static DAO mssqlDao(boolean _cds, String _hostPortDb, String _user, String _pass) {
        return mssqlDao(_cds, _hostPortDb, _user, _pass, null);
    }

    public static DAO mssqlDao(String _hostPortDb, String _user, String _pass, Properties _infos)
    {
        return mssqlDao(true, _hostPortDb, _user, _pass, _infos);
    }

    public static DAO mssqlDao(String _hostPortDb, String _user, String _pass)
    {
        return mssqlDao(true, _hostPortDb, _user, _pass, null);
    }

    public static DAO jtdsDao(boolean _cds, String _hostPortDb, String _user, String _pass, Properties _infos) {
        DAO _dao = daoFromJdbc(_cds, "net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:" + _hostPortDb, _user, _pass, _infos);
        _dao.setDbType(DAO.DbType.DB_TYPE_SYBASE);
        return _dao;
    }

    public static DAO jtdsDao(boolean _cds, String _hostPortDb, String _user, String _pass) {
        return jtdsDao(_cds, _hostPortDb, _user, _pass, null);
    }

    public static DAO jtdsDao(String _hostPortDb, String _user, String _pass, Properties _infos)
    {
        return jtdsDao(true, _hostPortDb, _user, _pass, _infos);
    }

    public static DAO jtdsDao(String _hostPortDb, String _user, String _pass)
    {
        return jtdsDao(true, _hostPortDb, _user, _pass, null);
    }

    public static DAO xlsxDao(boolean _cds, String _filePath, Properties _infos) {
        DAO _dao = daoFromJdbc(_cds, "com.sqlsheet.XlsDriver", "jdbc:xls:file:" + _filePath, null, null, _infos);
        _dao.setDbType(DAO.DbType.DB_TYPE_ANSI);
        return _dao;
    }

    public static DAO xlsxDao(boolean _cds, String _filePath) {
        return xlsxDao(_cds, _filePath, null);
    }

    public static DAO xlsxDao(String _filePath, Properties _infos)
    {
        return xlsxDao(true, _filePath, _infos);
    }

    public static DAO xlsxDao(String _filePath)
    {
        return xlsxDao(true, _filePath, null);
    }

    public static Map<String,Object> filterFields(Map<String,Object> row, List<String> fieldList) throws Exception
    {
        LinkedHashMap<String,Object> returnRow = new LinkedHashMap();
        for(String key : fieldList)
        {
            if(row.containsKey(key))
            {
                returnRow.put(key, row.get(key));
            }
        }
        return returnRow;
    }

    public static String buildInsert(DAO.DbType _dbType, String _table, Map<String, Object> _parms, List<Object> _bind)
    {
        return buildInsert(_dbType, _table, null, null, new ArrayList<>(_parms.keySet()), false, null, _parms, _bind);
    }

    public static String buildInsert(DAO.DbType _dbType, String _table, List<String> _cols, Map<String, Object> _parms, List<Object> _bind)
    {
        return buildInsert(_dbType, _table, null, null, _cols, false, null, _parms, _bind);
    }

    public static String buildInsert(DAO.DbType _dbType, String _table, String _pkcol, String _pkval, Map<String, Object> _parms, List<Object> _bind)
    {
        return buildInsert(_dbType, _table, _pkcol, _pkval, new ArrayList<>(_parms.keySet()), false, null, _parms, _bind);
    }

    public static String buildInsert(DAO.DbType _dbType, String _table, String _pkcol, String _pkval, List<String> _cols, Map<String, Object> _parms, List<Object> _bind)
    {
        return buildInsert(_dbType, _table, _pkcol, _pkval, _cols, false, null, _parms, _bind);
    }

    public static String buildInsert(DAO.DbType _dbType, String _table, String _pkcol, String _pkval, List<String> _cols, boolean _onDuplicateKeyUpdate, List<String> _ucols, Map<String, Object> _parms, List<Object> _bind)
    {
        StringBuilder qq=new StringBuilder();
        qq.append("INSERT INTO "+_table+" ( ");

        boolean op = true;
        if(_pkcol != null)
        {
            qq.append(_pkcol);
            if(_pkval != null)
            {
                _bind.add(_pkval);
            }
            else
            {
                _bind.add(_parms.get(_pkcol));
            }
            op = false;
        }

        for(String _k : _cols)
        {
            _bind.add(_parms.get(_k));
            if(!op)
            {
                qq.append(",");
            }
            qq.append(_k);
            op=false;
        }
        qq.append(" ) VALUES (");

        op = true;
        for(Object v : _bind)
        {
            if(!op)
            {
                qq.append(",");
            }
            qq.append("?");
            op=false;
        }
        qq.append(" ) ");

        if(_onDuplicateKeyUpdate)
        {
            switch(_dbType)
            {
                case DB_TYPE_POSTGRES:
                case DB_TYPE_SQLITE:
                {
                    qq.append(" ON CONFLICT ( ");
                    boolean ff = true;
                    for(String _k : _cols)
                    {
                        if(!_ucols.contains(_k))
                        {
                            if(!ff) qq.append(",");
                            qq.append(_k);
                            ff=false;
                        }
                    }
                    qq.append(" ) ");
                    Map<String, Object> um =new HashMap();
                    for(String _o : _ucols)
                    {
                        um.put(_o, _parms.get(_o));
                    }
                    String setuqq = buildSet(_dbType, _bind, um);
                    qq.append(" DO UPDATE SET ");
                    qq.append(setuqq);
                    break;
                }
                case DB_TYPE_MYSQL:
                case DB_TYPE_CRATE:
                {
                    Map<String, Object> um =new HashMap();
                    for(String _o : _ucols)
                    {
                        um.put(_o, _parms.get(_o));
                    }
                    String setuqq = buildSet(_dbType, _bind, um);
                    qq.append(" ON DUPLICATE KEY UPDATE ");
                    qq.append(setuqq);
                    break;
                }
                default:
                    throw new IllegalArgumentException("DB TYPE NOT UPSERT-ABLE");
            }
        }

        return qq.toString();
    }

    /**
     * create a set statement-fragment and parameter-list from a column-map.
     *
     */
    public static String buildSet(DAO.DbType _dbType, List parm, Map vm)
    {
        StringBuilder qq=new StringBuilder();
        boolean op = true;
        for(Object kv : vm.entrySet())
        {
            String k = ((Map.Entry)kv).getKey().toString();
            Object v = ((Map.Entry)kv).getValue();
            if(op == true)
            {
                qq.append(k+"=?");
                op = false;
            }
            else
            {
                qq.append(", "+k+"=?");
            }
            parm.add(v);
        }
        return(qq.toString());
    }


    public static enum TemplateType {
        TEMPLATE_TYPE_AUTO,
        TEMPLATE_TYPE_EQUAL,
        TEMPLATE_TYPE_NOT_EQUAL,
        TEMPLATE_TYPE_SUBSTRING,
        TEMPLATE_TYPE_STARTSWITH,
        TEMPLATE_TYPE_LIKE,
        TEMPLATE_TYPE_REGEX
    }
    
    public static enum ConstraintType {
        CONSTRAINT_ANY_OF,
        CONSTRAINT_ALL_OF
    }
    
    /**
     * create a where statement-fragment and parameter-list from a column-map and constraint-type based on LIKE.
     *
     */
    public static String buildWhereLike(DAO.DbType _dbType, ConstraintType constraintType, List param, Map<String,Object> vm)
    {
        if(vm.size()==0)
        {
            return " TRUE ";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        sb.append(" ( ");
        for(String k : vm.keySet())
        {
            String v = vm.get(k).toString();
            if(v!="" && v!="*" && v!="%")
            {
                if(first)
                {
                    sb.append(" ("+likeOpPerDbType(_dbType, k, "?", false)+")");
                }
                else if(constraintType.equals(ConstraintType.CONSTRAINT_ALL_OF))
                {
                    sb.append(" AND ("+likeOpPerDbType(_dbType, k, "?", false)+")");
                }
                else if(constraintType.equals(ConstraintType.CONSTRAINT_ANY_OF))
                {
                    sb.append(" OR ("+likeOpPerDbType(_dbType, k, "?", false)+")");
                }
                else
                {
                    sb.append(" OR ("+likeOpPerDbType(_dbType, k, "?", false)+")");
                }
                param.add(v);
                first=false;
            }
        }
        sb.append(" ) ");

        return(sb.toString());
    }

    public static String likeOpPerDbType(DAO.DbType _dbType, String arg1, String arg2, boolean invert)
    {
        switch(_dbType)
        {
            case DB_TYPE_POSTGRES:
            {
                return arg1+(invert?" NOT":"")+" ILIKE "+arg2;
            }
            case DB_TYPE_ORACLE:
            case DB_TYPE_MSSQL:
            {
                return "LOWER("+arg1+")"+(invert?" NOT":"")+" LIKE "+arg2;
            }
            case DB_TYPE_ANSI:
            case DB_TYPE_MYSQL:
            case DB_TYPE_SYBASE:
            case DB_TYPE_DB2:
            case DB_TYPE_H2:
            case DB_TYPE_SQLITE:
            case DB_TYPE_CRATE:
            default:
            {
                return arg1+(invert?" NOT":"")+" LIKE "+arg2;
            }
        }
    }

    public static String existsOpPerDbType(DAO.DbType _dbType, String arg1, boolean invert)
    {
        switch(_dbType)
        {
            case DB_TYPE_POSTGRES:
            case DB_TYPE_ORACLE:
            case DB_TYPE_MSSQL:
            case DB_TYPE_ANSI:
            case DB_TYPE_MYSQL:
            case DB_TYPE_SYBASE:
            case DB_TYPE_DB2:
            case DB_TYPE_H2:
            case DB_TYPE_SQLITE:
            case DB_TYPE_CRATE:
            default:
            {
                return (invert?" NOT":" ")+"(("+arg1+" IS NOT NULL) AND ("+arg1+" !='')) ";
            }
        }
    }

    public static String regexpOpPerDbType(DAO.DbType _dbType, String arg1, String arg2, boolean invert)
    {
        switch(_dbType)
        {
            case DB_TYPE_CRATE:
            {
                return arg1+(invert?" !":" ")+"~ "+arg2;
            }
            case DB_TYPE_POSTGRES:
            {
                return arg1+(invert?" !":" ")+"~* "+arg2;
            }
            case DB_TYPE_ORACLE:
            {
                return (invert?"NOT ":"")+"REGEXP_LIKE("+arg1+", "+arg2+", 'i')";
            }
            case DB_TYPE_MYSQL:
            {
                return arg1+(invert?" NOT":"")+" RLIKE "+arg2;
            }
            case DB_TYPE_MSSQL:
            case DB_TYPE_SYBASE:
            case DB_TYPE_DB2:
            case DB_TYPE_H2:
            case DB_TYPE_SQLITE:
            case DB_TYPE_ANSI:
            default:
            {
                return arg1+(invert?" NOT":"")+" REGEXP "+arg2;
            }
        }
    }
    /**
     * create a where statement-fragment and parameter-list from a column-map and constraint-type based on REGEXP.
     *
     */
    public static String buildWhereRegexp(DAO.DbType _dbType, ConstraintType constraintType, List param, Map<String,Object> vm)
    {
        if(vm.size()==0)
        {
            return " TRUE ";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        sb.append(" ( ");
        for(String k : vm.keySet())
        {
            String v = vm.get(k).toString();
            if(v!="" && v!="*" && v!=".*")
            {
                if(first)
                {
                    sb.append(" ("+regexpOpPerDbType(_dbType, k, "?", false)+")");
                }
                else if(constraintType.equals(ConstraintType.CONSTRAINT_ALL_OF))
                {
                    sb.append(" AND ("+regexpOpPerDbType(_dbType, k, "?", false)+")");
                }
                else if(constraintType==ConstraintType.CONSTRAINT_ANY_OF)
                {
                    sb.append(" OR ("+regexpOpPerDbType(_dbType, k, "?", false)+")");
                }
                else
                {
                    sb.append(" OR ("+regexpOpPerDbType(_dbType, k, "?", false)+")");
                }
                param.add(v);
                first=false;
            }
        }
        sb.append(" ) ");

        return(sb.toString());
    }


    /**
     * create a where statement-fragment and parameter-list from a column-map, template-type and constraint-type.
     *
     */
    public static String buildWhere(DAO.DbType _dbType, TemplateType templateType, List param, Map<String,Object> template)
    {
        return buildWhere(_dbType, templateType, ConstraintType.CONSTRAINT_ANY_OF, param, template);
    }

    public static String buildWhere(DAO.DbType _dbType, TemplateType templateType, ConstraintType constraintType, List param, Map<String,Object> template)
    {
        switch(templateType)
        {
            case TEMPLATE_TYPE_AUTO:
                return buildWhereAuto(_dbType, constraintType, param, template);
            case TEMPLATE_TYPE_EQUAL:
                return buildWhereEqual(_dbType, constraintType, param, template);
            case TEMPLATE_TYPE_NOT_EQUAL:
                return buildWhereNotEqual(_dbType, constraintType, param, template);
            case TEMPLATE_TYPE_LIKE:
                return buildWhereLike(_dbType, constraintType, param, template);
            case TEMPLATE_TYPE_REGEX:
                return buildWhereRegexp(_dbType, constraintType, param, template);
            case TEMPLATE_TYPE_STARTSWITH:
                return buildWherePrefix(_dbType, constraintType, param, template);
            case TEMPLATE_TYPE_SUBSTRING:
                return buildWhereSubstr(_dbType, constraintType, param, template);
            default:
                return buildWhereLike(_dbType, constraintType, param, template);
        }
    }


    public static void parseSpec(DAO.DbType _dbType, StringBuilder sb, List param, String k, String s)
    {
        if(s==null || s.trim().length()==0)
        {
            sb.append(" TRUE ");
            return;
        }
        boolean invert = false;

        if(s.charAt(0)=='!')
        {
            invert = true;
            s = s.substring(1);
        }

        if(s.charAt(0)=='+' || s.charAt(0)=='-')
        {
            String[] list = s.split("[,;]");

            sb.append(" (");

            boolean first = true;
            for(String item : list)
            {
                if(!first)
                {
                    sb.append(invert ? " AND ": " OR ");
                }

                if(s.charAt(0)=='+')
                {
                    parseSpec_(_dbType, sb, param, k, item.substring(1), invert);
                }
                else if(s.charAt(0)=='-')
                {
                    parseSpec_(_dbType, sb, param, k, item.substring(1), !invert);
                }
                else
                {
                    parseSpec_(_dbType, sb, param, k, item, invert);
                }
                first = false;
            }

            sb.append(")");
        }
        else
        {
            parseSpec_(_dbType, sb, param, k, s, invert);
        }
    }

    public static void parseSpec_(DAO.DbType _dbType, StringBuilder sb, List param, String k, String s, boolean invert)
    {
        if(s.trim().length()==0)
        {
            return;
        }

        if(s.charAt(0)=='~')
        {
            s=s.substring(1);
            sb.append(" ("+regexpOpPerDbType(_dbType, k, "?", invert)+")");
        }
        else if(s.charAt(0)=='^')
        {
            s=s.substring(1);
            sb.append(" ("+likeOpPerDbType(_dbType, k, "?", invert)+")");
            param.add(s+'%');
            return;
        }
        else if("*".equals(s) || "%".equals(s))
        {
            sb.append(" ("+existsOpPerDbType(_dbType, k, invert)+")");
            return;
        }
        else if(s.indexOf('*')>=0)
        {
            s=s.replace('*', '%');
            sb.append(" ("+likeOpPerDbType(_dbType, k, "?", invert)+")");
        }
        else if(s.indexOf('%')>=0)
        {
            sb.append(" ("+likeOpPerDbType(_dbType, k, "?", invert)+")");
        }
        else
        {
            if(invert)
            {
                sb.append(" ("+k+"!=?)");
            }
            else
            {
                sb.append(" ("+k+"=?)");
            }
        }
        param.add(s);
    }


    public static String buildWhereAuto(DAO.DbType _dbType, List param, String _key, String _value)
    {
        StringBuilder sb = new StringBuilder();

        if(!_value.contentEquals(""))
        {
            parseSpec(_dbType, sb, param, _key, _value);
        }
        else
        {
            sb.append(" TRUE ");
        }
        return(sb.toString());
    }

    public static String buildWhereAuto(DAO.DbType _dbType, List param, Map<String,Object> vm)
    {
        return buildWhereAuto(_dbType, ConstraintType.CONSTRAINT_ALL_OF, param, vm);
    }

    public static String buildWhereAuto(DAO.DbType _dbType, ConstraintType constraintType, List param, Map<String,Object> vm)
    {
        if(vm.size()==0)
        {
            return " TRUE ";
        }

        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for(String k : vm.keySet())
        {
            Object v = vm.get(k);
            if((v instanceof String) && v.toString()!="")
            {
                if(!first) {
                    if (constraintType == ConstraintType.CONSTRAINT_ALL_OF) {
                        sb.append(" AND ");
                    } else {
                        sb.append(" OR ");
                    }
                }
                parseSpec(_dbType, sb, param, k, v.toString());
                first = false;
            }
        }
        return(sb.toString());
    }

    public static String buildWhereEqual(DAO.DbType _dbType, List param, Map<String,Object> vm)
    {
        return buildWhereEqual(_dbType, ConstraintType.CONSTRAINT_ALL_OF, param, vm);
    }

    public static String buildWhereEqual(DAO.DbType _dbType, ConstraintType constraintType, List param, Map<String,Object> vm)
    {
        if(vm.size()==0)
        {
            return " TRUE ";
        }

        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for(String k : vm.keySet())
        {
            Object v = vm.get(k);
            if(v!=null && !((v instanceof String) && (v.toString().equals(""))))
            {
                if(!first)
                {
                    if(constraintType == ConstraintType.CONSTRAINT_ALL_OF)
                    {
                        sb.append(" AND ");
                    }
                    else
                    {
                        sb.append(" OR ");
                    }
                }
                sb.append("("+k+" = ?)");
                param.add(v);
                first = false;
            }
        }
        return(sb.toString());
    }

    public static String buildWhereNotEqual(DAO.DbType _dbType, List param, Map<String,Object> vm)
    {
        return buildWhereEqual(_dbType, ConstraintType.CONSTRAINT_ALL_OF, param, vm);
    }

    public static String buildWhereNotEqual(DAO.DbType _dbType, ConstraintType constraintType, List param, Map<String,Object> vm)
    {
        if(vm.size()==0)
        {
            return " TRUE ";
        }

        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for(String k : vm.keySet())
        {
            Object v = vm.get(k);
            if(v!=null && !((v instanceof String) && (v.toString().equals(""))))
            {
                if(!first) {
                    if (constraintType == ConstraintType.CONSTRAINT_ALL_OF) {
                        sb.append(" AND ");
                    } else {
                        sb.append(" OR ");
                    }
                }
                sb.append("(" + k + " != ?)");
                param.add(v);
                first = false;
            }
        }
        return(sb.toString());
    }

    public static String buildWhereSubstr(DAO.DbType _dbType, List param, Map<String,Object> vm)
    {
        return buildWhereSubstr(_dbType, ConstraintType.CONSTRAINT_ALL_OF, param, vm);
    }

    public static String buildWhereSubstr(DAO.DbType _dbType, ConstraintType constraintType, List param, Map<String,Object> vm)
    {
        if(vm.size()==0)
        {
            return " TRUE ";
        }

        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for(String k : vm.keySet())
        {
            Object v = vm.get(k);
            if(v!=null && !((v instanceof String) && (v.toString().equals(""))))
            {
                if(!first) {
                    if (constraintType == ConstraintType.CONSTRAINT_ALL_OF) {
                        sb.append(" AND ");
                    } else {
                        sb.append(" OR ");
                    }
                }
                sb.append("("+likeOpPerDbType(_dbType, k, "?", false)+")");

                param.add("%"+v+"%");
                first = false;
            }
        }
        return(sb.toString());
    }

    public static String buildWherePrefix(DAO.DbType _dbType, List param, Map<String,Object> vm)
    {
        return buildWherePrefix(_dbType, ConstraintType.CONSTRAINT_ALL_OF, param, vm);
    }

    public static String buildWherePrefix(DAO.DbType _dbType, ConstraintType constraintType, List param, Map<String,Object> vm)
    {
        if(vm.size()==0)
        {
            return " TRUE ";
        }

        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for(String k : vm.keySet())
        {
            Object v = vm.get(k);
            if(v!=null && !((v instanceof String) && (v.toString().equals(""))))
            {
                if(!first) {
                    if (constraintType == ConstraintType.CONSTRAINT_ALL_OF) {
                        sb.append(" AND ");
                    } else {
                        sb.append(" OR ");
                    }
                }
                sb.append("("+likeOpPerDbType(_dbType, k, "?", false)+")");
                param.add(v+"%");
                first = false;
            }
        }
        return(sb.toString());
    }

    public static String preparseParameters(DAO.DbType _dbType, String _format, Map<String, Object> _params, List<Object> _bind) {
        String prefix = "?{";
        String suffix = "}";
        StringBuilder sb = new StringBuilder();

        int offset = 0;
        int found = -1;
        while ((found = _format.indexOf(prefix, offset)) >= offset) {
            sb.append(_format.substring(offset, found));

            if (suffix.length() == 0) {
                offset = found + prefix.length() + 1;
            } else {
                offset = _format.indexOf(suffix, found + prefix.length());
            }

            if (offset > found) {
                String tag = _format.substring(found + prefix.length(), offset);
                offset += suffix.length();

                sb.append("?");
                if (_params.containsKey(tag)) {
                    _bind.add(_params.get(tag));
                } else if (_params.containsKey(tag.toLowerCase())) {
                    _bind.add(_params.get(tag.toLowerCase()));
                } else if (_params.containsKey(tag.toUpperCase())) {
                    _bind.add(_params.get(tag.toUpperCase()));
                } else {
                    _bind.add("{" + tag.toUpperCase() + "}");
                }
            } else {
                sb.append(prefix);
                offset = found + prefix.length();
            }
        }
        sb.append(_format.substring(offset));

        return sb.toString();
    }

}
