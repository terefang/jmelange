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
package com.github.terefang.jmelange.pdf.ext.encoding;

import java.util.HashMap;
import java.util.Map;

public class IdentityAndMappedGlyphEncoder extends IdentityGlyphEncoder
{
	public static IdentityAndMappedGlyphEncoder getInstance()
	{
		return new IdentityAndMappedGlyphEncoder();
	}

	public IdentityAndMappedGlyphEncoder()
	{
		super();
	}

	Map<Character, Integer> mappedGlyphs = new HashMap<>();

	public void addMappedGlyph(Character c, Integer i)
	{
		this.mappedGlyphs.put(c,i);
	}

	@Override
	public int encodeChar(Character _c)
	{
		return this.mappedGlyphs.getOrDefault(_c, (int) _c.charValue());
	}
	
	@Override
	public int getGlyphNum()
	{
		return -1;
	}
	
	@Override
	public int getGlyphId(int _c)
	{
		return this.mappedGlyphs.getOrDefault(Character.valueOf((char) _c), _c);
	}
	
	@Override
	public int getCode(int _g)
	{
		return _g;
	}
}
