package com.github.terefang.jmelange.script.as;

import com.github.terefang.jmelange.script.*;

import java.util.List;


public abstract class AbstractAsTemplateFactory extends AbstractTemplateFactory
{
    public abstract IScriptContextFactory getScriptFactory();

    public AbstractTemplate createTemplateScript()
    {
        return new AsTemplateImpl();
    }

    public ITemplateContext newInstance(String _name, String _ext, List<String> _path) {
        AsTemplateImpl _ts = (AsTemplateImpl)this.createTemplateScript();
        IScriptContext _scp = this.getScriptFactory().newInstance(_name, _ext, _path);
        _ts.setScript(_scp);
        return _ts;
    }
}
