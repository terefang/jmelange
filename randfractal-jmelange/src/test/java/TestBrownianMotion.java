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

public class TestBrownianMotion
{

    @SneakyThrows
    public static void main(String[] args) {
        ColorRamp _cr = ColorRamp.getAdvanced();
        long seed = 0xdeaddeefcafeaffeL;
        float _size = 16f;
        float _frequency = 2.3456789f; //(float) (Math.PI*Math.PI*Math.PI*Math.PI);

        ArcRandom _arc = new ArcRandom();
        _arc.setSeed(seed);

        int _ssize = 4096;
        SvgImage _svg = ImageUtil.svgImage(_ssize, _ssize);
        _svg.beginDraw();
        _svg.setColor(Color.BLACK.getRGB());
        _svg.fillRect(0,0, _ssize, _ssize);
        float _mx = 0.5f;
        float _my = 0.5f;
        float _x = _ssize*.5f;
        float _y = _x;
        float _lx = _x;
        float _ly = _x;
        _svg.setColor(Color.WHITE.getRGB());
        for(float _i=0; _i<1024; _i++)
        {
            _my += FastNoiseLite.singleByNoiseAndTransform(FastNoiseLite.NoiseType.SIMPLEX, FastNoiseLite.TransformType.T_0NONE, (int)seed, (_mx*_frequency), (_my*_frequency) )/2f;
            _mx += FastNoiseLite.singleByNoiseAndTransform(FastNoiseLite.NoiseType.SIMPLEX, FastNoiseLite.TransformType.T_0NONE, (int)seed, (_my*_frequency), (_mx*_frequency) )/2f;

            _mx = (float) Math.sin(_mx);
            _my = (float) Math.sin(_my);

            System.err.println(String.format("%.3f %.3f", _mx, _my));

            _x += (_mx*_size);
            _y += (_my*_size);

            _svg.drawLine((int)_lx,(int)_ly,(int)_x,(int)_y);

            _lx = _x;
            _ly = _y;
        }
        _svg.endDraw();
        _svg.save("./out/TestBrownianMotion/motion.svg");

    }
}
