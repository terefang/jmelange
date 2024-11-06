package com.github.terefang.jmelange.swing.widgets;

import com.github.terefang.jmelange.commons.color.ColorDef;
import com.github.terefang.jmelange.commons.util.ListMapUtil;

import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;
import javax.swing.Renderer;
import java.util.List;

public class TRenderedComboBox<T> extends JComboBox
{
    private T[]  theColors;
    
    
    public TRenderedComboBox(T[] _c, ListCellRenderer<T> _r)
    {
        if(_c!=null)
        {
            this.theColors = _c;
            for(T _it : _c)
            {
                this.addItem(_it);
            }
        }
        
        if(_r!=null)
        {
            this.setRenderer(_r);
        }
    }
    
    public TRenderedComboBox(List<T> _c, ListCellRenderer<T> _r)
    {
        if(_c!=null)
        {
            this.theColors = ListMapUtil.asArray(_c);
            for(T _it : _c)
            {
                this.addItem(_it);
            }
        }
        
        if(_r!=null)
        {
            this.setRenderer(_r);
        }
    }
    
    
    public T getValue()
    {
        if( this.theColors == null )
            return null;
        if( this.getSelectedItem() == null )
            return null;
        return (T)this.getSelectedItem();
    }
}