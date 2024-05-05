package com.github.terefang.jmelange.pdf.ml;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.ListMapUtil;
import com.github.terefang.jmelange.commons.util.OsUtil;
import com.github.terefang.jmelange.pdf.ml.cmd.PageLevelDirective;
import com.github.terefang.jmelange.pdf.ml.script.AbstractPmlScriptContext;
import com.github.terefang.jmelange.pdf.ml.script.PmlDrawScriptContext;
import com.github.terefang.jmelange.pdf.ml.script.PmlLuayScriptContext;
import com.vladsch.flexmark.ext.attributes.AttributeNode;
import com.vladsch.flexmark.ext.attributes.AttributesNode;
import com.vladsch.flexmark.util.ast.Node;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

@Slf4j
public class PmlParserUtil 
{
	@SneakyThrows
	public static boolean handleEntityOrIconPi(PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp, boolean isIcon) throws IOException, XmlPullParserException
	{
		Properties _attributes = PmlParserUtil.toProperties(_xpp);

		String _font = _p.getAttributeValueOrNull(_attributes,"font");
		String _name = _p.getAttributeValueOrNull(_attributes,"name");
		String _code = _p.getAttributeValueOrNull(_attributes,"code");

		int _v = CommonUtil.createInteger(_code);

		if(isIcon)
		{
			if(!_p._iconMap.containsKey(_font))
			{
				_p._iconMap.put(_font, new HashMap<>());
			}
			_p._iconMap.get(_font).put(_name, _v);
		}
		else
		{
			_p.HTML_ENTITIES.setProperty(_name, _code);
			_xpp.defineEntityReplacementText(_name, Character.toString((char) _v));
		}
		return false;
	}

	public static boolean handleClassPi(PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp) throws IOException, XmlPullParserException
	{
		Properties _pp = PmlParserUtil.toProperties(_xpp);
		String _id = _pp.getProperty(PmlParserUtil.PROP_ID);
		_pp.remove(PmlParserUtil.PROP_ID);
		_p.CLASS_MAP.put(_id, _pp);
		log.debug("defined class: "+_id);
		return false;
	}

	public static boolean handleMountPi(PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp) throws IOException, XmlPullParserException
	{
		String _src = _xpp.getAttributeValue(null, "src");
		String _dir = _xpp.getAttributeValue(null, "dir");
		if(CommonUtil.isBlank(_dir))
		{
			_p.mountZip(_src, _xpc.getBasedir(), _xpc.getFile().getParentFile());
		}
		else
		{
			_p.mountDir(_dir, _xpc.getBasedir(), _xpc.getFile().getParentFile());
		}
		return false;
	}

	public static boolean handleOutlines(PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp) throws IOException, XmlPullParserException
	{
		String _tag = _xpp.getName();
		Properties _pp = toProperties(_xpp);
		String _text = _p.getAttributeValueOrDefault(_pp, "text", "XRef-Link");
		int _hl = CommonUtil.createInteger(_p.getAttributeValueOrDefault(_pp, "level", "4"));

		if("part".equalsIgnoreCase(_tag))
		{
			_p.addPart(_p.mapEntitiesToString(_text));
		}
		else
		if("chapter".equalsIgnoreCase(_tag))
		{
			_p.addChapter(_p.mapEntitiesToString(_text));
		}
		else
		if("section".equalsIgnoreCase(_tag))
		{
			_p.addSection(_p.mapEntitiesToString(_text));
		}
		else
		if("subsection".equalsIgnoreCase(_tag))
		{
			_p.addSubSection(_p.mapEntitiesToString(_text));
		}
		else
		if("subsubsection".equalsIgnoreCase(_tag))
		{
			_p.addSubSubSection(_p.mapEntitiesToString(_text));
		}
		else
		if("reference".equalsIgnoreCase(_tag))
		{
			_p.addReference(_p.mapEntitiesToString(_text));
		}
		else
		if(_hl==0)
		{
			_p.addChapter(_p.mapEntitiesToString(_text));
		}
		else
		if(_hl==1)
		{
			_p.addSection(_p.mapEntitiesToString(_text));
		}
		else
		if(_hl==2)
		{
			_p.addSubSection(_p.mapEntitiesToString(_text));
		}
		else
		if(_hl==3)
		{
			_p.addSubSubSection(_p.mapEntitiesToString(_text));
		}
		else
		{
			_p.addOutline(_p.mapEntitiesToString(_text));
		}
		_xpp.next();
		return false;
	}


