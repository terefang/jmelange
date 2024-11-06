package com.github.terefang.jmelange.dao2.rsh;

import com.github.terefang.jmelange.dao2.DAO;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public class DefaultMapHandler implements ResultRowProcessor<Map<String, Object>>
{
    public static DefaultMapHandler INSTANCE = new DefaultMapHandler();

    @SneakyThrows
    @Override
    public Map<String, Object> processResultRow(DAO _dao, Statement _st, ResultSet _rs)
    {
        _rs.next();
        return DefaultMapRowHandler.INSTANCE.processResultRow(_dao, _st, _rs);
    }
}
