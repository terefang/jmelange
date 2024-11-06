package com.github.terefang.jmelange.swing.easylayout;

public class Constraint extends Position
{
    public enum Alignment
    {
        N, NC, NF,
        S, SC, SF,
        E, EC, EF,
        W, WC, WF,
        NE, NW,
        SE, SW,
        C, CC,
        F, FF,
        CF, FC
    }
    
    public static final Alignment ALIGN_N = Alignment.N;
    public static final Alignment ALIGN_NC = Alignment.NC;
    public static final Alignment ALIGN_NF = Alignment.NF;
    
    public static final Alignment ALIGN_S = Alignment.S;
    public static final Alignment ALIGN_SC = Alignment.SC;
    public static final Alignment ALIGN_SF = Alignment.SF;
    
    public static final Alignment ALIGN_E = Alignment.E;
    public static final Alignment ALIGN_EC = Alignment.EC;
    public static final Alignment ALIGN_EF = Alignment.EF;
    
    public static final Alignment ALIGN_W = Alignment.W;
    public static final Alignment ALIGN_WC = Alignment.WC;
    public static final Alignment ALIGN_WF = Alignment.WF;
    
    public static final Alignment ALIGN_NE = Alignment.NE;
    public static final Alignment ALIGN_NW = Alignment.NW;
    public static final Alignment ALIGN_SE = Alignment.SE;
    public static final Alignment ALIGN_SW = Alignment.SW;
    
    public static final Alignment ALIGN_CC = Alignment.CC;
    public static final Alignment ALIGN_FF = Alignment.FF;
    public static final Alignment ALIGN_CF = Alignment.CF;
    public static final Alignment ALIGN_FC = Alignment.FC;
    
    public static final Alignment ALIGN_VF = Alignment.CF;
    public static final Alignment ALIGN_HF = Alignment.FC;
    
    public static final Alignment ALIGN_TOP_CENTER = Alignment.NC;
    public static final Alignment ALIGN_TOP_LEFT = Alignment.NW;
    public static final Alignment ALIGN_TOP_RIGHT = Alignment.NE;
    public static final Alignment ALIGN_TOP_FILL = Alignment.NF;
    public static final Alignment ALIGN_BOTTOM_CENTER = Alignment.SC;
    public static final Alignment ALIGN_BOTTOM_LEFT = Alignment.SW;
    public static final Alignment ALIGN_BOTTOM_RIGHT = Alignment.SE;
    public static final Alignment ALIGN_BOTTOM_FILL = Alignment.SF;
    
    public static final Alignment ALIGN_MID_CENTER = Alignment.CC;
    public static final Alignment ALIGN_MID_LEFT = Alignment.WC;
    public static final Alignment ALIGN_MID_RIGHT = Alignment.EC;
    public static final Alignment ALIGN_MID_FILL = Alignment.FC;
    
    public static Constraint next(int _cspan)
    {
        return from(-1, -1, _cspan, 1, ALIGN_CC, 0, 0);
    }
    
    public static Constraint next(Alignment _align)
    {
        return from(-1, -1, 1, 1, _align, 0, 0);
    }
    public static Constraint next(int _cspan, Alignment _align)
    {
        return from(-1, -1, _cspan, 1, _align, 0, 0);
    }
    public static Constraint next()
    {
        return from(-1, -1, 1, 1, ALIGN_CC, 0, 0);
    }

    public static Constraint from(int ai_column, int ai_row)
    {
        return from(ai_column, ai_row, 1, 1, ALIGN_CC, 0, 0);
    }

    public static Constraint from(int ai_column, int ai_row, int _cspan)
    {
        return from(ai_column, ai_row, _cspan, 1, ALIGN_CC, 0, 0);
    }
    
    public static Constraint from(int ai_column, int ai_row, Alignment _align)
    {
        return from(ai_column, ai_row, 1, 1, _align, 0, 0);
    }
    
