package com.github.terefang.jmelange.scripted.impl;


import com.github.terefang.jmelange.commons.util.IOUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.impl.luay.AdvancedGlobals;
import com.github.terefang.jmelange.scripted.impl.luay.JStringLib;

import com.github.terefang.jmelange.scripted.impl.luay.useful.BitopLib;
import com.github.terefang.jmelange.scripted.impl.luay.useful.FilesysLib;
import lombok.extern.slf4j.Slf4j;
import luay.main.LuayBuilder;
import luay.main.LuayContext;
import luay.vm.*;
import luay.vm.compiler.LuaC;
import luay.vm.lib.*;
import luay.vm.lib.jse.*;
import org.apache.commons.lang3.StringUtils;


import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@Slf4j
public class LuayScript extends AbstractScript
{
    private File _parentDir;
    List<TwoArgFunction> externalLibraries = new Vector<>();

    public List<TwoArgFunction> getExternalLibraries() {
        return externalLibraries;
    }

    public void setExternalLibraries(List<TwoArgFunction> externalLibraries) {
        this.externalLibraries = externalLibraries;
    }

    public void addExternalLibrary(TwoArgFunction extlib) {
        this.externalLibraries.add(extlib);
    }

    public static AbstractScript create() {
        return new LuayScript();
    }

    StringBuffer scriptSource;

    @Override
    public boolean init(File _script, List<File> _inc)
    {
        this._parentDir = _script.getParentFile();
        return super.init(_script, _inc);
    }

    @Override
    public boolean init(File _script)
    {
        this._parentDir = _script.getParentFile();
        return super.init(_script);
    }

    @Override
    public boolean init(Reader _script) {
        try
        {
            this.addExternalLibrary(new JStringLib());

            StringWriter _sw = new StringWriter();
            IOUtil.copy(_script, _sw);
            IOUtil.close(_script);
            this.scriptSource = _sw.getBuffer();
            return true;
        }
        catch (Exception e)
        {
            log.warn("error", e);
        }
        finally
        {
            IOUtil.close(_script);
        }
        return false;
    }

    public LuayContext createGlobals(Globals previousGlobals)
    {
        final LuayBuilder _builder = LuayBuilder.create();

        List<String> pkgSearchPath = new Vector<>();
        if(this._parentDir!=null)
        {
            pkgSearchPath.add(this._parentDir.getAbsolutePath());
        }
        if(this.getIncludeDirectories()!=null)
        {
            for(File _f : this.getIncludeDirectories())
            {
                pkgSearchPath.add(_f.getAbsolutePath());
            }
        }
        pkgSearchPath.add(".");
        pkgSearchPath.forEach((x) -> _builder.searchPath(x));

        _builder.outputStream(this.getOutputStream());
        _builder.inputStream(this.getInputStream());

        _builder.globalLibrary(new FilesysLib());
        _builder.globalLibrary(new BitopLib());

        /* if(previousGlobals!=null)
        {
            _globals.compiler = previousGlobals.compiler;
            _globals.loader = previousGlobals.loader;
            _globals.undumper = previousGlobals.undumper;
        }*/

        this.externalLibraries.forEach((x) -> _builder.extensionLibrary(x));

        return _builder.build();
    }

    @Override
    public Object executeObject(boolean _scopeOrBinding)
    {
        try
        {
            LuayContext _luay = createGlobals(null);


            for(Map.Entry<String,Object> _entry : this.assembleContext().entrySet())
            {
                _luay.set(_entry.getKey(), _entry.getValue());
            }

            return _luay.execute(this.scriptSource.toString(), "luaj-script");
        }
        catch(Exception xe)
        {
            log.warn("error in file ", xe);
        }

        return null;
    }
}
