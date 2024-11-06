package com.github.terefang.jmelange.swing.edit;

import com.github.terefang.jmelange.commons.lang.Executable;
import com.github.terefang.jmelange.swing.SwingHelper;
import com.google.common.io.Files;
import com.jidesoft.swing.JideTabbedPane;
import lombok.SneakyThrows;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.Painter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;


public class BasicTitledEditorPane
        extends JXTitledPanel
{
    private boolean changed;
    private JLabel _state_label;

    private BasicEditorPane wrapPanel;
    
    public BasicEditorPane getWrapPanel()
    {
        return wrapPanel;
    }
    
    
    public Map<String, RSyntaxTextArea> getEditors()
    {
        return getWrapPanel().getEditors();
    }
    
    private JXPanel actionPanel;
    
    public JXPanel getActionPanel()
    {
        return actionPanel;
    }
    
    public BasicTitledEditorPane(String title)
    {
        this(title, false);
    }
    
    public BasicTitledEditorPane(String title, boolean _showTA)
    {
        super(title);
        setTitleForeground(Color.WHITE);
        setTitlePainter(new Painter()
        {
            @Override
            public void paint(Graphics2D g, Object object, int width, int height)
            {
                g = (Graphics2D)g.create();
                g.setBackground(Color.BLACK);
                g.fillRect(0,0,width,height);
            }
        });
        setOpaque(true);
    
        this.createPanels(_showTA);
    }
    
    private void createPanels(boolean _showTA)
    {
        this.add(this.wrapPanel = new BasicEditorPane(_showTA));
        this.createActionPanel((n)->{ this.setTitle(n); });
        
        this.wrapPanel.addUpdateListener(()->{
            this.changed=true;
            updateLabel();
        });
    }
    
    private void createActionPanel(Executable<String> _callback)
    {
        this.actionPanel = new JXPanel();
        this.actionPanel.setLayout(new BoxLayout(this.actionPanel, BoxLayout.LINE_AXIS));
        this.actionPanel.setBackground(Color.BLACK);
        
        this.actionPanel.add(this._state_label = SwingHelper.createLabel("-", 200,-1,Color.WHITE,Color.BLACK));
        this.actionPanel.add(SwingHelper.createButton("Load", ()->{ this.handleLoad(_callback);}));
        this.actionPanel.add(SwingHelper.createButton("Save", ()->{ this.handleSave(_callback,true);}));
        this.actionPanel.add(SwingHelper.createButton("Save As", ()->{this.handleSave(_callback,false);}));

        this.setRightDecoration(this.actionPanel);
    }
    
    @SneakyThrows
    public void handleLoad(Executable<String> _callback)
    {
        int _idx = this.getTabPanel().getSelectedIndex();
        String _name = this.getTabPanel().getTitleAt(_idx);
        if(_idx==0)
        {
            this.wrapPanel.handleLoad(_callback);
            this.changed=false;
            updateLabel();
        }
        else if(this.getEditors().containsKey(_name))
        {
            RSyntaxTextArea _area = this.getEditors().get(_name);
            SwingHelper.executeFileOpenChooser(this, "Select Source File ...","Load",null,null,
                    (f)->{
                        try
                        {
                            _area.setText(Files.toString( f, StandardCharsets.UTF_8));
                        }
                        catch (IOException _e)
                        {
                            throw new RuntimeException(_e);
                        }
                    },
                    null,
                    this.getFileExt().get(_name) );
        }
        else if(this.getLoaders().containsKey(_name))
        {
            SwingHelper.executeFileSaveChooser(this, "Select Source File ...","Load",null,null,
                    (f)->{ this.getLoaders().get(_name).execute(f); },
                    null,
                    this.getFileExt().get(_name) );
        }
    }
    
    @SneakyThrows
    private void handleSave(Executable<String> _callback, boolean _sameFile)
    {
        int _idx = this.getTabPanel().getSelectedIndex();
        String _name = this.getTabPanel().getTitleAt(_idx);
        if(_idx==0)
        {
            this.wrapPanel.handleSave(_callback, _sameFile);
            this.changed=false;
            updateLabel();
        }
        else if(this.getEditors().containsKey(_name))
        {
            RSyntaxTextArea _area = this.getEditors().get(_name);
            SwingHelper.executeFileSaveChooser(this, "Select Destination File ...","Save",null,null,
                    (f)->{
                        try
                        {
                            Files.write(_area.getText(), f, StandardCharsets.UTF_8);
                        }
                        catch (IOException _e)
                        {
                            throw new RuntimeException(_e);
                        }
                    },
                    null,
                    this.getFileExt().get(_name) );
        }
        else if(this.getSavers().containsKey(_name))
        {
            SwingHelper.executeFileSaveChooser(this, "Select Destination File ...","Save",null,null,
                    (f)->{ this.getSavers().get(_name).execute(f); },
                    null,
                    this.getFileExt().get(_name) );
        }
    }
    
    public void load(File _infile)
    {
        wrapPanel.load(_infile, null);
    }
    
    public void load(File _infile, String _default)
    {
        wrapPanel.load(_infile, _default);
    }
    
    public void updateLabel()
    {
        if(!this.changed) {
            this._state_label.setText("-");
        } else {
            this._state_label.setText("(unsaved)");
        }
    }
    
    public JXPanel getNorthPanel()
    {
        return this.getWrapPanel().getNorthPanel();
    }
    public JXPanel getSouthPanel()
    {
        return this.getWrapPanel().getSouthPanel();
    }
    public JideTabbedPane getTabPanel()
    {
        return this.getWrapPanel().getTabPanel();
    }
    public RSyntaxTextArea getSourceArea()
    {
        return this.getWrapPanel().getSourceArea();
    }
    
    public void setExtensions(FileNameExtensionFilter[] _ext)
    {
        this.getWrapPanel().setExtensions(_ext);
    }
    
    Map<String, Executable<File>> _loaders = new HashMap<>();
    Map<String, Executable<File>> _savers = new HashMap<>();
    
    public Map<String, Executable<File>> getLoaders()
    {
        return _loaders;
    }
    
    public Map<String, Executable<File>> getSavers()
    {
        return _savers;
    }
    Map<String,FileNameExtensionFilter[]> _filext = new HashMap<>();
    public Map<String, FileNameExtensionFilter[]> getFileExt()
    {
        return _filext;
    }
    
    public Callable<String> getStopwordsProvider()
    {
        return this.wrapPanel.getStopwordsProvider();
    }
    
    public void setStopwordsProvider(Callable<String> _stopwordsProvider)
    {
        this.wrapPanel.setStopwordsProvider(_stopwordsProvider);
    }
    
}
