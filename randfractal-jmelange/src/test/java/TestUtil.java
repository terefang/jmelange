import com.github.terefang.jmelange.gfx.ImageUtil;
import com.github.terefang.jmelange.gfx.impl.SvgImage;
import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.utils.NoiseFieldUtil;
import com.orsoncharts.Chart3D;
import com.orsoncharts.Chart3DFactory;
import com.orsoncharts.data.function.Function3D;
import com.orsoncharts.graphics3d.Dimension3D;
import com.orsoncharts.graphics3d.ViewPoint3D;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.renderer.xyz.SurfaceRenderer;
import lombok.SneakyThrows;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;

public class TestUtil
{


    @SneakyThrows
    public static void saveWAV(Noisefield _nf, String _out)
    {
        byte[] pcm_data= new byte[_nf.getData().length];
        for(int i=0;i<pcm_data.length;i++){
            pcm_data[i]=  (byte)((_nf.getData()[i]-.5)*127);
        }

        AudioFormat frmt= new AudioFormat(12000,8,1,true,false);
        AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(pcm_data),frmt,pcm_data.length);
        AudioSystem.write(
                ais
                , AudioFileFormat.Type.WAVE
                ,new File(_out));
    }

    @SneakyThrows
    public static void saveHF(Noisefield _nf, String _out)
    {
        _nf.normalize(.0, 1.);
        NoiseFieldUtil.saveHFImage(_nf, _out);
    }

    public static class NfFunction implements Function3D
    {
        public Noisefield _nf;
        @Override
        public double getValue(double x, double y)
        {
            return _nf.getPoint((int)(_nf.getWidth()*x), (int)(_nf.getHeight()*y) )/100.;
        }
    }

    @SneakyThrows
    public static void savePlot(Noisefield _nf, String _out, String _name)
    {
        int _ssize = 2048;

        NfFunction _nff = new NfFunction();
        _nff._nf = _nf;
        Chart3D chart = Chart3DFactory.createSurfaceChart(_name, null, _nff, "x", "height", "y");
        chart.setViewPoint(ViewPoint3D.createAboveViewPoint(25));
        chart.getViewPoint().moveUpDown(-40*Math.PI/360);
        chart.setProjDistance(2000);

        XYZPlot plot = (XYZPlot)chart.getPlot();
        plot.setDimensions(new Dimension3D(20, 6, 20));

        SurfaceRenderer renderer = (SurfaceRenderer) plot.getRenderer();
        renderer.setXSamples(128);
        renderer.setZSamples(128);

        SvgImage _svg = ImageUtil.svgImage(_ssize, _ssize);
        Graphics2D _g2d = _svg.getG2d();
        chart.draw(_g2d, new Rectangle(0,0,_ssize,_ssize));
        _g2d.dispose();
        _svg.save(String.format(_out));
    }
}
