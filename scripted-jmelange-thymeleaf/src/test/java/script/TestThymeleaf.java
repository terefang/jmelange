package script;

import com.github.terefang.jmelange.script.ITemplateContext;
import com.github.terefang.jmelange.script.ITemplateContextFactory;
import lombok.SneakyThrows;
import util.TestHelper;
import java.util.Collections;

public class TestThymeleaf {

    @SneakyThrows
    public static void main(String[] args) {
        ITemplateContextFactory _tf = ITemplateContextFactory.findByName("thymeleaf");
        ITemplateContext _t = _tf.newInstance();
        _t.compile(TestHelper.getStream("test0.txt.tl"), "test0.txt.tl");
        Object _ret = _t.run(Collections.singletonMap("_var", 2), System.out);
        System.err.println(_ret);
    }

}
