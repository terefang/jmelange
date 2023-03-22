package com.github.terefang.jmelange.planetj.codec;

import com.github.terefang.jmelange.gfx.GfxFont;
import com.github.terefang.jmelange.gfx.impl.BmpFont;
import com.github.terefang.jmelange.gfx.impl.PixelImage;
import com.github.terefang.jmelange.planetj.PlanetJ;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageCodec
{
    public static void saveOutline(PlanetJ _p, String _f, int _pct)
    {
        saveAs(_f, _p.makeRgbImageOutline(_pct));
    }

    public static void saveOutline(PlanetJ _p, String _f, int _pct, String _comment)
    {
        PixelImage _pix = PixelImage.from(_p.makeRgbImageOutline(_pct));
        GfxFont _fnt = BmpFont.default6x12();
        _fnt.drawString(_pix, 0, 0, _comment, Color.WHITE.getRGB(), Color.BLACK.getRGB());
        _pix.savePng(_f);
    }

    public static void saveWLD(PlanetJ _p, String f, double rh, double rw, double scale, double lng, double lat, double lngOff, double latOff)
    {
        try
        {
            File outFile = new File(f);
            PrintStream out = new PrintStream(outFile);

            double sx = (360.0/(rw))/scale;
            double sy = sx;
            double sr = (180.0/(rh))/scale;

            lng += lngOff;
            while(lng>180.) lng -= 360.;
            out.printf("%.21f\n", sx);
            out.printf("%.21f\n", 0f);
            out.printf("%.21f\n", 0f);
            out.printf("%.21f\n", -sy);
            //out.printf("%.5f\n", (-sr*rh)+lng+(sr/2.0));
            out.printf("%.21f\n", (-sr*rh)+lng+sr);
            //out.printf("%.5f\n", (sr*rh)+(lat*2.0)-(sr/2.0));
            out.printf("%.21f\n", (sr*rh/2.0)+(lat*2.0)-sr+latOff);
            out.close();
        }
        catch(Exception xe)
        {
            xe.printStackTrace();
        }
    }

    public static void saveWLD(PlanetJ _p, String f, double lngOff, double latOff)
    {
        saveWLD(_p, f, (double)_p.Height, (double)_p.Width, _p.scale, _p.baseLongitude*180.0/Math.PI, _p.baseLatitude*180.0/Math.PI, lngOff, latOff);
    }

    public static void saveWLD(PlanetJ _p, String f)
    {
        saveWLD(_p, f, (double)_p.Height, (double)_p.Width, _p.scale, _p.baseLongitude*180.0/Math.PI, _p.baseLatitude*180.0/Math.PI, 0, 0);
    }

    public static void saveBiome(PlanetJ _p, String f)
    {
        saveBiome(_p, f, true);
    }

    public static void saveBiome(PlanetJ _p, String f, boolean _comment)
    {
        if(_comment)
        {
            PixelImage _pix = PixelImage.from(_p.makeRgbImageBiome());
            GfxFont _fnt = BmpFont.default6x12();
            int _l = 10;
            for(Character _c : PlanetJ.biomeText.keySet())
            {
                Color _col = PlanetJ.biomeColors.get(_c);
                _fnt.drawString(_pix, _p.getWidth()-(PlanetJ.biomeText.get(_c).length()*6), _l, PlanetJ.biomeText.get(_c), Color.BLACK.getRGB(), _col.getRGB());
                _l+=14;
            }
            _pix.savePng(f);
        }
        else
        {
            saveAs(f, _p.makeRgbImageBiome());
        }
    }

    public static void saveRainfall(PlanetJ _p, String f)
    {
        saveAs(f, _p.makeRgbImageRainfall());
    }

    public static void saveTemperature(PlanetJ _p, String f)
    {
        saveAs(f, _p.makeRgbImageTemperature());
    }

    public static void saveTempAdj(PlanetJ _p, String f)
    {
        saveAs(f, _p.makeRgbImageTempAdj());
    }

    public static void saveRainAdj(PlanetJ _p, String f)
    {
        saveAs(f, _p.makeRgbImageRainAdj());
    }

    public static void saveAs(String f, BufferedImage bufferedImage)
    {
        String image_type = "jpg";
        if(f.toLowerCase().endsWith(".jpg") || f.toLowerCase().endsWith(".jpeg"))
        {
            image_type = "jpg";
        }
        else if(f.toLowerCase().endsWith(".png"))
        {
            image_type = "png";
        }
        else if(f.toLowerCase().endsWith(".gif"))
        {
            image_type = "gif";
        }
        else
        {
            throw new IllegalArgumentException("Unknown image extension: "+f);
        }

        try
        {
            BufferedOutputStream fh = new BufferedOutputStream(new FileOutputStream(new File(f)), 8192<<8);
            ImageIO.write(bufferedImage, image_type, fh);
            fh.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveWaterflux(PlanetJ _p, String f) {
        saveAs(f, _p.makeRgbImageWaterFlux());
    }
}
