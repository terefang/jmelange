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
import com.github.terefang.jmelange.pdf.core.PdfResRef;
import com.github.terefang.jmelange.pdf.core.PdfValue;
import com.github.terefang.jmelange.pdf.core.content.AbstractPdfContent;

import java.io.IOException;

public abstract class PdfXObject extends AbstractPdfContent implements PdfResRef
{
	public static class PdfXObjectResource extends PdfResource<PdfXObject>
	{
		public PdfXObjectResource(PdfXObject _xo)
		{
			super("XO", "XObject");
			set(_xo);
			_xo.setName(this.getResName());
		}
	}

	double width;
	double height;

	public PdfXObject(PdfDocument doc, boolean _flate)
	{
		super(doc, _flate);
		this.set("Type", PdfName.of("XObject"));
	}
	
	public void streamOut(boolean _res) throws IOException
	{
		if(_res)
		{
			if(this.fonts!=null)
			{
				for(PdfValue _f : this.fonts.asMap().values())
				{
					if(_f instanceof AbstractPdfObject)
					{
						((AbstractPdfObject)_f).streamOut();
					}
				}
			}
			
			if(this.colorspace!=null)
			{
				for(PdfValue _cs : this.colorspace.asMap().values())
				{
					if(_cs instanceof AbstractPdfObject)
					{
						((AbstractPdfObject)_cs).streamOut();
					}
				}
			}
			
			if(this.xo!=null)
			{
				for(PdfValue _xo : this.xo.asMap().values())
				{
					if(_xo instanceof AbstractPdfObject)
					{
						((AbstractPdfObject)_xo).streamOut();
					}
				}
			}
		}
		super.streamOut(_res);
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
}
