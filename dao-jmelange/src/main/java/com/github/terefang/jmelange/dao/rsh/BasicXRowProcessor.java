package com.github.terefang.jmelange.dao.rsh;

import org.apache.commons.dbutils.BasicRowProcessor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class BasicXRowProcessor extends BasicRowProcessor
{

    public BasicXRowProcessor() { super(); }

    @Override
    public Map<String,Object> toMap(ResultSet rs) throws SQLException
    {
        Map result = new CaseInsensitiveHashMap();
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        for (int i = 1; i <= cols; i++) {
            if(rsmd.getColumnLabel(i)!=null)
            {
                result.put(rsmd.getColumnLabel(i), rs.getObject(i));
            }
            else
            {
                result.put(rsmd.getColumnName(i), rs.getObject(i));
            }
        }

        return result;
    }

    private static class CaseInsensitiveHashMap extends LinkedHashMap
    {
        /**
         * @see Map#containsKey(Object)
         */
        public boolean containsKey(Object key)
        {
            return super.containsKey(key.toString().toLowerCase());
        }

        /**
         * @see Map#get(Object)
         */
        public Object get(Object key)
        {
            return super.get(key.toString().toLowerCase());
        }

        /**
         * @see Map#put(Object, Object)
         */
        public Object put(Object key, Object value)
        {
            return super.put(key.toString().toLowerCase(), value);
        }

        /**
         * @see Map#putAll(Map)
         */
        public void putAll(Map m)
        {
            Iterator iter = m.keySet().iterator();
            while (iter.hasNext())
            {
                Object key = iter.next();
                Object value = m.get(key);
                this.put(key, value);
            }
        }

        /**
         * @see Map#remove(Object)
         */
        public Object remove(Object key)
        {
            return super.remove(key.toString().toLowerCase());
        }
    }
}
