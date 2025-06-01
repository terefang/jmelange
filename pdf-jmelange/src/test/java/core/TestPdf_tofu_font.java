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
import com.github.terefang.jmelange.pdf.core.fonts.PdfTofuFont;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.List;
import java.util.Vector;

public class TestPdf_tofu_font
{
	public static void main(String[] args) throws Exception
	{

		PdfDocument doc = new PdfDocument();
		PdfFont hf = doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);
		List<PdfFont> _list = new Vector();
		
		
		_list.add((PdfFont) PdfTofuFont.of(doc));

		doc.streamBegin("./out/pdf/test-tofu-font.pdf");

		for(PdfFont _font :  _list)
		{
			_font.streamOut();
			System.err.println(_font.getFontName());
			PdfPage _page = doc.newPage();
			doc.newOutline(_font.getFontName(), _page);

			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(false);
			_content.setFont(hf, 20);
			_content.drawString(_font.getFontName(), 30, 820);
			_content.setFont(_font, 30);
			for(int _cp = 0; _cp<256; _cp++)
			{
				_content.drawString(Character.toString((char) (_cp)) ,30+(35 * (_cp % 16)), 800-(45+(45 * (_cp / 16))));
			}
		}

		doc.streamEnd(true);
		System.exit(0);
	}
}
