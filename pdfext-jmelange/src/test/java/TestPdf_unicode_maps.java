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
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.image.PdfImage;
import com.github.terefang.jmelange.pdf.core.loader.*;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.util.AFM;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import com.github.terefang.jmelange.pdf.ext.PdfExtDocument;
import com.github.terefang.jmelange.pdf.ext.fonts.PdfOtuFont;

import java.util.List;
import java.util.Vector;

public class TestPdf_unicode_maps
{
	static String MAPS = "pdfdoc bmp0 latin-15 cp1250 adobe-standard adobe-symbol adobe-zapf-dingbats cp1251 cp1252 cp1253 cp1254 cp1255 cp1256 cp1257 cp1258 hp-roman8 ibm437 ibm850 ibm851 ibm852 ibm855 ibm857 iso-8859-1 iso-8859-2 iso-8859-3 iso-8859-4 iso-8859-5 iso-8859-6 iso-8859-7 iso-8859-8 iso-8859-9 iso-8859-13 iso-8859-15 macintosh microsoft-dingbats koi8-r koi8-u koi8-ru";

	public static void main(String[] args) throws Exception
	{

		PdfExtDocument doc = new PdfExtDocument();
		PdfFont hf = doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);

		List<PdfFont> _flist = new Vector();

		_flist.add(doc.registerOtuFont(FileResourceLoader.of("res/fonts/unifont/unifont-12.1.04.ttf")));
		_flist.add(doc.registerOtuFont(FileResourceLoader.of("res/fonts/dejavu/DejaVuSans.ttf")));
		PdfOtuFont _ur = (PdfOtuFont) doc.registerUnicodeRegularFont();

		doc.streamBegin("./out/test-unicode-maps.pdf");

		for(PdfFont _uf :  _flist)
		{
			for(String _map :  MAPS.split(" "))
			{
				doPage(doc,hf,_uf,_map);
			}
			_uf.streamOut();
		}

		for(int _i=0; _i<256; _i++)
		{
			if(_ur.hasCoverage(_i))
			{
				doPage(doc, hf, _ur,"bmp"+_i);
			}
		}

		doc.streamEnd(true);
	}

	public static void doPage(PdfExtDocument doc, PdfFont hf, PdfFont _uf, String _map) throws Exception
	{
		PdfPage _page = doc.newPage();
		_page.setMediabox(0,0,595,842);
		PdfContent _content = _page.newContent(true);
		_content.setFont(hf, 20);
		_content.drawString(_map+" -- "+_uf.getFontName(), 30, 820);
		_content.setFont(_uf, 30);
		Character[] _cs = AFM.loadCharset(_map);
		for(int _cp = 0; _cp<256; _cp++)
		{
			_content.drawString(_cs[_cp]!=null ? _cs[_cp].toString() : "" ,30+(35 * (_cp % 16)), 800-(45+(45 * (_cp / 16))));
		}
		_content.streamOut();
		_page.streamOut();
	}
}
