import com.github.terefang.jmelange.commons.match.Filter;
import com.github.terefang.jmelange.commons.match.Matcher;
import com.github.terefang.jmelange.commons.match.QLFilter;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class TestQL
{
    static String[] _qlvalues = {"my", "some", "that", "1", "2", "3", "römersalatisgesund"};
    static String[] _qlfilter = {
            "(gh_b = \"some\") OR (gh_x == \"this\") OR (gh_b LIKE \"*t*\")OR NOT(gh_b ~= \"*y*\")",
            "(gh_b ~~ \"some\") OR (gh_b MATCH \"y$\")",
            "gh_b > 2",
            "gh_b < 2",
            "gh_b >= 2",
            "gh_b <= 2",
            "gh_b == 2",
            "gh_b LIKE \"*r?mer*is*esu?d\""
    };

    public static final void main(String[] args) throws Exception
    {
        Hashtable h = new Hashtable();
        for(String _ql : _qlfilter)
        {
            QLFilter q = new QLFilter(_ql);
            for(String _qv : _qlvalues)
            {
                h.put("gh_b", _qv);
                System.out.println(q.match(h)+" v="+_qv+" q="+_ql);
            }
        }

        Map<String, String> p = new HashMap<>();
        p.put("key1", "v1");
        p.put("key2", "v2");
        p.put("key3", "v3");

        h.put("obj", p);

        Filter kvf = Matcher.keyValueFilter("key2","v2");

        System.out.println("----------------------------");
        System.out.println(kvf.match(h));
        System.out.println(kvf.match(p));
        System.out.println(kvf.match("v2"));

        kvf = Matcher.keyValueFilter("obj.key2","v2");

        System.out.println("----------------------------");
        System.out.println(kvf.match(h));
        System.out.println(kvf.match(p));
        System.out.println(kvf.match("v2"));

        Filter sf = Matcher.parse("(key2=*)");
        System.out.println("----------------------------");
        System.out.println(sf.toString());
        System.out.println(sf.match(p));
        System.out.println(sf.match("v2"));

        sf = Matcher.parse("(attr~~reg.*ul\\\\r ?expr\\([^a-z]+\\))");
        System.out.println("----------------------------");
        System.out.println(sf.toString());

        sf = Matcher.parse("(key2~~v.*)");
        System.out.println("----------------------------");
        System.out.println(sf.toString());
        System.out.println(sf.match(p));
        System.out.println(sf.match("v2"));

        sf = Matcher.parse("(key2~~.v.*)");
        System.out.println("----------------------------");
        System.out.println(sf.toString());
        System.out.println(sf.match(p));
        System.out.println(sf.match("v2"));

        System.out.println("----------------------------");
        Map map = new Hashtable();
        map.put("fattr", Boolean.FALSE);
        System.out.println(sf.match(map));
    }
}
