package com.github.terefang.jmelange.pdf.ml;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.image.PdfImage;
import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.commons.loader.FileResourceLoader;
import com.github.terefang.jmelange.commons.loader.ResourceLoader;
import com.github.terefang.jmelange.pdf.core.values.PdfOutline;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import com.github.terefang.jmelange.pdf.ext.PdfExtDocument;
import com.github.terefang.jmelange.pdf.ext.image.PdfSvgImage;
import com.github.terefang.jmelange.pdf.ext.text.BreakCell;
import com.github.terefang.jmelange.pdf.ext.text.Cell;
import com.github.terefang.jmelange.pdf.ext.text.HtmlEntityTextCell;
import com.github.terefang.jmelange.pdf.ext.text.TextCell;
import com.github.terefang.jmelange.pdf.ml.cell.AbstractTableCell;
import com.github.terefang.jmelange.pdf.ml.cell.MarkdownCell;
import com.github.terefang.jmelange.pdf.ml.cell.TableFormatCell;
import com.github.terefang.jmelange.pdf.ml.cmd.TopLevelDirective;
import com.github.terefang.jmelange.pdf.ml.cmd.TopLevelProcessInstruction;
import com.github.terefang.jmelange.pdf.ml.io.PmlClasspathResourceLoader;
import com.github.terefang.jmelange.pdf.ml.io.PmlFileResourceLoader;
import com.github.terefang.jmelange.pdf.ml.io.PmlResourceWriter;
import com.github.terefang.jmelange.pdf.ml.kxml2.EntityResolver;
import com.github.terefang.jmelange.pdf.ml.kxml2.KXmlParser;
import com.github.terefang.jmelange.pdf.ml.script.AbstractPmlScriptContext;
import com.github.terefang.jmelange.pdf.ml.script.PmlDrawScriptContext;

import com.github.terefang.jmelange.pdf.ml.script.PmlLuayScriptContext;
import com.vladsch.flexmark.ast.*;
import com.vladsch.flexmark.ext.admonition.AdmonitionBlock;
import com.vladsch.flexmark.ext.admonition.AdmonitionExtension;
import com.vladsch.flexmark.ext.attributes.AttributeNode;
import com.vladsch.flexmark.ext.attributes.AttributesExtension;
import com.vladsch.flexmark.ext.attributes.AttributesNode;
import com.vladsch.flexmark.ext.tables.*;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.ext.typographic.TypographicQuotes;
import com.vladsch.flexmark.ext.typographic.TypographicSmarts;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.collection.iteration.ReversiblePeekingIterable;
import com.vladsch.flexmark.util.data.MutableDataSet;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.awt.*;
import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
public class PmlParser
{
    public static String FONT_DEFAULT_ID = "default";
    public static String FONT_DEFAULT_NAME = "SansSerif";
    public static String FONT_DEFAULT_CHARSET = "pdfdoc";

    public static String SOURCE_TYPE_HTML = "html-source";
    public static String SOURCE_TYPE_TEXT = "text-source";
    public static String SOURCE_TYPE_MARKDOWN = "markdown-source";

    public static final int BASE_INDENT = 15;

    public Deque<PmlParserContext> _queue =new ArrayDeque<>();
    public PdfExtDocument _pdf;
    public PdfFontRegistry _reg;
    public PdfPage _page;
    PdfContent _background;

    Properties HTML_ENTITIES = new Properties();

    int _page_num;

    boolean flatOutline = false;

    boolean obfuscate;

    public boolean isObfuscate() {
        return obfuscate;
    }

    public void setObfuscate(boolean obfuscate) {
        this.obfuscate = obfuscate;
    }

    private PdfOutline _part;
    private PdfOutline _chapter;
    private PdfOutline _section;
    private PdfOutline _subsection;
    private PdfOutline _subsubsection;
    private PdfOutline _refoutlines;

    public boolean isFlatOutline() {
        return flatOutline;
    }

    public void setFlatOutline(boolean flatOutline) {
        this.flatOutline = flatOutline;
    }

    public PmlParser() throws IOException
    {
        HTML_ENTITIES.load(PmlClasspathResourceLoader.of("html-entities.properties").getInputStream());
    }

    public void processPML(File _ref, PmlResourceWriter _out, boolean _allT3)
    {
        processPML(_ref.getParentFile(), _ref, _out, _allT3);
    }

    public void processPML(File _base, File _ref, PmlResourceWriter _out, boolean _allT3)
    {
        try
        {
            pushFile(_base, _ref);
            _pdf = PdfExtDocument.create();

            _pdf.setAllT3(_allT3);
            _pdf.setObfuscate(obfuscate);
            _pdf.setEmbedCoreFonts(true);
            _pdf.setSubject(_ref.getName());
            _pdf.setTitle(_out.getName());
            _pdf.setProducer(this.getClass().getCanonicalName()+" "+ Version.FULL);

            _reg = PdfFontRegistry.of(_pdf);
            _pdf.streamBegin(_out.getOutputStream());

            while(_queue.size()>0)
            {
                PmlParserContext _xpc = _queue.poll();
                handleBase(_xpc);
            }

            _pdf.streamEnd(true);
        }
        catch(Exception _xe)
        {
            _xe.printStackTrace();
        }
    }

    public void processDRW(File _ref, PmlResourceWriter _out, boolean _allT3)
    {
        processDRW(_ref.getParentFile(), _ref, _out, _allT3, new Properties());
    }

    public void processDRW(File _ref, PmlResourceWriter _out, boolean _allT3, Properties _opts)
    {
        processDRW(_ref.getParentFile(), _ref, _out, _allT3, _opts);
    }

    public void processDRW(File _base, File _ref, PmlResourceWriter _out, boolean _allT3, Properties _opts)
    {
        try
        {
            _pdf = PdfExtDocument.create();
            _pdf.setAllT3(_allT3);
            _pdf.setObfuscate(obfuscate);
            _reg = PdfFontRegistry.of(_pdf);
            _pdf.streamBegin(_out.getOutputStream());

            PmlParserContext _xpc = PmlParserContext
                    .builder()
                    .basedir(_base)
                    .file(_ref)
                    .properties(_opts)
                    .build();

            processDRW(_xpc);

            _pdf.streamEnd(true);
        }
        catch(Exception _xe)
        {
            _xe.printStackTrace();
        }
    }

    @SneakyThrows
    public void processDRW(PmlParserContext _xpc)
    {
        try
        {
            PmlDrawScriptContext _psc = PmlDrawScriptContext.create(this, _xpc);

            _psc.runDraw(_xpc.getFile(), false, false, "", false);
        }
        catch(Exception _xe)
        {
            _xe.printStackTrace();
        }
    }

    public void processLUA(File _ref, PmlResourceWriter _out, boolean _allT3)
    {
        processLUA(_ref.getParentFile(), _ref, _out, _allT3, new Properties());
    }

    public void processLUA(File _ref, PmlResourceWriter _out, boolean _allT3, Map<String,Object> _data)
    {
        processLUA(_ref.getParentFile(), _ref, _out, _allT3, new Properties(), _data);
    }

    public void processLUA(File _ref, PmlResourceWriter _out, boolean _allT3, Properties _opts)
    {
        processLUA(_ref.getParentFile(), _ref, _out, _allT3, _opts);
    }

    public void processLUA(File _ref, PmlResourceWriter _out, boolean _allT3, Properties _opts, Map<String,Object> _data)
    {
        processLUA(_ref.getParentFile(), _ref, _out, _allT3, _opts, _data);
    }

    public void processLUA(File _base, File _ref, PmlResourceWriter _out, boolean _allT3, Properties _opts)
    {
        processLUA(_base, _ref, _out, _allT3, _opts, null);
    }
    public void processLUA(File _base, File _ref, PmlResourceWriter _out, boolean _allT3, Properties _opts, Map<String,Object> _data)
    {
        try
        {
            _pdf = PdfExtDocument.create();
            _pdf.setAllT3(_allT3);
            _pdf.setObfuscate(obfuscate);
            _reg = PdfFontRegistry.of(_pdf);
            _pdf.streamBegin(_out.getOutputStream());

            PmlParserContext _xpc = PmlParserContext
                    .builder()
                    .basedir(_base)
                    .file(_ref)
                    .properties(_opts)
                    .build();

            processLUA(_xpc, _data);

            _pdf.streamEnd(true);
        }
        catch(Exception _xe)
        {
            _xe.printStackTrace();
        }
    }

    public void processLUA(PmlParserContext _xpc)
    {
        processLUA(_xpc, null);
    }

    @SneakyThrows
    public void processLUA(PmlParserContext _xpc, Map<String,Object> _data)
    {
        try
        {
            PmlLuayScriptContext _psc = PmlLuayScriptContext.create(this, _xpc);

            _psc.runLua(_xpc.getFile(), _xpc, _xpc.parser,_xpc.getProperties(),"", false, _data);
        }
        catch(Exception _xe)
        {
            _xe.printStackTrace();
        }
    }


    public Map<String, ZipFile> ZIP_MOUNTS = new HashMap<>();
    public List<File> DIR_MOUNTS = new Vector();

    public void mountZip(String _src, File _basedir, File _parentFile)
    {
        mountZip(PmlUtil.sourceToFile(_src, _basedir, _parentFile));
    }

    @SneakyThrows
    public void mountZip(File _file)
    {
        ZipFile _zip = new ZipFile(_file);
        ZIP_MOUNTS.put(_file.getAbsolutePath(), _zip);
        ZipEntry _fa = _zip.getEntry("config/font-aliases.properties");
        if(_fa!=null)
        {
            addFontAliasses(_zip.getInputStream(_fa));
        }
    }

    @SneakyThrows
    public void mountClasspath()
    {
        ClasspathResourceLoader _rl = ClasspathResourceLoader.of("config/font-aliases.properties", null);
        if(_rl!=null)
        {
            addFontAliasses(_rl.getInputStream());
        }
        this.DIR_MOUNTS.add(new File("cp:"));
    }

    @SneakyThrows
    public void mountDir(String _src, File _basedir, File _parentFile)
    {
        File _base = null;
        if(_src.startsWith("./") || _src.startsWith("../"))
        {
            _base = new File(_parentFile, _src);
            if(!_base.exists())
            {
                _base = new File(_basedir, _src);
                if(!_base.exists())
                {
                    _base = new File(_src);
                }
            }
        }
        else
        {
            _base = new File(_src);
        }

        if(_base != null && _base.exists())
        {
            DIR_MOUNTS.add(_base);
            File _fa = new File(_base, "config/font-aliases.properties");
            if(_fa.exists())
            {
                addFontAliasses(new FileInputStream(_fa));
            }
        }
    }

    @SneakyThrows
    void addFontAliasses(InputStream _fh)
    {
        Properties _p = new Properties();
        _p.load(_fh);
        IOUtil.close(_fh);
        _p.entrySet().forEach((x)->{ FONT_ALIASES.setProperty(x.getKey().toString().toLowerCase(), x.getValue().toString()); });
    }

    private void handleBase(PmlParserContext _xpc) throws IOException, XmlPullParserException
    {
        XmlPullParser _xpp = _xpc.getParser();
        int _event = -1;

        if(ATTRIBUTE_DEFAULTS.size()==0)
        {
            ATTRIBUTE_DEFAULTS.load(ClasspathResourceLoader.of("attribute-defaults.properties", null).getInputStream());
        }

        while((_event = _xpp.next()) != XmlPullParser.END_DOCUMENT)
        {
            switch(_event)
            {
                case XmlPullParser.START_DOCUMENT: {
                    log.warn("Starting document "+_xpc.getFile().getName());
                    break;
                }
                case XmlPullParser.PROCESSING_INSTRUCTION: {
                    TopLevelProcessInstruction _tlpi = TopLevelProcessInstruction.valueOf(_xpp.getName().toLowerCase());
                    if(_tlpi!=null) {
                        if (_tlpi.execute(_xpp.getName().toLowerCase(), this, _xpc, _xpp))
                        {
                            return;
                        }
                    }
                    else
                    if("header".equalsIgnoreCase(_xpp.getName())
                        || "footer".equalsIgnoreCase(_xpp.getName())
                        || _xpp.getName().startsWith("header-")
                        || _xpp.getName().startsWith("footer-"))
                    {
                        executeProcessInstructionHeaderOrFooter(_xpp, _xpc);
                    }
                    else
                    {
                        log.warn("E="+_event+" @ "+_xpp.getPositionDescription());
                    }
                    break;
                }
                case XmlPullParser.START_TAG: {
                    TopLevelDirective _tld = TopLevelDirective.valueOf(_xpp.getName().toLowerCase());
                    if(_tld!=null) {
                        if (_tld.executeStart(_xpp.getName().toLowerCase(), this, _xpc, _xpp))
                        {
                            return;
                        }
                    }
                    else
                    {
                        log.warn("E="+_event+" @ "+_xpp.getPositionDescription());
                    }
                    break;
                }
                case XmlPullParser.END_TAG: {
                    TopLevelDirective _tld = TopLevelDirective.valueOf(_xpp.getName().toLowerCase());
                    if(_tld!=null) {
                        if (_tld.executeEnd(_xpp.getName().toLowerCase(), this, _xpc, _xpp))
                        {
                            return;
                        }
                    }
                    else
                    {
                        log.warn("E="+_event+" @ "+_xpp.getPositionDescription());
                    }
                    break;
                }
                case XmlPullParser.TEXT: {
                    break;
                }
                default: {
                    log.warn("E="+_event+" @ "+_xpp.getPositionDescription());
                    break;
                }
            }
        }

    }

    public void executeProcessInstructionOption(XmlPullParser _xpp, PmlParserContext _xpc)
    {
        Properties _attributes = PmlParserUtil.toProperties(_xpp);

        for(String _key : _attributes.stringPropertyNames())
        {
            if("jpeg-compression".equalsIgnoreCase(_key))
            {
                this._pdf.setJpegCompression(CommonUtil.toFloat(_attributes.getProperty(_key))/100f);
            }
            else
            if("print-attribute-access".equalsIgnoreCase(_key))
            {
                this.dumpAttributeLookups = CommonUtil.toBoolean(_attributes.getProperty(_key));
            }
            else
            if("flat-outline".equalsIgnoreCase(_key))
            {
                this.flatOutline = CommonUtil.toBoolean(_attributes.getProperty(_key));
            }
            else
            if("all-type3".equalsIgnoreCase(_key))
            {
                this._pdf.setAllT3(CommonUtil.toBoolean(_attributes.getProperty(_key)));
            }
        }
    }

    private void executeProcessInstructionHeaderOrFooter(XmlPullParser _xpp, PmlParserContext _xpc)
    {
        String _tag = _xpp.getName();
        if("header".equalsIgnoreCase(_tag))
        {
            Properties _attributes = PmlParserUtil.toPropertiesWithPrefix("page-header", _xpp);
            HEADER_EVEN.putAll(_attributes);
            HEADER_ODD.putAll(_attributes);
        }
        else
        if("header-even".equalsIgnoreCase(_tag))
        {
            Properties _attributes = PmlParserUtil.toPropertiesWithPrefix("page-header", _xpp);
            HEADER_EVEN.putAll(_attributes);
        }
        else
        if("header-odd".equalsIgnoreCase(_tag))
        {
            Properties _attributes = PmlParserUtil.toPropertiesWithPrefix("page-header", _xpp);
            HEADER_ODD.putAll(_attributes);
        }
        else
        if("footer".equalsIgnoreCase(_tag))
        {
            Properties _attributes = PmlParserUtil.toPropertiesWithPrefix("page-footer", _xpp);
            FOOTER_EVEN.putAll(_attributes);
            FOOTER_ODD.putAll(_attributes);
        }
        else
        if("footer-even".equalsIgnoreCase(_tag))
        {
            Properties _attributes = PmlParserUtil.toPropertiesWithPrefix("page-footer", _xpp);
            FOOTER_EVEN.putAll(_attributes);
        }
        else
        if("footer-odd".equalsIgnoreCase(_tag))
        {
            Properties _attributes = PmlParserUtil.toPropertiesWithPrefix("page-footer", _xpp);
            FOOTER_ODD.putAll(_attributes);
        }
        else
        {
            throw new IllegalArgumentException(_tag);
        }
    }

