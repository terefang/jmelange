import com.github.terefang.jmelange.fonts.sfnt.SfntUtil;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.Font;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.Tag;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.CMap;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.CMapTable;
import lombok.SneakyThrows;

import java.util.Iterator;

public class TestSfnt
{
    @SneakyThrows
    public static void main(String[] args)
    {
        Font _fnt0 = SfntUtil.loadFont("/u/fredo/IdeaProjects/pandoc-typst-setup-template/fonts/noto/NotoSansSymbols-Regular.otf");
        CMapTable _ct0 = _fnt0.getTable(Tag.cmap);
        

        Font _fnt1 = SfntUtil.loadFont("/u/fredo/IdeaProjects/pandoc-typst-setup-template/fonts/noto/NotoSansSymbols2-Regular.otf");
        CMapTable _ct1 = _fnt1.getTable(Tag.cmap);

        for(int _i = 0; _i<0x1fffff; _i++)
        {
            int _g = 0;
            
            Iterator<CMap> _cmi = _ct0.iterator();
            while(_cmi.hasNext() && _g == 0)
            {
                _g = _cmi.next().glyphId(_i);
            }
            
            if(_g>0)
            {
                String _name = Character.getName(_i);
                if(_name==null) _name = String.format("unicode-%04x",_i);
                _name = _name.toLowerCase().replaceAll(" ", "-");
                System.out.printf("#let nsy-%s-g = text(font:\"Noto Sans Symbols\",str.from-unicode(0x%04x));\n", _name, _i);
            }
            else
            {
                _cmi = _ct1.iterator();
                while(_cmi.hasNext() && _g == 0)
                {
                    _g = _cmi.next().glyphId(_i);
                }
                
                if(_g>0)
                {
                    String _name = Character.getName(_i);
                    if(_name==null) _name = String.format("unicode-%04x",_i);
                    _name = _name.toLowerCase().replaceAll(" ", "-");
                    System.out.printf("#let nsy-%s-g = text(font:\"Noto Sans Symbols 2\",str.from-unicode(0x%04x));\n", _name, _i);
                }
            }
        }
        
    }
}
