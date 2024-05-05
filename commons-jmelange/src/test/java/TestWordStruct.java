import com.github.terefang.jmelange.random.ArcRand;
import com.github.terefang.jmelange.words.Structor;
import org.apache.commons.text.WordUtils;

import java.util.*;

public class TestWordStruct
{
	public static void main(String[] args)
	{
		Set<String> _words1 = new HashSet<>();
		Structor _st = Structor.fromClasspath("wordstruct/test.pdata");
		ArcRand _rng = ArcRand.from(0x1337);
		while(_words1.size()<36)
		{
			_words1.add(WordUtils.capitalize(_st.nextWord(_rng, "KROOT")));
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
		Structor _st = Structor.fromClasspath("test.pdata");
		ArcRand _rng = ArcRand.from(0x1337);
		while(_words1.size()<36*3)
		{
			_words1.add(WordUtils.capitalize(_st.nextWord(_rng, "HK-Female-Given")));
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
		Structor _st = Structor.fromClasspath("material-names.pdata");
		ArcRand _rng = ArcRand.from(0x1337);
		for(int _i=0; _i<100; _i++)
		{
			_words.add(_st.nextWord(_rng));
		}
		Collections.sort(_words);
		for (String _w : _words)
		{
			System.out.println(_w);
		}
	}
}
