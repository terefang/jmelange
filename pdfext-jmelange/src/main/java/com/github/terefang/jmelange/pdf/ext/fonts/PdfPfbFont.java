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
package com.github.terefang.jmelange.pdf.ext.fonts;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.commons.loader.*;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.fonts.PdfAfmFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontDescriptor;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontFileStream;
import com.github.terefang.jmelange.pdf.core.util.AFM;
import com.github.terefang.jmelange.pdf.ext.util.AFMX;
import org.apache.fontbox.pfb.PfbParser;
import org.apache.fontbox.type1.Type1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfPfbFont extends PdfAfmFont
{
	PdfFontFileStream _fs;
	PdfFontDescriptor _des;
	public PdfPfbFont(PdfDocument doc, AFM _afm, String _cs, ResourceLoader _pfb)
	{
		super(doc, _afm, _cs, _afm.getFontName(), _afm.getFirstChar(), _afm.getGlyphNames(_cs), _afm.getWidths(_cs));
		
		this.makeDescriptor(doc, _pfb, _afm);
	}
	
	private void makeDescriptor(PdfDocument doc, ResourceLoader _pfb, AFM _afm)
	{
		_des = PdfFontDescriptor.create(doc);

		_fs = PdfFontFileStream.create(doc);
		_des.setFontFile(_fs);
		
		ByteArrayOutputStream _baos = new ByteArrayOutputStream();
		try
		{
			CommonUtil.copy(_pfb.getInputStream(), _baos);
		}
		catch(Exception _xe)
		{
			_xe.printStackTrace();
		}
		try
		{
			Type1Font _type1 = Type1Font.createWithPFB(_baos.toByteArray());
			PfbParser _parser = new PfbParser(_baos.toByteArray());
			_fs.setLength1(_parser.getLengths()[0]);
			_fs.setLength2(_parser.getLengths()[1]);
			_fs.setLength3(_parser.getLengths()[2]);
			_fs.putStream(_parser.getInputStream());

			_des.setFontName(_afm.getFontName());
			_des.setFontFamily(_afm.getFamilyName());

			boolean isSymbolic = _afm.isSymbol();
			int flags = 0;
			flags |= isSymbolic ? PDF.FD_FLAG_SYMBOLIC : 0;
			flags |= !isSymbolic ? PDF.FD_FLAG_NON_SYMBOLIC : 0;
			_des.setFlags(flags);

			_des.setFontBBox(_afm.getFontBBox());
			_des.setItalicAngle(_afm.getItalicAngle());
			_des.setAscent(_afm.getAscender());
			_des.setDescent(_afm.getDescender());
			_des.setCapHeight(_afm.getCapHeight());
			_des.setXHeight(_afm.getxHeight());
			_des.setAverageWidth(_afm.getAvgWidth());
			_des.setMissingWidth(_afm.getAvgWidth());
			_des.setMaxWidth((int) _afm.getMaxWidth());
			_des.setCharSet("/"+ CommonUtil.join(_afm.getNames().iterator(), " /"));

			_des.setStemV(0);
		}
		catch(Exception _xe)
		{
			_xe.printStackTrace();
		}
		this.setFontDescriptor(_des);
	}
	
	
	public static PdfFont of(PdfDocument doc, AFM _afm, String _cs, ResourceLoader _pfb)
	{
		if(_afm==null)
		{
			_afm = AFMX.fromPFB(_pfb.getInputStream(), _cs);
		}
		return new PdfPfbFont(doc, _afm, _afm.isSymbol() ? null : _cs, _pfb);
	}
	
	@Override
	public void streamOut(boolean _res) throws IOException
	{
		if(_res)
		{
			this._fs.streamOut();
			this._des.streamOut();
		}
		super.streamOut(_res);
	}
}
