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


import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.util.AFM;
import com.github.terefang.jmelange.pdf.core.util.SfontlyHelper;
import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.Tag;
import com.google.typography.font.sfntly.table.core.CMap;
import com.google.typography.font.sfntly.table.core.CMapTable;
import com.google.typography.font.sfntly.table.core.MaximumProfileTable;

public class SfontlyTtfGlyphEncoder extends GlyphEncoder
{
	CMap cmap;
	int num;
	boolean privateUseArea = false;
	private Character[] _uni;

	public SfontlyTtfGlyphEncoder(Font _font, boolean _pua)
	{
		super();
		this.privateUseArea = _pua;

		MaximumProfileTable _maxp = (MaximumProfileTable)_font.getTable(Tag.maxp);
		this.num = _maxp.numGlyphs();

		this.cmap = SfontlyHelper.findCMap(_font,false);

		if(this.cmap == null) throw new IllegalArgumentException("no proper cmap");
	}

	public void addMapping(String _cs)
	{
		_uni = AFM.getUnicodeBase(_cs);
	}

	@Override
	public int encodeChar(Character _c)
	{
		if(this.privateUseArea && _c.charValue()<0xe000 && _c.charValue()>0x7f)
		{
			return _c.charValue()-0x80;
		}

		if(_uni!=null && _c.charValue()<_uni.length)
		{
			return this.cmap.glyphId(_uni[_c.charValue()]);
		}

		return this.cmap.glyphId(_c.charValue());
	}
	
	@Override
	public int getGlyphNum()
	{
		return -1;
	}
	
	@Override
	public int getGlyphId(int _c)
	{
		if(this.privateUseArea && _c<0xe000 && _c>0x7f)
		{
			return _c-0x80;
		}
		return this.cmap.glyphId(_c);
	}
	
	@Override
	public int getCode(int _g)
	{
		// TODO
		return _g;
	}
}
