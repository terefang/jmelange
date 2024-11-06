package com.github.terefang.jmelange.swing;

import org.jdesktop.swingx.JXPanel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;

abstract class AbstractTitledPanel extends JXPanel
{
    private String title;
    private final JLabel label;
    private final JPanel panel = makePanel();
    
    public abstract JPanel makePanel();
    
    protected AbstractTitledPanel(String title) {
        super(new BorderLayout());
        this.title = title;
        label = new JLabel(title);
        label.setForeground(Color.WHITE);
        label.setBackground(Color.BLACK);
        label.setOpaque(true);
        add(label, BorderLayout.NORTH);
        
        panel.setVisible(true);
        panel.setOpaque(true);
        add(panel);
        initPanel();
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String _t)
    {
        this.title = _t;
        if(this.label!=null) this.label.setText(this.title);
    }
    
    @Override public final Component add(Component comp) {
        return super.add(comp);
    }
    
    @Override public final void add(Component comp, Object constraints) {
        super.add(comp, constraints);
    }
    
    @Override public Dimension getPreferredSize() {
        Dimension d = label.getPreferredSize();
        if (panel.isVisible()) {
            d.height += panel.getPreferredSize().height;
        }
        return d;
    }
    
    @Override public Dimension getMaximumSize() {
        Dimension d = getPreferredSize();
        d.width = Short.MAX_VALUE;
        return d;
    }
    
    protected void initPanel()
    {
        revalidate();
        EventQueue.invokeLater(() -> panel.scrollRectToVisible(panel.getBounds()));
    }
}