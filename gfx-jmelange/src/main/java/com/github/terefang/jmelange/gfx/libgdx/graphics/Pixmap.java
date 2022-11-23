package com.github.terefang.jmelange.gfx.libgdx.graphics;

import com.github.terefang.jmelange.gfx.impl.PixelImage;
import com.github.terefang.jmelange.gfx.libgdx.Net;
import com.github.terefang.jmelange.gfx.libgdx.files.FileHandle;
import com.github.terefang.jmelange.gfx.libgdx.net.NetDelegateSingleton;
import com.github.terefang.jmelange.gfx.libgdx.utils.BufferUtils;
import com.github.terefang.jmelange.gfx.libgdx.utils.Disposable;
import com.github.terefang.jmelange.gfx.libgdx.utils.GdxRuntimeException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

/**
 * <p>
 * A Pixmap represents an image in memory. It has a width and height expressed in pixels as well as a {@link Format} specifying
 * the number and order of color components per pixel. Coordinates of pixels are specified with respect to the top left corner of
 * the image, with the x-axis pointing to the right and the y-axis pointing downwards.
 * <p>
 * By default all methods use blending. You can disable blending with {@link Pixmap#setBlending(Blending)}, which may reduce
 * blitting time by ~30%. The {@link Pixmap#drawPixmap(Pixmap, int, int, int, int, int, int, int, int)} method will scale and
 * stretch the source image to a target image. There either nearest neighbour or bilinear filtering can be used.
 * <p>
 * A Pixmap stores its data in native heap memory. It is mandatory to call {@link Pixmap#dispose()} when the pixmap is no longer
 * needed, otherwise memory leaks will result
 * @author badlogicgames@gmail.com */
public class Pixmap implements Disposable {
    /** Different pixel formats.
     *
     * @author mzechner */
    public enum Format {
        Alpha, Intensity, LuminanceAlpha, RGB565, RGBA4444, RGB888, RGBA8888;

        public static int toAwtPixmapFormat (Format format) {
            if (format == Alpha) return BufferedImage.TYPE_BYTE_GRAY;
            if (format == Intensity) return BufferedImage.TYPE_BYTE_GRAY;
            if (format == LuminanceAlpha) return BufferedImage.TYPE_4BYTE_ABGR;
            if (format == RGB565) return BufferedImage.TYPE_USHORT_565_RGB;
            if (format == RGBA4444) return BufferedImage.TYPE_4BYTE_ABGR;
            if (format == RGB888) return BufferedImage.TYPE_3BYTE_BGR;
            if (format == RGBA8888) return BufferedImage.TYPE_4BYTE_ABGR;
            throw new GdxRuntimeException("Unknown Format: " + format);
        }

        public static Format fromAwtPixmapFormat (int format) {
            if (format == BufferedImage.TYPE_BYTE_GRAY) return Alpha;
            if (format == BufferedImage.TYPE_USHORT_565_RGB) return RGB565;
            if (format == BufferedImage.TYPE_3BYTE_BGR) return RGB888;
            if (format == BufferedImage.TYPE_4BYTE_ABGR) return RGBA8888;
            throw new GdxRuntimeException("Unknown Gdx2DPixmap Format: " + format);
        }
    }

    /** Blending functions to be set with {@link Pixmap#setBlending}.
     * @author mzechner */
    public enum Blending {
        None, SourceOver
    }

    /** Filters to be used with {@link Pixmap#drawPixmap(Pixmap, int, int, int, int, int, int, int, int)}.
     *
     * @author mzechner */
    public enum Filter {
        NearestNeighbour, BiLinear
    }

    private Blending blending = Blending.SourceOver;
    private Filter filter = Filter.BiLinear;

    final PixelImage pixmap;
    int color = 0;

    private boolean disposed;

    /** Sets the type of {@link Blending} to be used for all operations. Default is {@link Blending#SourceOver}.
     * @param blending the blending type */
    public void setBlending (Blending blending) {
        this.blending = blending;
    }

    /** Sets the type of interpolation {@link Filter} to be used in conjunction with
     * {@link Pixmap#drawPixmap(Pixmap, int, int, int, int, int, int, int, int)}.
     * @param filter the filter. */
    public void setFilter (Filter filter) {
        this.filter = filter;
    }

    /** Creates a new Pixmap instance with the given width, height and format.
     * @param width the width in pixels
     * @param height the height in pixels
     * @param format the {@link Format} */
    public Pixmap (int width, int height, Format format) {
        pixmap = PixelImage.from(new BufferedImage(width, height, Format.toAwtPixmapFormat(format)));
        setColor(0, 0, 0, 0);
        fill();
    }

