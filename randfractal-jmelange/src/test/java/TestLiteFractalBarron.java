import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TestLiteFractalBarron extends TestUtil
{

    public static int _SIZE = 512;
    public static float[] _FREQ = { 5f, 12f, 56f, 123f, 567f };
    @SneakyThrows
    public static void main(String[] args) {
        ExecutorService _EX = Executors.newFixedThreadPool(8);
        int _seed = FastNoiseLite.BASE_SEED1;

        float _freq = 123f;
        //for(final float _freq : _FREQ)
        {
            int _oct = 4;
            //for(int _oct = 1; _oct < 9; _oct++)
            {
                //FastNoiseLiteBase.FractalType _ftype = FastNoiseLiteBase.FractalType.FRACTAL_MULTI;
                for(final FastNoiseLite.FractalType _ftype : FastNoiseLite.FractalType.values())
                {
                    //FastNoiseLite.NoiseType _type = FastNoiseLite.NoiseType.SIMPLEX;
                    for(final FastNoiseLite.NoiseType _type : FastNoiseLite.NoiseType.values())
                    {
                        //FastNoiseLite.TransformType _transform = FastNoiseLite.TransformType.T_0NONE;
                        for(final FastNoiseLite.TransformType _transform : CommonUtil.toList(FastNoiseLite.TransformType.T_0NONE, FastNoiseLite.TransformType.T_BARRONSPLINE, FastNoiseLite.TransformType.T_QUINTICSPLINE, FastNoiseLite.TransformType.T_HERMITESPLINE))
                        //for(final FastNoiseLite.TransformType _transform : FastNoiseLite.TransformType.values())
                        {
                            _EX.execute(() -> {
                                Noisefield _nf = new Noisefield(_SIZE,_SIZE);
                                for(int _y=0; _y<_SIZE; _y++)
                                {
                                    for(int _x=0; _x<_SIZE; _x++)
                                    {
                                        float _fx = (((float)_x) / ((float)_SIZE))-.5f;
                                        float _fy = (((float)_y) / ((float)_SIZE))-.5f;
                                        float _h = FastNoiseLite.fractalByType(_ftype, _type, FastNoiseLite.BASE_MUTATION, FastNoiseLite.BASE_SHARPNESS, _transform, 1f,0.5f,0f, _seed, _fx*_freq, _fy*_freq, FastNoiseLite.BASE_OFFSET, FastNoiseLite.BASE_H, _oct, FastNoiseLite.BASE_FREQUENCY, FastNoiseLite.BASE_LACUNARITY, FastNoiseLite.BASE_GAIN, true);
                                        _nf.setPoint(_x, _y, _h);
                                    }
                                }

                                String _name = String.format("%s~%s~%s~fq=%04d~oct=%02d", _ftype.name(), _type.name(), _transform.name(), (int)_freq, _oct);

                                //savePlot(_nf, "./out/fract/barron/fchart~"+_name+".svg", _name);
                                saveHF(_nf, "./out/fract/barron/fractal~"+_name+".png");
                                _nf.dispose();
                                System.err.println(_name);
                                System.gc();
                            });
                        }
                    }
                }
            }
        }
        _EX.shutdown();
    }
}
