package com.github.terefang.jmelange.dao2.rsh;

import java.sql.ResultSet;
import java.sql.Statement;
import com.github.terefang.jmelange.dao2.DAO;

public interface ResultSetProcessor<T>
{
    T processResultSet(DAO _dao, Statement _st, ResultSet _rs);
}
