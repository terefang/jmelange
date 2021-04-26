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
package com.github.terefang.jmelange.pdf.core.values;

import com.github.terefang.jmelange.pdf.core.PdfDocument;

public class PdfFormXObject extends PdfXObject
{
	public static class PdfFormXObjectResource extends PdfResource<PdfFormXObject>
	{
		public PdfFormXObjectResource(PdfFormXObject _xo)
		{
			super("XF", "XObject");
			set(_xo);
			_xo.setName(this.getResName());
		}
	}

	public static final PdfFormXObjectResource createResource(PdfFormXObject _xo)
	{
		return new PdfFormXObjectResource(_xo);
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

	public PdfFormXObject(PdfDocument doc, boolean _flate)
	{
		super(doc, _flate);
		this.set("Subtype", PdfName.of("Form"));
	}
	
	public void setFormBBox(int _a, int _b, int _c, int _d)
	{
		this.set("BBox", PdfArray.from(_a, _b, _c, _d));
		this.setWidth(_c);
		this.setHeight(_d);
	}

	public void setFormBBox(double _a, double _b, double _c, double _d)
	{
		this.set("BBox", PdfArray.fromFloat((float) _a, (float)_b, (float)_c, (float)_d));
		this.setWidth(_c);
		this.setHeight(_d);
	}

	public void setFormBBox(float _a, float _b, float _c, float _d)
	{
		this.set("BBox", PdfArray.fromFloat(_a, _b, _c, _d));
		this.setWidth(_c);
		this.setHeight(_d);
	}

	public static final PdfFormXObject create(PdfDocument _doc)
	{
		return new PdfFormXObject(_doc, true);
	}
}
