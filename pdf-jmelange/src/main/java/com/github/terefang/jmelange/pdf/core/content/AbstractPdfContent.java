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
package com.github.terefang.jmelange.pdf.core.content;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.PdfResNamed;
import com.github.terefang.jmelange.pdf.core.PdfValue;
import com.github.terefang.jmelange.pdf.core.color.PdfColor;
import com.github.terefang.jmelange.pdf.core.color.PdfColorSpace;
import com.github.terefang.jmelange.pdf.core.color.PdfRgbColor;
import com.github.terefang.jmelange.pdf.core.filter.PdfFlateFilter;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.image.PdfImage;
import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.pdf.core.util.Matrix;
import com.github.terefang.jmelange.pdf.core.values.*;
import com.github.terefang.jmelange.pdf.core.values.PdfFormXObject;
import com.github.terefang.jmelange.pdf.core.values.PdfXObject;
import lombok.SneakyThrows;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.github.terefang.jmelange.pdf.core.PDF.geomDP;
import static com.github.terefang.jmelange.pdf.core.PDF.transformDP;

public class AbstractPdfContent extends PdfDictObjectWithStream
{
	PdfPage page;

	public PdfPage getPage()
	{
		return page;
	}
	
	public void setPage(PdfPage page)
	{
		this.page = page;
	}
	
	PdfDict resources = PdfDict.create();
	protected PdfDict fonts = PdfDict.create();
	protected PdfDict xo = PdfDict.create();
	protected PdfDict colorspace = PdfDict.create();
	protected PdfDict properties = PdfDict.create();

	private AffineTransform prevTransInv;
	private PdfFont font;
	private double fontSize;
	private boolean inText;
	private double textLead = 0;
	private double wordSpace = 0;
	private double charSpace = 0;
	private double hScale = 100;

	public PdfFont setFont(PdfFont font, double _size)
	{
		setFont(font.getResource());
		this.fontSize = _size;
		return font;
	}

	public PdfFont setFont(PdfFont font, int _size)
	{
		setFont(font.getResource());
		this.fontSize = _size;
		return font;
	}

	public PdfFont setFont(PdfResource font)
	{
		this.font = (PdfFont) font.get();
		this.addFont(font.getResName(), (PdfDictObject) font.get());
		return this.font;
	}
	
	public PdfFont setFont(PdfResource font, int _size)
	{
		this.font = (PdfFont) font.get();
		this.fontSize = _size;
		this.addFont(font.getResName(), (PdfDictObject) font.get());
		return this.font;
	}
	
	public PdfFont setFont(PdfResource font, double _size)
	{
		this.font = (PdfFont) font.get();
		this.fontSize = _size;
		this.addFont(font.getResName(), (PdfDictObject) font.get());
		return this.font;
	}
	
	public void setFontSize(double s)
	{
		this.fontSize = s;
	}

	public PdfFont getFont() {
		return font;
	}

	public double getFontSize() {
		return fontSize;
	}

	protected AbstractPdfContent(PdfDocument doc, boolean _flate)
	{
		super(doc);
		if(_flate)
		{
			this.setFilter(PdfFlateFilter.create());
		}
		
	}
	
	@Override
	public void writeTo(OutputStream os) throws IOException
	{
		if(this.page==null)
		{
			this.addResource("Font", this.fonts);
			this.addResource("XObject", this.xo);
			this.addResource("ExtGState", getEgs());
			this.addResource("ColorSpace", this.colorspace);
			this.addResource("Properties", this.properties);
			this.addResource("ProcSet", PdfArray.from("PDF", "Text", "ImageC", "ImageB", "ImageI"));

			this.set("Resources", this.resources);
		}
		super.writeTo(os);
	}

	PdfDict egs;

	public PdfDict getEgs()
	{
		if(this.egs==null) this.egs = PdfDict.create();
		return egs;
	}

	public void addResource(String name, PdfValue obj)
	{
		if(this.page==null)
		{
			this.resources.set(name, obj);
		}
		else
		{
			this.page.addResource(name, obj);
		}
	}

	public void addEGState(PdfEGState _state)
	{
		if(this.page==null)
		{
			this.getEgs().set(_state.getResource().getResName(), _state);
		}
		else
		{
			this.page.addEGState(_state);
		}
	}

	public void addFont(PdfFont _font)
	{
		this.addFont(_font.getResource().getResName(), _font);
	}

	public void addFont(String name, PdfDictObject obj)
	{
		if(this.page==null)
		{
			this.fonts.set(name, obj);
		}
		else
		{
			this.page.addFont(name, obj);
		}
	}

	public void addProperty(String name, PdfDictObject obj)
	{
		if(this.page==null)
		{
			this.properties.set(name, obj);
		}
		else
		{
			this.page.addProperty(name, obj);
		}
	}

	public PdfOptionalContentGroup addLayer(String name)
	{
		PdfOptionalContentGroup _cg = this.getDoc().layers.get(name);
		if(_cg==null)
		{
			_cg = PdfOptionalContentGroup.of(this.getDoc(), name);
			this.getDoc().layers.put(name, _cg);
			this.getDoc().newOCGroup(_cg);
		}

		if(this.page==null)
		{
			this.properties.set(_cg.getResource().getResName(), _cg);
		}
		else
		{
			this.page.addProperty(_cg.getResource().getResName(), _cg);
		}
		return _cg;
	}

	public void startLayer(String name)
	{
		PdfOptionalContentGroup _cg = this.addLayer(name);
		this.addContent("\n /OC "+PdfName.of(_cg.getResource().getResName()).asString()+" BDC q\n");
	}

	public void endLayer()
	{
		this.addContent("\n Q EMC \n");
	}



