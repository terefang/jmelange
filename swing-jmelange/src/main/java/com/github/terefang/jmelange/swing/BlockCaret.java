package com.github.terefang.jmelange.swing;

/*
Java Swing, 2nd Edition
By Marc Loy, Robert Eckstein, Dave Wood, James Elliott, Brian Cole
ISBN: 0-596-00408-7
Publisher: O'Reilly
*/
// FancyCaret.java
//Another (fancier) custom caret class.
//

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

public class BlockCaret
        extends DefaultCaret
{
    
    protected synchronized void damage(Rectangle r)
    {
        if (r == null)
            return;
        
        // give values to x,y,width,height (inherited from java.awt.Rectangle)
        x = r.x;
        y = r.y;
        height = r.height;
        // A value for width was probably set by paint(), which we leave alone.
        // But the first call to damage() precedes the first call to paint(), so
        // in this case we must be prepared to set a valid width, or else
        // paint()
        // will receive a bogus clip area and caret will not get drawn properly.
        if (width <= 0)
            width = getComponent().getWidth();
        
        repaint(); // calls getComponent().repaint(x, y, width, height)
    }
    
    public void paint(Graphics g)
    {
        JTextComponent comp = getComponent();
        if (comp == null)
            return;
        
        int       dot = getDot();
        Rectangle r   = null;
        char      dotChar;
        try
        {
            r = comp.modelToView(dot);
            if (r == null)
                return;
            dotChar = comp.getText(dot, 1)
                    .charAt(0);
        }
        catch (BadLocationException e)
        {
            return;
        }
        
        if ((x != r.x) || (y != r.y))
        {
            // paint() has been called directly, without a previous call to
            // damage(), so do some cleanup. (This happens, for example, when
            // the
            // text component is resized.)
            repaint(); // erase previous location of caret
            x = r.x; // Update dimensions (width gets set later in this method)
            y = r.y;
            height = r.height;
        }
        
        g.setColor(comp.getCaretColor());
        g.setXORMode(comp.getBackground()); // do this to draw in XOR mode
        
        width = g.getFontMetrics()
                .charWidth('X');
        width = Math.max(g.getFontMetrics().charWidth(dotChar), width);
        if (isVisible())
            g.fillRect(r.x, r.y, width, r.height);
    }
}
