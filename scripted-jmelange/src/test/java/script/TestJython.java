package script;

import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.factory.JythonScriptFactory;
import lombok.SneakyThrows;

import java.io.File;

public class TestJython {

    @SneakyThrows
    public static void main(String[] args) {
        AbstractScript _scp = new JythonScriptFactory().createScript();
        _scp.init(new File("examples/scripts/test0.jy"));
        boolean _ret = _scp.execute();
        System.err.println(_ret);
    }

}
