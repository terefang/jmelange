import com.github.terefang.jmelange.ScriptCli;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.g2d.PdfGraphics2D;
import com.github.terefang.jmelange.pdf.core.values.PdfFormXObject;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import com.github.terefang.jmelange.pdf.ext.PdfExtDocument;

import lombok.SneakyThrows;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import java.awt.geom.Rectangle2D;


public class TestCharts
{
    @SneakyThrows
    public static void main(String[] args) {
        PdfExtDocument _doc = PdfExtDocument.create();
        PdfFontRegistry _reg = _doc.createFontRegistry();
        PdfPage _page = _doc.newPage();

        double[][] xData = new double[][] {{0.0,1.0,2}, {1,2,3}};
        double[][] yData = new double[][] {{2.0,1.0,0.0}, {1,2,3}};

        DefaultXYDataset dataset = new DefaultXYDataset();
        dataset.addSeries("1", xData);
        dataset.addSeries("2", yData);
        JFreeChart _chart = ChartFactory.createXYLineChart("TestChart", "X",
                "Y", dataset, PlotOrientation.VERTICAL, false, false, false);

        PdfGraphics2D _g2d = PdfGraphics2D.from(_page);
        _g2d.setPdfFontRegistry(_reg);

        _chart.draw(_g2d,new Rectangle2D.Float(50,50,500,200));


        _doc.writeTo("./chart.pdf");
    }
}
