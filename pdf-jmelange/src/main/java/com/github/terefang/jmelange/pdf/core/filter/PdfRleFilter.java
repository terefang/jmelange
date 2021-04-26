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
package com.github.terefang.jmelange.pdf.core.filter;

import java.io.*;

public class PdfRleFilter extends PdfFilter
{
	public static PdfRleFilter create() { return new PdfRleFilter(); }

	public PdfRleFilter()
	{
		super("RunLengthDecode");
	}
	
	public byte[] wrap(byte[] content)
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(content);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			this.encode(bais, baos);
			baos.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return baos.toByteArray();
	}


	protected void encode(InputStream rawData, OutputStream encoded) throws IOException
	{
		byte[] _buf = new byte[128];
		int _i = 0;
		while(rawData.available()>0)
		{
			_buf[_i] = (byte) (rawData.read() & 0xff);
			if((_i>1 && _buf[_i]!=_buf[_i-1]) || (_i==127))
			{
				encoded.write((byte) (257-_i));
				encoded.write(_buf, 0, 1);
				_buf[0] = _buf[_i];
				_i=0;

			}
			else
			if((_i>0 && _buf[_i]!=_buf[_i-1]))
			{
				encoded.write((byte) (_i-1));
				encoded.write(_buf, 0, _i);
				_buf[0] = _buf[_i];
				_i=0;
			}
			_i++;
		}
		// flush last block
		encoded.write((byte) (_i-1));
		encoded.write(_buf, 0, _i);
		//End-of-Data
		encoded.write((byte) 128);
		encoded.flush();
	}

}
