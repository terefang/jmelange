package com.github.terefang.jmelange.swing.layout.river;

import java.util.Vector;

public class Ruler
{
    private Vector tabs = new Vector();
    
    public void setTab(int num, int xpos) {
        if (num >= tabs.size()) tabs.add(num, new Integer(xpos));
        else {
            // Transpose all tabs from this tab stop and onwards
            int delta = xpos - getTab(num);
            if (delta > 0) {
                for (int i = num; i < tabs.size(); i++) {
                    tabs.set(i, new Integer(getTab(i) + delta));
                }
            }
        }
    }
    
    public int getTab(int num) {
        return ((Integer) tabs.get(num)).intValue();
    }
    
    public String toString() {
        StringBuffer ret = new StringBuffer(getClass().getName() + " {");
        for (int i = 0; i < tabs.size(); i++) {
            ret.append(tabs.get(i));
            if (i < tabs.size() - 1) ret.append(',');
        }
        ret.append('}');
        return ret.toString();
    }
}