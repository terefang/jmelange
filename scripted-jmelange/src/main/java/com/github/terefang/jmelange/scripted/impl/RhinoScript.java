package com.github.terefang.jmelange.scripted.impl;

import com.github.terefang.jmelange.scripted.AbstractScript;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mozilla.javascript.*;

import java.io.Reader;
import java.text.MessageFormat;
import java.util.Map;

@Slf4j
public class RhinoScript extends AbstractScript implements ErrorReporter
{
    private Script scriptCode;

    static PrimitiveWrapFactory wrapFactory = new PrimitiveWrapFactory();

    @Override
    @SneakyThrows
    public boolean init(Reader _script) {
        Context l_engine = Context.enter();
        try {
            l_engine.setLanguageVersion(Context.VERSION_ES6);
            l_engine.setErrorReporter(this);
            l_engine.setWrapFactory(wrapFactory);
            this.scriptCode = l_engine.compileReader(_script, this.getScriptFile() == null ? "null" : this.getScriptFile().getAbsolutePath(), 1, null);
        } catch (Exception xe) {
            log.warn("error in file", xe);
        } finally {
            Context.exit();
        }

        return (this.scriptCode != null);
    }

    @Override
    public Object executeObject(boolean _scopeOrBindings)
    {
        Context l_engine = Context.enter();
        try {
            l_engine.setLanguageVersion(Context.VERSION_ES6);
            l_engine.setErrorReporter(this);
            l_engine.setWrapFactory(wrapFactory);
            Scriptable l_bind = l_engine.initStandardObjects();
            for (Map.Entry<String, Object> e : this.assembleContext().entrySet()) {
                ScriptableObject.putProperty(l_bind, e.getKey(), Context.javaToJS(e.getValue(), l_bind));
            }

            Object _ret = this.scriptCode.exec(l_engine, l_bind);

            if (_ret instanceof NativeJavaObject) {
                _ret = ((NativeJavaObject) _ret).unwrap();
            }
            return _ret;
        } catch (Exception xe) {
            log.warn("error in file", xe);
            return null;
        } finally {
            Context.exit();
        }
    }

    @Override
    public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
        this.log.warn(MessageFormat.format("L{0}:C{1} F={2} : {3} : {4}", line, lineOffset, sourceName, message, lineSource));
    }

    @Override
    public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
        this.log.error(MessageFormat.format("L{0}:C{1} F={2} : {3} : {4}", line, lineOffset, sourceName, message, lineSource));
    }

    @Override
    public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
        return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
    }

    static class PrimitiveWrapFactory extends WrapFactory {
        public Object wrap(Context cx, Scriptable scope, Object obj, Class staticType)
        {
            if (obj instanceof String || obj instanceof Number || obj instanceof Boolean)
            {
                return obj;
            }
            else if (obj instanceof Character)
            {
                char[] a = {((Character) obj).charValue()};
                return new String(a);
            }
            return super.wrap(cx, scope, obj, staticType);
        }
    }
}
