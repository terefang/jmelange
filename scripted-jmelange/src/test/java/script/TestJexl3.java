package script;

import com.github.terefang.jmelange.scripted.impl.Jexl3Script;
import lombok.SneakyThrows;

import java.io.File;

public class TestJexl3 {

    @SneakyThrows
    public static void main(String[] args) {
        Jexl3Script _scp = (Jexl3Script) Jexl3Script.create();
        _scp.init(new File("examples/scripts/test0.jexl"));
        boolean _ret = _scp.execute();
        System.err.println(_ret);
    }
}
