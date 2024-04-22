import com.github.terefang.jmelange.commons.match.Filter;
import com.github.terefang.jmelange.commons.match.MatchContext;
import com.github.terefang.jmelange.commons.match.Matcher;
import com.github.terefang.jmelange.commons.match.QLFilter;
import com.github.terefang.jmelange.commons.match.basic.FunctionCallFilter;
import com.github.terefang.jmelange.commons.match.basic.IVariable;


import java.util.*;


public class TestQL
{
    static Object[] _qlvalues = {"my", "some", "that", "y","n",1, 2, 2.5, 3, "römersalatisgesund"};
    static String[] _qlfilter = {
            "IS_IN(gh_b,a,CH(gh_b))",
            "(gh_b = \"some\") OR (gh_x == \"this\") OR (gh_b LIKE \"*t*\")OR NOT(gh_b ~= \"*y*\")",
            "gh_b > 2",
            "gh_b < 2",
            "gh_b >= 2",
            "gh_b <= 2",
            "gh_b == 2",
            "(gh_b ~~ \"some\") OR (gh_b MATCH \"y$\")",
            "gh_b > c",
            "gh_b < c",
            "gh_b >= c",
            "gh_b <= c",
            "gh_b == c",
            "gh_b LIKE \"*r?mer*is*esu?d\"",
            "gh_b > b",
            "gh_b < b",
            "gh_b >= b",
            "gh_b <= b",
            "gh_b == b",
            "(a like b) and gh_b in LIST(a,b,c) or t or ipmatch(\"1.1.1.1\",c,d,e,f)",
            "gh_b > 2.5",
            "gh_b < 2.5",
            "gh_b >= 2.5",
            "gh_b <= 2.5",
            "gh_b == 2.5",
            "IS_IN(gh_b,a,b,c)",
            "IS_IN(gh_b,a,b,CH(c))"
    };

    static String[] _ql2filter = {
            " 2 ~~ 2 ",            " 2 ~= 2 ",            " 2 like 2 ",            " 2 match 2 ",
            " '2' ~~ '2' ",        " '2' ~= '2' ",        " '2' like '2' ",        " '2' match '2' ",
            " '3' ~~ '2' ",        " '3' ~= '2' ",        " '3' like '2' ",        " '3' match '2' ",
            " \"3\" ~~ '2' ",      " \"3\" ~= '2' ",      " \"3\" like '2' ",      " \"3\" match '2' ",
            " \"3\" ~~ \"3\" ",    " \"3\" ~= \"3\" ",    " \"3\" like \"3\" ",    " \"3\" match \"3\" ",
            " \"A\" ~~ \"a\" ",    " \"A\" ~= \"a\" ",    " \"A\" like \"a\" ",    " \"A\" match \"a\" ",
            "NOT( 1 == 2 )",
            "( \"123\" =^ \"1\" )",
            "( \"123\" =$ \"3\" )",
            "( \"abc\" ~^ \"A\" )",
            "( \"abc\" =^ \"A\" )",
            "( \"abc\" ~$ \"C\" )",
            "( \"abc\" =$ \"C\" )",
            "( \"abc\" =* \"b\" )",
            "( \"abc\" ~* \"B\" )",
            "( \"abc\" =* \"B\" )",
            " ab->some "
    };

    public static final void main(String[] args) throws Exception
    {
        MatchContext h = new MatchContext();
        h.register("IS_IN", (_fn, _args, _ctx)->{
            for(int _i = 1; _i<_args.size(); _i++)
            {
                if(_args.get(0).asString(_ctx).equals(_args.get(_i).asString(_ctx)))
                {
                    return IVariable._TRUE;
                }
            }
            return IVariable._FALSE;
        });
        h.register("CH", (_fn, _args, _ctx)->{
            return IVariable.make(_args.get(0).asString(_ctx).substring(0,1));
        });
        h.set("a", "y");
        h.set("b", "y");
        h.set("c", "n");
        h.set("func", "n");
        h.set("t", "false");
        for(String _ql : _qlfilter)
        {
            QLFilter q = new QLFilter(_ql);
            for(Object _qv : _qlvalues)
            {
                h.set("gh_b", _qv);
                System.out.println(q.match(h)+" v="+_qv+" q="+_ql);
            }
        }

        //h.set("ab", Collections.singletonMap("some", "0"));
        System.out.println("----------------------------");
        for(String _ql : _ql2filter)
        {
            QLFilter q = new QLFilter(_ql);
            System.out.println(q.match(h)+" q="+_ql);
        }

        Map _em = Collections.emptyMap();
        long _t0 = System.nanoTime();
        for(int _i=0; _i<1024; _i++)
        {
            for(String _ql : _ql2filter)
            {
                QLFilter q = new QLFilter(_ql);
                q.match(_em);
            }
        }
        long _t1 = System.nanoTime()-_t0;
        System.out.println("ms = "+_t1/1000000);
        _t1/=1024;
        _t1/=_ql2filter.length;
        System.out.println("ns/q = "+_t1);
        System.out.println("----------------------------");

        Filter sf = Matcher.parse("(key2=*)");
        System.out.println("----------------------------");
        System.out.println(sf.toString());
        System.out.println(sf.match("v2"));

        sf = Matcher.parse("(attr~~reg.*ul\\\\r ?expr\\([^a-z]+\\))");
        System.out.println("----------------------------");
        System.out.println(sf.toString());

        sf = Matcher.parse("(key2~~v.*)");
        System.out.println("----------------------------");
        System.out.println(sf.toString());
        System.out.println(sf.match("v2"));

        sf = Matcher.parse("(key2~~.v.*)");
        System.out.println("----------------------------");
        System.out.println(sf.toString());
        System.out.println(sf.match("v2"));

        System.out.println("----------------------------");
        Map map = new Hashtable();
        map.put("fattr", Boolean.FALSE);
        System.out.println(sf.match(map));
    }
}