    public Map<String, PdfSvgImage>SVG_REGISTRY = new HashMap<>();
    public Map<String, PdfImage>IMG_REGISTRY = new HashMap<>();

    @SneakyThrows
    public void executeProcessInstructionSvgOrImage(XmlPullParser _xpp, PmlParserContext _xpc)
    {
        Properties _attributes = PmlParserUtil.toProperties(_xpp);

        float _s = CommonUtil.toFloat(getAttributeValueOrDefault(_attributes, "render", "-1"));

        String _trans = getAttributeValueOrDefault(_attributes, "transparency", "false");
        float _av = CommonUtil.toFloat(getAttributeValueOrDefault(_attributes, "alpha", "100"));

        int _rot = getAttributeValueOrDefaultAsInt(_attributes, "rotate", 0);

        String _type = getAttributeValueOrDefault(_attributes, "compress", "false");

        String _src = getAttributeValueOrNull(_attributes, "src");
        String _id = getAttributeValueOrNull(_attributes, "id");

        if("svg".equalsIgnoreCase(_xpp.getName()))
        {
            loadSvg(_id, _src, _s, _type, _trans, _av, _rot, _xpc);
        }
        else
        if("image".equalsIgnoreCase(_xpp.getName()))
        {
            loadImage(_id, _src, _type, _trans, _av, _rot, _xpc);
        }
    }

    @SneakyThrows
    public void loadImage(String _id, String _src, String _compress, String _transp, float _alphabias, int _rot, PmlParserContext _xpc)
    {
        if(IMG_REGISTRY.containsKey(_id))
        {
            return;
        }
        ResourceLoader _rl = PmlUtil.sourceToLoader(_src, _xpc.getBasedir(), _xpc.getFile().getParentFile(), this.ZIP_MOUNTS, this.DIR_MOUNTS);
        if(_rl!=null)
        {
            boolean _t = CommonUtil.checkBoolean(_transp);
            boolean _a = CommonUtil.toBoolean(_transp, "alpha");

            PdfImage _img = this._pdf.registerImage(_rl, _compress, _t, _a, _alphabias, _rot);
            IMG_REGISTRY.put(_id, _img);
        }
        else
        {
            throw new IllegalArgumentException(_src);
        }
    }

    @SneakyThrows
    public void loadSvg(String _id, String _src, float _s, String _compress, String _transp, float _alphabias, int _rot, PmlParserContext _xpc)
    {
        ResourceLoader _rl = PmlUtil.sourceToLoader(_src, _xpc.getBasedir(), _xpc.getFile().getParentFile(), this.ZIP_MOUNTS, this.DIR_MOUNTS);
        if(_rl!=null)
        {
            boolean _t = CommonUtil.toBoolean(_transp);
            boolean _a = CommonUtil.toBoolean(_transp, "alpha");

            if(_s>0)
            {
                if(IMG_REGISTRY.containsKey(_id))
                {
                    return;
                }
                PdfImage _svg = this._pdf.registerRenderedSvgImage(_rl, 100, 100, _s, _t, _a, _alphabias, _compress, _rot);
                IMG_REGISTRY.put(_id, _svg);
            }
            else
            {
                if(SVG_REGISTRY.containsKey(_id))
                {
                    return;
                }
                PdfSvgImage _svg = this._pdf.registerSvgImage(_rl,100,100);
                SVG_REGISTRY.put(_id, _svg);
            }
        }
        else
        {
            throw new IllegalArgumentException(_src);
        }
    }

    private int getAttributeValueOrDefaultAsInt(Properties _attributes, String _key, int i)
    {
        String _val = getAttributeValueOrNull(_attributes, _key);
        if(CommonUtil.isBlank(_val))
        {
            return i;
        }
        return CommonUtil.createInteger(_val);
    }

    private float getAttributeValueOrDefaultAsFloat(Properties _attributes, String _k, float _v, boolean _tagonly, String... tags)
    {
        String _val = getAttributeValueOrPrefix(_attributes, _k, _tagonly, tags);
        if(CommonUtil.isBlank(_val))
        {
            return _v;
        }
        return CommonUtil.toFloat(_val);
    }

    private boolean getAttributeValueOrDefaultAsBoolean(Properties _attributes, String _key, boolean i)
    {
        String _val = getAttributeValueOrNull(_attributes, _key);
        if(CommonUtil.isBlank(_val))
        {
            return i;
        }
        return CommonUtil.toBoolean(_val);
    }


    @SneakyThrows
    public void loadDefaults(File _file)
    {
        this.ATTRIBUTE_DEFAULTS.load(FileResourceLoader.of(_file, null).getInputStream());
        log.debug("loading/merging defaults from file: "+_file.getName());
    }

    public void loadDefaults(Map<String,Object> _def)
    {
        for(String _key : _def.keySet())
        {
            this.ATTRIBUTE_DEFAULTS.setProperty(_key, _def.get(_key).toString());
        }
    }

    public Map<String,Properties> CLASS_MAP = new HashMap<>();

    @SneakyThrows
    public void defineEntityOrIcon(XmlPullParser _xpp, String _font, String _name, int _code, boolean isIcon)
    {
        if(isIcon)
        {
            if(!this._iconMap.containsKey(_font))
            {
                this._iconMap.put(_font, new HashMap<>());
            }
            this._iconMap.get(_font).put(_name, _code);
        }
        else
        {
            this.HTML_ENTITIES.setProperty(_name, Integer.toString(_code));
            if(_xpp!=null) _xpp.defineEntityReplacementText(_name, Character.toString((char) _code));
        }
    }

    boolean isIcon(String _entity)
    {
        if(_entity.startsWith("&"))
        {
            _entity = _entity.substring(1);
        }

        if(_entity.endsWith(";"))
        {
            _entity = _entity.substring(0, _entity.length()-1);
        }

        int offs;

        if((offs = _entity.indexOf('-'))<0)
        {
            return false;
        }

        String _ifont = _entity.substring(0, offs);
        if(!this._iconMap.containsKey(_ifont))
        {
            return false;
        }

        String _iname = _entity.substring(offs+1);
        if(_iname.startsWith("#"))
        {
            return true;
        }
        else
        if(this._iconMap.get(_ifont).containsKey(_iname))
        {
            return true;
        }
        return false;
    }

    public void executeProcessInstructionFont(XmlPullParser _xpp, PmlParserContext _xpc)
    {
        Properties _attributes = PmlParserUtil.toProperties(_xpp);

        String _id = getAttributeValueOrDefault(_attributes, "id", FONT_DEFAULT_ID);
        String _name = getAttributeValueOrDefault(_attributes, "name",
            getAttributeValueOrDefault(_attributes, "src", _id));
        String _cs = getAttributeValueOrDefault(_attributes, "charset",
                getAttributeValueOrDefault(_attributes, "encoding", FONT_DEFAULT_CHARSET));

        String _opts = getAttributeValueOrDefault(_attributes, "options", null);
        String[] _options = null;
        if(_opts != null)
        {
            _options = CommonUtil.split(_opts, ";");
        }

        log.debug("define font: "+_name);

        if("icons".equalsIgnoreCase(_cs))
        {
            String _im = getAttributeValueOrNull(_attributes, "icon-map");
            if(CommonUtil.isNotBlank(_im))
            {
                registerIconMap(_id, _im, _xpc.getBasedir(), _xpc.getFile().getParentFile());
            }
            registerFont(_id, _name, _options, null, _xpc.getBasedir(), _xpc.getFile().getParentFile());
        }
        else
        {
            registerFont(_id, _name, _options, _cs, _xpc.getBasedir(), _xpc.getFile().getParentFile());
            try
            {
                // try find embedded icons
                registerIconMap(_id, _name, _xpc.getBasedir(), _xpc.getFile().getParentFile());
            }
            catch (Exception _xe)
            {
                //IGNORE
            }
        }
    }

    Map<String,Map<String,Integer>> _iconMap = new HashMap<>();

    public void registerIconMap(String _id, String _im, File _basedir, File _parentFile)
    {
        Map<String,Integer> _map = new HashMap<>();
        log.debug("define icon-map: "+_im);

        ResourceLoader _rl = PmlUtil.sourceToLoader(_im, _basedir, _parentFile, this.ZIP_MOUNTS, this.DIR_MOUNTS);

        if(_rl==null)
        {
            _rl = ClasspathResourceLoader.of("fonts/icons/"+_im+".properties", null);
        }

        // try aliases
        while(_rl==null && FONT_ALIASES.containsKey(_im.toLowerCase()))
        {
            _im = FONT_ALIASES.getProperty(_im.toLowerCase());
            _rl = ClasspathResourceLoader.of("fonts/icons/"+_im+".properties", null);
            if(_rl==null)
            {
                _rl = PmlUtil.sourceToLoader(_im+".properties", _basedir, _parentFile, this.ZIP_MOUNTS, this.DIR_MOUNTS);
            }
        }

        if(_rl == null) throw new IllegalArgumentException();

        try
        {
            Properties _icons = new Properties();
            _icons.load(_rl.getInputStream());
            for(String _ent : _icons.stringPropertyNames())
            {
                String _val = _icons.getProperty(_ent).trim();
                int _v = CommonUtil.createInteger(_val);
                int off = _ent.indexOf('-');
                _map.put(_ent.substring(off+1), _v);
            }

            _iconMap.put(_id, _map);
        }
        catch (Exception _xe)
        {
            _xe.printStackTrace();
        }
    }

    public float handlePageTextOrLabel(XmlPullParser _xpp) throws IOException, XmlPullParserException
    {
        float _hsize = 0;
        String _tag = _xpp.getName();

        Properties _attributes = PmlParserUtil.toPropertiesWithPrefix(_tag, _xpp);
        resolveFromClassDefinitionWithPrefix(_tag, _attributes);

        int[] _pos = PmlParserUtil.toIntArray(getAttributeValueOrPrefix(_attributes, "pos", false, _tag));
        int _fs = getAttributeValueOrPrefixAsInt(_attributes, "font-size", false, _tag);
        int _w = getAttributeValueOrPrefixAsInt(_attributes, "width", false, _tag);

        PdfContent _cnt = this.getPage().newContent();
        String _layer = _attributes.getProperty("layer", "false");
        boolean _doLayer = CommonUtil.toBooleanDefaultIfNull(_layer, true);
        if(_doLayer)
        {
            _cnt.startLayer(_layer);
        }

        _hsize += handleAttributedHeading(_attributes, _cnt, _w, _pos[0], _pos[1]);

        if(_hsize>0f)
        {
            _hsize += _fs;
        }

        int _event = -1;
        StringBuilder _sb = new StringBuilder();
        while((_event = _xpp.next())==XmlPullParser.TEXT)
        {
            _sb.append(_xpp.getText());
        }

        _hsize += layoutAndRenderText(_pos[0], _pos[1]-_hsize, _attributes, _cnt, _sb.toString(), "label".equalsIgnoreCase(_tag), _tag, false, _tag);

        String _link = getAttributeValueOrNull(_attributes, "link");
        if(CommonUtil.isNotBlank(_link))
        {
            this.getPage().newNamedLink(_link, _pos[0], (int) (_pos[1]-_hsize), _w, (int) _hsize);
        }

        if(_doLayer)
        {
            _cnt.endLayer();
        }
        return _hsize;
    }

    private float layoutAndRenderText(float _px, float _py, Properties _attributes, PdfContent _cnt, String _text, boolean _enforceAlign, String _source, boolean _tagOnly, String... _tags)
    {
        float _hsize = 0;

        String _color = getAttributeValueOrPrefix(_attributes, "color", _tagOnly, _tags);
        String _fn = getAttributeValueOrPrefix(_attributes, "font", _tagOnly, _tags);
        int _fs = getAttributeValueOrPrefixAsInt(_attributes, "font-size", _tagOnly, _tags);
        int _w = getAttributeValueOrPrefixAsInt(_attributes, "width", _tagOnly, _tags);
        float _ls = getAttributeValueOrPrefixAsFloat(_attributes, "font-lead", _tagOnly, _tags);
        float _hscale = getAttributeValueOrPrefixAsFloat(_attributes, "font-scale", 100f, _tagOnly, _tags);
        if(_ls<0) _ls = (-_ls)*_fs;
        String _align = getAttributeValueOrPrefix(_attributes, "align", _tagOnly, _tags);

        PdfFont _font = this._reg.lookupFont(_fn);

        String[] _paras = _text.trim().split("\n");
        for(int _i=0; _i< _paras.length; _i++)
        {
            boolean lastLine = (_i==_paras.length-1);
            if(!lastLine)
            {
                lastLine = _paras[_i+1].trim().equals("");
            }

            List<Cell> _pc = TextCell.builder()
                    .text(_paras[_i])
                    .fontFace(_fn)
                    .fontSize(_fs)
                    .spaceBefore(false)
                    .build()
                    .breakup(this._reg, -1, _hscale);
            List<Cell[]> _pl = layoutCells(_pc, -1, _hscale, false, _align, SOURCE_TYPE_TEXT, false);

            _cnt.save();
            _cnt.startText();
            _cnt.setFont(_font, _fs);
            _cnt.font(_font, _fs);
            _cnt.fillColor(_color);
            _cnt.moveText(_px, _py-((_fs+_ls)/2f)-_hsize);
            _cnt.hscale(_hscale);

            _hsize += renderCells(_pl, _cnt, _w , 0, _ls, _hscale, _align, !lastLine || _enforceAlign);

            _cnt.endText();
            _cnt.restore();
        }

        _hsize += ((_fs+_ls)/2f);

        String _link = getAttributeValueOrNull(_attributes, "link");
        if(CommonUtil.isNotBlank(_link))
        {
            this.getPage().newNamedLink(_link, (int)_px, (int)(_py-_hsize), _w, (int)_hsize);
        }

        renderConditionalBackground(_attributes, _px, _py, _w, _hsize, _tagOnly, _tags);
        renderConditionalBorder(_attributes, _px, _py, _w, _hsize, _tagOnly, _tags);

        return _hsize;
    }

    public void renderConditionalBackground(Properties _attributes, float _px, float _py, int _w, float _h, boolean _tagOnly, String... _tags)
    {
        String _layer = _attributes.getProperty("layer", "false");
        String _bgcolor = getAttributeValueOrPrefix(_attributes, "background", _tagOnly, _tags);
        if(CommonUtil.isNotBlank(_bgcolor))
        {
            float _bpad = getAttributeValueOrPrefixAsFloat(_attributes, "background-padding", 0f, _tagOnly, _tags);
            renderBackground(_layer, _bgcolor, _px-_bpad, _py+_bpad, (int) (_w+(2f*_bpad)), _h+(2f*_bpad));
        }
    }

    public void renderBackground(String _layer, String _bgcolor, float _px, float _py, int _w, float _h)
    {
        PdfContent _bg = this.getBackground();
        boolean _doLayer = CommonUtil.toBooleanDefaultIfNull(_layer, true);
        if(_doLayer)
        {
            _bg.startLayer(_layer+"-background");
        }
        _bg.save();
        _bg.fillColor(_bgcolor);
        _bg.rectangle(_px, _py-_h, _w, _h);
        _bg.fill();
        _bg.restore();
        if(_doLayer)
        {
            _bg.endLayer();
        }
    }

