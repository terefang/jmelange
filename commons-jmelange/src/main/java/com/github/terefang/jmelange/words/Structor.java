package com.github.terefang.jmelange.words;

import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.commons.util.LdataUtil;
import com.github.terefang.jmelange.random.ArcRand;
import com.github.terefang.jmelange.apache.lang3.text.WordUtils;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class Structor
{
	public interface ExprEvalHandler
	{
		public String eval(ArcRand _rng, String _expr, boolean _nloop, Structor _that);
	}

	int _breaker = 20;

	Map<String, List<String>> _structure;

	ExprEvalHandler evaluator;

	public ExprEvalHandler getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(ExprEvalHandler evaluator) {
		this.evaluator = evaluator;
	}

	public Map<String, List<String>> get_structure() {
		return _structure;
	}

	private boolean _pwr_option;

	public static Structor from(Reader _fh) { return from(_fh, false);}

	public static Structor from(Reader _fh, boolean _pwr) {
		Structor _ws = new Structor().extractFrom(_fh);
		_ws._pwr_option = _pwr;
		return _ws;
	}

	public static Structor from(File _fh) { return from(_fh, false);}

	public static Structor from(File _fh, boolean _pwr) {
		Structor _ws = new Structor().extractFrom(_fh);
		_ws._pwr_option = _pwr;
		return _ws;
	}

	public static Structor fromClasspath(String _name) { return fromClasspath(_name, false);}

	public static Structor fromClasspath(String _name, boolean _pwr) {
		Structor _ws = new Structor();
		_ws._structure = (Map) LdataUtil.loadFrom(new InputStreamReader(ClasspathResourceLoader.of(_name, null).getInputStream()));
		postprocessStructureForReference(_ws._structure);
		return _ws;
	}

	public static Structor create() { return create(false); }

	public static Structor create(boolean _pwr) { return new Structor().set_pwr(_pwr); }

	public boolean is_pwr() {
		return _pwr_option;
	}

	public Structor extractFrom(Reader _fh) {
		_structure = (Map) LdataUtil.loadFrom(_fh);
		postprocessStructureForReference(_structure);
		return this;
	}

	public Structor extractFrom(File _fh) {
		_structure = (Map) LdataUtil.loadFrom(_fh);
		postprocessStructureForReference(_structure);
		return this;
	}

	public Structor set_pwr(boolean _pwr_option) {
		this._pwr_option = _pwr_option;
		return this;
	}

	static void postprocessStructureForReference(Map<String, List<String>> _structure) {
		for(String _k : _structure.keySet()) {
			List<String> _values = _structure.get(_k);

			for(int _i = 0; _i < _values.size(); _i++) {
				String _v = _values.get(_i);
				if(_v.startsWith("<~") && _v.endsWith(">")) {
					String _lu = _v.substring(2, _v.length()-1);
					_values.remove(_i);
					_values.addAll(_i, _structure.get(_lu));
					_i--;
				}
			}
		}
	}

	public String nextWord(ArcRand _rng) {
		return nextWord(_rng, "DEFAULT");
	}

	public String nextWord(ArcRand _rng, String _entry) {
		String _word = "<"+_entry+">";
		int _i = 0;
		while(_word.indexOf("<")>=0) {
			_word = resolveWord(_rng, _word, this._breaker>_i);
			_i++;
		}
		return _word;
	}

	public String nextWordExpr(ArcRand _rng, String _entry) {
		String _word = lookupWord(_rng, _entry, true);
		return resolveExprWord(_rng, _word, false);
	}

	public String resolveExprWord(ArcRand _rng, String _word, boolean _nloop)
	{
		if(this.evaluator!=null)
		{
			_word = this.evaluator.eval(_rng,_word,_nloop, this);
		}
		return resolveWord(_rng,_word,_nloop);
	}

	public String resolveWord(ArcRand _rng, String _word, boolean _nloop)
	{
		int _off = _word.indexOf("<");
		while(_off >= 0) {
			String _pre = _word.substring(0,_off);
			int _end = _word.indexOf(">", _off)+1;
			if(_end<1) break;
			String _post = _word.substring(_end);
			String _lookup = _word.substring(_off+1, _end-1);

			_lookup = lookupWord(_rng, _lookup, _nloop);

			_word = _pre+_lookup+_post;
			_off = _word.indexOf("<");
		}
		return _word;
	}

	public String lookupWord(ArcRand _rng, String _lookup, boolean _nloop)
	{
		while(_nloop && _lookup.startsWith("*"))
		{
			_lookup = _lookup.substring(1);
		}

		boolean _to_upper = false;
		boolean _to_lower = false;
		boolean _to_capitalize = false;

		if(_lookup.startsWith("+"))
		{
			_lookup = _lookup.substring(1);
			_to_upper = true;
		}
		else if(_lookup.startsWith("-"))
		{
			_lookup = _lookup.substring(1);
			_to_lower = true;
		}
		else if(_lookup.startsWith("!"))
		{
			_lookup = _lookup.substring(1);
			_to_capitalize = true;
		}

		if(this._structure.containsKey(_lookup))
		{
			int _point = this._structure.get(_lookup).size();
			if(this._pwr_option)
			{
				int _pct = 100/(_point+1);
				if(_pct<5) _pct=5;
				_point = _rng.powerLaw(_point, _pct);
			}
			else
			{
				_point = _rng.nextInt(_point);
			}
			_lookup = this._structure.get(_lookup).get(_point);
			_lookup = resolveWord(_rng, _lookup, _nloop);

			if(_to_upper) return _lookup.toUpperCase();
			if(_to_lower) return _lookup.toLowerCase();
			if(_to_capitalize) return WordUtils.capitalize(_lookup,' ', '-', '\'');
			return _lookup;
		}
		return "*MISSING="+_lookup+"*";
	}
	
	
	public List<String> processReplacer(List<String> _list)
	{
		if(this._structure.containsKey("$replace"))
		{
			for(int _i = 0; _i<_list.size(); _i++)
			{
				String _text = _list.get(_i);
				StringBuilder _sb = new StringBuilder(_text);
				for(String _key : this._structure.get("$replace"))
				{
					String _target = _key;
					int _off = _target.indexOf(':');
					if(_off>=0)
					{
						_target = _target.substring(_off+1);
					}
					
					for(String _match : this._structure.get(_key))
					{
						if(_match.startsWith("~"))
						{
							_text = _sb.toString().replaceAll(_match.substring(1), _target);
							_sb.setLength(0);
							_sb.append(_text);
						}
						else
						{
							_off = 0;
							while((_off = _sb.indexOf(_match,_off)) >=0)
							{
								_sb.replace(_off, _off+_match.length(),_target);
								_off+=_target.length();
							}
						}
					}
				}
				_list.set(_i, _sb.toString());
			}
		}
		return _list;
	}
	
	public List<String> processReplacer2(String _entry, List<String> _list)
	{
		if(this._structure.containsKey(_entry))
		{
			for(int _i = 0; _i<_list.size(); _i++)
			{
				String _text = _list.get(_i);
				StringBuilder _sb = new StringBuilder(_text);
				for(String _key : this._structure.get(_entry))
				{
					List<String> _mdef = this._structure.get(_key);
					
					String _target = _mdef.get(0);
					
					for(int _x = 1; _x<_mdef.size(); _x++)
					{
						String _match = _mdef.get(_x);
						if(_match.startsWith("~"))
						{
							_text = _sb.toString().replaceAll(_match.substring(1), _target);
							_sb.setLength(0);
							_sb.append(_text);
						}
						else
						{
							int _off = 0;
							while((_off = _sb.indexOf(_match,_off)) >=0)
							{
								_sb.replace(_off, _off+_match.length(),_target);
								_off+=_target.length();
							}
						}
					}
				}
				_list.set(_i, _sb.toString());
			}
		}
		return _list;
	}
	
}
