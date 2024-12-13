package com.github.terefang.jmelange.commons.gfx.impl;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.gfx.AbstractGfxInterface;
import com.github.terefang.jmelange.commons.gfx.GfxInterface;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PixelImage extends AbstractGfxInterface
        implements GfxInterface {

    BufferedImage _bimg;

    public static final PixelImage create(int _width, int _height) {
        return new PixelImage(_width, _height);
    }

    public static final PixelImage from(BufferedImage _bimg) {
        PixelImage _pi = new PixelImage(_bimg.getWidth(), _bimg.getHeight(), _bimg.getType());
        _bimg.copyData(_pi._bimg.getRaster());
        return _pi;
    }

    public static final PixelImage from(int _width, int _height, BufferedImage _bimg) {
        PixelImage _pi = new PixelImage(_width, _height, _bimg.getType());
        _pi.gImage(0,0,_bimg);
        return _pi;
    }

    @SneakyThrows
    public static final PixelImage from(File _file)
    {
        return from(ImageIO.read(_file));
    }

    public PixelImage(int _width, int _height) {
        _bimg = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_ARGB);
    }

    public PixelImage(int _width, int _height, int _type) {
        _bimg = new BufferedImage(_width, _height, _type);
    }

    @SneakyThrows
    public void savePng(String _path) {
        this.saveAs("png", new File(_path));
    }

    @SneakyThrows
    public void savePng(File _out) {
        _out.getParentFile().mkdirs();
        this.saveAs("png", _out);
    }

    @SneakyThrows
    public void savePng(OutputStream _out) {
        this.saveAs("png", _out);
    }

    @SneakyThrows
    public void saveAs(String _type, File _out) {
        _out.getParentFile().mkdirs();
        ImageIO.write(this._bimg, _type, _out);
    }

    @SneakyThrows
    public void saveAs(String _type, OutputStream _out) {
        ImageIO.write(this._bimg, _type, _out);
    }

    @SneakyThrows
    public void save(File _out) { this.saveAs("png", _out);}

    @SneakyThrows
    public void save(OutputStream _out) { this.saveAs("png", _out);}

    public static final String RAW_2CC = "00";
    public static final String GZIP_2CC = "GZ";

    public void save4CC(OutputStream _out, String _2cc)
    {
        save4CC(this, _out, _2cc);
    }

    @SneakyThrows
    public static void save4CC(PixelImage _img, OutputStream _out, String _2cc) {
        int _width = _img._bimg.getWidth();
        int _height = _img._bimg.getHeight();
        DataBuffer toArray = _img._bimg.getRaster().getDataBuffer();
        byte[] _array = ((DataBufferByte) toArray).getData();
        boolean _hasAlphaChannel = _img._bimg.getAlphaRaster() != null;
        _out.write("JBIR".getBytes());
        _out.flush();
        _out.write(CommonUtil.from2CC(_2cc));
        _out.write(CommonUtil.from2CC(_img._bimg.getType()));
        _out.write(CommonUtil.toByteArray(_width));
        _out.write(CommonUtil.toByteArray(_height));
        _out.write(CommonUtil.toByteArray(_array.length));
        _out.flush();
        if(RAW_2CC.equalsIgnoreCase(_2cc))
        {
            // do nothing raw stream
        }
        else
        if(GZIP_2CC.equalsIgnoreCase(_2cc))
        {
            _out = new GZIPOutputStream(_out, 8192, true);
        }
        else
        {
            throw new IllegalArgumentException("unknown compression 2CC="+_2cc);
        }

        _out.write(_array);
        _out.flush();
        _out.close();
    }

    public static void save4CC(PixelImage _img, OutputStream _out)
    {
        save4CC(_img, _out, "00");
    }

    @SneakyThrows
    public static PixelImage load4CC(File _in)
    {
        return load4CC(new FileInputStream(_in));
    }

    @SneakyThrows
    public static PixelImage load4CC(InputStream _in)
    {
        try
        {
            String _4cc = CommonUtil.read4CC(_in);
            if(!"JBIR".equalsIgnoreCase(_4cc)) throw new IllegalArgumentException("unknown 4CC="+_4cc);

            String _compr = CommonUtil.read2CC(_in);
            int _type = CommonUtil.readShort(_in);
            int _width = CommonUtil.readInt(_in);
            int _height = CommonUtil.readInt(_in);
            int _len = CommonUtil.readInt(_in);

            byte[] _buf = null;
            if(RAW_2CC.equalsIgnoreCase(_compr))
            {
                // ignore raw bytes
            }
            else
            if(GZIP_2CC.equalsIgnoreCase(_compr))
            {
                _in = new GZIPInputStream(_in, 8192);
            }
            else
            {
                throw new IllegalArgumentException("unknown compression 2CC="+_compr);
            }

            _buf = CommonUtil.readBytes(_len, _in);

            PixelImage _img = new PixelImage(_width, _height, _type);
            _img._bimg.setData(Raster.createRaster(_img._bimg.getSampleModel(), new DataBufferByte(_buf, _buf.length), new Point() ) );
            return _img;
        }
        finally
        {
            _in.close();
        }
    }

    @SneakyThrows
    public static PixelImage load(File _file) {
        ImageInputStream stream = ImageIO.createImageInputStream(_file);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);

        if (readers.hasNext()) {
            ImageReader reader = readers.next();
            reader.setInput(reader);

            int w = reader.getWidth(0);
            int h = reader.getHeight(0);

            ImageReadParam param = reader.getDefaultReadParam();

            PixelImage _pi = PixelImage.create(w, h);
            param.setDestination(_pi._bimg);

            reader.read(0, param);
            return _pi;
        }
        return null;
    }

    @Override
    public Graphics2D getG2d() {
        return (Graphics2D) this._bimg.getGraphics();
    }
    
    @Override
    public GfxInterface getSub()
    {
        return null;
    }
    
    @Override
    public String beginGroup() {
        return null;
    }

    @Override
    public String beginGroup(String _id) {
        return null;
    }

    @Override
    public void endGroup() {

    }
    
    @Override
    public GfxInterface getSub(String _id)
    {
        return null;
    }
    
    public int gGet(int _x, int _y) { try{return this._bimg.getRGB(_x, _y);}catch(Exception _xe){ return 0;} }

    public void gSet(int _x, int _y, long _color) { try { this._bimg.setRGB(_x, _y, (int)_color);}catch(Exception _xe){} }

    public int gColorReplace(int _src, int _dst) { return 0; }
    public int gColorReplaceThreshold(int _src, int _dst, float _threshold) { return 0; }

    public int getHeight() {
        return _bimg.getHeight();
    }

    public int getWidth() {
        return _bimg.getWidth();
    }

    public int getType() {
        return _bimg.getType();
    }

    public int getRGB(int x, int y) {
        return _bimg.getRGB(x, y);
    }

    public void setRGB(int x, int y, int rgb) {
        _bimg.setRGB(x, y, rgb);
    }

}
