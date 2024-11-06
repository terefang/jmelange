package com.github.terefang.jmelange.swing.edit;

import com.alexandriasoftware.swing.JSplitButton;
import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.*;
import com.github.terefang.jmelange.commons.lang.Executable;
import com.github.terefang.jmelange.swing.SwingHelper;
import com.github.terefang.jmelange.swing.easylayout.Constraint;
import com.google.common.io.Files;
import com.ibm.icu.text.Transliterator;
import com.jidesoft.swing.JideTabbedPane;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.fife.rsta.ui.search.FindDialog;
import org.fife.rsta.ui.search.ReplaceDialog;
import org.fife.rsta.ui.search.SearchEvent;
import org.fife.rsta.ui.search.SearchListener;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;
import java.util.concurrent.Callable;

@Slf4j
public class BasicEditorPane
        extends JXPanel
        implements DocumentListener, SearchListener
{
    private JPanel centerPanel;
    private JXPanel northPanel;
    private JXPanel southPanel;
    private RSyntaxTextArea sourceArea;
    private JToggleButton _ignore_case;
    private JToggleButton _use_regex;
    private JToggleButton _words_only;
    private JToggleButton _selection_only;
    private JTextField _find_text;
    private JTextField _replace_text;
    private FindDialog _fdlg;
    private ReplaceDialog _rdlg;
    private boolean changed;
    
    public boolean isChanged()
    {
        return changed;
    }
    
    public JPanel getCenterPanel()
    {
        return centerPanel;
    }
    
    public JXPanel getNorthPanel()
    {
        return northPanel;
    }
    
    public JXPanel getSouthPanel()
    {
        return southPanel;
    }
    
    public RSyntaxTextArea getSourceArea()
    {
        return sourceArea;
    }
    
    public JideTabbedPane getTabPanel()
    {
        return tabPanel;
    }
    
    private JPanel _check_panel;
    private Vector<JCheckBox> _cv_check;
    private LinkedHashMap<String, List<String>> _cv_set;
    private JideTabbedPane tabPanel;
    
    public BasicEditorPane()
    {
        this(false);
    }
    public BasicEditorPane(boolean _showTA)
    {
        super();
        this.setLayout(new BorderLayout());
        this.createPanels(_showTA);
    }
    
    public static final String _EDITOR = "Editor";
    
    Map<String,RSyntaxTextArea> _editors = new HashMap<>();
    
    public Map<String, RSyntaxTextArea> getEditors()
    {
        return _editors;
    }
    
    
    private void createPanels(boolean _showTA)
    {
        this.centerPanel = SwingHelper.createEGrid(1,1);
        this.add(this.centerPanel, BorderLayout.CENTER);
        
        this.northPanel = new JXPanel();
        this.northPanel.setLayout(new BoxLayout(this.northPanel, BoxLayout.PAGE_AXIS));
        this.add(this.northPanel, BorderLayout.NORTH);
        
        this.southPanel = new JXPanel();
        this.southPanel.setLayout(new BoxLayout(this.southPanel, BoxLayout.PAGE_AXIS));
        this.add(this.southPanel, BorderLayout.SOUTH);
        
        this.tabPanel = new JideTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        this.tabPanel.setTabShape(JideTabbedPane.SHAPE_WINDOWS_SELECTED);
        this.tabPanel.setColorTheme(JideTabbedPane.COLOR_THEME_WINXP);
        
        JPanel _grid = SwingHelper.createEGrid(1, 1);
        _grid.add(new RTextScrollPane(this.sourceArea = new RSyntaxTextArea(20,20)));
        this.sourceArea.setEditable(true);
        this.sourceArea.setAutoIndentEnabled(true);
        this.sourceArea.setFont(SwingHelper.createEditFont(14f));
        this.sourceArea.getDocument().addDocumentListener(this);
        this.tabPanel.addTab(_EDITOR,_grid);
        this.getEditors().put(_EDITOR, this.sourceArea);
        
        JXPanel _panel = new JXPanel();
        _panel.setLayout(new BoxLayout(_panel, BoxLayout.LINE_AXIS));
        
        _panel.add(SwingHelper.createIdeButton("A↑", ()-> {
            this.sourceArea.setFont(this.sourceArea.getFont().deriveFont(this.sourceArea.getFont().getSize2D()+2f));
        }));
        _panel.add(SwingHelper.createIdeButton("A↓", ()-> {
            this.sourceArea.setFont(this.sourceArea.getFont().deriveFont(this.sourceArea.getFont().getSize2D()-2f));
        }));
        this.tabPanel.setTabTrailingComponent(_panel);
        
        this.centerPanel.add(this.tabPanel, Constraint.from(0,0, Constraint.Alignment.FF));
        
        createSubActions(_showTA);
    }
    
    static FileNameExtensionFilter[] _exts = {
            new FileNameExtensionFilter("Text/Markdown Files (*.txt, *.md, *.markdown)", "txt", "md", "markdown"),
    };
    
    public FileNameExtensionFilter[] getExtensions() {
        if(_extensions==null)
            return _exts;
        return _extensions;
    }
    
    public void setExtensions(FileNameExtensionFilter[] _ext)
    {
        _extensions = _ext;
    }
    
    FileNameExtensionFilter[] _extensions;
    
    @SneakyThrows
    public void handleLoad(Executable<String> _callback)
    {
        JFileChooser _j = SwingHelper.createFileChooser("Select Document ...", "Select",
                OsUtil.getApplicationName(),
                CfgDataUtil.getLastDirFromConfig(OsUtil.getApplicationName(), null),
                true, getExtensions());
        
        File _file = (File) this.getClientProperty(File.class);
        
        if(_file != null) _j.setCurrentDirectory(_file.getParentFile());
        
        int _opt = _j.showOpenDialog(this);
    
        if(_opt == JFileChooser.APPROVE_OPTION)
        {
            this.load(_j.getSelectedFile(), null);
            _callback.execute(_j.getSelectedFile().getName());
        }
        updateLabel();
    }
    
    @SneakyThrows
    public void handleSave(Executable<String> _callback, boolean _sameFile) {
        this.changed = false;
        
        File _file = (File) this.getClientProperty(File.class);
        
        if(!_sameFile) {
            JFileChooser _j = SwingHelper.createFileChooser("Save Document ...", "Sase",
                    OsUtil.getApplicationName(),
                    CfgDataUtil.getLastDirFromConfig(OsUtil.getApplicationName(), null),
                    true, getExtensions());
            
            if(_file != null) {
                _j.setCurrentDirectory(_file.getParentFile());
                _j.setSelectedFile(_file);
            }
            
            int _opt = _j.showSaveDialog(this);
            if(_opt == JFileChooser.APPROVE_OPTION) {
                this.putClientProperty(File.class, _j.getSelectedFile());
                _file = _j.getSelectedFile();
                CfgDataUtil.addRecentToConfig(OsUtil.getApplicationName(),_file.getAbsolutePath());
            } else {
                return;
            }
        }
        
        if(_file != null)
        {
            CfgDataUtil.setLastDirToConfig(OsUtil.getApplicationName(),_file.getParentFile());
            Files.write(this.sourceArea.getText(), _file, StandardCharsets.UTF_8);
            this.setName(_file.getName());
            _callback.execute(this.getName());
            updateLabel();
            this.changed = true;
        }
    }
    
    private void createSubActionFindReplace(JPanel tmpPanel)
    {
        JPanel _panel = SwingHelper.createHxBox("Find/Replace");
        
        _panel.add(this._ignore_case = SwingHelper.createToggleButton("cC", 40));
        _panel.add(this._use_regex = SwingHelper.createToggleButton(".*", 40));
        _panel.add(this._words_only = SwingHelper.createToggleButton("W", 40));
        _panel.add(this._selection_only = SwingHelper.createToggleButton("{}", 40));
        
        _panel.add(SwingHelper.createLabel(" | F:"));
        _panel.add(this._find_text = SwingHelper.createTextField("find", 150));
        _panel.add(SwingHelper.createLabel(" R:"));
        _panel.add(this._replace_text = SwingHelper.createTextField("replace", 150));
        
        _panel.add(SwingHelper.setLeftRightMargin(SwingHelper.createButton("F↓", 30 ,()->{ this.makeFind(true);}),2));
        _panel.add(SwingHelper.setLeftRightMargin(SwingHelper.createButton("F↑", 30 ,()->{ this.makeFind(false);}),2));
        _panel.add(SwingHelper.setLeftRightMargin(SwingHelper.createButton("Rp", 30 ,()->{ this.makeReplace(false);}),2));
        _panel.add(SwingHelper.setLeftRightMargin(SwingHelper.createButton("RpA", 40 ,()->{ this.makeReplace(true);}),2));
        _panel.add(new JLabel("|"));
        _panel.add(SwingHelper.createButton("Undo", 60 ,()->{ this.sourceArea.undoLastAction();}));
        
        
        this._fdlg = new FindDialog((Dialog) null, this);
        this._fdlg.setVisible(false);
        
        this._rdlg = new ReplaceDialog((Dialog) null, this);
        this._rdlg.setSearchContext(this._fdlg.getSearchContext());
        this._rdlg.setVisible(false);
        
        if(this.sourceArea.getPopupMenu()==null)
        {
            this.sourceArea.setPopupMenu(new JPopupMenu());
        }
        else
        {
            this.sourceArea.getPopupMenu().addSeparator();
        }
        this.sourceArea.getPopupMenu().add(SwingHelper.createMenuItem("Find...", ()->{
                this._fdlg.setVisible(true);
                this._rdlg.setVisible(false);
        }));
        this.sourceArea.getPopupMenu().add(SwingHelper.createMenuItem("Replace...", ()->{
                this._fdlg.setVisible(false);
                this._rdlg.setVisible(true);
        }));
        
        tmpPanel.add(_panel);
    }
    
    private void createSubActionTextStringCases(JPanel _panel)
    {
        _panel.add(SwingHelper.createSplitButton("Case", null,null, -1,
                "De-Accent", ()->{ this.handleRemoveAccents(); },
                "---1", null,
                "LowerCase", ()->{ this.makeCase(CaseMode._LOWER_CASE); },
                "UpperCase", ()->{  this.makeCase(CaseMode._UPPER_CASE); },
                "Kebab-Case", ()->{ this.makeCase(CaseMode._KEBAB_CASE); },
                "Snake_Case", ()->{ this.makeCase(CaseMode._SNAKE_CASE); },
                "camelCase", ()->{ this.makeCase(CaseMode._CAMEL_CASE); },
                "PascalCase", ()->{ this.makeCase(CaseMode._PASCAL_CASE); }
        ),Constraint.next(Constraint.Alignment.FC));
    }
    
    private void createSubActionTextStringLines(JPanel _panel)
    {
        _panel.add(SwingHelper.createSplitButton("Lines", null, null, -1,
                "Sort", ()->{ this.makeSortLines(SortMode._MODE_SORT); },
                "Sort Reverse", ()->{ this.makeSortLines(SortMode._MODE_SORT_REVERSE); },
                "Reverse", ()->{ this.makeSortLines(SortMode._MODE_REVERSE); },
                "---1", null,
                "Shuffle", ()->{ this.makeSortLines(SortMode._MODE_SHUFFLE); },
                "Unique", ()->{ this.makeSortLines(SortMode._MODE_UNIQ); },
                "---2", null,
                "Quote \"L\"", ()->{ this.makeQuoteLines("\"");},
                "Quote 'L'", ()->{ this.makeQuoteLines("'"); },
                "---3", null,
                "Initials", ()->{ this.makeInitials(); }
        ),Constraint.next(Constraint.Alignment.FC));
    }
    
    private void createSubActionTextStringWords(JPanel _panel)
    {
        _panel.add(SwingHelper.createSplitButton("Words", null, null, -1,
                "Sort", ()->{this.makeSortWords(SortMode._MODE_SORT); },
                "Sort Reverse", ()->{this.makeSortWords(SortMode._MODE_SORT_REVERSE);},
                "Reverse", ()->{this.makeSortWords(SortMode._MODE_REVERSE);},
                "---1", null,
                "Shuffle", ()->{this.makeSortWords(SortMode._MODE_SHUFFLE);},
                "Unique", ()->{this.makeSortWords(SortMode._MODE_UNIQ); },
                "---2", null,
                "Quote \"W\"", ()->{this.makeQuoteWords("\"");},
                "Quote 'W'", ()->{this.makeQuoteWords("'"); }
        ),Constraint.next(Constraint.Alignment.FC));
    }
    
    private void createSubActionTextStringFilter(JPanel _panel)
    {
        _panel.add(SwingHelper.createSplitButton("Filter", null,null, -1,
                "Space→Tabs", ()->{ this.sourceArea.convertSpacesToTabs(); },
                "Tabs→Space", ()->{  this.sourceArea.convertTabsToSpaces(); },
                "Ws→Space", ()->{  this.makeWsTo( true, false); },
                "Ws→Linefeed", ()->{  this.makeWsTo( false, false); },
                "Ws→WordWrap", ()->{  this.makeWsTo(true, true); },
                "Remove Punctuation", ()->{  this.makeRemovePunctuation(); },
                "Remove Stopwords", ()->{  this.makeRemoveStopwords(); }
        ),Constraint.next(Constraint.Alignment.FC));
    }
    
    JPanel textActions;
    
    public JPanel getTextActions()
    {
        return textActions;
    }
    
    private void createSubActionTextStrings(JPanel tmpPanel)
    {
        this.textActions = SwingHelper.createHGrid(5,"Text/String Actions");
        
        createSubActionTextStringFilter(this.textActions);
        createSubActionTextStringCases(this.textActions);
        createSubActionTextStringWords(this.textActions);
        createSubActionTextStringLines(this.textActions);
        createSubActionTextStringTransliterate(this.textActions);
        
        tmpPanel.add(this.textActions);
        
    }
    
    private void createSubActionTextStringTransliterate(JPanel _panel)
    {
        JSplitButton sButton = SwingHelper.createSplitButton("Transliterate", "Transliterate Text", null, 50);
        
        char  _lastChar = 0;
        JMenu _sbtn     = null;
        
        Enumeration<String> availableIDs = Transliterator.getAvailableIDs();
        List<String>        _ids         = new ArrayList<>();
        while (availableIDs.hasMoreElements()) {
            _ids.add(availableIDs.nextElement());
        }
        ListMapUtil.sort(_ids,true);
        
        for(final String _id : _ids)
        {
            if(_id.toLowerCase().startsWith("any-"))
            {
                char _c = _id.toUpperCase().charAt(4);
                if(_lastChar < _c || _sbtn == null)
                {
                    _lastChar = _c;
                    _sbtn = SwingHelper.createMenu(sButton.getPopupMenu(),"ICU4J / "+Character.toString(_c)+" ...");
                }
                SwingHelper.createPopupMenu(_sbtn.getPopupMenu(),_id, ()->{
                    this.handleTransliterateIcu4j(_id);
                });
            }
        }
        _panel.add(sButton,Constraint.next(Constraint.Alignment.FC));
    }
    
    private void handleTransliterateIcu4j(String _id)
    {
        if(this.getTabPanel().getSelectedIndex()!=0) return;
        
        int _start = this.getSourceArea().getSelectionStart();
        int _end = this.getSourceArea().getSelectionEnd();
        
        String _text = this.doGetText(_start,_end);
        _text = Transliterator.getInstance(_id).transliterate(_text);
        this.doSetText(_start,_end,_text);
    }
    
    private void createSubActions(boolean _showTA)
    {
        JPanel tmpPanel = new JPanel();
        tmpPanel.setLayout(new BoxLayout(tmpPanel, BoxLayout.Y_AXIS));
        createSubActionFindReplace(tmpPanel);
        if(_showTA)
        {
            createSubActionTextStrings(tmpPanel);
        }
        this.southPanel.add(tmpPanel);
    }
    
    
    private void makeFind(boolean _fw)
    {
        if(this.getTabPanel().getSelectedIndex()!=0) return;
        
        SearchContext _sc = new SearchContext(this._find_text.getText());
        _sc.setRegularExpression(this._use_regex.isSelected());
        _sc.setSearchForward(_fw);
        _sc.setMatchCase(!this._ignore_case.isSelected());
        _sc.setSearchWrap(true);
        _sc.setWholeWord(this._words_only.isSelected());
        _sc.setSearchSelectionOnly(this._selection_only.isSelected());
        SearchResult _res = SearchEngine.find(this.sourceArea, _sc);
        if(_res.getCount()==0)
        {
        
        }
    }
    
    private void makeReplace(boolean _all)
    {
        if(this.getTabPanel().getSelectedIndex()!=0) return;
        
        SearchContext _sc = new SearchContext(this._find_text.getText());
        _sc.setRegularExpression(this._use_regex.isSelected());
        _sc.setReplaceWith(this._replace_text.getText());
        _sc.setMatchCase(!this._ignore_case.isSelected());
        _sc.setWholeWord(this._words_only.isSelected());
        _sc.setSearchSelectionOnly(this._selection_only.isSelected());
        SearchResult _res = null;
        if(_all) {
            _res = SearchEngine.replaceAll(this.sourceArea, _sc);
        } else {
            _res = SearchEngine.replace(this.sourceArea, _sc);
        }
    }
    
    @Override
    public void searchEvent(SearchEvent searchEvent)
    {
        SearchEvent.Type type = searchEvent.getType();
        SearchContext context = searchEvent.getSearchContext();
        SearchResult result;
        
        switch (type) {
            default: // Prevent FindBugs warning later
            case MARK_ALL:
                result = SearchEngine.markAll(this.sourceArea, context);
                break;
            case FIND:
                result = SearchEngine.find(this.sourceArea, context);
                break;
            case REPLACE:
                result = SearchEngine.replace(this.sourceArea, context);
                break;
            case REPLACE_ALL:
                result = SearchEngine.replaceAll(this.sourceArea, context);
                break;
        }
    }
    
    @SneakyThrows
    public void load(File _infile, String _default)
    {
        this.putClientProperty(File.class, _infile);
        if(!_infile.exists())
        {
            if(_default == null)
            {
                this.getSourceArea().setText("");
            }
            else
            {
                this.getSourceArea().setText(_default);
            }
        }
        else
        {
            CfgDataUtil.setLastDirToConfig(OsUtil.getApplicationName(),_infile.getParentFile());
            CfgDataUtil.addRecentToConfig(OsUtil.getApplicationName(),_infile.getAbsolutePath());
            this.getSourceArea().setText(Files.toString(_infile, StandardCharsets.UTF_8));
            this.changed = false;
        }
    }
    
    
    enum SortMode { _MODE_SORT, _MODE_SORT_CASE, _MODE_SHUFFLE, _MODE_UNIQ, _MODE_SORT_REVERSE, _MODE_SORT_REVERSE_CASE, _MODE_REVERSE }
    
    enum CaseMode {
        _UPPER_CASE, // NUM OF WORDS IS BAD
        _LOWER_CASE, // num of words is bad
        _FIRST_CASE,
        _SNAKE_CASE, // num_of_words_is_bad
        _KEBAB_CASE,  // num-of-words-is-bad
        _PASCAL_CASE,   // NumOfWordsIsBad
        _CAMEL_CASE // numOfWordsIsBad
    }
    
    String makeRealCase(CaseMode _cm, String _text)
    {
        switch (_cm) {
            case _KEBAB_CASE:
                return(CommonUtil.toKebabCase(_text));
            case _SNAKE_CASE:
                return(CommonUtil.toSnakeCase(_text));
            case _CAMEL_CASE:
                return(CommonUtil.toCamelCase(_text,false,' ', '_', '-'));
            case _UPPER_CASE:
                return(CommonUtil.upperCase(_text));
            case _LOWER_CASE:
                return(CommonUtil.lowerCase(_text));
            case _PASCAL_CASE:
                return(CommonUtil.toCamelCase(_text,true,' ', '_', '-'));
            case _FIRST_CASE:
                return(CommonUtil.capitaliseAllWords(_text));
            default:
                return _text;
        }
    }
    
    @SneakyThrows
    private void makeCase(CaseMode _cm)
    {
        if(this.getTabPanel().getSelectedIndex()!=0) return;
        
        int _start = this.sourceArea.getSelectionStart();
        int _end = this.sourceArea.getSelectionEnd();
        doSetText(_start,_end,makeRealCase(_cm,doGetText(_start,_end)));
    }
    
    private void makeLetters()
    {
        if(this.getTabPanel().getSelectedIndex()!=0) return;
        
        int _start = this.sourceArea.getSelectionStart();
        int _end = this.sourceArea.getSelectionEnd();
        
        char [] _l = this.doGetText(_start, _end).toCharArray();
        Arrays.sort(_l);
        for(int _i = 1; _i<_l.length; _i++) {
            if(_l[_i-1]==_l[_i]) {
                _l[_i-1]=' ';
            }
        }
        this.doSetText(_start,_end, CommonUtil.join(_l, '\n'));
    }
    
    public void doSetText(int _start, int _end, String _s)
    {
        if(_start==_end)
        {
            this.sourceArea.setText(_s);
        }
        else
        {
            this.sourceArea.replaceRange(_s,_start, _end);
            this.sourceArea.setSelectionStart(_start);
            this.sourceArea.setSelectionEnd(_start+_s.length());
        }
    }
    
    @SneakyThrows
    public String doGetText(int _start, int _end)
    {
        if(_start==_end)
        {
            return this.sourceArea.getText();
        }
        else
        {
            return this.sourceArea.getText(_start, _end-_start);
        }
    }
    
    static Map<String, List<String>> _CVMAP = new LinkedHashMap<>();
    
    static {
        _CVMAP.put("DASH", ListMapUtil.toList("-","–","—"));
        _CVMAP.put("APOS", ListMapUtil.toList("'","`","'"));
        _CVMAP.put("SCH", ListMapUtil.toList("dh", "gh", "lh", "rh", "th"));
        _CVMAP.put("XCH", ListMapUtil.toList("ch", "kh", "ph", "sh"));
        _CVMAP.put("CN", ListMapUtil.toList("gn", "nd", "ng", "m", "n"));
        _CVMAP.put("CH", ListMapUtil.toList("ch", "dh", "gh", "kh", "lh", "ph", "rh", "sh", "th"));
        _CVMAP.put("CKQ", ListMapUtil.toList("ck", "qu", "qw", "kw", "q"));
        _CVMAP.put("VX", ListMapUtil.toList("ae", "oe", "ea", "oa", "ai", "ei", "oi", "ou", "uo"));
        _CVMAP.put("VV", ListMapUtil.toList("aa", "ee", "ii", "oo", "uu", "yy"));
        _CVMAP.put("VAEI", ListMapUtil.toList("a", "e", "i"));
        _CVMAP.put("VOUI", ListMapUtil.toList("o", "u", "i"));
        _CVMAP.put("VAE", ListMapUtil.toList("a", "e"));
        _CVMAP.put("VOU", ListMapUtil.toList("o", "u"));
        _CVMAP.put("VIY", ListMapUtil.toList("i", "y"));
        _CVMAP.put("V", ListMapUtil.toList("a","e","i","o","u","y"));
        _CVMAP.put("C", ListMapUtil.toList("b","c","d","f","g","h","j","k","l","m","n","p","q","r","s","t","v","w","x","z"));
    }
    
    private void makeRules()
    {
        StringBuilder _sb = new StringBuilder();
        _sb.append("\n");
        _sb.append("# - rules \n");
        List<String> _repls = new Vector<>();
        for(JCheckBox _k : this._cv_check) {
            if(_k.isSelected()) _repls.add(_k.getText());
        }
        
        for(String _rk : _repls) {
            List<String> _rl = this._cv_set.get(_rk);
            _sb.append(String.format("\"%s\" = [ \"%s\" ]\n", _rk , CommonUtil.join(_rl, "\" \"")));
        }
        
        this.sourceArea.append(_sb.toString());
    }
    
    private void handleRemoveAccents()
    {
        int _start = this.sourceArea.getSelectionStart();
        int _end = this.sourceArea.getSelectionEnd();
        
        String _s = this.doGetText(_start, _end);
        
        _s = StringUtil.asciifyAccents(_s);
        
        this.doSetText(_start,_end, _s);
        
    }
    
    public void createCVActions()
    {
        JXPanel _panel = SwingHelper.createTitledXPanel("Consonant/Vowel/Letter Actions");
        _panel.setLayout(new BoxLayout(_panel, BoxLayout.X_AXIS));
        //_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE,mainGUI._BHEIGHT));
        
        _panel.add(SwingHelper.createButton("<C/V>", "Transform to CV", 70, ()->{
            this.makeCV();
        }));
        
        _panel.add(SwingHelper.createButton("→", "Enable all", 35, ()->{
            for(JCheckBox _k : this._cv_check) {
                _k.setSelected(true);
            }
        }));
        
        _panel.add(this._check_panel = SwingHelper.createRxGrid(10));
        this._cv_check = new Vector<JCheckBox>();
        this._cv_set = new LinkedHashMap<String,List<String>>();
        this.handleCvChange( true);
        
        _panel.add(SwingHelper.createButton("...", "Load from File", ()->{
            this.handleCvChange(false);
        }));
        
        _panel.add(SwingHelper.createButton("X", "Reset to Standard", 45, ()->{
            this.handleCvChange(true);
        }));
        
        _panel.add(SwingHelper.createButton("<↑>", "Dump to Editor",  ()->{
            this.makeRules();
        }));
        
        _panel.add(SwingHelper.createButton("<↓>", "Parse from Editor", ()->{
            this.parseRules();
        }));
        
        _panel.add(SwingHelper.createLabel(" | "));
        _panel.add(SwingHelper.createButton("<L>", "Extract Unique Letters", ()->{
            this.makeLetters();
        }));
        
        this.northPanel.add(_panel);
    }
    
    private void handleCvChange(boolean _reset) {
        new Thread(()->{
            File _file = null;
            
            if(!_reset) {
                JFileChooser _j = SwingHelper.createFileChooser("Select C/V Rules ...", "Select",
                        OsUtil.getApplicationName(),CfgDataUtil.getLastDirFromConfig(OsUtil.getApplicationName(), null),false, _cv_exts);
                int _opt = _j.showOpenDialog(this);
                if(_opt == JFileChooser.APPROVE_OPTION) {
                    CfgDataUtil.setLastDirToConfig(OsUtil.getApplicationName(), _j.getSelectedFile().getParentFile());
                    _file = _j.getSelectedFile();
                }
            }
            
            if(_reset || (_file!=null)) {
                for(JCheckBox _b : this._cv_check) {
                    this._check_panel.remove(_b);
                }
                this._cv_check.clear();
                this._cv_set.clear();
            }
            
            if(_reset) {
                this._cv_set.putAll(_CVMAP);
            } else if(_file!=null) {
                for(Map.Entry<String, Object> _e : LdataUtil.loadFrom(_file).entrySet()) {
                    this._cv_set.put(_e.getKey().toUpperCase(), (List) _e.getValue());
                }
            }
            
            SwingUtilities.invokeLater(()->{
                for(String _k : this._cv_set.keySet()) {
                    JCheckBox _cb = new JCheckBox(_k);
                    this._cv_check.add(_cb);
                }
                
                for(JCheckBox _b : this._cv_check) {
                    this._check_panel.add(_b);
                }
                this._check_panel.revalidate();
                this._check_panel.repaint();
            });
        }).start();
    }
    
    static FileNameExtensionFilter _cv_exts = new FileNameExtensionFilter("C/V rules (*.pdata, *.cvrules)", "pdata", "cvrules");
    
    private void makeCV()
    {
        if(this.getTabPanel().getSelectedIndex()!=0) return;
        
        int _start = this.sourceArea.getSelectionStart();
        int _end = this.sourceArea.getSelectionEnd();
        
        StringBuilder _sb = new StringBuilder();
        
        String _s = this.doGetText(_start, _end);
 
        List<String> _repls = new Vector<>();
        for(JCheckBox _k : this._cv_check) {
            if(_k.isSelected()) _repls.add(_k.getText());
        }
        
        for(String _rk : _repls) {
            List<String> _rl = this._cv_set.get(_rk);
            for(String _r : _rl) {
                _s = _s.replace(_r, "<"+_rk+">");
            }
        }
        
        StringBuilder _sb2 = new StringBuilder();
        boolean inRule = false;
        for(char _c: _s.toCharArray()) {
            if(!inRule && (_c=='<')) {
                _sb2.append(_c);
                inRule=true;
            } else if(inRule && (_c=='>')) {
                _sb2.append(_c);
                inRule=false;
            } else if(inRule) {
                _sb2.append(_c);
            } else if(!Character.isLetter(_c)) {
                _sb2.append("\n");
            } else if(_c > 126) {
                _sb2.append(_c);
            } else if(Character.isLetter(_c) && Character.isLowerCase(_c)) {
                if(_c == 'a' || _c == 'e' || _c == 'i' || _c == 'o' || _c == 'u' || _c == 'y') {
                    _sb2.append("<V>");
                } else {
                    _sb2.append("<C>");
                }
            } else {
                _sb2.append(_c);
            }
        }
        
        _sb.append(_sb2.toString().replace(' ','\n').replace("\n\n", "\n"));
        
        doSetText(_start,_end,_sb.toString());
    }
    
    private void parseRules()
    {
        new Thread(()->{
            for(JCheckBox _b : this._cv_check) {
                this._check_panel.remove(_b);
            }
            this._cv_check.clear();
            this._cv_set.clear();
            
            int _start = this.sourceArea.getSelectionStart();
            int _end = this.sourceArea.getSelectionEnd();
            
            StringBuilder _sb = new StringBuilder();
            
            _sb.append(doGetText(_start,_end));

            for(Map.Entry<String, Object> _e : LdataUtil.loadFrom(new StringReader(_sb.toString())).entrySet()) {
                this._cv_set.put(_e.getKey().toUpperCase(), (List) _e.getValue());
            }
            
            SwingUtilities.invokeLater(()->{
                for(String _k : this._cv_set.keySet()) {
                    JCheckBox _cb = new JCheckBox(_k);
                    this._cv_check.add(_cb);
                }
                
                for(JCheckBox _b : this._cv_check) {
                    this._check_panel.add(_b);
                }
                this._check_panel.revalidate();
                this._check_panel.repaint();
            });
        }).start();
    }
    @Override
    public void insertUpdate(DocumentEvent e) {
        this.changed=true;
        updateLabel();
    }
    
    @Override
    public void removeUpdate(DocumentEvent e) {
        this.changed=true;
        updateLabel();
    }
    
    @Override
    public void changedUpdate(DocumentEvent e) {
        this.changed=true;
        updateLabel();
    }
    
    public void updateLabel()
    {
        for(Runnable _r : _updateListener)
        {
            try
            {
                _r.run();
            }
            catch(Throwable _th)
            {
                //IGNORE
            }
        }
    }
    
    List<Runnable> _updateListener = new Vector<>();
    
    public void addUpdateListener(Runnable _callback)
    {
        this._updateListener.add(_callback);
    }

    public void removeUpdateListener(Runnable _callback)
    {
        this._updateListener.remove(_callback);
    }
    
    @SneakyThrows
    private void makeWsTo(boolean _spaceOrLF, boolean _wordWrap)
    {
        int _start = this.sourceArea.getSelectionStart();
        int _end = this.sourceArea.getSelectionEnd();
        
        StringBuilder _sb = new StringBuilder();
        String[] _words = doGetText(_start, _end).split("\\s+");
        
        if(_wordWrap)
        {
            int _w = 0;
            for(int _i = 0; _i< _words.length; _i++) {
                _sb.append(_words[_i]);
                _sb.append(_spaceOrLF ? ' ' : '\n');
                _w+=_words[_i].length()+1;
                if(_w>75) {
                    _w=0;
                    _sb.append('\n');
                }
            }
        }
        else
        {
            _sb.append(CommonUtil.join(_words, _spaceOrLF ? ' ' : '\n'));
        }
        
        doSetText(_start,_end,_sb.toString());
    }
    
    @SneakyThrows
    private void makeInitials()
    {
        if(this.getTabPanel().getSelectedIndex()!=0) return;
        
        int _start = this.sourceArea.getSelectionStart();
        int _end = this.sourceArea.getSelectionEnd();
        
        StringBuilder _sb = new StringBuilder();
        String[] _words = doGetText(_start, _end).split("\\s+");
        
        for(String _w : _words)
        {
            _w = _w.trim();
            _sb.append(CommonUtil.initials(_w)+"\n");
        }
        
        doSetText(_start,_end,_sb.toString());
    }
    
    @SneakyThrows
    private void makeQuoteLines(String _q)
    {
        if(this.getTabPanel().getSelectedIndex()!=0) return;
        
        int _start = this.sourceArea.getSelectionStart();
        int _end = this.sourceArea.getSelectionEnd();
        
        StringBuilder _sb = new StringBuilder();
        String[] _words = doGetText(_start, _end).split("\n+");
        
        for(String _w : _words)
        {
            _w = _w.trim();
            if(_w.length()>0)_sb.append(_q+_w+_q+"\n");
        }
        
        doSetText(_start,_end,_sb.toString());
    }
    
    @SneakyThrows
    private void makeSortLines(SortMode _sm)
    {
        if(this.getTabPanel().getSelectedIndex()!=0) return;
        
        int _start = this.sourceArea.getSelectionStart();
        int _end = this.sourceArea.getSelectionEnd();
        
        String[] _words = doGetText(_start,_end).split("\\n+");
        
        for(int _i = 0; _i< _words.length; _i++)
        {
            _words[_i] = _words[_i].trim();
        }
        
        makeRealSort(_sm, _words);
        
        doSetText(_start,_end,CommonUtil.join(_words, '\n'));
    }
    
    void makeRealSort(SortMode _sm, String[] _words)
    {
        switch (_sm)
        {
            case _MODE_SORT:
                Arrays.sort(_words, (a,b)->{ return CommonUtil.compare(a.toLowerCase(),b.toLowerCase());});
                break;
            case _MODE_SORT_CASE:
                Arrays.sort(_words, (a,b)->{ return CommonUtil.compare(a,b);});
                break;
            case _MODE_SHUFFLE:
                ListMapUtil.shuffle(_words);
                break;
            case _MODE_UNIQ:
                ListMapUtil.uniq(_words);
                break;
            case _MODE_SORT_REVERSE:
                Arrays.sort(_words, (a,b)->{ return CommonUtil.compare(b.toLowerCase(),a.toLowerCase());});
                break;
            case _MODE_SORT_REVERSE_CASE:
                Arrays.sort(_words, (a,b)->{ return CommonUtil.compare(b,a);});
                break;
            case _MODE_REVERSE:
                ArrayUtils.reverse(_words);
                break;
        }
    }
    
    @SneakyThrows
    private void makeSortWords(SortMode _sm)
    {
        if(this.getTabPanel().getSelectedIndex()!=0) return;
        
        int _start = this.sourceArea.getSelectionStart();
        int _end = this.sourceArea.getSelectionEnd();
        
        String[] _words = doGetText(_start,_end).split("\\s+");
        
        for(int _i = 0; _i< _words.length; _i++)
        {
            _words[_i] = _words[_i].trim();
        }
        
        makeRealSort(_sm, _words);
        
        doSetText(_start,_end,CommonUtil.join(_words, '\n'));
    }
    @SneakyThrows
    private void makeQuoteWords(String _q)
    {
        if(this.getTabPanel().getSelectedIndex()!=0) return;
        
        int _start = this.sourceArea.getSelectionStart();
        int _end = this.sourceArea.getSelectionEnd();
        
        StringBuilder _sb = new StringBuilder();
        String[] _words = doGetText(_start, _end).split("\\s+");
        
        for(String _w : _words)
        {
            _w = _w.trim();
            if(_w.length()>0)_sb.append(_q+_w+_q+" ");
        }
        
        doSetText(_start,_end,_sb.toString());
    }
    
    private void makeRemovePunctuation()
    {
        if(this.getTabPanel().getSelectedIndex()!=0) return;
        
        int _start = this.sourceArea.getSelectionStart();
        int _end = this.sourceArea.getSelectionEnd();
        
        String _tmp = CommonUtil.removePunctuation(doGetText(_start, _end));
        
        doSetText(_start,_end,_tmp);
    }
    
    public void handleSetFont(String _fn) {
        int _size = this.sourceArea.getFont().getSize();
        this.handleSetFont(_fn, _size);
    }
    
    public void handleSetFont(String _fn, int _size) {
        this.sourceArea.setFont(SwingHelper.createFont(_fn,false, false,_size,true));
        for(Style _Style : this.sourceArea.getSyntaxScheme().getStyles())
        {
            _Style.font=this.sourceArea.getFont();
            _Style.fontMetrics=null;
        }
    }
    
    public void handleSetFont(Font _f)
    {
        this.sourceArea.setFont(_f);
        for(Style _Style : this.sourceArea.getSyntaxScheme().getStyles())
        {
            _Style.font=_f;
            _Style.fontMetrics=null;
        }
    }
    
    public void handleIncreaseFontSize()
    {
        int _size = this.sourceArea.getFont().getSize()+2;
        this.handleSetFont(this.sourceArea.getFont().deriveFont((float)_size));
    }
    
    public void handleDecreaseFontSize()
    {
        int _size = this.sourceArea.getFont().getSize()-2;
        this.handleSetFont(this.sourceArea.getFont().deriveFont((float)_size));
    }
    
    void wordsToStops(String _words, Set<String> _stops)
    {
        if(_words==null) return;
        
        for(String _line : _words.split("\\n+")) {
            if((!_line.trim().startsWith("#")) && (_line.trim().length()>0)) {
                for(String _w : _line.split("\\s+" )) {
                    _stops.add(_w.toLowerCase());
                }
            }
        }
    }
    
    @SneakyThrows
    private void makeRemoveStopwords()
    {
        if(this.getTabPanel().getSelectedIndex()!=0) return;
        
        Set<String> _stops = new HashSet<>();
        
        if(this.getStopwordsProvider()!=null)
        {
            String _words = this.getStopwordsProvider().call();
            wordsToStops(_words, _stops);
        }
        
        if(_stops.size()==0)
        {
            SwingHelper.executeFileOpenChooser(this,"Select Stopwords File ...", "Select",null,null,(f)->{
                try
                {
                    wordsToStops(Files.toString(f, StandardCharsets.UTF_8), _stops);
                }
                catch (IOException _e)
                {
                    //throw new RuntimeException(_e);
                }
            }, SwingHelper.createSimpleFileNameFilter("Stopwords Files","txt", "stopwords"));
        }
        
        if(_stops.size()==0) return;
        
        new Thread(()->{
            StringBuilder _sb = new StringBuilder();
            int _l = 1;
            for(String _line : this.sourceArea.getText().split("\\n"))
            {
                log.info("line "+(_l++));
                _line = _line.trim();
                if(_line.length()>0)
                {
                    for(String _word : CommonUtil.removePunctuation(_line).split("\\s+"))
                    {
                        if(!_stops.contains(_word.toLowerCase()))
                        {
                            _sb.append(_word);
                            _sb.append(" ");
                        }
                    }
                }
                _sb.append("\n");
            }
            this.sourceArea.setText(_sb.toString());
        }).start();
    }
    
    @Override
    public String getSelectedText() {
        return null;
    }
    
    Callable<String> stopwordsProvider;
    
    public Callable<String> getStopwordsProvider()
    {
        return stopwordsProvider;
    }
    
    public void setStopwordsProvider(Callable<String> _stopwordsProvider)
    {
        stopwordsProvider = _stopwordsProvider;
    }
}
