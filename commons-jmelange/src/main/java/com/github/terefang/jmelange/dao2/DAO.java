package com.github.terefang.jmelange.dao2;

import com.github.terefang.jmelange.dao2.rsh.*;
import com.github.terefang.jmelange.dao2.util.DaoUtil;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class DAO implements AutoCloseable
{

    public static enum DbType {
        DB_TYPE_POSTGRES,
        DB_TYPE_ORACLE,
        DB_TYPE_MSSQL,
        DB_TYPE_SYBASE,
        DB_TYPE_DB2,
        DB_TYPE_H2,
        DB_TYPE_SQLITE,
        DB_TYPE_CRATE,
        DB_TYPE_ANSI,
        DB_TYPE_MYSQL
    }

    DataSource dataSource;
    Connection connection;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void checkReadOnly() throws Exception
    {
        if(this.isReadOnly())
        {
            throw new IllegalAccessException("DAO is read-only.");
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
    boolean autoCommit;

    public boolean isAutoCommit() {
        return autoCommit;
    }

    @SneakyThrows
    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;

        if (this.dataSource != null) {
            if (this.connection != null) {
                if (!autoCommit) {
                    this.connection.commit();
                }
                this.connection.close();
                this.connection = null;
            }

            if (!autoCommit) {
                this.connection = this.dataSource.getConnection();
            }
        }

        if (this.connection != null) {
            this.connection.setAutoCommit(autoCommit);
        }
    }

    DbType dbType = DbType.DB_TYPE_ANSI;

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }


    /**
     * commits a transaction
     */
    @SneakyThrows
    public void commit() {
        if (this.connection != null) {
            this.connection.commit();
        }
    }

    /**
     * rollsback a transaction
     */
    @SneakyThrows
    public void rollback() {
        if (this.connection != null) {
            this.connection.rollback();
        }
    }

    /**
     * @param _query
     * @param _params
     */
    public int execute(String _query, Map<String, Object> _params) {
        return _execute(this, _query, _params);
    }

    @SneakyThrows
    static int _execute(DAO _dao, String _query, Map<String, Object> _params) {
        if (_dao.connection != null) {
            return _doExecute(_dao, _dao.connection, _query, _params);
        } else if (_dao.dataSource != null) {
            try (Connection _connection = _dao.dataSource.getConnection();) {
                return _doExecute(_dao, _connection, _query, _params);
            }
        }
        return -1;
    }

    @SneakyThrows
    private static int _doExecute(DAO _dao, Connection _connection, String _query, Map<String, Object> _params) {
        List<Object> _bind = new Vector<>();
        _query = DaoUtil.preparseParameters(_dao.dbType, _query, _params, _bind);
        try (PreparedStatement _statement = _connection.prepareStatement(_query);) {
            for (int _i = 1; _i <= _bind.size(); _i++) {
                _statement.setObject(_i, _bind.get(_i - 1));
            }
            return _statement.execute() ? 0 : -1;
        }
    }

    /**
     * @param _query
     * @param _params
     */
    public int execute(String _query, List<Object> _params) {
        return _execute(this, _query, _params);
    }

    @SneakyThrows
    static int _execute(DAO _dao, String _query, List<Object> _params) {
        if (_dao.connection != null) {
            return _doExecute(_dao, _dao.connection, _query, _params);
        } else if (_dao.dataSource != null) {
            try (Connection _connection = _dao.dataSource.getConnection();) {
                return _doExecute(_dao, _connection, _query, _params);
            }
        }
        return -1;
    }

    @SneakyThrows
    private static int _doExecute(DAO _dao, Connection _connection, String _query, List<Object> _bind) {
        try (PreparedStatement _statement = _connection.prepareStatement(_query);) {
            for (int _i = 1; _i <= _bind.size(); _i++) {
                _statement.setObject(_i, _bind.get(_i - 1));
            }
            return _statement.execute() ? 0 : -1;
        }
    }

    /**
     * @param _query
     */
    public int execute(String _query) {
        return _execute(this, _query);
    }

    @SneakyThrows
    static int _execute(DAO _dao, String _query) {
        if (_dao.connection != null) {
            return _doExecute(_dao, _dao.connection, _query);
        } else if (_dao.dataSource != null) {
            try (Connection _connection = _dao.dataSource.getConnection();) {
                return _doExecute(_dao, _connection, _query);
            }
        }
        return -1;
    }

    @SneakyThrows
    private static int _doExecute(DAO _dao, Connection _connection, String _query) {
        List<Object> _bind = new Vector<>();
        try (PreparedStatement _statement = _connection.prepareStatement(_query);) {
            return _statement.execute() ? 0 : -1;
        }
    }

    // queryTemplates

    @SneakyThrows
    public static <T> T _queryTemplateForT(DAO _dao, String _table, List<String> _cols, Map<String, Object> _params, String suffixQuery, DaoUtil.TemplateType templateType, DaoUtil.ConstraintType constraintType, ResultSetProcessor<T> _proc)
    {
        List<Object> param = new Vector();
        String colString = ((_cols == null) ? "*" : DaoUtil.join(',', _cols));
        return _queryForT(_dao, "SELECT "+colString+" FROM "+_table+" WHERE "+DaoUtil.buildWhere(_dao.getDbType(), templateType, constraintType, param, _params)+(suffixQuery==null?"":" "+suffixQuery), param, _proc);
    }

    @SneakyThrows
    public static <T> T _queryTemplateForT(DAO _dao, String _table, List<String> _cols, Map<String, Object> _params, String suffixQuery, ResultSetProcessor<T> _proc)
    {
        return _queryTemplateForT(_dao, _table, _cols,_params, suffixQuery, DaoUtil.TemplateType.TEMPLATE_TYPE_AUTO, DaoUtil.ConstraintType.CONSTRAINT_ALL_OF, _proc);
    }

    @SneakyThrows
    public static <T> T _queryTemplateForT(DAO _dao, String _table, List<String> _cols, Map<String, Object> _params, ResultSetProcessor<T> _proc)
    {
        return _queryTemplateForT(_dao, _table, _cols, _params, null, DaoUtil.TemplateType.TEMPLATE_TYPE_AUTO, DaoUtil.ConstraintType.CONSTRAINT_ALL_OF, _proc);
    }

    @SneakyThrows
    public static <T> T _queryTemplateForT(DAO _dao, String _table, Map<String, Object> _params, ResultSetProcessor<T> _proc)
    {
        return _queryTemplateForT(_dao, _table, null, _params, null, DaoUtil.TemplateType.TEMPLATE_TYPE_AUTO, DaoUtil.ConstraintType.CONSTRAINT_ALL_OF, _proc);
    }

    /**
     * @param _table    — the table to query
     * @param _cols     — the columns to return in the result
     * @param _params   — the query filter
     * @return a list of maps for each row
     */
    public List<Map<String, Object>> queryTemplateForMapList(String _table, List<String> _cols, Map<String, Object> _params) {
        return _queryTemplateForT(this, _table, _cols, _params, DefaultMapListHandler.INSTANCE);
    }

    /**
     * @param _table    — the table to query
     * @param _params   — the query filter
     * @return a list of maps for each row
     */
    public List<Map<String, Object>> queryTemplateForMapList(String _table, Map<String, Object> _params) {
        return _queryTemplateForT(this, _table, _params, DefaultMapListHandler.INSTANCE);
    }

    /**
     * @param _table    — the table to query
     * @param _col1     — the key name column
     * @param _col2     — the value column
     * @param _params   — the query filter
     * @return a kv-pair for each row
     */
    public Map<String, Object> queryTemplateForKvMap(String _table, String _col1, String _col2, Map<String, Object> _params) {
        return _queryTemplateForT(this, _table, Arrays.asList(_col1,_col2), _params, DefaultKvMapHandler.INSTANCE);
    }

    /**
     * @param _table    — the table to query
     * @param _cols     — the columns to return in the result
     * @param _params   — the query filter
     * @return a list of arrays for each row
     */
    public List<Object[]> queryTemplateForArrayList(String _table, List<String> _cols, Map<String, Object> _params) {
        return _queryTemplateForT(this, _table, _cols, _params, DefaultArrayListHandler.INSTANCE);
    }
    public List<Object> queryTemplateForColumnList(String _table, String _col, Map<String, Object> _params) {
        return _queryTemplateForT(this, _table, Collections.singletonList(_col), _params, DefaultColumnListHandler.INSTANCE);
    }

    public Object queryTemplateForScalar(String _table, Map<String, Object> _params) {
        return _queryTemplateForT(this, _table, _params, DefaultScalarHandler.INSTANCE);
    }


    // QUERIES ----------------------------------------------------------------------------------------------------------------------------
    public <T> T queryForT(String _query, Map<String, Object> _params, ResultSetProcessor<T> _proc) {
        return _queryForT(this, _query, _params, _proc);
    }

    @SneakyThrows
    static <T> T _queryForT(DAO _dao, String _query, Map<String, Object> _params, ResultSetProcessor<T> _proc) {
        if (_dao.connection != null) {
            return _doQueryForT(_dao, _dao.connection, _query, _params, _proc);
        } else if (_dao.dataSource != null) {
            try (Connection _connection = _dao.dataSource.getConnection();) {
                return _doQueryForT(_dao, _connection, _query, _params, _proc);
            }
        }
        return null;
    }

    @SneakyThrows
    static <T> T _doQueryForT(DAO _dao, Connection _connection, String _query, Map<String, Object> _params, ResultSetProcessor<T> _proc) {
        List<Object> _bind = new Vector<>();
        _query = DaoUtil.preparseParameters(_dao.dbType, _query, _params, _bind);
        try (PreparedStatement _statement = _connection.prepareStatement(_query);) {
            for (int _i = 1; _i <= _bind.size(); _i++) {
                _statement.setObject(_i, _bind.get(_i - 1));
            }

            try (ResultSet _rs = _statement.executeQuery(_query);) {
                return _proc.processResultSet(_dao, _statement, _rs);
            }
        }
    }

    public <T> T queryForT(String _query, ResultSetProcessor<T> _proc) {
        return _queryForT(this, _query, _proc);
    }

    @SneakyThrows
    static <T> T _queryForT(DAO _dao, String _query, ResultSetProcessor<T> _proc) {
        if (_dao.connection != null) {
            return _doQueryForT(_dao, _dao.connection, _query, _proc);
        } else if (_dao.dataSource != null) {
            try (Connection _connection = _dao.dataSource.getConnection();) {
                return _doQueryForT(_dao, _connection, _query, _proc);
            }
        }
        return null;
    }

    @SneakyThrows
    static <T> T _doQueryForT(DAO _dao, Connection _connection, String _query, ResultSetProcessor<T> _proc) {
        try (PreparedStatement _statement = _connection.prepareStatement(_query);) {
            try (ResultSet _rs = _statement.executeQuery();) {
                return _proc.processResultSet(_dao, _statement, _rs);
            }
        }
    }

    public <T> T queryForT(String _query, List<Object> _params, ResultSetProcessor<T> _proc) {
        return _queryForT(this, _query, _params, _proc);
    }

    @SneakyThrows
    static <T> T _queryForT(DAO _dao, String _query, List<Object> _bind, ResultSetProcessor<T> _proc) {
        if (_dao.connection != null) {
            return _doQueryForT(_dao, _dao.connection, _query, _bind, _proc);
        } else if (_dao.dataSource != null) {
            try (Connection _connection = _dao.dataSource.getConnection();) {
                return _doQueryForT(_dao, _connection, _query, _bind, _proc);
            }
        }
        return null;
    }

    @SneakyThrows
    static <T> T _doQueryForT(DAO _dao, Connection _connection, String _query, List<Object> _bind, ResultSetProcessor<T> _proc) {
        try (PreparedStatement _statement = _connection.prepareStatement(_query);) {
            for (int _i = 1; _i <= _bind.size(); _i++) {
                _statement.setObject(_i, _bind.get(_i - 1));
            }

            try (ResultSet _rs = _statement.executeQuery(_query);) {
                return _proc.processResultSet(_dao, _statement, _rs);
            }
        }
    }

    // --- with iterators
    @SneakyThrows
    static <T> Iterator<T> makeIterator(final DAO _dao, final PreparedStatement _statement, final ResultSet _rs, final ResultRowProcessor<T> _proc)
    {
        return new Iterator<T>() {
            boolean _has_next = false;
            @Override
            @SneakyThrows
            public boolean hasNext()
            {
                _has_next = false;
                try
                {
                    _has_next = _rs.next();
                    if(!_has_next)
                    {
                        try {_rs.close();} catch (Exception _xe) {}
                        try {_statement.close();} catch (Exception _xe) {}
                    }
                }
                finally
                {
                    return _has_next;
                }
            }

            @Override
            public T next() {
                if(!_has_next)
                {
                    try {_rs.close();} catch (Exception _xe) {}
                    try {_statement.close();} catch (Exception _xe) {}
                    return null;
                }
                return _proc.processResultRow(_dao, _statement, _rs);
            }
        };
    }

    public <T> Iterator<T> queryForTiter(String _query, Map<String, Object> _params, ResultRowProcessor<T> _proc) {
        return _queryForTiter(this, _query, _params, _proc);
    }

    @SneakyThrows
    static <T> Iterator<T> _queryForTiter(DAO _dao, String _query, Map<String, Object> _params, ResultRowProcessor<T> _proc) {
        if (_dao.connection != null) {
            return _doQueryForTiter(_dao, _dao.connection, _query, _params, _proc);
        } else if (_dao.dataSource != null) {
            try (Connection _connection = _dao.dataSource.getConnection();) {
                return _doQueryForTiter(_dao, _connection, _query, _params, _proc);
            }
        }
        return null;
    }

    @SneakyThrows
    static <T> Iterator<T> _doQueryForTiter(DAO _dao, Connection _connection, String _query, Map<String, Object> _params, ResultRowProcessor<T> _proc)
    {
        List<Object> _bind = new Vector<>();
        _query = DaoUtil.preparseParameters(_dao.dbType, _query, _params, _bind);
        PreparedStatement _statement = _connection.prepareStatement(_query);
        for (int _i = 1; _i <= _bind.size(); _i++) {
            _statement.setObject(_i, _bind.get(_i - 1));
        }

        ResultSet _rs = _statement.executeQuery(_query);

        return makeIterator(_dao,_statement,_rs,_proc);
    }

    public <T> Iterator<T> queryForTiter(String _query, ResultRowProcessor<T> _proc) {
        return _queryForTiter(this, _query, _proc);
    }

    @SneakyThrows
    static <T> Iterator<T> _queryForTiter(DAO _dao, String _query, ResultRowProcessor<T> _proc) {
        if (_dao.connection != null) {
            return _doQueryForTiter(_dao, _dao.connection, _query, _proc);
        } else if (_dao.dataSource != null) {
            try (Connection _connection = _dao.dataSource.getConnection();) {
                return _doQueryForTiter(_dao, _connection, _query, _proc);
            }
        }
        return null;
    }

    @SneakyThrows
    static <T> Iterator<T> _doQueryForTiter(DAO _dao, Connection _connection, String _query, ResultRowProcessor<T> _proc) {
        PreparedStatement _statement = _connection.prepareStatement(_query);
        ResultSet _rs = _statement.executeQuery();
        return makeIterator(_dao, _statement, _rs, _proc);
    }

    public <T> Iterator<T> queryForTiter(String _query, List<Object> _params, ResultRowProcessor<T> _proc) {
        return _queryForTiter(this, _query, _params, _proc);
    }

    @SneakyThrows
    static <T> Iterator<T> _queryForTiter(DAO _dao, String _query, List<Object> _bind, ResultRowProcessor<T> _proc) {
        if (_dao.connection != null) {
            return _doQueryForTiter(_dao, _dao.connection, _query, _bind, _proc);
        } else if (_dao.dataSource != null) {
            try (Connection _connection = _dao.dataSource.getConnection();) {
                return _doQueryForTiter(_dao, _connection, _query, _bind, _proc);
            }
        }
        return null;
    }

    @SneakyThrows
    static <T> Iterator<T> _doQueryForTiter(DAO _dao, Connection _connection, String _query, List<Object> _bind, ResultRowProcessor<T> _proc) {
        PreparedStatement _statement = _connection.prepareStatement(_query);

        for (int _i = 1; _i <= _bind.size(); _i++) {
            _statement.setObject(_i, _bind.get(_i - 1));
        }

        ResultSet _rs = _statement.executeQuery(_query);

        return makeIterator(_dao, _statement, _rs, _proc);
    }


    // --- inserts

    @SneakyThrows
    public int insertTemplate(String _table, Map<String, Object> _parms)
    {
        return _insertTemplate(this, _table, null, null, new ArrayList<>(_parms.keySet()), false, null, _parms);
    }

    @SneakyThrows
    public int insertTemplate(String _table, String _pkcol, String _pkval, Map<String, Object> _parms)
    {
        return _insertTemplate(this, _table, _pkcol, _pkval, new ArrayList<>(_parms.keySet()), false, null, _parms);
    }

    public int insertTemplate(String _table, List<String> _cols, Map<String, Object> _parms)
    {
        return _insertTemplate(this, _table, null, null, _cols, false, null, _parms);
    }

    @SneakyThrows
    public int insertTemplate(String _table, String _pkcol, String _pkval, List<String> _cols, Map<String, Object> _parms)
    {
        return _insertTemplate(this, _table, _pkcol, _pkval, _cols, false, null, _parms);
    }

    @SneakyThrows
    public int insertTemplate(String _table, String _pkcol, String _pkval, List<String> _cols, boolean _onDuplicateKeyUpdate, List<String> _ucols, Map<String, Object> _parms)
    {
        return _insertTemplate(this, _table, _pkcol, _pkval, _cols, _onDuplicateKeyUpdate, _ucols, _parms);
    }

    @SneakyThrows
    private static int _insertTemplate(DAO _dao, String _table, String _pkcol, String _pkval, List<String> _cols, boolean _onDuplicateKeyUpdate, List<String> _ucols, Map<String, Object> _parms)
    {
        if (_dao.connection != null)
        {
            return _doInsertTemplate(_dao, _dao.connection, _table,_pkcol,_pkval, _cols, _onDuplicateKeyUpdate, _ucols, _parms);
        }
        else
        if (_dao.dataSource != null)
        {
            try (Connection _connection = _dao.dataSource.getConnection();)
            {
                return _doInsertTemplate(_dao, _connection, _table,_pkcol,_pkval, _cols, _onDuplicateKeyUpdate, _ucols, _parms);
            }
        }
        return -1;
    }

    @SneakyThrows
    private static int _doInsertTemplate(DAO _dao, Connection _connection, String _table, String _pkcol, String _pkval, List<String> _cols, boolean _onDuplicateKeyUpdate, List<String> _ucols, Map<String, Object> _parms)
    {
        List<Object> _bind = new Vector<>();
        String _query = DaoUtil.buildInsert(_dao.getDbType(), _table,_pkcol,_pkval, _cols, _onDuplicateKeyUpdate, _ucols, _parms, _bind);
        try (PreparedStatement _statement = _connection.prepareStatement(_query);) {
            for (int _i = 1; _i <= _bind.size(); _i++) {
                _statement.setObject(_i, _bind.get(_i - 1));
            }
            return _statement.executeUpdate();
        }
    }

    @SneakyThrows
    public int insertTemplateList(String _table, List<Map<String, Object>> _parms)
    {
        return _insertTemplateList(this, _table, null, new ArrayList<>(_parms.get(1).keySet()), false, null, _parms);
    }

    public int insertTemplateList(String _table, List<String> _cols, List<Map<String, Object>> _parms)
    {
        return _insertTemplateList(this, _table, null, _cols, false, null, _parms);
    }

    @SneakyThrows
    public int insertTemplateList(String _table, String _pkcol, List<String> _cols, List<Map<String, Object>> _parms)
    {
        return _insertTemplateList(this, _table, _pkcol, _cols, false, null, _parms);
    }


    @SneakyThrows
    public int insertTemplateList(String _table, String _pkcol, List<String> _cols, boolean _onDuplicateKeyUpdate, List<String> _ucols, List<Map<String, Object>> _parms)
    {
        return _insertTemplateList(this, _table, _pkcol, _cols, _onDuplicateKeyUpdate, _ucols, _parms);
    }

    @SneakyThrows
    private static int _insertTemplateList(DAO _dao, String _table, String _pkcol, List<String> _cols, boolean _onDuplicateKeyUpdate, List<String> _ucols, List<Map<String, Object>> _parms)
    {
        if (_dao.connection != null)
        {
            return _doInsertTemplateList(_dao, _dao.connection, _table,_pkcol, _cols, _onDuplicateKeyUpdate, _ucols, _parms);
        }
        else
        if (_dao.dataSource != null)
        {
            try (Connection _connection = _dao.dataSource.getConnection();)
            {
                return _doInsertTemplateList(_dao, _connection, _table,_pkcol, _cols, _onDuplicateKeyUpdate, _ucols, _parms);
            }
        }
        return -1;
    }

    @SneakyThrows
    private static int _doInsertTemplateList(DAO _dao, Connection _connection, String _table, String _pkcol, List<String> _cols, boolean _onDuplicateKeyUpdate, List<String> _ucols, List<Map<String, Object>> _parms)
    {
        int _ret = 0;
        for(Map<String, Object> _p : _parms)
        {
            List<Object> _bind = new Vector<>();
            String _query = DaoUtil.buildInsert(_dao.getDbType(), _table,_pkcol,null, _cols, _onDuplicateKeyUpdate, _ucols, _p, _bind);
            try (PreparedStatement _statement = _connection.prepareStatement(_query);)
            {
                for (int _i = 1; _i <= _bind.size(); _i++) {
                    _statement.setObject(_i, _bind.get(_i - 1));
                }
                _ret += _statement.executeUpdate();
            }
        }
        return _ret;
    }


    @SneakyThrows
    public List<String> insertTemplateListForPK(String _table, List<Map<String, Object>> _parms)
    {
        return _insertTemplateListForPK(this, _table, null, new ArrayList<>(_parms.get(1).keySet()), false, null, _parms, DefaultPkMapper.INSTANCE);
    }

    public List<String> insertTemplateListForPK(String _table, List<String> _cols, List<Map<String, Object>> _parms)
    {
        return _insertTemplateListForPK(this, _table, null, _cols, false, null, _parms, DefaultPkMapper.INSTANCE);
    }

    @SneakyThrows
    public List<String> insertTemplateListForPK(String _table, String _pkcol, List<String> _cols, List<Map<String, Object>> _parms)
    {
        return _insertTemplateListForPK(this, _table, _pkcol, _cols, false, null, _parms, DefaultPkMapper.INSTANCE);
    }


    @SneakyThrows
    public List<String> insertTemplateListForPK(String _table, String _pkcol, List<String> _cols, boolean _onDuplicateKeyUpdate, List<String> _ucols, List<Map<String, Object>> _parms)
    {
        return _insertTemplateListForPK(this, _table, _pkcol, _cols, _onDuplicateKeyUpdate, _ucols, _parms, DefaultPkMapper.INSTANCE);
    }

    @SneakyThrows
    private static <T> List<T> _insertTemplateListForPK(DAO _dao, String _table, String _pkcol, List<String> _cols, boolean _onDuplicateKeyUpdate, List<String> _ucols, List<Map<String, Object>> _parms, ResultPkProcessor<T> _mapper)
    {
        if (_dao.connection != null)
        {
            return _doInsertTemplateListForPK(_dao, _dao.connection, _table,_pkcol, _cols, _onDuplicateKeyUpdate, _ucols, _parms, _mapper);
        }
        else
        if (_dao.dataSource != null)
        {
            try (Connection _connection = _dao.dataSource.getConnection();)
            {
                return _doInsertTemplateListForPK(_dao, _connection, _table,_pkcol, _cols, _onDuplicateKeyUpdate, _ucols, _parms, _mapper);
            }
        }
        return Collections.emptyList();
    }

    @SneakyThrows
    private static <T> List<T> _doInsertTemplateListForPK(DAO _dao, Connection _connection, String _table, String _pkcol, List<String> _cols, boolean _onDuplicateKeyUpdate, List<String> _ucols, List<Map<String, Object>> _parms, ResultPkProcessor<T> _mapper)
    {
        List<T> _ret = new Vector<>();
        for(Map<String, Object> _p : _parms)
        {
            List<Object> _bind = new Vector<>();
            String _query = DaoUtil.buildInsert(_dao.getDbType(), _table,_pkcol,null, _cols, _onDuplicateKeyUpdate, _ucols, _p, _bind);
            try (PreparedStatement _statement = _connection.prepareStatement(_query, Statement.RETURN_GENERATED_KEYS);)
            {
                for (int _i = 1; _i <= _bind.size(); _i++) {
                    _statement.setObject(_i, _bind.get(_i - 1));
                }
                int _rc = _statement.executeUpdate();
                _ret.add(_mapper.processResultPK(_dao, _statement));
            }
        }
        return _ret;
    }

    @SneakyThrows
    public String insertTemplateForPK(String _table, List<String> _cols, boolean _onDuplicateKeyUpdate, List<String> _ucols, Map<String, Object> _params)
    {
        return _insertTemplateForPK(this, _table, _cols, _onDuplicateKeyUpdate, _ucols, _params, DefaultPkMapper.INSTANCE);
    }

    @SneakyThrows
    public String insertTemplateForPK(String _table, List<String> _cols,Map<String, Object> _params)
    {
        return _insertTemplateForPK(this, _table, _cols, false, null, _params, DefaultPkMapper.INSTANCE);
    }

    @SneakyThrows
    public String insertTemplateForPK(String _table, Map<String, Object> _params)
    {
        return _insertTemplateForPK(this, _table, new ArrayList<>(_params.keySet()), false, null, _params, DefaultPkMapper.INSTANCE);
    }

    @SneakyThrows
    public String insertForPK(String _query, Map<String, Object> _params)
    {
        List<Object> _bind = new Vector<>();
        _query = DaoUtil.preparseParameters(this.dbType,_query, _params, _bind);
        return _insertForPK(this, _query, _bind, DefaultPkMapper.INSTANCE);
    }

    @SneakyThrows
    public String insertForPK(String _query, List<Object> _bind)
    {
        return _insertForPK(this, _query, _bind, DefaultPkMapper.INSTANCE);
    }

    @SneakyThrows
    public String insertForPK(String _query)
    {
        return _insertForPK(this, _query, Collections.emptyList(), DefaultPkMapper.INSTANCE);
    }

    @SneakyThrows
    public static <T> T _insertForPK(DAO _dao, String _query, List<Object> _bind, ResultPkProcessor<T> _mapper)
    {
        if (_dao.connection != null)
        {
            return _doInsertForPK(_dao, _dao.connection, _query, _bind, _mapper);
        }
        else
        if (_dao.dataSource != null)
        {
            try (Connection _connection = _dao.dataSource.getConnection();)
            {
                return _doInsertForPK(_dao, _connection, _query, _bind, _mapper);
            }
        }
        return null;
    }


    @SneakyThrows
    public static <T> T _insertTemplateForPK(DAO _dao, String _table, List<String> _cols, boolean _onDuplicateKeyUpdate, List<String> _ucols, Map<String, Object> _params, ResultPkProcessor<T> _mapper)
    {
        if (_dao.connection != null)
        {
            return _doInsertTemplateForPK(_dao, _dao.connection, _table, _cols, _onDuplicateKeyUpdate, _ucols, _params, _mapper);
        }
        else
        if (_dao.dataSource != null)
        {
            try (Connection _connection = _dao.dataSource.getConnection();)
            {
                return _doInsertTemplateForPK(_dao, _connection, _table, _cols, _onDuplicateKeyUpdate, _ucols, _params, _mapper);
            }
        }
        return null;
    }

    @SneakyThrows
    public static <T> T _doInsertTemplateForPK(DAO _dao, Connection _connection, String _table, List<String> _cols, boolean _onDuplicateKeyUpdate, List<String> _ucols, Map<String, Object> _params, ResultPkProcessor<T> _mapper)
    {
        Vector parm = new Vector();
        StringBuilder qq=new StringBuilder();
        qq.append("INSERT INTO "+_table+" ( ");
        boolean op = true;
        for(String kv : _cols)
        {
            parm.add(_params.get(kv));
            if(!op)
            {
                qq.append(",");
            }
            qq.append(kv);
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

        if(_onDuplicateKeyUpdate)
        {
            if((_dao.dbType == DAO.DbType.DB_TYPE_MYSQL) || (_dao.dbType == DAO.DbType.DB_TYPE_CRATE))
            {
                Map um =new HashMap();
                for(Object o : _ucols)
                {
                    um.put(o, _params.get(o));
                }
                String setuqq = DaoUtil.buildSet(_dao.dbType, parm, um);
                qq.append(" ON DUPLICATE KEY UPDATE ");
                qq.append(setuqq);
            }
            else
            if(_dao.dbType == DAO.DbType.DB_TYPE_SQLITE)
            {
                Map um =new HashMap();
                for(Object o : _ucols)
                {
                    um.put(o, _params.get(o));
                }
                String setuqq = DaoUtil.buildSet(_dao.dbType, parm, um);
                qq.append(" ON CONFLICT("+ DaoUtil.join(",", _cols) +") DO UPDATE SET ");
                qq.append(setuqq);
            }
            else
            {
                throw new IllegalArgumentException("DB TYPE NO UPSERT SUPPORT");
            }
        }

        return (T)_doInsertForPK(_dao,_connection,qq.toString(), parm, _mapper);
    }

    @SneakyThrows
    private static <T> T _doInsertForPK(DAO _dao, Connection _connection, String _qq, List<Object> _bind, ResultPkProcessor<T> _mapper)
    {
        try (PreparedStatement _statement = _connection.prepareStatement(_qq, Statement.RETURN_GENERATED_KEYS);) {
            for (int _i = 1; _i <= _bind.size(); _i++) {
                _statement.setObject(_i, _bind.get(_i - 1));
            }
            int _rc = _statement.executeUpdate();
            return _mapper.processResultPK(_dao, _statement);
        }
    }


    public int update(String _query, Map<String, Object> _params) {
        return _update(this, _query, _params);
    }

    @SneakyThrows
    static int _update(DAO _dao, String _query, Map<String, Object> _params) {
        if (_dao.connection != null) {
            return _doUpdate(_dao, _dao.connection, _query, _params);
        } else if (_dao.dataSource != null) {
            try (Connection _connection = _dao.dataSource.getConnection();) {
                return _doUpdate(_dao, _connection, _query, _params);
            }
        }
        return -1;
    }

    @SneakyThrows
    private static int _doUpdate(DAO _dao, Connection _connection, String _query, Map<String, Object> _params) {
        List<Object> _bind = new Vector<>();
        _query = DaoUtil.preparseParameters(_dao.dbType, _query, _params, _bind);
        try (PreparedStatement _statement = _connection.prepareStatement(_query);) {
            for (int _i = 1; _i <= _bind.size(); _i++) {
                _statement.setObject(_i, _bind.get(_i - 1));
            }
            return _statement.executeUpdate();
        }
    }

    public int update(String _query, List<Object> _params) {
        return _update(this, _query, _params);
    }

    @SneakyThrows
    static int _update(DAO _dao, String _query, List<Object> _params) {
        if (_dao.connection != null) {
            return _doUpdate(_dao, _dao.connection, _query, _params);
        } else if (_dao.dataSource != null) {
            try (Connection _connection = _dao.dataSource.getConnection();) {
                return _doUpdate(_dao, _connection, _query, _params);
            }
        }
        return -1;
    }

    @SneakyThrows
    private static int _doUpdate(DAO _dao, Connection _connection, String _query, List<Object> _bind) {
        try (PreparedStatement _statement = _connection.prepareStatement(_query);) {
            for (int _i = 1; _i <= _bind.size(); _i++) {
                _statement.setObject(_i, _bind.get(_i - 1));
            }
            return _statement.executeUpdate();
        }
    }

    public int update(String _query) {
        return _update(this, _query);
    }

    @SneakyThrows
    static int _update(DAO _dao, String _query) {
        if (_dao.connection != null) {
            return _doUpdate(_dao, _dao.connection, _query);
        } else if (_dao.dataSource != null) {
            try (Connection _connection = _dao.dataSource.getConnection();) {
                return _doUpdate(_dao, _connection, _query);
            }
        }
        return -1;
    }

    @SneakyThrows
    private static int _doUpdate(DAO _dao, Connection _connection, String _query) {
        List<Object> _bind = new Vector<>();
        try (PreparedStatement _statement = _connection.prepareStatement(_query);) {
            return _statement.executeUpdate();
        }
    }

    // MapList -------------------------------------------------------------------------------------------------------
    public List<Map<String, Object>> queryForMapList(String _query) {
        return _queryForT(this, _query, DefaultMapListHandler.INSTANCE);
    }

    public List<Map<String, Object>> queryForMapList(String _query, List<Object> _bind) {
        return _queryForT(this, _query, _bind, DefaultMapListHandler.INSTANCE);
    }

    public List<Map<String, Object>> queryForMapList(String _query, Map<String, Object> _params) {
        return _queryForT(this, _query, _params, DefaultMapListHandler.INSTANCE);
    }

    // KvMap -------------------------------------------------------------------------------------------------------
    public Map<String, Object> queryForKvMap(String _query) {
        return _queryForT(this, _query, DefaultKvMapHandler.INSTANCE);
    }

    public Map<String, Object> queryForKvMap(String _query, List<Object> _bind) {
        return _queryForT(this, _query, _bind, DefaultKvMapHandler.INSTANCE);
    }

    public Map<String, Object> queryForKvMap(String _query, Map<String, Object> _params) {
        return _queryForT(this, _query, _params, DefaultKvMapHandler.INSTANCE);
    }

    // ArrayList -------------------------------------------------------------------------------------------------------
    public List<Object[]> queryForArrayList(String _query) {
        return _queryForT(this, _query, DefaultArrayListHandler.INSTANCE);
    }

    public List<Object[]> queryForArrayList(String _query, List<Object> _bind) {
        return _queryForT(this, _query, _bind, DefaultArrayListHandler.INSTANCE);
    }

    public List<Object[]> queryForArrayList(String _query, Map<String, Object> _params) {
        return _queryForT(this, _query, _params, DefaultArrayListHandler.INSTANCE);
    }

    // ColumnList -------------------------------------------------------------------------------------------------------
    public List<Object> queryForColumnList(String _query) {
        return _queryForT(this, _query, DefaultColumnListHandler.INSTANCE);
    }

    public List<Object> queryForColumnList(String _query, List<Object> _bind) {
        return _queryForT(this, _query, _bind, DefaultColumnListHandler.INSTANCE);
    }

    public List<Object> queryForColumnList(String _query, Map<String, Object> _params) {
        return _queryForT(this, _query, _params, DefaultColumnListHandler.INSTANCE);
    }

    // Scalar -------------------------------------------------------------------------------------------------------
    public Object queryForScalar(String _query) {
        return _queryForT(this, _query, DefaultScalarHandler.INSTANCE);
    }

    public Object queryForScalar(String _query, List<Object> _bind) {
        return _queryForT(this, _query, _bind, DefaultScalarHandler.INSTANCE);
    }

    public Object queryForScalar(String _query, Map<String, Object> _params) {
        return _queryForT(this, _query, _params, DefaultScalarHandler.INSTANCE);
    }

    @Override
    public void close() throws Exception
    {
        if(this.connection!=null)
        {
            try{this.connection.close();} catch (Exception _xe) {}
        }
    }


}
