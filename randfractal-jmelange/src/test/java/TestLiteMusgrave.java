import com.github.terefang.jmelange.gfx.ImageUtil;
import com.github.terefang.jmelange.gfx.impl.SvgImage;
import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import com.github.terefang.jmelange.randfractal.utils.NoiseFieldUtil;
import com.orsoncharts.Chart3D;
import com.orsoncharts.Chart3DFactory;
import com.orsoncharts.data.function.Function3D;
import com.orsoncharts.graphics3d.Dimension3D;
import com.orsoncharts.graphics3d.ViewPoint3D;
import com.orsoncharts.plot.Plot3D;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.renderer.xyz.ScatterXYZRenderer;
import com.orsoncharts.renderer.xyz.SurfaceRenderer;
import com.orsoncharts.renderer.xyz.XYZRenderer;
import lombok.SneakyThrows;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TestLiteMusgrave extends TestUtil
{
    public static int _SIZE = 512;
    public static int[] _OCT = { 0,1,2,3,4,5 };
    public static float[] _FREQ = { 12f, 56f, 123f, 567f };
    public static float[] _H = { .5f, .77f, 1.f, 1.23f, 1.5f, 1.9f, 2.2f, 2.5f, 5f };
    @SneakyThrows
    public static void main(String[] args) {
        ExecutorService _EX = Executors.newFixedThreadPool(8);
        int _seed = FastNoiseLite.BASE_SEED1;

        final float _h = .5f;
        //for(final float _h : _H)
        {
            final float _freq = 234.56f;
            //for(final float _freq : _FREQ)
            {
                int _oct = 4;
                //for(int _oct : _OCT)
                {
                    //final FastNoiseLite.FractalType _ftype = FastNoiseLite.FractalType.F_HETERO;
                    //for(final FastNoiseLite.FractalType _ftype : FastNoiseLite.FractalType.values())
                    for(final FastNoiseLite.FractalType _ftype : Arrays.asList(
                            FastNoiseLite.FractalType.F_BILLOW,
                            FastNoiseLite.FractalType.F_DISTORT,
                            FastNoiseLite.FractalType.F_FBM,
                            FastNoiseLite.FractalType.F_FBM2,
                            FastNoiseLite.FractalType.F_HETERO,
                            FastNoiseLite.FractalType.F_HYBRID,
                            FastNoiseLite.FractalType.F_MULTI,
                            FastNoiseLite.FractalType.F_RIDGED
                    ))
                    {
                        final FastNoiseLite.NoiseType _type = FastNoiseLite.NoiseType.PYRAMID_QUINTIC;
                        //for(final FastNoiseLite.NoiseType _type : FastNoiseLite.NoiseType.values())
                        {
                            final FastNoiseLite.TransformType _transform = FastNoiseLite.TransformType.T_0NONE;
                            //for(final FastNoiseLiteBase.TransformType _transform : FastNoiseLiteBase.TransformType.values())
                            {
                                _EX.execute(() -> {
                                    Noisefield _nf = new Noisefield(_SIZE, _SIZE);
                                    for (int _y = 0; _y < _SIZE; _y++) {
                                        for (int _x = 0; _x < _SIZE; _x++) {
                                            float _fx = (((float) _x) / ((float) _SIZE)) - .5f;
                                            float _fy = (((float) _y) / ((float) _SIZE)) - .5f;
                                            double _value = FastNoiseLite.fractalByType(_ftype, _type, _seed, _fx * _freq, _fy * _freq, FastNoiseLite.BASE_OFFSET, _h, _oct, true);
                                            _value = FastNoiseLite.singleTransform(_transform, _value,0,0,0);
                                            _nf.setPoint(_x, _y, _value);
                                        }
                                    }

                                    //String _name = _ftype.name() + "~" + _type.name() + "~" + _transform.name() + String.format("~fq=%04d,oct=%02d,H=%02d", (int) _freq, _oct, (int)(_h*10));
                                    String _name = _ftype.name() + "~" + _type.name(); // + "~" + _transform.name();

                                    //savePlot(_nf, "./out/TestLiteMusgrave/fchart~" + _name + ".svg", _name);
                                    saveHF(_nf, "./out/TestLiteMusgrave/fractal~" + _name + ".png");
                                });
                            }
                        }
                    }
                }
            }
        }
        _EX.shutdown();
    }
}
