package com.github.terefang.jmelange.scripted.impl;

import com.github.terefang.jmelange.scripted.AbstractScript;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mozilla.javascript.*;
import org.slf4j.Logger;

import java.io.OutputStream;
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


    static int[] _rhino_features = {};
    @Override
    public Object executeObject(boolean _scopeOrBindings)
    {
        ContextFactory.initGlobal(new ContextFactory(){
            @Override
            protected boolean hasFeature(Context cx, int featureIndex)
            {
                switch(featureIndex)
                {
                    case Context.FEATURE_ENABLE_JAVA_MAP_ACCESS: return true;
                    case Context.FEATURE_ENUMERATE_IDS_FIRST: return true;
                    case Context.FEATURE_V8_EXTENSIONS: return true;
                    case Context.FEATURE_ENHANCED_JAVA_ACCESS: return true;
                    case Context.FEATURE_INTEGER_WITHOUT_DECIMAL_PLACE: return true;
                }
                return super.hasFeature(cx, featureIndex);
            }
        });
        Context l_engine = Context.enter();
        try {
            l_engine.setLanguageVersion(Context.VERSION_ES6);
            l_engine.setErrorReporter(this);
            l_engine.setWrapFactory(wrapFactory);
            RhinoScriptScope _scope = new RhinoScriptScope(this);
            Scriptable l_bind = l_engine.initStandardObjects(_scope);
            _scope.defineFunctionProperties(RhinoScriptScope.FUNCTIONS, RhinoScriptScope.class, ScriptableObject.DONTENUM);

            Map<String, Object> _map = this.assembleContext();
            for (Map.Entry<String, Object> e : _map.entrySet()) {
                ScriptableObject.putProperty(l_bind, e.getKey(), Context.javaToJS(e.getValue(), l_bind));
            }

            if(_map.containsKey(SCRIPT_ARGUMENTS_VAR))
            {
                ScriptableObject.putProperty(l_bind, "arguments", Context.javaToJS(_map.get(SCRIPT_ARGUMENTS_VAR), l_bind));
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

    static class RhinoScriptScope extends ScriptableObject
    {
        public static final String[] FUNCTIONS = { "print", "println" };
        RhinoScript _script;
        public RhinoScriptScope(RhinoScript rhinoScript)
        {
            this._script = rhinoScript;
        }

        @Override
        public String getClassName() {
            return "global";
        }

        @SneakyThrows
        public static void print(Context cx, Scriptable thisObj, Object[] args, Function funObj)
        {
            StringBuilder _sb = new StringBuilder();
            for (int i=0; i < args.length; i++) {
                if (i > 0) _sb.append(" ");

                // Convert the arbitrary JavaScript value into a string form.
                String s = Context.toString(args[i]);
                _sb.append(s);
            }

            if(((RhinoScriptScope)thisObj).get(SCRIPT_OUTPUT_STREAM_VAR)!=null)
            {
                ((OutputStream)((RhinoScriptScope)thisObj).get(SCRIPT_OUTPUT_STREAM_VAR)).write(_sb.toString().getBytes());
            }
            else
            if(((RhinoScriptScope)thisObj).get(SCRIPT_LOGGER_VAR)!=null)
            {
                ((Logger)((RhinoScriptScope)thisObj).get(SCRIPT_LOGGER_VAR)).info(_sb.toString());
            }
            else
            {
                System.out.print(_sb.toString());
            }
        }

        @SneakyThrows
        public static void println(Context cx, Scriptable thisObj, Object[] args, Function funObj)
        {
            print(cx, thisObj, args, funObj);
            if(((RhinoScriptScope)thisObj).get(SCRIPT_OUTPUT_STREAM_VAR)!=null)
            {
                ((OutputStream)((RhinoScriptScope)thisObj).get(SCRIPT_OUTPUT_STREAM_VAR)).write(0x0a);
            }
            else
            if(((RhinoScriptScope)thisObj).get(SCRIPT_LOGGER_VAR)==null)
            {
                System.out.println();
            }
        }
    }
}
