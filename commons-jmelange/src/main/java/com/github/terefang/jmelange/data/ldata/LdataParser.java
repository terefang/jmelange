package com.github.terefang.jmelange.data.ldata;

import com.github.terefang.jmelange.commons.io.CustomStreamTokenizer;
import lombok.SneakyThrows;

import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public class LdataParser {

    @SneakyThrows
    public static Map<String, Object> loadFrom(File _file)
    {
        return loadFrom(_file, false, false);
    }
    
    @SneakyThrows
    public static Map<String, Object> loadFrom(File _file, boolean _byteLiterals)
    {
        return loadFrom(_file, _byteLiterals, false);
    }

    @SneakyThrows
    public static Map<String, Object> loadFrom(File _file, boolean _byteLiterals, boolean _dupkeys)
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
        return loadFrom(_file, _byteLiterals, false);
    }
    
    @SneakyThrows
    public static Map<String, Object> loadFrom(Reader _file, boolean _byteLiterals, boolean _dupkeys)
    {
        try {
            CustomStreamTokenizer _tokener = new CustomStreamTokenizer(_file);
            _tokener.resetSyntax();
            _tokener.quoteChar('"');
            _tokener.tripleQuotes(true);
            _tokener.whitespaceChars(0, 32);
            _tokener.whitespaceChar(',', ';',':', '=');
            _tokener.wordChars('a', 'z');
            _tokener.wordChars('A', 'Z');
            _tokener.wordChars('0', '9');
            _tokener.wordChar('_', '-', '@');
            _tokener.parseNumbers();
            _tokener.hexLiterals(true);
            _tokener.byteLiterals(_byteLiterals);
            _tokener.slashSlashComments(true);
            _tokener.slashStarComments(true);
            _tokener.commentChar('#');
            _tokener.commentChar('!');
            _tokener.commentChar('%');
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

            if(_token=='[' || _token=='(')
            {
                return Collections.singletonMap("data", readList(_tokener, _byteLiterals,_dupkeys));
            }

            if(_token=='{')
            {
                _tokener.nextToken();
            }

            return readKeyValuePairs(_tokener, _byteLiterals, _dupkeys);
        }
        finally
        {
            try { _file.close(); } catch(Exception _xe) {}
        }
    }

    @SneakyThrows
    static Map<String, Object> readKeyValuePairs(CustomStreamTokenizer _tokener, boolean _byteLiterals, boolean _dupkeys)
    {
        Map<String, Object> _ret = new LinkedHashMap<>();

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
                switch (_token)
                {
                    case '\'':
                    case '"':
                    case CustomStreamTokenizer.TOKEN_TYPE_WORD:
                    {
                        _key = _tokener.tokenAsString();
                        break;
                    }
                    case CustomStreamTokenizer.TOKEN_TYPE_NUMBER:
                    {
                        _key = Long.toString((long) _tokener.tokenAsNumber());
                        break;
                    }
                    case CustomStreamTokenizer.TOKEN_TYPE_CARDINAL:
                    {
                        _key = Long.toString(_tokener.tokenAsCardinal());
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }

                if(_key!=null)
                {
                    _val = readValue(_tokener, _byteLiterals, _dupkeys);

                   if(_ret.containsKey(_key) && !_dupkeys)
                    {
                        throw new IllegalArgumentException(String.format("duplicate Key in line %d", _tokener.lineno()));
                    }
                    _ret.put(_key, _val);
                }
                else
                {
                    throw new IllegalArgumentException(String.format("Illegal Key in line %d, type = %d", _tokener.lineno(), _token));
                }
            }
        }
        return _ret;
    }

    @SneakyThrows
    static Object readValue(CustomStreamTokenizer _tokener, boolean _byteLiterals, boolean _dupkeys)
    {
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
            case '[': case '(': return readList(_tokener, _byteLiterals, _dupkeys);
            case '{': return readKeyValuePairs(_tokener, _byteLiterals, _dupkeys);
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
    static List readList(CustomStreamTokenizer _tokener,boolean _byteLiterals, boolean _dupkeys)
    {
        List _ret = new Vector();

        int _token;
        while((_token = _tokener.nextToken()) != CustomStreamTokenizer.TOKEN_TYPE_EOF)
        {
            switch (_token)
            {
                case ']': case ')': return _ret;
                case CustomStreamTokenizer.TOKEN_TYPE_BYTES: if(_byteLiterals) {_ret.add(_tokener.tokenAsBytes()); break;}
                case '<': _ret.add(readHereDoc(_tokener)); break;
                case '[': case '(': _ret.add(readList(_tokener, _byteLiterals,_dupkeys)); break;
                case '{': _ret.add(readKeyValuePairs(_tokener, _byteLiterals,_dupkeys)); break;
                case '"':
                case CustomStreamTokenizer.TOKEN_TYPE_WORD: _ret.add(_tokener.tokenAsString()); break;
                case CustomStreamTokenizer.TOKEN_TYPE_CARDINAL: _ret.add(_tokener.tokenAsCardinal()); break;
                case CustomStreamTokenizer.TOKEN_TYPE_NUMBER: _ret.add(_tokener.tokenAsNumber()); break;
                default: throw new IllegalArgumentException(String.format("Illegal Token '%s' in line %d", Character.toString((char) _token), _tokener.lineno()));
            }
        }
        return _ret;
    }
}