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
package com.github.terefang.jmelange.pdf.core.fonts;

import com.github.terefang.jmelange.commons.loader.ResourceLoader;
import com.github.terefang.jmelange.commons.util.GuidUtil;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.encoding.IdentityAndMappedGlyphEncoder;
import com.github.terefang.jmelange.pdf.core.encoding.SfontlyTtfGlyphEncoder;
import com.github.terefang.jmelange.fonts.*;
import com.github.terefang.jmelange.pdf.core.values.*;
import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.FontFactory;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.core.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PdfOtxFont extends PdfType0Font
{
	private PdfDictObjectWithStream _map;

	public static final PdfFontResource createResource(PdfOtxFont _f)
	{
		return PdfFont.createResource(_f, _f.trueTypefont.hasTable(Tag.CFF) ? "FOT" : "FTT");
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

	private final int[] widths;
	Font trueTypefont;
	PdfFontFileStream _fs;
	PdfFontDescriptor _des;

	Map<String, Integer> nameToChar = new HashMap<>();

	public PdfOtxFont(PdfDocument doc, Font _font, ResourceLoader _fontfile, String _cs) throws Exception
	{
		super(doc, SfntUtil.isCFF(_font) ? new SfontlyTtfGlyphEncoder(_font, false) : new IdentityAndMappedGlyphEncoder(), true, SfntUtil.isCFF(_font));
		this.trueTypefont = _font;

		boolean _isPS = SfntUtil.isCFF(_font);
		String _name = SfntUtil.findName(_font, NameTable.NameId.FullFontName.value());

		if(_name==null) _name = _name.replaceAll("\\s+", "");

		if(_name==null) _name = _fontfile.getName();

		//_name = _name.replaceAll("[^a-zA-Z0-9]", "-");
		if(doc.isObfuscate())
		{
			_name = GuidUtil.toHashGUID(_name).substring(0,8);
		}

		this.setName(this.getResource().getResName());
		this.setFontName(makeFontSubsetTag(this.getRef().getValue(), this.getResource().getResPrefix(), _name));

		if(this._touni!=null) this._touni.set("X_OtxFontName", PdfString.of(_name));

 		PdfDictObject _desc = this.getDescender();

		if(_isPS)
		{
			_desc.setSubtype("CIDFontType0");
		}
		else
		{
			_desc.setSubtype("CIDFontType2");
		}
		_desc.set("BaseFont", PdfName.of(this.getFontName()));
		PdfArray _width = PdfArray.create();
		int emUnit = SfntUtil.getUnitsPerEm(_font);
		int numGlyphs = SfntUtil.getNumGlyphs(_font);

		CMap _cmap = SfntUtil.findCMap(_font, false);
		//PostScriptTable _post = (PostScriptTable)_font.getTable(Tag.post);
		if(_cmap!=null && !_isPS)
		{
			byte[] _buf = new byte[0x20000];
			for(int _u = 0; _u<0x10000; _u++)
			{
				int _g = _cmap.glyphId(_u);
				_buf[_u<<1] = (byte) ((_g>>>8) & 0xff);
				_buf[(_u<<1)+1] = (byte) (_g & 0xff);

				if(_g>0) this.setCoverage((_u>>8) & 0xff);
				//String _gname = _post.glyphName(_g);
				//System.err.println(_gname);
			}

			_map = new PdfDictObjectWithStream(this.getDoc());
			_map.setFlateFilter();
			_map.putStream(_buf);
			_map.streamOut();
			_desc.set("CIDToGIDMap", _map);

			if(_cs != null)
			{
				Character[] _uni = AFM.getUnicodeBase(_cs);
				for(int i=0;i<256; i++) {
					if(_uni[i]!=null)
					{
						((IdentityAndMappedGlyphEncoder)this.getEncoder())
								.addMappedGlyph(Character.valueOf((char) i), (int) _uni[i].charValue());
					}
				}
			}
		}
		else
		if(_cmap!=null && _isPS)
		{
			for(int _u = 0; _u<0x10000; _u++)
			{
				int _g = _cmap.glyphId(_u);
				if(_g>0) this.setCoverage((_u>>8) & 0xff);
				//String _gname = _post.glyphName(_g);
				//System.err.println(_gname);
			}
			_desc.set("CIDToGIDMap", PdfName.of("Identity"));
			if(_cs!=null)
			{
				((SfontlyTtfGlyphEncoder)this.getEncoder()).addMapping(_cs);
			}
		}
		else
		{
			_desc.set("CIDToGIDMap", PdfName.of("Identity"));
		}

		// TODO
		//if(this.getEncoder()!=null) this.mapToUnicode(this.getEncoder());
		if(_cmap!=null)
		{
			this.nameToChar.putAll(SfntUtil.getGlyphNames(_font, _cmap, _cs));
		}

		this.widths = new int[0x10000];

		_width.add(PdfNum.of(0));
		PdfArray _widths = PdfArray.create();

		int lastChar = -2;

		HorizontalMetricsTable _hmtx = (HorizontalMetricsTable)_font.getTable(Tag.hmtx);

		if(_cmap!=null && !_isPS)
		{
			for(int i=0; i<0x10000; i++)
			{
				int _g = _cmap.glyphId(i);
				int _w = _hmtx.advanceWidth(_g);
				_w = _w * 1000 / emUnit;
				this.widths[i] = _w;
				if(_w>0)
				{
					if(i!=0 && ((_widths.size()==256) || (lastChar != i-1)))
					{
						_width.add(_widths);
						_width.add(PdfNum.of(i));
						_widths = PdfArray.create();
					}
					_widths.add(PdfNum.of(_w));
					lastChar=i;
				}
			}
		}
		else
		{
			for(int i=0; i<=numGlyphs; i++)
			{
				int _w = _hmtx.advanceWidth(i);
				_w = _w * 1000 / emUnit;
				this.widths[i] = _w;
				if(_w>0)
				{
					if(i!=0 && ((_widths.size()==256) || (lastChar != i-1)))
					{
						_width.add(_widths);
						_width.add(PdfNum.of(i));
						_widths = PdfArray.create();
					}
					_widths.add(PdfNum.of(_w));
					lastChar=i;
				}
			}
		}
		_width.add(_widths);

		_desc.set("W", _width);
		_desc.set("DW", PdfNum.of(0));
		// _desc.set("DW", PdfNum.of(300); // missing width
		_des = PdfFontDescriptor.create(doc);
		_des.setFontBBox(SfntUtil.getFontBBox(_font));
		_desc.set("FontDescriptor", _des);
		_fs = PdfFontFileStream.create(doc);

		if(_isPS)
		{
			_des.setFontFile3(_fs);
			_fs.putStream(_fontfile);
			_fs.setSubtype("OpenType");
		}
		else
		{
			_des.setFontFile2(_fs);
			_fs.putStream(_fontfile);
			_fs.setLength1(_fs.getBuf().toByteArray().length);
		}

		_fs.set("X_OtuFontName", PdfString.of(_name));
		_des.setFontName(_name);
		_des.setFontFamily(_name);

		_des.setFontStretch(SfntUtil.getFontStretch(_font));
		int _cH = SfntUtil.getCapHeight(_font, emUnit, true);
		int _xH = SfntUtil.getXHeight(_font, emUnit, true);

		_des.setCapHeight(_cH);
		this.setFontCapHeight(_cH);
		this.setFontXHeight(_xH);
		_des.setFlags(0 /*1<<5*/);
		_des.setStemV(0);
		_des.setItalicAngle(0);
		this.setFontAscent(SfntUtil.getAscender(_font, emUnit));
		this.setFontDescent(SfntUtil.getDescender(_font, emUnit));
		_des.setAscent((int) this.getFontAscent());
		_des.setDescent((int) this.getFontDescent());
	}

	public static PdfFont of(PdfDocument doc, ResourceLoader _fontfile, String _cs) throws Exception
	{
		InputStream _stream = _fontfile.getInputStream();
		FontFactory _sffactory = FontFactory.getInstance();
		com.google.typography.font.sfntly.Font[] _sfonts = _sffactory.loadFonts(_stream);
		com.google.typography.font.sfntly.Font _sfont = _sfonts[0];
		return new PdfOtxFont(doc, _sfont, _fontfile, _cs);
	}
	
	@Override
	public double width(String _text)
	{
		return width(_text, this.isKerning());
	}
	
	public int _width(String _text)
	{
		int _advance = 0;
		for(char _c : _text.toCharArray())
		{
			int _g = this.getEncoder().getGlyphId(_c);
			_advance += this.widths[_g];
		}
		return _advance;
	}
	
	@Override
	public double width(String _text, boolean _kern)
	{
		int _advance = _width(_text);

		return _advance/1000d;
	}

	Map<String,Integer> gidToName =new HashMap<>();

	@Override
	public char glyphToChar(String _name)
	{
		if(this.nameToChar.containsKey(_name))
		{
			return (char)this.nameToChar.get(_name).intValue();
		}
		if(this.nameToChar.containsKey(_name.toLowerCase()))
		{
			return (char)this.nameToChar.get(_name.toLowerCase()).intValue();
		}
		return '?';
	}

	@Override
	public void streamOut(boolean _res) throws IOException
	{
		if(_res)
		{
			if(this._map!=null) this._map.streamOut();
			if(this._fs!=null) this._fs.streamOut();
			if(this._des!=null) this._des.streamOut();
			this.getDescender().streamOut();
		}
		super.streamOut(_res);
	}

	public Font getTrueTypefont() {
		return this.trueTypefont;
	}
}
