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

import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.values.*;

public class PdfEncoding extends PdfDictObject
{
	Encoder encoder;
	
	boolean issymbol = false;
	int firstChar = 0;
	String[] glyphNames = {};
	
	public PdfEncoding(PdfDocument doc, Encoder encoder)
	{
		super(doc);
		this.encoder = encoder;
		this.set("Type", PdfName.of("Encoding"));
		this.set("BaseEncoding", PdfName.of("WinAnsiEncoding"));
	}
	
	public static PdfEncoding of(PdfDocument doc, Encoder encoder)
	{
		return new PdfEncoding(doc, encoder);
	}

	public static PdfEncoding of(PdfDocument doc, String _cs)
	{
		PdfEncoding _enc;
		if(_cs != null)
		{
			_enc = new PdfEncoding(doc, ByteEncoder.from(_cs));
		}
		else
		{
			_enc = new PdfEncoding(doc, null);
		}
		return _enc;
	}

	public static PdfEncoding of(PdfDocument doc, String _cs, int _fc, String... _g)
	{
		PdfEncoding _enc = of(doc, _cs);
		_enc.firstChar = _fc;
		_enc.glyphNames = _g;
		return _enc;
	}

	public static PdfEncoding of(PdfDocument doc, int _fc, String... _g)
	{
		PdfEncoding _enc = new PdfEncoding(doc, ByteEncoder.from(true, _fc, _g));
		_enc.issymbol=true;
		_enc.firstChar = _fc;
		_enc.glyphNames = _g;
		return _enc;
	}
	
	@Override
	public String asString()
	{
		PdfArray _encoding = PdfArray.create();
		if(this.issymbol==true || this.glyphNames!=null)
		{
			_encoding.add(PdfNum.of(this.firstChar));
			for(String _g : this.glyphNames)
			{
				_encoding.add(PdfName.of(_g));
			}
		}
		else
		{
			for(int i = this.firstChar; i<256; i++)
			{
				_encoding.add(PdfName.of(this.encoder.nameChar(i)));
			}
		}
		this.set("Differences", _encoding);
		return super.asString();
	}
	
	public Encoder getEncoder()
	{
		return encoder;
	}
	
	public void setEncoder(Encoder encoder)
	{
		this.encoder = encoder;
	}
	
	public String encode(String sequence, double wordSpace, double charSpace)
	{
		return this.getEncoder().encode(sequence, wordSpace, charSpace);
	}

	public String encode(Integer _c, double wordSpace, double charSpace)
	{
		return this.getEncoder().encodeSingle(_c, wordSpace, charSpace);
	}

	public static void main(String[] args) {
		Encoder _enc = ByteEncoder.from("pdfdoc");
		for(int i = 0; i<256; i++)
		{
			System.out.println(_enc.nameChar(i));
		}
	}
}
