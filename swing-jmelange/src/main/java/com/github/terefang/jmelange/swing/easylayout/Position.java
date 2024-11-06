package com.github.terefang.jmelange.swing.easylayout;

import java.awt.Component;
import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable
{
    public final static int FULL = 1;
    public final static int LEFT = 2;
    public final static int TOP = 2;
    public final static int RIGHT = 3;
    public final static int BOTTOM = 3;
    public final static int CENTER = 4;
    public final static int DEFAULT = -1;
    
    int column;
    int row;
    int columnSpan;
    int rowSpan;
    int xPersentage;
    int yPersentage;
    int widthPersentage;
    int heightPersentage;
    int gridOrigHeight;
    int gridOrigWidth;
    int origHeight;
    int origWidth;
    int origX;
    int origY;
    int hAligment;
    int vAligment;
    int hGap;
    int vGap;
    
    /**
     * @param ai_column
     * @param ai_row
     * @param ai_columnSpan
     * @param ai_rowSpan
     * @param ai_hAligment
     * @param ai_vAligment
     * @param ai_hGap
     * @param ai_vGap
     */
    public Position(int ai_column, int ai_row, int ai_columnSpan, int ai_rowSpan, int ai_hAligment, int ai_vAligment, int ai_hGap, int ai_vGap)
    {
        this.column = ai_column;
        this.row = ai_row;
        this.columnSpan = ai_columnSpan;
        this.rowSpan = ai_rowSpan;
        this.hAligment = ai_hAligment;
        this.vAligment = ai_vAligment;
        this.hGap = ai_hGap;
        this.vGap = ai_vGap;
    }
    
    /**
     * @param ai_column
     * @param ai_row
     * @param ai_columnSpan
     * @param ai_rowSpan
     */
    public Position(int ai_column, int ai_row, int ai_columnSpan, int ai_rowSpan)
    {
        this(ai_column, ai_row, ai_columnSpan, ai_rowSpan, FULL, FULL, DEFAULT, DEFAULT);
    }
    
    /**
     * @param ai_column
     * @param ai_row
     */
    public Position(int ai_column, int ai_row)
    {
        this(ai_column, ai_row, 1, 1, FULL, FULL, DEFAULT, DEFAULT);
    }
    
    /**
     * @param ai_column
     * @param ai_row
     * @param ai_hAligment
     * @param ai_vAligment
     * @param ai_hGap
     * @param ai_vGap
     */
    public Position(int ai_column, int ai_row, int ai_hAligment, int ai_vAligment, int ai_hGap, int ai_vGap)
    {
        this(ai_column, ai_row, 1, 1, ai_hAligment, ai_vAligment, ai_hGap, ai_vGap);
    }
    
    public String toString()
    {
        return this.getClass().getName()
                + "[column="
                + this.column
                + ",row="
                + this.row
                + ",columnSpan="
                + this.columnSpan
                + ",rowSpan="
                + this.rowSpan
                + ",origX="
                + this.origX
                + ",origY="
                + this.origY
                + ",gridOrigWidth="
                + this.gridOrigWidth
                + ",gridOrigHeight="
                + this.gridOrigHeight
                + ",origWidth="
                + this.origWidth
                + ",origHeight="
                + this.origHeight
                + ",xPersentage="
                + this.xPersentage
                + ",yPersentage="
                + this.yPersentage
                + ",widthPersentage="
                + this.widthPersentage
                + ",heightPersentage="
                + this.heightPersentage
                + ",hGap="
                + this.hGap
                + ",vGap="
                + this.vGap
                + "]";
    }
    
    @Override
    public boolean equals(Object _o)
    {
        if (this == _o)
        {
            return true;
        }
        if (_o == null || getClass() != _o.getClass())
        {
            return false;
        }
        Position position = (Position) _o;
        return column == position.column && row == position.row && columnSpan == position.columnSpan &&
                rowSpan == position.rowSpan && xPersentage == position.xPersentage &&
                yPersentage == position.yPersentage &&
                widthPersentage == position.widthPersentage && heightPersentage == position.heightPersentage &&
                gridOrigHeight == position.gridOrigHeight && gridOrigWidth == position.gridOrigWidth &&
                origHeight == position.origHeight && origWidth == position.origWidth && origX == position.origX &&
                origY == position.origY && hAligment == position.hAligment && vAligment == position.vAligment &&
                hGap == position.hGap && vGap == position.vGap;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(column, row, columnSpan, rowSpan, xPersentage, yPersentage, widthPersentage, heightPersentage, gridOrigHeight, gridOrigWidth, origHeight, origWidth, origX, origY, hAligment, vAligment, hGap, vGap);
    }
}
