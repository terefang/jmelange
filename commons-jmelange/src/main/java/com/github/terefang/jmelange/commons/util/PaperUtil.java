package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.commons.CommonUtil;

import java.util.*;

public class PaperUtil
{
    
    public static enum PointUnit
    {
        PT("Point", "pt", 1.),
        IN("Inch", "in", 72.),
        FT("Foot", "ft", 12.*72.),
        
        MM("Millimeter", "mm", 720./254.),
        CM("Centimeter", "cm", 7200./254.),
        DM("Decimeter", "dm", 72000./254.),
        M("Meter", "m", 720000./254.),
        ;
        
        PointUnit(String _name, String _abbreviation, double _unitPointFactor)
        {
            name = _name;
            abbreviation = _abbreviation;
            unitPointFactor = _unitPointFactor;
        }
        
        private String name;
        private String abbreviation;
        private double unitPointFactor;
        
        public String getName()
        {
            return name;
        }
        
        public String getAbbreviation()
        {
            return abbreviation;
        }
        
        public double getUnitPointFactor()
        {
            return unitPointFactor;
        }
        
        public double convertUnitToPoints(double _u)
        {
            return this.unitPointFactor*_u;
        }

        public double convertPointToUnits(double _u)
        {
            return _u/this.unitPointFactor;
        }
    }

    public interface IPaperSize
    {
        public String getName();
        public String getDescription();
        public int getPointWidth();
        public int getPointHeight();
        public PointUnit getPreferredUnit();
        
        default String configKey()
        {
            return "paper-size-"+CommonUtil.safeName(getName()+"_"+getDescription());
        }
        
        default int[] bbx()
        {
            return new int[] { getPointWidth(),getPointHeight() };
        }
    }
    
