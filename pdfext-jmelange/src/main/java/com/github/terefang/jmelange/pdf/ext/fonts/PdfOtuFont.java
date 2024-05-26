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
package com.github.terefang.jmelange.pdf.ext.fonts;

import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontDescriptor;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontFileStream;
import com.github.terefang.jmelange.commons.loader.*;
import com.github.terefang.jmelange.pdf.core.fonts.PdfType0Font;
import com.github.terefang.jmelange.pdf.core.values.*;
import com.github.terefang.jmelange.pdf.ext.encoding.FontBoxTtfGlyphEncoder;
import org.apache.fontbox.ttf.*;

import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfOtuFont extends PdfType0Font
{
	public static final PdfFont.PdfFontResource createResource(PdfOtuFont _f)
	{
		return PdfFont.createResource(_f, _f.trueTypefont.isPostScript() ? "FOU" : "FTU");
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
	private PdfDictObjectWithStream _map;
	OpenTypeFont trueTypefont;
	PdfFontFileStream _fs;
	PdfFontDescriptor _des;
	
	public PdfOtuFont(PdfDocument doc, OpenTypeFont _font, ResourceLoader _fontfile, String _cs) throws Exception
	{
		super(doc, new FontBoxTtfGlyphEncoder(_font, _font.getNumberOfGlyphs(), false), true, _font.isPostScript());
		this.trueTypefont = _font;

		String _name = this.trueTypefont.getName();
		if(_name==null) _name = this.trueTypefont.getNaming().getPostScriptName();
		if(_name==null)
		{
			_name = this.trueTypefont.getNaming().getFontFamily();
			if(_name!=null)
			{
				_name += " "+this.trueTypefont.getNaming().getFontSubFamily();
			}
		}
		if(_name==null) _name = this.trueTypefont.getNaming().getNameRecords().get(0).getString();
		//_name = _name.replaceAll("[^a-zA-Z0-9]", "-");

		this.setName(this.getResource().getResName());
		this.setFontName(makeFontSubsetTag(this.getRef().getValue(), this.getResource().getResPrefix(), _name));

		if(this._touni!=null) this._touni.set("X_OtuFontName", PdfString.of(this.getFontName()));

 		PdfDictObject _desc = this.getDescender();

		if(_font.isPostScript())
		{
			_desc.setSubtype("CIDFontType0");
		}
		else
		{
			_desc.setSubtype("CIDFontType2");
		}
		_desc.set("BaseFont", PdfName.of(this.getFontName()));
		PdfArray _width = PdfArray.create();
		int emUnit=this.trueTypefont.getUnitsPerEm();
		this.widths = new int[0x10000];
		CmapLookup _cmap = null;

		try
		{
			_cmap = this.trueTypefont.getUnicodeCmapLookup();
		}
		catch(Exception _xe)
		{
			_cmap = this.trueTypefont.getCmap().getCmaps()[0];
		}

		
		if(this.trueTypefont.getCmap().getSubtable(3, 0)!=null)
		{
			_cmap = this.trueTypefont.getCmap().getSubtable(3, 0);
		}

		_width.add(PdfNum.of(0));
		PdfArray _widths = PdfArray.create();

		int lastChar = -2;
		for(int i=0; i<=_font.getNumberOfGlyphs(); i++)
		{
			int _w = _font.getAdvanceWidth(i);
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

				List<Integer> _cc = _cmap.getCharCodes(i);
				if(_cc!=null && _cc.size()>0)
				{
					int _c = _cc.get(0);
					this.setCoverage(_c>>>8);
				}
			}
		}
		_width.add(_widths);

		_desc.set("CIDToGIDMap", PdfName.of("Identity"));

		_desc.set("W", _width);
		_desc.set("DW", PdfNum.of(0));
		// _desc.set("DW", PdfNum.of(300); // missing width
		_des = PdfFontDescriptor.create(doc);
		_des.setFontBBox(
				(int)this.trueTypefont.getFontBBox().getLowerLeftX(),
				(int)this.trueTypefont.getFontBBox().getLowerLeftY(),
				(int)this.trueTypefont.getFontBBox().getUpperRightX(),
				(int)this.trueTypefont.getFontBBox().getUpperRightY());
		_desc.set("FontDescriptor", _des);
		_fs = PdfFontFileStream.create(doc);

		if(_font.isPostScript())
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

		_fs.set("X_OtuFontName", PdfString.of(this.getFontName()));
		_des.setFontName(this.getFontName());
		_des.setFontFamily(this.trueTypefont.getNaming().getFontFamily()!=null ? this.trueTypefont.getNaming().getFontFamily() : this.trueTypefont.getNaming().getPostScriptName());

		OS2WindowsMetricsTable _os2 = this.trueTypefont.getOS2Windows();
		if(_os2!=null)
		{
			_des.setFontStretch(_os2.getWidthClass() < WIDTH_CLASS.length ? WIDTH_CLASS[this.trueTypefont.getOS2Windows().getWidthClass()] : "Normal");
			_des.setCapHeight(_os2.getCapHeight()*1000/emUnit);

			if(_os2.getVersion() >= 2)
			{
				this.setFontCapHeight(_os2.getCapHeight()*1000/emUnit);
				this.setFontXHeight(_os2.getHeight()*1000/emUnit);
			}
			else
			{
				try
				{
					GeneralPath capHPath = this.trueTypefont.getPath("H");
					if (capHPath != null)
					{
						this.setFontCapHeight(Math.round(capHPath.getBounds2D().getMaxY()) * 1000/emUnit);
					}
					else
					{
						// estimate by summing the typographical +ve ascender and -ve descender
						this.setFontCapHeight((_os2.getTypoAscender() + _os2.getTypoDescender()) * 1000/emUnit);
					}

					GeneralPath xPath = this.trueTypefont.getPath("x");
					if (xPath != null)
					{
						this.setFontXHeight(Math.round(xPath.getBounds2D().getMaxY()) * 1000/emUnit);
					}
					else
					{
						// estimate by halving the typographical ascender
						this.setFontXHeight(_os2.getTypoAscender() / 2.0f * 1000/emUnit);
					}
				}
				catch(Exception _xe) {}
			}
		}
		_des.setFlags(0 /*1<<5*/);

		_des.setStemV(0);
		_des.setItalicAngle(0);
		_des.setFontBBox(
				(int)this.trueTypefont.getFontBBox().getLowerLeftX(),
				(int)this.trueTypefont.getFontBBox().getLowerLeftY(),
				(int)this.trueTypefont.getFontBBox().getUpperRightX(),
				(int)this.trueTypefont.getFontBBox().getUpperRightY());
		
		if(this.trueTypefont.getHorizontalHeader()!=null)
		{
			_des.setAscent(this.trueTypefont.getHorizontalHeader().getAscender()*1000/emUnit);
			_des.setDescent(this.trueTypefont.getHorizontalHeader().getDescender()*1000/emUnit);
			_des.setMaxWidth(this.trueTypefont.getHorizontalHeader().getAdvanceWidthMax()*1000/emUnit);
			_des.setMissingWidth(this.trueTypefont.getHorizontalHeader().getAdvanceWidthMax()*1000/emUnit);

			this.setFontAscent(this.trueTypefont.getHorizontalHeader().getAscender()*1000/emUnit);
			this.setFontDescent(this.trueTypefont.getHorizontalHeader().getDescender()*1000/emUnit);
		}
	}
	public static final String[] WIDTH_CLASS = { "Normal", "UltraCondensed", "ExtraCondensed", "Condensed", "SemiCondensed", "Normal", "SemiExpanded", "Expanded", "ExtraExpanded", "UltraExpanded" };
	
	public static PdfFont of(PdfDocument doc, ResourceLoader _fontfile, String _cs) throws Exception
	{
		InputStream _stream = _fontfile.getInputStream();
		OpenTypeFont ttf1 = new OTFParser().parse(_stream);
		_stream.close();
		return new PdfOtuFont(doc, ttf1, _fontfile, _cs);
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
		try
		{
			int _g = (!this.gidToName.containsKey(_name)) ? 0 : this.gidToName.get(_name);

			if(_g==0) _g = this.trueTypefont.nameToGID(_name);

			if(_g==0)
			{
				for(int _i = 0; _i<this.trueTypefont.getNumberOfGlyphs(); _i++)
				{
					String _gn = this.trueTypefont.getCFF().getFont().getCharset().getNameForGID(_i);
					gidToName.put(_gn, _i);
					if(_name.equals(_gn))
					{
						_g = _i;
						break;
					}
				}
			}

			gidToName.put(_name, _g);

			if(this.trueTypefont.getUnicodeCmapLookup()!=null
				&& this.trueTypefont.getUnicodeCmapLookup().getCharCodes(_g)!=null)
			{
				_g = this.trueTypefont.getUnicodeCmapLookup().getCharCodes(_g).get(0).intValue();
			}

			return (char) _g;
		}
		catch (Exception _xe)
		{
			_xe.printStackTrace();
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

	public TrueTypeFont getTrueTypefont() {
		return this.trueTypefont;
	}
}
