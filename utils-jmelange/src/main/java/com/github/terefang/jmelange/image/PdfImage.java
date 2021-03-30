package com.github.terefang.jmelange.image;

import com.github.terefang.jmelange.utils.ImageUtil;
import de.rototor.pdfbox.graphics2d.PdfBoxGraphics2D;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.util.Matrix;
import org.jfree.graphics2d.svg.SVGHints;

import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

public class PdfImage extends AbstractGfxInterface implements GfxInterface
{
    PdfCustomFontTextDrawer fontTextDrawer = new PdfCustomFontTextDrawer();
    PDDocument document;
    PDPage page;
    PdfBoxGraphics2D g2d;
    public static final PdfImage create(int _width, int _height) {
        return new PdfImage(_width, _height);
    }

    public void registerFont(Font _font, File _file)
    {
        this.fontTextDrawer.registerFont(_font.getFontName(), _file);
    }

    @SneakyThrows
    public PdfImage(int width, int height)
    {
        super();
        this.document = new PDDocument();
        this.page = new PDPage(new PDRectangle(width, height));
        this.document.addPage(this.page);
        this.g2d = new PdfBoxGraphics2D(this.document, width, height);
        this.g2d.setFontTextDrawer(this.fontTextDrawer);
    }

    @Override
    public Graphics2D getG2d() {
        return (Graphics2D) this.g2d.create();
    }

    @Override
    public void gSet(int _x, int _y, long _color) {
        Graphics2D _g = this.getG2d();
        _g.setColor(ImageUtil.createColor(_color));
        _g.fillRect(_x, _y, 1, 1);
        _g.dispose();
    }

    public String beginGroup()
    {
        return beginGroup(UUID.randomUUID().toString());
    }

    public String beginGroup(String _id)
    {
        this.g2d.setRenderingHint(SVGHints.KEY_BEGIN_GROUP, _id);
        return _id;
    }

    public void endGroup()
    {
        this.g2d.setRenderingHint(SVGHints.KEY_END_GROUP, "true");
    }

    @Override
    public int gGet(int _x, int _y) {
        return 0;
    }

    @SneakyThrows
    public void save(String _path) {
        this.save(new File(_path));
    }

    @SneakyThrows
    public void save(File _out)
    {
        this.save(new FileOutputStream(_out));
    }

    @SneakyThrows
    public void save(OutputStream _out)
    {
        this.g2d.dispose();
        PDFormXObject xform = this.g2d.getXFormObject();
        Matrix matrix = new Matrix();
        matrix.translate(0, this.page.getMediaBox().getHeight());
        PDPageContentStream contentStream = new PDPageContentStream(this.document, this.page);
        //contentStream.transform(matrix);
        contentStream.drawForm(xform);
        contentStream.close();

        this.document.save(new BufferedOutputStream(_out, 8192));
        document.close();
    }
}
