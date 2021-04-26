package com.github.terefang.jmelange.dao.rsh;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class KvMapHandler implements ResultSetHandler<Map<String,String>>
{
    BasicRowProcessor basicRowProcessor = new BasicRowProcessor();

    public KvMapHandler() { super(); }

    public Map<String, String> handle(ResultSet rs)
            throws SQLException
    {
        Map<String, String> ret = new LinkedHashMap<String, String>();
        while(rs.next())
        {
            Object[] row = basicRowProcessor.toArray(rs);
            if(row.length>=2)
            {
                ret.put(String.valueOf(row[0]), String.valueOf(row[1]));
            }
        }
        return ret;
    }
}