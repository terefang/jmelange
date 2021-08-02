import com.github.terefang.jmelange.commons.CommonUtil;
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
import java.util.ArrayList;

public class TestStarNoise
{
    static class StarCube
    {
        Noisefield _f[] = null;

        public void init(long _seed, int _size, int _ox, int _oy, int _oz)
        {
            INoiseSampler _noise = INoiseSampler.pluginNoiseSampler(INoise.jlGradientCoherentNoise(_seed));
            _f = new Noisefield[3];
            for(int _i = 0; _i<3; _i++)
            {
                _f[_i] = new Noisefield(_size,_size);
                _f[_i].setProjection(Noisefield.FP_NONE);
                switch(_i)
                {
                    case 0:
                        _f[_i].setXOff(_ox);
                        _f[_i].setYOff(_oy);
                        break;
                    case 1:
                        _f[_i].setXOff(_oz);
                        _f[_i].setYOff(_oy);
                        break;
                    case 2:
                        _f[_i].setXOff(_oz);
                        _f[_i].setYOff(_ox);
                        break;
                }
                _f[_i].applyNoise(_noise, Noisefield.NF_OP_ADD, Noisefield.NF_PROC_FRIDGEDMULTI_SIN, 1000, 3, 1.0, 2.0, 6.0, 1);
                _f[_i].normalize( 0, 1);
            }
        }

        public Noisefield[] getF()
        {
            return _f;
        }
    }

    static Matrix4 _3t2d = Matrix4.IDENTITY.multiply(
            Matrix4.rotateY(Math.toRadians(30))
                    .multiply(Matrix4.rotateX(Math.toRadians(35)))
                    .multiply(Matrix4.rotateZ(Math.toRadians(-15))).multiply(Matrix4.scale(0.4)));

