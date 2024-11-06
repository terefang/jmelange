package com.github.terefang.jmelange.swing.html;

import com.github.terefang.jmelange.apache.io.IOUtils;
import com.github.terefang.jmelange.apache.lang3.StringEscapeUtils;
import com.github.terefang.jmelange.swing.SwingHelper;
import com.github.terefang.jmelange.swing.html.url.internal.Handler;
import de.milchreis.uibooster.UiBooster;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jdesktop.swingx.JXTitledPanel;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
public class HtmlViewPane extends JXTitledPanel implements HyperlinkListener
{
    static {
        Handler.register();
    }
    private final JTextField findText;
    private final HTMLDocument htmlDoc;
    JScrollPane scrollPane;
    JEditorPane htmlView;
    HTMLEditorKit editorKit;
    private int searchIndex = -1;
    
    String _id;
    
    @SneakyThrows
    public HtmlViewPane(String _title)
    {
        super(_title);
        this._id = UUID.randomUUID().toString();
        this.scrollPane = new JScrollPane(this.htmlView = new JEditorPane());
        this.htmlView.setEditorKit(this.editorKit = new HTMLEditorKit());
        this.htmlDoc = (HTMLDocument) this.editorKit.createDefaultDocument();
        this.htmlDoc.setBase(new URL("internal://"+_id));
        this.htmlView.setDocument(this.htmlDoc);
        this.htmlView.setEditable(false);
        this.htmlView.addHyperlinkListener(this);
        
        //System.err.println(this.editorKit.getStyleSheet().toString());
        
        JPanel _hPanel = SwingHelper.createHxBox();
        _hPanel.add(this.findText = SwingHelper.createTextField("... to find ...",200));
        _hPanel.add(SwingHelper.createButton("Find",()->{ this.makeFind(); }));
        _hPanel.add(SwingHelper.createButton("Top",()->{ this.makeTop(); }));
        _hPanel.add(SwingHelper.createButton("Dump",()->{ System.err.println(this.htmlView.getText()); }));
        
        this.setRightDecoration(_hPanel);
        this.add(this.scrollPane);
    }
    
    private void makeTop()
    {
        this.htmlView.scrollRectToVisible(new Rectangle(0,0,0,0));
        this.htmlView.setCaretPosition(0);
        this.searchIndex = 0;
    }
    
    private void makeFind()
    {
        SwingUtilities.invokeLater(()->{
            if(this.searchIndex < 0) this.searchIndex = 0;
            boolean _useRX = false;
            String  _search   = this.findText.getText().toLowerCase();
            Pattern _searchRX = null;
            
            if(_search.startsWith("~"))
            {
                _useRX = true;
                _searchRX = Pattern.compile(this.findText.getText().substring(1), Pattern.CASE_INSENSITIVE);
            }
            
            try
            {
                HTMLDocument    _doc = (HTMLDocument)this.htmlView.getDocument();
                ElementIterator _eli = new ElementIterator(_doc);
                while(_eli.next() != null)
                {
                    Element _e = _eli.current();
                    if(_e instanceof AbstractDocument.LeafElement)
                    {
                        String _text = this.htmlView.getText(_e.getStartOffset(), _e.getEndOffset()-_e.getStartOffset());
                        if(this.searchIndex<_e.getStartOffset() && (_useRX ? _searchRX.matcher(_text).find():_text.toLowerCase().contains(_search)))
                        {
                            Rectangle _r   = this.htmlView.modelToView(_e.getStartOffset());
                            if (_r != null)
                            {
                                Rectangle _vis = this.htmlView.getVisibleRect();
                                _r.height = _vis.height;
                                this.htmlView.scrollRectToVisible(_r);
                                this.htmlView.setCaretPosition(_e.getStartOffset());
                                this.htmlView.getHighlighter().removeAllHighlights();
                                this.htmlView.getHighlighter().addHighlight(_e.getStartOffset(), _e.getEndOffset(), DefaultHighlighter.DefaultPainter);
                                this.searchIndex = _e.getEndOffset();
                            }
                            break;
                        }
                    }
                }
            }
            catch (BadLocationException _e)
            {
                //IGNORE
            }
        });
    }
    
