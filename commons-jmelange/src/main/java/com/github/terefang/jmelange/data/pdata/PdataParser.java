package com.github.terefang.jmelange.data.pdata;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.io.CustomStreamTokenizer;
import lombok.SneakyThrows;
import com.github.terefang.jmelange.plexus.util.IOUtil;

import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public class PdataParser {

	@SneakyThrows
	public static Map<String, Object> loadFrom(File _file)
	{
		return loadFrom(_file, false);
	}

	@SneakyThrows
	public static Map<String, Object> loadFrom(File _file, boolean _byteLiterals)
	{
		try (FileReader _fh = new FileReader(_file))
		{
			return loadFrom(_fh, _byteLiterals);
		}
	}

	@SneakyThrows
	public static Map<String, Object> loadFrom(Reader _file)
	{
		return loadFrom(_file, false);
	}

	@SneakyThrows
	public static Map<String, Object> loadFrom(Reader _file, boolean _byteLiterals)
	{
		try {
			CustomStreamTokenizer _tokener = new CustomStreamTokenizer(_file);
			_tokener.resetSyntax();
			_tokener.quoteChar('"');
			_tokener.tripleQuotes(true);
			_tokener.slashSlashComments(true);
			_tokener.slashStarComments(true);
			_tokener.commentChar('#');
			_tokener.whitespaceChars(0, 32);
			_tokener.whitespaceChar(',', ';');
			_tokener.wordChars('a', 'z');
			_tokener.wordChars('A', 'Z');
			_tokener.wordChars('0', '9');
			_tokener.wordChar('_', '-', '@');
			_tokener.parseNumbers();
			_tokener.hexLiterals(true);
			_tokener.byteLiterals(_byteLiterals);
			_tokener.autoUnicodeMode(true);

			int _token = _tokener.nextToken();
			// check for unicode marker !
			if(_token==CustomStreamTokenizer.TOKEN_TYPE_WORD && _tokener.tokenAsString().charAt(0)==0xfeff)
			{
				//ignore
			}
			else
			{
				_tokener.pushBack();
			}

			if(_token=='[')
			{
				return CommonUtil.toMap("data", readList(_tokener, _byteLiterals));
			}

			if(_token=='{')
			{
				_tokener.nextToken();
			}

			return readKeyValuePairs(_tokener, _byteLiterals);
		}
		finally
		{
			IOUtil.close(_file);
		}
	}

	@SneakyThrows
	static Map<String, Object> readKeyValuePairs(CustomStreamTokenizer _tokener, boolean _byteLiterals)
	{
		return readKeyValuePairs(_tokener, null, false, _byteLiterals);
	}

	@SneakyThrows
	static Map<String, Object> readKeyValuePairs(CustomStreamTokenizer _tokener, String _first, boolean _check, boolean _byteLiterals)
	{
		Map<String, Object> _ret = new LinkedHashMap<>();

		if(_first!=null)
		{
			_ret.put(_first, readValue(_tokener, _check, _byteLiterals));
		}
		else if(_check)
		{
			int _assign = _tokener.nextToken();
			if((_assign!='=') && (_assign!=':'))
			{
				throw new IllegalArgumentException(String.format("Token not '=' or ':' in line %d", _tokener.lineno()));
			}
		}

		int _token;
		while((_token = _tokener.nextToken()) != CustomStreamTokenizer.TOKEN_TYPE_EOF)
		{
			//System.out.println(String.format("T=%s n=%f t=%d s=%s", tokenToString(_token), _tokener.nval, _tokener.ttype, _tokener.sval));
			String _key = null;
			Object _val;
			if(_token == '}')
			{
				break;
			}
			else
			{
				if(_token == CustomStreamTokenizer.TOKEN_TYPE_WORD || _token == '"')
				{
					_key = _tokener.tokenAsString();
				}
				else
				if(_token == CustomStreamTokenizer.TOKEN_TYPE_NUMBER)
				{
					_key = Long.toString((long) _tokener.tokenAsNumber());
				}
				else
				if(_token == CustomStreamTokenizer.TOKEN_TYPE_CARDINAL)
				{
					_key = Long.toString(_tokener.tokenAsCardinal());
				}

				if(_key!=null)
				{
					_ret.put(_key, readValue(_tokener, true, _byteLiterals));
				}
				else
				{
					throw new IllegalArgumentException(String.format("Illegal Key in line %d", _tokener.lineno()));
				}
			}
		}

		return _ret;
	}

	@SneakyThrows
	static Object readValue(CustomStreamTokenizer _tokener, boolean _check, boolean _byteLiterals)
	{
		//System.err.println("-value-");
		if(_check)
		{
			int _assign = _tokener.nextToken();
			if(_assign!='=' && _assign!=':') {
				throw new IllegalArgumentException(String.format("Token not '=' or ':' in line %d", _tokener.lineno()));
			}
		}
		int _peek = _tokener.nextToken();
		Object _ret = null;
		switch(_peek)
		{
			case CustomStreamTokenizer.TOKEN_TYPE_BYTES: if(_byteLiterals) { return _tokener.tokenAsBytes(); }
			case '<': return readHereDoc(_tokener);
			case '"':
			case CustomStreamTokenizer.TOKEN_TYPE_WORD: return _tokener.tokenAsString();
			case CustomStreamTokenizer.TOKEN_TYPE_CARDINAL: return _tokener.tokenAsCardinal();
			case CustomStreamTokenizer.TOKEN_TYPE_NUMBER: return _tokener.tokenAsNumber();
			case '[': _tokener.pushBack(); return readList(_tokener, null, _byteLiterals);
			case '{': _tokener.pushBack(); return readObjectOrList(_tokener, _byteLiterals);
		}
		return _ret;
	}

	@SneakyThrows
	static String readHereDoc(CustomStreamTokenizer _tokener)
	{
		StringBuilder _sb = new StringBuilder();
		int _lineno = _tokener.lineno();
		try {
			_tokener.readHereDocument("\n>>>\n", _sb);
		}
		catch(EOFException _eof)
		{
			throw new IllegalArgumentException(String.format("unfinished here document starting on line %d", _lineno));
		}

		return _sb.toString().substring(_sb.toString().indexOf('\n')+1);
	}


	@SneakyThrows
	static List readList(CustomStreamTokenizer _tokener, boolean _byteLiterals)
	{
		return readList(_tokener, null, _byteLiterals);
	}

	@SneakyThrows
	static List readList(CustomStreamTokenizer _tokener, Object _first, boolean _byteLiterals)
	{
		List _ret = new Vector();

		if(_first==null)
		{
			int _assign = _tokener.nextToken();
			if(_assign!='[' && _assign!='{') throw new IllegalArgumentException(String.format("Token not '[' or '{' in line %d", _tokener.lineno()));
		}
		else
		{
			_ret.add(_first);
		}

		int _token;
		while((_token = _tokener.nextToken()) != CustomStreamTokenizer.TOKEN_TYPE_EOF)
		{
			switch (_token)
			{
				case ']':
				case '}': return _ret;
				case CustomStreamTokenizer.TOKEN_TYPE_BYTES: if(_byteLiterals) {_ret.add(_tokener.tokenAsBytes()); break;}
				case '<': _ret.add(readHereDoc(_tokener)); break;
				case '[': _tokener.pushBack(); _ret.add(readList(_tokener, null, _byteLiterals)); break;
				case '{': _tokener.pushBack(); _ret.add(readObjectOrList(_tokener, _byteLiterals)); break;
				case '"':
				case CustomStreamTokenizer.TOKEN_TYPE_WORD: _ret.add(_tokener.tokenAsString()); break;
				case CustomStreamTokenizer.TOKEN_TYPE_CARDINAL: _ret.add(_tokener.tokenAsCardinal()); break;
				case CustomStreamTokenizer.TOKEN_TYPE_NUMBER: _ret.add(_tokener.tokenAsNumber()); break;
				default: throw new IllegalArgumentException(String.format("Illegal Token '%s' in line %d", Character.toString((char) _token), _tokener.lineno()));
			}
		}
		return _ret;
	}

	@SneakyThrows
	static Object readObjectOrList(CustomStreamTokenizer _tokener, boolean _byteLiterals)
	{
		int _assign = _tokener.nextToken();
		if(_assign!='{') throw new IllegalArgumentException(String.format("Token not '{' in line %d", _tokener.lineno()));

		int _token;
		// we need to peek 2 tokens worth deep
		_token = _tokener.nextToken();
		if(_token=='}') return null;
		if(_token==']') return null;

		// case '[' -> list
		// case '{' -> list
		if(_token=='{' || _token=='[')
		{
			_tokener.pushBack();
			Object _first = readValue(_tokener, false, _byteLiterals);
			return readList(_tokener, _first, _byteLiterals);
		}

		// case WORD/NUM -> switch
		// case WORD/NUM WORD/NUM -> list
		// case WORD/NUM '='/':' -> kv
		if(_token=='"'
				|| _token == CustomStreamTokenizer.TOKEN_TYPE_WORD
				|| _token == CustomStreamTokenizer.TOKEN_TYPE_NUMBER
				|| _token == CustomStreamTokenizer.TOKEN_TYPE_CARDINAL)
		{
			String _first = _tokener.tokenAsString();
			boolean _first_is_number=false;
			boolean _first_is_cadinal=false;
			if(_token == CustomStreamTokenizer.TOKEN_TYPE_NUMBER)
			{
				_first = ""+_tokener.tokenAsNumber();
				_first_is_number = true;
			}
			else
			if(_token == CustomStreamTokenizer.TOKEN_TYPE_CARDINAL)
			{
				_first = ""+_tokener.tokenAsCardinal();
				_first_is_cadinal = true;
			}
			_token = _tokener.nextToken();
			_tokener.pushBack();
			if((_token == '=') || (_token == ':'))
			{
				if(_first_is_number)
				{
					_first=Long.toString((long)Double.parseDouble(_first));
				}
				return readKeyValuePairs(_tokener, _first, true, _byteLiterals);
			}
			else
			{
				if(_first_is_cadinal)
				{
					return readList(_tokener, Long.parseLong(_first), _byteLiterals);
				}
				else
				if(_first_is_number)
				{
					return readList(_tokener, Double.parseDouble(_first), _byteLiterals);
				}
				return readList(_tokener, _first, _byteLiterals);
			}
		}

		throw new IllegalArgumentException(String.format("Illegal Token (0x%x) in line %d", _token, _tokener.lineno()));
	}

}
