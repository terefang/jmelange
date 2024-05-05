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
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.File;
import java.util.*;

public class PdfFontRegistry
{
	PdfDocument doc;
	Map<String, PdfFont> registry = new LinkedHashMap<>();
	
	public static PdfFontRegistry of(PdfDocument _doc)
	{
		PdfFontRegistry _reg = new PdfFontRegistry();
		_reg.doc = _doc;
		return _reg;
	}
	
	public PdfFont registerFont(PdfFont _font)
	{
		return registerFont(_font, _font.getFontName());
	}

	public PdfFont registerFont(PdfFont _font, String _name, String _w)
	{
		return registerFont(_font, _name+"-"+_w);
	}

	public PdfFont registerFont(PdfFont _font, String _name)
	{
		String _key = _name.toLowerCase();
		this.registry.put(_key, _font);
		//this.registry.put(_font.getFontName().toLowerCase(), _font);
		return _font;
	}
	
	public PdfFont lookupFont(String _name, String _w)
	{
		return lookupFont(_name+"-"+_w);
	}
	
	public PdfFont lookupFont(String _name)
	{
		String _key = _name.toLowerCase();
		if(this.registry.containsKey(_key))
		{
			return this.registry.get(_key);
		}

		if(_key.startsWith("pdf:"))
		{
			PdfFont _font = null;
			String _pdf_font_name = _key.substring(4);
			if(PDF.FONT_HELVETICA.equalsIgnoreCase(_pdf_font_name))
			{
				_font = doc.registerHelveticaFont(PDF.ENCODING_PDFDOC);
			}
			else
			if(PDF.FONT_HELVETICA_BOLD.equalsIgnoreCase(_pdf_font_name))
			{
				_font = doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);
			}
			else
			if(PDF.FONT_HELVETICA_BOLD_OBLIQUE.equalsIgnoreCase(_pdf_font_name))
			{
				_font = doc.registerHelveticaBoldObliqueFont(PDF.ENCODING_PDFDOC);
			}
			else
			if(PDF.FONT_HELVETICA_OBLIQUE.equalsIgnoreCase(_pdf_font_name))
			{
				_font = doc.registerHelveticaObliqueFont(PDF.ENCODING_PDFDOC);
			}
			else
			if(PDF.FONT_COURIER.equalsIgnoreCase(_pdf_font_name))
			{
				_font = doc.registerCourierFont(PDF.ENCODING_PDFDOC);
			}
			else
			if(PDF.FONT_COURIER_BOLD.equalsIgnoreCase(_pdf_font_name))
			{
				_font = doc.registerCourierBoldFont(PDF.ENCODING_PDFDOC);
			}
			else
			if(PDF.FONT_COURIER_OBLIQUE.equalsIgnoreCase(_pdf_font_name))
			{
				_font = doc.registerCourierObliqueFont(PDF.ENCODING_PDFDOC);
			}
			else
			if(PDF.FONT_COURIER_BOLD_OBLIQUE.equalsIgnoreCase(_pdf_font_name))
			{
				_font = doc.registerCourierBoldObliqueFont(PDF.ENCODING_PDFDOC);
			}
			else
			if(PDF.FONT_TIMES.equalsIgnoreCase(_pdf_font_name))
			{
				_font = doc.registerTimesRomanFont(PDF.ENCODING_PDFDOC);
			}
			else
			if(PDF.FONT_TIMES_BOLD.equalsIgnoreCase(_pdf_font_name))
			{
				_font = doc.registerTimesBoldFont(PDF.ENCODING_PDFDOC);
			}
			else
			if(PDF.FONT_TIMES_BOLD_ITALIC.equalsIgnoreCase(_pdf_font_name))
			{
				_font = doc.registerTimesBoldItalicFont(PDF.ENCODING_PDFDOC);
			}
			else
			if(PDF.FONT_TIMES_ITALIC.equalsIgnoreCase(_pdf_font_name))
			{
				_font = doc.registerTimesItalicFont(PDF.ENCODING_PDFDOC);
			}
			else
			if(PDF.FONT_SYMBOL.equalsIgnoreCase(_pdf_font_name))
			{
				_font = doc.registerSymbolFont();
			}
			else
			if(PDF.FONT_ZAPFDINGBATS.equalsIgnoreCase(_pdf_font_name))
			{
				_font = doc.registerZapfDingbatsFont();
			}
			else
			{
				_font = doc.registerCourierFont(PDF.ENCODING_PDFDOC);
			}

			this.registerFont(_font, _key);
			this.registerFont(_font, _pdf_font_name);
			return _font;
		}
		else
		if(_key.startsWith("awt:"))
		{
			String _pdf_font_name = _key.substring(4);
			String[] _options = null;
			if(_pdf_font_name.indexOf(';')>0)
			{
				_options = CommonUtil.split(_pdf_font_name.substring(_pdf_font_name.indexOf(';')+1).toLowerCase(), ";");
				_pdf_font_name = _pdf_font_name.substring(0, _pdf_font_name.indexOf(';'));
			}
			PdfFont _font = doc.registerAwtFont(_pdf_font_name, PDF.ENCODING_PDFDOC, _options);
			this.registerFont(_font, _key);
			this.registerFont(_font, _pdf_font_name);
			this.registerFont(_font, _name);
			return _font;
		}
		return null;
	}

	public PdfFont syntheticFont(String _id, PdfFont _font, String _cs, String[] _options)
	{
		PdfFont _sfont = doc.registerSytheticFont(_cs, _font, _options);
		this.registerFont(_sfont, _id);
		return _sfont;
	}

	public PdfFont syntheticFont(String _id, String _font, String _cs, String[] _options)
	{
		String _key = _font.toLowerCase();
		if(this.registry.containsKey(_key))
		{
			return syntheticFont(_id, this.registry.get(_key), _cs, _options);
		}
		return null;
	}

	public Collection<PdfFont> allFonts()
	{
		return Collections.unmodifiableCollection(this.registry.values());
	}

	public List<String> allFontNames()
	{
		return new LinkedList(this.registry.keySet());
	}

	static String[] ttfIncl = { "**/*.ttf", "**/*.otf" };

	public List<PdfFont> loadTtFonts(String _cs, String _base)
	{
		return loadTtFonts(_cs, _base, false);
	}

	public List<PdfFont> loadTtFonts(String _cs, String _base, boolean _otx)
	{
		List<PdfFont> _list = new Vector<>();
		DirectoryScanner _ds = new DirectoryScanner();
		_ds.setBasedir(_base);
		_ds.setIncludes(ttfIncl);
		_ds.setCaseSensitive(false);
		_ds.scan();
		String[] _flist = _ds.getIncludedFiles();
		Arrays.sort(_flist);
		for(String _file : _flist)
		{
			PdfFont _font = null;
			if(_otx)
			{
				_font = this.doc.registerOtxFont(_cs, new File(_base, _file));
			}
			else
			{
				_font = this.doc.registerTtfFont(_cs, new File(_base, _file));
			}
			this.registerFont(_font);
			_list.add(_font);
		}
		return _list;
	}
}
