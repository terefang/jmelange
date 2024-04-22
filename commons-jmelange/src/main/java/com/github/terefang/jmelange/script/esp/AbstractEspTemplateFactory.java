package com.github.terefang.jmelange.script.esp;

import com.github.terefang.jmelange.script.*;

import java.util.List;

public abstract class AbstractEspTemplateFactory extends AbstractTemplateFactory
{
    public abstract IScriptContextFactory getScriptFactory();

    public AbstractTemplate createTemplateScript()
    {
        return new EspTemplateImpl();
    }

    public ITemplateContext newInstance(String _name, String _ext, List<String> _path) {
        EspTemplateImpl _ets = (EspTemplateImpl)this.createTemplateScript();
        IScriptContext _scp = this.getScriptFactory().newInstance(_name, _ext,_path);
        _ets.setScript(_scp);
        return _ets;
    }
}
