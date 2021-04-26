package core;/*
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

import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;

public class TestGrid
{
	public static void main(String[] args) throws Exception
	{
		PdfDocument doc = new PdfDocument();

		PdfFont _hf = doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);
		
		/* https://github.com/Bubblbu/paper-sizes
		Paper size	mm		inches			points
		A0		841 x 1189	33.1 x 46.8		2384 x 3370
		A1		594 x 841	23.4 x 33.1		1684 x 2384
		A2		420 x 594	16.5 x 23.4		1191 x 1684
		A3		297 x 420	11.7 x 16.5		842 x 1191
		A4		210 x 297	8.3 x 11.7		595 x 842
		A5		148 x 210	5.8 x 8.3		420 x 595
		A6		105 x 148	4.1 x 5.8		298 x 420
		A7		74 x 105	2.9 x 4.1		210 x 298
		A8		52 x 74		2 x 2.9			147 x 210
		A9		37 x 52		1.5 x 2			105 x 147
		*/
		float _ppw = 1191f;
		float _pph = 842f;
		float _pg = 36f;

		for(float _size : new float[]{ 0.5f, 1f, 2f, 3f, 4f, 5f, 6f })
		{
			PdfPage _page = _page = doc.newPage();
			_page.setMediabox(0, 0, (int)_ppw, (int)_pph);
			PdfContent _content = _page.newContent(false);
			drawSquareGrid(_content, _hf, 6f, _ppw, _pph, 30f, 2f, _pg*_size, 0.5f);
		}

		for(float _size : new float[]{ 1f, 2f, 3f, 4f, 5f, 8f, 10f})
		{
			PdfPage _page = _page = doc.newPage();
			_page.setMediabox(0, 0, (int)_ppw, (int)_pph);
			PdfContent _content = _page.newContent(false);
			drawHexGrid(_content, true, _hf, 6f, _ppw, _pph, 30f, 2f, _pg*_size, 0.5f, true);
		}
		doc.writeTo("./out/test-grid.pdf");
		System.exit(0);
	}

	static void drawSquareGrid(PdfContent _content, PdfFont _hf, float _fs, float _w, float _h, float _b, float _bw, float _g, float _gw)
	{
		_content.save();
		_content.linedash();

		_content.linewidth(_bw);
		_content.rectanglexy(_b, _b, _w - _b, _h - _b);
		_content.stroke();
		_content.restore();

		_content.save();
		_content.rectanglexy(_b, _b, _w - _b, _h - _b);
		_content.clip();
		_content.endpath();

		_content.linewidth(_gw);
		_content.linedashWithOffset(3, 6, 6);

		for(float i = _b; i < _w-_b; i += _g)
		{
			for(float j = _b; j < _h-_b; j += _g)
			{
				_content.moveTo(i, j);
				_content.lineTo(i, j+_g);
			}
		}
		for(float j = _b; j < _h-_b; j += _g)
		{
			for(float i = _b; i < _w-_b; i += _g)
			{
				_content.moveTo(i, j);
				_content.lineTo(i+_g, j);
			}
		}
		_content.stroke();
		_content.restore();
	}

	static void drawHexGrid(PdfContent _content, boolean _t, PdfFont _hf, float _fs, float _w, float _h, float _b, float _bw, float _g, float _gw, boolean _rot)
	{
		_content.save();
		_content.linedash();

		_content.linewidth(_bw);
		_content.rectanglexy(_b, _b, _w - _b, _h - _b);
		_content.stroke();
		_content.restore();

		_content.save();
		_content.rectanglexy(_b, _b, _w - _b, _h - _b);
		_content.clip();
		_content.endpath();

		_content.linewidth(_gw);
		_content.linedashWithOffset(3, 6, 6);

		float _lstd = _g;
		float _l30 = (float) Math.sin(30f*Math.PI/180f)*_lstd;
		float _l60 = (float) Math.sin(60f*Math.PI/180f)*_lstd;
		float _ti = (((int)(_w / _lstd))+3)*_lstd;
		float _tj = (((int)(_h / _lstd))+3)*_lstd;
		if(_rot)
		{
			for(float _i = 0; _i < _ti; _i += _lstd*3f)
			{
				for(float _j = 0f; _j < _tj; _j += _l60*2f)
				{
					_content.moveTo(_i, _j);
					_content.lineTo(_i+_lstd, _j);
					_content.lineTo(_i+_lstd+_l30, _j-_l60);
					_content.lineTo(_i+_lstd, _j-_l60-_l60);

					_content.moveTo(_i+_lstd+_l30, _j-_l60);
					_content.lineTo(_i+_lstd+_l30+_lstd, _j-_l60);
					_content.lineTo(_i+_lstd+_l30+_lstd+_l30, _j-_l60-_l60);
					_content.lineTo(_i+_lstd+_l30+_lstd, _j-_l60-_l60-_l60);
				}
			}
		}
		else
		{
			for(float _i = 0; _i < _ti; _i += _l60*2f)
			{
				for(float _j = 0f; _j < _tj; _j += _lstd*3f)
				{
					_content.moveTo(_i, _j);
					_content.lineTo(_i, _j+_lstd);
					_content.lineTo(_i-_l60, _j+_lstd+_l30);
					_content.lineTo(_i-_l60-_l60, _j+_lstd);

					_content.moveTo(_i-_l60, _j+_lstd+_l30);
					_content.lineTo(_i-_l60, _j+_lstd+_l30+_lstd);
					_content.lineTo(_i-_l60-_l60, _j+_lstd+_l30+_lstd+_l30);
					_content.lineTo(_i-_l60-_l60-_l60, _j+_lstd+_l30+_lstd);
				}
			}
		}
		_content.stroke();
		_content.restore();

		if(_t)
		{
			if(_rot)
			{
				for(int _k = 1; _k < (((_w-(_b*2f))/(_l60*2f))+1); _k += 2)
				{
					for(int _l = 1; _l < ((_h-(_b*2))/(_l60*2)); _l += 1)
					{
						_content.save();
						_content.startText();
						_content.setFont(_hf, _fs);
						_content.font(_hf, _fs);
						_content.moveText(_l30+_lstd+(((_k-1)*3)*_l30), _b+(((_l*2)-1)*_l60));
						_content.text(String.format("%02d.%02d", _k,_l));

						_content.moveText(_l30+_lstd, _l60);
						_content.text(String.format("%02d.%02d", _k+1,_l));

						_content.restore();
					}
				}
			}
			else
			{
				for(int _k = 1; _k < ((_w-(_b*2))/(_l60*2)); _k += 1)
				{
					for(int _l = 1; _l < ((_h-(_b*2))/(_lstd*1.5f)); _l += 2)
					{
						_content.save();
						_content.startText();
						_content.setFont(_hf, _fs);
						_content.font(_hf, _fs);
						_content.moveText(((_k*2)*_l60)-_l30, _lstd+_lstd+(((_l-1)*1.5f)*_lstd));
						_content.text(String.format("%02d.%02d", _l,_k));

						_content.moveText(_l60,_l30+_lstd);
						_content.text(String.format("%02d.%02d", _l+1,_k));

						_content.restore();
					}
				}
			}
		}
	}
}
