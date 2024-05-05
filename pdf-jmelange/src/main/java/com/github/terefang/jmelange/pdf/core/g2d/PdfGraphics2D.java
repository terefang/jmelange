package com.github.terefang.jmelange.pdf.core.g2d;

/*
 * $Id: PdfGraphics2D.java,v 1.6 2007/09/22 12:58:40 gil1 Exp $
 *
 * $Date: 2007/09/22 12:58:40 $
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.content.AbstractPdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.image.PdfImage;
import com.github.terefang.jmelange.pdf.core.values.PdfFormXObject;
import com.github.terefang.jmelange.pdf.core.values.PdfName;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import com.github.terefang.jmelange.pdf.core.values.PdfXObject;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.*;
import java.awt.image.renderable.RenderableImage;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Map;

/**
 * This class is our implementation of AWT's Graphics class. It provides a
 * Java standard way of rendering into a PDF Document's Page.
 *
 * @author Peter T Mount, http://www.retep.org.uk/pdf/
 * @author Eric Z. Beard, ericzbeard@hotmail.com
 * @author Gilbert DeLeeuw, gil1@users.sourceforge.net
 * @version $Revision: 1.6 $, $Date: 2007/09/22 12:58:40 $
 */
public class PdfGraphics2D extends Graphics2D implements Serializable
{
    double minX, minY = Double.POSITIVE_INFINITY;
    double maxX, maxY = Double.NEGATIVE_INFINITY;

    void updateMinMaxXY(double _x, double _y)
    {
        _y= _y;
        if(_x<this.minX) this.minX = _x;
        if(_y<this.minY) this.minY = _y;
        if(_x>this.maxX) this.maxX = _x;
        if(_y>this.maxY) this.maxY = _y;
    }
    public static final PdfGraphics2D from(PdfPage _page)
    {
        PdfGraphics2D _g2d = new PdfGraphics2D();
        _g2d.init(_page);
        return _g2d;
    }

    public static final PdfGraphics2D from(PdfXObject _xo)
    {
        PdfGraphics2D _g2d = new PdfGraphics2D();
        _g2d.init(_xo);
        return _g2d;
    }

    /**
     * One degree in radians
     */
    private static final double degrees_to_radians = Math.PI/180.0;

    private static final int FILL = 1;
    private static final int STROKE = 2;
    private static final int CLIP = 3;

    private static final AffineTransform IDENTITY = new AffineTransform();

    private static final Stroke DEF_STROKE = new BasicStroke();

    /*
     * NOTE: The original class is the work of Peter T. Mount, who released it
     * in the uk.org.retep.pdf package.  It was modified by Eric Z. Beard as
     * follows:
     * The package name was changed to gnu.pdf.
     * The formatting was changed a little bit.
     * This used to subclass an abstract class in a different package with
     *   the same name (confusing). Now it's one concrete class.
     * drawImage() was implemented
     * It is still licensed under the LGPL.
     */


    // Implementation notes:
    //
    // Pages 333-335 of the PDF Reference Manual
    //
    // Unless absolutely required, use the moveto, lineto and rectangle
    // operators to perform those actions.
    // They contain some extra optimizations
    // which will reduce the output size by up to half in some cases.
    //
    // About fill operators: For correct operation, any fill operation should
    // start with closeBlock(), which will ensure any previous path is completed,
    // otherwise you may find the fill will include previous items

    private static final DecimalFormat df = new DecimalFormat("#.######");

    private Color background;

    /**
     * This is true for any Graphics instance that didn't create the stream.
     * @see #create
     */
    private boolean child;

    private Area clip;

    /**
     * This holds the current clipRectangle
     */
    protected Rectangle clipRectangle;

    private Composite composite;