	public void addXObject(String name, PdfXObject obj)
	{
		if(this.page==null)
		{
			this.xo.set(name, obj);
		}
		else
		{
			this.page.addXObject(name, obj);
		}
	}
	
	public void addColorSpace(String name, PdfColorSpace obj)
	{
		if(this.page==null)
		{
			this.colorspace.set(name, obj);
		}
		else
		{
			this.page.addColorSpace(name, obj);
		}
	}
	
	public void setFlateFilter(boolean ff)
	{
		if(ff)
		{
			this.setFilter(PdfFlateFilter.create());
		}
		else
		{
			this.setFilter(null);
		}
	}
	
	public void addContentLn(String s)
	{
		PrintStream wr = this.getPrintStream();
		wr.print(s);
		wr.print("\n");
		wr.flush();
	}

	public void addContent(String s)
	{
		PrintStream wr = this.getPrintStream();
		wr.print(s);
		wr.print(" ");
		wr.flush();
	}

	public void addContent(byte[] s)
	{
		OutputStream wr = this.getOutputStream();
		try
		{
			wr.write(s);
			wr.flush();
		}catch(Exception xe) {}
	}
	
	
	public static double[] _translate(double _x, double _y)
	{
		return new double[]{1,0,0,1,_x,_y};
	}
	
	public void translate(double _x, double _y)
	{
		transform(Collections.singletonMap("translate", new double[]{_x,_y}));
	}
	
	public static double[] _rotate(double _a)
	{
		return new double[]{(double) Math.cos(Math.toRadians(_a)), (double) Math.sin(Math.toRadians(_a)), (double) -Math.sin(Math.toRadians(_a)), (double) Math.cos(Math.toRadians(_a)),0,0};
	}
	
	public void rotate(double _a)
	{
		transform(Collections.singletonMap("rotate", new double[]{_a}));
	}
	
	public static double[] _scale(double _x, double _y)
	{
		return new double[]{_x,0,0,_y,0,0};
	}
	
	public void scale(double _x, double _y)
	{
		transform(Collections.singletonMap("scale", new double[]{_x,_y}));
	}
	
	public static double[] _skew(double _a, double _b)
	{
		return new double[]{1, Math.tan(Math.toRadians(_a)), Math.tan(Math.toRadians(_b)),1,0,0};
	}
	
	public void skew(double _a, double _b)
	{
		transform(Collections.singletonMap("skew", new double[]{_a,_b}));
	}
	
	public double[] _transform(Map<String,double[]> _transform)
	{
		Matrix matrix = new Matrix(new double[][]{{1d, 0d, 0d}, {0d, 1d, 0d}, {0d, 0d, 1d}});
		for(String _mtype : new String[]{"matrix", "skew", "scale", "rotate", "translate" })
		{
			double[] _m = null;
			if(_transform.containsKey(_mtype) && "translate".equalsIgnoreCase(_mtype))
			{
				double[] _v = _transform.get(_mtype);
				_m = _translate(_v[0], _v[1]);
			}
			else
			if(_transform.containsKey(_mtype) && "rotate".equalsIgnoreCase(_mtype))
			{
				double[] _v = _transform.get(_mtype);
				_m = _rotate(_v[0]);
			}
			else
			if(_transform.containsKey(_mtype) && "scale".equalsIgnoreCase(_mtype))
			{
				double[] _v = _transform.get(_mtype);
				_m = _scale(_v[0],_v[1]);
			}
			else
			if(_transform.containsKey(_mtype) && "skew".equalsIgnoreCase(_mtype))
			{
				double[] _v = _transform.get(_mtype);
				_m = _skew(_v[0],_v[1]);
			}
			else
			if(_transform.containsKey(_mtype) && "matrix".equalsIgnoreCase(_mtype))
			{
				double[] _v = _transform.get(_mtype);
				_m = _v;
			}
			
			if(_m!=null)
			{
				Matrix _t = new Matrix(new double[][]{{_m[0], _m[1], 0d}, {_m[2], _m[3], 0d}, {_m[4], _m[5], 1d}});
				matrix = matrix.multiply(_t);
			}
		}
		
		if(_transform.containsKey("point"))
		{
			double[] _p = _transform.get("point");
			Matrix _point = new Matrix(new double[]{_p[0], _p[1], 1d});
			_point = _point.multiply(matrix);
			return new double[]{(double) _point.getEntry(0, 0), (double) _point.getEntry(1, 0)};
		}
		else
		{
			return new double[]{(double) matrix.getEntry(0, 0), (double) matrix.getEntry(0, 1),
					(double) matrix.getEntry(1, 0), (double) matrix.getEntry(1, 1),
					(double) matrix.getEntry(2, 0), (double) matrix.getEntry(2, 1)};
		}
	}
	
	public void transform(Map<String,double[]> _transform)
	{
		double[] _m = _transform(_transform);
		matrix(_m);
	}
	
	public void matrix(boolean _tm, double... _matrix)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(transformDP(_matrix[0])).append(" ");
		sb.append(transformDP(_matrix[1])).append(" ");
		sb.append(transformDP(_matrix[2])).append(" ");
		sb.append(transformDP(_matrix[3])).append(" ");
		sb.append(transformDP(_matrix[4])).append(" ");
		sb.append(transformDP(_matrix[5])).append(" ");

