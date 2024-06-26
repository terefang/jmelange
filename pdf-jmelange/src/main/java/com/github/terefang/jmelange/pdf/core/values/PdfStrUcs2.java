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

public class PdfStrUcs2 extends AbstractPdfValue
{
	private char[] value;

	public static PdfStrUcs2 of(String name)
	{
		return of(name.toCharArray());
	}
	public static PdfStrUcs2 of(char[] name)
	{
		return new PdfStrUcs2(name);
	}

	public static PdfStrUcs2 create()
	{
		return new PdfStrUcs2();
	}
	
	public PdfStrUcs2()
	{
	}
	
	public PdfStrUcs2(char[] name)
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
		for(char _c : v)
		{
			_sb.append(String.format("%04x", (_c &  0xffff)));
		}
		return _sb.toString();
	}
}
