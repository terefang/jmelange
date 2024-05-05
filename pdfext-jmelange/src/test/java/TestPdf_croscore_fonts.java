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
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import com.github.terefang.jmelange.pdf.ext.PdfExtDocument;
import com.github.terefang.jmelange.pdf.ext.fonts.PdfOtuFont;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class TestPdf_croscore_fonts
{
	public static void main(String[] args) throws Exception
	{
		PdfExtDocument doc = new PdfExtDocument();

		doc.streamBegin("./out/pdf/test-croscore.pdf");

		PdfFont _hf = doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);
		PdfFont _uni = doc.registerOtuFont("/home/fredo/IdeaProjects/pdf-gen/res/fonts/unifont/unifont-12.1.04.ttf");

		for(String _fn : scandirs("**/*.ttf,**/*.otf", "./res/fonts/croscore"))
		{

			PdfFont _cf = doc.registerOtuFont(_fn, PDF.ENCODING_PDFDOC);

			System.err.println(_fn);
			try
			{
				PdfPage _page = null;
				PdfContent _content = null;
				for(int _bmp = 0; _bmp<256; _bmp++)
				{
					if(_cf.hasCoverage(_bmp))
					{
						_page = doc.newPage();
						_page.setMediabox(0,0,750,842);
						_page.setRotate(0);
						_content = _page.newContent(true);
						_content.startLayer(_cf.getFontName()+" | bmp="+_bmp);

						_content.fillColor("#000000");
						_content.setFont(_hf, 15);
						_content.drawString(_cf.getFontName()+" | bmp="+_bmp, 30, 820);
						doc.newOutline(_cf.getFontName()+" | bmp="+_bmp, _page);
						_content.setFont(_hf, 9);
						_content.drawString(new File(_fn).getName(), 30, 810);
						for(int _cp = 0; _cp<256; _cp++)
						{
							_content.fillColor("#000000");
							_content.setFont(_cf, 30);
							_content.drawString(Character.toString((char) (_cp +(_bmp<<8))), 30+(45 * (_cp % 16)), 800-(30+(45 * (_cp / 16))));
							_content.setFont(_hf, 7);
							_content.drawString(String.format("%04X", (_cp +(_bmp<<8))), 30+(45 * (_cp % 16)), 800-(45+(45 * (_cp / 16))));
							_content.fillColor("#ff0000");
							_content.setFont(_uni, 10);
							_content.drawString(Character.toString((char) (_cp +(_bmp<<8))), 50+(45 * (_cp % 16)), 800-(45+(45 * (_cp / 16))));
						}
						_content.endLayer();

						_page.streamOut();
					}
				}
			}
			catch(Exception _xe)
			{
				_xe.printStackTrace();
			}
		}
		doc.streamEnd(true);
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
