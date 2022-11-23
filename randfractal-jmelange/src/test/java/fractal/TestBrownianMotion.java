package fractal;

import com.github.terefang.jmelange.gfx.ImageUtil;
import com.github.terefang.jmelange.gfx.impl.SvgImage;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import com.github.terefang.jmelange.randfractal.map.ColorRamp;
import com.github.terefang.jmelange.random.ArcRandom;
import lombok.SneakyThrows;

import java.awt.*;

public class TestBrownianMotion
{

    @SneakyThrows
    public static void main(String[] args) {
        ColorRamp _cr = ColorRamp.getAdvanced();
        long seed = 0xdeaddeefcafeaffeL;
        int _steps = 1 << 12;
        float _size = 16f;
        float _frequency = 23456.789f; //(float) (Math.PI*Math.PI*Math.PI*Math.PI);

        ArcRandom _arc = new ArcRandom();
        _arc.setSeed(seed);

        int _ssize = 4096;
        //FastNoiseLite.NoiseType _nt = FastNoiseLite.NoiseType.WHITE_NORMAL;
        FastNoiseLite.NoiseType[] _ntl = { FastNoiseLite.NoiseType.SIMPLEX_LUMP, FastNoiseLite.NoiseType.WHITE_NORMAL };
        //FastNoiseLite.NoiseType[] _ntl = FastNoiseLite.NoiseType.values();
        for(FastNoiseLite.NoiseType _nt : _ntl)
        {
            SvgImage _svg = ImageUtil.svgImage(_ssize, _ssize);
            _svg.beginDraw();
            _svg.setColor(Color.BLACK.getRGB());
            _svg.fillRect(0,0, _ssize, _ssize);
            float _mx = 0.5f;
            float _my = 0.5f;
            float _x = _ssize*.5f;
            float _y = _x;
            float _lx = _x;
            float _ly = _x;
            _svg.setColor(Color.WHITE.getRGB());
            for(float _i=0; _i<_steps; _i++)
            {
                double _m = FastNoiseLite.singleByNoiseAndTransform(_nt, FastNoiseLite.TransformType.T_0NONE, (int)seed, (_mx*_frequency), (_my*_frequency) )*Math.PI;
                _my = (float) Math.sin(_m);
                _mx = (float) Math.cos(_m);

                //_mx = (float) Math.sin(_mx);
                //_my = (float) Math.sin(_my);

                //System.err.println(String.format("%.3f %.3f", _mx, _my));

                _x += (_mx*_size);
                _y += (_my*_size);

                _svg.drawLine((int)_lx,(int)_ly,(int)_x,(int)_y);

                _lx = _x;
                _ly = _y;
            }
            _svg.endDraw();
            _svg.save("./out/TestBrownianMotion/motion."+_nt.name()+".svg");
        }

    }
}
