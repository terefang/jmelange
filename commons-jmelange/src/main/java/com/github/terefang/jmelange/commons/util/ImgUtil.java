package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.commons.CommonUtil;
import lombok.SneakyThrows;

import javax.imageio.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ImgUtil
{
    public static BufferedImage toBufferedImage(Image img, ImageObserver obs, ColorSpace _cs)
    {
        return toBufferedImage(img, obs, new ComponentColorModel(_cs, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE));
    }
    
    @SneakyThrows
    public static ColorModel loadPalette(File _file)
    {
        if(_file.getName().endsWith(".hex"))
        {
            return loadPaletteHEX(new FileReader(_file, StandardCharsets.UTF_8));
        }
        else if(_file.getName().endsWith(".gpl"))
        {
            return loadPaletteGPL(new FileReader(_file, StandardCharsets.UTF_8));
        }
        else if(_file.getName().endsWith(".txt"))
        {
            return loadPalettePNET(new FileReader(_file, StandardCharsets.UTF_8));
        }
        else if(_file.getName().endsWith(".pal"))
        {
            return loadPalettePAL(new FileReader(_file, StandardCharsets.UTF_8));
        }
        
        throw new IllegalArgumentException("Unknown file-type: "+_file.getName());
    }
    
    @SneakyThrows
    public static ColorModel loadPalettePAL(Reader _fileReader)
    {
        BufferedReader _br = new BufferedReader(_fileReader, 8192);
        try
        {
            if(_br.readLine().trim().equalsIgnoreCase("JASC-PAL"))
            {
                if(_br.readLine().trim().equalsIgnoreCase("0100"))
                {
                    int _num = CommonUtil.checkInt(_br.readLine().trim());
                    byte[] _r = new byte[_num];
                    byte[] _g = new byte[_num];
                    byte[] _b = new byte[_num];
                    for(int _i = 0; _i<_num; _i++)
                    {
                        String[] _parts = _br.readLine().trim().split(" ");
                        _r[_i] = (byte) (CommonUtil.checkInt(_parts[0])&0xff);
                        _g[_i] = (byte) (CommonUtil.checkInt(_parts[1])&0xff);
                        _b[_i] = (byte) (CommonUtil.checkInt(_parts[2])&0xff);
                    }
                    return new IndexColorModel(8,_num,_r,_g,_b);
                }
            }
        }
        catch (Exception _xe)
        {
        
        }
        finally
        {
            IOUtil.closeQuietly(_br);
        }
        throw new IllegalArgumentException("Unknown file-type");
    }
    
    public static ColorModel loadPalettePNET(Reader _fileReader)
    {
        BufferedReader _br = new BufferedReader(_fileReader, 8192);
        try
        {
            List<String> _list = new ArrayList<>(16);
            String _line = null;
            while((_line = _br.readLine())!=null)
            {
                if(_line.trim().startsWith(";")) continue;
                if(_line.trim().startsWith("#")) continue;
                if(_line.trim().startsWith("%")) continue;
                if(_line.trim().startsWith("!")) continue;
                if(_line.trim().startsWith("/")) continue;
                _list.add(_line.trim());
            }
            int _num = _list.size();
            byte[] _a = new byte[_num];
            byte[] _r = new byte[_num];
            byte[] _g = new byte[_num];
            byte[] _b = new byte[_num];
            for(int _i = 0; _i<_num; _i++)
            {
                _a[_i] = (byte) (Integer.parseInt(_list.get(_i).substring(0,2),16)&0xff);
                _r[_i] = (byte) (Integer.parseInt(_list.get(_i).substring(2,4),16)&0xff);
                _g[_i] = (byte) (Integer.parseInt(_list.get(_i).substring(4,6),16)&0xff);
                _b[_i] = (byte) (Integer.parseInt(_list.get(_i).substring(6,8),16)&0xff);
            }
            return new IndexColorModel(8,_num,_r,_g,_b,_a);
        }
        catch (Exception _xe)
        {
        
        }
        finally
        {
            IOUtil.closeQuietly(_br);
        }
        throw new IllegalArgumentException("Unknown file-type");
    }
    
    public static ColorModel loadPaletteGPL(Reader _fileReader)
    {
        BufferedReader _br = new BufferedReader(_fileReader, 8192);
        try
        {
            if(_br.readLine().trim().equalsIgnoreCase("GIMP Palette"))
            {
                List<String> _list = new ArrayList<>(16);
                String _line = null;
                while((_line = _br.readLine())!=null)
                {
                    if(_line.trim().startsWith(";")) continue;
                    if(_line.trim().startsWith("#")) continue;
                    if(_line.trim().startsWith("%")) continue;
                    if(_line.trim().startsWith("!")) continue;
                    if(_line.trim().equalsIgnoreCase("")) continue;
                    _list.add(_line.trim());
                }
                int _num = _list.size();
                byte[] _r = new byte[_num];
                byte[] _g = new byte[_num];
                byte[] _b = new byte[_num];
                for(int _i = 0; _i<_num; _i++)
                {
                    String[] _parts = _list.get(_i).split("\\s+");
                    _r[_i] = (byte) (CommonUtil.checkInt(_parts[0])&0xff);
                    _g[_i] = (byte) (CommonUtil.checkInt(_parts[1])&0xff);
                    _b[_i] = (byte) (CommonUtil.checkInt(_parts[2])&0xff);
                }
                return new IndexColorModel(8,_num,_r,_g,_b);
            }
        }
        catch (Exception _xe)
        {
        
        }
        finally
        {
            IOUtil.closeQuietly(_br);
        }
        throw new IllegalArgumentException("Unknown file-type");
    }
    
    public static ColorModel loadPaletteHEX(Reader _fileReader)
    {
        BufferedReader _br = new BufferedReader(_fileReader, 8192);
        try
        {
            List<String> _list = new ArrayList<>(16);
            String _line = null;
            while((_line = _br.readLine())!=null)
            {
                if(_line.trim().startsWith(";")) continue;
                if(_line.trim().startsWith("#")) continue;
                if(_line.trim().startsWith("%")) continue;
                if(_line.trim().startsWith("!")) continue;
                if(_line.trim().startsWith("/")) continue;
                if(_line.trim().equalsIgnoreCase("")) continue;
                _list.add(_line.trim());
            }
            int _num = _list.size();
            byte[] _r = new byte[_num];
            byte[] _g = new byte[_num];
            byte[] _b = new byte[_num];
            for(int _i = 0; _i<_num; _i++)
            {
                _r[_i] = (byte) (Integer.parseInt(_list.get(_i).substring(0,2),16)&0xff);
                _g[_i] = (byte) (Integer.parseInt(_list.get(_i).substring(2,4),16)&0xff);
                _b[_i] = (byte) (Integer.parseInt(_list.get(_i).substring(4,6),16)&0xff);
            }
            return new IndexColorModel(8,_num,_r,_g,_b);
        }
        catch (Exception _xe)
        {
        
        }
        finally
        {
            IOUtil.closeQuietly(_br);
        }
        throw new IllegalArgumentException("Unknown file-type");
    }
    
    @SneakyThrows
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight)
    {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
    
    public static BufferedImage toBufferedImage(Image img, ImageObserver obs, ColorModel colorModel)
    {
        BufferedImage bimage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(img.getWidth(obs), img.getHeight(obs)), colorModel.isAlphaPremultiplied(), null);
        
        Graphics2D bGr = bimage.createGraphics();
        bGr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bGr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        bGr.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        bGr.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        bGr.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        bGr.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        
        return bimage;
    }
    
    public static BufferedImage toBufferedImage(Image img, ImageObserver obs)
    {
        BufferedImage bimage = new BufferedImage(img.getWidth(obs), img.getHeight(obs), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bGr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        bGr.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        bGr.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        bGr.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        bGr.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        
        return bimage;
    }
    
    public static BufferedImage toBufferedImage(Image img, ImageObserver obs, int _type)
    {
        BufferedImage bimage = new BufferedImage(img.getWidth(obs), img.getHeight(obs), _type);
        
        Graphics2D bGr = bimage.createGraphics();
        bGr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bGr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        bGr.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        bGr.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        bGr.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        bGr.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        bGr.drawImage(img, 0, 0, img.getWidth(obs), img.getHeight(obs), obs);
        bGr.dispose();
        
        return bimage;
    }
    
    public static BufferedImage makeMono(BufferedImage _img)
    {
        return toBufferedImage(_img, null, BufferedImage.TYPE_BYTE_BINARY);
    }
    
    public static BufferedImage makeMono(BufferedImage _img, int _level)
    {
        _img = makeGray(_img);
        
        BufferedImage bimage = new BufferedImage(_img.getWidth(), _img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        
        int[] rgb = _img.getData().getSamples(0,0, _img.getWidth(), _img.getHeight(), 0, new int[_img.getWidth()*_img.getHeight()]);
        
        int _w = _img.getWidth();
        int _h = _img.getHeight();
        
        for(int _i = 0; _i<rgb.length; _i++)
        {
            float _af = (float) (rgb[_i] & 0xff);
            int _x = _i % _w;
            int _y = _i/_w;
            if(_af<_level)
            {
                bimage.setRGB(_x,_y, 0);
            }
            else
            {
                bimage.setRGB(_x,_y, 0xffffffff);
            }
        }
        
        return bimage;
    }
    
    public static BufferedImage makeGray(BufferedImage _img)
    {
        return toBufferedImage(_img, null, BufferedImage.TYPE_BYTE_GRAY);
    }
    
    public static BufferedImage makeGray(BufferedImage _img, double _levels)
    {
        _img = makeGray(_img);
        
        int _w = _img.getWidth();
        int _h = _img.getHeight();

        if(_levels>1.)
        {
            int[] sid = _img.getData().getSamples(0,0, _img.getWidth(), _img.getHeight(), 0, new int[_img.getWidth()*_img.getHeight()]);
            
            for(int _i = 0; _i<sid.length; _i++)
            {
                int _x = _i % _w;
                int _y = _i/_w;

                int c = sid[_i];
                if(c!=0 && c!=255)
                {
                    c = (int)(255*(((int)(((double)c)*_levels/255.))/_levels)) ;
                    if(c>255) c=255;
                }
                _img.setRGB(_x,_y,0xff000000|(c<<16)|(c<<8)|(c));
            }
        }
        return _img;
    }
    
    public static BufferedImage makeSmask(BufferedImage _img, float _av)
    {
        
        BufferedImage bimage = new BufferedImage(_img.getWidth(), _img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        
        int _w = _img.getWidth();
        int _h = _img.getHeight();
        
        int[] rgb = _img.getData().getSamples(0,0, _img.getWidth(), _img.getHeight(), 0, new int[_img.getWidth()*_img.getHeight()]);
        
        for(int _i = 0; _i<rgb.length; _i++)
        {
            float _af = (float) ((rgb[_i]>>>24) & 0xff);
            if(_av<0f)
            {
                _af /= (-_av/100f);
            }
            else
            {
                _af *= (_av/100f);
            }
            int _a = (int) _af;
            if(_af>255f) _a = 255;
            
            int _x = _i % _w;
            int _y = _i/_w;
            bimage.setRGB(_x,_y,0xff000000|(_a<<16)|(_a<<8)|(_a));
        }
        
        return bimage;
    }

    public static BufferedImage makeTmask(BufferedImage _img, int _level)
    {
        BufferedImage bimage = new BufferedImage(_img.getWidth(), _img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        
        int[] rgb = _img.getData().getSamples(0,0, _img.getWidth(), _img.getHeight(), 0, new int[_img.getWidth()*_img.getHeight()]);
        
        int _w = _img.getWidth();
        int _h = _img.getHeight();

        for(int _i = 0; _i<rgb.length; _i++)
        {
            int _x = _i % _w;
            int _y = _i/_w;
            float _af = (float) ((rgb[_i]>>>24) & 0xff);
            if(_af<_level)
            {
                bimage.setRGB(_x,_y,0x0);
            }
            else
            {
                bimage.setRGB(_x,_y,0xffffffff);
            }
        }
        
        return bimage;
    }
    
    public static BufferedImage makeRgb8(BufferedImage _img)
    {
        _img = toBufferedImage(_img, null, BufferedImage.TYPE_INT_RGB);
        int[] _rgb = _img.getRGB(0,0, _img.getWidth(), _img.getHeight(), null,0,_img.getWidth());
        
        int _w = _img.getWidth();
        int _h = _img.getHeight();
        BufferedImage _rimg = new BufferedImage(_w,_h,BufferedImage.TYPE_INT_RGB);
        
        for(int _y = 0; _y<_h; _y++)
        for(int _x = 0; _x<_w; _x++)
        {
            int _i = (_y*_w)+_x;
            int _r = (_rgb[_i]>>>16)&0xff;
            int _g = (_rgb[_i]>>>8)&0xff;
            int _b = (_rgb[_i])&0xff;
            _rgb[_i] = fromRgb8(toRgb8(_r,_g,_b));
            _r = (_rgb[_i]>>>16)&0xff;
            _g = (_rgb[_i]>>>8)&0xff;
            _b = (_rgb[_i])&0xff;
            
            Color _c = ColorUtil.fromRgb(_r, _g, _b);
            _rimg.setRGB(_x, _y, _c.getRGB());
        }

        return _rimg;
    }
    
    public static int toRgb8(int r, int g, int b)
    {
        r = (r>>>5)&0x7;
        g = (g>>>6)&0x3;
        b = (b>>>5)&0x7;
        
        return (((r<<5) | (g<<3) | (b)) & 0xff);
    }
    
    public static int fromRgb8(int i)
    {
        int r = ((i>>>5)&0x7)<<5;
        r |= (r>>3);
        r |= (r>>6);
        int g = (i>>>3)&0x3;
        g |= (g<<2);
        g |= (g<<4);
        int b = (i&0x7)<<5;
        b |= (b>>3);
        b |= (b>>6);
        
        return ((((r&0xff)<<16) | ((g&0xff)<<8) | (b&0xff)) & 0xffffff);
    }
    
    public static BufferedImage rotateImageByDegrees(BufferedImage _img, int _rot)
    {
        //return _img;
        
        if(_rot==0)
        {
            return _img;
        }
        
        BufferedImage rotated = new BufferedImage(_img.getHeight(), _img.getWidth(), _img.getType());
        
        for(int _x=0; _x<_img.getWidth(); _x++)
        {
            for(int _y=0; _y<_img.getHeight(); _y++)
            {
                if(_rot<0)
                {
                    rotated.setRGB(_y,_x,_img.getRGB(_x,_y));
                }
                else
                {
                    rotated.setRGB(_y,_img.getWidth()-_x-1,_img.getRGB(_x,_y));
                }
            }
        }
        
        return rotated;
    }
    
    @SneakyThrows
    public static void main(String[] args)
    {
        String _file = "/u/fredo/Pictures/85ce9d1715a1a71c2239215b037c182b-d9d5xnj.jpg";
        BufferedImage _img = ImageIO.read(new File(_file));
        ImageIO.write(makeGray(_img),"png", new File(_file+".grey.png"));
        ImageIO.write(makeGray(_img, 4.),"png", new File(_file+".grey4.png"));
        ImageIO.write(makeGray(_img, 7.),"png", new File(_file+".grey7.png"));
        ImageIO.write(makeMono(_img),"png", new File(_file+".mono.png"));
        ImageIO.write(makeMono(_img, 64),"png", new File(_file+".mono64.png"));
        ImageIO.write(makeMono(_img, 192),"png", new File(_file+".mono192.png"));
        ImageIO.write(makeRgb8(_img),"png", new File(_file+".rgb8.png"));
    }
}
