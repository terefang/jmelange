package com.github.terefang.jmelange.dao.rsh;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultCallbackHandler<T> implements ResultSetHandler<List<T>>
{
    ResultRowCallbackHandler<T> rowCallback = null;
    RowProcessor rowProcessor = new BasicXRowProcessor();

    public ResultCallbackHandler(ResultRowCallbackHandler<T> rowCallback)
    {
        this.rowCallback = rowCallback;
    }

    public ResultCallbackHandler(ResultRowCallbackHandler<T> rowCallback, RowProcessor rowProcessor)
    {
        this.rowCallback = rowCallback;
        this.rowProcessor = rowProcessor;
    }

    @Override
    public List<T> handle(ResultSet rs) throws SQLException
    {
        ArrayList rows = new ArrayList();

        while(rs.next())
        {
            T row = this.rowCallback.handleRow(rowProcessor.toMap(rs));
            if(row!=null)
            {
                rows.add(row);
            }
        }

        return rows;
    }
}
