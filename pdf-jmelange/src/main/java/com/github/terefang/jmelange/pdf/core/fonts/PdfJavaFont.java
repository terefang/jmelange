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
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.PdfValue;
import com.github.terefang.jmelange.pdf.core.encoding.ByteEncoder;
import com.github.terefang.jmelange.pdf.core.encoding.PdfEncoding;
import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.pdf.core.values.PdfArray;
import com.github.terefang.jmelange.pdf.core.values.PdfDictObject;
import com.github.terefang.jmelange.pdf.core.values.PdfDictObjectWithStream;
import com.github.terefang.jmelange.pdf.core.values.PdfResource;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.PrintStream;

public class PdfJavaFont extends PdfType3Font
{
	public static final PdfFontResource createResource(PdfJavaFont _f)
	{
		return new PdfFontResource(_f, "A");
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
	
	public PdfJavaFont(PdfDocument doc, String _cs, String _name, int _first, String[] _glyphs, int[] _widths)
	{
		super(doc, _cs, _name, _first, _glyphs, _widths);
		//this.setName(this.getResName());
		this.setFontName(makeFontSubsetTag(this.getRef().getValue(), "T3A", _name));
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
	
	public static final PdfJavaFont of(PdfDocument _doc, String _cs, Font _awt, String[] _options)
	{
		Font _awt2 = _awt.deriveFont(1000f);
		String[] _g = new String[256];
		Character[] _u =  new Character[256];
		PdfEncoding _enc = null;
		boolean _auto = _cs==null ? true : false;

		String _fname = _awt.getName(); // GuidUtil.toHashGUID(_awt.getName());

		boolean _mods = false;
		float _widthFactor = 1f;
		float _obliqueFactor = 0f;

		if(_options!=null)
		for(String _opt : _options)
		{
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
		}

		if(_auto)
		{
			for(int p = 33; p < 160; p++)
			{
				if(_awt.canDisplay(p))
				{
					_auto = false;
					break;
				}
			}
			for(int p = 161; p < 256; p++)
			{
				if(_awt.canDisplay(p))
				{
					_auto = false;
					break;
				}
			}
		}
		
		if(_auto || _cs==null)
		{
			int[] _c =  new int[256];
			int i = 0;
			int j = 0;
			while((i < _awt.getNumGlyphs()) && (i < 256) && (j < 0xffff))
			{
				if(_awt.canDisplay(j))
				{
					_g[i] = AFM.AGLFN.containsKey(j) ? AFM.AGLFN.get(j) : String.format("uni%04x", j);
					_u[i] = Character.valueOf((char) j);
					_c[i] = j;
					i++;
				}
				j++;
			}
			_enc = PdfEncoding.of(_doc, ByteEncoder.from(0, _c, _g));
		}
		else
		{
			_g = AFM.getGlyphNamesBase(_cs);
			_u = AFM.getUnicodeBase(_cs);
		}
		
		int[] _w = new int[256];
		FontMetrics _fm = PdfJavaFont.getFontMetrics(_awt2);
		Rectangle2D _bbx = _awt2.getMaxCharBounds(_fm.getFontRenderContext());
		for(int i = 0; i<_u.length; i++)
		{
			_w[i]= (int) (_widthFactor*_fm.charWidth(_u[i]==null ? _awt2.getMissingGlyphCode() : _u[i].charValue()));
		}
		
		PdfJavaFont _font = null;
		if(_enc!=null)
		{
			_font = new PdfJavaFont(_doc, null, _fname, 0, _g, _w);
			_font.setEncoding(_enc);
		}
		else
		{
			_font = new PdfJavaFont(_doc, _cs, _fname, 0, _g, _w);
		}

		_font.setFontAscent(_fm.getMaxAscent());
		_font.setFontDescent(_fm.getMaxDescent());

		_font.set("FontBBox", PdfArray.from(((int)(_widthFactor*_bbx.getMinX()*2f)),
											((int)(_bbx.getMinY()*2f)),
											((int)(_widthFactor*_bbx.getMaxX()*2f)),
											((int)(_bbx.getMaxY()*2f))));
		_font.set("FontMatrix", PdfArray.fromFloat(0.001f,0f,0f,0.001f,0f,0f));
		PdfDictObject _dict = PdfDictObject.create(_doc);
		_font.set("CharProcs", _dict);
		_font._dict = _dict;
		for(int i = 0; i<_u.length; i++)
		{
			if(!(".notdef".equalsIgnoreCase(_g[i]) || _g[i]==null))
			{
				PdfDictObjectWithStream _glyph = PdfDictObjectWithStream.create(_doc, true);
				GlyphVector _v = _awt2.createGlyphVector(_fm.getFontRenderContext(), _u[i].toString());
				Rectangle2D _gbbx = _v.getLogicalBounds();

				if(_u[i].charValue()=='H')
				{
					_font.setFontCapHeight((float) _gbbx.getMaxY());
				}
				else
				if(_u[i].charValue()=='x')
				{
					_font.setFontXHeight((float) _gbbx.getMaxY());
				}

				//_glyph.set("_EncodedChar", PdfString.of(_g[i]));
				_dict.set(_g[i],_glyph);
				PrintStream _writer = _glyph.getPrintStream();
				_writer.println(PDF.transformDP(_w[i]) + " 0 " +
								PDF.transformDP(_gbbx.getMinX())+" " +
								PDF.transformDP(_gbbx.getMaxX())+" " +
								PDF.transformDP(_gbbx.getMinY())+" " +
								PDF.transformDP(_gbbx.getMaxY())+" d1\n");
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
				_font.followPath(_v.getOutline(), _writer);
				_writer.println(" Q");
			}
		}
		return _font;
	}
	
	private static final double degrees_to_radians = Math.PI/180.0;
	
	private static final int FILL = 1;
	private static final int STROKE = 2;
	private static final int CLIP = 3;
	
	private static final AffineTransform IDENTITY = new AffineTransform();
	
	void followPath(Shape s, PrintStream _writer) {
		PathIterator points;
		
		if (s==null) return;
		points = s.getPathIterator(IDENTITY);
		int segments = 0;
		float[] coords = new float[6];
		while(!points.isDone()) {
			segments++;
			int segtype = points.currentSegment(coords);
			switch(segtype) {
				case PathIterator.SEG_CLOSE:
					_writer.println("h ");
					break;
				
				case PathIterator.SEG_CUBICTO:
					curveto(_writer, coords[0], -coords[1], coords[2], -coords[3], coords[4], -coords[5]);
					break;
				
				case PathIterator.SEG_LINETO:
					lineto(_writer, coords[0], -coords[1]);
					break;
				
				case PathIterator.SEG_MOVETO:
					moveto(_writer, coords[0], -coords[1]);
					break;
				
				case PathIterator.SEG_QUADTO:
					curveto(_writer, coords[0], -coords[1], coords[2], -coords[3]);
					break;
			}
			points.next();
		}
		
		if (segments > 0) {
			if (points.getWindingRule() == PathIterator.WIND_EVEN_ODD)
				_writer.println("f*");
			else
				_writer.println("f");
		}
	}
	
	void lineto(PrintStream _writer, double x,double y) {
		newPath(_writer);
		// no optimisation here as it may introduce errors on decimal coordinates.
		_writer.println(cxy(x,y)+"l ");
		lx=(float)x;
		ly=(float)y;
	}
	
	void curveto(PrintStream _writer, double x1,double y1,double x2,double y2,double x3,double y3) {
		newPath(_writer);
		_writer.println(cxy(x1,y1)+cxy(x2,y2)+cxy(x3,y3)+"c");
		lx=(float)x3;
		ly=(float)y3;
	}
	
	void curveto(PrintStream _writer, double x1,double y1,double x2,double y2)
	{
		newPath(_writer);
		_writer.println(cxy(x1,y1)+cxy(x2,y2)+"v");
		lx=(float)x2;
		ly=(float)y2;
	}
	
	void moveto(PrintStream _writer, double x,double y) {
		newPath(_writer);
		// no optimisation here as it may introduce errors on decimal coordinates.
		_writer.println(cxy(x,y)+"m ");
		this.lx=(float)x;
		this.ly=(float)y;
	}
	
	private String cxy(double x, double y) {
		return PDF.transformDP(x)+" "+PDF.transformDP(y)+" ";
	}
	
	void newPath(PrintStream _writer) {
		if(!inStroke) {
			_writer.println("n ");
		}
		
		inStroke=true;
		
		// an unlikely coordinate to fool the moveto() optimizer
		lx = ly = -9999;
	}

	public static final FontMetrics getFontMetrics(Font font)
	{
		Canvas c = new Canvas();
		return c.getFontMetrics(font);
	}
}
