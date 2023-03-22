package com.github.terefang.jmelange.conflux.words;

import com.github.terefang.jmelange.commons.io.CustomStreamTokenizer;
import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.conflux.util.ArcRand;
import com.github.terefang.jmelange.data.DataUtil;
import com.github.terefang.jmelange.pdata.PdataUtil;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

public class Histo
{
	@SneakyThrows
	public static void main(String[] args) {
		File _file = new File("/data/fredo/_PROJECTS/magic-systems.md");
		CustomStreamTokenizer _st = new CustomStreamTokenizer(new FileReader(_file));
		_st.resetSyntax();
		_st.whitespaceChars('\'', '\'');
		_st.whitespaceChars('"', '"');
		_st.whitespaceChars(0, '@');
		_st.whitespaceChars(127, 255);
		_st.wordChars('A', 'Z');
		_st.wordChars('a', 'z');
		Map<String, Integer> _historgram = new LinkedHashMap<>();
		while(_st.nextToken() != CustomStreamTokenizer.TOKEN_TYPE_EOF)
		{
			if(_st.ttype == CustomStreamTokenizer.TOKEN_TYPE_WORD)
			{
				String _t = _st.tokenAsString().toLowerCase();
				if(_t.length()<=1) continue;
				if(_historgram.containsKey(_t))
				{
					_historgram.put(_t , _historgram.get(_t)+1);
				}
				else
				{
					_historgram.put(_t , 1);
				}
			}
		}

		_st = new CustomStreamTokenizer(new InputStreamReader(ClasspathResourceLoader.of("stopwords/en-mysql.txt", null).getInputStream()));
		_st.resetSyntax();
		_st.commentChar('#');
		_st.whitespaceChars('\'', '\'');
		_st.whitespaceChars('"', '"');
		_st.whitespaceChars(0, '@');
		_st.whitespaceChars(127, 255);
		_st.wordChars('A', 'Z');
		_st.wordChars('a', 'z');

		while(_st.nextToken() != CustomStreamTokenizer.TOKEN_TYPE_EOF)
		{
			if(_st.ttype == CustomStreamTokenizer.TOKEN_TYPE_WORD)
			{
				String _t = _st.tokenAsString().toLowerCase();
				_historgram.remove(_t);
			}
		}

		ArcRand _arc = ArcRand.from(1337);
		List<String> _list = new Vector(_historgram.keySet());
		while(_list.size() > 0)
		{
			String _w = _list.remove((int)_arc.next(_list.size()));
			System.err.println(_w+" - "+ _historgram.get(_w));
		}

	}
}
