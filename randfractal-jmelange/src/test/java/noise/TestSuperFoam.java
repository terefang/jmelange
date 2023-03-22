package noise;

import ch.vorburger.exec.ManagedProcessBuilder;
import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import com.github.terefang.jmelange.randfractal.lite.FnFoam;
import com.github.terefang.jmelange.randfractal.utils.NoiseFieldUtil;
import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TestSuperFoam
{
    public static int _SIZE = 1024;
    public static float _FREQ = 12.3456f;
    @SneakyThrows
    public static void main(String[] args)
    {
        ExecutorService _EX = Executors.newFixedThreadPool(8);

        final int _seed = FastNoiseLite.BASE_SEED2;

        for(final FastNoiseLite.TransformType _trans : FastNoiseLite.TransformType.values()) {
            for (final FastNoiseLite.NoiseType _type : FastNoiseLite.NoiseType.values()) {
                String _name = String.format("./out/TestSuperFoam/noise-%s-%s.png", _type.name(), _trans.name());
                _EX.execute(() -> {
                    makenoise(_name, _type, _trans, _seed);
                });
            }
        }

        for(final FastNoiseLite.NoiseType _type : FastNoiseLite.NoiseType.values())
        {
            String _name = String.format("./out/TestSuperFoam/foam-%s.png", _type.name());
            _EX.execute(() -> {
                makefoam(_name, _type,_seed);
            });
        }
        _EX.shutdown();
        while (!_EX.isTerminated())
        {
            System.err.println(".");
            Thread.sleep(1000L);
        }
    }

    static void makefoam(String _name, FastNoiseLite.NoiseType _type, int _seed)
    {
        Noisefield _nf = new Noisefield(_SIZE,_SIZE);

        for(int _y=0; _y<_SIZE; _y++)
        {
            for(int _x=0; _x<_SIZE; _x++)
            {
                float _fx = (((float)_x) / ((float)_SIZE));
                float _fy = (((float)_y) / ((float)_SIZE));
                double _h = FnFoam.singleFoam(_type, FastNoiseLite.BASE_SHARPNESS, true, _seed,_fx*_FREQ, _fy*_FREQ);
                _nf.setPoint(_x, _y, _h);
            }
        }

        //_nf.normalize(.0, 1.);
        _nf.mathClip(-1.1, 1.1);
        NoiseFieldUtil.saveHistogramHFEImage(_nf, _name);
        System.err.println(_name);
    }

    static void makenoise(String _name, FastNoiseLite.NoiseType _type, FastNoiseLite.TransformType _trans, int _seed)
    {
        Noisefield _nf = new Noisefield(_SIZE,_SIZE);

        for(int _y=0; _y<_SIZE; _y++)
        {
            for(int _x=0; _x<_SIZE; _x++)
            {
                float _fx = (((float)_x) / ((float)_SIZE));
                float _fy = (((float)_y) / ((float)_SIZE));
                double _h = FastNoiseLite.singleByNoiseAndTransform(_type, _trans, _seed,_fx*_FREQ, _fy*_FREQ);
                _nf.setPoint(_x, _y, _h);
            }
        }

        //_nf.normalize(.0, 1.);
        _nf.mathClip(-1.1, 1.1);
        NoiseFieldUtil.saveHFEImage(_nf, _name);
        NoiseFieldUtil.saveHistogramHFEImage(_nf, _name);
        System.err.println(_name);
    }
}
