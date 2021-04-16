package script;

import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.JMelangeScriptFactoryLoader;
import lombok.SneakyThrows;

import java.io.File;

public class TestGroovy {

    @SneakyThrows
    public static void main(String[] args) {
        JMelangeScriptFactory _fact = JMelangeScriptFactoryLoader.loadFactoryByName("groovy");
        AbstractScript _scp = _fact.createScript();
        _scp.setOutputStream(System.out);
        _scp.init(new File("examples/scripts/test0.groovy"));
        boolean _ret = _scp.execute();
        System.err.println(_ret);
    }

}
