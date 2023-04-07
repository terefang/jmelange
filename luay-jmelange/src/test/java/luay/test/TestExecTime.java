package luay.test;

import luay.main.LuayBuilder;
import luay.main.LuayContext;

import java.io.InputStream;

public class TestExecTime
{
    public static void main_2(String[] args)
    {
        LuayContext _ctx = LuayBuilder.create()
                .inputStream(System.in)
                .outputStream(System.out)
                .errorStream(System.err)
                .baseLibraries()
                .build();

        _ctx.set("_var2", 2);

        long _t0 = System.currentTimeMillis();
        for(int _i=0; _i<1024000; _i++)
        {
            _ctx.set("_var", _i);
            Object _ret = _ctx.execute(getStream("base.lua"), "base.lua");
            _ctx.unsetAll();
        }
        long _t1 = System.currentTimeMillis();
        System.out.println("ms = "+(_t1-_t0));
    }


    public static void main(String[] args)
    {
        LuayContext _ctx = LuayBuilder.create()
                .inputStream(System.in)
                .outputStream(System.out)
                .errorStream(System.err)
                .baseLibraries()
                .build();
        _ctx.compileScript(getStream("base.lua"), "base.lua");

        _ctx.set("_var2", 2);

        long _t0 = System.currentTimeMillis();
        //for(int _i=0; _i<1024000; _i++)
        {
          //  _ctx.set("_var", _i);
            Object _ret = _ctx.runScript();
            _ctx.unsetAll();
        }
        long _t1 = System.currentTimeMillis();
        System.out.println("ms = "+(_t1-_t0));
    }

    public static InputStream getStream(String _file)
    {
        return TestExecTime.class.getClassLoader().getResourceAsStream(_file);
    }
}
