import com.github.terefang.jmelange.gfx.GfxFont;
import com.github.terefang.jmelange.gfx.impl.BmpFont;
import com.github.terefang.jmelange.gfx.impl.PixelImage;
import com.github.terefang.jmelange.planetj.PlanetJ;
import com.github.terefang.jmelange.planetj.codec.ImageCodec;
import com.github.terefang.jmelange.randfractal.map.ColorRamp;
import lombok.SneakyThrows;

import java.awt.*;

public class TestWorldMap {

    @SneakyThrows
    public static void main_x(String[] args)
    {
        PlanetJ planet = new PlanetJ();
        planet.init();
        planet.setSeed(0.900000016); //0.6 // 0.900000016

        int _rr = 512;
        planet.setWidth(_rr);
        planet.setHeight(_rr);
        //planet.setHeight(_rr/2);
        planet.setView(PlanetJ.PROJ_VIEW_STEREOGRAPHIC);
        planet.setScale(.5);

        planet.setUseAlternativeColors(true);
        planet.setNumberOfColors(32);
        planet.setLatitudeColors(true);
        planet.setNonLinear(false);
        planet.setWrinkleContribution(-1);

        planet.setDoshade(true);
        planet.setDoWaterShade(true);

        planet.setTemperatureBase(0.);
        planet.setTemperatureVariationFactor(0.02);
        planet.setTemperatureVariationFrequency(12.34567);

        planet.setRainfallBase(.0066);
        planet.setRainfallVariationFactor(0.033);
        planet.setRainfallVariationFrequency(12.34567);

        planet.setHgrid(15);
        planet.setVgrid(15);

        int [] _longs = { 0, 45, 90, 135, 180, 225, 270, 315 };

        for(int _l : _longs)
        {
            planet.setBaseLongitude(_l);

            planet.setup();
            planet.process();

            planet.saveOutline(String.format("out/planet/xtest-outline-%03d.png", _l));
            planet.save(String.format("out/planet/xtest-%03d.png", _l));
            planet.saveBiome(String.format("out/planet/xtest-biome-%03d.png", _l));
        }
        planet.setBaseLongitude(0);
        {
            planet.setBaseLatitude(90);

            planet.setup();
            planet.process();

            planet.saveOutline("out/planet/xtest-outline-north.png");
            planet.save("out/planet/xtest-north.png");
            planet.saveBiome("out/planet/xtest-biome-north.png");
        }
        {
            planet.setBaseLatitude(-90);

            planet.setup();
            planet.process();

            planet.saveOutline("out/planet/xtest-outline-south.png");
            planet.save("out/planet/xtest-south.png");
            planet.saveBiome("out/planet/xtest-biome-south.png");
        }
    }

    public static void main/*_square_full*/(String[] args)
    {
        PlanetJ planet = new PlanetJ();
        planet.init();
        planet.setSeed(0.900000016); //0.6 // 0.900000016
        //planet.setAltitudeAdjustment(.00012);
        //planet.setInitialAltitude(44);

        int _rr = 2048;
        planet.setWidth(_rr);
        planet.setHeight(_rr/2);
        //planet.setHeight(_rr);

        //planet.setView(PlanetJ.PROJ_VIEW_SQUARE);
        //planet.setView(PlanetJ.PROJ_VIEW_STEREOGRAPHIC);
        planet.setView(PlanetJ.PROJ_VIEW_LONGLAT);
        //planet.setBaseLatitude(40);
        planet.setBaseLongitude(-90);;
        // STEREOGRAPHIC w/scale=1 -> 1/4 circumference
        //planet.setScale(2);

        planet.setUseAlternativeColors(true);
        planet.setNumberOfColors(32);
        planet.setLatitudeColors(true);
        planet.setNonLinear(false);
        planet.setWrinkleContribution(-1);

        planet.setDoshade(true);
        planet.setDoWaterShade(true);


        //if(false)
        {
            ColorRamp cr = ColorRamp.getLefebvre2();
            planet.setColorRamp(cr);
            planet.setColorRampSeaMin(-0.1);
            planet.setColorRampLandMax(0.1);
        }

        //if(false)
        {
            planet.setHgrid(10);
            planet.setVgrid(10);
        }

        planet.setTemperatureBase(0.);
        planet.setTemperatureVariationFactor(0.02);
        planet.setTemperatureVariationFrequency(12.34567);

        planet.setRainfallBase(.0066);
        planet.setRainfallVariationFactor(0.033);
        planet.setRainfallVariationFrequency(12.34567);

        planet.setHgrid(15);
        planet.setVgrid(15);
        //planet.setWaterLandPercentage(50);

        planet.setup();
        planet.process();

        /*
        for (int i = 0; i < planet.Width; i++)
        {
            for (int j = 0; j < planet.Height; j++)
            {
                planet.planet0_waterflux(i,j,0,0,0,1);
            }
        }
        planet.saveWaterflux("out/planet/test-full-flux.png");
        */

        planet.save("out/planet/test-full.png");
        planet.saveBiome("out/planet/test-full-biome.png");
        planet.saveTemperature("out/planet/test-full-temp.png");
        planet.saveRainfall("out/planet/test-full-rain.png");
        planet.saveOutline("out/planet/test-full-outline.png");
        planet.saveH("out/planet/test-full-h.png");
        //planet.save_TER("out/planet/test-full.ter");
        planet.save_MDR("out/planet/test-full.mdr", -1);
        //planet.save_GXF0("out/planet/test-full.gxf", -1.);
    }
}
