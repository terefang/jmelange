import com.github.terefang.jmelange.gfx.BgInterface;
import com.github.terefang.jmelange.gfx.GfxFont;
import com.github.terefang.jmelange.gfx.impl.BmpFont;
import com.github.terefang.jmelange.gfx.impl.PixelImage;
import lombok.SneakyThrows;

import java.io.File;

public class TestBGI
{
    @SneakyThrows
    public static void main(String[] args)
    {
        PixelImage _px = PixelImage.create(256,256);
        BgInterface _bgi = _px.bgi();
        int _f = _bgi.installuserfont(BgInterface.FONT_7x12);
        int _c = 0;

        _bgi.setcolor(0xff000000L);
        _bgi.setbkcolor(0xffffffffL);
        _bgi.settextstyle(_f, 0, -1);

        for (int _y=0; _y<256 && _c<1024; _y+=16)
        {
            for (int _x=0; _x<256 && _c<1024; _x+=16)
            {
                _bgi.outtextxy(_x,_y,Character.toString((char) _c));
                _c++;
                _bgi.rectangle(_x-1,_y-1,_x+7,_y+12);
            }
        }
        _bgi.end();
        _px.savePng("./out/bgi/test-bgi-7x8.png");
    }

}
