import com.github.terefang.jmelange.fonts.BDF;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class TestBDF
{
   
    @SneakyThrows
    public static void main(String[] args)
    {
        //String _fontname = BDF.FONT_FIXED;
        for(String _fontname : BDF._FONTLIST)
        {
            BDF.BDFont _bdf = BDF.load(_fontname);
            
            BufferedImage _img = new BufferedImage(512,512,BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D    _g   = (Graphics2D)_img.getGraphics();
            for(int _i = 0;_i<256; _i++)
            {
                int _x = (_i*32)%512;
                int _y = (_i/16)*32;
                BDF.drawChar(_g,_bdf,(char)_i,_x,_y, Color.YELLOW, Color.RED,false);
            }
            
            BDF.drawString(_g,_bdf,0,0,"Hello World", Color.WHITE, Color.RED);
            
            _g.dispose();
            ImageIO.write(_img,"png",new File("out/font-bdf-"+_fontname+".png"));
        }
    }
}
