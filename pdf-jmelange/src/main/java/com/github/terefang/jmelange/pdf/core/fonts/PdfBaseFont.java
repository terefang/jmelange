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
package com.github.terefang.jmelange.pdf.core.fonts;

import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.encoding.PdfEncoding;
import com.github.terefang.jmelange.pdf.core.values.*;

public abstract class PdfBaseFont extends PdfFont
{
	public String[] glyphs;

	public PdfBaseFont(PdfDocument doc, String _name)
	{
		super(doc);
		this.setSubtype("Type1");
		this.setFontName(_name);
	}

	public PdfBaseFont(PdfDocument doc, String _cs, String _name, int _first, String[] _glyphs)
	{
		super(doc, _cs, _first, _glyphs);
		this.setSubtype("Type1");
		this.setFontName(_name);
	}

	public PdfBaseFont(PdfDocument doc, String _cs, String _name, int _first, String[] _glyphs, int[] _widths)
	{
		this(doc, _cs, _name, _first, _glyphs);
		this.setFirstChar(_first);
		this.firstChar = _first;
		this.setLastChar(_first+_glyphs.length-1);
		
		PdfArray widths = PdfArray.create();
		for(int _w : _widths)
		{
			widths.add(PdfNum.of(_w));
		}
		this.set("Widths", widths);
		this.widths = _widths;

		if(this.encoding==null)
		{
			this.encoding = PdfEncoding.of(doc, _first, _glyphs);
			this.set("Encoding", this.encoding);
		}

		if(_glyphs!=null)
		{
			this.glyphs = _glyphs;
		}
	}

	public void setLastChar(int i)
	{
		this.set("LastChar", PdfNum.of(i));
	}

	public void setFirstChar(int first)
	{
		this.set("FirstChar",PdfNum.of(first));
	}

	int firstChar = 0;
	int[] widths;
	
	@Override
	public double width(String _text)
	{
		return width(_text, this.isKerning());
	}
	
	@Override
	public double width(String _text, boolean _kern)
	{
		int _advance = 0;
		for(char _c : _text.toCharArray())
		{
			int _e = this.getEncoding().getEncoder().encodeChar(_c);
			if(_e>=this.firstChar && _e<(this.firstChar+this.widths.length))
			{
				_advance += this.widths[_e-this.firstChar];
			}
		}
		
		return _advance/1000d;
	}
}
