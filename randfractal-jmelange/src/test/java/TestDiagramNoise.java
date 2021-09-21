import com.github.terefang.jmelange.gfx.ImageUtil;
import com.github.terefang.jmelange.gfx.impl.SvgImage;
import com.github.terefang.jmelange.randfractal.INoise;
import com.github.terefang.jmelange.randfractal.INoiseSampler;
import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.map.ColorRamp;
import com.github.terefang.jmelange.randfractal.noise.impl.JlibNoiseRef;
import com.github.terefang.jmelange.randfractal.random.ArcRandom;
import com.github.terefang.jmelange.randfractal.utils.NoiseFieldUtil;
import com.github.terefang.jmelange.randfractal.xnoise.KrushkalMST;
import com.github.terefang.jmelange.randfractal.xnoise.Matrix4;
import com.github.terefang.jmelange.randfractal.xnoise.Point3;
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

public class TestDiagramNoise
{

    @SneakyThrows
    public static void main(String[] args) {
        long seed = 1L;
        ArcRandom _arc = new ArcRandom();
        _arc.setSeed(seed);

        double[] histo = new double[256];
        for(int _z=0 ; _z<0x1000000 ; ++_z)
        {
            double _l0 = _arc.nextGaussian(4.0, 0.125, 0.5);
            histo[(int) (Math.abs(_l0) * 0xff)]++;
        }

        DefaultKeyedValues _v = new DefaultKeyedValues();

        for(int _i = 0; _i< histo.length; _i++)
        {
            _v.addValue(""+_i, histo[_i]);
        }
        CategoryDataset _cd = DatasetUtils.createCategoryDataset("key", _v);
        JFreeChart _bc = ChartFactory.createBarChart("", "TH", "N", _cd);
        SvgImage _svg = ImageUtil.svgImage(1024, 1024);
        Graphics2D _g2d = _svg.getG2d();
        _bc.draw(_g2d, new Rectangle(0,0,1024,1024));
        _g2d.dispose();
        _svg.save("./out/arcrand-histogram.svg");

    }
}
