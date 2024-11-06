package com.github.terefang.jmelange.dao2.rsh;

import com.github.terefang.jmelange.dao2.DAO;

import java.sql.ResultSet;
import java.sql.Statement;

public interface ResultPkProcessor<T>
{
    T processResultPK(DAO _dao, Statement _st);
}
