package com.github.terefang.jmelange.pdf.ml.script;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.ListMapUtil;
import com.github.terefang.jmelange.commons.util.OsUtil;
import com.github.terefang.jmelange.pdf.ml.PmlParser;
import com.github.terefang.jmelange.pdf.ml.PmlParserContext;
import com.github.terefang.jmelange.pdf.ml.PmlParserUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import luay.lib.LuayStringifierFunction;
import luay.main.LuayBuilder;
import luay.main.LuayContext;
import luay.main.LuayHelper;
import luay.vm.LuaValue;
import luay.vm.Varargs;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

@Slf4j
public class PmlLuayScriptContext
{

	public static PmlLuayScriptContext create(PmlParser _p, PmlParserContext _xpc)
	{
		PmlLuayScriptContext _c = new PmlLuayScriptContext();
		_c._proxy = new ProxyPmlScriptContext();
		_c._proxy.parser = _p;
		_c._proxy.parserContext = _xpc;
		_c._proxy.page = _p.getPage(false);
		_c._proxy.binding = new HashMap<>();
		_c._proxy.extractBindings(_c._proxy, _xpc.getProperties());
		return _c;
	}


	public static PmlLuayScriptContext create(PmlParser _p, PmlParserContext _xpc, Map<String, Object> _b)
	{
		PmlLuayScriptContext _c = create(_p, _xpc);
		_c._proxy.binding.putAll(_b);
		return _c;
	}

	static class ProxyPmlScriptContext extends AbstractPmlScriptContext
	{

	}

	ProxyPmlScriptContext _proxy;
	LuayContext luayContext;
	LuayBuilder luayContextBuilder;

	@SneakyThrows
	public void runLua(File _sref, String _source, PmlParserContext _xpc, XmlPullParser _xpp, Properties _attributes, String _layer, boolean _bg)
	{
		runLua(_sref, new StringReader(_source), _xpc, _xpp, _attributes, _layer, _bg);
	}

	@SneakyThrows
	public void runLua(File _sref, String _source, PmlParserContext _xpc, XmlPullParser _xpp, Properties _attributes, String _layer, boolean _bg, Map<String, Object> _data)
	{
		runLua(_sref, new StringReader(_source), _xpc, _xpp, _attributes, _layer, _bg, _data);
	}

	@SneakyThrows
	public void runLua(File _source, PmlParserContext _xpc, XmlPullParser _xpp, Properties _attributes, String _layer, boolean _bg)
	{
		runLua(_source, new FileReader(_source), _xpc, _xpp, _attributes, _layer, _bg);
	}

	@SneakyThrows
	public void runLua(File _source, PmlParserContext _xpc, XmlPullParser _xpp, Properties _attributes, String _layer, boolean _bg, Map<String, Object> _data)
	{
		runLua(_source, new FileReader(_source), _xpc, _xpp, _attributes, _layer, _bg,_data);
	}

	@SneakyThrows
	public void runLua(File _sref, Reader _source, PmlParserContext _xpc, XmlPullParser _xpp, Properties _attributes, String _layer, boolean _bg)
	{
		runLua(_sref,_source, _xpc,_xpp, _attributes, _layer, _bg, null);
	}
	public void runLua(File _sref, Reader _source, PmlParserContext _xpc, XmlPullParser _xpp, Properties _attributes, String _layer, boolean _bg, Map<String, Object> _data)
	{
		try
		{
			this.luayContextBuilder = LuayBuilder.create()
					.outputStream(System.out)
					.errorStream(System.err)
					.allLibraries();

			if(_sref != null && _sref.getParent() != null)
			{
				this.luayContextBuilder.searchPath(_sref.getParent());
			}

			if(_xpc.getBasedir() != null)
			{
				this.luayContextBuilder.searchPath(_xpc.getBasedir().getAbsolutePath());
			}

			if(_xpc.getSearchPath() != null)
			{
				this.luayContextBuilder.searchPath(_xpc.getSearchPath());
			}

			for(String _Path : PmlParserUtil.defaultSearchPaths())
			{
				this.luayContextBuilder.searchPath(_Path);
			}

			for(String _Path : PmlParserUtil.defaultLuayPaths())
			{
				this.luayContextBuilder.searchPath(_Path);
			}

			this.luayContext = this.luayContextBuilder
					.build();

			this.luayContext.set("_pdf", this);

			if(_attributes!=null && _attributes.size()>0)
			{
				AbstractPmlScriptContext.extractBindings( this._proxy, _attributes);
				this._proxy.getBinding().forEach((_k,_v)->{ this.luayContext.set(_k, _v); });
			}

			if(_data!=null && _data.size()>0)
			{
				_data.forEach((_k,_v)->{ this.luayContext.set(_k, _v); });
			}

			if(_sref != null)
			{
				this.luayContext.setBase(_sref);
			}
			Object _ret = this.luayContext.execute(_source, _sref.getAbsolutePath());
		}
		catch (Exception _xe)
		{
			log.error(Objects.toString(_sref), _xe);
		}
		finally
		{
			this.luayContext.unsetAll();
			this.luayContext = null;
			this.luayContextBuilder = null;
		}
	}

