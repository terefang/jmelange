package com.github.terefang.jmelange.pdf.ml.cell;

import com.github.terefang.jmelange.pdf.ext.text.Cell;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.List;
import java.util.Properties;

@Builder
@With
@Data
public class TableFormatCell implements AbstractTableCell
{
    int span;
    Properties attributes;
    String source;
    boolean header;
    List<Cell> cells;
    String align;
    String tag;
}