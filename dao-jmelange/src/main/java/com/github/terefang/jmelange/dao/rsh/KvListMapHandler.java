package com.github.terefang.jmelange.dao.rsh;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class KvListMapHandler implements ResultSetHandler<Map<String, List<String>>>
{
    BasicRowProcessor basicRowProcessor = new BasicRowProcessor();

    public KvListMapHandler() { super(); }

    public Map<String, List<String>> handle(ResultSet rs)
            throws SQLException
    {
        Map<String, List<String>> ret = new LinkedHashMap<String, List<String>>();
        while(rs.next())
        {
            Object[] row = basicRowProcessor.toArray(rs);
            if(row.length>=2)
            {
                String key = String.valueOf(row[0]);
                if(!ret.containsKey(key))
                {
                    ret.put(key, new Vector<String>());
                }
                ret.get(key).add(String.valueOf(row[1]));
            }
        }
        return ret;
    }
}