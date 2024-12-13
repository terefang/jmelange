import com.github.terefang.jmelange.gfx.ImageUtil;
import com.github.terefang.jmelange.gfx.impl.PdfImage;

import java.awt.*;

public class TestImagePdf
{
    public static void main(String[] args) {
        PdfImage _pdf = ImageUtil.pdfImage(200, 200);
        Font _font = _pdf.getTTFont("res/fonts/NotoSansMerged-Regular.ttf", 20);
        _pdf.beginText();
        _pdf.setFont(_font);
        _pdf.drawString("Hellow Worlds !", 20, 20);
        _pdf.endText();

        _pdf.beginText();
        _font = _pdf.getTTFont("res/fonts/NotoSansMerged-Regular.ttf", 50);
        _pdf.setFont(_font);
        _pdf.drawString("Hellow Worlds !", 100, 100);
        _pdf.endText();

        _pdf.save("out/test_image_pdf.pdf");
    }
}
