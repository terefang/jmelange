package com.github.terefang.jmelange.dao2.rsh;

import com.github.terefang.jmelange.dao2.DAO;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.Statement;

public class DefaultScalarRowHandler implements ResultRowProcessor<Object>
{
    public static final DefaultScalarRowHandler INSTANCE = new DefaultScalarRowHandler();

    @SneakyThrows
    @Override
    public Object processResultRow(DAO _dao, Statement _st, ResultSet _rs)
    {
        return DefaultColumnProcessor.INSTANCE.processResultColumn(_dao, _st, _rs, 1);
    }
}
