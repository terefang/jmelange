package com.github.terefang.jmelange.dao2.rsh;

import com.github.terefang.jmelange.dao2.DAO;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.Statement;

public abstract class GenericResultRowHandler<L,T> implements ResultRowProcessor<L>
{
    ResultColumnProcessor<T> columnProcessor;

    public ResultColumnProcessor<T> getColumnProcessor() {
        return columnProcessor;
    }

    public void setColumnProcessor(ResultColumnProcessor<T> columnProcessor) {
        this.columnProcessor = columnProcessor;
    }

    @SneakyThrows
    @Override
    public L processResultRow(DAO _dao, Statement _st, ResultSet _rs)
    {
        int _cols = _rs.getMetaData().getColumnCount();
        L _row = createRow(_cols);
        for(int _i=1; _i<=_cols; _i++)
        {
            addToRow(_row, this.columnProcessor.processResultColumn(_dao, _st, _rs, _i), _i);
        }
        return _row;
    }

    public abstract L createRow(int _num);
    public abstract void addToRow(L _list, T _col, int _index);
}
