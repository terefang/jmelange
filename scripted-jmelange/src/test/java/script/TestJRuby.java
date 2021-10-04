package script;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.factory.JRubyScriptFactory;
import lombok.SneakyThrows;
import org.apache.commons.io.output.WriterOutputStream;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestJRuby {

    @SneakyThrows
    public static void main(String[] args) {
        AbstractScript _scp = new JRubyScriptFactory().createScript();
        _scp.init(new File("examples/scripts/test0.rb"));
        StringWriter _stw = new StringWriter();
        _scp.setOutputStream(new WriterOutputStream(_stw, StandardCharsets.UTF_8));
        _scp.setArgs((List)CommonUtil.toList("1", "zwe", "dre", "4", "five"));
        boolean _ret = _scp.execute();
        System.err.println(_ret);
        System.out.println(_stw.getBuffer().toString());
    }

}
