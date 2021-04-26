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

public class PdfName extends AbstractPdfValue
{
	private String name;
	
	public static PdfName of(String name)
	{
		return new PdfName(name);
	}
	
	public static PdfName create()
	{
		return new PdfName();
	}
	
	public PdfName()
	{
	}
	
	public PdfName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String asString()
	{
		StringBuilder _sb = new StringBuilder();
		_sb.append("/");
		for(char _c : name.toCharArray())
		{
			if(_c=='#')
			{
				_sb.append("#23");
			}
			else
			if(_c>='!' && _c<='~')
			{
				_sb.append(_c);
			}
			else
			if(_c>0xff)
			{
				_sb.append("#20");
			}
			else
			{
				_sb.append(String.format("#%02X", (int)_c));
			}
		}
		return _sb.toString();
	}
	
}
