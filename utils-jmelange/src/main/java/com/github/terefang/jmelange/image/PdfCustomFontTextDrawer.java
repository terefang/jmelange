package com.github.terefang.jmelange.image;

import de.rototor.pdfbox.graphics2d.PdfBoxGraphics2DFontTextDrawer;
import org.apache.fontbox.ttf.OTFParser;
import org.apache.fontbox.ttf.OpenTypeFont;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;

public class PdfCustomFontTextDrawer extends PdfBoxGraphics2DFontTextDrawer
{
    Map<String,File> pfontMap = new HashMap<>();
    Map<String,PDFont> xfontMap = new HashMap<>();
    PdfStdEncoding ENC = new PdfStdEncoding();
    @Override
    public void registerFont(String fontName, File fontFile) {
        super.registerFont(fontName, fontFile);
        pfontMap.put(fontName, fontFile);
    }

    @Override
    public boolean canDrawText(AttributedCharacterIterator iterator, IFontTextDrawerEnv env) throws IOException, FontFormatException
    {
        Font attributeFont = (Font) iterator.getAttribute(TextAttribute.FONT);

        if (attributeFont == null) attributeFont = env.getFont();

        if(this.pfontMap.containsKey(attributeFont.getFontName()))
        {
            return true;
        }

        return super.canDrawText(iterator, env);
    }

    @Override
    protected PDFont mapFont(Font font, IFontTextDrawerEnv env)
            throws IOException, FontFormatException {
        // Using the font, especially the font.getFontName() or font.getFamily() to determine which
        // font to use... return null if the font can not be mapped. You can also call registerFont() here.

        // Default lookup in the registered fonts
        String fontName = font.getFontName();
        if(this.pfontMap.containsKey(fontName))
        {
            if(!this.xfontMap.containsKey(fontName))
            {
                try
                {
                    PDFont pdFont = PDType0Font.load(env.getDocument(), this.pfontMap.get(fontName));
                    this.xfontMap.put(fontName, pdFont);
                }
                catch(Exception _xe)
                {
                    OTFParser parser = new OTFParser(false, true);
                    OpenTypeFont _font = parser.parse(this.pfontMap.get(fontName));
                    PDFont pdFont = PDTrueTypeFont.load(env.getDocument(), _font, ENC);
                    this.xfontMap.put(fontName, pdFont);
                }
            }
            return this.xfontMap.get(fontName);
        }

        return super.mapFont(font, env);
    }
}
