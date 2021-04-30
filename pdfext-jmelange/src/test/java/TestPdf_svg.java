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
import com.github.terefang.jmelange.commons.loader.*;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.values.PdfFormXObject;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import com.github.terefang.jmelange.pdf.ext.PdfExtDocument;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TestPdf_svg
{
	public static void main(String[] args) throws Exception
	{
		try
		{
			main_svg(args);
		}
		catch(Exception _xe)
		{
			System.err.println("ERR: "+_xe.getMessage());
		}
		System.exit(0);
	}

	public static void main_svg(String[] args) throws Exception
	{
		//List<String> _list = scandirs("**/*.svg", "/home/fredo/IdeaProjects/pdf-gen/docs/svg");
		ZipFile _zip = new ZipFile("./res/svg.zip");
		List<String> _list = scanzips(".svg", _zip);
		Collections.sort(_list);
		PdfExtDocument doc = new PdfExtDocument();
		doc.streamBegin("./out/test-svg.pdf");
		PdfFont hf = doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);
		for(String _svgfile : _list)
		{
			try
			{
				System.err.println(_svgfile);

				List<PdfFormXObject> _forms = new Vector<>();
				PdfPage _page = doc.newPage();
				_page.setMediabox(0, 0, 595, 842);
				doc.newOutline(FileUtils.basename(_svgfile, ".svg"), _page);
				PdfContent _content = _page.newContent(true);
				_content.setFont(hf,20);
				_content.drawString(FileUtils.basename(_svgfile, ".svg"), 50, 810);
				PdfImage _svg = doc.registerRenderedSvgImage(ZipResourceLoader.of(_zip, _zip.getEntry(_svgfile)), 100, 100, 1, true, false, 0f, "false", 0);
				_content.save();
				_content.matrix(_svg.getWidth()/5,0,0,_svg.getHeight()/5,30,30);
				_content.image(_svg);
				_content.restore();
				_content.setFont(hf,20);
				_content.drawString(FileUtils.basename(_svgfile, ".svg"), 50, 810);
				_page.streamOut();
			}
			catch(Exception _xe)
			{
				System.err.println("ERR: "+_svgfile+" "+_xe.getMessage());
			}
		}
		doc.streamEnd(true);
		_zip.close();
	}

	private static List<String> scanzips(String _suf, ZipFile _zip)
	{
		List<String> _ret = new Vector<>();
		Enumeration<? extends ZipEntry> en = _zip.entries();
		while(en.hasMoreElements())
		{
			String _f = en.nextElement().getName();
			if(_f.endsWith(_suf))
			{
				_ret.add(_f);
			}
		}
		return _ret;
	}

	public static List<String> scandirs(String _include, String... dirs) throws Exception
	{
		List<String> _ret = new Vector();
		for(String _dir : dirs)
		{
			DirectoryScanner _scanner = new DirectoryScanner();
			_scanner.setBasedir(_dir);
			_scanner.setCaseSensitive(true);
			_scanner.setIncludes(new String[]{_include});
			_scanner.scan();
			
			for(String includedFile : _scanner.getIncludedFiles())
			{
				_ret.add(new File(_scanner.getBasedir(), includedFile).getAbsolutePath());
			}
		}
		return _ret;
	}

}
