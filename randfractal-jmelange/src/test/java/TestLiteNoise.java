import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.lite.*;
import com.github.terefang.jmelange.randfractal.utils.NoiseFieldUtil;
import lombok.SneakyThrows;


public class TestLiteNoise
{
    public static int _SIZE = 1024;
    public static float _FREQ = 123f;
    @SneakyThrows
    public static void main(String[] args) {
        Noisefield _nf = new Noisefield(_SIZE,_SIZE);

        int _seed = FastNoiseLite.BASE_SEED2;


        for(FastNoiseLite.NoiseType _type : FastNoiseLite.NoiseType.values())
        {
            for(FastNoiseLite.TransformType _transform : FastNoiseLite.TransformType.values())
            {
                for(int _y=0; _y<_SIZE; _y++)
                {
                    for(int _x=0; _x<_SIZE; _x++)
                    {
                        float _fx = (((float)_x) / ((float)_SIZE));
                        float _fy = (((float)_y) / ((float)_SIZE));
                        double _h = FastNoiseLite.singleByNoiseAndTransform(_type, _transform,_seed,_fx*_FREQ, _fy*_FREQ);
                        _nf.setPoint(_x, _y, _h);
                    }
                }

                _nf.normalize(.0, 1.);
                NoiseFieldUtil.saveHFImage(_nf, "./out/fract/noise_"+_type.name()+"_"+_transform.name()+".png");
            }
        }
    }
}
