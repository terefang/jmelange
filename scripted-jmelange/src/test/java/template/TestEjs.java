package template;

import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactory;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactoryLoader;
import lombok.SneakyThrows;

import java.io.File;

public class TestEjs {

    @SneakyThrows
    public static void main(String[] args) {
        JMelangeTemplateScriptFactory _fact = JMelangeTemplateScriptFactoryLoader.loadFactoryByName("embeddedrhino");
        AbstractTemplateScript _scp = _fact.createTemplateScript();
        _scp.setOutputStream(System.out);
        _scp.init(new File("examples/templates/test0.ejs"));
        boolean _ret = _scp.executeTemplate();
        System.err.println(_ret);
    }

}