    private Graphics2D dg2 = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB).createGraphics();

    /**
     * This is the current font (in Java format)
     */
    private Font    font;

    /**
     * Part of the optimizer:
     * When true, we are drawing a path.
     */
    private boolean inStroke;

    /**
     * Part of the optimizer:
     * When true, we are within a Text Block.
     */
    private boolean inText;   // true if within a Text Block - see newTextBlock()

    /**
     * The stroke line cap code;
     */
    private int lineCap = 0;

    /**
     * The stroke line join code
     */
    private int lineJoin = 0;

    /**
     * The stroke line width
     */
    private float lineWidth = 1.0f;

    /**
     * Part of the optimizer:
     * The last known moveto/lineto x coordinate
     * @see #moveto
     * @see #lineto
     */
    private float lx;             // last known moveto/lineto coordinates

    /**
     * Part of the optimizer:
     * The last known moveto/lineto y coordinate
     * @see #moveto
     * @see #lineto
     */
    private float ly;             // last known moveto/lineto coordinates


    private float miterLimit = 10.0f;

    /**
     * Part of the optimizer:
     * When true, the font has changed.
     */
    private boolean newFont;    // true if the font changes - see newTextBlock()

    private Stroke originalStroke;

    // Original transform
    private AffineTransform oTransform;

    /**
     * This is a reference to the PdfPage we are rendering to.
     */
    private PdfPage page;

    /**
     * This is the current pen/fill color
     */
    private Paint paint;

    /**
     * This is the current font (in PDF format)
     */
    private PdfFont pdffont;

    /**
     * Part of the optimizer:
     * This is written to the stream when the newPath() is called. np then clears
     * this value.
     */
    private String pre_np;

    // PDF space transform
    private AffineTransform pTransform;

    /**
     * This is the PrintWriter used to write PDF drawing commands to the Stream
     */
    private PrintWriter pw;
    /**
     * RenderingHints
     */
    private RenderingHints rhints = new RenderingHints(null);

    private Stroke stroke;

    // Start of Graphics2D properties

    // Java space transform
    private AffineTransform transform;
    /**
     * This is used to translate coordinates
     */
    protected float trax;

    /**
     * This is used to translate coordinates
     */
    protected float tray;


    /**
     * Part of the optimizer:
     * The last x coordinate when rendering text
     */
    private float tx;             // the last coordinate for text rendering



    /**
     * Part of the optimizer:
     * The last y coordinate when rendering text
     */
    private float ty;             // the last coordinate for text rendering
    private AbstractPdfContent stream;

    /**
     * @see Graphics2D#addRenderingHints(Map)
     */
    public void addRenderingHints(Map<?,?> hints) {
        rhints.putAll(hints);
    }

    /**
     * This produces an arc by breaking it down into one or more Bezier curves.
     * It is used internally to implement the drawArc and fillArc methods.
     *
     * @param axc X coordinate of arc centre
     * @param ayc Y coordinate of arc centre
     * @param width of bounding rectangle
     * @param height of bounding rectangle
     * @param ang1 Start angle
     * @param ang2 End angle
     * @param clockwise true to draw clockwise, false anti-clockwise
     */
    public void arc(double axc,double ayc,
                    double width,double height,
                    double ang1,double ang2,
                    boolean clockwise) {

        double adiff;
        double x0, y0;
        double x3r, y3r;
        boolean first = true;

        // may not need this
        //if( ar < 0 ) {
        //ang1 += fixed_180;
        //ang2 += fixed_180;
        //ar = - ar;
        //}

        double ang1r = (ang1%360.0)*degrees_to_radians;

        double sin0 = Math.sin(ang1r);
        double cos0 = Math.cos(ang1r);

        x0 = axc + width*cos0;
        y0 = ayc + height*sin0;

        // NB: !clockwise here as Java Space is inverted to User Space
        if( !clockwise ) {
            // Quadrant reduction
            while ( ang1 < ang2 ) ang2 -= 360.0;
            while ( (adiff = ang2 - ang1) < -90.0 ) {
                double w = sin0; sin0 = -cos0; cos0 = w;
                x3r = axc + width*cos0;
                y3r = ayc + height*sin0;
                arc_add(first,
                        width, height,
                        x0, y0,
                        x3r, y3r,
                        (x0 + width*cos0),
                        (y0 + height*sin0)
                );

                x0 = x3r;
                y0 = y3r;
                ang1 -= 90.0;
                first = false;
            }
        } else {
            // Quadrant reduction
            while ( ang2 < ang1 ) ang2 += 360.0;
            while ( (adiff = ang2 - ang1) > 90.0 ) {
                double w = cos0; cos0 = -sin0; sin0 = w;
                x3r = axc + width*cos0;
                y3r = ayc + height*sin0;
                arc_add(first,
                        width, height,
                        x0, y0,
                        x3r, y3r,
                        (x0 + width*cos0),
                        (y0 + height*sin0)
                );

                x0 = x3r;
                y0 = y3r;
                ang1 += 90.0;
                first = false;
            }
        }

        // Compute the intersection of the tangents.
        // We know that -fixed_90 <= adiff <= fixed_90.
        double trad = Math.tan(adiff * (degrees_to_radians / 2));
        double ang2r = ang2 * degrees_to_radians;
        double xt = x0 - trad * width*sin0;
        double yt = y0 + trad * height*cos0;
        arc_add(first, width, height, x0, y0,
                (axc + width * Math.cos(ang2r)),
                (ayc + height * Math.sin(ang2r)),
                xt, yt);
    }


    /**
     * Used by the arc method to actually add an arc to the path
     * Important: We write directly to the stream here, because this method
     * operates in User space, rather than Java space.
     * @param first true if the first arc
     * @param w width
     * @param h height
     * @param x0 coordinate
     * @param y0 coordinate
     * @param x3 coordinate
     * @param y3 coordinate
     * @param xt coordinate
     * @param yt coordinate
     */
    private void arc_add(boolean first,
                         double w,double h,
                         double x0,double y0,
                         double x3,double y3,
                         double xt,double yt) {
        double dx = xt - x0, dy = yt - y0;
        double dist = dx*dx + dy*dy;
        double w2 = w*w, h2=h*h;
        double r2 = w2+h2;

        double fw = 0.0, fh = 0.0;
        if(dist < (r2*1.0e8)) {
            // JM
            fw = (w2 != 0.0) ? ((4.0/3.0)/(1+Math.sqrt(1+dist/w2))) : 0.0;
            fh = (h2 != 0.0) ? ((4.0/3.0)/(1+Math.sqrt(1+dist/h2))) : 0.0;
        }

        // The path must have a starting point
        if(first)
            moveto(x0,y0);

        double x = x0+((xt-x0)*fw);
        double y = y0+((yt-y0)*fh);
        x0 = x3+((xt-x3)*fw);
        y0 = y3+((yt-y3)*fh);

        // Finally the actual curve.
        curveto(x,y,x0,y0,x3,y3);
    }


    /**
     * This simply draws a White Rectangle to clear the area
     * @param x coordinate
     * @param y coordinate
     * @param w width
     * @param h height
     */
    public void clearRect(int x,int y,int w,int h) {
        closeBlock();
        pw.print("q 1 1 1 RG ");// save state, set colour to White
        drawRect(x,y,w,h);
        closeBlock("B Q");               // close fill & stroke, then restore state
    }



    /**
     * @see Graphics2D#clip(Shape)
     */
    public void clip(Shape s) {
        if (s == null) {
            setClip(null);
            return;
        }
        s = transform.createTransformedShape(s);
        if (clip == null)
            clip = new Area(s);
        else
            clip.intersect(new Area(s));
//      followPath(s, CLIP);
    }



    /**
     * This extra method allows PDF users to clip to a Polygon.
     *
     * <p>In theory you could use setClip(), except that java.awt.Graphics
     * only supports Rectangle with that method, so we will have an extra
     * method.
     * @param p Polygon to clip to
     */
    public void clipPolygon(Polygon p) {
        closeBlock();            // finish off any existing path
        polygon(p.xpoints,p.ypoints,p.npoints);
        closeBlock("W");         // clip to current path
        clipRectangle = p.getBounds();
    }

    /**
     * Clips to a set of coordinates
     * @param x coordinate
     * @param y coordinate
     * @param w width
     * @param h height
     */
    public void clipRect(int x,int y,int w,int h) {
        setClip(x,y,w,h);
    }


    /**
     * All functions should call this to close any existing optimized blocks.
     */
    void closeBlock() {
        closeBlock("S");
    }

    public void flush()
    {
        this.pw.flush();
    }

    /**
     * <p>This is used by code that use the path in any way other than Stroke
     * (like Fill, close path & Stroke etc). Usually this is used internally.</p>
     *
     * @param code PDF operators that will close the path
     */
    void closeBlock(String code) {
        if(inText) {
            pw.println("ET Q");
            // setOrientation(); // fixes Orientation matrix
        }

        if(inStroke) {
            pw.println(code);
        }

        inStroke=inText=false;
    }


    /**
     * This is unsupported - how do you do this with Vector graphics?
     * @param x coordinate
     * @param y coordinate
     * @param w width
     * @param h height
     * @param dx coordinate
     * @param dy coordinate
     */
    public void copyArea(int x,int y,int w,int h,int dx,int dy) {
        // Hmm...   Probably need to keep track of everything
        // that has been drawn so far to get the contents of an area
        System.err.println(this.getClass().toString()+" hit unimplemented method; copyArea");
    }

    //============ Line operations =======================


    /**
     * <p>This returns a child instance of this Graphics object. As with AWT, the
     * affects of using the parent instance while the child exists, is not
     * determined.</p>
     *
     * <p>Once complete, the child should be released with it's dispose()
     * method which will restore the graphics state to it's parent.</p>
     *
     * @return Graphics object to render onto the page
     */
    public Graphics create() {
        closeBlock();

        PdfGraphics2D g = createGraphic();

        // The new instance inherits a few items
//    g.media = new Rectangle(media);
        g.trax = trax;
        g.tray = tray;
        g.clipRectangle = new Rectangle(clipRectangle);

        return (Graphics) g;
    } // end create()

    /**
     * This method creates a new instance of the class based on the page
     * and a print writer.
     *
     */
    protected PdfGraphics2D createGraphic()
    {
        PdfGraphics2D g = new PdfGraphics2D();
        g._reg = this._reg;
        g.initChild(this.page);
        return g;
    }

    /**
     * This extension appends a Bezier curve to the path. The curve
     * extends from the current point to (x2,y2) using the current
     * point and (x1,y1) as the Bezier control points.
     * <p>The new current point is (x2,y2)
     *
     * @param x1 Second control point
     * @param y1 Second control point
     * @param x2 Destination point
     * @param y2 Destination point
     */
    public void curveto(double x1,double y1,double x2,double y2) {
        newPath();
        pw.println(cxy(x1,y1)+cxy(x2,y2)+"v");
        lx=(float)x2;
        ly=(float)y2;
        updateMinMaxXY(x1, y1);
        updateMinMaxXY(x2, y2);
    }

    /**
     * This extension appends a Bezier curve to the path. The curve
     * extends from the current point to (x3,y3) using (x1,y1) and
     * (x2,y2) as the Bezier control points.
     * <p>The new current point is (x3,y3)
     *
     * @param x1 First control point
     * @param y1 First control point
     * @param x2 Second control point
     * @param y2 Second control point
     * @param x3 Destination point
     * @param y3 Destination point
     */
    public void curveto(double x1,double y1,double x2,double y2,double x3,double y3) {
        newPath();
        pw.println(cxy(x1,y1)+cxy(x2,y2)+cxy(x3,y3)+"c");
        lx=(float)x3;
        ly=(float)y3;
        updateMinMaxXY(x1, y1);
        updateMinMaxXY(x2, y2);
        updateMinMaxXY(x3, y3);
    }

    /**
     * This extension appends a Bezier curve to the path. The curve
     * extends from the current point to (x2,y2) using the current
     * point and (x1,y1) as the Bezier control points.
     * <p>The new current point is (x2,y2)
     *
     * @param x1 Second control point
     * @param y1 Second control point
     * @param x2 Destination point
     * @param y2 Destination point
     */
    public void curveto(int x1,int y1,int x2,int y2) {
        newPath();
        pw.println(cxy(x1,y1)+cxy(x2,y2)+"v");
        lx=x2;
        ly=y2;
        updateMinMaxXY(x1, y1);
        updateMinMaxXY(x2, y2);
    }

    /**
     * This extension appends a Bezier curve to the path. The curve
     * extends from the current point to (x3,y3) using (x1,y1) and
     * (x2,y2) as the Bezier control points.
     * <p>The new current point is (x3,y3)
     *
     * @param x1 First control point
     * @param y1 First control point
     * @param x2 Second control point
     * @param y2 Second control point
     * @param x3 Destination point
     * @param y3 Destination point
     */
    public void curveto(int x1,int y1,int x2,int y2,int x3,int y3) {
        newPath();
        pw.println(cxy(x1,y1)+cxy(x2,y2)+cxy(x3,y3)+"c");
        lx=x3;
        ly=y3;
        updateMinMaxXY(x1, y1);
        updateMinMaxXY(x2, y2);
        updateMinMaxXY(x3, y3);
    }

    /**
     * This extension appends a Bezier curve to the path. The curve
     * extends from the current point to (x2,y2) using (x1,y1) and
     * the end point as the Bezier control points.
     * <p>The new current point is (x2,y2)
     *
     * @param x1 Second control point
     * @param y1 Second control point
     * @param x2 Destination point
     * @param y2 Destination point
     */
    public void curveto2(double x1,double y1,double x2,double y2) {
        newPath();
        pw.println(cxy(x1,y1)+cxy(x2,y2)+"y");
        lx=(float)x2;
        ly=(float)y2;
        updateMinMaxXY(x1, y1);
        updateMinMaxXY(x2, y2);
    }


    // Arcs are horrible and complex. They are at the end of the
    // file, because they are the largest. This is because, unlike
    // Postscript, PDF doesn't have any arc operators, so we must
    // implement them by converting into one or more Bezier curves
    // (which is how Postscript does them internally).

    /**
     * This extension appends a Bezier curve to the path. The curve
     * extends from the current point to (x2,y2) using (x1,y1) and
     * the end point as the Bezier control points.
     * <p>The new current point is (x2,y2)
     *
     * @param x1 Second control point
     * @param y1 Second control point
     * @param x2 Destination point
     * @param y2 Destination point
     */
    public void curveto2(int x1,int y1,int x2,int y2) {
        newPath();
        pw.println(cxy(x1,y1)+cxy(x2,y2)+"y");
        lx=x2;
        ly=y2;
        updateMinMaxXY(x1, y1);
        updateMinMaxXY(x2, y2);
    }

    /**
     * Converts the Java space dimension into pdf.
     * @param w width
     * @param h height
     * @return String containing the coordinates in PDF space
     */
    private String cwh(double w,double h) {
        double nw=w,nh=h; // scratch

//    switch(mediaRot) {
//    case PageFormat.PORTRAIT:
        // Portrait
        //nw = w;
        nh = -h;
//      break;
//
//    case PageFormat.LANDSCAPE:
//      // Landscape
//      nw = h;
//      nh = w;
//      break;
//
////    case 180:
////      // Inverse Portrait
////      nw = -w;
////      //nh = h;
////      break;
//
//    case PageFormat.REVERSE_LANDSCAPE:
//      // Seascape
//      nw = -h;
//      nh = -w;
//      break;
//    }

        return ""+df.format(nw)+" "+df.format(nh)+" ";
    }

    /**
     * Converts the Java space dimension into pdf.
     * @param w width
     * @param h height
     * @return String containing the coordinates in PDF space
     */
    private String cwh(int w,int h) {
        return cwh((double)w,(double)h);
    }


    /**
     * Converts the Java space coordinates into pdf.
     * @param x coordinate
     * @param y coordinate
     * @return String containing the coordinates in PDF space
     */
    private String cxy(double x, double y) {
//		double nx = x, ny = y; // scratch
//		double mh = page.getPageFormat().getHeight();

        Point2D ptSrc = new Point2D.Double(x, y);
        Point2D ptDst = new Point2D.Double();
        transform.transform(ptSrc, ptDst);

//		x += trax;
//		y += tray;
//
//		nx = x;
//		ny = mh - y;
//
//		System.out.println("\ncxy(" + ptSrc.getX() + ", " + ptSrc.getY() + ")");
//		System.out.println("Old [" + nx + "," + ny + "]");
//		System.out.println("Trn [" + ptDst.getX() + ", " + ptDst.getY() + "]");
//
//		return "" + df.format(nx) + " " + df.format(ny) + " ";
        return ""+df.format(ptDst.getX())+" "+df.format(ptDst.getY())+" ";

    }

    /**
     * Converts the Java space coordinates into pdf.
     * @param x coordinate
     * @param y coordinate
     * @return String containing the coordinates in PDF space
     */
    private String cxy(int x,int y) {
        return cxy((double)x,(double)y);
    }

    /**
     * <p>This releases any resources used by this Graphics object. You must use
     * this method once finished with it. Leaving it open will leave the PDF
     * stream in an inconsistent state, and will produce errors.</p>
     *
     * <p>If this was created with Graphics.create() then the parent instance
     * can be used again. If not, then this closes the graphics operations for
     * this page when used with PDFJob.</p>
     *
     * <p>When using PdfPage, you can create another fresh Graphics instance,
     * which will draw over this one.</p>
     *
     */
    public void dispose() {
        closeBlock();
        if(child) {
            pw.println("Q");    // restore graphics context
            pw.flush();
        }
        else {
            pw.flush();
            pw.close(); // close the stream if were the parent
        }
    }



    // *********************************************
    // **** Implementation of java.awt.Graphics ****
    // *********************************************

    //============ Rectangle operations =======================

    /**
     * @see Graphics2D#draw(Shape)
     */
    public void draw(Shape s) {
        followPath(s, STROKE);
    }

    /**
     * <p>Not implemented</p>
     *
     * <p>Draws a 3-D highlighted outline of the specified rectangle.
     * The edges of the rectangle are highlighted so that they appear
     * to be beveled and lit from the upper left corner.
     * The colors used for the highlighting effect are determined based on
     * the current color. The resulting rectangle covers an area that
     * is width + 1 pixels wide by height + 1 pixels tall.
     *</p>
     *
     * @param x an <code>int</code> value
     * @param y an <code>int</code> value
     * @param width an <code>int</code> value
     * @param height an <code>int</code> value
     * @param raised a <code>boolean</code> value
     */
    public void draw3DRect(int x, int y,
                           int width, int height, boolean raised) {
        // Not implemented
        System.err.println(this.getClass().toString()+" hit unimplemented method; draw3DRect");
    }

    /**
     * Draws an arc
     * @param x coordinate
     * @param y coordinate
     * @param w width
     * @param h height
     * @param sa Start angle
     * @param aa End angle
     */
    public void drawArc(int x,int y,int w,int h,int sa,int aa) {
        w=w>>1;
        h=h>>1;
        x+=w;
        y+=h;

        arc((double)x,(double)y,
                (double)w,(double)h,
                (double)-sa,(double)(-sa-aa),
                false);
    }

    /**
     * <p>Not implemented</p>
     *
     * @param data a <code>byte[]</code> value
     * @param offset an <code>int</code> value
     * @param length an <code>int</code> value
     * @param x an <code>int</code> value
     * @param y an <code>int</code> value
     */
    public void drawBytes(byte[] data, int offset, int length, int x, int y) {
        System.err.println(this.getClass().toString()+" hit unimplemented method; drawBytes");
    }


    //============ Optimizers =======================

    /**
     * @see Graphics2D#drawGlyphVector(GlyphVector, float, float)
     */
    public void drawGlyphVector(GlyphVector g, float x, float y) {
        Shape s = g.getOutline(x, y);
        fill(s);
    }

    /**
     * @see Graphics2D#drawImage(BufferedImage, BufferedImageOp, int, int)
     */
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        BufferedImage result = img;
        if (op != null) {
            result = op.createCompatibleDestImage(img, img.getColorModel());
            result = op.filter(img, result);
        }
        drawImage(result, x, y, null);
    }


    /**
     * @see Graphics2D#drawImage(Image, AffineTransform, ImageObserver)
     */
    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        // return drawImage(img, null, xform, null, obs);
        System.err.println(this.getClass().toString()+" hit unimplemented method; drawImage");
        return true;
    }

    /**
     * <p>Draw's an image onto the page, with a backing colour.</p>
     *
     * @param img The java.awt.Image
     * @param x coordinate on page
     * @param y coordinate on page
     * @param bgcolor Background colour
     * @param obs ImageObserver
     * @return true if drawn
     */
    public boolean drawImage(Image img,int x,int y,Color bgcolor,
                             ImageObserver obs) {
        return drawImage(img,x,y,img.getWidth(obs),img.getHeight(obs),
                bgcolor,obs);
    }

    /**
     * Draw's an image onto the page
     * @param img The java.awt.Image
     * @param x coordinate on page
     * @param y coordinate on page
     * @param obs ImageObserver
     * @return true if drawn
     */
    public boolean drawImage(Image img,int x,int y,ImageObserver obs) {
        return drawImage(img,x,y,img.getWidth(obs),img.getHeight(obs),obs);
    }

    /**
     * <p>Draw's an image onto the page, with a backing colour.</p>
     *
     * @param img The java.awt.Image
     * @param x coordinate on page
     * @param y coordinate on page
     * @param w Width on page
     * @param h height on page
     * @param bgcolor Background colour
     * @param obs ImageObserver
     * @return true if drawn
     */
    public boolean drawImage(Image img,int x,int y,int w,int h,
                             Color bgcolor,ImageObserver obs) {
        closeBlock();
        pw.print("q "); // save state
        Color c = getColor();        // save current colour
        setColor(bgcolor);      // change the colour
        drawRect(x,y,w,h);
        closeBlock("B Q");               // fill stroke, restore state
        paint = c;              // restore original colour
        return drawImage(img,x,y,img.getWidth(obs),img.getHeight(obs),obs);
    }

    /**
     * <p>Draws an image onto the page.</p>
     *
     * <p>This method is implemented with ASCIIbase85 encoding and the
     * zip stream deflater.  It results in a stream that is anywhere
     * from 3 to 10 times as big as the image.  This obviously needs some
     * improvement, but it works well for small images</p>
     *
     * @param img The java.awt.Image
     * @param x coordinate on page
     * @param y coordinate on page
     * @param w Width on page
     * @param h height on page
     * @param obs ImageObserver
     * @return true if drawn
     */
    public boolean drawImage(Image img,int x,int y,int w,int h,
                             ImageObserver obs) {
        closeBlock();
        PdfImage image = null;
        if(this.page==null)
        {
            image = PdfImage.of(this.stream, img,obs, "", false, -1, 0);
        }
        else
        {
            image = PdfImage.of(this.page, img,obs, 0);
        }

        // q w 0 0 h x y cm % the coordinate matrix
        pw.print("q " +
                image.getWidth() +
                " 0 0 " +
                image.getHeight() +
                " " + x + " " +
                ((int) getMediaDimension().getHeight()-y-image.getHeight()) +
                " cm \n" + PdfName.of(image.getResource().getResName()).asString() + " Do\nQ\n");
        return false;
    }

    public Rectangle getMediaDimension()
    {
        if(this.page!=null)
        {
            return new Rectangle(this.page.getMediaboxRight(), this.page.getMediaboxTop());
        }

        if(this.stream instanceof PdfXObject)
        {
            PdfXObject _xo = (PdfXObject) this.stream;

            return new Rectangle((int)_xo.getWidth(), (int)_xo.getHeight());
        }

        return null;
    }

    /**
     * Draw's an image onto the page, with scaling
     * <p>This is not yet supported.
     *
     * @param img The java.awt.Image
     * @param dx1 coordinate on page
     * @param dy1 coordinate on page
     * @param dx2 coordinate on page
     * @param dy2 coordinate on page
     * @param sx1 coordinate on image
     * @param sy1 coordinate on image
     * @param sx2 coordinate on image
     * @param sy2 coordinate on image
     * @param bgcolor Background colour
     * @param obs ImageObserver
     * @return true if drawn
     */
    public boolean drawImage(Image img,int dx1,int dy1,int dx2,
                             int dy2,int sx1,int sy1,int sx2,int sy2,
                             Color bgcolor,ImageObserver obs) {
        System.err.println(this.getClass().toString()+" hit unimplemented method; drawImage");
        return false;
    }

    //============ Clipping operations =======================

    /**
     * Draw's an image onto the page, with scaling
     * <p>This is not yet supported.
     *
     * @param img The java.awt.Image
     * @param dx1 coordinate on page
     * @param dy1 coordinate on page
     * @param dx2 coordinate on page
     * @param dy2 coordinate on page
     * @param sx1 coordinate on image
     * @param sy1 coordinate on image
     * @param sx2 coordinate on image
     * @param sy2 coordinate on image
     * @param obs ImageObserver
     * @return true if drawn
     */
    public boolean drawImage(Image img,int dx1,int dy1,int dx2,
                             int dy2,int sx1,int sy1,int sx2,int sy2,
                             ImageObserver obs) {
        // This shouldn't be too bad, just change the coordinate matrix
        System.err.println(this.getClass().toString()+" hit unimplemented method; drawImage");
        return false;
    }

    /**
     * Draws a line between two coordinates.
     *
     * If the first coordinate is the same as the last one drawn
     * (i.e. a previous drawLine, moveto, etc) it is ignored.
     * @param x1 coordinate
     * @param y1 coordinate
     * @param x2 coordinate
     * @param y2 coordinate
     */
    public void drawLine(int x1,int y1,int x2,int y2) {
        moveto(x1,y1);
        lineto(x2,y2);
    }

    //============ Arcs operations ==============================
    // These are the standard Graphics operators. They use the
    // arc extension operators to achieve the affect.

    /**
     * <p>Draws an oval</p>
     *
     * @param x coordinate
     * @param y coordinate
     * @param w width
     * @param h height
     */
    public void drawOval(int x,int y,int w,int h) {
        drawArc(x, y, w, h, 0, 360);
    }

    /**
     * Draws a polygon, linking the first and last coordinates.
     * @param xp Array of x coordinates
     * @param yp Array of y coordinates
     * @param np number of points in polygon
     */
    public void drawPolygon(int[] xp,int[] yp,int np) {
        polygon(xp,yp,np);
        closeBlock("s"); // close path and stroke
    }

    /**
     * Draws a polyline. The first and last coordinates are not linked.
     * @param xp Array of x coordinates
     * @param yp Array of y coordinates
     * @param np number of points in polyline
     */
    public void drawPolyline(int[] xp,int[] yp,int np) {
        polygon(xp,yp,np);
        // no stroke, as we keep the optimizer in stroke state
    }

    /**
     * We override Graphics.drawRect as it doesn't join the 4 lines.
     * Also, PDF provides us with a Rectangle operator, so we will use that.
     * @param x coordinate
     * @param y coordinate
     * @param w width
     * @param h height
     */
    public void drawRect(int x,int y,int w,int h) {
        newPath();
        pw.print(cxy(x,y)+cwh(w,h)+"re "); // rectangle
        lx=x; // I don't know if this is correct, but lets see if PDF ends
        ly=y; // the rectangle at it's start.
        // stroke (optimized)
        updateMinMaxXY(x, y);
        updateMinMaxXY(x+w, y+h);
    }


    /**
     * @see Graphics2D#drawRenderableImage(RenderableImage, AffineTransform)
     */
    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
        drawRenderedImage(img.createDefaultRendering(), xform);
    }

    /**
     * @see Graphics2D#drawRenderedImage(RenderedImage, AffineTransform)
     */
    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        BufferedImage image = null;
        if (img instanceof BufferedImage) {
            image = (BufferedImage)img;
        } else {
            ColorModel cm = img.getColorModel();
            int width = img.getWidth();
            int height = img.getHeight();
            WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            Hashtable<String, Object> properties = new Hashtable<String, Object>();
            String[] keys = img.getPropertyNames();
            if (keys!=null) {
                for (int i = 0; i < keys.length; i++) {
                    properties.put(keys[i], img.getProperty(keys[i]));
                }
            }
            BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
            img.copyData(raster);
            image=result;
        }
        drawImage(image, xform, null);
    }

    /**
     * This is not yet implemented
     *
     * @param x coordinate
     * @param y coordinate
     * @param w width
     * @param h height
     * @param aw a-width
     * @param ah a-height
     */
    public void drawRoundRect(int x,int y,int w,int h,int aw,int ah) {
        System.err.println(this.getClass().toString()+" hit unimplemented method; drawRoundRect");
    }

    //============ Oval operations =======================

    /**
     * Draws a string using a AttributedCharacterIterator.
     * <p>This is not supported yet, as I have no idea what an
     * AttributedCharacterIterator is.
     * <p>This method is new to the Java2 API.
     */
    public void drawString(java.text.AttributedCharacterIterator aci,
                           float x,float y) {
        System.err.println(this.getClass().toString()+" hit unimplemented method; drawString");
    }

    /**
     * Draws a string using a AttributedCharacterIterator.
     * <p>This is not supported yet, as I have no idea what an
     * AttributedCharacterIterator is.
     * <p>This method is new to the Java2 API.
     */
    public void drawString(java.text.AttributedCharacterIterator aci,
                           int x,int y) {
        System.err.println(this.getClass().toString()+" hit unimplemented method; drawString");
    }


    public void drawString(String s,float x,float y) {
        newTextBlock(x, y);
        pw.println(" [ "+this.pdffont.encodeToString(s, 0,0)+" ] TJ");
    }

    /**
     * This draws a string.
     *
     * @oaran s String to draw
     * @param x coordinate
     * @param y coordinate
     */
    public void drawString(String s,int x,int y) {
        newTextBlock(x,y);
        pw.println(" [ "+this.pdffont.encodeToString(s, 0,0)+" ] TJ");
    }

    /**
     * @see Graphics2D#fill(Shape)
     */
    public void fill(Shape s) {
        followPath(s, FILL);
    }

    /**
     * <p>Not implemented</p>
     *
     * @param x an <code>int</code> value
     * @param y an <code>int</code> value
     * @param width an <code>int</code> value
     * @param height an <code>int</code> value
     * @param raised a <code>boolean</code> value
     */
    public void fill3DRect(int x, int y,
                           int width, int height, boolean raised) {
        // Not implemented
        System.err.println(this.getClass().toString()+" hit unimplemented method; fill3DRect");
    }

    /**
     * Fills an arc, joining the start and end coordinates
     * @param x coordinate
     * @param y coordinate
     * @param w width
     * @param h height
     * @param sa Start angle
     * @param aa End angle
     */
    public void fillArc(int x,int y,int w,int h,int sa,int aa) {
        // here we fool the optimizer. We force any open path to be closed,
        // then draw the arc. Finally, as the optimizer hasn't stroke'd the
        // path, we close and fill it, and mark the Stroke as closed.
        //
        // Note: The lineto to the centre of the object is required, because
        //       the fill only fills the arc. Skipping this includes an extra
        //       chord, which isn't correct. Peter May 31 2000
        closeBlock();
        drawArc(x,y,w,h,sa,aa);
        lineto(x+(w>>1),y+(h>>1));
        closeBlock("b"); // closepath and fill
        updateMinMaxXY(x,y);
        updateMinMaxXY(x+(w>>1),y+(h>>1));
    }

    //============ Extension operations ==============================
    // These are extensions, and provide access to PDF Specific
    // operators.

    /**
     * <p>Draws a filled oval</p>
     *
     * @param x coordinate
     * @param y coordinate
     * @param w width
     * @param h height
     */
    public void fillOval(int x,int y,int w,int h) {
        fillArc(x, y, w, h, 0, 360);
    }

    //============ Polygon operations =======================

    /**
     * Fills a polygon.
     * @param xp Array of x coordinates
     * @param yp Array of y coordinates
     * @param np number of points in polygon
     */
    public void fillPolygon(int[] xp,int[] yp,int np) {
        closeBlock();    // finish off any previous paths
        polygon(xp,yp,np);
        closeBlock("b"); // closepath, fill and stroke
    }

    //============ Image operations =======================

    /**
     * Fills a rectangle with the current colour
     *
     * @param x coordinate
     * @param y coordinate
     * @param w width
     * @param h height
     */
    public void fillRect(int x,int y,int w,int h) {
        // end any path & stroke. This ensures the fill is on this
        // rectangle, and not on any previous graphics
        closeBlock();
        drawRect(x,y,w,h);
        closeBlock("B"); // rectangle, fill stroke
    }

    //============ Round Rectangle operations =======================

    /**
     * This is not yet implemented
     *
     * @param x coordinate
     * @param y coordinate
     * @param w width
     * @param h height
     * @param aw a-width
     * @param ah a-height
     */
    public void fillRoundRect(int x,int y,int w,int h,int aw,int ah) {
        System.err.println(this.getClass().toString()+" hit unimplemented method; fillRoundRect");
    }

    ///////////////////////////////////////////////
    //
    //
    //		implementation specific methods
    //
    //


    private void followPath(Shape s, int drawType) {
        PathIterator points;

        if (s==null) return;

        if (drawType==STROKE) {
            if (!(stroke instanceof BasicStroke)) {
                s = stroke.createStrokedShape(s);
                followPath(s, FILL);
                return;
            }
        }
//      if (drawType==STROKE) {
//          setStrokeDiff(stroke, oldStroke);
//          oldStroke = stroke;
//          setStrokePaint();
//      }
//      else if (drawType==FILL)
//          setFillPaint();
        points = s.getPathIterator(IDENTITY);
        int segments = 0;
        float[] coords = new float[6];
        while(!points.isDone()) {
            segments++;
            int segtype = points.currentSegment(coords);
            switch(segtype) {
                case PathIterator.SEG_CLOSE:
                    pw.print("h ");
                    break;

                case PathIterator.SEG_CUBICTO:
                    curveto(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                    break;

                case PathIterator.SEG_LINETO:
                    lineto(coords[0], coords[1]);
                    break;

                case PathIterator.SEG_MOVETO:
                    moveto(coords[0], coords[1]);
                    break;

                case PathIterator.SEG_QUADTO:
                    curveto(coords[0], coords[1], coords[2], coords[3]);
                    break;
            }
            points.next();
        }

        switch (drawType) {
            case FILL:
                if (segments > 0) {
                    if (points.getWindingRule() == PathIterator.WIND_EVEN_ODD)
                        closeBlock("B*");
                    else
                        closeBlock("B");
                }
                break;
            case STROKE:
                if (segments > 0)
                    closeBlock("S");
                break;
            case CLIP:
            default: //drawType==CLIP
                if (segments == 0)
                    drawRect(0, 0, 0, 0);
                if (points.getWindingRule() == PathIterator.WIND_EVEN_ODD)
                    closeBlock("W*");
                else
                    closeBlock("W");
        }
    }

    /**
     * @see Graphics2D#getBackground()
     */
    public Color getBackground() {
        return background;
    }

    /**
     * Returns the Shape of the clipping region
     * As my JDK docs say, this may break with Java 2D.
     * @return Shape of the clipping region
     */
    public Shape getClip() {
        return null;
    }

    /**
     * Returns the Rectangle that fits the current clipping region
     * @return the Rectangle that fits the current clipping region
     */
    public Rectangle getClipBounds() {
        return clipRectangle;
    }

    //============ Color operations =======================

    /**
     * Returns the current pen Colour
     * @return the current pen Colour
     */
    public Color getColor() {
        return (paint instanceof Color) ? (Color) paint : Color.black;
    }

    /**
     * @see Graphics2D#getComposite()
     */
    public Composite getComposite() {
        return composite;
    }

    /**
     * @see Graphics2D#getDeviceConfiguration()
     */
    public GraphicsConfiguration getDeviceConfiguration() {
        return dg2.getDeviceConfiguration();
    }

    /**
     * Return's the current font.
     * @return the current font.
     */
    public Font getFont() {
        if(font==null)
            setFont(new Font("SansSerif",Font.PLAIN,12));
        return font;
    }

    /**
     * Returns the FontMetrics for a font.
     * <p>This doesn't work correctly. Perhaps having some way of mapping
     * the base 14 fonts to our own FontMetrics implementation?
     * @param font The java.awt.Font to return the metrics for
     * @return FontMetrics for a font
     */
    public FontMetrics getFontMetrics(Font font) {
        Canvas c = new Canvas();
        return c.getFontMetrics(font);
    }

    /**
     * @see Graphics2D#getFontRenderContext()
     */
    public FontRenderContext getFontRenderContext() {
        boolean antialias = RenderingHints.VALUE_TEXT_ANTIALIAS_ON.equals(getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING));
        boolean fractions = RenderingHints.VALUE_FRACTIONALMETRICS_ON.equals(getRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS));
        return new FontRenderContext(new AffineTransform(), antialias, fractions);
    }

    /**
     * Returns the associated PdfPage for this graphic
     * @return the associated PdfPage for this graphic
     */
    public PdfPage getPage() {
        return page;
    }

    /**
     * Returns the current pen Colour
     * @return the current pen Colour
     */
    public Paint getPaint() {
        return paint;
    }

    /**
     * @param arg0 a key
     * @return the rendering hint
     */
    public Object getRenderingHint(Key arg0) {
        return rhints.get(arg0);
    }

    /**
     * @see Graphics2D#getRenderingHints()
     */
    public RenderingHints getRenderingHints() {
        return rhints;
    }

    /**
     * @see Graphics2D#getStroke()
     */
    public Stroke getStroke() {
        return originalStroke;
    }

    /**
     * @see Graphics2D#getTransform()
     */
    public AffineTransform getTransform() {
        return new AffineTransform(oTransform);
    }

    /**
     * Returns the PrintWriter handling the underlying stream
     * @return the PrintWriter handling the underlying stream
     */
    public PrintWriter getWriter() {
        return pw;
    }

    /**
     * @see Graphics2D#hit(Rectangle, Shape, boolean)
     */
    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        if (onStroke) {
            s = stroke.createStrokedShape(s);
        }
        s = transform.createTransformedShape(s);
        Area area = new Area(s);
        if (clip != null)
            area.intersect(clip);
        return area.intersects(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * This initialises the stream by saving the current graphics state, and
     * setting up the default line width (for us).
     *
     * It also sets up the instance ready for graphic operations and any
     * optimisations.
     *
     * <p>For child instances, the stream is already open, so this should keep
     * things happy.
     */
    private void init() {
        Rectangle pf = getMediaDimension();

        // save graphics state (restored by dispose)
        if(child) {
            pw.print("q ");
        }

        // now initialise the instance
        //setColor(Color.black);
        paint = Color.black;
        // possible: if parent.color is not black, then force black?
        // must check to see what AWT does?

        // Original User Space Transform (identity)
        oTransform = new AffineTransform();

        // Transform from Java Space to PDF Space
        pTransform = new AffineTransform();
        pTransform.translate(0, pf.getHeight());
        pTransform.scale(1d, -1d);

        // Combined Transform User->Java->PDF
        transform = new AffineTransform(oTransform);
        transform.concatenate(pTransform);

        // Set the line width
        setStroke(DEF_STROKE);
    }



    /**
     * This is called by PdfPage when creating a Graphcis instance.
     * @param page The PdfPage to draw onto.
     */
    public void init(PdfPage page) {
        this.page = page;

        // We are the parent instance
        child = false;

        // Now create a stream to store the graphics in
        stream = this.page.newContent(true);

        pw = stream.getWriter();

        // initially, we are limited to the page size
        clipRectangle = getMediaDimension();

        // finally initialise the stream
        init();
    }

    public void init(PdfXObject _cnt) {
        this.page = null;

        // We are the parent instance
        child = false;

        // Now create a stream to store the graphics in
        stream = _cnt;

        pw = stream.getWriter();

        // initially, we are limited to the page size
        clipRectangle = getMediaDimension();

        // finally initialise the stream
        init();
    }

    /**
     * This method is used internally by create() and by the PDFJob class
     * @param _page PdfPage to draw into
     */
    protected void initChild(PdfPage _page)
    {
        init(_page);
    }

    /**
     * This adds a line segment to the current path
     * @param x coordinate
     * @param y coordinate
     */
    public void lineto(double x,double y) {
        newPath();
        // no optimisation here as it may introduce errors on decimal coordinates.
        pw.print(cxy(x,y)+"l ");
        lx=(float)x;
        ly=(float)y;
        updateMinMaxXY(x,y);
    }



    /**
     * This adds a line segment to the current path
     * @param x coordinate
     * @param y coordinate
     */
    public void lineto(int x,int y) {
        newPath();
        if(lx!=x && ly!=y)
            pw.print(cxy(x,y)+"l ");
        lx=x;
        ly=y;
        updateMinMaxXY(x,y);
    }

    /**
     * This moves the current drawing point.
     * @param x coordinate
     * @param y coordinate
     */
    public void moveto(double x,double y) {
        newPath();
        // no optimisation here as it may introduce errors on decimal coordinates.
        pw.print(cxy(x,y)+"m ");
        lx=(float)x;
        ly=(float)y;
        updateMinMaxXY(x,y);
    }

    /**
     * This moves the current drawing point.
     * @param x coordinate
     * @param y coordinate
     */
    public void moveto(int x,int y) {
        newPath();
        if(lx!=x || ly!=y)
            pw.print(cxy(x,y)+"m ");
        lx=x;
        ly=y;
        updateMinMaxXY(x,y);
    }

    /**
     * Functions that draw lines should start by calling this. It starts a
     * new path unless inStroke is set, in that case it uses the existing path
     */
    void newPath() {
        if(inText) {
            closeBlock();
        }
        if(!inStroke) {
            if(pre_np!=null) {
                pw.print(pre_np);       // this is the prefix set by setOrientation()
                pre_np = null;
            }
            pw.print("n ");
        }

        inText=false;
        inStroke=true;

        // an unlikely coordinate to fool the moveto() optimizer
        lx = ly = -9999;
    }

    /**
     * <p>Functions that draw text should start by calling this. It starts a text
     * block (accounting for media orientation) unless we are already in a Text
     * block.</p>
     *
     * <p>It also handles if the font has been changed since the current text
     * block was started, so your function will be current.</p>
     *
     * @param x x coordinate in java space
     * @param y y coordinate in java space
     */
    void newTextBlock(float x,float y) {
        // close the current path if there is one
        if(inStroke) {
            closeBlock();
        }
        // create the text block if one is not current. If we are, the newFont
        // condition at the end catches font changes
        if(!inText) {
            // This ensures that there is a font available
            getFont();

            pw.print("q BT ");
            tx=ty=0;

            // produce the text matrix for the media
//      switch(mediaRot) {
//      case PageFormat.PORTRAIT: // Portrait
//        //pw.println("1 0 0 1 0 0 Tm");
//        break;
//
//      case PageFormat.LANDSCAPE:        // Landscape
//        pw.println("0 1 -1 0 0 0 Tm");      // rotate
//        break;
//
//      case 180:       // Inverted Portrait
//        pw.println("1 0 0 -1 0 0 Tm");
//        break;
//
//      case PageFormat.REVERSE_LANDSCAPE:       // Seascape
//        pw.println("0 -1 1 0 0 0 Tm");      // rotate
//        break;
//      }

            // move the text cursor by an absolute amount
            pw.print(txy(x,y)+"Td ");

        } else {
            // move the text cursor by a relative amount
            //int ox=x-tx, oy=ty-y;
            //pw.print(""+ox+" "+oy+" Td ");
            //pw.print(cwh(x-tx,y-ty)+"Td ");
            pw.print(twh(x,y,tx,ty)+"Td ");
        }

        // preserve the coordinates for the next time
        tx = x;
        ty = y;

        if(newFont || !inText)
            pw.print(PdfName.of(pdffont.getResource().getResName()).asString()+" "+font.getSize()+" Tf ");

        // later add colour changes here (if required)

        inStroke = newFont = false;
        inText = true;
        updateMinMaxXY(x,y);
    }

    /**
     * This is used to add a polygon to the current path.
     * Used by drawPolygon(), drawPolyline() and fillPolygon() etal
     * @param xp Array of x coordinates
     * @param yp Array of y coordinates
     * @param np number of points in polygon
     * @see #drawPolygon
     * @see #drawPolyline
     * @see #fillPolygon
     */
    public void polygon(int[] xp,int[] yp,int np) {
        // newPath() not needed here as moveto does it ;-)
        moveto(xp[0],yp[0]);
        for(int i=1;i<np;i++)
            lineto(xp[i],yp[i]);
    }

    /**
     * @see Graphics2D#rotate(double)
     */
    public void rotate(double theta) {
        transform.rotate(theta);
    }

    /**
     * @see Graphics2D#rotate(double, double, double)
     */
    public void rotate(double theta, double x, double y) {
        transform.rotate(theta, x, y);
    }

    /**
     * @see Graphics2D#scale(double, double)
     */
    public void scale(double sx, double sy) {
        transform.scale(sx, sy);
        this.stroke = transformStroke(originalStroke);
    }

    /**
     * @see Graphics2D#setBackground(Color)
     */
    public void setBackground(Color color) {
        background = color;
    }

    /**
     * Clips to a set of coordinates
     * @param x coordinate
     * @param y coordinate
     * @param w width
     * @param h height
     */
    public void setClip(int x,int y,int w,int h) {
        clipRectangle = new Rectangle(x,y,w,h);
        closeBlock();            // finish off any existing paths
        drawRect(x,y,w,h);
        closeBlock("W n");               // clip to current path
    }

    /**
     * As my JDK docs say, this may break with Java 2D.
     * <p>Sets the clipping region to that of a Shape.
     * @param s Shape to clip to.
     */
    public void setClip(Shape s) {
        if(s==null) return;
        Rectangle r = s.getBounds();
        setClip(r.x,r.y,r.width,r.height);
    }

    /**
     * Sets the color for drawing
     * @param c Color to use
     */
    public void setColor(Color c) {
        setPaint(c);
    }

    /**
     * @see Graphics2D#setComposite(Composite)
     */
    public void setComposite(Composite comp) {
        this.composite = comp;
    }

    /**
     * This extension sets the line width to the default of 1mm which is what
     * Java uses when drawing to a PrintJob.
     */
    public void setDefaultLineWidth() {
        closeBlock(); // draw any path before we change the line width
        pw.println("1 w");
    }

    public void setPdfFontRegistry(PdfFontRegistry _r)
    {
        this._reg = _r;
    }

    /**
     * This sets the font.
     * @param f java.awt.Font to set to.
     */
    PdfFontRegistry _reg = null;
    public void setFont(Font f)
    {
        if(_reg==null)
        {
            _reg = PdfFontRegistry.of(this.page.getDoc());
        }
        // optimize: Save some space if the font is already the current one.
        if(font!=f) {
            font = f;
            pdffont = _reg.lookupFont(f.getName());
            if(pdffont==null)
            {
                if(this.page!=null)
                {
                    pdffont = _reg.registerFont(this.page.getDoc().registerAwtFont(f, PDF.ENCODING_PDFDOC, null), f.getName());
                }
                else
                if(this.stream!=null)
                {
                    pdffont = _reg.registerFont(this.stream.getDoc().registerAwtFont(f, PDF.ENCODING_PDFDOC, null), f.getName());
                }
                else
                {
                    pdffont = _reg.registerFont(this.stream.getDoc().registerAwtFont(f, PDF.ENCODING_PDFDOC, null), f.getName());
                }
            }
            this.stream.setFont(pdffont.getResource());
            // mark the font as changed
            newFont = true;
        }
    }

    private void setLineCap(int cap) {
        int lineCap = 0;
        switch (cap) {
            case BasicStroke.JOIN_MITER:
                lineCap = 0;
                break;
            case BasicStroke.JOIN_ROUND:
                lineCap = 1;
                break;
            case BasicStroke.JOIN_BEVEL:
                lineCap = 2;
                break;
        }
        if (this.lineCap != lineCap) {
            closeBlock(); // draw any path before we change the line width
            this.lineCap = lineCap;
            pw.println(""+lineCap+" J");
        }
    }

    private void setLineJoin(int join) {
        int lineJoin = 0;
        switch (join) {
            case BasicStroke.JOIN_MITER:
                lineJoin = 0;
                break;
            case BasicStroke.JOIN_ROUND:
                lineJoin = 1;
                break;
            case BasicStroke.JOIN_BEVEL:
                lineJoin = 2;
                break;
        }
        if (this.lineJoin != lineJoin) {
            closeBlock(); // draw any path before we change the line width
            this.lineJoin = lineJoin;
            pw.println(""+lineJoin+" j");
        }
    }

    /**
     * This extension allows the width of the drawn line to be set
     * @param width Line width in pdf graphic units (points)
     */
    public void setLineWidth(float width) {
        if (width != this.lineWidth) {
            closeBlock(); // draw any path before we change the line width
            this.lineWidth = width;
            pw.println(""+width+" w");
        }
    }

    private void setMiterLimit(float limit) {
        if (limit != this.miterLimit) {
            closeBlock(); // draw any path before we change the line width
            this.miterLimit = limit;
            pw.println(""+limit+" M");
        }
    }

    /**
     * Sets the paint for drawing
     * @param paint Paint to use
     */
    public void setPaint(Paint paint) {
        this.paint = paint;

        if (paint instanceof Color) {
            Color c = (Color) paint;
            double r = ((double) c.getRed()) / 255.0;
            double g = ((double) c.getGreen()) / 255.0;
            double b = ((double) c.getBlue()) / 255.0;
            closeBlock(); // This ensures any paths are drawn in the previous
            // colours
            pw.println("" + r + " " + g + " " + b + " rg "
                    + r + " " + g + " " + b + " RG");
        }
        else
        {
            System.err.println(this.getClass().toString()+" hit unimplemented method; setPaint");
        }
    }

    /**
     * Not implemented, as this is not supported in the PDF specification.
     */
    public void setPaintMode() {
        System.err.println(this.getClass().toString()+" hit unimplemented method; setPaintMode");
    }

    /**
     * Sets a rendering hint
     * @param arg0
     * @param arg1
     */
    public void setRenderingHint(Key arg0, Object arg1) {
        if (arg1 != null) {
            rhints.put(arg0, arg1);
        } else {
            rhints.remove(arg0);
        }
    }

    // Add Graphics2D methods.

    /**
     * @see Graphics2D#setRenderingHints(Map)
     */
    public void setRenderingHints(Map<?,?> hints) {
        rhints.clear();
        rhints.putAll(hints);
    }

    /**
     * @see Graphics2D#setStroke(Stroke)
     */
    public void setStroke(Stroke s) {
        originalStroke = s;
        this.stroke = transformStroke(s);

        if (stroke instanceof BasicStroke)
        {
            BasicStroke bs = (BasicStroke) stroke;
            setLineCap(bs.getEndCap());
            setLineJoin(bs.getLineJoin());
            setLineWidth(bs.getLineWidth());
            setMiterLimit(bs.getMiterLimit());
            if(bs.getDashArray()!=null && bs.getDashArray().length>1)
            {
                this.stream.linedashWithOffset(bs.getDashPhase(), bs.getDashArray());
            }
            else
            {
                this.stream.linedash();
            }
        }
    }

    /**
     * @see Graphics2D#setTransform(AffineTransform)
     */
    public void setTransform(AffineTransform t) {
        // Save copy of original transform.
        oTransform = t;
        // Working copy of transform
        transform = new AffineTransform(t);
        // Concatenate Java Space to PDF Space transform
        transform.concatenate(pTransform);
        this.stroke = transformStroke(originalStroke);
    }

    /**
     * Not implemented, as this is not supported in the PDF specification.
     * @param c1 Color to xor with
     */
    public void setXORMode(Color c1) {
        System.err.println(this.getClass().toString()+" hit unimplemented method; setXORMode");
    }

    //============ Text operations =======================

    /**
     * @see Graphics2D#shear(double, double)
     */
    public void shear(double shx, double shy) {
        transform.shear(shx, shy);
    }

    /**
     * @see Graphics2D#transform(AffineTransform)
     */
    public void transform(AffineTransform tx) {
        transform.concatenate(tx);
        this.stroke = transformStroke(originalStroke);
    }

    private Stroke transformStroke(Stroke stroke) {
        if (!(stroke instanceof BasicStroke))
            return stroke;
        BasicStroke st = (BasicStroke)stroke;
        float scale = (float)Math.sqrt(Math.abs(transform.getDeterminant()));
        float dash[] = st.getDashArray();
        if (dash != null) {
            for (int k = 0; k < dash.length; ++k)
                dash[k] *= scale;
        }
        return new BasicStroke(st.getLineWidth() * scale, st.getEndCap(), st.getLineJoin(), st.getMiterLimit(), dash, st.getDashPhase() * scale);
    }


    /**
     * @see Graphics2D#translate(double, double)
     */
    public void translate(double tx, double ty) {
        transform.translate(tx, ty);
        trax = (float) tx;
        tray = (float) ty;
    }

    /**
     * @see Graphics#translate(int, int)
     */
    public void translate(int x, int y) {
        translate((double)x, (double)y);
    }

    /**
     * Converts the Java space coordinates into pdf text space.
     * @param x coordinate
     * @param y coordinate
     * @param tx coordinate
     * @param ty coordinate
     * @return String containing the coordinates in PDF text space
     */
    private String twh(float x,float y,float tx,float ty) {
        float nx=x, ny=y;
        float ntx=tx, nty=ty;
        int mh = (int) getMediaDimension().getHeight();
        int sx=1,sy=1;

//    switch(mediaRot)
//      {
//      case PageFormat.PORTRAIT:
        // Portrait
        //nx = x;
        ny  = mh - y;
        nty = mh - ty;
//	break;
//
//      case PageFormat.LANDSCAPE:
//	// Landscape
//	//nx = y;
//	//ny = x;
//	//ntx = ty;
//	//nty = tx;
//	//sy=-1;
//	nx = x;
//	ny = -y;
//	ntx = tx;
//	nty = -ty;
//	//sy=-1;
//	break;
//
//      case 180:
//	// Inverse Portrait
//	// to be completed
//	nx = mw - x;
//	//ny = y;
//	break;
//
//      case PageFormat.REVERSE_LANDSCAPE:
//	// Seascape
//	// to be completed
//	nx = mw - y;
//	ny = mh - x;
//	break;
//      }

        nx = sx*(nx-ntx);
        ny = sy*(ny-nty);
        return ""+df.format(nx)+" "+df.format(ny)+" ";
    }

    /**
     * Converts the Java space coordinates into pdf text space.
     * @param x coordinate
     * @param y coordinate
     * @return String containing the coordinates in PDF text space
     */
    private String txy(float x,float y) {
//	  float nx=x, ny=y;
//	  int mh = (int) page.getPageFormat().getHeight();

        Point2D ptSrc = new Point2D.Float(x, y);
        Point2D ptDst = new Point2D.Float();
        transform.transform(ptSrc, ptDst);

//    // handle any translations
//    x+=trax;
//    y+=tray;
//
//	nx = x;
//	ny = mh - y;
//    
//	System.out.println("\ntxy(" + ptSrc.getX() + ", " + ptSrc.getY() + ")");
//	System.out.println("Old [" + nx + "," + ny + "]");
//	System.out.println("Trn [" + ptDst.getX() + ", " + ptDst.getY() + "]");
//
//    return ""+df.format(nx)+" "+df.format(ny)+" ";

        return ""+df.format(ptDst.getX())+" "+df.format(ptDst.getY())+" ";
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }


    public void startLayer(String _name)
    {
        this.pw.flush();
        this.stream.startLayer(_name);
    }

    public void endLayer()
    {
        this.pw.flush();
        this.stream.endLayer();;
    }
} // end class PdfGraphics2D
