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
package com.github.terefang.jmelange.pdf.core.encoding;

public class IdentityGlyphEncoder extends GlyphEncoder
{
	public static IdentityGlyphEncoder getInstance()
	{
		return new IdentityGlyphEncoder();
	}
	
	public IdentityGlyphEncoder()
	{
		super();
	}
	
	@Override
	public int encodeChar(Character _c)
	{
		return _c.charValue();
	}
	
	@Override
	public int getGlyphNum()
	{
		return -1;
	}
	
	@Override
	public int getGlyphId(int _c)
	{
		return _c;
	}
	
	@Override
	public int getCode(int _g)
	{
		return _g;
	}
}
