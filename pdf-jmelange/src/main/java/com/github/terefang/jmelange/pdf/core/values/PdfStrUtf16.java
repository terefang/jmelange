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

public class PdfStrUtf16 extends AbstractPdfValue
{
	private char[] value;

	public static PdfStrUtf16 of(char[] name)
	{
		return new PdfStrUtf16(name);
	}
	public static PdfStrUtf16 of(String name)
	{
		return new PdfStrUtf16(name.toCharArray());
	}

	public static PdfStrUtf16 create()
	{
		return new PdfStrUtf16();
	}

	public PdfStrUtf16()
	{
	}

	public PdfStrUtf16(char[] name)
	{
		this.value = name;
	}
	
	public char[] getValue()
	{
		return value;
	}
	
	public void setValue(char[] name)
	{
		this.value = name;
	}
	
	public String asString()
	{
		return "<feff"+encodeString(this.value)+"> ";
	}
	
	public static String encodeString(char[] v)
	{
		StringBuilder _sb = new StringBuilder();
		//_sb.append(String.format("%04x", 0xefff));
		for(char _c : v)
		{
			/*
			for(int _b : Character.toString(_c).getBytes(StandardCharsets.UTF_8))
			{
				_sb.append(String.format("%02x", (_b &  0xff)));
			}
			*/
			_sb.append(String.format("%04x", (_c &  0xffff)));
		}
		return _sb.toString();
	}
}