	public void log_debug(Varargs _vargs)
	{
		this._proxy.logDebug(LuayStringifierFunction._stringify_vararg(_vargs, false).tojstring());
	}
	public void log_info(Varargs _vargs)
	{
		this._proxy.logInfo(LuayStringifierFunction._stringify_vararg(_vargs, false).tojstring());
	}
	public void log_warn(Varargs _vargs)
	{
		this._proxy.logWarn(LuayStringifierFunction._stringify_vararg(_vargs, false).tojstring());
	}
	public void log_error(Varargs _vargs)
	{
		this._proxy.logError(LuayStringifierFunction._stringify_vararg(_vargs, false).tojstring());
	}

	public void set_title(Varargs _vargs)
	{
		this._proxy.setTitle(_vargs.checkjstring(1));
	}

	public void set_creator(Varargs _vargs)
	{
		this._proxy.setCreator(_vargs.checkjstring(1));
	}

	public void set_author(Varargs _vargs)
	{
		this._proxy.setAuthor(_vargs.checkjstring(1));
	}

	public void set_subject(Varargs _vargs)
	{
		this._proxy.setSubject(_vargs.checkjstring(1));
	}

	public void set_keywords(Varargs _vargs)
	{
		this._proxy.setKeywords(_vargs.checkjstring(1));
	}

	public void set_producer(Varargs _vargs)
	{
		this._proxy.setProducer(_vargs.checkjstring(1));
	}

