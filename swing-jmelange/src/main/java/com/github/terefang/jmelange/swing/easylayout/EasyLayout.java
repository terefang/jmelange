package com.github.terefang.jmelange.swing.easylayout;

import com.github.terefang.jmelange.swing.SwingHelper;

import javax.swing.JComponent;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


/**
 * EasyLayout
 * <p>
 * Layout manager for awt and swing.
 * Easylayout has designed for creation of professional looking forms
 * easily and quicly.
 * <p>
 * EasyLayout is based on grid.
 *
 * Resizing is based by percentage values of
 * columns and rows of grid. Components are placed to grid cells and they resize
 * according to grid resize rule.
 *
 *
 * <pre>
 *      0      1      2
 *   +--0%--+-100%-+--0%--+
 * 0 0%     |      |      |
 *   +------+------+------+
 * 1 0%     |      |      |
 *   +------+------+------+
 * 2 50%    |      |      |
 *   +------+------+------+
 * 3 50%    |      |      |
 *   +------+------+------+
 * </pre>
 *
 * @author Sampsa Sohlman
 * @version 6.5.2003
 */
public class EasyLayout implements LayoutManager2, java.io.Serializable
{
    /**
     * @see #EasyLayout(int[], int[] , int[] , int[] , int , int )
     */
    public final static int MAXCOMPONENTHEIGHT = -1;
    /**
     * @see #EasyLayout(int[], int[] , int[] , int[] , int , int )
     */
    public final static int MAXCOMPONENTWIDTH = -1;
    
    
    
    private boolean ib_calculationDone = false;
    
    private boolean[] ib_isMaxWidths;
    private boolean[] ib_isMaxHeights;
    
    private int[] ii_rows;
    private int[] ii_columns;
    
    private int[] ii_origColumnWidths;
    private int[] ii_origRowHeights;
    
    private int ii_vGap = 0;
    private int ii_hGap = 0;
    
    private int ii_origWidth;
    private int ii_origHeight;
    
    private int _currRow = 0;
    private int _currCol = 0;
    
    
    private int[] ii_origXPositions;
    private int[] ii_origYPositions;
    
    private Constraint i_Constraint_Default = new Constraint(0, 0, 1, 1);
    
    private Map<Component,Constraint> iVe_Components = new HashMap<>();
    
    public static EasyLayout from(int _c, int _r)
    {
        return new EasyLayout(SwingHelper.toIntArray(_c,100),SwingHelper.toIntArray(_r,100), 0,0);
    }
    
    public static EasyLayout from(int[] _c, int[] _r)
    {
        return new EasyLayout(_c,_r, 0,0);
    }
    
    public static EasyLayout from(int _c, int _r, int _g)
    {
        return new EasyLayout(SwingHelper.toIntArray(_c,100),SwingHelper.toIntArray(_r,100), _g,_g);
    }
    
    public static EasyLayout from(int[] _c, int[] _r, int _g)
    {
        return new EasyLayout(_c,_r, _g,_g);
    }
    
