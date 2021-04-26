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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;

public class PdfFlateFilter extends PdfFilter
{
	public static PdfFlateFilter create() { return new PdfFlateFilter(); }
	
	public PdfFlateFilter()
	{
		super("FlateDecode");
	}
	
	public byte[] wrap(byte[] content)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DeflaterOutputStream dos = new DeflaterOutputStream(baos);
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
