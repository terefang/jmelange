package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.color.ColorDef;
import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ColorUtil
{

    public static int toRGB(Color _c)
    {
        int _i = _c.getRed();
        _i|=_c.getGreen()<<8;
        _i|=_c.getBlue()<<16;
        return _i;
    }
    
    //Given a temperature adjustment on the range -100 to 100,
    // apply the following adjustment to each pixel in the image:
    //
    //r = r + adjustmentValue
    //g = g
    //b = b - adjustmentValue
    
    //Given a tint adjustment on the range -100 to 100,
    // apply the following adjustment to each pixel in the image:
    //
    //r = r
    //g = g + adjustmentValue
    //b = b
    
    public static Color fromARGB(int argb)
    {
        int a = (argb & 0xFF000000) >>> 24;
        int r = (argb & 0x00FF0000) >>> 16;
        int g = (argb & 0x0000FF00) >>> 8;
        int b = (argb & 0x000000FF);
        
        return new Color(r, g, b, a);
    }
    
    /**
     A temperature to color conversion, inspired from a blogpost from PhotoDemon
     (http://www.tannerhelland.com/4435/convert-temperature-rgb-algorithm-code/).
     
     @param temperature The temperature of an ideal black body, in Kelvins;
     @param alpha       If true, the return value will be RGBA instead of RGB.
     @return The corresponding RGB color.
     */
    public static int[] getRgbFromTemperature(double temperature, boolean alpha)
    {
        // Temperature must fit between 1000 and 40000 degrees
        temperature = MathUtil.clamp(temperature, 1000, 40000);
        
        // All calculations require tmpKelvin \ 100, so only do the conversion once
        temperature /= 100;
        
        // Compute each color in turn.
        int red, green, blue;
        // First: red
        if (temperature <= 66)
            red = 255;
        else
        {
            // Note: the R-squared value for this approximation is .988
            red = (int) (329.698727446 * (Math.pow(temperature - 60, -0.1332047592)));
            red = MathUtil.clamp(red, 0, 255);
        }
        
        // Second: green
        if (temperature <= 66)
            // Note: the R-squared value for this approximation is .996
            green = (int) (99.4708025861 * Math.log(temperature) - 161.1195681661);
        else
            // Note: the R-squared value for this approximation is .987
            green = (int) (288.1221695283 * (Math.pow(temperature - 60, -0.0755148492)));
        
        green = MathUtil.clamp(green, 0, 255);
        
        // Third: blue
        if (temperature >= 66)
            blue = 255;
        else if (temperature <= 19)
            blue = 0;
        else
        {
            // Note: the R-squared value for this approximation is .998
            blue = (int) (138.5177312231 * Math.log(temperature - 10) - 305.0447927307);
            
            blue = MathUtil.clamp(blue, 0, 255);
        }
        
        if (alpha)
            return new int[]
                    {
                            red, green, blue, 255
                    };
        else
            return new int[]
                    {
                            red, green, blue
                    };
    }
    
    public static Color fromHSB(float h, float s, float b)
    {
        float[] _rgb = HsbToRgb(h,s,b);
        return fromRgb(_rgb[0],_rgb[1],_rgb[2]);
    }
    
    public static float[] RgbToHsb(float r, float g, float b)
    {
        float hue, saturation, brightness;
        float[] hsbvals = new float[3];
        float cmax = (r > g) ? r : g;
        if (b > cmax) cmax = b;
        float cmin = (r < g) ? r : g;
        if (b < cmin) cmin = b;
        
        brightness = cmax;
        if (cmax != 0)
            saturation = (cmax - cmin) / cmax;
        else
            saturation = 0;
        
        if (saturation == 0) {
            hue = 0;
        } else {
            float redc = (cmax - r) / (cmax - cmin);
            float greenc = (cmax - g) / (cmax - cmin);
            float bluec = (cmax - b) / (cmax - cmin);
            if (r == cmax)
                hue = bluec - greenc;
            else if (g == cmax)
                hue = (float) (2.0 + redc - bluec);
            else
                hue = (float) (4.0 + greenc - redc);
            hue = (float) (hue / 6.0);
            if (hue < 0)
                hue = (float) (hue + 1.0);
        }
        hsbvals[0] = hue * 360;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }
    public static float[] HsbToRgb(float hue, float saturation, float brightness) {
        // normalize the hue
        float normalizedHue = ((hue % 360) + 360) % 360;
        hue = normalizedHue/360;
        
        float r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = brightness;
        } else {
            float h = (float) ((hue - Math.floor(hue)) * 6.0);
            float f = (float) (h - Math.floor(h));
            float p = (float) (brightness * (1.0 - saturation));
            float q = (float) (brightness * (1.0 - saturation * f));
            float t = (float) (brightness * (1.0 - (saturation * (1.0 - f))));
            switch ((int) h) {
                case 0:
                    r = brightness;
                    g = t;
                    b = p;
                    break;
                case 1:
                    r = q;
                    g = brightness;
                    b = p;
                    break;
                case 2:
                    r = p;
                    g = brightness;
                    b = t;
                    break;
                case 3:
                    r = p;
                    g = q;
                    b = brightness;
                    break;
                case 4:
                    r = t;
                    g = p;
                    b = brightness;
                    break;
                case 5:
                    r = brightness;
                    g = p;
                    b = q;
                    break;
            }
        }
        float[] f = new float[3];
        f[0] = r;
        f[1] = g;
        f[2] = b;
        return f;
    }
    
    
    public static final Properties COLOR_ALIASES = new Properties();

    public static Color from(String _color)
    {
        _color = _color.trim().toLowerCase();

        //if(false)
        {

            if (COLOR_ALIASES.size() == 0) {
                try (InputStream _fh = ClasspathResourceLoader.of("color/names.properties", null).getInputStream();) {
                    COLOR_ALIASES.load(_fh);
                } catch (IOException _iox) {
                    // IGNORE
                }
            }

            if (COLOR_ALIASES.containsKey(_color)) _color = COLOR_ALIASES.getProperty(_color, _color).trim();
        }

        try
        {
            ColorDef _tmp = ColorDef.valueOf(_color.toUpperCase());
            if(_tmp != null) return _tmp.getColor();
        }
        catch (IllegalArgumentException _iae)
        {
            // IGNORE
        }

        if(_color.startsWith("#") && _color.length()==4)
        {
            int _cr = CommonUtil.checkInteger("0x"+_color.substring(1,2));
            _cr |= (_cr<<4);
            int _cg = CommonUtil.checkInteger("0x"+_color.substring(2,3));
            _cg |= (_cg<<4);
            int _cb = CommonUtil.checkInteger("0x"+_color.substring(3,4));
            _cb |= (_cb<<4);

            return fromRgb(_cr,_cg,_cb);
        }
        else
        if(_color.startsWith("#") && _color.length()==7)
        {
            int _cr = CommonUtil.checkInteger("0x"+_color.substring(1,3));
            int _cg = CommonUtil.checkInteger("0x"+_color.substring(3,5));
            int _cb = CommonUtil.checkInteger("0x"+_color.substring(5,7));
            return fromRgb(_cr,_cg,_cb);
        }
        else
        if(_color.startsWith("#") && _color.length()==9)
        {
            int _cr = CommonUtil.checkInteger("0x"+_color.substring(1,3));
            int _cg = CommonUtil.checkInteger("0x"+_color.substring(3,5));
            int _cb = CommonUtil.checkInteger("0x"+_color.substring(5,7));
            int _ca = CommonUtil.checkInteger("0x"+_color.substring(7,9));
            return fromRgbA(_cr,_cg,_cb,_ca);
        }
        else
        if(_color.startsWith("#") && _color.length()==10)
        {
            float _cr = CommonUtil.checkInteger("0x"+_color.substring(1,4))/((float)0xfff);
            float _cg = CommonUtil.checkInteger("0x"+_color.substring(4,7))/((float)0xfff);
            float _cb = CommonUtil.checkInteger("0x"+_color.substring(7,10))/((float)0xfff);
            return fromRgb(_cr,_cg,_cb);
        }
        else
        if(_color.startsWith("#") && _color.length()==13)
        {
            float _cr = CommonUtil.checkInteger("0x"+_color.substring(1,4))/((float)0xfff);
            float _cg = CommonUtil.checkInteger("0x"+_color.substring(4,7))/((float)0xfff);
            float _cb = CommonUtil.checkInteger("0x"+_color.substring(7,10))/((float)0xfff);
            float _ca = CommonUtil.checkInteger("0x"+_color.substring(10,13))/((float)0xfff);
            return fromRgbA(_cr,_cg,_cb,_ca);
        }
        else
        if(_color.startsWith("rgb(") && _color.endsWith(")"))
        {
            String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
            int[] _cv = new int[_parts.length];
            for(int i=0; i<_parts.length; i++)
            {
                String _ct = _parts[i].trim();
                if(_ct.endsWith("%"))
                {
                    _cv[i] = (int) ((Float.parseFloat(_ct.substring(0, _ct.length()-1))*255f)/100f);
                }
                else
                {
                    _cv[i] = CommonUtil.checkInteger(_ct);
                }
            }

            return fromRgb(_cv[0],_cv[1],_cv[2]);
        }
        else
        if(_color.startsWith("cmyk(") && _color.endsWith(")"))
        {
            String[] _parts = CommonUtil.split(_color.substring(5,_color.length()-1), ",");
            int[] _cv = new int[_parts.length];
            for(int i=0; i<_parts.length; i++)
            {
                String _ct = _parts[i].trim();
                if(_ct.endsWith("%"))
                {
                    _cv[i] = (int) ((Float.parseFloat(_ct.substring(0, _ct.length()-1))*255f)/100f);
                }
                else
                {
                    _cv[i] = CommonUtil.checkInteger(_ct);
                }
            }

            return fromCmyk(_cv[0],_cv[1],_cv[2],_cv[3]);
        }
        else
        if(_color.startsWith("hsl(") && _color.endsWith(")"))
        {
            String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
            float[] _cv = new float[_parts.length];

            _cv[0] = CommonUtil.toFloat(_parts[0].trim());

            for(int i=1; i<_parts.length; i++)
            {
                String _ct = _parts[i].trim();
                if(_ct.endsWith("%"))
                {
                    _cv[i] = CommonUtil.toFloat(_ct.substring(0, _ct.length()-1));
                }
                else
                {
                    _cv[i] = CommonUtil.toFloat(_ct)*100;
                }
            }

            return fromHSL(_cv[0],_cv[1],_cv[2]);
        }
        else
        if(_color.startsWith("hsv(") && _color.endsWith(")"))
        {
            String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
            float[] _cv = new float[_parts.length];
            
            _cv[0] = CommonUtil.toFloat(_parts[0].trim());
            
            for(int i=1; i<_parts.length; i++)
            {
                String _ct = _parts[i].trim();
                if(_ct.endsWith("%"))
                {
                    _cv[i] = CommonUtil.toFloat(_ct.substring(0, _ct.length()-1));
                }
                else
                {
                    _cv[i] = CommonUtil.toFloat(_ct)*100;
                }
            }
            
            return fromHSV(_cv[0],_cv[1],_cv[2]);
        }
        else
        if(_color.startsWith("hsb(") && _color.endsWith(")"))
        {
            String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
            float[] _cv = new float[_parts.length];
            
            _cv[0] = CommonUtil.toFloat(_parts[0].trim());
            
            for(int i=1; i<_parts.length; i++)
            {
                String _ct = _parts[i].trim();
                if(_ct.endsWith("%"))
                {
                    _cv[i] = CommonUtil.toFloat(_ct.substring(0, _ct.length()-1));
                }
                else
                {
                    _cv[i] = CommonUtil.toFloat(_ct)*100;
                }
            }
            
            return fromHSB(_cv[0],_cv[1],_cv[2]);
        }
        else
        if(_color.startsWith("hwb(") && _color.endsWith(")"))
        {
            String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
            float[] _cv = new float[_parts.length];

            _cv[0] = CommonUtil.toFloat(_parts[0].trim());

            for(int i=1; i<_parts.length; i++)
            {
                String _ct = _parts[i].trim();
                if(_ct.endsWith("%"))
                {
                    _cv[i] = CommonUtil.toFloat(_ct.substring(0, _ct.length()-1));
                }
                else
                {
                    _cv[i] = CommonUtil.toFloat(_ct)*100;
                }
            }

            return fromHWB(_cv[0],_cv[1],_cv[2]);
        }
        else
        if(_color.startsWith("hcg(") && _color.endsWith(")"))
        {
            String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
            float[] _cv = new float[_parts.length];

            _cv[0] = CommonUtil.toFloat(_parts[0].trim());

            for(int i=1; i<_parts.length; i++)
            {
                String _ct = _parts[i].trim();
                if(_ct.endsWith("%"))
                {
                    _cv[i] = CommonUtil.toFloat(_ct.substring(0, _ct.length()-1));
                }
                else
                {
                    _cv[i] = CommonUtil.toFloat(_ct)*100;
                }
            }

            return fromHCG(_cv[0],_cv[1],_cv[2]);
        }
        else
        if(_color.startsWith("xyz(") && _color.endsWith(")"))
        {
            String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
            float[] _cv = new float[_parts.length];

            for(int i=0; i<_parts.length; i++)
            {
                String _ct = _parts[i].trim();
                _cv[i] = CommonUtil.toFloat(_ct);
            }

            return fromXYZ(_cv[0],_cv[1],_cv[2]);
        }
        else
        if(_color.startsWith("lab(") && _color.endsWith(")"))
        {
            String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
            float[] _cv = new float[_parts.length];

            for(int i=0; i<_parts.length; i++)
            {
                String _ct = _parts[i].trim();
                _cv[i] = CommonUtil.toFloat(_ct);
            }

            return fromLAB(_cv[0],_cv[1],_cv[2]);
        }
        else
        if(_color.startsWith("lch(") && _color.endsWith(")"))
        {
            String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
            float[] _cv = new float[_parts.length];

            for(int i=0; i<_parts.length; i++)
            {
                String _ct = _parts[i].trim();
                _cv[i] = CommonUtil.toFloat(_ct);
            }

            return fromLCH(_cv[0],_cv[1],_cv[2]);
        }
        else
        {
            throw new IllegalArgumentException(_color);
        }
    }

    public static Color fromCmyk(int _c, int _m, int _y, int _k)
    {
        int _R = (int) ((255 - _c) * (255 - _k));
        int _G = (int) ((255 - _m) * (255 - _k));
        int _B = (int) ((255 - _y) * (255 - _k));
        return fromRgb(_R, _G, _B);
    }

    public static Color fromCmyk(float _c, float _m, float _y, float _k)
    {
        int _R = (int) (255f * ((1f - _c) * (1f - _k)));
        int _G = (int) (255f * ((1f - _m) * (1f - _k)));
        int _B = (int) (255f * ((1f - _y) * (1f - _k)));
        return fromRgb(_R, _G, _B);
    }
    
    
    public static Color fromRgbA(int _r, int _g, int _b, int _a)
    {
        return new Color(_r, _g,_b,_a);
    }
    public static Color fromRgb(int _r, int _g, int _b)
    {
        return fromRgbA(_r,_g,_b,255);
    }

    public static Color fromRgbA(float _r, float _g, float _b, float _a)
    {
        return new Color(_r, _g,_b,_a);
    }
    public static Color fromRgb(float _r, float _g, float _b)
    {
        return fromRgbA(_r,_g,_b,1f);
    }

    public static Color fromGreyA(float _g, float _a)
    {
        return new Color(_g, _g,_g,_a);
    }

    public static Color fromGrey(float _g)
    {
        return fromGreyA(_g,1f);
    }

    public static Color fromGreyA(int _g, int _a)
    {
        return new Color(_g, _g,_g,_a);
    }

    public static Color fromGrey(int _g)
    {
        return fromGreyA(_g,255);
    }
    
    public static Color fromHSL(float h, float s, float l)
    {
        h = h % 360.0f;
        h /= 360f;
        s /= 100f;
        l /= 100f;

        float q = 0;

        if (l < 0.5)
            q = l * (1 + s);
        else
            q = (l + s) - (s * l);

        float p = 2 * l - q;

        float r = Math.max(0, HueToRGB(p, q, h + (1.0f / 3.0f)));
        float g = Math.max(0, HueToRGB(p, q, h));
        float b = Math.max(0, HueToRGB(p, q, h - (1.0f / 3.0f)));

        r = Math.min(r, 1.0f)*255;
        g = Math.min(g, 1.0f)*255;
        b = Math.min(b, 1.0f)*255;

        return fromRgb((int)r, (int)g, (int)b);
    }

    static float HueToRGB(float p, float q, float h)
    {
        if (h < 0)
            h += 1;

        if (h > 1)
            h -= 1;

        if (6 * h < 1) {
            return p + ((q - p) * 6 * h);
        }

        if (2 * h < 1) {
            return q;
        }

        if (3 * h < 2) {
            return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
        }

        return p;
    }

    public static Color fromHSV(float h, float s, float v) {
        h /= 60f;
        s /= 100f;
        v /= 100f;
        int hi = (int) (Math.floor(h) % 6);

        float f = (float) (h - Math.floor(h));
        int p = (int) (255 * v * (1 - s));
        int q = (int) (255 * v * (1 - (s * f)));
        int t = (int) (255 * v * (1 - (s * (1 - f))));
        v *= 255;

        switch (hi) {
            case 1:
                return fromRgb(q, (int)v, p);
            case 2:
                return fromRgb(p, (int)v, t);
            case 3:
                return fromRgb(p, q, (int)v);
            case 4:
                return fromRgb(t, p, (int)v);
            case 5:
                return fromRgb((int)v, p, q);
            case 0:
            default:
                return fromRgb((int)v, t, p);
        }
    }

    // http://dev.w3.org/csswg/css-color/#hwb-to-rgb
    public static Color fromHWB(float h, float wh, float bl)
    {
        h /= 360f;
        wh /= 100f;
        bl /= 100f;

        float ratio = wh + bl;
        int i;
        float v;
        float f;
        float n;

        float r;
        float g;
        float b;

        // wh + bl cant be > 1
        if (ratio > 1) {
            wh /= ratio;
            bl /= ratio;
        }

        i = (int) Math.floor(6 * h);
        v = 1 - bl;
        f = 6 * h - i;

        if ((i & 0x01) != 0)
        {
            f = 1 - f;
        }

        n = wh + f * (v - wh); // linear interpolation

        switch (i) {
            default:
            case 6:
            case 0: r = v; g = n; b = wh; break;
            case 1: r = n; g = v; b = wh; break;
            case 2: r = wh; g = v; b = n; break;
            case 3: r = wh; g = n; b = v; break;
            case 4: r = n; g = wh; b = v; break;
            case 5: r = v; g = wh; b = n; break;
        }

        return fromRgb((int)(r * 255), (int)(g * 255), (int)(b * 255));
    }

    public static Color fromHCG(float h, float c, float g)
    {
        h /= 360f;
        c /= 100f;
        g /= 100f;

        if (c == 0.0) {
            return fromGrey((int)(g * 255));
        }

        float[] pure = new float[3];
        float hi = (h % 1) * 6;
        float v = hi % 1;
        float w = 1 - v;
        float mg = 0;

        switch ((int) Math.floor(hi)) {
            case 0:
                pure[0] = 1; pure[1] = v; pure[2] = 0; break;
            case 1:
                pure[0] = w; pure[1] = 1; pure[2] = 0; break;
            case 2:
                pure[0] = 0; pure[1] = 1; pure[2] = v; break;
            case 3:
                pure[0] = 0; pure[1] = w; pure[2] = 1; break;
            case 4:
                pure[0] = v; pure[1] = 0; pure[2] = 1; break;
            default:
                pure[0] = 1; pure[1] = 0; pure[2] = w;
        }

        mg = (1f - c) * g;

        return fromRgb(
                (int)((c * pure[0] + mg) * 255),
                (int)((c * pure[1] + mg) * 255),
                (int)((c * pure[2] + mg) * 255)
        );
    }

    public static Color fromXYZ(float x, float y, float z)
    {
        x /= 100f;
        y /= 100f;
        z /= 100f;
        float r;
        float g;
        float b;

        r = (x * 3.2406f) + (y * -1.5372f) + (z * -0.4986f);
        g = (x * -0.9689f) + (y * 1.8758f) + (z * 0.0415f);
        b = (x * 0.0557f) + (y * -0.2040f) + (z * 1.0570f);

        // assume sRGB
        r = r > 0.0031308f
                ? (float) ((1.055f * Math.pow(r, 1.0f / 2.4f)) - 0.055f)
                : r * 12.92f;

        g = g > 0.0031308f
                ? (float) ((1.055f * Math.pow(g, 1.0f / 2.4f)) - 0.055f)
                : g * 12.92f;

        b = b > 0.0031308f
                ? (float) ((1.055f * Math.pow(b, 1.0f / 2.4f)) - 0.055f)
                : b * 12.92f;

        r = Math.min(Math.max(0, r), 1);
        g = Math.min(Math.max(0, g), 1);
        b = Math.min(Math.max(0, b), 1);

        return fromRgb(
                (int)(r * 255),
                (int)(g * 255),
                (int)(b * 255)
        );
    }
    
    public static Color fromLAB(float l, float a, float b)
    {
        float x;
        float y;
        float z;
        
        y = (l + 16f) / 116f;
        x = a / 500f + y;
        z = y - b / 200f;
        
        float y2 = (float) Math.pow(y, 3);
        float x2 = (float) Math.pow(x, 3);
        float z2 = (float) Math.pow(z, 3);
        y = y2 > 0.008856f ? y2 : (y - 16f / 116f) / 7.787f;
        x = x2 > 0.008856f ? x2 : (x - 16f / 116f) / 7.787f;
        z = z2 > 0.008856f ? z2 : (z - 16f / 116f) / 7.787f;
        
        x *= 95.047f;
        y *= 100f;
        z *= 108.883f;
        
        return fromXYZ(x, y, z);
    }
    
    public static Color fromOkLAB(float l, float a, float b)
    {
        float [] rgb = oklab_to_linear_srgb(l,a,b);
        return fromRgb(rgb[0],rgb[1],rgb[2]);
    }
    
    
    public static Color fromLCH(float l, float c, float h)
    {
        float hr = (float) (h / 360f * 2f * Math.PI);
        float a = (float) (c * Math.cos(hr));
        float b = (float) (c * Math.sin(hr));

        return fromLAB(l, a, b);
    }

    static double RgbToHsvV(double _r, double _g, double _b) {
        return Math.max(_r,Math.max(_g,_b));
    }

    static double RgbToHsvS(double _r, double _g, double _b) {
        double rgbMin = Math.min(_r,Math.min(_g,_b));
        double rgbMax = Math.max(_r,Math.max(_g,_b));
        double chroma = rgbMax - rgbMin;
        double L = (rgbMax+rgbMin)/2f;
        double V = L + (chroma/2f);
        if(V<=0f) return 0f;
        return (chroma/V);
    }

    
    static double RgbToHsvH(Color _rgb)
    {
        return RgbToHsvH(((double)_rgb.getRed())/255., ((double)_rgb.getGreen())/255., ((double)_rgb.getBlue())/255.);
    }
    static double RgbToHsvH(double _r, double _g, double _b) {
        double rgbMin = Math.min(_r,Math.min(_g,_b));
        double rgbMax = Math.max(_r,Math.max(_g,_b));
        double chroma = rgbMax - rgbMin;
        double L = (rgbMax+rgbMin)/2f;
        double V = rgbMax;

        double _H = 0f;
        if(chroma <= 0f) return _H;
        if(_r == V)
        {
            _H = (((_g-_b)/chroma) + (_g < _b ? 6 : 0)) % 6;
        }
        else
        if(_g == V)
        {
            _H = ((_b-_r)/chroma) + 2;
        }
        else
        if(_b == V)
        {
            _H = ((_r-_g)/chroma) + 4;
        }

        return 60f * _H;
    }

    static double HueToRGB(double p, double q, double h) {
        if (h < 0)
            h += 1;

        if (h > 1)
            h -= 1;

        if (6 * h < 1) {
            return p + ((q - p) * 6 * h);
        }

        if (2 * h < 1) {
            return q;
        }

        if (3 * h < 2) {
            return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
        }

        return p;
    }

    public static Color setSaturation(Color _cl, double _s)
    {
        double _r = _cl.getRed()/255f;
        double _g = _cl.getGreen()/255f;
        double _b = _cl.getBlue()/255f;
        double _V = RgbToHsvV(_r,_g,_b)*100.;
        double _H = RgbToHsvH(_r,_g,_b);
        return fromHSV((float) _H, (float) _s, (float) _V);
    }

    public static Color adjSaturation(Color _cl, double _s)
    {
        double _r = _cl.getRed()/255f;
        double _g = _cl.getGreen()/255f;
        double _b = _cl.getBlue()/255f;
        double _V = RgbToHsvV(_r,_g,_b)*100.;
        double _S = RgbToHsvS(_r,_g,_b)*100.;
        double _H = RgbToHsvH(_r,_g,_b);
        return fromHSV((float) _H, (float) (_S*_s), (float) _V);
    }

    public static Color biasSaturation(Color _cl, double _s)
    {
        double _r = _cl.getRed()/255f;
        double _g = _cl.getGreen()/255f;
        double _b = _cl.getBlue()/255f;
        double _V = RgbToHsvV(_r,_g,_b)*100.;
        double _S = RgbToHsvS(_r,_g,_b)*100.;
        double _H = RgbToHsvH(_r,_g,_b);
        return fromHSV((float) _H, (float) (_S+_s), (float) _V);
    }
    
    public static Color setValue(Color _cl, double _v)
    {
        double _r = _cl.getRed()/255f;
        double _g = _cl.getGreen()/255f;
        double _b = _cl.getBlue()/255f;
        //double _V = RgbToHsvV(_r,_g,_b)*100.;
        double _S = RgbToHsvS(_r,_g,_b)*100.;
        double _H = RgbToHsvH(_r,_g,_b);
        return fromHSV((float) _H, (float) _S, (float) (_v));
    }

    public static Color setLightness(Color _cl, double _v)
    {
        double _r = _cl.getRed()/255f;
        double _g = _cl.getGreen()/255f;
        double _b = _cl.getBlue()/255f;
        double _S = RgbToHsvS(_r,_g,_b)*100.;
        double _H = RgbToHsvH(_r,_g,_b);
        return fromHSL((float) _H, (float) _S, (float) (_v));
    }

    public static Color adjValue(Color _cl, double _v)
    {
        double _r = _cl.getRed()/255f;
        double _g = _cl.getGreen()/255f;
        double _b = _cl.getBlue()/255f;
        double _V = RgbToHsvV(_r,_g,_b)*100.;
        double _S = RgbToHsvS(_r,_g,_b)*100.;
        double _H = RgbToHsvH(_r,_g,_b);
        return fromHSV((float) _H, (float) _S, (float) (_V*_v));
    }

    public static Color biasValue(Color _cl, double _v)
    {
        double _r = _cl.getRed()/255f;
        double _g = _cl.getGreen()/255f;
        double _b = _cl.getBlue()/255f;
        double _V = RgbToHsvV(_r,_g,_b)*100.;
        double _S = RgbToHsvS(_r,_g,_b)*100.;
        double _H = RgbToHsvH(_r,_g,_b);
        return fromHSV((float) _H, (float) _S, (float) (_V+_v));
    }

    public static Color setHue(Color _cl, double _h)
    {
        double _r = _cl.getRed()/255f;
        double _g = _cl.getGreen()/255f;
        double _b = _cl.getBlue()/255f;
        double _V = RgbToHsvV(_r,_g,_b)*100.;
        double _S = RgbToHsvS(_r,_g,_b)*100.;
        return fromHSV((float) _h, (float) _S, (float) _V);
    }

    public static Color biasHue(Color _cl, double _h)
    {
        double _r = _cl.getRed()/255f;
        double _g = _cl.getGreen()/255f;
        double _b = _cl.getBlue()/255f;
        double _V = RgbToHsvV(_r,_g,_b)*100.;
        double _S = RgbToHsvS(_r,_g,_b)*100.;
        double _H = RgbToHsvH(_r,_g,_b);
        return fromHSV((float) (_H+_h), (float) (_S), (float) (_V));
    }

    public static Color setAlpha(Color _c,float a)
    {
        return new Color(_c.getRed(), _c.getGreen(), _c.getBlue(), (int)(255*a));
    }

    public static Color setAlpha(Color _c,int a)
    {
        return new Color(_c.getRed(), _c.getGreen(), _c.getBlue(), a);
    }
    public static Color rgbLerp(Color a, Color b, double t)
    {
        return setAlpha(fromRgb(
                MathUtil.lerp(a.getRed(), b.getRed(), t),
                MathUtil.lerp(a.getGreen(), b.getGreen(), t),
                MathUtil.lerp(a.getBlue(), b.getBlue(), t)
                ),
                MathUtil.lerp(a.getAlpha(),b.getAlpha(),t)
        );
    }

    public static Color hsvLerp(Color a, Color b, double t)
    {
        double _ar = a.getRed()/255f;
        double _ag = a.getGreen()/255f;
        double _ab = a.getBlue()/255f;
        double _br = b.getRed()/255f;
        double _bg = b.getGreen()/255f;
        double _bb = b.getBlue()/255f;
        double _ah = RgbToHsvH(_ar,_ag, _ab);
        double _bh = RgbToHsvH(_br,_bg, _bb);
        double _ch = MathUtil.lerp(_ah,_bh, t);
        double _as = RgbToHsvS(_ar,_ag, _ab);
        double _bs = RgbToHsvS(_br,_bg, _bb);
        double _cs = MathUtil.lerp(_as,_bs, t);
        double _av = RgbToHsvV(_ar,_ag, _ab);
        double _bv = RgbToHsvV(_br,_bg, _bb);
        double _cv = MathUtil.lerp(_av,_bv, t);
        return setAlpha(fromHSV((float) _ch, (float) (_cs*100f), (float) (_cv*100f)),
                MathUtil.lerp(a.getAlpha(),b.getAlpha(),t)
        );
    }

    public static int SIZE = 38;
    public static double GAMMA = 2.4;
    public static double EPSILON = 0.00000001;


    public static double compareDistance(Color _a , Color _b)
    {
        double _distance = 0.;
        float[] _av = _a.getComponents(new float[4]);
        float[] _bv = _b.getComponents(new float[4]);
        for(int _i = 0; _i< _av.length; _i++)
        {
            _distance+= Math.abs(_av[_i]-_bv[_i]);
        }
        _distance /= _av.length;
        return _distance;
    }

    public static double compareHueDistance(Color _a , Color _b)
    {
        return Math.abs(RgbToHsvH(_a)-RgbToHsvH(_b));
    }
    
    public static Color toPerceptiveGrey(Color _color)
    {
        return toPerceptiveGrey(_color, 1.0);
    }
    
    public static double toPerceptiveGreyValue(Color _color)
    {
        return toPerceptiveGreyValue(_color.getRed(), _color.getGreen(), _color.getBlue(), 1.0);
    }
    
    public static Color toPerceptiveGrey(Color _color, double _gamma)
    {
        double _l = toPerceptiveGreyValue(_color.getRed(),_color.getGreen(),_color.getBlue(),_gamma)*255./100.;
        int _il = (int) _l;
        if(_il<0) _il=0;
        if(_il>255) _il=255;
        return fromGrey(_il);
    }
    
    public static double toPerceptiveGreyValue(int _r,int _g, int _b)
    {
        return toPerceptiveGreyValue(_r,_g,_b, 1.0);
    }

    public static double toPerceptiveGreyValue(int _r,int _g, int _b, double _gamma)
    {
        double _vr = _r/255.;
        double _vg = _g/255.;
        double _vb = _b/255.;
        _vr = (.2126 * Math.pow(_vr,_gamma));
        _vg = (.7152 * Math.pow(_vg,_gamma));
        _vb = (.0722 * Math.pow(_vb,_gamma));
        double _Y = _vr + _vg + _vb;
        double _Lstar = (116. * Math.pow(_Y,1./3.)) - 16.;
        if(_Lstar<0. || _Lstar>100.) System.err.println("L* Gamut warning "+_Lstar);
        return _Lstar; // 0 - 100 %
    }
    
    public static String toNum(Color _c)
    {
        return String.format("%d,%d,%d", _c.getRed(), _c.getGreen(), _c.getBlue());
    }

    public static String toHex(Color _c)
    {
        return String.format("#%02X%02X%02X", _c.getRed(), _c.getGreen(), _c.getBlue());
    }

    public static String toHexWithAlpha(Color _c)
    {
        return String.format("#%02X%02X%02X%02X", _c.getAlpha(), _c.getRed(), _c.getGreen(), _c.getBlue());
    }
    public static void main(String[] args)
    {
        Color _a   = from("#ff0000");
        Color _b   = from("#00ff00");
        Color _tmp = hsvLerp(_a, _b, 0.5);
        System.err.println(_tmp);
        _tmp = rgbLerp(_a, _b, 0.5);
        System.err.println(_tmp);
        System.err.println(compareDistance(_a, _b));
        System.err.println(compareHueDistance(_a, _b));
        System.err.println(toPerceptiveGrey(_a));
        System.err.println(toPerceptiveGrey(_a, 1. / 2.2));
        System.err.println(toPerceptiveGrey(_tmp));
        System.err.println(toPerceptiveGrey(_tmp, 1. / 2.2));
        
        int _i=1;
        for (String _c : new String[]{"#020202", "#92929a", "#dddddd", "#ffc97f", "#ff5fb4", "#ff1414", "#b90059", "#bb5d00", "#ff9a00", "#ffe302", "#0ad200", "#009dae", "#11d6ff", "#0054ff", "#3f00b8", "#a000ff"})
        {
            _tmp = from(_c);
            String _fc = "black";
            double _pg =toPerceptiveGreyValue(_tmp);
            if(_pg<88.0)
            {
                _fc = "white";
            }
            System.err.println(String.format("pal-%d: (stroke: rgb(%s), fill: rgb(%s), title: %s), //%f", _i++,toNum(_tmp),toNum(rgbLerp(_tmp,Color.WHITE,0.92)), _fc, _pg));
        }
        
        
        _i=1;
        for (String _c : new String[]{"#020202", "#92929a", "#dddddd", "#ffc97f", "#ff5fb4", "#ff1414", "#b90059", "#bb5d00", "#ff9a00", "#ffe302", "#0ad200", "#009dae", "#11d6ff", "#0054ff", "#3f00b8", "#a000ff"})
        {
            _tmp = from(_c);
            Color _end = setLightness(_tmp,98.);//rgbLerp(_tmp,Color.WHITE,0.92)
            String _fc = "black";
            double _pg =toPerceptiveGreyValue(_tmp);
            if(_pg<88.0)
            {
                _fc = "white";
            }
            System.err.println(String.format("pal-%d: (stroke: rgb(%s), fill: rgb(%s), title: %s), //%f", _i++,toNum(_tmp),toNum(_end), _fc, _pg));
        }
        
    }
    
    
    static float[] linear_srgb_to_oklab(float[] rgb)
    {
        return linear_srgb_to_oklab(rgb[0],rgb[1],rgb[2]);
    }
    
    static float[] linear_srgb_to_oklab(float _r,float _g,float _b)
    {
        float l = 0.4122214708f * _r + 0.5363325363f * _g + 0.0514459929f * _b;
        float m = 0.2119034982f * _r + 0.6806995451f * _g + 0.1073969566f * _b;
        float s = 0.0883024619f * _r + 0.2817188376f * _g + 0.6299787005f * _b;
        
        float l_ = (float) Math.pow(l,1./3.);
        float m_ = (float) Math.pow(m,1./3.);
        float s_ = (float) Math.pow(s,1./3.);
        
        return new float[]{
                0.2104542553f*l_ + 0.7936177850f*m_ - 0.0040720468f*s_,
                1.9779984951f*l_ - 2.4285922050f*m_ + 0.4505937099f*s_,
                0.0259040371f*l_ + 0.7827717662f*m_ - 0.8086757660f*s_,
        };
    }
    
    static float[] oklab_to_linear_srgb(float[] lab)
    {
        return oklab_to_linear_srgb(lab[0],lab[1],lab[2]);
    }
    
    static float[] oklab_to_linear_srgb(float _L, float _a, float _b)
    {
        float l_ = _L + 0.3963377774f * _a + 0.2158037573f * _b;
        float m_ = _L - 0.1055613458f * _a - 0.0638541728f * _b;
        float s_ = _L - 0.0894841775f * _a - 1.2914855480f * _b;
        
        float l = l_*l_*l_;
        float m = m_*m_*m_;
        float s = s_*s_*s_;
        
        return new float[]{
                +4.0767416621f * l - 3.3077115913f * m + 0.2309699292f * s,
                -1.2684380046f * l + 2.6097574011f * m - 0.3413193965f * s,
                -0.0041960863f * l - 0.7034186147f * m + 1.7076147010f * s,
        };
    }
}
