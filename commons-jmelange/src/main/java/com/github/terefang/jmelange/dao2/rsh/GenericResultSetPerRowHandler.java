package com.github.terefang.jmelange.dao2.rsh;

import lombok.SneakyThrows;
import com.github.terefang.jmelange.dao2.DAO;

import java.sql.ResultSet;
import java.sql.Statement;

public abstract class GenericResultSetPerRowHandler<L,T> implements ResultSetProcessor<L>
{
    ResultRowProcessor<T> rowProcessor;

    public ResultRowProcessor<T> getRowProcessor() {
        return rowProcessor;
    }

    public void setRowProcessor(ResultRowProcessor<T> rowProcessor) {
        this.rowProcessor = rowProcessor;
    }

    @SneakyThrows
    @Override
    public L processResultSet(DAO _dao, Statement _st, ResultSet _rs)
    {
        L _list = createList();
        while(_rs.next())
        {
            addToList(_list, this.rowProcessor.processResultRow(_dao, _st, _rs));
        }
        return _list;
    }

    public abstract L createList();
    public abstract void addToList(L _list, T _row);
}
