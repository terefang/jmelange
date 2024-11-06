package com.github.terefang.jmelange.commons.color;

import com.github.terefang.jmelange.commons.util.ColorUtil;

import java.awt.Color;

public class CustomColorDef implements IColorDef
{
    public CustomColorDef(Color _color)
    {
        color = _color;
    }
    
    public CustomColorDef(String _name, Color _color)
    {
        name = _name;
        color = _color;
    }
    
    String name;
    private Color color;
    
    public Color getColor() {
        return color;
    }
    
    @Override
    public String name()
    {
        if(this.name==null)
        {
            return String.format("%02X%02X%02X%02X", this.color.getAlpha(), this.color.getRed(), this.color.getGreen(), this.color.getBlue());
        }
        return this.name;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
}
