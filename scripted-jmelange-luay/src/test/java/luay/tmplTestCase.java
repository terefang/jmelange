package luay.test;

import com.github.terefang.jmelange.script.ITemplateContext;
import com.github.terefang.jmelange.script.ITemplateContextFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class tmplTestCase
{
    private ITemplateContext scriptContext;

    protected void setUp(String _type, String _ext, List<String> _path) {
        this.scriptContext = ITemplateContextFactory.findByName(_type).newInstance(_type, _ext, _path);
    }

    protected void testScript(String _name)
    {
        testScript(_name, null);
    }

    protected void testScript(String _name, String k, Object v, Object... map)
    {
        Map<String, Object> _vars = new HashMap<>();
        _vars.put(k,v);
        if(map!=null && map.length>0 && map.length%2==0)
        {
            for(int _i = 0; _i<map.length; _i+=2)
            {
                _vars.put(map[_i].toString(),map[_i+1]);
            }
        }
        testScript(_name, _vars);
    }

    protected void testScript(String _name, Map<String, Object> _vars)
    {
        this.scriptContext.compile(getStream(_name), _name);

        if(_vars!=null)
        {
            _vars.forEach((k,v)->{ this.scriptContext.set(k, v); });
        }


        long _t0 = System.currentTimeMillis();
        try
        {
            Object _ret = this.scriptContext.run(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    System.out.write(b);
                }
            });
            this.scriptContext.unsetAll();
        }
        catch (Exception _xe)
        {
            _xe.printStackTrace(System.out);
        }

        long _t1 = System.currentTimeMillis();
        System.out.println("ms = "+(_t1-_t0));
    }

    public static InputStream getStream(String _file)
    {
        return tmplTestCase.class.getClassLoader().getResourceAsStream(_file);
    }
}
