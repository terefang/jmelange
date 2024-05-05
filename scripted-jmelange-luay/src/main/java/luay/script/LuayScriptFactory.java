package luay.script;

import com.github.terefang.jmelange.script.AbstractScriptFactory;
import com.github.terefang.jmelange.script.IScriptContext;
import com.github.terefang.jmelange.script.IScriptContextFactory;

import java.util.Arrays;
import java.util.List;

public class LuayScriptFactory extends AbstractScriptFactory implements IScriptContextFactory
{
    @Override
    public IScriptContext newInstance(String _name, String _ext, List<String> _path) {
        return LuayScriptContext.create(_path);
    }

    @Override
    public String getName() {
        return "luay";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("lua", "luay");
    }

    @Override
    public String getExt() {
        return ".lua";
    }

    @Override
    public List<String> getExts() {
        return Arrays.asList(".lua",".luay");
    }

}
