import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import com.github.terefang.jmelange.randfractal.utils.NoiseFieldUtil;
import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TestLiteNoiseBarron
{
    public static int _SIZE = 1024;
    public static float _FREQ = 5.6f;
    @SneakyThrows
    public static void main(String[] args) {
        ExecutorService _EX = Executors.newFixedThreadPool(8);


        int _seed = FastNoiseLite.BASE_SEED2;

        //FastNoiseLite.NoiseType _type = FastNoiseLite.NoiseType.SIMPLEX;
        for(final FastNoiseLite.NoiseType _type : FastNoiseLite.NoiseType.values())
        {
            final FastNoiseLite.TransformType _transform = FastNoiseLite.TransformType.T_BARRONSPLINE;
            //for(FastNoiseLite.TransformType _transform : FastNoiseLite.TransformType.values())
            {
                float _shape = 10.f;
                //for(float _shape = 1f; _shape < 10.5f; _shape+=1f)
                {
                    //float _turn = 0f;
                    for(float _turn = 0f; _turn < 1f; _turn+=0.1f)
                    {
                        final float _s = _shape;
                        final float _t = _turn;
                        _EX.execute(() -> {
                            Noisefield _nf = new Noisefield(_SIZE, _SIZE);
                            for (int _y = 0; _y < _SIZE; _y++) {
                                for (int _x = 0; _x < _SIZE; _x++) {
                                    float _fx = (((float) _x) / ((float) _SIZE));
                                    float _fy = (((float) _y) / ((float) _SIZE));
                                    double _h = FastNoiseLite.singleByNoiseAndTransform(_type, _transform, _s, _t, 0f, _seed, _fx * _FREQ, _fy * _FREQ);
                                    _nf.setPoint(_x, _y, _h);
                                }
                            }

                            _nf.mathClip(.0, 1.);
                            String _name = String.format("./out/fract/barron/noise~%s~%s~F=%.1f~s=%.1f~t=%.1f.png",
                                    _type.name(),
                                    _transform.name(),
                                    _FREQ,
                                    _s,
                                    _t);
                            NoiseFieldUtil.saveHFImage(_nf, _name);
                            System.err.println(_name);
                        });
                    }
                }
            }
        }
        _EX.shutdown();
    }
}