    public static enum PaperSize implements IPaperSize
    {
        LETTER      ("Letter", "(8.5 x 11 in)",  612, 792, PointUnit.IN),
        NOTE        ("Note", "(8.5 x 11 in)",  612, 792, PointUnit.IN),
        LEGAL       ("Legal", "(8.5 x 14 in)",  612, 1008, PointUnit.IN),
        EXECUTIVE   ("Executive", "(7.25 x 10.5 in)",  522, 756, PointUnit.IN),
        _6X9        ("6x9", "(6 x 9 in)",  432, 648, PointUnit.IN),
        HALF_LETTER ("Half Letter", "(5.5 x 8.5 in)",  396, 612, PointUnit.IN),
        HALF_EXECUTIVE ("Half Executive", "(5.25 x 7.25 in)",  378, 522, PointUnit.IN),
        A0          ("A0", "(841 x 1189 mm)",  2383, 3370, PointUnit.MM),
        A1          ("A1", "(594 x 841 mm)",  1683, 2383, PointUnit.MM),
        A2          ("A2", "(420 x 594 mm)",  1190, 1683, PointUnit.MM),
        A3          ("A3", "(297 x 420 mm)",  841, 1190, PointUnit.MM),
        A4          ("A4", "(210 x 297 mm)",  595, 841, PointUnit.MM),
        A5          ("A5", "(148 x 210 mm)",  419, 595, PointUnit.MM),
        A6          ("A6", "(105 x 148 mm)",  297, 419, PointUnit.MM),
        A7          ("A7", "(74 x 105 mm)",  209, 297, PointUnit.MM),
        A8          ("A8", "(52 x 74 mm)",  147, 209, PointUnit.MM),
        A9          ("A9", "(37 x 52 mm)",  104, 147, PointUnit.MM),
        A10         ("A10", "(26 x 37 mm)",  73, 104, PointUnit.MM),
        SUPER_A3    ("Super A3", "(330 x 483 mm)",  935, 1369, PointUnit.MM),
        B0          ("B0", "(1000 x 1414 mm)",  2834, 4008, PointUnit.MM),
        B1          ("B1", "(707 x 1000 mm)",  2004, 2834, PointUnit.MM),
        B2          ("B2", "(500 x 707 mm)",  1417, 2004, PointUnit.MM),
        B3          ("B3", "(353 x 500 mm)",  1000, 1417, PointUnit.MM),
        B4          ("B4", "(250 x 353 mm)",  708, 1000, PointUnit.MM),
        B5          ("B5", "(176 x 250 mm)",  498, 708, PointUnit.MM),
        B6          ("B6", "(125 x 176 mm)",  354, 498, PointUnit.MM),
        B7          ("B7", "(88 x 125 mm)",  249, 354, PointUnit.MM),
        B8          ("B8", "(62 x 88 mm)",  175, 249, PointUnit.MM),
        B9          ("B9", "(44 x 62 mm)",  124, 175, PointUnit.MM),
        B10         ("B10", "(31 x 44 mm)",  87, 124, PointUnit.MM),
        C0          ("C0", "(917 x 1297 mm)",  2599, 3676, PointUnit.MM),
        C1          ("C1", "(648 x 917 mm)",  1836, 2599, PointUnit.MM),
        C2          ("C2", "(458 x 648 mm)",  1298, 1836, PointUnit.MM),
        C3          ("C3", "(324 x 458 mm)",  918, 1298, PointUnit.MM),
        C4          ("C4", "(229 x 354 mm)",  649, 1003, PointUnit.MM),
        C5          ("C5", "(162 x 229 mm)",  459, 649, PointUnit.MM),
        C6          ("C6", "(114 x 162 mm)",  323, 459, PointUnit.MM),
        C7          ("C7", "(81 x 114 mm)",  229, 323, PointUnit.MM),
        C8          ("C8", "(57 x 81 mm)",  161, 229, PointUnit.MM),
        C9          ("C9", "(40 x 57 mm)",  113, 161, PointUnit.MM),
        C10         ("C10", "(28 x 40 mm)",  79, 113, PointUnit.MM),
        JIS_B0      ("JIS B0", "(1030 x 1456 mm)",  2919, 4127, PointUnit.MM),
        JIS_B1      ("JIS B1", "(728 x 1030 mm)",  2063, 2919, PointUnit.MM),
        JIS_B2      ("JIS B2", "(515 x 728 mm)",  1459, 2063, PointUnit.MM),
        JIS_B3      ("JIS B3", "(364 x 515 mm)",  1031, 1459, PointUnit.MM),
        JIS_B4      ("JIS B4", "(257 x 364 mm)",  728, 1031, PointUnit.MM),
        JIS_B5      ("JIS B5", "(182 x 257 mm)",  515, 728, PointUnit.MM),
        JIS_B6      ("JIS B6", "(128 x 182 mm)",  362, 515, PointUnit.MM),
        JIS_B7      ("JIS B7", "(91 x 128 mm)",  257, 362, PointUnit.MM),
        JIS_B8      ("JIS B8", "(64 x 91 mm)",  181, 257, PointUnit.MM),
        JIS_B9      ("JIS B9", "(45 x 64 mm)",  127, 181, PointUnit.MM),
        JIS_B10     ("JIS B10", "(32 x 45 mm)",  90, 127, PointUnit.MM),
        _11X17      ("11x17", "(11 x 17 in)",  792, 1224, PointUnit.IN),
        STATEMENT   ("Statement", "(5.5 x 8.5 in)",  396, 612, PointUnit.IN),
        FOLIO       ("Folio", "(8.5 x 13 in)",  612, 936, PointUnit.IN),
        _10X14      ("10x14", "(10 x 14 in)",  720, 1008, PointUnit.IN),
        LEDGER      ("Ledger", "(17 x 11 in)",  1224, 792, PointUnit.IN),
        TABLOID     ("Tabloid", "(11 x 17 in)",  792, 1224, PointUnit.IN),
        DL          ("DL", "(110 x 220 mm)",  311, 623, PointUnit.MM),
        COMM_10     ("Comm 10", "(4.125 x 9.5 in)",  297, 684, PointUnit.IN),
        MONARCH     ("Monarch", "(3.875 x 7.5 in)",  279, 540, PointUnit.IN),
        ARCH_A      ("Arch A", "(9 x 12 in)",  648, 864, PointUnit.IN),
        ARCH_B      ("Arch B", "(12 x 18 in)",  864, 1296, PointUnit.IN),
        ARCH_C      ("Arch C", "(18 x 24 in)",  1296, 1728, PointUnit.IN),
        ARCH_D      ("Arch D", "(24 x 36 in)",  1728, 2592, PointUnit.IN),
        ARCH_E      ("Arch E", "(36 x 48 in)",  2592, 3456, PointUnit.IN),
        ARCH_E1     ("Arch E1", "(30 x 42 in)",  2160, 3024, PointUnit.IN),
        ARCH_E2     ("Arch E2", "(26 x 36 in)",  1872, 2592, PointUnit.IN),
        ARCH_E3     ("Arch E3", "(27 x 39 in)",  1944, 2808, PointUnit.IN),
        ANSI_SHEET_A     ("ANSI A Sheet", "(8.5 x 11 in)",  612, 792, PointUnit.IN),
        ANSI_SHEET_B     ("ANSI B Sheet", "(11 x 17 in)",  792, 1224, PointUnit.IN),
        ANSI_SHEET_C     ("ANSI C Sheet", "(17 x 22 in)",  1224, 1584, PointUnit.IN),
        ANSI_SHEET_D     ("ANSI D Sheet", "(22 x 34 in)",  1584, 2448, PointUnit.IN),
        ANSI_SHEET_E     ("ANSI E Sheet", "(34 x 44 in)",  2448, 3168, PointUnit.IN),
        ANSI_SUPER_B     ("ANSI Super B", "(13 x 19 in)",  936, 1368, PointUnit.IN),
        ;
        
