package com.github.terefang.jmelange.swing.widgets;

import com.github.terefang.jmelange.commons.util.PaperUtil;

import javax.swing.*;
import java.awt.Component;

public class PaperDefRenderer
        implements ListCellRenderer<PaperUtil.IPaperSize>
{
    @Override
    public Component getListCellRendererComponent(
            JList<? extends PaperUtil.IPaperSize> list,
            PaperUtil.IPaperSize value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
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
        
        JLabel title = new JLabel();
        title.setHorizontalAlignment(JLabel.LEFT);
        title.setVerticalAlignment(JLabel.CENTER);
        if(value.getDescription()==null)
        {
            title.setText(String.format("%s (%d x %d pts)",value.getName(),value.getPointWidth(),value.getPointHeight()));
        }
        else
        {
            title.setText(String.format("%s %s",value.getName(), value.getDescription()));
        }
        panel.add(title);
        
        return panel;
    }
}
