/*
 * Copyright (c) 2019. terefang@gmail.com
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
package com.github.terefang.jmelange.pdf.core.values;

import com.github.terefang.jmelange.pdf.core.PdfDocument;

import java.io.IOException;
import java.io.OutputStream;

public class PdfCatalog extends PdfDictObject
{
	public static PdfCatalog create(PdfDocument doc)
	{
		return new PdfCatalog(doc);
	}
	
	public PdfCatalog(PdfDocument doc)
	{
		super(doc);
		this.set("Type", PdfName.of("Catalog"));
	}
	
	@Override
	public void writeTo(OutputStream os) throws IOException
	{
		/*
		PdfDictObjectWithStream _meta = PdfDictObjectWithStream.create(this.getDoc(), false);
		this.set("Metadata", _meta);
		_meta.set("Type", PdfName.of("Metadata"));
		_meta.set("Subtype", PdfName.of("XML"));
		IOUtil.copy(ClasspathResourceLoader.of("com/github/terefang/pdfgen/xmp/pdfx.xmp").getInputStream(), _meta.getOutputStream());
		*/
		super.writeTo(os);
	}
}
