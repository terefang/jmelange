import com.github.terefang.jmelange.commons.util.LdataUtil;

import java.io.StringReader;

public class TestLdata
{
    public static void main(String[] args)
    {
        LdataUtil.loadFrom(new StringReader("{'key' = 'value'}"));
    }
}
