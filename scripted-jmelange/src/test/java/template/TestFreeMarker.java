package template;

import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactory;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactoryLoader;
import lombok.SneakyThrows;

import java.io.File;

public class TestFreeMarker {

    @SneakyThrows
    public static void main(String[] args) {
        JMelangeTemplateScriptFactory _fact = JMelangeTemplateScriptFactoryLoader.loadFactoryByName("freemarker");
        AbstractTemplateScript _scp = _fact.createTemplateScript();
        _scp.setOutputStream(System.out);
        _scp.setOutputType("TEXT");
        _scp.init(new File("examples/templates/test0.txt.fm"));
        boolean _ret = _scp.executeTemplate();
        System.out.flush();
        System.err.println(_ret);
    }

}
