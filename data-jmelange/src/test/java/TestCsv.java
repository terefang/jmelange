import com.github.terefang.jmelange.data.*;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TestCsv
{
    public static void main(String[] args)
    {
        File _ifile = new File("examples/data/test.scsv");
        RowDataReader _odr = DataReadWriteFactory.findByName("scsv", RowDataReader.class);
        List<Map<String, Object>> _data = _odr.readRows(_ifile);

        File _ofile = new File("examples/data/test.csv");
        RowDataWriter _odw = DataReadWriteFactory.findByName("csv", RowDataWriter.class);
        _odw.writeRows(_data, _ofile);

        _ifile = new File("examples/data/test.csv");
        _odr = DataReadWriteFactory.findByName("csv", RowDataReader.class);
        _data = _odr.readRows(_ifile);

        _ofile = new File("examples/data/test.xlsx");
        _odw = DataReadWriteFactory.findByName("xlsx", RowDataWriter.class);
        _odw.writeRows(_data, _ofile);

        _ofile = new File("examples/data/test.jpl");
        _odw = DataReadWriteFactory.findByName("jsonperline", RowDataWriter.class);
        _odw.writeRows(_data, _ofile);

        _ofile = new File("examples/data/test.rows.hson");
        _odw = DataReadWriteFactory.findByName("hson", RowDataWriter.class);
        _odw.writeRows(_data, _ofile);
    }
}
