package luay.test.script;

import luay.test.scriptTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class BaseScriptTest extends scriptTestCase
{
    @Test
    void testBase()
    {
        setUp("luay", ".lua", Collections.emptyList());
        testScript("test_script.lua", "_var", 2);
    }

    public static void main(String[] args) {
        new BaseScriptTest().testBase();
    }
}
