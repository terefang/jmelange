import com.github.terefang.jmelange.commons.gfx.GfxFont;
import com.github.terefang.jmelange.commons.gfx.bgi.BmpFont;
import com.github.terefang.jmelange.commons.gfx.impl.PixelImage;
import com.github.terefang.jmelange.fonts.BDF;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class TestRomBin
{
   
    @SneakyThrows
    public static void main(String[] args)
    {
        //String _fontname = BDF.FONT_FIXED;
        GfxFont _fnt = BmpFont.load(new File("/u/fredo/IdeaProjects/jmelange/commons-jmelange/src/main/resources/fonts/atfont.bin"));
        
        PixelImage _img = PixelImage.create(128,128);
        for(int _i = 0;_i<256; _i++)
        {
            int _x = (_i*8)%128;
            int _y = (_i/16)*8;

            _fnt.drawString(_img, _x,_y,Character.toString((char)_i), 0xffffffffL, 0l);
        }

        _img.save(new File("out/font-rombin.png"));
    }
    
}
