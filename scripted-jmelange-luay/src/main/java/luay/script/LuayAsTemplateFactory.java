package luay.script;


import com.github.terefang.jmelange.script.*;
import com.github.terefang.jmelange.script.as.AbstractAsTemplateFactory;

import java.util.Arrays;
import java.util.List;

public class LuayAsTemplateFactory extends AbstractAsTemplateFactory implements ITemplateContextFactory
{
    @Override
    public IScriptContextFactory getScriptFactory() {
        return IScriptContextFactory.findByName("luay");
    }

    @Override
    public String getName() {
        return "lua";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("lua");
    }

    @Override
    public String getExt() {
        return ".lua";
    }

    @Override
    public List<String> getExts() {
        return Arrays.asList(".lua");
    }

}
