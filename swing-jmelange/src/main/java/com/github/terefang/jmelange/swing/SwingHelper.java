package com.github.terefang.jmelange.swing;

import com.alexandriasoftware.swing.JSplitButton;
import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.color.ColorDef;
import com.github.terefang.jmelange.commons.color.CustomColorDef;
import com.github.terefang.jmelange.commons.color.IColorDef;
import com.github.terefang.jmelange.commons.lang.Executable;
import com.github.terefang.jmelange.commons.lang.Executable2;
import com.github.terefang.jmelange.commons.loader.ResourceLoader;
import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.commons.util.*;
import com.github.terefang.jmelange.swing.easylayout.Constraint;
import com.github.terefang.jmelange.swing.easylayout.EasyLayout;
import com.github.terefang.jmelange.swing.handler.SetFontNameAndSizeHandler;
import com.github.terefang.jmelange.swing.handler.SetFontNameOnlyHandler;
import com.github.terefang.jmelange.swing.handler.SetFontObjectHandler;
import com.github.terefang.jmelange.swing.list.LabeledFileListItem;
import com.github.terefang.jmelange.swing.list.LabeledFileListModel;

import com.github.terefang.jmelange.swing.widgets.ColorDefRenderer;
import com.github.terefang.jmelange.swing.widgets.PaperDefRenderer;
import com.github.terefang.jmelange.swing.widgets.TRenderedComboBox;
import com.github.terefang.jmelange.swing.widgets.voops.MiniColorChooser;
import com.github.terefang.jmelange.swing.widgets.voops.MiniColorChooserDialog;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideSplitButton;
import de.milchreis.uibooster.UiBooster;
import de.milchreis.uibooster.components.ProgressDialog;
import de.milchreis.uibooster.model.UiBoosterOptions;
import io.github.dheid.fontchooser.FontDialog;
import lombok.SneakyThrows;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXRadioGroup;
import org.jdesktop.swingx.icon.EmptyIcon;
import org.jdesktop.swingx.painter.BusyPainter;
import org.jfree.ui.FontChooserDialog;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.*;
import java.util.List;

public class SwingHelper
{
    
    @SneakyThrows
    public static synchronized Font createAwtFont(String _family, boolean _bold, boolean _italic, float _size)
    {
        return new Font(_family, (_italic ? Font.ITALIC:Font.PLAIN)|(_bold ? Font.BOLD:Font.PLAIN),(int)_size);
    }
    
    static Map<String,List<String>> _fontlist = new LinkedHashMap<>();
    static List<String> _pfonts;
    static List<String> _mfonts;
    
