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

import com.github.terefang.jmelange.commons.loader.ByteArrayResourceLoader;
import com.github.terefang.jmelange.commons.loader.ResourceLoader;
import com.github.terefang.jmelange.commons.util.GuidUtil;
import com.github.terefang.jmelange.fonts.sfnt.SfntUtil;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.CMap;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.HorizontalMetricsTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.NameTable;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.encoding.GlyphEncoder;
import com.github.terefang.jmelange.pdf.core.encoding.IdentityAndMappedGlyphEncoder;
import com.github.terefang.jmelange.pdf.core.encoding.SfontlyTtfGlyphEncoder;
import com.github.terefang.jmelange.fonts.*;
import com.github.terefang.jmelange.pdf.core.values.*;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.Font;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.FontFactory;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.Tag;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class PdfOtxFont extends PdfType0Font
{
	private PdfDictObjectWithStream _map;

	public static final PdfFontResource createResource(PdfOtxFont _f)
	{
		return PdfFont.createResource(_f, _f.trueTypefont.hasTable(Tag.CFF) ? "FOT" : "FTT");
	}

	PdfResource _res;
	
	@SneakyThrows
	public static List<PdfFont> fromTtc(PdfDocument _doc, ResourceLoader _rl, String _cs)
	{
		List<PdfFont> _ret = new Vector<>();
		
		InputStream _stream = _rl.getInputStream();
		FontFactory _sffactory = FontFactory.getInstance();
		for(Font _sfont : _sffactory.loadFonts(_stream))
		{
			ByteArrayOutputStream _baos = new ByteArrayOutputStream();
			_sffactory.serializeFont(_sfont,_baos);
			ResourceLoader _barl = ByteArrayResourceLoader.of(_rl.getName(),_baos.toByteArray(),_rl.getOptions());
			_ret.add(new PdfOtxFont(_doc, _sfont, _barl, _cs));
		}
		return  _ret;
	}
	
	@Override
	public PdfResource getResource()
	{
		if(_res==null)
		{
			_res = createResource(this);
		}
		return _res;
	}

	int[] widths;
	Font trueTypefont;
	PdfFontFileStream _fs;
	PdfFontDescriptor _des;

	Map<String, Integer> nameToChar = new HashMap<>();
	
	public PdfOtxFont(PdfDocument doc, GlyphEncoder _enc, boolean _otf, boolean _cff) throws Exception
	{
		super(doc, _enc, _otf, _cff);
	}
	
	public PdfOtxFont(PdfDocument doc, Font _font, ResourceLoader _fontfile, String _cs) throws Exception
	{
		this(doc, new SfontlyTtfGlyphEncoder(_font, false), true, SfntUtil.isCFF(_font));
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
		
		this.setPsName(_name.replaceAll("\\s+", ""));
		
		this.setName(this.getResource().getResName());
		this.setFontName(makeFontSubsetTag(this.getRef().getValue(), this.getResource().getResPrefix(), _name));

		if(this._touni!=null) this._touni.set("X_OtxFontName", PdfString.of(_name));

 		PdfDictObject _desc = this.getDescender();

		CMap _cmap = makeCmapCidGid(_font, _isPS, _desc, _cs);
		
		makeWidths(_font, _isPS, _cmap, _desc);
		
		makeDescriptor(_font, _isPS, _name, _desc, _fontfile);
	}
	
	void makeDescriptor(Font _font, boolean _isPS, String _name, PdfDictObject _desc, ResourceLoader _fontfile)
	{
		int emUnit = SfntUtil.getUnitsPerEm(_font);
		
		if(_isPS)
		{
			_desc.setSubtype("CIDFontType0");
		}
		else
		{
			_desc.setSubtype("CIDFontType2");
		}
		_desc.set("BaseFont", PdfName.of(this.getFontName()));
		
		this._des = PdfFontDescriptor.create(this.getDoc());
		this._des.setFontBBox(SfntUtil.getFontBBox(_font));
		_desc.set("FontDescriptor", this._des);
		this._fs = PdfFontFileStream.create(this.getDoc());
		
		if(_isPS)
		{
			this._des.setFontFile3(this._fs);
			this._fs.putStream(_fontfile);
			this._fs.setSubtype("OpenType");
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
	
	void makeWidths(Font _font, boolean _isPS, CMap _cmap, PdfDictObject _desc)
	{
		PdfArray _width = PdfArray.create();
		
		int emUnit = SfntUtil.getUnitsPerEm(_font);
		int numGlyphs = SfntUtil.getNumGlyphs(_font);
		
		this.widths = new int[0x20000];
		
		_width.add(PdfNum.of(0));
		PdfArray _widths = PdfArray.create();
		
		int lastChar = -2;
		
		HorizontalMetricsTable _hmtx = (HorizontalMetricsTable)_font.getTable(Tag.hmtx);
		
		if(_cmap!=null && !_isPS)
		{
			for(int i=0; i<0x20000; i++)
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
	}
	
	@SneakyThrows
	CMap makeCmapCidGid(Font _font, boolean _isPS, PdfDictObject _desc, String _cs)
	{
		int numGlyphs = SfntUtil.getNumGlyphs(_font);
		
		CMap _cmap = SfntUtil.findCMap(_font, false);
		//PostScriptTable _post = (PostScriptTable)_font.getTable(Tag.post);
		if(_cmap!=null)
		{
			for(int _u = 0; _u<0x100000; _u++)
			{
				int _g = _cmap.glyphId(_u);
				if(_g>0) this.setCoverage((_u>>8) & 0xffff);
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
		
		return _cmap;
	}
	
	public static PdfFont of(PdfDocument doc, ResourceLoader _fontfile, String _cs) throws Exception
	{
		InputStream _stream = _fontfile.getInputStream();
		FontFactory _sffactory = FontFactory.getInstance();
		Font[]      _sfonts    = _sffactory.loadFonts(_stream);
		Font        _sfont     = _sfonts[0];
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
