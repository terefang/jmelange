import com.github.terefang.jmelange.pdata.PdataParser;
import com.github.terefang.jmelange.pdata.PdataWriter;

import java.io.File;
import java.util.Map;

public class TestPdata {
    public static void main(String[] args) {
        Map<String, Object> _data = PdataParser.loadFrom(new File("examples/test-it-all.pdata"));
        PdataWriter.writeTo(_data, new File("examples/test-it-all.pdata.out"));
    }
}
