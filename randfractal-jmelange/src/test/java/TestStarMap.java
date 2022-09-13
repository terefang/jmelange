import com.github.terefang.jmelange.gfx.ImageUtil;
import com.github.terefang.jmelange.gfx.impl.SvgImage;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import com.github.terefang.jmelange.randfractal.map.ColorRamp;
import com.github.terefang.jmelange.randfractal.xnoise.KrushkalMST;
import com.github.terefang.jmelange.randfractal.xnoise.Matrix4;
import com.github.terefang.jmelange.randfractal.xnoise.Point3;
import com.github.terefang.jmelange.random.ArcRandom;
import com.orsoncharts.Chart3D;
import com.orsoncharts.Chart3DFactory;
import com.orsoncharts.data.xyz.XYZDataItem;
import com.orsoncharts.data.xyz.XYZSeries;
import com.orsoncharts.data.xyz.XYZSeriesCollection;
import com.orsoncharts.graphics3d.ViewPoint3D;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.renderer.xyz.ScatterXYZRenderer;
import lombok.SneakyThrows;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtils;

import java.awt.*;
import java.io.File;

public class TestStarMap
{
    static class StarCube
    {
        double[][][][] _f = null;

        public void init(long _seed, int _size, int _ox, int _oy, int _oz)
        {
            _f = new double[_size][_size][_size][];

            for(int _ix = 0; _ix<_size; _ix++)
            {
                for(int _iy = 0; _iy<_size; _iy++)
                {
                    for(int _iz = 0; _iz<_size; _iz++)
                    {
                        _f[_ix][_iy][_iz] =
                        new double[] {
                                FastNoiseLite.f_ridged_multi(FastNoiseLite.NoiseType.SIMPLEX, (double)_ix/(double)_size, (double)_iy/(double)_size, (double)_iz/(double)_size, (int)_seed, 0f, FastNoiseLite.BASE_H, FastNoiseLite.BASE_OCTAVES,FastNoiseLite.BASE_FREQUENCY*30f,FastNoiseLite.BASE_LACUNARITY,FastNoiseLite.BASE_GAIN, FastNoiseLite.BASE_HARSHNESS, FastNoiseLite.BASE_MUTATION, FastNoiseLite.BASE_SHARPNESS, true),
                                FastNoiseLite.singleNoiseByType(FastNoiseLite.NoiseType.RIPPLE_HERMITE, (int)_seed, (double)_iy/(double)_size, (double)_iz/(double)_size, (double)_ix/(double)_size),
                                FastNoiseLite.singleNoiseByType(FastNoiseLite.NoiseType.RIPPLE_HERMITE, (int)_seed, (double)_iz/(double)_size, (double)_ix/(double)_size, (double)_iy/(double)_size)
                        };

                    }
                }
            }
        }

        public double[][][][] getF()
        {
            return _f;
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        ColorRamp _cr = ColorRamp.getAdvanced();
        long seed = 1L;
        int _size = 16;
        StarCube _sc = new StarCube();
        ArcRandom _arc = new ArcRandom();
        _arc.setSeed(seed);

        _sc.init(seed, _size, 0,0,0);

        int _ssize = 1024;
        int _k = 0;
        float _th=.8f;
        {
            for (int _z = 0; _z < _size; ++_z) {
                for (int _y = 0; _y < _size; ++_y) {
                    for (int _x = 0; _x < _size; ++_x) {
                        double _xh = FastNoiseLite.singleNoiseByType(FastNoiseLite.NoiseType.MUTANT_HERMITE, ((_x << 8) ^ (_y << 16) ^ _z), _x, _y);
                        double _yh = FastNoiseLite.singleNoiseByType(FastNoiseLite.NoiseType.MUTANT_HERMITE, ((_y << 8) ^ (_z << 16) ^ _x), _z, _y);
                        double _zh = FastNoiseLite.singleNoiseByType(FastNoiseLite.NoiseType.MUTANT_HERMITE, ((_z << 8) ^ (_x << 16) ^ _y), _z, _x);

                        double[] _h = _sc.getF()[_x][_y][_z];

                        if (Math.abs(_h[0]) >= _th) {
                            int _sx = (50 + ((_x - (_size / 2)) * 100) + ((int) (_xh * 64)));
                            int _sy = (50 + ((_y - (_size / 2)) * 100) + ((int) (_yh * 64)));
                            int _sz = (50 + ((_z - (_size / 2)) * 100) + ((int) (_zh * 64)));

                            System.out.println(String.format("%f %d %d (%d %d %d)", _h[0], (Double.doubleToLongBits(_h[1])>>15) & 0xffff, (Double.doubleToLongBits(_h[2])>>15) & 0xffff, _sx, _sy, _sz));
                            _k++;
                        }
                    }
                }
            }
        }
        System.out.println(String.format("count = %d", _k));
    }
}
