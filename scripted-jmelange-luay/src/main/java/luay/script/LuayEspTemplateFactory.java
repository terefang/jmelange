package luay.script;


import com.github.terefang.jmelange.script.*;
import com.github.terefang.jmelange.script.esp.AbstractEspTemplateFactory;
import com.github.terefang.jmelange.script.esp.EspDeTagifier;
import com.github.terefang.jmelange.script.esp.EspTemplateImpl;

import java.util.Arrays;
import java.util.List;

public class LuayEspTemplateFactory extends AbstractEspTemplateFactory implements ITemplateContextFactory
{
    @Override
    public IScriptContextFactory getScriptFactory() {
        return IScriptContextFactory.findByName("luay");
    }

    @Override
    public AbstractTemplate createTemplateScript()
    {
        EspTemplateImpl _ets = (EspTemplateImpl)super.createTemplateScript();
        _ets.setVp(BasicVariableProvider.create());
        EspDeTagifier _dt = _ets.getDeTagifier();
        _dt.setParseStart("-- /* START */ -- \n");
        _dt.setParseEnd("\n-- /* END */ -- \n");
        _dt.setOutputStart(" printnl(\"");
        _dt.setOutputEnd("\");\n");
        _dt.setExprStart(" printnl(");
        _dt.setExprEnd(");\n");
        _dt.setCommentStart(" -- ");
        _dt.setCommentEnd(" -- \n");
        _dt.setEscapeStyle(EspDeTagifier.ESCAPE_LUAJ_UNICODE);
        return _ets;
    }

    @Override
    public String getName() {
        return "lua-esp";
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList("elua", "lua-esp");
    }

    @Override
    public String getExt() {
        return ".elua";
    }

    @Override
    public List<String> getExts() {
        return Arrays.asList(".lua.esp", ".elua");
    }

}