    public Pixmap (int width, int height) {
        pixmap = PixelImage.from(new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR));
        setColor(0, 0, 0, 0);
        fill();
    }

    /** Creates a new Pixmap instance from the given encoded image data. The image can be encoded as JPEG, PNG or BMP. Not
     * available on GWT backend.
     *
     * @param encodedData the encoded image data
     * @param offset the offset
     * @param len the length */
    public Pixmap (byte[] encodedData, int offset, int len) {
        try {
            pixmap = PixelImage.from(ImageIO.read(new ByteArrayInputStream(encodedData, offset, len)));
        } catch (Exception e) {
            throw new GdxRuntimeException("Couldn't load pixmap from image data", e);
        }
    }

    /** Creates a new Pixmap instance from the given encoded image data. The image can be encoded as JPEG, PNG or BMP. Not
     * available on GWT backend.
     *
     * @param encodedData the encoded image data
     * @param offset the offset relative to the base address of encodedData
     * @param len the length */
    public Pixmap (ByteBuffer encodedData, int offset, int len) {
        if (!encodedData.isDirect()) throw new GdxRuntimeException("Couldn't load pixmap from non-direct ByteBuffer");
        try {
            pixmap = PixelImage.from(ImageIO.read(new ByteArrayInputStream(encodedData.array(), offset, len)));
        } catch (Exception e) {
            throw new GdxRuntimeException("Couldn't load pixmap from image data", e);
        }
    }

    /** Creates a new Pixmap instance from the given encoded image data. The image can be encoded as JPEG, PNG or BMP. Not
     * available on GWT backend.
     *
     * Offset is based on the position of the buffer. Length is based on the remaining bytes of the buffer.
     *
     * @param encodedData the encoded image data */
    public Pixmap (ByteBuffer encodedData) {
        this(encodedData, encodedData.position(), encodedData.remaining());
    }

    /** Creates a new Pixmap instance from the given file. The file must be a Png, Jpeg or Bitmap. Paletted formats are not
     * supported.
     *
     * @param file the {@link FileHandle} */
    public Pixmap (FileHandle file) {
        try {
            byte[] bytes = file.readBytes();
            pixmap = PixelImage.from(ImageIO.read(new ByteArrayInputStream(bytes, 0, bytes.length)));
        } catch (Exception e) {
            throw new GdxRuntimeException("Couldn't load file: " + file, e);
        }
    }

    /** Constructs a new Pixmap from a {@link BufferedImage}.
     * @param pixmap */
    public Pixmap (BufferedImage pixmap) {
        this.pixmap = PixelImage.from(pixmap);
    }

    /** Downloads an image from http(s) url and passes it as a {@link Pixmap} to the specified
     * {@link DownloadPixmapResponseListener}
     *
     * @param url http url to download the image from
     * @param responseListener the listener to call once the image is available as a {@link Pixmap} */
    public static void downloadFromUrl (String url, final DownloadPixmapResponseListener responseListener) {
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
        request.setUrl(url);
        NetDelegateSingleton.getInstance().sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                final byte[] result = httpResponse.getResult();
                (new Runnable() {
                    @Override
                    public void run () {
                        try {
                            Pixmap pixmap = new Pixmap(result, 0, result.length);
                            responseListener.downloadComplete(pixmap);
                        } catch (Throwable t) {
                            failed(t);
                        }
                    }
                }).run();
            }

            @Override
            public void failed (Throwable t) {
                responseListener.downloadFailed(t);
            }

            @Override
            public void cancelled () {
                // no way to cancel, will never get called
            }
        });
    }

    /** Sets the color for the following drawing operations
     * @param color the color, encoded as RGBA8888 */
    public void setColor (int color) {
        this.color = color;
    }

    /** Sets the color for the following drawing operations.
     *
     * @param r The red component.
     * @param g The green component.
     * @param b The blue component.
     * @param a The alpha component. */
    public void setColor (float r, float g, float b, float a) {
        color = Color.rgba8888(r, g, b, a);
    }

    /** Sets the color for the following drawing operations.
     * @param color The color. */
    public void setColor (Color color) {
        this.color = Color.rgba8888(color.r, color.g, color.b, color.a);
    }

    /** Fills the complete bitmap with the currently set color. */
    public void fill () {
        this.pixmap.beginDraw();
        this.pixmap.setColor(color);
        this.pixmap.setBackground(color);
        this.pixmap.fillRect(0,0, this.pixmap.getWidth(), this.pixmap.getHeight());
        this.pixmap.endDraw();
    }

