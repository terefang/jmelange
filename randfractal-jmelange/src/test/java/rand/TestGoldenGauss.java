package rand;

import com.github.terefang.jmelange.random.GoldenRatioRandom;
import lombok.SneakyThrows;

public class TestGoldenGauss
{
    @SneakyThrows
    public static void main(String[] args)
    {
        long[] _buf = new long[256];
        GoldenRatioRandom _r = new GoldenRatioRandom();
        _r.setSeed(0L);

        for(int _i=0; _i<0x1000000; _i++)
        {
            _buf[(int) _r.randNorm(128, 64)]++;
        }

        for(int _i=0; _i<_buf.length; _i++)
        {
            System.out.println(_buf[_i]);
        }

    }
}
