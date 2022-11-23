package noise;

import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import com.github.terefang.jmelange.randfractal.map.ColorRamp;
import com.github.terefang.jmelange.randfractal.utils.NoiseFieldUtil;
import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TestLiteNoiseLandWater
{
    public static int _SIZE = 512;
    public static float _FREQ = 34.567f;
    @SneakyThrows
    public static void main(String[] args)
    {
        ExecutorService _EX = Executors.newFixedThreadPool(8);
        final int _seed = FastNoiseLite.BASE_SEED2;

        final FastNoiseLite.NoiseType _type = FastNoiseLite.NoiseType.SIMPLEX;

        for(int _i=0; _i<256;_i++)
        {
            final String _name = String.format("./out/%s/noise_%03d.png", TestLiteNoiseLandWater.class.getSimpleName(), _i);
            final int _I = _i;
            _EX.execute(()->{
                Noisefield _nf = new Noisefield(_SIZE,_SIZE);

                //processCheatedWorldMap(_nf, _I, _seed, _type);
                //processPangeaWorldMap(_nf, _I, _seed, _type);
                processDiamondWorldMap(_nf, _I, _seed, _type);
                //_nf.mathClip(-1.1, 1.1);
                //NoiseFieldUtil.saveHFEImage(_nf, _name);
                NoiseFieldUtil.saveCRImage(_nf, _name, ColorRamp.getLefebvre2(), -1.1, 1.1);
                System.err.println(_name);
            });
        }
        _EX.shutdown();
    }

    static void processCheatedWorldMap(Noisefield _nf, int _i, int _seed, FastNoiseLite.NoiseType _type)
    {
        for(int _y=0; _y<_nf.getHeight(); _y++)
        {
            for(int _x=0; _x<_nf.getWidth(); _x++)
            {
                float _fx = (((float)_x) / ((float)_nf.getWidth()))*2f-1f;
                float _fy = (((float)_y) / ((float)_nf.getHeight()))*2f-1f;
                double _m=(.5-Math.pow(1.8*(Math.abs(_fx)+Math.abs(_fy)), 2)*.5);
                double _n=(_fx*_fx)+(_fy*_fy)+.5;

                //if(_n>1f) {
                //    _m = -200f;
                //}
                //else
                {
                    _m = .5-Math.pow(_n, 1.5);
                }

                double[] v = Noisefield.PhiRhoToXYZ(_fx*Math.PI,_fy*Math.PI/4.);
                v[2]+= _i;

                double _h = FastNoiseLite.fractalByType(FastNoiseLite.FractalType.F_FBM, _type, _seed+1,(float)v[0]*_FREQ, (float)v[1]*_FREQ, (float)v[2]*_FREQ);
                _h += FastNoiseLite.fractalByType(FastNoiseLite.FractalType.F_DISTORT, _type, _seed+2,(float)v[0]*_FREQ/2.5f, (float)v[1]*_FREQ/2.5f, (float)v[2]*_FREQ);
                _h -= FastNoiseLite.fractalByType(FastNoiseLite.FractalType.F_RIDGED, _type, _seed,(float)v[0]*_FREQ, (float)v[1]*_FREQ, (float)v[2]*_FREQ);
                //if(_m<0) _h = 0;
                //Math.cos(*Math.PI);

                _nf.setPoint(_x, _y, _h+_m);
            }
        }

        _nf.normalize(-5.1, .91);
    }

    static void processPangeaWorldMap(Noisefield _nf, int _i, int _seed, FastNoiseLite.NoiseType _type)
    {
        for(int _y=0; _y<_nf.getHeight(); _y++)
        {
            for(int _x=0; _x<_nf.getWidth(); _x++)
            {
                float _fx = (((float)_x) / ((float)_nf.getWidth()))*2f-1f;
                float _fy = (((float)_y) / ((float)_nf.getHeight()))*2f-1f;
                double _m=(.5-Math.pow(1.8*(Math.abs(_fx)+Math.abs(_fy)), 2)*.5);
                double _n=(_fx*_fx)+(_fy*_fy)+.5;

                //if(_n>1f) {
                //    _m = -200f;
                //}
                //else
                {
                    _m = .5-Math.pow(_n, 1.5);
                }

                double[] v = Noisefield.PhiRhoToXYZ(_fx*Math.PI,_fy*Math.PI/4.);

                double _h = FastNoiseLite.fractalByType(FastNoiseLite.FractalType.F_FBM, _type, _seed+1+_i, (float)v[0]*_FREQ, (float)v[1]*_FREQ, (float)v[2]*_FREQ);
                _h += FastNoiseLite.fractalByType(FastNoiseLite.FractalType.F_DISTORT, _type, _seed+2+_i,(float)v[0]*_FREQ/2.5f, (float)v[1]*_FREQ/2.5f, (float)v[2]*_FREQ);
                _h -= FastNoiseLite.fractalByType(FastNoiseLite.FractalType.F_RIDGED, _type, _seed+_i,(float)v[0]*_FREQ, (float)v[1]*_FREQ, (float)v[2]*_FREQ);
                //if(_m<0) _h = 0;
                //Math.cos(*Math.PI);

                _nf.setPoint(_x, _y, _h+_m);
            }
        }

        _nf.normalize(-5.1, .91);
    }

    static void processWorldIsland(Noisefield _nf, int _i, int _seed, FastNoiseLite.NoiseType _type)
    {
        for(int _y=0; _y<_nf.getHeight(); _y++)
        {
            for(int _x=0; _x<_nf.getWidth(); _x++)
            {
                float _fx = (((float)_x) / ((float)_nf.getWidth()))*2f-1f;
                float _fy = (((float)_y) / ((float)_nf.getHeight()))*2f-1f;

                float _fz = (float) _i;

                double _h = .5*FastNoiseLite.fractalByType(FastNoiseLite.FractalType.F_FBM, _type, _seed+1,_fx*_FREQ, _fy*_FREQ, _fz);
                _h -= .5*FastNoiseLite.fractalByType(FastNoiseLite.FractalType.F_DISTORT, _type, _seed+2+_i,_fx*_FREQ, _fy*_FREQ, _fz*_FREQ);
                _h += .5*FastNoiseLite.fractalByType(FastNoiseLite.FractalType.F_RIDGED, _type, _seed,_fx*_FREQ/1.5f, _fy*_FREQ/1.5f, _fz);
                double _m=-Math.pow(Math.abs(_fx)+Math.abs(_fy)+.125, 1.1);

                _nf.setPoint(_x, _y, _h+_m);
            }
        }

        //_nf.normalize((n) -> { return (n*2.0)-1.0;});
        //_nf.mathClip(-1.1, 1.1);
        _nf.normalize(-5.1, .91);
    }

    // Fractal FBM Simplex Noise applied as World-Island/Pangea style
    // fractal mapmaking world island pangea
    static void processDiamondWorldMap(Noisefield _nf, int _i, int _seed, FastNoiseLite.NoiseType _type)
    {
        for(int _y=0; _y<_nf.getHeight(); _y++)
        {
            for(int _x=0; _x<_nf.getWidth(); _x++)
            {
                float _fx = (((float)_x) / ((float)_nf.getWidth()))*2f-1f;
                float _fy = (((float)_y) / ((float)_nf.getHeight()))*2f-1f;

                float _fz = (float) _i;

                double _h = FastNoiseLite.fractalByType(FastNoiseLite.FractalType.F_FBM, _type, _seed+1,_fx*_FREQ, _fy*_FREQ, _fz);
                //_h -= .5*FastNoiseLite.singleNoiseByType(FastNoiseLite.NoiseType.RIPPLE_HERMITE, _seed,_fx*_FREQ/11f, _fy*_FREQ/11f, _fz);
                double _m=-Math.pow(Math.abs(_fx)+Math.abs(_fy)+.125, 1.1);

                _nf.setPoint(_x, _y, _h+_m);
            }
        }

        //_nf.normalize((n) -> { return (n*2.0)-1.0;});
        //_nf.mathClip(-1.1, 1.1);
        _nf.normalize(-5.1, .91);
    }
}
