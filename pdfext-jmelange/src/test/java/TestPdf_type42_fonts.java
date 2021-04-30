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
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.image.PdfImage;
import com.github.terefang.jmelange.commons.loader.*;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import com.github.terefang.jmelange.pdf.ext.PDFX;
import com.github.terefang.jmelange.pdf.ext.PdfExtDocument;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class TestPdf_type42_fonts
{
	public static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse ut volutpat arcu. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Sed sed efficitur lectus, nec ultrices sapien. Pellentesque vulputate rutrum odio eu ullamcorper. Curabitur ut aliquam lacus. Sed a posuere arcu. Aenean pretium condimentum placerat. Integer luctus velit eget arcu imperdiet rutrum quis non nulla. Fusce iaculis urna id urna pulvinar, sit amet tristique odio sagittis. Aenean odio nisl, pellentesque ut rhoncus maximus, tempor sed risus. Cras tellus ipsum, varius id tincidunt et, pellentesque quis mi. Cras tempor fringilla volutpat. Proin tristique arcu justo, et ultrices risus luctus ut. Ut gravida vel quam ut efficitur. Proin eget risus nec dui dapibus pulvinar. Aenean luctus ullamcorper sapien at elementum.";
	public static final String LOREM_IPSUM_EN = "On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammeled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selection: he rejects pleasures to secure other greater pleasures, or else he endures pains to avoid worse pains.";
	public static final String[] _fontlist = {
			PDFX.TEX_GYRE_CURSOR_REGULAR,
			PDFX.TEX_GYRE_CURSOR_BOLD,
			PDFX.TEX_GYRE_CURSOR_ITALIC,
			PDFX.TEX_GYRE_CURSOR_BOLDITALIC,
			PDFX.TEX_GYRE_HEROS_REGULAR,
			PDFX.TEX_GYRE_HEROS_BOLD,
			PDFX.TEX_GYRE_HEROS_ITALIC,
			PDFX.TEX_GYRE_HEROS_BOLDITALIC,
			PDFX.TEX_GYRE_TERMES_REGULAR,
			PDFX.TEX_GYRE_TERMES_BOLD,
			PDFX.TEX_GYRE_TERMES_ITALIC,
			PDFX.TEX_GYRE_TERMES_BOLDITALIC,
	};
	public static void main(String[] args) throws Exception
	{
		PdfExtDocument doc = new PdfExtDocument();
		PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC, false);

		doc.streamBegin("./out/test_type42_fonts.pdf");

		PdfFont hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);

		for(String _f : _fontlist)
		{
			PdfFont _font = _reg.registerFont(doc.registerTtfFont(PDF.ENCODING_PDFDOC, PDF.loadFrom(_f)));
			System.err.println(_font.getFontName());
			PdfPage _page = doc.newPage();
			doc.newOutline(_font.getFontName(), _page);
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(true);
			_content.setFont(hf, 20);
			_content.drawString(_font.getFontName(), 30, 810);

			_content.setFont(_font, 30);
			for(int _cp = 0; _cp<256; _cp++)
			{
				_content.drawString(Character.toString((char) _cp), 30+(35 * (_cp % 16)), 800-(30+(45 * (_cp / 16))));
			}

			_font.streamOut();
			_content.streamOut();
			_page.streamOut();
		}

		doc.streamEnd(true);
	}
}
