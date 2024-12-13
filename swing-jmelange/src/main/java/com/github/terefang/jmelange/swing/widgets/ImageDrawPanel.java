package com.github.terefang.jmelange.swing.widgets;

import com.github.terefang.jmelange.fonts.BDF;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;

public class ImageDrawPanel extends JPanel
{
    public static enum ImageGridType {
        GRID,ISOMETRIC,MINOR_MAJOR_SQUARE
    }
    Color imageGridColor;
    Dimension2D imageGrid;
    ImageGridType imageGridType;
    
    String imageGridFont = BDF.FONT_4_X_6;
    
    boolean imageGridLabels = true;
    
    public String getImageGridFont()
    {
        return imageGridFont;
    }
    
    public void setImageGridFont(String _imageGridFont)
    {
        imageGridFont = _imageGridFont;
    }
    
    public Color getImageGridColor()
    {
        return imageGridColor;
    }
    
    public void setImageGridColor(Color _imageGridColor)
    {
        imageGridColor = _imageGridColor;
    }
    
    public Dimension2D getImageGrid()
    {
        return imageGrid;
    }
    
    public void setImageGrid(Dimension2D _imageGrid)
    {
        imageGrid = _imageGrid;
        repaint();
    }
    
    public ImageGridType getImageGridType()
    {
        return imageGridType;
    }
    
    public void setImageGridType(ImageGridType _imageGridType)
    {
        imageGridType = _imageGridType;
    }
    
    BufferedImage image;
    Dimension size = new Dimension();
    double scale = 100.;
    
    public double getScale()
    {
        return scale;
    }
    
    public void setScale(double _scale)
    {
        scale = _scale;
        setComponentSize();
        repaint();
    }
    
    public ImageDrawPanel()
    {
        super();
    }
    
    public ImageDrawPanel(BufferedImage _image)
    {
        image = _image;
        scale = 100.;
        setComponentSize();
    }
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        Graphics2D _g2d = (Graphics2D)g;
        super.paintComponent(g);
        if(image != null)
        {
            Image _simage = image.getScaledInstance(size.width, size.height, BufferedImage.SCALE_SMOOTH);
            _g2d.drawImage(_simage, 0, 0, this);
            if(this.imageGrid!=null)
            {
                switch(this.imageGridType)
                {
                    case MINOR_MAJOR_SQUARE:
                        this.drawImageGridMinMajSquare(_g2d,this.scale, _simage.getWidth(null),_simage.getHeight(null));
                        break;
                    case ISOMETRIC:
                        this.drawImageGridIsometric(_g2d,this.scale, _simage.getWidth(null),_simage.getHeight(null));
                        break;
                    case GRID:
                    default:
                        this.drawImageGrid(_g2d,this.scale, _simage.getWidth(null),_simage.getHeight(null));
                        break;
                }
            }
        }
    }
    
    static float[] _BASE_DASH = new float[]{ 2f,2f};
    
    private void drawImageGridMinMajSquare(Graphics2D _g, double _scale, int _width, int _height)
    {
        double _xp = (this.getImageGrid().getWidth()*_scale/100.);
        double _yp = (this.getImageGrid().getHeight());
        double _step = (_xp/_yp);
        
        Color _col = this.imageGridColor==null ? Color.BLACK : this.imageGridColor;
        _g.setPaint(_col.brighter());
        _g.setStroke(new BasicStroke(1.f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0f, _BASE_DASH, 0f));

        for(double _i=0; _i<_width; _i+=_step)
        {
            _g.drawLine((int) _i, 0, (int) _i, _height-1);
        }
        
        for(double _i=0; _i<_height; _i+=_step)
        {
            _g.drawLine(0, (int) _i, _width-1, (int) _i);
        }
        
        _g.setPaint(_col);
        _g.setStroke(new BasicStroke(1.f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0));
        for(double _i=0; _i<_width; _i+=_xp)
        {
            _g.drawLine((int) _i, 0, (int) _i, _height-1);
        }
        
        for(double _i=0; _i<_height; _i+=_xp)
        {
            _g.drawLine(0, (int) _i, _width-1, (int) _i);
        }
    }
    
    private void drawImageGridIsometric(Graphics2D _g, double _scale, int _width, int _height)
    {
        int _xp = (int) (this.getImageGrid().getWidth()*_scale/100.);
        int _yp = (int) (this.getImageGrid().getHeight()*_scale/100.);

        _g.setPaint(this.imageGridColor==null ? Color.BLACK : this.imageGridColor);
        
        Stroke _s = new BasicStroke(1.f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);
        _g.setStroke(_s);
        
        int _xs = (int) ((_width/_xp)+1);
        int _ys = (int) ((_height/_yp)+1);
        
        _xs = (_xs>_ys) ? _xs : _ys;
        _ys = _xs;
        
        _xs *= _xp;
        _ys *= _yp;
        
        for(int _i=0; _i<_width; _i+=_xp)
        {
            _g.drawLine(_i, 0, _i+_xs, _ys);
        }
        for(int _i=0; _i<_height*2; _i+=_yp)
        {
            _g.drawLine(0, _i, _xs, _i-_ys);
            _g.drawLine(0, _i, _xs, _i+_ys);
        }
    }
    
    private void drawImageGrid(Graphics2D _g, double _scale, int _width, int _height)
    {
        _g = (Graphics2D) _g.create();
        try
        {
            int _xp = (int) (this.getImageGrid().getWidth()*_scale/100.);
            int _yp = (int) (this.getImageGrid().getHeight()*_scale/100.);
            
            _g.setPaint(this.imageGridColor==null ? Color.BLACK : this.imageGridColor);
            
            Stroke _s = new BasicStroke(1.f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);
            _g.setStroke(_s);
            
            for(int _i=0; _i<_width; _i+=_xp)
            {
                _g.drawLine(_i, 0, _i, _height-1);
            }
            
            for(int _i=0; _i<_height; _i+=_yp)
            {
                _g.drawLine(0, _i, _width-1, _i);
            }
            
            if(this.imageGridLabels)
            {
                BDF.BDFont _fnt = BDF.load(this.imageGridFont);
                for(int _j=0; _j<_width; _j+=_xp)
                {
                    for(int _i=0; _i<_height; _i+=_yp)
                    {
                        BDF.drawString(_g,_fnt,_j+2, _i+2, String.format("%c.%d", (char)('A'+((_j/_xp)%26)), (_i/_yp)), this.imageGridColor);
                    }
                }
            }
        }
        finally
        {
            _g.dispose();
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return size;
    }
    
    public void setImage(BufferedImage bi) {
        image = bi;
        setComponentSize();
        repaint();
    }

    public void setImage(BufferedImage bi, double _scale) {
        scale = _scale;
        setImage(bi);
    }
    
    private void setComponentSize()
    {
        if(image != null) {
            size.width  = (int) (image.getWidth()*this.scale/100.);
            size.height = (int) (image.getHeight()*this.scale/100.);
            revalidate();  // signal parent/scrollpane
        }
    }
}
