package template;

import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactory;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactoryLoader;
import lombok.SneakyThrows;

import java.io.File;

public class TestErb {

    @SneakyThrows
    public static void main(String[] args)
    {
        String _esp_file = "examples/templates/test.erb";

        JMelangeTemplateScriptFactory _fact = JMelangeTemplateScriptFactoryLoader.loadFactoryByExtension(_esp_file);
        AbstractTemplateScript _scp = _fact.createTemplateScript();
        _scp.setOutputStream(System.out);
        _scp.init(new File(_esp_file));
        boolean _ret = _scp.executeTemplate();
        System.err.println(_ret);
    }
}
