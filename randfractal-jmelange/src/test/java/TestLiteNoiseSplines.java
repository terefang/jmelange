import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import com.github.terefang.jmelange.randfractal.utils.NoiseFieldUtil;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TestLiteNoiseSplines
{
    public static int _SIZE = 1024;
    public static float _FREQ = 12.3f;
    @SneakyThrows
    public static void main(String[] args) {
        ExecutorService _EX = Executors.newFixedThreadPool(8);


        int _seed = FastNoiseLite.BASE_SEED2;

        //FastNoiseLite.NoiseType _type = FastNoiseLite.NoiseType.MUTANT_NORMAL_QUINTIC;
        //for(final FastNoiseLite.NoiseType _type : CommonUtil.toList(FastNoiseLite.NoiseType.MUTANT_NORMAL_QUINTIC , FastNoiseLite.NoiseType.MUTANT_QUINTIC))
        for(final FastNoiseLite.NoiseType _type : FastNoiseLite.NoiseType.values())
        {
            //final FastNoiseLite.TransformType _transform = FastNoiseLite.TransformType.T_BARRONSPLINE;
            for(FastNoiseLite.TransformType _transform : FastNoiseLite.TransformType.values())
//            for(FastNoiseLite.TransformType _transform : Arrays.asList(FastNoiseLite.TransformType.T_0NONE, FastNoiseLite.TransformType.T_HERMITESPLINE, FastNoiseLite.TransformType.T_QUINTICSPLINE, FastNoiseLite.TransformType.T_BARRONSPLINE))
            {
                float _shape = 10.f;
                //for(float _shape = 1f; _shape < 10.5f; _shape+=1f)
                {
                    float _turn = 0.5f;
                    //for(float _turn = 0f; _turn < 1f; _turn+=0.1f)
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

                            _nf.mathClip(-.1, 1.1);
//                            _nf.normalize(0., 1.);
// tilde is used for expansions in Windows filenames; not sure if we can name files with tildes.
                            String _name = String.format("./out/splines/noise#%s#%s#F=%.1f#s=%.1f#t=%.1f.png",
                                    _type.name(),
                                    _transform.name(),
                                    _FREQ,
                                    _s,
                                    _t);
                            NoiseFieldUtil.saveHFEImage(_nf, _name);
                            System.err.println(_name);
                        });
                    }
                }
            }
        }
        _EX.shutdown();
    }
}
