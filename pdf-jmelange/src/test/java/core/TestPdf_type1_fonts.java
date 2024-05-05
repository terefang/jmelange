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
import org.codehaus.plexus.util.DirectoryScanner;

import java.awt.*;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class TestPdf_type1_fonts
{
	public static void main(String[] args) throws Exception
	{
		main_t1_fonts(args);
		System.exit(0);
	}
	
	public static void main_t1_fonts(String[] args) throws Exception
	{
		try
		{
			PdfDocument doc = new PdfDocument();
			doc.setAllT3(false);
			PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC);
			PdfFont _hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);
			doc.streamBegin("./out/pdf/test-type1-fonts.pdf");
			for(String _pfbfont : scandirs("**/*.pfb","/usr/share/fonts"))
			{
				String _afmfont = _pfbfont.substring(0, _pfbfont.length()-4)+".afm";
				if(new File(_afmfont).exists())
				{
					try
					{
						System.err.println(_pfbfont);

						PdfFont _pf = doc.registerT1Font(PDF.ENCODING_PDFDOC, _pfbfont, _afmfont);

						PdfPage _page = doc.newPage();
						doc.newOutline(_pf.getFontName(), _page);
						_page.setMediabox(0,0,595,842);
						PdfContent _content = _page.newContent(true);
						_content.setFont(_hf, 20);
						_content.drawString(_pf.getFontName(), 30, 820);
						_content.setFont(_hf, 10);
						_content.drawString(_pf.getFontName(), 30, 800);

						_content.setFont(_pf, 30);
						for(int _cp = 0; _cp<256; _cp++)
						{
							_content.drawString(Character.toString((char) _cp), 30 + (35 * (_cp % 16)), 800-(30+(45 * (_cp / 16))));
						}
						_page.streamOut();

					}
					catch(Exception _xe)
					{
						_xe.printStackTrace();
					}
				}
			}
			System.err.println("writing flush ...");
			doc.streamEnd(true);
		}
		catch(RuntimeException _xe)
		{
			_xe.printStackTrace();
		}
		catch(Exception _xe)
		{
			_xe.printStackTrace();
		}
	}

	public static List<String> scandirs(String _match, String... dirs) throws Exception
	{
		List<String> _ret = new Vector();
		for(String _dir : dirs)
		{
			DirectoryScanner _scanner = new DirectoryScanner();
			_scanner.setBasedir(_dir);
			_scanner.setCaseSensitive(true);
			_scanner.setIncludes(new String[]{ _match});
			_scanner.scan();

			for(String includedFile : _scanner.getIncludedFiles())
			{
				_ret.add(new File(_scanner.getBasedir(), includedFile).getAbsolutePath());
			}
		}
		_ret.sort((a,b) -> { return a.compareToIgnoreCase(b); });
		return _ret;
	}
}
