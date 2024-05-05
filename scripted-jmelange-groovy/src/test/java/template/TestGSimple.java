package template;

import com.github.terefang.jmelange.script.ITemplateContext;
import com.github.terefang.jmelange.script.ITemplateContextFactory;

import lombok.SneakyThrows;
import util.TestHelper;

import java.util.Collections;

public class TestGSimple {

    @SneakyThrows
    public static void main(String[] args) 
    {
        ITemplateContextFactory _btf = ITemplateContextFactory.findByName("gsimple");
        ITemplateContext _bt = _btf.newInstance();
        _bt.compile(TestHelper.getStream("test0.gst"),"test0.gst");
        Object _ret = _bt.run(Collections.singletonMap("_var", 2), System.out);
        System.err.println(_ret);
    }

}
