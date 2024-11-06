package com.github.terefang.jmelange.dao2.rsh;

import com.github.terefang.jmelange.dao2.DAO;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultKvRowHandler implements ResultRowProcessor<Map<String,Object>>
{
    public static final DefaultKvRowHandler INSTANCE = new DefaultKvRowHandler();

    ResultColumnProcessor<Object> columnProcessor = DefaultColumnProcessor.INSTANCE;

    public ResultColumnProcessor<Object> getColumnProcessor() {
        return columnProcessor;
    }

    public void setColumnProcessor(ResultColumnProcessor<Object> columnProcessor) {
        this.columnProcessor = columnProcessor;
    }

    @SneakyThrows
    @Override
    public Map<String,Object> processResultRow(DAO _dao, Statement _st, ResultSet _rs)
    {
        Map<String,Object> _map = new LinkedHashMap<>();
        _map.put(columnProcessor.processResultColumn(_dao, _st, _rs, 1).toString(), columnProcessor.processResultColumn(_dao, _st, _rs, 2));
        return _map;
    }
}
