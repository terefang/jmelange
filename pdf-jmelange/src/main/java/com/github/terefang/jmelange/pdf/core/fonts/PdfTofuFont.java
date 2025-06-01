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

import com.github.terefang.jmelange.commons.util.GuidUtil;
import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.PdfValue;
import com.github.terefang.jmelange.pdf.core.encoding.ByteEncoder;
import com.github.terefang.jmelange.pdf.core.encoding.PdfEncoding;
import com.github.terefang.jmelange.pdf.core.values.PdfArray;
import com.github.terefang.jmelange.pdf.core.values.PdfDictObject;
import com.github.terefang.jmelange.pdf.core.values.PdfDictObjectWithStream;
import com.github.terefang.jmelange.pdf.core.values.PdfResource;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.PrintStream;

public class PdfTofuFont
        extends PdfType3Font
{
	public static final PdfFontResource createResource(PdfTofuFont _f)
	{
		return PdfFont.createResource(_f, "FB");
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

	private boolean inStroke;
	private float lx;
	private float ly;
	private PdfDictObject _dict;
	
	public PdfTofuFont(PdfDocument doc, String _cs, String _name, int _first, String[] _glyphs, int[] _widths)
	{
		super(doc, _cs, _name, _first, _glyphs, _widths);
		this.setPsName(_name.replaceAll("\\s+", ""));
		
		this.setFontName(makeFontSubsetTag(this.getRef().getValue(), "T3A", _name));
	}
	
	@Override
	public char glyphToChar(String _name)
	{
		return 0xff;
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
	
	public static final PdfTofuFont of(PdfDocument _doc)
	{
		String[] _g = new String[256];
		Character[] _u =  new Character[256];
		int[] _w = new int[256];
		for(int _i=0; _i<256; _i++)
		{
			_g[_i]="uni00ff";
			_u[_i]=(char)_i;
			_w[_i]=1000;
		}
		
		String _fname = "Tofu"; // GuidUtil.toHashGUID(_awt.getName());
		
		PdfTofuFont _font = new PdfTofuFont(_doc, "tofu", "tofu", 0,_g,_w);
		_font.setFontAscent(1000);
		_font.setFontDescent(0);

		_font.set("FontBBox", PdfArray.from(0,0,1000,1000));
		_font.set("FontMatrix", PdfArray.fromFloat(0.001f,0f,0f,0.001f,0f,0f));
		PdfDictObject _dict = PdfDictObject.create(_doc);
		_font.set("CharProcs", _dict);
		_font._dict = _dict;
		_font.setFontCapHeight(1000f);
		_font.setFontXHeight(500f);
		
		PdfDictObjectWithStream _glyph = PdfDictObjectWithStream.create(_doc, true);
		
		_dict.set("uni00ff",_glyph);
		PrintStream _writer = _glyph.getPrintStream();
		_writer.println("1000 0 -1000 2000 -1000 2000 d1");
		_writer.println(" q ");
		_writer.println(" 3 w");
		_writer.println(" 1 1 m 1 999 l 999 1 l 999 999 l 1 1 l f* 0 0 1000 1000 re s");
		_writer.println(" Q");

		return _font;
	}
	
}
