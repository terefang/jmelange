package com.github.terefang.jmelange.pdf.ext.image;

import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.loader.PdfResourceLoader;
import com.github.terefang.jmelange.pdf.core.g2d.PdfGraphics2D;
import com.github.terefang.jmelange.pdf.core.values.PdfFormXObject;
import com.github.terefang.jmelange.pdf.core.values.PdfString;
import com.github.terefang.jmelange.pdf.core.values.PdfXObject;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URI;

public class PdfSvgImage extends PdfFormXObject
{
    public PdfSvgImage(PdfDocument doc) {
        super(doc, true);
    }

    @SneakyThrows
    public static PdfSvgImage of(PdfDocument pdfDocument, PdfResourceLoader img, int w, int h)
    {
        PdfSvgImage psi = new PdfSvgImage(pdfDocument);
        psi.setWidth(w);
        psi.setHeight(h);

        psi.save();
        PdfGraphics2D _g2d = PdfGraphics2D.from((PdfXObject) psi);

        SVGUniverse _u = new SVGUniverse();
        URI _res = _u.loadSVG(img.getInputStream(), img.getName());
        SVGDiagram _d = _u.getDiagram(_res);
        _d.setIgnoringClipHeuristic(false);
        _d.setDeviceViewport(_g2d.getMediaDimension());

        _d.render(_g2d);
        _g2d.dispose();
        psi.restore();
        //double _a = Math.abs(_d.getWidth());
        //double _b = Math.abs(_d.getHeight());
        //psi.setFormBBox(-_a,-_b,_a,_b);
        psi.set("X_SvgSource", PdfString.of(img.getName()));
        psi.set("X_SvgViewRect", PdfString.of(""+_d.getViewRect()));
        psi.set("X_SvgWidthHeight", PdfString.of(""+_d.getWidth()+" "+_d.getHeight()));
        psi.set("X_SvgCenter", PdfString.of(""+_d.getViewRect().getCenterX()+" "+_d.getViewRect().getCenterY()));
        psi.set("X_SvgG2dRect", PdfString.of(""+_g2d.getMinX()+" "+_g2d.getMinY()+" "+_g2d.getMaxX()+" "+_g2d.getMaxY()));
        int x1 = (int)(_g2d.getMinX()*1.5d);
        int y1 = (int)(_g2d.getMinY()*1.5d);
        int x2 = (int)(_g2d.getMaxX()*1.5d);
        int y2 = (int)(_g2d.getMaxY()*1.5d);
        if(_d.getViewRect().getMinX()<x1) x1 = (int) _d.getViewRect().getMinX();
        if(_d.getViewRect().getMinY()<y1) y1 = (int) _d.getViewRect().getMinY();
        if(_d.getViewRect().getMaxX()>x2) x2 = (int) _d.getViewRect().getMaxX();
        if(_d.getViewRect().getMaxY()>y2) y2 = (int) _d.getViewRect().getMaxY();
        psi.setFormBBox(x1, y1, x2, y2);
        psi.setWidth(_d.getWidth());
        psi.setHeight(_d.getHeight());
        return psi;
    }

    @SneakyThrows
    public static BufferedImage render(PdfResourceLoader img, int w, int h, float _s, boolean _t, boolean _a)
    {
        SVGUniverse _u = new SVGUniverse();
        _u.setImageDataInlineOnly(true);
        URI _res = _u.loadSVG(img.getInputStream(), img.getName());

        SVGDiagram _d = _u.getDiagram(_res);
        double _scale = Math.PI/2d;
        if(_s>_scale) _scale = _s;
        BufferedImage bufferedImage = new BufferedImage((int)(_d.getWidth()*_scale), (int)(_d.getHeight()*_scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D _g2d = (Graphics2D) bufferedImage.getGraphics();
        _g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        _g2d.transform(AffineTransform.getScaleInstance(_scale,_scale));
        _g2d.setColor((_t||_a) ? new Color(Color.TRANSLUCENT, true) : Color.WHITE);
        _g2d.fillRect(0,0, (int)(_d.getWidth()*_scale), (int)(_d.getHeight()*_scale));
        _d.render(_g2d);
        _g2d.dispose();
        return bufferedImage;
    }
}
