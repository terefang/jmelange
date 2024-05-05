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
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;

public class TestPaper
{
	public static void main(String[] args) throws Exception
	{
		main_paper(args);
	}
	
	public static void main_paper(String[] args) throws Exception
	{
		PdfDocument doc = new PdfDocument();
		
		PdfFont _hf = doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);
		
		{
			PdfPage _page = _page = doc.newPage();
			_page.setMediabox(0, 0, 595, 842);
			PdfContent _content = _page.newContent(false);
			_content.setFont(_hf, 15);
			_content.save();
			_content.linewidth(2);
			_content.rectanglexy(30, 30, 595 - 30, 842 - 30);
			_content.stroke();
			_content.restore();
			_content.save();
			_content.linewidth(0.5f);
			_content.linedash(5, 5);
			for(int i = 72 + 30; i < (595 - 30); i += 72)
			{
				_content.moveTo(i, 30);
				_content.lineTo(i, 842 - 30);
			}
			for(int i = 72 + 30; i < (842 - 30); i += 72)
			{
				_content.moveTo(30, i);
				_content.lineTo(595 - 30, i);
			}
			_content.stroke();
			_content.restore();
		}
		
		{
			PdfPage _page = _page = doc.newPage();
			_page.setMediabox(0, 0, 595, 842);
			PdfContent _content = _page.newContent(false);
			_content.setFont(_hf, 15);
			_content.save();
			_content.linewidth(2);
			_content.rectanglexy(30, 30, 595 - 30, 842 - 30);
			_content.stroke();
			_content.restore();
			_content.save();
			_content.linewidth(0.5f);
			_content.linedashWithOffset(3, 6, 6);
			_content.rectanglexy(30, 30, 595 - 30, 842 - 30);
			_content.clip();
			_content.endpath();
			
			for(int i = 30; i < 595-30; i += 36)
			{
				for(int j = 30; j < 842-30; j += 36)
				{
					_content.moveTo(i+36, j);
					_content.lineTo(i+36, j+36);
				}
			}
			for(int j = 30; j < 842-30; j += 36)
			{
				for(int i = 30; i < 595-30; i += 36)
				{
					_content.moveTo(i, j+36);
					_content.lineTo(i+36, j+36);
				}
			}
			_content.stroke();
			_content.restore();
		}
		doc.writeTo("./out/pdf/test-paper.pdf");
		System.exit(0);
	}
	
	
}
