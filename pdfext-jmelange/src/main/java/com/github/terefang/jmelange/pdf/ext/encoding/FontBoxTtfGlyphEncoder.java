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
package com.github.terefang.jmelange.pdf.ext.encoding;


import com.github.terefang.jmelange.pdf.core.encoding.GlyphEncoder;

import lombok.SneakyThrows;
import org.apache.fontbox.ttf.CmapLookup;
import org.apache.fontbox.ttf.OpenTypeFont;

import java.util.List;

public class FontBoxTtfGlyphEncoder extends GlyphEncoder
{
	CmapLookup cmap;
	int num;
	boolean privateUseArea = false;

	@SneakyThrows
	public FontBoxTtfGlyphEncoder(OpenTypeFont _font, int _num, boolean _pua)
	{
		super();
		try
		{
			this.cmap = _font.getUnicodeCmapLookup();
		}
		catch(Exception _xe)
		{
			this.cmap = _font.getCmap().getCmaps()[0];
		}

		if(_font.getCmap().getSubtable(3, 0)!=null)
		{
			this.cmap = _font.getCmap().getSubtable(3, 0);
		}

		this.num = _num;
		this.privateUseArea = _pua;
	}
	
	@Override
	public int encodeChar(Character _c)
	{
		if(this.privateUseArea && _c.charValue()<0xe000 && _c.charValue()>0x7f)
		{
			return _c.charValue()-0x80;
		}
		return this.cmap.getGlyphId(_c.charValue());
	}
	
	@Override
	public int getGlyphNum()
	{
		return this.num;
	}
	
	@Override
	public int getGlyphId(int _c)
	{
		if(this.privateUseArea && _c<0xe000 && _c>0x7f)
		{
			return _c-0x80;
		}
		return this.cmap.getGlyphId(_c);
	}
	
	@Override
	public int getCode(int _g)
	{
		List<Integer> _code = this.cmap.getCharCodes(_g);
		return _code==null ? 0 : _code.get(0);
	}
}
