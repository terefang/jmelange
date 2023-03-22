package noise;

import ch.vorburger.exec.ManagedProcess;
import ch.vorburger.exec.ManagedProcessBuilder;
import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import com.github.terefang.jmelange.randfractal.utils.NoiseFieldUtil;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TestLiteNoiseLump
{
    public static int _SIZE = 1024;
    public static float _FREQ = 12.3f;
    @SneakyThrows
    public static void main(String[] args) {
        ExecutorService _EX = Executors.newFixedThreadPool(8);

        List<String> _list = new Vector<>();

        int _seed = FastNoiseLite.BASE_SEED2;

        FastNoiseLite.NoiseType _type = FastNoiseLite.NoiseType.SIMPLEX_LUMP;

        //for(final FastNoiseLite.NoiseType _type : Arrays.asList(FastNoiseLite.NoiseType.MUTANT_NORMAL_QUINTIC , FastNoiseLite.NoiseType.MUTANT_QUINTIC))
        //for(final FastNoiseLite.NoiseType _type : FastNoiseLite.NoiseType.values())
        {
            final FastNoiseLite.TransformType _transform = FastNoiseLite.TransformType.T_0NONE;
            //for(FastNoiseLite.TransformType _transform : FastNoiseLite.TransformType.values())
            {
                float _shape = 10.f;
                //for(float _shape = 1f; _shape < 10.5f; _shape+=1f)
                {
                    float _turn = 0.5f;
                    //for(float _turn = 0f; _turn < 1f; _turn+=0.1f)
                    {
                        //float _harshness = 2.5f;
                        for(float _harshness = 9f; _harshness > 0f; _harshness-=1.0f)
                        {
                            final float _s = _shape;
                            final float _t = _turn;
                            final float _hn = _harshness;
                            _EX.execute(() -> {
                                Noisefield _nf = new Noisefield(_SIZE, _SIZE);
                                for (int _y = 0; _y < _SIZE; _y++) {
                                    for (int _x = 0; _x < _SIZE; _x++) {
                                        float _fx = (((float) _x) / ((float) _SIZE));
                                        float _fy = (((float) _y) / ((float) _SIZE));
                                        double _h = FastNoiseLite.singleByNoiseAndTransform(_type, _hn, FastNoiseLite.BASE_MUTATION, FastNoiseLite.BASE_SHARPNESS, _transform, _s, _t, 0f, _seed, _fx * _FREQ, _fy * _FREQ);
                                        _nf.setPoint(_x, _y, _h);
                                    }
                                }

                                //_nf.mathClip(-.1, 1.1);
                                //_nf.normalize(-1., 1.);
                                String _name = String.format("./out/fract/LUMP/noise~%s~%s~F=%.1f~s=%.1f~t=%.1f~h=%.1f.png",
                                        _type.name(),
                                        _transform.name(),
                                        _FREQ,
                                        _s,
                                        _t,
                                        _hn);
                                _list.add(_name);
                                NoiseFieldUtil.saveHistogramHFEImage(_nf, _name);
                                System.err.println(_name);
                            });
                        }
                    }
                }
            }
        }
        _EX.shutdown();
        while(!_EX.isTerminated())
        {
            Thread.sleep(1000L);
        }

        Collections.sort(_list);

        final ManagedProcessBuilder _mpb = new ManagedProcessBuilder("montage")
                .addArgument("-font")
                .addArgument("Helvetica-Narrow")
                .addArgument("-pointsize")
                .addArgument("10")
                .addArgument("-tile")
                .addArgument("4x")
                .addArgument("-label")
                .addArgument("%t");
        for(String _n : _list)
        {
            _mpb.addArgument(_n);
        }

        String _name = String.format("./out/fract/LUMP/noise-chart~%s~F=%.1f.png",
                _type.name(),
                _FREQ);
        ManagedProcess _mp = _mpb.addArgument("-geometry")
                .addArgument("256x256+0+0")
                .addArgument(_name).build();
        _mp.start();
        _mp.waitForExit();
    }
}
