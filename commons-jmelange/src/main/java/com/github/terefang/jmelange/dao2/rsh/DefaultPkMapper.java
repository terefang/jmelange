package com.github.terefang.jmelange.dao2.rsh;

import com.github.terefang.jmelange.dao2.DAO;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.Statement;

public class DefaultPkMapper implements ResultPkProcessor<String>
{
    public static final DefaultPkMapper INSTANCE = new DefaultPkMapper();

    @Override
    @SneakyThrows
    public String processResultPK(DAO _dao, Statement _statement)
    {
        try (ResultSet _keys = _statement.getGeneratedKeys())
        {
            _keys.next();
            return _keys.getString(1);
        }
    }
}
