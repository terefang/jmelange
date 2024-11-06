package com.github.terefang.jmelange.swing.widgets;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;

public class ColorPanel extends JPanel
{
    public ColorPanel(Color color, Dimension dims)
    {
        this.setBackground(color);
        this.setPreferredSize(dims);
    }
}