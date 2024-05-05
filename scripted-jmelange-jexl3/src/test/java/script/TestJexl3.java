package script;

import com.github.terefang.jmelange.script.IScriptContext;
import com.github.terefang.jmelange.script.IScriptContextFactory;

import lombok.SneakyThrows;
import util.TestHelper;

import java.util.Collections;

public class TestJexl3 {

    @SneakyThrows
    public static void main(String[] args) {
        IScriptContextFactory _tf = IScriptContextFactory.findByName("jexl3");
        IScriptContext _t = _tf.newInstance();
        _t.setOutputStream(System.out);
        _t.compile(TestHelper.getStream("test0.jexl"),"test0.jexl");
        Object _ret = _t.run(Collections.singletonMap("_var", 2));
        System.err.println(_ret);
    }
}
