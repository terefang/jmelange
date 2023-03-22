package planet;

import com.github.terefang.jmelange.planetj.PlanetJ;
import com.github.terefang.jmelange.planetj.codec.ImageCodec;
import com.github.terefang.jmelange.planetj.project.IProjectionCallback;
import com.github.terefang.jmelange.planetj.project.PlanetJProjectionContext;
import com.github.terefang.jmelange.randfractal.lite.FastNoiseLite;
import com.github.terefang.jmelange.randfractal.map.ColorRamp;
import lombok.SneakyThrows;

import java.awt.*;

public class TestWorldMapHex {


    @SneakyThrows
    public static void main/*_long_lat*/(String[] args)
    {
        String _NAME = "HEXA";
        PlanetJ planet = new PlanetJ();
        planet.init();
        planet.setSeed(0.900000016); //0.6 // 0.900000016
        //planet.setAltitudeAdjustment(.00012);
        //planet.setInitialAltitude(44);

        int _rr = 1024;
        planet.setWidth(_rr);
        planet.setHeight(_rr/2);

        planet.setView(PlanetJ.PROJ_VIEW_HEXAGONAL);
        planet.setBaseLatitude(0);
        planet.setBaseLongitude(-50);
        planet.setScale(1);

        planet.setUseAlternativeColors(true);
        planet.setNumberOfColors(32);
        planet.setLatitudeColors(true);
        planet.setNonLinear(false);
        planet.setWrinkleContribution(-1);

        planet.setDoshade(true);
        planet.setDoWaterShade(true);


        ColorRamp cr = ColorRamp.getLefebvre2();
        //ColorRamp cr = ColorRamp.getBase(Color.ORANGE, Color.MAGENTA);
        planet.setColorRamp(cr);
        planet.setColorRampSeaMin(-0.1);
        planet.setColorRampLandMax(0.1);

        planet.setTemperatureBase(0.);
        planet.setTemperatureVariationFactor(0.02);
        planet.setTemperatureVariationFrequency(12.34567);

        planet.setRainfallBase(.0066);
        planet.setRainfallVariationFactor(0.033);
        planet.setRainfallVariationFrequency(12.34567);

        planet.setFractalOverlay(new IProjectionCallback<PlanetJProjectionContext>()
        {
            final int _seed = FastNoiseLite.BASE_SEED1;
            final float _h = .5f;
            final float _freq = 234.56f;
            final int _oct = 4;
            final FastNoiseLite.FractalType _ftype = FastNoiseLite.FractalType.F_RIDGED;
//                                        FastNoiseLite.FractalType.F_BILLOW,
//                                        FastNoiseLite.FractalType.F_DISTORT,
//                                        FastNoiseLite.FractalType.F_FBM,
//                                        FastNoiseLite.FractalType.F_FBM2,
//                                        FastNoiseLite.FractalType.F_HETERO,
//                                        FastNoiseLite.FractalType.F_HYBRID,
//                                        FastNoiseLite.FractalType.F_MULTI,
//                                        FastNoiseLite.FractalType.F_RIDGED
            final FastNoiseLite.NoiseType _type = FastNoiseLite.NoiseType.PYRAMID_QUINTIC;
            final FastNoiseLite.TransformType _transform = FastNoiseLite.TransformType.T_0NONE;

            @Override
            public void projectCallback(PlanetJProjectionContext _context, int _i, int _j, double _x, double _y, double _z, int _depth, boolean _valid)
            {
                if(_valid)
                {
                    double _value = FastNoiseLite.fractalByType(_ftype, _type, _seed, _x * _freq, _y * _freq, _z * _freq, FastNoiseLite.BASE_OFFSET, _h, _oct, true);
                    _value = Math.abs(_value/32.);
                    if(_context.getMain().heights[_i][_j]>0.)
                    {
                        _context.getMain().heights[_i][_j] += _value;
                    }
                    else
                    {
                        _context.getMain().heights[_i][_j] -= _value;
                    }
                }
            }
        });
        planet.setFractalOverlay(null);
        //planet.setWaterLandPercentage(40);

        planet.setup();
        planet.process();

        planet.save(String.format("out/planet/test-%s-full.png", _NAME));
        ImageCodec.saveWLD(planet, String.format("out/planet/test-%s-full.wld", _NAME));
        planet.saveH(String.format("out/planet/test-%s-hf.png", _NAME));
        ImageCodec.saveBiome(planet, String.format("out/planet/test-%s-full-biome.png", _NAME));
        ImageCodec.saveWLD(planet, String.format("out/planet/test-%s-full-biome.wld", _NAME));
        ImageCodec.saveRainfall(planet, String.format("out/planet/test-%s-full-rain.png", _NAME));
        ImageCodec.saveRainAdj(planet, String.format("out/planet/test-%s-full-rain-adj.png", _NAME));
        ImageCodec.saveTemperature(planet, String.format("out/planet/test-%s-full-temp.png", _NAME));
        ImageCodec.saveTempAdj(planet, String.format("out/planet/test-%s-full-temp-adj.png", _NAME));
        planet.save_GXF0(String.format("out/planet/test-%s-full-0.gxf", _NAME), -1.);
        planet.save_GXF(String.format("out/planet/test-%s-full.gxf", _NAME), -1.);
        planet.saveWaterflux(String.format("out/planet/test-%s-waterflux.png", _NAME));
    }

}
