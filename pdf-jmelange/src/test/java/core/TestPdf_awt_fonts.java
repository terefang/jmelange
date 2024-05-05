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

import java.awt.*;
import java.util.List;
import java.util.Vector;

public class TestPdf_awt_fonts
{
	public static void main(String[] args) throws Exception
	{

		PdfDocument doc = new PdfDocument();
		PdfFont hf = doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);
		List<PdfFont> _list = new Vector();

		GraphicsEnvironment _env = GraphicsEnvironment.getLocalGraphicsEnvironment();

		for(Font _font : _env.getAllFonts())
		{
			System.err.println(_font.getFontName());

			if(_font.getFontName().startsWith("Iosevka")) continue;
			if(_font.getFontName().startsWith("Noto")) continue;

			if(_font.getName().equalsIgnoreCase("treksigns2"))
			{
				_list.add(doc.registerAwtFont(_font, "bmp0xe8", null));
			}
			else
			{
				_list.add(doc.registerAwtFont(_font, "bmp0", null));
			}
		}

		doc.streamBegin("./out/pdf/test-awt-fonts.pdf");

		for(PdfFont _font :  _list)
		{
			System.err.println(_font.getFontName());
			PdfPage _page = doc.newPage();
			doc.newOutline(_font.getFontName(), _page);

			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(true);
			_content.setFont(hf, 20);
			_content.drawString(_font.getFontName(), 30, 820);
			_content.setFont(_font, 30);
			for(int _cp = 0; _cp<256; _cp++)
			{
				_content.drawString(Character.toString((char) (_cp)) ,30+(35 * (_cp % 16)), 800-(45+(45 * (_cp / 16))));
			}
			_font.streamOut();
			_content.streamOut();
			_page.streamOut();
		}

		doc.streamEnd(true);
		System.exit(0);
	}
}
