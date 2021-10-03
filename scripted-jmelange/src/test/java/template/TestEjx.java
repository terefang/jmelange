package template;

import com.github.terefang.jmelange.scripted.util.DeTagifier;
import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactory;
import com.github.terefang.jmelange.templating.JMelangeTemplateScriptFactoryLoader;
import lombok.SneakyThrows;
import org.codehaus.plexus.util.IOUtil;

import java.io.*;

public class TestEjx {

    @SneakyThrows
    public static void main(String[] args)
    {
        String _esp_file = "examples/templates/test.ejx";

        JMelangeTemplateScriptFactory _fact = JMelangeTemplateScriptFactoryLoader.loadFactoryByExtension(_esp_file);
        AbstractTemplateScript _scp = _fact.createTemplateScript();
        _scp.setOutputStream(System.out);
        _scp.init(new File(_esp_file));
        boolean _ret = _scp.executeTemplate();
        System.err.println(_ret);
    }
}