    @SneakyThrows
    public static void main(String[] args) {
        ColorRamp _cr = ColorRamp.getAdvanced();
        long seed = 1L;
        int _size = 16;
        StarCube _sc = new StarCube();
        ArcRandom _arc = new ArcRandom();
        _arc.setSeed(seed);

        _sc.init(seed, _size, 0,0,0);

        for(int _i = 0; _i<3; _i++)
        {
            NoiseFieldUtil.saveHFImage(_sc.getF()[_i], 1f, "./out/fract/starCube_"+_i+".png");
        }

        int _ssize = 1024;

        if(true)
        {
            double[] histo = new double[300];
            for(int _z=0 ; _z<_size ; ++_z)
            {
                for(int _y=0 ; _y<_size ; ++_y)
                {
                    for(int _x=0 ; _x<_size ; ++_x)
                    {
                        float _xh= (float)_sc.getF()[0].getPoint(_x,_y);
                        float _yh= (float)_sc.getF()[1].getPoint(_z,_y);
                        float _zh= (float)_sc.getF()[2].getPoint(_z,_x);

                        float _h = _xh+_yh+(_zh*0.75f);

                        int _off = (int) (_h*100f);
                        if(_off>=histo.length)
                        {
                            histo[histo.length-1]++;
                        }
                        else
                        {
                            histo[_off]++;
                        }
                    }
                }
            }

            DefaultKeyedValues _v = new DefaultKeyedValues();

            for(int _i = 0; _i< histo.length; _i++)
            {
                _v.addValue(""+_i, histo[_i]);
            }
            CategoryDataset _cd = DatasetUtils.createCategoryDataset("key", _v);
            JFreeChart _bc = ChartFactory.createBarChart("", "TH", "N", _cd);
            SvgImage _svg = ImageUtil.svgImage(_ssize, _ssize);
            Graphics2D _g2d = _svg.getG2d();
            _bc.draw(_g2d, new Rectangle(0,0,_ssize,_ssize));
            _g2d.dispose();
            _svg.save("./out/fract/starCube-histogram.svg");
        }

        for(float _th=2.3f; _th>2.0f; _th-=0.1f)
        {
            int _j = 0;
            int _k = 0;
            XYZSeries<String> s1 = new XYZSeries<String>("S1");
            for(int _z=0 ; _z<_size ; ++_z)
            {
                for(int _y=0 ; _y<_size ; ++_y)
                {
                    for(int _x=0 ; _x<_size ; ++_x, ++_k)
                    {
                        float _xh= (float)_sc.getF()[0].getPoint(_x,_y);
                        float _yh= (float)_sc.getF()[1].getPoint(_z,_y);
                        float _zh= (float)_sc.getF()[2].getPoint(_z,_x);

                        float _h = _xh+_yh+(_zh*0.75f);

                        if(_h>=_th)
                        {
                            int _sx = 50+((_x-(_size/2))*100)+((int)(JlibNoiseRef.GradientCoherentNoise3D(_xh,_yh,_zh, (int) _k)*64));
                            int _sy = 50+((_y-(_size/2))*100)+((int)(JlibNoiseRef.GradientCoherentNoise3D(_zh,_xh,_yh, (int) _k)*64));
                            int _sz = 50+((_z-(_size/2))*100)+((int)(JlibNoiseRef.GradientCoherentNoise3D(_yh,_zh,_xh, (int) _k)*64));
                            s1.add(_sx, _sy, _sz);
                            _j++;
                        }

                    }
                }
            }

            KrushkalMST _mst = new KrushkalMST();

            int _a = 0;
            for(XYZDataItem _ia : s1.getItems())
            {
                int _b = 0;
                for(XYZDataItem _ib : s1.getItems())
                {
                    if(_b > _a)
                    {

                        double _w = Math.pow(Math.abs(_ia.getX() - _ib.getX()),2);
                        _w += Math.pow(Math.abs(_ia.getY() - _ib.getY()),2);
                        _w += Math.pow(Math.abs(_ia.getZ() - _ib.getZ()),2);
                        if(_w<3*Math.pow(200,2))
                        {
                            _mst.addEgde(_a, _b, _w);
                        }
                    }
                    _b++;
                }
                _a++;
            }

            _mst.setVertices(s1.getItemCount());

            if(_mst.getAllEdges().size()>0)
            {
                SvgImage _svgx = ImageUtil.svgImage(_ssize, _ssize);
                _svgx.gFilledRectangle(0, 0, _ssize, _ssize, ImageUtil.WHITE);
                for(KrushkalMST.Edge _e :  _mst.kruskalMST())
                {
                    double _sx1 = s1.getXValue(_e.getSource());
                    double _sy1 = s1.getYValue(_e.getSource());
                    double _sz1 = s1.getZValue(_e.getSource());

                    Point3 _xy1 = _3t2d.transformP(new Point3(_sx1, _sy1, _sz1));

                    double _sx2 = s1.getXValue(_e.getDestination());
                    double _sy2 = s1.getYValue(_e.getDestination());
                    double _sz2 = s1.getZValue(_e.getDestination());

                    Point3 _xy2 = _3t2d.transformP(new Point3(_sx2, _sy2, _sz2));


                    _svgx.gDashedLine((_ssize/2) + (int)_xy1.get(0), (_ssize/2) + (int)_xy1.get(1), (_ssize/2) + (int)_xy2.get(0), (_ssize/2) + (int)_xy2.get(1), ImageUtil.RED, 1f);
                }
                for(XYZDataItem _ia : s1.getItems())
                {
                    double _sx1 = _ia.getX();
                    double _sy1 = _ia.getY();
                    double _sz1 = _ia.getZ();

                    Point3 _xy1 = _3t2d.transformP(new Point3(_sx1, _sy1, _sz1));

                    _svgx.gFilledCircle((_ssize/2) + (int)_xy1.get(0), (_ssize/2) + (int)_xy1.get(1), 3, ImageUtil.HALF_BLUE);
                }
                _svgx.save(String.format("./out/fract/starCube-%.4f-xy.svg", _th));
            }
            else
            {
                new File(String.format("./out/fract/starCube-%.4f-xy.svg", _th)).delete();
            }


            XYZSeriesCollection<String> dataset = new XYZSeriesCollection<String>();
            dataset.add(s1);

            Chart3D chart = Chart3DFactory.createScatterChart("th = "+_th+" | count = "+_j,
                    null, dataset, "X", "Y", "Z");
            XYZPlot plot = (XYZPlot) chart.getPlot();
            plot.getXAxis().setRange(-1000,1000);
            plot.getYAxis().setRange(-1000,1000);
            plot.getZAxis().setRange(-1000,1000);
            ScatterXYZRenderer renderer = (ScatterXYZRenderer) plot.getRenderer();
            //plot.setDimensions(new Dimension3D(10, 6, 10));
            renderer.setSize(0.1);
            renderer.setColors(new Color(255, 128, 128), new Color(128, 255, 128));
            chart.setViewPoint(ViewPoint3D.createAboveLeftViewPoint(40));
            chart.setProjDistance(2000);
            SvgImage _svg = ImageUtil.svgImage(_ssize, _ssize);
            Graphics2D _g2d = _svg.getG2d();
            chart.draw(_g2d, new Rectangle(0,0,_ssize,_ssize));
            _g2d.dispose();
            _svg.save(String.format("./out/fract/starCube-%.4f.svg", _th));
            System.err.println(" count =  "+_j);
        }
    }
}
