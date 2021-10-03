package com.github.terefang.jmelange.templating;

import com.github.terefang.jmelange.scripted.AbstractScript;
import lombok.Data;

import java.io.Reader;

@Data
public abstract class AbstractTemplateScript extends AbstractScript
{

    @Override
    public boolean init(Reader _script) {
        return this.initTemplate(_script);
    }

    public abstract boolean initTemplate(Reader _script);

    @Override
    public Object executeObject(boolean _scopeOrBindings) {
        return this.executeTemplate();
    }

    public abstract boolean executeTemplate();
}
