import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.color.PdfColorSpace;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import com.github.terefang.jmelange.pdf.ext.PdfExtDocument;

import java.util.Arrays;

public class TestPdf_pdfx
{
	public static final String LOREM_IPSUM_EN = "On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammeled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selection: he rejects pleasures to secure other greater pleasures, or else he endures pains to avoid worse pains.";

	public static void main(String[] args) throws Exception
	{
		PdfExtDocument doc = new PdfExtDocument();
		doc.setPdfXIccCrpc1();
		make(doc, "./out/pdf/test-pdfx-crpc1.pdf");

		doc = new PdfExtDocument();
		doc.setPdfXPSOCv3();
		make(doc,"./out/pdf/test-pdfx-psocv3.pdf");

		doc = new PdfExtDocument();
		doc.setPdfXPSOUCv3();
		make(doc, "./out/pdf/test-pdfx-psoucv3.pdf");
	}


	public static void make(PdfExtDocument doc, String _out) throws Exception
	{
		PdfFont hf = doc.registerTexGyreHerosBoldFont();

		for(int i =0; i<4; i++)
		{
			PdfPage _page = doc.newPage();
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent();
			//_content.setFilter(null);
			_content.setFont(hf, 30);
			_content.fillColorSpace(doc.getDeviceCMYK());
			for(int _bmp = 255; _bmp>=9; _bmp-=16)
			{
				int[] _cmyk = new int[4];
				_cmyk[i] = _bmp;
				_content.startLayer("color+cmyk+"+i+"-"+_bmp);
				_content.fillSpaceColor(true, _cmyk[0]/255f,_cmyk[1]/255f,_cmyk[2]/255f,_cmyk[3]/255f);
				_content.drawString(LOREM_IPSUM_EN, 30, 30+(_bmp*2));
				_content.endLayer();
			}
		}

		for(int i =0; i<3; i++)
		{
			PdfPage _page = doc.newPage();
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent();
			//_content.setFilter(null);
			_content.setFont(hf, 30);
			for(int _bmp = 255; _bmp>=9; _bmp-=16)
			{
				int[] _rgb = new int[3];
				_rgb[i] = _bmp;
				_content.startLayer("color+rgb+"+i+"-"+_bmp);
				_content.fillColor(_rgb[0],_rgb[1],_rgb[2]);
				_content.drawString(LOREM_IPSUM_EN, 30, 30+(_bmp*2));
				_content.endLayer();
			}
		}
		doc.writeTo(_out);
	}

}
