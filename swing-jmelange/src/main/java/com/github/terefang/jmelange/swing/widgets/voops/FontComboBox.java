package com.github.terefang.jmelange.swing.widgets.voops;

import java.awt.Font;
import javax.swing.JComboBox;
import java.awt.GraphicsEnvironment;

public class FontComboBox extends JComboBox
{
    private static Font[]  theFonts;
    
    public FontComboBox(float _size)
    {
        findFonts(_size);
        for( int i = 0 ; i < theFonts.length ; i++ )
            addItem(Integer.valueOf(i));
        this.setRenderer(new FontRenderer(theFonts));
    }
    
    public static Font[] findFonts(float _size)
    {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        
        theFonts = new Font[fontNames.length];
        for( int i = 0 ; i < fontNames.length ; i++ )
        {
            theFonts[i] = new Font(fontNames[i], Font.PLAIN, (int)_size);
        }
        
        return theFonts;
    }
    
    public Font getFont()
    {
        if( theFonts == null )
            return null;
        if( getSelectedItem() == null )
            return null;
        return theFonts[((Integer)getSelectedItem())];
    }
}