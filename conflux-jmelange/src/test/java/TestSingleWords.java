import com.github.terefang.jmelange.conflux.Conflux;
import com.github.terefang.jmelange.conflux.util.ArcRand;
import lombok.SneakyThrows;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.util.*;


public class TestSingleWords {

    @SneakyThrows
    public static void main_(String[] args) {
        Conflux _cf = new Conflux();
        _cf.load(new File("conflux-jmelange/res/necronic-names.txt"), 2);
        ArcRand _rng = ArcRand.from(0x1337beef);
        List<String> _names = new Vector();
        _names = _cf.generateWords(_rng, _names, 5, 500);
        Collections.sort(_names);
        for(String _n : _names)
        {
            System.out.println(WordUtils.capitalizeFully(_n));
        }
    }

    @SneakyThrows
    public static void main_2(String[] args) {
        Conflux _cf = new Conflux();
        _cf.load(new File("conflux-jmelange/res/human-latin-names.txt"), 2);
        ArcRand _rng = ArcRand.from(0x1337beef);
        List<String> _names = new Vector();
        _names = _cf.generateWords(_rng, _names, 5, 12);
        Collections.sort(_names);
        for(String _n : _names)
        {
            System.out.println(WordUtils.capitalizeFully(_n));
        }
    }


    @SneakyThrows
    public static void main(String[] args)
    {
        Set<String> _set = new TreeSet<>();
        ArcRand _rng = ArcRand.from(0x1337beef);
        Conflux _cf = new Conflux();
        _cf.setLoopBreaker(100);
        _cf.setFudge(4);
        _cf.setBreakHarder(true);
        //_cf.setUseDashSpace(true);
        //_cf.load(new File("conflux-jmelange/res/human-latin-names.txt"), 2);
        _cf.load(new File("conflux-jmelange/res/necronic-names.txt"), 2);
        // _cf.printTable();
        int _i = 0;
        while(_set.size()<200)
        {
            _i++;
            switch(_i%4)
            {
                case 0:
                {
                    _set.add(WordUtils.capitalizeFully(_cf.toWid(ArcRand.from(_i), 3, 7, (_i%16!=0))));
                    break;
                }
                case 1:
                {
                    _set.add(WordUtils.capitalizeFully(_cf.toId(ArcRand.from(_i), Integer.toHexString(_i), 7, (_i%8!=0), 0xff)));
                    break;
                }
                case 2:
                {
                    _set.add(WordUtils.capitalizeFully(_cf.generateWord(_i, 7, (_i%4!=0))));
                    break;
                }
                case 3:
                {
                    _set.add(WordUtils.capitalizeFully(_cf.toId(ArcRand.from(_i), Integer.toHexString(_i), 7, (_i%16!=0), 0xff, 10, false)));
                    break;
                }
            }
        }

        for(String _sn : _set)
        {
            System.out.println(_sn);
        }
    }
}
