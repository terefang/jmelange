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
    boolean bindScope = false;

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
            _ret.put(SCRIPT_ID_VAR, GuidUtil.toUUID(this.getScriptFile().getAbsolutePath()));
            _ret.put(SCRIPT_FILENAME_VAR, this.getScriptFile().getAbsolutePath());
        }
        else
        if(this.getSourceFile()!=null)
        {
            _ret.put(SCRIPT_ID_VAR, GuidUtil.toUUID(this.getSourceFile().getAbsolutePath()));
            _ret.put(SCRIPT_FILENAME_VAR, this.getSourceFile().getAbsolutePath());
        }
        else
        {
            String _id = GuidUtil.randomUUID();
            _ret.put(SCRIPT_ID_VAR, _id );
            _ret.put(SCRIPT_FILENAME_VAR, _id);
        }

        if(this.getSourceFile()!=null)
        {
            _ret.put(SCRIPT_INPUT_FILE_VAR, this.getSourceFile());
            _ret.put(SCRIPT_INPUT_FILEPATH_VAR, this.getSourceFile().getAbsolutePath());
            _ret.put(SCRIPT_INPUT_FILEDIR_VAR, this.getSourceFile().getParent());
        }

        if(this.getDestinationFile()!=null)
        {
            _ret.put(SCRIPT_OUTPUT_FILE_VAR, this.getDestinationFile());
            _ret.put(SCRIPT_OUTPUT_FILEPATH_VAR, this.getDestinationFile().getAbsolutePath());
            _ret.put(SCRIPT_OUTPUT_FILEDIR_VAR, this.getDestinationFile().getParent());
        }

        if(this.getInputStream()!=null) _ret.put(SCRIPT_INPUT_STREAM_VAR, this.getInputStream());
        if(this.getOutputStream()!=null) _ret.put(SCRIPT_OUTPUT_STREAM_VAR, this.getOutputStream());
        if(this.getErrorLogger()!=null) {
            _ret.put(SCRIPT_LOGGER_VAR, this.getErrorLogger());
        } else {
            _ret.put(SCRIPT_LOGGER_VAR, log);
        }

        if(this.getArgs()!=null) {
            _ret.put(SCRIPT_ARGUMENTS_VAR, this.getArgs());
        } else {
            _ret.put(SCRIPT_ARGUMENTS_VAR, Collections.emptyList());
        }

        if(this.getVars()!=null) {
            _ret.put(SCRIPT_VARIABLES_VAR, this.getVars());
        } else {
            _ret.put(SCRIPT_VARIABLES_VAR, Collections.emptyMap());
        }

        _ret.put(SCRIPT_OUTPUT_TYPE_VAR, this.getOutputType().toLowerCase());
        _ret.put(SCRIPT_CONTEXT_VAR, _ret);
        ScriptHelper _h = ScriptHelper.create();
        _ret.put(SCRIPT_HELPER_VAR, _h);
        _ret.put(SCRIPT_HELPER_VAR_ALT, _h);
        _ret.put(SCRIPT_RESULT_VAR, 0);
        return _ret;
    }

    public static final String SCRIPT_ID_VAR = "_id";
    public static final String SCRIPT_FILENAME_VAR = "_filename";

    public static final String SCRIPT_INPUT_FILE_VAR = "_fin";
    public static final String SCRIPT_INPUT_FILEPATH_VAR = "_finpath";
    public static final String SCRIPT_INPUT_FILEDIR_VAR = "_findir";

    public static final String SCRIPT_OUTPUT_FILE_VAR = "_fout";
    public static final String SCRIPT_OUTPUT_FILEPATH_VAR = "_foutpath";
    public static final String SCRIPT_OUTPUT_FILEDIR_VAR = "_foutdir";

    public static final String SCRIPT_INPUT_STREAM_VAR = "_sin";
    public static final String SCRIPT_OUTPUT_STREAM_VAR = "_sout";
    public static final String SCRIPT_LOGGER_VAR = "_log";
    public static final String SCRIPT_ARGUMENTS_VAR = "_args";
    public static final String SCRIPT_VARIABLES_VAR = "_vars";
    public static final String SCRIPT_OUTPUT_TYPE_VAR = "_output_type";
    public static final String SCRIPT_HELPER_VAR = "_";
    public static final String SCRIPT_HELPER_VAR_ALT = "_H";
    public static final String SCRIPT_CONTEXT_VAR = "_ctx";
    public static final String SCRIPT_RESULT_VAR = "_result";

    public abstract Object executeObject(boolean _scopeOrBinding);

    @SneakyThrows
    public boolean execute()
    {
        return CommonUtil.checkBoolean(executeObject(this.bindScope));
    }
}
