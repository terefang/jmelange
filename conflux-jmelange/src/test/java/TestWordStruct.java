import com.github.terefang.jmelange.conflux.words.WordStructor;
import org.apache.commons.text.WordUtils;

import java.util.*;

public class TestWordStruct
{
	public static void main(String[] args)
	{
		Set<String> _words1 = new HashSet<>();
		WordStructor _st = WordStructor.fromClasspath("test.pdata");
		_st.init(0x1337);
		while(_words1.size()<36)
		{
			_words1.add(WordUtils.capitalize(_st.nextWord("KROOT")));
		}

		List<String> _words = new Vector<>(_words1);
		Collections.sort(_words);
		for (String _w : _words)
		{
			System.out.println(_w);
		}
	}

	public static void main_3(String[] args)
	{
		Set<String> _words1 = new HashSet<>();
		WordStructor _st = WordStructor.fromClasspath("test.pdata");
		_st.init(0x1337);
		while(_words1.size()<36*3)
		{
			_words1.add(WordUtils.capitalize(_st.nextWord("HK-Female-Given")));
		}

		List<String> _words = new Vector<>(_words1);
		Collections.sort(_words);
		for (String _w : _words)
		{
			System.out.println(_w);
		}
	}

	public static void main_2(String[] args)
	{
		Set<String> _words1 = new HashSet<>();
		WordStructor _st = WordStructor.fromClasspath("test.pdata");
		_st.init(0x1337);
		List<String> _list = _st.accumulateWords("HUMANKIN-Male");
		for(String _w : _list)
		{
			_words1.add(WordUtils.capitalize(_w));
		}

		List<String> _words = new Vector<>(_words1);
		Collections.sort(_words);
		for (String _w : _words)
		{
			System.out.println(_w);
		}
	}

	public static void main_(String[] args)
	{
		List<String> _words = new Vector<>();
		WordStructor _st = WordStructor.fromClasspath("material-names.pdata");
		_st.init(0x1337);
		for(int _i=0; _i<100; _i++)
		{
			_words.add(_st.nextWord());
		}
		Collections.sort(_words);
		for (String _w : _words)
		{
			System.out.println(_w);
		}
	}
}
