package script;


import com.github.terefang.jmelange.script.IScriptContext;
import com.github.terefang.jmelange.script.IScriptContextFactory;
import lombok.SneakyThrows;
import util.TestHelper;

import java.io.File;
import java.util.Collections;

public class TestGroovy {

    @SneakyThrows
    public static void main(String[] args)
    {
        IScriptContextFactory _btf = IScriptContextFactory.findByName("groovy");
        IScriptContext _bt = _btf.newInstance();
        _bt.compile(TestHelper.getStream("test0.groovy"),"test0.groovy");
        Object _ret = _bt.run(Collections.singletonMap("_var", 2));
        System.err.println(_ret);
    }

}
