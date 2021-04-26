package com.github.terefang.jmelange.dao.rsh;

import java.sql.SQLException;
import java.util.Map;

public interface ResultRowCallbackHandler<T>
{
    public T handleRow(Map<String,Object> row) throws SQLException;
}
