package util;

import java.io.InputStream;

public class TestHelper {
    public static InputStream getStream(String _file)
    {
        return TestHelper.class.getClassLoader().getResourceAsStream(_file);
    }
}
