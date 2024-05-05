package luay.script;

import com.github.terefang.jmelange.script.AbstractScript;
import com.github.terefang.jmelange.script.BasicVariableProvider;
import com.github.terefang.jmelange.script.IScriptContext;
import luay.main.LuayBuilder;
import luay.main.LuayContext;

import java.io.File;
import java.io.OutputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public class LuayScriptContext extends AbstractScript implements IScriptContext
{
    File file;

    @Override
    public File getFile()
    {
        return file;
    }

    @Override
    public File getScriptFile()
    {
        return file;
    }

    @Override
    public void setFile(File file)
    {
        this.file = file;
    }

    LuayContext _context = null;
    public static IScriptContext create(List<String> _path)
    {
        LuayScriptContext pLuayScriptContext = new LuayScriptContext();
        pLuayScriptContext._context = LuayBuilder
                .create()
                .allLibraries()
                .searchPath(_path)
                .build();
        pLuayScriptContext.setIncludes(_path);
        pLuayScriptContext.setVp(BasicVariableProvider.create());
        return pLuayScriptContext;
    }

    @Override
    public void setIncludes(List<String> includes)
    {
        super.setIncludes(includes);
        this._context = LuayBuilder
                .create()
                .allLibraries()
                .searchPath(includes)
                .build();
    }

    @Override
    public Object execute(File _file) {
        this.setFile(_file);
        return super.execute(_file);
    }

    @Override
    public Object execute(File _file, boolean _useLocal) {
        this.setFile(_file);
        return super.execute(_file, _useLocal);
    }

    @Override
    public void compile(File _file) {
        this.setFile(_file);
        super.compile(_file);
    }

    @Override
    public void compile(Reader _reader, String _name)
    {
        this._context.compileScript(_reader, _name);
    }

    @Override
    public Object run(Map<String, Object> _top, boolean _useLocal) {
        return this._context.runScript(this.provide(_top));
    }

    @Override
    public void setOutputStream(OutputStream _out) {
        this._context.setOutputStream(_out);
    }

    @Override
    public void setAll(Map<String, Object> _map) {
        this._context.setAll(_map);
    }

    @Override
    public void set(String _k, Object _v) {
        this._context.set(_k,_v);
    }

    @Override
    public void unset(String _k) {
        this._context.unset(_k);
    }

    @Override
    public void unsetAll() {
        this._context.unsetAll();
    }
}