	public void new_page(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.newPage(_vargs.checkint(1), _vargs.checkint(2));
		}
		else
		if(_vargs.narg()==4)
		{
			this._proxy.newPage(_vargs.checkint(1), _vargs.checkint(2), _vargs.checkint(3), _vargs.checkint(4));
		}
		else
		{
			this._proxy.newPage();
		}
	}

	public void page_mediabox(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.pageMediabox(_vargs.checkint(1), _vargs.checkint(2));
		}
		else
		if(_vargs.narg()==4)
		{
			this._proxy.pageMediabox(_vargs.checkint(1), _vargs.checkint(2), _vargs.checkint(3), _vargs.checkint(4));
		}
	}


	@SneakyThrows
	public void end_page(Varargs _vargs)
	{
		this._proxy.endPage();
	}

	public void place_image(Varargs _vargs)
	{
		if(_vargs.narg()==3)
		{
			this._proxy.placeImage(_vargs.checkjstring(1), _vargs.checkint(2), _vargs.checkint(3));
		}
		else
		if(_vargs.narg()==5)
		{
			this._proxy.placeImage(_vargs.checkjstring(1), _vargs.checkint(2), _vargs.checkint(3), _vargs.checkint(4), _vargs.checkint(5), false, "false");
		}
		else
		if(_vargs.narg()==6)
		{
			this._proxy.placeImage(_vargs.checkjstring(1), _vargs.checkint(2), _vargs.checkint(3), _vargs.checkint(4), _vargs.checkint(5), _vargs.checkboolean(6), "false");
		}
		else
		if(_vargs.narg()==7)
		{
			this._proxy.placeImage(_vargs.checkjstring(1), _vargs.checkint(2), _vargs.checkint(3), _vargs.checkint(4), _vargs.checkint(5), _vargs.checkboolean(6), _vargs.checkjstring(7));
		}
		else
		{
			log.warn("wrong parameters for place_image - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void place_svg(Varargs _vargs)
	{
		if(_vargs.narg()==3)
		{
			this._proxy.placeSVG(_vargs.checkjstring(1), _vargs.checkint(2), _vargs.checkint(3));
		}
		else
		if(_vargs.narg()==5)
		{
			this._proxy.placeSvg(_vargs.checkjstring(1), _vargs.checkint(2), _vargs.checkint(3), _vargs.checkint(4), _vargs.checkint(5), false, "false");
		}
		else
		if(_vargs.narg()==6)
		{
			this._proxy.placeSvg(_vargs.checkjstring(1), _vargs.checkint(2), _vargs.checkint(3), _vargs.checkint(4), _vargs.checkint(5), _vargs.checkboolean(6), "false");
		}
		else
		if(_vargs.narg()==7)
		{
			this._proxy.placeSvg(_vargs.checkjstring(1), _vargs.checkint(2), _vargs.checkint(3), _vargs.checkint(4), _vargs.checkint(5), _vargs.checkboolean(6), _vargs.checkjstring(7));
		}
		else
		{
			log.warn("wrong parameters for place_svg - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void place_header(Varargs _vargs)
	{
		if(_vargs.narg()==5)
		{
			//(int _x, int _y, int _level, String _fn, String _text)
			{
				this._proxy.placeHeading("", _vargs.checkint(1), _vargs.checkint(2), _vargs.checkint(3), _vargs.checkjstring(4), -1, _vargs.checkjstring(5), "");
			}
		}
		else
		if(_vargs.narg()==6)
		{
			//(int _x, int _y, int _level, String _fn, String _text, String _outline)
			{
				this._proxy.placeHeading("", _vargs.checkint(1), _vargs.checkint(2), _vargs.checkint(3), _vargs.checkjstring(4), -1, _vargs.checkjstring(5), _vargs.checkjstring(6));
			}
		}
		else
		if(_vargs.narg()==7)
		{
			//(String _id, int _x, int _y, int _level, String _fn, String _text, String _outline)
			{
				this._proxy.placeHeading(_vargs.checkjstring(1), _vargs.checkint(2), _vargs.checkint(3), _vargs.checkint(4), _vargs.checkjstring(5), -1, _vargs.checkjstring(6), _vargs.checkjstring(7));
			}
		}
		else
		if(_vargs.narg()==8)
		{
			//(String _id, int _x, int _y, int _level, String _fn, int _fs, String _text, String _outline)
			{
				this._proxy.placeHeading(_vargs.checkjstring(1), _vargs.checkint(2), _vargs.checkint(3), _vargs.checkint(4), _vargs.checkjstring(5), _vargs.checkint(6), "#000", _vargs.checkjstring(7), _vargs.checkjstring(8));
			}
		}
		else
		if(_vargs.narg()==9)
		{
			//(String _id, int _x, int _y, int _level, String _fn, int _fs, String _color, String _text, String _outline)
			{
				this._proxy.placeHeading(_vargs.checkjstring(1), _vargs.checkint(2), _vargs.checkint(3), _vargs.checkint(4), _vargs.checkjstring(5), _vargs.checkint(6), _vargs.checkjstring(7), _vargs.checkjstring(8), _vargs.checkjstring(9));
			}
		}
		else
		if(_vargs.narg()==10)
		{
			//(String _id, int _x, int _y, int _level, String _fn, int _fs, String _color, String _text, String _outline, String _layer)
			{
				this._proxy.placeHeading(_vargs.checkjstring(1), _vargs.checkint(2), _vargs.checkint(3), _vargs.checkint(4), _vargs.checkjstring(5), _vargs.checkint(6), _vargs.checkjstring(7), _vargs.checkjstring(8), _vargs.checkjstring(9), _vargs.checkjstring(10));
			}
		}
		else
		{
			log.warn("wrong parameters for place_header - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void new_content(Varargs _vargs)
	{
		if(_vargs.narg()==0)
		{
			this._proxy.newContent();
		}
		else
		if(_vargs.narg()==1)
		{
			this._proxy.newContent(_vargs.checkboolean(1));
		}
		else
		{
			log.warn("wrong parameters for new_content - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void end_content(Varargs _vargs)
	{
		this._proxy.endContent();
	}

	public void add_content(Varargs _vargs)
	{
		for(int _i=1; _i<= _vargs.narg(); _i++)
		{
			this._proxy.addContent(LuayStringifierFunction._stringify(_vargs.arg(_i), false).tojstring());
		}
	}

	public void load_font(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.loadFont(_vargs.checkjstring(1), _vargs.checkjstring(2));
		}
		else
		if(_vargs.narg()==3)
		{
			this._proxy.loadFont(_vargs.checkjstring(1), _vargs.checkjstring(2), _vargs.checkjstring(3));
		}
		else
		if(_vargs.narg()==4)
		{
			this._proxy.loadFont(_vargs.checkjstring(1), _vargs.checkjstring(2), _vargs.checkjstring(3), _vargs.checkjstring(4));
		}
		else
		{
			log.warn("wrong parameters for load_font - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void mount_zip(Varargs _vargs)
	{
		if(_vargs.narg()==1)
		{
			this._proxy.mountZip(_vargs.checkjstring(1));
		}
		else
		{
			log.warn("wrong parameters for mount_zip - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void mount_dir(Varargs _vargs)
	{
		if(_vargs.narg()==1)
		{
			this._proxy.mountDir(_vargs.checkjstring(1));
		}
		else
		{
			log.warn("wrong parameters for mount_dir - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void define_entity(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.defineEntity(_vargs.checkjstring(1), _vargs.checkint(2));
		}
		else
		{
			log.warn("wrong parameters for define_entity - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void define_icon(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.defineIcon(_vargs.checkjstring(1), _vargs.checkjstring(2), _vargs.checkint(3));
		}
		else
		{
			log.warn("wrong parameters for define_icon - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void load_image(Varargs _vargs)
	{
		if(_vargs.narg()==2)//(String _id, String _src)
		{
			this._proxy.loadImage(_vargs.checkjstring(1), _vargs.checkjstring(2));
		}
		else
		if(_vargs.narg()==3)//(String _id, String _src, String _compress)
		{
			this._proxy.loadImage(_vargs.checkjstring(1), _vargs.checkjstring(2), _vargs.checkjstring(3));
		}
		else
		if(_vargs.narg()==5)//(String _id, String _src, String _compress, String _transp, float _alphabias)
		{
			this._proxy.loadImage(_vargs.checkjstring(1), _vargs.checkjstring(2), _vargs.checkjstring(3), _vargs.checkjstring(4), (float) _vargs.checkdouble(5));
		}
		else
		if(_vargs.narg()==6)//(String _id, String _src, String _compress, String _transp, float _alphabias, int _rot)
		{
			this._proxy.loadImage(_vargs.checkjstring(1), _vargs.checkjstring(2), _vargs.checkjstring(3), _vargs.checkjstring(4), (float) _vargs.checkdouble(5), _vargs.checkint(6));
		}
		else
		{
			log.warn("wrong parameters for load_image - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public LuaValue load_image_id(Varargs _vargs)
	{
		String _id = UUID.randomUUID().toString();
		if(_vargs.narg()==1)//(String _src)
		{
			this._proxy.loadImage(_id, _vargs.checkjstring(1));
		}
		else
		if(_vargs.narg()==2)//(String _src, String _compress)
		{
			this._proxy.loadImage(_id, _vargs.checkjstring(1), _vargs.checkjstring(2));
		}
		else
		if(_vargs.narg()==4)//(String _src, String _compress, String _transp, float _alphabias)
		{
			this._proxy.loadImage(_id, _vargs.checkjstring(1), _vargs.checkjstring(2), _vargs.checkjstring(3), (float) _vargs.checkdouble(4));
		}
		else
		if(_vargs.narg()==5)//(String _src, String _compress, String _transp, float _alphabias, int _rot)
		{
			this._proxy.loadImage(_id, _vargs.checkjstring(1), _vargs.checkjstring(2), _vargs.checkjstring(3), (float) _vargs.checkdouble(4), _vargs.checkint(5));
		}
		else
		{
			log.warn("wrong parameters for load_image_id - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
		return LuaValue.valueOf(_id);
	}

	/*
	public void loadSvg(String _id, String _src, float _s, String _compress, String _transp, float _alphabias, int _rot)
	{
		this.getParser().loadSvg(_id, _src, _s, _compress, _transp, _alphabias, _rot, this.getParserContext());
	}
	*/

	public void save(Varargs _vargs)
	{
		this._proxy.gSave();
	}

	public void restore(Varargs _vargs)
	{
		this._proxy.gRestore();
	}

	public void start_text(Varargs _vargs)
	{
		this._proxy.startText();
	}

	public void end_text(Varargs _vargs)
	{
		this._proxy.endText();
	}

	public void move_text(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.moveText(_vargs.checkdouble(1), _vargs.checkdouble(2));
		}
		else
		{
			log.warn("wrong parameters for move_text - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void font(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.font(_vargs.checkjstring(1), _vargs.checkdouble(2));
		}
		else
		{
			log.warn("wrong parameters for font - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void set_font(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.setFont(_vargs.checkjstring(1), _vargs.checkdouble(2));
		}
		else
		{
			log.warn("wrong parameters for set_font - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void text(Varargs _vargs)
	{
		for(int _i=1; _i<= _vargs.narg(); _i++)
		{
			this._proxy.text(_vargs.checkjstring(_i));
		}
	}

	public void text_center(Varargs _vargs)
	{
		if(_vargs.narg()==1)
		{
			this._proxy.textCenter(_vargs.checkjstring(1));
		}
		else
		if(_vargs.narg()==2)
		{
			this._proxy.textCenter(_vargs.checkjstring(1), (float) _vargs.checkdouble(2));
		}
		else
		{
			log.warn("wrong parameters for text_center - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void draw_text(Varargs _vargs)
	{
		if(_vargs.narg()==3)//(float _x, float _y, String _text)
		{
			this._proxy.drawText((float)_vargs.checkdouble(1), (float)_vargs.checkdouble(2), _vargs.checkjstring(3));
		}
		else
		{
			log.warn("wrong parameters for draw_text - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void draw_string(Varargs _vargs)
	{
		if(_vargs.narg()==3)//(String _text, float _x, float _y)
		{
			this._proxy.drawString(_vargs.checkjstring(1), (float) _vargs.checkdouble(2), (float) _vargs.checkdouble(3));
		}
		else
		if(_vargs.narg()==4)//(String _text, float _x, float _y, String _align)
		{
			this._proxy.drawString(_vargs.checkjstring(1), (float) _vargs.checkdouble(2), (float) _vargs.checkdouble(3), _vargs.checkjstring(4));
		}
		else
		if(_vargs.narg()==5)//(String _text, float _x, float _y, String _align, float _w)
		{
			this._proxy.drawString(_vargs.checkjstring(1), (float) _vargs.checkdouble(2), (float) _vargs.checkdouble(3), _vargs.checkjstring(4), (float) _vargs.checkdouble(5));
		}
		else
		if(_vargs.narg()==6)//(String _text, float _x, float _y, String _align, float _w, float _hs)
		{
			this._proxy.drawString(_vargs.checkjstring(1), (float) _vargs.checkdouble(2), (float) _vargs.checkdouble(3), _vargs.checkjstring(4), (float) _vargs.checkdouble(5), (float) _vargs.checkdouble(6));
		}
		else
		{
			log.warn("wrong parameters for text_center - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public LuaValue text_width(Varargs _vargs)
	{
		if(_vargs.narg()==1)//(String _text)
		{
			return LuaValue.valueOf(this._proxy.textWidth(_vargs.checkjstring(1)));
		}
		else
		if(_vargs.narg()==3)//(String _text, String _fn, float _size)
		{
			return LuaValue.valueOf(this._proxy.textWidth(_vargs.checkjstring(1), _vargs.checkjstring(2), (float) _vargs.checkdouble(3)));
		}
		else
		if(_vargs.narg()==6)//(String _text, String _fn, float _size, float _wsp, float _csp, float _hsp)
		{
			return LuaValue.valueOf(this._proxy.textWidth(_vargs.checkjstring(1), _vargs.checkjstring(2), (float) _vargs.checkdouble(3)
					, (float) _vargs.checkdouble(4), (float) _vargs.checkdouble(5), (float) _vargs.checkdouble(6)));
		}
		else
		{
			log.warn("wrong parameters for text_width - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
		return LuaValue.NIL;
	}

	public void render_text(Varargs _vargs)
	{
		if(_vargs.narg()==1)//(String _text)
		{
			this._proxy.renderText(_vargs.checkjstring(1));
		}
		else
		if(_vargs.narg()==3)//(String _arg, String _align, float _w)
		{
			this._proxy.renderText(_vargs.checkjstring(1), _vargs.checkjstring(2), (float) _vargs.checkdouble(3));
		}
		else
		if(_vargs.narg()==5)//(String _arg, String _align, float _w, String _fn, float _fs)
		{
			this._proxy.renderText(_vargs.checkjstring(1), _vargs.checkjstring(2), (float) _vargs.checkdouble(3), _vargs.checkjstring(4), (float) _vargs.checkdouble(5));
		}
		else
		{
			log.warn("wrong parameters for render_text - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public LuaValue start_layer(Varargs _vargs)
	{
		if(_vargs.narg()==0)
		{
			String _id = UUID.randomUUID().toString();
			this._proxy.startLayer(_id);
			return LuaValue.valueOf(_id);
		}
		else
		if(_vargs.narg()==1)
		{
			this._proxy.startLayer(_vargs.checkjstring(1));
			return _vargs.arg(1);
		}
		else
		{
			log.warn("wrong parameters for start_layer - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
		return LuaValue.NIL;
	}

	public void end_layer(Varargs _vargs)
	{
		this._proxy.endLayer();
	}

	public void fill_alpha(Varargs _vargs)
	{
		if(_vargs.narg()==1)
		{
			this._proxy.fillAlpha((float) _vargs.checkdouble(1));
		}
		else
		{
			log.warn("wrong parameters for fill_alpha - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void stroke_alpha(Varargs _vargs)
	{
		if(_vargs.narg()==1)
		{
			this._proxy.strokeAlpha((float) _vargs.checkdouble(1));
		}
		else
		{
			log.warn("wrong parameters for stroke_alpha - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void fill_color(Varargs _vargs)
	{
		if(_vargs.narg()==1 && _vargs.isnumber(1))
		{
			this._proxy.fillColor((float)_vargs.checkdouble(1));
		}
		else
		if(_vargs.narg()==1 && _vargs.isstring(1))
		{
			this._proxy.fillColor(_vargs.checkjstring(1));
		}
		else
		{
			log.warn("wrong parameters for fill_color - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void stroke_color(Varargs _vargs)
	{
		if(_vargs.narg()==1 && _vargs.isnumber(1))
		{
			this._proxy.strokeColor((float)_vargs.checkdouble(1));
		}
		else
		if(_vargs.narg()==1 && _vargs.isstring(1))
		{
			this._proxy.strokeColor(_vargs.checkjstring(1));
		}
		else
		{
			log.warn("wrong parameters for stroke_color - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void color(Varargs _vargs)
	{
		if(_vargs.narg()==2 && _vargs.isnumber(2))
		{
			this._proxy.color(_vargs.checkboolean(1), (float)_vargs.checkdouble(2));
		}
		else
		if(_vargs.narg()==2 && _vargs.isstring(2))
		{
			this._proxy.color(_vargs.checkboolean(1), _vargs.checkjstring(2));
		}
		else
		if(_vargs.narg()==4)
		{
			this._proxy.color(_vargs.checkboolean(1), (float)_vargs.checkdouble(2), (float)_vargs.checkdouble(3), (float)_vargs.checkdouble(4));
		}
		else
		if(_vargs.narg()==5)
		{
			this._proxy.color(_vargs.checkboolean(1), (float)_vargs.checkdouble(2), (float)_vargs.checkdouble(3), (float)_vargs.checkdouble(4), (float)_vargs.checkdouble(5));
		}
		else
		{
			log.warn("wrong parameters for color - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void color_string(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.color(_vargs.checkboolean(1), _vargs.checkjstring(2));
		}
		else
		{
			log.warn("wrong parameters for color_string - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void color_gray(Varargs _vargs)
	{
		if(_vargs.narg()==2 && _vargs.isnumber(2))
		{
			this._proxy.color(_vargs.checkboolean(1), (float)_vargs.checkdouble(2));
		}
		else
		{
			log.warn("wrong parameters for color_gray - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void color_rgb(Varargs _vargs)
	{
		if(_vargs.narg()==4)
		{
			this._proxy.color(_vargs.checkboolean(1), (float)_vargs.checkdouble(2), (float)_vargs.checkdouble(3), (float)_vargs.checkdouble(4));
		}
		else
		{
			log.warn("wrong parameters for color_rgb - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void color_cmyk(Varargs _vargs)
	{
		if(_vargs.narg()==5)
		{
			this._proxy.color(_vargs.checkboolean(1), (float)_vargs.checkdouble(2), (float)_vargs.checkdouble(3), (float)_vargs.checkdouble(4), (float)_vargs.checkdouble(5));
		}
		else
		{
			log.warn("wrong parameters for color_cmyk - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void color_hsl(Varargs _vargs)
	{
		if(_vargs.narg()==4)
		{
			this._proxy.colorHSL(_vargs.checkboolean(1), (float)_vargs.checkdouble(2), (float)_vargs.checkdouble(3), (float)_vargs.checkdouble(4));
		}
		else
		{
			log.warn("wrong parameters for color_hsl - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void color_hsv(Varargs _vargs)
	{
		if(_vargs.narg()==4)
		{
			this._proxy.colorHSV(_vargs.checkboolean(1), (float)_vargs.checkdouble(2), (float)_vargs.checkdouble(3), (float)_vargs.checkdouble(4));
		}
		else
		{
			log.warn("wrong parameters for color_hsv - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void color_hwb(Varargs _vargs)
	{
		if(_vargs.narg()==4)
		{
			this._proxy.colorHWB(_vargs.checkboolean(1), (float)_vargs.checkdouble(2), (float)_vargs.checkdouble(3), (float)_vargs.checkdouble(4));
		}
		else
		{
			log.warn("wrong parameters for color_hwb - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void h_scale(Varargs _vargs)
	{
		if(_vargs.narg()==1)
		{
			this._proxy.hScale(_vargs.checkdouble(1));
		}
		else
		{
			log.warn("wrong parameters for h_scale - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void hline(Varargs _vargs)
	{
		if(_vargs.narg()==1)
		{
			this._proxy.hLine(_vargs.checkdouble(1));
		}
		else
		{
			log.warn("wrong parameters for hline - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void vline(Varargs _vargs)
	{
		if(_vargs.narg()==1)
		{
			this._proxy.vLine(_vargs.checkdouble(1));
		}
		else
		{
			log.warn("wrong parameters for vline - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void move_to(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.moveTo(_vargs.checkdouble(1), _vargs.checkdouble(2));
		}
		else
		{
			log.warn("wrong parameters for move_to - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void move_to_x(Varargs _vargs)
	{
		if(_vargs.narg()==1)
		{
			this._proxy.moveToX(_vargs.checkdouble(1));
		}
		else
		{
			log.warn("wrong parameters for move_to_x - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void move_to_y(Varargs _vargs)
	{
		if(_vargs.narg()==1)
		{
			this._proxy.moveToY(_vargs.checkdouble(1));
		}
		else
		{
			log.warn("wrong parameters for move_to_y - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void move_polar(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.movePolar(_vargs.checkdouble(1), _vargs.checkdouble(2));
		}
		else
		{
			log.warn("wrong parameters for move_polar - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void move_rel(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.moveRel(_vargs.checkdouble(1), _vargs.checkdouble(2));
		}
		else
		{
			log.warn("wrong parameters for move_rel - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void line(Varargs _vargs)
	{
		if(_vargs.narg()==4)
		{
			this._proxy.line(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkdouble(4));
		}
		else
		{
			log.warn("wrong parameters for line - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void line_to(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.lineTo(_vargs.checkdouble(1), _vargs.checkdouble(2));
		}
		else
		{
			log.warn("wrong parameters for line_to - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void curve_to(Varargs _vargs)
	{
		if(_vargs.narg()==5)
		{
			this._proxy.curveTo(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkdouble(4), _vargs.checkboolean(5));
		}
		else
		if(_vargs.narg()==6)
		{
			this._proxy.curveTo(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkdouble(4), _vargs.checkdouble(5), _vargs.checkdouble(6));
		}
		else
		{
			log.warn("wrong parameters for curve_to - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void line_width(Varargs _vargs)
	{
		if(_vargs.narg()==1)
		{
			this._proxy.lineWidth(_vargs.checkdouble(1));
		}
		else
		{
			log.warn("wrong parameters for line_width - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void arc(Varargs _vargs)
	{
		if(_vargs.narg()==6)
		{
			this._proxy.arc(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkdouble(4), _vargs.checkdouble(5), _vargs.checkdouble(6));
		}
		else
		if(_vargs.narg()==7)
		{
			this._proxy.arc(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkdouble(4), _vargs.checkdouble(5), _vargs.checkdouble(6), _vargs.checkboolean(7));
		}
		else
		{
			log.warn("wrong parameters for arc - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void spline(Varargs _vargs)
	{
		if(_vargs.narg()==4)
		{
			this._proxy.spline(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkdouble(4));
		}
		else
		{
			log.warn("wrong parameters for spline - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void pen(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.pen(_vargs.checkjstring(1), _vargs.checkdouble(2));
		}
		else
		if(_vargs.narg()==3)
		{
			this._proxy.pen(_vargs.checkjstring(1), _vargs.checkdouble(2), _vargs.checkdouble(3));
		}
		else
		{
			log.warn("wrong parameters for pen - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void ellipse(Varargs _vargs)
	{
		if(_vargs.narg()==4)
		{
			this._proxy.ellipse(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkdouble(4));
		}
		else
		{
			log.warn("wrong parameters for ellipse - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void clip(Varargs _vargs)
	{
		if(_vargs.narg()==1)
		{
			this._proxy.clip();
		}
		else
		if(_vargs.narg()==1)
		{
			this._proxy.clip(_vargs.checkboolean(1));
		}
		else
		{
			log.warn("wrong parameters for clip - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void circle(Varargs _vargs)
	{
		if(_vargs.narg()==3)//(double _x, double _y, double _r)
		{
			this._proxy.circle(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3));
		}
		else
		{
			log.warn("wrong parameters for circle - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void pie(Varargs _vargs)
	{
		if(_vargs.narg()==6)//(double _x, double _y, double _a, double _b, double _alpha, double _beta)
		{
			this._proxy.pie(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkdouble(4), _vargs.checkdouble(5), _vargs.checkdouble(6));
		}
		else
		{
			log.warn("wrong parameters for pie - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void rect(Varargs _vargs)
	{
		if(_vargs.narg()==4)//(double _a, double _b, double _c, double _d)
		{
			this._proxy.rect(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkdouble(4));
		}
		else
		{
			log.warn("wrong parameters for rect - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void rect_xy(Varargs _vargs)
	{
		if(_vargs.narg()==4)//(double _a, double _b, double _c, double _d)
		{
			this._proxy.rectXY(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkdouble(4));
		}
		else
		{
			log.warn("wrong parameters for rect_xy - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void arrow_to(Varargs _vargs)
	{
		if(_vargs.narg()==5)//(double _x1, double _y1, double _scale, boolean _from, boolean _to)
		{
			this._proxy.arrowTo(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkboolean(4), _vargs.checkboolean(5));
		}
		else
		{
			log.warn("wrong parameters for arrow_to - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void arrow_rel(Varargs _vargs)
	{
		if(_vargs.narg()==5)//(double _x1, double _y1, double _scale, boolean _from, boolean _to)
		{
			this._proxy.arrowRel(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkboolean(4), _vargs.checkboolean(5));
		}
		else
		{
			log.warn("wrong parameters for arrow_rel - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void arrow_polar(Varargs _vargs)
	{
		if(_vargs.narg()==5)//(double _dL, double _alpha, double _scale, boolean _from, boolean _to)
		{
			this._proxy.arrowPolar(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkboolean(4), _vargs.checkboolean(5));
		}
		else
		{
			log.warn("wrong parameters for arrow_rel - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void arrow(Varargs _vargs)
	{
		if(_vargs.narg()==7)//(double _x0, double _y0, double _x1, double _y1, double _scale, boolean _from, boolean _to)
		{
			this._proxy.arrow(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkdouble(4), _vargs.checkdouble(5), _vargs.checkboolean(6), _vargs.checkboolean(7));
		}
		else
		{
			log.warn("wrong parameters for arrow - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void matrix(Varargs _vargs)
	{
		if(_vargs.narg()==6)//(double _a, double _b, double _c, double _d, double _e, double _f)
		{
			this._proxy.matrix(_vargs.checkdouble(1), _vargs.checkdouble(2), _vargs.checkdouble(3), _vargs.checkdouble(4), _vargs.checkdouble(5), _vargs.checkdouble(6));
		}
		else
		{
			log.warn("wrong parameters for matrix - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void rotate(Varargs _vargs)
	{
		if(_vargs.narg()==1)
		{
			this._proxy.rotate(_vargs.checkdouble(1));
		}
		else
		{
			log.warn("wrong parameters for rotate - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void scale(Varargs _vargs)
	{
		if(_vargs.narg()==2)//(double _a, double _b)
		{
			this._proxy.scale(_vargs.checkdouble(1), _vargs.checkdouble(2));
		}
		else
		{
			log.warn("wrong parameters for scale - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void skew(Varargs _vargs)
	{
		if(_vargs.narg()==2)//(double _a, double _b)
		{
			this._proxy.skew(_vargs.checkdouble(1), _vargs.checkdouble(2));
		}
		else
		{
			log.warn("wrong parameters for skew - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void translate(Varargs _vargs)
	{
		if(_vargs.narg()==2)//(double _x, double _y)
		{
			this._proxy.translate(_vargs.checkdouble(1), _vargs.checkdouble(2));
		}
		else
		{
			log.warn("wrong parameters for translate - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void stroke(Varargs _vargs)
	{
		this._proxy.stroke();
	}

	public void fill(Varargs _vargs)
	{
		if(_vargs.narg()==0)
		{
			this._proxy.fill();
		}
		else
		if(_vargs.narg()==1)
		{
			this._proxy.fill(_vargs.checkboolean(1));
		}
		else
		{
			log.warn("wrong parameters for fill - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void fill_stroke(Varargs _vargs)
	{
		if(_vargs.narg()==0)
		{
			this._proxy.fillStroke();
		}
		else
		if(_vargs.narg()==1)
		{
			this._proxy.fillStroke(_vargs.checkboolean(1));
		}
		else
		{
			log.warn("wrong parameters for fill_stroke - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void line_dash(Varargs _vargs)
	{
		if(_vargs.narg()==0)
		{
			this._proxy.lineDash();
		}
		else
		if(_vargs.narg()==1)
		{
			this._proxy.lineDash((float)_vargs.checkdouble(1), (float)_vargs.checkdouble(1));
		}
		else
		{
			float[] _fargs = new float[_vargs.narg()];
			for (int _i = 0; _i < _fargs.length; _i++)
			{
				_fargs[_i] = (float)_vargs.checkdouble(_i+1);
			}
			this._proxy.lineDash(_fargs);
		}
	}

	public void line_dash_offset(Varargs _vargs)
	{
		if(_vargs.narg()==2)
		{
			this._proxy.lineDashX(_vargs.checkdouble(1), (float)_vargs.checkdouble(2), (float)_vargs.checkdouble(2));
		}
		else
		{
			float[] _fargs = new float[_vargs.narg()-1];
			for (int _i = 0; _i < _fargs.length; _i++)
			{
				_fargs[_i] = (float)_vargs.checkdouble(_i+2);
			}
			this._proxy.lineDashX(_vargs.checkdouble(1), _fargs);
		}
	}

	public void line_cap(Varargs _vargs)
	{
		if(_vargs.narg()==0)
		{
			this._proxy.lineCap(0);
		}
		else
		if(_vargs.narg()==1)
		{
			this._proxy.lineCap(_vargs.checkint(1));
		}
		else
		{
			log.warn("wrong parameters for line_cap - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void line_join(Varargs _vargs)
	{
		if(_vargs.narg()==0)
		{
			this._proxy.lineJoin(0);
		}
		else
		if(_vargs.narg()==1)
		{
			this._proxy.lineJoin(_vargs.checkint(1));
		}
		else
		{
			log.warn("wrong parameters for line_join - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void meter_limit(Varargs _vargs)
	{
		if(_vargs.narg()==0)
		{
			this._proxy.meterlimit(1.0);
		}
		else
		if(_vargs.narg()==1)
		{
			this._proxy.meterlimit(_vargs.checkdouble(1));
		}
		else
		{
			log.warn("wrong parameters for meter_limit - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void flatness(Varargs _vargs)
	{
		if(_vargs.narg()==0)
		{
			this._proxy.flatness(0);
		}
		else
		if(_vargs.narg()==1)
		{
			this._proxy.flatness(_vargs.checkint(1));
		}
		else
		{
			log.warn("wrong parameters for meter_limit - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
		}
	}

	public void end_path(Varargs _vargs)
	{
		this._proxy.endPage();
	}

	public void close_path(Varargs _vargs)
	{
		this._proxy.closePath();
	}

	public LuaValue paragraph(Varargs _vargs)
	{
		if(_vargs.narg()==4)//(String _text, float _lead, float _w, float _h)
		{
			return LuaValue.valueOf(this._proxy.paragraph(_vargs.checkjstring(1), (float)_vargs.checkdouble(2), (float)_vargs.checkdouble(3), (float)_vargs.checkdouble(4)));
		}
		else
		if(_vargs.narg()==6)//(String _text, float _lead, float _w, float _h, bool spill)
		{
			return LuaValue.valueOf(this._proxy.getContent(false).paragraph(_vargs.checkjstring(1), (float)_vargs.checkdouble(2), (float)_vargs.checkdouble(3), (float)_vargs.checkdouble(4), _vargs.checkboolean(5), true));
		}
		else
		if(_vargs.narg()==6)//(String _text, float _lead, float _w, float _h, bool spill, bool justify)
		{
			return LuaValue.valueOf(this._proxy.getContent(false).paragraph(_vargs.checkjstring(1), (float)_vargs.checkdouble(2), (float)_vargs.checkdouble(3), (float)_vargs.checkdouble(4), _vargs.checkboolean(5), _vargs.checkboolean(6)));
		}
		else
		if(_vargs.narg()==7)//(String _text, float _lead, float _w, float _h, bool spill, bool justify, bool wsvshs)
		{
			return LuaValue.valueOf(this._proxy.getContent(false).paragraph(_vargs.checkjstring(1), (float)_vargs.checkdouble(2), (float)_vargs.checkdouble(3), (float)_vargs.checkdouble(4), _vargs.checkboolean(5), _vargs.checkboolean(6), _vargs.checkboolean(7)));
		}
		else
		{
			log.warn("wrong parameters for paragraph - "+LuayStringifierFunction._stringify_vararg(_vargs, true).tojstring());
			return LuaValue.NIL;
		}
	}

	public void part(Varargs _vargs)
	{
		this._proxy.part(_vargs.checkjstring(1));
	}

	public void outline(Varargs _vargs)
	{
		this._proxy.outline(_vargs.checkjstring(1));
	}

	public void chapter(Varargs _vargs)
	{
		this._proxy.chapter(_vargs.checkjstring(1));
	}

	public void section(Varargs _vargs)
	{
		this._proxy.section(_vargs.checkjstring(1));
	}

	public void subsection(Varargs _vargs)
	{
		this._proxy.subsection(_vargs.checkjstring(1));
	}

	public void subsubsection(Varargs _vargs)
	{
		this._proxy.subsubsection(_vargs.checkjstring(1));
	}

	public void reference(Varargs _vargs)
	{
		this._proxy.reference(_vargs.checkjstring(1));
	}
}
