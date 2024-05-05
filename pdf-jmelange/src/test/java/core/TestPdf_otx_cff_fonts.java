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
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;

import java.io.File;

public class TestPdf_otx_cff_fonts
{
	public static final String[] _fontlist = {
			"/nas/_fontz/_free/TeXGyre/TeXGyreHeros-Regular.otf",
	};

	public static void main(String[] args) throws Exception
	{
		PdfDocument doc = new PdfDocument();

		doc.streamBegin("./out/pdf/test-otx-cff-fonts.pdf");

		PdfFont _hf = doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);
		PdfFont _uni = doc.registerOtxFont(null, "res/fonts/unifont/unifont-12.1.04.ttf");

		for(String _fn : _fontlist)
		{
			System.err.println(_fn);
			try
			{
				PdfPage _page = null;
				PdfContent _content = null;
				PdfFont _cf = doc.registerOtxFont(PDF.loadFrom(_fn));
				for(int _bmp = 0; _bmp<256; _bmp++)
				{
					if(_cf.hasCoverage(_bmp))
					{
						_page = doc.newPage();
						_page.setMediabox(0,0,750,842);
						_content = _page.newContent(true);
						_content.setFont(_hf, 15);
						_content.drawString(_cf.getFontName()+" | bmp="+_bmp, 30, 820);
						doc.newOutline(_cf.getFontName()+" | bmp="+_bmp, _page);
						_content.setFont(_hf, 9);
						_content.drawString(new File(_fn).getName(), 30, 810);
						for(int _cp = 0; _cp<256; _cp++)
						{
							_content.fillColor("#000000");
							_content.setFont(_cf, 30);
							_content.drawString(Character.toString((char) (_cp +(_bmp<<8))), 30+(45 * (_cp % 16)), 800-(30+(45 * (_cp / 16))));
							_content.fillColor("#ff0000");
							_content.setFont(_uni, 10);
							_content.drawString(String.format("%04X %s", (_cp +(_bmp<<8)), Character.toString((char) (_cp +(_bmp<<8)))), 30+(45 * (_cp % 16)), 800-(45+(45 * (_cp / 16))));
						}
						_page.streamOut();
					}
				}
			}
			catch(Exception _xe)
			{
				_xe.printStackTrace();
			}
		}
		doc.streamEnd(true);
		System.exit(0);
	}
}
