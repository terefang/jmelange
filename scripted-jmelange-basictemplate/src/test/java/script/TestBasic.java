package script;

import com.github.terefang.jmelange.script.ITemplateContext;
import com.github.terefang.jmelange.script.ITemplateContextFactory;
import util.TestHelper;

import java.util.Collections;

public class TestBasic {
    public static void main(String[] args) {
        ITemplateContextFactory _btf = ITemplateContextFactory.findByName("basictemplate");
        ITemplateContext _bt = _btf.newInstance();
        _bt.compile(TestHelper.getStream("test.bt"),"test.bt");
        _bt.run(Collections.singletonMap("_var", 2), System.out);
    }
}
