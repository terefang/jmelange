import ch.vorburger.exec.ManagedProcess;
import ch.vorburger.exec.ManagedProcessBuilder;
import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.lite.*;
import com.github.terefang.jmelange.randfractal.utils.NoiseFieldUtil;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TestLiteNoise
{
    public static int _SIZE = 16;
    public static float _FREQ = 23.456f;
    @SneakyThrows
    public static void main(String[] args)
    {
        ExecutorService _EX = Executors.newFixedThreadPool(8);


        final int _seed = FastNoiseLite.BASE_SEED2;

        Map<FastNoiseLite.NoiseType, ManagedProcessBuilder> _list = new HashMap<>();

        //final FastNoiseLite.NoiseType _type = FastNoiseLite.NoiseType.PYRAMID;
        //for(final FastNoiseLite.NoiseType _type : FastNoiseLite.NoiseType.values())
        for(final FastNoiseLite.NoiseType _type : Arrays.asList(FastNoiseLite.NoiseType.PYRAMID_LINEAR, FastNoiseLite.NoiseType.PYRAMID_HERMITE, FastNoiseLite.NoiseType.PYRAMID_QUINTIC))
        {
            final ManagedProcessBuilder _mpb = new ManagedProcessBuilder("montage")
                    .addArgument("-font")
                    .addArgument("Helvetica-Narrow")
                    .addArgument("-pointsize")
                    .addArgument("10")
                    .addArgument("-tile")
                    .addArgument("4x")
                    .addArgument("-label")
                    .addArgument("%t");
            _list.put(_type, _mpb);
            for(final FastNoiseLite.TransformType _transform : FastNoiseLite.TransformType.values())
            {
                String _name = String.format("./out/%s/noise_%s_%s.png", TestLiteNoise.class.getSimpleName(),_type.name(), _transform.name());
                _mpb.addArgument(_name);
                _EX.execute(() -> {
                    Noisefield _nf = new Noisefield(_SIZE,_SIZE);

                    for(int _y=0; _y<_SIZE; _y++)
                    {
                        for(int _x=0; _x<_SIZE; _x++)
                        {
                            float _fx = (((float)_x) / ((float)_SIZE));
                            float _fy = (((float)_y) / ((float)_SIZE));
                            double _h = FastNoiseLite.singleByNoiseAndTransform(_type, _transform, 10f, .5f, 0f, _seed,_fx*_FREQ, _fy*_FREQ);
                            _nf.setPoint(_x, _y, _h);
                        }
                    }

                    //_nf.normalize(.0, 1.);
                    _nf.mathClip(-1.1, 1.1);
                    NoiseFieldUtil.saveHFEImage(_nf, _name);
                    System.err.println(_name);
                });
            }
        }
        _EX.shutdown();
        while (!_EX.isTerminated())
        {
            System.err.println(".");
            Thread.sleep(1000L);
        }
        for(final FastNoiseLite.NoiseType _ntype : _list.keySet())
        {
            String _name = String.format("./out/%s/chart_%s.png", TestLiteNoise.class.getSimpleName(),_ntype.name());
            ManagedProcess _mp = _list.get(_ntype)
                    .addArgument("-geometry")
                    .addArgument("256x256+0+0")
                    .addArgument(_name).build();
            _mp.start();
            _mp.waitForExit();
            System.err.println(_name);
        }
    }
}
