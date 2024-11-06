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
package com.github.terefang.jmelange.pdf.ext;

import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.fonts.*;
import com.github.terefang.jmelange.pdf.core.image.PdfImage;
import com.github.terefang.jmelange.commons.loader.*;
import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.commons.util.FontUtil;
import com.github.terefang.jmelange.pdf.ext.fonts.PdfOtuFont;
import com.github.terefang.jmelange.pdf.ext.fonts.PdfPfbFont;
import com.github.terefang.jmelange.pdf.ext.image.PdfSvgImage;

import lombok.SneakyThrows;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PdfExtDocument extends PdfDocument
{

	public static PdfExtDocument create()
	{
		return new PdfExtDocument();
	}

	public PdfExtDocument()
	{
		super();
	}
	
	public PdfFont registerTexGyreHerosRegularFont()
	{
		return registerOtuFont(PDF.loadFrom(PDFX.TEX_GYRE_HEROS_REGULAR));
	}

	public PdfFont registerTexGyreHerosItalicFont()
	{
		return registerOtuFont(PDF.loadFrom(PDFX.TEX_GYRE_HEROS_ITALIC));
	}

	public PdfFont registerTexGyreHerosBoldFont()
	{
		return registerOtuFont(PDF.loadFrom(PDFX.TEX_GYRE_HEROS_BOLD));
	}

	public PdfFont registerTexGyreHerosBoldItalicFont()
	{
		return registerOtuFont(PDF.loadFrom(PDFX.TEX_GYRE_HEROS_BOLDITALIC));
	}

	public PdfFont registerTexGyreTermesRegularFont()
	{
		return registerOtuFont(PDF.loadFrom(PDFX.TEX_GYRE_TERMES_REGULAR));
	}

	public PdfFont registerTexGyreTermesItalicFont()
	{
		return registerOtuFont(PDF.loadFrom(PDFX.TEX_GYRE_TERMES_ITALIC));
	}

	public PdfFont registerTexGyreTermesBoldFont()
	{
		return registerOtuFont(PDF.loadFrom(PDFX.TEX_GYRE_TERMES_BOLD));
	}

	public PdfFont registerTexGyreTermesBoldItalicFont()
	{
		return registerOtuFont(PDF.loadFrom(PDFX.TEX_GYRE_TERMES_BOLDITALIC));
	}

	public PdfFont registerTexGyreCursorRegularFont()
	{
		return registerOtuFont(PDF.loadFrom(PDFX.TEX_GYRE_CURSOR_REGULAR));
	}

	public PdfFont registerTexGyreCursorItalicFont()
	{
		return registerOtuFont(PDF.loadFrom(PDFX.TEX_GYRE_CURSOR_ITALIC));
	}

	public PdfFont registerTexGyreCursorBoldFont()
	{
		return registerOtuFont(PDF.loadFrom(PDFX.TEX_GYRE_CURSOR_BOLD));
	}

	public PdfFont registerTexGyreCursorBoldItalicFont()
	{
		return registerOtuFont(PDF.loadFrom(PDFX.TEX_GYRE_CURSOR_BOLDITALIC));
	}

	public PdfFont registerUnicodeRegularFont()
	{
		return registerOtuFont(PDF.loadFrom(PDFX.UNICODE_REGULAR));
	}

	static Properties FONT_ALIASES = new Properties();
	public PdfFont registerFontByName(String _font, String _cs)
	{
		if(FONT_ALIASES.size()==0)
		{
			try (InputStream _is = PDF.loadFrom("cp:fonts/aliases.properties").getInputStream())
			{
				FONT_ALIASES.load(_is);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		if(!FONT_ALIASES.containsKey(_font)) throw new IllegalArgumentException(_font);

		String _res = FONT_ALIASES.getProperty(_font);
		ResourceLoader _rl = PDF.loadFrom(_res);
		if(_res.endsWith(".ttf") || _res.endsWith(".otf"))
		{
			return registerOtuFont(_rl, _cs);
		}
		else
		if(_res.endsWith(".svg"))
		{
			return registerSvgFont(_cs, _rl, null);
		}
		throw new IllegalArgumentException(_font);
	}

	public PdfFont registerOtuFont(String _file)
	{
		return this.registerOtuFont(FileResourceLoader.of(_file));
	}

	public PdfFont registerOtuFont(String _file, String _cs)
	{
		return this.registerOtuFont(FileResourceLoader.of(_file), _cs);
	}

	public PdfFont registerOtuFont(File _file)
	{
		return this.registerOtuFont(FileResourceLoader.of(_file, null));
	}

	public PdfFont registerOtuFont(File _file, String _cs)
	{
		return this.registerOtuFont(FileResourceLoader.of(_file, null), _cs);
	}

	public PdfFont registerOtuFont(ResourceLoader _file)
	{
		return this.registerOtuFont(_file, null);
	}

	public PdfFont registerOtuFont(ResourceLoader _file, String _cs)
	{
		return registerOtuFont(_file, _cs, "icons".equalsIgnoreCase(_cs) || "unicode".equalsIgnoreCase(_cs));
	}

	public PdfSvgImage registerSvgImage(String _img, int w, int h)
	{
		return registerSvgImage(FileResourceLoader.of(_img), w, h);
	}

	public PdfSvgImage registerSvgImage(File _img, int w, int h)
	{
		return registerSvgImage(FileResourceLoader.of(_img, null), w, h);
	}

	public PdfSvgImage registerSvgImage(ResourceLoader _img, int w, int h)
	{
		PdfSvgImage pi = PdfSvgImage.of(this, _img, w , h);
		return pi;
	}

	public PdfImage registerRenderedSvgImage(String _img, int w, int h, float _s, boolean _t, boolean _a, float _av, String _compr, int _rot)
	{
		return registerRenderedSvgImage(FileResourceLoader.of(_img), w, h, _s, _t, _a, _av, _compr, _rot);
	}

	public PdfImage registerRenderedSvgImage(File _img, int w, int h, float _s, boolean _t, boolean _a, float _av, String _compr, int _rot)
	{
		return registerRenderedSvgImage(FileResourceLoader.of(_img, null), w, h, _s, _t, _a, _av, _compr, _rot);
	}

	public PdfImage registerRenderedSvgImage(ResourceLoader _img, int w, int h, float _s, boolean _t, boolean _a, float _av, String _compr, int _rot)
	{
		BufferedImage bufferedImage = PdfSvgImage.render(_img, w, h, _s, _t, _a);
		PdfImage pi = null;
		if("indexed".equalsIgnoreCase(_compr)
			|| "index".equalsIgnoreCase(_compr))
		{
			pi = this.registerIndexedImage(bufferedImage,  _t, _a, _av, _rot);
		}
		else
		{
			pi = this.registerImage(bufferedImage, _compr, _t, _a, _av, _rot);
		}
		pi.setWidth(bufferedImage.getWidth(null));
		pi.setHeight(bufferedImage.getHeight(null));
		return pi;
	}

	public PdfFont registerPfbAfmFont(File _pfb, File _afm, String _cs)
	{
		return this.registerPfbAfmFont(FileResourceLoader.of(_pfb, null), (_afm==null) ? null : FileResourceLoader.of(_afm, null), _cs);
	}
	
	public PdfFont registerPfbAfmFont(ResourceLoader _pfb, ResourceLoader _afm, String _cs)
	{
		try
		{
			return PdfPfbFont.of(this, (_afm==null) ? null : new AFM(_afm), _cs, _pfb);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@SneakyThrows
	public PdfFont registerAwtFont(Font _awt, String _cs, String[] _options)
	{
		if(_options!=null && _options.length>0)
		{
			return super.registerAwtFont(_awt, _cs, _options);
		}
		else
		if("unicode".equalsIgnoreCase(_cs) && FontUtil.isTT(_awt))
		{
			String platName = FontUtil.getAwtFileName(_awt);
			return registerOtuFont(FileResourceLoader.of(platName), PDF.ENCODING_PDFDOC, true);
		}
		else
		if(_cs==null && FontUtil.isTT(_awt))
		{
			String platName = FontUtil.getAwtFileName(_awt);
			return registerOtuFont(FileResourceLoader.of(platName));
		}
		else
		{
			return super.registerAwtFont(_awt, _cs, _options);
		}
	}

	public PdfFont registerOtuFont(ResourceLoader _file, String _cs, boolean _forceUnicode)
	{
		try
		{
			if(this.isAllT3() && !_forceUnicode)
			{
				return PdfJavaFont.of(this, _cs, Font.createFont(Font.TRUETYPE_FONT, _file.getInputStream()), _file.getOptions());
			}
			return PdfOtuFont.of(this, _file, _cs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public PdfFontRegistry createFontRegistry()
	{
		return PdfFontRegistry.of(this);
	}

	public PdfFontRegistry registerBase14Fonts(String _enc, boolean _embed)
	{
		PdfFontRegistry _reg = PdfFontRegistry.of(this);
		return registerBase14Fonts(_reg, _enc, _embed);
	}

	public PdfFontRegistry registerBase14Fonts(PdfFontRegistry _reg, String _enc, boolean _embed)
	{
		if(!_embed)
		{
			return super.registerBase14Fonts(_reg, _enc);
		}
		else
		{
			_reg.registerFont(this.registerTexGyreHerosRegularFont(), PDF.FONT_HELVETICA);
			_reg.registerFont(this.registerTexGyreHerosBoldFont(), PDF.FONT_HELVETICA_BOLD);
			_reg.registerFont(this.registerTexGyreHerosItalicFont(), PDF.FONT_HELVETICA_OBLIQUE);
			_reg.registerFont(this.registerTexGyreHerosBoldItalicFont(), PDF.FONT_HELVETICA_BOLD_OBLIQUE);

			_reg.registerFont(this.registerTexGyreTermesRegularFont(), PDF.FONT_TIMES);
			_reg.registerFont(this.registerTexGyreTermesBoldFont(), PDF.FONT_TIMES_BOLD);
			_reg.registerFont(this.registerTexGyreTermesItalicFont(), PDF.FONT_TIMES_ITALIC);
			_reg.registerFont(this.registerTexGyreTermesBoldItalicFont(), PDF.FONT_TIMES_BOLD_ITALIC);

			_reg.registerFont(this.registerTexGyreCursorRegularFont(), PDF.FONT_COURIER);
			_reg.registerFont(this.registerTexGyreCursorBoldFont(), PDF.FONT_COURIER_BOLD);
			_reg.registerFont(this.registerTexGyreCursorItalicFont(), PDF.FONT_COURIER_OBLIQUE);
			_reg.registerFont(this.registerTexGyreCursorBoldItalicFont(), PDF.FONT_COURIER_BOLD_OBLIQUE);

			_reg.registerFont(this.registerSymbolFont(), PDF.FONT_SYMBOL);
			_reg.registerFont(this.registerZapfDingbatsFont(), PDF.FONT_ZAPFDINGBATS);
		}
		return _reg;
	}

	public void setPdfXIccCrpc1()
	{
		this.setPdfxConformance(PDFX.PDFX_4_2010);
		this.setIccProfile(PDFX.PDFX_ICC_TYPE_CMYK, PDF.loadFrom(PDFX.ICC_CGATS21));
	}

	public void setPdfXPSOCv3()
	{
		this.setPdfxConformance(PDFX.PDFX_4_2010);
		this.setIccProfile(PDFX.PDFX_ICC_TYPE_CMYK, PDF.loadFrom(PDFX.ICC_PSO_COATED_V3));
	}

	public void setPdfXPSOUCv3()
	{
		this.setPdfxConformance(PDFX.PDFX_4_2010);
		this.setIccProfile(PDFX.PDFX_ICC_TYPE_CMYK, PDF.loadFrom(PDFX.ICC_PSO_UNCOATED_V3));
	}
}
