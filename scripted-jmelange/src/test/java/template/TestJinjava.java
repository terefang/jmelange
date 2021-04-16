package template;

import com.github.terefang.jmelange.templating.AbstractTemplateScript;
import com.github.terefang.jmelange.templating.impl.JinjavaTemplateScript;
import lombok.SneakyThrows;

import java.io.File;

public class TestJinjava {

    @SneakyThrows
    public static void main(String[] args) {
        AbstractTemplateScript _scp = new JinjavaTemplateScript();

        _scp.init(new File("examples/templates/test0.j2"));
        _scp.setOutputStream(System.out);
        boolean _ret = _scp.execute();
        System.out.flush();
        System.err.println(_ret);
    }

}
