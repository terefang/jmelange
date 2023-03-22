package com.github.terefang.jmelange.conflux.words;

import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.conflux.util.ArcRand;
import com.github.terefang.jmelange.pdata.PdataUtil;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class WordStructor
{
	Map<String, List<String>> structure;
	int breaker = 20;

	public static WordStructor from(Reader _fh)
	{
		WordStructor _ws = new WordStructor();
		_ws.structure = (Map)PdataUtil.loadFrom(_fh);
		return _ws;
	}

	public static WordStructor from(File _fh)
	{
		WordStructor _ws = new WordStructor();
		_ws.structure = (Map)PdataUtil.loadFrom(_fh);
		return _ws;
	}

	public static WordStructor fromClasspath(String _name)
	{
		WordStructor _ws = new WordStructor();
		_ws.structure = (Map)PdataUtil.loadFrom(new InputStreamReader(ClasspathResourceLoader.of(_name, null).getInputStream()));
		return _ws;
	}

	ArcRand _rng = null;

	public void init(int _seed)
	{
		this._rng = ArcRand.from(_seed);
	}

	public String nextWord()
	{
		return nextWord("DEFAULT");
	}

	public String nextWord(String _entry)
	{
		String _word = "<"+_entry+">";
		int _i = 0;
		while(_word.indexOf("<")>=0)
		{
			_word = resolveWord(_word, this.breaker>_i);
			_i++;
		}
		return _word;
	}

	public String resolveWord(String _word, boolean _nloop)
	{
		int _off = _word.indexOf("<");
		while(_off >= 0)
		{
			String _pre = _word.substring(0,_off);
			int _end = _word.indexOf(">", _off)+1;
			if(_end<0) break;
			String _post = _word.substring(_end);
			String _lookup = _word.substring(_off+1, _end-1);
			_lookup = lookupWord(_lookup, _nloop);
			_word = _pre+_lookup+_post;
			_off += _lookup.length();
			_off = _word.indexOf("<", _off);
			//System.err.println(_word);
		}
		return _word;
	}

	public String lookupWord(String _lookup, boolean _nloop)
	{
		while(_nloop && _lookup.startsWith("*"))
		{
			_lookup = _lookup.substring(1);
		}

		if(this.structure.containsKey(_lookup))
		{
			int _point = this.structure.get(_lookup).size();
			_point = this._rng.next16() % _point;
			return this.structure.get(_lookup).get(_point);
		}
		System.err.println("MISSING -- "+_lookup);
		return "*MISSING*";
	}


	public List<String> accumulateWords()
	{
		return accumulateWords("DEFAULT");
	}

	public List<String> accumulateWords(String _entry)
	{
		List<String> _ret = new Vector<>();
		Deque<String> _stack = new ArrayDeque<>();
		_stack.add("<"+_entry+">");

		while(_stack.size()>0)
		{
			String _word = _stack.pop();
			if(_word.indexOf("<")<0)
			{
				System.err.println(_word+" / "+_ret.size());
				_ret.add(_word);
			}
			else
			{
				_stack.addAll(accumulateResolveWord(_word, true));
			}
		}
		return _ret;
	}

	public List<String> accumulateResolveWord(String _word, boolean _nloop)
	{
		List<String> _ret = new Vector<>();
		int _off = 0;
		while(( _off = _word.indexOf("<", _off) )>= 0)
		{
			String _pre = _word.substring(0,_off);
			int _end = _word.indexOf(">", _off)+1;
			if(_end<0) break;
			String _post = _word.substring(_end);
			String _lookup = _word.substring(_off+1, _end-1);
			for(String _l : accumulateLookupWord(_lookup, _nloop))
			{
				_ret.add(_pre+_l+_post);
			}
			_off += _lookup.length();
		}
		return _ret;
	}

	public List<String> accumulateLookupWord(String _lookup, boolean _nloop)
	{
		while(_nloop && _lookup.startsWith("*"))
		{
			_lookup = _lookup.substring(1);
		}

		if(this.structure.containsKey(_lookup))
		{
			return this.structure.get(_lookup);
		}
		System.err.println("MISSING -- "+_lookup);
		return Collections.singletonList("*MISSING*");
	}



}
