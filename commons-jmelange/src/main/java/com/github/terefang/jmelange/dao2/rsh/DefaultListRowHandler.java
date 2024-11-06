package com.github.terefang.jmelange.dao2.rsh;

import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;
import com.github.terefang.jmelange.dao2.DAO;

public class DefaultListRowHandler implements ResultRowProcessor<List<Object>>
{
    public static final DefaultListRowHandler INSTANCE = new DefaultListRowHandler();

    ResultColumnProcessor<Object> columnProcessor = DefaultColumnProcessor.INSTANCE;

    public ResultColumnProcessor<Object> getColumnProcessor() {
        return columnProcessor;
    }

    public void setColumnProcessor(ResultColumnProcessor<Object> columnProcessor) {
        this.columnProcessor = columnProcessor;
    }

    @SneakyThrows
    @Override
    public List<Object> processResultRow(DAO _dao, Statement _st, ResultSet _rs)
    {
        ResultSetMetaData _meta = _rs.getMetaData();
        int _cols = _meta.getColumnCount();
        List<Object> _row = new Vector<>();
        for(int _i=1; _i<=_cols; _i++)
        {
            _row.add(columnProcessor.processResultColumn(_dao, _st, _rs, _i));
        }
        return _row;
    }
}
