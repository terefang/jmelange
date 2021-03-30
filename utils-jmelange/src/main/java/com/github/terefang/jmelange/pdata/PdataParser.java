package com.github.terefang.jmelange.pdata;

import com.github.terefang.jmelange.commons.CustomStreamTokenizer;
import lombok.SneakyThrows;
import org.apache.commons.collections4.map.MultiValueMap;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public class PdataParser {

    @SneakyThrows
    public static Map<String, Object> loadFrom(File _file)
    {
        ;
        try (FileReader _fh = new FileReader(_file))
        {
            return loadFrom(_fh);
        }
    }

    @SneakyThrows
    public static Map<String, Object> loadFrom(Reader _file)
    {
        CustomStreamTokenizer _tokener = new CustomStreamTokenizer(_file);

        _tokener.quoteChar('"');
        _tokener.tripleQuotes(true);
        _tokener.slashSlashComments(true);
        _tokener.slashStarComments(true);
        _tokener.commentChar('#');
        _tokener.whitespaceChars(0, 32);
        _tokener.wordChar('_', '-', ':', '@', '\'');
        _tokener.hexLiterals(true);
        _tokener.dateTimeLiterals(true);

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

        return readKeyValuePairs(_tokener);
    }

    @SneakyThrows
    static Map<String, Object> readKeyValuePairs(CustomStreamTokenizer _tokener)
    {
        return readKeyValuePairs(_tokener, null, false);
    }

    @SneakyThrows
    static Map<String, Object> readKeyValuePairs(CustomStreamTokenizer _tokener, String _first, boolean _check)
    {
        Map<String, Integer> _keyi = new HashMap<>();
        Map<String, Object> _ret = new LinkedHashMap<>();
        MultiValueMap<String, String> _dups = new MultiValueMap();

        if(_first!=null)
        {
            _ret.put(_first, readValue(_tokener, _check));
        }
        else if(_check)
        {
            int _assign = _tokener.nextToken();
            if(_assign!='=') throw new IllegalArgumentException(String.format("Token not '=' in line %d", _tokener.lineno()));
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
                    _val = readValue(_tokener, true);

                    //System.out.println("K: "+_key);
                    //System.out.println("V: "+_val);

                    if(_ret.containsKey(_key))
                    {
                        int _i = 1;
                        if(!_keyi.containsKey(_key))
                        {
                            _keyi.put(_key,_i);
                            _dups.put(_key, _key);
                        }
                        else
                        {
                            _i = _keyi.get(_key)+1;
                            _keyi.put(_key,_i);
                        }
                        _dups.put(_key, _key+"+"+_i);
                        //_ret.put("___dup_keys___", _dups);
                        _key+="+"+_i;
                    }
                    _ret.put(_key, _val);
                }
                else
                {
                    throw new IllegalArgumentException(String.format("Illegal Key in line %d", _tokener.lineno()));
                }
            }
        }

        if(_dups.size()>0)
        {
            for(String _base : _dups.keySet())
            {
                List _l = new Vector();
                for(String _key : _dups.getCollection(_base))
                {
                    _l.add(_ret.get(_key));
                    _ret.remove(_key);
                }
                _ret.put(_base, _l);
            }
        }

        return _ret;
    }

    @SneakyThrows
    static Object readValue(CustomStreamTokenizer _tokener, boolean _check)
    {
        //System.err.println("-value-");
        if(_check)
        {
            int _assign = _tokener.nextToken();
            if(_assign!='=') throw new IllegalArgumentException(String.format("Token not '=' in line %d", _tokener.lineno()));
        }
        int _peek = _tokener.nextToken();
        Object _ret = null;
        switch(_peek)
        {
            case '"':
            case CustomStreamTokenizer.TOKEN_TYPE_WORD: return _tokener.tokenAsString();
            case CustomStreamTokenizer.TOKEN_TYPE_CARDINAL: return _tokener.tokenAsCardinal();
            case CustomStreamTokenizer.TOKEN_TYPE_DATETIME: return new Date(_tokener.tokenAsCardinal());
            case CustomStreamTokenizer.TOKEN_TYPE_NUMBER: return _tokener.tokenAsNumber();
            case '[': _tokener.pushBack(); return readList(_tokener, null);
            case '{': _tokener.pushBack(); return readObjectOrList(_tokener);
        }
        return _ret;
    }

    @SneakyThrows
    static List readList(CustomStreamTokenizer _tokener, Object _first)
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
                case '[': _tokener.pushBack(); _ret.add(readList(_tokener, null)); break;
                case '{': _tokener.pushBack(); _ret.add(readObjectOrList(_tokener)); break;
                case '"':
                case CustomStreamTokenizer.TOKEN_TYPE_WORD: _ret.add(_tokener.tokenAsString()); break;
                case CustomStreamTokenizer.TOKEN_TYPE_CARDINAL: _ret.add(_tokener.tokenAsCardinal()); break;
                case CustomStreamTokenizer.TOKEN_TYPE_DATETIME: _ret.add(new Date(_tokener.tokenAsCardinal())); break;
                case CustomStreamTokenizer.TOKEN_TYPE_NUMBER: _ret.add(_tokener.tokenAsNumber()); break;
                default: _ret.add(Character.toString((char) _token)); break;
            }
        }
        return _ret;
    }

    @SneakyThrows
    static Object readObjectOrList(CustomStreamTokenizer _tokener)
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
            Object _first = readValue(_tokener, false);
            return readList(_tokener, _first);
        }

        // case WORD/NUM -> switch
        // case WORD/NUM WORD/NUM -> list
        // case WORD/NUM '=' -> kv
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
            if(_token == '=')
            {
                if(_first_is_number)
                {
                    _first=Long.toString((long)Double.parseDouble(_first));
                }
                return readKeyValuePairs(_tokener, _first, true);
            }
            else
            {
                if(_first_is_cadinal)
                {
                    return readList(_tokener, Long.parseLong(_first));
                }
                else
                if(_first_is_number)
                {
                    return readList(_tokener, Double.parseDouble(_first));
                }
                return readList(_tokener, _first);
            }
        }

        throw new IllegalArgumentException(String.format("Illegal Token in line %d", _tokener.lineno()));
    }

}