    /**
     * With constructor you defined
     *
     * @param ai_origColumnWidths Array of original Column widths in pixels
     * @param ai_origRowHeights   Array of original Column heights in pixels
     * @param ai_columns          Array of column percentages
     * @param ai_rows             Array of row persce
     * @param ai_hGap             Default horizontal gap between componenents
     * @param ai_vGap             Default vertical gap between componenents
     */
    public EasyLayout(int[] ai_origColumnWidths, int[] ai_origRowHeights, int[] ai_columns, int[] ai_rows, int ai_hGap, int ai_vGap)
    {
        int li_count = 0;
        ii_hGap = ai_hGap;
        ii_vGap = ai_vGap;
        ii_origXPositions = new int[ai_columns.length];
        ii_origYPositions = new int[ai_rows.length];
        ib_isMaxWidths = new boolean[ai_columns.length];
        ib_isMaxHeights = new boolean[ai_rows.length];
        
        for (int li_index = 0; li_index < ai_columns.length; li_index++)
        {
            li_count += ai_columns[li_index];
            
        }
        if (li_count != 100)
        {
            //throw new IllegalArgumentException("Sum of column percentage has to be 100");
            for (int li_index = 0; li_index < ai_columns.length; li_index++)
            {
                ai_columns[li_index]=ai_columns[li_index]*100/li_count;
            }
        }

        li_count = 0;
        for (int li_index = 0; li_index < ai_rows.length; li_index++)
        {
            li_count += ai_rows[li_index];
        }
        if (li_count != 100)
        {
            //throw new IllegalArgumentException("Sum of row percentage has to be 100");
            for (int li_index = 0; li_index < ai_rows.length; li_index++)
            {
                ai_rows[li_index]= ai_rows[li_index]*100/li_count;
            }
        }
        
        ii_rows = ai_rows;
        ii_columns = ai_columns;
        if (ai_origColumnWidths != null)
        {
            ii_origColumnWidths = ai_origColumnWidths;
            for (int li_index = 0; li_index < ii_origColumnWidths.length; li_index++)
            {
                if( ii_origColumnWidths[li_index] == MAXCOMPONENTWIDTH)
                {
                    ib_isMaxWidths[li_index ] = true;
                }
            }
        }
        else
        {
            ii_origColumnWidths = new int[ai_columns.length];
            for (int li_index = 0; li_index < ii_origColumnWidths.length; li_index++)
            {
                ii_origColumnWidths[li_index] = MAXCOMPONENTWIDTH;
                ib_isMaxWidths[li_index ] = true;
            }
        }
        
        if (ai_origRowHeights != null)
        {
            ii_origRowHeights = ai_origRowHeights;
            for (int li_index = 0; li_index < ii_origRowHeights.length; li_index++)
            {
                if( ii_origRowHeights[li_index] == MAXCOMPONENTWIDTH)
                {
                    ib_isMaxHeights[li_index ] = true;
                }
            }
        }
        else
        {
            ii_origRowHeights = new int[ai_rows.length];
            for (int li_index = 0; li_index < ii_origRowHeights.length; li_index++)
            {
                ii_origRowHeights[li_index] = MAXCOMPONENTHEIGHT;
                ib_isMaxHeights[li_index ] = true;
            }
        }
    }
    
    /**
     * Column and row size is maximum of each component.preferredSize()
     * @see #EasyLayout(int[] , int[] , int[] , int[] , int , int )
     *
     * @param ai_columns          Array of column percentages
     * @param ai_rows             Array of row persce
     * @param ai_hGap             Default horizontal gap between componenents
     * @param ai_vGap             Default vertical gap between componenents
     */
    public EasyLayout(int[] ai_columns, int[] ai_rows, int ai_hGap, int ai_vGap)
    {
        this(null, null, ai_columns, ai_rows, ai_hGap, ai_vGap);
    }
    
    /**
     * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component, java.lang.Object)
     */
    public void addLayoutComponent(Component a_Component, Object aO_Constraint)
    {
        //System.out.println("addLayoutComponent Component : " + a_Component.getMinimumSize());
        
        if (aO_Constraint == null)
        {
            aO_Constraint = i_Constraint_Default;
        }
        
        if (aO_Constraint instanceof Constraint)
        {
            Constraint l_Constraint = (Constraint)aO_Constraint;
            if (l_Constraint.hGap == Position.DEFAULT)
            {
                l_Constraint.hGap = ii_hGap;
            }
            if (l_Constraint.vGap == Position.DEFAULT)
            {
                l_Constraint.vGap = ii_vGap;
            }
            
            Dimension l_Dimension = a_Component.getPreferredSize();
            
            l_Constraint.origWidth = l_Dimension.width;
            l_Constraint.origHeight = l_Dimension.height;
            iVe_Components.put(a_Component, l_Constraint);
            ib_calculationDone = false;
            
            //next auto mode for cells
            if(l_Constraint.column==-1 && l_Constraint.row==-1)
            {
                l_Constraint.column=this._currCol;
                l_Constraint.row=this._currRow;

                // shorten span if too wide for same row/col
                if(l_Constraint.column>=this.ii_columns.length) throw  new IllegalArgumentException("origX out of bounds");
                while((l_Constraint.column+l_Constraint.columnSpan)>this.ii_columns.length)
                {
                    l_Constraint.columnSpan--;
                }
                if(l_Constraint.row>=this.ii_rows.length) throw  new IllegalArgumentException("origY out of bounds");
                while((l_Constraint.row+l_Constraint.rowSpan)>this.ii_rows.length)
                {
                    l_Constraint.rowSpan--;
                }
                
                this._currCol = (this._currCol+l_Constraint.columnSpan)%this.ii_columns.length;
                if(this._currCol==0) this._currRow++;
            }
            else
            {
                this._currCol = (l_Constraint.column+l_Constraint.columnSpan)%this.ii_columns.length;
                if(this._currCol==0) this._currRow++;
            }
        }
        else
        {
            throw new IllegalArgumentException("cannot add to layout: constraints must be a Constraint");
        }
        
    }
    
