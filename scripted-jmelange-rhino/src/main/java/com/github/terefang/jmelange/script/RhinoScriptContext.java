package com.github.terefang.jmelange.script;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.IOUtil;
import org.mozilla.javascript.*;
import org.slf4j.Logger;


import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Slf4j
public class RhinoScriptContext extends AbstractScript implements IScriptContext, ErrorReporter
{
    private Script scriptCode;

    static PrimitiveWrapFactory wrapFactory = new PrimitiveWrapFactory();

    private List<String> includes;
    OutputStream outputStream;
    @Override
    public void setOutputStream(OutputStream _out) {
        this.outputStream = _out;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public static IScriptContext create(List<String> _path)
    {
        RhinoScriptContext _p = new RhinoScriptContext();
        _p.includes = _path;
       return _p;
    }

    @Override
    @SneakyThrows
    public void compile(Reader _reader, String _name)
    {
        Context l_engine = Context.enter();
        try {
            l_engine.setLanguageVersion(Context.VERSION_ES6);
            l_engine.setErrorReporter(this);
            l_engine.setWrapFactory(wrapFactory);
            this.scriptCode = l_engine.compileReader(_reader, _name, 1, null);
        } catch (Exception xe) {
            log.warn("error in file", xe);
        } finally {
            Context.exit();
            IOUtil.close(_reader);
        }
    }

    static int[] _rhino_features = {};

    @Override
    public Object run(Map<String, Object> _top, boolean _useLocal)
    {
        PrintStream _sout = null;

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

            Map<String, Object> _map = this.provide(_top);
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
        RhinoScriptContext _script;
        public RhinoScriptScope(RhinoScriptContext rhinoScript)
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

            if(((RhinoScriptScope)thisObj)._script.getOutputStream()!=null)
            {
                ((RhinoScriptScope)thisObj)._script.getOutputStream().write(_sb.toString().getBytes(StandardCharsets.UTF_8));
            }
            else
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
            if(((RhinoScriptScope)thisObj)._script.getOutputStream()!=null)
            {
                ((RhinoScriptScope)thisObj)._script.getOutputStream().write(0x0a);
            }
            else
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
