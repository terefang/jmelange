package script;

import com.github.terefang.jmelange.script.ITemplateContext;
import com.github.terefang.jmelange.script.ITemplateContextFactory;
import lombok.SneakyThrows;
import util.TestHelper;

import java.io.File;
import java.util.Collections;

public class TestTrimou {

    @SneakyThrows
    public static void main(String[] args)
    {
        ITemplateContextFactory _tf = ITemplateContextFactory.findByName("trimou");
        ITemplateContext _t = _tf.newInstance();
        _t.compile(TestHelper.getStream("test0.tri"), "test0.tri");
        Object _ret = _t.run(Collections.singletonMap("_var", 2), System.out);
        System.err.println(_ret);
    }

}
