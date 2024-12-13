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

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractEncoder implements Encoder
{
	String name = "unnamed";
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String _name)
	{
		name = _name;
	}
	
	int mask = 0xff;
	boolean needWordSpaceAdjust = false;
	
	public boolean isNeedWordSpaceAdjust()
	{
		return needWordSpaceAdjust;
	}
	
	public void setNeedWordSpaceAdjust(boolean needWordSpaceAdjust)
	{
		this.needWordSpaceAdjust = needWordSpaceAdjust;
	}
	
	public int getMask()
	{
		return mask;
	}

	String format = "%02x";

	public String getFormat()
	{
		return format;
	}
	
	public void setFormat(String format)
	{
		this.format = format;
	}
	
	public void setMask(int mask)
	{
		this.mask = mask;
	}
	
	Map<Character,Integer> glyphMap = new HashMap<>();
	Map<Character,String> glyphNames = new HashMap<>();
	
	public Map<Character, Integer> getGlyphMap()
	{
		return glyphMap;
	}
	
	public void setGlyphMap(Map<Character, Integer> glyphMap)
	{
		this.glyphMap = glyphMap;
	}
	
	public Map<Character, String> getGlyphNames()
	{
		return glyphNames;
	}
	
	public void setGlyphNames(Map<Character, String> glyphNames)
	{
		this.glyphNames = glyphNames;
	}
	
	@Override
	public int encodeChar(Character _c)
	{
		if(this.glyphMap.containsKey(_c))
		{
			return (this.glyphMap.get(_c) & this.mask);
		}
		return 0;
	}
	
	@Override
	public String nameChar(Character _c)
	{
		if(this.glyphNames.containsKey(_c))
		{
			return this.glyphNames.get(_c);
		}
		return ".notdef";
	}
	
	@Override
	public String encodeSingle(Character _c, double wordSpace, double charSpace)
	{
		StringBuilder _ret = new StringBuilder();
		int _i = this.encodeChar(_c);
		_ret.append(String.format("<"+this.format+">", _i));
		if(isNeedWordSpaceAdjust())
		{
			if(Character.isSpaceChar(_c) && (wordSpace>0d))
			{
				_ret.append(" " + PDF.transformDP(-wordSpace) + " ");
			}
		}
		return _ret.toString();
	}
	
	@Override
	public String encode(String _c, double wordSpace, double charSpace)
	{
		StringBuilder _ret = new StringBuilder();
		for(int i=0; i<_c.length(); i++)
		{
			_ret.append(encodeSingle(Character.valueOf(_c.charAt(i)), wordSpace, charSpace));
		}
		return _ret.toString();
	}
}
