package com.github.terefang.jmelange.swing.widgets.ascii;

import com.github.terefang.jmelange.commons.gfx.bgi.BmpFont;
import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.fonts.BDF;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class holds provides all available Fonts for the AsciiPanel.
 * Some graphics are from the Dwarf Fortress Tileset Wiki Page
 *
 * @author zn80
 *
 */
public class AsciiFont {
    
    public static final AsciiFont CP437_8x8 = new AsciiFont("cp437_8x8.png", 8, 8);
    public static final AsciiFont CP437_10x10 = new AsciiFont("cp437_10x10.png", 10, 10);
    public static final AsciiFont CP437_12x12 = new AsciiFont("cp437_12x12.png", 12, 12);
    public static final AsciiFont CP437_16x16 = new AsciiFont("cp437_16x16.png", 16, 16);
    public static final AsciiFont CP437_9x16 = new AsciiFont("cp437_9x16.png", 9, 16);
    public static final AsciiFont DRAKE_10x10 = new AsciiFont("drake_10x10.png", 10, 10);
    public static final AsciiFont TAFFER_10x10 = new AsciiFont("taffer_10x10.png", 10, 10);
    public static final AsciiFont QBICFEET_10x10 = new AsciiFont("qbicfeet_10x10.png", 10, 10);
    public static final AsciiFont TALRYTH_15_15 = new AsciiFont("talryth_square_15x15.png", 15, 15);
    
    public static final AsciiFont of(BmpFont f)
    {
        return new AsciiFont(f);
    }

    public static final AsciiFont of(Font f, int width, int height)
    {
        return new AsciiFont(f, width, height);
    }
    
    public static final AsciiFont of(BDF.BDFont f)
    {
        return new AsciiFont(f);
    }
    
    private Map<Integer,BufferedImage> glyphs = new HashMap<>();
    BufferedImage defaultGlyph;
    
    private BufferedImage glyphSprite;
    private String fontFilename;
    private File fontFile;
    
    private BmpFont bmpFont;
    
    private BDF.BDFont bdFont;
    private Font awtFont;
    
    private int width;
    
    public int getWidth() {
        return width;
    }
    
    private int height;
    
    public int getHeight() {
        return height;
    }
    
    public AsciiFont(String filename, int width, int height) {
        this.fontFilename = filename;
        this.width = width;
        this.height = height;
    }
    
    public AsciiFont(BmpFont bfont) {
        this.bmpFont = bfont;
        this.width = bfont.getFontWidth();
        this.height = bfont.getFontHeight();
    }
    
    public AsciiFont(BDF.BDFont bfont) {
        this.bdFont = bfont;
        this.width = bfont.getBbW();
        this.height = bfont.getSize()+ bfont.descent;
    }
    
    public AsciiFont(Font bfont, int width, int height) {
        this.awtFont = bfont.deriveFont((float)height);
        this.width = width;
        this.height = height;
    }
    
    public BufferedImage getGlyph(char c)
    {
        if(glyphs.containsKey(c))
        {
            return glyphs.get(c);
        }
        return defaultGlyph;
    }
    
    public boolean isGlyph(char _c)
    {
        if(glyphs.containsKey(_c))
        {
            return true;
        }
        return false;
    }

    public void loadGlyphs()
    {
        try {
            if(this.fontFilename!=null)
            {
                glyphSprite = ImageIO.read(ClasspathResourceLoader.of(this.fontFilename, null).getInputStream());
                loadGlyphsImage();
            }
            else
            if(this.fontFile!=null)
            {
                glyphSprite = ImageIO.read(this.fontFile);
                loadGlyphsImage();
            }
            else
            if(this.bmpFont!=null)
            {
                loadGlyphsBmpFnt();
            }
            else
            if(this.bdFont!=null)
            {
                loadGlyphsBdFnt();
            }
            else
            if(this.awtFont!=null)
            {
                loadGlyphsAwtFnt();
            }
        } catch (IOException e) {
            System.err.println("loadGlyphs(): " + e.getMessage());
        }
        
    }
    
    public void loadGlyphsImage()
    {
        for (int i = 0; i < 256; i++) {
            int sx = (i % 16) * this.width;
            int sy = (i / 16) * this.height;
            
            BufferedImage _tmp = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
            _tmp.getGraphics().drawImage(glyphSprite, 0, 0, this.width, this.height, sx, sy, sx + this.width, sy + this.height, null);
            this.glyphs.put(i,_tmp);
        }
    }
    
    public void loadGlyphsBmpFnt()
    {
        int[] charMap = bmpFont.getFontCMap();
        
        if(charMap==null)
        {
            charMap = new int[bmpFont.getNumChar()];
            for (int i = 0; i < bmpFont.getNumChar(); i++)
            {
                charMap[i] = (int) (bmpFont.getFirstChar()+i);
            }
        }
        for (int _c : charMap)
        {
            BufferedImage _tmp = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
            bmpFont.drawChar(_tmp, 0,0,_c, Color.WHITE,null, false);
            this.glyphs.put(_c,_tmp);
        }
    }
    
    public void loadGlyphsBdFnt()
    {
        for (Integer _c : bdFont.getChars())
        {
            BufferedImage _tmp = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
            
            BDF.drawChar(_tmp, bdFont, _c, 0,0, Color.WHITE,null, false);
            this.glyphs.put(_c,_tmp);
        }
    }
    public void loadGlyphsAwtFnt()
    {
        for (int _c=0; _c<0x20000; _c++)
        {
            if(!awtFont.canDisplay(_c)) continue;
            
            BufferedImage _tmp = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D _g = (Graphics2D) _tmp.getGraphics();
            
            _g.setRenderingHint(
                    RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            
            _g.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            _g.setRenderingHint(
                    RenderingHints.KEY_DITHERING,
                    RenderingHints.VALUE_DITHER_DISABLE);
            
            _g.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, (220));
            
            _g.setComposite(AlphaComposite.Src);
            _g.setColor(Color.WHITE);
            _g.setFont(awtFont);
            _g.drawString(Character.valueOf((char) _c).toString(), 0,(this.height*8)/10);
            _g.dispose();
            this.glyphs.put(_c,_tmp);
        }
    }
}