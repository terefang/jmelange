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
import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.commons.loader.*;
import com.github.terefang.jmelange.pdf.core.values.PdfName;

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
	
	public static final String RES_HELVETICA_REGULAR = "cp:fonts/qhv/qhvr.svg";
	public static final String RES_HELVETICA_ITALIC = "cp:fonts/qhv/qhvri.svg";
	public static final String RES_HELVETICA_BOLD = "cp:fonts/qhv/qhvb.svg";
	public static final String RES_HELVETICA_BOLD_ITALIC = "cp:fonts/qhv/qhvbi.svg";
	
	public static final String RES_TIMES_REGULAR = "cp:fonts/qtm/qtmr.svg";
	public static final String RES_TIMES_ITALIC = "cp:fonts/qtm/qtmri.svg";
	public static final String RES_TIMES_BOLD = "cp:fonts/qtm/qtmb.svg";
	public static final String RES_TIMES_BOLD_ITALIC = "cp:fonts/qtm/qtmbi.svg";
	
	public static final String RES_COURIER_REGULAR = "cp:fonts/qcr/qcrr.svg";
	public static final String RES_COURIER_ITALIC = "cp:fonts/qcr/qcrri.svg";
	public static final String RES_COURIER_BOLD = "cp:fonts/qcr/qcrb.svg";
	public static final String RES_COURIER_BOLD_ITALIC = "cp:fonts/qcr/qcrbi.svg";
	
	public static final String RES_SYMBOL = "cp:fonts/qsym.svg";
	public static final String RES_DINGBATS = "cp:fonts/qdb.svg";
	
	public static final String RES_HELVETICA_REGULAR_OTF = "cp:fonts/qhv/qhvr.ttf";
	public static final String RES_HELVETICA_ITALIC_OTF = "cp:fonts/qhv/qhvri.ttf";
	public static final String RES_HELVETICA_BOLD_OTF = "cp:fonts/qhv/qhvb.ttf";
	public static final String RES_HELVETICA_BOLD_ITALIC_OTF = "cp:fonts/qhv/qhvbi.ttf";
	
	public static final String RES_TIMES_REGULAR_OTF = "cp:fonts/qtm/qtmr.ttf";
	public static final String RES_TIMES_ITALIC_OTF = "cp:fonts/qtm/qtmri.ttf";
	public static final String RES_TIMES_BOLD_OTF = "cp:fonts/qtm/qtmb.ttf";
	public static final String RES_TIMES_BOLD_ITALIC_OTF = "cp:fonts/qtm/qtmbi.ttf";
	
	public static final String RES_COURIER_REGULAR_OTF = "cp:fonts/qcr/qcrr.ttf";
	public static final String RES_COURIER_ITALIC_OTF = "cp:fonts/qcr/qcrri.ttf";
	public static final String RES_COURIER_BOLD_OTF = "cp:fonts/qcr/qcrb.ttf";
	public static final String RES_COURIER_BOLD_ITALIC_OTF = "cp:fonts/qcr/qcrbi.ttf";
	
	public static final String RES_SYMBOL_OTF = "cp:fonts/qsym.ttf";
	public static final String RES_DINGBATS_OTF = "cp:fonts/qdb.ttf";
	
	public static final String RES_COURIER_AFM = "cp:afm/Courier.afm";
	public static final String RES_COURIER_BOLD_AFM = "cp:afm/Courier-Bold.afm";
	public static final String RES_COURIER_BOLD_OBLIQUE_AFM = "cp:afm/Courier-BoldOblique.afm";
	public static final String RES_COURIER_OBLIQUE_AFM = "cp:afm/Courier-Oblique.afm";
	public static final String RES_HELVETICA_AFM = "cp:afm/Helvetica.afm";
	public static final String RES_HELVETICA_BOLD_AFM = "cp:afm/Helvetica-Bold.afm";
	public static final String RES_HELVETICA_BOLD_OBLIQUE_AFM = "cp:afm/Helvetica-BoldOblique.afm";
	public static final String RES_HELVETICA_OBLIQUE_AFM = "cp:afm/Helvetica-Oblique.afm";
	public static final String RES_SYMBOL_AFM = "cp:afm/Symbol.afm";
	public static final String RES_TIMES_BOLD_AFM = "cp:afm/Times-Bold.afm";
	public static final String RES_TIMES_BOLD_ITALIC_AFM = "cp:afm/Times-BoldItalic.afm";
	public static final String RES_TIMES_ITALIC_AFM = "cp:afm/Times-Italic.afm";
	public static final String RES_TIMES_ROMAN_AFM = "cp:afm/Times-Roman.afm";
	public static final String RES_ZAPF_DINGBATS_AFM = "cp:afm/ZapfDingbats.afm";
	
	public static final String ENCODING_PDFDOC = "pdfdoc";

	public static final String VERSION_12 = "1.2";
	public static final String VERSION_13 = "1.3";
	public static final String VERSION_14 = "1.4";
	public static final String VERSION_15 = "1.5";
	public static final String VERSION_16 = "1.6";
	public static final String VERSION_17 = "1.7";
	public static final String VERSION_20 = "2.0";


	public static final int PDFX_NONE = 0x0000000;
	public static final int PDFX_MASK = 0xF000000;

	public static final int PDFA_ID = 0x1000000;
	public static final int PDFX_ID = 0x3000000;

	public static final int PDFA_SRGB = PDFA_ID + 0;
	public static final int PDFA_CMYK = PDFA_ID + 1;
	
	public static String SRGB_IEC_ICC =
		"AAAMSExpbm8CEAAAbW50clJHQiBYWVogB84AAgAJAAYAMQAAYWNzcE1TRlQAAAAASUVDIHNSR0IAAAAA" +
		"AAAAAAAAAAAAAPbWAAEAAAAA0y1IUCAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
		"AAAAAAAAAAAAAAARY3BydAAAAVAAAAAzZGVzYwAAAYQAAABsd3RwdAAAAfAAAAAUYmtwdAAAAgQAAAAU" +
		"clhZWgAAAhgAAAAUZ1hZWgAAAiwAAAAUYlhZWgAAAkAAAAAUZG1uZAAAAlQAAABwZG1kZAAAAsQAAACI" +
		"dnVlZAAAA0wAAACGdmlldwAAA9QAAAAkbHVtaQAAA/gAAAAUbWVhcwAABAwAAAAkdGVjaAAABDAAAAAM" +
		"clRSQwAABDwAAAgMZ1RSQwAABDwAAAgMYlRSQwAABDwAAAgMdGV4dAAAAABDb3B5cmlnaHQgKGMpIDE5" +
		"OTggSGV3bGV0dC1QYWNrYXJkIENvbXBhbnkAAGRlc2MAAAAAAAAAEnNSR0IgSUVDNjE5NjYtMi4xAAAA" +
		"AAAAAAAAAAASc1JHQiBJRUM2MTk2Ni0yLjEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
		"AAAAAAAAAAAAAAAAAAAAAFhZWiAAAAAAAADzUQABAAAAARbMWFlaIAAAAAAAAAAAAAAAAAAAAABYWVog" +
		"AAAAAAAAb6IAADj1AAADkFhZWiAAAAAAAABimQAAt4UAABjaWFlaIAAAAAAAACSgAAAPhAAAts9kZXNj" +
		"AAAAAAAAABZJRUMgaHR0cDovL3d3dy5pZWMuY2gAAAAAAAAAAAAAABZJRUMgaHR0cDovL3d3dy5pZWMu" +
		"Y2gAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAZGVzYwAAAAAAAAAu" +
		"SUVDIDYxOTY2LTIuMSBEZWZhdWx0IFJHQiBjb2xvdXIgc3BhY2UgLSBzUkdCAAAAAAAAAAAAAAAuSUVD" +
		"IDYxOTY2LTIuMSBEZWZhdWx0IFJHQiBjb2xvdXIgc3BhY2UgLSBzUkdCAAAAAAAAAAAAAAAAAAAAAAAA" +
		"AAAAAGRlc2MAAAAAAAAALFJlZmVyZW5jZSBWaWV3aW5nIENvbmRpdGlvbiBpbiBJRUM2MTk2Ni0yLjEA" +
		"AAAAAAAAAAAAACxSZWZlcmVuY2UgVmlld2luZyBDb25kaXRpb24gaW4gSUVDNjE5NjYtMi4xAAAAAAAA" +
		"AAAAAAAAAAAAAAAAAAAAAAAAAAB2aWV3AAAAAAATpP4AFF8uABDPFAAD7cwABBMLAANcngAAAAFYWVog" +
		"AAAAAABMCVYAUAAAAFcf521lYXMAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAKPAAAAAnNpZyAAAAAA" +
		"Q1JUIGN1cnYAAAAAAAAEAAAAAAUACgAPABQAGQAeACMAKAAtADIANwA7AEAARQBKAE8AVABZAF4AYwBo" +
		"AG0AcgB3AHwAgQCGAIsAkACVAJoAnwCkAKkArgCyALcAvADBAMYAywDQANUA2wDgAOUA6wDwAPYA+wEB" +
		"AQcBDQETARkBHwElASsBMgE4AT4BRQFMAVIBWQFgAWcBbgF1AXwBgwGLAZIBmgGhAakBsQG5AcEByQHR" +
		"AdkB4QHpAfIB+gIDAgwCFAIdAiYCLwI4AkECSwJUAl0CZwJxAnoChAKOApgCogKsArYCwQLLAtUC4ALr" +
		"AvUDAAMLAxYDIQMtAzgDQwNPA1oDZgNyA34DigOWA6IDrgO6A8cD0wPgA+wD+QQGBBMEIAQtBDsESARV" +
		"BGMEcQR+BIwEmgSoBLYExATTBOEE8AT+BQ0FHAUrBToFSQVYBWcFdwWGBZYFpgW1BcUF1QXlBfYGBgYW" +
		"BicGNwZIBlkGagZ7BowGnQavBsAG0QbjBvUHBwcZBysHPQdPB2EHdAeGB5kHrAe/B9IH5Qf4CAsIHwgy" +
		"CEYIWghuCIIIlgiqCL4I0gjnCPsJEAklCToJTwlkCXkJjwmkCboJzwnlCfsKEQonCj0KVApqCoEKmAqu" +
		"CsUK3ArzCwsLIgs5C1ELaQuAC5gLsAvIC+EL+QwSDCoMQwxcDHUMjgynDMAM2QzzDQ0NJg1ADVoNdA2O" +
		"DakNww3eDfgOEw4uDkkOZA5/DpsOtg7SDu4PCQ8lD0EPXg96D5YPsw/PD+wQCRAmEEMQYRB+EJsQuRDX" +
		"EPURExExEU8RbRGMEaoRyRHoEgcSJhJFEmQShBKjEsMS4xMDEyMTQxNjE4MTpBPFE+UUBhQnFEkUahSL" +
		"FK0UzhTwFRIVNBVWFXgVmxW9FeAWAxYmFkkWbBaPFrIW1hb6Fx0XQRdlF4kXrhfSF/cYGxhAGGUYihiv" +
		"GNUY+hkgGUUZaxmRGbcZ3RoEGioaURp3Gp4axRrsGxQbOxtjG4obshvaHAIcKhxSHHscoxzMHPUdHh1H" +
		"HXAdmR3DHeweFh5AHmoelB6+HukfEx8+H2kflB+/H+ogFSBBIGwgmCDEIPAhHCFIIXUhoSHOIfsiJyJV" +
		"IoIiryLdIwojOCNmI5QjwiPwJB8kTSR8JKsk2iUJJTglaCWXJccl9yYnJlcmhya3JugnGCdJJ3onqyfc" +
		"KA0oPyhxKKIo1CkGKTgpaymdKdAqAio1KmgqmyrPKwIrNitpK50r0SwFLDksbiyiLNctDC1BLXYtqy3h" +
		"LhYuTC6CLrcu7i8kL1ovkS/HL/4wNTBsMKQw2zESMUoxgjG6MfIyKjJjMpsy1DMNM0YzfzO4M/E0KzRl" +
		"NJ402DUTNU01hzXCNf02NzZyNq426TckN2A3nDfXOBQ4UDiMOMg5BTlCOX85vDn5OjY6dDqyOu87LTtr" +
		"O6o76DwnPGU8pDzjPSI9YT2hPeA+ID5gPqA+4D8hP2E/oj/iQCNAZECmQOdBKUFqQaxB7kIwQnJCtUL3" +
		"QzpDfUPARANER0SKRM5FEkVVRZpF3kYiRmdGq0bwRzVHe0fASAVIS0iRSNdJHUljSalJ8Eo3Sn1KxEsM" +
		"S1NLmkviTCpMcky6TQJNSk2TTdxOJU5uTrdPAE9JT5NP3VAnUHFQu1EGUVBRm1HmUjFSfFLHUxNTX1Oq" +
		"U/ZUQlSPVNtVKFV1VcJWD1ZcVqlW91dEV5JX4FgvWH1Yy1kaWWlZuFoHWlZaplr1W0VblVvlXDVchlzW" +
		"XSddeF3JXhpebF69Xw9fYV+zYAVgV2CqYPxhT2GiYfViSWKcYvBjQ2OXY+tkQGSUZOllPWWSZedmPWaS" +
		"ZuhnPWeTZ+loP2iWaOxpQ2maafFqSGqfavdrT2una/9sV2yvbQhtYG25bhJua27Ebx5veG/RcCtwhnDg" +
		"cTpxlXHwcktypnMBc11zuHQUdHB0zHUodYV14XY+dpt2+HdWd7N4EXhueMx5KnmJeed6RnqlewR7Y3vC" +
		"fCF8gXzhfUF9oX4BfmJ+wn8jf4R/5YBHgKiBCoFrgc2CMIKSgvSDV4O6hB2EgITjhUeFq4YOhnKG14c7" +
		"h5+IBIhpiM6JM4mZif6KZIrKizCLlov8jGOMyo0xjZiN/45mjs6PNo+ekAaQbpDWkT+RqJIRknqS45NN" +
		"k7aUIJSKlPSVX5XJljSWn5cKl3WX4JhMmLiZJJmQmfyaaJrVm0Kbr5wcnImc951kndKeQJ6unx2fi5/6" +
		"oGmg2KFHobaiJqKWowajdqPmpFakx6U4pammGqaLpv2nbqfgqFKoxKk3qamqHKqPqwKrdavprFys0K1E" +
		"rbiuLa6hrxavi7AAsHWw6rFgsdayS7LCszizrrQltJy1E7WKtgG2ebbwt2i34LhZuNG5SrnCuju6tbsu" +
		"u6e8IbybvRW9j74KvoS+/796v/XAcMDswWfB48JfwtvDWMPUxFHEzsVLxcjGRsbDx0HHv8g9yLzJOsm5" +
		"yjjKt8s2y7bMNcy1zTXNtc42zrbPN8+40DnQutE80b7SP9LB00TTxtRJ1MvVTtXR1lXW2Ndc1+DYZNjo" +
		"2WzZ8dp22vvbgNwF3IrdEN2W3hzeot8p36/gNuC94UThzOJT4tvjY+Pr5HPk/OWE5g3mlucf56noMui8" +
		"6Ubp0Opb6uXrcOv77IbtEe2c7ijutO9A78zwWPDl8XLx//KM8xnzp/Q09ML1UPXe9m32+/eK+Bn4qPk4" +
		"+cf6V/rn+3f8B/yY/Sn9uv5L/tz/bf//";
	
	public static final String PDFA1_NAME = "PDF/A-1";
	public static final String PDFA1_SRGB_S = "GTS_PDFA1";
	public static final String PDFA1_SRGB_OI = "sRGB IEC61966-2.1";
	public static final String PDFA_INFO_KEY = "GTS_PDFAVersion";

	public static final int PDFX_4_2008 = PDFX_ID+0x402008;
	public static final String PDFX_4_NAME = "PDF/X-4";
	public static final String PDFX_4_2008_NAME = "PDF/X-4:2008";
	public static final int PDFX_4_2010 = PDFX_ID+0x402010;
	public static final String PDFX_4_2010_NAME = "PDF/X-4:2010";
	public static final String PDFX_INFO_KEY = "GTS_PDFXVersion";

	public static final int PDFX_ICC_TYPE_GREY = 1;
	public static final int PDFX_ICC_TYPE_RGB = 3;
	public static final int PDFX_ICC_TYPE_CMYK = 4;

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

	public static ResourceLoader loadFrom(String _src)
	{
		if(_src.startsWith("cp:/"))
		{
			return ClasspathResourceLoader.of(_src.substring(4), null);
		}
		else
		if(_src.startsWith("cp:"))
		{
			return ClasspathResourceLoader.of(_src.substring(3), null);
		}
		else
		if(_src.startsWith("file:"))
		{
			return UrlResourceLoader.of(_src, null);
		}
		else
		if(_src.startsWith("./") || _src.startsWith("../"))
		{
			File _test = new File(_src);
			if(_test.exists())
			{
				return FileResourceLoader.of(_test, null);
			}
		}
		else
		if(_src.startsWith("/"))
		{
			return FileResourceLoader.of(_src);
		}
		return null;
	}
	
	public static PdfDocument create()
	{
		return PdfDocument.create();
	}
}
