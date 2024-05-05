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
import com.github.terefang.jmelange.commons.util.GuidUtil;
import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.PdfValue;
import com.github.terefang.jmelange.pdf.core.encoding.ByteEncoder;
import com.github.terefang.jmelange.pdf.core.encoding.PdfEncoding;
import com.github.terefang.jmelange.pdf.core.values.*;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.PrintStream;

import static com.github.terefang.jmelange.pdf.core.PDF.geomDP;

public class PdfSynthFont extends PdfType3Font
{
	public static final PdfFontResource createResource(PdfSynthFont _f)
	{
		return new PdfFontResource(_f, "SF");
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

	private PdfDictObject _dict;

	public PdfSynthFont(PdfDocument doc, String _cs, String _name, int _first, String[] _glyphs, int[] _widths)
	{
		super(doc, _cs, _name, _first, _glyphs, _widths);
		this.setFontName(makeFontSubsetTag(this.getRef().getValue(), "T3SF", _name));
	}
	
	@Override
	public char glyphToChar(String _name)
	{
		return 0;
	}

	@Override
	public void streamOut(boolean _res) throws IOException
	{
		if(_res)
		{
			for(PdfValue _v : this._dict.values())
			{
				if(_v instanceof PdfDictObjectWithStream)
				{
					((PdfDictObjectWithStream)_v).streamOut();
				}
			}
			this._dict.streamOut();
		}
		super.streamOut(_res);
	}
	
	public static final PdfSynthFont of(PdfDocument _doc, String _cs, PdfFont _font, String[] _options)
	{
		String[] _g = new String[256];
		Character[] _u =  new Character[256];

		String _fname = _font.getFontName()+"+"+CommonUtil.join(_options,'-');

		boolean _mods = false;
		float _widthFactor = 1f;
		float _obliqueFactor = 0f;
		float _boldFactor = 0f;

		if(_options!=null)
		for(String _opt : _options)
		{
			if(_opt.startsWith("bold"))
			{
				_boldFactor=4f;
				_mods = true;
				if(_opt.indexOf('=')>0)
				{
					_boldFactor=Integer.parseInt(_opt.substring(_opt.indexOf('=')+1))/100f;
				}
			}
			else
			if(_opt.startsWith("condensed"))
			{
				_widthFactor=0.8f;
				_mods = true;
				if(_opt.indexOf('=')>0)
				{
					_widthFactor=Integer.parseInt(_opt.substring(_opt.indexOf('=')+1))/100f;
				}
			}
			else
			if(_opt.startsWith("expanded"))
			{
				_widthFactor=1.3f;
				_mods = true;
				if(_opt.indexOf('=')>0)
				{
					_widthFactor=Integer.parseInt(_opt.substring(_opt.indexOf('=')+1))/100f;
				}
			}
			else
			if(_opt.startsWith("italic")
					|| _opt.startsWith("oblique"))
			{
				_obliqueFactor=0.267f;
				_mods = true;
				if(_opt.indexOf('=')>0)
				{
					_obliqueFactor=Integer.parseInt(_opt.substring(_opt.indexOf('=')+1))/100f;
				}
			}
			else
			if(_opt.startsWith("slant"))
			{
				_obliqueFactor=-0.267f;
				_mods = true;
				if(_opt.indexOf('=')>0)
				{
					_obliqueFactor=-Integer.parseInt(_opt.substring(_opt.indexOf('=')+1))/100f;
				}
			}
		}

		_g = AFM.getGlyphNamesBase(_cs);
		_u = AFM.getUnicodeBase(_cs);

		int[] _w = new int[256];
		for(int i = 0; i<_u.length; i++)
		{
			_w[i]= (int) ((_widthFactor*_font.width(Character.toString(_u[i]==null ? 0xfffe : _u[i]))*1000.)+(_boldFactor*5.));
		}
		
		PdfSynthFont _sfont = new PdfSynthFont(_doc, _cs, _fname, 0, _g, _w);
		_sfont.setFontAscent(1000);
		_sfont.setFontDescent(200);

		_sfont.set("FontBBox", PdfArray.from(((int)(_widthFactor*(-400f))),
											((int)(-400f)),
											((int)(_widthFactor*1500f)),
											((int)(1500f))));
		_sfont.set("FontMatrix", PdfArray.fromFloat(0.001f,0f,0f,0.001f,0f,0f));
		PdfDict _Res = PdfDict.create();
		_Res.set("Font", PdfDict.create(_font.getResource().getResName(), _font));
		_Res.set("ProcSet", PdfArray.from("PDF", "Text"));
		_sfont.set("Resources", _Res);
		PdfDictObject _dict = PdfDictObject.create(_doc);
		_sfont.set("CharProcs", _dict);
		_sfont._dict = _dict;
		for(int i = 0; i<_u.length; i++)
		{
			if(!(".notdef".equalsIgnoreCase(_g[i]) || _g[i]==null))
			{
				PdfDictObjectWithStream _glyph = PdfDictObjectWithStream.create(_doc);
				_dict.set(_g[i],_glyph);
				PrintStream _writer = _glyph.getPrintStream();
				_writer.println(PDF.transformDP(_w[i]) + " 0 " +
								PDF.transformDP(-400f)+" " +
								PDF.transformDP(1500f)+" " +
								PDF.transformDP(-400f)+" " +
								PDF.transformDP(1500f)+" d1\n");
				_writer.println(" q");
				if(_mods)
				{
					_writer.println(
							PDF.transformDP(_widthFactor) + " " +
									PDF.transformDP(0) + " " +
									PDF.transformDP(_obliqueFactor) + " " +
									PDF.transformDP(1) + " " +
									PDF.transformDP(0) + " " +
									PDF.transformDP(0) + " cm "
					);
				}
				_writer.println(" BT /"+_font.getResource().getResName()+" "+geomDP(1000)+" Tf ");
				if(_mods && _boldFactor>0f)
				{
					_writer.println(" 2 Tr "+PDF.transformDP(_boldFactor*10)+" w ");
				}
				_writer.println(new String(_font.text(_u[i].toString(), 0., 0., 0.)));
				_writer.println(" ET Q");
			}
		}
		return _sfont;
	}
}
