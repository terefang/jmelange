package noise;

import ch.vorburger.exec.ManagedProcess;
import ch.vorburger.exec.ManagedProcessBuilder;
import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import com.github.terefang.jmelange.randfractal.utils.NoiseFieldUtil;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Vector;


public class TestNoiseWhite
{
    public static int _SIZE = 1024;
    public static int _KSIZE = 9;
    public static float _FREQ = 56.7f;

    @SneakyThrows
    public static void main(String[] args)
    {
        int _seed = FastNoiseLite.BASE_SEED2;
        List<String> _list = new Vector<>();

        final FastNoiseLite.NoiseType _type = FastNoiseLite.NoiseType.BLUR;
        final FastNoiseLite.TransformType _transform = FastNoiseLite.TransformType.T_0NONE;
        float _shape = 10.f;
        float _turn = 0.5f;
        float _harshness = 2f;

        Noisefield _nf = new Noisefield(_SIZE, _SIZE);
        for (int _y = 0; _y < _SIZE; _y++) {
            for (int _x = 0; _x < _SIZE; _x++) {
                float _fx = (((float) _x) / ((float) _SIZE));
                float _fy = (((float) _y) / ((float) _SIZE));
                double _h = FastNoiseLite.singleByNoiseAndTransform(_type, _harshness, FastNoiseLite.BASE_MUTATION, FastNoiseLite.BASE_SHARPNESS, _transform, _shape, _turn, 0f, _seed, _fx * _FREQ, _fy * _FREQ);
                _nf.setPoint(_x, _y, _h);
            }
        }
        /*
        System.err.println(System.currentTimeMillis());
        for(int _i=1; _i<5; _i++)
        {
            _nf.scaleUp();
            //_nf.pyramidalFilter(3);
            _nf.gaussFilter(_KSIZE<<_i, 1);
            _nf.normalize(-1,1);
            System.err.println(System.currentTimeMillis());
        }
         */
        _nf.normalize(-1,1);
        String _name = "./out/noise.TestNoiseWhite/noise.png";
        NoiseFieldUtil.saveHFEImage(_nf, _name);
        _list.add(_name);
        System.err.println(_name);


    }
}