// /**
// * Sets the width in pixels of strokes.
// *
// * @param width The stroke width in pixels.
// */
// public void setStrokeWidth (int width);

    /** Draws a line between the given coordinates using the currently set color.
     *
     * @param x The x-coodinate of the first point
     * @param y The y-coordinate of the first point
     * @param x2 The x-coordinate of the first point
     * @param y2 The y-coordinate of the first point */
    public void drawLine (int x, int y, int x2, int y2) {
        this.pixmap.beginDraw();
        this.pixmap.setColor(color);
        this.pixmap.drawLine(x, y, x2, y2);
        this.pixmap.endDraw();
    }

    /** Draws a rectangle outline starting at x, y extending by width to the right and by height downwards (y-axis points
     * downwards) using the current color.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @param width The width in pixels
     * @param height The height in pixels */
    public void drawRectangle (int x, int y, int width, int height) {
        this.pixmap.beginDraw();
        this.pixmap.setColor(color);
        this.pixmap.drawRect(x, y, width, height);
        this.pixmap.endDraw();
    }

    /** Draws an area from another Pixmap to this Pixmap.
     *
     * @param pixmap The other Pixmap
     * @param x The target x-coordinate (top left corner)
     * @param y The target y-coordinate (top left corner) */
    public void drawPixmap (Pixmap pixmap, int x, int y) {
        drawPixmap(pixmap, x, y, 0, 0, pixmap.getWidth(), pixmap.getHeight());
    }

    /** Draws an area from another Pixmap to this Pixmap.
     *
     * @param pixmap The other Pixmap
     * @param x The target x-coordinate (top left corner)
     * @param y The target y-coordinate (top left corner)
     * @param srcx The source x-coordinate (top left corner)
     * @param srcy The source y-coordinate (top left corner);
     * @param srcWidth The width of the area from the other Pixmap in pixels
     * @param srcHeight The height of the area from the other Pixmap in pixels */
    public void drawPixmap (Pixmap pixmap, int x, int y, int srcx, int srcy, int srcWidth, int srcHeight) {
        this.pixmap.beginDraw();
        //TODO//this.pixmap.drawImage(...);
        this.pixmap.endDraw();
    }

    /** Draws an area from another Pixmap to this Pixmap. This will automatically scale and stretch the source image to the
     * specified target rectangle. Use {@link Pixmap#setFilter(Filter)} to specify the type of filtering to be used (nearest
     * neighbour or bilinear).
     *
     * @param pixmap The other Pixmap
     * @param srcx The source x-coordinate (top left corner)
     * @param srcy The source y-coordinate (top left corner);
     * @param srcWidth The width of the area from the other Pixmap in pixels
     * @param srcHeight The height of the area from the other Pixmap in pixels
     * @param dstx The target x-coordinate (top left corner)
     * @param dsty The target y-coordinate (top left corner)
     * @param dstWidth The target width
     * @param dstHeight the target height */
    public void drawPixmap (Pixmap pixmap, int srcx, int srcy, int srcWidth, int srcHeight, int dstx, int dsty, int dstWidth,
                            int dstHeight) {
        this.pixmap.beginDraw();
        //TODO//this.pixmap.drawImage(...);
        this.pixmap.endDraw();
    }

    /** Fills a rectangle starting at x, y extending by width to the right and by height downwards (y-axis points downwards) using
     * the current color.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @param width The width in pixels
     * @param height The height in pixels */
    public void fillRectangle (int x, int y, int width, int height) {
        this.pixmap.beginDraw();
        this.pixmap.setColor(color);
        this.pixmap.fillRect(x, y, width, height);
        this.pixmap.endDraw();
    }

    /** Draws a circle outline with the center at x,y and a radius using the current color and stroke width.
     *
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     * @param radius The radius in pixels */
    public void drawCircle (int x, int y, int radius) {
        this.pixmap.beginDraw();
        this.pixmap.setColor(color);
        this.pixmap.drawOval(x, y, radius, radius);
        this.pixmap.endDraw();
    }

    /** Fills a circle with the center at x,y and a radius using the current color.
     *
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     * @param radius The radius in pixels */
    public void fillCircle (int x, int y, int radius) {
        this.pixmap.beginDraw();
        this.pixmap.setColor(color);
        this.pixmap.fillOval(x, y, radius, radius);
        this.pixmap.endDraw();
    }

    /** Fills a triangle with vertices at x1,y1 and x2,y2 and x3,y3 using the current color.
     *
     * @param x1 The x-coordinate of vertex 1
     * @param y1 The y-coordinate of vertex 1
     * @param x2 The x-coordinate of vertex 2
     * @param y2 The y-coordinate of vertex 2
     * @param x3 The x-coordinate of vertex 3
     * @param y3 The y-coordinate of vertex 3 */
    public void fillTriangle (int x1, int y1, int x2, int y2, int x3, int y3) {
        this.pixmap.beginDraw();
        this.pixmap.setColor(color);
        this.pixmap.gFilledPolygon(color, x1, y1, x2, y2, x3, y3);
        this.pixmap.endDraw();
    }

    /** Returns the 32-bit RGBA8888 value of the pixel at x, y. For Alpha formats the RGB components will be one.
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return The pixel color in RGBA8888 format. */
    public int getPixel (int x, int y) {
        return pixmap.getRGB(x, y);
    }

    /** @return The width of the Pixmap in pixels. */
    public int getWidth () {
        return pixmap.getWidth();
    }

    /** @return The height of the Pixmap in pixels. */
    public int getHeight () {
        return pixmap.getHeight();
    }

    /** Releases all resources associated with this Pixmap. */
    public void dispose () {
        if (disposed) throw new GdxRuntimeException("Pixmap already disposed!");
        pixmap.dispose();
        disposed = true;
    }

    public boolean isDisposed () {
        return disposed;
    }

    /** Draws a pixel at the given location with the current color.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate */
    public void drawPixel (int x, int y) {
        pixmap.setRGB(x, y, color);
    }

    /** Draws a pixel at the given location with the given color.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param color the color in RGBA8888 format. */
    public void drawPixel (int x, int y, int color) {
        pixmap.setRGB(x, y, color);
    }

    /** Returns the OpenGL ES format of this Pixmap. Used as the seventh parameter to
     * {@link GL20#glTexImage2D(int, int, int, int, int, int, int, int, java.nio.Buffer)}.
     * @return one of GL_ALPHA, GL_RGB, GL_RGBA, GL_LUMINANCE, or GL_LUMINANCE_ALPHA. */
    public int getGLFormat () {
        return 0;//TODO//pixmap.getGLFormat();
    }

    /** Returns the OpenGL ES format of this Pixmap. Used as the third parameter to
     * {@link GL20#glTexImage2D(int, int, int, int, int, int, int, int, java.nio.Buffer)}.
     * @return one of GL_ALPHA, GL_RGB, GL_RGBA, GL_LUMINANCE, or GL_LUMINANCE_ALPHA. */
    public int getGLInternalFormat () {
        return 0;//TODO//pixmap.getGLInternalFormat();
    }

    /** Returns the OpenGL ES type of this Pixmap. Used as the eighth parameter to
     * {@link GL20#glTexImage2D(int, int, int, int, int, int, int, int, java.nio.Buffer)}.
     * @return one of GL_UNSIGNED_BYTE, GL_UNSIGNED_SHORT_5_6_5, GL_UNSIGNED_SHORT_4_4_4_4 */
    public int getGLType () {
        return 0;//TODO//pixmap.getGLType();
    }

    /** Returns the direct ByteBuffer holding the pixel data. For the format Alpha each value is encoded as a byte. For the format
     * LuminanceAlpha the luminance is the first byte and the alpha is the second byte of the pixel. For the formats RGB888 and
     * RGBA8888 the color components are stored in a single byte each in the order red, green, blue (alpha). For the formats RGB565
     * and RGBA4444 the pixel colors are stored in shorts in machine dependent order.
     * @return the direct {@link ByteBuffer} holding the pixel data. */
    public ByteBuffer getPixels () {
        if (disposed) throw new GdxRuntimeException("Pixmap already disposed");
        //TODO//return pixmap.getPixels();
        throw new GdxRuntimeException("Pixmap not available.");
    }

    /** Sets pixels from a provided byte buffer.
     * @param pixels Pixels to copy from, should match Pixmap data size (see {@link #getPixels()}). */
    public void setPixels (ByteBuffer pixels) {
        ByteBuffer dst = getPixels();
        BufferUtils.copy(pixels, dst, dst.limit());
    }

    /** @return the {@link Format} of this Pixmap. */
    public Format getFormat () {
        return Format.fromAwtPixmapFormat(pixmap.getType());
    }

    /** @return the currently set {@link Blending} */
    public Blending getBlending () {
        return blending;
    }

    /** @return the currently set {@link Filter} */
    public Filter getFilter () {
        return filter;
    }

    /** Response listener for {@link #downloadFromUrl(String, DownloadPixmapResponseListener)} */
    public interface DownloadPixmapResponseListener {

        /** Called on the render thread when image was downloaded successfully.
         * @param pixmap */
        void downloadComplete (Pixmap pixmap);

        /** Called when image download failed. This might get called on a background thread. */
        void downloadFailed (Throwable t);
    }
}
