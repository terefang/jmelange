package template;

import com.github.terefang.jmelange.script.ITemplateContext;
import com.github.terefang.jmelange.script.ITemplateContextFactory;

import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TestFreeMarker {

    @SneakyThrows
    public static void main(String[] args) {
        ITemplateContextFactory _fact = ITemplateContextFactory.findByName("freemarker");
        ITemplateContext _scp = _fact.newInstance();
        _scp.compile(new File("examples/templates/test0.txt.fm"));
        Object _ret = _scp.run(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                System.out.write(b);
            }
        });
        System.out.flush();
        System.err.println(_ret);
    }

}
