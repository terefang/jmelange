import com.github.terefang.jmelange.gfx.ImageUtil;
import com.github.terefang.jmelange.gfx.impl.SvgImage;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import com.github.terefang.jmelange.randfractal.random.ArcRandom;
import lombok.SneakyThrows;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtils;

import java.awt.*;

public class TestDiagramBarron
{

    @SneakyThrows
    public static void main(String[] args)
    {
        SvgImage _svg = ImageUtil.svgImage(10000, 10000);
        Graphics2D _g2d = _svg.getG2d();

        int _i = 0;
        for(float _turn = 0f; _turn < 1f; _turn+=.1f ) {
            for(float _shape = 1f; _shape < 10.1f; _shape+=1f )
            {
                DefaultKeyedValues _v = new DefaultKeyedValues();
                for (float _j = -0f; _j < 1.01f; _j += 0.05f) {
                    _v.addValue(String.format("%.2f", _j), FastNoiseLite.barronSpline(_j, _shape, _turn));
                }
                CategoryDataset _cd = DatasetUtils.createCategoryDataset("key", _v);
                JFreeChart _bc = ChartFactory.createBarChart("S=" + _shape+" T="+_turn, "TH", "N", _cd);
                _bc.draw(_g2d, new Rectangle((_i / 10) * 1000, (_i % 10) * 1000, 1000, 1000));
                _i++;
            }
        }

        _g2d.dispose();
        _svg.save("./out/barron.svg");
    }
}
