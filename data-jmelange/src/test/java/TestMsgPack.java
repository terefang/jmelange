import com.github.terefang.jmelange.beans.BeanUtil;
import com.github.terefang.jmelange.data.DataReadWriteFactory;
import com.github.terefang.jmelange.data.ObjectDataReader;
import com.github.terefang.jmelange.data.ObjectDataWriter;

import java.io.File;
import java.util.Map;

public class TestMsgPack
{
    public static void main(String[] args)
    {
        File _ifile = new File("examples/data/test-it-all.pdata");
        ObjectDataReader _odr = DataReadWriteFactory.findByName("pdata", ObjectDataReader.class);
        Map<String, Object> _data = _odr.readObject(_ifile);

        File _ofile = new File("examples/data/test-it-all.msgp");
        ObjectDataWriter _odw = DataReadWriteFactory.findByName("msgpack", ObjectDataWriter.class);
        _odw.writeObject(_data, _ofile);

        _odr = DataReadWriteFactory.findByName("msgpack", ObjectDataReader.class);
        _data = _odr.readObject(_ofile);

        BeanUtil.writeBean(_data, "msgpack", new File("examples/data/test-it-all.msgp.bean.out"));
        BeanUtil.writeBean(_data, "hson", new File("examples/data/test-it-all.msgp.hson"));

        /*
        File _ifile2 = new File("examples/data/test-it-all.rows.hson");
        ObjectDataReader _rdr = DataReadWriteFactory.findByName("hson", RowDataReader.class);
        Map<String, Object> _rows = _odr.readObject(_ifile2);

        File _ofile2 = new File("examples/data/test-it-all.rows.hson.out");
        ObjectDataWriter _rdw = DataReadWriteFactory.findByName("hson", RowDataWriter.class);
        _odw.writeObject(_rows, _ofile2);
        */
    }
}
