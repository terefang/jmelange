import com.github.terefang.jmelange.data.ByRowDataWriter;
import com.github.terefang.jmelange.data.DataReadWriteFactory;
import com.github.terefang.jmelange.data.DataUtil;
import com.github.terefang.jmelange.data.RowDataReader;
import com.github.terefang.jmelange.data.impl.CsvByRowWriter;
import com.github.terefang.jmelange.data.impl.CsvZipByRowWriter;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TestCsvByLine
{
    public static void main(String[] args)
    {
        File _ifile = new File("examples/data/test.scsv");
        RowDataReader _odr = DataReadWriteFactory.findByName("scsv", RowDataReader.class);
        List<Map<String, Object>> _data = _odr.readRows(_ifile);

        File _ofile = new File("examples/data/test-by-line.csv");

        CsvByRowWriter _wr = CsvByRowWriter.from(_ofile);
        _wr.newSheet("default", new Vector(_data.get(0).keySet()));
        for(Map<String, Object> _row : _data)
        {
            _wr.write(_row);
        }
        _wr.close();

        _ofile = new File("examples/data/test-by-line.csv.zip");
        writeto(_ofile, _data);

        _ofile = new File("examples/data/test-by-line.xlsx");
        writeto(_ofile, _data);
    }

    static void writeto(File _ofile, List<Map<String, Object>> _data)
    {
        ByRowDataWriter _csv = DataUtil.newByRowDataWriter(_ofile);
        for(int _i = 0; _i < 256; _i++)
        {
            _csv.newSheet("default"+_i, new Vector(_data.get(0).keySet()));
            for(Map<String, Object> _row : _data)
            {
                _csv.write(_row);
            }
        }
        _csv.close();
    }
}
