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

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.ListMapUtil;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestPdf_afm_fonts
{
	public static final String[] _FONTLIST = {
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreAdventor-BoldItalic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreAdventor-Bold",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreAdventor-Italic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreAdventor-Regular",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreBonum-BoldItalic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreBonum-Bold",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreBonum-Italic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreBonum-Regular",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreChorus-Medium",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreCursor-BoldItalic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreCursor-Bold",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreCursor-Italic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreCursor-Regular",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreHeros-BoldItalic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreHeros-Bold",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreHerosCondensed-BoldItalic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreHerosCondensed-Bold",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreHerosCondensed-Italic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreHerosCondensed-Regular",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreHeros-Italic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreHeros-Regular",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyrePagella-BoldItalic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyrePagella-Bold",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyrePagella-Italic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyrePagella-Regular",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreSchola-BoldItalic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreSchola-Bold",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreSchola-Italic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreSchola-Regular",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreTermes-BoldItalic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreTermes-Bold",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreTermes-Italic",
			"/mnt/nas/fredos/_fontz/_free/TeXGyre/TeXGyreTermes-Regular",

		//	"/mnt/nas/fredos/_fontz/_domain/ms/Andale-Mono-Regular",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Arial-Black-Regular",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Arial-Bold-Italic",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Arial-Bold",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Arial-Italic",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Comic-Sans-MS-Bold",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Comic-Sans-MS-Regular",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Georgia-Bold-Italic",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Georgia-Bold",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Georgia-Italic",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Georgia-Regular",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Impact-Regular",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Segoe-UI-Bold",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Segoe-UI-Regular",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Tahoma-Bold",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Tahoma-Regular",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Times-New-Roman-Bold-Italic",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Times-New-Roman-Bold",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Times-New-Roman-Italic",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Times-New-Roman-Regular",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Trebuchet-MS-Bold-Italic",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Trebuchet-MS-Bold",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Trebuchet-MS-Italic",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Trebuchet-MS-Regular",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Webdings-Regular",
		//	"/mnt/nas/fredos/_fontz/_domain/ms/Wingdings-Regular",
	};
	public static void main(String[] args) throws Exception
	{
		fakeUnembedded(false,_FONTLIST, "Type1","./out/pdf/texg-unembed-t1-fonts.pdf");
		fakeUnembedded(false,_FONTLIST, "TrueType","./out/pdf/texg-unembed-tt-fonts.pdf");
		fakeUnembedded(true,_FONTLIST, "svg","./out/pdf/texg-embed-t3-fonts.pdf");
		fakeUnembedded(true,_FONTLIST, "ttf","./out/pdf/texg-embed-ttf-fonts.pdf");
		fakeUnembedded(true,_FONTLIST, "otf","./out/pdf/texg-embed-otf-fonts.pdf");
	}
	
	@SneakyThrows
	public static void fakeUnembedded(boolean _embed, String[] _filelist, String _type, String _out)
	{
		PdfDocument _doc = new PdfDocument();
		PdfFontRegistry _reg = PdfFontRegistry.of(_doc);
		
		PdfFont hf = _doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC, false);
		for(String path : _filelist)
		{
			System.err.println(path.toString());
			
			PdfFont _font = null;
			if(_embed && "svg".equalsIgnoreCase(_type))
			{
				_font = _doc.registerSvgFont(PDF.ENCODING_PDFDOC, path+".svg", null);
			}
			else
			if(_embed && "ttf".equalsIgnoreCase(_type))
			{
				_font = _doc.registerTtfFont(PDF.ENCODING_PDFDOC, path+".otf");
			}
			else
			if(_embed && "otf".equalsIgnoreCase(_type))
			{
				_font = _doc.registerOtxFont(PDF.ENCODING_PDFDOC, path+".otf");
			}
			else
			{
				_font = _doc.registerAfmFont(path+".afm", PDF.ENCODING_PDFDOC);
				_font.setSubtype(_type);
			}
			PdfPage _page = _doc.newPage();
			_doc.newOutline(_font.getFontName(), _page);
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
		_doc.writeTo(_out);
	}
}
