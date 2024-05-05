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

import com.github.terefang.jmelange.commons.util.ListMapUtil;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;

import java.util.List;
import java.util.Vector;

public class TestPdf_base14_fonts_synth
{
	public static void main(String[] args) throws Exception
	{
		main_1(args);
		System.exit(0);
	}

	public static void main_1(String[] args) throws Exception {
		PdfDocument _doc = new PdfDocument();
		PdfFontRegistry _reg = PdfFontRegistry.of(_doc);

		_doc.registerBase14Fonts(_reg, PDF.ENCODING_PDFDOC);

		PdfFont hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);

		List<PdfFont> _list = new Vector(_reg.allFonts());
		for (String _opt : ListMapUtil.toList("bold", "oblique", "slant", "condensed", "expanded"))
			for (PdfFont _font : _list) {
				PdfPage _page = _doc.newPage();
				_page.setMediabox(0, 0, 595, 842);
				PdfContent _content = _page.newContent(true);
				PdfFont _sfont = _reg.syntheticFont(_font.getFontName() + "-" + _opt, _font, "pdfdoc", new String[]{_opt});
				_content.startLayer(_sfont.getFontName());
				_content.setFont(hf, 20);
				_content.drawString(_sfont.getFontName(), 30, 820);

				_content.setFont(_sfont, 30);
				for (int _cp = 0; _cp < 256; _cp++) {
					_content.drawString(Character.toString((char) _cp), 30 + (35 * (_cp % 16)), 30 + (45 * (_cp / 16)));
				}
				_content.endLayer();
			}

		_doc.writeTo("./out/pdf/test-base14-fonts-synth.pdf");
	}

}
