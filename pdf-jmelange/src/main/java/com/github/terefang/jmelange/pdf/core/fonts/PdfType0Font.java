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
import com.github.terefang.jmelange.pdf.core.encoding.GlyphEncoder;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.values.*;

import java.io.IOException;

public abstract class PdfType0Font extends PdfFont
{
	GlyphEncoder encoder;
	PdfDictObject descender;
	
	public PdfType0Font(PdfDocument doc, GlyphEncoder _enc)
	{
		super(doc, null, 0, null);
		this.encoder = _enc;
		this.setSubtype("Type0");
		this.set("Encoding", PdfName.of("Identity-H"));
		
		this.descender = PdfDictObject.create(doc);
		this.set("DescendantFonts", PdfArray.from(this.descender));
		
		this.descender.setType("Font");
		
		PdfDict _dict = PdfDict.create();
		this.descender.set("CIDSystemInfo", _dict);
		_dict.set("Registry", PdfString.of("Adobe"));
		_dict.set("Ordering", PdfString.of("Identity"));
		_dict.set("Supplement", PdfNum.of(0));

		if(this.encoder.getGlyphNum()>0)
		{
			this.descender.set("CIDToGIDMap", PdfName.of("Identity"));

			this.mapToUnicode(_enc);
		}
	}
	
	public GlyphEncoder getEncoder()
	{
		return encoder;
	}
	
	public void setEncoder(GlyphEncoder encoder)
	{
		this.encoder = encoder;
	}
	
	public PdfDictObject getDescender()
	{
		return descender;
	}
	
	public void setDescender(PdfDictObject descender)
	{
		this.descender = descender;
	}
	
	@Override
	public String encodeToString(String sequence, double wordSpace, double charSpace)
	{
		return this.encoder.encode(sequence, wordSpace, charSpace);
	}
	
	@Override
	public String encodeToStringKerned(String sequence, double wordSpace, double charSpace)
	{
		return this.encoder.encode(sequence, wordSpace, charSpace);
	}

	@Override
	public void streamOut(boolean _res) throws IOException
	{
		if(_res)
		{
			if(this.descender != null) this.descender.streamOut();
			if(this._touni != null) this._touni.streamOut();
		}
		super.streamOut(_res);
	}
}