    static {
        
        _fontlist.put("Agave", CommonUtil.toList("fonts/Nerd-Lite/AgaveNerdFontMono-Regular.ttf"));
        _fontlist.put("AurulentSansMono", CommonUtil.toList("fonts/Nerd-Lite/AurulentSansMNerdFontMono-Regular.otf"));
        _fontlist.put("CodeNewRoman", CommonUtil.toList("fonts/Nerd-Lite/CodeNewRomanNerdFontMono-Regular.otf"));
        _fontlist.put("EnvyCodeR", CommonUtil.toList("fonts/Nerd-Lite/EnvyCodeRNerdFontMono-Regular.ttf"));
        _fontlist.put("JetBrainsMono", CommonUtil.toList("fonts/Nerd-Lite/JetBrainsMonoNerdFontMono-Regular.ttf"));
        _fontlist.put("JetBrainsMonoNL", CommonUtil.toList("fonts/Nerd-Lite/JetBrainsMonoNLNerdFontMono-Regular.ttf"));
        _fontlist.put("Terminus", CommonUtil.toList("fonts/Nerd-Lite/TerminessNerdFontMono-Regular.ttf"));
        _fontlist.put("UniFont", CommonUtil.toList("fonts/unifont.otf"));
        
        _pfonts = new Vector<>();
        _mfonts = new Vector<>();
        FontRenderContext frc = new FontRenderContext(null, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
        for(Font _fnt : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts())
        {
            if(!_pfonts.contains(_fnt.getFamily()))
            {
                _pfonts.add(_fnt.getFamily());
            }
            
            Rectangle2D iBounds = _fnt.getStringBounds("i", frc);
            Rectangle2D mBounds = _fnt.getStringBounds("m", frc);
            if (iBounds.getWidth() == mBounds.getWidth())
            {
                if(!_mfonts.contains(_fnt.getFamily()))
                {
                    _mfonts.add(_fnt.getFamily());
                }
            }
        }
        ListMapUtil.sort(_pfonts,true);
        ListMapUtil.sort(_mfonts,true);
    }
    
    public static int[] _FSIZES = { 8,10,12,14,16,18,20,24,28,36,42,56,72};
    
    
    static public synchronized List<String> listFonts() {
        return new Vector(_fontlist.keySet());
    }
    static public synchronized List<String> getFontsFont(String _k) {
        return _fontlist.get(_k);
    }
    
    static public synchronized List<Font> getAvailableFonts()
    {
        List<Font> _ret = new Vector<>();
        
        for(Font _fnt : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts())
        {
            _ret.add(_fnt);
        }
        
        return _ret;
    }
    
    static public synchronized List<String> listPlatformFonts(boolean _mono)
    {
        if(_mono) return Collections.unmodifiableList(_mfonts);
        
        return Collections.unmodifiableList(_pfonts);
    }
    
    static Map<String,Font> _font = new HashMap<>();
    
    @SneakyThrows
    static public synchronized Font createEditFont() {
        return createEditFont(12f);
    }
    
    static Font _editfont = null;
    
    @SneakyThrows
    static public synchronized Font createEditFont(float _size) {
        if(_editfont==null) {
            _editfont = Font.createFont(Font.TRUETYPE_FONT,
                    ClasspathResourceLoader.of("fonts/Nerd-Lite/AgaveNerdFontMono-Regular.ttf", null).getInputStream())
            ;
        }
        return _editfont.deriveFont(_size);
    }

    public static String _FONT_NUNITO = "fonts/NunitoSans-Regular.ttf";
    
    @SneakyThrows
    static public synchronized Font createStdFont(float _size)
    {
        return createFont(_FONT_NUNITO,_size);
    }
    
    @SneakyThrows
    static public synchronized Font createFont(String _file, float _size)
    {
        return createFont(ClasspathResourceLoader.of(_file, null),_size);
    }
    
    @SneakyThrows
    static public synchronized Font createFont(ResourceLoader _rl, float _size)
    {
        if(!_font.containsKey(_rl.getName())) {
            Font _ffont = Font.createFont(Font.TRUETYPE_FONT,
                    _rl.getInputStream());
            _font.put(_rl.getName(), _ffont);
        }
        return _font.get(_rl.getName()).deriveFont(_size);
    }
    
    
    static public synchronized Font createFont(String[] _families, boolean _bold, boolean _italic, float _size) {
        int _last = _families.length-1;
        for (int _i = 0; _i<=_last; _i++)
        {
            Font _ffont = createFont(_families[_i], _bold,_italic,_size, _i==_last);
            if(_ffont!=null)
                return _ffont;
        }
        return null;
    }
    @SneakyThrows
    static public synchronized Font createFont(String _family, boolean _bold, boolean _italic, float _size, boolean _useDefault) {
        String _key = String.format("%s-%s-%s-%d", _family, _bold, _italic, (int) _size);
        
        if (_font.containsKey(_key)) {
            return _font.get(_key);
        }
        
        if ("monospace".equalsIgnoreCase(_family)) {
            return createEditFont().deriveFont(_size);
        }
        
        int _index = 0;
        if(_bold) _index+=2;
        if(_italic) _index+=1;
        
        if(listFonts().contains(_family))
        {
            List<String> _fam = getFontsFont(_family);
            if(_fam.size()<=_index) _index=0;
            
            Font _ffont = createFont(_fam.get(_index), _size);
            _font.put(_key, _ffont);
            return _font.get(_key);
        }
        
        if(listPlatformFonts(false).contains(_family))
        {
            Font _ffont = createAwtFont(_family, _bold, _italic, _size);
            _font.put(_key, _ffont);
            return _font.get(_key);
        }
        
        if(_useDefault)
        {
            _font.put(_key, createAwtFont(Font.DIALOG, _bold,_italic,_size));
        }
        return _font.get(_key);
    }
    
    public static void setMinSizeFactor(JComponent comp, double xFactor, double yFactor) {
        Dimension size = comp.getPreferredSize();
        size.width *= xFactor;
        size.height *= yFactor;
        comp.setMinimumSize(size);
    }

    public static void setMaxSizeFactor(JComponent comp, double xFactor, double yFactor) {
        Dimension size = comp.getPreferredSize();
        size.width *= xFactor;
        size.height *= yFactor;
        comp.setMaximumSize(size);
    }
    
    public static void setMaxSizeFactor(JComponent comp, double factor)
    {
        setMaxSizeFactor(comp, factor, factor);
    }
    
    public static void setMinMaxSizeFactor(JComponent comp, double nfactor, double mfactor)
    {
        setMinSizeFactor(comp, nfactor, nfactor);
        setMaxSizeFactor(comp, mfactor, mfactor);
    }
    
    public static JPopupMenu createPopupMenu()
    {
        return new JPopupMenu();
    }
    
    public static JPopupMenu createPopupMenu(Map<String,Runnable> _items)
    {
        JPopupMenu _pm = createPopupMenu();
        return createPopupMenu(_pm, _items);
    }
    
    public static JPopupMenu createPopupMenu(JPopupMenu _pm, Map<String,Runnable> _items)
    {
        for(Map.Entry<String, Runnable> _entry : _items.entrySet())
        {
            if(_entry.getKey().startsWith("---"))
            {
                _pm.addSeparator();
            }
            else
            {
                _pm.add(new JMenuItem(new AbstractAction(_entry.getKey()) {
                    @Override
                    public void actionPerformed(ActionEvent e) { _entry.getValue().run(); }
                }));
            }
        }
        return _pm;
    }
    
    public static JPopupMenu createPopupMenu(JPopupMenu _pm, String _name, Runnable _action)
    {
        if(_name.startsWith("---"))
        {
            _pm.addSeparator();
        }
        else
        {
            _pm.add(createMenuItem(_name,_action));
        }
        return _pm;
    }
    
    public static JPopupMenu createPopupMenu(JPopupMenu _pm, String _n1, Runnable _a1, String _n2, Runnable _a2)
    {
        _pm = createPopupMenu(_pm,_n1,_a1);
        _pm = createPopupMenu(_pm,_n2,_a2);
        return _pm;
    }
    
    public static JPopupMenu createPopupMenu(JPopupMenu _pm, String _n1, Runnable _a1, String _n2, Runnable _a2, String _n3, Runnable _a3)
    {
        _pm = createPopupMenu(_pm,_n1,_a1);
        _pm = createPopupMenu(_pm,_n2,_a2);
        _pm = createPopupMenu(_pm,_n3,_a3);
        return _pm;
    }
    
    public static JPopupMenu createPopupMenu(JPopupMenu _pm, String _n1, Runnable _a1, String _n2, Runnable _a2, String _n3, Runnable _a3, String _n4, Runnable _a4)
    {
        _pm = createPopupMenu(_pm,_n1,_a1);
        _pm = createPopupMenu(_pm,_n2,_a2);
        _pm = createPopupMenu(_pm,_n3,_a3);
        _pm = createPopupMenu(_pm,_n4,_a4);
        return _pm;
    }
    
    public static JPopupMenu createPopupMenu(JPopupMenu _pm, String _n1, Runnable _a1, String _n2, Runnable _a2, String _n3, Runnable _a3, String _n4, Runnable _a4, String _n5, Runnable _a5)
    {
        _pm = createPopupMenu(_pm,_n1,_a1);
        _pm = createPopupMenu(_pm,_n2,_a2);
        _pm = createPopupMenu(_pm,_n3,_a3);
        _pm = createPopupMenu(_pm,_n4,_a4);
        _pm = createPopupMenu(_pm,_n5,_a5);
        return _pm;
    }
    
    public static JPopupMenu createPopupMenu(JPopupMenu _pm, String _n1, Runnable _a1, String _n2, Runnable _a2, String _n3, Runnable _a3, String _n4, Runnable _a4, String _n5, Runnable _a5, String _n6, Runnable _a6)
    {
        _pm = createPopupMenu(_pm,_n1,_a1);
        _pm = createPopupMenu(_pm,_n2,_a2);
        _pm = createPopupMenu(_pm,_n3,_a3);
        _pm = createPopupMenu(_pm,_n4,_a4);
        _pm = createPopupMenu(_pm,_n5,_a5);
        _pm = createPopupMenu(_pm,_n6,_a6);
        return _pm;
    }
    
    public static JPopupMenu createPopupMenu(JPopupMenu _pm, String _n1, Runnable _a1, String _n2, Runnable _a2, String _n3, Runnable _a3, String _n4, Runnable _a4, String _n5, Runnable _a5, String _n6, Runnable _a6, String _n7, Runnable _a7)
    {
        _pm = createPopupMenu(_pm,_n1,_a1);
        _pm = createPopupMenu(_pm,_n2,_a2);
        _pm = createPopupMenu(_pm,_n3,_a3);
        _pm = createPopupMenu(_pm,_n4,_a4);
        _pm = createPopupMenu(_pm,_n5,_a5);
        _pm = createPopupMenu(_pm,_n6,_a6);
        _pm = createPopupMenu(_pm,_n7,_a7);
        return _pm;
    }
    
    public static JPopupMenu createPopupMenu(JPopupMenu _pm, String _n1, Runnable _a1, String _n2, Runnable _a2, String _n3, Runnable _a3, String _n4, Runnable _a4, String _n5, Runnable _a5, String _n6, Runnable _a6, String _n7, Runnable _a7, String _n8, Runnable _a8)
    {
        _pm = createPopupMenu(_pm,_n1,_a1);
        _pm = createPopupMenu(_pm,_n2,_a2);
        _pm = createPopupMenu(_pm,_n3,_a3);
        _pm = createPopupMenu(_pm,_n4,_a4);
        _pm = createPopupMenu(_pm,_n5,_a5);
        _pm = createPopupMenu(_pm,_n6,_a6);
        _pm = createPopupMenu(_pm,_n7,_a7);
        _pm = createPopupMenu(_pm,_n8,_a8);
        return _pm;
    }

    public static JPopupMenu createPopupMenu(JPopupMenu _pm, String _n1, Runnable _a1, String _n2, Runnable _a2, String _n3, Runnable _a3, String _n4, Runnable _a4, String _n5, Runnable _a5, String _n6, Runnable _a6, String _n7, Runnable _a7, String _n8, Runnable _a8, String _n9, Runnable _a9)
    {
        _pm = createPopupMenu(_pm,_n1,_a1);
        _pm = createPopupMenu(_pm,_n2,_a2);
        _pm = createPopupMenu(_pm,_n3,_a3);
        _pm = createPopupMenu(_pm,_n4,_a4);
        _pm = createPopupMenu(_pm,_n5,_a5);
        _pm = createPopupMenu(_pm,_n6,_a6);
        _pm = createPopupMenu(_pm,_n7,_a7);
        _pm = createPopupMenu(_pm,_n8,_a8);
        _pm = createPopupMenu(_pm,_n9,_a9);
        return _pm;
    }
    
    public static JMenuItem createMenuItem(String _title, final Runnable _callback)
    {
        return new JMenuItem(new AbstractAction(_title)
        {
            @Override
            public void actionPerformed(ActionEvent e) { _callback.run(); }
        });
    }
    
    public static JMenuItem createMenuItem(JPopupMenu _pmenu, String _title, final Runnable _callback)
    {
        JMenuItem _mi = createMenuItem(_title, _callback);
        _pmenu.add(_mi);
        return _mi;
    }
    
    public static JMenuItem createMenuItem(JMenu _pmenu, String _title, final Runnable _callback)
    {
        JMenuItem _mi = createMenuItem(_title, _callback);
        _pmenu.add(_mi);
        return _mi;
    }
    
    public static JMenu createMenu(String _title)
    {
        return new JMenu(_title);
    }
    
    public static JMenu createMenu(JPopupMenu _pmenu, String _title)
    {
        JMenu _menu = new JMenu(_title);
        _pmenu.add(_menu);
        return _menu;
    }
    
    public static JMenu createMenu(JMenu _pmenu, String _title)
    {
        JMenu _menu = new JMenu(_title);
        _pmenu.add(_menu);
        return _menu;
    }
    
    
    public static JPanel createHBox()
    {
        return createHBox(null);
    }
    
    public static JPanel createHBox(Component... _components)
    {
        return createHBox(null, _components);
    }
    
    public static JPanel createHBox(String _title, Component... _components)
    {
        JPanel _panel = new JPanel();
        _panel.setLayout(new BoxLayout(_panel, BoxLayout.X_AXIS));
        
        if(_components!=null && _components.length>0)
        {
            for(Component _c : _components)
            {
                _panel.add(_c);
            }
        }
        
        return setTitle(_panel,_title);
    }
    
    
    
    public static JPanel createHxBox(String _title)
    {
        JPanel _panel = new JPanel();
        _panel.setLayout(new BoxLayout(_panel, BoxLayout.LINE_AXIS));
        return setTitle(_panel,_title);
    }
    
    public static JPanel createHxBox(String _title, Component... _components)
    {
        JPanel _panel = createHxBox(_title);
        if(_components!=null && _components.length>0)
        {
            for(Component _c : _components)
            {
                _panel.add(_c);
            }
        }
        return _panel;
    }
    
    public static JPanel createHxBox()
    {
        return createHxBox((String)null);
    }
    
    public static JPanel createBorderPanel(String _title)
    {
        JPanel _panel = new JPanel();
        if(_title!=null)
        {
            Border _etch = BorderFactory.createEtchedBorder((EtchedBorder.LOWERED));
            TitledBorder _t = BorderFactory.createTitledBorder(_etch, _title);
            _panel.setBorder(_t);
        }
        _panel.setLayout(new BorderLayout());
        
        return _panel;
    }
    
    public static JPanel createRxGrid()
    {
        return createRxGrid(1);
    }

    public static JPanel createRxGrid(int _c)
    {
        return createRxGrid(_c, (String)null);
    }
    
    public static JPanel createRxGrid(String _title, int _c)
    {
        return createRxGrid(_c, _title);
    }
    
    public static JPanel createRxGrid(int _c,String _title, Component... _comps)
    {
        JPanel _panel = new JPanel();
        if(_title!=null)
        {
            Border _etch = BorderFactory.createEtchedBorder((EtchedBorder.LOWERED));
            TitledBorder _t = BorderFactory.createTitledBorder(_etch, _title);
            _panel.setBorder(_t);
        }
        
        if(_c<0 && _comps!=null && _comps.length>0)
        {
            // 3 x 4 = 12
            _c = Math.max(1,(3*_comps.length)/12);
        }
        
        _panel.setLayout(new GridLayout(0,_c));
        
        if(_comps!=null && _comps.length>0)
        {
            for(Component _comp : _comps)
            {
                _panel.add(_comp);
            }
        }
        
        return _panel;
    }
    
    public static JPanel createGrid(int _c,int _r)
    {
        return createGrid(_c,_r,null);
    }
    
    public static JPanel createGrid(int _c,int _r,String _title, Component... _comps)
    {
        JPanel _panel = new JPanel();
        if(_title!=null)
        {
            Border _etch = BorderFactory.createEtchedBorder((EtchedBorder.LOWERED));
            TitledBorder _t = BorderFactory.createTitledBorder(_etch, _title);
            _panel.setBorder(_t);
        }
        
        if(_c<0 && _comps!=null && _comps.length>0)
        {
            // 3 x 4 = 12
            _c = Math.max(1,(3*_comps.length)/12);
        }
        
        _panel.setLayout(new GridLayout(_r,_c));
        
        if(_comps!=null && _comps.length>0)
        {
            for(Component _comp : _comps)
            {
                _panel.add(_comp);
            }
        }
        
        return _panel;
    }
    
    public static JPanel createVxBox()
    {
        return createVxBox((String)null);
    }
    
    public static JPanel createVxBox(String _title)
    {
        JPanel _panel = new JPanel();
        _panel.setLayout(new BoxLayout(_panel, BoxLayout.PAGE_AXIS));
        
        return setTitle(_panel,_title);
    }
    
    public static JPanel createVxBox(String _title, Component... _components)
    {
        JPanel _panel = createVxBox(_title);
        if(_components!=null && _components.length>0)
        {
            for(Component _c : _components)
            {
                _panel.add(_c);
            }
        }
        return _panel;
    }
    
    public static JPanel createVBox()
    {
        return createVBox(null);
    }
    
    public static JPanel createVBox(Component... _components)
    {
        return createVBox(null, _components);
    }
    
    public static JPanel createVBox(String _title, Component... _components)
    {
        JPanel _panel = new JPanel();
        _panel.setLayout(new BoxLayout(_panel, BoxLayout.Y_AXIS));
        
        if(_components!=null && _components.length>0)
        {
            for(Component _c : _components)
            {
                _panel.add(_c);
            }
        }
        
        return setTitle(_panel,_title);
    }
    
    public static JPanel setTitle(JPanel _panel, String _text)
    {
        if(_text!=null)
        {
            Border       _etch  = BorderFactory.createEtchedBorder((EtchedBorder.LOWERED));
            TitledBorder _title = BorderFactory.createTitledBorder(_etch, _text);
            _panel.setBorder(_title);
        }
        return _panel;
    }
    
    public static JPanel createEGrid(int _c,int _r)
    {
        JPanel _panel = new JPanel();
        _panel.setLayout(EasyLayout.from(_c,_r));
        return _panel;
    }
    
    public static JPanel createEGrid(int[] _c,int[] _r)
    {
        JPanel _panel = new JPanel();
        _panel.setLayout(EasyLayout.from(_c,_r));
        return _panel;
    }
    
    public static JPanel createEGrid(int _c,int _r, String _text)
    {
        JPanel       _panel = createEGrid(_c,_r);
        return setTitle(_panel, _text);
    }
    
    public static JPanel createEGrid(int[] _c,int[] _r, String _text)
    {
        JPanel       _panel = createEGrid(_c,_r);
        return setTitle(_panel, _text);
    }
    
    public static JPanel createHGrid(int _n)
    {
        JPanel _panel = new JPanel();
        _panel.setLayout(new GridLayout(1, _n));
        return _panel;
    }
    
    public static JPanel createHGrid(int _n, String _text)
    {
        JPanel       _panel = createHGrid(_n);
        return setTitle(_panel, _text);
    }
    
    public static JPanel createHGrid(String _text, Component... _components)
    {
        JPanel _panel = createHGrid(_components.length, _text);
        int _i = 0;
        for(Component _c : _components)
        {
            _panel.add(_c, Constraint.from(_i++,0));
        }
        return _panel;
    }
    
    public static JPanel createHGrid(Component... _components)
    {
        return createHGrid(null, _components);
    }
    
    
    public static JPanel createVGrid(int _n)
    {
        JPanel _panel = new JPanel();
        _panel.setLayout(new GridLayout(_n,1));
        return _panel;
    }
    
    public static JPanel createVGrid(int _n, String _text)
    {
        JPanel       _panel = createVGrid(_n);
        return setTitle(_panel, _text);
    }
    
    public static JPanel createVGrid(String _text, Component... _components)
    {
        JPanel _panel = createVGrid(_components.length, _text);
        int _i = 0;
        for(Component _c : _components)
        {
            _panel.add(_c, Constraint.from(0,_i++));
        }
        return _panel;
    }
    
    public static JPanel createVGrid(Component... _components)
    {
        return  createVGrid(null, _components);
    }
    
    public static JLabel createLabel(int _align, String _title)
    {
        return new JLabel(_title, _align);
    }
    
    public static JLabel createLabel(String _title)
    {
        return createLabel(SwingConstants.LEFT,_title);
    }
    
    public static JLabel createLabel(String _title, int _width)
    {
        return createLabel(SwingConstants.LEFT, _title,_width);
    }
    
    public static JLabel createLabel(int _align, String _title, int _width)
    {
        return createLabel(_align, _title,_width, -1);
    }
    
    public static JLabel createLabel(int _align, String _title, int _width, int _height)
    {
        JLabel _lbl = createLabel(_align, _title);
        if(_width>0 && _height<0)
        {
            Dimension _sz = _lbl.getPreferredSize();
            _sz.width = _width;
            _lbl.setPreferredSize(_sz);
            _lbl.setMaximumSize(_sz);
        }
        else if(_width<0 && _height>0)
        {
            Dimension _sz = _lbl.getPreferredSize();
            _sz.height = _height;
            _lbl.setPreferredSize(_sz);
            _lbl.setMaximumSize(_sz);
        }
        else if(_width>0 && _height>0)
        {
            Dimension _sz = _lbl.getPreferredSize();
            _sz.width = _width;
            _sz.height = _height;
            _lbl.setPreferredSize(_sz);
            _lbl.setMaximumSize(_sz);
        }
        return _lbl;
    }
    
    public static JLabel createLabel(String _title, Color _front, Color _back)
    {
        return createLabel(SwingConstants.LEFT, _title, -1, -1, _front, _back);
    }

    public static JLabel createLabel(int _align, String _title, Color _front, Color _back)
    {
        return createLabel(_align, _title, -1, -1, _front, _back);
    }
    
    public static JLabel createLabel(String _title, int _width, int _height, Color _front, Color _back)
    {
        return createLabel(SwingConstants.LEFT, _title, _width, _height, _front, _back);
    }
    public static JLabel createLabel(int _align, String _title, int _width, int _height, Color _front, Color _back)
    {
        JLabel _l = createLabel(_align,_title,_width,_height);
        _l.setForeground(_front);
        if(_back==null)
        {
            _l.setOpaque(false);
        }
        else
        {
            _l.setOpaque(true);
            _l.setBackground(_back);
        }
        return _l;
    }
    
    public static JPanel createFontFileChooser(File _dir, Executable<File> _ok, Runnable _nok)
    {
        return createFontFileChooser(_dir, -1, _ok,_nok);
    }
    
    public static JPanel createFontFileChooser(File _dir, int _w, Executable<File> _ok, Runnable _nok)
    {
        JPanel _p = createEGrid(new int[]{500,50}, new int[]{100});
        JTextField _tf = createTextField("font", _w-50);
        _p.add(_tf, Constraint.next(Constraint.Alignment.FF));
        _p.add(createButton("...", 50,()->{
            JFileChooser _j = createFileChooser("Select Font ...", "Select", OsUtil.getApplicationName(), _dir, false, "Font File", "ttf", "otf");
            int          _rc = _j.showDialog(null, "Select");
            if(_rc == JFileChooser.APPROVE_OPTION)
            {
                _tf.setText(_j.getSelectedFile().getName());
                CfgDataUtil.addRecentToConfig(OsUtil.getApplicationName(),_j.getSelectedFile().getAbsolutePath());
                if(_ok!=null) _ok.execute(_j.getSelectedFile());
            }
            else
            {
                if(_nok!=null) _nok.run();
            }
        }), Constraint.next(Constraint.Alignment.FC));
        
        return _p;
    }
    public static JLabel createLabelRight(String _title, int _w, int _h)
    {
        return createLabel(SwingConstants.RIGHT,_title,_w,_h);
    }

    public static JLabel createLabelRight(String _title, int _w)
    {
        return createLabel(SwingConstants.RIGHT,_title,_w);
    }
    
    public static JSpinner createIntegerSpinner(String _title, int _val, int _min, int _max, int _step, Executable<Integer> _callback)
    {
        return createSpinner(_title,new SpinnerNumberModel(_val,_min,_max,_step), _callback);
    }
    
    public static <T> JComboBox<T> createCombobox(List<T> _items)
    {
        return createCombobox(-1, null,ListMapUtil.asArray(_items));
    }
    
    public static <T> JComboBox<T> createCombobox(int _w, List<T> _items)
    {
        return createCombobox(_w, null,ListMapUtil.asArray(_items));
    }
    
    public static <T> JComboBox<T> createCombobox(int _w, T... _items)
    {
        return createCombobox(_w, null,_items);
    }
    
    public static JComboBox<String> createStringCombobox(int _w)
    {
        return createCombobox(_w, (Executable<String>) null);
    }
    
    public static <T> JComboBox<T> createCombobox(int _w, Executable<T> _callback, List<T> _items)
    {
        return createCombobox(_w, _callback, ListMapUtil.asArray(_items));
    }

    public static <T> JComboBox<T> createCombobox(int _w, Executable<T> _callback, T... _items)
    {
        JComboBox<T> _b = new JComboBox<>();
        for(T _it : _items)
        {
            _b.addItem(_it);
        }
        
        Dimension _sz = _b.getPreferredSize();
        if(_w>0)
        {
            _sz.width = _w;
        }
        _b.setPreferredSize(_sz);
        if(_callback!=null)
        {
            _b.addItemListener((e)->{
                if(e.getStateChange()== ItemEvent.SELECTED)
                {
                    _callback.execute((T)_b.getSelectedItem());
                }
            });
        }
        return _b;
    }
    
    public static <T> TRenderedComboBox<T> createRenderedComboBox(ListCellRenderer<T> _renderer, List<T> _items)
    {
        return createRenderedComboBox(-1, null, _renderer,ListMapUtil.asArray(_items));
    }
    
    public static <T> TRenderedComboBox<T> createRenderedComboBox(int _w, ListCellRenderer<T> _renderer,List<T> _items)
    {
        return createRenderedComboBox(_w, null,_renderer,ListMapUtil.asArray(_items));
    }
    
    public static <T> TRenderedComboBox<T> createRenderedComboBox(Executable<T> _callback, ListCellRenderer<T> _renderer,List<T> _items)
    {
        return createRenderedComboBox(-1, _callback,_renderer,ListMapUtil.asArray(_items));
    }
    
    public static <T> TRenderedComboBox<T> createRenderedComboBox(ListCellRenderer<T> _renderer, T... _items)
    {
        return createRenderedComboBox(-1, null, _renderer,_items);
    }
    
    public static <T> TRenderedComboBox<T> createRenderedComboBox(int _w, ListCellRenderer<T> _renderer,T... _items)
    {
        return createRenderedComboBox(_w, null,_renderer,_items);
    }
    
    public static <T> TRenderedComboBox<T> createRenderedComboBox(Executable<T> _callback, ListCellRenderer<T> _renderer,T... _items)
    {
        return createRenderedComboBox(-1, _callback,_renderer,_items);
    }
    
    public static <T> TRenderedComboBox<T> createRenderedComboBox(int _w, Executable<T> _callback, ListCellRenderer<T> _renderer,List<T> _items)
    {
        return createRenderedComboBox(_w, _callback,_renderer,ListMapUtil.asArray(_items));
    }
    
    public static <T> TRenderedComboBox<T> createRenderedComboBox(int _w, Executable<T> _callback, ListCellRenderer<T> _renderer, T... _items)
    {
        TRenderedComboBox<T> _b = new TRenderedComboBox<>(_items, _renderer);

        Dimension _sz = _b.getPreferredSize();
        if(_w>0)
        {
            _sz.width = _w;
        }
        _b.setPreferredSize(_sz);
        if(_callback!=null)
        {
            _b.addItemListener((e)->{
                if(e.getStateChange()== ItemEvent.SELECTED)
                {
                    _callback.execute((T)_b.getSelectedItem());
                }
            });
        }
        return _b;
    }
    
    public static TRenderedComboBox<IColorDef> createIColorDefComboBox()
    {
        return createRenderedComboBox(-1, null, new ColorDefRenderer(), ColorDef.values());
    }
    
    public static TRenderedComboBox<IColorDef> createIColorDefComboBox(int _w)
    {
        return createRenderedComboBox(_w, null, new ColorDefRenderer(), ColorDef.values());
    }
    
    public static TRenderedComboBox<IColorDef> createIColorDefComboBox(Executable<IColorDef> _callback)
    {
        return createRenderedComboBox(-1, _callback, new ColorDefRenderer(), ColorDef.values());
    }
    
    public static TRenderedComboBox<IColorDef> createIColorDefComboBox(int _w, Executable<IColorDef> _callback)
    {
        return createRenderedComboBox(_w, _callback, new ColorDefRenderer(), ColorDef.values());
    }
    
    public static JPanel createIColorDefComboBoxExt(int _w, Executable<IColorDef> _callback)
    {
        TRenderedComboBox<IColorDef> _colorcbx = SwingHelper.createIColorDefComboBox(_w-35,_callback);
        JPanel _grid = SwingHelper.createEGrid(new int[]{80,20}, new int[]{100});
        _grid.add(_colorcbx, Constraint.next(Constraint.Alignment.FC));
        _grid.add(SwingHelper.createButton("...", 35,()->{
            //new MiniColorChooser().
            Color _c = JColorChooser.showDialog(_colorcbx, "Color", null);
            if(_c!=null)
            {
                CustomColorDef _cd = new CustomColorDef(_c);
                _colorcbx.addItem(_cd);
                _colorcbx.setSelectedItem(_cd);
            }
        }), Constraint.next(Constraint.Alignment.CC));
        return _grid;
    }
    
    @SneakyThrows
    public static JPanel createIColorDefComboBoxExt2(int _w, Executable<IColorDef> _callback)
    {
        TRenderedComboBox<IColorDef> _colorcbx = SwingHelper.createIColorDefComboBox(_w-35,_callback);
        JPanel _grid = SwingHelper.createEGrid(new int[]{80,20}, new int[]{100});
        _grid.add(_colorcbx, Constraint.next(Constraint.Alignment.FC));
        _grid.add(SwingHelper.createButton("...", 35,()->{
            new MiniColorChooserDialog().show((_c)->{
                if(_c!=null)
                {
                    CustomColorDef _cd = new CustomColorDef(_c);
                    _colorcbx.addItem(_cd);
                    _colorcbx.setSelectedItem(_cd);
                }
            });
        }), Constraint.next(Constraint.Alignment.CC));
        return _grid;
    }
    
    
    
    
    // ---
    
    public static TRenderedComboBox<IColorDef> createIColorDefComboBox(IColorDef... _items)
    {
        return createRenderedComboBox(-1, null, new ColorDefRenderer(), _items);
    }
    
    public static TRenderedComboBox<IColorDef> createIColorDefComboBox(int _w,IColorDef... _items)
    {
        return createRenderedComboBox(_w, null, new ColorDefRenderer(), _items);
    }
    
    public static TRenderedComboBox<IColorDef> createIColorDefComboBox(Executable<IColorDef> _callback, IColorDef... _items)
    {
        return createRenderedComboBox(-1, _callback, new ColorDefRenderer(), _items);
    }
    
    public static TRenderedComboBox<IColorDef> createIColorDefComboBox(int _w, Executable<IColorDef> _callback, IColorDef... _items)
    {
        return createRenderedComboBox(_w, _callback, new ColorDefRenderer(), _items);
    }
    
    // ---
    
    public static TRenderedComboBox<IColorDef> createIColorDefComboBox(List<IColorDef> _items)
    {
        return createRenderedComboBox(-1, null, new ColorDefRenderer(), _items);
    }
    
    public static TRenderedComboBox<IColorDef> createIColorDefComboBox(int _w,List<IColorDef> _items)
    {
        return createRenderedComboBox(_w, null, new ColorDefRenderer(), _items);
    }
    
    public static TRenderedComboBox<IColorDef> createIColorDefComboBox(Executable<IColorDef> _callback, List<IColorDef> _items)
    {
        return createRenderedComboBox(-1, _callback, new ColorDefRenderer(), _items);
    }
    
    public static TRenderedComboBox<IColorDef> createIColorDefComboBox(int _w, Executable<IColorDef> _callback, List<IColorDef> _items)
    {
        return createRenderedComboBox(_w, _callback, new ColorDefRenderer(), _items);
    }
    
    public static TRenderedComboBox<PaperUtil.IPaperSize> createIPaperSizeComboBox()
    {
        return createRenderedComboBox((ListCellRenderer)new PaperDefRenderer(), ListMapUtil.asArray(PaperUtil.allPaperSizes(false), PaperUtil.IPaperSize.class));
    }
    
    public static TRenderedComboBox<PaperUtil.IPaperSize> createIPaperSizeComboBox(int _w)
    {
        return createRenderedComboBox(_w, (ListCellRenderer)new PaperDefRenderer(), ListMapUtil.asArray(PaperUtil.allPaperSizes(false), PaperUtil.IPaperSize.class));
    }
    
    public static TRenderedComboBox<PaperUtil.IPaperSize> createIPaperSizeComboBox(Executable<PaperUtil.IPaperSize> _callback)
    {
        return createRenderedComboBox(-1, (ListCellRenderer)new PaperDefRenderer(), ListMapUtil.asArray(PaperUtil.allPaperSizes(false), PaperUtil.IPaperSize.class));
    }
    
    public static TRenderedComboBox<PaperUtil.IPaperSize> createIPaperSizeComboBox(int _w, Executable<PaperUtil.IPaperSize> _callback)
    {
        return createRenderedComboBox(_w, (Executable) _callback, (ListCellRenderer)new PaperDefRenderer(), ListMapUtil.asArray(PaperUtil.allPaperSizes(false), PaperUtil.IPaperSize.class));
    }
    

    // ---
    
    public static TRenderedComboBox<PaperUtil.IPaperSize> createIPaperSizeComboBox(boolean _f)
    {
        return createRenderedComboBox((ListCellRenderer)new PaperDefRenderer(), ListMapUtil.asArray(PaperUtil.allPaperSizes(_f), PaperUtil.IPaperSize.class));
    }
    
    public static TRenderedComboBox<PaperUtil.IPaperSize> createIPaperSizeComboBox(int _w, boolean _f)
    {
        return createRenderedComboBox(_w, (ListCellRenderer)new PaperDefRenderer(), ListMapUtil.asArray(PaperUtil.allPaperSizes(_f), PaperUtil.IPaperSize.class));
    }
    
    public static TRenderedComboBox<PaperUtil.IPaperSize> createIPaperSizeComboBox(boolean _f, Executable<PaperUtil.IPaperSize> _callback)
    {
        return createRenderedComboBox(-1, (ListCellRenderer)new PaperDefRenderer(), ListMapUtil.asArray(PaperUtil.allPaperSizes(_f), PaperUtil.IPaperSize.class));
    }
    
    public static TRenderedComboBox<PaperUtil.IPaperSize> createIPaperSizeComboBox(int _w, boolean _f, Executable<PaperUtil.IPaperSize> _callback)
    {
        return createRenderedComboBox(_w, (Executable) _callback, (ListCellRenderer)new PaperDefRenderer(), ListMapUtil.asArray(PaperUtil.allPaperSizes(_f), PaperUtil.IPaperSize.class));
    }
    
    
    public static JCheckBox createCheckBox(String _title, final Executable<Boolean> _callback)
    {
        JCheckBox _len = new JCheckBox(_title);
        _len.addChangeListener((e)->{
            if(_callback!=null)_callback.execute(_len.isSelected());
        });
        return _len;
    }
    
    public static <T> JSpinner createSpinner(String _title, SpinnerModel _mdel, final Executable<T> _callback)
    {
        JSpinner _len = new JSpinner(_mdel);
        _len.addChangeListener((e)->{
            if(_callback!=null)_callback.execute((T)_len.getValue());
        });
        return _len;
    }
    public static JButton createButton(String _title, final Runnable _action)
    {
        JButton _btn = createButton(_title);
        _btn.setAction(new AbstractAction(_title) {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(_action!=null)_action.run();
            }
        });
        return _btn;
    }
    
