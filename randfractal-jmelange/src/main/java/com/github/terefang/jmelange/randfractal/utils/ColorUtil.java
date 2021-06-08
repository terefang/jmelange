package com.github.terefang.jmelange.randfractal.utils;

import java.awt.*;

/**
 * Created by fredo on 18.10.15.
 */
public class ColorUtil
{
    public static Color rgb_pct(int r, int g, int b, double p)
    {
        r = MathHelper.lerp(0,r,p);
        g = MathHelper.lerp(0,g,p);
        b = MathHelper.lerp(0,b,p);
        return new Color(r,g,b);
    }

    public static int HSV_to_RGB(float $h, float $s, float $v)
    {
        //# force $h to 0 <= $h < 360
        //# FIXME should not loop, looks infinite
        while ($h < 0f) { $h += 360.0f; }
        while ($h >= 360f) { $h -= 360.0f; }

        $h /= 60.0;                       //## sector 0 to 5
        int $i = (int)Math.floor( $h );
        float $f = $h - $i;               //## fractional part of h
        float $p = $v * ( 1 - $s );
        float $q = $v * ( 1 - $s * $f );
        float $t = $v * ( 1 - $s * ( 1 - $f ) );
        int $r, $b, $g;
        if($i == 0)
        {
            $r = (int)$v;
            $g = (int)$t;
            $b = (int)$p;
        }
        else if($i == 1)
        {
            $r = (int)$q;
            $g = (int)$v;
            $b = (int)$p;
        }
        else if($i == 2)
        {
            $r = (int)$p;
            $g = (int)$v;
            $b = (int)$t;
        }
        else if($i == 3)
        {
            $r = (int)$p;
            $g = (int)$q;
            $b = (int)$v;
        }
        else if($i == 4)
        {
            $r = (int)$t;
            $g = (int)$p;
            $b = (int)$v;
        }
        else //# if $i == 5
        {
            $r = (int)$v;
            $g = (int)$p;
            $b = (int)$q;
        }
        return ( (($r&0xff)<<16) | (($g&0xff)<<8) | ($b&0xff) );
    }
}
