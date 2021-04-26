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
package com.github.terefang.jmelange.pdf.ext.text;

import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.values.PdfName;
import jhyphenator.HyphenationPattern;
import jhyphenator.Hyphenator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

@Getter
@Setter
@With
@Builder
public class TextCell implements Cell
{
	String text;
	boolean spaceBefore = false;
	String fontFace;
	int fontSize;
	private List<String> _hyres;
	String link;
	@Override
	public Cell[] hypenate(PdfFontRegistry reg, boolean _firstInLine, double _limit, double _hscale, Hyphenator _hy)
	{
		if(this._hyres==null)
		{
			throw new IllegalArgumentException();
		}

		Cell[] _ret = new Cell[2];
		StringBuilder _sb = new StringBuilder();
		for(int _i=1; _i<this._hyres.size(); _i++)
		{
			_sb.append(this._hyres.get(_i));
		}

		_ret[0] = TextCell.builder()
				.text(this._hyres.get(0)+"-")
				.spaceBefore(this.spaceBefore)
				.fontFace(this.fontFace)
				.fontSize(this.fontSize)
				.build();
		_ret[1] = TextCell.builder()
				.text(_sb.toString())
				.spaceBefore(true)
				.fontFace(this.fontFace)
				.fontSize(this.fontSize)
				.build();
		return _ret;
	}

	@Override
	public boolean canHypenate(PdfFontRegistry _r, boolean _firstInLine, double _hscale, double _limit, Hyphenator _hy)
	{
		if(_hy==null)
		{
			_hy = Hyphenator.getInstance(HyphenationPattern.EN_US);
		}

		if(this._hyres==null)
		{
			this._hyres = _hy.hyphenate(this.text);
		}

		if(this._hyres.size()>1)
		{
			double _cw = _width(this._hyres.get(0), _r.lookupFont(this.getFontFace()), this.getFontSize(), _hscale, true, this.spaceBefore);
			if(_limit > _cw)
			{
				return true;
			}
		}
		return false;
	}

	public List<Cell> breakup(PdfFontRegistry _r, double _limit, double _hscale)
	{
		PdfFont _font = _r.lookupFont(this.getFontFace());
		List<TextCell> _ret = new Vector();
		
		double _cw = this.width(_r, true);
		int _off = this.text.length();
		
		if(_cw<=_limit)
		{
			return (List)Collections.singletonList(this);
		}
		
		if(_limit>0)
		{
			while(_cw>_limit && _off>=0)
			{
				int _off1 = this.text.lastIndexOf(' ', _off-1);
				int _off2 = this.text.lastIndexOf('\n', _off-1);
				if(_off1<_off2)
				{
					_off=_off1;
				}
				else
				{
					_off=_off2;
				}
				_cw = PDF.advancewidth(this.text.substring(0, _off),_font, this.getFontSize(), 0, 0, _hscale);
			}
			
			if(_off>0)
			{
				_ret.add(TextCell.builder()
						.text(this.text.substring(0, _off))
						.spaceBefore(this.spaceBefore)
						.fontFace(this.fontFace)
						.fontSize(this.fontSize)
						.build());
				_ret.add(TextCell.builder()
						.text(this.text.substring(_off+1))
						.spaceBefore(true)
						.fontFace(this.fontFace)
						.fontSize(this.fontSize)
						.build());
				return (List)_ret;
			}
		}
		
		TextCell _last = null;
		boolean _first = true;
		for(String _part : this.text.split("\\s+"))
		{
			_last = TextCell.builder()
					.text(_part)
					.spaceBefore(_first ? this.spaceBefore : true)
					.fontFace(this.fontFace)
					.fontSize(this.fontSize)
					.build();
			_ret.add(_last);
			_first = false;
		}
		return (List)_ret;
	}

	public double width(PdfFontRegistry _r, boolean _firstInLine)
	{
		return width(_r, 100f, _firstInLine);
	}

	public double width(PdfFontRegistry _r, double _hscale, boolean _firstInLine)
	{
		PdfFont _font = _r.lookupFont(this.getFontFace());
		return _width(this.getText(), _font, this.getFontSize(), _hscale, _firstInLine, this.spaceBefore);
	}

	public static double _width(String _text, PdfFont _font, int _fontSize, double _hscale, boolean _firstInLine, boolean _spaceBefore)
	{
		double _ret = 0.3 * _fontSize;
		if(_font!=null)
		{
			_ret = PDF.advancewidth(_text, _font, _fontSize, 0, 0, _hscale);
			if((!_firstInLine) && _spaceBefore)
			{
				_ret += PDF.advancewidth(" ", _font, _fontSize, 0, 0, _hscale);
			}
		}
		return _ret;
	}

	public String render(PdfFontRegistry _r, PdfContent _cnt, boolean _firstInLine, double _ident, double _wordspace, double _charspace, double _hscale)
	{
		StringBuilder _sb = new StringBuilder();
		PdfFont _font = _r.lookupFont(this.getFontFace());
		if(_font!=null)
		{
			_cnt.hscale(_hscale);
			_cnt.addFont(_font);
			_sb.append(PdfName.of(_font.getResource().getResName()).asString()+" "+PDF.transformDP(this.fontSize)+" Tf ");
			_sb.append(" [ ");
			if(_ident!=0d)
			{
				_sb.append(PDF.transformDP(-_ident*((100f/_hscale)*(1000d/this.fontSize))));
			}
			String _x = _font.encodeToString(((!_firstInLine) && this.spaceBefore ? " " : "")+this.getText(), _wordspace*(1000d/this.fontSize), _charspace*(1000d/this.fontSize));
			_sb.append(_x);
			_sb.append(" ] TJ ");
		}
		return _sb.toString();
	}
	
	public int numberOfSpaces(boolean _firstInLine)
	{
		int _ret = PDF.countSpaces(this.text);
		if(!_firstInLine)
		{
			_ret += (this.spaceBefore ? 1: 0);
		}
		return _ret;
	}

	public int numberOfChars(boolean _firstInLine)
	{
		int _ret = this.text.length();
		if(!_firstInLine)
		{
			_ret += (this.spaceBefore ? 1: 0);
		}
		return _ret;
	}
}
