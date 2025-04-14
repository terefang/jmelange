import com.github.terefang.jmelange.beans.BeanUtil;
import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.PdataUtil;
import com.github.terefang.jmelange.data.DataReadWriteFactory;
import com.github.terefang.jmelange.data.ObjectDataReader;
import com.github.terefang.jmelange.data.ObjectDataWriter;
import com.github.terefang.jmelange.data.util.PlistUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TestPlist
{
    public static void main(String[] args)
    {
        runTest("examples/data/test-it-all.pdata");
    }

    public static void runTest(String _path)
    {
        File _ifile = new File(_path);
        ObjectDataReader _odr = DataReadWriteFactory.findByName("pdata", ObjectDataReader.class);
        Map<String, Object> _data = _odr.readObject(_ifile);

        File _ofile = new File(_path+".plist");
        PlistUtil.writeContext(_data, _ofile);
    }


}