    /**
     * @see java.awt.LayoutManager2#getLayoutAlignmentX(java.awt.Container)
     */
    public float getLayoutAlignmentX(Container a_Component)
    {
        //System.out.println("public float getLayoutAlignmentX(Container a_Component)");
        return 0;
    }
    
    /**
     * @see java.awt.LayoutManager2#getLayoutAlignmentY(java.awt.Container)
     */
    public float getLayoutAlignmentY(Container a_Container)
    {
        //System.out.println("public float getLayoutAlignmentY(Container a_Container)");
        return 0;
    }
    
    /**
     * @see java.awt.LayoutManager2#invalidateLayout(java.awt.Container)
     */
    public void invalidateLayout(Container a_Container)
    {
        //System.out.println("public void invalidateLayout(Container a_Container)");
        calculate(a_Container);
    }
    
    /**
     * @see java.awt.LayoutManager2#maximumLayoutSize(java.awt.Container)
     */
    public Dimension maximumLayoutSize(Container a_Container)
    {
        //System.out.println("public Dimension maximumLayoutSize(Container a_Container)");
        calculate(a_Container);
        return new Dimension(this.ii_origWidth,ii_origHeight);
    }
    
    /**
     * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
     */
    public void addLayoutComponent(String a_String, Component a_Component)
    {
        //System.out.println("public void addLayoutComponent(String a_String, Component a_Component)");
    }
    
    /**
     * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
     */
    public void layoutContainer(Container a_Container)
    {
        calculate(a_Container);
        synchronized (a_Container.getTreeLock())
        {
            Dimension l_Dimension = a_Container.getSize();
            
            int li_xAdd = l_Dimension.width - ii_origWidth;
            int li_yAdd = l_Dimension.height - ii_origHeight;
            
            for (int li_index = 0; li_index < a_Container.getComponentCount(); li_index++)
            {
                Component l_Component = a_Container.getComponent(li_index);
                Constraint l_Constraint = iVe_Components.get(l_Component);
                layoutComponent(l_Component, l_Constraint, li_xAdd, li_yAdd);
            }
        }
    }
    
    /**
     * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
     */
    public Dimension minimumLayoutSize(Container a_Container)
    {
        calculate(a_Container);
        synchronized (a_Container.getTreeLock())
        {
            //System.out.println("public Dimension minimumLayoutSize(Container a_Container)");
            return new Dimension(ii_origWidth, ii_origHeight);
        }
    }
    
    /**
     * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
     */
    public Dimension preferredLayoutSize(Container a_Container)
    {
        calculate(a_Container);
        synchronized (a_Container.getTreeLock())
        {
            //System.out.println("public Dimension preferredLayoutSize(Container a_Container)");
            return new Dimension(ii_origWidth, ii_origHeight);
        }
        
    }
    
    /**
     * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
     */
    public void removeLayoutComponent(Component a_Component)
    {
        //System.out.println("public void removeLayoutComponent(Component a_Component)");
        iVe_Components.remove(a_Component);
        ib_calculationDone = false;
        
    }
    
