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
package com.github.terefang.jmelange.pdf.core;

import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.util.AFM;
import com.github.terefang.jmelange.pdf.core.loader.*;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;

public class PDF
{
	public static final String FONT_COURIER = "courier";
	public static final String FONT_COURIER_BOLD = "courier-bold";
	public static final String FONT_COURIER_OBLIQUE = "courier-oblique";
	public static final String FONT_COURIER_BOLD_OBLIQUE = "courier-boldoblique";
	public static final String FONT_HELVETICA = "helvetica";
	public static final String FONT_HELVETICA_BOLD = "helvetica-bold";
	public static final String FONT_HELVETICA_OBLIQUE = "helvetica-oblique";
	public static final String FONT_HELVETICA_BOLD_OBLIQUE = "helvetica-boldoblique";
	public static final String FONT_TIMES = "times-roman";
	public static final String FONT_TIMES_BOLD = "times-bold";
	public static final String FONT_TIMES_ITALIC = "times-italic";
	public static final String FONT_TIMES_BOLD_ITALIC = "times-bolditalic";
	public static final String FONT_ZAPFDINGBATS = "zapfdingbats";
	public static final String FONT_SYMBOL       = "symbol";

	public static final String ENCODING_PDFDOC = "pdfdoc";

	public static final String VERSION_12 = "1.2";
	public static final String VERSION_13 = "1.3";
	public static final String VERSION_14 = "1.4";
	public static final String VERSION_15 = "1.5";
	public static final String VERSION_16 = "1.6";
	public static final String VERSION_17 = "1.7";

	public static final int FD_FLAG_FIXED_PITCH = 1;
	public static final int FD_FLAG_SERIF = 2;
	public static final int FD_FLAG_SYMBOLIC = 4;
	public static final int FD_FLAG_SCRIPT = 8;
	public static final int FD_FLAG_NON_SYMBOLIC = 32;
	public static final int FD_FLAG_ITALIC = 64;
	public static final int FD_FLAG_ALL_CAP = 65536;
	public static final int FD_FLAG_SMALL_CAP = 131072;
	public static final int FD_FLAG_FORCE_BOLD = 262144;

    private static final DecimalFormat geometryFormat;
	private static final DecimalFormat transformFormat;
	static {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		geometryFormat = new DecimalFormat("0.##", dfs);
		transformFormat = new DecimalFormat("0.######", dfs);
	}
	
	public static String geomDP(double d) {
		return geometryFormat.format(d);
	}
	
	public static String transformDP(double d) {
		return transformFormat.format(d);
	}
	
	public static int countSpaces(String _text)
	{
		int _count = 0;
		for(char _c : _text.toCharArray())
		{
			if(Character.isSpaceChar(_c))
			{
				_count++;
			}
		}
		return _count;
	}
	
	public static double advancewidth(String _text, PdfFont _font, double _size, double _wordSpace, double _charSpace, double _hScale)
	{
		double _glyph_width = _font.width(_text)*_size;
		int _num_space = PDF.countSpaces(_text);
		int _num_char = _text.length();
		double _word_spaces = _wordSpace * _num_space;
		double _char_spaces = _charSpace * (_num_char -1);
		double _advance = (_glyph_width+_word_spaces+_char_spaces)* (_hScale/100d);
		return _advance;
	}

	public static String makePDFString(String s) {
		s = makePDFStringNoBrackets(s);

		return "("+s+")";
	}

	public static String makePDFStringNoBrackets(String s)
	{
		if(s.indexOf("\\")>-1)
			s = replace(s,"\\","\\\\");

		if(s.indexOf("(")>-1)
			s = replace(s,"(","\\(");

		if(s.indexOf(")")>-1)
			s = replace(s,")","\\)");

		if(s.indexOf("\n")>-1)
			s = replace(s,"\n","\\n");

		if(s.indexOf("\r")>-1)
			s = replace(s,"\r","\\r");

		if(s.indexOf("\t")>-1)
			s = replace(s,"\t","\\t");

		if(s.indexOf("\f")>-1)
			s = replace(s,"\f","\\f");

		return s;
	}

	private static String replace(String source,
								  String removeThis,
								  String replaceWith) {
		StringBuffer b = new StringBuffer();
		int p = 0, c=0;
		
		while(c>-1) {
			if((c = source.indexOf(removeThis,p)) > -1) {
				b.append(source.substring(p,c));
				b.append(replaceWith);
				p=c+1;
			}
		}
		
		// include any remaining text
		if(p<source.length())
			b.append(source.substring(p));
		
		return b.toString();
	}

	public static String makePDFStringHex(String s)
	{
		return "<"+makePDFStringHexNoBrackets(s)+">";
	}

	public static String makePDFStringHexNoBrackets(String s) {
		StringBuilder sb = new StringBuilder();
		for(char _x : s.toCharArray())
		{
			sb.append(String.format("%02x", (int)_x));
		}
		return sb.toString();
	}

	public static String makePDFString(String s, boolean b)
	{
		if(b)
		{
			return makePDFStringHex(s);
		}
		else
		{
			return makePDFString(s);
		}
	}

	static Map<Character,Integer> PDFDOC_MAP = new HashMap<>();

	public synchronized static String mapToPdfDocEncoding(String _text)
	{
		if(PDFDOC_MAP.size()==0)
		{
			Character[] _enc = AFM.loadCharset(PDF.ENCODING_PDFDOC);
			for(int i=0; i<_enc.length; i++)
			{
				PDFDOC_MAP.put(_enc[i], i);
			}
		}

		StringBuilder _sb = new StringBuilder();
		for(int i=0; i<_text.length(); i++)
		{
			char _c = _text.charAt(i);
			if(PDFDOC_MAP.containsKey(_c))
			{
				_sb.append((char)(PDFDOC_MAP.get(_c).intValue() & 0xff));
			}
		}
		return _sb.toString();
	}

    public static String normalizeName(String text)
	{
		StringBuilder _sb = new StringBuilder();
		for(char _c : text.toCharArray())
		{
			if(_c>='A' && _c<='Z')
			{
				_sb.append((char) _c);
			}
			else
			if(_c>='a' && _c<='z')
			{
				_sb.append((char) _c);
			}
			else
			if(_c>='0' && _c<='9')
			{
				_sb.append((char) _c);
			}
			else
			{
				_sb.append(String.format("_%02X_", (int)_c));
			}
		}
		return _sb.toString();
    }

	public static PdfResourceLoader loadFrom(String _src)
	{
		if(_src.startsWith("cp:/"))
		{
			return ClasspathResourceLoader.of(_src.substring(4));
		}
		else
		if(_src.startsWith("cp:"))
		{
			return ClasspathResourceLoader.of(_src.substring(3));
		}
		else
		if(_src.startsWith("file:"))
		{
			return UrlResourceLoader.of(_src);
		}
		else
		if(_src.startsWith("./") || _src.startsWith("../"))
		{
			File _test = new File(_src);
			if(_test.exists())
			{
				return FileResourceLoader.of(_test);
			}
		}
		else
		if(_src.startsWith("/"))
		{
			return FileResourceLoader.of(_src);
		}
		return null;
	}

}
