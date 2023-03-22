package noise;

import ch.vorburger.exec.ManagedProcess;
import ch.vorburger.exec.ManagedProcessBuilder;
import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import com.github.terefang.jmelange.randfractal.utils.NoiseFieldUtil;
import com.github.terefang.jmelange.starsys.util.ArcRand;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Vector;


public class TestNoiseDungeon2
{
    public static int _SIZE = 1024;
    public static float _FREQ = 12.3f;
    public static float _DIV = .9f;
    public static float _MAX = .5f;//.5f;
    public static float _MIN = -.5f;//-.5f;
    @SneakyThrows
    public static void main(String[] args)
    {
        int _seed = FastNoiseLite.BASE_SEED2;
        List<String> _list = new Vector<>();

        final FastNoiseLite.NoiseType _type = FastNoiseLite.NoiseType.SIMPLEX_LUMP;
        final FastNoiseLite.TransformType _transform = FastNoiseLite.TransformType.T_0NONE;
        float _shape = 10.f;
        float _turn = 0.5f;
        float _harshness = 2f;
        Noisefield _nf = new Noisefield(_SIZE, _SIZE);
        for (float _div = _DIV; _div > 0.05f; _div-=0.15)
        {
            for (int _y = 0; _y < _SIZE; _y++) {
                for (int _x = 0; _x < _SIZE; _x++) {
                    float _fx = (((float) _x) / ((float) _SIZE));
                    float _fy = (((float) _y) / ((float) _SIZE));
                    double _h = FastNoiseLite.singleByNoiseAndTransform(_type, _harshness, FastNoiseLite.BASE_MUTATION, FastNoiseLite.BASE_SHARPNESS, _transform, _shape, _turn, 0f, _seed, _fx * _FREQ*_div, _fy * _FREQ*_div);
                    if(true)
                    {
                        if(_h>_MAX*_div)
                        {
                            _nf.setPoint(_x, _y, -1.);
                        }
                        else
                        if(_h<_MIN*_div)
                        {
                            _nf.setPoint(_x, _y, -1.);
                        }
                        else
                        {
                            _nf.setPoint(_x, _y, 1.);
                        }
                    }
                    //_nf.setPoint(_x, _y, _h);
                }
            }

            ArcRand _arc = ArcRand.from(0x1E371E37);
            int _l = 0;
            while(_l < 1000)
            {
                int _x = _arc.nextInt(_SIZE);
                int _y = _arc.nextInt(_SIZE);
                double _h = _nf.getPoint(_x, _y);
                if(_h>.5)
                {
                    int _xn = _x / (_SIZE>>4);
                    int _yn = _y / (_SIZE>>4);
                    int _xm = _xn + 1;
                    int _ym = _yn + 1;
                    for(int _j=_xn*(_SIZE>>4); _j<=_xm*(_SIZE>>4); _j++)
                    {
                        for(int _i=_yn*(_SIZE>>4); _i<=_ym*(_SIZE>>4); _i++)
                        {
                            _nf.setPoint(_j,_i, 0.);
                        }
                    }
                }
                else
                {
                    _l++;
                }
            }

            String _name = String.format("./out/TestNoiseDungeon2/noise-%.2f.png", _div);
            //NoiseFieldUtil.saveHFEImage(_nf, _name);
            NoiseFieldUtil.saveBLImage(_nf, -.9, _name);
            _list.add(_name);
            System.err.println(_name);
        }

        final ManagedProcessBuilder _mpb = new ManagedProcessBuilder("montage")
                .addArgument("-font")
                .addArgument("Helvetica-Narrow")
                .addArgument("-pointsize")
                .addArgument("10")
                .addArgument("-tile")
                .addArgument("3x")
                .addArgument("-label")
                .addArgument("%t");
        for(String _n : _list)
        {
            _mpb.addArgument(_n);
        }

        String _name = "./out/TestNoiseDungeon/noise-chart.png";
        ManagedProcess _mp = _mpb.addArgument("-geometry")
                .addArgument("256x256+0+0")
                .addArgument(_name).build();
        _mp.start();
        _mp.waitForExit();
    }
}
