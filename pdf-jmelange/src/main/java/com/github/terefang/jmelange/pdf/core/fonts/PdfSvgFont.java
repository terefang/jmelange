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

import com.github.terefang.jmelange.commons.GuidUtil;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.PdfValue;
import com.github.terefang.jmelange.pdf.core.encoding.ByteEncoder;
import com.github.terefang.jmelange.pdf.core.encoding.PdfEncoding;
import com.github.terefang.jmelange.pdf.core.fonts.svg.ParseException;
import com.github.terefang.jmelange.pdf.core.fonts.svg.PathHandler;
import com.github.terefang.jmelange.pdf.core.fonts.svg.PathParser;
import com.github.terefang.jmelange.pdf.core.fonts.svg.jaxb.SvgFontContainer;
import com.github.terefang.jmelange.pdf.core.fonts.svg.jaxb.SvgFontGlyph;
import com.github.terefang.jmelange.commons.loader.ResourceLoader;
import com.github.terefang.jmelange.pdf.core.util.AFM;
import com.github.terefang.jmelange.pdf.core.values.*;
import lombok.SneakyThrows;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PdfSvgFont extends PdfType3Font
{
	public static final PdfFontResource createResource(PdfSvgFont _f)
	{
		return new PdfFontResource(_f, "VF");
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

	public PdfSvgFont(PdfDocument doc, String _cs, String _name, int _first, String[] _glyphs, int[] _widths)
	{
		super(doc, _cs, _name, _first, _glyphs, _widths);
		//this.setName(this.getResName());
		this.setFontName(makeFontSubsetTag(this.getRef().getValue(), "T3V", _name));
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
	
	public static final PdfSvgFont of(PdfDocument _doc, String _cs, ResourceLoader _rl, String[] _options)
	{
		_cs = _cs==null ? "pdfdoc" : _cs;

		String[] _g = AFM.getGlyphNamesBase(_cs);
		Character[] _u =  AFM.getUnicodeBase(_cs);

		PdfEncoding _enc = null;

		SvgFontContainer _svgFont = loadSvg(_rl);
		Map<String, SvgFontGlyph> _sglyphs = new HashMap<>();
		for(SvgFontGlyph _sg : _svgFont.defs.font.glyphs)
		{
			_sglyphs.put(_sg.glyphName, _sg);
		}

		//String _fname = (_svgFont.defs.font.id!=null ? _svgFont.defs.font.id : _rl.getName())+"-"+_cs;
		String _fname = GuidUtil.toHashGUID(_svgFont.defs.font.id!=null ? _svgFont.defs.font.id : _rl.getName()).substring(0,6)+"-"+_cs;

		boolean _mods = false;
		float _widthFactor = 1f;
		float _obliqueFactor = 0f;

		if(_options!=null)
		{
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
					_fname+="+cond";
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
					_fname+="+exp";
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
					_fname+="+obl";
				}
			}
		}

		double _unitsPerEm = _svgFont.defs.font.face.unitsPerEm;
		double _xf = _svgFont.defs.font.horizAdvX;
		double _xm = _svgFont.defs.font.missingGlyph.horizAdvX;

		int[] _w = new int[256];
		for(int _i = 0; _i<_u.length; _i++)
		{
			if(_sglyphs.containsKey(_g[_i]))
			{
				double _x = (_sglyphs.get(_g[_i]).horizAdvX != null ? _sglyphs.get(_g[_i]).horizAdvX : _xf)*1000f/_unitsPerEm;
				_w[_i]= (int) _x;
			}
			else
			{
				_g[_i] = ".notdef";
				_w[_i] = (int) (_xm*1000f/_unitsPerEm);
			}
		}

		PdfSvgFont _font = new PdfSvgFont(_doc, _cs, _fname, 0, _g, _w);
		_font.setFontAscent((float) (_svgFont.defs.font.face.ascent*1000f/_unitsPerEm));
		_font.setFontDescent((float) (_svgFont.defs.font.face.descent*1000f/_unitsPerEm));
		_font.setFontXHeight((float) (_svgFont.defs.font.face.xHeight*1000f/_unitsPerEm));
		_font.setFontCapHeight((float) (_svgFont.defs.font.face.capHeight*1000f/_unitsPerEm));

		_font.set("FontBBox", PdfArray.from(((int)(_widthFactor*(-1000f))),
											((int)((-1000f))),
											((int)(_widthFactor*2000f)),
											((int)(2000f))));
		_font.set("FontMatrix", PdfArray.fromFloat(0.001f,0f,0f,0.001f,0f,0f));
		PdfDictObject _dict = PdfDictObject.create(_doc);
		_font.set("CharProcs", _dict);
		_font._dict = _dict;
		for(int _i = 0; _i<_u.length; _i++)
		{
			if(!(".notdef".equalsIgnoreCase(_g[_i]) || _g[_i]==null))
			{
				PdfDictObjectWithStream _glyph = PdfDictObjectWithStream.create(_doc, true);
				_glyph.set("X_EncodedChar", PdfString.of(_g[_i]));
				_dict.set(_g[_i],_glyph);
				PrintStream _writer = _glyph.getPrintStream();
				_writer.println(PDF.transformDP(_w[_i]) + " 0 " +
								PDF.transformDP(-1000f)+" " +
								PDF.transformDP(2000f)+" " +
								PDF.transformDP(-1000f)+" " +
								PDF.transformDP(2000f)+" d1\n");
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
				_font.renderPath(_unitsPerEm, _sglyphs.get(_g[_i]), _writer);
				_writer.println(" Q");
				_writer.flush();
			}
		}
		return _font;
	}

	private void renderPath(double _unitsPerEm, SvgFontGlyph _svgFontGlyph, PrintStream _writer)
	{
		if(_svgFontGlyph.path!=null)
		{
			PathParser _pp = new PathParser();
			pdfPathHandler _ph = new pdfPathHandler();
			_ph.pw = _writer;
			_ph.upem = 1000d/_unitsPerEm;
			_pp.setPathHandler(_ph);
			_pp.parse(_svgFontGlyph.path);
		}
	}

	static class pdfPathHandler implements PathHandler
	{
		public PrintStream pw;
		public double upem;
		private double lx;
		private double ly;
		private double cx;
		private double cy;

		private String cxy(double x, double y) {
			return PDF.transformDP(x)+" "+PDF.transformDP(y)+" ";
		}

		@Override
		public void startPath() throws ParseException
		{
			//pw.println("%% startPath ");
			pw.println("n ");
			cx = cy = lx = ly = 0;
		}

		@Override
		public void endPath() throws ParseException {
			//pw.println("%% endPath ");
			pw.println("f*"); // even-odd winding
			//	pw.println("f");
		}

		@Override
		public void movetoRel(float x, float y) throws ParseException {
			//pw.println("%% movetoRel ");
			movetoAbs((float)this.lx+x, (float)this.ly+y);
		}

		@Override
		public void movetoAbs(float x, float y) throws ParseException {
			//pw.println("%% movetoAbs ");
			pw.println(cxy(x*upem,y*upem)+"m ");
			cx = lx=x;
			cy = ly=y;
		}

		@Override
		public void closePath() throws ParseException {
			//pw.println("%% closePath ");
			pw.println("h ");
		}

		@Override
		public void linetoRel(float x, float y) throws ParseException {
			//pw.println("%% linetoRel ");
			linetoAbs((float)this.lx+x, (float)this.ly+y);
		}

		@Override
		public void linetoAbs(float x, float y) throws ParseException {
			//pw.println("%% linetoAbs ");
			// no optimisation here as it may introduce errors on decimal coordinates.
			pw.println(cxy(x*upem,y*upem)+"l ");
			cx = lx=x;
			cy = ly=y;
		}

		@Override
		public void linetoHorizontalRel(float x) throws ParseException {
			//pw.println("%% linetoHorizontalRel ");
			linetoHorizontalAbs((float)lx+x);
		}

		@Override
		public void linetoHorizontalAbs(float x) throws ParseException {
			//pw.println("%% linetoHorizontalAbs ");
			pw.println(cxy(x*upem,ly*upem)+"l ");
			cx = lx=x;
		}

		@Override
		public void linetoVerticalRel(float y) throws ParseException {
			//pw.println("%% linetoVerticalRel ");
			linetoVerticalAbs((float)ly+y);
		}

		@Override
		public void linetoVerticalAbs(float y) throws ParseException {
			//pw.println("%% linetoVerticalAbs ");
			pw.println(cxy(lx*upem,y*upem)+"l ");
			cy = ly=y;
		}

		@Override
		public void curvetoCubicRel(float x1, float y1, float x2, float y2, float x, float y) throws ParseException {
			//pw.println("%% curvetoCubicRel ");
			curvetoCubicAbs((float)lx+x1, (float)ly+y1, (float)lx+x2, (float)ly+y2, (float)lx+x, (float)ly+y);
		}

		@Override
		public void curvetoCubicAbs(float x1, float y1, float x2, float y2, float x, float y) throws ParseException {
			//pw.println("%% curvetoCubicAbs ");
			pw.println(cxy(x1*upem,y1*upem)+cxy(x2*upem,y2*upem)+cxy(x*upem,y*upem)+"c");
			lx=(float)x;
			ly=(float)y;
			cx = x2;
			cy = y2;
		}

		@Override
		public void curvetoCubicSmoothRel(float x2, float y2, float x, float y) throws ParseException {
			//pw.println("%% curvetoCubicSmoothRel ");
			curvetoCubicSmoothAbs((float)lx+x2, (float)ly+y2, (float)lx+x, (float)ly+y);
		}

		@Override
		public void curvetoCubicSmoothAbs(float x2, float y2, float x, float y) throws ParseException {
			//pw.println("%% curvetoCubicSmoothAbs ");
			curvetoCubicAbs((float)(lx*2-cx), (float)(ly*2-cy), (float)x2, (float)y2, (float)x, (float)y);
		}

		@Override
		public void curvetoQuadraticRel(float x1, float y1, float x, float y) throws ParseException {
			//pw.println("%% curvetoQuadraticRel ");
			curvetoQuadraticAbs((float)lx+x1, (float)ly+y1, (float)lx+x, (float)ly+y);
		}

		@Override
		public void curvetoQuadraticAbs(float x1, float y1, float x, float y) throws ParseException {
			//pw.println("%% curvetoQuadraticAbs ");
			pw.println(cxy(x1*upem,y1*upem)+cxy(x*upem,y*upem)+"v");
			lx =(float)x;
			ly =(float)y;
			cx =(float)x1;
			cy =(float)y1;
		}

		@Override
		public void curvetoQuadraticSmoothRel(float x, float y) throws ParseException {
			//pw.println("%% curvetoQuadraticSmoothRel ");
			curvetoQuadraticSmoothAbs((float)lx+x, (float)ly+y);
		}

		@Override
		public void curvetoQuadraticSmoothAbs(float x, float y) throws ParseException {
			//pw.println("%% curvetoQuadraticSmoothAbs ");
			curvetoQuadraticAbs((float)(lx*2-cx),(float)(ly*2-cy),x,y);
		}

		@Override
		public void arcRel(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) throws ParseException {
			pw.println("%% arcRel ");
		}

		@Override
		public void arcAbs(float rx, float ry, float xAxisRotation, boolean largeArcFlag, boolean sweepFlag, float x, float y) throws ParseException {
			pw.println("%% arcAbs ");
		}
	}

	private static final double degrees_to_radians = Math.PI/180.0;

	@SneakyThrows
	public static SvgFontContainer loadSvg(ResourceLoader _rl)
	{
		//File _font = new File("res/fonts/DroidSans.svg");
		//File _font = new File("res/fonts/texgyre/courier/qcrr.svg");

		JAXBContext _context = JAXBContext.newInstance(SvgFontContainer.class);

		SAXParserFactory _spf = SAXParserFactory.newInstance();
		_spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		_spf.setFeature("http://xml.org/sax/features/validation", false);
		// _spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
		XMLReader _xmlReader = _spf.newSAXParser().getXMLReader();
		InputSource _inputSource = new InputSource(_rl.getInputStream());
		SAXSource _source = new SAXSource(_xmlReader, _inputSource);

		Unmarshaller _um = _context.createUnmarshaller();
		return (SvgFontContainer)_um.unmarshal(_source);
	}
}