	public static final String PROP_ID = "id";
	public static final String PROP_LANG = "lang";
	public static final String PROP_CLASS = "class";
	public static final String PROP_FONT_FACE = "font";
	public static final String PROP_FONT_SIZE = "font-size";
	public static final String PROP_ALIGN = "align";
	public static final String PROP_POSITION = "pos";
	public static final String PROP_COLOR = "color";
	public static final String PROP_WIDTH = "width";
	public static final String PROP_HYPHENATE = "hyphenate";
	public static final String PROP_FONT_LEAD = "font-lead";

	public static final String PROP_HEADING = "heading";
	public static final String PROP_HEADING_FONT = "heading-font";
	public static final String PROP_HEADING_SIZE = "heading-size";
	public static final String PROP_HEADING_OUTLINE = "heading-outline";

	public static Properties toProperties(XmlPullParser _xpp, Properties predef)
	{
		Properties _prop = new Properties();
		_prop.putAll(predef);

		for(int i = 0; i<_xpp.getAttributeCount(); i++)
		{
			_prop.setProperty(_xpp.getAttributeName(i), _xpp.getAttributeValue(i));
		}

		return _prop;
	}

	public static Properties toProperties(XmlPullParser _xpp, String... predef)
	{
		Properties _prop = toProperties(predef);

		for(int i = 0; i<_xpp.getAttributeCount(); i++)
		{
			_prop.setProperty(_xpp.getAttributeName(i), _xpp.getAttributeValue(i));
		}

		return _prop;
	}

	public static Properties toPropertiesWithPrefix(String _prefix, XmlPullParser _xpp, String... predef)
	{
		if(!_prefix.endsWith("-")) _prefix+="-";

		Properties _prop = toPropertiesWithPrefix(_prefix, predef);

		for(int i = 0; i<_xpp.getAttributeCount(); i++) {
			String _key = _xpp.getAttributeName(i);
			if ("id".equalsIgnoreCase(_key)) {
				_prop.setProperty("id", _xpp.getAttributeValue(i));
			}
			else
			if ("link".equalsIgnoreCase(_key)) {
				_prop.setProperty("link", _xpp.getAttributeValue(i));
			}
			else
			if ("layer".equalsIgnoreCase(_key)) {
				_prop.setProperty("layer", _xpp.getAttributeValue(i));
			}
			else
			if (_key.startsWith(_prefix)) {
				_prop.setProperty(_key, _xpp.getAttributeValue(i));
			}
			else
			{
				_prop.setProperty(_prefix+_key, _xpp.getAttributeValue(i));
			}
		}
		return _prop;
	}

	public static Properties toPropertiesWithPrefix(String _prefix, XmlPullParser _xpp, Properties predef)
	{
		if(!_prefix.endsWith("-")) _prefix+="-";

		Properties _prop = new Properties();
		_prop.putAll(predef);
		for(int i = 0; i<_xpp.getAttributeCount(); i++) {
			String _key = _xpp.getAttributeName(i);
			if ("id".equalsIgnoreCase(_key)) {
				_prop.setProperty("id", _xpp.getAttributeValue(i));
			}
			else
			if ("link".equalsIgnoreCase(_key)) {
				_prop.setProperty("link", _xpp.getAttributeValue(i));
			}
			else
			if ("layer".equalsIgnoreCase(_key)) {
				_prop.setProperty("layer", _xpp.getAttributeValue(i));
			}
			else
			if (_key.startsWith(_prefix)) {
				_prop.setProperty(_key, _xpp.getAttributeValue(i));
			}
			else
			{
				_prop.setProperty(_prefix+_key, _xpp.getAttributeValue(i));
			}
		}
		return _prop;
	}

	public static Properties toPropertiesWithPrefix(String _prefix, String... predef)
	{
		if(!_prefix.endsWith("-")) _prefix+="-";

		Properties _prop = new Properties();

		for(int i = 1; i<predef.length; i+=2)
		{
			String _key = predef[i-1];
			if (_key.startsWith(_prefix)) {
				_prop.setProperty(predef[i-1], predef[i]);
			}
			else
			{
				_prop.setProperty(_prefix+predef[i-1], predef[i]);
			}
		}

		return _prop;
	}

