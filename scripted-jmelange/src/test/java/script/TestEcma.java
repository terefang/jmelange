package script;

import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.factory.RhinoScriptFactory;
import lombok.SneakyThrows;

import java.io.File;

public class TestEcma {

    @SneakyThrows
    public static void main(String[] args) {
        AbstractScript _scp = new RhinoScriptFactory().createScript();
        _scp.init(new File("examples/scripts/test0.ecma"));
        boolean _ret = _scp.execute();
        System.err.println(_ret);
    }

}
