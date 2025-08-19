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
import lombok.SneakyThrows;

import java.io.File;

public class TestPdf_unembedded_fonts
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
	public static final String[] _AFMLIST = {
		"/mnt/nas/fredos/_fontz/_adobe/afm/Courier",
		"/mnt/nas/fredos/_fontz/_adobe/afm/Courier-Bold",
		"/mnt/nas/fredos/_fontz/_adobe/afm/Courier-BoldOblique",
		"/mnt/nas/fredos/_fontz/_adobe/afm/Courier-Oblique",
		"/mnt/nas/fredos/_fontz/_adobe/afm/Helvetica",
		"/mnt/nas/fredos/_fontz/_adobe/afm/Helvetica-Bold",
		"/mnt/nas/fredos/_fontz/_adobe/afm/Helvetica-BoldOblique",
		"/mnt/nas/fredos/_fontz/_adobe/afm/Helvetica-Oblique",
		"/mnt/nas/fredos/_fontz/_adobe/afm/Symbol",
		"/mnt/nas/fredos/_fontz/_adobe/afm/Times-Bold",
		"/mnt/nas/fredos/_fontz/_adobe/afm/Times-BoldItalic",
		"/mnt/nas/fredos/_fontz/_adobe/afm/Times-Italic",
		"/mnt/nas/fredos/_fontz/_adobe/afm/Times-Roman",
		"/mnt/nas/fredos/_fontz/_adobe/afm/ZapfDingbats",
	};
	
	public static void main(String[] args) throws Exception
	{
		fakeUnembedded(false,_AFMLIST, "Type1","./out/pdf/base14-unembed-t1-");
		fakeUnembedded(false,_AFMLIST, "TrueType","./out/pdf/base14-unembed-tt-");
		fakeUnembedded(false,_FONTLIST, "Type1","./out/pdf/texg-unembed-t1-");
		fakeUnembedded(false,_FONTLIST, "TrueType","./out/pdf/texg-unembed-tt-");
		fakeUnembedded(true,_FONTLIST, "svg","./out/pdf/texg-embed-t3-");
		fakeUnembedded(true,_FONTLIST, "ttf","./out/pdf/texg-embed-ttf-");
		fakeUnembedded(true,_FONTLIST, "otf","./out/pdf/texg-embed-otf-");
	}
	
	@SneakyThrows
	public static void fakeUnembedded(boolean _embed, String[] _filelist, String _type, String _prefix)
	{
		PdfDocument _doc = new PdfDocument();
		PdfFontRegistry _reg = PdfFontRegistry.of(_doc);
		int _max = (_filelist.length-1) * 20;
		
		PdfPage _page = _doc.newPage();
		_page.setMediabox(0,0,200,_max+20);
		PdfContent _content = _page.newContent(true);

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
			_content.setFont(_font, 20);
			_content.drawString("Hello World!", 0, _max);
			_max-=20;
		}
		_doc.writeTo(_prefix+"fonts.pdf");
	}
}
