package com.github.terefang.jmelange.dao2.rsh;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultKvMapHandler extends GenericResultSetPerRowHandler<Map<String, Object>, Map<String, Object>> {

    public static final DefaultKvMapHandler INSTANCE = new DefaultKvMapHandler();

    public DefaultKvMapHandler()
    {
        this.setRowProcessor(DefaultKvRowHandler.INSTANCE);
    }

    @Override
    public Map<String, Object> createList() {
        return new LinkedHashMap<>();
    }

    @Override
    public void addToList(Map<String, Object> _list, Map<String, Object> _row) {
        _list.putAll(_row);
    }
}