    public static JButton createButton(String _title, String _tooltip, final Runnable _action)
    {
        JButton _btn = createButton(_title, _action);
        _btn.setToolTipText(_tooltip);
        return _btn;
    }
    
    public static JButton createButton(String _title)
    {
        JButton _btn = new JButton(_title);
        Insets _i = _btn.getMargin();
        if(_i!=null)
        {
            _i.left >>= 1;
            _i.right >>= 1;
            _btn.setMargin(_i);
        }
        return _btn;
    }
    
    public static <T> T setLeftRightMargin(T _btn, int _n)
    {
        if(_btn instanceof AbstractButton)
        {
            Insets _i = ((AbstractButton)_btn).getMargin();
            if(_i!=null)
            {
                _i.left = _n;
                _i.right = _n;
                ((AbstractButton)_btn).setMargin(_i);
            }
        }
        return _btn;
    }
    
    
    public static <T extends Component> T setHeight(T _c, int _h)
    {
        Dimension _sz = _c.getPreferredSize();
        if(_sz!=null)
        {
            _sz.height = _h;
            _c.setPreferredSize(_sz);
        }
        
        if(_c.isMinimumSizeSet())
        {
            _sz = _c.getMinimumSize();
            _sz.height = _h;
            _c.setMinimumSize(_sz);
        }
        
        if(_c.isMaximumSizeSet())
        {
            _sz = _c.getMaximumSize();
            _sz.height = _h;
            _c.setMaximumSize(_sz);
        }
        return _c;
    }
    
