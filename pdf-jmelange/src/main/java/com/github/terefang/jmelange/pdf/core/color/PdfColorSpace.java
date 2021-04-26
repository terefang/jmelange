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
package com.github.terefang.jmelange.pdf.core.color;

import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.PdfResRef;
import com.github.terefang.jmelange.pdf.core.values.PdfResource;

public class PdfColorSpace extends AbstractPdfColorSpace implements PdfResRef
{
	public PdfColorSpace(PdfDocument doc)
	{
		super(doc);
	}

	public static class PdfColorSpaceResource extends PdfResource<PdfColorSpace>
	{
		public PdfColorSpaceResource(PdfColorSpace _xo, String _prefix)
		{
			super(_prefix, "ColorSpace");
			set(_xo);
		}
	}

	public static final PdfResource createResource(PdfColorSpace _f)
	{
		return new PdfColorSpaceResource(_f, "CS");
	}

	PdfResource _res;
	@Override
	public PdfResource getResource()
	{
		if(_res==null)
		{
			_res = createResource(this);
		}
		return _res;
	}
}
