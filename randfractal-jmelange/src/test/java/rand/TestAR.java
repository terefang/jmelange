package rand;

import com.github.terefang.jmelange.randfractal.Noisefield;
import com.github.terefang.jmelange.random.ArcRandom;
import com.github.terefang.jmelange.randfractal.utils.NoiseFieldUtil;
import com.github.terefang.jmelange.random.IRandom;
import lombok.SneakyThrows;

import java.nio.charset.Charset;
import java.util.Map;

public class TestAR
{
    public static void main_(String[] args)
    {
        for(Map.Entry<String, Charset> _cs : Charset.availableCharsets().entrySet())
        {
            System.out.println(_cs.getKey());
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        Noisefield _nf = new Noisefield(256,256);

        IRandom _arc = IRandom.arcRandom(0xdeadbeefL);

        double[] _data = _nf.getData();

        for(int _j = 0; _j < 256; _j++)
        {
            for(int _i = 0; _i < _data.length; _i++)
            {
                if(false)
                {
                        int _d = 0;
                        for(int _k = 0; _k < 8; _k++)
                        {
                            _d = (_d << 1) | (_arc.nextByte() & 1);
                        }
                        _data[_i] = (double)(127-_d)/128.0;
                }
                else
                {
                    _data[_i] = (double)(128-(_arc.nextByte()&0xff))/128.0;
                }
            }

            _nf.mathClip(-1, 1);
            NoiseFieldUtil.saveHFEImage(_nf, "./out/arc-"+_j+".png");
            System.err.println(".");
        }
    }
}
