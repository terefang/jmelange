package script;

import com.github.terefang.jmelange.script.IScriptContext;
import com.github.terefang.jmelange.script.IScriptContextFactory;
import lombok.SneakyThrows;
import util.TestHelper;

import java.util.Collections;

public class TestEcma {

    @SneakyThrows
    public static void main(String[] args) {
        IScriptContextFactory _tf = IScriptContextFactory.findByName("rhino");
        IScriptContext _t = _tf.newInstance();
        _t.compile(TestHelper.getStream("test0.ecma"), "test0.ecma");
        Object _ret = _t.run(Collections.singletonMap("_var", 2));
        System.err.println(_ret);
    }

}
