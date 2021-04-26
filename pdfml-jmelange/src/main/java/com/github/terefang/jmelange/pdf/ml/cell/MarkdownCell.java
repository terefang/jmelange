package com.github.terefang.jmelange.pdf.ml.cell;

import com.vladsch.flexmark.util.ast.Node;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.Properties;


@Builder
@Data@With
public class MarkdownCell implements AbstractTableCell
{
    int span;
    Properties attributes;
    String source;
    boolean header;
    Node markdown;
    String tag;
}
