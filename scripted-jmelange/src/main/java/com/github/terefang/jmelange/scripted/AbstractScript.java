package com.github.terefang.jmelange.scripted;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.GuidUtil;
import com.github.terefang.jmelange.scripted.util.ScriptHelper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public abstract class AbstractScript
{
    String outputType = "text";
    InputStream inputStream;
    OutputStream outputStream;
    Logger errorLogger;
    List<Object> args;
    Map<String, Object> vars;
    List<File> includeDirectories;
    File scriptFile;
    File sourceFile;
    File destinationFile;
    List<VariableProvider> variableProviders;

    @SneakyThrows
    public boolean init(File _script, List<File> _inc)
    {
        this.setIncludeDirectories(_inc);
        return init(_script);
    }

    @SneakyThrows
    public boolean init(File _script)
    {
        this.setScriptFile(_script);
        try (FileReader _fh = new FileReader(_script)) {
            return init(_fh);
        }
    }

    public abstract boolean init(Reader _script);

    @SneakyThrows
    public boolean execute(OutputStream _out, Logger _err, Map<String, Object> _vars)
    {
        this.setOutputStream(_out);
        this.setErrorLogger(_err);
        this.setVars(_vars);
        return execute();
    }

    public Map<String, Object> assembleContext()
    {
        Map<String, Object> _ret = new HashMap<>();

        if(this.getVariableProviders()!=null)
        {
            for(VariableProvider _vp : this.getVariableProviders())
            {
                _ret.putAll(_vp.provide(_ret));
            }
        }

        if(this.getScriptFile()!=null)
        {
            _ret.put("_id", GuidUtil.toUUID(this.getScriptFile().getAbsolutePath()));
        }
        else
        if(this.getSourceFile()!=null)
        {
            _ret.put("_id", GuidUtil.toUUID(this.getSourceFile().getAbsolutePath()));
        }
        else
        {
            _ret.put("_id", GuidUtil.randomUUID());
        }

        if(this.getSourceFile()!=null)
        {
            _ret.put("_fin", this.getSourceFile());
            _ret.put("_finpath", this.getSourceFile().getAbsolutePath());
            _ret.put("_findir", this.getSourceFile().getParent());
        }

        if(this.getDestinationFile()!=null)
        {
            _ret.put("_fout", this.getDestinationFile());
            _ret.put("_foutpath", this.getDestinationFile().getAbsolutePath());
            _ret.put("_foutdir", this.getDestinationFile().getParent());
        }

        if(this.getInputStream()!=null) _ret.put("_sin", this.getInputStream());
        if(this.getOutputStream()!=null) _ret.put("_sout", this.getOutputStream());
        if(this.getErrorLogger()!=null) {
            _ret.put("_log", this.getErrorLogger());
        } else {
            _ret.put("_log", log);
        }

        if(this.getArgs()!=null) {
            _ret.put("_args", this.getArgs());
        } else {
            _ret.put("_args", Collections.emptyList());
        }

        if(this.getVars()!=null) {
            _ret.put("_vars", this.getVars());
        } else {
            _ret.put("_vars", Collections.emptyMap());
        }

        _ret.put("_output_type", this.getOutputType().toLowerCase());
        _ret.put("_ctx", _ret);
        _ret.put("_", ScriptHelper.create());
        _ret.put("_result", 0);
        return _ret;
    }

    public abstract Object executeObject();

    @SneakyThrows
    public boolean execute()
    {
        return CommonUtil.checkBoolean(executeObject());
    }
}
