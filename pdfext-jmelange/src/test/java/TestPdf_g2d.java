/*
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
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.g2d.PdfGraphics2D;
import com.github.terefang.jmelange.pdf.core.loader.*;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import com.github.terefang.jmelange.pdf.ext.PdfExtDocument;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class TestPdf_g2d
{
	public static void main(String[] args) throws Exception
	{
		System.setProperty("java.awt.headless","true");

		PdfExtDocument doc = new PdfExtDocument();
		PdfFont _hf = doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);

		GraphicsEnvironment _env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		List<Font> _list = new LinkedList<>();
		int i=0;
		for(Font _f : _env.getAllFonts())
		{
			_list.add(_f);
			//System.err.println(_f.getFontName());
			//System.err.println(_f.getName());
			//System.err.println(_f.getPSName());
			//if(i++>90) break;
		}

		doc.streamBegin("./out/test-g2d.pdf");
		for(Font _font : _list)
		{
			System.err.println(_font);
			PdfPage _page = doc.newPage();
			doc.newOutline(_font.getPSName(), _page);
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(true);
			_content.setFont(_hf, 10);
			_content.drawString(_font.getPSName()+" | "+_font.getName()+" | "+_font.getFontName(), 30, 820);
			PdfGraphics2D _g2d = PdfGraphics2D.from(_page);
			_g2d.setFont(_font.deriveFont(30f));
			for(int _cp = 0; _cp<256; _cp++)
			{
				_g2d.drawString(Character.toString((char) _cp), 30+(35 * (_cp % 16)), 60+(45 * (_cp / 16)));
			}
			_g2d.dispose();
			_page.streamOut();
		}
		doc.streamEnd(true);
		System.exit(0);
	}
}
