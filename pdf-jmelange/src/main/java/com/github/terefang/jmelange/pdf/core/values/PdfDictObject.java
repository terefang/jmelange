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
import com.github.terefang.jmelange.pdf.core.PdfValue;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

public class PdfDictObject extends AbstractPdfObject
{
	PdfDict dict = new PdfDict();
	
	public static PdfDictObject create(PdfDocument doc) { return new PdfDictObject(doc); }
	
	public PdfDictObject(PdfDocument doc) { super(doc); }
	
	@Override
	public String asString()
	{
		return this.ref.asString();
	}
	
	public PdfDict set(String key, PdfValue value)
	{
		return dict.set(key, value);
	}
	
	public PdfValue get(String key)
	{
		return this.dict.get(key);
	}
	
	public Collection<PdfValue> values() { return this.dict.values(); }
	
	@Override
	public void writeBodyTo(OutputStream os) throws IOException
	{
		this.dict.writeTo(os);
	}
	
	@Override
	public void invalidateBuffer() throws IOException
	{
	
	}

	public void setName(String _name) {
		dict.setName(_name);
	}

	public void setType(String _type) {
		dict.setType(_type);
	}

	public void setSubtype(String _type) {
		dict.setSubtype(_type);
	}
}