        PaperSize(String _name, String _description, int _pointWidth, int _pointHeight, PointUnit _preferredUnit)
        {
            name = _name;
            description = _description;
            pointWidth = _pointWidth;
            pointHeight = _pointHeight;
            preferredUnit = _preferredUnit;
        }
        
        private String name;
        private String description;
        private int pointWidth;
        private int pointHeight;
        private PointUnit preferredUnit;
        
        public String getName()
        {
            return name;
        }
        
        public String getDescription()
        {
            if(description==null)
            {
                return String.format("(%d x %d pt)", pointWidth, pointHeight);
            }
            
            return description;
        }
        
        public int getPointWidth()
        {
            return pointWidth;
        }
        
        public int getPointHeight()
        {
            return pointHeight;
        }
        
        public PointUnit getPreferredUnit()
        {
            return preferredUnit;
        }
    }
    public static IPaperSize PAPER_SIZE_LETTER = PaperSize.LETTER;
    public static IPaperSize PAPER_SIZE_NOTE = PaperSize.NOTE;
    public static IPaperSize PAPER_SIZE_LEGAL = PaperSize.LEGAL;
    public static IPaperSize PAPER_SIZE_EXECUTIVE = PaperSize.EXECUTIVE;
    public static IPaperSize PAPER_SIZE__6X9 = PaperSize._6X9;
    public static IPaperSize PAPER_SIZE_HALF_LETTER = PaperSize.HALF_LETTER;
    public static IPaperSize PAPER_SIZE_HALF_EXECUTIVE = PaperSize.HALF_EXECUTIVE;
    public static IPaperSize PAPER_SIZE_A0 = PaperSize.A0;
    public static IPaperSize PAPER_SIZE_A1 = PaperSize.A1;
    public static IPaperSize PAPER_SIZE_A2 = PaperSize.A2;
    public static IPaperSize PAPER_SIZE_A3 = PaperSize.A3;
    public static IPaperSize PAPER_SIZE_A4 = PaperSize.A4;
    public static IPaperSize PAPER_SIZE_A5 = PaperSize.A5;
    public static IPaperSize PAPER_SIZE_A6 = PaperSize.A6;
    public static IPaperSize PAPER_SIZE_A7 = PaperSize.A7;
    public static IPaperSize PAPER_SIZE_A8 = PaperSize.A8;
    public static IPaperSize PAPER_SIZE_A9 = PaperSize.A9;
    public static IPaperSize PAPER_SIZE_A10 = PaperSize.A10;
    public static IPaperSize PAPER_SIZE_SUPER_A3 = PaperSize.SUPER_A3;
    public static IPaperSize PAPER_SIZE_B0 = PaperSize.B0;
    public static IPaperSize PAPER_SIZE_B1 = PaperSize.B1;
    public static IPaperSize PAPER_SIZE_B2 = PaperSize.B2;
    public static IPaperSize PAPER_SIZE_B3 = PaperSize.B3;
    public static IPaperSize PAPER_SIZE_B4 = PaperSize.B4;
    public static IPaperSize PAPER_SIZE_B5 = PaperSize.B5;
    public static IPaperSize PAPER_SIZE_B6 = PaperSize.B6;
    public static IPaperSize PAPER_SIZE_B7 = PaperSize.B7;
    public static IPaperSize PAPER_SIZE_B8 = PaperSize.B8;
    public static IPaperSize PAPER_SIZE_B9 = PaperSize.B9;
    public static IPaperSize PAPER_SIZE_B10 = PaperSize.B10;
    public static IPaperSize PAPER_SIZE_C0 = PaperSize.C0;
    public static IPaperSize PAPER_SIZE_C1 = PaperSize.C1;
    public static IPaperSize PAPER_SIZE_C2 = PaperSize.C2;
    public static IPaperSize PAPER_SIZE_C3 = PaperSize.C3;
    public static IPaperSize PAPER_SIZE_C4 = PaperSize.C4;
    public static IPaperSize PAPER_SIZE_C5 = PaperSize.C5;
    public static IPaperSize PAPER_SIZE_C6 = PaperSize.C6;
    public static IPaperSize PAPER_SIZE_C7 = PaperSize.C7;
    public static IPaperSize PAPER_SIZE_C8 = PaperSize.C8;
    public static IPaperSize PAPER_SIZE_C9 = PaperSize.C9;
    public static IPaperSize PAPER_SIZE_C10 = PaperSize.C10;
    public static IPaperSize PAPER_SIZE_JIS_B0 = PaperSize.JIS_B0;
    public static IPaperSize PAPER_SIZE_JIS_B1 = PaperSize.JIS_B1;
    public static IPaperSize PAPER_SIZE_JIS_B2 = PaperSize.JIS_B2;
    public static IPaperSize PAPER_SIZE_JIS_B3 = PaperSize.JIS_B3;
    public static IPaperSize PAPER_SIZE_JIS_B4 = PaperSize.JIS_B4;
    public static IPaperSize PAPER_SIZE_JIS_B5 = PaperSize.JIS_B5;
    public static IPaperSize PAPER_SIZE_JIS_B6 = PaperSize.JIS_B6;
    public static IPaperSize PAPER_SIZE_JIS_B7 = PaperSize.JIS_B7;
    public static IPaperSize PAPER_SIZE_JIS_B8 = PaperSize.JIS_B8;
    public static IPaperSize PAPER_SIZE_JIS_B9 = PaperSize.JIS_B9;
    public static IPaperSize PAPER_SIZE_JIS_B10 = PaperSize.JIS_B10;
    public static IPaperSize PAPER_SIZE__11X17 = PaperSize._11X17;
    public static IPaperSize PAPER_SIZE_STATEMENT = PaperSize.STATEMENT;
    public static IPaperSize PAPER_SIZE_FOLIO = PaperSize.FOLIO;
    public static IPaperSize PAPER_SIZE__10X14 = PaperSize._10X14;
    public static IPaperSize PAPER_SIZE_LEDGER = PaperSize.LEDGER;
    public static IPaperSize PAPER_SIZE_TABLOID = PaperSize.TABLOID;
    public static IPaperSize PAPER_SIZE_DL = PaperSize.DL;
    public static IPaperSize PAPER_SIZE_COMM_10 = PaperSize.COMM_10;
    public static IPaperSize PAPER_SIZE_MONARCH = PaperSize.MONARCH;
    public static IPaperSize PAPER_SIZE_ARCH_A = PaperSize.ARCH_A;
    public static IPaperSize PAPER_SIZE_ARCH_B = PaperSize.ARCH_B;
    public static IPaperSize PAPER_SIZE_ARCH_C = PaperSize.ARCH_C;
    public static IPaperSize PAPER_SIZE_ARCH_D = PaperSize.ARCH_D;
    public static IPaperSize PAPER_SIZE_ARCH_E = PaperSize.ARCH_E;
    public static IPaperSize PAPER_SIZE_ARCH_E1 = PaperSize.ARCH_E1;
    public static IPaperSize PAPER_SIZE_ARCH_E2 = PaperSize.ARCH_E2;
    public static IPaperSize PAPER_SIZE_ARCH_E3 = PaperSize.ARCH_E3;
    public static IPaperSize PAPER_SIZE_ANSI_SHEET_A = PaperSize.ANSI_SHEET_A;
    public static IPaperSize PAPER_SIZE_ANSI_SHEET_B = PaperSize.ANSI_SHEET_B;
    public static IPaperSize PAPER_SIZE_ANSI_SHEET_C = PaperSize.ANSI_SHEET_C;
    public static IPaperSize PAPER_SIZE_ANSI_SHEET_D = PaperSize.ANSI_SHEET_D;
    public static IPaperSize PAPER_SIZE_ANSI_SHEET_E = PaperSize.ANSI_SHEET_E;
    public static IPaperSize PAPER_SIZE_ANSI_SUPER_B = PaperSize.ANSI_SUPER_B;
    
