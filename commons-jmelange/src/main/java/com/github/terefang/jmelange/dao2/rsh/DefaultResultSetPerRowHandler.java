package com.github.terefang.jmelange.dao2.rsh;

import java.util.List;
import java.util.Vector;

public class DefaultResultSetPerRowHandler<T> extends GenericResultSetPerRowHandler<List<T>,T>
{
    @Override
    public List<T> createList() {
        return new Vector<>();
    }

    @Override
    public void addToList(List<T> _list, T _row) {
        _list.add(_row);
    }
}
