package com.github.terefang.jmelange.script;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;

@Slf4j
public abstract class AbstractContext implements IContext, IContextVariableProvider
{
    private List<String> includes = new Vector<>();

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    private List<IContextVariableProvider> vps = new Vector<>();

    private AbstractVariableProvider vp = BasicVariableProvider.create();

    public List<IContextVariableProvider> getVps()
    {
        return vps;
    }

    public void setVps(List<IContextVariableProvider> vp)
    {
        this.vps = vp;
    }

    public AbstractVariableProvider getVp()
    {
        return vp;
    }

    public void setVp(AbstractVariableProvider vp)
    {
        this.vp = vp;
    }

    @Override
    public void setAll(Map<String, Object> _vars)
    {
        if(this.vp!=null) {
            this.vp.setAll(_vars);
        }
    }

    @Override
    public void set(String _key, Object _val)
    {
        if(this.vp!=null) {
            this.vp.set(_key, _val);
        }
    }

    @Override
    public void unset(String _key)
    {
        if(this.vp!=null) {
            this.vp.unset(_key);
        }
    }

    @Override
    public void unsetAll()
    {
        if(this.vp!=null) {
            this.vp.unsetAll();
        }
    }

    @Override
    public void add(IContextVariableProvider _p) {
        this.vps.add(_p);
    }

    @Override
    public void addAll(List<IContextVariableProvider> _p) {
        this.vps.addAll(_p);
    }

    @Override
    public Map<String, Object> getVars()
    {
        Map<String, Object> _tmp = new HashMap<>();

        if(this.vp!=null)
        {
            _tmp.putAll(this.vp.provide(_tmp));
        }

        return _tmp;
    }

    @Override
    public Map<String, Object> provide(Map<String, Object> _map)
    {
        Map<String, Object> _tmp = new HashMap<>(_map);

        for(IContextVariableProvider _vp : this.vps)
        {
            _tmp.putAll(_vp.provide(_tmp));
        }

        return assembleContext(Collections.singletonList(_tmp), this.getErrorLogger()==null ? this.log : this.getErrorLogger(), ScriptHelper.create());
    }

    File scriptFile = null;
    String outputType = "TEXT";

    List<String> args;
    @Override
    public List<String> getArgs() {
        return this.args;
    }

    @Override
    public void setArgs(List<String> _args) {
        this.args = _args;
    }

    public File getFile() {
        return scriptFile;
    }

    public void setFile(File scriptFile) {
        this.scriptFile = scriptFile;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }
}