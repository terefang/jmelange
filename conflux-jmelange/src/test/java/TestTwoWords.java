import com.github.terefang.jmelange.conflux.Conflux;
import lombok.SneakyThrows;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.util.*;


public class TestTwoWords {

    @SneakyThrows
    public static void main(String[] args) {
        Conflux _cf = new Conflux();
        _cf.setTableSize(2);
        _cf.load(" Melaxinar Toltekhotep Xenosphobe axar ");
        _cf.setSeed(-1);
        List<String> _names = new Vector();
        try {
            _names = _cf.generateWords(_names, 5, 100);
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
