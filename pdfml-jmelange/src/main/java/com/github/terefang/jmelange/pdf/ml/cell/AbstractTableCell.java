package com.github.terefang.jmelange.pdf.ml.cell;

import java.util.Properties;

public interface AbstractTableCell
{
    Properties getAttributes();
    boolean isHeader();
    int getSpan();
    String getTag();
    String getSource();
}
