package com.github.terefang.jmelange.swing.widgets;

import com.github.terefang.jmelange.commons.color.ColorDef;
import com.github.terefang.jmelange.commons.color.IColorDef;

import javax.swing.*;
import java.awt.Component;

public class ColorDefRenderer
        implements ListCellRenderer<IColorDef>
{
    
    public ColorDefRenderer()
    {
        super();
    }
    
    
    public Component getListCellRendererComponent(JList list,IColorDef value,int index,boolean isSelected,boolean cellHasFocus)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
        panel.setOpaque(true);
        
        if(isSelected)
        {
            panel.setBackground(list.getSelectionBackground());
            panel.setForeground(list.getSelectionForeground());
        }
        else
        {
            panel.setBackground(list.getBackground());
            panel.setForeground(list.getForeground());
        }
        
        addSampleLabel(panel, value);
        addNameLabel(panel, value);
        return panel;
    }
    
    void addNameLabel(JPanel panel, IColorDef selected)
    {
        JLabel title = new JLabel();
        title.setHorizontalAlignment(JLabel.LEFT);
        title.setVerticalAlignment(JLabel.CENTER);
        title.setText(String.format("  %06X - %s",selected.getColor().getRGB() &0xffffff, selected.name()));
        panel.add(title);
    }

    void addSampleLabel(JPanel panel, IColorDef selected)
    {
        JLabel title = new JLabel();
        title.setHorizontalAlignment(JLabel.LEFT);
        title.setVerticalAlignment(JLabel.CENTER);
        title.setText("     ");
        title.setOpaque(true);
        title.setBackground(selected.getColor());
         panel.add(title);
    }
}