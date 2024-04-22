import com.github.terefang.jmelange.words.Conflux;
import com.github.terefang.jmelange.random.ArcRand;
import lombok.SneakyThrows;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.util.*;


public class TestTwoWords {

    @SneakyThrows
    public static void main(String[] args) {
        Conflux _cf = new Conflux();
        _cf.setTableSize(3);
        _cf.loadFromString(" Melaxinar Toltekhotep Xenosphobe axar ");
        _cf.load(new File("/u/fredo/IdeaProjects/jmelange/conflux-jmelange/res/human-names-chinese.txt"));
        ArcRand _rng = ArcRand.from(0x1337beef);
        List<String> _names = new Vector();
        try {
            _names = _cf.generateWords(_rng, _names, 5, 20, false);
        }
        catch(Exception _xe)
        {
            System.err.println(_xe);
        }
        Collections.sort(_names);
        for(String _n : _names)
        {
            System.out.println(WordUtils.capitalizeFully(_n));
        }
    }

}
