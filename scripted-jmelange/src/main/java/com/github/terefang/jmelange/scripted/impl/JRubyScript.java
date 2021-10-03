package com.github.terefang.jmelange.scripted.impl;

import com.github.terefang.jmelange.scripted.AbstractScript;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jruby.CompatVersion;
import org.jruby.RubyInstanceConfig;
import org.jruby.embed.EmbedEvalUnit;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;
import org.jruby.embed.internal.BiVariableMap;

import java.io.PrintStream;
import java.io.Reader;
import java.util.Map;

@Slf4j
public class JRubyScript extends AbstractScript
{
    public static JRubyScript create() { return new JRubyScript(); }
    private ScriptingContainer sinstance;
    private EmbedEvalUnit scriptCode;

    @Override
    @SneakyThrows
    public boolean init(Reader _script)
    {
        try
        {
            this.sinstance = new ScriptingContainer(LocalContextScope.THREADSAFE, LocalVariableBehavior.PERSISTENT);
            this.sinstance.setCompileMode(RubyInstanceConfig.CompileMode.FORCE);
            this.sinstance.setCompatVersion(CompatVersion.RUBY2_1);
            this.scriptCode = this.sinstance.parse(_script, this.getScriptFile().getName(), 0);
        }
        catch (Exception xe)
        {
            log.warn("error in file", xe);
        }
        finally
        {
            return (this.scriptCode != null);
        }
    }

    @Override
    public Object executeObject(boolean _scopeOrBindings)
    {
        try
        {

            synchronized (this.sinstance)
            {
                this.sinstance.clear();

                if(this.getArgs()!=null) this.sinstance.setArgv(this.getArgs().toArray(new String[this.getArgs().size()]));

                if(this.getInputStream()!=null) this.sinstance.setInput(this.getInputStream());
                if(this.getOutputStream()!=null) this.sinstance.setOutput(new PrintStream(this.getOutputStream()));

                for (Map.Entry<String, Object> e : this.assembleContext().entrySet()) {
                    this.sinstance.put("$"+e.getKey(), e.getValue());
                }

                BiVariableMap _vars = this.sinstance.getVarMap();

                Object _ret = this.scriptCode.run();

                return _ret;
            }
        }
        catch (Exception xe)
        {
            log.warn("error in file", xe);
            return null;
        }
    }

}
