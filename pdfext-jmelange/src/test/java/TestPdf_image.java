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
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;

public class TestPdf_image
{
	public static void main(String[] args) throws Exception
	{
		String _cs = PDF.ENCODING_PDFDOC;

		PdfDocument doc = new PdfDocument();
		doc.setJpegCompression(0.2f);
		PdfImage pi = doc.registerImage("/data/fredo/wappen/IMG_0250_2.png", "dct-grey", false, false, 0f, 0);

		PdfFont _hf = doc.registerHelveticaBoldFont(_cs);

		doc.streamBegin("./out/test-image-xo.pdf");

		PdfPage page = doc.newPage();
		PdfContent content = page.newContent(true);
		content.setFont(_hf, 50);
		content.drawString("Image XO", 30, 800);
		content.image(pi, 0, 0, 595, 700);
		page.streamOut();

		doc.streamEnd(true);
	}
}