	public static Properties toProperties(String... predef)
	{
		Properties _prop = new Properties();

		for(int i = 1; i<predef.length; i+=2)
		{
			_prop.setProperty(predef[i-1], predef[i]);
		}

		return _prop;
	}

	public static Properties toProperties(Properties _prop, String... predef)
	{
		for(int i = 1; i<predef.length; i+=2)
		{
			_prop.setProperty(predef[i-1], predef[i]);
		}

		return _prop;
	}

	public static Properties toPropertiesByPrefix(Properties _prop, String _prefix, Properties _table)
	{
		for(String _key : _table.stringPropertyNames())
		{
			if(_key.startsWith(_prefix))
			{
				_prop.setProperty(_key.substring(_prefix.length()), _table.getProperty(_key));
			}
		}
		return _prop;
	}

	public static Properties toProperties(Properties _prop, String _key, String _val)
	{
		_prop.setProperty(_key, _val);

		return _prop;
	}

	public static Properties attributesToProperties(AttributesNode _attrs, String... predef)
	{
		Properties _prop = new Properties();

		for(int i = 1; i<predef.length; i+=2)
		{
			_prop.setProperty(predef[i-1], predef[i]);
		}

		return attributesToProperties(_attrs, _prop);
	}

	public static Properties attributesToProperties(AttributesNode _attrs, Properties predef)
	{
		Properties _prop = new Properties();
		_prop.putAll(predef);
		for(Node _node : _attrs.getLastChild().getChildren())
		{
			AttributeNode _attr = (AttributeNode) _node;
			_prop.setProperty(_attr.getName().toString().toLowerCase(), _attr.getValue().toString().trim());
		}

		return _prop;
	}

	
	public static <T> List<T> toList(T... items)
	{
		List<T> _ret = new Vector<T>();
		for(T _item : items)
		{
			_ret.add(_item);
		}
		return _ret;
	}

	public static int[] toIntArray(String _vals)
	{
		String[] _parts = _vals.split(",");
		int[] _ret = new int[_parts.length];
		for(int i=0; i<_parts.length; i++)
		{
			_ret[i] = CommonUtil.createInteger(_parts[i]);
		}
		return _ret;
	}

	public static float[] toFloatArray(String _vals)
	{
		String[] _parts = _vals.split(",");
		float[] _ret = new float[_parts.length];
		for(int i=0; i<_parts.length; i++)
		{
			_ret[i] = Float.parseFloat(_parts[i]);
		}
		return _ret;
	}

