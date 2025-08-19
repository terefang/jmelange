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
package com.github.terefang.jmelange.pdf.core.fonts;

import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.fonts.AFM;

public abstract class PdfType3Font extends PdfBaseFont
{

	public PdfType3Font(PdfDocument doc, String _cs, String _name, int _first, String[] _glyphs, int[] _widths)
	{
		super(doc, _cs, _name, _first, _glyphs, _widths, false, false, false);
		this.setSubtype("Type3");
		this.setPsName(_name.replaceAll("\\s+", ""));
		if(_cs!=null) this.mapToUnicode(_cs);
	}

	@Override
	public char glyphToChar(String _name)
	{
		for(int _i = 0; _i<this.glyphs.length; _i++)
		{
			if(_name.equals(this.glyphs[_i]))
			{
				return (char) AFM.getUnicode(_name);
			}
		}
		return '?';
	}
}
