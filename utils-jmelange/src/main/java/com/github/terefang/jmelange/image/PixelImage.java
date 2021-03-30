package com.github.terefang.jmelange.image;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Iterator;

public class PixelImage extends AbstractGfxInterface implements GfxInterface {

    BufferedImage _bimg;

    public static final PixelImage create(int _width, int _height) {
        return new PixelImage(_width, _height);
    }

    public static final PixelImage from(BufferedImage _bimg) {
        PixelImage _pi = new PixelImage(_bimg.getWidth(), _bimg.getHeight());
        _bimg.copyData(_pi._bimg.getRaster());
        return _pi;
    }

    public PixelImage(int _width, int _height) {
        _bimg = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_ARGB);
    }

    @SneakyThrows
    public void savePng(String _path) {
        this.saveAs("png", new File(_path));
    }

    @SneakyThrows
    public void savePng(File _out) {
        this.saveAs("png", _out);
    }

    @SneakyThrows
    public void savePng(OutputStream _out) {
        this.saveAs("png", _out);
    }

    @SneakyThrows
    public void saveAs(String _type, File _out) {
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

    public int gGet(int _x, int _y) { return this._bimg.getRGB(_x, _y); }

    public void gSet(int _x, int _y, long _color) { this._bimg.setRGB(_x, _y, (int)_color); }

    public int gColorReplace(int _src, int _dst) { return 0; }
    public int gColorReplaceThreshold(int _src, int _dst, float _threshold) { return 0; }

}
