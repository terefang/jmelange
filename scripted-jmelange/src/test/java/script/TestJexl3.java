package script;

import com.github.terefang.jmelange.scripted.impl.Jsr223Script;
import lombok.SneakyThrows;

import java.io.File;

public class TestJexl3 {

    @SneakyThrows
    public static void main(String[] args) {
        Jsr223Script _scp = Jsr223Script.from("jexl3");
        _scp.init(new File("examples/scripts/test0.jexl"));
        boolean _ret = _scp.execute();
        System.err.println(_ret);
    }
}