    public static class CustomPaperSize implements IPaperSize
    {
        
        public CustomPaperSize(String _name, String _description, int _pointWidth, int _pointHeight)
        {
            this(_name,_description,_pointWidth,_pointHeight,PointUnit.PT);
        }
        
        public CustomPaperSize(String _name, String _description, int _pointWidth, int _pointHeight, PointUnit _preferredUnit)
        {
            name = _name;
            description = _description;
            pointWidth = _pointWidth;
            pointHeight = _pointHeight;
            preferredUnit = _preferredUnit;
        }
        
        private String name;
        private String description;
        private int pointWidth;
        private int pointHeight;
        private PointUnit preferredUnit;
        
        public String getName()
        {
            return name;
        }
        
        public String getDescription()
        {
            if(description==null)
            {
                return String.format("(%d x %d pt)", pointWidth, pointHeight);
            }
            
            return description;
        }
        
        public int getPointWidth()
        {
            return pointWidth;
        }
        
        public int getPointHeight()
        {
            return pointHeight;
        }
        
        public PointUnit getPreferredUnit()
        {
            return preferredUnit;
        }
    }
    
    public static List<IPaperSize> customPaperSizes()
    {
        return customPaperSizes(OsUtil.getApplicationName());
    }
    
    public static String _CUSTOM_PAPER_SIZES_CFG_KEY = "custom-paper-sizes.ldata";
    
