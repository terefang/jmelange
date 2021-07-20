package com.github.terefang.jmelange.randfractal.map;

import com.github.terefang.jmelange.randfractal.utils.ColorUtil;

import java.awt.*;
import java.io.File;

/**
 * Created by fredo on 20.11.15.
 */
public interface ColorRamp {

    public static ColorRamp getFile(String path)
    {
        ColorRampStaticImpl r = new ColorRampStaticImpl();
        r.load(new File(path));
        return r;
    }

    public static ColorRamp getDefault()
    {
        ColorRampStaticImpl r = new ColorRampStaticImpl();
        r.SEA_COLOR = new Color[] {
                Color.BLACK,
                new Color(32,64,128),
                new Color(64,64,160),
                new Color(64,96,255),
                new Color(64,128,255),
        };
        r.LAND_COLOR = new Color[] {
                new Color(83, 194, 108),
                new Color(144, 193,  58),
                new Color(197, 172,  64),
                new Color(219, 151, 119),
                new Color(255, 255, 255)
        };
        return r;
    }

    public static ColorRamp getComplex()
    {
        return ColorRampDynImpl.getDefault();
    }


    public static ColorRamp getAdvanced()
    {
        ColorRampStaticImpl r = new ColorRampStaticImpl();
        r.SEA_COLOR = new Color[] {
                ColorUtil.rgb_pct(41, 97, 156, 0.7),
                ColorUtil.rgb_pct(89, 148, 204, 0.7),
                ColorUtil.rgb_pct(115, 166, 224, 0.7),
                ColorUtil.rgb_pct(133, 179, 235, 0.7),
                ColorUtil.rgb_pct(148, 194, 247, 0.7),
                ColorUtil.rgb_pct(166, 206, 245, 0.7),
                ColorUtil.rgb_pct(186, 222, 255, 0.7),
        };
        r.LAND_COLOR = new Color[] {
                ColorUtil.rgb_pct( 84, 229, 151, 0.9),
                ColorUtil.rgb_pct(158, 254, 135, 0.9),
                ColorUtil.rgb_pct(207, 254, 144, 0.9),
                ColorUtil.rgb_pct(247, 254, 154, 0.9),
                ColorUtil.rgb_pct(254, 238, 146, 0.9),
                ColorUtil.rgb_pct(254, 215, 121, 0.9),
                ColorUtil.rgb_pct(244, 197, 136, 0.9),
                ColorUtil.rgb_pct(235, 194, 164, 0.9),
                ColorUtil.rgb_pct(224, 207, 194, 0.9),
                ColorUtil.rgb_pct(239, 231, 225, 0.9),
                ColorUtil.rgb_pct(255, 255, 255, 0.9)
        };
        return r;
    }

    public boolean isNonlinear();
    
    public Color mapHeight(double h, double seaMin, double landMax);
    public String toHtml(double seaMin, double landMax);
}
