import com.github.terefang.jmelange.data.DataReadWriteFactory;
import com.github.terefang.jmelange.data.ObjectDataReader;
import com.github.terefang.jmelange.data.ObjectDataWriter;

import java.io.File;
import java.util.Map;

public class TestYaml
{
    public static void main(String[] args)
    {
        File _ifile = new File("examples/data/test-it-all.yaml");
        ObjectDataReader _odr = DataReadWriteFactory.findByName("yaml", ObjectDataReader.class);
        Map<String, Object> _data = _odr.readObject(_ifile);

        File _ofile = new File("examples/data/test-it-all.yaml.out");
        ObjectDataWriter _odw = DataReadWriteFactory.findByName("yaml", ObjectDataWriter.class);
        _odw.writeObject(_data, _ofile);
    }
}