    public static Constraint from(int ai_column, int ai_row, int ai_columnSpan, Alignment _align)
    {
        return from(ai_column, ai_row, ai_columnSpan, 1, _align, 0, 0);
    }
    
    public static Constraint from(int ai_column, int ai_row, Alignment _align, int _gap)
    {
        return from(ai_column, ai_row, 1, 1, _align, _gap, _gap);
    }
    
    public static Constraint from(int ai_column, int ai_row, int ai_columnSpan, Alignment _align, int _hgap, int _vgap)
    {
        return from(ai_column, ai_row, ai_columnSpan, 1, _align, _hgap, _vgap);
    }
    
    public static Constraint from(int ai_column, int ai_row, int ai_columnSpan, Alignment _align, int _gap)
    {
        return from(ai_column, ai_row, ai_columnSpan, 1, _align, _gap, _gap);
    }
    
    public static Constraint from(int ai_column, int ai_row, int ai_columnSpan, int ai_rowSpan, Alignment _align, int _gap)
    {
        return from(ai_column, ai_row, ai_columnSpan, ai_rowSpan, _align, _gap, _gap);
    }
    
    public static Constraint from(int ai_column, int ai_row, int ai_columnSpan, int ai_rowSpan, Alignment _align)
    {
        return from(ai_column, ai_row, ai_columnSpan, ai_rowSpan, _align, 0, 0);
    }
    
    public static Constraint from(int ai_column, int ai_row, int ai_columnSpan, int ai_rowSpan, Alignment _align, int _hgap, int _vgap)
    {
        switch(_align)
        {
            case N:
            case NC:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.CENTER, Constraint.TOP, _hgap,_vgap);
            case NF:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.FULL, Constraint.TOP, _hgap,_vgap);
            case NE:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.RIGHT, Constraint.TOP, _hgap,_vgap);
            case NW:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.LEFT, Constraint.TOP, _hgap,_vgap);
            case S:
            case SC:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.CENTER, Constraint.BOTTOM, _hgap,_vgap);
            case SF:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.FULL, Constraint.BOTTOM, _hgap,_vgap);
            case SE:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.RIGHT, Constraint.BOTTOM, _hgap,_vgap);
            case SW:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.LEFT, Constraint.BOTTOM, _hgap,_vgap);
            case E:
            case EC:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.RIGHT, Constraint.CENTER, _hgap,_vgap);
            case EF:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.RIGHT, Constraint.FULL, _hgap,_vgap);
            case W:
            case WC:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.LEFT, Constraint.CENTER, _hgap,_vgap);
            case WF:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.LEFT, Constraint.FULL, _hgap,_vgap);
            case CF:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.CENTER, Constraint.FULL, _hgap,_vgap);
            case FC:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.FULL, Constraint.CENTER, _hgap,_vgap);
            case F:
            case FF:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.FULL, Constraint.FULL, _hgap,_vgap);
            case C:
            case CC:
            default:
                return new Constraint(ai_column, ai_row, ai_columnSpan, ai_rowSpan, Constraint.CENTER, Constraint.CENTER, _hgap,_vgap);
        }
    }
    
    public Constraint(int ai_column, int ai_row, int ai_columnSpan, int ai_rowSpan, int ai_hAligment, int ai_vAligment, int ai_hGap, int ai_vGap)
    {
        super(ai_column, ai_row, ai_columnSpan, ai_rowSpan, ai_hAligment, ai_vAligment, ai_hGap, ai_vGap);
    }
    
    public Constraint(int ai_column, int ai_row, int ai_columnSpan, int ai_rowSpan)
    {
        super(ai_column, ai_row, ai_columnSpan, ai_rowSpan);
        
    }
    
    public Constraint(int ai_column, int ai_row)
    {
        super(ai_column, ai_row);
    }
    
    public Constraint(int ai_column, int ai_row, int ai_hAligment, int ai_vAligment, int ai_hGap, int ai_vGap)
    {
        super(ai_column, ai_row, ai_hAligment, ai_vAligment, ai_hGap, ai_vGap);
    }
    
}