package com.github.terefang.jmelange.gfx;

import com.github.terefang.jmelange.apache.codec.binary.Base64OutputStream;
import com.github.terefang.jmelange.commons.gfx.GfxInterface;
import com.github.terefang.jmelange.gfx.impl.PdfImage;
import com.github.terefang.jmelange.commons.gfx.impl.PixelImage;
import com.github.terefang.jmelange.gfx.impl.SvgImage;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;

public class ImageUtil
{
    public static PixelImage pngImage(int _w, int _h)
    {
        return PixelImage.create(_w,_h);
    }
    
    public static SvgImage svgImage(int _w, int _h)
    {
        return SvgImage.create(_w,_h);
    }
    
    public static PdfImage pdfImage(int _w, int _h)
    {
        return PdfImage.create(_w,_h);
    }
    
    @SneakyThrows
    public static String imageToDataUrl(String _mediaType, GfxInterface _image)
    {
        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        _baos.write(("data:"+_mediaType+";base64,").getBytes());
        _baos.flush();
        Base64OutputStream _b64s = new Base64OutputStream(_baos, true);
        _image.save(_b64s);
        _b64s.flush();
        return new String(_baos.toByteArray());
    }
    
    @SneakyThrows
    public static String imageToDataUrl(GfxInterface _image)
    {
        if(_image instanceof PixelImage)
        {
            return imageToDataUrl("image/png", _image);
        }
        else if(_image instanceof SvgImage)
        {
            return imageToDataUrl("image/svg+xml", _image);
        }
        else if(_image instanceof PdfImage)
        {
            return imageToDataUrl("application/pdf", _image);
        }
        throw new IllegalArgumentException("unknown image type");
    }
}
