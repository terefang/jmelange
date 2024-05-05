package template;

import com.github.terefang.jmelange.script.ITemplateContext;
import com.github.terefang.jmelange.script.ITemplateContextFactory;
import lombok.SneakyThrows;
import util.TestHelper;
import java.util.Collections;

public class TestJinjava {

    @SneakyThrows
    public static void main(String[] args)
    {
        ITemplateContextFactory _tf = ITemplateContextFactory.findByName("jinjava");
        ITemplateContext _t = _tf.newInstance();
        _t.compile(TestHelper.getStream("test0.j2"),"test0.j2");
        _t.run(Collections.singletonMap("_var", 2), System.out);
    }

}
