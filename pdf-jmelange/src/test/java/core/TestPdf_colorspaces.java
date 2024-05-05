package core;/*
 * Copyright (c) 2020. terefang@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.color.PdfColorSpace;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;

import java.util.Arrays;

public class TestPdf_colorspaces
{
	public static final String LOREM_IPSUM_EN = "On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammeled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selection: he rejects pleasures to secure other greater pleasures, or else he endures pains to avoid worse pains.";

	public static void main(String[] args) throws Exception
	{
		main_colorspace_cmyk(args);
		main_colorspace_gray(args);
		main_colorspace_rgb(args);
		System.exit(0);
	}

	public static void main_colorspace_cmyk(String[] args) throws Exception
	{
		PdfDocument doc = new PdfDocument();
		PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC);
		PdfFont hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);

		for(int i =0; i<4; i++)
		{
			PdfPage _page = doc.newPage();
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(false);
			_content.setFont(hf, 30);
			_content.fillColorSpace(doc.getDeviceCMYK());
			for(int _bmp = 255; _bmp>=9; _bmp-=16)
			{
				int[] _cmyk = new int[4];
				_cmyk[i] = _bmp;
				_content.startLayer("color+"+i+"-"+_bmp);
				_content.fillSpaceColor(true, _cmyk[0]/255f,_cmyk[1]/255f,_cmyk[2]/255f,_cmyk[3]/255f);
				_content.drawString(LOREM_IPSUM_EN, 30, 30+(_bmp*2));
				_content.endLayer();
			}
		}
		doc.writeTo("./out/pdf/test-cs-cmyk.pdf");
	}
	
	public static void main_colorspace_rgb(String[] args) throws Exception
	{
		PdfDocument doc = new PdfDocument();
		PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC);
		PdfFont hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);
		
		for(int i =0; i<3; i++)
		{
			PdfPage _page = doc.newPage();
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(true);
			_content.setFont(hf, 30);
			_content.fillColorSpace(doc.getDeviceRGB());
			for(int _bmp = 255; _bmp>=9; _bmp-=16)
			{
				int[] _rgb = new int[3];
				_rgb[i] = _bmp;
				_content.fillSpaceColor(false, _rgb[0]/255f,_rgb[1]/255f,_rgb[2]/255f);
				_content.drawString(LOREM_IPSUM_EN, 30, 30+(_bmp*2));
			}
		}
		doc.writeTo("./out/pdf/test-cs-rgb.pdf");
	}
	
	public static void main_colorspace_gray(String[] args) throws Exception
	{
		PdfDocument doc = new PdfDocument();
		PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC);
		PdfFont hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);
		
		for(PdfColorSpace _cs : Arrays.asList( doc.getDeviceGray(), doc.getCalGray(1f,1f,1f,0f,0f,0f,4.5f)))
		{
			PdfPage _page = doc.newPage();
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(false);
			_content.setFont(hf, 30);
			_content.fillColorSpace(_cs);
			for(int _bmp = 255; _bmp>=9; _bmp-=16)
			{
				_content.fillSpaceColor(false, _bmp/255f);
				_content.drawString(LOREM_IPSUM_EN, 30, 30+(_bmp*2));
			}
		}
		doc.writeTo("./out/pdf/test-cs-gray.pdf");
	}
}