    @SneakyThrows
    public void loadStyleSheet(URL _uriCss)
    {
        this.loadStyleSheet(_uriCss, false);
    }
    
    @SneakyThrows
    public void loadStyleSheet(URL _uriCss, boolean _reset)
    {
        try
        {
            if(_reset) this.editorKit.setStyleSheet(new StyleSheet());
            
            this.editorKit.getStyleSheet().loadRules(new StringReader(IOUtils.toString(_uriCss)), _uriCss);
        }
        catch (Exception _xe)
        {
            //ignore
        }
    }
    
    @SneakyThrows
    public void loadDocument(String _content)
    {
        try
        {
            this.htmlView.setText(_content);
        }
        catch (Exception _xe)
        {
            this.htmlView.setText("<pre>"+ StringEscapeUtils.escapeHtml4(_xe.getMessage())+"</pre>");
        }
        finally
        {
            makeTop();
        }
    }
    
    @SneakyThrows
    public void loadDocument(URL _url)
    {
        this.loadDocument(IOUtils.toString(_url),_url);
    }
    
    @SneakyThrows
    public void loadDocument(File _file)
    {
        this.loadDocument(_file.toURL());
    }
    
    @SneakyThrows
    public void loadDocument(String _content, URL _url)
    {
        this.setBaseFromUrl(_url);
        this.loadDocument(_content);
    }
    
    @SneakyThrows
    public void loadDocument(String _content, String _classpath)
    {
        this.setBaseFromClasspath(_classpath);
        this.loadDocument(_content);
    }
    
    @SneakyThrows
    public void setBaseFromClasspath(String _path)
    {
        if(!_path.startsWith("/"))
        {
            _path = "/"+_path;
        }
        this.htmlDoc.setBase(new URL("internal://"+_id+_path));
    }
    
    @SneakyThrows
    public void setBaseFromUrl(URL _url)
    {
        this.htmlDoc.setBase(_url);
    }
    
    @Override
    public void hyperlinkUpdate(HyperlinkEvent e)
    {
        if(e.getEventType()== HyperlinkEvent.EventType.ACTIVATED)
        {
            if(e.getURL()==null && e.getDescription()!=null && e.getDescription().startsWith("#"))
            {
                this.scrollToId(e.getDescription().substring(1));
            }
            else
            if(e.getURL().getProtocol().equalsIgnoreCase(this.htmlDoc.getBase().getProtocol()) && e.getURL().getPath().equalsIgnoreCase(this.htmlDoc.getBase().getPath()) && e.getURL().getRef()!=null)
            {
                this.scrollToId(e.getURL().getRef());
            }
            else
            if(e.getURL()!=null && Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
            {
                try
                {
                    new UiBooster(SwingHelper.makeUiBoosterOptions()).showConfirmDialog("Do you really want to open "+e.getURL().toString(), "CONFIRM", ()->{
                        try
                        {
                            Desktop.getDesktop()
                                    .browse(e.getURL()
                                            .toURI());
                        }
                        catch (Exception _xe)
                        {
                            log.error(_xe.getMessage(),_xe);
                        }
                    }, null);
                }
                catch(Exception _xe){ log.error(_xe.getMessage(),_xe);}
            }
        }
    }
    
    private void scrollToId(final String _id)
    {
        SwingUtilities.invokeLater(()->{
            try
            {
                HTMLDocument _doc = (HTMLDocument)this.htmlView.getDocument();
                Element      _el  = _doc.getElement(_id);
                if(_el==null) return;
                int       _pos = _el.getStartOffset();
                Rectangle _r   = this.htmlView.modelToView(_pos);
                if (_r != null)
                {
                    Rectangle _vis = this.htmlView.getVisibleRect();
                    _r.height = _vis.height;
                    this.htmlView.scrollRectToVisible(_r);
                    this.htmlView.setCaretPosition(_pos);
                }
            }
            catch (BadLocationException ble)
            {
                UIManager.getLookAndFeel().provideErrorFeedback(this.htmlView);
            }
        });
        
    }
}
