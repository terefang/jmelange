import com.github.terefang.jmelange.data.*;

import java.io.File;
import java.util.Map;

public class TestJson
{
    public static void main(String[] args)
    {
        File _ifile = new File("examples/data/test-it-all.json");
        ObjectDataReader _odr = DataReadWriteFactory.findByName("json", ObjectDataReader.class);
        Map<String, Object> _data = _odr.readObject(_ifile);

        File _ofile = new File("examples/data/test-it-all.json.out");
        ObjectDataWriter _odw = DataReadWriteFactory.findByName("json", ObjectDataWriter.class);
        _odw.writeObject(_data, _ofile);

        /*
        File _ifile2 = new File("examples/data/test-it-all.rows.json");
        ObjectDataReader _rdr = DataReadWriteFactory.findByName("json", RowDataReader.class);
        Map<String, Object> _rows = _odr.readObject(_ifile2);

        File _ofile2 = new File("examples/data/test-it-all.rows.json.out");
        ObjectDataWriter _rdw = DataReadWriteFactory.findByName("json", RowDataWriter.class);
        _odw.writeObject(_rows, _ofile2);

         */
    }
}