    public void renderConditionalBorder(Properties _attributes, float _px, float _py, int _w, float _h, boolean _tagOnly, String... _tags)
    {
        String _layer = _attributes.getProperty("layer", "false");
        boolean _doLayer = CommonUtil.toBooleanDefaultIfNull(_layer, true);

        String _border = getAttributeValueOrPrefix(_attributes, "border",_tagOnly, _tags);
        if(CommonUtil.isNotBlank(_border))
        {
            float _bpad = getAttributeValueOrPrefixAsFloat(_attributes, "border-padding", 0f, _tagOnly, _tags);
            _px -= _bpad;
            _py += _bpad;
            _w += (2f*_bpad);
            _h += (2f*_bpad);
            float[] _b = PmlParserUtil.toFloatArray(_border);
            PdfContent _bg = this.getBackground();
            if(_doLayer)
            {
                _bg.startLayer(_layer);
            }

            _bg.save();
            _bg.strokeColor("#000");

            float _top = 0, _left = 0, _bot = 0, _right = 0;

            if(_b.length==1)
            {
                _top = _b[0];
                _left = _b[0];
                _bot = _b[0];
                _right = _b[0];
            }
            else
            if(_b.length==2)
            {
                _top = _b[0];
                _left = _b[1];
                _bot = _b[0];
                _right = _b[1];
            }
            else
            if(_b.length==3)
            {
                _top = _b[0];
                _left = _b[1];
                _bot = _b[2];
                _right = _b[1];
            }
            else
            if(_b.length>=3)
            {
                _top = _b[0];
                _left = _b[1];
                _bot = _b[2];
                _right = _b[3];
            }

            if(_top>0f)
            {
                _bg.linewidth(_top);
                _bg.moveTo(_px, _py);
                _bg.lineTo(_px+_w, _py);
            }

            if(_bot>0f)
            {
                _bg.linewidth(_bot);
                _bg.moveTo(_px, _py-_h);
                _bg.lineTo(_px+_w, _py-_h);
            }

            if(_left>0f)
            {
                _bg.linewidth(_left);
                _bg.moveTo(_px, _py-_h);
                _bg.lineTo(_px, _py);
            }

            if(_right>0f)
            {
                _bg.linewidth(_right);
                _bg.moveTo(_px+_w, _py-_h);
                _bg.lineTo(_px+_w, _py);
            }

            _bg.stroke();
            _bg.restore();

            if(_doLayer)
            {
                _bg.endLayer();
            }
        }
    }

    public float getAttributeValueOrPrefixAsFloat(Properties attributes, String a, boolean tagOnly, String... tag)
    {
        return getAttributeValueOrPrefixAsFloat(attributes, a, 0f, tagOnly, tag);
    }

    public float getAttributeValueOrPrefixAsFloat(Properties attributes, String s, float v, boolean tagOnly, String... tag)
    {
        String _val = getAttributeValueOrPrefix(attributes, s, tagOnly, tag);
        if(CommonUtil.isBlank(_val))
        {
            return v;
        }
        return CommonUtil.toFloat(_val);
    }

    public boolean getAttributeValueOrPrefixAsBool(Properties attributes, String a, boolean tagOnly, String... tag)
    {
        return getAttributeValueOrPrefixAsBool(attributes, a, false, tagOnly, tag);
    }

    public boolean getAttributeValueOrPrefixAsBool(Properties attributes, String s, boolean v, boolean tagOnly, String... tag)
    {
        String _val = getAttributeValueOrPrefix(attributes, s, tagOnly, tag);
        if(CommonUtil.isBlank(_val))
        {
            return v;
        }
        if(CommonUtil.isNumber(_val))
        {
            return CommonUtil.toBoolean(CommonUtil.createInteger(_val));
        }
        return CommonUtil.toBoolean(_val);
    }

    public int getAttributeValueOrPrefixAsInt(Properties attributes, String a, boolean tagOnly, String... tag)
    {
        return getAttributeValueOrPrefixAsInt(attributes, a, 0, tagOnly, tag);
    }

    public int getAttributeValueOrPrefixAsInt(Properties attributes, String s, int v, boolean tagOnly, String... tag)
    {
        String _val = getAttributeValueOrPrefix(attributes, s, tagOnly, tag);
        if(CommonUtil.isBlank(_val))
        {
            return v;
        }
        return CommonUtil.createInteger(_val);
    }

