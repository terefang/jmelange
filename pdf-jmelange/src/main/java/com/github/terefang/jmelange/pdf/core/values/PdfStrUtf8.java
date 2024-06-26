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

import java.nio.charset.StandardCharsets;

public class PdfStrUtf8 extends AbstractPdfValue
{
	private char[] value;

	public static PdfStrUtf8 of(String name)
	{
		return new PdfStrUtf8(name);
	}
	public static PdfStrUtf8 of(char[] name)
	{
		return new PdfStrUtf8(name);
	}

	public static PdfStrUtf8 create()
	{
		return new PdfStrUtf8();
	}

	public PdfStrUtf8()
	{
	}

	public PdfStrUtf8(String name)
	{
		this.value = name.toCharArray();
	}
	public PdfStrUtf8(char[] name)
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
		return "<efbbbf"+encodeString(this.value)+"> ";
	}
	
	public static String encodeString(char[] v)
	{
		StringBuilder _sb = new StringBuilder();
		for(char _c : v)
		{
			for(int _b : Character.toString(_c).getBytes(StandardCharsets.UTF_8))
			{
				_sb.append(String.format("%02x", (_b &  0xff)));
			}
		}
		return _sb.toString();
	}
}
