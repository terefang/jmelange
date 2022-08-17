package template;

import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.factory.esp.LuajEspTemplateScriptFactory;
import lombok.SneakyThrows;

import java.io.File;

public class TestElua {

    @SneakyThrows
    public static void main(String[] args)
    {
        String _esp_file = "examples/templates/test.elua";

        LuajEspTemplateScriptFactory _fact = new LuajEspTemplateScriptFactory();
        AbstractTemplateScript _scp = _fact.createTemplateScript();
        _scp.setOutputStream(System.out);
        _scp.init(new File(_esp_file));
        boolean _ret = _scp.executeTemplate();
        System.err.println(_ret);
    }
}
