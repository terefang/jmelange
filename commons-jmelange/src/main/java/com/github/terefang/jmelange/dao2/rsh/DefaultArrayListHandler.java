package com.github.terefang.jmelange.dao2.rsh;

import java.util.Map;

public class DefaultArrayListHandler extends DefaultResultSetPerRowHandler<Object[]>
{
    public static final DefaultArrayListHandler INSTANCE = new DefaultArrayListHandler();

    public DefaultArrayListHandler()
    {
        this.setRowProcessor(DefaultArrayRowHandler.INSTANCE);
    }
}
