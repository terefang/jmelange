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

import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.PdfResRef;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.encoding.GlyphEncoder;
import com.github.terefang.jmelange.pdf.core.encoding.PdfEncoding;
import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.pdf.core.values.*;

import java.io.IOException;
import java.io.PrintStream;

public abstract class PdfFont extends PdfDictObject implements PdfResRef
{
	public static class PdfFontResource extends PdfResource<PdfFont>
	{
		public PdfFontResource(PdfFont _xo, String _prefix)
		{
			super(_prefix, "Font");
			set(_xo);
			_xo.setName(this.getResName());
		}
	}

	public static final PdfFontResource createResource(PdfFont _f, String _prefix)
	{
		return new PdfFontResource(_f, _prefix);
	}

	public PdfDictObjectWithStream _touni;
	public PdfEncoding encoding;
	boolean  kerning = true;
	String fontName;

	float fontAscent = 800f;
	float fontDescent = -200f;
	float fontCapHeight = 800f;
	float fontXHeight = 400f;

	public float getFontAscent() {
		return fontAscent;
	}

	public void setFontAscent(float fontAscent) {
		this.fontAscent = fontAscent;
	}

	public float getFontDescent() {
		return fontDescent;
	}

	public void setFontDescent(float fontDescent) {
		this.fontDescent = fontDescent;
	}

	public float getFontCapHeight() {
		return fontCapHeight;
	}

	public void setFontCapHeight(float fontCapHeight) {
		this.fontCapHeight = fontCapHeight;
	}

	public float getFontXHeight() {
		return fontXHeight;
	}

	public void setFontXHeight(float fontXHeight) {
		this.fontXHeight = fontXHeight;
	}

	public String getFontName()
	{
		return fontName;
	}
	
	public void setFontName(String fontName)
	{
		this.fontName = fontName;
		this.setBaseFont(fontName);
	}

	public void setBaseFont(String fontName)
	{
		this.set("BaseFont", PdfName.of(fontName));
	}

	public boolean isKerning()
	{
		return kerning;
	}
	
	public void setKerning(boolean kerning)
	{
		this.kerning = kerning;
	}

	public PdfFont(PdfDocument doc, String _cs, int _first, String[] _glyphs)
	{
		super(doc);
		this.setType("Font");
		//this.set("Name", PdfName.of(this.getName()));

		if(_cs!=null)
		{
			PdfEncoding _enc = doc.getEncoding(_cs, _first, _glyphs);
			this.set("Encoding", _enc);
			this.set("X_Charset", PdfString.of(_cs));
			this.encoding = _enc;
		}
	}

	public PdfFont(PdfDocument doc)
	{
		super(doc);
		this.setType("Font");
	}

	public PdfEncoding getEncoding()
	{
		return encoding;
	}
	
	public void setEncoding(PdfEncoding encoding)
	{
		this.encoding = encoding;
		this.set("Encoding", this.encoding);
	}
	
	public String encodeToString(String sequence, double wordSpace, double charSpace)
	{
		return this.encoding.encode(sequence, wordSpace, charSpace);
	}
	
	public String encodeToStringKerned(String sequence, double wordSpace, double charSpace)
	{
		return this.encoding.encode(sequence, wordSpace, charSpace);
	}
	
	public abstract double width(String _text);
	
	public abstract double width(String _text, boolean _kern);

	public byte[] text(String _text, double _indent, double wordSpace, double charSpace)
	{
		StringBuilder _ret = new StringBuilder();
		if(this.isKerning())
		{
			_ret.append("[ ");
			if(_indent!=0)
			{
				_ret.append(PDF.geomDP(-_indent)).append(" ");
			}
			_ret.append(this.encodeToStringKerned(_text, wordSpace, charSpace))
					.append(" ] TJ ");
		}
		else
		{
			if(_indent==0)
			{
				_ret.append("[ ")
						.append(this.encodeToString(_text, wordSpace, charSpace))
						.append(" ] TJ ");
			}
			else
			{
				_ret.append("[ ")
						.append(PDF.geomDP(-_indent))
						.append(" ")
						.append(this.encodeToString(_text, wordSpace, charSpace))
						.append(" ] TJ ");
			}
		}
		return _ret.toString().getBytes();
	}

	public void text(PdfContent _cnt, double _size, String _text, double _indent, double wordSpace, double charSpace)
	{
		_cnt.font(this, _size);
		StringBuilder _ret = new StringBuilder();

		if(this.isKerning())
		{
			_ret.append("[ ");
			if(_indent!=0)
			{
				_ret.append(PDF.geomDP(-_indent)).append(" ");
			}
			_ret.append(this.encodeToStringKerned(_text, wordSpace, charSpace))
					.append(" ] TJ ");
		}
		else
		{
			if(_indent==0)
			{
				_ret.append("[ ")
						.append(this.encodeToString(_text, wordSpace, charSpace))
						.append(" ] TJ ");
			}
			else
			{
				_ret.append("[ ")
						.append(PDF.geomDP(-_indent))
						.append(" ")
						.append(this.encodeToString(_text, wordSpace, charSpace))
						.append(" ] TJ ");
			}
		}
		_cnt.addContentLn(_ret.toString());
	}

