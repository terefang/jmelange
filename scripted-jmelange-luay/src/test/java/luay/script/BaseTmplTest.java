package luay.test.script;

import luay.test.tmplTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class BaseTmplTest extends tmplTestCase
{
    @Test
    void testEsp()
    {
        setUp("lua-esp", ".lua.esp", Collections.emptyList());
        testScript("test_esp.lua.esp", "_var", 2);
    }

    @Test
    void testAs()
    {
        setUp("lua", ".lua", Collections.emptyList());
        testScript("test_as.lua", "_var", 2);
    }

    public static void main(String[] args) {
        BaseTmplTest _base = new BaseTmplTest();
        _base.testAs();
        _base.testEsp();
    }
}
