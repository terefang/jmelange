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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfHexFilter extends PdfFilter
{
	public static PdfHexFilter create() { return new PdfHexFilter(); }
	public int pairs = 16;
	public static final byte[] HEX = { '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F' };
	
	public PdfHexFilter()
	{
		super("ASCIIHexDecode");
	}
	
	public byte[] wrap(byte[] content)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			for(int i=0; i < content.length; i++)
			{
				if(i%pairs == 0)
				{
					baos.write('\n');
				}
				baos.write((byte) HEX[((content[i]>>4) & 0xf)]);
				baos.write((byte) HEX[(content[i] & 0xf)]);
			}
			baos.flush();
			baos.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
}
