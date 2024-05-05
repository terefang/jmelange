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
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;

import java.util.List;
import java.util.Vector;

public class TestPdf_base14_param
{
	public static void main(String[] args) throws Exception
	{
		main_1(args);
		//main_2(args);
		System.exit(0);
	}

	public static int _PXY = 2000;
	public static int _POF = 50;
	public static int _FSZ = 300;

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
			_page.setMediabox(0,0,_PXY,_PXY);
			PdfContent _content = _page.newContent(true);
			_content.setFont(hf, 20);
			_content.drawString(_font.getFontName(), _POF, _PXY-_POF);

			_content.setFont(_font, _FSZ);
			int _y = _POF;
			drawBaseLines(_page, _content, _y, _font);
			_content.drawString("QWERTZASFGHYqertzipadfghjklyb", _POF, _y); _y+=_FSZ+_POF+_POF;
			drawBaseLines(_page, _content, _y, _font);
			_content.drawString("ASFGHYqertzipadfghjklyb", _POF, _y); _y+=_FSZ+_POF+_POF;
			drawBaseLines(_page, _content, _y, _font);
			_content.drawString("qertzipadfghjklyb", _POF, _y); _y+=_FSZ+_POF+_POF;
			drawBaseLines(_page, _content, _y, _font);
			_content.drawString("padfghjklyb", _POF, _y); _y+=_FSZ+_POF+_POF;
			drawBaseLines(_page, _content, _y, _font);
			_content.drawString("jklyb", _POF, _y);
		}

		_doc.writeTo("./out/pdf/test-base14-param.pdf");
	}

	public static void drawBaseLines(PdfPage _page, PdfContent _content, int _y, PdfFont _font)
	{
		_content.save();
		_content.linedash(2, 1);
		_content.strokeColor("#ff0000");
		_content.moveTo(0, _y);
		_content.lineTo(_PXY, _y);
		_content.moveTo(0, _y+_FSZ);
		_content.lineTo(_PXY, _y+_FSZ);
		_content.stroke();

		_content.linedash(5,3);
		_content.strokeColor("#0000ff");
		int _yy = (int) (_y + _font.getFontAscent()*_FSZ/1000f);
		_content.moveTo(0, _yy);
		_content.lineTo(_PXY, _yy);
		_yy = (int) (_y + _font.getFontDescent()*_FSZ/1000f);
		_content.moveTo(0, _yy);
		_content.lineTo(_PXY, _yy);
		_content.stroke();

		_content.linedash(10,5);
		_content.strokeColor("#008800");
		_yy = (int) (_y + _font.getFontXHeight()*_FSZ/1000f);
		_content.moveTo(0, _yy);
		_content.lineTo(_PXY, _yy);
		_yy = (int) (_y + _font.getFontCapHeight()*_FSZ/1000f);
		_content.moveTo(0, _yy);
		_content.lineTo(_PXY, _yy);
		_content.stroke();

		_content.stroke();
		_content.restore();
	}

}
