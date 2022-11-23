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
package com.github.terefang.jmelange.fonts;

/*
 * Copyright 2006-2012 ICEsoft Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.loader.ResourceLoader;
import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class parses and stores data on the 14 PostScript AFM files.
 *
 * @since 1.0
 */
public class AFM {
	
	private static final Logger logger =
			Logger.getLogger(AFM.class.toString());
	
	public static final int COURIER = 0;
	public static final int COURIER_BOLD = 1;
	public static final int COURIER_OBLIQUE = 2;
	public static final int COURIER_BOLD_OBLIQUE = 3;
	
	public static final int HELVETICA = 4;
	public static final String HELVETICA_NAME = "helvetica";
	public static final int HELVETICA_BOLD = 5;
	public static final int HELVETICA_OBLIQUE = 6;
	public static final int HELVETICA_BOLD_OBLIQUE = 7;
	
	public static final int TIMES_ROMAN = 8;
	public static final int TIMES_BOLD = 9;
	public static final int TIMES_ITALIC = 10;
	public static final int TIMES_BOLD_ITALIC = 11;
	
	public static final int ZAPF_DINGBATS = 12;
	public static final int SYMBOL = 13;
	
	
	public static String[] AFMnames = {
			"Courier.afm",
			"Courier-Bold.afm",
			"Courier-Oblique.afm",
			"Courier-BoldOblique.afm",
			
			"Helvetica.afm",
			"Helvetica-Bold.afm",
			"Helvetica-Oblique.afm",
			"Helvetica-BoldOblique.afm",
			
			"Times-Roman.afm",
			"Times-Bold.afm",
			"Times-Italic.afm",
			"Times-BoldItalic.afm",
			
			"ZapfDingbats.afm",
			"Symbol.afm"
	};
	/**
	 * <p>The value of the <b>Flags</b> entry in a font descriptor is an
	 * unsized 32-bit integer containg flags specifying various characteristics
	 * of the font.</p>
	 * <table border="1" cellpadding="1">
	 * <tr>
	 * <td><b>Bit Position</b></td>
	 * <td><b>Name</b></td>
	 * <td><b>Meaning</b></td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>FixedPitch</td>
	 * <td>All glyphs have the same width (as opposed to proportional or
	 * variable-pitch fonts, which have different widths).</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>Serif</td>
	 * <td>Glyphs have serifs, which are short strokes drawn at an angle on
	 * the top and bottom of glyph stems. ( Sans serif fonts do not have serifs.)</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>Symbolic</td>
	 * <td>Font contains glyphs outside the Adobe standard Latin character
	 * set. This flag and the Nonsymbolic flag cannot both be set or both be clear.</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>Script</td>
	 * <td>Glyphs resemble cursive handwriting.</td>
	 * </tr>
	 * <tr>
	 * <td>6</td>
	 * <td>Nonsymbolic</td>
	 * <td>Font uses the Adobe standard Latin character set or a subset of it.</td>
	 * </tr>
	 * <tr>
	 * <td>7</td>
	 * <td>Italic</td>
	 * <td>Glyphs have dominant vertical strokes that are slanted.</td>
	 * </tr>
	 * <tr>
	 * <td>17</td>
	 * <td>AllCap</td>
	 * <td>Font contains no lowercase letters; typically used for display
	 * purposes, such as for titles or headlines.</td>
	 * </tr>
	 * <tr>
	 * <td>18</td>
	 * <td>SmallCap</td>
	 * <td>Font contains both uppercase and lowercase letters. The uppercase
	 * letters are similar to those in the regular version of the same
	 * typeface family. The glyphs for the lowercase letters have the same
	 * shapes as the corresponding uppercase letters, but they are sized
	 * and their proportions adjusted so that they have the same size and
	 * stroke weight as lowercase glyphs in the same typeface family.</td>
	 * </tr>
	 * <tr>
	 * <td>19</td>
	 * <td>ForceBold</td>
	 * <td></td>
	 * </tr>
	 * </table>
	 * Bit Position    name    Meaning
	 */
	