    public float handleAttributedHeading(Properties _attributes, PdfContent _cnt, int _w, int _px, int _py) throws IOException, XmlPullParserException
    {
        float _hsize = 0;
        String _heading = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_HEADING, false, "text");
        if(_heading!=null
                && CommonUtil.isNotBlank(_heading)
                && CommonUtil.isNotEmpty(_heading)
                && !"".equalsIgnoreCase(_heading.trim())
                && !"false".equalsIgnoreCase(_heading.trim()))
        {
            String _hfn = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_FONT_FACE, false, "text-"+PmlParserUtil.PROP_HEADING);
            String _color = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_COLOR, false, "text-"+PmlParserUtil.PROP_HEADING);
            String _hol = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_HEADING_OUTLINE, false, "text-"+PmlParserUtil.PROP_HEADING, "text");
            float _hscale = getAttributeValueOrDefaultAsFloat(_attributes, "font-scale", 100f, true, "text-"+PmlParserUtil.PROP_HEADING, "text");
            int _hfs = getAttributeValueOrPrefixAsInt(_attributes, PmlParserUtil.PROP_FONT_SIZE, false, "text-"+PmlParserUtil.PROP_HEADING);
            int _level = CommonUtil.createInteger(getAttributeValueOrDefault(_attributes, "level", "1"));

            _cnt.save();
            _cnt.startText();
            _cnt.fillColor(_color);
            _cnt.moveText(_px,_py-_hfs);
            //_hsize += _hfs;
            TextCell _hcell = TextCell.builder()
                    .text(_heading)
                    .fontFace(_hfn)
                    .fontSize(_hfs)
                    .spaceBefore(false)
                    .build();
            List<Cell[]> _pl = layoutCells(PmlParserUtil.toList(_hcell), _w, _hscale, false, "left", SOURCE_TYPE_HTML, false);
            _hsize += renderCells(_pl, _cnt, _w, 0, _hfs, _hscale, "left", false);
            _cnt.endText();
            _cnt.restore();

            String _outline_text = mapEntitiesToString(_heading);
            if(CommonUtil.isNotBlank(_hol)
                    && !"false".equalsIgnoreCase(_hol.trim()))
            {
                _outline_text = mapEntitiesToString(_hol);
            }

            if(_level==0)
            {
                this.addChapter(_outline_text);
            }
            else
            if(_level==1)
            {
                this.addSection(_outline_text);
            }
            else
            {
                this.addOutline(_outline_text);
            }
        }
        return _hsize;
    }

    public float handlePageParagraph(XmlPullParser _xpp) throws IOException, XmlPullParserException
    {
        float _hsize = 0;

        String _tag = "paragraph";
        Properties _attributes = PmlParserUtil.toPropertiesWithPrefix(_tag, _xpp);
        resolveFromClassDefinitionWithPrefix(_tag, _attributes);

        int[] _pos = PmlParserUtil.toIntArray(getAttributeValueOrPrefix(_attributes, "pos", false, _tag));
        int _fs = getAttributeValueOrPrefixAsInt(_attributes, "font-size", false, _tag);
        int _w = getAttributeValueOrPrefixAsInt(_attributes, "width", false, _tag);

        String _fn = getAttributeValueOrPrefix(_attributes, "font", false, _tag);
        String _align = getAttributeValueOrPrefix(_attributes, "align", false, _tag);
        boolean _hyphen = getAttributeValueOrPrefixAsBool(_attributes, "hyphenate", false, _tag);
        float _ls = getAttributeValueOrPrefixAsFloat(_attributes, "font-lead", false, _tag);
        float _hscale = getAttributeValueOrDefaultAsFloat(_attributes, "font-scale", 100f, true, _tag);
        if(_ls<0) _ls = (-_ls)*_fs;

        PdfContent _cnt = this.getPage().newContent();
        String _layer = _attributes.getProperty("layer", "false");
        boolean _doLayer = CommonUtil.toBooleanDefaultIfNull(_layer, true);
        if(_doLayer)
        {
            _cnt.startLayer(_layer);
        }

        PdfFont _font = this._reg.lookupFont(_fn);

        _hsize += handleAttributedHeading(_attributes, _cnt, _w, _pos[0], _pos[1]);


        int _event = -1;
        StringBuilder _sb = new StringBuilder();
        while((_event = _xpp.next())==XmlPullParser.TEXT)
        {
            String _text = _xpp.getText().trim();
            _sb.append(_text);
        }

        for(String _para : _sb.toString().split("\n\n"))
        {
            List<Cell> _pc = TextCell.builder()
                    .text(_para)
                    .fontFace(_fn)
                    .fontSize(_fs)
                    .spaceBefore(false)
                    .build()
                    .breakup(this._reg, -1, _hscale);

            _cnt.save();
            _cnt.startText();
            _cnt.setFont(_font, _fs);
            _cnt.font(_font, _fs);
            _cnt.moveText(_pos[0], _pos[1]-_hsize-_fs);

            List<Cell[]> _pl = layoutCells(_pc, _w, _hscale, _hyphen, _align, SOURCE_TYPE_HTML, false);
            _hsize += renderCells(_pl, _cnt, _w, 0, _ls, _hscale, _align, false);
            _cnt.endText();
            _cnt.restore();
            _hsize += _ls;
        }

        String _bgcolor = getAttributeValueOrPrefix(_attributes, "background", false, _tag);
        if(CommonUtil.isNotBlank(_bgcolor))
        {
            PdfContent _bg = this.getBackground();
            _bg.save();
            _bg.fillColor(_bgcolor);
            _bg.rectangle(_pos[0], _pos[1]-_hsize, _w, _hsize);
            _bg.fill();
            _bg.restore();
        }

        if(_doLayer)
        {
            _cnt.endLayer();
        }
        return _hsize;
    }

    public float handlePageHeading(XmlPullParser _xpp) throws IOException, XmlPullParserException
    {
        String _tag = "heading";
        Properties _attributes = PmlParserUtil.toPropertiesWithPrefix(_tag, _xpp);
        resolveFromClassDefinitionWithPrefix(_tag, _attributes);
        String _id = getAttributeValueOrNull(_attributes, "id");

        int[] _pos = PmlParserUtil.toIntArray(getAttributeValueOrPrefix(_attributes, "pos", false, _tag));
        int _fs = getAttributeValueOrPrefixAsInt(_attributes, "font-size", false, _tag);

        String _fn = getAttributeValueOrPrefix(_attributes, "font", false, _tag);
        String _color = getAttributeValueOrPrefix(_attributes, "color", false, _tag);

        String _outline = getAttributeValueOrNull(_attributes, "outline");

        int _hl = getAttributeValueOrPrefixAsInt(_attributes, "level", 1,false, _tag);

        StringBuilder _sb = new StringBuilder();
        int _event = -1;
        while((_event = _xpp.next())==XmlPullParser.TEXT)
        {
            String _text = _xpp.getText();
            _sb.append(_text);
        }
        String _text = _sb.toString().replaceAll("\\s", " ");

        String _layer = _attributes.getProperty("layer", "false");

        return placeHeading(_id, _pos[0], _pos[1], _hl, _fn, _fs, _color, _text, _outline, _layer);
    }

    public int placeHeading(String _id, int _x, int _y, int _hl, String _fn, int _fs, String _color, String _text, String _outline, String _layer)
    {
        int _fsh = (int)(12 * (1f + 0.2f*(6-_hl)));
        if(_fs>0) _fsh = _fs;

        PdfContent _cnt = this.getPage().newContent();
        PdfFont _font = this._reg.lookupFont(_fn);
        boolean _doLayer = CommonUtil.toBooleanDefaultIfNull(_layer, true);
        if(_doLayer)
        {
            _cnt.startLayer(_layer);
        }
        _cnt.save();
        _cnt.startText();
        _cnt.fillColor(_color);
        _cnt.setFont(_font, _fsh);
        _cnt.font(_font, _fsh);
        _cnt.moveText(_x, _y);
        _cnt.text(_text);
        _cnt.endText();
        _cnt.restore();
        if(_doLayer)
        {
            _cnt.endLayer();
        }

        if(CommonUtil.isNotBlank(_outline))
        {
            _text = _outline;
        }

        if(_hl==0)
        {
            this.addChapter(mapEntitiesToString(_text));
        }
        else
        if(_hl==1)
        {
            this.addSection(mapEntitiesToString(_text));
        }
        else
        if(_hl==2)
        {
            this.addSubSection(mapEntitiesToString(_text));
        }
        else
        if(_hl==3)
        {
            this.addSubSubSection(mapEntitiesToString(_text));
        }
        else
        {
            this.addOutline(mapEntitiesToString(_text));
        }

        this._pdf.newNamedDestination(mapEntitiesToString(_text), this.getPage());
        if(CommonUtil.isNotBlank(_id)) this._pdf.newNamedDestination(_id, this.getPage());
        return _fsh;
    }

    public void placeSvg(String _ref, int _x, int _y, int _w, int _h, boolean _bg, String _layer)
    {
        boolean _doLayer = CommonUtil.toBooleanDefaultIfNull(_layer, true);
        PdfContent _cnt = _bg ? this.getBackground()
                : this.getPage().newContent();

        if(_doLayer)
        {
            _cnt.startLayer(_layer);
        }
        _cnt.save();

        if(SVG_REGISTRY.containsKey(_ref))
        {
            PdfSvgImage _svg = SVG_REGISTRY.get(_ref);
            _cnt.matrix(_w/_svg.getWidth(), 0, 0, _h/_svg.getHeight(), _x, _y);
            _cnt.form(_svg);
        }
        else
        if(IMG_REGISTRY.containsKey(_ref))
        {
            PdfImage _svg = IMG_REGISTRY.get(_ref);
            _cnt.matrix(_w,0,0,_h,_x,_y);
            _cnt.image(_svg);
        }
        else
        {
            throw new IllegalArgumentException(_ref);
        }

        _cnt.restore();
        if(_doLayer)
        {
            _cnt.endLayer();
        }
    }

    public void placeImage(String _ref, int _x, int _y, int _w, int _h, boolean _bg, String _layer)
    {
        boolean _doLayer = CommonUtil.toBooleanDefaultIfNull(_layer, true);
        PdfContent _cnt = _bg ? this.getBackground()
                : this.getPage().newContent();

        if(_doLayer)
        {
            _cnt.startLayer(_layer);
        }
        _cnt.save();

        PdfImage _img = IMG_REGISTRY.get(_ref);
        if(_img==null) throw new IllegalArgumentException(_ref);
        _cnt.matrix(_w,0,0,_h,_x,_y);
        _cnt.image(_img);

        _cnt.restore();
        if(_doLayer)
        {
            _cnt.endLayer();
        }
    }

    @SneakyThrows
    public void handlePageImage(PmlParserContext _xpc, XmlPullParser _xpp)
    {
        Properties _attributes = PmlParserUtil.toProperties(_xpp);

        int[] _pos = PmlParserUtil.toIntArray(getAttributeValueOrDefault(_attributes, "pos", "30,30"));
        int _w = CommonUtil.createInteger(getAttributeValueOrDefault(_attributes, "width", "535"));
        int _h = CommonUtil.createInteger(getAttributeValueOrDefault(_attributes, "height", "770"));
        float _s = CommonUtil.toFloat(getAttributeValueOrDefault(_attributes, "render", "-1"));
        boolean _bg = CommonUtil.toBoolean(getAttributeValueOrDefault(_attributes, "background", "false"));

        String _trans = getAttributeValueOrDefault(_attributes, "transparency", "false");
        boolean _t = CommonUtil.toBoolean(_trans);
        boolean _a = CommonUtil.toBoolean(_trans, "alpha");
        float _av = CommonUtil.toFloat(getAttributeValueOrDefault(_attributes, "alpha", "100"));

        int _rot = getAttributeValueOrDefaultAsInt(_attributes, "rotate", 0);

        String _type = getAttributeValueOrDefault(_attributes, "compress", "false");

        String _src = getAttributeValueOrNull(_attributes, "src");
        String _ref = getAttributeValueOrNull(_attributes, "ref");

        String _layer = _attributes.getProperty("layer", "false");

        if("svg".equalsIgnoreCase(_xpp.getName()) && CommonUtil.isNotBlank(_src))
        {
            loadSvg(_src, _src, _s, _type, _trans, _av, _rot, _xpc);
            placeSvg(_src, _pos[0], _pos[1], _w, _h, _bg, _layer);
        }
        else
        if("image".equalsIgnoreCase(_xpp.getName()) && CommonUtil.isNotBlank(_src))
        {
            loadImage(_src, _src, _type, _trans, _av, _rot, _xpc);
            placeImage(_src, _pos[0], _pos[1], _w, _h, _bg, _layer);
        }
        else
        if("svg".equalsIgnoreCase(_xpp.getName()))
        {
            placeSvg(_ref, _pos[0], _pos[1], _w, _h, _bg, _layer);
        }
        else
        if("image".equalsIgnoreCase(_xpp.getName()))
        {
            placeImage(_ref, _pos[0], _pos[1], _w, _h, _bg, _layer);
        }

        _xpp.next();
    }

    public float handlePageMarkdownBlockNode(Properties _attributes, PdfContent _cnt, Node _node, int _w, float _indent, float _px, float _py, float _last, boolean _first, String... _tags)
    {
        float _ysize = 0;
        String _layer = _attributes.getProperty("layer", "false");
        boolean _doLayer = CommonUtil.toBooleanDefaultIfNull(_layer, true);

        if(_doLayer)
        {
            _cnt.startLayer(_layer);
        }

        _cnt.save();
        if(_node instanceof Heading)
        {
            Heading _h = (Heading)_node;
            _ysize += handlePageMarkdownHeading(_attributes, _cnt, _h, _w, (int) _py, _px, _py, _first, _tags);
        }
        else
        if(_node instanceof Paragraph)
        {
            Paragraph _p = (Paragraph)_node;

            float _tmp = handlePageMarkdownParagraph(_attributes, _cnt, _p, _w, (int) _py, _indent, _px, _py, _tags);
            if(Float.isNaN(_tmp))
            {
                _ysize += -_last;
            }
            else
            {
                _ysize += _tmp;
            }
        }
        else
        if(_node instanceof BlockQuote)
        {
            BlockQuote _bq = (BlockQuote)_node;
            _ysize += handlePageMarkdownBlockQuote(_attributes, _cnt, _bq, _w, (int) _py, _indent, _px, _py, _last, _tags);
        }
        else
        if(_node instanceof OrderedList)
        {
            _ysize += handlePageMarkdownOrderedList(_attributes, _cnt, (OrderedList)_node, _w, (int) _py, _indent, _px, _py, _last, _tags);
        }
        else
        if(_node instanceof BulletList)
        {
            _ysize += handlePageMarkdownBulletList(_attributes, _cnt, (BulletList)_node, _w, (int) _py, _indent, _px, _py, _last, _tags);
        }
        else
        if(_node instanceof FencedCodeBlock)
        {
            _ysize += handlePageMarkdownFencedCodeBlock(_attributes, _cnt, (FencedCodeBlock)_node, _w, (int) _py, _indent, _px, _py, _tags);
        }
        else
        if(_node instanceof IndentedCodeBlock)
        {
            _ysize += handlePageMarkdownIndentedCodeBlock(_attributes, _cnt, (IndentedCodeBlock)_node, _w, (int) _py, _indent, _px, _py, _tags);
        }
        else
        if(_node instanceof TableBlock)
        {
            _ysize += handlePageMarkdownTableBlock(_attributes, _cnt, (TableBlock)_node, _px, _py, _tags);
        }
        else
        if(_node instanceof AdmonitionBlock)
        {
            _ysize += handlePageMarkdownAdmonitionBlock(_attributes, _cnt, (AdmonitionBlock)_node, _px, _py, _last, _tags);
        }
        else
        if(_node instanceof ThematicBreak)
        {
            _ysize += handlePageMarkdownThematicBreak(_attributes, _cnt, (ThematicBreak)_node, _px, _py, _w, _last, _tags);
        }
        else
            /*
            if(_node instanceof ThematicBreak)
            if(_node instanceof HtmlBlock)
            if(_node instanceof HtmlCommentBlock)
            if(_node instanceof Reference)
            */
        {
            log.warn("Err: MarkDown Block Node: " + _node.getNodeName());
            _cnt.startText();
            _cnt.moveText(_px, _py-20);
            _cnt.text(_node.getNodeName());
            _ysize += 20;
            _ysize += _cnt.paragraph(_node.getChars().toString(), -20, _w, 1000);
            _cnt.endText();
        }
        _cnt.restore();

        if(_doLayer)
        {
            _cnt.endLayer();
        }
        return _ysize;
    }

    public float handlePageMarkdownThematicBreak(Properties _attributes, PdfContent _cnt, ThematicBreak _node, float _px, float _py, int _w, float _last, String... _tags)
    {
        String[] _ntags = addSuffixAdd("break", _tags);

        String _color = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_COLOR, "#000", true, _ntags);

        int iLead = getAttributeValueOrPrefixAsInt(_attributes, "padding", 0, true, _ntags);

        int _width = getAttributeValueOrPrefixAsInt(_attributes, "line-width", 1, true, _ntags);

        if(_last==0) iLead = 0;

        _cnt.save();
        _cnt.strokeColor(_color);
        _cnt.linewidth(_width);
        _cnt.moveTo(_px, _py-(iLead/2));
        _cnt.lineTo(_px+_w, _py-(iLead/2));
        _cnt.stroke();
        _cnt.restore();

        return iLead;
    }

    static String[] addSuffixAdd(String s, String... tags)
    {
        String[] rtags = new String[tags.length+1];
        for(int _i = 0; _i<tags.length; _i++)
        {
            rtags[_i] = tags[_i]+"-"+s;
        }
        rtags[tags.length] = s;
        return rtags;
    }

    static String[] addSuffixAppendAdd(String s, String... tags)
    {
        String[] rtags = new String[(tags.length*2)+1];
        for(int _i = 0; _i<tags.length; _i++)
        {
            rtags[_i] = tags[_i]+"-"+s;
        }
        rtags[tags.length] = s;
        for(int _i = 0; _i<tags.length; _i++)
        {
            rtags[_i+tags.length+1] = tags[_i];
        }
        return rtags;
    }

    static String[] addSuffix(String s, String... tags)
    {
        String[] rtags = new String[tags.length];
        for(int _i = 0; _i<tags.length; _i++)
        {
            rtags[_i] = tags[_i]+"-"+s;
        }
        return rtags;
    }

    static String[] addSuffixAppend(String s, String... tags)
    {
        String[] rtags = new String[tags.length*2];
        for(int _i = 0; _i<tags.length; _i++)
        {
            rtags[_i] = tags[_i]+"-"+s;
        }
        System.arraycopy(tags,0,rtags,tags.length,tags.length);
        return rtags;
    }

    static String[] append(String s, String... tags)
    {
        String[] rtags = new String[tags.length+1];
        System.arraycopy(tags,0,rtags,0,tags.length);
        rtags[rtags.length-1] = s;
        return rtags;
    }

    public float handlePageMarkdownAdmonitionBlock(Properties _attributes, PdfContent _cnt, AdmonitionBlock _n, float _px, float _py, float _last, String... _tags)
    {
        String[] _ntags = addSuffixAdd("admonition", _tags);

        float _w = getAttributeValueOrPrefixAsFloat(_attributes, PmlParserUtil.PROP_WIDTH, true, _ntags);
        String _qualifier = _n.getInfo().toString();
        String[] _xtags = addSuffixAppendAdd(_qualifier, _ntags);
        String _title = _n.getTitle().toString(); // title text

        float _ysize = 0;

        int blockPadding = getAttributeValueOrPrefixAsInt(_attributes, "block-padding", 10, false, _xtags);

        if(this.isIcon(_qualifier))
        {
            _title = "&space;&"+_qualifier+";&space;&space;"+_title;
        }
        else
        if("example".equalsIgnoreCase(_qualifier))
        {
            _title = "&bullet;&space;Example"+(CommonUtil.isBlank(_title) ? "" : "&space;&emdash;&space;"+_title);
        }
        _ysize += layoutAndRenderText(_px, _py, _attributes, _cnt, _title, false, SOURCE_TYPE_MARKDOWN, true, _xtags);
        _ysize += blockPadding;
        _last = blockPadding;

        boolean _first = true;
        for(Node _node : _n.getChildren()) 
        {
            float _tmp = handlePageMarkdownBlockNode(_attributes, _cnt, _node, (int) (_w-5), 0, _px+5, _py-_ysize, _last, _first, _xtags);
            if(_tmp>0f)
            {
                _ysize += _tmp;
                _ysize += blockPadding;
                _last = blockPadding;
            }
            _first = false;
        }
        _ysize -= blockPadding;

        String[] _btags = addSuffix("base", _xtags);
        renderConditionalBackground(_attributes, _px, _py, (int) _w, _ysize, true, _btags);
        renderConditionalBorder(_attributes, _px, _py, (int) _w, _ysize, true, _btags);

        _ysize += blockPadding;
        return _ysize;
    }

    public float handlePageMarkdownTableBlock(Properties _attributes, PdfContent _cnt, TableBlock _n, float _px, float _py, String... _tags)
    {
        List<AbstractTableCell> _list = new Vector<>();
        _tags = addSuffix("table", _tags);
        int _fs = getAttributeValueOrPrefixAsInt(_attributes, PmlParserUtil.PROP_FONT_SIZE, false, _tags);

        List<Float> _widths = new Vector<>();
        for(Node _node : _n.getChildren())
        {
            if(_node instanceof TableHead)
            {
                collectPageMarkdownTableHead(_list, _attributes, (TableHead) _node, _tags);
            }
            else
            if(_node instanceof TableBody)
            {
                collectPageMarkdownTableBody(_list, _attributes, (TableBody) _node, _tags);
            }
            else
            if(_node instanceof TableSeparator)
            {
                for(Node _tn : _node.getFirstChild().getChildren())
                {
                    _widths.add((float) ((Text)_tn.getFirstChild()).getTextLength());
                }
            }
            else
            {
                log.warn(_node.getNodeName());
            }
        }

        try
        {
            float _ysize = layoutAndRenderTable(_attributes, _list, (int)_px, (int)_py, _widths, true, _tags);
            return _ysize;
        }
        catch(Exception _xe)
        {
            log.error("Error in table layout at \""+_list.get(0).toString()+" ....\"",_xe);
        }
        return 0;
    }

    public void collectPageMarkdownTableHead(List<AbstractTableCell> _list, Properties _attributes, TableHead _n, String... _tags)
    {
        _tags = addSuffix("head", _tags);
        for(Node _node : _n.getChildren())
        {
            if(_node instanceof TableRow)
            {
                collectPageMarkdownTableRow(_list, _attributes, (TableRow) _node,_tags);
            }
            else
            {
                log.warn(_node.getNodeName());
            }
        }
    }

    public void collectPageMarkdownTableRow(List<AbstractTableCell> _list, Properties _attributes, TableRow _n, String... _tags)
    {
        _tags = addSuffix("row", _tags);
        for(Node _node : _n.getChildren())
        {
            if(_node instanceof TableCell)
            {
                collectPageMarkdownTableCell(_list, _attributes, (TableCell) _node, _tags);
            }
            else
            {
                log.warn(_node.getNodeName());
            }
        }
    }

    public void collectPageMarkdownTableCell(List<AbstractTableCell> _list, Properties _attributes, TableCell _n, String... _tags)
    {
        _tags = addSuffix("cell", _tags);
        Properties _cattr = new Properties();
        //_cattr.putAll(_attributes);

        String _align = _n.getAlignment()==null ? "justified" : _n.getAlignment().name().toLowerCase();

        if(_n.isHeader() && _n.getAlignment()==null)
        {
            String _text = _n.getChars().toString();
            _text = _text.substring(_n.getOpeningMarker().length(), _text.length()-_n.getClosingMarker().length());
            int _pstart = CommonUtil.countPrefix(_text, ' ')/2;
            int _pend = CommonUtil.countSuffix(_text, ' ')/2;
            if(_pstart==_pend)
            {
                _align = "center";
            }
            else
            if(_pstart>_pend)
            {
                _align = "right";
            }
            else
            {
                _align = "left";
            }
        }

        TableFormatCell _tfc = TableFormatCell.builder()
                .attributes(_cattr)
                .source(SOURCE_TYPE_MARKDOWN)
                .span(_n.getSpan())
                .cells(new Vector<>())
                .header(_n.isHeader())
                .align(_align)
                .build();

        int _fs = getAttributeValueOrPrefixAsInt(_attributes, PmlParserUtil.PROP_FONT_SIZE, false, _tags);
        markdownNodesToCells(_attributes,true, _n.getChildren(), _tfc.getCells(), false, _fs, false, _n.isHeader(), _tags);
        _list.add(_tfc);
    }

    public void collectPageMarkdownTableBody(List<AbstractTableCell> _list, Properties _attributes, TableBody _n, String... _tags)
    {
        _tags = addSuffix("body", _tags);
        for(Node _node : _n.getChildren())
        {
            if(_node instanceof TableRow)
            {
                collectPageMarkdownTableRow(_list, _attributes, (TableRow) _node, _tags);
            }
            else
            {
                log.warn(_node.getNodeName());
            }
        }
    }

    public float handlePageMarkdownIndentedCodeBlock(Properties _attributes, PdfContent _cnt, IndentedCodeBlock _n, int _w, int _h, float _indent, float _px, float _py, String... _tags)
    {
        _tags = addSuffix("code-indented", _tags);
        return layoutAndRenderMarkdown(_attributes, _cnt, _n, -1, _h, _indent+BASE_INDENT, false, true, true, _px, _py, _tags);
    }

    public float handlePageMarkdownFencedCodeBlock(Properties _attributes, PdfContent _cnt, FencedCodeBlock _n, int _w, int _h, float _indent, float _px, float _py, String... _tags)
    {
        float _hsize = 0;
        _tags = addSuffix("code-fenced", _tags);
        for(Node _node : _n.getChildren())
        {
            _hsize += layoutAndRenderMarkdown(_attributes, _cnt, _node, -1, _h, _indent+BASE_INDENT, false, true, true, _px, _py-_hsize, _tags);
        }
        return _hsize;
    }

    public float handlePageMarkdownListItem(boolean _isBullet, Properties _attributes, PdfContent _cnt, ListItem _n, int _w, int _h, float _indent, float _px, float _py, float _last, String... _tag)
    {
        float _hsize = 0;

        String _color = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_COLOR, false, _tag);
        String _fn = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_FONT_FACE, false, _tag);
        int _fs = getAttributeValueOrPrefixAsInt(_attributes, PmlParserUtil.PROP_FONT_SIZE, false, _tag);
        float _ls = getAttributeValueOrPrefixAsFloat(_attributes, PmlParserUtil.PROP_FONT_LEAD, false, _tag);
        float _bindent = getAttributeValueOrDefaultAsFloat(_attributes, "indent", BASE_INDENT, true, _tag);
        if(_ls<0) _ls = (-_ls)*_fs;

        PdfFont _font = this._reg.lookupFont(_fn);
        _cnt.save();
        _cnt.startText();
        _cnt.moveText(_px+_indent, _py-_fs);
        _cnt.fillColor(_color);
        _cnt.setFont(_font, _fs);
        _cnt.font(_font, _fs);

        String _marker = _n.getOpeningMarker().toString();
        if(_isBullet && "*".equalsIgnoreCase(_marker))
        {
            _cnt.text(htmlEntityToChar("&bullet;").toString());
        }
        else
        if(_isBullet && "-".equalsIgnoreCase(_marker))
        {
            _cnt.text(htmlEntityToChar("&emdash;").toString());
        }
        else
        if(_isBullet && "+".equalsIgnoreCase(_marker))
        {
            _cnt.text(htmlEntityToChar("&dagger;").toString());
        }
        else
        {
            _cnt.text(_marker);
        }
        _indent+=_bindent;

        _cnt.endText();
        _cnt.restore();
        boolean _first = true;
        int _padding = getAttributeValueOrPrefixAsInt(_attributes, "item-padding", 5, false, _tag);
        for(Node _node : _n.getChildren())
        {
            float _tmp = handlePageMarkdownBlockNode(_attributes, _cnt, _node, _w, _indent, _px, _py-_hsize, _last, _first, _tag);
            if(_tmp>0f)
            {
                _hsize += _tmp;
                _hsize += _padding;
                _last = _padding;
            }
            _first = false;
        }
        _hsize -= (_padding/2);
        return _hsize;
    }

    public float handlePageMarkdownOrderedList(Properties _attributes, PdfContent _cnt, OrderedList _n, int _w, int _h, float _indent, float _px, float _py, float _last, String... _tags)
    {
        float _hsize = 0;
        _tags = addSuffix("list-ordered", _tags);

        for(Node _node : _n.getChildren())
        {
            if(_node instanceof OrderedListItem)
            {
                _hsize += handlePageMarkdownListItem(false, _attributes, _cnt, (ListItem) _node, _w, _h, _indent, _px, _py-_hsize, _last, _tags);
            }
            else
            {
                log.warn(_node.getNodeName());
            }
        }
        return _hsize;
    }

    public float handlePageMarkdownBulletList(Properties _attributes, PdfContent _cnt, BulletList _n, int _w, int _h, float _indent, float _px, float _py, float _last, String... _tags)
    {
        float _hsize = 0;
        _tags = addSuffix("list-bullet", _tags);
        for(Node _node : _n.getChildren())
        {
            if(_node instanceof BulletListItem)
            {
                _hsize += handlePageMarkdownListItem(true, _attributes, _cnt, (ListItem)_node, _w, _h, _indent, _px, _py-_hsize, _last, _tags);
            }
            else
            {
                log.warn(_node.getNodeName());
            }
        }
        return _hsize;
    }

    public float handlePageMarkdownBlockQuote(Properties _attributes, PdfContent _cnt, BlockQuote _bq, int _w, int _h, float _indent, float _px, float _py, float _last, String... _tags)
    {
        float _hsize = 0;
        _tags = addSuffix("blockquote", _tags);

        int blockPadding = getAttributeValueOrPrefixAsInt(_attributes, "block-padding", 10, false, _tags);

        boolean _first = true;
        for(Node _n : _bq.getChildren())
        {
            float _tmp = handlePageMarkdownBlockNode(_attributes, _cnt, _n, (_w-5), 0, _px+5, _py-_hsize, _last, _first, _tags);
            if(_tmp>0f)
            {
                _hsize += _tmp;
                _hsize += blockPadding;
                _last = blockPadding;
            }
            _first = false;
        }

        _hsize -= blockPadding;

        renderConditionalBackground(_attributes, _px, _py, _w, _hsize, true, addSuffixAppend("base", _tags));
        renderConditionalBorder(_attributes, _px, _py, _w, _hsize, true, addSuffixAppend("base", _tags));


        return _hsize;
    }

    public PdfContent getBackground()
    {
        if(this._background == null)
        {
            this._background = this.getPage().prependContent(true);
        }
        return this._background;
    }

    public float handlePageMarkdown(PmlParserContext _xpc, XmlPullParser _xpp) throws IOException, XmlPullParserException
    {
        String _tag = "markdown";
        Properties _attributes = PmlParserUtil.toPropertiesWithPrefix(_tag, _xpp);
        resolveFromClassDefinitionWithPrefix(_tag, _attributes);

        int[] _pos = PmlParserUtil.toIntArray(getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_POSITION, false, _tag));
        int _w = getAttributeValueOrPrefixAsInt(_attributes, PmlParserUtil.PROP_WIDTH, false, _tag);

        StringBuilder _sb = new StringBuilder();
        String _src = getAttributeValueOrPrefix(_attributes, "src", false, _tag);

        int _event = -1;
        while((_event = _xpp.next())==XmlPullParser.TEXT)
        {
            String _text = _xpp.getText();
            _sb.append(_text);
        }

        if(_src!=null && !"".equalsIgnoreCase(_src))
        {
            readFileInto(_xpc, _src, _sb);
        }

        return layoutAndRenderMarkdown(_attributes, _pos[0], _pos[1], _w, _sb.toString(), _tag);
    }

    public float layoutAndRenderMarkdown(Properties _attributes, int _px, int _py, int _w, String _document, String... _tags)
    {
        float _hsize = 0;
        Node _node = parseMarkdown(new StringReader(_document));
        _hsize += processMarkdown(_attributes, _px, _py, _w, _node, _tags);
        return _hsize;
    }

    public float processMarkdown(Properties _attributes, int _px, int _py, int _w, Node _document, String... _tags)
    {
        float _hsize = 0;
        String _color = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_COLOR, false, _tags);
        String _fn = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_FONT_FACE, false, _tags);
        int _fs = getAttributeValueOrPrefixAsInt(_attributes, PmlParserUtil.PROP_FONT_SIZE, false, _tags);
        float _ls = getAttributeValueOrPrefixAsFloat(_attributes, PmlParserUtil.PROP_FONT_LEAD, false, _tags);
        if(_ls<0) _ls = (-_ls)*_fs;
        int blockPadding = getAttributeValueOrPrefixAsInt(_attributes, "block-padding", 10, false, _tags);

        String _id = getAttributeValueOrNull(_attributes, "id");
        if(CommonUtil.isNotBlank(_id))
        {
            this._pdf.newNamedDestination(_id, this.getPage());
        }

        Deque<Node> _q = new ArrayDeque<Node>();
        for(Node _n : _document.getChildren())
        {
            _q.add(_n);
        }

        PdfFont _font = this._reg.lookupFont(_fn);
        PdfContent _cnt = null;

        _cnt = this.getPage().newContent();
        String _layer = _attributes.getProperty("layer", "false");
        boolean _doLayer = CommonUtil.checkBoolean(_layer);
        if(_doLayer)
        {
            _cnt.startLayer(_layer);
        }

        _cnt.save();

        boolean _first = true;
        float _last = 0;
        while(!_q.isEmpty())
        {
            Node _node = _q.poll();

            float _tmp = handlePageMarkdownBlockNode(_attributes, _cnt, _node, _w, 0, _px, _py-_hsize, _last, _first, _tags);;
            if(_tmp>0f)
            {
                _hsize += _tmp;
                _hsize += blockPadding;
                _last = blockPadding;
            }
        }
        _hsize -= _last;
        _cnt.restore();

        if(_doLayer)
        {
            _cnt.endLayer();
        }
        String _link = getAttributeValueOrNull(_attributes, "link");
        if(CommonUtil.isNotBlank(_link))
        {
            this.getPage().newNamedLink(_link, _px, (int)(_py-_hsize), _w, (int)_hsize);
        }

        return _hsize;
    }

    @SneakyThrows
    public Node parseMarkdown(Reader stringReader)
    {
        MutableDataSet options = new MutableDataSet();

        // TODO : make document-level and defaults-level configure !!!
        options.set(Parser.EXTENSIONS, Arrays.asList(
                AttributesExtension.create(),
                AdmonitionExtension.create(),
                TypographicExtension.create(),
                TablesExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build();

        // You can re-use parser and renderer instances
        return parser.parseReader(stringReader);
    }

    @SneakyThrows
    private void readFileInto(PmlParserContext _xpc, String _src, StringBuilder _sb)
    {
        ResourceLoader _rl = PmlUtil.sourceToLoader(_src, _xpc.getBasedir(), _xpc.getFile().getParentFile(), this.ZIP_MOUNTS, this.DIR_MOUNTS);
        BufferedReader _br = new BufferedReader(new InputStreamReader(_rl.getInputStream()));
        _br.lines().forEach((_line) -> { _sb.append(_line);_sb.append("\n"); });
        _br.close();
    }

    public Character htmlEntityToChar(String _name)
    {
        if(_name.startsWith("&"))
        {
            _name = _name.substring(1);
        }

        if(_name.endsWith(";"))
        {
            _name = _name.substring(0, _name.length()-1);
        }

        _name = _name.trim();

        if(_name.startsWith("#x"))
        {
            return Character.valueOf((char) CommonUtil.createInteger("0"+(_name.substring(1))).intValue());
        }
        else
        if(_name.startsWith("#"))
        {
            return Character.valueOf((char) CommonUtil.createInteger(_name.substring(1)).intValue());
        }
        else
        if(this.HTML_ENTITIES.containsKey(_name))
        {
            String _code = this.HTML_ENTITIES.getProperty(_name);
            int _v = CommonUtil.createInteger(_code);
            return Character.valueOf((char) _v);
        }
        else
        {
            return Character.valueOf((char) 0xfffd);
        }
    }

    public float handlePageMarkdownParagraph(Properties _attributes, PdfContent _cnt, Paragraph _p, int _w, int _h, float _indent, float _px, float _py, String... _tags)
    {
        if(_p.getFirstChild().equals(_p.getLastChild())
            && _p.getFirstChild() instanceof AttributesNode)
        {
            for(Node _node : ((AttributesNode)_p.getLastChild()).getChildren())
            {
                AttributeNode _attr = (AttributeNode) _node;
                String _key = _attr.getName().toString();
                String _val = _attr.getValue().toString().trim();
                if(".".equalsIgnoreCase(_key))
                {
                    _attributes.setProperty("id", _val);
                }
                else
                if(_key.startsWith(_tags[0]))
                {
                    _attributes.setProperty(_key, _val);
                }
                else
                {
                    _attributes.setProperty(_tags[0]+"-"+_key, _val);
                }
            }
            return Float.NaN;
        }
        else
        {
            String[] _ntags = addSuffix("paragraph", _tags);

            return layoutAndRenderMarkdown(_attributes, _cnt, _p, _w, _h, _indent, true, false, false, _px, _py, _ntags);
        }
    }

    public float layoutAndRenderMarkdown(Properties _pattributes, PdfContent _cnt, Node _n, int _w, int _h, float _indent, boolean _breakUp, boolean _forceAlign, boolean _breakCell, float _px, float _py, String... _tags)
    {
        Properties _attributes = new Properties();
        _attributes.putAll(_pattributes);

        String _color = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_COLOR, false, _tags);
        String _align = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_ALIGN, false, _tags);
        boolean _hyphen = getAttributeValueOrPrefixAsBool(_attributes, PmlParserUtil.PROP_HYPHENATE, false, false, _tags);
        int _fs = getAttributeValueOrPrefixAsInt(_attributes, PmlParserUtil.PROP_FONT_SIZE, false, _tags);
        float _hscale = getAttributeValueOrDefaultAsFloat(_attributes, "font-scale", 100f, true, _tags);
        float _ls = getAttributeValueOrPrefixAsFloat(_attributes, PmlParserUtil.PROP_FONT_LEAD, false, _tags);
        if(_ls<0) _ls = (-_ls)*_fs;

        List<Cell> _ccq = new ArrayList<>();
        boolean _spaceBefore = false;
        if(_n.hasChildren())
        {
            markdownNodesToCells(_attributes, _breakUp, _n.getChildren(), _ccq, _spaceBefore, _fs, _breakCell, false, _tags);
        }
        else
        {
            markdownNodeToCells(_attributes, _breakUp, _n, _ccq, _spaceBefore, _fs, _breakCell, false, _tags);
        }

        String _org_color = _color;
        if(_n.getLastChild() instanceof AttributesNode)
        {
            for(Node _node : ((AttributesNode)_n.getLastChild()).getChildren())
            {
                AttributeNode _attr = (AttributeNode) _node;
                String _key = _attr.getName().toString();
                String _val = _attr.getValue().toString().trim();
                if(_key.startsWith(_tags[0]))
                {
                    _attributes.setProperty(_key, _val);
                }
                else
                {
                    _attributes.setProperty(_tags[0]+"-"+_key, _val);
                }
            }
        }
        _color = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_COLOR, false, _tags);

        float _ysize = 0;
        _cnt.save();
        _cnt.startText();
        _cnt.fillColor(_color);

        List<Cell[]> _clines = layoutCells(_ccq, _w-_indent, _hscale, _hyphen, _align, SOURCE_TYPE_MARKDOWN, _breakCell);

        _cnt.moveText(_px+_indent,_py-_fs);
        _ysize += renderCells(_clines, _cnt, _w-_indent, 0, _ls, _hscale, _align, _forceAlign);
        _cnt.endText();
        _cnt.restore();

        renderConditionalBackground(_attributes, _px, _py, _w, _ysize, false, _tags);
        renderConditionalBorder(_attributes, _px, _py, _w, _ysize, false, _tags);

        return _ysize;
    }

    public boolean markdownNodeToCells(Properties _attributes, boolean _breakUp, Node _node, List<Cell> _ccq, boolean _spaceBefore, int _fs, boolean _breakCell, boolean _isheader, String... _tags)
    {
        if(_isheader)
        {
            _tags = addSuffix("strong", _tags);
        }
        if(_node instanceof Text)
        {
            _tags = addSuffix("text", _tags);
            String _fn = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_FONT_FACE, false, _tags);
            float _hscale = getAttributeValueOrDefaultAsFloat(_attributes, "font-scale", 100f, true, _tags);
            String _text = _node.getChars().toString();
            if(_breakCell)
            {
                boolean _firstLine = true;
                for(String _part : _text.split("\n"))
                {
                    Cell _cl = TextCell.builder()
                            .fontFace(_fn)
                            .fontSize(_fs)
                            .text(_part)
                            .spaceBefore(_firstLine ? _spaceBefore : true)
                            .build();
                    if(_breakUp)
                    {
                        _ccq.addAll(_cl.breakup(this._reg, -1, _hscale));
                    }
                    else
                    {
                        _ccq.add(_cl);
                    }
                    _ccq.add(new BreakCell());
                    _spaceBefore = _part.endsWith(" ") || _part.endsWith("\n");
                    _firstLine = false;
                }
            }
            else
            {
                Cell _cl = TextCell.builder()
                        .fontFace(_fn)
                        .fontSize(_fs)
                        .text(_text)
                        .spaceBefore(_spaceBefore)
                        .build();
                if(_breakUp)
                {
                    _ccq.addAll(_cl.breakup(this._reg, -1, _hscale));
                }
                else
                {
                    _ccq.add(_cl);
                }
                _spaceBefore = _text.endsWith(" ") || _text.endsWith("\n");
            }
        }
        else
        if(_node instanceof IndentedCodeBlock)
        {
            _tags = addSuffix("code-indented", _tags);
            String _fn = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_FONT_FACE, false, _tags);
            float _hscale = getAttributeValueOrDefaultAsFloat(_attributes, "font-scale", 100f, true, _tags);
            String _text = _node.getChars().toString();
            if(_breakCell)
            {
                for(String _part : _text.split("\n"))
                {
                    Cell _cl = TextCell.builder()
                            .fontFace(_fn)
                            .fontSize(_fs)
                            .text(_part)
                            .spaceBefore(_spaceBefore)
                            .build();
                    if(_breakUp)
                    {
                        _ccq.addAll(_cl.breakup(this._reg, -1, _hscale));
                    }
                    else
                    {
                        _ccq.add(_cl);
                    }
                    _ccq.add(new BreakCell());
                    _spaceBefore = _part.endsWith(" ") || _part.endsWith("\n");
                }
            }
        }
        else
        if(_node instanceof Code)
        {
            _tags = addSuffix("code", _tags);
            String _fn = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_FONT_FACE, false, _tags);
            float _hscale = getAttributeValueOrDefaultAsFloat(_attributes, "font-scale", 100f, true, _tags);
            String _text = _node.getChars().toString();
            for(String _part : _text.split("\n"))
            {
                Cell _cl = TextCell.builder()
                        .fontFace(_fn)
                        .fontSize(_fs)
                        .text(_part)
                        .spaceBefore(_spaceBefore)
                        .build();
                if(_breakUp)
                {
                    _ccq.addAll(_cl.breakup(this._reg, -1, _hscale));
                }
                else
                {
                    _ccq.add(_cl);
                }
                _spaceBefore = _text.endsWith(" ") || _text.endsWith("\n");
            }
        }
        else
        if(_node instanceof HtmlEntity)
        {
            String _fn = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_FONT_FACE, false, _tags);
            String _ent = ((HtmlEntity) _node).getChars().toString();
            _ccq.add(HtmlEntityTextCell.builder()
                    .entity(_ent)
                    .character(htmlEntityToChar(_ent))
                    .textFace(_fn)
                    .fontFace(_fn)
                    .fontSize(_fs)
                    .spaceBefore(_spaceBefore)
                    .build());
            _spaceBefore = false;
        }
        else
        if(_node instanceof Emphasis)
        {
            for(Node _nx : _node.getChildren())
            {
                _spaceBefore = markdownNodeToCells(_attributes, _breakUp, _nx, _ccq, _spaceBefore, _fs, _breakCell, _isheader, addSuffix("emphasis",  _tags));
            }
        }
        else
        if(_node instanceof StrongEmphasis)
        {
            for(Node _nx : _node.getChildren())
            {
                _spaceBefore = markdownNodeToCells(_attributes, _breakUp, _nx, _ccq, _spaceBefore, _fs, _breakCell, _isheader, addSuffix("strong",  _tags));
            }
        }
        else
        if(_node instanceof TypographicQuotes)
        {
            String _fn = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_FONT_FACE, false, _tags);
            TypographicQuotes _tq = ((TypographicQuotes)_node);
            String _open = _tq.getOpeningMarker().toString();

            String _ent1 = _tq.getTypographicOpening();
            String _ent2 = _tq.getTypographicClosing();

            _ccq.add(HtmlEntityTextCell.builder()
                    .entity(_ent1)
                    .character(htmlEntityToChar(_ent1))
                    .textFace(_fn)
                    .fontFace(_fn)
                    .fontSize(_fs)
                    .spaceBefore(_spaceBefore)
                    .build());
            _spaceBefore = false;

            for(Node _nx : _node.getChildren())
            {
                _spaceBefore = markdownNodeToCells(_attributes, _breakUp, _nx, _ccq, _spaceBefore, _fs, _breakCell, _isheader, _tags);
            }

            _ccq.add(HtmlEntityTextCell.builder()
                    .entity(_ent2)
                    .character(htmlEntityToChar(_ent2))
                    .textFace(_fn)
                    .fontFace(_fn)
                    .fontSize(_fs)
                    .spaceBefore(_spaceBefore)
                    .build());
            _spaceBefore = false;
        }
        else
        if(_node instanceof TypographicSmarts)
        {
            String _fn = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_FONT_FACE, false, _tags);
            TypographicSmarts _tq = ((TypographicSmarts)_node);
            String _ent = _tq.getTypographicText();

            _ccq.add(HtmlEntityTextCell.builder()
                    .entity(_ent)
                    .character(htmlEntityToChar(_ent))
                    .textFace(_fn)
                    .fontFace(_fn)
                    .fontSize(_fs)
                    .spaceBefore(_spaceBefore)
                    .build());
            _spaceBefore = false;
        }
        else
        if(_node instanceof HtmlInline)
        {
            _tags = addSuffix("html", _tags);
            String _fn = getAttributeValueOrPrefix(_attributes, "strong-"+PmlParserUtil.PROP_FONT_FACE, false, _tags);
            float _hscale = getAttributeValueOrDefaultAsFloat(_attributes, "strong-font-scale", 100f, true, _tags);
            Cell _cl = TextCell.builder()
                    .fontFace(_fn)
                    .fontSize(_fs)
                    .text(_node.getChars().toString())
                    .spaceBefore(_spaceBefore)
                    .build();
            if(_breakUp)
            {
                _ccq.addAll(_cl.breakup(this._reg, -1, _hscale));
            }
            else
            {
                _ccq.add(_cl);
            }
            _spaceBefore = true;
        }
        else
        if(_node instanceof LinkRef)
        {
            _tags = addSuffix("link", _tags);
            String _fn = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_FONT_FACE, false, _tags);
            float _hscale = getAttributeValueOrDefaultAsFloat(_attributes, "font-scale", 100f, true, _tags);
            Cell _cl = TextCell.builder()
                    .fontFace(_fn)
                    .fontSize(_fs)
                    .text("["+((LinkRef)_node).getReference().toString()+"]")
                    .spaceBefore(_spaceBefore)
                    .build();
            if(_breakUp)
            {
                _ccq.addAll(_cl.breakup(this._reg, -1, _hscale));
            }
            else
            {
                _ccq.add(_cl);
            }
            _spaceBefore = true;
        }
        else
        if(_node instanceof SoftLineBreak)
        {
            if(_breakCell) _ccq.add(new BreakCell());
            _spaceBefore = true;
        }
        else
        if(_node instanceof HardLineBreak)
        {
            if(_breakCell) _ccq.add(new BreakCell());
            _spaceBefore = false;
        }
        else
        if(_node instanceof AttributesNode)
        {
            // IGNORE
        }
        else
        {
            log.warn("Err: markdownNodeToCells Node: "+_node.getNodeName());
            String _fn = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_FONT_FACE, false, _tags);
            float _hscale = getAttributeValueOrDefaultAsFloat(_attributes, "font-scale", 100f, true, _tags);
            String _text = _node.getChars().toString();
            Cell _cl = TextCell.builder()
                    .fontFace(_fn)
                    .fontSize(_fs)
                    .text(_node.getNodeName()+" "+_text.trim())
                    .spaceBefore(true)
                    .build();
            if(_breakUp)
            {
                _ccq.addAll(_cl.breakup(this._reg, -1, _hscale));
            }
            else
            {
                _ccq.add(_cl);
            }
            _spaceBefore = _text.endsWith(" ") || _text.endsWith("\n");
        }
        return _spaceBefore;
    }

    public void markdownNodesToCells(Properties _attributes, boolean _breakUp, ReversiblePeekingIterable<Node> _children, List<Cell> _ccq, boolean _spaceBefore, int _fs, boolean _breakCell, boolean _isheader, String... _tags)
    {
        for(Node _node : _children)
        {
            _spaceBefore = markdownNodeToCells(_attributes, _breakUp, _node, _ccq, _spaceBefore, _fs, _breakCell, _isheader, _tags);
        }
    }


    public float handlePageMarkdownHeading(Properties _attributes, PdfContent _cnt, Heading _p, int _w, int _h, float _px, float _py, boolean _first, String... _tag)
    {
        _tag = addSuffix("heading", _tag);

        String _color = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_COLOR, false, _tag);
        String _fnb = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_FONT_FACE, false, _tag);
        int _fs = getAttributeValueOrPrefixAsInt(_attributes, PmlParserUtil.PROP_FONT_SIZE, false, _tag);
        boolean _hyphen = getAttributeValueOrPrefixAsBool(_attributes, PmlParserUtil.PROP_HYPHENATE, false, _tag);

        int _fsh = (int)(_fs * (1f + 0.2f*(6-_p.getLevel())));

        float _ls = getAttributeValueOrPrefixAsFloat(_attributes, PmlParserUtil.PROP_FONT_LEAD, false, _tag);
        float _hscale = getAttributeValueOrDefaultAsFloat(_attributes, "font-scale", 100f, false, _tag);
        if(_ls<0) _ls = (-_ls)*_fsh;

        List<Cell> _ccq = new ArrayList<>();
        boolean _spaceBefore = false;
        markdownNodesToCells(_attributes,true, _p.getChildren(), _ccq, _spaceBefore, _fsh, false, true, _tag);

        String _org_color = _color;
        if(_p.getLastChild() instanceof AttributesNode)
        {
            for(Node _node : ((AttributesNode)_p.getLastChild()).getChildren())
            {
                AttributeNode _attr = (AttributeNode) _node;
                String _key = _attr.getName().toString();
                String _val = _attr.getValue().toString().trim();
                if(_key.startsWith(_tag[0]))
                {
                    _attributes.setProperty(_key, _val);
                }
                else
                {
                    _attributes.setProperty(_tag[0]+"-"+_key, _val);
                }
            }
        }

        String _outline = getAttributeValueOrPrefix(_attributes, "outline", true, _tag);
        if(CommonUtil.isBlank(_outline))
        {
            _outline = _p.getText().toString();
        }

        if(_p.getLevel()==1)
        {
            this.addChapter(mapEntitiesToString(_outline));
        }
        else
        if(_p.getLevel()==2)
        {
            this.addSection(mapEntitiesToString(_outline));
        }
        else
        if(_p.getLevel()==3)
        {
            this.addSubSection(mapEntitiesToString(_outline));
        }
        else
        if(_p.getLevel()==4)
        {
            this.addSubSubSection(mapEntitiesToString(_outline));
        }
        else
        {
            this.addOutline(mapEntitiesToString(_outline));
        }

        _color = getAttributeValueOrPrefix(_attributes, PmlParserUtil.PROP_COLOR, true, _tag);
        _cnt.save();
        _cnt.startText();
        _cnt.fillColor(_color);

        List<Cell[]> _clines = layoutCells(_ccq, _w, _hscale, _hyphen, "left", SOURCE_TYPE_MARKDOWN, false);

        float _hsize = 0;
        _cnt.moveText(_px, _py-_fsh);
        _hsize += renderCells(_clines, _cnt, _w, 0, _ls, _hscale, "left", false);
        _cnt.endText();
        _cnt.restore();
        return _hsize;
    }


    public List<Cell[]> layoutCells(List<Cell> _ccq, double _w, float _hscale, boolean _hyphen, String _align, String _sourceType, boolean _breakCell)
    {
        List<Cell[]> _ret = new LinkedList<>();

        if(SOURCE_TYPE_TEXT.equalsIgnoreCase(_sourceType)
                || SOURCE_TYPE_MARKDOWN.equalsIgnoreCase(_sourceType))
        {
            mapEntitiesToCells(_ccq, HTML_ENTITY_PATTERN, 1, 1);
        }

        mapEntitiesToCells(_ccq, MAP_ENTITY_PATTERN, 2, 1);

        if(_w<0)
        {
            if(_breakCell)
            {
                List<Cell> _line = new Vector<>();
                for(Cell _c : _ccq)
                {
                    if(_c instanceof BreakCell)
                    {
                        _ret.add(_line.toArray(new Cell[_line.size()]));
                        _line = new Vector<>();
                    }
                    else
                    {
                        _line.add(_c);
                    }
                }
                if (_line.size()>0) {
                    _ret.add((Cell[]) _line.toArray());
                }
            }
            else
            {
                Cell[] _line = new Cell[_ccq.size()];
                _line = _ccq.toArray(_line);
                _ret.add(_line);
            }
            return _ret;
        }

        while(!_ccq.isEmpty())
        {
            double _lw = _ccq.get(0).width(this._reg, _hscale, true);
            int _i;
            for (_i = 1; _i < _ccq.size(); _i++) {
                double _cw = _ccq.get(_i).width(this._reg, _hscale, false);
                if ((_lw + _cw) > _w) {
                    break;
                }
                _lw+=_cw;
            }

            Cell[] _line = null;
            if((_hyphen && _i < _ccq.size())
                    && _ccq.get(_i).canHypenate(this._reg, false, _w-_lw, _hscale, null))
            {
                Cell[] _c = _ccq.remove(_i).hypenate(this._reg, false, _w-_lw, _hscale, null);
                _ccq.add(_i, _c[1]);

                _line = new Cell[_i+1];
                _line[_i] = _c[0];
            }
            else
            {
                _line = new Cell[_i];
            }

            for (int _j = 0; _j < _i; _j++) {
                _line[_j] = _ccq.remove(0);
            }

            _ret.add(_line);
        }
        return _ret;
    }

    public Cell mapEntityToCell(String _np, TextCell _tc, boolean _spaceBefore)
    {
        String _tf = _tc.getFontFace();
        String _ff = _tc.getFontFace();
        int _fs = _tc.getFontSize();
        char _g = 0;

        if(_np.indexOf('-')>0)
        {
            String[] _ep = _np.split("\\-", 2);
            if(_ep[1].startsWith("#x"))
            {
                _g = (char)CommonUtil.createInteger("0"+(_ep[1].substring(1))).intValue();
            }
            else
            if(_ep[1].startsWith("#"))
            {
                _g = (char)CommonUtil.createInteger(_ep[1].substring(1)).intValue();
            }
            else
            if(_iconMap.containsKey(_ep[0]))
            {
                Integer _G = _iconMap.get(_ep[0]).get(_ep[1]);
                if(_G!=null)
                {
                    _g = (char)_G.intValue();
                }
            }

            if(_g==0)
            {
                PdfFont _font = _reg.lookupFont(_ep[0]);
                _g = _font.glyphToChar(_ep[1]);
            }
            _ff = _ep[0];
        }
        else
        {
            _g = this.htmlEntityToChar(_np);
        }

        return HtmlEntityTextCell.builder()
                .entity(_np)
                .character(_g)
                .textFace(_tf)
                .fontFace(_ff)
                .fontSize(_fs)
                .spaceBefore(_spaceBefore)
                .build();
    }

    public Cell mapTextToCell(String _np, TextCell _tc, boolean _spaceBefore)
    {
        String _ff = _tc.getFontFace();
        int _fs = _tc.getFontSize();

        return mapTextToCell(_np, _ff, _fs, _spaceBefore);
    }

    public Cell mapTextToCell(String _np, String _ff, int _fs, boolean _spaceBefore)
    {
        return TextCell.builder()
                .text(_np)
                .fontFace(_ff)
                .fontSize(_fs)
                .spaceBefore(_spaceBefore)
                .build();
    }

    static Pattern HTML_ENTITY_PATTERN = Pattern.compile("(\\&[a-zA-Z0-9_\\-\\.\\#]+\\;)");

    static Pattern MAP_ENTITY_PATTERN = Pattern.compile("(\\&\\{[a-zA-Z0-9_\\-\\.\\#]+\\})");

    public void mapEntitiesToCells(List<Cell> _ccq, Pattern _pattern, int _prefixSize, int _suffixSize)
    {
        for (int _j = 0; _j < _ccq.size(); _j++)
        {
            Cell _cell = _ccq.get(_j);

            if(_cell instanceof TextCell)
            {
                TextCell _tc = ((TextCell) _cell);
                String _text = _tc.getText();

                Matcher _matcher = _pattern.matcher(_text);
                List<Cell> _repl = new LinkedList<>();
                boolean _spaceBefore = _tc.isSpaceBefore();
                int _lastEnd = 0;
                boolean found = false;
                while(_matcher.find())
                {
                    int _start = _matcher.start();
                    int _end = _matcher.end();
                    if(_start!=_lastEnd)
                    {
                        Cell _rc = mapTextToCell(_text.substring(_lastEnd, _start), _tc, _tc.isSpaceBefore());
                        _repl.add(_rc);
                    }
                    String _ent = _text.substring(_start+_prefixSize, _end-_suffixSize);
                    if(_start>0)
                    {
                        _spaceBefore = Character.isSpaceChar(_text.substring(_start-1, _start).charAt(0));
                    }
                    Cell _rc = mapEntityToCell(_ent, _tc, _spaceBefore);
                    _repl.add(_rc);
                    _lastEnd=_end;
                    found = true;
                }

                if(found)
                {
                    if(_lastEnd!=_text.length())
                    {
                        if(_lastEnd>0)
                        {
                            _spaceBefore = Character.isSpaceChar(_text.substring(_lastEnd-1, _lastEnd).charAt(0));
                        }
                        Cell _rc = mapTextToCell(_text.substring(_lastEnd, _text.length()), _tc, _spaceBefore);
                        _repl.add(_rc);
                    }

                    _ccq.remove(_j);
                    for(int _i = 0; _i < _repl.size(); _i++)
                    {
                        _ccq.add(_j, _repl.get(_i));
                        _j++;
                    }
                    _j--;
                }
            }
        }
    }

    public String mapEntitiesToString(String _text)
    {
        _text = mapEntitiesToString(_text, HTML_ENTITY_PATTERN, 1, 1);
        _text = mapEntitiesToString(_text, MAP_ENTITY_PATTERN, 2, 1);
        return _text;
    }

    public String mapEntitiesToString(String _text, Pattern _pattern, int _prefixSize, int _suffixSize)
    {
        Matcher _matcher = _pattern.matcher(_text);
        StringBuilder _repl = new StringBuilder();
        int _lastEnd = 0;
        boolean found = false;
        while(_matcher.find())
        {
            int _start = _matcher.start();
            int _end = _matcher.end();
            if(_start!=_lastEnd)
            {
                _repl.append(_text.substring(_lastEnd, _start));
            }
            String _ent = _text.substring(_start+_prefixSize, _end-_suffixSize);
            _ent = htmlEntityToChar(_ent).toString();
            _repl.append(_ent);
            _lastEnd=_end;
            found = true;
        }

        if(found)
        {
            if(_lastEnd!=_text.length())
            {
                _repl.append(_text.substring(_lastEnd, _text.length()));
            }
            return _repl.toString();
        }

        return _text;
    }

    public float renderCells(List<Cell[]> _clines, PdfContent _cnt, double _w, double _indent, double _ls, double _hscale, String _align, boolean _forceAlign)
    {
        float _hsize = 0;
        for(int _j = 0; _j < _clines.size(); _j++)
        {
            Cell[] _line = _clines.get(_j);
            boolean lastLine = (_j == _clines.size()-1);
            boolean firstInLine = true;

            double _diff = _w-_indent;
            double _lw = 0;
            for(Cell _c : _line)
            {
                double _cw = _c.width(this._reg, firstInLine)*_hscale/100f;
                _diff -= _cw;
                _lw += _cw;
                firstInLine = false;
            }

            double _id = _diff/(_line.length-1);
            firstInLine = true;

            if("right".equalsIgnoreCase(_align))
            {
                for(Cell _c : _line)
                {
                    _cnt.addContent(_c.render(this._reg, _cnt, firstInLine, firstInLine ? _diff : _indent, 0, 0, _hscale));
                    firstInLine = false;
                }
            }
            else
            if("center".equalsIgnoreCase(_align))
            {
                for(Cell _c : _line)
                {
                    _cnt.addContent(_c.render(this._reg, _cnt, firstInLine, firstInLine ? _diff/2d : _indent, 0, 0, _hscale));
                    firstInLine = false;
                }
            }
            else
            if(("justified".equalsIgnoreCase(_align) || "scaled".equalsIgnoreCase(_align)) && (lastLine && !_forceAlign))
            {
                _cnt.wordspace(0);
                for(Cell _c : _line)
                {
                    _cnt.addContent(_c.render(this._reg, _cnt, firstInLine, (firstInLine ? _indent: 0), 0, 0, _hscale));
                    firstInLine = false;
                }
            }
            else
            if("justified".equalsIgnoreCase(_align))
            {
                for(Cell _c : _line)
                {
                    _cnt.addContent(_c.render(this._reg, _cnt, firstInLine, (firstInLine ? _indent: _id),  0, 0, _hscale));
                    firstInLine = false;
                }
            }
            else
            if("scaled".equalsIgnoreCase(_align))
            {
                double _wd = _w-_indent;
                //double _scale = (_wd/(_wd-(_diff/2.)));
                double _scale = (_wd/(_wd-_diff));
                _hscale*=_scale;
                //_id = ((_wd - (_lw*_scale))/(_line.length-1))/_scale;
                _id = 0;

                for(Cell _c : _line)
                {
                    _cnt.addContent(_c.render(this._reg, _cnt, firstInLine, (firstInLine ? _indent: _id),  0, 0, _hscale));
                    firstInLine = false;
                }
            }
            else
            {
                for(Cell _c : _line)
                {
                    _cnt.addContent(_c.render(this._reg, _cnt, firstInLine, _indent, 0, 0, _hscale));
                    firstInLine = false;
                }
            }
            _cnt.cr(-_ls);
            _hsize += _ls;
        }
        return _hsize;
    }

    public Properties ATTRIBUTE_DEFAULTS = new Properties();

    public void addPart(String _text)
    {
        this._part = this._pdf.newOutline(_text, this.getPage(false));
    }

    public void addChapter(String _text)
    {
        if(this.isFlatOutline())
        {
            this._pdf.newOutline(_text, this.getPage(false));
            return;
        }

        if(this._part == null)
        {
            addPart("PART");
        }
        this._chapter = this._part.add(_text, this.getPage(false));
    }

    public void addSection(String _text)
    {
        if(this.isFlatOutline())
        {
            this._pdf.newOutline(_text, this.getPage(false));
            return;
        }

        if(this._part == null)
        {
            addPart("PART");
        }
        if(this._chapter == null)
        {
            addChapter("DOCUMENT");
        }
        this._section = this._chapter.add(_text, this.getPage(false));
    }

    public void addSubSection(String _text)
    {
        if(this.isFlatOutline())
        {
            this._pdf.newOutline(_text, this.getPage(false));
            return;
        }

        if(this._part == null)
        {
            addPart("PART");
        }
        if(this._chapter == null)
        {
            addChapter("DOCUMENT");
        }
        if(this._section == null)
        {
            addSection("SECTION");
        }
        this._subsection = this._section.add(_text, this.getPage(false));
    }

    public void addSubSubSection(String _text)
    {
        if(this.isFlatOutline())
        {
            this._pdf.newOutline(_text, this.getPage(false));
            return;
        }

        if(this._part == null)
        {
            addPart("PART");
        }
        if(this._chapter == null)
        {
            addChapter("DOCUMENT");
        }
        if(this._section == null)
        {
            addSection("SECTION");
        }
        if(this._subsection == null)
        {
            addSubSection("SUBSECTION");
        }
        this._subsubsection = this._subsection.add(_text, this.getPage(false));
    }

    public void addOutline(String _text)
    {
        if(this.isFlatOutline())
        {
            this._pdf.newOutline(_text, this.getPage(false));
            return;
        }

        if(this._part == null)
        {
            addPart(_text);
        }
        else
        if(this._chapter == null)
        {
            addChapter(_text);
        }
        else
        if(this._section == null)
        {
            addSection(_text);
        }
        else
        if(this._subsection == null)
        {
            addSubSection(_text);
        }
        else
        if(this._subsubsection == null)
        {
            addSubSubSection(_text);
        }
        else
        {
            this._subsubsection.add(_text, this.getPage(false));
        }
    }

    public void addReference(String _text)
    {
        if(this.isFlatOutline())
        {
            this._pdf.newOutline(_text, this.getPage(false));
            return;
        }

        if(this._refoutlines == null)
        {
            this._refoutlines = this._pdf.newOutlineAnchor("REFERENCES");
        }
        this._refoutlines.add(_text, this.getPage(false));
    }

    public PdfPage getPage(boolean _new)
    {
        if(this._page==null && _new)
        {
            this.newPage();
        }
        return this._page;
    }

    public PdfPage getPage()
    {
        return this.getPage(true);
    }

    public PdfPage newPage()
    {
        this._page = _pdf.newPage();
        this._background = null;
        this._page_num++;
        log.warn("processing page "+this._page_num+" ...");
        return this._page;
    }

    public void doDrawRect(int _px, int _py, int _w, int _h, String _fill, String _stroke, float _swidth, int[] _dash, boolean _background, String _layer)
    {
        PdfContent _cnt = _background ? this.getBackground() : this.getPage().newContent();
        boolean _doLayer = CommonUtil.toBooleanDefaultIfNull(_layer, true);
        if(_doLayer)
        {
            _cnt.startLayer(_layer);
        }
        _cnt.save();
        if(CommonUtil.isNotBlank(_fill))
            _cnt.fillColor(_fill);
        if(_dash!=null && _dash.length>0)
        {
            _cnt.linedash(_dash);
        }
        if(CommonUtil.isNotBlank(_stroke))
        {
            _cnt.linewidth(_swidth);
            _cnt.strokeColor(_stroke);
        }
        _cnt.rectangle(_px, _py, _w, _h);
        if(CommonUtil.isNotBlank(_fill)
                && CommonUtil.isNotBlank(_stroke))
        {
            _cnt.fillStroke();
        }
        else
        if(CommonUtil.isNotBlank(_fill))
        {
            _cnt.fill();
        }
        else
        {
            _cnt.stroke();
        }
        _cnt.restore();
        if(_doLayer)
        {
            _cnt.endLayer();
        }
    }



    public void handlePageHeaderAndFooter(Properties _attributes, int pageNum)
    {
        // apply header
        boolean _noHeader = getAttributeValueOrPrefixAsBool(_attributes, "no-header", false, true, "page");
        if(!_noHeader)
            handlePageHeader(_attributes, pageNum);

        // apply footer
        boolean _noFooter = getAttributeValueOrPrefixAsBool(_attributes, "no-footer", false, true, "page");
        if(!_noFooter)
            handlePageFooter(_attributes, pageNum);
    }

    Properties HEADER_EVEN = new Properties();
    Properties HEADER_ODD = new Properties();

    Properties FOOTER_EVEN = new Properties();
    Properties FOOTER_ODD = new Properties();

    private void handlePageFooter(Properties _attributes, int pageNum)
    {
        Properties _xattr = new Properties();
        if((pageNum % 2) == 0)
        {
            if(FOOTER_EVEN.size()>0)
            {
                _xattr.putAll(FOOTER_EVEN);
            }
        }
        else
        {
            if(FOOTER_ODD.size()>0)
            {
                _xattr.putAll(FOOTER_ODD);
            }
        }
        _xattr = PmlParserUtil.toPropertiesByPrefix(_xattr, "page-footer-", _attributes);
        resolveFromClassDefinition(_xattr);

        if(_xattr.size()>0)
        {
            renderPageHeaderOrFooter("page-footer", true, _xattr, pageNum);
        }
    }

    private void handlePageHeader(Properties _attributes, int pageNum)
    {
        Properties _xattr = new Properties();
        if((pageNum % 2) == 0)
        {
            if(HEADER_EVEN.size()>0)
            {
                _xattr.putAll(HEADER_EVEN);
            }
        }
        else
        {
            if(HEADER_ODD.size()>0)
            {
                _xattr.putAll(HEADER_ODD);
            }
        }
        _xattr = PmlParserUtil.toPropertiesByPrefix(_xattr, "page-header-", _attributes);
        resolveFromClassDefinition(_xattr);

        if(_xattr.size()>0)
        {
            renderPageHeaderOrFooter("page-header", true, _xattr, pageNum);
        }
    }

    Properties RUNNING_ATTRIBUTES = new Properties();

    void renderPageHeaderOrFooter(String _tag, boolean _tagOnly, Properties _xattr, int pageNum)
    {
        int[] _pos = PmlParserUtil.toIntArray(getAttributeValueOrPrefix(_xattr, "pos", false, _tag));
        String _text = getAttributeValueOrPrefix(_xattr, "content", false, _tag);
        Properties _repls = new Properties();
        _repls.putAll(RUNNING_ATTRIBUTES);
        _repls = PmlParserUtil.toProperties(_repls, "page-number", Integer.toString(pageNum));
        _text = PmlUtil.formatSimple(_text, _repls);
        layoutAndRenderText(_pos[0], _pos[1], _xattr, this.getPage().newContent(), _text, true, SOURCE_TYPE_TEXT, _tagOnly, _tag);
    }

    @SneakyThrows
    public float handlePageTable(PmlParserContext _xpc, XmlPullParser _xpp)
    {
        Properties _attributes = PmlParserUtil.toProperties(_xpp);
        String _type = getAttributeValueOrDefault(_attributes, "type", "xml");
        if("xml".equalsIgnoreCase(_type))
        {
            return handlePageTableXml(_xpc, _xpp);
        }
        return 0;
    }

    @SneakyThrows
    public float handlePageTableXml(PmlParserContext _xpc, XmlPullParser _xpp)
    {
        String _tag = "table";
        Properties _attributes = PmlParserUtil.toPropertiesWithPrefix(_tag, _xpp);
        resolveFromClassDefinitionWithPrefix(_tag, _attributes);

        int[] _pos = PmlParserUtil.toIntArray(getAttributeValueOrPrefix(_attributes, "pos", false, _tag));
        int _w = getAttributeValueOrPrefixAsInt(_attributes, "width", false, _tag);

        String _align = getAttributeValueOrPrefix(_attributes, "align", false, _tag);
        String[] _aligns = CommonUtil.isBlank(_align) ? null : _align.split(",");
        float _hscale = getAttributeValueOrDefaultAsFloat(_attributes, "font-scale", 100f, true, _tag);

        String widths = getAttributeValueOrPrefix(_attributes, "widths", false, _tag);
        if(CommonUtil.isBlank(widths))
        {
            throw new IllegalArgumentException(_tag+":widths");
        }

        String[] _widths = widths.split(",");

        List<AbstractTableCell> _cells = new Vector<>();

        int _i = 0;
        int _event = -1;
        while(!((_event = _xpp.next())==XmlPullParser.END_TAG && "table".equalsIgnoreCase(_xpp.getName())))
        {
            if(_event==XmlPullParser.START_TAG && "cell".equalsIgnoreCase(_xpp.getName()))
            {
                String _ttag = _tag + "-body-row-cell";
                _i = (_i % _widths.length);
                TableFormatCell.TableFormatCellBuilder _tcb = TableFormatCell.builder();
                _tcb.tag(_ttag);
                Properties _cattr = PmlParserUtil.toPropertiesWithPrefix(_ttag, _xpp, _attributes);

                _tcb.attributes(_cattr);

                int _c_span = getAttributeValueOrPrefixAsInt(_cattr, "span", true, _ttag);
                _tcb.span(_c_span);

                String _cell = _xpp.nextText();

                String _c_align = _cattr.getProperty(_ttag+"-align", _aligns==null ? null : _aligns[_i % _aligns.length]);
                _tcb.align(_c_align);

                String _c_fn = getAttributeValueOrPrefix(_cattr, "font",true,_ttag);
                int _c_fs = getAttributeValueOrPrefixAsInt(_cattr, "font-size",true,_ttag);
                float _c_hscale = getAttributeValueOrDefaultAsFloat(_cattr, "font-scale", _hscale, false, _tag);
                List<Cell> _pc = TextCell.builder()
                        .text(_cell)
                        .fontFace(_c_fn)
                        .fontSize(_c_fs)
                        .spaceBefore(false)
                        .build()
                        .breakup(this._reg, -1, _c_hscale);
                _tcb.cells(_pc);
                _tcb.source(SOURCE_TYPE_HTML);
                _cells.add(_tcb.build());
                _i++;
            }
            else
            if(_event==XmlPullParser.START_TAG && "markdown".equalsIgnoreCase(_xpp.getName()))
            {
                String _ttag = _tag + "-body-row-markdown";
                _i = (_i % _widths.length);
                MarkdownCell.MarkdownCellBuilder _tcb = MarkdownCell.builder();
                _tcb.tag(_ttag);

                Properties _cattr = PmlParserUtil.toPropertiesWithPrefix(_ttag, _xpp, _attributes);
                _tcb.attributes(_cattr);

                int _c_span = getAttributeValueOrPrefixAsInt(_cattr, "span", 1,true, _ttag);
                _tcb.span(_c_span);

                String _cell = _xpp.nextText();
                _tcb.markdown(parseMarkdown(new StringReader(_cell)));

                _tcb.source(SOURCE_TYPE_MARKDOWN);
                _cells.add(_tcb.build());
                _i++;
            }
        }
        return layoutAndRenderTable(_attributes, _cells, _pos[0], _pos[1], Collections.emptyList(), true, _tag);
    }

    public float layoutAndRenderTable(Properties _attributes, List<AbstractTableCell> _cells, int _px, int _py, List<Float> _widthList, boolean _tagOnly, String... _tags)
    {
        float _hsize = 0;
        int _ls = getAttributeValueOrPrefixAsInt(_attributes, "font-lead", _tagOnly, _tags);
        String _align = getAttributeValueOrPrefix(_attributes, "align", _tagOnly, _tags);
        float _w = getAttributeValueOrPrefixAsFloat(_attributes, "width", _tagOnly, _tags);
        float _hscale = getAttributeValueOrDefaultAsFloat(_attributes, "font-scale", 100f, true, _tags);
        String[] _aligns = _align.split(",");

        float[] startOff;
        float[] endOff;
        float accu = 0;
        if(_widthList.size()!=0)
        {
            startOff = new float[_widthList.size()];
            endOff = new float[_widthList.size()];
            for(int _i = 0; _i<_widthList.size(); _i++)
            {
                startOff[_i] = accu;
                accu += _widthList.get(_i);
                endOff[_i] = accu;
            }
        }
        else
        {
            String widths = getAttributeValueOrPrefix(_attributes, "widths", _tagOnly, _tags);
            if(CommonUtil.isBlank(widths))
            {
                throw new IllegalArgumentException(_tags[0]+":"+_tagOnly+":widths");
            }

            String[] _widths = widths.split(",");
            startOff = new float[_widths.length];
            endOff = new float[_widths.length];
            for(int _i = 0; _i<_widths.length; _i++)
            {
                startOff[_i] = accu;
                accu += Float.parseFloat(_widths[_i]);
                endOff[_i] = accu;
            }
        }

        accu = _w/accu;
        for(int _i = 0; _i<startOff.length; _i++)
        {
            startOff[_i] *= accu;
            startOff[_i] += 2;
            endOff[_i] *= accu;
            endOff[_i] -= 2;
        }

        PdfContent _cnt = this.getPage().newContent();
        String _layer = _attributes.getProperty("layer", "false");
        boolean _doLayer = CommonUtil.toBooleanDefaultIfNull(_layer, true);
        if(_doLayer)
        {
            _cnt.startLayer(_layer);
        }
        int _i = 0;
        int _event = -1;
        float _ysize = 0 /* _ls */;
        int _lineno = -1;
        for(AbstractTableCell _cell : _cells)
        {
            _i = _i % startOff.length;
            if(_i==0)
            {
                _hsize += _ysize;
                _ysize=0;
                _lineno++;
            }

            String[] _ctags = new String[_tags.length+1];
            _ctags[0] = _cell.getTag();
            System.arraycopy(_tags, 0, _ctags, 1, _tags.length);

            Properties _cattr = new Properties();
            _cattr.putAll(_attributes);
            _cattr = PmlParserUtil.toProperties(_cattr, _cell.getTag()+"-align", _aligns[_i>=_aligns.length ? 0 : (_i % _aligns.length)]);
            _cattr.putAll(_cell.getAttributes());

            String _c_color = getAttributeValueOrPrefix(_cattr, "color", "#000", _tagOnly, _ctags);
            int _c_fs = getAttributeValueOrPrefixAsInt(_cattr, "font-size", _tagOnly, _ctags);
            String _c_align = getAttributeValueOrPrefix(_cattr, "align", _tagOnly, _ctags);
            boolean _c_hyphen = getAttributeValueOrPrefixAsBool(_cattr, "hyphenate", _tagOnly, _ctags);
            float _c_ls = getAttributeValueOrPrefixAsFloat(_cattr, "font-lead", _tagOnly, _ctags);
            if(_c_ls<0) _c_ls = (-_c_ls)*_c_fs;

            int _c_span = _cell.getSpan();
            int _t_span = getAttributeValueOrPrefixAsInt(_cattr, "span", 1, _tagOnly, _ctags);
            if(_t_span>_c_span) _c_span = _t_span;
            float _pad = getAttributeValueOrPrefixAsFloat(_attributes, "padding", _tagOnly, _ctags);
            float _c_hscale = getAttributeValueOrDefaultAsFloat(_cattr, "font-scale", _hscale, true, _tags);

            float _cw = endOff[_i] - startOff[_i] - (2f*_pad);
            if(_c_span>1)
            {
                _cw = endOff[_i+_c_span-1] - startOff[_i] - (2f*_pad);
            }

            float _cpx = _px+startOff[_i]+_pad;
            float _cpy = _py-_hsize-_pad -(_ls*4f/5f) ;
            if(_cell instanceof TableFormatCell)
            {
                TableFormatCell _tcell = (TableFormatCell) _cell;
                _cnt.save();
                _cnt.startText();
                _cnt.fillColor(_c_color);
                _cnt.moveText(_cpx, _cpy-_ls);

                List<Cell> _pc = _tcell.getCells();
                List<Cell[]> _pl = layoutCells(_pc, _cw, _c_hscale, _c_hyphen, _c_align, _cell.getSource(), false);
                float _tsize = renderCells(_pl, _cnt, _cw, 0, _c_ls, _c_hscale, CommonUtil.isBlank(_tcell.getAlign()) ? _c_align : _tcell.getAlign(), false);
                _tsize += 2f*_pad;
                if(_tsize > _ysize) _ysize=_tsize;

                _cnt.endText();
                _cnt.restore();

                String _id = getAttributeValueOrNull(_cell.getAttributes(), "id");
                if(CommonUtil.isNotBlank(_id))
                {
                    this._pdf.newNamedDestination(_id, this.getPage());
                }

                String _link = getAttributeValueOrNull(_cell.getAttributes(), "link");
                if(CommonUtil.isNotBlank(_link))
                {
                    this.getPage().newNamedLink(_link, (int)_cpx, (int)(_cpy-_ysize), (int)(_cw), (int)_ysize);
                }
            }
            else
            if(_cell instanceof MarkdownCell)
            {
                MarkdownCell _mcell = (MarkdownCell) _cell;
                float _tsize = processMarkdown(_cell.getAttributes(), (int)_cpx, (int)_cpy, (int)_cw, _mcell.getMarkdown(), append("markdown",_ctags));
                _tsize += 2f*_pad;
                if(_tsize > _ysize) _ysize=_tsize;
            }

            if(_c_span>1)
            {
                _i += _c_span;
            }
            else
            {
                _i++;
            }

            String _bgcolor = getAttributeValueOrPrefix(_attributes, "background", true, _tags);
            if(CommonUtil.isNotBlank(_bgcolor))
            {
                float _bpad = getAttributeValueOrPrefixAsFloat(_attributes, "background-padding", 0f, true, _tags);
                String[] _ncol = _bgcolor.split(",");
                if (_i == startOff.length) {
                    String _xcolor = _ncol[_lineno % _ncol.length];
                    renderBackground(_layer, _xcolor, _px-_bpad, _py - _hsize -_ls+_bpad, (int) (_w+(2f*_bpad)), _ysize);
                }
            }
        }

        _hsize += _ysize;

        renderConditionalBorder(_attributes, _px,  _py-_ls, (int) _w, _hsize, true, _tags);
        if(_doLayer)
        {
            _cnt.endLayer();
        }
        return _hsize+_ls;
    }

    void resolveFromClassDefinition(Properties attributes)
    {
        if(attributes.containsKey(PmlParserUtil.PROP_CLASS))
        {
            String _cnx = attributes.getProperty(PmlParserUtil.PROP_CLASS);
            for(String _cn : CommonUtil.split(_cnx, " "))
            {
                if(CLASS_MAP.containsKey(_cn))
                {
                    Properties _cm = CLASS_MAP.get(_cn);
                    for(String _key : _cm.stringPropertyNames())
                    {
                        if(!attributes.containsKey(_key))
                        {
                            attributes.setProperty(_key, _cm.getProperty(_key));
                        }
                    }
                }
            }
        }
    }

    void resolveFromClassDefinitionWithPrefix(String _source, Properties attributes)
    {
        String _cnx = null;
        List<Integer> _offs = new Vector<>();
        int lastOff = _source.length();
        if(_source.endsWith("-")) lastOff--;
        _offs.add(lastOff);

        while(lastOff>0)
        {
            int off = _source.lastIndexOf("-", lastOff-1);
            if(off>0)
            {
                _offs.add(off);
            }
            lastOff=off;
        }
        _offs.add(0);

        for(int off : _offs)
        {
            String _prefix = _source.substring(0,off)+(off!=0 ? "-" : "");
            if(attributes.containsKey(_prefix+PmlParserUtil.PROP_CLASS))
            {
                _cnx = attributes.getProperty(_prefix+PmlParserUtil.PROP_CLASS);
                break;
            }
        }

        if(CommonUtil.isNotBlank(_cnx))
        {
            for(String _cn : CommonUtil.split(_cnx, " "))
            {
                if(CLASS_MAP.containsKey(_cn))
                {
                    Properties _cm = CLASS_MAP.get(_cn);
                    for(String _key : _cm.stringPropertyNames())
                    {
                        if(!attributes.containsKey(_key))
                        {
                            attributes.setProperty(_key, _cm.getProperty(_key));
                        }
                    }
                }
            }
        }
    }

    public String getAttributeValueOrDefault(Properties _pp, String _attr, String _default)
    {
        String _var = _pp.getProperty(_attr, _default);
        _var = PmlUtil.formatSimple(_var, this.ATTRIBUTE_DEFAULTS);
        return _var;
    }

    public String getAttributeValueOrDefault(Properties _pp, String _attr)
    {
        String _var = _pp.getProperty(_attr, "");
        if(CommonUtil.isBlank(_var)) _var = this.ATTRIBUTE_DEFAULTS.getProperty(_attr);
        _var = PmlUtil.formatSimple(_var, this.ATTRIBUTE_DEFAULTS);
        return _var;
    }

    public String getAttributeValueOrPrefix(Properties _pp, String _attr, boolean _prefixOnly, String... _prefixs)
    {
        return getAttributeValueOrPrefix(_pp, _attr, "", _prefixOnly, _prefixs);
    }

    public String getAttributeValueOrPrefix(Properties _pp, String _attr, String _default, boolean _prefixOnly, String... _prefixs)
    {
        String _val = null;
        for(String _prefix : _prefixs)
        {
            if(_prefix!=null)
            {
                String _xattr = _prefix+"-"+_attr;
                if(_prefix.endsWith("-")) _xattr = _prefix+_attr;

                _val = this.getAttributeValueOrDefault(_pp, _xattr);

                if(CommonUtil.isNotBlank(_val))
                    return PmlUtil.formatSimple(_val, ATTRIBUTE_DEFAULTS);
            }
        }

        List<Integer> _offs = new Vector<>();
        if(_prefixs.length>0 && _prefixs[0]!=null)
        {
            int lastOff = _prefixs[0].length();
            if(_prefixs[0].endsWith("-")) lastOff--;
            _offs.add(lastOff);

            while(lastOff>0)
            {
                int off = _prefixs[0].lastIndexOf("-", lastOff-1);
                if(off>0)
                {
                    _offs.add(off);
                }
                lastOff=off;
            }
            if(!_prefixOnly) _offs.add(0);


            for(int off : _offs)
            {
                String _pfx = _prefixs[0].substring(0,off)+(off!=0 ? "-" : "");

                if(dumpAttributeLookups) log.info(_pfx+":"+_attr);

                if(_pp.containsKey(_pfx+_attr))
                {
                    return PmlUtil.formatSimple(_pp.getProperty(_pfx+_attr), ATTRIBUTE_DEFAULTS);
                }
                else
                if(ATTRIBUTE_DEFAULTS.containsKey(_pfx+_attr))
                {
                    return PmlUtil.formatSimple(ATTRIBUTE_DEFAULTS.getProperty(_pfx+_attr), ATTRIBUTE_DEFAULTS);
                }
            }
        }

        return _default;
    }

    public String getAttributeValueOrNull(Properties _pp, String _attr)
    {
        return getAttributeValueOrDefault(_pp, _attr, null);
    }


    boolean dumpAttributeLookups = false;

    public Properties FONT_ALIASES = new Properties();


    public void registerFont(String id, String name, String[] _options, String cs, File basedir, File parentFile)
    {
        while(FONT_ALIASES.containsKey(name.toLowerCase()))
        {
            name = FONT_ALIASES.getProperty(name.toLowerCase());
        }

        PdfFont _font = null;

        if(_options!=null && _options.length>0)
        {
            PdfFont _tfont = _reg.lookupFont(name);
            if(_tfont==null)
            {
                String _rid = UUID.randomUUID().toString();
                this.registerFont(_rid, name, null, cs, basedir, parentFile);
                _tfont = _reg.lookupFont(_rid);
            }
            _font = _reg.syntheticFont(id, _tfont, cs, _options);
            this.registerFont(_font, id);
            return;
        }
        else
        if(name.startsWith("pdf:"))
        {
            _font = _reg.lookupFont(name);
        }
        else
        if(name.startsWith("awt:"))
        {
            _font = _reg.lookupFont(name);
        }
        else
        {
            ResourceLoader _rl = PmlUtil.sourceToLoader(name, _options, basedir, parentFile, this.ZIP_MOUNTS, this.DIR_MOUNTS);

            if(name.equalsIgnoreCase(id))
            {
                _font = _pdf.registerFontByName(id, "icons".equalsIgnoreCase(cs) ? null : cs);
            }
            else
            if(_rl!=null && "icons".equalsIgnoreCase(cs))
            {
                //_font = _pdf.registerOtuFont(_rl, cs);
                _font = _pdf.registerOtxFont(cs, _rl);
            }
            else
            if(_rl!=null && "unicode".equalsIgnoreCase(cs))
            {
                // _font = _pdf.registerOtuFont(_rl, null, true);
                _font = _pdf.registerOtxFont(_rl);
            }
            else
            if((_rl!=null) && (name!=null)
                    && (name.endsWith(".svg")
                    || name.endsWith(".svg.gz")
                    || name.endsWith(".svgz")
                    || name.endsWith(".svgf")))
            {
                _font = _pdf.registerSvgFont(cs, _rl, _options);
            }
            else
            if((_rl!=null) && (name!=null) && name.endsWith(".otf"))
            {
                //_font = _pdf.registerOtuFont(_rl, cs, false);
                _font = _pdf.registerOtxFont(cs, _rl);
            }
            else
            if((_rl!=null) && (name!=null) && name.endsWith(".ttf"))
            {
                if(cs!=null)
                {
                    _font = _pdf.registerTtfFont(cs, _rl);
                }
                else
                {
                    //_font = _pdf.registerOtuFont(_rl, cs, false);
                    _font = _pdf.registerOtxFont(_rl);
                }
            }
            else
            {
                Font _awt = Font.getFont(name);
                if(_awt==null)
                {
                    _awt = new Font(name, Font.PLAIN,1);
                }

                _font = _pdf.registerAwtFont(_awt, "icons".equalsIgnoreCase(cs) ? null : cs, null);
            }
        }

        if(_font == null) throw new IllegalArgumentException();

        if(id != null) this.registerFont(_font, id);
        this.registerFont(_font, name);
    }

    static EntityResolver entityResolver = new PmlEntityResolver();

    public void pushFile(File _base, File file) throws XmlPullParserException
    {
        KXmlParser _xp = new KXmlParser();
        _xp.setFeature(KXmlParser.FEATURE_PROCESS_PROCESSING_INSTRUCTION, true);
        _xp.setInput(PmlFileResourceLoader.of(file).getInputStream(), "UTF-8");
        _xp.setEntityResolver(entityResolver);
        for(String _ent : HTML_ENTITIES.stringPropertyNames())
        {
            String _val = HTML_ENTITIES.getProperty(_ent).trim();
            try
            {
                int _v = CommonUtil.createInteger(_val);
                _xp.defineEntityReplacementText(_ent, Character.toString((char) _v));
            }
            catch(Exception _xe)
            {
                log.warn(_ent+" = "+_val, _xe);
            }
        }
        _queue.add(PmlParserContext
                .builder()
                .basedir(_base)
                .file(file)
                .parser(_xp)
                .build());
    }

    public PdfFont registerFont(PdfFont _font, String _name) {
        return _reg.registerFont(_font, _name);
    }

}
