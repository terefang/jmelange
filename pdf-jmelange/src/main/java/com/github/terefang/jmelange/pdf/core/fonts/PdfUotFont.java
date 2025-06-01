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
import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.fonts.sfnt.SfntUtil;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.Font;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.FontFactory;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.Tag;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.CMap;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.HorizontalMetricsTable;
import com.github.terefang.jmelange.fonts.sfnt.sfntly.table.core.NameTable;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.encoding.IdentityAndMappedGlyphEncoder;
import com.github.terefang.jmelange.pdf.core.encoding.SfontlyTtfGlyphEncoder;
import com.github.terefang.jmelange.pdf.core.encoding.Utf8Encoder;
import com.github.terefang.jmelange.pdf.core.values.*;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfUotFont
        extends PdfOtxFont
{
	private PdfDictObjectWithStream _map;

	public static final PdfFontResource createResource(PdfUotFont _f)
	{
		return PdfFont.createResource(_f, _f.trueTypefont.hasTable(Tag.CFF) ? "UOT" : "UTT");
	}
	
	public PdfUotFont(PdfDocument doc, Font _font, ResourceLoader _fontfile, String _cs) throws Exception
	{
		super(doc, SfntUtil.isCFF(_font) ? new SfontlyTtfGlyphEncoder(_font, false) : new IdentityAndMappedGlyphEncoder(), true, SfntUtil.isCFF(_font));
		
		this.setEncoder(new Utf8Encoder(_cs));
		
		this.trueTypefont = _font;
		
		boolean _isPS = SfntUtil.isCFF(_font);
		String _name = SfntUtil.findName(_font, NameTable.NameId.FullFontName.value());
		
		if(_name==null) _name = _name.replaceAll("\\s+", "");
		
		if(_name==null) _name = _fontfile.getName();
		
		this.setPsName(_name.replaceAll("\\s+", ""));
		if(doc.isObfuscate())
		{
			_name = GuidUtil.toHashGUID(_name).substring(0,8);
		}
		
		this.setName(this.getResource().getResName());
		this.setFontName(makeFontSubsetTag(this.getRef().getValue(), this.getResource().getResPrefix(), _name));
		
		if(this._touni!=null) this._touni.set("X_UotFontName", PdfString.of(_name));
		
		PdfDictObject _desc = this.getDescender();
		
		CMap _cmap = makeCmapCidGid(_font, _isPS, _desc, _cs);
		
		makeWidths(_font, _isPS, _cmap, _desc);
		
		makeDescriptor(_font, _isPS, _name, _desc, _fontfile);
		
		makeUtfCidMap(_cmap);
	}

	public static PdfFont of(PdfDocument doc, ResourceLoader _fontfile, String _cs) throws Exception
	{
		InputStream _stream = _fontfile.getInputStream();
		FontFactory _sffactory = FontFactory.getInstance();
		Font[]      _sfonts    = _sffactory.loadFonts(_stream);
		Font        _sfont     = _sfonts[0];
		return new PdfUotFont(doc, _sfont, _fontfile, _cs);
	}
	
	void makeCidMap(CMap _cmap, String _reg, String _ord,  int _sup)
	{
		PdfDict _dict = PdfDict.create();
		this.descender.set("CIDSystemInfo", _dict);
		_dict.set("Registry", PdfString.of(_reg));
		_dict.set("Ordering", PdfString.of(_ord));
		_dict.set("Supplement", PdfNum.of(_sup));
		
		//PdfDictObjectWithStream _cidmap = PdfDictObjectWithStream.create(this.getDoc(), false);
		PdfDictObjectWithStream _cidmap = PdfDictObjectWithStream.create(this.getDoc(), false);
		_cidmap.set("Type", PdfName.of("CMap"));
		_cidmap.set("CMapName", PdfName.of(_reg+"-"+_ord+"-"+_sup));
		PrintStream _print = _cidmap.getPrintStream();
		_print.println("%!PS-Adobe-3.0 Resource-CMap");
		_print.println("%%DocumentNeededResources: ProcSet (CIDInit)");
		_print.println("%%IncludeResource: ProcSet (CIDInit)");
		_print.println("%%BeginResource: CMap ("+_reg+"-"+_ord+"-"+_sup+")");
		_print.println("%%Title: ("+_reg+"-"+_ord+"-"+_sup+" "+_reg+"-"+_ord+" "+_sup+" 0)");
		_print.println("%%Version: 1.000");
		_print.println("%%Copyright: -----------------------------------------------------------");
		_print.println("%%Copyright: none claimed.");
		_print.println("%%Copyright: -----------------------------------------------------------");
		_print.println("%%EndComments");
		_print.println("/CIDInit /ProcSet findresource begin");
		_print.println("12 dict begin begincmap");
		_print.println("/CIDSystemInfo <<");
		_print.println("   /Registry ("+_reg+")");
		_print.println("   /Ordering ("+_ord+")");
		_print.println("   /Supplement "+_sup);
		_print.println(">> def");
		_print.println("/CMapName /"+_reg+"-"+_ord+"-"+_sup+" def");
		_print.println("/WMode 0 def");
		_print.println("");
		_print.println("4 begincodespacerange");
		_print.println("	<00>       <7F>");
		_print.println("	<C080>     <DFBF>");
		_print.println("	<E08080>   <EFBFBF>");
		_print.println("	<F0808080> <F7BFBFBF>");
		_print.println("endcodespacerange");
		_print.println("");
		_print.println("1 beginnotdefrange");
		_print.println("	<00> <1f> 1");
		_print.println("endnotdefrange");
		_print.println("");
		
		if(_cmap!=null)
		{
			List<String> _v = new ArrayList<>();
			int _umax = _cmap.maxCodePoint();
			for(int _u = 0; _u<_umax; _u++)
			{
				int _g = _cmap.glyphId(_u);
				
				if (_g>0)
				{
					StringBuilder _ret = new StringBuilder();
					_ret.append("<");
					for (int _j : Character.toString(_u)
							.getBytes(StandardCharsets.UTF_8))
					{
						_ret.append(String.format("%02x", (_j & 0xff)));
					}
					_ret.append("> " + _g);
					_v.add(_ret.toString());
				}
			}
			
			int _l = _v.size();
			int _j = 0;
			for(String _line : _v)
			{
				int _i = ((_l-_j) > 100) ? 100 : (_l - _j);
				if (_j == 0)
				{
					_print.println(String.format("%d begincidchar", _i));
				}
				else if (_j % 100 == 0)
				{
					_print.println("endcidchar");
					_print.println(String.format("%d begincidchar", _i));
				}
				_print.println(_line);
				_j++;
			}
			
			_print.println("endcidchar");
		}
		
		_print.println("endcmap CMapName currendict /CMap defineresource pop end end");
		
		this.set("Encoding", _cidmap);
	}
	
	void makeUniMap(CMap _cmap, String _reg, String _ord,  int _sup)
	{
		//PdfDictObjectWithStream _cidmap = PdfDictObjectWithStream.create(this.getDoc(), false);
		PdfDictObjectWithStream _cidmap = PdfDictObjectWithStream.create(this.getDoc(), false);
		_cidmap.set("Type", PdfName.of("CMap"));
		_cidmap.set("CMapName", PdfName.of(_reg+"-"+_ord+"-Unicode-"+_sup));
		PrintStream _print = _cidmap.getPrintStream();
		_print.println("%!PS-Adobe-3.0 Resource-CMap");
		_print.println("%%DocumentNeededResources: ProcSet (CIDInit)");
		_print.println("%%IncludeResource: ProcSet (CIDInit)");
		_print.println("%%BeginResource: CMap ("+_reg+"-"+_ord+"-Unicode-"+_sup+")");
		_print.println("%%Title: ("+_reg+"-"+_ord+"-Unicode-"+_sup+" "+_reg+"-"+_ord+"-Unicode "+_sup+" 0)");
		_print.println("%%Version: 1.000");
		_print.println("%%Copyright: -----------------------------------------------------------");
		_print.println("%%Copyright: none claimed.");
		_print.println("%%Copyright: -----------------------------------------------------------");
		_print.println("%%EndComments");
		_print.println("/CIDInit /ProcSet findresource begin");
		_print.println("12 dict begin begincmap");
		_print.println("/CIDSystemInfo <<");
		_print.println("   /Registry ("+_reg+")");
		_print.println("   /Ordering ("+_ord+"-Unicode)");
		_print.println("   /Supplement "+_sup);
		_print.println(">> def");
		_print.println("/CMapName /"+_reg+"-"+_ord+"-Unicode-"+_sup+" def");
		_print.println("/WMode 0 def");
		_print.println("");
		if(false)
		{
			_print.println("4 begincodespacerange");
			_print.println("	<00>       <7F>");
			_print.println("	<C080>     <DFBF>");
			_print.println("	<E08080>   <EFBFBF>");
			_print.println("	<F0808080> <F7BFBFBF>");
			_print.println("endcodespacerange");
		}
		else
		{
			_print.println("1 begincodespacerange");
			_print.println("	<0000>   <FFFF>");
			_print.println("endcodespacerange");
		}
	
		_print.println("");
		
		if(_cmap!=null)
		{
			List<String> _v = new ArrayList<>();
			
			int _umax = _cmap.maxCodePoint();
			for(int _u = 0; _u<_umax; _u++)
			{
				int _g = _cmap.glyphId(_u);
				
				if (_g>0)
				{
					StringBuilder _ret = new StringBuilder();
					if(Character.charCount(_u)==2)
					{
						_ret.append(String.format("<%04X> <%04X%04X>", _g, (int)Character.highSurrogate(_u), (int)Character.lowSurrogate(_u)));
					}
					else
					{
						_ret.append(String.format("<%04X> <%04X>", _g, _u));
					}
					_v.add(_ret.toString());
				}
			}
			
			int _l = _v.size();
			int _j = 0;
			for(String _line : _v)
			{
				int _i = ((_l-_j) > 100) ? 100 : (_l - _j);
				if (_j == 0)
				{
					_print.println(String.format("%d beginbfchar", _i));
				}
				else if (_j % 100 == 0)
				{
					_print.println("endbfchar");
					_print.println(String.format("%d beginbfchar", _i));
				}
				_print.println(_line);
				_j++;
			}
			
			_print.println("endbfchar");
		}
		
		_print.println("endcmap CMapName currendict /CMap defineresource pop end end");
		
		this.set("ToUnicode", _cidmap);
	}
	
	void makeUtfCidMap(CMap _cmap)
	{
		this.set("Encoding", PdfName.of("Identity-H"));
		
		String _reg = "Local";
		String _ord = "UTF8";
		int _sup = this.getRef().getValue();
		
		makeCidMap(_cmap,_reg, _ord,_sup);
		//makeUniMap(_cmap,_reg, _ord,_sup);
	}
	
	@Override
	@SneakyThrows
	CMap makeCmapCidGid(Font _font, boolean _isPS, PdfDictObject _desc, String _cs)
	{
		int numGlyphs = SfntUtil.getNumGlyphs(_font);
		
		CMap _cmap = SfntUtil.findCMap(_font, false);
		if(_cmap!=null)
		{
			int _umax = _cmap.maxCodePoint();
			for(int _u = 0; _u<_umax; _u++)
			{
				int _g = _cmap.glyphId(_u);
				if(_g>0) this.setCoverage((_u>>8) & 0xffff);
			}
		}
		
		_desc.set("CIDToGIDMap", PdfName.of("Identity"));

		if(_cmap!=null)
		{
			this.nameToChar.putAll(SfntUtil.getGlyphNames(_font, _cmap, _cs));
		}
		
		return _cmap;
	}
}