	@SneakyThrows
	public static boolean handlePage(PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
	{
		_p.newPage();
		Properties _attributes = PmlParserUtil.toPropertiesWithPrefix("page-", _xpp);
		_p.resolveFromClassDefinitionWithPrefix("page-", _attributes);

		int _rotate = _p.getAttributeValueOrPrefixAsInt(_attributes, "rotate", true, "page");
		_p.getPage().setRotate(_rotate);
		int[] _mb = PmlParserUtil.toIntArray(_p.getAttributeValueOrPrefix(_attributes, "mediabox", false, "page"));
		if(_mb.length==4)
		{
			_p.getPage().setMediabox(_mb[0], _mb[1], _mb[2], _mb[3]);
		}
		else
		if(_mb.length==2)
		{
			_p.getPage().setMediabox(0, 0, _mb[0], _mb[1]);
		}
		else
		{
			_p.getPage().setMediabox(0, 0, _mb[0], _mb[0]);
		}

		int _event;
		while((_event = _xpp.next()) != XmlPullParser.END_TAG)
		{
			if (_event == XmlPullParser.START_TAG)
			{

				Properties _attr = PmlParserUtil.toProperties(_xpp);
				String _id = _p.getAttributeValueOrNull(_attr, "id");
				if(CommonUtil.isNotBlank(_id))
				{
					_p._pdf.newNamedDestination(_id, _p.getPage());
				}

				PageLevelDirective _pld = PageLevelDirective.valueOf(_xpp.getName());
				if(_pld!=null)
				{
					_pld.execute(_xpp.getName(), _p, _xpc, _xpp);
				}
				else if (_xpp.getName().startsWith("draw-"))
				{
					handlePageDraw(_p, _xpc, _xpp);
				}
			}
		}

		_p.handlePageHeaderAndFooter(_attributes, _p._page_num);
		return false;
	}

	@SneakyThrows
	public static void handlePageDraw(PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
	{
		Properties _attributes = PmlParserUtil.toProperties(_xpp);

		if ("draw-rect".equalsIgnoreCase(_xpp.getName()))
		{
			handlePageDrawRect(_p, _xpc, _xpp, _attributes);
		}
		else
		{
			String _type = _p.getAttributeValueOrNull(_attributes, "type");
			if ("rect".equalsIgnoreCase(_type))
			{
				handlePageDrawRect(_p, _xpc, _xpp, _attributes);
			}
			else
			if ("script".equalsIgnoreCase(_type))
			{
				handlePageDrawScript(_p, _xpc, _xpp, _attributes);
			}
			/*
			else
			if ("lua".equalsIgnoreCase(_type))
			{
				handlePageDrawLua(_p, _xpc, _xpp, _attributes);
			}
			*/
			else // default
			{
				handlePageDrawScript(_p, _xpc, _xpp, _attributes);
			}
		}
	}

	@SneakyThrows
	public static void handlePageDrawRect(PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp, Properties _attributes)
	{
		_p.resolveFromClassDefinition(_attributes);

		int[] _pos = PmlParserUtil.toIntArray(_p.getAttributeValueOrNull(_attributes, "pos"));
		int _w = CommonUtil.createInteger(_p.getAttributeValueOrNull(_attributes, "width"));
		int _h = CommonUtil.createInteger(_p.getAttributeValueOrNull(_attributes, "height"));

		String _fill = _p.getAttributeValueOrNull(_attributes, "fill-color");
		String _stroke = _p.getAttributeValueOrNull(_attributes, "stroke-color");
		float _swidth = CommonUtil.toFloat(_p.getAttributeValueOrDefault(_attributes, "stroke", "1"));

		boolean _bg = CommonUtil.toBoolean(_p.getAttributeValueOrDefault(_attributes, "background", "false"));

		int[] _dash = null;
		String _ldash = _p.getAttributeValueOrNull(_attributes, "dash");
		if(CommonUtil.isNotBlank(_ldash))
		{
			_dash = PmlParserUtil.toIntArray(_ldash);
		}

		_p.doDrawRect(_pos[0], _pos[1], _w, _h, _fill, _stroke, _swidth, _dash, _bg, _attributes.getProperty("layer", "off"));

		_xpp.next();
	}

	@SneakyThrows
	public static void handlePageDrawScript(PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp, Properties _attributes)
	{
		String _layer = _attributes.getProperty("layer", "false");
		String _source = _attributes.getProperty("src", "");
		boolean _bg = CommonUtil.toBoolean(_p.getAttributeValueOrDefault(_attributes, "background", "false"));
		boolean _tmpl = CommonUtil.toBoolean(_p.getAttributeValueOrDefault(_attributes, "templating", "false"));

		PmlDrawScriptContext _psc = PmlDrawScriptContext.create(_p, _xpc);
		extractBindings(_p, _xpc, _xpp, _psc);

		StringBuilder _sb = new StringBuilder();
		extractSource(_p, _xpc, _xpp, _source, _sb);

		_psc.runDraw(_sb.toString(), _tmpl, false, _layer, _bg);
	}

	@SneakyThrows
	public static void extractSource(PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp, String _source, StringBuilder _sb)
	{
		if(CommonUtil.isNotBlank(_source))
		{
			_sb.append(IOUtil.toString(PmlUtil.sourceToLoader(_source, _xpc.getBasedir(), _xpc.getFile(), _p.ZIP_MOUNTS, _p.DIR_MOUNTS).getInputStream()));
			_xpp.next();
		}
		else
		{
			int _event = -1;
			while((_event = _xpp.next())==XmlPullParser.TEXT)
			{
				_sb.append(_xpp.getText());
			}
		}
	}

	public static void extractBindings(PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp, AbstractPmlScriptContext _psc)
	{
		int _count = _xpp.getAttributeCount();
		for(int i = 0; i<_count; i++)
		{
			String _key = _xpp.getAttributeName(i).toLowerCase();
			String _val = _xpp.getAttributeValue(i);
			if(_key.startsWith("int:"))
			{
				_psc.getBinding().put(_key.substring(4),CommonUtil.createInteger(_val));
			}
			else
			if(_key.startsWith("i:"))
			{
				_psc.getBinding().put(_key.substring(2),CommonUtil.createInteger(_val));
			}
			else
			if(_key.startsWith("float:"))
			{
				_psc.getBinding().put(_key.substring(6),CommonUtil.toFloat(_val));
			}
			else
			if(_key.startsWith("f:"))
			{
				_psc.getBinding().put(_key.substring(2),CommonUtil.toFloat(_val));
			}
			else
			if(_key.startsWith("long:"))
			{
				_psc.getBinding().put(_key.substring(5),CommonUtil.toLong(_val));
			}
			else
			if(_key.startsWith("l:"))
			{
				_psc.getBinding().put(_key.substring(2),CommonUtil.toLong(_val));
			}
			else
			if(_key.startsWith("double:"))
			{
				_psc.getBinding().put(_key.substring(7),CommonUtil.toDouble(_val));
			}
			else
			if(_key.startsWith("d:"))
			{
				_psc.getBinding().put(_key.substring(2),CommonUtil.toDouble(_val));
			}
			else
			if(_key.startsWith("bool:"))
			{
				_psc.getBinding().put(_key.substring(5),CommonUtil.toBoolean(_val));
			}
			else
			if(_key.startsWith("b:"))
			{
				_psc.getBinding().put(_key.substring(2),CommonUtil.toBoolean(_val));
			}
			else
			if(_key.startsWith("string:"))
			{
				_psc.getBinding().put(_key.substring(7),_val);
			}
			else
			if(_key.startsWith("s:"))
			{
				_psc.getBinding().put(_key.substring(2),_val);
			}
			else
			if(_key.startsWith("_"))
			{
				_psc.getBinding().put(_key,_val);
			}
			else
			if("json".equalsIgnoreCase(_key))
			{
				_psc.loadContextFromHjson(new InputStreamReader(PmlUtil.sourceToLoader(_val, _xpc.getBasedir(), _xpc.getFile(), _p.ZIP_MOUNTS, _p.DIR_MOUNTS).getInputStream(), StandardCharsets.UTF_8));
			}
		}
	}

	public static boolean handleLua(PmlParser _p, PmlParserContext _xpc, XmlPullParser _xpp)
	{
		Properties _attributes = PmlParserUtil.toProperties(_xpp);
		String _layer = _attributes.getProperty("layer", "false");
		String _source = _attributes.getProperty("src", "");
		boolean _bg = CommonUtil.toBoolean(_p.getAttributeValueOrDefault(_attributes, "background", "false"));

		StringBuilder _sb = new StringBuilder();
		extractSource(_p, _xpc, _xpp, _source, _sb);

		PmlLuayScriptContext _c = PmlLuayScriptContext.create(_p, _xpc);
		if(CommonUtil.isNotBlank(_source))
		{
			File _file = PmlUtil.sourceToFile(_source, _xpc.getBasedir(), _xpc.getFile().getParentFile());
			_c.runLua(_file, _sb.toString(), _xpc, _xpp, _attributes, _layer, _bg);
		}
		else
		{
			_c.runLua(_xpc.getFile(), _sb.toString(), _xpc, _xpp, _attributes, _layer, _bg);
		}

		return false;
	}

	public static List<String> defaultSearchPaths()
	{
		List<String> _ret = new Vector<>();
		for(String _App : ListMapUtil.toList("pmltopdf","luay"))
		{
			_ret.add(OsUtil.getUserConfigDirectory(_App));
			_ret.add(OsUtil.getUserDataDirectory(_App));
			_ret.add(OsUtil.getUnixyUserDataDirectory(_App));
		}
		_ret.add(OsUtil.getUserDataDirectory());
		return _ret;
	}

	public static List<String> defaultLuayPaths()
	{
		List<String> _ret = new Vector<>();

		String LUAY_VAR = null;
		for(String _Var : ListMapUtil.toList("LUAY_HOME","LUAY_PATH"))
			if((LUAY_VAR = System.getenv(_Var))!=null)
			{
				for(String _Path : CommonUtil.split(LUAY_VAR, ":"))
				{
					_ret.add(_Path);
				}
			}

		if(_ret.size()==0)
		{
			_ret.add(OsUtil.getUserDataDirectory("luay/lib"));
			_ret.add(OsUtil.getUnixyUserDataDirectory("luay/lib"));
		}
		return _ret;
	}

}
