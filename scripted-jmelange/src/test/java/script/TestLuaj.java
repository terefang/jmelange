package script;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.JMelangeScriptFactory;
import com.github.terefang.jmelange.scripted.JMelangeScriptFactoryLoader;
import com.github.terefang.jmelange.scripted.impl.LuajScript;
import lombok.SneakyThrows;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;

public class TestLuaj {

    static String _BASEDIR = "/u/fredo/IdeaProjects/jmelange/scripted-jmelange/src/main/lua/luazdf";
    @SneakyThrows
    public static void main(String[] args)
    {
        DirectoryScanner _ds = new DirectoryScanner();
        _ds.setBasedir(_BASEDIR);
        _ds.setIncludes(new String[] { "*.ex1.lua" } );
        _ds.scan();
        for(String _f : _ds.getIncludedFiles())
        {
            System.err.println(_f);

            AbstractScript _scp = LuajScript.create();
            _scp.setOutputStream(System.out);
            _scp.init(new File(_BASEDIR, _f));
            boolean _ret = _scp.execute();
            System.err.println(_ret);
            Thread.sleep(1000L);
        }
    }

}
