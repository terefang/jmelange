import com.github.terefang.jmelange.commons.gfx.impl.PixelImage;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class TestJBIR {
    @SneakyThrows
    public static void main(String[] args) {
        PixelImage _px = PixelImage.from(new File("/data/fredo/wappen/IMG_0233.JPG"));
        OutputStream _out = new FileOutputStream(new File("./out/test.ipng"));
        _px.savePng(_out);

        _out = new FileOutputStream(new File("./out/test.img"));
        _px.save4CC(_out, PixelImage.RAW_2CC);

        _out = new FileOutputStream(new File("./out/test.igz"));
        _px.save4CC(_out, PixelImage.GZIP_2CC);
        
        _px = PixelImage.load4CC(new File("./out/test.igz"));
        _px.savePng(new File("./out/test.0.png"));

    }
}
