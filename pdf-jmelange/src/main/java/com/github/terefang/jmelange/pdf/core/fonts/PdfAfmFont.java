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
import com.github.terefang.jmelange.pdf.core.encoding.Encoder;
import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.pdf.core.values.PdfResource;

public class PdfAfmFont extends PdfBaseFont
{
	public static final PdfFontResource createResource(PdfAfmFont _f)
	{
		return PdfFont.createResource(_f, "FA");
	}

	PdfResource _res;
	@Override
	public PdfResource getResource()
	{
		if(_res==null)
		{
			_res = createResource(this);
		}
		return _res;
	}

	AFM afm;

	public PdfAfmFont(PdfDocument doc, AFM _afm, String _cs, String _name, int _first, String[] _glyphs, int[] _widths)
	{
		super(doc, _cs, _name, _first, _glyphs, _widths, false, false);
		//this.setName(this.getResName());
		this.afm = _afm;
		this.setKerning(!_afm.getKmap().isEmpty());
		if(_afm.isSymbol())
		{
			this.setKerning(false);
		}
		if(_afm.isFixed())
		{
			this.setKerning(false);
		}

		this.setFontAscent(_afm.getAscender());
		this.setFontDescent(_afm.getDescender());
		this.setFontXHeight(_afm.getxHeight());
		this.setFontCapHeight(_afm.getCapHeight());
	}

	@Override
	public char glyphToChar(String _name)
	{
		for(int _i = 0; _i<this.glyphs.length; _i++)
		{
			if(_name.equals(this.glyphs[_i]))
			{
				return (char) AFM.getUnicode(_name);
			}
		}
		return '?';
	}

	public static PdfFont of(PdfDocument doc, AFM _afm, String _cs)
	{
		return new PdfAfmFont(doc, _afm, _afm.isSymbol() ? null : _cs, _afm.getFontName(), _afm.getFirstChar(), _afm.getGlyphNames(_cs), _afm.getWidths(_cs));
	}

	public static PdfFont fromType(PdfDocument doc, AFM _afm, String _cs, String _type)
	{
		PdfFont _font = PdfAfmFont.of(doc, _afm, _cs);
		_font.setSubtype(_type);
		return _font;
	}

	@Override
	public double width(String _text, boolean _kern)
	{
		int _advance = (int) (super.width(_text, false) * 1000);
		int _last = -1;
		
		if(_kern)
		{
			for(char _c : _text.toCharArray())
			{
				int _e = this.getEncoding().getEncoder().encodeChar(_c);
				if(_e>=this.firstChar && _e<(this.firstChar+this.widths.length))
				{
					if(_last>=0)
					{
						String _n1 =this.glyphs[_last-this.firstChar];
						String _n2 =this.glyphs[_e-this.firstChar];
						if(this.afm.getKmap().containsKey(_n1+":"+_n2))
						{
							_advance += this.afm.getKmap().get(_n1+":"+_n2);
						}
					}
				}
				_last = _e;
			}
		}
		
		return _advance/1000d;
	}

	@Override
	public String encodeToString(String sequence, double wordSpace, double charSpace)
	{
		return this.encodeToStringKerned(sequence, wordSpace, charSpace);
	}
	
	@Override
	public String encodeToStringKerned(String sequence, double wordSpace, double charSpace)
	{
		StringBuilder sb = new StringBuilder();
		int _last = -1;

		Encoder _encoder = this.getEncoding().getEncoder();
		for(char _c : sequence.toCharArray())
		{
			int _e = 0;
			if(_encoder!=null)
			{
				_e = _encoder.encodeChar(_c);
			}
			else
			{

			}

			if(_e>=this.firstChar && _e<(this.firstChar+this.widths.length))
			{
				if(_last>=this.firstChar && this.isKerning())
				{
					String _n1 =this.glyphs[_last-this.firstChar];
					String _n2 =this.glyphs[_e-this.firstChar];
					if(this.afm.getKmap().containsKey(_n1+":"+_n2))
					{
						sb.append((-this.afm.getKmap().get(_n1+":"+_n2))+" ");
					}
				}
				sb.append(this.getEncoding().encode(_c, wordSpace, charSpace)+" ");
			}
			else
			{
				sb.append(this.getEncoding().encode((char) this.firstChar, wordSpace, charSpace)+" ");
			}
			_last = _e;
		}
		return sb.toString();
	}

}
