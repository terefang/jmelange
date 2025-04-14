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

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.commons.util.IOUtil;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.PdfResRef;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.encoding.GlyphEncoder;
import com.github.terefang.jmelange.pdf.core.encoding.PdfEncoding;
import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.pdf.core.values.*;
import lombok.SneakyThrows;
import okio.internal._ByteStringKt;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class PdfFont extends PdfDictObject implements PdfResRef
{
	public static class PdfFontResource extends PdfResource<PdfFont>
	{
		private PdfFontResource(PdfFont _xo, String _prefix)
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

	public PdfFont(PdfDocument doc, String _cs, int _first, String[] _glyphs, boolean _otf, boolean _cff)
	{
		super(doc);
		this.setOpentype(_otf);
		this.setCff(_cff);
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

	public static String longToString(long _l)
	{
		_l += 0xDeadBeefL;
		_l &= 0x7fffffffffffffffL;
		StringBuilder _sb = new StringBuilder();
		while(_l>0L)
		{
			_sb.append((char)('A'+(_l%26)));
			_l>>>=1;
		}
		return _sb.toString();
	}

	public static String makeFontSubsetTag(int _i, String _prefix, String _font)
	{
		//_font = CommonUtil.sha1Hex(_font).substring(0,8).toUpperCase();
		StringBuilder _sb = new StringBuilder(_font.length());
		long _hash = 0;
		for(char _c : _font.toCharArray())
		{
			if((_c>='a' && _c<='z')
				|| (_c>='A' && _c<='Z')
				|| (_c>='0' && _c<='9')
				|| (_c=='-')
				|| (_c=='+'))
			{
				_sb.append((char)_c);
			}
			else
			if(Character.isWhitespace(_c) || _c<0x20)
			{
				_hash<<=1;
			}
			else
			{
				_hash=(_hash*37)+((int)_c-0x20);
			}
		}

		if(_hash>0)
		{
			_sb.append("+"+Long.toString(_hash,36).toString());
		}

		String _front = longToString(_i).substring(0,6);
		return _front+"+"+_prefix+"+"+_sb.toString();
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

	public PrintStream mapToUnicodeBase(String _name)
	{
		String _type = "Unicode";
		String _id = Integer.toString(this.getRef().getValue());
		//_touni = PdfDictObjectWithStream.create(this.getDoc(),false);
		_touni = PdfDictObjectWithStream.create(this.getDoc());
		_touni.set("Type", PdfName.of("CMap"));
		_touni.set("CMapName", PdfName.of(_name+"-"+_id+"-"+_type+"-0"));
		PrintStream _print = _touni.getPrintStream();
		_print.println("%!PS-Adobe-3.0 Resource-CMap");
		_print.println("%%DocumentNeededResources: ProcSet (CIDInit)");
		_print.println("%%IncludeResource: ProcSet (CIDInit)");
		_print.println("%%BeginResource: CMap ("+_name+"-"+_id+"-"+_type+"-000)");
		_print.println("%%Title: ("+_name+"-"+_id+"-"+_type+"-000 "+_name+"-"+_id+" "+_type+" 0)");
		_print.println("%%Version: 1.000");
		_print.println("%%Copyright: -----------------------------------------------------------");
		_print.println("%%Copyright: none claimed.");
		_print.println("%%Copyright: -----------------------------------------------------------");
		_print.println("%%EndComments");
		_print.println("/CIDInit /ProcSet findresource begin");
		_print.println("12 dict begin begincmap");
		_print.println("/CIDSystemInfo <<");
		_print.println("   /Registry ("+_name+"-"+_id+")");
		_print.println("   /Ordering ("+_type+")");
		_print.println("   /Supplement 0");
		_print.println(">> def");
		_print.println("/CMapName /"+_name+"-"+_id+"-"+_type+"-0 def");
		return _print;
	}

	public void mapToUnicodeTail(PrintStream _print,String _name)
	{
		String _id = Integer.toString(this.getRef().getValue());
		_print.println("endcmap CMapName currendict /CMap defineresource pop end end");
		PdfDict _info = PdfDict.create();
		_info.set("Registry", PdfString.of(_name+"-"+_id));
		_info.set("Ordering", PdfString.of("Unicode"));
		_info.set("Supplement", PdfNum.of(0));
		_touni.set("CIDSystemInfo", _info);
		this.set("ToUnicode", _touni);
	}

	public void mapToUnicode(GlyphEncoder _enc)
	{
		boolean _set = this.isCoverage();
		PrintStream _print = mapToUnicodeBase(_enc.getName());

		_print.println("1 begincodespacerange <0000> <FFFF> endcodespacerange");

		List<String> _v = new ArrayList<>();
		_v.add(String.format("<%04x> <%04x> <%04x>", 0,0, 0xfffe));

		if(this.isOpentype())
		{
			int _start_g = -2;
			int _start_u = -2;
			int _last_g = -2;
			for (int _j = 1; _j < 0xffff; _j++)
			{
				int _g = _enc.getGlyphId(_j);
				if(!_set && _g > 0) this.setCoverage((_enc.getCode(_g))>>>8);
				
				if(_start_u>0 && _g-1==_last_g)
				{
					_last_g = _g;
					continue;
				}
				
				if(_start_u>0 && _start_g>0)
				{
					if(_start_u>0xffff)
					{
						
						_v.add(String.format("<%04x> <%04x> <%04x%04x>", _start_g,_last_g, (int)Character.highSurrogate(_start_u), (int)Character.lowSurrogate(_start_u)));
					}
					else
					{
						_v.add(String.format("<%04x> <%04x> <%04x>", _start_g,_last_g, _start_u));
					}
				}

				_start_g = _last_g = _g;
				_start_u = _j;

				//if(_g == 0 || _g>_enc.getGlyphNum()) continue;
				
				//_v.add(String.format("<%04x> <%04x> <%04x>", _g,_g, _j));
			}
		}
		else
		{
			for (int j = 1; j < _enc.getGlyphNum(); j++)
			{
				int _c = _enc.getCode(j);
				if(_c>0xffff)
				{
					_v.add(String.format("<%04x> <%04x> <%04x%04x>", j,j, (int)Character.highSurrogate(_c), (int)Character.lowSurrogate(_c)));
				}
				else
				{
					_v.add(String.format("<%04x> <%04x> _v.add(String.format(\"<%04x> <%04x> <%04x>", j,j, _c));
				}
				if(!_set) this.setCoverage(_c>>>8);
			}
		}

		int _l = _v.size();
		int _j = 0;
		for(String _line : _v)
		{
			int _i = ((_l-_j) > 100) ? 100 : (_l - _j);
			if (_j == 0)
			{
				_print.println(String.format("%d beginbfrange", _i));
			}
			else if (_j % 100 == 0)
			{
				_print.println("endbfrange");
				_print.println(String.format("%d beginbfrange", _i));
			}
			_print.println(_line);
			_j++;
		}

		_print.println("endbfrange");

		this.mapToUnicodeTail(_print,_enc.getName());
	}

	public void mapToUnicode(String _cs)
	{
		boolean _set = this.isCoverage();
		ClasspathResourceLoader _rl = ClasspathResourceLoader.of(
				"cp:/cmaps/" + _cs.toLowerCase() + "-unicode-0.cmap", null);
		
		InputStream _in = _rl.getInputStream();
		if(_in!=null)
		{
			mapToUnicodeCmapFile(_cs,_in);
			return;
		}
		
		PrintStream _print = mapToUnicodeBase(_cs);

		Integer[] _chars = AFM.getUnicodeBaseInt(_cs);
		_print.println("1 begincodespacerange <0000> <FFFF> endcodespacerange");
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
			
			if(_chars[j]>0xffff)
			{
				_print.println(String.format("<%04x> <%04x> <%04x%04x>", j,j, (int)Character.highSurrogate(_chars[j]), (int)Character.lowSurrogate(_chars[j])));
			}
			else
			{
				_print.println(String.format("<%04x> <%04x> <%04x>", j,j, _chars[j]));
			}
			if(!_set) this.setCoverage(_chars[j]>>>8);
		}
		_print.println("endbfrange");

		this.mapToUnicodeTail(_print,_cs);
	}
	
	@SneakyThrows
	public void mapToUnicodeCmapFile(String _cs, InputStream _in)
	{
		_touni = PdfDictObjectWithStream.create(this.getDoc());
		_touni.set("Type", PdfName.of("CMap"));
		_touni.set("CMapName", PdfName.of(_cs+"-Unicode-0"));
		PrintStream _print = _touni.getPrintStream();
		_print.print(IOUtil.toString(_in, StandardCharsets.UTF_8));
		PdfDict _info = PdfDict.create();
		_info.set("Registry", PdfString.of(_cs));
		_info.set("Ordering", PdfString.of("Unicode"));
		_info.set("Supplement", PdfNum.of(0));
		_touni.set("CIDSystemInfo", _info);
		this.set("ToUnicode", _touni);
	}
	
	public boolean[] bmp;
	public boolean isCoverage() { return this.bmp!=null; }
	public boolean hasCoverage(int _bmp) { return isCoverage() ? (_bmp<this.bmp.length ? this.bmp[_bmp] : false) : false; }
	public void setCoverage(int _bmp)
	{
		if(this.bmp==null) this.bmp = new boolean[0x2000];
		if(_bmp<this.bmp.length)
		{
			this.bmp[_bmp]=true;
		}
	}

	public int maxCoverage()
	{
		for(int _i = this.bmp.length-1; _i>=0; _i--)
		{
			if(this.bmp[_i]) return _i;
		}
		return 0;
	}

	public boolean opentype = false;
	public boolean cff = false;

	public boolean isOpentype() {
		return opentype;
	}

	public void setOpentype(boolean opentype) {
		this.opentype = opentype;
	}

	public boolean isCff() {
		return cff;
	}

	public void setCff(boolean cff) {
		this.cff = cff;
	}
}
