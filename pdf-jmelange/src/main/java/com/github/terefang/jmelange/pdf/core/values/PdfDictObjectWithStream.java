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

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.loader.*;
import com.github.terefang.jmelange.pdf.core.filter.*;
import lombok.SneakyThrows;

import java.io.*;

public class PdfDictObjectWithStream extends PdfDictObject
{
	private PrintWriter writer;

	public static PdfDictObjectWithStream create(PdfDocument doc) { return new PdfDictObjectWithStream(doc); }
	
	public PdfDictObjectWithStream(PdfDocument doc)
	{
		super(doc);
	}
	
	public static PdfDictObjectWithStream create(PdfDocument doc, boolean _flate)
	{
		PdfDictObjectWithStream _o = create(doc);
		if(_flate)
		{
			_o.setFlateFilter();
		}
		return _o;
	}

	public void setLength(int _l)
	{
		this.set("Length", PdfNum.of(_l));
	}

	@SneakyThrows
	public void putStream(File _file)
	{
		this.putStream(FileResourceLoader.of(_file));
	}

	@SneakyThrows
	public void putStream(PdfResourceLoader _rl)
	{
		CommonUtil.copy(_rl.getInputStream(), this.getOutputStream());
	}

	@SneakyThrows
	public void putStream(InputStream _data)
	{
		CommonUtil.copy(_data, this.getOutputStream());
	}

	@SneakyThrows
	public void putStream(byte[] _data)
	{
		CommonUtil.copy(_data, this.getOutputStream());
	}

	public boolean hasStream()
	{
		return this.buf!=null;
	}
	
	PdfFilter filter;

	public void setHexFilter()
	{
		this.setFilter(PdfHexFilter.create());
	}

	public void setRleFilter()
	{
		this.setFilter(PdfRleFilter.create());
	}

	public void setFilter(PdfFilter filter)
	{
		this.filter = filter;
	}

	public void setFlateFilter()
	{
		this.filter = PdfFlateFilter.create();
	}
	
	public boolean hasFilter()
	{
		return this.filter!=null;
	}
	
	public PdfFilter getFilter()
	{
		return this.filter;
	}
	
	public byte[] getStream()
	{
		return this.buf.toByteArray();
	}
	
	ByteArrayOutputStream buf;
	PrintStream print;
	
	public ByteArrayOutputStream getBuf()
	{
		return buf;
	}
	
	@Override
	public void invalidateBuffer() throws IOException
	{
		if(this.buf!=null)
		{
			ByteArrayOutputStream _buf = this.buf;
			buf=null;
		}
	}
	
	public OutputStream getOutputStream()
	{
		if(this.buf==null) this.buf = new ByteArrayOutputStream();
		return this.buf;
	}

	public PrintStream getPrintStream()
	{
		if(this.print==null) this.print = new PrintStream(this.getOutputStream());
		return this.print;
	}

	public PrintWriter getWriter()
	{
		if(this.writer==null) this.writer = new PrintWriter(this.getOutputStream());
		return this.writer;
	}

	public void writeStreamTo(OutputStream os) throws IOException
	{
		byte[] buf = this.getStream();
		os.write("\nstream\n".getBytes());
		os.write(buf);
		os.write("\nendstream\n".getBytes());
	}
	
	@Override
	public void writeBodyTo(OutputStream os) throws IOException
	{
		if(this.hasStream())
		{
			byte[] _buf = this.getStream();
			if(this.hasFilter())
			{
				this.set("Filter", this.getFilter());
				this.set("X_Size", PdfString.of(""+(_buf.length)));
				_buf = this.getFilter().wrap(_buf);
				this.buf = new ByteArrayOutputStream();
				this.buf.write(_buf);
			}
			
			this.setLength(_buf.length);
			super.writeBodyTo(os);
			writeStreamTo(os);
		}
		else
		{
			super.writeBodyTo(os);
		}
	}

	@Override
	public void streamOut() throws IOException
	{
		if(this.writer!=null) this.writer.flush();
		if(this.print!=null) this.print.flush();
		super.streamOut();
	}


}
