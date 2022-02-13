import com.github.terefang.jmelange.gfx.GfxFont;
import com.github.terefang.jmelange.gfx.impl.BmpFont;
import com.github.terefang.jmelange.gfx.impl.PixelImage;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class TestBmpFont
{

    @SneakyThrows
    public static void main(String[] args)
    {
        PixelImage _px = PixelImage.create(512,512);
        GfxFont _f = BmpFont.defaultInstance();
        int _c = 0;

        for (int _y=0; _y<512 && _c<1024; _y+=16)
        {
            for (int _x=0; _x<512 && _c<1024; _x+=16)
            {
                _f.drawString(_px,_x,_y,Character.toString((char) _c), 0xff000000, 0xffffffff);
                _c++;
            }
        }
        _px.savePng("./out/test-bmp-font-default.png");
    }

    static String[] _cfnt = {
            "C_6x10_LE", "C_6x12_LE", "C_6x13_LE",
            "C_7x14_LE", "C_8x8_LE", "C_8x10_LE",
            "C_8x12_LE", "C_8x13_LE", "C_8x14_LE",
            "C_8x16_LE" };

    @SneakyThrows
    public static void main_3(String[] args) {

        for(String _type : _cfnt)
        {
            PixelImage _px = PixelImage.create(512,512);
            GfxFont _f = BmpFont.load(new File("gfx-jmelange/src/main/resources/cfnt/"+_type+".gdf.gz"));
            int _c = 0;

            for (int _y=0; _y<512 && _c<1024; _y+=16)
            {
                for (int _x=0; _x<512 && _c<1024; _x+=16)
                {
                    _f.drawString(_px,_x,_y,Character.toString((char) _c), 0xff000000, 0xffffffff);
                    _c++;
                }
            }
            _px.savePng("./out/test-bmp-font-cfnt-"+_type+".png");
        }
    }

    static String[] _gdf = { "ami8", "display" };
    @SneakyThrows
    public static void main_2(String[] args) {

        for(String _type : _gdf)
        {
            PixelImage _px = PixelImage.create(512,512);
            GfxFont _f = BmpFont.load(new File("gfx-jmelange/src/main/resources/gdf/"+_type+".gdf"));
            int _c = 0;

            for (int _y=0; _y<512 && _c<1024; _y+=16)
            {
                for (int _x=0; _x<512 && _c<1024; _x+=16)
                {
                    _f.drawString(_px,_x,_y,Character.toString((char) _c), 0xff000000, 0xffffffff);
                    _c++;
                }
            }
            _px.savePng("./out/test-bmp-font-gdf-"+_type+".png");
        }
    }



    static String[] _gdfa = { "tiny", "small", "medium", "large", "giant" };
    @SneakyThrows
    public static void main_1(String[] args) {

        for(String _type : _gdfa)
        {
            PixelImage _px = PixelImage.create(512,512);
            GfxFont _f = BmpFont.load(new File("gfx-jmelange/src/main/resources/gd/"+_type+".gdfa"));
            int _c = 0;

            for (int _y=0; _y<512 && _c<1024; _y+=16)
            {
                for (int _x=0; _x<512 && _c<1024; _x+=16)
                {
                    _f.drawString(_px,_x,_y,Character.toString((char) _c), 0xff000000, 0xffffffff);
                    _c++;
                }
            }
            _px.savePng("./out/test-bmp-font-gdfa-"+_type+".png");
        }
    }
}