	private static int[] AFMFlags = {
			35, //  0x100011   "Courier.afm",
			35, //  0x100011   "Courier-Bold.afm",
			99, //  0x1100011  "Courier-Oblique.afm",
			99, //  0x1100011  "Courier-BoldOblique.afm",
			//
			32, //  0x100000   "Helvetica.afm",
			32, //  0x100000   "Helvetica-Bold.afm",
			96, //  0x1100000  "Helvetica-Oblique.afm",
			96, //  0x1100000  "Helvetica-BoldOblique.afm",
			//
			34, //  0x100010   "Times-Roman.afm",
			34, //  0x100010   "Times-Bold.afm",
			98, //  0x1100010  "Times-Italic.afm",
			98, //  0x1100010  "Times-BoldItalic.afm",
			//
			4, //   0x100     "ZapfDingbats.afm",
			4  //   0x100     "Symbol.afm"
	};
	
	
	public static HashMap<String, AFM> AFMs = new HashMap<String, AFM>(14);
	public static HashMap<Integer, String> AGLFN = new HashMap<Integer, String>();
	public static HashMap<String, Integer> AGLUNI = new HashMap();

	public String fontName;
	public String familyName;
	public String fullName;
	public float[] widths = new float[256];
	public String[] names = new String[256];
	public Map<String,Integer> wxmap = new HashMap<String, Integer>();
	public Map<String,Integer> kmap = new HashMap<String, Integer>();
	public List<String> nlist = new Vector<String>();
	public int[] fontBBox = new int[4];
	public float italicAngle = 0;
	public float maxWidth = 0;
	public int avgWidth = 0;
	public int flags = 0;
	public int capHeight = 0;
	public int xHeight = 0;
	public int descender = 0;
	public int ascender = 0;
	/**
	 * Reader and parse all the core 14 AFM font descriptors
	 */
	static {
		try {
			for (int i = 0; i < AFMnames.length; i++) {
				AFM afm = new AFM("afm/" + AFMnames[i]);
				afm.setFlags(AFMFlags[i]);
				AFMs.put(afm.fontName.toLowerCase(), afm);
			//	System.err.println(afm.fontName.toLowerCase());
			}
			AFM.parseAglFN();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public byte[] panose;

	public byte[] getPanose() {
		return panose;
	}

	public void setPanose(byte[] panose) {
		this.panose = panose;
	}

	/**
	 * Creates a new AFM file based on the
	 *
	 * @param resource name of desired resource.
	 * @throws IOException if the specified resource could not be found or o
	 *                     pened.
	 */
	public AFM(String resource) throws IOException {
		this(ClasspathResourceLoader.of(resource, null));
	}

	public AFM(InputStream in) throws IOException {
		this();
		parse(new InputStreamReader(in));
	}
	
	public AFM(File resource) throws IOException {
		InputStream in = new FileInputStream(resource);
		if (in != null) {
			parse(new InputStreamReader(in));
		} else {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Could not find AFM File: " + resource);
			}
		}
		in.close();
	}
	
	public AFM(ResourceLoader _file) throws IOException
	{
		this(_file.getInputStream());
	}
	
	public AFM()
	{
	}
	
	public String getFontName() {
		return fontName;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public String getFamilyName() {
		return familyName;
	}
	
	public int[] getFontBBox() {
		return fontBBox;
	}
	
	public float getItalicAngle() {
		return italicAngle;
	}
	
	public int getWidth(String _n) {
		if(!this.wxmap.containsKey(_n)) return 0;
		return this.wxmap.get(_n);
	}
	
	public List<String> getNames()
	{
		return Collections.unmodifiableList(this.nlist);
	}

	public float[] getWidths() {
		return widths;
	}
	
	public float getMaxWidth() {
		return maxWidth;
	}
	
	public int getAvgWidth() {
		return avgWidth;
	}
	
	public int getFlags() {
		return flags;
	}
	
	private void setFlags(int value) {
		flags = value;
	}
	
	public Map<String, Integer> getKmap()
	{
		return kmap;
	}
	
	public void setKmap(Map<String, Integer> kmap)
	{
		this.kmap = kmap;
	}
	
	/**
	 * Utility class for parsing the contents of the care AFM files.
	 *
	 * @param i stream to read
	 * @throws IOException if the reader can not find the specified
	 *                             afm file.
	 */
	private void parse(Reader i) throws IOException {
		BufferedReader r = new BufferedReader(i);
		String s;
		int count = 0;
		while ((s = r.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(s, " ;\t\n\r\f");
			String s1 = st.nextToken();
			if (s1.equalsIgnoreCase("FontName")) {
				fontName = st.nextToken();
			} else if (s1.equalsIgnoreCase("FullName")) {
				fullName = st.nextToken();
			} else if (s1.equalsIgnoreCase("FamilyName")) {
				familyName = st.nextToken();
			} else if (s1.equalsIgnoreCase("FontBBox")) {
				fontBBox[0] = new Integer(st.nextToken());
				fontBBox[1] = new Integer(st.nextToken());
				fontBBox[2] = new Integer(st.nextToken());
				fontBBox[3] = new Integer(st.nextToken());
			} else if (s1.equalsIgnoreCase("ItalicAngle")) {
				italicAngle = new Float(st.nextToken());
			} else if (s1.equalsIgnoreCase("CapHeight")) {
				this.setCapHeight(CommonUtil.createInteger(st.nextToken()));
			} else if (s1.equalsIgnoreCase("XHeight")) {
				this.setxHeight(CommonUtil.createInteger(st.nextToken()));
			} else if (s1.equalsIgnoreCase("Descender")) {
				this.setDescender(CommonUtil.createInteger(st.nextToken()));
			} else if (s1.equalsIgnoreCase("Ascender")) {
				this.setAscender(CommonUtil.createInteger(st.nextToken()));
			} else if (s1.equalsIgnoreCase("CharacterSet")) {
				this.flags|= "Special".equalsIgnoreCase(st.nextToken()) ? 4 : 0;
			} else if (s1.equalsIgnoreCase("IsFixedPitch")) {
				this.flags|= "true".equalsIgnoreCase(st.nextToken()) ? 1 : 0;
			}
			// kerning
			else if (s1.equalsIgnoreCase("KPX")) {
				String _a = st.nextToken();
				String _b = st.nextToken();
				int _k = CommonUtil.createInteger(st.nextToken());
				this.kmap.put(_a+":"+_b, _k);
			}
			// font width data
			else if (s1.equalsIgnoreCase("C")) {
				int c = CommonUtil.createInteger(st.nextToken());
				while (!st.nextToken().equals("WX")) ;
				float wx = 0;
				String _token = st.nextToken();
				try
				{
					wx = CommonUtil.createInteger(_token) / 1000f;
				}
				catch(Exception _xe)
				{
					wx = CommonUtil.toFloat(_token) / 1000f;
				}
				while (!st.nextToken().equals("N")) ;
					String _n = st.nextToken();
				if (c >= 0 && c < 255) {
					widths[c] = wx ;
					names[c] = _n;
					// update max
				}
				if (wx > maxWidth) {
					maxWidth = wx*1000;
				}
				// update average
				avgWidth += wx*1000;
				count++;

				nlist.add(_n);
				wxmap.put(_n, (int)(wx*1000));
			}
		}
		// finalized average
		avgWidth = avgWidth / count;
	}
	
	public static void parseAglFN()
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(ClasspathResourceLoader.of("afm/aglfn.txt", null).getInputStream()));
		try
		{
			String _line;
			while((_line = in.readLine())!=null)
			{
				if(_line.trim().startsWith("#")) { continue; }
				String[] _parts = _line.split(";", 3);
				int _c = CommonUtil.createInteger("0x"+_parts[0]);
				AFM.AGLFN.put(_c, _parts[1]);
				AFM.AGLUNI.put(_parts[1], _c);
			}
		}
		catch(Exception _xe)
		{
			//IGNORE
		}
		finally
		{
			CommonUtil.close(in);
		}
		
		in = new BufferedReader(new InputStreamReader(ClasspathResourceLoader.of("afm/zapfdingbats.txt",null).getInputStream()));
		try
		{
			String _line;
			while((_line = in.readLine())!=null)
			{
				if(_line.trim().startsWith("#")) { continue; }
				String[] _parts = _line.split(";", 3);
				int _c = CommonUtil.createInteger("0x"+_parts[1]);
				AFM.AGLFN.put(_c, _parts[0]);
				AFM.AGLUNI.put(_parts[0], _c);
			}
		}
		catch(Exception _xe)
		{
			//IGNORE
		}
		finally
		{
			CommonUtil.close(in);
		}

		in = new BufferedReader(new InputStreamReader(ClasspathResourceLoader.of("afm/glyphlist.txt", null).getInputStream()));
		try
		{
			String _line;
			while((_line = in.readLine())!=null)
			{
				_line = _line.trim();
				if(_line.startsWith("#")
						|| _line.startsWith("%")
						|| _line.startsWith(";")) { continue; }
				String[] _parts = _line.split(";", 3);
				int _c = CommonUtil.createInteger("0x"+_parts[1]);
				AFM.AGLFN.put(_c, _parts[0]);
				AFM.AGLUNI.put(_parts[0], _c);
			}
		}
		catch(Exception _xe)
		{
			//IGNORE
		}
		finally
		{
			CommonUtil.close(in);
		}
	}
	
	public static int getUnicode(String _name)
	{
		if(AGLUNI.containsKey(_name))
		{
			return AGLUNI.get(_name);
		}
		return 0;
	}

	public static String getPostscriptForUnicode(int _u)
	{
		String _name = AGLFN.get(_u);
		if(_name == null)
		{
			_name = ".notdef";
		}
		return _name;
	}

	public int getFirstChar()
	{
		return 0;
	}
	
	public boolean isSymbol()
	{
		return (this.flags & 4)==4;
	}
	
	public String[] getGlyphNames(String _cs)
	{
		String[] _ret = new String[256];
		if((_cs != null) && ((this.flags & 4)==0))
		{
			_ret = getGlyphNamesBase(_cs);
		}
		else
		{
			for(int _c = 0; _c<256; _c++)
			{
				_ret[_c] = names[_c];
				if(_ret[_c]==null) { _ret[_c] = ".notdef"; }
			}
		}
		return _ret;
	}
	
	public static String[] getGlyphNamesBase(Character[] _uni)
	{
		String[] _ret = new String[_uni.length];
		for(int _c = 0; _c<_uni.length; _c++)
		{
			if(_uni[_c]!=null)
			{
				char _l = _uni[_c].charValue();
				_ret[_c] = AFM.AGLFN.get((int)_l);
				if(_ret[_c]==null)
				{
					_ret[_c]=String.format("uni%04X", (int)_uni[_c].charValue());
				}
			}
			else
			{
				_ret[_c] = ".notdef";
			}
		}
		return _ret;
	}
	
	public static String[] getGlyphNamesBase(String _cs)
	{
		Character[] _uni = getUnicodeBase(_cs);
		return getGlyphNamesBase(_uni);
	}
	public static Character[] getUnicodeBase(String _cs)
	{
		Character[] _ret = loadCharset(_cs);
		for(int _c = 0; _c<256; _c++)
		{
			if(_ret[_c]==null) { _ret[_c] = Character.MAX_VALUE; }
		}
		return _ret;
	}

	static Properties _CHARSET_ALIASES = new Properties();
	static Map<String,Character[]> _CHARSET_CACHE = new HashMap<>();
	public static final Character[] loadCharset(String _cs)
	{
		if(_CHARSET_ALIASES.size()==0)
		{
			try(InputStream _is = ClasspathResourceLoader.of("cmaps/aliases.properties", null).getInputStream())
			{
				_CHARSET_ALIASES.load(_is);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		_cs = (String) _CHARSET_ALIASES.getOrDefault(_cs.toLowerCase(), _cs.toLowerCase());

		if(_CHARSET_CACHE.containsKey(_cs))
		{
			return _CHARSET_CACHE.get(_cs);
		}

		Character[] _ret = new Character[256];

		for(int _c = 0; _c<_ret.length; _c++)
		{
			_ret[_c] = Character.valueOf((char) _c);
		}

		if(_cs.startsWith("bmp"))
		{
			int _bmp = CommonUtil.createInteger(_cs.substring(3));

			for(int _c = 0; _c<_ret.length; _c++)
			{
				int _u = (_bmp<<8)|_c;
				_ret[_c] = Character.valueOf((char) _u);
				if(_ret[_c]==null) { _ret[_c] = Character.MAX_VALUE; }
			}
		}
		else
		{
			ClasspathResourceLoader _rl = ClasspathResourceLoader.of("cmaps/" + _cs + ".map", null);
			InputStream _is = _rl.getInputStream();
			if(_is!=null)
			{
				DataInputStream _fh = new DataInputStream(_is);
				try
				{
					int code;
					while((code = _fh.readUnsignedShort())>=0)
					{
						_ret[code&0xff] = (char) _fh.readUnsignedShort();
					}
					CommonUtil.close(_fh);
				}
				catch (EOFException _xe)
				{
					CommonUtil.close(_fh);
				}
				catch (Exception _xe)
				{
					throw new IllegalArgumentException(_xe);
				}
			}

			_rl = ClasspathResourceLoader.of("cmaps/" + _cs + ".txt", null);
			_is = _rl.getInputStream();
			if(_is!=null)
			{
				BufferedReader _fh = new BufferedReader(new InputStreamReader(_is), 8192);
				String _line;
				try
				{
					int code, uni;
					while((_line = _fh.readLine())!=null)
					{
						_line = _line.trim();
						if(_line.startsWith("#")
								|| _line.startsWith("%")
								|| _line.startsWith(";"))
						{
							continue;
						}
						String[] _parts = CommonUtil.split(_line, " ");
						code = CommonUtil.createInteger(_parts[0].trim());
						uni = CommonUtil.createInteger(_parts[1].trim());
						_ret[code&0xff] = (char) uni;
					}
					CommonUtil.close(_fh);
				}
				catch (EOFException _xe)
				{
					CommonUtil.close(_fh);
				}
				catch (Exception _xe)
				{
					throw new IllegalArgumentException(_xe);
				}
			}

			_rl = ClasspathResourceLoader.of("cmaps/" + _cs + ".enc", null);
			_is = _rl.getInputStream();
			if(_is!=null)
			{
				BufferedReader _fh = new BufferedReader(new InputStreamReader(_is), 8192);
				String _line;
				try
				{
					int code; String glyph;
					while((_line = _fh.readLine())!=null)
					{
						_line = _line.trim();
						if(_line.startsWith("#")
								|| _line.startsWith("%")
								|| _line.startsWith(";"))
						{
							continue;
						}
						String[] _parts = CommonUtil.split(_line, " ");
						code = CommonUtil.createInteger(_parts[0].trim());
						glyph = _parts[1].trim();
						_ret[code&0xff] = (char) AFM.getUnicode(glyph);
					}
					CommonUtil.close(_fh);
				}
				catch (EOFException _xe)
				{
					CommonUtil.close(_fh);
				}
				catch (Exception _xe)
				{
					throw new IllegalArgumentException(_xe);
				}
			}
		}
		_CHARSET_CACHE.put(_cs, _ret);
		return _ret;
	}

	public int[] getWidths(String _cs)
	{
		int[] _ret = new int[256];
		String[] _map = getGlyphNames(_cs);
		for(int _c = 0; _c<256; _c++)
		{
			Integer _wx = this.wxmap.get(_map[_c]);
			if(_wx==null) { _wx = 300; }
			_ret[_c] = _wx;
		}
		return _ret;
	}
	
	public boolean isFixed()
	{
		return ((this.flags & 1) == 1);
	}

	public int getCapHeight() {
		return capHeight;
	}

	public void setCapHeight(int capHeight) {
		this.capHeight = capHeight;
	}

	public int getxHeight() {
		return xHeight;
	}

	public void setxHeight(int xHeight) {
		this.xHeight = xHeight;
	}

	public int getDescender() {
		return descender;
	}

	public void setDescender(int descender) {
		this.descender = descender;
	}

	public int getAscender() {
		return ascender;
	}

	public void setAscender(int ascender) {
		this.ascender = ascender;
	}

	public static final <T,S> Map<T,S> toMap(Class<T> _t, Class<S> _s, Object... _values)
	{
		Map<T,S> _ret = new HashMap<>();
		for(int _i=1; _i<_values.length; _i+=2)
		{
			_ret.put((T)_values[_i-1], (S)_values[_i]);
		}
		return _ret;
	}
}