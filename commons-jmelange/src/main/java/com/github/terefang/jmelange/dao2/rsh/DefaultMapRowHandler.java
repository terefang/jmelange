package com.github.terefang.jmelange.dao2.rsh;

import com.github.terefang.jmelange.commons.lang.CaseInsensitiveHashMap;
import lombok.SneakyThrows;
import com.github.terefang.jmelange.dao2.DAO;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Map;

public class DefaultMapRowHandler implements ResultRowProcessor<Map<String, Object>>
{
    public static final DefaultMapRowHandler INSTANCE = new DefaultMapRowHandler();

    ResultColumnProcessor<Object> columnProcessor = DefaultColumnProcessor.INSTANCE;

    public ResultColumnProcessor<Object> getColumnProcessor() {
        return columnProcessor;
    }

    public void setColumnProcessor(ResultColumnProcessor<Object> columnProcessor) {
        this.columnProcessor = columnProcessor;
    }

    @SneakyThrows
    @Override
    public Map<String, Object> processResultRow(DAO _dao, Statement _st, ResultSet _rs)
    {
        ResultSetMetaData _meta = _rs.getMetaData();
        int _cols = _meta.getColumnCount();
        Map<String, Object> _row = CaseInsensitiveHashMap.create();
        for(int _i=1; _i<=_cols; _i++)
        {
            String _k = _meta.getColumnLabel(_i);
            if(_k==null)
            {
                _k = _meta.getColumnName(_i);
            }
            _row.put(_k, columnProcessor.processResultColumn(_dao, _st, _rs, _i));
        }
        return _row;
    }
}
