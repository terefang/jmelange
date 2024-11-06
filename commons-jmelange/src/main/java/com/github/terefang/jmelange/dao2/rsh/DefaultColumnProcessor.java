package com.github.terefang.jmelange.dao2.rsh;

import com.github.terefang.jmelange.dao2.DAO;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;

public class DefaultColumnProcessor implements ResultColumnProcessor<Object>
{
    public static final DefaultColumnProcessor INSTANCE = new DefaultColumnProcessor();

    boolean mapping = true;

    public boolean isMapping() {
        return mapping;
    }

    public void setMapping(boolean mapping) {
        this.mapping = mapping;
    }

    @Override
    @SneakyThrows
    public Object processResultColumn(DAO _dao, Statement _st, ResultSet _rs, int _index)
    {
        if(this.isMapping())
        {
            // String, Integer, Long, Double, Float, byte[], Boolean
            ResultSetMetaData _meta = _rs.getMetaData();
            int _type = _meta.getColumnType(_index);
            switch(_type)
            {
                case Types.BOOLEAN:
                    return _rs.getBoolean(_index);
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                case Types.NCHAR:
                case Types.NVARCHAR:
                case Types.LONGNVARCHAR:
                    return _rs.getString(_index);
                case Types.BIGINT:
                    return _rs.getLong(_index);
                case Types.INTEGER:
                case Types.SMALLINT:
                case Types.TINYINT:
                case Types.BIT:
                    return _rs.getInt(_index);
                case Types.DECIMAL:
                case Types.NUMERIC:
                case Types.DOUBLE:
                    return _rs.getDouble(_index);
                case Types.FLOAT:
                    return _rs.getFloat(_index);
                case Types.BINARY:
                case Types.VARBINARY:
                case Types.CLOB:
                case Types.BLOB:
                    return _rs.getBytes(_index);
                case Types.DATE:
                    return _rs.getDate(_index).getTime();
                case Types.TIMESTAMP:
                    return _rs.getTimestamp(_index).getTime();
                default:
                    System.err.printf("Unknown SQL Type (%d)\n", _type);
            }
        }
        return _rs.getObject(_index);
    }
}
