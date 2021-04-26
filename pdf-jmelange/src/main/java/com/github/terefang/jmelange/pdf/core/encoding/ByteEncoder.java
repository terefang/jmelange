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
package com.github.terefang.jmelange.pdf.core.encoding;

import com.github.terefang.jmelange.pdf.core.util.AFM;

public class ByteEncoder extends AbstractEncoder
{
	public ByteEncoder()
	{
		this.setFormat("%02x");
		this.setMask(0xff);
	}
	
	public static ByteEncoder bmp(int x)
	{
		ByteEncoder _enc = new ByteEncoder();
		for(int i=0;i<256; i++)
		{
			_enc.getGlyphMap().put(Character.valueOf((char) (i+(256*x))), i);
			_enc.getGlyphNames().put(Character.valueOf((char) (i+(256*x))), AglFn.glyphName(AglFn.AGLFN, (i+(256*x)), true));
		}
		return _enc;
	}
	
	public static Encoder from(String _cs)
	{
		Character[] _uni = AFM.getUnicodeBase(_cs);
		String[] _names = AFM.getGlyphNamesBase(_cs);
		ByteEncoder _enc = new ByteEncoder();
		for(int i=0;i<256; i++) {
			_enc.getGlyphMap().put(Character.valueOf((char) i), (i));
			_enc.getGlyphNames().put(Character.valueOf((char) i), _names[i]);
		}
		for(int i=0;i<256; i++)
		{
			_enc.getGlyphMap().put(Character.valueOf( _uni[i]), (i));
			_enc.getGlyphNames().put(Character.valueOf( _uni[i]), _names[i]);
		}
		return _enc;
	}
	
	public static Encoder from(boolean _issymbol, int _first, String... _glyphs)
	{
		ByteEncoder _enc = new ByteEncoder();
		for(int i=0;i<_glyphs.length; i++)
		{
			if(_issymbol)
			{
				_enc.getGlyphNames().put(Character.valueOf((char) (i+_first)), _glyphs[i]);
				_enc.getGlyphMap().put(Character.valueOf((char) (i+_first)), i+_first);
			}
			int _code = AglFn.glyphCode(AglFn.UNIFN, _glyphs[i], true);
			_enc.getGlyphNames().put(Character.valueOf((char) _code), _glyphs[i]);
			_enc.getGlyphMap().put(Character.valueOf((char) _code), i+_first);
		}
		return _enc;
	}
	
	public static Encoder from(int _first, int[] _glyphs, String[] _names)
	{
		ByteEncoder _enc = new ByteEncoder();
		for(int i=0;i<_glyphs.length; i++)
		{
			int _code = _glyphs[i];
			String _name = _names==null ? AglFn.glyphName(AglFn.AGLFN, _code, true) : _names[i];

			_enc.getGlyphNames().put(Character.valueOf((char) (i+_first)), _name);
			_enc.getGlyphMap().put(Character.valueOf((char) (i+_first)), i+_first);

			_enc.getGlyphNames().put(Character.valueOf((char) _code), _name);
			_enc.getGlyphMap().put(Character.valueOf((char) _code), i+_first);
		}
		return _enc;
	}
}