	public static String makeFontSubsetTag(int _i, String _prefix, String _font)
	{
		_font = _font.replaceAll("[^a-zA-Z0-9]+", "-");
		String _front = (new StringBuilder(Long.toString(_i + 0xDeadBeefL,36)).reverse().toString()).toUpperCase()+"AQSWDEFRGTH";
		char[] _ca = _front.toCharArray();
		for(int i = 0; i<_front.length(); i++)
		{
			if(_front.charAt(i)<='9')
			{
				_ca[i]+=17;
			}
		}
		_front = new String(_ca).substring(0, 6);
		return _front+"+"+_prefix+"+"+_font;
	}

    public abstract char glyphToChar(String _name);

	PdfFontDescriptor pdfFontDescriptor;
	public void setFontDescriptor(PdfFontDescriptor des) {
		this.pdfFontDescriptor = des;
		this.set("FontDescriptor", des);
	}

	public PdfFontDescriptor getFontDescriptor() {
		return pdfFontDescriptor;
	}

	@Override
	public void streamOut(boolean _res) throws IOException
	{
		if(_res)
		{
			if(this.pdfFontDescriptor!=null)
			{
				this.pdfFontDescriptor.streamOut();
			}
			if(this._touni!=null)
			{
				this._touni.streamOut();
			}
		}
		super.streamOut(_res);
	}

	public PrintStream mapToUnicodeBase()
	{
		_touni = PdfDictObjectWithStream.create(this.getDoc(), true);
		_touni.set("Type", PdfName.of("CMap"));
		_touni.set("CMapName", PdfName.of("Variant-Unicode+"+Integer.toString(this.getRef().getValue(),36)));
		PrintStream _print = _touni.getPrintStream();
		_print.println("%% Custom");
		_print.println("%% CMAP");
		_print.println("%%");
		_print.println("/CIDInit /ProcSet findresource begin");
		_print.println("12 dict begin begincmap");
		_print.println("/CIDSystemInfo <<");
		_print.println("   /Registry (Variant)");
		_print.println("   /Ordering (Unicode)");
		_print.println("   /Supplement "+Integer.toString(this.getRef().getValue()));
		_print.println(">> def");
		_print.println("/CMapName /Variant-Unicode+"+Integer.toString(this.getRef().getValue())+" def");
		return _print;
	}

	public void mapToUnicodeTail(PrintStream _print)
	{
		_print.println("endcmap CMapName currendict /CMap defineresource pop end end");
		PdfDict _info = PdfDict.create();
		_info.set("Registry", PdfString.of("Variant"));
		_info.set("Ordering", PdfString.of("Unicode"));
		_info.set("Supplement", PdfNum.of(this.getRef().getValue()));
		_touni.set("CIDSystemInfo", _info);
		this.set("ToUnicode", _touni);
	}

	public void mapToUnicode(GlyphEncoder _enc)
	{
		PrintStream _print = mapToUnicodeBase();

		_print.println(String.format("1 begincodespacerange <0000> <%04X> endcodespacerange", _enc.getGlyphNum()-1));
		for (int j = 0; j < _enc.getGlyphNum(); j++)
		{
			int i = ((_enc.getGlyphNum()-j) > 100) ? 100 : (_enc.getGlyphNum() - j);
			if (j == 0)
			{
				_print.println(String.format("%d beginbfrange", i));
			}
			else if (j % 100 == 0)
			{
				_print.println("endbfrange");
				_print.println(String.format("%d beginbfrange", i));
			}
			_print.println(String.format("<%04x> <%04x> <%04x>", j,j, _enc.getCode(j)));
			this.setCoverage((_enc.getCode(j))>>>8);
		}
		_print.println("endbfrange");

		this.mapToUnicodeTail(_print);
	}

	public void mapToUnicode(String _cs)
	{
		PrintStream _print = mapToUnicodeBase();

		Character[] _chars = AFM.getUnicodeBase(_cs);
		_print.println(String.format("1 begincodespacerange <0000> <%04X> endcodespacerange", _chars.length-1));
		for (int j = 0; j < _chars.length; j++)
		{
			int i = ((_chars.length-j) > 100) ? 100 : (_chars.length - j);
			if (j == 0)
			{
				_print.println(String.format("%d beginbfrange", i));
			}
			else if (j % 100 == 0)
			{
				_print.println("endbfrange");
				_print.println(String.format("%d beginbfrange", i));
			}
			_print.println(String.format("<%04x> <%04x> <%04x>", j,j, (int)_chars[j].charValue()));
			this.setCoverage(((int)_chars[j].charValue())>>>8);
		}
		_print.println("endbfrange");

		this.mapToUnicodeTail(_print);
	}

	public boolean[] bmp = new boolean[256];
	public boolean hasCoverage(int _bmp) { return _bmp<this.bmp.length ? this.bmp[_bmp] : false; }
	public void setCoverage(int _bmp) { if(_bmp<this.bmp.length) { this.bmp[_bmp]=true;} }

}
