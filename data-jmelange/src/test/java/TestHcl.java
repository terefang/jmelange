import com.bertramlabs.plugins.hcl4j.HCLParser;
import com.github.terefang.jmelange.beans.BeanUtil;
import com.github.terefang.jmelange.data.DataReadWriteFactory;
import com.github.terefang.jmelange.data.DataUtil;
import com.github.terefang.jmelange.data.ObjectDataReader;
import com.github.terefang.jmelange.data.ObjectDataWriter;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TestHcl
{
    @SneakyThrows
    public static void main(String[] args)
    {
        File _ifile = new File("examples/data/test-it-all.hcl");

        HCLParser _p = new HCLParser();
        Map<String, Object> _data = _p.parse(_ifile, StandardCharsets.UTF_8);

        BeanUtil.writeBean(_data, "hson", new File("examples/data/test-it-all.hcl.hson.out"));
    }
}
