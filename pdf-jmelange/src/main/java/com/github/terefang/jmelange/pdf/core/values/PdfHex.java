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

public class PdfHex extends AbstractPdfValue
{
	private byte[] value;

	public static PdfHex of(byte[] name)
	{
		return new PdfHex(name);
	}

	public static PdfHex create()
	{
		return new PdfHex();
	}

	public PdfHex()
	{
	}

	public PdfHex(byte[] name)
	{
		this.value = name;
	}
	
	public byte[] getValue()
	{
		return value;
	}
	
	public void setValue(byte[] name)
	{
		this.value = name;
	}
	
	public String asString()
	{
		return "<"+encodeString(this.value)+"> ";
	}
	
	public static String encodeString(byte[] v)
	{
		StringBuilder _sb = new StringBuilder();
		for(byte _c : v)
		{
			_sb.append(String.format("%02x", (int)(_c & 0xff)));
		}
		return _sb.toString();
	}
}