    public static <T extends Component> T setWidth(T _c, int _w)
    {
        Dimension _sz = _c.getPreferredSize();
        if(_sz!=null)
        {
            _sz.width = _w;
            _c.setPreferredSize(_sz);
        }
        
        if(_c.isMinimumSizeSet())
        {
            _sz = _c.getMinimumSize();
            _sz.width = _w;
            _c.setMinimumSize(_sz);
        }
        
        if(_c.isMaximumSizeSet())
        {
            _sz = _c.getMaximumSize();
            _sz.width = _w;
            _c.setMaximumSize(_sz);
        }
        return _c;
    }
    
    public static <T extends Component> T setSize(T _c, int _w, int _h)
    {
        Dimension _sz = _c.getPreferredSize();
        if(_sz!=null)
        {
            _sz.height = _h;
            _sz.width = _w;
            _c.setPreferredSize(_sz);
        }
        _sz = _c.getMinimumSize();
        if(_sz!=null)
        {
            _sz.height = _h;
            _sz.width = _w;
            _c.setMinimumSize(_sz);
        }
        _sz = _c.getMaximumSize();
        if(_sz!=null)
        {
            _sz.height = _h;
            _sz.width = _w;
            _c.setMaximumSize(_sz);
        }
        return _c;
    }
    
    
    public static JButton createButton(String _title, final Executable<ActionEvent> _action)
    {
        return new JButton(new AbstractAction(_title) {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _action.execute(e);
            }
        });
    }
    
    public static JButton createButton(String _title, String _tooltip, final Executable<ActionEvent> _action)
    {
        JButton _btn = createButton(_title, _action);
        _btn.setToolTipText(_tooltip);
        return _btn;
    }
    
    
    public static JButton createButton(String _title, String _tooltip, int _width, final Runnable _action)
    {
        JButton _btn = createButton(_title, _width,_action);
        _btn.setToolTipText(_tooltip);
        return _btn;
    }
    
    public static JButton createButton(String _title, int _width, final Runnable _action)
    {
        JButton _btn = createButton(_title,_action);
        Dimension _sz = _btn.getPreferredSize();
        _sz.width = _width;
        _btn.setPreferredSize(_sz);
        _btn.setMaximumSize(_sz);
        return _btn;
    }
    
    public static JButton createButton(String _title, String _tooltip, int _width, final Executable<ActionEvent> _action)
    {
        JButton _btn = createButton(_title,_width,_action);
        _btn.setToolTipText(_tooltip);
        return _btn;
    }
    
    public static JButton createButton(String _title, int _width, final Executable<ActionEvent> _action)
    {
        JButton _btn = createButton(_title,_action);
        Dimension _sz = _btn.getPreferredSize();
        _sz.width = _width;
        _btn.setPreferredSize(_sz);
        _btn.setMaximumSize(_sz);
        return _btn;
    }
    
    public static <T> List<JRadioButton> createRadioButtonList(Executable<T> _setter, T... _values)
    {
        return createRadioButtonList((T)null, _setter, _values);
    }
    
    public static <T> List<JRadioButton> createRadioButtonList(T _default, Executable<T> _setter, T... _values)
    {
        List<JRadioButton> _ret = new Vector<>();
        for(final T _val : _values)
        {
            JRadioButton _btn = null;
            final String _n = Objects.toString(_val);
            _ret.add(_btn = new JRadioButton(new AbstractAction(_n)
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    for(JRadioButton _rb : _ret)
                    {
                        if(!_rb.getText().equalsIgnoreCase(_n))
                        {
                            _rb.setSelected(false);
                        }
                    }
                    _setter.execute(_val);
                }
            }));
            if(Objects.equals(_val,_default))
            {
                _btn.setSelected(true);
            }
            else
            {
                _btn.setSelected(false);
            }
        }
        return _ret;
    }
    
    public static <T> JXRadioGroup<T> createRadioButtonGroup(Executable<T> _setter, T... _values)
    {
        JXRadioGroup<T> _btn = new JXRadioGroup<>(_values);
        _btn.addActionListener((e)->{
            _setter.execute(_btn.getSelectedValue());
        });
        return _btn;
    }
    
    public static <T> JXRadioGroup<T> createRadioButtonGroup(T _default, Executable<T> _setter, T... _values)
    {
        JXRadioGroup<T> _btn = createRadioButtonGroup(_setter, _values);
        _btn.setSelectedValue(_default);
        return _btn;
    }
    
    public static JXBusyLabel createBusyLabel(int _w, int _h, int _v, int _i, Color _base, Color _trail)
    {
        JXBusyLabel label = new JXBusyLabel(new Dimension(_w, _h));
        BusyPainter painter = new BusyPainter(
                new Rectangle2D.Float(0, 0, _v,_v),
                new Rectangle2D.Float(_v, _v, _w-_v-_v, _h-_v-_v));
        painter.setTrailLength(_i>>>1);
        painter.setPoints(_i);
        painter.setFrame(-1);
        
        
        painter.setBaseColor(_base);
        painter.setHighlightColor(_trail);
        
        label.setPreferredSize(new Dimension(_w, _h));
        label.setIcon(new EmptyIcon(_w, _h));
        label.setBusyPainter(painter);
        
        return label;
    }
    public static JFrame  createBusyWindow(int _x, int _y, JXBusyLabel _label)
    {
        JFrame _frame = new JFrame();
        _frame.setUndecorated(true);
        _frame.add(_label);
        _frame.setSize(_label.getPreferredSize());
        _frame.setLocation(_x,_y);
        _frame.pack();
        _frame.setVisible(true);
        _frame.toFront();
        return _frame;
    }
    public static JFrame  createBusyWindow(int _x, int _y, int _w, int _h, int _v, int _i, Color _base, Color _trail)
    {
        return  createBusyWindow(_x, _y , createBusyLabel(_w, _h, _v, _i, _base, _trail));
    }
    
    public static int[] toArray(int... param)
    {
        return param;
    }
    
    public static int[] toIntArray(int _n, int _t)
    {
        int[] _ret = new int[_n];
        int _x = _t/_n;
        for(int _i = 0; _i<_n; _i++)
        {
            _ret[_i] = _x;
            _t-=_x;
        }
        
        if(_t>0)
        {
            _ret[_ret.length-1]+=_t;
        }
        return _ret;
    }

    
    
    public static float[] toArray(float... param)
    {
        return param;
    }
    public static long[] toArray(long... param)
    {
        return param;
    }
    public static double[] toArray(double... param)
    {
        return param;
    }
    
    public static String[] toArray(String... param)
    {
        return param;
    }
    
    public static void center(Window window, int width, int height) {
        window.setSize(width, height);
        center(window);
    }
    
    public static void center(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        window.setLocation((screenSize.width - window.getWidth()) / 2,
                (screenSize.height - window.getHeight()) / 3);
        
    }
    
    public static void setEnabledComponents(JComponent component, boolean enabled)
    {
        if (component.getComponentCount() == 0) {
            component.setEnabled(enabled);
        } else {
            for (int i = 0; i < component.getComponentCount(); i++) {
                if (component.getComponent(i) instanceof JComponent) {
                    setEnabledComponents(
                            (JComponent) component.getComponent(i), enabled);
                }
            }
        }
    }
    
    public static class JFileAndTypeChooser<T> extends JFileChooser
    {
        private JComboBox<String> _cb;
        
        public JFileAndTypeChooser(File currentDirectory)
        {
            super(currentDirectory);
        }
        
        private Map<String,T> types;
        
        public Map<String, T> getTypes()
        {
            return types;
        }
        
        public void setTypes(Map<String, T> _types)
        {
            types = _types;
        }
        
        public T getSelectedType()
        {
            if(getTypes()!=null && getTypes().size()!=0 && _cb!=null && _cb.getSelectedIndex()!=-1)
            {
                return getTypes().get(_cb.getSelectedItem());
            }
            return null;
        }
        
        @Override
        protected JDialog createDialog(Component parent)
                throws HeadlessException
        {
            JDialog _d = super.createDialog(parent);
            if(getTypes()!=null && getTypes().size()!=0)
            {
                Container _p = _d.getContentPane();
                _cb = createCombobox(new Vector(getTypes().keySet()));
                _cb.setBorder(new EmptyBorder(2,10,2,10));
                _cb.setSelectedIndex(0);
                _p.add(_cb, BorderLayout.SOUTH);
            }
            return _d;
        }
    }
    
    public static JFileChooser createDirectoryChooser(String _title, String _action, String _APP, File _lastDir)
    {
        JFileChooser _j = new JFileAndTypeChooser(_lastDir);
        _j.setDialogTitle(_title);
        _j.setDialogType(JFileChooser.CUSTOM_DIALOG);
        _j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        _j.setMultiSelectionEnabled(false);
        _j.setControlButtonsAreShown(true);
        _j.setApproveButtonText(_action);
        
        return customizeChooser(_j,_APP,_lastDir,JFileChooser.DIRECTORIES_ONLY);
    }
    
    public static JFileChooser createFileChooser(String _title, String _action, String _APP, File _lastDir)
    {
        return createFileChooser(_title,_action,_APP,_lastDir,true);
    }
    
    public static JFileChooser createFileChooser(String _title, String _action, String _APP, File _lastDir, boolean _allFilter, String _fileType, String... _fileExt)
    {
        return createFileChooser(_title,_action,_APP,_lastDir,_allFilter, (Map)ListMapUtil.toMap(_fileType,Arrays.asList(_fileExt)));
    }
    
    public static JFileChooser createFileChooser(String _title, String _action, String _APP, File _lastDir, boolean _allFilter, Map<String,List<String>> _filters)
    {
        JFileChooser _j = createFileChooser(_title, _action, _APP, _lastDir, _allFilter);
        for(Map.Entry<String, List<String>> _entry : _filters.entrySet())
        {
            _j.addChoosableFileFilter(new FileNameExtensionFilter(_entry.getKey(), ListMapUtil.asArray(_entry.getValue())));
        }
        return _j ;
    }
    
    public static JFileChooser createFileChooser(String _title, String _action, String _APP, File _lastDir, boolean _allFilter, FileNameExtensionFilter... _filters)
    {
        JFileChooser _j = new JFileAndTypeChooser(_lastDir);
        _j.setDialogTitle(_title);
        _j.setDialogType(JFileChooser.CUSTOM_DIALOG);
        _j.setFileSelectionMode(JFileChooser.FILES_ONLY);
        _j.setMultiSelectionEnabled(false);
        _j.setControlButtonsAreShown(true);
        _j.setApproveButtonText(_action);
        
        return customizeChooser(_j,_APP,_lastDir,JFileChooser.FILES_ONLY, _allFilter, _filters);
    }
    
    public static JFileChooser customizeChooser(JFileChooser _j, String _APP, File _lastDir, int _filesOnly, FileNameExtensionFilter... _filters)
    {
        return customizeChooser(_j, _APP, _lastDir,_filesOnly,false, _filters);
    }
    
    public static JFileChooser customizeChooser(JFileChooser _j, String _APP, File _lastDir, int _filesOnly, boolean _allOption, FileNameExtensionFilter... _filters)
    {
        JPanel _panel = new JPanel();
        _panel.setLayout(new BorderLayout());
        _panel.setPreferredSize(new Dimension(200,200));
        
        JXPanel _cpanel = new JXPanel();
        _cpanel.setLayout(new GridLayout(2,1));
        
        LabeledFileListModel _model2 = LabeledFileListModel.create();
        _model2.addFile(OsUtil.getJarDirectory(),"%_BASE%");
        //_model2.addFile(CfgDataUtil.getJarConfDir(),"%APP_CONF%");
        //_model2.addFile(CfgDataUtil.getJarShareDir(),"%APP_DATA%");
        _model2.addFile(CfgDataUtil.getConfigDir(_APP),"%_CONF%");
        _model2.addFile(CfgDataUtil.getDataDir(_APP),"%_DATA%");
        _model2.addFile(OsUtil.getCurrentDirectory(),"%CURRENT%");
        
        _model2.addFile(CfgDataUtil.getLastDirFromConfig(_APP, (_lastDir==null ? null: _lastDir.getAbsolutePath())),"%RECENT%");
        
        _model2.addFiles(CfgDataUtil.getRecentDirsFromConfig(_APP));
        
        JList _list2 = new JList(_model2){
            @Override
            public String getToolTipText(MouseEvent event)
            {
                LabeledFileListModel m = (LabeledFileListModel)this.getModel();
                int index = this.locationToIndex(event.getPoint());
                if( index>-1 ) {
                    return m.getElementAt(index).getFile().getAbsolutePath();
                }
                return "";
            }
        };
        _list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _list2.setLayoutOrientation(JList.VERTICAL);
        _list2.setVisibleRowCount(-1);
        _list2.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                LabeledFileListItem _item = (LabeledFileListItem) _list2.getModel().getElementAt(_list2.getSelectedIndex());
                _j.setCurrentDirectory(_item.getFile());
            }
        });
        _cpanel.add(new AbstractTitledPanel("Recent Dirs"){
            @Override
            public JPanel makePanel()
            {
                JXPanel _p = new JXPanel();
                _p.setLayout(new GridLayout());
                JScrollPane _scr = new JScrollPane(_list2);
                _p.add(_scr);
                return _p;
            }
        });
        
        LabeledFileListModel _model1 = LabeledFileListModel.from(CfgDataUtil.getRecentFromConfig(_APP), _filters);
        JList _list1 = new JList(_model1){
            @Override
            public String getToolTipText(MouseEvent event)
            {
                LabeledFileListModel m = (LabeledFileListModel)this.getModel();
                int index = this.locationToIndex(event.getPoint());
                if( index>-1 ) {
                    return m.getElementAt(index).getFile().getAbsolutePath();
                }
                return "";
            }
        };
        _list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _list1.setLayoutOrientation(JList.VERTICAL);
        _list1.setVisibleRowCount(-1);
        _list1.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                LabeledFileListItem _item = (LabeledFileListItem) _list1.getModel().getElementAt(_list1.getSelectedIndex());
                _j.setCurrentDirectory(_item.getFile().getParentFile());
                _j.setSelectedFile(_item.getFile());
            }
        });
        
        _cpanel.add(new AbstractTitledPanel("Recent Files"){
            @Override
            public JPanel makePanel()
            {
                JXPanel _p = new JXPanel();
                _p.setLayout(new GridLayout());
                JScrollPane _scr = new JScrollPane(_list1);
                _p.add(_scr);
                return _p;
            }
        });
        _panel.add(_cpanel);
        
        _j.setAccessory(_panel);
        _j.setCurrentDirectory(_lastDir==null ? CfgDataUtil.getLastDirFromConfig(_APP, null) : _lastDir);
        _j.setAcceptAllFileFilterUsed(_allOption);
        _j.setFileSelectionMode(_filesOnly);
        if(_filters!=null && _filters.length!=0)
        {
            for(FileNameExtensionFilter _f : _filters)
            {
                _j.addChoosableFileFilter(_f);
                _j.setFileFilter(_f);
            }
        }
        return _j;
    }
    
    
    public static Color createColor(long _color) {
        return new Color((int)_color, true);
    }
    
    public static JTextField createTextField(String _title)
    {
        return createTextField(-1, _title, -1, -1, null);
    }
    
    public static JTextField createTextField(String _title, Executable<JTextField> _callback)
    {
        return createTextField(-1, _title, -1, -1, _callback);
    }
    
    public static JTextField createTextField(int _cols, String _title)
    {
        return createTextField(_cols, _title, -1, -1, null);
    }

    public static JTextField createTextField(int _cols, int _rows, String _title)
    {
        return createTextField(_cols, _rows, _title, -1, -1, null);
    }
    
    public static JTextField createTextField(int _cols, String _title, int _w)
    {
        return createTextField(_cols, _title, _w, -1, null);
    }
    
    public static JTextField createTextField(int _cols, String _title, Executable<JTextField> _callback)
    {
        return createTextField(_cols, _title, -1, -1, _callback);
    }
    
    public static JTextField createTextField(int _cols, int _rows, String _title, Executable<JTextField> _callback)
    {
        return createTextField(_cols, _rows, _title, -1, -1, _callback);
    }
    
    public static JTextField createTextField(String _title, int _w)
    {
        return createTextField(-1, _title, _w, -1, null);
    }
    
    public static JTextField createTextField(int _cols, String _title, int _width, int _height, Executable<JTextField> _callback)
    {
        return createTextField(_cols, -1, _title, _width, _height, _callback);
    }

    public static JTextField createTextField(int _cols, int _rows, String _title, int _width, int _height, Executable<JTextField> _callback)
    {
        final JTextField _tf = new JTextField(_title);
        if(_callback!=null)
        {
            addChangeListener(_tf, (e)->{
                _callback.execute(_tf);
            });
        }
        if(_rows>0) _tf.setColumns(_rows);
        if(_cols>0) _tf.setColumns(_cols);
        Dimension _sz = _tf.getPreferredSize();
        if(_width>0)
        {
            _sz.width = _width;
        }
        if(_height>0)
        {
            _sz.height = _height;
        }
        _tf.setPreferredSize(_sz);
        return _tf;
    }
    
    public static void addChangeListener(JTextComponent text, ChangeListener changeListener) {
        Objects.requireNonNull(text);
        Objects.requireNonNull(changeListener);
        DocumentListener dl = new DocumentListener() {
            private int lastChange = 0, lastNotifiedChange = 0;
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                lastChange++;
                SwingUtilities.invokeLater(() -> {
                    if (lastNotifiedChange != lastChange) {
                        lastNotifiedChange = lastChange;
                        changeListener.stateChanged(new ChangeEvent(text));
                    }
                });
            }
        };
        
        text.addPropertyChangeListener("document", (PropertyChangeEvent e) -> {
            Document d1 = (Document)e.getOldValue();
            Document d2 = (Document)e.getNewValue();
            if (d1 != null) d1.removeDocumentListener(dl);
            if (d2 != null) d2.addDocumentListener(dl);
            dl.changedUpdate(null);
        });
        Document d = text.getDocument();
        if (d != null) d.addDocumentListener(dl);
    }
    
    public static <T extends JComponent> T correctMargins(T _o, int _w, int _h, int _t, int _l, int _b, int _r) {
        
        if(_o instanceof AbstractButton) {
            AbstractButton _c = ((AbstractButton) _o);
            Dimension _dim = new Dimension(_w<0 ? -_w:(_c.getText().length()*10), _h);
            _o.setSize(_dim);
            _o.setPreferredSize(_dim);
            _o.setMinimumSize(_dim);
            _o.setMaximumSize(new Dimension((int) (_dim.getWidth()*100),_h));
            if(_t>0) _c.setMargin(new Insets(_t, _l, _b, _r));
        }
        else
        if(_o instanceof JComponent) {
            JComponent _c = ((JComponent) _o);
            Dimension _dim = new Dimension(_w<0 ? -_w:_w, _h);
            _o.setSize(_dim);
            _o.setPreferredSize(_dim);
            _o.setMinimumSize(_dim);
            _o.setMaximumSize(_dim);
        }
        return _o;
    }
    public static <T extends JComponent> T correctMargins(T _o, int _w, int _h) {
        return correctMargins(_o, _w, _h, -1, -1, -1, -1);
    }
    public static <T extends JComponent> T correctMargins(T _o, int _w, int _h,int _t, int _l, int _r) {
        return correctMargins(_o, _w, _h, _t,	_l,	_t,	_r);
    }
    public static <T extends JComponent> T correctMargins(T _o, int _w, int _h,int _t, int _l) {
        return correctMargins(_o, _w, _h, _t,	_l,	_t,	_l);
    }
    public static <T extends JComponent> T correctMargins(T _o, int _w, int _h,int _t) {
        return correctMargins(_o, _w, _h, _t,	_t,	_t,	_t);
    }
    public static <T extends JComponent> T correctMargins(T _o,int _t) {
        return correctMargins(_o, 50, 30, _t,	_t,	_t,	_t);
    }
    public static <T extends JComponent> T correctMargins(T _o) {
        return correctMargins(_o, 50, 30);
    }
    
    public static void createNerdFontSetterMenuList(JPopupMenu _btn, Executable<String> _callback)
    {
        for(final String _fn : SwingHelper.listFonts())
        {
            createPopupMenu(_btn,_fn,()->{ if(_callback!=null) _callback.execute(_fn);});
        }
    }
    
    public static void createFontSetterMenuList(JFrame _root, JMenu _btn, boolean _selector, boolean _monolist, final SetFontNameOnlyHandler _handler, final SetFontObjectHandler _handler2, final SetFontNameAndSizeHandler _handler3)
    {
        for(final String _fn : SwingHelper.listFonts())
        {
            _btn.add(new JMenuItem(new AbstractAction(_fn) {
                @Override
                public void actionPerformed(ActionEvent _e) {
                    _handler.setFont(_fn);
                }
            }));
        }
        if(_selector)
        {
            _btn.addSeparator();
            _btn.add(new JMenuItem(new AbstractAction("Select ...") {
                @Override
                public void actionPerformed(ActionEvent _e)
                {
                    SwingUtilities.invokeLater(() -> {
                        FontDialog dialog = new FontDialog((Frame)null, "Select Font", true);
                        dialog.setDefaultCloseOperation(2);
                        dialog.setLocation(_root.getLocation());
                        //dialog.setSelectedFont(this._textArea.getFont());
                        dialog.setVisible(true);
                        if (!dialog.isCancelSelected()) {
                            _handler2.setFont(dialog.getSelectedFont());
                        }
                    });
                }
            }));
            _btn.addSeparator();
            _btn.add(new JMenuItem(new AbstractAction("Select ...") {
                @Override
                public void actionPerformed(ActionEvent _e)
                {
                    SwingUtilities.invokeLater(() -> {
                        //int _size = this._textArea.getFont().getSize();
                        FontChooserDialog _fntd = new FontChooserDialog(new JFrame(""), "Choose Font", false, /*this._textArea.getFont()*/null)
                        {
                            @Override
                            public void actionPerformed(ActionEvent event) {
                                super.actionPerformed(event);
                                String _cmd = event.getActionCommand();
                                if("okButton".equalsIgnoreCase(_cmd))
                                {
                                    _handler2.setFont(this.getSelectedFont());
                                }
                            }
                        };
                        _fntd.setMinimumSize(new Dimension(300,300));
                        _fntd.setLocation(_root.getLocation());
                        _fntd.setVisible(true);
                    });
                }
            }));
        }
        if(_monolist)
        {
            _btn.addSeparator();
            char _lastChar = 0;
            JMenu _sbtn = null;
            for(final String _fn : SwingHelper.listPlatformFonts(true))
            {
                char _c = _fn.toUpperCase().charAt(0);
                if(_lastChar < _c)
                {
                    _lastChar = _c;
                    _btn.add(_sbtn = new JMenu(Character.toString(_c)+"..."));
                }
                _sbtn.add(new JMenuItem(new AbstractAction(_fn) {
                    @Override
                    public void actionPerformed(ActionEvent _e) {
                        _handler.setFont(_fn);
                    }
                }));
            }
        }
        
    }
    
    public static JideButton createIdeButton(String _title, final Runnable _action)
    {
        JideButton _btn = createIdeButton(_title);
        _btn.setAction(new AbstractAction(_title) {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _action.run();
            }
        });
        return _btn;
    }
    
    public static JideButton createIdeButton(String _title, String _tooltip, final Runnable _action)
    {
        JideButton _btn = createIdeButton(_title, _action);
        _btn.setToolTipText(_tooltip);
        return _btn;
    }
    
    public static JideButton createIdeButton(String _title)
    {
        JideButton _btn = new JideButton(_title);
        Insets _i = _btn.getMargin();
        if(_i!=null)
        {
            _i.left >>= 1;
            _i.right >>= 1;
            _btn.setMargin(_i);
        }
        return _btn;
    }
    
    public static JideButton createIdeButton(String _title, final Executable<ActionEvent> _action)
    {
        return new JideButton(new AbstractAction(_title) {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _action.execute(e);
            }
        });
    }
    
    public static JideButton createIdeButton(String _title, String _tooltip, final Executable<ActionEvent> _action)
    {
        JideButton _btn = createIdeButton(_title, _action);
        _btn.setToolTipText(_tooltip);
        return _btn;
    }
    
    
    public static JideButton createIdeButton(String _title, String _tooltip, int _width, final Runnable _action)
    {
        JideButton _btn = createIdeButton(_title, _width,_action);
        _btn.setToolTipText(_tooltip);
        return _btn;
    }
    
    public static JideButton createIdeButton(String _title, int _width, final Runnable _action)
    {
        JideButton _btn = createIdeButton(_title,_action);
        Dimension _sz = _btn.getPreferredSize();
        _sz.width = _width;
        _btn.setPreferredSize(_sz);
        _btn.setMaximumSize(_sz);
        return _btn;
    }
    
    public static JideButton createIdeButton(String _title, String _tooltip, int _width, final Executable<ActionEvent> _action)
    {
        JideButton _btn = createIdeButton(_title,_width,_action);
        _btn.setToolTipText(_tooltip);
        return _btn;
    }
    
    public static JideButton createIdeButton(String _title, int _width, final Executable<ActionEvent> _action)
    {
        JideButton _btn = createIdeButton(_title,_action);
        Dimension _sz = _btn.getPreferredSize();
        _sz.width = _width;
        _btn.setPreferredSize(_sz);
        _btn.setMaximumSize(_sz);
        return _btn;
    }
    
    public static JPanel createTitledPanel(String _title)
    {
        JPanel _panel = new JPanel();
        if(_title!=null)
        {
            Border _etch = BorderFactory.createEtchedBorder((EtchedBorder.LOWERED));
            TitledBorder _t = BorderFactory.createTitledBorder(_etch, _title);
            _panel.setBorder(_t);
        }
        return _panel;
    }
    
    public static JXPanel createTitledXPanel(String _title)
    {
        JXPanel _panel = new JXPanel();
        if(_title!=null)
        {
            Border _etch = BorderFactory.createEtchedBorder((EtchedBorder.LOWERED));
            TitledBorder _t = BorderFactory.createTitledBorder(_etch, _title);
            _panel.setBorder(_t);
        }
        return _panel;
    }
    
    
    public static JToggleButton createToggleButton(String _title)
    {
        JToggleButton _btn = new JToggleButton(_title);
        Insets _i = _btn.getMargin();
        if(_i!=null)
        {
            _i.left >>= 1;
            _i.right >>= 1;
            _btn.setMargin(_i);
        }
        return _btn;
    }
    
    public static JToggleButton createToggleButton(String _title, final Executable<Boolean> _action)
    {
        JToggleButton _btn = createToggleButton(_title);
        if(_action!=null)
        {
            _btn.addChangeListener((e)-> {
                _action.execute(_btn.isSelected());
            });
        }
        return _btn;
    }
    
    public static JToggleButton createToggleButton(String _title, String _tooltip, final Executable<Boolean> _action)
    {
        JToggleButton _btn = createToggleButton(_title, _action);
        _btn.setToolTipText(_tooltip);
        return _btn;
    }
    
    
    public static JToggleButton createToggleButton(String _title, String _tooltip, int _width, final Executable<Boolean> _action)
    {
        JToggleButton _btn = createToggleButton(_title,_width,_action);
        _btn.setToolTipText(_tooltip);
        return _btn;
    }
    
    public static JToggleButton createToggleButton(String _title, int _width)
    {
        return createToggleButton(_title,_width,null);
    }

    public static JToggleButton createToggleButton(String _title, int _width, final Executable<Boolean> _action)
    {
        JToggleButton _btn = createToggleButton(_title,_action);
        Dimension _sz = _btn.getPreferredSize();
        if(_width>0)
        {
            _sz.width = _width;
        }
        _btn.setPreferredSize(_sz);
        return _btn;
    }
    
    
    public static JideSplitButton createIdeSplitButton(String _title, String _tooltip, final Runnable _main, int _width)
    {
        JideSplitButton _btn = new JideSplitButton(_title);
        if(_tooltip!=null) _btn.setToolTipText(_tooltip);
        Insets _i = _btn.getMargin();
        if(_i!=null)
        {
            _i.left >>= 1;
            _i.right >>= 1;
            _btn.setMargin(_i);
        }
        if(_main!=null)
        {
            _btn.setAction(new AbstractAction(_title) {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    _main.run();
                }
            });
        }
        Dimension _sz = _btn.getPreferredSize();
        if(_width>0)
        {
            _sz.width = _width;
        }
        _btn.setPreferredSize(_sz);
        return _btn;
    }

    public static JSplitButton createSplitButton(String _title, String _tooltip, final Runnable _main, int _width)
    {
        JSplitButton _btn = new JSplitButton(_title);
        if(_tooltip!=null) _btn.setToolTipText(_tooltip);
        Insets _i = _btn.getMargin();
        if(_i!=null)
        {
            _i.left >>= 1;
            _i.right >>= 1;
            _btn.setMargin(_i);
        }
        if(_main!=null)
        {
            _btn.setAction(new AbstractAction(_title) {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    _main.run();
                }
            });
        }
        Dimension _sz = _btn.getPreferredSize();
        if(_width>0)
        {
            _sz.width = _width;
        }
        _btn.setPreferredSize(_sz);
        _btn.setPopupMenu(new JPopupMenu());
        return _btn;
    }
    
    public static JSplitButton createSplitButton(String _title, String _tooltip, final Runnable _main, int _width
            , String _m0, Runnable _r0
            , String _m1, Runnable _r1
            , String _m2, Runnable _r2
            , String _m3, Runnable _r3
            , String _m4, Runnable _r4
            , String _m5, Runnable _r5
            , String _m6, Runnable _r6
            , String _m7, Runnable _r7
            , String _m8, Runnable _r8
            , String _m9, Runnable _r9
            , String _m10, Runnable _r10
            , String _m11, Runnable _r11)
    {
        JSplitButton _btn = createSplitButton(_title, _tooltip, _main, _width);
        createPopupMenu(_btn.getPopupMenu(), _m0,_r0);
        createPopupMenu(_btn.getPopupMenu(), _m1,_r1);
        createPopupMenu(_btn.getPopupMenu(), _m2,_r2);
        createPopupMenu(_btn.getPopupMenu(), _m3,_r3);
        createPopupMenu(_btn.getPopupMenu(), _m4,_r4);
        createPopupMenu(_btn.getPopupMenu(), _m5,_r5);
        createPopupMenu(_btn.getPopupMenu(), _m6,_r6);
        createPopupMenu(_btn.getPopupMenu(), _m7,_r7);
        createPopupMenu(_btn.getPopupMenu(), _m8,_r8);
        createPopupMenu(_btn.getPopupMenu(), _m9,_r9);
        createPopupMenu(_btn.getPopupMenu(), _m10,_r10);
        createPopupMenu(_btn.getPopupMenu(), _m11,_r11);
        return  _btn;
    }
    
    public static JSplitButton createSplitButton(String _title, String _tooltip, final Runnable _main, int _width
            , String _m0, Runnable _r0
            , String _m1, Runnable _r1
            , String _m2, Runnable _r2
            , String _m3, Runnable _r3
            , String _m4, Runnable _r4
            , String _m5, Runnable _r5
            , String _m6, Runnable _r6
            , String _m7, Runnable _r7
            , String _m8, Runnable _r8
            , String _m9, Runnable _r9
            , String _m10, Runnable _r10)
    {
        JSplitButton _btn = createSplitButton(_title, _tooltip, _main, _width);
        createPopupMenu(_btn.getPopupMenu(), _m0,_r0);
        createPopupMenu(_btn.getPopupMenu(), _m1,_r1);
        createPopupMenu(_btn.getPopupMenu(), _m2,_r2);
        createPopupMenu(_btn.getPopupMenu(), _m3,_r3);
        createPopupMenu(_btn.getPopupMenu(), _m4,_r4);
        createPopupMenu(_btn.getPopupMenu(), _m5,_r5);
        createPopupMenu(_btn.getPopupMenu(), _m6,_r6);
        createPopupMenu(_btn.getPopupMenu(), _m7,_r7);
        createPopupMenu(_btn.getPopupMenu(), _m8,_r8);
        createPopupMenu(_btn.getPopupMenu(), _m9,_r9);
        createPopupMenu(_btn.getPopupMenu(), _m10,_r10);
        return  _btn;
    }
    
    public static JSplitButton createSplitButton(String _title, String _tooltip, final Runnable _main, int _width
            , String _m0, Runnable _r0
            , String _m1, Runnable _r1
            , String _m2, Runnable _r2
            , String _m3, Runnable _r3
            , String _m4, Runnable _r4
            , String _m5, Runnable _r5
            , String _m6, Runnable _r6
            , String _m7, Runnable _r7
            , String _m8, Runnable _r8
            , String _m9, Runnable _r9)
    {
        JSplitButton _btn = createSplitButton(_title, _tooltip, _main, _width);
        createPopupMenu(_btn.getPopupMenu(), _m0,_r0);
        createPopupMenu(_btn.getPopupMenu(), _m1,_r1);
        createPopupMenu(_btn.getPopupMenu(), _m2,_r2);
        createPopupMenu(_btn.getPopupMenu(), _m3,_r3);
        createPopupMenu(_btn.getPopupMenu(), _m4,_r4);
        createPopupMenu(_btn.getPopupMenu(), _m5,_r5);
        createPopupMenu(_btn.getPopupMenu(), _m6,_r6);
        createPopupMenu(_btn.getPopupMenu(), _m7,_r7);
        createPopupMenu(_btn.getPopupMenu(), _m8,_r8);
        createPopupMenu(_btn.getPopupMenu(), _m9,_r9);
        return  _btn;
    }
    
    public static JSplitButton createSplitButton(String _title, String _tooltip, final Runnable _main, int _width
            , String _m0, Runnable _r0
            , String _m1, Runnable _r1
            , String _m2, Runnable _r2
            , String _m3, Runnable _r3
            , String _m4, Runnable _r4
            , String _m5, Runnable _r5
            , String _m6, Runnable _r6
            , String _m7, Runnable _r7
            , String _m8, Runnable _r8)
    {
        JSplitButton _btn = createSplitButton(_title, _tooltip, _main, _width);
        createPopupMenu(_btn.getPopupMenu(), _m0,_r0);
        createPopupMenu(_btn.getPopupMenu(), _m1,_r1);
        createPopupMenu(_btn.getPopupMenu(), _m2,_r2);
        createPopupMenu(_btn.getPopupMenu(), _m3,_r3);
        createPopupMenu(_btn.getPopupMenu(), _m4,_r4);
        createPopupMenu(_btn.getPopupMenu(), _m5,_r5);
        createPopupMenu(_btn.getPopupMenu(), _m6,_r6);
        createPopupMenu(_btn.getPopupMenu(), _m7,_r7);
        createPopupMenu(_btn.getPopupMenu(), _m8,_r8);
        return  _btn;
    }
    
    public static JSplitButton createSplitButton(String _title, String _tooltip, final Runnable _main, int _width
            , String _m0, Runnable _r0
            , String _m1, Runnable _r1
            , String _m2, Runnable _r2
            , String _m3, Runnable _r3
            , String _m4, Runnable _r4
            , String _m5, Runnable _r5
            , String _m6, Runnable _r6
            , String _m7, Runnable _r7)
    {
        JSplitButton _btn = createSplitButton(_title, _tooltip, _main, _width);
        createPopupMenu(_btn.getPopupMenu(), _m0,_r0);
        createPopupMenu(_btn.getPopupMenu(), _m1,_r1);
        createPopupMenu(_btn.getPopupMenu(), _m2,_r2);
        createPopupMenu(_btn.getPopupMenu(), _m3,_r3);
        createPopupMenu(_btn.getPopupMenu(), _m4,_r4);
        createPopupMenu(_btn.getPopupMenu(), _m5,_r5);
        createPopupMenu(_btn.getPopupMenu(), _m6,_r6);
        createPopupMenu(_btn.getPopupMenu(), _m7,_r7);
        return  _btn;
    }
    
    public static JSplitButton createSplitButton(String _title, String _tooltip, final Runnable _main, int _width
            , String _m0, Runnable _r0
            , String _m1, Runnable _r1
            , String _m2, Runnable _r2
            , String _m3, Runnable _r3
            , String _m4, Runnable _r4
            , String _m5, Runnable _r5
            , String _m6, Runnable _r6)
    {
        JSplitButton _btn = createSplitButton(_title, _tooltip, _main, _width);
        createPopupMenu(_btn.getPopupMenu(), _m0,_r0);
        createPopupMenu(_btn.getPopupMenu(), _m1,_r1);
        createPopupMenu(_btn.getPopupMenu(), _m2,_r2);
        createPopupMenu(_btn.getPopupMenu(), _m3,_r3);
        createPopupMenu(_btn.getPopupMenu(), _m4,_r4);
        createPopupMenu(_btn.getPopupMenu(), _m5,_r5);
        createPopupMenu(_btn.getPopupMenu(), _m6,_r6);
        return  _btn;
    }
    
    public static JSplitButton createSplitButton(String _title, String _tooltip, final Runnable _main, int _width
            , String _m0, Runnable _r0
            , String _m1, Runnable _r1
            , String _m2, Runnable _r2
            , String _m3, Runnable _r3
            , String _m4, Runnable _r4
            , String _m5, Runnable _r5)
    {
        JSplitButton _btn = createSplitButton(_title, _tooltip, _main, _width);
        createPopupMenu(_btn.getPopupMenu(), _m0,_r0);
        createPopupMenu(_btn.getPopupMenu(), _m1,_r1);
        createPopupMenu(_btn.getPopupMenu(), _m2,_r2);
        createPopupMenu(_btn.getPopupMenu(), _m3,_r3);
        createPopupMenu(_btn.getPopupMenu(), _m4,_r4);
        createPopupMenu(_btn.getPopupMenu(), _m5,_r5);
        return  _btn;
    }
    
    public static JSplitButton createSplitButton(String _title, String _tooltip, final Runnable _main, int _width
            , String _m0, Runnable _r0
            , String _m1, Runnable _r1
            , String _m2, Runnable _r2
            , String _m3, Runnable _r3
            , String _m4, Runnable _r4)
    {
        JSplitButton _btn = createSplitButton(_title, _tooltip, _main, _width);
        createPopupMenu(_btn.getPopupMenu(), _m0,_r0);
        createPopupMenu(_btn.getPopupMenu(), _m1,_r1);
        createPopupMenu(_btn.getPopupMenu(), _m2,_r2);
        createPopupMenu(_btn.getPopupMenu(), _m3,_r3);
        createPopupMenu(_btn.getPopupMenu(), _m4,_r4);
        return  _btn;
    }
    
    public static JSplitButton createSplitButton(String _title, String _tooltip, final Runnable _main, int _width
            , String _m0, Runnable _r0
            , String _m1, Runnable _r1
            , String _m2, Runnable _r2
            , String _m3, Runnable _r3)
    {
        JSplitButton _btn = createSplitButton(_title, _tooltip, _main, _width);
        createPopupMenu(_btn.getPopupMenu(), _m0,_r0);
        createPopupMenu(_btn.getPopupMenu(), _m1,_r1);
        createPopupMenu(_btn.getPopupMenu(), _m2,_r2);
        createPopupMenu(_btn.getPopupMenu(), _m3,_r3);
        return  _btn;
    }
    
    public static JSplitButton createSplitButton(String _title, String _tooltip, final Runnable _main, int _width
            , String _m0, Runnable _r0
            , String _m1, Runnable _r1
            , String _m2, Runnable _r2)
    {
        JSplitButton _btn = createSplitButton(_title, _tooltip, _main, _width);
        createPopupMenu(_btn.getPopupMenu(), _m0,_r0);
        createPopupMenu(_btn.getPopupMenu(), _m1,_r1);
        createPopupMenu(_btn.getPopupMenu(), _m2,_r2);
        return  _btn;
    }
    
    public static JSplitButton createSplitButton(String _title, String _tooltip, final Runnable _main, int _width
            , String _m0, Runnable _r0
            , String _m1, Runnable _r1)
    {
        JSplitButton _btn = createSplitButton(_title, _tooltip, _main, _width);
        createPopupMenu(_btn.getPopupMenu(), _m0,_r0);
        createPopupMenu(_btn.getPopupMenu(), _m1,_r1);
        return  _btn;
    }
    
    public static JSplitButton createSplitButton(String _title, String _tooltip, final Runnable _main, int _width
            , String _m0, Runnable _r0)
    {
        JSplitButton _btn = createSplitButton(_title, _tooltip, _main, _width);
        createPopupMenu(_btn.getPopupMenu(), _m0,_r0);
        return  _btn;
    }
    
    public static UiBoosterOptions makeUiBoosterOptions()
    {
        return new UiBoosterOptions((BasicLookAndFeel) UIManager.getLookAndFeel(), UiBoosterOptions.defaultIconPath, UiBoosterOptions.defaultLoadingImage);
    }
    
    public static void showConfirmDialog(String _title, String _message, Runnable _ok)
    {
        showConfirmDialog(_title,_message,_ok, null);
    }
    
    public static void showConfirmDialog(String _title, String _message, Runnable _ok, Runnable _cancel)
    {
        new UiBooster(SwingHelper.makeUiBoosterOptions())
                .showConfirmDialog(
                        _message,
                        _title, ()->{
                            if(_ok!=null) _ok.run();
                        }, ()->{
                            if(_cancel!=null) _cancel.run();
                        });
    }
    
    public static void showNotification(String _title, String _message)
    {
        new UiBooster(makeUiBoosterOptions()).createNotification(_message, _title);
    }
    
    public static void showException(String _title, String _message, Exception _th)
    {
        new UiBooster(makeUiBoosterOptions()).showException(_message, _title,_th);
    }
    
    public static ProgressDialog showProgressDialogPercent(String _title, String _message)
    {
        return showProgressDialog(_title,_message, 0, 100);
    }
    
    public static ProgressDialog showProgressDialog(String _title, String _message, int _min, int _max)
    {
        return new UiBooster(makeUiBoosterOptions()).showProgressDialog(_message, _title, _min, _max);
    }
    
    public static void executeFileSaveChooser(Component _modal, String _title, String _action, File _lastdir, File _lastfile, Executable<File> _ok, String _type, String... _exts)
    {
        executeFileSaveChooser(_modal,_title, _action,_lastdir, _lastfile,_ok,
                createSimpleFileNameFilter(_type, _exts));
    }
    
    public static void executeFileSaveChooser(Component _modal, String _title, String _action, File _lastdir, File _lastfile, Executable<File> _ok, FileNameExtensionFilter... _filter)
    {
        executeFileSaveChooser(_modal,_title, _action,_lastdir, _lastfile,_ok,null, _filter);
    }
    
    @SneakyThrows
    public static void executeFileSaveChooser(Component _modal, String _title, String _action, File _lastdir, File _lastfile, Executable<File> _ok, Runnable _cancel, FileNameExtensionFilter... _filter)
    {
        JFileChooser _j = createFileChooser(_title, _action, OsUtil.getApplicationName(),
                CfgDataUtil.getLastDirFromConfig(OsUtil.getApplicationName(), _lastdir==null ? null : _lastdir.getAbsolutePath()),
                true, _filter);
        
        if(_lastfile != null)
        {
            _j.setCurrentDirectory(_lastfile.getParentFile());
            _j.setSelectedFile(_lastfile);
        }
        
        File _file = null;
        int _opt = _j.showSaveDialog(_modal);
        if(_opt == JFileChooser.APPROVE_OPTION)
        {
            _file = _j.getSelectedFile();
            CfgDataUtil.addRecentToConfig(OsUtil.getApplicationName(),_file.getAbsolutePath());
        }
        else
        {
            if(_cancel!=null)_cancel.run();
            return;
        }
        
        if(_file != null)
        {
            CfgDataUtil.setLastDirToConfig(OsUtil.getApplicationName(),_file.getParentFile());
            if(_ok!=null)_ok.execute(_file);
        }
    }
    
    public static <T> void executeFileAndTypeSaveChooser(Component _modal, String _title, String _action, File _lastdir, File _lastfile, Executable2<File,T> _ok, Map<String,T> _types, String _type, String... _exts)
    {
        executeFileAndTypeSaveChooser(_modal,_title, _action,_lastdir, _lastfile,_ok,null,_types,
                createSimpleFileNameFilter(_type, _exts));
    }
    
    public static <T> void executeFileAndTypeSaveChooser(Component _modal, String _title, String _action, File _lastdir, File _lastfile, Executable2<File,T> _ok, Map<String,T> _types, FileNameExtensionFilter... _filter)
    {
        executeFileAndTypeSaveChooser(_modal,_title, _action,_lastdir, _lastfile,_ok,null, _types, _filter);
    }
    
    @SneakyThrows
    public static <T> void executeFileAndTypeSaveChooser(Component _modal, String _title, String _action, File _lastdir, File _lastfile, Executable2<File,T> _ok, Runnable _cancel, Map<String,T> _types, FileNameExtensionFilter... _filter)
    {
        JFileAndTypeChooser<T> _j = (JFileAndTypeChooser<T>) createFileChooser(_title, _action, OsUtil.getApplicationName(),
                CfgDataUtil.getLastDirFromConfig(OsUtil.getApplicationName(), _lastdir==null ? null : _lastdir.getAbsolutePath()),
                true, _filter);
        
        _j.setTypes(_types);
        
        if(_lastfile != null)
        {
            _j.setCurrentDirectory(_lastfile.getParentFile());
            _j.setSelectedFile(_lastfile);
        }
        
        File _file = null;
        int _opt = _j.showSaveDialog(_modal);
        if(_opt == JFileChooser.APPROVE_OPTION)
        {
            _file = _j.getSelectedFile();
            CfgDataUtil.addRecentToConfig(OsUtil.getApplicationName(),_file.getAbsolutePath());
        }
        else
        {
            if(_cancel!=null)_cancel.run();
            return;
        }
        
        if(_file != null)
        {
            CfgDataUtil.setLastDirToConfig(OsUtil.getApplicationName(),_file.getParentFile());
            if(_ok!=null)_ok.execute(_file, _j.getSelectedType());
        }
    }
    
    public static void executeFileOpenChooser(Component _modal, String _title, String _action, File _lastdir, File _lastfile, Executable<File> _ok, String _type, String... _exts)
    {
        executeFileOpenChooser(_modal,_title, _action,_lastdir, _lastfile,_ok,
                createSimpleFileNameFilter(_type, _exts));
    }
    
    public static void executeFileOpenChooser(Component _modal, String _title, String _action, File _lastdir, File _lastfile, Executable<File> _ok, FileNameExtensionFilter... _filter)
    {
        executeFileOpenChooser(_modal,_title, _action,_lastdir, _lastfile,_ok,null, _filter);
    }
    
    public static void executeFileOpenChooser(Component _modal, String _title, String _action, File _lastdir, File _lastfile, Executable<File> _ok, Runnable _cancel, String _type, String... _exts)
    {
        executeFileOpenChooser(_modal,_title, _action,_lastdir, _lastfile,_ok,_cancel,
                createSimpleFileNameFilter(_type, _exts));
    }
    
    public static FileNameExtensionFilter createSimpleFileNameFilter(String _type, String... _exts)
    {
        return new FileNameExtensionFilter(_type+"(*."+StringUtil.join(_exts,", *.")+")",_exts);
    }
    
    @SneakyThrows
    public static void executeFileOpenChooser(Component _modal, String _title, String _action, File _lastdir, File _lastfile, Executable<File> _ok, Runnable _cancel, FileNameExtensionFilter... _filter)
    {
        JFileChooser _j = createFileChooser(_title, _action, OsUtil.getApplicationName(),
                CfgDataUtil.getLastDirFromConfig(OsUtil.getApplicationName(), _lastdir==null ? null : _lastdir.getAbsolutePath()),
                true, _filter);
        
        if(_lastfile != null)
        {
            _j.setCurrentDirectory(_lastfile.getParentFile());
            _j.setSelectedFile(_lastfile);
        }
        
        File _file = null;
        int _opt = _j.showOpenDialog(_modal);
        if(_opt == JFileChooser.APPROVE_OPTION)
        {
            _file = _j.getSelectedFile();
            CfgDataUtil.addRecentToConfig(OsUtil.getApplicationName(),_file.getAbsolutePath());
        }
        else
        {
            if(_cancel!=null)_cancel.run();
            return;
        }
        
        if(_file != null)
        {
            CfgDataUtil.setLastDirToConfig(OsUtil.getApplicationName(),_file.getParentFile());
            if(_ok!=null)_ok.execute(_file);
        }
    }
    
    public static <T> void executeFileAndTypeOpenChooser(Component _modal, String _title, String _action, File _lastdir, File _lastfile, Executable2<File,T> _ok, Map<String,T> _types, String _type, String... _exts)
    {
        executeFileAndTypeOpenChooser(_modal,_title, _action,_lastdir, _lastfile,_ok,_types,
                createSimpleFileNameFilter(_type, _exts));
    }
    
    public static <T> void executeFileAndTypeOpenChooser(Component _modal, String _title, String _action, File _lastdir, File _lastfile, Executable2<File,T> _ok, Map<String,T> _types, FileNameExtensionFilter... _filter)
    {
        executeFileAndTypeOpenChooser(_modal,_title, _action,_lastdir, _lastfile,_ok, null, _types, _filter);
    }
    
    public static <T> void executeFileAndTypeOpenChooser(Component _modal, String _title, String _action, File _lastdir, File _lastfile, Executable2<File,T> _ok, Runnable _cancel, Map<String,T> _types, String _type, String... _exts)
    {
        executeFileAndTypeOpenChooser(_modal,_title, _action,_lastdir, _lastfile,_ok,_cancel, _types,
                createSimpleFileNameFilter(_type, _exts));
    }
    
    @SneakyThrows
    public static <T> void executeFileAndTypeOpenChooser(Component _modal, String _title, String _action, File _lastdir, File _lastfile, Executable2<File,T> _ok, Runnable _cancel, Map<String,T> _types, FileNameExtensionFilter... _filter)
    {
        JFileAndTypeChooser<T> _j = (JFileAndTypeChooser)createFileChooser(_title, _action, OsUtil.getApplicationName(),
                CfgDataUtil.getLastDirFromConfig(OsUtil.getApplicationName(), _lastdir==null ? null : _lastdir.getAbsolutePath()),
                true, _filter);
        
        _j.setTypes(_types);
        
        if(_lastfile != null)
        {
            _j.setCurrentDirectory(_lastfile.getParentFile());
            _j.setSelectedFile(_lastfile);
        }
        
        File _file = null;
        int _opt = _j.showOpenDialog(_modal);
        if(_opt == JFileChooser.APPROVE_OPTION)
        {
            _file = _j.getSelectedFile();
            CfgDataUtil.addRecentToConfig(OsUtil.getApplicationName(),_file.getAbsolutePath());
        }
        else
        {
            if(_cancel!=null)_cancel.run();
            return;
        }
        
        if(_file != null)
        {
            CfgDataUtil.setLastDirToConfig(OsUtil.getApplicationName(),_file.getParentFile());
            if(_ok!=null)_ok.execute(_file, _j.getSelectedType());
        }
    }
    
    public static void executeDirectoryChooser(Component _modal, String _title, String _action, File _lastdir, Executable<File> _ok)
    {
        executeDirectoryChooser(_modal,_title,_action,_lastdir,_ok, null);
    }
    
    @SneakyThrows
    public static void executeDirectoryChooser(Component _modal, String _title, String _action, File _lastdir, Executable<File> _ok, Runnable _cancel)
    {
        JFileChooser _j = createDirectoryChooser(_title, _action, OsUtil.getApplicationName(),
                CfgDataUtil.getLastDirFromConfig(OsUtil.getApplicationName(), _lastdir==null ? null : _lastdir.getAbsolutePath()));
        
        File _file = null;
        int _opt = _j.showDialog(_modal,_action);
        if(_opt == JFileChooser.APPROVE_OPTION)
        {
            _file = _j.getSelectedFile();
            CfgDataUtil.addRecentToConfig(OsUtil.getApplicationName(),_file.getAbsolutePath());
        }
        else
        {
            if(_cancel!=null)_cancel.run();
            return;
        }
        
        if(_file != null)
        {
            CfgDataUtil.setLastDirToConfig(OsUtil.getApplicationName(),_file.getParentFile());
            if(_ok!=null)_ok.execute(_file);
        }
    }
    
    public static void runLater(Runnable _run)
    {
        SwingUtilities.invokeLater(_run);
    }

    public static void run(Runnable _run)
    {
        new Thread(_run).start();
    }
    
    private static RenderingHints FAST_RENDER_HINTS = new RenderingHints(null);
    private static RenderingHints BEAUTIFUL_RENDER_HINTS = new RenderingHints(null);
    static {
        FAST_RENDER_HINTS.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
        FAST_RENDER_HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        FAST_RENDER_HINTS.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        FAST_RENDER_HINTS.put(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DISABLE);
        FAST_RENDER_HINTS.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        FAST_RENDER_HINTS.put(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        FAST_RENDER_HINTS.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
        FAST_RENDER_HINTS.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED );
        FAST_RENDER_HINTS.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
        BEAUTIFUL_RENDER_HINTS.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        BEAUTIFUL_RENDER_HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        BEAUTIFUL_RENDER_HINTS.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        BEAUTIFUL_RENDER_HINTS.put(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_ENABLE);
        BEAUTIFUL_RENDER_HINTS.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        BEAUTIFUL_RENDER_HINTS.put(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        BEAUTIFUL_RENDER_HINTS.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
        BEAUTIFUL_RENDER_HINTS.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );
        BEAUTIFUL_RENDER_HINTS.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    }
    
    /** performs the annoying calculation required for centering text in a box of size d.
     * Try to reuse this result as calculating string bounds is not terribly efficient.
     * */
    public static Point2D getCenteredText(Graphics _g, int _w, int _h, Font _f, String _s)
    {
        return getCenteredText(_g,new Dimension(_w,_h),_f, _s);
    }
    public static Point2D getCenteredText( Graphics _g, Dimension _d, Font _f, String _s )
    {
        FontMetrics _fm   = _g.getFontMetrics(_f);
        Rectangle2D _rect = _fm.getStringBounds(_s, _g);
        
        // Center text horizontally and vertically
        double _x = (_d.getWidth()  - _rect.getWidth())  / 2;
        double _y = (_d.getHeight() - _rect.getHeight()) / 2  + _fm.getAscent();
        
        return new Point2D.Double( _x, _y );
    }
    
    /** Sets the given graphics object to draw with anti-aliasing and other beautiful options. */
    public static void setBeautifulRendering( Graphics2D g ) {
        g.setRenderingHints(BEAUTIFUL_RENDER_HINTS);
    }
    /** Sets the given graphics object to draw with options that will probably go as fast as possible. */
    public static void setPerformaceRendering( Graphics2D g ) {
        g.setRenderingHints(FAST_RENDER_HINTS);
    }
}