		if(_tm)
		{
			sb.append("Tm ");
		}
		else
		{
			sb.append("cm ");
		}
		addContent(sb.toString());
	}
	
	public void matrix(double... _matrix)
	{
		matrix(this.inText, _matrix);
	}
	
	public void save()
	{
		this.addContent("q ");
	}
	
	public void restore()
	{
		this.addContent("Q ");
	}
	
	public void linewidth(double _w)
	{
		this.addContent(geomDP(_w)+" w ");
	}
	
	public void linecap(int _n)
	{
		this.addContent(_n+" J ");
	}
	
	public void linejoin(int _n)
	{
		this.addContent(_n+" J ");
	}
	
	public void meterlimit(double _l)
	{
		this.addContent(geomDP(_l)+" M ");
	}
	
	public void linedash()
	{
		this.addContent("[ ] 0 d ");
	}
	
	public void linedash(float _l)
	{
		this.addContent("[ "+geomDP(_l)+" ] 0 d ");
	}

	public void linedash(float... _l)
	{
		linedashWithOffset(0, _l);
	}

	public void linedashWithOffset(double _sh, float... _l)
	{
		String[] _s = new String[_l.length];
		for(int i =0; i<_l.length; i++)
		{
			_s[i] = geomDP(_l[i]);
		}
		this.addContent("[ " + CommonUtil.join(_s, " ") + " ] "+geomDP(_sh)+" d ");
	}

	public void linedash(int _dash)
	{
		this.addContent("[ "+_dash+" ] 0 d ");
	}

	public void linedash(int... _dash)
	{
		this.linedashWithOffset(0d, _dash);
	}

	public void linedashWithOffset(double _sh, int... _l)
	{
		String[] _s = new String[_l.length];
		for(int i =0; i<_l.length; i++)
		{
			_s[i] = Integer.toString(_l[i]);
		}
		this.addContent("[ " + CommonUtil.join(_s, " ") + " ] "+geomDP(_sh)+" d ");
	}

	public void flatness(int _f)
	{
		this.addContent(_f+" i ");
	}
	
	public void egstate(PdfResNamed _egs)
	{
		this.addResource(_egs.getResName(), (PdfValue) _egs);
		this.addContent("/"+_egs.getResName()+" gs ");
	}
	
	double px,py,pmx,pmy;

	public double getPx()
	{
		return this.px;
	}

	public double getPy()
	{
		return this.py;
	}

	public double getPmx()
	{
		return this.pmx;
	}

	public double getPmy()
	{
		return this.pmy;
	}

	public void moveTo(double _x, double _y)
	{
		this.addContent(geomDP(_x)+" "+geomDP(_y)+" m ");
		px = pmx = _x;
		py = pmy = _y;
	}
	
	public void lineTo(double _x, double _y)
	{
		this.addContent(geomDP(_x)+" "+geomDP(_y)+" l ");
		px=_x;
		py=_y;
	}
	
	public void curveTo(double _x2, double _y2, double _x3, double _y3, boolean _startOrEnd)
	{
		this.addContent(geomDP(_x2)+" "+geomDP(_y2)+" "+geomDP(_x3)+" "+geomDP(_y3)+(_startOrEnd ? " v " : " y "));
		px=_x3;
		py=_y3;
	}
	
	public void curveTo(double _x1, double _y1, double _x2, double _y2, double _x3, double _y3)
	{
		this.addContent(geomDP(_x1)+" "+geomDP(_y1)+" "+geomDP(_x2)+" "+geomDP(_y2)+" "+geomDP(_x3)+" "+geomDP(_y3)+" c ");
		px=_x3;
		py=_y3;
	}
	
	public void rectangle(double _x, double _y, double _w, double _h)
	{
		this.addContent(geomDP(_x)+" "+geomDP(_y)+" "+geomDP(_w)+" "+geomDP(_h)+" re ");
	}
	
	public void rectanglexy(double _x, double _y, double _x2, double _y2)
	{
		this.addContent(geomDP(_x)+" "+geomDP(_y)+" "+geomDP(_x2-_x)+" "+geomDP(_y2-_y)+" re ");
	}
	
	public void closepath()
	{
		this.addContent("h ");
		px=pmx;
		py=pmy;
	}
	
	public void endpath()
	{
		this.addContent("n ");
	}
	
	public void poly(double... _p)
	{
		this.moveTo(_p[0], _p[1]);
		pmx=_p[0];
		pmy=_p[1];
		for(int i = 3; i<_p.length; i+=2)
		{
			this.lineTo(_p[i-1], _p[i]);
			px=_p[i-1];
			py=_p[i];
		}
	}
	
	public void hline(double _x)
	{
		this.lineTo(_x, py);
	}

	public void vline(double _y)
	{
		this.lineTo(px, _y);
	}
	
	public void spline(double... _p)
	{
		for(int i=3; i<_p.length; i+=4)
		{
			double _cx = _p[i-3];
			double _cy = _p[i-2];
			double _x = _p[i-1];
			double _y = _p[i];
			double _c1x = (2d*_cx+px)/3d;
			double _c1y = (2d*_cy+py)/3d;
			double _c2x = (2d*_cx+_x)/3d;
			double _c2y = (2d*_cy+_y)/3d;
			this.curveTo(_c1x,_c1y,_c2x,_c2y,_x,_y);
		}
	}
	
	public double[] _arc2curve(double _a, double _b, double _alpha, double _beta)
	{
		if (Math.abs(_beta-_alpha) > 15d)
		{
			double[] _part1 = _arc2curve(_a,_b,_alpha,(_beta+_alpha)/2d);
			double[] _part2 = _arc2curve(_a,_b,(_beta+_alpha)/2d,_beta);
        	double[] _res = new double [_part1.length+_part2.length];
			System.arraycopy(_part1,0, _res, 0, _part1.length);
			System.arraycopy(_part2,0, _res, _part1.length, _part2.length);
			return _res;
		}
		else 
		{
			_alpha = Math.toRadians(_alpha);
			_beta  = Math.toRadians(_beta);
			
			double _bcp = (4.0/3 * (1 - Math.cos((_beta - _alpha)/2)) / Math.sin((_beta - _alpha)/2));
			double _sin_alpha = Math.sin(_alpha);
			double _sin_beta =  Math.sin(_beta);
			double _cos_alpha = Math.cos(_alpha);
			double _cos_beta =  Math.cos(_beta);
			
			double _p0_x = _a * _cos_alpha;
			double _p0_y = _b * _sin_alpha;
			double _p1_x = _a * (_cos_alpha - _bcp * _sin_alpha);
			double _p1_y = _b * (_sin_alpha + _bcp * _cos_alpha);
			double _p2_x = _a * (_cos_beta + _bcp * _sin_beta);
			double _p2_y = _b * (_sin_beta - _bcp * _cos_beta);
			double _p3_x = _a * _cos_beta;
			double _p3_y = _b * _sin_beta;
			
			return new double[]{ _p0_x,_p0_y,_p1_x,_p1_y,_p2_x,_p2_y,_p3_x,_p3_y };
		}
	}
	
	public void arc(double _x, double _y, double _a, double _b, double _alpha, double _beta, boolean _move)
	{
		double[] _points = _arc2curve(_a,_b,_alpha,_beta);
		
		double _p0_x= _x + _points[0];
		double _p0_y= _y + _points[1];
		
		if(_move)
		{
			this.moveTo(_p0_x,_p0_y);
		}
		
		for(int i = 7; i<_points.length; i+=8)
		{
			this.curveTo(_points[i-5],_points[i-4],_points[i-3],_points[i-2],_points[i-1],_points[i]);
		}
	}
	
	public void ellipse(double _x,double _y,double _a,double _b)
	{;
		this.arc(_x,_y,_a,_b,0,360,true);
		this.closepath();
	}
	
	public void circle(double _x,double _y,double _r)
	{;
		this.arc(_x,_y,_r,_r,0,360,true);
		this.closepath();
	}
	
	public void pie(double _x, double _y, double _a, double _b, double _alpha, double _beta)
	{
		double[] _p = _arc2curve(_a,_b,_alpha,_beta);
		this.moveTo(_x,_y);
		this.lineTo(_p[0]+_x,_p[1]+_y);
		this.arc(_x,_y,_a,_b,_alpha,_beta,false);
		this.closepath();
	}
	
	public void stroke()
	{
		this.addContent("S ");
	}
	
	public void fill()
	{
		this.addContent("f ");
	}
	
	public void fill(boolean _evenOdd)
	{
		this.addContent(_evenOdd ? "f* " : "f ");
	}
	
	public void fillStroke()
	{
		this.addContent("B ");
	}
	
	public void fillStroke(boolean _evenOdd)
	{
		this.addContent(_evenOdd ? "B* " : "B ");
	}
	
	public void clip()
	{
		this.addContent("W ");
	}
	
	public void clip(boolean _evenOdd)
	{
		this.addContent(_evenOdd ? "W* " : "W ");
	}
	
	public void fillColorGray(double _c)
	{
		this.addContent( geomDP(_c)+" g ");
	}
	
	public void strokeColorGray(double _c)
	{
		this.addContent( geomDP(_c)+" G ");
	}
	
	public void fillColorRGB(double _r, double _g, double _b)
	{
		this.addContent( geomDP(_r)+" "+geomDP(_g)+" "+geomDP(_b)+" rg ");
	}
	
	public void strokeColorRGB(double _r, double _g, double _b)
	{
		this.addContent( geomDP(_r)+" "+geomDP(_g)+" "+geomDP(_b)+" RG ");
	}

	public void fillColorCMYK(double _c, double _m, double _y, double _k)
	{
		this.addContent( geomDP(_c)+" "+geomDP(_m)+" "+geomDP(_y)+" "+geomDP(_k)+" k ");
	}

	public void strokeColorCMYK(double _c, double _m, double _y, double _k)
	{
		this.addContent( geomDP(_c)+" "+geomDP(_m)+" "+geomDP(_y)+" "+geomDP(_k)+" K ");
	}

	public void fillColorHSL(double _h, double _s, double _l)
	{
		PdfRgbColor _C = PdfColor.fromHSL((float) _h, (float) _s, (float) _l);
		_C.setFillColor(this);
	}

	public void strokeColorHSL(double _h, double _s, double _l)
	{
		PdfRgbColor _C = PdfColor.fromHSL((float) _h, (float) _s, (float) _l);
		_C.setStrokeColor(this);
	}

	public void fillColorHSV(double _h, double _s, double _v)
	{
		PdfRgbColor _C = PdfColor.fromHSV((float) _h, (float) _s, (float) _v);
		_C.setFillColor(this);
	}

	public void strokeColorHSV(double _h, double _s, double _v)
	{
		PdfRgbColor _C = PdfColor.fromHSV((float) _h, (float) _s, (float) _v);
		_C.setStrokeColor(this);
	}

	public void fillColorHWB(double _h, double _w, double _b)
	{
		PdfRgbColor _C = PdfColor.fromHWB((float) _h, (float) _w, (float) _b);
		_C.setFillColor(this);
	}

	public void strokeColorHWB(double _h, double _w, double _b)
	{
		PdfRgbColor _C = PdfColor.fromHWB((float) _h, (float) _w, (float) _b);
		_C.setStrokeColor(this);
	}

	public void fillColor(int _c)
	{
		this.fillColorGray(_c/255d);
	}

	public void strokeColor(int _c)
	{
		this.strokeColorGray(_c/255d);
	}

	public void fillColor(String _color)
	{
		this.colorByString(_color, true);
	}

	public void strokeColor(String _color)
	{
		this.colorByString(_color, false);
	}

	static Properties COLOR_ALIASES = new Properties();

	@SneakyThrows
	private void colorByString(String _color, boolean _fill)
	{
		_color=_color.trim().toLowerCase();

		if(COLOR_ALIASES.size()==0)
		{
			COLOR_ALIASES.load(ClasspathResourceLoader.of("color/names.properties", null).getInputStream());
		}

		if(COLOR_ALIASES.containsKey(_color)) _color = COLOR_ALIASES.getProperty(_color).trim();

		if(_color.startsWith("#") && _color.length()==4)
		{
			int _cr = CommonUtil.checkInteger("0x"+_color.substring(1,2));
			_cr |= (_cr<<4);
			int _cg = CommonUtil.checkInteger("0x"+_color.substring(2,3));
			_cg |= (_cg<<4);
			int _cb = CommonUtil.checkInteger("0x"+_color.substring(3,4));
			_cb |= (_cb<<4);
			if(_fill)
			{
				this.fillColor(_cr,_cg,_cb);
			}
			else
			{
				this.strokeColor(_cr,_cg,_cb);
			}
		}
		else
		if(_color.startsWith("#") && _color.length()==5)
		{
			int _cc = CommonUtil.checkInteger("0x"+_color.substring(1,2));
			_cc |= (_cc<<4);
			int _cm = CommonUtil.checkInteger("0x"+_color.substring(2,3));
			_cm |= (_cm<<4);
			int _cy = CommonUtil.checkInteger("0x"+_color.substring(3,4));
			_cy |= (_cy<<4);
			int _ck = CommonUtil.checkInteger("0x"+_color.substring(3,4));
			_ck |= (_ck<<4);
			if(_fill)
			{
				this.fillColor(_cc,_cm,_cy,_ck);
			}
			else
			{
				this.strokeColor(_cc,_cm,_cy,_ck);
			}
		}
		else
		if(_color.startsWith("#") && _color.length()==7)
		{
			int _cr = CommonUtil.checkInteger("0x"+_color.substring(1,3));
			int _cg = CommonUtil.checkInteger("0x"+_color.substring(3,5));
			int _cb = CommonUtil.checkInteger("0x"+_color.substring(5,7));
			if(_fill)
			{
				this.fillColor(_cr,_cg,_cb);
			}
			else
			{
				this.strokeColor(_cr,_cg,_cb);
			}
		}
		else
		if(_color.startsWith("#") && _color.length()==9)
		{
			int _cc = CommonUtil.checkInteger("0x"+_color.substring(1,3));
			int _cm = CommonUtil.checkInteger("0x"+_color.substring(3,5));
			int _cy = CommonUtil.checkInteger("0x"+_color.substring(5,7));
			int _ck = CommonUtil.checkInteger("0x"+_color.substring(7,9));
			if(_fill)
			{
				this.fillColor(_cc,_cm,_cy,_ck);
			}
			else
			{
				this.strokeColor(_cc,_cm,_cy,_ck);
			}
		}
		else
		if(_color.startsWith("rgb(") && _color.endsWith(")"))
		{
			String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
			int[] _cv = new int[_parts.length];
			for(int i=0; i<_parts.length; i++)
			{
				String _ct = _parts[i].trim();
				if(_ct.endsWith("%"))
				{
					_cv[i] = (int) ((Float.parseFloat(_ct.substring(0, _ct.length()-1))*255f)/100f);
				}
				else
				{
					_cv[i] = CommonUtil.checkInteger(_ct);
				}
			}

			if(_fill)
			{
				this.fillColor(_cv[0],_cv[1],_cv[2]);
			}
			else
			{
				this.strokeColor(_cv[0],_cv[1],_cv[2]);
			}
		}
		else
		if(_color.startsWith("cmyk(") && _color.endsWith(")"))
		{
			String[] _parts = CommonUtil.split(_color.substring(5,_color.length()-1), ",");
			int[] _cv = new int[_parts.length];
			for(int i=0; i<_parts.length; i++)
			{
				String _ct = _parts[i].trim();
				if(_ct.endsWith("%"))
				{
					_cv[i] = (int) ((Float.parseFloat(_ct.substring(0, _ct.length()-1))*255f)/100f);
				}
				else
				{
					_cv[i] = CommonUtil.checkInteger(_ct);
				}
			}

			if(_fill)
			{
				this.fillColor(_cv[0],_cv[1],_cv[2],_cv[3]);
			}
			else
			{
				this.strokeColor(_cv[0],_cv[1],_cv[2],_cv[3]);
			}
		}
		else
		if(_color.startsWith("hsl(") && _color.endsWith(")"))
		{
			String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
			float[] _cv = new float[_parts.length];

			_cv[0] = CommonUtil.toFloat(_parts[0].trim());

			for(int i=1; i<_parts.length; i++)
			{
				String _ct = _parts[i].trim();
				if(_ct.endsWith("%"))
				{
					_cv[i] = CommonUtil.toFloat(_ct.substring(0, _ct.length()-1));
				}
				else
				{
					_cv[i] = CommonUtil.toFloat(_ct)*100;
				}
			}

			PdfRgbColor _c0 = PdfColor.fromHSL(_cv[0], _cv[1], _cv[2]);
			if(_fill)
			{
				_c0.setFillColor(this);
			}
			else
			{
				_c0.setStrokeColor(this);
			}
		}
		else
		if(_color.startsWith("hsv(") && _color.endsWith(")"))
		{
			String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
			float[] _cv = new float[_parts.length];

			_cv[0] = CommonUtil.toFloat(_parts[0].trim());

			for(int i=1; i<_parts.length; i++)
			{
				String _ct = _parts[i].trim();
				if(_ct.endsWith("%"))
				{
					_cv[i] = CommonUtil.toFloat(_ct.substring(0, _ct.length()-1));
				}
				else
				{
					_cv[i] = CommonUtil.toFloat(_ct)*100;
				}
			}

			PdfRgbColor _c0 = PdfColor.fromHSV(_cv[0], _cv[1], _cv[2]);
			if(_fill)
			{
				_c0.setFillColor(this);
			}
			else
			{
				_c0.setStrokeColor(this);
			}
		}
		else
		if(_color.startsWith("hwb(") && _color.endsWith(")"))
		{
			String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
			float[] _cv = new float[_parts.length];

			_cv[0] = CommonUtil.toFloat(_parts[0].trim());

			for(int i=1; i<_parts.length; i++)
			{
				String _ct = _parts[i].trim();
				if(_ct.endsWith("%"))
				{
					_cv[i] = CommonUtil.toFloat(_ct.substring(0, _ct.length()-1));
				}
				else
				{
					_cv[i] = CommonUtil.toFloat(_ct)*100;
				}
			}

			PdfRgbColor _c0 = PdfColor.fromHWB(_cv[0], _cv[1], _cv[2]);
			if(_fill)
			{
				_c0.setFillColor(this);
			}
			else
			{
				_c0.setStrokeColor(this);
			}
		}
		else
		if(_color.startsWith("hcg(") && _color.endsWith(")"))
		{
			String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
			float[] _cv = new float[_parts.length];

			_cv[0] = CommonUtil.toFloat(_parts[0].trim());

			for(int i=1; i<_parts.length; i++)
			{
				String _ct = _parts[i].trim();
				if(_ct.endsWith("%"))
				{
					_cv[i] = CommonUtil.toFloat(_ct.substring(0, _ct.length()-1));
				}
				else
				{
					_cv[i] = CommonUtil.toFloat(_ct)*100;
				}
			}

			PdfRgbColor _c0 = PdfColor.fromHCG(_cv[0], _cv[1], _cv[2]);
			if(_fill)
			{
				_c0.setFillColor(this);
			}
			else
			{
				_c0.setStrokeColor(this);
			}
		}
		else
		if(_color.startsWith("xyz(") && _color.endsWith(")"))
		{
			String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
			float[] _cv = new float[_parts.length];

			for(int i=0; i<_parts.length; i++)
			{
				String _ct = _parts[i].trim();
				_cv[i] = CommonUtil.toFloat(_ct);
			}

			PdfRgbColor _c0 = PdfColor.fromXYZ(_cv[0], _cv[1], _cv[2]);
			if(_fill)
			{
				_c0.setFillColor(this);
			}
			else
			{
				_c0.setStrokeColor(this);
			}
		}
		else
		if(_color.startsWith("lab(") && _color.endsWith(")"))
		{
			String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
			float[] _cv = new float[_parts.length];

			for(int i=0; i<_parts.length; i++)
			{
				String _ct = _parts[i].trim();
				_cv[i] = CommonUtil.toFloat(_ct);
			}

			PdfRgbColor _c0 = PdfColor.fromLAB(_cv[0], _cv[1], _cv[2]);
			if(_fill)
			{
				_c0.setFillColor(this);
			}
			else
			{
				_c0.setStrokeColor(this);
			}
		}
		else
		if(_color.startsWith("lch(") && _color.endsWith(")"))
		{
			String[] _parts = CommonUtil.split(_color.substring(4,_color.length()-1), ",");
			float[] _cv = new float[_parts.length];

			for(int i=0; i<_parts.length; i++)
			{
				String _ct = _parts[i].trim();
				_cv[i] = CommonUtil.toFloat(_ct);
			}

			PdfRgbColor _c0 = PdfColor.fromLCH(_cv[0], _cv[1], _cv[2]);
			if(_fill)
			{
				_c0.setFillColor(this);
			}
			else
			{
				_c0.setStrokeColor(this);
			}
		}
		else
		{
			throw new IllegalArgumentException(_color);
		}
	}

	public void fillColor(int _r, int _g, int _b)
	{
		this.fillColorRGB(_r/255d, _g/255d, _b/255d);
	}
	
	public void strokeColor(int _r, int _g, int _b)
	{
		this.strokeColorRGB(_r/255d, _g/255d, _b/255d);
	}
	
	public void fillColor(int _c, int _m, int _y, int _k)
	{
		this.fillColorCMYK(_c/255d, _m/255d, _y/255d, _k/255d);
	}
	
	public void strokeColor(int _c, int _m, int _y, int _k)
	{
		this.strokeColorCMYK(_c/255d, _m/255d, _y/255d, _k/255d);
	}
	
	public void fillColorSpace(PdfColorSpace _space)
	{
		if("DeviceGray".equalsIgnoreCase(_space.getResource().getResName())
				|| "DeviceCMYK".equalsIgnoreCase(_space.getResource().getResName())
				|| "DeviceRGB".equalsIgnoreCase(_space.getResource().getResName())
		)
		{
			// IGNORE
		}
		else
		{
			this.addColorSpace(_space.getResource().getResName(), _space);
		}
		this.addContent( PdfName.of(_space.getResource().getResName()).asString()+" cs ");
	}
	
	public void strokeColorSpace(PdfColorSpace _space)
	{
		this.addColorSpace(_space.getResource().getResName(), _space);
		this.addContent( PdfName.of(_space.getResource().getResName()).asString()+" CS ");
	}
	
	public void fillSpaceColor(boolean _nf, float... _c)
	{
		for(double _n : _c)
		{
			this.addContent( geomDP(_n)+" ");
		}
		this.addContent(_nf ? " scn ":" sc ");
	}
	
	public void fillSpaceColor(boolean _nf, PdfColor _color)
	{
		fillColorSpace(_color.getColorSpace(this.getDoc()));
		fillSpaceColor(_nf, _color.getComponents());
	}
	
	public void strokeSpaceColor(boolean _nf, float... _c)
	{
		for(double _n : _c)
		{
			this.addContent( geomDP(_n)+" ");
		}
		this.addContent(_nf ? " SCN ":" SC ");
	}

	public void strokeSpaceColor(boolean _nf, PdfColor _color)
	{
		strokeColorSpace(_color.getColorSpace(this.getDoc()));
		strokeSpaceColor(_nf, _color.getComponents());
	}

	Map<Integer,PdfEGState> fillAlphaMap = new HashMap<>();
	Map<Integer,PdfEGState> strokeAlphaMap = new HashMap<>();

	public void fillAlpha(int _c)
	{
		this.fillAlpha(_c/255f);
	}

	public void fillAlpha(float _c)
	{
		PdfEGState _egs = null;
		Integer _n = (int)(_c*100f);
		if(fillAlphaMap.containsKey(_n))
		{
			_egs = fillAlphaMap.get(_n);
		}
		else
		{
			_egs = PdfEGState.create(this.getDoc());
			_egs.fillAlpha(_c);
			this.addEGState(_egs);
			fillAlphaMap.put(_n, _egs);
		}
		this.addContent(" "+PdfName.of(_egs.getResource().getResName()).asString()+" gs ");
	}

	public void strokeAlpha(int _c)
	{
		this.strokeAlpha(_c/255f);
	}

	public void strokeAlpha(float _c)
	{
		PdfEGState _egs = null;
		Integer _n = (int)(_c*100f);
		if(strokeAlphaMap.containsKey(_n))
		{
			_egs = strokeAlphaMap.get(_n);
		}
		else
		{
			_egs = PdfEGState.create(this.getDoc());
			_egs.strokeAlpha(_c);
			this.addEGState(_egs);
			strokeAlphaMap.put(_n, _egs);
		}
		this.addContent(" "+PdfName.of(_egs.getResource().getResName()).asString()+" gs ");
	}

	public void form(PdfResource _form, double _x, double _y)
	{
		this.addXObject(_form.getResName(), (PdfXObject) _form.get());
		this.save();
		this.addContent( "1 0 0 1 "+geomDP(_x)+" "+geomDP(_y)+" cm /"+_form.getResName()+" Do ");
		this.restore();
	}

	public void form(PdfFormXObject _form, double _x, double _y)
	{
		this.form(_form.getResource(), _x, _y);
	}

	public void form(PdfFormXObject _form)
	{
		this.form(_form.getResource());
	}

	public void form(PdfResource _form)
	{
		this.addXObject(_form.getResName(), (PdfXObject) _form.get());
		this.addContent( " /"+_form.getResName()+" Do ");
	}

	public void image(PdfImage _img, double _x, double _y, double _w, double _h)
	{
		this.addXObject(_img.getResource().getResName(), _img);
		this.save();
		this.addContent( geomDP(_w)+" 0 0 "+geomDP(_h)+" "+geomDP(_x)+" "+geomDP(_y)+" cm /"+_img.getResource().getResName()+" Do ");
		this.restore();
	}

	public void image(PdfImage _img)
	{
		this.addXObject(_img.getResource().getResName(), _img);
		this.addContent("/"+_img.getResource().getResName()+" Do ");
	}

	public void image(PdfImage img, double x, double y)
	{
		this.image(img, x, y, (double)img.getWidth(), (double)img.getHeight());
	}

	public void image(PdfImage img, double x, double y, double _s)
	{
		this.image(img, x, y, img.getWidth()*_s, img.getHeight()*_s);
	}

	public double getWordSpace() {
		return wordSpace;
	}

	public double getCharSpace() {
		return charSpace;
	}

	public double gethScale() {
		return hScale;
	}

	public void startText()
	{
		this.addContent("\nBT ");
		this.inText = true;
	}

	public void endText()
	{
		this.addContent("ET \n");
		this.inText = false;
	}
	
	public void charspace(double _factor)
	{
		this.charSpace = _factor;
		this.addContent(transformDP(_factor)+" Tc ");
	}
	
	public void wordspace(double _factor)
	{
		this.wordSpace = _factor;
		this.addContent(transformDP(_factor)+" Tw ");
	}
	
	public void hscale(double _factor)
	{
		this.hScale = _factor;
		this.addContent(transformDP(_factor)+" Tz ");
	}
	
	public void lead(double _distance)
	{
		this.addContent(geomDP(_distance)+" TL ");
		this.textLead = _distance;
	}
	
	public void renderMode(int _m)
	{
		this.addContent(_m+" Tr ");
	}
	
	public void rise(double _distance)
	{
		this.addContent(geomDP(_distance)+" Ts ");
	}
	
	public void distance(double _dx, double _dy)
	{
		this.addContent(geomDP(_dx)+" "+geomDP(_dy)+" Td ");
	}
	
	public void cr()
	{
		this.addContent("T* ");
	}
	
	public void cr(double _offset)
	{
		this.addContent("0 "+geomDP(_offset)+" Td ");
	}
	
	public void nl()
	{
		this.addContent("T* ");
	}
	
	public void text(String text)
	{
		this.addContent(this.font.text(text, 0, this.wordSpace*(1000d/this.fontSize), this.charSpace*(1000d/this.fontSize)));
	}
	
	public void text(String text, double _indent)
	{
		this.addContent(this.font.text(text, (_indent*(1000d/this.fontSize)*(100d/this.hScale)), this.wordSpace*(1000d/this.fontSize), this.charSpace*(1000d/this.fontSize)));
	}
	
	public void text_center(String text)
	{
		this.text(text, -this.advancewidth(text)/2d);
	}
	
	public void text_right(String text)
	{
		this.text(text, -this.advancewidth(text));
	}
	
	public void text_justified(String text, double _width, boolean _ws)
	{
		double _initial_width = this.advancewidth(text);
		int _sc = PDF.countSpaces(text);
		double _fs = this.fontSize;
		double _T = ((_width-_initial_width)/_sc)*6d;
		if(_T<_fs)
		{
			_ws=false;
		}
		
		if(_ws)
		{
			double _wordspace = this.wordSpace;
			double _xws = (_width-_initial_width)/_sc;
			this.wordspace(_xws);
			this.text(text);
			this.wordspace(_wordspace);
		}
		else
		{
			double _hscale = this.hScale;
			double _xhs = 100d*_width/_initial_width;
			this.hscale(_xhs);
			this.text(text);
			this.hscale(_hscale);
		}
	}

	public String[] _text_fill_line(String text, double _width)
	{
		return this._text_fill_line(text, _width, false);
	}

	//, PdfFont _font, double _size, double _wordSpace, double _charSpace, double _hScale
	public String[] _text_fill_line(String text, double _width, boolean _spillover)
	{
		String[] _parts = CommonUtil.split(text, " ");
		double _adv = 0;
		int i = 0;
		boolean _fullLine = true;
		for(i = 0 ; i<_parts.length; i++)
		{
			_adv += this.advancewidth(_parts[i]);
			if(i>0)
			{
				_adv += this.advancewidth(" ");
			}
			if(_adv >= _width)
			{
				_fullLine = false;
				break;
			}
		}
		
		if(_fullLine)
		{
			return new String[]{ text, null};
		}
		else
		{
			if(!_spillover && i>0)
			{
				i-=1;
			}
			String[] _print = new String[i+1];
			System.arraycopy(_parts, 0, _print, 0, i+1);
			String[] _ret = new String[_parts.length-_print.length];
			System.arraycopy(_parts, _print.length, _ret, 0, _ret.length);
			return new String[]{ CommonUtil.join(_print, " "), CommonUtil.join(_ret, " ")};
		}
	}
	
	public String text_fill_left(String _text, double _width, boolean _spillover)
	{
		String[] _parts = this._text_fill_line(_text, _width,_spillover);
		this.text(_parts[0]);
		return _parts[1];
	}
	
	public String text_fill_center(String _text, double _width, boolean _spillover)
	{
		String[] _parts = this._text_fill_line(_text, _width,_spillover);
		this.text_center(_parts[0]);
		return _parts[1];
	}
	
	public String text_fill_right(String _text, double _width, boolean _spillover)
	{
		String[] _parts = this._text_fill_line(_text, _width,_spillover);
		this.text_right(_parts[0]);
		return _parts[1];
	}
	
	public String text_fill_justified(String _text, double _width, boolean _spillover)
	{
		String[] _parts = this._text_fill_line(_text, _width,_spillover);
		this.text_justified(_parts[0], _width, false);
		return _parts[1];
	}
	
	public float paragraph(String _text, double _lead, double _width, double _height)
	{
		return this.paragraph(_text, _lead, _width, _height, false, false);
	}
	
	public float paragraph(String _text, double _lead, double _width, double _height, boolean _spillover, boolean _justified)
	{
		float _h = 0;
		_text = _text.replaceAll("\\s", " ");
		while((_h<_height) && _text!=null && _text.length()!=0)
		{
			String[] _parts = this._text_fill_line(_text, _width, _spillover);
			_text = _parts[1];
			if(_justified && _text==null)
			{
				this.text(_parts[0]);
			}
			else
			if(_justified && !_spillover)
			{
				this.text_justified(_parts[0], _width, true);
			}
			else
			if(_justified)
			{
				this.text_justified(_parts[0], _width, true);
			}
			else
			{
				this.text(_parts[0]);
			}
			this.cr(_lead);
			_h+=_lead;
		}
		return _h;
	}

	public void font(PdfResource font, double _size)
	{
		this.setFont(font);
		this.setFontSize(_size);
		this.font(font.getResName(), _size);
	}

	public void font(PdfFont font, double _size)
	{
		this.setFont(font.getResource());
		this.setFontSize(_size);
		this.font(font.getResource().getResName(), _size);
	}

	public void font(PdfName font, double _size)
	{
		this.addContent(font.asString()+" "+geomDP(_size)+" Tf ");
	}

	public void font(String font, double _size)
	{
		this.font(PdfName.of(font), _size);
	}
	
	public double advancewidth(String _text)
	{
		return PDF.advancewidth(_text, this.font, this.fontSize, this.wordSpace, this.charSpace, this.hScale);
	}
	
	public void moveText(double x, double y)
	{
		this.addContent(geomDP(x)+" "+geomDP(y)+" Td ");
	}


	public void drawString(String text, float x, float y)
	{
		this.drawString(text, x, y, "-", 0f, 100f);
	}

	public void drawString(String text, float x, float y, String _align)
	{
	}

	public void drawString(String text, float x, float y, String _align, float _w)
	{
		this.drawString(text, x, y, _align, _w, 100f);
	}

	public void drawString(String text, float x, float y, String _align, float _w, float _hs)
	{
		if(text==null) return;
		if(text.length()==0) return;
		this.startText();
		this.hscale(_hs);
		this.font(this.font, this.fontSize);
		if("right".equalsIgnoreCase(_align))
		{
			this.moveText(x+_w,y);
			this.text_right(text);
		}
		else
		if("center".equalsIgnoreCase(_align))
		{
			this.moveText(x+(_w/2f),y);
			this.text_center(text);
		}
		else
		{
			this.moveText(x,y);
			this.text(text);
		}
		this.endText();
	}

	public void drawString(String text) {
		if(text==null) return;
		if(text.length()==0) return;
		this.startText();
		this.font(this.font, this.fontSize);
		this.text(text);
		this.endText();
	}

	public void setFont(PdfFont f, float fs)
	{
		setFont(f.getResource(), fs);
	}

}
