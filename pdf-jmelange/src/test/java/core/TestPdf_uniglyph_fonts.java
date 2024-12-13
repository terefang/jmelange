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

import com.github.terefang.jmelange.fonts.sfnt.SfntUtil;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.Font;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class TestPdf_uniglyph_fonts
{
	public static final String[] _fontvars = { "adf","croscore","dejavu", "fira","lange","urw","texgyre" };
	public static final String _fontbase = "res/fonts/";

	public static void main(String[] args) throws Exception
	{
		for(String _var :_fontvars)
		{
			PdfDocument doc = new PdfDocument();

			doc.streamBegin("./out/pdf/test-"+_var+"-fonts.pdf");

			PdfFont _hf = doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);
			PdfFont _uni = doc.registerOtxFont(null, "res/fonts/unifont/unifont-12.1.04.ttf");


			for (String _fn : scandirs("**/*.ttf,**/*.otf", _fontbase+_var)) {
				System.err.println(_fn);
				try {
					Font _sfont = SfntUtil.loadFonts(_fn)[0];
					char[] _chars = SfntUtil.findGlyphCharacters(_sfont);
					PdfPage _page = null;
					PdfContent _content = null;
					PdfFont _cf = doc.registerOtxFont(PDF.loadFrom(_fn));
					for (int _i = 0; _i < _chars.length; _i += 256) {
						if (_i % 256 == 0) {
							_page = doc.newPage();
							_page.setMediabox(0, 0, 750, 842);
							_content = _page.newContent(true);
							_content.setFont(_hf, 15);
							_content.drawString(_cf.getFontName(), 30, 820);
							doc.newOutline(_cf.getFontName() + " | " + _i, _page);
							_content.setFont(_hf, 9);
							_content.drawString(new File(_fn).getName(), 30, 810);
						}

						for (int _cp = 0; _cp < 256 && ((_i + _cp) < _chars.length); _cp++) {
							_content.fillColor("#000000");
							_content.setFont(_cf, 30);
							_content.drawString(Character.toString(_chars[_cp + (_i)]), 30 + (45 * (_cp % 16)), 800 - (30 + (45 * (_cp / 16))));
							_content.fillColor("#ff0000");
							_content.setFont(_uni, 10);
							_content.drawString(String.format("%04X %s", (int) _chars[_cp + (_i)], Character.toString(_chars[_cp + (_i)])), 30 + (45 * (_cp % 16)), 800 - (45 + (45 * (_cp / 16))));
						}
						_page.streamOut();
					}
				} catch (Exception _xe) {
					_xe.printStackTrace();
				}
			}
			doc.streamEnd(true);
		}
		System.exit(0);
	}

	public static List<String> scandirs(String _inc, String... dirs) throws Exception
	{
		List<String> _ret = new Vector();
		for(String _dir : dirs)
		{
			DirectoryScanner _scanner = new DirectoryScanner();
			_scanner.setBasedir(_dir);
			_scanner.setCaseSensitive(true);
			_scanner.setIncludes(_inc.split(","));
			_scanner.scan();

			for(String includedFile : _scanner.getIncludedFiles())
			{
				_ret.add(new File(_scanner.getBasedir(), includedFile).getAbsolutePath());
			}
		}
		_ret.sort(new Comparator<String>()
		{
			@Override
			public int compare(String o1, String o2)
			{
				File f1 = new File(o1);
				File f2 = new File(o2);
				return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
			}
		});
		return _ret;
	}
}