    private void calculate(Container a_Container)
    {
        if (!ib_calculationDone)
        {
            // Calculate Box sizes in Grid
            int[] li_columnPercentages = new int[ii_columns.length];
            int[] li_rowPercentages = new int[ii_rows.length];
            
            for (int li_index = 0; li_index < iVe_Components.size(); li_index++)
            {
                Component l_Component = a_Container.getComponent(li_index);
                Constraint l_Constraint = iVe_Components.get(l_Component);
                
                if (ib_isMaxWidths[l_Constraint.column] && l_Constraint.columnSpan == 1)
                {
                    
                    if (ii_origColumnWidths[l_Constraint.column] < l_Constraint.origWidth)
                    {
                        ii_origColumnWidths[l_Constraint.column] = l_Constraint.origWidth + (2 * l_Constraint.hGap);
                    }
                }
                if (ib_isMaxHeights[l_Constraint.row] &&  l_Constraint.rowSpan == 1)
                {
                    if (ii_origRowHeights[l_Constraint.row] < l_Constraint.origHeight)
                    {
                        ii_origRowHeights[l_Constraint.row] = l_Constraint.origHeight + (2 * l_Constraint.vGap);
                    }
                }
            }
            
            // Calculate original width and height
            
            int li_tmpC = 0;
            int li_tmpP = 0;
            if((a_Container instanceof JComponent) && ((JComponent)a_Container).getBorder()!=null)
            {
                Border _b = ((JComponent) a_Container).getBorder();
                Insets _i = _b.getBorderInsets(a_Container);
                if(_i!=null)
                {
                    li_tmpC += _i.left;
                }
            }
            for (int li_index = 0; li_index < ii_origColumnWidths.length; li_index++)
            {
                li_tmpP += ii_columns[li_index];
                li_columnPercentages[li_index] = li_tmpP;
                ii_origXPositions[li_index] = li_tmpC;
                li_tmpC += ii_origColumnWidths[li_index];
            }
            if((a_Container instanceof JComponent) && ((JComponent)a_Container).getBorder()!=null)
            {
                Border _b = ((JComponent) a_Container).getBorder();
                Insets _i = _b.getBorderInsets(a_Container);
                if(_i!=null)
                {
                    li_tmpC += _i.right;
                }
            }
            ii_origWidth = li_tmpC;
            li_tmpC = 0;
            li_tmpP = 0;
            if((a_Container instanceof JComponent) && ((JComponent)a_Container).getBorder()!=null)
            {
                Border _b = ((JComponent) a_Container).getBorder();
                Insets _i = _b.getBorderInsets(a_Container);
                if(_i!=null)
                {
                    li_tmpC += _i.top;
                }
            }
            for (int li_index = 0; li_index < ii_origRowHeights.length; li_index++)
            {
                li_tmpP += ii_rows[li_index];
                li_rowPercentages[li_index] = li_tmpP;
                ii_origYPositions[li_index] = li_tmpC;
                li_tmpC += ii_origRowHeights[li_index];
            }
            if((a_Container instanceof JComponent) && ((JComponent)a_Container).getBorder()!=null)
            {
                Border _b = ((JComponent) a_Container).getBorder();
                Insets _i = _b.getBorderInsets(a_Container);
                if(_i!=null)
                {
                    li_tmpC += _i.bottom;
                }
            }
            ii_origHeight = li_tmpC;
            //System.out.println(" OrigSize " + ii_origWidth + "," + ii_origHeight);
            
            // Calculate Resize Percentages to all components
            
            for (int li_index = 0; li_index < iVe_Components.size(); li_index++)
            {
                Component l_Component = a_Container.getComponent(li_index);
                Constraint l_Constraint = iVe_Components.get(l_Component);

                l_Constraint.origX = ii_origXPositions[l_Constraint.column];
                l_Constraint.origY = ii_origYPositions[l_Constraint.row];
                if (l_Constraint.column > 0)
                {
                    l_Constraint.xPersentage = li_columnPercentages[l_Constraint.column - 1];
                }
                if (l_Constraint.row > 0)
                {
                    l_Constraint.yPersentage = li_rowPercentages[l_Constraint.row - 1];
                }
                
                l_Constraint.gridOrigWidth = 0;
                l_Constraint.widthPersentage = 0;
                
                for (int li_i = l_Constraint.column; li_i < l_Constraint.column + l_Constraint.columnSpan; li_i++)
                {
                    l_Constraint.widthPersentage += ii_columns[li_i];
                    l_Constraint.gridOrigWidth += ii_origColumnWidths[li_i];
                    
                }
                
                l_Constraint.heightPersentage = 0;
                l_Constraint.gridOrigHeight = 0;
                
                for (int li_i = l_Constraint.row; li_i < l_Constraint.row + l_Constraint.rowSpan; li_i++)
                {
                    l_Constraint.heightPersentage += ii_rows[li_i];
                    l_Constraint.gridOrigHeight += ii_origRowHeights[li_i];
                    
                }
                
            }
            ib_calculationDone = true;
        }
    }
    
