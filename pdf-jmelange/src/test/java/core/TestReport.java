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
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;

import java.io.FileReader;

public class TestReport
{
	public static void main(String[] args) throws Exception
	{
		PdfDocument _doc = new PdfDocument();
		FileReader _fh = new FileReader("./examples/ipsum.txt");
		String[] _lines = CommonUtil.toString(_fh).split( "\n");
		CommonUtil.close(_fh);

		_doc.report(_lines);

		PdfFont _font = _doc.registerHelveticaFont(PDF.ENCODING_PDFDOC);
		_doc.report(595, 842, 36, 10, 12, -1, _font, _lines);

		_font = _doc.registerTimesRomanFont(PDF.ENCODING_PDFDOC);
		_doc.report(595, 842, 36, 10, 12, -1, _font, _lines);

		_doc.writeTo("./out/pdf/test-report.pdf");
		System.exit(0);
	}
	
	
}
