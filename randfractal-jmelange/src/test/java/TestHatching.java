import com.github.terefang.jmelange.gfx.ImageUtil;
import com.github.terefang.jmelange.gfx.impl.SvgImage;
import com.github.terefang.jmelange.random.ArcRandom;
import lombok.SneakyThrows;

import java.awt.*;

public class TestHatching
{
    static int _SIZE = 1024;
    static int _PART = 16;
    @SneakyThrows
    public static void main(String[] args)
    {
        long seed = 1L;
        ArcRandom _arc = new ArcRandom();
        _arc.setSeed(seed);

        SvgImage _svg = ImageUtil.svgImage(_SIZE, _SIZE);
        _svg.beginDraw();
        _svg.setColor(Color.WHITE.getRGB());
        _svg.fillRect(0,0, _SIZE, _SIZE);
        _svg.endDraw();
        int _step = _SIZE/_PART;
        double _rot = _arc.nextByte()/128.;
        for(int _y = 0 ; _y<_SIZE; _y+=_step)
        for(int _x = 0 ; _x<_SIZE; _x+=_step)
        {
            drawHatch(_x+(_step/2)+(_step/(10*((_arc.nextByte()&0xff)+1))), _y+(_step/2)+(_step/(10*((_arc.nextByte()&0xff)+1))), _svg, _step, _rot);
            //_rot += .7;//+(_arc.nextByte()/64.);
            _rot += .3+(_arc.nextByte()/64.);
        }

        _svg.save("./out/TestHatching/test.svg");
    }

    public static void drawHatch(int _x, int _y, SvgImage _svg, int _size, double _rot)
    {
        double _parts = 2.;
        int _lsize = (int)(_size/2.0);
        double _vsize = (_size/2.3);
        _svg.beginDraw();
        _svg.setStroke(new BasicStroke(2f));
        _svg.translate(_x,_y);
        _svg.rotate(_rot);
        for(double _sy = _vsize; _sy>=-_vsize; _sy-=_vsize*2./_parts)
        {
            _svg.drawLine(-_lsize,(int)_sy, _lsize, (int)_sy);
        }
        _svg.endDraw();
    }
}
