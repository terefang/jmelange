import com.github.terefang.jmelange.data.*;

import java.io.File;
import java.util.Map;

public class TestHson
{
    public static void main(String[] args)
    {
        File _ifile = new File("examples/data/test-it-all.hson");
        ObjectDataReader _odr = DataReadWriteFactory.findByName("hson", ObjectDataReader.class);
        Map<String, Object> _data = _odr.readObject(_ifile);

        File _ofile = new File("examples/data/test-it-all.hson.out");
        ObjectDataWriter _odw = DataReadWriteFactory.findByName("hson", ObjectDataWriter.class);
        _odw.writeObject(_data, _ofile);

        File _ifile2 = new File("examples/data/test-it-all.rows.hson");
        ObjectDataReader _rdr = DataReadWriteFactory.findByName("hson", RowDataReader.class);
        Map<String, Object> _rows = _odr.readObject(_ifile2);

        File _ofile2 = new File("examples/data/test-it-all.rows.hson.out");
        ObjectDataWriter _rdw = DataReadWriteFactory.findByName("hson", RowDataWriter.class);
        _odw.writeObject(_rows, _ofile2);
    }
}
