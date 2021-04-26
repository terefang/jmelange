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
import com.github.terefang.jmelange.pdf.core.PdfValue;

import java.io.IOException;
import java.io.OutputStream;

public class PdfArrayObject extends AbstractPdfObject
{
	PdfArray _array = new PdfArray();
	
	public static PdfArrayObject create(PdfDocument doc) { return new PdfArrayObject(doc); }
	
	public PdfArrayObject(PdfDocument doc) { super(doc); }
	
	public int size() { return _array.size(); }
	
	@Override
	public String asString()
	{
		return this.ref.asString();
	}

	public PdfArrayObject add(PdfValue value)
	{
		_array.add(value);
		return this;
	}

	public PdfArrayObject prepend(PdfValue value)
	{
		_array.prepend(value);
		return this;
	}
	
	@Override
	public void writeBodyTo(OutputStream os) throws IOException
	{
		this._array.writeTo(os);
	}
	
	@Override
	public void invalidateBuffer() throws IOException
	{
	
	}
	
}
