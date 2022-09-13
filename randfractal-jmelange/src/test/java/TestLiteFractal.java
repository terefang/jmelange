import ch.vorburger.exec.ManagedProcess;
import ch.vorburger.exec.ManagedProcessBuilder;
import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TestLiteFractal extends TestUtil
{

    public static int _SIZE = 512;
    public static float[] _FREQ = { 5f, 12f, 56f, 123f };
    @SneakyThrows
    public static void main(String[] args) {
        ExecutorService _EX = Executors.newFixedThreadPool(8);
        int _seed = FastNoiseLite.BASE_SEED1;
        Map<String, ManagedProcessBuilder> _list = new HashMap<>();

        //FastNoiseLite.FractalType _ftype = FastNoiseLite.FractalType.F_FBM;
        for(final FastNoiseLite.FractalType _ftype : FastNoiseLite.FractalType.values())
        {
            FastNoiseLite.NoiseType _type = FastNoiseLite.NoiseType.RIPPLE_HERMITE;
            //for(final FastNoiseLite.NoiseType _type : FastNoiseLite.NoiseType.values())
            {
                final ManagedProcessBuilder _mpb = new ManagedProcessBuilder("montage")
                        .addArgument("-font")
                        .addArgument("Helvetica-Narrow")
                        .addArgument("-pointsize")
                        .addArgument("10")
                        .addArgument("-tile")
                        .addArgument("2x")
                        .addArgument("-label")
                        .addArgument("%t");
                _list.put(String.format("%s~%s", _ftype.name(), _type.name()), _mpb);
                //float _freq = 12.34f;
                for(final float _freq : _FREQ)
                {
                    int _octave = 4;
                    //for(int _octave = 1; _octave < 13; _octave++)
                    {
                        final int _oct = _octave;
                        FastNoiseLite.TransformType _transform = FastNoiseLite.TransformType.T_INVERT;
                        //for(final FastNoiseLite.TransformType _transform : FastNoiseLite.TransformType.values())
                        {
                            String _name = String.format("%s__%s__%s__fq=%04d__oct=%02d", _ftype.name(), _type.name(), _transform.name(), (int)(_freq*10), _oct);
                            String _fname = String.format("./out/%s/noise__%s.png", TestLiteFractal.class.getSimpleName(),_name);
                            String _cname = String.format("./out/%s/plot__%s.svg", TestLiteFractal.class.getSimpleName(),_name);
                            _mpb.addArgument(_fname);
                            _EX.execute(() -> {
                                Noisefield _nf = new Noisefield(_SIZE,_SIZE);
                                for(int _y=0; _y<_SIZE; _y++)
                                {
                                    for(int _x=0; _x<_SIZE; _x++)
                                    {
                                        float _fx = (((float)_x) / ((float)_SIZE))-.5f;
                                        float _fy = (((float)_y) / ((float)_SIZE))-.5f;
                                        double _h = FastNoiseLite.fractalByType(_ftype, _type, FastNoiseLite.BASE_HARSHNESS, FastNoiseLite.BASE_MUTATION, FastNoiseLite.BASE_SHARPNESS, _seed, _fx, _fy, FastNoiseLite.BASE_OFFSET, FastNoiseLite.BASE_H, _oct, _freq, FastNoiseLite.BASE_LACUNARITY, FastNoiseLite.BASE_GAIN, true);
                                        _h = FastNoiseLite.singleTransform(_transform, _h, 1f,0.5f,0f);
                                        _nf.setPoint(_x, _y, _h);
                                    }
                                }

                                if(_transform.equals(FastNoiseLite.TransformType.T_0NONE))
                                {
                                    savePlot(_nf, _cname, _name);
                                }
                                _nf.normalize(-1f, 1f);
                                //_nf.mathClip(-1.1f, 1.1f);
                                saveHFE(_nf, _fname);
                                _nf.dispose();
                                System.gc();
                                System.err.println(_name);
                            });
                        }
                    }
                }
            }
        }
        _EX.shutdown();
        while (!_EX.isTerminated())
        {
            System.err.println(".");
            Thread.sleep(1000L);
        }
        for(final String _ftype : _list.keySet())
        {
            String _name = String.format("./out/%s/chart__%s.png", TestLiteFractal.class.getSimpleName(),_ftype);
            ManagedProcess _mp = _list.get(_ftype)
                    .addArgument("-geometry")
                    .addArgument("512x512+0+0")
                    .addArgument(_name).build();
            _mp.start();
            _mp.waitForExit();
            System.err.println(_name);
        }
    }
}
