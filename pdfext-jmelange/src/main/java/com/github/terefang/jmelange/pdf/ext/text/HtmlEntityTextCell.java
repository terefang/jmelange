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
package com.github.terefang.jmelange.pdf.ext.text;

import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.values.PdfName;
import jhyphenator.Hyphenator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@With
@Builder
@Slf4j
public class HtmlEntityTextCell implements Cell
{
	String entity;
	Character character;
	boolean spaceBefore = false;
	String textFace;
	String fontFace;
	String fontWeight="*";
	String fontStyle="*";
	int fontSize;
	String link;

	@Override
	public boolean canHypenate(PdfFontRegistry reg, boolean _firstInLine, double _limit, double _hscale, Hyphenator _hy)
	{
		return false;
	}

	@Override
	public Cell[] hypenate(PdfFontRegistry reg, boolean _firstInLine, double _limit, double _hscale, Hyphenator _hy) {
		throw new IllegalArgumentException();
	}

	public List<Cell> breakup(PdfFontRegistry _r, double _limit, double _hscale)
	{
		return (List)Collections.singletonList(this);
	}
	
	@Override
	public double width(PdfFontRegistry _r, boolean _firstInLine)
	{
		return width(_r, 100f, _firstInLine);
	}

	public double width(PdfFontRegistry _r, double _hscale, boolean _firstInLine)
	{
		PdfFont _font = _r.lookupFont(this.getFontFace());
		PdfFont _textfont = _r.lookupFont(this.getTextFace());
		if(_font!=null)
		{
			String _char = this.character.toString();
			return (_font.width(_char)+(this.numberOfSpaces(_firstInLine)*_textfont.width(" ")))*this.fontSize*_hscale/100f;
		}
		return 0;
	}
	
	@Override
	public String render(PdfFontRegistry _r, PdfContent _cnt, boolean _firstInLine, double _ident, double _wordspace, double _charspace, double _hscale)
	{
		StringBuilder _sb = new StringBuilder();
		PdfFont _font = _r.lookupFont(this.getFontFace());
		if(_font!=null)
		{
			_cnt.hscale(_hscale);
			if(!_firstInLine && this.isSpaceBefore())
			{
				PdfFont _textfont = _r.lookupFont(this.getTextFace());
				_cnt.addFont(_textfont);
				_sb.append(PdfName.of(_textfont.getResource().getResName()).asString()+" "+PDF.transformDP(this.fontSize)+" Tf ");
				_sb.append(" [ "+PDF.transformDP(-_ident*((100f/_hscale)*(1000d/this.fontSize))));
				_sb.append(_textfont.encodeToString(" ",_wordspace*(1000d/this.fontSize), _charspace*(1000d/this.fontSize)));
				_sb.append(" ] TJ ");
			}

			_cnt.addFont(_font);
			_sb.append(PdfName.of(_font.getResource().getResName()).asString()+" "+PDF.transformDP(this.fontSize)+" Tf ");
			_sb.append(" [ ");
			if(!(!_firstInLine && this.isSpaceBefore()))
			{
				_sb.append(PDF.transformDP(-_ident*(100f/_hscale)*(1000d/this.fontSize)));
			}
			_sb.append(_font.encodeToString(this.getCharacter().toString(), _wordspace*(1000d/this.fontSize), _charspace*(1000d/this.fontSize)));
			_sb.append(" ] TJ ");
		}
		else
		{
			log.warn("font lookup failed: "+this.fontFace);
		}
		return _sb.toString();
	}
	
	@Override
	public int numberOfSpaces(boolean _firstInLine)
	{
		return (_firstInLine ? 0 : (this.spaceBefore ? 1 : 0));
	}
	
	@Override
	public int numberOfChars(boolean _firstInLine)
	{
		return (_firstInLine ? 1 : (this.spaceBefore ? 2 : 1));
	}
}
