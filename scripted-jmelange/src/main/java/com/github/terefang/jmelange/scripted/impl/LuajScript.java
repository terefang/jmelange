package com.github.terefang.jmelange.scripted.impl;


import com.github.terefang.jmelange.commons.util.IOUtil;
import com.github.terefang.jmelange.scripted.AbstractScript;
import com.github.terefang.jmelange.scripted.impl.luaj.JStringLib;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@Slf4j
public class LuajScript extends AbstractScript
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
        return new LuajScript();
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

    public Globals createGlobals(Globals previousGlobals)
    {
        Globals globals = new Globals();
        globals.load(new JseBaseLib());

        PackageLib _pkglib = new PackageLib();
        globals.load(_pkglib);
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
        String _path = StringUtils.join(pkgSearchPath.iterator(), "/?.lua;")+"/?.lua;?.lua";
        log.info("setting search-path "+_path);
        _pkglib.setLuaPath(_path);
        if(this.getOutputStream()!=null)
        {
            globals.STDOUT=new PrintStream(this.getOutputStream());
        }
        globals.load(new Bit32Lib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new CoroutineLib());
        globals.load(new JseMathLib());
        globals.load(new JseIoLib());
        globals.load(new JseOsLib());
        globals.load(new LuajavaLib());

        if(previousGlobals!=null)
        {
            globals.compiler = previousGlobals.compiler;
            globals.loader = previousGlobals.loader;
            globals.undumper = previousGlobals.undumper;
        }

        for(TwoArgFunction _ex : this.externalLibraries)
        {
            globals.load(_ex);
        }

        return globals;
    }

    @Override
    public Object executeObject(boolean _scopeOrBinding)
    {
        try
        {
            Globals globals = createGlobals(null);
            LoadState.install(globals);
            LuaC.install(globals);

            for(Map.Entry<String,Object> _entry : this.assembleContext().entrySet())
            {
                globals.set(_entry.getKey(), CoerceJavaToLua.coerce(_entry.getValue()));
            }

            LuaValue _chunk = globals.load(new StringReader(this.scriptSource.toString()), "luaj-script");
            LuaValue _ret = _chunk.call();
            return CoerceLuaToJava.coerce(_ret, Object.class);
        }
        catch(Exception xe)
        {
            log.warn("error in file ", xe);
        }

        return null;
    }
}