    private void layoutComponent(Component a_Component, Position a_Constraint, int ai_xAdd, int ai_yAdd)
    {
        int li_newX = a_Constraint.origX;
        int li_newY = a_Constraint.origY;
        int li_newWidth = a_Constraint.gridOrigWidth;
        int li_newHeight = a_Constraint.gridOrigHeight;
        
        if (a_Constraint.xPersentage > 0)
        {
            li_newX = a_Constraint.origX + (ai_xAdd * a_Constraint.xPersentage) / 100;
        }
        if (a_Constraint.yPersentage > 0)
        {
            li_newY = a_Constraint.origY + (ai_yAdd * a_Constraint.yPersentage) / 100;
        }
        
        if (a_Constraint.widthPersentage > 0)
        {
            li_newWidth = a_Constraint.gridOrigWidth + (ai_xAdd * a_Constraint.widthPersentage) / 100;
        }
        
        if (a_Constraint.heightPersentage > 0)
        {
            li_newHeight = a_Constraint.gridOrigHeight + (ai_yAdd * a_Constraint.heightPersentage) / 100;
        }
        
        li_newX += a_Constraint.hGap;
        li_newY += a_Constraint.vGap;
        li_newWidth -= (2 * a_Constraint.hGap);
        li_newHeight -= (2 * a_Constraint.vGap);
        
        switch (a_Constraint.hAligment)
        {
            case Position.LEFT :
                if (li_newWidth > a_Constraint.origWidth)
                {
                    li_newWidth = a_Constraint.origWidth;
                }
                break;
            case Position.RIGHT :
                if (li_newWidth > a_Constraint.origWidth)
                {
                    li_newX = li_newX + li_newWidth - a_Constraint.origWidth;
                    li_newWidth = a_Constraint.origWidth;
                }
                break;
            case Position.CENTER :
                if (li_newWidth > a_Constraint.origWidth)
                {
                    li_newX = li_newX + (li_newWidth - a_Constraint.origWidth) / 2;
                    li_newWidth = a_Constraint.origWidth;
                }
                break;
            default : // Constraint.FULL
        }
        
        switch (a_Constraint.vAligment)
        {
            case Position.TOP :
                if (li_newHeight > a_Constraint.origHeight)
                {
                    li_newHeight = a_Constraint.origHeight;
                }
                break;
            case Position.BOTTOM :
                if (li_newHeight > a_Constraint.origHeight)
                {
                    li_newY = li_newY + li_newHeight - a_Constraint.origHeight;
                    li_newHeight = a_Constraint.origHeight;
                }
                break;
            case Position.CENTER :
                if (li_newHeight > a_Constraint.origHeight)
                {
                    li_newY = li_newY + (li_newHeight - a_Constraint.origHeight) / 2;
                    li_newHeight = a_Constraint.origHeight;
                }
                break;
            default : // Constraint.FULL
        }
        a_Component.setBounds(li_newX, li_newY, li_newWidth, li_newHeight);
    }
}