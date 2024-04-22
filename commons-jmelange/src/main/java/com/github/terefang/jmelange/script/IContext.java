package com.github.terefang.jmelange.script;

import com.github.terefang.jmelange.commons.util.GuidUtil;
import org.slf4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IContext
{
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

    default public Map<String, Object> assembleContext(List<Map<String, Object>> _ctxs, Logger _log, Object _h)
    {
        Map<String, Object> _ret = new HashMap<>();

        if(_ctxs!=null)
        {
            for(Map<String, Object> _vp : _ctxs)
            {
                _ret.putAll(_vp);
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
            if(this.getDestinationFile().isDirectory())
            {
                _ret.put(SCRIPT_OUTPUT_FILEDIR_VAR, this.getDestinationFile().getAbsolutePath());
                File _f = new File(this.getDestinationFile(), _ret.get(SCRIPT_ID_VAR).toString());
                _ret.put(SCRIPT_OUTPUT_FILE_VAR, _f);
                _ret.put(SCRIPT_OUTPUT_FILEPATH_VAR, _f.getAbsolutePath());
            }
            else
            {
                _ret.put(SCRIPT_OUTPUT_FILE_VAR, this.getDestinationFile());
                _ret.put(SCRIPT_OUTPUT_FILEDIR_VAR, this.getDestinationFile().getParent());
                _ret.put(SCRIPT_OUTPUT_FILEPATH_VAR, this.getDestinationFile().getAbsolutePath());
            }
        }

        if(this.getInputStream()!=null) _ret.put(SCRIPT_INPUT_STREAM_VAR, this.getInputStream());
        if(this.getOutputStream()!=null) _ret.put(SCRIPT_OUTPUT_STREAM_VAR, this.getOutputStream());
        if(_log!=null)
        {
            _ret.put(SCRIPT_LOGGER_VAR, _log);
        }
        else if(this.getErrorLogger()!=null)
        {
            _ret.put(SCRIPT_LOGGER_VAR, this.getErrorLogger());
        }

        if(this.getArgs()!=null)
        {
            _ret.put(SCRIPT_ARGUMENTS_VAR, this.getArgs());
        }
        else
        {
            _ret.put(SCRIPT_ARGUMENTS_VAR, Collections.emptyList());
        }

        if(this.getVars()!=null)
        {
            _ret.put(SCRIPT_VARIABLES_VAR, this.getVars());
        }
        else
        {
            _ret.put(SCRIPT_VARIABLES_VAR, Collections.emptyMap());
        }

        _ret.put(SCRIPT_OUTPUT_TYPE_VAR, this.getOutputType().toLowerCase());
        _ret.put(SCRIPT_CONTEXT_VAR, _ret);

        if(_h!=null)
        {
            _ret.put(SCRIPT_HELPER_VAR, _h);
            _ret.put(SCRIPT_HELPER_VAR_ALT, _h);
        }
        _ret.put(SCRIPT_RESULT_VAR, 0);
        return _ret;
    }

    default public String getOutputType() { return "TEXT"; }
    default public List<String> getArgs() { return null; }
    default public void setArgs(List<String> _args) { /**/ }
    default public Map<String, Object> getVars() { return null; }
    default public Logger getErrorLogger() { return null; }
    default public File getScriptFile() { return null; }
    default public File getSourceFile() { return null; }
    default public File getDestinationFile() { return null; }
    default public InputStream getInputStream() { return null; }
    default public OutputStream getOutputStream() { return null; }

    public void setAll(Map<String,Object> _vars);
    public void set(String _key, Object _val);
    public void unset(String _key);
    public void unsetAll();
    public void add(IContextVariableProvider _p);
    public void addAll(List<IContextVariableProvider> _p);
    public void setFile(File _file);
}
