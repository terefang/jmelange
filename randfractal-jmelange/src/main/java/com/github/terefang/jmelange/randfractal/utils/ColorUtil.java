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

    public static Color from(int _r, int _g, int _b)
    {
        return new Color(_r, _g, _b);
    }
    public static Color from(int _rgb)
    {
        return new Color(_rgb);
    }
    public static Color from(int _rgba, boolean _ha)
    {
        return new Color(_rgba, _ha);
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

        return from((int)r, (int)g, (int)b);
    }

    static float RgbToHsvV(float _r, float _g, float _b) {
        return Math.max(_r,Math.max(_g,_b));
    }

    static float RgbToHsvS(float _r, float _g, float _b) {
        float rgbMin = Math.min(_r,Math.min(_g,_b));
        float rgbMax = Math.max(_r,Math.max(_g,_b));
        float chroma = rgbMax - rgbMin;
        float L = (rgbMax+rgbMin)/2f;
        float V = L + (chroma/2f);
        if(V<=0f) return 0f;
        return (chroma/V);
    }

    static float RgbToHsvH(float _r, float _g, float _b) {
        float rgbMin = Math.min(_r,Math.min(_g,_b));
        float rgbMax = Math.max(_r,Math.max(_g,_b));
        float chroma = rgbMax - rgbMin;
        float L = (rgbMax+rgbMin)/2f;
        float V = rgbMax;

        float _H = 0f;
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

    static float HueToRGB(float p, float q, float h) {
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
                return from(q, (int)v, p);
            case 2:
                return from(p, (int)v, t);
            case 3:
                return from(p, q, (int)v);
            case 4:
                return from(t, p, (int)v);
            case 5:
                return from((int)v, p, q);
            case 0:
            default:
                return from((int)v, t, p);
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

        return from((int)(r * 255), (int)(g * 255), (int)(b * 255));
    }

    public static Color fromHCG(float h, float c, float g)
    {
        h /= 360f;
        c /= 100f;
        g /= 100f;

        if (c == 0.0) {
            return from((int)(g * 255), (int)(g * 255), (int)(g * 255));
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

        return from(
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

        return from(
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

    public static Color fromLCH(float l, float c, float h)
    {
        float hr = (float) (h / 360f * 2f * Math.PI);
        float a = (float) (c * Math.cos(hr));
        float b = (float) (c * Math.sin(hr));

        return fromLAB(l, a, b);
    }

    public static Color setSaturation(Color _cl, float _s)
    {
        float _r = _cl.getRed()/255f;
        float _g = _cl.getGreen()/255f;
        float _b = _cl.getBlue()/255f;
        float _V = RgbToHsvV(_r,_g,_b);
        float _H = RgbToHsvH(_r,_g,_b);
        return fromHSV(_H, _s, _V);
    }

    public static Color adjSaturation(Color _cl, float _s)
    {
        float _r = _cl.getRed()/255f;
        float _g = _cl.getGreen()/255f;
        float _b = _cl.getBlue()/255f;
        float _V = RgbToHsvV(_r,_g,_b);
        float _S = RgbToHsvS(_r,_g,_b);
        float _H = RgbToHsvH(_r,_g,_b);
        return fromHSV(_H, _S*_s, _V);
    }

    public static Color biasSaturation(Color _cl, float _s)
    {
        float _r = _cl.getRed()/255f;
        float _g = _cl.getGreen()/255f;
        float _b = _cl.getBlue()/255f;
        float _V = RgbToHsvV(_r,_g,_b);
        float _S = RgbToHsvS(_r,_g,_b);
        float _H = RgbToHsvH(_r,_g,_b);
        return fromHSV(_H, _S+_s, _V);
    }

    public static Color setValue(Color _cl, float _v)
    {
        float _r = _cl.getRed()/255f;
        float _g = _cl.getGreen()/255f;
        float _b = _cl.getBlue()/255f;
        float _S = RgbToHsvS(_r,_g,_b);
        float _H = RgbToHsvH(_r,_g,_b);
        return fromHSV(_H, _S, _v);
    }

    public static Color adjValue(Color _cl, float _v)
    {
        float _r = _cl.getRed()/255f;
        float _g = _cl.getGreen()/255f;
        float _b = _cl.getBlue()/255f;
        float _V = RgbToHsvV(_r,_g,_b);
        float _S = RgbToHsvS(_r,_g,_b);
        float _H = RgbToHsvH(_r,_g,_b);
        return fromHSV(_H, _S, _V*_v);
    }

    public static Color biasValue(Color _cl, float _v)
    {
        float _r = _cl.getRed()/255f;
        float _g = _cl.getGreen()/255f;
        float _b = _cl.getBlue()/255f;
        float _V = RgbToHsvV(_r,_g,_b);
        float _S = RgbToHsvS(_r,_g,_b);
        float _H = RgbToHsvH(_r,_g,_b);
        return fromHSV(_H, _S, _V+_v);
    }

    public static Color setHue(Color _cl, float _h)
    {
        float _r = _cl.getRed()/255f;
        float _g = _cl.getGreen()/255f;
        float _b = _cl.getBlue()/255f;
        float _V = RgbToHsvV(_r,_g,_b);
        float _S = RgbToHsvS(_r,_g,_b);
        return fromHSV(_h, _S, _V);
    }

    public static Color biasHue(Color _cl, float _h)
    {
        float _r = _cl.getRed()/255f;
        float _g = _cl.getGreen()/255f;
        float _b = _cl.getBlue()/255f;
        float _V = RgbToHsvV(_r,_g,_b);
        float _S = RgbToHsvS(_r,_g,_b);
        float _H = RgbToHsvH(_r,_g,_b);
        return fromHSV(_H+_h, _S, _V);
    }

    public static Color colorLerp(Color a, Color b, float t)
    {
        float _ar = a.getRed()/255f;
        float _ag = a.getGreen()/255f;
        float _ab = a.getBlue()/255f;
        float _br = b.getRed()/255f;
        float _bg = b.getGreen()/255f;
        float _bb = b.getBlue()/255f;
        float _ah = RgbToHsvH(_ar,_ag, _ab);
        float _bh = RgbToHsvH(_br,_bg, _bb);
        float _ch = MathHelper.lerp(_ah,_bh, t);
        float _as = RgbToHsvS(_ar,_ag, _ab);
        float _bs = RgbToHsvS(_br,_bg, _bb);
        float _cs = MathHelper.lerp(_as,_bs, t);
        float _av = RgbToHsvV(_ar,_ag, _ab);
        float _bv = RgbToHsvV(_br,_bg, _bb);
        float _cv = MathHelper.lerp(_av,_bv, t);
        return fromHSV(_ch, _cs*100f, _cv*100f);
    }
}
