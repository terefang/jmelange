package com.github.terefang.jmelange.dao2.rsh;

import com.github.terefang.jmelange.dao2.DAO;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class DefaultArrayRowHandler implements ResultRowProcessor<Object[]>
{
    public static final DefaultArrayRowHandler INSTANCE = new DefaultArrayRowHandler();

    ResultColumnProcessor<Object> columnProcessor = DefaultColumnProcessor.INSTANCE;

    public ResultColumnProcessor<Object> getColumnProcessor() {
        return columnProcessor;
    }

    public void setColumnProcessor(ResultColumnProcessor<Object> columnProcessor) {
        this.columnProcessor = columnProcessor;
    }

    @SneakyThrows
    @Override
    public Object[] processResultRow(DAO _dao, Statement _st, ResultSet _rs)
    {
        ResultSetMetaData _meta = _rs.getMetaData();
        int _cols = _meta.getColumnCount();
        Object[] _row = new Object[_cols];
        for(int _i=1; _i<=_cols; _i++)
        {
            _row[_i-1] = this.columnProcessor.processResultColumn(_dao, _st, _rs, _i);
        }
        return _row;
    }
}
