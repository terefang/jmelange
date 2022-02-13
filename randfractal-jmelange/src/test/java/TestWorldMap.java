import com.github.terefang.jmelange.gfx.GfxFont;
import com.github.terefang.jmelange.gfx.impl.BmpFont;
import com.github.terefang.jmelange.gfx.impl.PixelImage;
import com.github.terefang.jmelange.planetj.PlanetJ;
import com.github.terefang.jmelange.randfractal.map.ColorRamp;
import lombok.SneakyThrows;

import java.awt.*;

public class TestWorldMap {
    @SneakyThrows
    public static void main/*_square full*/(String[] args)
    {
        PlanetJ planet = new PlanetJ();
        planet.init();
        planet.setSeed(0.900000016); //0.6 // 0.900000016
        //planet.setAltitudeAdjustment(.00012);
        //planet.setInitialAltitude(44);

        int _rr = 4096;
        planet.setWidth(_rr);
        //planet.setHeight(_rr/2);
        planet.setHeight(_rr);

        //planet.setView(PlanetJ.PROJ_VIEW_SQUARE);
        planet.setView(PlanetJ.PROJ_VIEW_STEREOGRAPHIC);
        planet.setBaseLatitude(40);
        planet.setBaseLongitude(-75);;
        // STEREOGRAPHIC w/scale=1 -> 1/4 circumference
        planet.setScale(2);

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

        planet.setup();
        planet.process();

        planet.save("out/planet/test-full.png");
        PixelImage _pix = PixelImage.from(planet.makeRgbImageBiome());
        GfxFont _fnt = BmpFont.defaultC8x14();
        int _l = 10;
        for(Character _c : PlanetJ.biomeText.keySet())
        {
            Color _col = PlanetJ.biomeColors.get(_c);
            _fnt.drawString(_pix, _rr-300, _l, PlanetJ.biomeText.get(_c), Color.BLACK.getRGB(), _col.getRGB());
            _l+=14;
        }
        _pix.savePng("out/planet/test-full-biome.png");
        planet.save_WLD("out/planet/test-full.wld");
        planet.saveH("out/planet/test-full-h.png");
        //planet.save_TER("out/planet/test-full.ter");
        planet.save_MDR("out/planet/test-full.mdr", -1);
        //planet.save_GXF0("out/planet/test-full.gxf", -1.);
    }
}
