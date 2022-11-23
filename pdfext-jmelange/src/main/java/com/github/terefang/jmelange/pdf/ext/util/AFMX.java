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
package com.github.terefang.jmelange.pdf.ext.util;

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

import com.github.terefang.jmelange.fonts.AFM;
import org.apache.fontbox.encoding.BuiltInEncoding;
import org.apache.fontbox.ttf.*;
import org.apache.fontbox.type1.Type1Font;
import org.apache.fontbox.util.BoundingBox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;


/**
 * This class parses and stores data on the 14 PostScript AFM files.
 *
 * @since 1.0
 */
public class AFMX extends AFM
{
	private static final Logger logger =
			Logger.getLogger(AFMX.class.toString());

	public static AFMX fromPFM(File pfm)
	{
		AFMX _afm = new AFMX();
		return _afm;
	}

	public static AFMX fromPFB(InputStream _pfb, String _cs)
	{
		AFMX _afm = new AFMX();
		try
		{
			Type1Font _t1 = Type1Font.createWithPFB(_pfb);
			_afm.fontName = _t1.getFontName();
			_afm.fullName = _t1.getFullName();
			_afm.familyName = _t1.getFamilyName();
			_afm.fontBBox[0] = (int) _t1.getFontBBox().getLowerLeftX();
			_afm.fontBBox[1] = (int) _t1.getFontBBox().getLowerLeftY();
			_afm.fontBBox[2] = (int) _t1.getFontBBox().getUpperRightX();
			_afm.fontBBox[3] = (int) _t1.getFontBBox().getUpperRightY();
			_afm.italicAngle = _t1.getItalicAngle();

			_afm.flags|= (_t1.getEncoding() instanceof BuiltInEncoding) ? 4 : 0;
			_afm.flags|= _t1.isFixedPitch() ? 1 : 0;
			int _count = 0;
			for(String _name : _t1.getCharStringsDict().keySet())
			{
				_afm.nlist.add(_name);

				int _w = (int) _t1.getWidth(_name);
				if(_w > _afm.maxWidth) _afm.maxWidth = _w;
				_afm.avgWidth += _w;
				_afm.wxmap.put(_name, _w);
				_count++;
			}
			_afm.avgWidth /= _count;
			if(_cs!=null)
			{
				String[] _names = AFMX.getGlyphNamesBase(_cs);
				for(int i=0; i<_names.length; i++)
				{
					_afm.names[i] = _names[i];
					_afm.widths[i] = _afm.getWidth(_names[i]);
				}
			}
			else
			{
				for(Map.Entry<Integer, String> _entry : _t1.getEncoding().getCodeToNameMap().entrySet())
				{
					_afm.names[_entry.getKey()] = _entry.getValue();
					_afm.widths[_entry.getKey()] = _afm.getWidth(_entry.getValue());
				}
			}
		}
		catch(Exception _xe)
		{
			_xe.printStackTrace();
		}
		return _afm;
	}

