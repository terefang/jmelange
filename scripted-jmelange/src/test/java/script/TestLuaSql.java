package script;

import com.github.terefang.jmelange.scripted.impl.LuayScript;
import com.github.terefang.jmelange.scripted.impl.luay.LuaSqlLib;
import lombok.SneakyThrows;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;

public class TestLuaSql {

    static String[] _BASEDIR = {
            "/u/fredo/IdeaProjects/jmelange/scripted-jmelange/src/test/luasql", "*.lua"
    };
    @SneakyThrows
    public static void main(String[] args)
    {
        for(int _i =0; _i < _BASEDIR.length; _i+=2)
        {
            DirectoryScanner _ds = new DirectoryScanner();
            _ds.setBasedir(_BASEDIR[_i]);
            _ds.setIncludes(new String[] { _BASEDIR[_i+1] } );
            _ds.scan();

            for(String _f : _ds.getIncludedFiles())
            {
                System.err.println(_f);

                LuayScript _scp = (LuayScript) LuayScript.create();
                _scp.setOutputStream(System.out);
                _scp.addExternalLibrary(new LuaSqlLib());
                _scp.init(new File(_BASEDIR[_i], _f));
                boolean _ret = _scp.execute();
                System.err.println(_ret);
                Thread.sleep(1000L);
            }
        }
    }

}
