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
package com.github.terefang.jmelange.pdf.core.filter;

import com.github.terefang.jmelange.pdf.core.values.PdfName;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

public class PdfHexFilter extends PdfFilter
{
	public static PdfHexFilter create() { return new PdfHexFilter(); }
	public int pairs = 32;
	public static final byte[] HEX = { '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F' };
	
	public PdfHexFilter()
	{
		super(PdfName.ASCIIHEXDECODE);
		this.add(PdfName.of(PdfName.FLATEDECODE));
	}
	
	public byte[] wrap(byte[] content)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream _hout = new OutputStream() {
			int _x = 0;
			@Override
			public void write(int b) throws IOException {
				_x++;
				baos.write((byte) HEX[((b>>4) & 0xf)]);
				baos.write((byte) HEX[(b & 0xf)]);
				if(_x%32 == 0)
				{
					baos.write((byte) '\n');
				}
			}
		};
		DeflaterOutputStream dos = new DeflaterOutputStream(_hout);
		try
		{
			dos.write(content);
			dos.flush();
			dos.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
}
