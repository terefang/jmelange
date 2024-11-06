package com.github.terefang.jmelange.swing.widgets.voops;

import javax.swing.ListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BoxLayout;

public class FontRenderer implements ListCellRenderer
{
    public static final int MODE_NAME_ONLY = 0;
    public static final int MODE_NAME_AND_SAMPLE = 1;
    public static final int MODE_SAMPLE_ONLY = 2;
    public static final int MODE_NAME_AND_SAMPLE_IF_DISPLAYABLE = 3;
    public static final int MODE_SAMPLE_ONLY_IF_DISPLAYABLE = 4;
    
    private Font[] theFonts;
    private int mode;
    
    public FontRenderer( int _mode, Font[] someFonts )
    {
        this.theFonts = someFonts;
        this.mode = _mode;
    }
    
    public FontRenderer( Font[] someFonts )
    {
        this(MODE_NAME_AND_SAMPLE_IF_DISPLAYABLE, someFonts);
    }
    
    public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus)
    {
        Integer selectedIndex = (Integer)value;
        
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
        
        if(this.mode == MODE_NAME_ONLY)
        {
            addNameLabel(panel, selectedIndex);
        }
        else
        if(this.mode == MODE_SAMPLE_ONLY)
        {
            addSampleLabel(panel, selectedIndex, null);
        }
        else
        if(this.mode == MODE_NAME_AND_SAMPLE)
        {
            addNameLabel(panel, selectedIndex);
            addSeparator(panel);
            addSampleLabel(panel, selectedIndex, null);
        }
        else
        if(this.mode == MODE_NAME_AND_SAMPLE_IF_DISPLAYABLE)
        {
            addNameLabel(panel, selectedIndex);
            addSeparator(panel);
            if( theFonts[selectedIndex].canDisplay('a') )
            {
                addSampleLabel(panel, selectedIndex, null);
            }
            else
            {
                addSampleLabel(panel, selectedIndex, "\uF021\uF085\uF0FF");
            }
        }
        else
        if(this.mode == MODE_SAMPLE_ONLY_IF_DISPLAYABLE)
        {
            if( theFonts[selectedIndex].canDisplay('a') )
            {
                addSampleLabel(panel, selectedIndex, null);
            }
            else
            {
                addNameLabel(panel, selectedIndex);
            }
        }
        else
        {
            addNameLabel(panel, selectedIndex);
        }
        return panel;
    }
    
    void addSeparator(JPanel panel)
    {
        JLabel title = new JLabel();
        title.setHorizontalAlignment(JLabel.LEFT);
        title.setVerticalAlignment(JLabel.CENTER);
        title.setText(" | ");
        panel.add(title);
    }

    void addNameLabel(JPanel panel, int selectedIndex)
    {
        JLabel title = new JLabel();
        title.setHorizontalAlignment(JLabel.LEFT);
        title.setVerticalAlignment(JLabel.CENTER);
        title.setText(theFonts[selectedIndex].getFontName());
        panel.add(title);
    }

    void addSampleLabel(JPanel panel, int selectedIndex, String _text)
    {
        
        JLabel title = new JLabel();
        title.setHorizontalAlignment(JLabel.LEFT);
        title.setVerticalAlignment(JLabel.CENTER);
        title.setFont(theFonts[selectedIndex]);
        title.setText(_text!=null ? _text : theFonts[selectedIndex].getFontName());
        panel.add(title);
    }
}