	public static AFMX fromOTF(OpenTypeFont _tf, String _cs)
	{
		AFMX _afm = new AFMX();
		try
		{
			int emUnit=_tf.getUnitsPerEm();

			String _name = _tf.getName();
			if(_name==null)
			{
				_name = _tf.getNaming().getFontFamily();
				if(_name!=null)
				{
					_name += " "+_tf.getNaming().getFontSubFamily();
				}
			}
			if(_name==null) _name = _tf.getNaming().getPostScriptName();
			if(_name==null) _name = _tf.getNaming().getNameRecords().get(0).getString();
			_name = _name.replaceAll("[^a-zA-Z0-9]", "-");

			_afm.fontName = _name;
			_afm.fullName = _tf.getNaming().getPostScriptName();
			_afm.familyName = _tf.getNaming().getFontFamily();
			if(_afm.fullName==null)
			{
				_afm.fullName = _name;
			}
			BoundingBox _bbx = _tf.getFontBBox();
			_afm.fontBBox[0] = (int) _bbx.getLowerLeftX();
			_afm.fontBBox[1] = (int) _bbx.getLowerLeftY();
			_afm.fontBBox[2] = (int) _bbx.getUpperRightX();
			_afm.fontBBox[3] = (int) _bbx.getUpperRightY();
			_afm.italicAngle = 0;

			if(_tf.getOS2Windows()!=null)
			{
				_afm.avgWidth = _tf.getOS2Windows().getAverageCharWidth()*1000/emUnit;
			}
			else
			{
				_afm.avgWidth = 300;
			}

			for(int _g=0; _g<_tf.getNumberOfGlyphs(); _g++)
			{
				String _n = _tf.getPostScript().getName(_g);
				_afm.nlist.add(_n);

				int _w = _tf.getAdvanceWidth(_g)*1000/emUnit;
				if(_w > _afm.maxWidth) _afm.maxWidth = _w;
				_afm.wxmap.put(_n, _w);
			}

			if(_tf.getCmap().getSubtable(3, 0)!=null)
			{
				CmapSubtable _cmap = _tf.getCmap().getSubtable(3, 0);
				for(int i=0; i<256; i++)
				{
					int _g = _cmap.getGlyphId(i+0xf000);
					if(_g==0)
						_g = _cmap.getGlyphId(i);
					_afm.names[i] = _tf.getPostScript().getName(_g);
					_afm.widths[i] = _tf.getAdvanceWidth(_g)*1000/emUnit;
				}
			}
			else
			if(_cs!=null)
			{
				String[] _names = AFMX.getGlyphNamesBase(_cs);
				for(int i=0; i<_names.length; i++)
				{
					_afm.names[i] = _names[i];
					_afm.widths[i] = _afm.getWidth(_names[i]);
				}
			}
			else
			{
				for(int i=0; i<_afm.names.length; i++)
				{
					int _g = _tf.getUnicodeCmapLookup().getGlyphId(i);
					String _n = _tf.getPostScript().getName(_g);

					if(_g==0 && "".equalsIgnoreCase(_n))
					{
						_g = _tf.getCmap().getCmaps()[0].getGlyphId(i);
						_n = _tf.getPostScript().getName(_g);
					}

					_afm.names[i] = _n;
					_afm.widths[i] = _afm.getWidth(_n);
				}
			}

			if(_tf.getKerning()!=null)
			{
				KerningSubtable _kern = _tf.getKerning().getHorizontalKerningSubtable();
				for(int i=0; i<_afm.names.length; i++)
				{
					for(int j=0; j<_afm.names.length; j++)
					{
						int _k = _kern.getKerning(i,j);
						if(_k!=0)
						{
							_afm.kmap.put(_afm.names[i]+":"+_afm.names[j], _k);
						}
					}
				}
			}

			if(_tf.getHorizontalHeader()!=null)
			{
				HorizontalHeaderTable _hhea = _tf.getHorizontalHeader();
				_afm.maxWidth=_hhea.getAdvanceWidthMax()*1000/emUnit;
				_afm.ascender=_hhea.getAscender()*1000/emUnit;
				_afm.descender=_hhea.getDescender()*1000/emUnit;
			}

			if(_tf.getOS2Windows()!=null)
			{
				OS2WindowsMetricsTable _os2 = _tf.getOS2Windows();
				_afm.capHeight=_os2.getCapHeight()*1000/emUnit;
				_afm.avgWidth=_os2.getAverageCharWidth()*1000/emUnit;
				_afm.xHeight=_os2.getPanose()[9];
				_afm.panose = _os2.getPanose();
			}
		}
		catch(Exception _xe)
		{
			_xe.printStackTrace();
		}
		return _afm;

	}
	public static AFMX fromOTF(InputStream _otf, String _cs)
	{
		try
		{
			return AFMX.fromOTF(new OTFParser().parse(_otf), _cs);
		}
		catch (IOException e)
		{
			return null;
		}
	}
}