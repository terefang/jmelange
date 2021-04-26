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

import com.github.terefang.jmelange.pdf.core.PDF;

public class PdfDocString extends AbstractPdfValue
{
	private String value;

	public static PdfDocString of(String name)
	{
		if(name==null) throw new NullPointerException();
		return new PdfDocString(name);
	}

	public static PdfDocString create()
	{
		return new PdfDocString();
	}

	public PdfDocString()
	{
	}

	public PdfDocString(String name)
	{
		this.value = name;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public void setValue(String name)
	{
		this.value = name;
	}
	
	public String asString()
	{
		return "<"+encodeString(this.value)+"> ";
	}
	
	public static String encodeString(String v)
	{
		if(v==null)
		{
			return "null";
		}
		v = PDF.mapToPdfDocEncoding(v);
		return PDF.makePDFStringHexNoBrackets(v);
	}
}
