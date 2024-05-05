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

import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;

import java.util.List;
import java.util.Vector;

public class TestPdf_base14_fonts
{
	public static void main(String[] args) throws Exception
	{
		main_1(args);
		main_2(args);
		System.exit(0);
	}

	public static void main_1(String[] args) throws Exception
	{
		PdfDocument _doc = new PdfDocument();
		PdfFontRegistry _reg = PdfFontRegistry.of(_doc);

		_doc.registerBase14Fonts(_reg, PDF.ENCODING_PDFDOC);

		PdfFont hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);

		List<PdfFont> _list = new Vector(_reg.allFonts());
		for(PdfFont _font : _list)
		{
			PdfPage _page = _doc.newPage();
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(true);
			_content.startLayer(_font.getFontName());
			_content.setFont(hf, 20);
			_content.drawString(_font.getFontName(), 30, 820);

			_content.setFont(_font, 30);
			for(int _cp = 0; _cp<256; _cp++)
			{
				_content.drawString(Character.toString((char) _cp), 30+(35 * (_cp % 16)), 30+(45 * (_cp / 16)));
			}
			_content.endLayer();
		}

		_doc.writeTo("./out/pdf/test-base14-fonts.pdf");
	}

	public static void main_2(String[] args) throws Exception
	{
		PdfDocument _doc = new PdfDocument();
		PdfFontRegistry _reg = PdfFontRegistry.of(_doc);

		PdfFont hf = _doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);

		String [] _encodings = {"adobe-standard","cp1250", "cp1251", "cp1252", "cp1253", "cp1254", "cp1255", "cp1256", "cp1257", "cp1258",
				"hp-roman8", "ibm437", "ibm850", "ibm851", "ibm852", "ibm855", "ibm857",
				"iso-8859-1", "iso-8859-2", "iso-8859-3", "iso-8859-4", "iso-8859-5", "iso-8859-6", "iso-8859-7", "iso-8859-8", "iso-8859-9", "iso-8859-13", "iso-8859-15",
				"koi8-r", "koi8-ru", "koi8-u",
				"macintosh", "pdfdoc", "texnansi", "text"};

		String[] _b14fonts = {
			PDF.FONT_COURIER,
			PDF.FONT_COURIER_BOLD,
			PDF.FONT_COURIER_OBLIQUE,
			PDF.FONT_COURIER_BOLD_OBLIQUE,
			PDF.FONT_HELVETICA,
			PDF.FONT_HELVETICA_BOLD,
			PDF.FONT_HELVETICA_OBLIQUE,
			PDF.FONT_HELVETICA_BOLD_OBLIQUE,
			PDF.FONT_TIMES,
			PDF.FONT_TIMES_BOLD,
			PDF.FONT_TIMES_ITALIC,
			PDF.FONT_TIMES_BOLD_ITALIC,
			PDF.FONT_ZAPFDINGBATS,
			PDF.FONT_SYMBOL
		};

		for(String _fnt : _b14fonts)
		{
			for(String _encode : _encodings)
			{
				PdfFont _xf = _doc.registerAfmFont(_fnt, _encode);
				PdfPage _page = _doc.newPage();
				_doc.newOutline(_xf.getFontName()+" | "+_encode, _page);
				_page.setMediabox(0,0,595,842);
				PdfContent _content = _page.newContent(true);
				_content.startLayer(_xf.getFontName()+_encode);
				_content.setFont(hf, 20);
				_content.drawString(_xf.getFontName()+" | "+_encode, 30, 820);

				_content.setFont(_xf, 30);
				for(int _cp = 0; _cp<256; _cp++)
				{
					_content.drawString(Character.toString((char) _cp), 30+(35 * (_cp % 16)), 30+(45 * (_cp / 16)));
				}
				_content.endLayer();
			}
		}

		_doc.writeTo("./out/pdf/test-base14-encoding.pdf");
	}

}
