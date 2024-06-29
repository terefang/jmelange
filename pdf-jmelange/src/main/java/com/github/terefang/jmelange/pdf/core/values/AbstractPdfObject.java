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

public abstract class AbstractPdfObject extends AbstractPdfValue
{
	PdfObjRef ref;
	PdfDocument doc;
	long streamOffset = -1L;
	
	public AbstractPdfObject(PdfDocument doc)
	{
		super();
		this.ref = new PdfObjRef();
		this.doc = doc;
		doc.add(this);
	}
	
	@Override
	public String asString()
	{
		return this.ref.asString();
	}
	
	public PdfObjRef getRef()
	{
		return ref;
	}
	public PdfDocument getDoc()
	{
		return doc;
	}
	
	public abstract void writeBodyTo(OutputStream os) throws IOException;
	
	public void writeHeaderTo(OutputStream os) throws IOException
	{
		os.write((Integer.toString(this.ref.getValue())+" 0 obj\n").getBytes());
	}
	
	public void writeFooterTo(OutputStream os) throws IOException
	{
		os.write("\nendobj\n".getBytes());
	}

	@Override
	public void writeTo(OutputStream os) throws IOException
	{
		//System.err.println(this.asString());
		this.writeHeaderTo(os);
		this.writeBodyTo(os);
		this.writeFooterTo(os);
	}
	
	public long getStreamOffset()
	{
		return streamOffset;
	}
	
	public void setStreamOffset(long streamOffset)
	{
		this.streamOffset = streamOffset;
	}
	
	public abstract void invalidateBuffer() throws IOException;
	
	public void streamOut() throws IOException
	{
		this.streamOut(true);
	}
	
	public void streamOut(boolean _res) throws IOException
	{
		if(this.getStreamOffset() == -1L)
		{
			this.doc.streamOut(this);
			this.invalidateBuffer();
		}
	}
}