    public static List<IPaperSize> customPaperSizes(String _app)
    {
        Map<String, List>  _data = (Map<String, List>) CfgDataUtil.getConfigAsComplex(_app, _CUSTOM_PAPER_SIZES_CFG_KEY, null);
        if(_data==null)
        {
            return Collections.emptyList();
        }
        
        List<IPaperSize> _ret = new ArrayList<>();
        for(Map.Entry<String, List> _entry : _data.entrySet())
        {
            try
            {
                String _key = _entry.getKey();
                String _descr = Objects.toString(_entry.getValue().get(0));
                int _w = CommonUtil.checkInt(_entry.getValue().get(1));
                int _h = CommonUtil.checkInt(_entry.getValue().get(2));
                _ret.add(new CustomPaperSize(_key,_descr,_w,_h));
            }
            catch(Exception _xe)
            {
                // IGNORE
            }
        }
        return _ret;
    }
    
    public static IPaperSize customPaperSize(String _name, String _desc, int _w, int _h, PointUnit _u)
    {
        return new CustomPaperSize(_name, _desc, _w, _h, _u);
    }
    
    public static IPaperSize customPaperSize(String _name, String _desc, int _w, int _h)
    {
        return new CustomPaperSize(_name, _desc, _w, _h, PointUnit.PT);
    }
    
    public static IPaperSize customPaperSize(String _name, int _w, int _h, PointUnit _u)
    {
        return new CustomPaperSize(_name, null, _w, _h, _u);
    }
    
    public static IPaperSize customPaperSize(String _name, int _w, int _h)
    {
        return new CustomPaperSize(_name, null, _w, _h, PointUnit.PT);
    }
    
    public static List<IPaperSize> paperSizes(boolean _filter)
    {
        List<IPaperSize> _ret = new ArrayList<>();
        for(PaperSize _sz : PaperSize.values())
        {
            if(CfgDataUtil.getConfigAsBoolean(_sz.configKey(), false))
            {
                _ret.add(_sz);
            }
        }
        return _ret;
    }
    
    public static List<IPaperSize> allPaperSizes(boolean _filter)
    {
        List<IPaperSize> _ret = new ArrayList<>();
        _ret.addAll(customPaperSizes());
        for(PaperSize _sz : PaperSize.values())
        {
            if(CfgDataUtil.getConfigAsBoolean(_sz.configKey(), false))
            {
                _ret.add(_sz);
            }
        }
        return _ret;
    }
    
    public static List<PaperSize> paperSizes()
    {
        List<PaperSize> _ret = new ArrayList<>();
        for(PaperSize _sz : PaperSize.values())
        {
            _ret.add(_sz);
        }
        return _ret;
    }
}
