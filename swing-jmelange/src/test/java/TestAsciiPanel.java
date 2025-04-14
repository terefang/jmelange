import com.github.terefang.jmelange.commons.gfx.bgi.BmpFont;
import com.github.terefang.jmelange.fonts.BDF;
import com.github.terefang.jmelange.swing.SwingHelper;
import com.github.terefang.jmelange.swing.widgets.ascii.AsciiFont;
import com.github.terefang.jmelange.swing.widgets.ascii.AsciiPanel;

import javax.swing.JFrame;
import java.awt.Color;
import java.io.File;

public class TestAsciiPanel extends JFrame
{
    public static void main(String[] args)
    {
        TestAsciiPanel _t = new TestAsciiPanel();
        _t.setDefaultCloseOperation(EXIT_ON_CLOSE);
        _t.setSize(900,600);
        AsciiPanel _p = new AsciiPanel(192, 50);
        //AsciiFont _f = AsciiFont.of(SwingHelper.createEditFont(32f),20,32);
        //AsciiFont _f = (AsciiFont.TALRYTH_15_15);
        //AsciiFont _f = (AsciiFont.of((BmpFont)BmpFont.defaultC8x16()));
        //AsciiFont _f = (AsciiFont.of(BDF.load(BDF.FONT_SPLEEN_12_X_24)));
        AsciiFont _f = (AsciiFont.of(BDF.load(new File("/u/fredo/IdeaProjects/font-collection/src/main/resources/lucy/tewi-medium-11.bdf"))));
        _p.setAsciiFont(_f);
        _p.setCharScale(2);
        _t.add(_p);
        _t.pack();
        _t.setVisible(true);
        _p.write("**** BASIC c64 ****", Color.YELLOW);
        _p.setCursorPosition(0,_p.getCursorY()+1);
        for(int _i = 0x0000; _i<0xffff; _i++)
        {
            if(_f.isGlyph((char) _i))
            {
                _p.write(Character.toString(_i), Color.WHITE, Color.BLUE);
                if(_p.getCursorX()%128==0) _p.setCursorPosition(0,_p.getCursorY()+1);
            }
        }
    }
}
