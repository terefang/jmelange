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

public class PdfOutlineAnchor extends PdfOutline
{
	public PdfOutlineAnchor(PdfDocument doc)
	{
		super(doc);
	}

	public static PdfOutlineAnchor of(PdfDocument doc, PdfOutline pdfOutlines, String text)
	{
		PdfOutlineAnchor _outline = new PdfOutlineAnchor(doc);
		_outline.setParent(pdfOutlines);
		_outline.setTitle(text);
		return _outline;
	}

	public static PdfOutlineAnchor ofUtf(PdfDocument doc, PdfOutline pdfOutlines, String text)
	{
		PdfOutlineAnchor _outline = new PdfOutlineAnchor(doc);
		_outline.setParent(pdfOutlines);
		_outline.setTitleUtf(text);
		return _outline;
	}

}
