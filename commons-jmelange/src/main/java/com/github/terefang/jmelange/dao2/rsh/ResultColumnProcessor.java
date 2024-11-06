package com.github.terefang.jmelange.dao2.rsh;

import com.github.terefang.jmelange.dao2.DAO;

import java.sql.ResultSet;
import java.sql.Statement;

public interface ResultColumnProcessor<T>
{
    T processResultColumn(DAO _dao, Statement _st, ResultSet _rs, int _index);
}
