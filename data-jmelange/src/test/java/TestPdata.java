import com.github.terefang.jmelange.beans.BeanUtil;
import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.data.DataReadWriteFactory;
import com.github.terefang.jmelange.data.ObjectDataReader;
import com.github.terefang.jmelange.data.ObjectDataWriter;
import com.github.terefang.jmelange.pdata.PdataParser;
import com.github.terefang.jmelange.pdata.PdataWriter;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TestPdata
{
    public static void main(String[] args)
    {
        File _ifile = new File("examples/data/test-it-all.pdata");
        ObjectDataReader _odr = DataReadWriteFactory.findByName("pdata", ObjectDataReader.class);
        Map<String, Object> _data = _odr.readObject(_ifile);

        List<String> _todo = CommonUtil.toList("props", "pdata", "json", "hson", "yaml");

        for(String _ext : _todo)
        {
            File _ofile = new File("examples/data/test-it-all.pdata."+_ext);
            ObjectDataWriter _odw = DataReadWriteFactory.findByName(_ext, ObjectDataWriter.class);
            _odw.writeObject(_data, _ofile);
        }

        BeanUtil.writeBean(_data, "pdata", new File("examples/data/test-it-all.pdata.bean.out"));


    }

}
