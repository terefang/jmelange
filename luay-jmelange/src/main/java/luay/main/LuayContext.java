package luay.main;

import lombok.SneakyThrows;
import luay.vm.LuaTable;
import luay.vm.LuaValue;
import luay.vm.Prototype;
import luay.vm.lib.jse.CoerceJavaToLua;
import luay.vm.lib.jse.CoerceLuaToJava;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LuayContext {
    private final LuayGlobals globals;

    Map<String,Object> top = new HashMap<>();
    private Prototype script;
    private String scriptName;

    public LuayContext(LuayGlobals _globals) {
        this.globals = _globals;
    }

    public void set(String key, Object val)
    {
        this.top.put(key, val);
    }

    public void unset(String key)
    {
        this.top.remove(key);
    }

    public void unsetAll()
    {
        this.top.clear();
    }

    @SneakyThrows
    public Object execute(File _file)
    {
        return execute(new FileReader(_file), _file.getName());
    }

    public Object execute(InputStream _stream, String _name)
    {
        return execute(new InputStreamReader(_stream, StandardCharsets.UTF_8), _name);
    }

    public Object execute(String _script, String _name)
    {
        return execute(new StringReader(_script), _name);
    }

    public Object execute(Reader _reader, String _name)
    {
        LuaTable _env = this.globals.cloneEnv();
        top.forEach((a,b) -> _env.set(a, CoerceJavaToLua.coerce(b)));

        LuaValue _chunk = this.globals.load(_reader, _name, _env);

        LuaValue _ret = _chunk.call();

        return CoerceLuaToJava.coerce(_ret, Object.class);
    }

    @SneakyThrows
    public void compileScript(File _file)
    {
        compileScript(new FileReader(_file), _file.getName());
    }

    public void compileScript(InputStream _stream, String _name)
    {
        compileScript(new InputStreamReader(_stream, StandardCharsets.UTF_8), _name);
    }

    public void compileScript(String _script, String _name)
    {
        compileScript(new StringReader(_script), _name);
    }

    @SneakyThrows
    public void compileScript(Reader _reader, String _name)
    {
        this.script = this.globals.compilePrototype(_reader, _name);
        this.scriptName = _name;
    }

    @SneakyThrows
    public Object runScript()
    {
        LuaTable _env = this.globals.cloneEnv();
        top.forEach((a,b) -> _env.set(a, CoerceJavaToLua.coerce(b)));

        return runScript(_env);
    }

    @SneakyThrows
    public Object runScript(Map<String,Object> _top)
    {
        LuaTable _env = this.globals.cloneEnv();
        _top.forEach((a,b) -> _env.set(a, CoerceJavaToLua.coerce(b)));

        return runScript(_env);
    }

    @SneakyThrows
    public Object runScript(LuaTable _env)
    {
        LuaValue _chunk = this.globals.loader.load(this.script, this.scriptName, _env);

        LuaValue _ret = _chunk.call();

        return CoerceLuaToJava.coerce(_ret, Object.class);
    }
}
